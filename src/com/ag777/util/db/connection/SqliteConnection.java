package com.ag777.util.db.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.ag777.util.db.model.DbDriver;

/**
 * Sqlite数据库连接辅助类
 * <p>
 * 需求sqlite-jdbc-xxx.jar(本工具包不自带)
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2018年04月25日
 */
public class SqliteConnection extends BaseDbConnectionUtils{

	private SqliteConnection(){}
	
	public static Connection connect(String filePath) throws ClassNotFoundException, SQLException {
		StringBuilder url = new StringBuilder()
			.append("jdbc:sqlite:")
			.append(filePath);
		// 加载驱动程序
		Class.forName(DbDriver.SQLITE.getName());
		return DriverManager.getConnection(url.toString());
	}

	@Override
	public int getDefaultPort() {	//不存在的，直接读取文件
		return -1;
	}
	
}
