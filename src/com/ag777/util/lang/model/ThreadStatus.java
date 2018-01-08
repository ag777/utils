package com.ag777.util.lang.model;

/**
 * 线程状态枚举类,配合<code>BasePeriodicTask</code>做任务状态控制。
 * 
 * @author ag777
 * @version create on 2018年01月08日,last modify at 2017年01月08日
 */
public enum ThreadStatus {

	prepare {
		@Override
		public String toString() {
			return "未开始";
		}
	},
	toPause {
		@Override
		public String toString() {
			return "暂停中";
		}
	},
	pause {
		@Override
		public String toString() {
			return "暂停";
		}
	},
	resume {
		@Override
		public String toString() {
			return "正常运行";
		}
	},
	toStop {
		@Override
		public String toString() {
			return "停止中";
		}
	},
	stop {
		@Override
		public String toString() {
			return "停止";
		}
	};
	
	public String toString() {
		return "";
	}
}
