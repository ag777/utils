package com.ag777.util.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.DbDriver;

/**
 * Mysql数据库连接辅助类
 * <p>
 * 需求sqljdbc-xxx.jar(本工具包不自带)
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2018年04月25日
 */
public class SqlServerConnection extends BaseDbConnectionUtils{

	private SqlServerConnection() {}
	
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
		StringBuilder url = new StringBuilder()
			.append("jdbc:sqlserver://")
			.append(ip)
			.append(':')
			.append(port)
			.append(";");
		if(dbName != null) {
			url.append("databaseName=")
				.append(dbName);
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
		return 1433;
	}
	
}
