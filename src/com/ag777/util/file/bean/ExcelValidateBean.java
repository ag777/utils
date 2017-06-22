package com.ag777.util.file.bean;

/**
 * @author wanggz
 * @Description excel验证中介类
 * Time: created at 2017/6/15. last modify at 2017/6/19.
 * Mark: 所有的复制方法都会根据原列表vector则会复制成vector，linkList复制成linklist,其余均复制成arrayList
 */
public class ExcelValidateBean {
	private String title;		//标题
	private String name;		//对应的中文名
	private String reg;			//正则
	private Integer minLength;
	private Integer maxLength;
	private boolean require;	//是否为空
	private String errKey;
	private String errMsg;		//验证错误返回消息的前缀(如:站点"【xxx】的ip格式不正确")
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getReg() {
		return reg;
	}
	public void setReg(String reg) {
		this.reg = reg;
	}
	public Integer getMinLength() {
		return minLength;
	}
	public void setMinLength(Integer minLength) {
		this.minLength = minLength;
	}
	public Integer getMaxLength() {
		return maxLength;
	}
	public void setMaxLength(Integer maxLength) {
		this.maxLength = maxLength;
	}
	public boolean isRequire() {
		return require;
	}
	public void setRequire(boolean require) {
		this.require = require;
	}
	public String getErrKey() {
		return errKey;
	}
	public void setErrKey(String errKey) {
		this.errKey = errKey;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
