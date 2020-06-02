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
 * 需求db2jcc4-xxx.jar(本工具包不自带)
 * </p>
 * 
 * @author ag777
 * @version create on 2019年08月20日,last modify at 2019年08月20日
 *
 */
public class Db2Connection extends BaseDbConnectionUtils {

	private Db2Connection() {}
	
	/**
	 * 
	 * jdbc:db2://192.168.10.10:50000/sample
	 * jdbc:db2://[fec0:ffff:ffff:8000:20e:cff:fe50:39c8]:50000/sample
	 * 
	 * 改用db2jcc4.jar(原来使用db2jcc.jar,两个包的区别大概是协议/标准不同),v11.5 FP0 (GA)	4.26.14
	 * 唯一指定下载地址:https://www-01.ibm.com/support/docview.wss?uid=swg21363866(需翻墙)
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
	public static Connection connect(String ip, String port, String user, String password, Object dbName,
			Map<String, Object> propMap) throws ClassNotFoundException, SQLException {
		StringBuilder url = new StringBuilder("jdbc:db2://");
		if(isIpV6(ip)) {
			ip = "[" + ip + "]";
		}
		url.append(ip).append(':').append(port).append('/');
		if (dbName != null) {
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
		return DbHelper.getConnection(url, user, password, DbDriver.DB2, props);
	}
	
	@Override
	public int getDefaultPort() {
		return 50000;
	}

}
