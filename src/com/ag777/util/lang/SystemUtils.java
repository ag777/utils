package com.ag777.util.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Properties;
import java.util.Set;

/**
 * 系统常量获取工具类
 * <p>
 * 		部分参考文献:http://blog.csdn.net/kongqz/article/details/3987198
 * </p>
 * 
 * @author ag777
 * @version create on 2017年06月12日,last modify at 2018年01月04日
 */
public class SystemUtils {

	/**
	 * 将控制台输出重定向到文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean setSystemOut(String filePath) {
		try {
			System.setOut(new PrintStream(new File(filePath)));
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 还原控制台输出到控制台
	 */
	public static void resetSystemOut() {
		System.setOut(System.out);
	}
	
	/**
	 * 获取cpu核数(可用于确定子线程数量以优化程序执行效率)
	 * 
	 * @return the maximum number of processors available to the virtual machine; never smaller than one
	 */
	public static int cpuCores() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 总内存
	 * <p>
	 * Returns the total amount of memory in the Java virtual machine. The value returned by this method may vary over time, depending on the host environment.
	 * </p>
	 * 
	 * @return the total amount of memory currently available for current and future objects, measured in bytes.
	 */
	public static long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}
	
	/**
	 * 最大内存
	 * <p>
	 * Returns the maximum amount of memory that the Java virtual machine will attempt to use. If there is no inherent limit then the value java.lang.Long.MAX_VALUE will be returned.
	 * </p>
	 * 
	 * @return Returns the maximum amount of memory that the Java virtual machine will attempt to use. If there is no inherent limit then the value java.lang.Long.MAX_VALUE will be returned.
	 */
	public static long maxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
	
	/**
	 * 剩余内存
	 * <p>
	 * Returns the amount of free memory in the Java Virtual Machine. Calling the gc method may result in increasing the value returned by freeMemory
	 * </p>
	 * 
	 * @return an approximation to the total amount of memory currently available for future allocated objects, measured in bytes.
	 */
	public static long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	/**
	 * 增加jvm关闭时的钩子
	 * 
	 * @param hook 在jvm关闭时会执行完这里的方法才会被关闭
	 */
	public static void addShutdownHook(Thread hook) {
		Runtime.getRuntime().addShutdownHook(hook);
	}
	
	/**
	 * 移除jvm关闭时的钩子
	 * 
	 * @param hook
	 */
	public static void removeShutdownHook(Thread hook) {
		Runtime.getRuntime().removeShutdownHook(hook);
	}
	
	
	/**
	 * 文件换行符
	 * 
	 * @return
	 */
	public static String fileSeparator() {
		return File.separator;//等同于System,getProperty("file.separator");
	}
	
	/**
	 * 路径分隔符
	 * <p>
	 * 		指的是分隔连续多个路径字符串的分隔符，例如: 
			java   -cp   test.jar;abc.jar   HelloWorld 中的";",unix中这个值为":"
		</p>
	 * @return
	 */
	public static String pathSeparator() {
		return File.pathSeparator;//等同于System.getProperty("path.separator");看过源码了,代理模式,windows从WinNTFileSystem中获取
	}
	
	/**
	 * 换行符windows \r\n,linux \n
	 * 
	 * @return
	 */
	public static String lineSeparator() {
		return System.lineSeparator();//等同于System.getProperty("line.separator");看过源码了,单例,随jvm初始化时创建
	}
	
	/**
	 * Java 运行时环境版本
	 * 
	 * @return
	 */
	public static String javaVersion() {
		return System.getProperty("java.version");
	}
	
	/**
	 * Java 安装目录
	 * 
	 * @return
	 */
	public static String pathJavaHome() {
		return System.getProperty("java.home");
	}
	
	/**
	 * 用户文件夹路径
	 * <p>
	 * 	如:C:\Users\ag777
	 * </p>
	 * 
	 * @return
	 */
	public static String pathUserHome() {
		return System.getProperty("user.home");
	}
	
	/**
	 * 系统临时文件的存放路径
	 * <p>
	 * 	如:C:\Users\ag777\AppData\Local\Temp\
	 * </p>
	 * 
	 * @return
	 */
	public static String pathTempDir() {
		return System.getProperty("java.io.tmpdir");
	}
	
	/**
	 * 控制台打印所有System.getProperties()中的属性
	 */
	public static void logAllProperties() {
		Properties sysProperty = System.getProperties(); // 系统属性
		Set<Object> keySet = sysProperty.keySet();
		for (Object object : keySet) {
			String property = sysProperty.getProperty(object.toString());
			System.out.println(object.toString() + " : " + property);
		}
	}
}
