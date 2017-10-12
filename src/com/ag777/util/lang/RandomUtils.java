package com.ag777.util.lang;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机工具类。
 * <p>
 * 		采用JDK7带来的ThreadLocalRandom来生成随机数
 * </p>
 * 
 * @author ag777
 * @version create on 2016年10月10日,last modify at 2017年10月12日
 */
public class RandomUtils {

	private RandomUtils() {}
	
	public static String uuid() {
		return StringUtils.uuid();
	}
	
	/**
	 * 产生随机整形数值[0,max),其中max为开区间
	 * @param max
	 * @return
	 */
	public static int rInt(int max) {
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * 产生随机整形数值[min,max),其中max为开区间
	 * @param min
	 * @param max
	 * @return
	 */
	public static int rInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}
	
	/**
	 * 产生随机Double类型数值[0,max),其中max为开区间
	 * @param max
	 * @return
	 */
	public static double rDouble(double max) {
		return ThreadLocalRandom.current().nextDouble(max);
	}
	
	
	/**
	 * 产生随机Double类型数值[min,max),其中max为开区间
	 * @param min
	 * @param max
	 * @return
	 */
	public static double rDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	/**
	 * 抽个奖吧
	 * <p>
	 * 传入的参数为中奖概率，值在(0,1)区间
	 * 如果在区间左边则返回false,右边则返回true
	 * 若产生的随机数[0,1)小于传入概率则返回true,反之返回false
	 * </p>
	 * 
	 * @param probability
	 * @return
	 */
	public static boolean draw(double probability) {
		if(probability <= 0) {
			return false;
		}
		if(probability >= 1) {
			return true;
		}
		return rDouble(1d) < probability;
	}

}
