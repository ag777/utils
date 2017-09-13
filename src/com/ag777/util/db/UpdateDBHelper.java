package com.ag777.util.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import com.ag777.util.db.model.VersionSqlPojo;
import com.ag777.util.db.model.VersionSqlPojo.DdlListBean;
import com.ag777.util.lang.Console;

/**
 * 数据库版本升级辅助类
 * @author ag777
 * Time: created at 2017/09/06. last modify at 2017/09/07.
 */
public abstract class UpdateDBHelper {

	private static boolean MODE_DEBUG = false;	//独立的debug模式，控制这块的输出
	public static void debugMode(boolean isDebugMode) {
		MODE_DEBUG = isDebugMode;
	}
	
	private List<VersionSqlPojo> VersionSqlPojoList;	//版本号及对应sql列表
	
	/**
	 * 执行更新数据库操作
	 * @throws SQLException
	 */
	private UpdateDBHelper(List<VersionSqlPojo> VersionSqlPojoList) throws SQLException{
		this.VersionSqlPojoList = VersionSqlPojoList;
	}
	
	/**
	 * 根据版本号和对应的sql列表升级数据库 
	 * @param versionCodeOld 当前版本号(支持多级，如33或1.25.345)
	 * @param conn				数据库连接
	 * @throws SQLException	主要抛出sql执行异常,其他异常也包装成SQLException,通过getMessage()方法获取错误信息
	 */
	public void update(String versionCodeOld, Connection conn) throws SQLException {
		
		for (int i = 0; i < VersionSqlPojoList.size(); i++) {
			VersionSqlPojo verionSql = VersionSqlPojoList.get(i);
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
					executeDdlList(ddlList, conn);
					executeDmlList(dmlList, conn);	//这里面带上了数据库版本号的更新
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
	 * 执行ddl语句
	 * @param ddlList
	 * @param conn
	 * @throws SQLException
	 */
	private static void executeDdlList(List<DdlListBean> ddlList, Connection conn) throws SQLException {
		conn.setAutoCommit(true);
		Statement stmt = conn.createStatement();
		for (DdlListBean ddl : ddlList) {
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
	 * @throws SQLException
	 */
	private static void executeDmlList(List<String> dmlList, Connection conn) throws SQLException {
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			for (String sql : dmlList) {
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
		if(versionCodeOld == null || versionCodeOld.trim().isEmpty()) {	//不存在旧版本号肯定是要更新的
			return true;
		}
		if(versionCodeOld.equals(versionCodeNew)) {	//两个版本字符串一致,则说明完全不用升级
			return false;
		}
		
		//线比较日期，日期相同再比较时间
		String[] codesOld = versionCodeOld.split("\\.");
		String[] codesNew = versionCodeNew.split("\\.");
		int length_old = codesOld.length;
		int length_new = codesNew.length;
		for(int i=0;;i++) {
			if(i>=length_old) {
				if(i>=length_new) {	//新旧版本都到底了还没分出胜负则平局
					return false;
				} else {	//旧版本到底了，新版本还有下文，则需要升级
					return true;
				}
			} else if(i>=length_new) {	//旧版本还有下文，新版本到底了，说明旧版本更新(存在这种情况一版说明版本控制有问题)
				return false;
			} else {	//每级版本进行不同的pk，胜者说明对应的是新版本
				int shouldUpdate = Integer.parseInt(codesNew[i]) - Integer.parseInt(codesOld[i]);
				if(shouldUpdate > 0) {
					return true;
				} else if(shouldUpdate < 0) {	//旧版本号比新版本号还大，出现这种情况说明版本控制没操作好
					return false;
				}
			}
			
		}
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
	private static void log(String msg) {
		if(MODE_DEBUG) {
			Console.log(msg);
		}
	}
}
