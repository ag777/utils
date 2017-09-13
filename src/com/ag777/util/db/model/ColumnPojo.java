package com.ag777.util.db.model;

public class ColumnPojo {

	private String name;
	private Integer sqlType;
	private Long size;
	private String remarks;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSqlType() {
		return sqlType;
	}
	public void setSqlType(Integer sqlType) {
		this.sqlType = sqlType;
	}
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	public String getRemarks() {
		return remarks;
	}
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
}
