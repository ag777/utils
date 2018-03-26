package com.ag777.util.lang.thread;

import java.util.Optional;

import com.ag777.util.lang.model.ThreadStatus;

/**
 * 周期性任务的基类。
 * <p>
 * 实现这个类，方便做状态控制
 * </p>
 * 
 * @author ag777
 * @version create on 2018年01月08日,last modify at 2017年03月26日
 */
public abstract class BasePeriodicTask {

	public Object key_status_change = new Object();	//状态锁
	private ThreadStatus status;
	
	private Runnable runnable;
	private Thread thread;
	
	private long intervalSleep = 100; //睡眠间隔,这个值影响状态改变的效率,在不需要改变状态的线程设置0
	
	/**
	 * 构造函数
	 * @param interval 每次任务的间隔时间(必须设置，方便后续退出循环)
	 */
	public BasePeriodicTask(long interval) {
		status = ThreadStatus.prepare;
		runnable = new Runnable() {
			
			@Override
			public void run() {
				
				try {	//开始时执行
					onStart();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
				while(true) {
					try {
						if(isPause()) {	//如果状态为已暂停则进入下一轮循环,期间会睡眠一段时间
							continue;
						} else if(isStop()){	//状态为已停止则跳出循环结束这个线程
							break;
						}
						task();
						
					} catch(InterruptedException ex) {
						if(onInterrupt(ex)) {	//如果线程被打断则不继续执行(返回true)
							return;
						}
					} catch(Exception ex) {
						try{
							if(onError(ex)) {	//线程发生错误则不继续执行(返回true)
								//置状态为已停止,并结束该线程
								synchronized (key_status_change) {
									status = ThreadStatus.stop;
								}
								return;
							}
						} catch(InterruptedException e) {
							if(onInterrupt(e)) {	//如果线程被打断则不继续执行(返回true)
								return;
							}
						}
					} finally {
						try {
							sleep(interval);
						} catch (InterruptedException ex) {
							if(onInterrupt(ex)) {	//如果线程被打断则不继续执行(返回true)
								return;
							}
						}
					}
				}	//while end
				
				try {	//结束时执行
					onFinish();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				
			}	//run end
		};	//初始化runnable end
		
	}
	
	/**
	 * 这个方法处理线程被interrupt()的情况,返回值false的话代表线程无视打断继续执行
	 * <p>
	 * 	返回true:将任务状态置为【已停止】并立即终止周期任务的执行
	 * </p>
	 * 
	 * @param ex
	 * @return
	 */
	protected boolean onInterrupt(InterruptedException ex) {
		synchronized (key_status_change) {
			status = ThreadStatus.stop;
		}
		return true;
	}
	
	/**
	 * 这个方法在任务开始轮询前调用(在线程start时)
	 * <p>
	 * 	请尽量在方法内处理完异常,外部只会捕获异常并进行打印
	 * </p>
	 */
	protected void onStart() {
	}
	
	/**
	 * 这个方法在任务结束轮询后调用(在线程即将结束前)
	 * <p>
	 * 	请尽量在方法内处理完异常,外部只会捕获异常并进行打印
	 * </p>
	 */
	protected void onFinish() {
	}
	
	/**
	 * 获取runnbale对象
	 * @return
	 */
	public Runnable getRunnable() {
		return runnable;
	}
	
	/**
	 * 配置最长单次睡眠间隔(如果睡眠大于该时间会被拆分)
	 * @param interval
	 * @return
	 */
	public BasePeriodicTask setIntervalSleep(long interval) {
		this.intervalSleep = interval;
		return this;
	}
	
	public boolean isPreparing() {
		return status == ThreadStatus.prepare;
	}
	
	/**
	 * 状态是否为正常运行
	 * @return
	 */
	public boolean isRunning() {
		return status == ThreadStatus.resume;
	}
	
	/**
	 * 线程是否存活
	 * @return
	 */
	public boolean isAlive() {
		return thread.isAlive();
	}
	
	/**
	 * 状态是否为正在暂停中
	 * @return
	 */
	public boolean isToPause() {
		return status == ThreadStatus.toPause;
	}
	
	/**
	 * 状态是否为已暂停
	 * @return
	 */
	public boolean isPause() {
		return status == ThreadStatus.pause;
	}
	
	/**
	 * 状态是否为正在停止中
	 * @return
	 */
	public boolean isToStop() {
		return status == ThreadStatus.toStop;
	}
	
	/**
	 * 状态是否为已停止
	 * @return
	 */
	public boolean isStop() {
		return status == ThreadStatus.stop;
	}
	
	/**
	 * 获取当前任务的状态
	 * @return
	 */
	public String getStatus() {
		return status.toString();
	}
	
	/**
	 * 开始执行任务
	 * <p>
	 *  1.初始化线程
	 *  2.设置状态为已开始
	 *  3.调用线程的start()方法
	 * </p>
	 */
	public BasePeriodicTask start() {
		if(!isPreparing()) {	//无视二次调用
			return this;
		}
		thread = new Thread(runnable);
		status = ThreadStatus.resume;
		thread.start();
		return this;
	}
	
	/**
	 * 强制中断任务
	 * <p>
	 * 	该方法会直接在任务的sleep处中断,不论是否在执行任务都不会继续往下执行
	 * </p>
	 * 
	 */
	public void interrupt() {
		if(isPreparing() || isStop()) {	//任务并未开始实行或已停止不做处理
			return;
		}
		thread.interrupt();
	}
	
	public Optional<Thread> interruptThread() {
		if(isPreparing() || isStop()) {	//任务并未开始实行或已停止不做处理
			return Optional.empty();
		}
		thread.interrupt();
		return Optional.of(new Thread(()->{
			try {
				while(!isStop()) {
					Thread.sleep(50);
				}
			} catch (InterruptedException e) {
			}
		}));
	}

	/**
	 * 调用Thread.interrupt(),详见interrupt()方法,并等待程序运行结束
	 * <p>
	 * 	注意:如果重写onInterrupt()并修改返回为false则该方法会永久卡住主线程
	 * </p>
	 */
	public void interruptWaitForStop() {
		if(isPreparing() || isStop()) {	//任务并未开始实行或已停止不做处理
			return;
		}
		interrupt();
		/*
		 * ①卡住当前线程循环执行
		 * ②当线程状态不为正在停止中，结束循环
		 */
		try {
			while(!isStop()) {
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * 销毁线程, 该方法执行可能需要一些时间(根据线程业务)
	 * <p>
	 * 	将线程状态置为已停止,并返回true
	 * 这时如果任务正在运行中，则会正常执行完这次循环(不论有没sleep)
	 * </p>
	 */
	public  void stop() {
		if(isPreparing()) {	//任务并未开始实行不做处理
			return;
		}
		
		synchronized (key_status_change) {
			if(status == ThreadStatus.stop) {	//如果线程已经被停止，则不往下继续执行
				return;
			}
			status =ThreadStatus.toStop;	//其余情况置线程为【正在停止】状态
		}	//sync end
		
		/*
		 * ①卡住当前线程循环执行
		 * ②当线程状态不为正在停止中，结束循环
		 */
		try {
			while(isToStop()) {
				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
		}

	}
	
	/**
	 * 置现线程状态为执行
	 * @return
	 */
	public boolean resume() {
		if(isPreparing()) {	//任务并未开始实行不做处理
			return false;
		}
		
		synchronized (key_status_change) {
			if(status == ThreadStatus.resume) {	//已经是执行状态则不改变状态
				return true;
			}
			if(status != ThreadStatus.stop && status != ThreadStatus.toStop && status != ThreadStatus.toPause) {
				status =ThreadStatus.resume;
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 将状态置为停止(如果状态为已停止则不能改变状态,返回false), 该方法执行可能需要一些时间(根据线程业务)
	 * @return
	 */
	public boolean pause() {
		if(isPreparing()) {	//任务并未开始实行不做处理
			return false;
		}
		
		synchronized (key_status_change) {
			if(status == ThreadStatus.pause) {
				return true;
			}
			if(status == ThreadStatus.stop || status == ThreadStatus.toStop) {	//已经被销毁则不能执行状态改变
				return false;
			}
			status =ThreadStatus.toPause;
		}	//sync end
		
		/*
		 * ①卡住当前线程循环执行
		 * ②当线程状态为已暂停，返回true
		 * ③当线程状态为停止中或已停止，返回false
		 * ④其余情况返回false(比如线程被打断)
		 */
		try {
			while(true) {
				if(isToPause()) {
					Thread.sleep(50);
				}
				if(isPause()) {
					return true;
				} else if(isStop() || isToStop()){
					return false;
				}
			}
		} catch (InterruptedException e) {
			return false;
		}
		
	}
	
	/**
	 * 将状态由正在暂停中置为已暂停
	 */
	private void toPause() {
		synchronized (key_status_change) {
			if(status == ThreadStatus.toPause) {
				status = ThreadStatus.pause;
			}
		}
	}
	
	/**
	 * 将状态为正在停止中置为已停止
	 */
	private void toStop() {
		synchronized (key_status_change) {
			if(status == ThreadStatus.toStop) {
				status = ThreadStatus.stop;
			}
		}
	}
	
	/**
	 * 改变状态为暂停或停止时不睡眠直接进入下一轮循环,每轮循环的头部会对状态做处理
	 * <p>
	 * 请注意!!!:
	 * 	由于线程暂停/停止实际生效是在一次轮询之后，所以在外部调用这个方法会直接导致与预期不符的问题<br/>
	 *  比如本来调用pause()方法希望线程暂停，然而等带完状态改变后线程并没有真正暂停,<br/>
	 *  所以该方法改为除非在周期任务末尾调用，外部依然调用Thread.sleep()方法进行睡眠,需要单纯改变状态请调用toPause()
	 *  加一句,只有调用这句才能保证在sleep时改变状态实时生效,所以请务必在轮询末尾或者在onErr中使用该方法睡眠线程
	 * </p>
	 * @param time
	 */
	@Deprecated
	public void sleep(long time) throws InterruptedException{
		/*
		 * ①每100毫秒sleep一次(剩余时间不足该时间则sleep剩余时间)
		 * ②每次都去判断状态是否为正在暂停中或者正在停止中
		 * ③如果条件满足②则改变状态，并结束该循环(下次任务循环不会执行并且会进入下一轮sleep)
		 */
		for(;time>0;) {
			if(isToPause()) {
				toPause();
				return;
			}
			if(isToStop()) {
				toStop();
				return;
			}
			if(isStop()) {
				return;
			}
			
			if(intervalSleep <= 0) {	//完整地睡眠一次
				Thread.sleep(intervalSleep);
				return;
			} else {
				if(time > intervalSleep) {
					Thread.sleep(intervalSleep);
					time -= intervalSleep;
				} else {	//睡眠剩余时间
					Thread.sleep(time);
					time = 0l;
					return;
				}
			}
				
		}	//sleep1 for end
		
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
