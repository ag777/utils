package com.ag777.util.db;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.ag777.util.lang.StringUtils;

/**
 * 数据库操作辅助类
 * @author ag777
 * Time: created at 2017/7/28. last modify at 2017/8/2.
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
	
	/**
	 * 根据sql获取结果集
	 * @param sql
	 * @return
	 */
	public ResultSet getResultSet(String sql) {
    	try {
    		Statement stmt = conn.createStatement();
	    	return stmt.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
    	return null;
	}
	
	/**
	 * 根据sql和参数获取结果集，如果参数数组为空或者为null，则调通过Statement获取结果集，反之通过PreparedStatement获取
	 * @param sql
	 * @param params 参数列表，按顺序写入sql
	 * @return
	 */
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
			if(isBasicClass(Character.class)){
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
	 * 获取第一条记录的第一个值(待测)
	 * @param sql
	 * @param params
	 * @param clazz
	 * @return
	 */
	public <T>T getObject(String sql, Object[] params, Class<T> clazz) {
		try{
			ResultSet rs =getResultSet(sql, params);
			if(rs.next()) {
				if(isBasicClass(clazz)) {
					return (T) rs.getObject(1);
				} else {
					List<T> list = convert2List(rs, clazz);
					if(list != null && !list.isEmpty()) {
						return list.get(0);
					}
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return null;
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
				Iterator<String> itor = resultMap.keySet().iterator();
				if(itor.hasNext()) {
					Object value = resultMap.get(itor.next());
					if(value != null) {
						
						if(value instanceof Integer) {
							return (Integer) value;
						} else {
							return Integer.parseInt(value.toString());
						}
						
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
				Iterator<String> itor = resultMap.keySet().iterator();
				if(itor.hasNext()) {
					Object value = resultMap.get(itor.next());
					if(value != null) {
						
						if(value instanceof Double) {
							return (Double) value;
						} else {
							return Double.parseDouble(value.toString());
						}
						
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
				if(resultMap != null) {
					Iterator<String> itor = resultMap.keySet().iterator();
					if(itor.hasNext()) {
						Object value = resultMap.get(itor.next());
						if(value != null) {
							return value.toString();
						}
					}
					
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
    		try {
				conn.rollback();
			} catch (SQLException e1) {
			}
		}  finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
				e.printStackTrace();
			}
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
	 * 批量执行sql语句,这里记得只能执行dml语句(针对数据的语句),执行ddl语句(表结构操作)时会立即触发commit导致无法回滚
	 * @param sqlList
	 * @return
	 */
	public boolean batchExcute(List<String> sqlList) {
		try {
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			for (String sql : sqlList) {
				try {
					stmt.executeUpdate(sql);
				} catch(SQLException ex) {
					throw ex;
				}
			}
			conn.commit();
			return true;
		} catch(Exception ex) {
			try {
				conn.rollback();
			} catch (SQLException e) {
			}
		} finally {
			try {
				conn.setAutoCommit(true);
			} catch (SQLException e) {
			}
		}
		return false;
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
	
	/*-----数据库结构层面的工具方法-----*/
	/**
	 * 获取所有表的名称
	 * @param tableName
	 * @return
	 */
	public ArrayList<String> tableList() {
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ArrayList<String> tableNameList = new ArrayList<>();
	        ResultSet rs = null;
	        String[] typeList = new String[] { "TABLE" };
	        rs = dbmd.getTables(null, "%", "%",  typeList);
	        for (boolean more = rs.next(); more; more = rs.next()) {
	            String s = rs.getString("TABLE_NAME");
	            String type = rs.getString("TABLE_TYPE");
	            if (type.equalsIgnoreCase("table") && s.indexOf("$") == -1)
	            	tableNameList.add(s);
	        }
	        return tableNameList;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 通过表名获取每一个字段的信息
	 * @param tableName
	 * @return
	 */
	public List<Map<String, Object>> columnList(String tableName) {
		List<Map<String, Object>> columns = new ArrayList<>();
		
		try {
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");

			while (columnSet.next()) {
				Map<String, Object> column = new HashMap<>();
				String columnName = columnSet.getString("COLUMN_NAME");
			    String remarks = columnSet.getString("REMARKS");
			    Integer sqlType = columnSet.getInt("DATA_TYPE");
			    Long columnSize = columnSet.getLong("COLUMN_SIZE");
			    
			    column.put("COLUMN_NAME", columnName);
			    column.put("REMARKS", remarks);
			    column.put("DATA_TYPE", sqlType);
			    column.put("COLUMN_SIZE", columnSize);
			     
			    columns.add(column);
			}
			
			return columns;
		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	/*----内部工具方法------*/
	
	/**
	 * 是否为空指针或空字符串
	 * @param params
	 * @return
	 */
	private boolean isNullOrEmpty(Object[] params) {
		return params == null || params.length == 0;
	}
	
	/**
	 * 判断一个类是否为基础类型
	 * @param clazz
	 * @return
	 */
	private static boolean isBasicClass(Class<?> clazz) {
		return clazz.isAssignableFrom(Integer.class) ||	//clazz是基本类型
				clazz.isAssignableFrom(Byte.class) ||
				clazz.isAssignableFrom(Short.class) ||
				clazz.isAssignableFrom(Long.class) ||
				clazz.isAssignableFrom(Float.class) ||
				clazz.isAssignableFrom(Double.class) ||
				clazz.isAssignableFrom(Boolean.class) ||
				clazz.isAssignableFrom(Character.class);
	}
}
