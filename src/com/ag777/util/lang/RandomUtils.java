package com.ag777.util.lang;

import java.util.Random;

public class RandomUtils {

	private RandomUtils() {}
	
	public static String uuid() {
		return StringUtils.uuid();
	}
	
	public static int rInt(int max) {
		return new Random().nextInt(max);
	}
	
	public static int rInt(int min, int max) {
		int size = max-min;
		return rInt(size) + min;
	}
}
