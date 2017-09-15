package com.ag777.util.file;

/**
 * 路径工具类
 * @author ag777
 *
 */
public class PathUtils {

	public static String getSrcPath() {
		//System.out.println(GetPath.class.getClassLoader().getResource(""));  
		return PathUtils.class.getResource("/").getPath().replaceAll("%20"," ");	//带空格的路径
	}
}
