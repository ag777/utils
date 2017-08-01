package com.ag777.util.lang;

import java.util.Arrays;
import java.util.List;

import com.ag777.util.Utils;
import com.ag777.util.other.ExceptionHelper;

/**
 * @Description 控制台输出辅助类
 * @author ag777
 * Time: created at 2017/6/6. last modify at 2017/8/1.
 * Mark: 
 */
public class Console {

	private static boolean devMode = true;
	
	/**
	 * getter/setter 控制是否打印日志
	 * @param devMode
	 */
	public static void setDevMode(boolean devMode) {
		Console.devMode = devMode;
	}
	public static boolean isDevMode() {
		return devMode;
	}
	
	/**
	 * 控制台打印信息
	 * @param obj
	 * @return
	 */
	public static String log(Object obj) {
		if(isDevMode()) {
			String msg = getMethod();
			
			if(obj != null && obj instanceof String) {
				msg += (String) obj;
			} else {
				msg += Utils.jsonUtils().toJson(obj);
			}
			
			System.out.println(msg);
			return msg;
		} 
		return null;
	}
	
	/**
	 * 将传入参数转为list并进行输出
	 * @param objs
	 * @return
	 */
	public static String log(Object... objs) {
		String msg = getMethod();
		if(isDevMode()) {
			if(objs != null) {
				msg += Utils.jsonUtils().toJson(Arrays.asList(objs));
			}
		}
		System.out.println(msg);
		return msg;
	}
	
	public static void err(String msg) {
		System.err.println(getMethod()+msg);
	}
	
	/**
	 * 控制台打印异常信息
	 * @param throwable
	 * @param helper
	 * @return
	 */
	public static String err(Throwable throwable, ExceptionHelper helper) {
		if(isDevMode()) {
			String errMsg = helper.getErrMsg(throwable);
			System.err.println(errMsg);
			return errMsg;
		}
		return null;
	}
	
	/**
	 * 打印错误栈信息(效果差不多等于throwable.printStackTrace())
	 * @param throwable
	 */
	public static void err(Throwable throwable) {
		List<String> list = ExceptionHelper.getStackTrace(throwable);
		for (String line : list) {
			System.err.println(line);
		}
	}
	
	/*=================工具方法====================*/
	/**
	 * 获取调用该方法的信息
	 * @return
	 */
	private static String getMethod() {
		
		StackTraceElement[] stacks = (new Throwable()).getStackTrace();
		if(stacks.length > 0) {
			StackTraceElement stack = stacks[stacks.length-1];
			return new StringBuilder()
					.append(stack.getClassName())
					.append('【').append(stack.getMethodName()).append('】')
					.append(":").toString();
		} else {
			return "";
		}
		
		
	}
	
}
