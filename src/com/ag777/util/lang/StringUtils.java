package com.ag777.util.lang;

import java.net.MalformedURLException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ag777.util.lang.collection.ListUtils;

/**
 * 字符串处理工具类
 * 
 * @author ag777
 * @version last modify at 2017年12月14日
 */
public class StringUtils {

	/**
	 * 获得字符串长度（一个汉字算两个字节）
	 * 
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
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(String src) {
		if(src == null || src.length() == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 判断字符串是否为null获取为空字符串(最多只含制表符 \t ('\u0009'),换行符 \n ('\u000A'),回车符 \r ('\u000D')，换页符 \f ('\u000C')以及半角/全角空格)
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isBlank(String src) {
		if(src == null || src.matches("^[\\s　]*$")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 如果字符串为null则返回空字符串
	 * 
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
	 * 
	 * @param obj 	第一个值
	 * @param objs 	后续的值
	 * @return
	 */
	public static String concat(Object obj, Object... objs) {
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
	 * 
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
	 * 
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
	 * 
	 * @param src
	 * @return
	 */
	public static String reverse(String src) {
		return new StringBuilder(src).reverse().toString();
	}
	
	/**
     * 下划线转驼峰法
     * 
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
	 * 
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
	 * 
	 * @return
	 */
	public static String uuid(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 两版本号比较判断旧版本号是否小于新版本号
	 * <p>
	 * 	支持任意多节点的版本号比较
	 * </p>
	 * 
	 * @param versionCodeOld
	 * @param versionCodeNew
	 * @return
	 */
	public static boolean isVersionBefore(String versionCodeOld, String versionCodeNew) {
		if(versionCodeOld == null || versionCodeOld.trim().isEmpty()) {	//不存在旧版本号肯定是要更新的
			return true;
		}
		if(versionCodeOld.equals(versionCodeNew)) {	//两个版本字符串一致,则说明完全不用升级
			return false;
		}
		
		//线比较日期，日期相同再比较时间
		String[] codesOld = versionCodeOld.split("\\.");
		String[] codesNew = versionCodeNew.split("\\.");
		int length_old = codesOld.length;
		int length_new = codesNew.length;
		for(int i=0;;i++) {
			if(i>=length_old) {
				if(i>=length_new) {	//新旧版本都到底了还没分出胜负则平局
					return false;
				} else {	//旧版本到底了，新版本还有下文，则需要升级
					return true;
				}
			} else if(i>=length_new) {	//旧版本还有下文，新版本到底了，说明旧版本更新(存在这种情况一版说明版本控制有问题)
				return false;
			} else {	//每级版本进行不同的pk，胜者说明对应的是新版本
				long shouldUpdate = Long.parseLong(codesNew[i]) - Long.parseLong(codesOld[i]);
				if(shouldUpdate > 0) {
					return true;
				} else if(shouldUpdate < 0) {	//旧版本号比新版本号还大，出现这种情况说明版本控制没操作好
					return false;
				}
			}
			
		}
	}
	
	/**
	 * 将url字符串转化成java.net.URL对象
	 * 
	 * @param urlStr
	 * @return
	 * @throws MalformedURLException
	 */
	public static java.net.URL toURL(String urlStr) throws MalformedURLException {
		if(urlStr.startsWith("http")) {
			urlStr = "http://"+urlStr;
		}
		java.net.URL url = new java.net.URL(urlStr);
		return url;
	}
	
	/**
	 * 字符串转Double
	 * 
	 * @param src
	 * @return
	 */
	public static Double toDouble(String src) {
		try {
			return Double.parseDouble(src);
		} catch(Exception ex) {
			//转换失败
		}
		return null;
	}
	
	/**
	 * 字符串转Float
	 * 
	 * @param src
	 * @return
	 */
	public static Float toFloat(String src) {
		try {
			return Float.parseFloat(src);
		} catch(Exception ex) {
			//转换失败
		}
		return null;
	}
	
	/**
	 * 字符串转Integer
	 * 
	 * @param src
	 * @return
	 */
	public static Integer toInteger(String src) {
		try {
			return Integer.parseInt(src);
		} catch(Exception ex) {
			//转换失败
		}
		return null;
	}
	
	/**
	 * 字符串转Long
	 * @param src
	 * @return
	 */
	public static Long toLong(String src) {
		try {
			return Long.parseLong(src);
		} catch(Exception ex) {
			//转换失败
		}
		return null;
	}
	
	/**
	 * 字符串转Boolean
	 * <p>
	 * 	当字符串为"true"或者"1"时返回true
	 * 	当字符串为"false"或者"0"时返回flase
	 * 	其余情况返回null
	 * </p>
	 * 
	 * @param src
	 * @return
	 */
	public static Boolean toBoolean(String src) {
		if(src != null) {
			if("true".equals(src) || "1".equals(src)) {
				return true;
			} else if("false".equals(src) || "0".equals(src)) {
				return false;
			}
		}
		
		return null;
	}
	
	/**
	 * 字符串转java.util.Date
	 * <p>
	 * 	支持四种格式
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd HH:mm
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * </p>
	 * 
	 * @param src
	 * @return
	 */
	public static java.util.Date toDate(String src) {
		if(src.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}")) {
			return DateUtils.toDate(src, "yyyy-MM-dd HH:mm:ss");
		} else if(src.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}")) {
			return DateUtils.toDate(src, "yyyy-MM-dd HH:mm");
		} else if(src.matches("\\d{4}-\\d{2}-\\d{2}")) {
			return DateUtils.toDate(src, "yyyy-MM-dd");
		} else if(src.matches("\\d{2}:\\d{2}:\\d{2}")) {
			return DateUtils.toDate(src, "HH:mm:ss");
		}
		return null;
	}
	
	/**
	 * 源字符串换行转list
	 * <p>
	 * 	对应的拆分正则为(\r)?\n
	 * </p>
	 * @param src
	 * @return
	 */
	public static List<String> toLineList(String src) {
		return ListUtils.ofList(src, "(\r)?\n");
	}
	
	//--转义相关
	/**
	 * 转义xml
	 * 
	 * <p>
	 * 	外层嵌套CDATA法
	 * </p>
	 * 
	 * @param src
	 * @return
	 */
	public static String escapeXmlByCDATA(String src) {
		return concat("<![CDATA[ ", src, " ]]>");
	}
	
	/**
	 * 转义xml
	 * 
	 * <p>
	 * 	替换法:
	 * &->&amp;
	 * <->&lt;
	 * >->&gt;
	 * '->&apos;
	 * "->&quot;
	 * </p>
	 * 
	 * @param src
	 * @return
	 */
	public static String escapeXmlByReplace(String src) {
		return src.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;");
	}
	
	//--emoji表情相关
	/**
	 * 过滤字符串中的emoji表情为目标字符串
	 * <p>
	 * 最直观的作用就是防止插入数据库时报错
	 * </p>
	 * @param src
	 * @param replacement
	 * @return
	 */
	public static String clearEmoji(String src) {
		return EmojiUtils.clearEmoji(src);
	}

}
