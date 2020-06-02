package com.ag777.util.lang.model;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * 存放编码的类
 * 
 * @author 
 * @version create on 2017年11月03日,last modify at 2018年04月13日
 */
public class Charsets {

	private Charsets() {}
	
	/** 7位ASCII字符，也叫作ISO646-US、Unicode字符集的基本拉丁块 */
	public static final Charset US_ASCII = StandardCharsets.US_ASCII;

	/** ISO 拉丁字母表 No.1，也叫作 ISO-LATIN-1 */
	public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

	/** 8 位 UCS 转换格式 */
	public static final Charset UTF_8 = StandardCharsets.UTF_8;

	/** 16 位 UCS 转换格式，Big Endian（最低地址存放高位字节）字节顺序 */
	public static final Charset UTF_16BE = StandardCharsets.UTF_16BE;

	/** 16 位 UCS 转换格式，Little-endian（最高地址存放低位字节）字节顺序 */
	public static final Charset UTF_16LE = StandardCharsets.UTF_16LE;

	/** 16 位 UCS 转换格式，字节顺序由可选的字节顺序标记来标识 */
	public static final Charset UTF_16 = StandardCharsets.UTF_16;

	/** 中文超大字符集 */
	public static final Charset GBK = of("GBK");
	
	public static Charset of(String encoding) {
		return Charset.forName(encoding);
	}
	
	
}
