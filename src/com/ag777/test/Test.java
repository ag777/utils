package com.ag777.test;

import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ag777.util.file.IniHelper;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.MapUtils;

public class Test {

	public static void main(String[] args) throws IOException {
//		String result = RegexUtils.find("wtmsb", "^([^=]+)=([^=]+)$", "$1=$2");
//		System.out.println(result);
//		Console.log("a=".split("="));
//		IniUtils iu = new IniUtils("e:\\converter_config.ini");
//		.section("bean").value("path.file.out"));
//		System.out.println(iu.getValue("bean", "path.file.out").get());
//		String line = "path.template.in=F:\\temp\\临时";
//		System.out.println(find(line, Pattern.compile("^([^=]+)=([^=]*)$"), "$1=$2"));
//		String a = "\\";
//		String b = "$1";
//		b = b.replaceAll("\\$1", a);
//		System.out.println(b);
//		Map<String, Object> map = MapUtils.of("a", 1);
//		map.remove("b");
//		String a = "\t\t\t";
//		System.out.println(a.replaceAll("(\\t*)\\t", "$1"));
//		String a = "   \r\n a\r\nb\r\n\r\n";
//		System.out.println(a.replaceAll("^\\s+?\\r?\\n", "").replaceAll("\\s+?\\r?\\n$", ""));
		
//		Console.log("a".split("\\.",2));
		String a = "a\r\nbb\nc\rs";
		Console.log(StringUtils.toLineList(a));
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
			Matcher matcher = pattern.matcher(src);

			if (!matcher.find()) {	//没有匹配到则返回null
				return null;
			} else {
				return getReplacement(matcher, replacement);
			}

		} else {	//如果元字符串为null或者正则表达式为null，返回源字符串
			return src;
		}
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
