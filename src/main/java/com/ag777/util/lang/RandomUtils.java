package com.ag777.util.lang;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.ag777.util.lang.model.Pair;

/**
 * 随机工具类。
 * <p>
 * 		采用JDK7带来的ThreadLocalRandom来生成随机数
 * </p>
 * 
 * @author ag777
 * @version create on 2016年10月10日,last modify at 2019年02月14日
 */
public class RandomUtils {

	private RandomUtils() {}
	
	public static String uuid() {
		return StringUtils.uuid();
	}
	
	/**
	 * 产生随机整形数值[0,max),其中max为开区间
	 * @param max max
	 * @return 随机值
	 */
	public static int rInt(int max) {
		return ThreadLocalRandom.current().nextInt(max);
	}
	
	/**
	 * 产生随机整形数值[min,max),其中max为开区间
	 * @param min min
	 * @param max max
	 * @return 随机值
	 */
	public static int rInt(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min, max);
	}

	/**
	 * 产生随机长整形数值[min,max),其中max为开区间
	 * @param max max
	 * @return 随机值
	 */
	public static long rLong(long max) {
		return ThreadLocalRandom.current().nextLong(max);
	}

	/**
	 * 产生随机长整形数值[min,max),其中max为开区间
	 * @param min min
	 * @param max max
	 * @return 随机值
	 */
	public static long rLong(long min, long max) {
		return ThreadLocalRandom.current().nextLong(min, max);
	}

	/**
	 * 产生随机Double类型数值[0,max),其中max为开区间
	 * @param max max
	 * @return 随机值
	 */
	public static double rDouble(double max) {
		return ThreadLocalRandom.current().nextDouble(max);
	}
	
	
	/**
	 * 产生随机Double类型数值[min,max),其中max为开区间
	 * @param min min
	 * @param max max
	 * @return 随机值
	 */
	public static double rDouble(double min, double max) {
		return ThreadLocalRandom.current().nextDouble(min, max);
	}
	
	/**
	 * 随机从列表里抽取一项
	 * @param list list
	 * @return 随机项
	 */
	public static <T>T rList(List<T> list) {
		int rInt = rInt(list.size());
		return list.get(rInt);
	}

	/**
	 * 从列表中随机抽取一项,所有项的概率平均
	 * @param list list
	 * @return 随机项
	 */
	public static <T>T draw(List<T> list) {
		if(list == null || list.isEmpty()) {
			return null;
		}
		int index = rInt(list.size());
		return list.get(index);
	}
	
	/**
	 * 从列表中抽取一项，每项跟随一个概率
	 * <p>
	 * 	每项概率介于(0,1]之间，总和应为1，提前算好
	 * </p>
	 * @param list list
	 * @return 随机项
	 * @throws Exception Exception
	 */
	public static <T>T rListByProbability(List<Pair<T, Double>> list) throws Exception {
		double rDouble = rDouble(1);
		for (Pair<T, Double> pair : list) {
			double probability = pair.second;
			if(rDouble<probability) {
				return pair.first;
			} else {
				rDouble -= probability;
			}
		}
		throw new Exception("抽奖概率异常:"+rDouble);
	}

	/**
	 * 从列表中随机抽取一项,所有项的概率平均
	 * @param arr arr
	 * @return 随机项
	 */
	public static <T>T draw(T[] arr) {
		if(arr == null || arr.length == 0) {
			return null;
		}
		int index = rInt(arr.length);
		return arr[index];
	}
	
	/**
	 * 抽个奖吧
	 * <p>
	 * 传入的参数为中奖概率，值在(0,1)区间
	 * 如果在区间左边则返回false,右边则返回true
	 * 若产生的随机数[0,1)小于传入概率则返回true,反之返回false
	 * </p>
	 * 
	 * @param probability probability
	 * @return 是否命中
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