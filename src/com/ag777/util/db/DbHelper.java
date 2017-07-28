package com.ag777.util.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 数据库操作辅助类
 * @author ag777
 * Time: created at 2017/7/28. last modify at 2017/7/28.
 */
public class DbHelper {

	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	
	public static String driverClassName() {
		return DRIVER_CLASS_NAME;
	}

	public static void driverClassName(String driverClassName) {
		DbHelper.DRIVER_CLASS_NAME = driverClassName;
	}

	private Connection conn;
	
	public DbHelper(Connection conn) {
		this.conn = conn;
	}
	
	public static DbHelper connectDB(String url,String user, String password)
    {
		
		// 加载驱动程序
		try {
			Class.forName(DRIVER_CLASS_NAME);
			// 连接数据库
			return new DbHelper(
					DriverManager.getConnection(url, user, password));

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
    }
	
	/**
	 * 关闭数据库连接.释放资源
	 */
	public void dispose() {
		try {
			if(conn != null && !conn.isClosed()) {
				conn.close();
			}
		} catch (SQLException e) {
//			e.printStackTrace();
		}
		conn = null;
	}
	
	/**
	 * 查询多行
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql) {
    	Statement stmt = null;
    	ResultSet rs = null;
    	try {
	    	stmt = conn.createStatement();
	    	rs = stmt.executeQuery(sql);
	    	return convertList(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return null;
    }
	
	/**
	 * 查询单行
	 * @param sql
	 * @param conn
	 * @return
	 */
	public Map<String, Object> getMap(String sql,Connection conn) {
		List<Map<String, Object>> list = queryList(sql);
		if(list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	
	/**
	 * 插入或更新
	 * @param sql
	 * @return
	 */
	public int update(String sql)
    {
    	Statement stmt = null;
    	int row = -1;
    	try {
			stmt = conn.createStatement();
			row = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	
    	return row;
    }
	
	/**
	 * 插入或更新
	 * @param sql
	 * @param params
	 * @return
	 */
	public int update(String sql, Object[] params) {
    	try {
	    	PreparedStatement pstmt = getPreparedStatement(sql, params);
	    	return pstmt.executeUpdate(); 
    	} catch (SQLException e) {
    		e.printStackTrace();
			return -1;
		} 
    }
	
	/**
	 * 批量插入或更新
	 * @param sql
	 * @param paramsList
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> paramsList) {
    	try {
    		conn.setAutoCommit(false);
	    	PreparedStatement pstmt = getBatchPreparedStatement(sql, paramsList);
	    	int[] results = pstmt.executeBatch(); //批量执行   
	    	conn.commit();//提交事务 
	    	return results;
    	} catch (SQLException e) {
    		e.printStackTrace();
		} 
    	return null;
    }
	
	/**
	 * 通过sql和参数列表获取PreparedStatement(批量)
	 * @param sql
	 * @param paramsList
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getBatchPreparedStatement(String sql, List<Object[]> paramsList) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
    	for (Object[] list : paramsList) {
    		for (int i = 0; i < list.length; i++) {
				pstmt.setObject(i+1, list[i]);
			}
			pstmt.addBatch();
		}
    	return pstmt;
	}
	
	/**
	 * 通过sql和参数列表获取PreparedStatement
	 * @param sql
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public PreparedStatement getPreparedStatement(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql);
	
    	for (int i = 0; i < params.length; i++) {
    		Object item = params[i];
    		pstmt.setObject(i+1, item);
		}
    	return pstmt;
	}
	
	private static List<Map<String, Object>> convertList(ResultSet rs) throws SQLException {

		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		ResultSetMetaData md = rs.getMetaData();

		int columnCount = md.getColumnCount(); // Map rowData;

		while (rs.next()) { // rowData = new HashMap(columnCount);

			Map<String, Object> rowData = new HashMap<String, Object>();

			for (int i = 1; i <= columnCount; i++) {

				rowData.put(md.getColumnName(i), rs.getObject(i));

			}

			list.add(rowData);

		}
		return list;

	}
	
}
