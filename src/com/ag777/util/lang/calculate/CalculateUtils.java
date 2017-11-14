package com.ag777.util.lang.calculate;

import java.math.BigDecimal;

/**
 * 科学计算工具类
 * <p>
 * 		java原有对于浮点型的计算结果不精确,例试下0.06612d+0.00413d
 * </p>
 * @author ag777
 * @version create on 2017年11月14日,last modify at 2017年11月14日
 */
public class CalculateUtils {

	//--计算和
	
	public static double addDouble(double a, double b) {
		return add(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).doubleValue();
	}
	
	public static double addDouble(double a, float b) {
		return add(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).doubleValue();
	}
	
	public static float addFloat(float a, float b) {
		return add(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).floatValue();
	}
	
	public static float addFloat(float a, double b) {
		return add(BigDecimal.valueOf(a), BigDecimal.valueOf(b)).floatValue();
	}
	
	public static BigDecimal add(BigDecimal a, BigDecimal b) {
		return a.add(b);
	}
}
