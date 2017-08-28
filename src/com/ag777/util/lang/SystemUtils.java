package com.ag777.util.lang;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

/**
 * @Description 系统常量获取工具类
 * @author ag777
 * Time: created at 2017/6/12. last modify at 2017/8/23.
 * Mark: 部分参考文献:http://blog.csdn.net/kongqz/article/details/3987198
 */
public class SystemUtils {

	/**
	 * 将控制台输出重定向到文件
	 * @param filePath
	 * @return
	 */
	public boolean setSystemOut(String filePath) {
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
	public void resetSystemOut() {
		System.setOut(System.out);
	}
	
	/**
	 * 获取cpu核数(可用于确定子线程数量以优化程序执行效率)
	 * @return the maximum number of processors available to the virtual machine; never smaller than one
	 */
	public static int cpuCores() {
		return Runtime.getRuntime().availableProcessors();
	}

	/**
	 * 总内存
	 * Returns the total amount of memory in the Java virtual machine. The value returned by this method may vary over time, depending on the host environment.
	 * @return the total amount of memory currently available for current and future objects, measured in bytes.
	 */
	public static long totalMemory() {
		return Runtime.getRuntime().totalMemory();
	}
	
	/**
	 * 最大内存
	 * Returns the maximum amount of memory that the Java virtual machine will attempt to use. If there is no inherent limit then the value java.lang.Long.MAX_VALUE will be returned.
	 * @return Returns the maximum amount of memory that the Java virtual machine will attempt to use. If there is no inherent limit then the value java.lang.Long.MAX_VALUE will be returned.
	 */
	public static long maxMemory() {
		return Runtime.getRuntime().maxMemory();
	}
	
	/**
	 * 剩余内存
	 * Returns the amount of free memory in the Java Virtual Machine. Calling the gc method may result in increasing the value returned by freeMemory
	 * @return an approximation to the total amount of memory currently available for future allocated objects, measured in bytes.
	 */
	public static long freeMemory() {
		return Runtime.getRuntime().freeMemory();
	}
	
	/**
	 * 增加jvm关闭时的钩子
	 * @param hook 在jvm关闭时会执行完这里的方法才会被关闭
	 */
	public static void addShutdownHook(Thread hook) {
		Runtime.getRuntime().addShutdownHook(hook);
	}
	
	/**
	 * 移除jvm关闭时的钩子
	 * @param hook
	 */
	public static void removeShutdownHook(Thread hook) {
		Runtime.getRuntime().removeShutdownHook(hook);
	}
	
	
	/**
	 * 文件换行符
	 * @return
	 */
	public static String fileSeparator() {
		return File.separator;//等同于System,getProperty("file.separator");
	}
	
	/**
	 * 路径分隔符
	 * 指的是分隔连续多个路径字符串的分隔符，例如: 
			java   -cp   test.jar;abc.jar   HelloWorld 中的";",unix中这个值为":"
	 * @return
	 */
	public static String pathSeparator() {
		return File.pathSeparator;//等同于System.getProperty("path.separator");看过源码了,代理模式,windows从WinNTFileSystem中获取
	}
	
	/**
	 * 换行符windows \r\n,linux \n
	 * @return
	 */
	public static String lineSeparator() {
		return System.lineSeparator();//等同于System.getProperty("line.separator");看过源码了,单例,随jvm初始化时创建
	}
	
	/**
	 * Java 运行时环境版本
	 * @return
	 */
	public static String javaVersion() {
		return System.getProperty("java.version");
	}
	
	/**
	 * Java 安装目录
	 * @return
	 */
	public static String javaHome() {
		return System.getProperty("java.home");
	}
	
}
