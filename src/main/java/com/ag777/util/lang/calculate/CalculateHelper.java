package com.ag777.util.lang.calculate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.exception.model.DivisorIsZeroException;

/**
 * 科学计算辅助类
 * <p>
 * 		java原有对于浮点型的计算结果不精确,例：试下用java直接计算0.06612d+0.00413d
 * </p>
 * @author ag777
 * @version create on 2017年11月14日,last modify at 2018年07月19日
 */
public class CalculateHelper {

	private BigDecimal num;
	
	//--构造函数
	public CalculateHelper(Object obj) {
		num = valueOfWithException(obj);
	}
	
	public CalculateHelper() {
		num = new BigDecimal(0);
	}
	
	public static CalculateHelper init() {
		return new CalculateHelper(); 
	}
	
	public static CalculateHelper init(int num) {
		return new CalculateHelper(num); 
	}
	
	public static CalculateHelper init(long num) {
		return new CalculateHelper(num);
	}
	
	public static CalculateHelper init(float num) {
		return new CalculateHelper(num);
	}
	
	public static CalculateHelper init(double num) {
		return new CalculateHelper(num);
	}
	
	//--外部工具方法
	//--获取值
	
	public BigDecimal get() {
		return num;
	}
	
	public int getInt() {
		return num.intValue();
	}
	
	public double getDouble() {
		return num.doubleValue();
	}
	
	public float getFloat() {
		return num.floatValue();
	}
	
	public long getLong() {
		return num.longValue();
	}
	
	/**
	 * 将字符格式化为字符串
	 * @param decimalPlaces 保留小数位数
	 * @return
	 */
	public String toString(int decimalPlaces) {
		return StringUtils.formatNum(num.doubleValue(), decimalPlaces);
	}
	
	//--计算
	/**
	 * 加
	 * 
	 * <p>
	 * 	将参数转换为BigDecimal类型再进行计算转化失败抛出runtime异常
	 * </p>
	 * @param augend augend
	 * @return
	 */
	public CalculateHelper add(Object augend) {
		num = num.add(valueOfWithException(augend));
		return this;
	}
	
	/**
	 * 减
	 * 
	 * <p>
	 * 	将参数转换为BigDecimal类型再进行计算转化失败抛出runtime异常
	 * </p>
	 * 
	 * @param subtrahend subtrahend
	 * @return
	 */
	public CalculateHelper subtract(Object subtrahend) {
		num = num.subtract(valueOfWithException(subtrahend));
		return this;
	}
	
	/**
	 * 乘
	 * 
	 * <p>
	 * 	将参数转换为BigDecimal类型再进行计算转化失败抛出runtime异常<br>
	 * 
	 * @param multiplicand multiplicand
	 * @return
	 */
	public CalculateHelper multiply(Object multiplicand) {
		num = num.multiply(valueOfWithException(multiplicand));
		return this;
	}
	
	/**
	 * 除
	 * 
	 * <p>
	 * 	将参数转换为BigDecimal类型再进行计算转化失败抛出runtime异常<br>
	 * 结果保留10位，四舍五入<br>
	 * 除数为0不做任何处理
	 * 
	 * @param divisor divisor
	 * @return
	 */
	public CalculateHelper divide(Object divisor) {
		if(divisor.equals(0)) {
			return this;
		}
		num = num.divide(valueOfWithException(divisor), 10, BigDecimal.ROUND_HALF_DOWN);
		return this;
	}
	
	/**
	 * 除
	 * <p>
	 * 结果保留10位，四舍五入<br>
	 * 除数为0抛出异常
	 * </p>
	 * 
	 * @param divisor divisor
	 * @return
	 * @throws DivisorIsZeroException DivisorIsZeroException
	 */
	public CalculateHelper divideWithException(Object divisor) throws DivisorIsZeroException {
		if(divisor.equals(0)) {
			throw new DivisorIsZeroException();
		}
		num = num.divide(valueOfWithException(divisor), 10, BigDecimal.ROUND_HALF_DOWN);
		return this;
	}
	
	/**
	 * 阶乘
	 * 
	 * <p>
	 * 	将参数转换为BigDecimal类型再进行计算转化失败抛出runtime异常
	 * CalculateHelper.init("4").pow(2) = 16
	 * </p>
	 * 
	 * @param n 指数
	 * @return
	 */
	public CalculateHelper pow(int n) {
		num = num.pow(n);
		return this;
	}
	
	/**
	 * 将任意类型转换为BigCecimal,转化失败抛出runtime异常
	 * @param obj obj
	 * @return
	 */
	public static BigDecimal valueOfWithException(Object obj) {
		Optional<BigDecimal> temp = valueOf(obj);
		if(temp.isPresent()) {
			return temp.get();
		} else {
			throw new RuntimeException(
					StringUtils.concat("object[", obj, "] is not a number"));
		}
	}
	
	/**
	 * 将任意类型转换为BigCecimal
	 * @param obj obj
	 * @return
	 */
	public static Optional<BigDecimal> valueOf(Object obj) {
		if(obj == null) {
			return Optional.empty();
		}
		try {
			if(obj instanceof BigDecimal) {
				return Optional.of(
						(BigDecimal)obj);
			}else if(obj instanceof Double) {
				return Optional.of(
						BigDecimal.valueOf((Double) obj));
			} else if(obj instanceof Long) {
				return Optional.of(
						BigDecimal.valueOf((Long) obj));
			} else if(obj instanceof Float) {
				return Optional.of(
						new BigDecimal((Float)obj));
			} else if(obj instanceof Integer) {
				return Optional.of(
						new BigDecimal((Integer)obj));
			} else if(obj instanceof BigInteger) {
				return Optional.of(
						new BigDecimal((BigInteger)obj));
			} else if(obj instanceof String) {
				return Optional.of(
						new BigDecimal((String)obj));
			} else if(obj instanceof char[]) {
				return Optional.of(
						new BigDecimal((char[])obj));
			} else {
				return Optional.of(
						new BigDecimal(obj.toString()));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}
	
}