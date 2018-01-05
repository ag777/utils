package com.ag777.util.file;

import java.io.File;
import java.io.InputStream;

import com.ag777.util.lang.StringUtils;

/**
 * 路径工具类
 * 
 * @author ag777
 * @version last modify at 2018年01月05日
 */
public class PathUtils {
	
	/**
	 * 获取resource文件夹下资源文件的io流
	 * @param clazz
	 * @param filePath
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
	 * @param clazz
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
	 * @param clazz
	 * @param subPath
	 * @return
	 */
	public static String srcPath(Class<?> clazz, String subPath) {
		return StringUtils.concatFilePath(srcPath(clazz), subPath);
	}
}
