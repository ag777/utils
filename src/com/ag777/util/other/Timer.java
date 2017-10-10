package com.ag777.util.other;

import org.joda.time.DateTime;
import com.ag777.util.lang.DateUtils;

/**
 * 计时工具类，用来查看代码执行耗时
 * 
 * @author ag777
 * @version create on 2017年10月10日,last modify at 2017年10月10日
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
	 * 打印当前操作用时和总操作用时
	 * @param operate
	 */
	public void sign(String operate) {
		long nowMills = new DateTime().getMillis();
		long totalBetween = nowMills - startMillis;
		long lastBetween = lastMillis == null?totalBetween:nowMills - lastMillis;
		StringBuilder sb = new StringBuilder();
		if(operate != null) {
			sb.append("操作[").append(operate).append("]");
		}
		sb.append("用时:")
			.append(DateUtils.toString(lastBetween, formatOutput))
			.append("||共用时:")
			.append(DateUtils.toString(totalBetween, formatOutput));
		System.out.println(sb);
	}
	
}
