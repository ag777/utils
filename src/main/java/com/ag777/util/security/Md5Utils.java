package com.ag777.util.security;

import com.ag777.util.lang.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 * @author ag777
 * @version last modify at 2023年05月09日
 */
public class Md5Utils {

	private Md5Utils() {}

	/**
	 * 计算字节数组md5
	 * @param bytes 字节数组
	 * @return md5
	 * @throws NoSuchAlgorithmException 没有md5算法
	 */
	public static String md5(byte[] bytes) throws NoSuchAlgorithmException {
		MessageDigest md = getMessageDigest();
		md.update(bytes, 0, bytes.length);
		BigInteger bigInt = new BigInteger(1, md.digest());
		return bigInt.toString(16);
	}

	/**
	 * 读取输入流并计算md5
	 * @param in 输入流
	 * @return md5
	 * @throws NoSuchAlgorithmException 没有md5算法
	 * @throws IOException 读取输入流异常
	 */
	public static String md5(InputStream in) throws NoSuchAlgorithmException, IOException {
		try {
			MessageDigest md = getMessageDigest();
			byte[] buffer = new byte[1024];
			int length;
			while ((length = in.read(buffer, 0, 1024)) != -1) {
				md.update(buffer, 0, length);
			}
			BigInteger bigInt = new BigInteger(1, md.digest());
			return bigInt.toString(16);
		} finally {
			IOUtils.close(in);
		}
	}

	public static MessageDigest getMessageDigest() throws NoSuchAlgorithmException {
		return MessageDigest.getInstance("MD5");
	}
	
}
