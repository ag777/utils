package com.ag777.util.lang;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
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
 * @version create on 2017年09月22日,last modify at 2018年11月22日
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
	 * @param obj
	 */
	public static void dispose(Disposable obj) {
		if(obj != null) {
			obj.dispose();
		}
	}
	
	/**
	 * 如果对象不为null则返回本身，否则返回默认值
	 * @param obj
	 * @param defaultObj
	 * @return
	 */
	public static <T>T defaultIfNull(T obj, T defaultObj) {
		return obj!=null?obj:defaultObj;
	}
	
	//--转换
	public static String toStr(Object obj) {
		if(obj != null) {
			return obj.toString();
		}
		return null;
	}
	
	public static String toStr(Object obj, String defaultValue) {
		String result = toStr(obj);
		return defaultIfNull(result, defaultValue);
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
		return defaultIfNull(result, defaultValue);
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
		return defaultIfNull(result, defaultValue);
	}
	
	public static Integer toInt(Object obj) {
		if(obj != null) {
			if(obj instanceof Integer) {
				return (Integer) obj;
			} else {
				return StringUtils.toInt(obj.toString());
			}
		}
		return null;
	}
	
	public static int toInt(Object obj, int defaultValue) {
		Integer result = toInt(obj);
		return defaultIfNull(result, defaultValue);
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
		return defaultIfNull(result, defaultValue);
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
		return defaultIfNull(result, defaultValue);
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
		return defaultIfNull(result, defaultValue);
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
	 * @param obj
	 * @param defaultValue
	 * @return
	 */
	public static <T>T getOrDefault(T obj, T defaultValue) {
		return obj != null?obj:defaultValue;
	}
	
	/**
	 * 判断对象是否为空
	 * <p>
	 * 	支持字符串，数组，列表，map;
	 * 此外都返回false;
	 * 	<p>
	 * 		ObjectUtils.isEmpty(new int[]{}) = false
	 * 	</p>
	 * 
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
	
	/**
	 * 判断Boolean变量是否为true，防止控指针异常
	 * @param bool
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
	 * @param a
	 * @param b
	 * @return
	 */
	public static boolean equals(Object a, Object b) {
		if(a==null || b==null) {
			return false;
		}
		return a.equals(b);
	}
	
	/**
	 * byte转二进制字符串
	 * @param b
	 * @return
	 */
	public static String byte2Binary(Byte b)  {
		if(b == null) {
			return null;
		}
		return Long.toString(b & 0xff, 2);
	}
	
	
	/**
	 * 16进制转2进制
	 * @param src
	 * @return
	 */
	public static String decimal2Binary(String src) {
		if(src == null) {
			return null;
		}
		if(src.startsWith("0x")) {
			src = src.substring(2, src.length());
		}
		StringBuilder sb = new StringBuilder();
		int length = src.length();
		src = src.toUpperCase();
		for (int i = 0; i < length; i++) {
			switch(src.charAt(i)) {
				case '0':
					sb.append("0000");
					break;
				case '1':
					sb.append("0001");
					break;
				case '2':
					sb.append("0010");
					break;
				case '3':
					sb.append("0011");
					break;
				case '4':
					sb.append("0100");
					break;
				case '5':
					sb.append("0101");
					break;
				case '6':
					sb.append("0110");
					break;
				case '7':
					sb.append("0111");
					break;
				case '8':
					sb.append("1000");
					break;
				case '9':
					sb.append("1001");
					break;
				case 'A':
					sb.append("1010");
					break;
				case 'B':
					sb.append("1011");
					break;
				case 'C':
					sb.append("1100");
					break;
				case 'D':
					sb.append("1101");
					break;
				case 'E':
					sb.append("1110");
					break;
				case 'F':
					sb.append("1111");
					break;
				default:
					throw new RuntimeException("含有非法字符:"+src.charAt(i)+",位置:"+i);
			}
		}
		return sb.toString();
	}
	
	/**
	 * 二进制转16进制
	 * <p>
	 * 会先转为Integer类型
	 * </p>
	 * @param src
	 * @return
	 */
	public static String binary2Decimal(String src) {
		if(src == null) {
			return null;
		}
		if(src.length()%4 != 0) {
			throw new RuntimeException("2进制串的长度不为4的倍数");
		}
		if(!src.matches("^(0|1)*$")) {
			throw new RuntimeException("2进制串包含0,1以外的字符");
		}
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<src.length();i=i+4) {
			String item = src.substring(i, i+4);
			int tem=Integer.parseInt(item, 2);
			sb.append(Integer.toHexString(tem).toUpperCase());
		}
		return sb.toString();
	}
	
}
