package com.ag777.util.lang.interf;

/**
 * 进度监听回调接口
 * 
 * @author ag777
 * @version create on 2018年05月15日,last modify at 2018年05月15日
 */
public interface ProgressListener {

	/**
	 * 
	 * @param cur 已读取字节数
	 * @param total 总字节数
	 * @param done 是否完成
	 */
	public void update(int cur, int total, boolean done);
}
