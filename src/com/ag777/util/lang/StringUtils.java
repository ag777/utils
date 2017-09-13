package com.ag777.util.lang;

import java.net.MalformedURLException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author ag777
 * @Description 字符串处理工具类
 * Time: last modify at 2017/9/13.
 * Mark:
 */
public class StringUtils {

	/**
	 * 获得字符串长度（一个汉字算两个字节）
	 * @param s
	 * @return
	 */
	public static int getLength(String s) {
		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}
	
	/**
	 * 字符串是否为null或者长度为0
	 * @param src
	 * @return
	 */
	public static boolean isNullOrEmpty(String src) {
		if(src == null || src.length() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为null获取为空字符串(最多只含制表符 \t ('\u0009'),换行符 \n ('\u000A'),回车符 \r ('\u000D')，换页符 \f ('\u000C')以及半角/全角空格)
	 * @param src
	 * @return
	 */
	public static boolean isNullOrBlank(String src) {
		if(src == null || src.matches("^[\\s　]*$")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 如果字符串为null则返回空字符串
	 * @param src
	 * @return
	 */
	public static String emptyIfNull(String src) {
		if(src == null) {
			return "";
		}
		return src;
	}
	
	/**
	 * 通过StringBuilder连接字符串
	 * @param obj 	第一个值
	 * @param objs 	后续的值
	 * @return
	 */
	public String concat(Object obj, Object... objs) {
		StringBuilder sb = new StringBuilder();
		sb.append(obj);
		if(objs != null) {
			for (Object item : objs) {
				sb.append(item);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 利用StringBuilder替换起止位置的字符串
	 * @param src
	 * @param start
	 * @param end
	 * @param newStr
	 * @return
	 */
	public static String replace(String src, int start, int end, String newStr) {
		StringBuilder sb = new StringBuilder(src);
		return sb.replace(start, end, newStr).toString();
	}
	
	/**
	 * StringBuilder拼接字符串
	 * @param objs
	 * @return
	 */
	public static StringBuilder combine(Object... objs) {
		StringBuilder sb = new StringBuilder();
		if(objs != null) {
			for (Object obj : objs) {
				if(obj != null) {
					sb.append(obj);
				}
				
			}
		}
		return sb;
	}
	
	/**
	 * 利用StringBuilder倒置字符串
	 * @param src
	 * @return
	 */
	public static String reverse(String src) {
		return new StringBuilder(src).reverse().toString();
	}
	
	/**
     * 下划线转驼峰法
     * @param line 源字符串
     * @param smallCamel 大小驼峰,是否为小驼峰（如果为false则首字母大写）
     * @return 转换后的字符串
     */
	public static String underline2Camel(String line, boolean smallCamel) {
		if (line == null || "".equals(line)) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		Pattern pattern = Pattern.compile("([A-Za-z\\d]+)(_)?");
		Matcher matcher = pattern.matcher(line);
		while (matcher.find()) {
			String word = matcher.group();
			sb.append(smallCamel && matcher.start() == 0 ? Character.toLowerCase(word.charAt(0))
					: Character.toUpperCase(word.charAt(0)));
			int index = word.lastIndexOf('_');
			if (index > 0) {
				sb.append(word.substring(1, index).toLowerCase());
			} else {
				sb.append(word.substring(1).toLowerCase());
			}
		}
		return sb.toString();
	}
	
	
	/**
	 * 首字母大写
	 * @param str
	 * @return
	 */
	public static String upperCaseFirst(String str) {  
	    char[] ch = str.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	}
	
	/**
	 * 生成随机的uuid
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 将url字符串转化成java.net.URL对象
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 */
	public java.net.URL toURL(String urlStr) throws MalformedURLException {
		if(urlStr.startsWith("http")) {
			urlStr = "http://"+urlStr;
		}
		java.net.URL url = new java.net.URL(urlStr);
		return url;
	}
}
