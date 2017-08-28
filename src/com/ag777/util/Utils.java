package com.ag777.util;

import java.util.HashMap;
import java.util.Map;
import com.ag777.util.file.FileUtils;
import com.ag777.util.file.PropertyUtils;
import com.ag777.util.gson.GsonUtils;
import com.ag777.util.jsoup.JsoupUtils;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.interf.JsonUtilsInterf;

/**
 * @Description 工具包用的通用方法类
 * @author ag777
 * Time: created at 2017/6/6. last modify at 2017/6/6.
 * Mark: 所有定制操作最好在程序初始化时执行以确保程序能照预想的运行
 */
public class Utils {

	private static JsonUtilsInterf jsonUtil = GsonUtils.get();		//懒加载
	
	private Utils(){}
	
	/**
	 * 获取这个工具包的包名
	 * @return
	 */
	public static String getUtilsPackageName() {
		return getPageName(Utils.class);
	}
	
	//--配置
	/**
	 * 切换开发模式,非开发模式下，部分输出不会打印出来
	 * @param isDeviceMode
	 */
	public static void deviceMode(Boolean isDeviceMode) {
		Console.setDevMode(isDeviceMode);
	}
	
	public static int jsoupTimeOut() {
		return JsoupUtils.defaultTimeOut();
	}
	/**
	 * 定制jsoup连接默认的超时时间
	 * @param timeOut
	 * @return 
	 */
	public static void jsoupTimeOut(int timeOut) {
		JsoupUtils.defaultTimeOut(timeOut);
	}
	
	/**
	 * 定制文件读写编码
	 * @param encoding
	 */
	public static void FileEncoding(String encoding) {
		FileUtils.encodingRead(encoding);
		FileUtils.encodingWrite(encoding);
	}
	
	public static JsonUtilsInterf jsonUtils() {
		return jsonUtil;
	}
	/**
	 * 定制json转换工具,工具包内部的所有json转换都会变成传入的这个
	 * @param JsonUtils
	 */
	public static void jsonUtils(JsonUtilsInterf JsonUtils) {
		Utils.jsonUtil = JsonUtils;
	}

	public static String info() {
		Map<String, Object> infoMap = infoMap();
		if(infoMap != null) {
			return jsonUtils().toJson(infoMap);
		}
		return null;
	}
	
	public static Map<String, Object> infoMap() {
		PropertyUtils pu = new PropertyUtils();
		try {
			
			pu.load(Utils.class.getResourceAsStream("/config/config.properties"));
			Map<String, Object> infoMap = new HashMap<String, Object>();
			infoMap.put("last_release_date", pu.get("last_release_date"));
			return infoMap;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	/*=============内部方法==================*/
	/**
	 * 获取类对应包名
	 * @param clazz
	 * @return
	 */
	private static String getPageName(Class<?> clazz) {
		Package p = clazz.getPackage();
		if(p == null) {
			return "";
		} else {
			return p.getName();
		}
	}
	/**
	 * 获取父包路径
	 * @param clazz
	 * @return
	 */
	public static String getParentPackageName(Class<?> clazz) {
		return getParentPackageName(getPageName(clazz));
	} 
	
	/**
	 * 获取父包路径
	 * @param packageName
	 * @return
	 */
	public static String getParentPackageName(String packageName) {
		return packageName.replaceFirst("\\.[^\\.]+$", "");
	} 
	
	
}
