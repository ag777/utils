package com.ag777.util.db.connection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.Properties;

import com.ag777.util.db.DbHelper;
import com.ag777.util.db.model.DbDriver;
import com.ag777.util.db.model.OracleRole;

/**
 * Oracle数据库连接辅助类
 * <p>
 * 需求ojdbc6.jar(本工具包不自带)
 * 该jar包为最新版本,以下为关联:
 * ojdbc14.jar - for Java 1.4 and 1.5
	ojdbc5.jar - for Java 1.5
	ojdbc6.jar - for Java 1.6
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2018年04月25日
 */
public class OracleConnection extends BaseDbConnectionUtils{

	private OracleConnection(){}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param dbName
	 * @param role
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName, OracleRole role) throws ClassNotFoundException, SQLException {
		return connect(ip, port, user, password, dbName, role, null);
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param dbName
	 * @param role
	 * @param propMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String ip, int port, String user, String password, String dbName, OracleRole role, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		StringBuilder url = new StringBuilder()
			.append("jdbc:mysql://")
			.append(ip)
			.append(':')
			.append(port);
		if(dbName != null) {
			url.append('/')
				.append(dbName);
		}
		
		return connect(url.toString(), user, password, role, propMap);
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param sid
	 * @param role
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connectBySid(String ip, int port, String user, String password, String sid, OracleRole role) throws ClassNotFoundException, SQLException {
		return connectBySid(ip, port, user, password, sid, role, null);
	}
	
	/**
	 * 
	 * @param ip
	 * @param port
	 * @param user
	 * @param password
	 * @param sid
	 * @param role
	 * @param propMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connectBySid(String ip, int port, String user, String password, String sid, OracleRole role, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		StringBuilder url = new StringBuilder()
			.append("jdbc:mysql://")
			.append(ip)
			.append(':')
			.append(port)
			.append(":")
			.append(sid);
		
		return connect(url.toString(), user, password, role, propMap);
	}
	
	/**
	 * 
	 * @param url
	 * @param user
	 * @param password
	 * @param role
	 * @param propMap
	 * @return
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public static Connection connect(String url, String user, String password, OracleRole role, Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		Properties props = getProperties(propMap);
		if(role != null) {
			props.put("internal_logon", role.getName());
		}
		
		return DbHelper.getConnection(url, user, password, DbDriver.ORACLE, props);
	}

	@Override
	public int getDefaultPort() {
		return 1521;
	}
}
