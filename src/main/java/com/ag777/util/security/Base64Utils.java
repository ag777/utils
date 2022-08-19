package com.ag777.util.security;

import java.nio.charset.Charset;
import java.util.Base64;

/**
 * 有关<code>Base64</code>处理工具类
 * <p>
 * 使用原生jdk实现
 * </p>
 * 
 * @author ag777
 * @version create on 2018年05月08日,last modify at 2018年08月19日
 */
public class Base64Utils {
	
	/**
	 * base64加密<pre>{@code (byte[] -> byte[]) }</pre>
	 * 
	 * @param src 字符串
	 * @return 解密结果
	 */
	public static byte[] encode(byte[] src) {
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encode(src);
	}

	/**
	 * base64加密<pre>{@code (String -> String)  }</pre>
	 *
	 * @param src 字符串
	 * @param charset 编码
	 * @return 加密结果base64后的字符串
	 */
	public static String encode2Str(String src, Charset charset) {
		Base64.Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(src.getBytes(charset));
	}

	/**
	 * base64加密<pre>{@code (byte[] -> String)  }</pre>
	 * @param src 需要被加密的数据
	 * @param charset 编码
	 * @return 加密结果base64后的字符串
	 */
	public static String encode2Str(byte[] src, Charset charset) {
		return new String(encode(src), charset);
	}
	
	/**
	 * base64解码<pre>{@code (String -> String) }</pre>
	 * <p>
	 * 值得注意的一点是Base64.Decoder的decode方法源码用的编码是ISO_8859_1不过对于加密串解密一般没啥影响(非中文)
	 * 几种情况抛出异常列举:
	 * 输入"1":Input byte[] should at least have 2 bytes for base64 bytes
	 * 输入"啊是":Illegal base64 character 3f
	 * </p>
	 * 
	 * @param src 字符串
	 * @param charset 编码
	 * @return 解密结果base64后的字符串
	 * @throws IllegalArgumentException IllegalArgumentException
	 */
	public static String decode2Str(String src, Charset charset) throws IllegalArgumentException {
		Base64.Decoder decoder = Base64.getDecoder();
		return new String(decoder.decode(src), charset);
	}
	
	/**
	 * base64解密<pre>{@code (byte[] -> byte[]) }</pre>
	 * 
	 * @param src 需要被解密的数据
	 * @return 解密结果
	 * @throws IllegalArgumentException IllegalArgumentException
	 */
	public static byte[] decode(byte[] src) throws IllegalArgumentException {
		Base64.Decoder decoder = Base64.getDecoder();
		return decoder.decode(src);
	}
	
	/**
	 * 判断字符串是否经过base64加密
	 * <p>
	 * 笨办法,将字符串base64解密后再加密，如果结果和源串一致就返回true
	 * apache的common-codec包下org.apache.commons.codec.binary.Base64类里有isBase64方法
	 * 但是感觉不够准确,比如我用"12"作为参数,解密再加密"77+9"明显和"12"不一样
	 * </p>
	 * 
	 * @param src 字符串
	 * @return 是否是base64编码的字符串
	 */
	@Deprecated
	public static boolean isBase64(String src, Charset charset) {
		try {
			String temp = decode2Str(src, charset);
			return src.equals(encode2Str(temp, charset));
		} catch(Exception ignored) {
		}
		return false;
	}
}