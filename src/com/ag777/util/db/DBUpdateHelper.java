package com.ag777.util.db;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.regex.Pattern;

import com.ag777.util.db.model.VersionSqlPojo;
import com.ag777.util.db.model.VersionSqlPojo.DdlListBean;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.exception.ExceptionHelper;
import com.ag777.util.lang.interf.Disposable;

/**
 * 数据库版本升级辅助类
 * <p>
 * 		直接引入项目可以作为数据库版本控制模块使用,
 * 		支持多级版本号x.xx.xx, xx.xxx等
 * </p>
 * 
 * @author ag777
 * @version create on 2017年09月06日,last modify at 2020年03月23日
 */
public abstract class DBUpdateHelper implements Disposable {

	private boolean mode_debug = false;	//独立的debug模式，控制这块的输出
	public void debugMode(boolean isDebugMode) {
		mode_debug = isDebugMode;
	}
	
	private static Pattern p_classPath = Pattern.compile("^([\\w\\d_]+\\.)+[\\w\\d_]+$");
	
	private List<VersionSqlPojo> versionSqlPojoList;	//版本号及对应sql列表
	
	public DBUpdateHelper(List<VersionSqlPojo> versionSqlPojoList) {
		this.versionSqlPojoList = versionSqlPojoList;
	}
	
	/**
	 * 根据版本号和对应的sql列表升级数据库 
	 * @param versionCodeOld 当前版本号(支持多级，如33或1.25.345)
	 * @param conn				数据库连接
	 * @throws SQLException	主要抛出sql执行异常,其他异常也包装成SQLException,通过getMessage()方法获取错误信息
	 */
	public void update(String versionCodeOld, Connection conn) throws SQLException {
		
		for (int i = 0; i < versionSqlPojoList.size(); i++) {
			VersionSqlPojo verionSql = versionSqlPojoList.get(i);
			String versionCodeNew = verionSql.getCode();
			if(isBefore(versionCodeOld, versionCodeNew)) {
				Console.log(
						new StringBuilder()
							.append("数据库版本")
							.append(versionCodeOld)
							.append("->")
							.append(versionCodeNew)
							.toString());
				List<DdlListBean> ddlList = verionSql.getDdlList();
				List<String> dmlList = verionSql.getDmlList();
				
				additionalSql(i, versionCodeNew, dmlList);
				
				try {
					executeDdlList(ddlList, conn, versionCodeNew);
					executeDmlList(dmlList, conn, versionCodeNew);	//这里面带上了数据库版本号的更新
					versionCodeOld = versionCodeNew;
				} catch(SQLException ex) {
					String errMsg = new StringBuilder()
										.append("升级版本")
										.append(versionCodeNew)
										.append("失败:")
										.append('[')
										.append(ex.getMessage())
										.append(']')
										.toString();
					throw new SQLException(errMsg);
				}
			}
		}
		
		
	}
	
	/**
	 * 需要提供升级数据库版本的sql,在版本升级sql都执行完后将版本写进数据库（业务默认数据库版本独立放在数据库里，可以简单改造该类，改为其他方式存储,以现有方式升级数据库版本操作会融入事务）
	 * @param versionCodeNew 将要变成的版本号
	 * @param isFirstVerion 	是否为第一个版本(有可能第一个版本数据库里还没存放版本号,视情况使用)
	 * @return 返回null则什么都不执行
	 */
	public abstract String dbVersionUpdateSql(String versionCodeNew, boolean isFirstVerion);
	
	/**
	 * 补充每个版本的sql
	 * @param index	版本号角标
	 * @param versionCodeOld
	 * @param versionCodeNew
	 * @param dmlList	dml语句列表
	 */
	private void additionalSql(int index, String versionCodeNew, List<String> dmlList) {
		String sql = dbVersionUpdateSql(versionCodeNew, index==0);
		if(sql != null) {
			dmlList.add(sql);
		}
	}
	
	/**
	 * 如果当前sql是执行方法获得，执行方法取得sql
	 * @param src
	 * @param conn
	 * @param stmt
	 * @param versionCodeNew
	 * @return
	 * @throws SQLException
	 */
	private static String toSql(String src, Connection conn, Statement stmt, String versionCodeNew) throws SQLException {
		if(src.startsWith("[method]")) {
			src = src.replace("[method]","");	//先去除标识
			if(!p_classPath.matcher(src).matches()) {
				throw new SQLException("数据库升级异常:方法路径配置不正确:["+src+"]请正确配置获取sql的方法(格式为类路径.方法名,例:com.test.A.dosth)");
			}
			String classPath = null;
			String methodName = null;
			try {
				/*开始拆分字符串获取类路径及方法名*/
				int lastIndexOfDot = src.lastIndexOf('.');
				methodName = src.substring(lastIndexOfDot+1, src.length());
				classPath = src.substring(0, lastIndexOfDot);
				/*根据类路径和方法名执行方法*/
				Class<?> clazz = Class.forName(classPath);
				Method mothod = clazz.getDeclaredMethod(methodName, Connection.class, Statement.class, String.class);
				Object sql = mothod.invoke(null, conn, stmt, versionCodeNew);
				if(sql != null) {
					return sql.toString();
				} else {
					return null;
				}
			} catch (ClassNotFoundException|NoClassDefFoundError e) {
				throw new SQLException("数据库升级异常:未找到类["+classPath+"]", e);
			} catch (NoSuchMethodException e) {
				throw new SQLException("数据库升级异常:未找到方法["+src+"]", e);
			} catch (SecurityException e) {
				throw new SQLException("数据库升级异常:无权执行方法["+src+"]", e);
			} catch (IllegalAccessException e) {
				throw new SQLException("数据库升级异常:执行方法获取sql失败["+src+"]", e);
			} catch (IllegalArgumentException e) {
				throw new SQLException("数据库升级异常:执行方法获取sql失败["+src+"]", e);
			} catch (InvocationTargetException e) {
				//方法本身抛出的异常
				e.printStackTrace();
//				System.out.println(e.getCause().getClass().getName());	//真正的抛出的异常
				throw new SQLException("数据库升级异常:执行方法["+src+"]抛出异常:"+ExceptionHelper.getErrMsg(e, "", ListUtils.of("java")), e);
			} catch(Exception ex) {
				ex.printStackTrace();
				throw new SQLException("数据库升级异常:发生未知异常:"+ExceptionHelper.getErrMsg(ex, "", ListUtils.of("java")), ex);
			}
		}
		return src;
	}
	
	/**
	 * 执行ddl语句
	 * @param ddlList
	 * @param conn
	 * @param versionCodeNew
	 * @throws SQLException
	 */
	private void executeDdlList(List<DdlListBean> ddlList, Connection conn, String versionCodeNew) throws SQLException {
		conn.setAutoCommit(true);
		Statement stmt = conn.createStatement();
		for (DdlListBean ddl : ddlList) {
			String sql = toSql(ddl.getSql(), conn, stmt, versionCodeNew);
			if(sql == null) {
				continue;
			}
			try {
				log("执行ddl:"+ddl.getSql());
				stmt.executeUpdate(ddl.getSql());
			} catch(SQLException ex) {
				
				if(ddl.getRollback() != null && !ddl.getRollback().isEmpty()) {	//执行回滚语句
					stmt.execute(ddl.getRollback());
				}
				if(ddl.getIsForce() != null && ddl.getIsForce()) {
					throw new SQLException(getErrMsg(ddl.getSql(), ex));
				}
			}
		}
	}
	
	/**
	 * 执行dml语句(事务)
	 * @param dmlList
	 * @param conn
	 * @param versionCodeNew
	 * @throws SQLException
	 */
	private void executeDmlList(List<String> dmlList, Connection conn, String versionCodeNew) throws SQLException {
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			for (String sql : dmlList) {
				sql = toSql(sql, conn, stmt, versionCodeNew);
				if(sql == null) {
					continue;
				}
				try {
					log("执行dml:"+sql);
					stmt.executeUpdate(sql);
				} catch(SQLException ex) {
					throw new SQLException(getErrMsg(sql, ex));
				}
			}
			conn.commit();
		} catch(SQLException ex) {
			conn.rollback();
			throw ex;
		}
	}
	
	/**
	 * 判断旧版本号是否小于新版本号
	 * @param versionCodeOld
	 * @param versionCodeNew
	 * @return
	 */
	private static boolean isBefore(String versionCodeOld, String versionCodeNew) {
		return StringUtils.isVersionBefore(versionCodeOld, versionCodeNew);
	}
	
	/**
	 * 统一错误信息的格式
	 * @param sql
	 * @param ex
	 * @return
	 */
	private static String getErrMsg(String sql, SQLException ex) {
		return new StringBuilder()
			.append("执行sql失败:")
			.append(sql)
			.append(",原因:")
			.append(ex.getMessage())
			.toString();
	}
	
	/**
	 * 统一打印出口
	 * @param msg
	 */
	private void log(String msg) {
		if(mode_debug) {
			Console.log(msg);
		}
	}
	
	@Override
	public void dispose() {
		p_classPath = null;
		versionSqlPojoList = null;
	}
}
