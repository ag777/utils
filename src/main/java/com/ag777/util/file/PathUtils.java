package com.ag777.util.file;

import com.ag777.util.lang.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Paths;

/**
 * 路径工具类
 * 
 * @author ag777
 * @version last modify at 2024年05月28日
 */
public class PathUtils {
	
	/**
	 * 获取resource文件夹下资源文件的io流
	 * @param clazz clazz
	 * @param filePath filePath
	 * @return
	 */
	public static InputStream getAsStream(String filePath, Class<?> clazz) {
		if(clazz == null) {
			clazz = PathUtils.class;
		}
		return clazz.getClassLoader().getResourceAsStream(filePath);
	}
	
	/**
	 * 获取src路径，必须传类，如果用这个类会得到lib包的路径
	 * <p>
	 * 	返回结果如:/D:/tools/programming/eclipse_neon/Utils-Java/bin/
	 * </p>
	 * 
	 * @param clazz clazz
	 * @return
	 */
	public static String srcPath(Class<?> clazz) {
		if(clazz == null) {
			clazz = PathUtils.class;
		}
		java.net.URL url = clazz.getProtectionDomain().getCodeSource().getLocation();
		String filePath = url.getPath();
		try {
			filePath = java.net.URLDecoder.decode(filePath, "utf-8");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		File file = new File(filePath);
		if(!file.isDirectory()) {	//可能是jar包，因为结果是/xxxx/xx.jar,所以我们要去得到jar包的同级路径
			return file.getParent();
		}
		return filePath;
	}
	
	/**
	 * 获取src路径中的子路径
	 * <p>
	 * 	srcPath(PathUtils.class, "config/") 得到xxx/src/config/
	 * </p>
	 * 
	 * @param clazz clazz
	 * @param subPath subPath
	 * @return
	 */
	public static String srcPath(Class<?> clazz, String subPath) {
		return StringUtils.concatFilePath(srcPath(clazz), subPath);
	}
	
	/**
	 * 获取相对路径
	 * <p>
	 * <pre>{@code
	 *  PathUtils.getRelativizePath("f:\\a\\b.txt","f:\\").toString(); => "a\\b.txt"
	 *  }</pre>
	 * 
	 * @param targetPath 目标路径(需要转化为相对路径的绝对路径)
	 * @param basePath 基础路径(标尺)
	 * @return
	 */
	public static String getRelativizePath(String targetPath, String basePath) {
		return Paths.get(basePath).relativize(Paths.get(targetPath)).toString();
	}

	/**
	 * 类是否在jar包中
	 * @param clazz 参照类，用于获取路径
	 * @return 当前代码是否处于jar包中
	 */
	public static boolean inJar(Class<?> clazz) {
		String protocol = clazz.getResource("").getProtocol();
		return "jar".equals(protocol);
	}


	/**
	 * 获取给定类的根路径。如果类是在JAR文件中，则返回运行时的根路径；如果类是在文件系统中，则返回类的代码源位置路径。
	 * @param clazz 任意类，用于确定根路径。
	 * @return 根路径的字符串表示，末尾包含文件分隔符。
	 */
	public static String getRootPath(Class<?> clazz) {
	    String path;
	    if (inJar(clazz)) {
	        // 当类在JAR中时，获取当前运行时的根路径
	        path = FileSystems.getDefault().getPath("").toAbsolutePath().toString();
	    } else {
	        // 当类在文件系统中时，获取类的代码源位置路径
	        path = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
	        path = new File(path).getAbsolutePath();
	    }
	    try {
	        // 尝试将路径URL解码为UTF-8格式
	        path = URLDecoder.decode(path, StandardCharsets.UTF_8.toString());
	    } catch (UnsupportedEncodingException ignored) {
	        // 忽略解码异常
	    }
	    // 返回解码后的路径的父路径，并在末尾添加一个文件分隔符
	    return path + File.separator;
	}

}