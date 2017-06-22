package com.ag777.util.lang;

import java.util.UUID;

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
	 * 字符串是否为null或者长度为0或者都是空格
	 * @param src
	 * @return
	 */
	public static boolean isNullOrEmpty(String src) {
		if(src == null || src.isEmpty() || src.matches("^\\s*$")) {
			return true;
		}
		return false;
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
	 * 生成随机的uuid
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
}
