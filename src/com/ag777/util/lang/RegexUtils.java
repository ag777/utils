package com.ag777.util.lang;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.ag777.util.lang.collection.CollectionAndMapUtils;
import com.ag777.util.lang.collection.ListUtils;

/**
 * 正则表达式工具类
 * <p>
 * 		2017年09月07日 增加对pattern支持
 * </p>
 * 
 * @author ag777
 * @version create on 2017年06月06日,last modify at 2017年01月22日
 */
public class RegexUtils {

	
	public static Pattern getPatternWithException(String regex) throws PatternSyntaxException{
		return Pattern.compile(regex);
	}
	
	public static Pattern getPatternWithException(String regex, int flags) {
		return Pattern.compile(regex, flags);
	}
	
	/**
	 * 字符串是否匹配正则,多做了一步非空判断
	 * 
	 * @param src
	 * @param regex
	 * @return
	 */
	public static boolean match(String src, String regex) {
		if(src != null) {
			return src.matches(regex);
		}
		return false;
	}
	
	/**
	 * 替换
	 * 
	 * @param src
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String src, String regex, String replacement) {
		if(src == null) {
			return src;
		}
		return replaceAll(src, Pattern.compile(regex), replacement);
	}
	
	/**
	 * 替换
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String replaceAll(String src, Pattern pattern, String replacement) {
		if(src == null) {
			return src;
		}
		return pattern.matcher(src).replaceAll(replacement);
	}
	
	/**
	 * 统计出现次数
	 * 
	 * @param src
	 * @param regex
	 * @return
	 */
	public static long count(String src, String regex) {
		return count(src, getPattern(regex));
	}
	
	/**
	 * 统计出现次数
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static long count(String src, Pattern pattern) {
		long count = 0;
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			count++;
		}
		return count;
	}
	
	/**
	 * 通过源字符串和正则获取Matcher,之后通过遍历就可以为所欲为(划掉)
	 * <p>
	 * 	例子
	 * 	<p>
	 * 		String a = "aacaa";
			Matcher matcher = RegexUtils.getMatcher(a, "a([^a]+?)a");
			while(matcher.find()) {
				System.out.println(matcher.group(1));
			}
			结果为"c"
	 * 	</p>
	 * </p>
	 */
	public static Matcher getMatcher(String src, String regex) {
		return getMatcher(src, getPattern(regex));
	}
	
	/**
	 * 通过源字符串和正则获取Matcher,之后通过遍历就可以为所欲为(划掉)
	 * <p>
	 * 		见getMatcher(String src, String regex)方法的说明
	 * </p>
	 */
	public static Matcher getMatcher(String src, Pattern pattern) {
		return pattern.matcher(src);
	}
	
	
	//查找单个不带替换式
	/**
	 * 从字符串中找到第一个匹配的字符串
	 * 
	 * @param src
	 * @param regex
	 * @return
	 */
	public static String find(String src, String regex) {
		return find(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中找到第一个匹配的字符串
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static String find(String src, Pattern pattern) {
		Matcher matcher = getMatcher(src, pattern);
		if(matcher.find()) {
			return matcher.group();
		}
		return null;
	}
	
	/**
	 * 查询符合条件的第一组数据
	 * @param src
	 * @param regex
	 * @return
	 */
	public static List<String> findGroups(String src, String regex) {
		return findGroups(src, getPattern(regex));
	}
	
	/**
	 * 查询符合条件的第一组数据
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<String> findGroups(String src, Pattern pattern) {
		List<String> list = ListUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		
		while(matcher.find()) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String item = matcher.group(i);
				list.add(item);
			}	
		}
		
		return list;
	}
	
	/**
	 * 查询符合条件的所有数据
	 * @param src
	 * @param regex
	 * @return
	 */
	public static List<List<String>> findAllGroups(String src, String regex) {
		return findAllGroups(src, getPattern(regex));
	}
	
	/**
	 * 查询符合条件的所有数据
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<List<String>> findAllGroups(String src, Pattern pattern) {
		List<List<String>> list = ListUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		
		while(matcher.find()) {
			List<String> itemList = ListUtils.newArrayList();
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String item = matcher.group(i);
				itemList.add(item);
			}	
			list.add(itemList);
		}
		
		return list;
	}
	
	/**
	 * 从字符串中找到第一个匹配的字符串并转为Long型
	 * 
	 * @param src
	 * @param regex
	 * @return
	 */
	public static Long findLong(String src, String regex) {
		return findLong(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中找到第一个匹配的字符串并转为Long型
	 * 
	 * @param src
	 * @param regex
	 * @return
	 */
	public static Long findLong(String src, Pattern pattern) {
		Matcher matcher = getMatcher(src, pattern);
		if(matcher.find()) {
			return ObjectUtils.toLong(matcher.group());
		}
		return null;
	}
	
	//查找所有不带替换式
	/**
	 * 从字符串中查找所有正则匹配的字符串列表
	 * 
	 * @param src 源字符串
	 * @param regex	正则
	 * @return
	 */
	public static List<String> findAll(String src, String regex) {
		return findAll(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中查找所有正则匹配的字符串列表
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<String> findAll(String src, Pattern pattern) {
		List<String> list = CollectionAndMapUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			list.add(matcher.group());
		}
		return list;
	}
	
	/**
	 * 从字符串中查找所有正则匹配的int型数字列表
	 * 
	 * @param src 源字符串
	 * @param regex	正则
	 * @return
	 */
	public static List<Integer> findAllInt(String src, String regex) {
		return findAllInt(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中查找所有正则匹配的int型数字列表
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<Integer> findAllInt(String src, Pattern pattern) {
		List<Integer> list = CollectionAndMapUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			Integer item = ObjectUtils.toInteger(matcher.group());	//转为数字，非数字不计入结果
			if(item != null) {
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * 从字符串中查找所有正则匹配的long型数字列表
	 * 
	 * @param src 源字符串
	 * @param regex	正则
	 * @return
	 */
	public static List<Long> findAllLong(String src, String regex) {
		return findAllLong(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中查找所有正则匹配的long型数字列表
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<Long> findAllLong(String src, Pattern pattern) {
		List<Long> list = CollectionAndMapUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			Long item = ObjectUtils.toLong(matcher.group());	//转为数字，非数字不计入结果
			if(item != null) {
				list.add(item);
			}
			
		}
		return list;
	}
	
	/**
	 * 从字符串中查找所有正则匹配的double型数字列表
	 * 
	 * @param src 源字符串
	 * @param regex	正则
	 * @return
	 */
	public static List<Double> findAllDouble(String src, String regex) {
		return findAllDouble(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中查找所有正则匹配的double型数字列表
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<Double> findAllDouble(String src, Pattern pattern) {
		List<Double> list = CollectionAndMapUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			Double item = ObjectUtils.toDouble(matcher.group());	//转为数字，非数字不计入结果
			if(item != null) {
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * 从字符串中查找所有正则匹配的boolean型数字列表
	 * 
	 * @param src 源字符串
	 * @param regex	正则
	 * @return
	 */
	public static List<Boolean> findAllBoolean(String src, String regex) {
		return findAllBoolean(src, getPattern(regex));
	}
	
	/**
	 * 从字符串中查找所有正则匹配的boolean型数字列表
	 * 
	 * @param src
	 * @param pattern
	 * @return
	 */
	public static List<Boolean> findAllBoolean(String src, Pattern pattern) {
		List<Boolean> list = CollectionAndMapUtils.newArrayList();
		Matcher matcher = getMatcher(src, pattern);
		while(matcher.find()) {
			Boolean item = ObjectUtils.toBoolean(matcher.group());	//转为数字，非数字不计入结果
			if(item != null) {
				list.add(item);
			}
			
		}
		return list;
	}
	
	
	
	//--查找单个带正则替换
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(借鉴某爬虫app的github开源代码，这是真心好用)
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static String find(String src, String regex, String replacement) {
		return find(src, getPattern(regex), replacement);
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(借鉴某爬虫app的github开源代码，这是真心好用)
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String find(String src, Pattern pattern, String replacement) {
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			if (!matcher.find()) {	//没有匹配到则返回null
				return null;
			} else {
				return getReplacement(matcher, replacement);
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回源字符串
			return src;
		}
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * <p>
	 * 若获取值为null则返回默认值
	 * </p>
	 * 
	 * @param src
	 * @param regex
	 * @param replacement
	 * @param defaultValue 默认值
	 * @return
	 */
	public static String find(String src, String regex, String replacement, String defaultValue) {
		String result = find(src, regex, replacement);
		if(result == null) {
			return defaultValue;
		}
		return result;
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * <p>
	 * 若获取值为null则返回默认值
	 * </p>
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @param defaultValue
	 * @return
	 */
	public static String find(String src, Pattern pattern, String replacement, String defaultValue) {
		String result = find(src, pattern, replacement);
		if(result == null) {
			return defaultValue;
		}
		return result;
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static Integer findInteger(String src, String regex, String replacement) {
		return findInteger(src, getPattern(regex), replacement);
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static Integer findInteger(String src, Pattern pattern, String replacement) {
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			if (!matcher.find()) {	//没有匹配到则返回null

			} else if (matcher.groupCount() >= 1) {
				return ObjectUtils.toInteger(getReplacement(matcher, replacement));
			}

		} else {	//如果源字符串为null或者正则表达式为null，返回null
			return null;
		}
		return null;
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static Long findLong(String src, String regex, String replacement) {
		return findLong(src, getPattern(regex), replacement);
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static Long findLong(String src, Pattern pattern, String replacement) {
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			if (!matcher.find()) {	//没有匹配到则返回null

			} else if (matcher.groupCount() >= 1) {
				return ObjectUtils.toLong(getReplacement(matcher, replacement));
			}

		} else {	//如果源字符串为null或者正则表达式为null，返回null
			return null;
		}
		return null;
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static Double findDouble(String src, String regex, String replacement) {
		return findDouble(src, getPattern(regex), replacement);
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static Double findDouble(String src, Pattern pattern, String replacement) {
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			if (!matcher.find()) {	//没有匹配到则返回null

			} else if (matcher.groupCount() >= 1) {
				return ObjectUtils.toDouble(getReplacement(matcher, replacement));
			}

		} else {	//如果源字符串为null或者正则表达式为null，返回null
			return null;
		}
		return null;
	}
	
	
	//--查找所有带正则替换
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(列表)
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static List<String> findAll(String src, String regex, String replacement) {
		return findAll(src, getPattern(regex), replacement);
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(列表)
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static List<String> findAll(String src, Pattern pattern, String replacement) {
		List<String> result = CollectionAndMapUtils.newArrayList();
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			while(matcher.find()) {
				result.add(
						getReplacement(matcher, replacement));
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回空列表
			return result;
		}
		return result;
	}
	
	/**
	 * 根据正则和替换表达式提取字符串中有用的部分以期望的格式返回(列表)
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static List<Integer> findAllInteger(String src, String regex, String replacement) {
		return findAllInteger(src, getPattern(regex), replacement);
	}
	
	/**
	 * 查找字符串中所有匹配的内容，并转化为Integer型
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static List<Integer> findAllInteger(String src, Pattern pattern, String replacement) {
		List<Integer> result = CollectionAndMapUtils.newArrayList();
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			while(matcher.find()) {
				Integer item = ObjectUtils.toInteger(getReplacement(matcher, replacement));
				result.add(item);
				if(item != null) {
					result.add(item);
				}
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回空列表
			return result;
		}
		return result;
	}
	
	
	/**
	 * 查找字符串中所有匹配的内容，并转化为Long型
	 * 
	 * @param src 源字符串
	 * @param regex	匹配用的正则表达式
	 * @param replacement	提取拼接预期结果的格式,如'$1-$2-$3 $4:$5'
	 * @return
	 */
	public static List<Long> findAllLong(String src, String regex, String replacement) {
		return findAllLong(src, getPattern(regex), replacement);
	}
	
	/**
	 * 查找字符串中所有匹配的内容，并转化为Long型
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static List<Long> findAllLong(String src, Pattern pattern, String replacement) {
		List<Long> result = CollectionAndMapUtils.newArrayList();
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			while(matcher.find()) {
				Long item = ObjectUtils.toLong(getReplacement(matcher, replacement));
				result.add(item);
				if(item != null) {
					result.add(item);
				}
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回空列表
			return result;
		}
		return result;
	}
	
	/**
	 * 查找字符串中所有匹配的内容，并转化为Double型
	 * 
	 * @param src
	 * @param regex
	 * @param replacement
	 * @return
	 */
	public static List<Double> findAllDouble(String src, String regex, String replacement) {
		return findAllDouble(src, getPattern(regex), replacement);
	}
	
	/**
	 * 查找字符串中所有匹配的内容，并转化为Double型
	 * 
	 * @param src
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static List<Double> findAllDouble(String src, Pattern pattern, String replacement) {
		List<Double> result = CollectionAndMapUtils.newArrayList();
		if(src != null && pattern != null) {
			Matcher matcher = getMatcher(src, pattern);

			while(matcher.find()) {
				Double item = ObjectUtils.toDouble(getReplacement(matcher, replacement));
				if(item != null) {
					result.add(item);
				}
			}
		} else {	//如果元字符串为null或者正则表达式为null，返回空列表
			return result;
		}
		return result;
	}
	
	/*--------------内部方法----------------*/
	private static Pattern getPattern(String regex) {
		return Pattern.compile(regex);
	}
	
	private static String getReplacement(Matcher matcher, String replacement) {
		String temp = replacement;
		if (replacement != null) {
			for (int i = 1; i <= matcher.groupCount(); i++) {
				String replace = matcher.group(i);
				temp =  temp.replace("$" + i, (replace != null) ? replace : "");
			}
			return temp;
		} else {
			return matcher.group(0);
		}
	}
}
