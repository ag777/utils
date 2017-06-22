package com.ag777.util.lang;

import java.io.File;

import com.ag777.util.file.FileUtils;

/**
 * @Description 系统常量获取工具类
 * @author wanggz
 * Time: created at 2017/6/12. last modify at 2017/6/16.
 * Mark: 部分参考文献:http://blog.csdn.net/kongqz/article/details/3987198
 */
public class SystemUtils {
	public enum OsType  {
		UNKOWN,
		WINDOWS,	
		LINUX,
		KYLIN
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
	
	/*--------------------操作系统相关-------------- */
	
	/**
	 * 获取操作系统的版本
	 * @return
	 */
	public static String osVersion() {
		return System.getProperty("os.version");
	}
	
	/**
	 * 获取操作系统名
	 * @return
	 */
	public static String osName() {
		return System.getProperty("os.name");
	}
	
	/**
	 * 获取操作系统类型
	 * @return
	 */
	public static OsType osType() {
		String osName = osName();
		if(osName.toLowerCase().startsWith("windows")) {
			return OsType.WINDOWS;
		}else if(osName.toLowerCase().startsWith("linux")) {
			try{	//linux下有两个文件/etc/issue和/etc/issue.net两个文件都能读出系统，但是都可以被修改,所以这么判断系统是不保险的
				String s = FileUtils.findText("/etc/issue", "^\\s*([^\\s]*).*release","$1");
				//理想的对应行为NeoKylin Desktop release 7.0 (x86)
				if("NeoKylin".equals(s)) {
					return OsType.KYLIN;
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			return OsType.LINUX;
		}
		return OsType.UNKOWN;
	}
	
	public static boolean isWindows() {
		return osType() == OsType.WINDOWS;
	}
	public static boolean isLinux() {
		return osType() == OsType.LINUX;
	}
	public static boolean isKylin() {
		return osType() == OsType.KYLIN;
	}
	
}
