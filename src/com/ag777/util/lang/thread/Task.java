package com.ag777.util.lang.thread;

public abstract class Task {
	
	private Runnable runnable;
	private Thread thread;
	
	public Task() {
		init();
		runnable = new Runnable() {
			
			@Override
			public void run() {
				try {
					task();
				} catch (InterruptedException e) {
					onInterrupt(e);
				} catch (Exception ex) {
					try {
						onError(ex);
					} catch (InterruptedException e) {
						onInterrupt(e);
					}
				}
			}
		};
		thread = new Thread(runnable);
	}
	
	public Thread getThread() {
		return thread;
	}
	
	public Task start() {
		thread.start();
		return this;
	}
	
	public void interrupt() {
		thread.interrupt();
	}
	
	/**
	 * 构造函数里执行
	 */
	protected void init() {
	}
	
	/**
	 * 中断时执行
	 * @param e
	 */
	protected void onInterrupt(InterruptedException e) {
	}
	
	/**
	 * 该线程的具体业务
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public abstract void task() throws Exception, InterruptedException;
	
	/**
	 * 程序发生错误时执行。如果这个方法返回true则直接终止该线程
	 * @param ex
	 * @return
	 * @throws InterruptedException
	 */
	public abstract boolean onError(Exception ex) throws InterruptedException;
}
