package com.ag777.util.lang.exception;

import java.util.List;
import java.util.Map;

import com.ag777.util.Utils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.collection.MapUtils;

/**
 * 统一的异常信息获取类
 * @author wanggz
 * Time: created at 2020/05/20. last modify at 2020/05/20.
 *
 */
public class ExceptionUtils {
	
	private ExceptionUtils() {}
	
	public static String getErrMsg(Throwable t) {
		return getErrMsg(t, null, null);
	}
	
	public static String getErrMsg(Throwable t, String workingPackage) {
		return getErrMsg(t, workingPackage, null);
	}
	
	public static String getErrMsg(Throwable t, String workingPackage, List<String> excludePackageList) {
		Map<String, Object> errMap = getErrMap(t, workingPackage, excludePackageList);
		return Utils.jsonUtils().toJson(errMap);
	}
	
	public static String getErrMsg(Throwable throwable, Class<?> clazz) {
		Map<String, Object> errMap = getErrMap(throwable, clazz);
		return Utils.jsonUtils().toJson(errMap);
	}
	
	
	public static Map<String, Object> getErrMap(Throwable throwable) {
		return getErrMap(throwable, null, null);
	}
	
	public static Map<String, Object> getErrMap(Throwable throwable, String workingPackage) {
		return getErrMap(throwable, workingPackage, null);
	}
	
	/**
	 * 从异常栈中提取异常信息
	 * @param throwable 异常
	 * @param workingPackage 工作包
	 * @param excludePackageList 排除包(列表)
	 * @return
	 */
	public static Map<String, Object> getErrMap(Throwable throwable, String workingPackage, List<String> excludePackageList) {
		boolean hasWorkingPackage = !StringUtils.isEmpty(workingPackage);
		boolean hasExculdePackage = !ListUtils.isEmpty(excludePackageList);
		return getErrMap(throwable, (stackTraceElement)->{
			if(!hasWorkingPackage) {	//不包含工作路径返回第一个
				return true;
			}
			String clazzName = stackTraceElement.getClassName();	//类路径
			if(hasExculdePackage) {	//排除包
				for (String excludePackage : excludePackageList) {
					if(clazzName.startsWith(excludePackage)) {
						return false;
					}
				}
			}
			//在工作包下
			return clazzName.startsWith(workingPackage);
		});
	}
	
	/**
	 * 从异常栈中提取异常信息
	 * @param throwable 异常
	 * @param clazz 对应产生异常的类,会优先获取该类中产生的异常
	 * @return
	 */
	public static Map<String, Object> getErrMap(Throwable throwable, Class<?> clazz) {
		return getErrMap(throwable, (stackTraceElement)->{
			String clazzName = stackTraceElement.getClassName();	//类路径
			return clazzName.equals(clazz.getName());
		});
	}
	
	/**
	 * 从异常栈中提取异常信息
	 * @param throwable 异常
	 * @param predicate 返回true则提取对应栈信息，并停止遍历(仅作用于最外层异常)
	 * @return
	 */
	public static Map<String, Object> getErrMap(Throwable throwable, java.util.function.Predicate<StackTraceElement> predicate) {
		if(throwable == null) {
			return null;
		}
		
		Map<String, Object> errMsg = MapUtils.of("msg", throwable.getMessage() != null?throwable.getMessage():throwable.toString());
		
		try {
			StackTraceElement[] s = throwable.getStackTrace();
			if(!ListUtils.isEmpty(s) ) {
				
				boolean findOne = false;
				for (StackTraceElement stackTraceElement : s) {
					if(predicate.test(stackTraceElement)) {
						findOne = true;
						String clazzName = stackTraceElement.getClassName();	//类路径
						int line = stackTraceElement.getLineNumber();			//异常相对于类所在行数
						String method = stackTraceElement.getMethodName();		//异常所属方法名
						errMsg.put("line", line);
						errMsg.put("method", method);
						errMsg.put("class", clazzName);
						break;
					}
				}
				if(!findOne) {	//如果前面没找到
					StackTraceElement s1 = s[0];
					String clazzName = s1.getClassName();	//类路径
					int line = s1.getLineNumber();			//异常相对于类所在行数
					String method = s1.getMethodName();		//异常所属方法名
					errMsg.put("line", line);
					errMsg.put("method", method);
					errMsg.put("class", clazzName);
				}
			}
			
			Throwable cause = throwable.getCause();
			if(cause != null) {
				errMsg.put("cause", getErrMap(cause, null, null));
			}
			
		} catch(Exception e) {
			//不处理
		}
		return errMsg;
	}
	
	/**
	 * 递归遍历异常栈
	 * @param throwable 异常
	 * @param predicate 判断是否停止遍历，参数:StackTraceElement 异常 | Integer 当前深度，每一个cause，深度+1. return true停止遍历
	 */
	public static void walk(Throwable throwable, java.util.function.BiPredicate<StackTraceElement, Integer> predicate) {
		walk(throwable, predicate, 0);
	}
	
	/**
	 * 递归遍历异常栈
	 * @param throwable 异常
	 * @param predicate 判断是否停止遍历，return true停止
	 * @param deep 当前深度，每一个cause，深度+1
	 */
	private static void walk(Throwable throwable, java.util.function.BiPredicate<StackTraceElement, Integer> predicate, int deep) {
		if(throwable == null) {
			return;
		}
		
		StackTraceElement[] s = throwable.getStackTrace();
		if(!ListUtils.isEmpty(s) ) {
			for (StackTraceElement stackTraceElement : s) {
				if(predicate.test(stackTraceElement, deep)) {
					return;
				}
			}
		}
		
		Throwable cause = throwable.getCause();
		if(cause != null) {
			walk(throwable, predicate, deep+1);
		}
		
	}

}
