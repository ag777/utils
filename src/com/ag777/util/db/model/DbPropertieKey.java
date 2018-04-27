package com.ag777.util.db.model;

public class DbPropertieKey {

	private DbPropertieKey() {}
	
	/**
	 * 用户名
	 */
	public final static String COMMON_USER = "user";
	
	/**
	 * 密码
	 */
	public final static String COMMON_PASSWORD = "password";
	
	/**
	 * 是否使用Unicode字符集，如果参数characterEncoding设置为gb2312或gbk，本参数值必须设置为true false 1.1g 
	 */
	public final static String COMMON_USEUNICODE= "useUnicode";
	
	/**
	 * 当useUnicode设置为true时，指定字符编码。比如可设置为gb2312或gbk false 1.1g 
	 */
	public final static String COMMON_ENCODING = "characterEncoding";
	
	/**
	 * 和数据库服务器建立socket连接时的超时，单位：毫秒。 0表示永不超时，适用于JDK 1.4及更高版本 0 3.0.1 
	 */
	public final static String MYSQL_TIMEOUT_CONNECT ="connectTimeout";
	
	/**
	 * oracle连接超时
	 */
	public final static String MYSQL_TIMEOUT_SOCKET ="socketTimeout";
	
	/**
	 * oracle读取数据超时(已经连接上了)
	 */
	public final static String ORACLE_TIMEOUT_SOCKET ="oracle.net.CONNECT_TIMEOUT";
	
	
}
