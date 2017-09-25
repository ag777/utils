package com.ag777.util.lang;


/**
 * @author ag777
 * @Description 列表工具类
 * Time: created at 2017/09/22. last modify at 2017/09/22.
 * Mark: 
 */
public class ObjectUtils {

	//--转换
	public static String toString(Object obj) {
		if(obj != null) {
			return obj.toString();
		}
		return null;
	}
	
	public static String toString(Object obj, String defaultValue) {
		String result = toString(obj);
		return result!=null?result:defaultValue;
	}
	
	public static Double toDouble(Object obj) {
		if(obj != null) {
			if(obj instanceof Double) {
				return (Double) obj;
			} else {
				return StringUtils.toDouble(obj.toString());
			}
		}
		return null;
	}
	
	public static double toDouble(Object obj, double defaultValue) {
		Double result = toDouble(obj);
		return result!=null?result:defaultValue;
	}
	
	public static Float toFloat(Object obj) {
		if(obj != null) {
			if(obj instanceof Float) {
				return (Float) obj;
			} else {
				return StringUtils.toFloat(obj.toString());
			}
		}
		return null;
	}
	
	public static float toFloat(Object obj, float defaultValue) {
		Float result = toFloat(obj);
		return result!=null?result:defaultValue;
	}
	
	public static Integer toInteger(Object obj) {
		if(obj != null) {
			if(obj instanceof Integer) {
				return (Integer) obj;
			} else {
				return StringUtils.toInteger(obj.toString());
			}
		}
		return null;
	}
	
	public static int toInteger(Object obj, int defaultValue) {
		Integer result = toInteger(obj);
		return result!=null?result:defaultValue;
	}
	
	public static Long toLong(Object obj) {
		if(obj != null) {
			if(obj instanceof Long) {
				return (Long) obj;
			} else {
				return StringUtils.toLong(obj.toString());
			}
		}
		return null;
	}
	
	public static long toLong(Object obj, long defaultValue) {
		Long result = toLong(obj);
		return result!=null?result:defaultValue;
	}
	
	//--判断
	/**
	 * 判断是否为数组
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public static boolean isArray(Object obj) throws Exception {
		if(obj == null) {
			throw new RuntimeException("对象为空,不能判断是否为数组");
		}
		return obj.getClass().isArray();
	}
	
}
