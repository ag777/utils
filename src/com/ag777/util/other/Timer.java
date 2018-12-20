package com.ag777.util.other;

import org.joda.time.DateTime;

/**
 * 计时工具类，用来查看代码执行耗时
 * 
 * @author ag777
 * @version create on 2017年10月10日,last modify at 2017年12月19日
 */
public class Timer {

	private String formatOutput = "mm:ss.SSS";
	private long startMillis;
	private Long lastMillis;
	
	
	public Timer() {
		init();
	}
	
	public Timer(String formatOutput) {
		this.formatOutput = formatOutput;
		init();
	}
	
	/**
	 * 初始化计时器
	 */
	public void init() {
		startMillis = new DateTime().getMillis();
	}
	
	/**
	 * 获取当前操作用时和总操作用时
	 * @return [用时,共用时]
	 */
	public long[] cutin() {
		long nowMills = new DateTime().getMillis();
		long totalBetween = nowMills - startMillis;
		long lastBetween = lastMillis == null?totalBetween:nowMills - lastMillis;
		lastMillis = nowMills;
		return new long[]{lastBetween, totalBetween};
	}
	
	/**
	 * 获取当前操作用时和总操作用时
	 * @return [用时,共用时]
	 */
	public String[] cutinStr() {
		long nowMills = new DateTime().getMillis();
		long totalBetween = nowMills - startMillis;
		long lastBetween = lastMillis == null?totalBetween:nowMills - lastMillis;
		lastMillis = nowMills;
		return new String[]{mills2Str(lastBetween, formatOutput), mills2Str(totalBetween, formatOutput)};
	}
	
	/**
	 * 打印当前操作用时和总操作用时
	 * @param operate
	 */
	public void sign(String operate) {
		String[] times = cutinStr();
		StringBuilder sb = new StringBuilder();
		if(operate != null) {
			sb.append("操作[").append(operate).append("]");
		}
		sb.append("用时:")
			.append(times[0])
			.append("||共用时:")
			.append(times[1]);
		System.out.println(sb);
	}
	
	private static String mills2Str(long mills, String template) {
		return new DateTime(mills).toString(template);
	}
	
}
