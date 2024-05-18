package com.ag777.util.db;

import com.ag777.util.db.model.DbDriver;
import com.ag777.util.db.model.DbPropertyKey;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author ag777＜ag777@vip.qq.com＞
 * @version 2024/5/18 下午5:45
 */
public class DbConnectionUtil {
    private static final String TRUE = "true";
    private static final String UTF_8 = "utf-8";
    private static final String CONVERT_TO_NULL = "convertToNull";

    /**
     * 通过给定的参数创建并返回数据库连接。
     * @param url 数据库连接URL
     * @param user 连接数据库的用户名
     * @param password 连接数据库的密码
     * @param driver 驱动程序实例，用于直接通过驱动连接数据库
     * @param props 连接属性，可包含任意额外的属性信息
     * @return 返回建立的数据库连接
     * @throws SQLException 如果连接失败则抛出SQLException
     */
    public static Connection connect(String url, String user, String password, Driver driver, Properties props) throws SQLException {
        props = prepareProperties(props, user, password); // 准备连接属性
        return driver.connect(url, props); // 使用驱动程序实例建立连接
    }

    /**
     * 通过给定的参数创建并返回数据库连接，此方法通过驱动名称动态加载驱动。
     * @param url 数据库连接URL
     * @param user 连接数据库的用户名
     * @param password 连接数据库的密码
     * @param driverName 驱动程序的类名，用于动态加载驱动
     * @param props 连接属性，可包含任意额外的属性信息
     * @return 返回建立的数据库连接
     * @throws ClassNotFoundException 如果驱动类未找到则抛出ClassNotFoundException
     * @throws SQLException 如果连接失败则抛出SQLException
     */
    public static Connection connect(String url, String user, String password, String driverName, Properties props) throws ClassNotFoundException, SQLException {
        props = prepareProperties(props, user, password); // 准备连接属性
        // 加载驱动程序
        Class.forName(driverName);
        return DriverManager.getConnection(url, props); // 使用驱动管理器建立连接
    }

    /**
     * 通过给定的参数创建并返回数据库连接，此方法通过DbDriver对象动态加载驱动。
     * @param url 数据库连接URL
     * @param user 连接数据库的用户名
     * @param password 连接数据库的密码
     * @param driver DbDriver实例，包含驱动相关信息
     * @param props 连接属性，可包含任意额外的属性信息
     * @return 返回建立的数据库连接
     * @throws ClassNotFoundException 如果驱动类未找到则抛出ClassNotFoundException
     * @throws SQLException 如果连接失败则抛出SQLException
     */
    public static Connection connect(String url, String user, String password, DbDriver driver, Properties props) throws ClassNotFoundException, SQLException {
        props = prepareProperties(props, user, password); // 准备连接属性
        // 加载驱动程序
        Class.forName(driver.getName());
        return DriverManager.getConnection(url, props); // 使用驱动管理器建立连接
    }

    /**
     * 准备或初始化数据库连接所需的属性。
     * @param props 连接时的属性配置，如果为null，则内部会创建一个新的Properties对象
     * @param user 连接数据库的用户名
     * @param password 连接数据库的密码
     * @return 返回配置好的Properties对象
     */
    private static Properties prepareProperties(Properties props, String user, String password) {
        if (props == null) {
            props = new Properties();
        }
        // 基本属性设置
        props.put(DbPropertyKey.COMMON_USER, user);
        props.put(DbPropertyKey.COMMON_PASSWORD, password);
        setIfNotContains(props, DbPropertyKey.COMMON_USEUNICODE, TRUE);
        setIfNotContains(props, DbPropertyKey.COMMON_ENCODING, UTF_8);
        // 额外的属性设置，例如日期时间的处理
        setIfNotContains(props, "zeroDateTimeBehavior", CONVERT_TO_NULL);
        return props;
    }

    /**
     * 如果给定的属性集中不包含指定的键，则添加该键值对。
     * @param props 属性集
     * @param key 要添加的键
     * @param value 要添加的值
     */
    private static void setIfNotContains(Properties props, String key, String value) {
        if (!props.containsKey(key)) {
            props.put(key, value);
        }
    }

}
