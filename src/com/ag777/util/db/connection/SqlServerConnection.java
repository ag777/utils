package com.ag777.util.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.DbDriver;
import com.ag777.util.lang.collection.MapUtils;

/**
 * Mysql数据库连接辅助类
 * <p>
 * 需求sqljdbc-xxx.jar(本工具包不自带)
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2019年08月20日
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
	 * 连接sqlserver数据库
	 * <p>连接数据库可以使用jtds这个驱动包，也可以使用sqljdbc4这个驱动包,这个方法使用后者
	 * 
	 * ipv4 Driver URL: 
	 *		jdbc:sqlserver://127.0.0.1:1433/master
	 *	ipv6 Driver URL:
	 *		jdbc:sqlserver://
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
				.append("jdbc:sqlserver://");
		if(!isIpV6(ip)) {	//ipV4
			url.append(ip)
					.append(':')
					.append(port)
					.append(";");
			if(dbName != null) {
				url.append("databaseName=")
					.append(dbName);
			}
		} else {	//ipV6
			if(propMap == null) {
				propMap = MapUtils.newHashMap();
			}
			propMap.put("portNumber", port);  
			propMap.put("instanceName ", dbName);  
			propMap.put("serverName", ip);
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
		return DbHelper.getConnection(url, user, password, DbDriver.SQLSERVER, props);
	}

	@Override
	public int getDefaultPort() {
		return 1433;
	}
	
}
