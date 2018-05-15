package com.ag777.util.file.model;

public interface ProgressListener {

	/**
	 * 
	 * @param cur 已读取字节数
	 * @param total 总字节数
	 * @param done 是否完成
	 */
	public void update(int cur, int total, boolean done);
}
