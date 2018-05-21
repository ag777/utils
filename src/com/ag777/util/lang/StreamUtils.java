package com.ag777.util.lang;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 有关<code>Stream</code>操作工具类
 * <p>
 * jdk8及以上
 * </p>
 * 
 * @author ag777
 * @version create on 2018年05月17日,last modify at 2018年05月17日
 */
public class StreamUtils {

	private StreamUtils() {}
	
	public static <T>List<T> toList(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}
}
