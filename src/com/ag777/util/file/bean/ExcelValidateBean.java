package com.ag777.util.file.bean;

import java.util.regex.Pattern;

/**
 * @author ag777
 * @Description excel验证中介类
 * Time: created at 2017/6/15. last modify at 2017/7/24.
 */
public class ExcelValidateBean {
	private String title;		//标题
	private String name;		//对应的中文名
	private Pattern reg;			//正则
	private Integer minLength;
	private Integer maxLength;
	private Class<?> clazz;		//类
	private Integer minNum;
	private Integer maxNum;
	private String dateFormat;	//日期格式
	
	private boolean require;	//是否为空
	private String errKey;
	private String errMsg;		//验证错误返回消息的前缀(如:站点"【xxx】的ip格式不正确")
	
	public ExcelValidateBean (String title, String name) {
		this.title = title;
		this.name = name;
		minLength = -1;
		maxLength = -1;
		require = false;
		clazz = String.class;
		dateFormat = "yyyy-MM-dd";
	}
	
	public String title() {
		return title;
	}
	public ExcelValidateBean title(String title) {
		this.title = title;
		return this;
	}
	public String name() {
		return name;
	}
	public ExcelValidateBean name(String name) {
		this.name = name;
		return this;
	}
	public Pattern reg() {
		return reg;//reg.toString();<-这个方法能拿到源正则表达式
	}
	public ExcelValidateBean reg(String reg) {
		this.reg = Pattern.compile(reg);
		return this;
	}
	public Integer minLength() {
		return minLength;
	}
	public ExcelValidateBean minLength(Integer minLength) {
		this.minLength = minLength;
		return this;
	}
	public Integer maxLength() {
		return maxLength;
	}
	public ExcelValidateBean maxLength(Integer maxLength) {
		this.maxLength = maxLength;
		return this;
	}
	public Class<?> clazz() {
		return clazz;
	}
	public ExcelValidateBean clazz(Class<?> clazz) {
		this.clazz = clazz;
		return this;
	}
	public Integer minNum() {
		return minNum;
	}
	public ExcelValidateBean minNum(Integer minNum) {
		this.minNum = minNum;
		return this;
	}
	public Integer maxNum() {
		return maxNum;
	}
	public String dateFormat() {
		return dateFormat;
	}
	public ExcelValidateBean dateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
		return this;
	}
	public ExcelValidateBean maxNum(Integer maxNum) {
		this.maxNum = maxNum;
		return this;
	}
	public boolean require() {
		return require;
	}
	public ExcelValidateBean require(boolean require) {
		this.require = require;
		return this;
	}
	public String errKey() {
		return errKey;
	}
	public ExcelValidateBean errKey(String errKey) {
		this.errKey = errKey;
		return this;
	}
	public String errMsg() {
		return errMsg;
	}
	public ExcelValidateBean errMsg(String errMsg) {
		this.errMsg = errMsg;
		return this;
	}
}
