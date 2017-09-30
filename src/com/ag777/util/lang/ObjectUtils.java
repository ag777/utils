package com.ag777.util.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Map;
import com.ag777.util.lang.reflection.ReflectionUtils;

/**
 * 有关 <code>Object</code> 工具类
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2017年09月30日
 */
public class ObjectUtils {

	/**
	 * 实例化class对象,支持内部类
	 * <p>
	 * 	详见ReflectionUtils.newInstace(Class<T> clazz)方法注释
	 * </p>
	 * 
	 */
	public static <T>T newInstace(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		return ReflectionUtils.newInstace(clazz);
	}
	
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
	
	public static Boolean toBoolean(Object obj) {
		if(obj != null) {
			if(obj instanceof Boolean) {
				return (Boolean) obj;
			} else {
				return StringUtils.toBoolean(obj.toString());
			}
		}
		return null;
	}
	
	public static boolean toBoolean(Object obj, boolean defaultValue) {
		Boolean result = toBoolean(obj);
		return result!=null?result:defaultValue;
	}
	
	//其它
	/**
	 * 判断对象是否为空
	 * <p>
	 * 	支持字符串，数组，列表，map;
	 * 此外都返回false;
	 * 	<p>
	 * 	ObjectUtils.isEmpty(new int[]{}) = false
	 * 	</p>
	 * </p>
	 * 
	 */
	@SuppressWarnings({ "rawtypes" })
	public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {	//字符串
            return StringUtils.isEmpty((String) obj);
        }
        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {	//数组
            return true;
        }
        if (obj instanceof Collection) {	//列表
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map) {	//map
            return ((Map) obj).isEmpty();
        }
        return false;
    }
	
	
	/**
	 * 获取对象长度
	 * <p>
	 * 	支持字符串(一个汉字占两个字节)，数组，列表，map;
	 * 此外都返回-1
	 * 	<p>
	 * 	ObjectUtils.isEmpty(new int[]{1,2,3}) = 3
	 * 	</p>
	 * </p>
	 * 
	 */
	@SuppressWarnings("rawtypes")
	public static int getLength(Object obj) {
		if (obj == null) {
            return 0;
        }
		 if (obj instanceof String) {	//字符串
            return StringUtils.getLength((String) obj);
        }
        if (obj.getClass().isArray()) {	//数组
            return Array.getLength(obj);
        }
        if (obj instanceof Collection) {	//列表
            return ((Collection) obj).size();
        }
        if (obj instanceof Map) {	//map
            return((Map) obj).size();
        }
        return -1;
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