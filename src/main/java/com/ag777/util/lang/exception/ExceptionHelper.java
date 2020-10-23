package com.ag777.util.lang.exception;

import java.util.List;

import com.ag777.util.Utils;
import com.ag777.util.lang.collection.ListUtils;

/**
 * 异常辅助类
 * 
 * @author ag777
 * Time: created at 2017/06/06. last modify at 2020/05/20.
 * Mark: 请使用ExceptionUtils中的方法
 */
@Deprecated
public class ExceptionHelper {

	/**
	 * 从抛出异常中提取对应类抛出的异常信息
	 * <p>
	 * 	遍历堆栈如果错误信息由clazz抛出，则拼接错误信息并返回
	 * </p>
	 * 
	 * @param throwable throwable
	 * @param clazz clazz
	 * @return
	 */
	public static String getErrMsg(Throwable throwable, Class<?> clazz) {
		return ExceptionUtils.getErrMsg(throwable, clazz);
	}
	
	/**
	 * 从捕获的异常中提取错误信息
	 * <p>
	 * 	遍历堆栈如果错误信息由workPackageName抛出，且不再排除目录内,则拼接错误信息并返回
	 * </p>
	 * 
	 * @param throwable				异常
	 * @param workPackageName		该方法会从错误栈中查找第一条该包下找到的异常信息作为异常源
	 * @param excludePackageList	排除包
	 * @return
	 */
	public static String getErrMsg(Throwable throwable, String workPackageName,List<String> excludePackageList) {
		return ExceptionUtils.getErrMsg(throwable, workPackageName, excludePackageList);
	}
	
	/**
	 * 从抛出的一场中提取错误信息(工作目录为app中的目录)
	 * @param throwable throwable
	 * @param shouldExcludeUtilsPackage 是否排除该工具包的路径,如果不传这个参数则默认为true
	 * @return
	 */
	public static String getErrMsg(Throwable throwable, boolean shouldExcludeUtilsPackage) {
		List<String> excludePackageList = ListUtils.newArrayList();
		//是否需要排除改工具包的路径
		if(shouldExcludeUtilsPackage) {
			//排除路径集中添加工具包的根路径
			excludePackageList.add(Utils.getUtilsPackageName());
		}
		return getErrMsg(throwable, null, excludePackageList);
	}
	
	/**
	 * 从抛出的一场中提取错误信息(工作目录为app中的目录，排除目录包含Utils类中的目录)
	 * @param throwable throwable
	 * @return
	 */
	public static String getErrMsg(Throwable throwable) {
		return getErrMsg(throwable, true);
	}
	
}