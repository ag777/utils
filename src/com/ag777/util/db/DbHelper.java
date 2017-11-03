package com.ag777.util.db;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.ag777.util.db.interf.DBTransactionInterf;
import com.ag777.util.db.model.ColumnPojo;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.reflection.ReflectionUtils;

/**
 * 数据库操作辅助类
 * 
 * @author ag777
 * @version create on 2017年07月28日,last modify at 2017年11月03日
 */
public class DbHelper {

	//控制控制台输出开关
	private static boolean MODE_DEBUG = true;
	//执行完sql后关闭数据库连接,一旦开启则该工具类不可重复使用(连接不存在了)
	private static boolean MODE_CLOSE_AFTER_EXECUTE = false;
	private static String DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver";
	private static String URL_TAIL = "?useUnicode=true&characterEncoding=UTF-8&zeroDateTimeBehavior=convertToNull";
	
	public static String driverClassName() {
		return DRIVER_CLASS_NAME;
	}

	public static void driverClassName(String driverClassName) {
		DbHelper.DRIVER_CLASS_NAME = driverClassName;
	}

	public static void setModeDebug(boolean debugMode) {
		DbHelper.MODE_DEBUG = debugMode;
	}
	
	public static void setModeCloseAfterExecute(boolean closeAfterExecuteMode) {
		DbHelper.MODE_CLOSE_AFTER_EXECUTE = closeAfterExecuteMode;
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
	
	//--静态方法
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
	 * 数据库类型转java类型(不全，只列出常用的，不在范围内返回null)
	 * <p>
	 * 	参考http://blog.csdn.net/haofeng82/article/details/34857991
	 * </p>
	 * @param sqlType
	 * @param size
	 * @return
	 */
	public static Class<?> toPojoType(int sqlType, long size) {
		Class<?> clazz = null;
		switch(sqlType) {
			case Types.VARCHAR:	//12
			case Types.CHAR:			//1
			case Types.LONGVARCHAR:	//-1
			case Types.NVARCHAR:	//-9
				clazz = String.class;
				break;
			case Types.BLOB:			//2004
			case Types.VARBINARY:	//-3
			case Types.LONGVARBINARY:	//-4
				clazz = Byte[].class;
				break;
			case Types.INTEGER:	//4
			case Types.SMALLINT:	//5
				clazz = Integer.class;
				break;
			case Types.TINYINT:		//-6
				if(size == 1) {
					clazz =Boolean.class;
				} else {
					clazz = Integer.class;
				}
				break;
			case Types.BIT:				//-7
				clazz = Boolean.class;
				break;
			case Types.BIGINT:		//-5
				clazz = Long.class;
				break;
			case Types.FLOAT:		//6
			case Types.REAL:			//7
				clazz = Float.class;
				break;
			case Types.DOUBLE:		//8
				clazz = Double.class;
				break;
			case Types.DECIMAL:	//3
			case Types.NUMERIC:	//2
				clazz = BigDecimal.class;
				break;
			case Types.DATE:			//91
				clazz = java.util.Date.class;	//java.sql.Date
				break;
			case Types.TIME:			//92
				clazz = java.sql.Time.class;
				break;
			case Types.TIMESTAMP:	//93
				clazz = java.sql.Timestamp.class;
				break;
//			case Types.JAVA_OBJECT:	//2000
//			case Types.OTHER:	//1111
//				clazz = Object.class;
//				break;
			default:
				clazz = Object.class;
				break;
		}
		return clazz;
	}
	
	/**
	 * int型的type对应mysql数据库的类型名称(不全，只列出常用的，不在范围内返回null)
	 * @param sqlType
	 * @param size
	 * @return
	 */
	public static String toString(int sqlType) {
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
			case Types.DATE:
				return "date";
			case Types.TIME:
				return "time";
			case Types.TIMESTAMP:
				return "timestamp";
			case Types.BLOB:
				return "blob";
			case Types.BINARY:
				return "binary";
			case Types.VARBINARY:
				return "varbinary";
			default:
				return null;
		}
	}
	
	/**
	 * 判断数据库类型是否为字符串类型
	 * @param sqlType
	 * @return
	 */
	public static boolean isSqlTypeVarchar(int sqlType) {
		switch(sqlType) {
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.VARBINARY:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
			case Types.NVARCHAR:
				return true;
			default:
				return false;
		}
	}
	
	/**
	 * java类型转数据库类型(不全，只列出常用的，不在范围内返回varchar)
	 * @param clazz
	 * @return
	 */
	public static Integer toSqlType(Class<?> clazz) {
		if(ReflectionUtils.isNumberType(clazz)) {
			if(Float.class == clazz || float.class == clazz) {
				return Types.FLOAT;
			} else if(Double.class == clazz || double.class == clazz) {
				return Types.DOUBLE;
			} else if(BigInteger.class == clazz) {
				return Types.BIGINT;
			} else if(BigDecimal.class == clazz) {
				return Types.DECIMAL;
			} else  {
				return Types.INTEGER;
			}
		} else if(Boolean.class == clazz || boolean.class == clazz) {
			return Types.BOOLEAN;
		} else if(java.sql.Date.class == clazz || java.util.Date.class == clazz) {
			return Types.DATE;
		} else if(java.sql.Timestamp.class == clazz) {
			return Types.TIMESTAMP;
		} else if(java.sql.Time.class == clazz) {
			return Types.TIME;
		} else if(Byte[].class == clazz) {
			return Types.BLOB;
		}  else {		//char,void
			return Types.VARCHAR;
		}
	}
	
	/**
	 * 将java类型转为数据库字段类型，返回对应的字符串
	 * @param clazz
	 * @return
	 */
	public String toSqlTypeStr(Class<?> clazz) {
		Integer sqlType = toSqlType(clazz);
		if(sqlType != null) {
			return toString(sqlType);
		}
		return null;
	}
	
	//--非静态方法
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
			if(conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		conn = null;
	}
	
	/**
	 * 执行数据库事务
	 * @param task
	 * @return
	 * @throws Exception
	 */
	public boolean doTransaction(DBTransactionInterf task) throws Exception {
		try {
			conn.setAutoCommit(false);
			boolean result = task.doTransaction(this);
			conn.commit();
			return result;
		} catch(Exception ex) {
			throw ex;
		} finally {
			conn.setAutoCommit(true);
		}
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
		} finally {
			closeAfterExecute();
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
		} finally {
			closeAfterExecute();
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
	@SuppressWarnings("unchecked")
	public <T>List<T> queryObjectList(String sql, Object[] params, Class<T> clazz) {
		
		try {
			List<T> list = null;
			ResultSet rs =getResultSet(sql, params);
			if(isBasicClass(clazz)){
				list = new ArrayList<>();
				while(rs.next()) {
					list.add((T) rs.getObject(1));
				}
			} else {
				list = convert2List(rs, clazz);
			}
			return list;
		} catch(Exception ex) {
			err(ex);
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
	@SuppressWarnings("unchecked")
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
			err(ex);
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
			err(ex);
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
			err(ex);
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
			err(ex);
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
		} finally {
			closeAfterExecute();
		} 
    	
    	return row;
    }
	
	/**
	 * 清空表数据
	 * @param tableName
	 * @return
	 */
	public boolean truncate(String tableName) {
		String sql = "TRUNCATE TABLE "+tableName;
		return update(sql) != -1;
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
		} finally {
			closeAfterExecute();
		}
    }
	
	/**
	 * 插入一条数据并获取对应的主键(自增长),如果失败，返回-1
	 * @param sql
	 * @param params
	 * @return
	 */
	public int insertAndGetKey(String sql, Object[] params) {
    	try {
	    	PreparedStatement pstmt = getPreparedStatement(sql, params, Statement.RETURN_GENERATED_KEYS);
	    	pstmt.executeUpdate(); 
	    	ResultSet rs = pstmt.getGeneratedKeys();
	        rs.next();
	        int key = rs.getInt(1);
	        return key;
    	} catch (SQLException ex) {
    		err(ex);
			return -1;
		} finally {
			closeAfterExecute();
		}
	}
	
	/**
	 * 批量插入或更新
	 * @param sql
	 * @param paramsList
	 * @return
	 */
	public int[] batchUpdate(String sql, List<Object[]> paramsList) {
		if(paramsList == null || paramsList.isEmpty()) {
			return new int[]{};
		}
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
			} catch (SQLException ex) {
				err(ex);
			}
			closeAfterExecute();
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
	
	public PreparedStatement getPreparedStatement(String sql, Object[] params,  int autoGeneratedKeys) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(sql, autoGeneratedKeys);
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
			closeAfterExecute();
		}
		return false;
	}
	
	/**
	 * 将resultset转化为List<Map<String, Object>>
	 * <p>
	 * 171021改获取字段名称的方法getColumnName(i)为
	 * 获取别名getColumnLabel(i) 以免sql里写的别名不生效
	 * </p>
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
				rowData.put(md.getColumnLabel(i), rs.getObject(i));
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
				
				T rowData = ReflectionUtils.newInstace(clazz);
	
				for (int i = 1; i < columnCount; i++) {
					Object value = rs.getObject(1);
					Field[] fields = clazz.getDeclaredFields();
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
			err(ex);
			throw new SQLException("转换结果为对象列表失败");
		}
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
			err(ex);
		} finally {
			closeAfterExecute();
		}
		return null;
	}
	
	/**
	 * 通过表名获取每一个字段的信息
	 * @param tableName
	 * @return
	 */
	public List<ColumnPojo> columnList(String tableName) {
		List<ColumnPojo> columns = new ArrayList<>();
		
		try {
			List<String> primaryKeyList = primaryKeyList(tableName);	//主键列表
			
			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet columnSet = dbmd.getColumns(null, "%", tableName, "%");

			while (columnSet.next()) {
				ColumnPojo column = new ColumnPojo();
				String columnName = columnSet.getString("COLUMN_NAME");
				if(primaryKeyList.contains(columnName)) {	//是否在主键列表里
					column.setPK(true);
				} else {
					column.setPK(false);
				}
			    column.setName(columnName);
			    column.setSqlType(columnSet.getInt("DATA_TYPE"));
			    column.setSize(columnSet.getLong("COLUMN_SIZE"));
			    column.setRemarks(columnSet.getString("REMARKS"));
			     
			    columns.add(column);
			}
			
			return columns;
		} catch(Exception ex) {
			err(ex);
		} finally {
			closeAfterExecute();
		}
		return null;
	}
	
	/**
	 * 通过表名获取所有主键
	 * @param tableName
	 * @return
	 */
	public List<String> primaryKeyList(String tableName) {
		List<String> list = new ArrayList<>();
		try {
			ResultSet rs = conn.getMetaData().getPrimaryKeys(null, null, tableName);
			while(rs.next()) {
				list.add(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException ex) {
			err(ex);
		}
		return list;
	}
	
	/**
	 * 判断数据库里是否存在某张表
	 * @param tableName
	 * @return
	 * @throws SQLException	可能是连接数据库异常,所以不能确定是否存在表
	 */
	public boolean isTableExisted(String tableName) throws SQLException {
		ResultSet rs = conn.getMetaData().getTables(null, null, tableName, null);  
        if (rs.next()) {  
              return true;  
        }else {  
              return false;  
        }  
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
		return ReflectionUtils.isBasicClass(clazz);
	}
	
	
	private static void err(Exception ex) {
		if(MODE_DEBUG) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * 执行完sql后关闭数据库连接
	 */
	private void closeAfterExecute() {
		if(MODE_CLOSE_AFTER_EXECUTE) {
			dispose();
		}
	
	}
}
