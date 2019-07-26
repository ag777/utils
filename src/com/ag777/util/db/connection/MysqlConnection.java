package com.ag777.util.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Properties;

import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.DbDriver;

/**
 * Mysql数据库连接辅助类
 * <p>
 * 需求mysql-connector-java-xxx.jar(本工具包不自带)
 * </p>
 * 
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2019年07月26日
 */
public class MysqlConnection extends BaseDbConnectionUtils{

	private MysqlConnection() {}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param dbName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName) throws ClassNotFoundException, SQLException {
		return connect(ip, port, user, password, dbName, null);
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param dbName
	 * @param propMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		/*这种写法同时支持ipV4和V6*/
		StringBuilder url = new StringBuilder("jdbc:mysql://address=")
				.append("(protocol=tcp)")
				.append("(host=").append(ip).append(')');
		if(port != 3306) {	//端口号可以省略,非8.0以上驱动url过长可能导致报错(来源百度，真实性未知),尽量缩减url长度
			url.append("(port=").append(port).append(')');
		}
		url.append('/');
		if(dbName != null) {
			url.append(dbName);
		}
		
		return connect(url.toString(), user, password, propMap);
	}
	
	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @param propMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String url, String user, String password, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		Properties props = getProperties(propMap);
		return DbHelper.getConnection(url, user, password, DbDriver.MYSQL, props);
	}

	@Override
	public int getDefaultPort() {
		return 3306;
	}
	
	
	/**
	 * 将java类型转为数据库字段类型，返回对应的字符串
	 * @param clazz
	 * @return
	 */
	public static String toSqlTypeStr(Class<?> clazz) {
		Integer sqlType = DbHelper.toSqlType(clazz);
		if(sqlType != null) {
			return toString(sqlType);
		}
		return null;
	}
	
	/**
	 * 数据库类型转名称
	 * @param sqlType
	 * @return
	 */
	private static String toString(int sqlType) {
		return toString(sqlType, 0);
	}
	
	/**
	 * int型的type对应mysql数据库的类型名称(不全，只列出常用的，不在范围内返回null)
	 * @param sqlType
	 * @param size
	 * @return
	 */
	public static String toString(int sqlType, int size) {
		switch(sqlType) {
			case Types.TINYINT:
				return "tinyint";
			case Types.SMALLINT:
				return "smallint";
			case Types.INTEGER:
				return "int";
			case Types.BIGINT:
				return "bigint";
			case Types.BIT:
				return "bit";
			case Types.REAL:
				return "real";
			case Types.DOUBLE:
				return "double";
			case Types.FLOAT:
				return "float";
			case Types.DECIMAL:
				return "decimal";
			case Types.NUMERIC:
				return "numeric";
			case Types.CHAR:
				return "char";
			case Types.VARCHAR:
				return "varchar";
			case Types.LONGVARCHAR:
				if(size > 65535) {
					return "mediumtext";
				}
				return "text";
			case Types.DATE:
				return "date";
			case Types.TIME:
				return "time";
			case Types.TIMESTAMP:
				return "timestamp";
			case Types.BLOB:
			case Types.LONGVARBINARY:
				return "blob";
			case Types.BINARY:
				return "binary";
			case Types.VARBINARY:
				return "varbinary";
			default:
				return null;
		}
	}
}
