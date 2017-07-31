package com.ag777.util.db;

import java.lang.reflect.Field;
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

import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.reflection.ReflectionHelper;

/**
 * 数据库操作辅助类
 * @author ag777
 * Time: created at 2017/7/28. last modify at 2017/7/31.
 */
public class DbHelper {

	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static String URL_TAIL = "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	
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
	
	public static DbHelper connectDB(String ip, int port, String dbName, String user, String password) throws ClassNotFoundException, SQLException {
		return connectDB(getDbUrlString(ip, port, dbName), user, password);
	}
	
	public static DbHelper connectDB(String url,String user, String password) throws ClassNotFoundException, SQLException {
		
		// 加载驱动程序
		Class.forName(DRIVER_CLASS_NAME);
		// 连接数据库
		return new DbHelper(
				DriverManager.getConnection(url, user, password));

    }
	
	/**
	 * 通过ip端口号和数据库名称获取用于连接数据库的url
	 * @param ip
	 * @param port
	 * @param dbName
	 * @return
	 */
	public static String getDbUrlString(String ip, int port, String dbName){
		return new StringBuilder()
						.append("jdbc:mysql://")
						.append(ip)
						.append(':')
						.append(port)
						.append('/')
						.append(dbName)
						.append(URL_TAIL).toString();
	}
	
	/**
	 * 获取连接
	 * @return
	 */
	public Connection getConn() {
		return conn;
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
	
	public ResultSet getResultSet(String sql) {
    	try {
    		Statement stmt = conn.createStatement();
	    	return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return null;
	}
	
	public ResultSet getResultSet(String sql, Object[] params) {
		if(isNullOrEmpty(params)) {
			return getResultSet(sql);
		}
		try {
			PreparedStatement ps = getPreparedStatement(sql, params);
			return ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询多行, 通过Statement执行
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql) {
    	try {
	    	ResultSet rs = getResultSet(sql);
	    	return convert2List(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return null;
    }
	
	/**
	 * 查询多行，带参数，通过PreparedStatement执行
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Map<String, Object>> queryList(String sql, Object[] params) {
		try {
			ResultSet rs =getResultSet(sql, params);
			return convert2List(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public <T>List<T> queryObjectList(String sql, Object[] params, Class<T> clazz) {
		
		try {
			List<T> list = null;
			ResultSet rs =getResultSet(sql, params);
			if(clazz.isAssignableFrom(Integer.class) ||	//clazz是基本类型
					clazz.isAssignableFrom(Byte.class) ||
					clazz.isAssignableFrom(Short.class) ||
					clazz.isAssignableFrom(Long.class) ||
					clazz.isAssignableFrom(Float.class) ||
					clazz.isAssignableFrom(Double.class) ||
					clazz.isAssignableFrom(Boolean.class) ||
					clazz.isAssignableFrom(Character.class)){
				list = new ArrayList<>();
				while(rs.next()) {
					list.add((T) rs.getObject(1));
				}
			} else {
				list = convert2List(rs, clazz);
			}
			return list;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 查询单行, 通过Statement执行
	 * @param sql
	 * @param conn
	 * @return
	 */
	public Map<String, Object> getMap(String sql) {
		List<Map<String, Object>> list = queryList(sql);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 查询单行,带参数，通过PreparedStatement执行
	 * @param sql
	 * @param params
	 * @return
	 */
	public Map<String, Object> getMap(String sql, Object[] params) {
		if(isNullOrEmpty(params)) {
			return getMap(sql);
		}
		List<Map<String, Object>> list = queryList(sql, params);
		if(list == null || list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}
	
	/**
	 * 获取int类型的结果
	 * @param sql
	 * @return
	 */
	public Integer getInteger(String sql, Object[] params) {
		try {
			Map<String, Object> resultMap = getMap(sql, params);
			if(resultMap != null) {
				Object value = resultMap.get(0);
				if(value != null) {
					
					if(value instanceof Integer) {
						return (Integer) value;
					} else {
						return Integer.parseInt(value.toString());
					}
					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取double类型的返回
	 * @param sql
	 * @param params
	 * @return
	 */
	public Double getDouble(String sql, Object[] params) {
		try {
			Map<String, Object> resultMap = getMap(sql, params);
			if(resultMap != null) {
				Object value = resultMap.get(0);
				if(value != null) {
					
					if(value instanceof Double) {
						return (Double) value;
					} else {
						return Double.parseDouble(value.toString());
					}
					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 获取字符串类型的返回
	 * @param sql
	 * @param params
	 * @return
	 */
	public String getString(String sql, Object[] params) {
		try {
			Map<String, Object> resultMap = getMap(sql, params);
			if(resultMap != null) {
				Object value = resultMap.get(0);
				if(value != null) {
					return value.toString();
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
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
		if(isNullOrEmpty(params)) {
			return update(sql);
		}
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
		if(params != null) {
	    	for (int i = 0; i < params.length; i++) {
	    		Object item = params[i];
	    		pstmt.setObject(i+1, item);
			}
		}
    	return pstmt;
	}
	
	/**
	 * 将resultset转化为List<Map<String, Object>>
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public static List<Map<String, Object>> convert2List(ResultSet rs) throws SQLException {

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
	
	/**
	 * 将resultSet转化为对象列表(方法有待验证及优化)
	 * @param rs
	 * @param clazz
	 * @return
	 * @throws SQLException
	 */
	public static <T>List<T> convert2List(ResultSet rs, Class<T> clazz) throws SQLException {
		try {
			List<T> list = new ArrayList<T>();
	
			ResultSetMetaData md = rs.getMetaData();
	
			int columnCount = md.getColumnCount(); // Map rowData;
	
			String[] cols = new String[columnCount];
			for (int i = 1; i <= columnCount; i++) {
				cols[i-1] = StringUtils.underline2Camel(md.getColumnName(i), false);	//首字母大写，驼峰
			}
			
			while (rs.next()) { // rowData = new HashMap(columnCount);
				
				T rowData = clazz.newInstance();
	
				for (int i = 1; i <= columnCount; i++) {
					Object value = rs.getObject(1);
					Field[] fields = clazz.getFields();
					for (Field field : fields) {
						if(field.getName().equalsIgnoreCase(cols[i])) {
							boolean flag = field.isAccessible();
							field.setAccessible(true);
							field.set(rowData, value);
							field.setAccessible(flag);
						}
					}
					
	
				}
	
				list.add(rowData);
	
			}
			return list;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	private boolean isNullOrEmpty(Object[] params) {
		return params == null || params.length == 0;
	}
	
}
