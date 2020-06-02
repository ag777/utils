package com.ag777.util.db.model;

import java.util.List;

/**
 * 数据库字段信息存放pojo
 * 
 * @author ag777
 * @version 
 */
public class DBIPojo {

	public String name;
	public List<String> columnNameList;
	public int type;
	public String typeName;
	public boolean unique;
	
	public String getName() {
		return name;
	}
	public DBIPojo setName(String name) {
		this.name = name;
		return this;
	}
	public List<String> getColumnNameList() {
		return columnNameList;
	}
	public DBIPojo setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
		return this;
	}
	public int getType() {
		return type;
	}
	public DBIPojo setType(int type) {
		this.type = type;
		return this;
	}
	public String getTypeName() {
		return typeName;
	}
	public DBIPojo setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}
	public boolean isUnique() {
		return unique;
	}
	public DBIPojo setUnique(boolean unique) {
		this.unique = unique;
		return this;
	}
	
}
