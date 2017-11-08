package com.ag777.util.file;

import java.io.File;
import java.io.InputStream;

import com.ag777.util.lang.SystemUtils;

/**
 * 路径工具类
 * 
 * @author ag777
 * @version last modify at 2017年11月08日
 */
public class PathUtils {
	
	public static InputStream getAsStream(String filePath) {
		return PathUtils.class.getClassLoader().getResourceAsStream(filePath);
	}
	
	public static String srcPath() {
		java.net.URL url = PathUtils.class.getProtectionDomain().getCodeSource().getLocation();
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
	public static void main(String[] args) {
		System.out.println(srcPath());
		System.out.println(new File("/e:/ccc/").isDirectory());
	}
}
