package com.ag777.util.db.connection;

import com.ag777.util.db.DbConnectionUtil;
import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.DbDriver;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Properties;

/**
 * Mysql数据库连接辅助类
 * <p>
 * 需求mysql-connector-java-xxx.jar(本工具包不自带)
 * </p>
 * 
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2020年10月09日
 */
public class MysqlConnection extends BaseDbConnectionUtils {

	private static DbDriver DRIVER_DEFAULT = DbDriver.MYSQL_OLD;

	private MysqlConnection() {}

	public static void driver(DbDriver driver) {
		MysqlConnection.DRIVER_DEFAULT = driver;
	}

	public static void setToNewDriver() {
		driver(DbDriver.MYSQL);
	}
	
	/**
	 * 
	 * @param ip ip
	 * @param port port
	 * @param user user
	 * @param password password
	 * @param dbName dbName
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName) throws ClassNotFoundException, SQLException {
		return connect(ip, port, user, password, dbName, null);
	}
	
	/**
	 * @see #connect(DbDriver, String, int, String, String, String, Map)
	 * @param ip ip
	 * @param port port
	 * @param user user
	 * @param password password
	 * @param dbName dbName
	 * @param propMap propMap
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		return connect(DRIVER_DEFAULT, ip, port, user, password, dbName, propMap);
	}

	/**
	 * 连接mysql数据库
	 * <p>
	 * ipv4 Driver URL:
	 *		jdbc:mysql://127.0.0.1:3306/database
	 *	ipv6 Driver URL:
	 *		jdbc:mysql://address=(protocol=tcp)(host=2001:470:23:13::6)(port=3306)/database
	 *
	 *  Deiver package version 5.1.31 以上
	 *
	 * <p>这方法里ipV4和V6都采用ipV6的url
	 *
	 * @param driver 驱动
	 * @param ip ip
	 * @param port port
	 * @param user user
	 * @param password password
	 * @param dbName dbName
	 * @param propMap propMap
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 */
	public static Connection connect(DbDriver driver, String ip, int port, String user, String password, String dbName, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
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

		return connect(driver, url.toString(), user, password, propMap);
	}

	/**
	 * 
	 * @param url url
	 * @param user user
	 * @param password password
	 * @param propMap propMap
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 */
	public static Connection connect(String url, String user, String password, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		return connect(DRIVER_DEFAULT, url, user, password, propMap);
	}

	/**
	 *
	 * @param driver 驱动
	 * @param url url
	 * @param user user
	 * @param password password
	 * @param propMap propMap
	 * @return Connection
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws SQLException SQLException
	 */
	public static Connection connect(DbDriver driver, String url, String user, String password, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		Properties props = getProperties(propMap);
		return DbConnectionUtil.connect(url, user, password, driver, props);
	}

	@Override
	public int getDefaultPort() {
		return 3306;
	}
	
	
	/**
	 * 将java类型转为数据库字段类型，返回对应的字符串
	 * @param clazz clazz
	 * @return String
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
	 * @param sqlType sqlType
	 * @return String
	 */
	private static String toString(int sqlType) {
		return toString(sqlType, 0);
	}
	
	/**
	 * int型的type对应mysql数据库的类型名称(不全，只列出常用的，不在范围内返回null)
	 * @param sqlType sqlType
	 * @param size size
	 * @return String
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
