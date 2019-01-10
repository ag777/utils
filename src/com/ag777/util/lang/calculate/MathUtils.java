package com.ag777.util.lang.calculate;

import java.math.BigDecimal;

/**
 * 科学计算工具类
 * @author ag777
 * @version create on 2019年01月07日,last modify at 2019年01月07日
 */
public class MathUtils {

	private MathUtils() {}
	
	/**
	 * 除(保留小数点后两位,尾数四舍五入)
	 * @param a
	 * @param b
	 * @return
	 */
	public static BigDecimal divide(double a, double b) {
		return divide(a, b,	 2, BigDecimal.ROUND_CEILING);
	}
	
	/**
	 * 除(尾数四舍五入)
	 * @param a
	 * @param b
	 * @param scale 保留小数点后几位
	 * @return
	 */
	public static BigDecimal divide(double a, double b, int scale) {
		return divide(a, b,	 scale, BigDecimal.ROUND_CEILING);
	}
	
	/**
	 * 除
	 * @param a
	 * @param b
	 * @param scale 保留小数点后几位
	 * @param roundingMode 如<br>
		BigDecimal.ROUND_DOWN 截端操作，类似truncate 该模式永远不会增加被操作的数的值<br>
		BigDecimal.ROUND_UP 在精度最后一位加一个单位  setScale(2,BigDecimal.ROUND_UP) 1.666 ->1.67<br>
		BigDecimal.ROUND_CEILING 朝正无穷方向round 如果为正数，行为和round_up一样，如果为负数，行为和round_down一样<br>
		BigDecimal.ROUND_FLOOR 朝负无穷方向round 如果为正数，行为和round_down一样，如果为负数，行为和round_up一样<br>
		BigDecimal.ROUND_HALF_UP 遇到.5的情况时往上近似,例: 1.5 ->2;<br>
		BigDecimal.ROUND_HALF_DOWN 遇到.5的情况时往下近似,例: 1.5 ->;1 注：1.51->2<br>
		BigDecimal.ROUND_HALF_EVEN 如果舍弃部分左边的数字为奇数，则作   ROUND_HALF_UP   ；如果它为偶数，则作   ROUND_HALF_DOWN
	 * @return
	 */
	public static  BigDecimal divide(double a, double b, int scale, int roundingMode) {
		return new BigDecimal(a).divide(new BigDecimal(b), scale, roundingMode);
	}
}
