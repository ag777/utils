package com.ag777.util.lang;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ag777.util.lang.collection.ListUtils;

/**
 * 字符串处理工具类
 * 
 * @author ag777
 * @version last modify at 2020年08月20日
 */
public class StringUtils {

	private static final Pattern PATTERN_EMPTY = Pattern.compile("^[\\u00A0\\s　]*$");	//空值验证(\\u00A0为ASCII值为160的空格)
	private static final Pattern PATTERN_EMPTY_LEFT = Pattern.compile("^[\\u00A0\\s　]+");	//空值验证(\\u00A0为ASCII值为160的空格)
	private static final Pattern PATTERN_EMPTY_RIGHT = Pattern.compile("[\\u00A0\\s　]+$");	//空值验证(\\u00A0为ASCII值为160的空格)
	
	/**
	 * 获得字符串长度(utf-8)
	 * @see #getLength(String, Charset)
	 * 
	 * @param src 源字符串
	 * @return
	 */
	public static int getLength(String src) {
//		src = src.replaceAll("[^\\x00-\\xff]", "**");
//		int length = src.length();
		return getLength(src, StandardCharsets.UTF_8);
	}
	
	/**
	 * 获得字符串长度<br>
	 * 实现原理:String.getBytes(charset).length
	 * @param src
	 * @param charset
	 * @return
	 */
	public static int getLength(String src, Charset charset) {
		if(isEmpty(src)) {
			return 0;
		}
		return src.getBytes(charset).length;
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
		if(src == null || PATTERN_EMPTY.matcher(src).matches()) {
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
	public static String emptyIfNull(Object src) {
		if(src == null) {
			return "";
		}
		return src.toString();
	}
	
	/**
	 * 去除首尾空格
	 * <p>
	 * java原生的trim有不完善的地方,无法去除特定的空格
	 * 该实现为正则"[\\u00A0\\s　]+"首尾匹配出的空格替换为空字符串
	 * </p>
	 * @param src
	 * @return
	 */
	public static String trim(String src) {
		if(src == null) {
			return null;
		}
		src = PATTERN_EMPTY_LEFT.matcher(src).replaceFirst("");
		return PATTERN_EMPTY_RIGHT.matcher(src).replaceAll("");
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
				if(item !=null) {
					sb.append(item);
				}
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
		if(isEmpty(src)) {
			return src;
		}
		StringBuilder sb = new StringBuilder(src);
		return sb.replace(start, end, newStr).toString();
	}
	
	/**
	 * 将一个字符串拆分成每一项定长为length(最后一项不定)的字符串列表<br>
	 * <p><pre>{@code
	 * splitStr("abc", 2)=>["ab","c"]
	 * splitStr("abc",4)=>["abc"]
	 * }</pre>
	 * 
	 * @param src
	 * @param length
	 * @return
	 */
	public static List<String> splitStr(String src, int length) {
		if(isEmpty(src)) {
			return ListUtils.newArrayList();
		}
		if(length<=0) {
			return ListUtils.of(src);
		}
		List<String> list = ListUtils.newArrayList();
		int max = src.length();
		for (int start = 0; start < max;) {
			int end = start+length;
			if(end > max) {
				end = max;
			}
			list.add(src.substring(start, end));
			start = end;
		}
		return list;
	}
	
	/**
	 * 拼接文件路径(避免重复的分隔符)
	 * @param filePath		基础路径
	 * @param extraPaths	额外路径
	 * @return
	 */
	public static String concatFilePath(String filePath, Object... extraPaths) {
		return concatFilePathBySeparator(File.separator, filePath, extraPaths);
	}
	
	/**
	 * 拼接文件路径(该方法会在每两个路径中插入fileSeparator, 替换原有的路径分隔符, 并避免重复的分隔符)
	 * @param fileSeparator	系统的路径分隔符,win传"\\",linux传"/"
	 * @param filePath			基础路径
	 * @param extraPaths		额外路径
	 * @return
	 */
	public static String concatFilePathBySeparator(String fileSeparator, String filePath, Object... extraPaths) {
		StringBuilder sb = new StringBuilder(filePath).append(fileSeparator);

		if(extraPaths != null) {
			for (Object item : extraPaths) {
				sb.append(fileSeparator);
				if(item != null) {
					
					sb.append(item);
				}
			}
			
		}
		if("\\".equals(fileSeparator)) {	//需要转义符
			return sb.toString().replaceAll("[\\\\/]+", "\\\\");
		}
		return sb.toString().replaceAll("[\\\\/]+", fileSeparator);
	}
	
	/**
	 * 利用StringBuilder倒置字符串
	 * 
	 * @param src
	 * @return
	 */
	public static String reverse(String src) {
		if(isEmpty(src)) {
			return src;
		}
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
		if (isEmpty(line)) {
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
	 * @param src 源字符串
	 * @return
	 */
	public static String upperCaseFirst(String src) {  
		if(isEmpty(src)) {
			return src;
		}
	    char[] ch = src.toCharArray();  
	    if (ch[0] >= 'a' && ch[0] <= 'z') {  
	        ch[0] = (char) (ch[0] - 32);  
	    }  
	    return new String(ch);  
	}
	
	/**
	 * 格式化数字,保留decimalPlaces位小数
	 * 
	 * @param num
	 * @param decimalPlaces 保留小数位数
	 * @return
	 */
	public static String formatNum(double num, int decimalPlaces) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(num);
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
		if(versionCodeNew == null) {	//不存在新版本肯定也升级不了
			return false;
		}
		if(versionCodeOld.equals(versionCodeNew)) {	//两个版本字符串一致,则说明完全不用升级
			return false;
		}
		
		//逐级比较
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
		if(urlStr == null) {
			return null;
		}
		if(!urlStr.startsWith("http")) {
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
	public static Integer toInt(String src) {
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
	 * @param src 源字符串
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
	 * 字符串转Boolean
	 * @param src 源字符串
	 * @param defaultValue 默认值
	 * @return
	 */
	public static boolean toBoolean(String src, boolean defaultValue) {
		return ObjectUtils.getOrDefault(toBoolean(src), defaultValue);
	}
	
	/**
	 * 字符串转java.util.Date
	 * <p>
	 * 	支持六种格式
	 *	yyyy-MM-dd HH:mm:ss
	 *	yyyy-MM-dd HH:mm
	 * 	yyyy-MM-dd
	 * 	HH:mm:ss
	 * 13位数字
	 * 10位数字(不包含毫秒数，很多api都这么存，为了不超过Integer类型的最大值2147483647)
	 * 
	 * 值得注意的是:
	 * 	根据函数System.out.println(
				DateUtils.toString(Integer.MAX_VALUE*1000l, DateUtils.DEFAULT_TEMPLATE_TIME));
				计算得出所有用int类型接收10位时间戳的程序都将在2038-01-19 11:14:07后报错,尽量用long型接收
	 * </p>
	 * 
	 * @param src
	 * @return
	 */
	public static java.util.Date toDate(String src) {
		if(isEmpty(src)) {
			return null;
		}
		if(src.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}")) {	//yyyy-MM-dd HH:mm:ss
			return DateUtils.toDate(src, "yyyy-MM-dd HH:mm:ss");
		} else if(src.matches("\\d{4}-\\d{2}-\\d{2}")) {		//yyyy-MM-dd
			return DateUtils.toDate(src, "yyyy-MM-dd");
		} else if(src.matches("\\d{13}")) {		//13位标准时间戳
			return new Date(toLong(src));
		} else if(src.matches("\\d{10}")) {		//10位标准时间戳
			return new Date(toLong(src)*1000);
		} else if(src.matches("\\d{2}:\\d{2}:\\d{2}")) {	//HH:mm:ss
			return DateUtils.toDate(src, "HH:mm:ss");
		} else if(src.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}")) {	//yyyy-MM-dd HH:mm
			return DateUtils.toDate(src, "yyyy-MM-dd HH:mm");
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
		if(isEmpty(src)) {
			return ListUtils.newArrayList();
		}
		return ListUtils.ofList(src, "(\r)?\n");
	}
	
	/**
	 * 汉字转Unicode字符串
	 * @param src
	 * @return
	 */
	public static String toUnicode(String src) {
		if(src == null) {
			return null;
		}
        StringBuffer unicode = new StringBuffer();
        for (int i = 0; i < src.length(); i++) {
            char c = src.charAt(i);  // 取出每一个字符
            unicode.append("\\u").append(Integer.toHexString(c));// 转换为unicode
        }
        return unicode.toString();
    }
	
	/**
	 * unicode字符串转汉字
	 * <p>
	 * 通过\\\\u分隔
	 * </p>
	 * 
	 * @param unicode
	 * @return
	 */
	public static String unicode2String(String unicode) {
		if(unicode == null) {
			return null;
		}
    	/* 以 \ u 分割，因为java注释也能识别unicode，因此中间加了一个空格*/
        String[] strs = unicode.split("\\\\u");
        String returnStr = "";
        // 由于unicode字符串以 \ u 开头，因此分割出的第一个字符是""。
        for (int i = 1; i < strs.length; i++) {
          returnStr += (char) Integer.valueOf(strs[i], 16).intValue();
        }
        return returnStr;
    }
	
	/**
	 * 将字符串a重复times次
	 * <p><pre>{@code
	 * 	比如stack("0", 3)=>"000"
	 * 函数命名参考游戏minecraft创世神插件的函数名
	 * }</pre>
	 * 
	 * @param src
	 * @param times
	 * @return
	 */
	public static String stack(String src, int times) {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<times;i++) {
			sb.append(src);
		}
		return sb.toString();
	}
	
	//--转义相关
	/**
	 * 16进制转2进制
	 * <p>
	 * 如果源数据以0x开头会先去掉0x在做处理<br>
	 * <pre>{@code
	 * decimal2Binary("AA");=>"10101010"
	 * decimal2Binary("0xAA");=>"10101010"
	 * }</pre>
	 * 
	 * @param src
	 * @return
	 */
	public static String decimal2Binary(String src) {
		if(src == null) {
			return null;
		}
		if(src.startsWith("0x")) {
			src = src.substring(2, src.length());
		}
		StringBuilder sb = new StringBuilder();
		int length = src.length();
		src = src.toUpperCase();
		for (int i = 0; i < length; i++) {
			switch(src.charAt(i)) {
				case '0':
					sb.append("0000");
					break;
				case '1':
					sb.append("0001");
					break;
				case '2':
					sb.append("0010");
					break;
				case '3':
					sb.append("0011");
					break;
				case '4':
					sb.append("0100");
					break;
				case '5':
					sb.append("0101");
					break;
				case '6':
					sb.append("0110");
					break;
				case '7':
					sb.append("0111");
					break;
				case '8':
					sb.append("1000");
					break;
				case '9':
					sb.append("1001");
					break;
				case 'A':
					sb.append("1010");
					break;
				case 'B':
					sb.append("1011");
					break;
				case 'C':
					sb.append("1100");
					break;
				case 'D':
					sb.append("1101");
					break;
				case 'E':
					sb.append("1110");
					break;
				case 'F':
					sb.append("1111");
					break;
				default:
					throw new RuntimeException("含有非法字符:"+src.charAt(i)+",位置:"+i);
			}
		}
		return sb.toString();
	}
	
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
		src = emptyIfNull(src);
		return concat("<![CDATA[ ", src, " ]]>");
	}
	
	/**
	 * 转义xml
	 * 
	 * <p>
	 * 	替换法:
	 * <pre>{@code
	 * &->&amp;
	 * <->&lt;
	 * >->&gt;
	 * '->&apos;
	 * "->&quot;
	 * }</pre>
	 * 
	 * @param src
	 * @return
	 */
	public static String escapeXmlByReplace(String src) {
		if(isEmpty(src)) {
			return src;
		}
		return src.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("'", "&apos;").replace("\"", "&quot;");
	}
	
	//--emoji表情相关
	/**
	 * 过滤字符串中的emoji表情为目标字符串
	 * <p>
	 * 最直观的作用就是防止插入数据库时报错
	 * </p>
	 * @param src 源字符串
	 * @return
	 */
	public static String clearEmoji(String src) {
		if(isEmpty(src)) {
			return src;
		}
		return EmojiUtils.clearEmoji(src);
	}

}
