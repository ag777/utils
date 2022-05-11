package com.ag777.util.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ag777.util.gson.GsonUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.interf.Disposable;
import com.ag777.util.lang.reflection.ReflectionUtils;

/**
 * 有关 <code>Object</code> 工具类
 * 
 * @author ag777
 * @version create on 2017年09月22日,last modify at 2022年05月11日
 */
public class ObjectUtils {

	/**
	 * 实例化class对象,支持内部类
	 * @see ReflectionUtils#newInstace(Class)
	 */
	public static <T>T newInstace(Class<T> clazz) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException {
		return ReflectionUtils.newInstace(clazz);
	}
	
	/**
	 * 释放对象内存
	 * @param obj obj
	 */
	public static void dispose(Disposable obj) {
		if(obj != null) {
			obj.dispose();
		}
	}
	
	//--转换
	public static String toStr(Object obj) {
		if(obj == null) {
			return null;
		}
		if (obj instanceof String) {
			return (String) obj;
		} else if(obj instanceof Date) {
			return DateUtils.toString(((Date)obj), DateUtils.DEFAULT_TEMPLATE_TIME);
		}
		return obj.toString();
	}
	
	public static String toStr(Object obj, String defaultValue) {
		String result = toStr(obj);
		return getOrDefault(result, defaultValue);
	}

	/**
	 *
	 * @param obj obj
	 * @return Double
	 */
	public static Double toDouble(Object obj) {
		if(obj == null) {
			return null;
		}
		if(obj instanceof Double) {
			return (Double) obj;
		} else if(obj instanceof Integer) {
			return ((Integer) obj).doubleValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).doubleValue();
		} else if (obj instanceof Float) {
			return ((Float)obj).doubleValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).doubleValue();
		}

		return StringUtils.toDouble(obj.toString());
	}

	/**
	 *
	 * @param obj obj
	 * @param defaultValue 默认值
	 * @return double
	 */
	public static double toDouble(Object obj, double defaultValue) {
		Double result = toDouble(obj);
		return getOrDefault(result, defaultValue);
	}

	/**
	 *
	 * @param obj obj
	 * @return Float
	 */
	public static Float toFloat(Object obj) {
		if(obj == null) {
			return null;
		}
		if(obj instanceof Float) {
			return (Float) obj;
		} else if(obj instanceof Integer) {
			return ((Integer) obj).floatValue();
		} else if (obj instanceof Long) {
			return ((Long)obj).floatValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).floatValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).floatValue();
		}

		return StringUtils.toFloat(obj.toString());
	}

	/**
	 *
	 * @param obj obj
	 * @param defaultValue 默认值
	 * @return float
	 */
	public static float toFloat(Object obj, float defaultValue) {
		Float result = toFloat(obj);
		return getOrDefault(result, defaultValue);
	}

	/**
	 *
	 * @param obj obj
	 * @return Integer
	 */
	public static Integer toInt(Object obj) {
		if(obj == null) {
			return null;
		}
		if(obj instanceof Integer) {
			return (Integer) obj;
		} else if (obj instanceof Float) {
			return ((Float)obj).intValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).intValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).intValue();
		}

		return StringUtils.toInt(obj.toString());
	}

	/**
	 *
	 * @param obj obj
	 * @param defaultValue 默认值
	 * @return int
	 */
	public static int toInt(Object obj, int defaultValue) {
		Integer result = toInt(obj);
		return getOrDefault(result, defaultValue);
	}

	/**
	 *
	 * @param obj obj
	 * @return Long
	 */
	public static Long toLong(Object obj) {
		if(obj == null) {
			return null;
		}
		if(obj instanceof Long) {
			return (Long) obj;
		} else if(obj instanceof Integer) {
			return (long) obj;
		} else if (obj instanceof Float) {
			return ((Float)obj).longValue();
		} else if (obj instanceof Double) {
			return ((Double)obj).longValue();
		} else if (obj instanceof BigDecimal) {
			return ((BigDecimal) obj).longValue();
		} else if(obj instanceof Date) {
			return ((Date)obj).getTime();
		}

		return StringUtils.toLong(obj.toString());
	}

	/**
	 *
	 * @param obj obj
	 * @param defaultValue 默认值
	 * @return long
	 */
	public static long toLong(Object obj, long defaultValue) {
		Long result = toLong(obj);
		return getOrDefault(result, defaultValue);
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
		return getOrDefault(result, defaultValue);
	}
	
	public static Date toDate(Object obj) {
		if(obj != null) {
			if(obj instanceof Date) {
				return (Date) obj;
			} else {
				return StringUtils.toDate(obj.toString());
			}
		}
		return null;
	}
	
	public static Date toDate(Object obj, Date defaultValue) {
		Date result = toDate(obj);
		return getOrDefault(result, defaultValue);
	}
	
	public static Map<String, Object> toMap(Object obj) {
		if(obj == null) {
			return null;
		}
		return GsonUtils.get().toMap(GsonUtils.get().toJson(obj));
	}
	
	public static <T>List<Map<String, Object>> toListMap(List<T> list) {
		if(list == null) {
			return null;
		}
		if(list.isEmpty()) {
			return ListUtils.newArrayList();
		}
		GsonUtils u = GsonUtils.get();
		return u.toListMap(u.toJson(list));
	}
	
	//其它
	/**
	 * 如果obj为null则返回默认值
	 * @param obj obj
	 * @param defaultValue defaultValue
	 * @return
	 */
	public static <T>T getOrDefault(T obj, T defaultValue) {
		return obj != null?obj:defaultValue;
	}
	
	/**
	 * 判断对象是否为空
	 * <p>
	 * 	支持字符串，数组，列表，map;<br>
	 * 此外都返回false;
	 * 	ObjectUtils.isEmpty(new int[]{}) = false<br>
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
	 * 	支持字符串(一个汉字占两个字节)，数组，列表，map;<br>
	 * 此外都返回-1<br>
	 * 	ObjectUtils.isEmpty(new int[]{1,2,3}) = 3<br>
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
	 * 判断对象是否为数值类型
	 * @param obj obj
	 * @return
	 * @throws Exception Exception
	 */
	public static boolean isNumber(Object obj) throws Exception {
		if(obj == null) {
			throw new RuntimeException("对象为空,不能判断是否为数组");
		}
		return Number.class.isAssignableFrom(obj.getClass());
	}
	
	/**
	 * @param obj obj
	 * @return 是否为数组
	 */
	public static boolean isArray(Object obj) {
		if(obj == null) {
			return false;
		}
		return obj.getClass().isArray();
	}
	
	/**
	 * @param obj obj
	 * @return 是否为collection
	 */
	public static boolean isCollection(Object obj) {
		if(obj == null) {
			return false;
		}
		return obj instanceof Collection;
	}
	
	/**
	 * 判断Boolean变量是否为true，防止控指针异常
	 * @param bool bool
	 * @return
	 */
	public static boolean isBooleanTrue(Boolean bool) {
		if(bool != null && bool) {
			return true;
		}
		return false;
	}
	
	/**
	 * 空指针安全equals
	 * <p>
	 * 	参数中其中一个为null则返回false
	 * </p>
	 * 
	 * @param a a
	 * @param b b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		if(a==null || b==null) {
			return false;
		}
		return a.equals(b);
	}
	
	/**
	 * 都为null时返回true
	 * @param a a
	 * @param b b
	 * @return
	 */
	public static boolean bothNull(Object a, Object... b) {
		if(a != null) {
			return false;
		}
		for (Object item : b) {
			if(item != null) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 简化switch-case
	 * <p>内部没做参数判断，并且相比switch可能会牺牲一些效率，请理解后再使用
	 * {@code
	 * switchCase("a", null, "a", 1, "b", 2) => 1
	 * switchCase("a", 9, "b", 2) => 9
	 * switchCase(null, 9, "b", 2) => null
	 * }
	 * @param target 目标
	 * @param defaultValue 默认返回
	 * @param key1 键1
	 * @param val1 值1
	 * @param otherMappings 键值对，参数数量为2N个:键2,值2,键3,值3...
	 * @param <T> 键的类型
	 * @param <V> 值的类型
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T, V>V switchCase(T target, V defaultValue, T key1, V val1, Object... otherMappings) {
		if(target == null) {
			return null;
		}
		if(target.equals(key1)) {
			return val1;
		}
		for (int i = 0; i < otherMappings.length;) {
			T key = (T) otherMappings[i];
			if(target.equals(key)) {
				return (V) otherMappings[i+1];
			}
			i+=2;
		}
		return defaultValue;
	}
	
}