package com.ag777.util.db.model;


/**
 * 数据库字段信息存放pojo
 * 
 * @author ag777
 * @version 
 */
public class TypePojo {

	private String field;
	private String type;
	private Boolean nullAble;
	private String extra;
	private String key;
	
	public String getField() {
		return field;
	}
	public TypePojo setField(String field) {
		this.field = field;
		return this;
	}
	public String getType() {
		return type;
	}
	public TypePojo setType(String type) {
		this.type = type;
		return this;
	}
	public Boolean getNullAble() {
		return nullAble;
	}
	public TypePojo setNullAble(Boolean nullAble) {
		this.nullAble = nullAble;
		return this;
	}
	public String getExtra() {
		return extra;
	}
	public TypePojo setExtra(String extra) {
		this.extra = extra;
		return this;
	}
	public String getKey() {
		return key;
	}
	public TypePojo setKey(String key) {
		this.key = key;
		return this;
	}
	
}
