package com.ag777.util.db.model;

/**
 * 数据库字段信息存放pojo
 * 
 * @author ag777
 * @version 
 */
public class ColumnPojo {

	private String name;				//字段名
	private Integer sqlType;		//sql类型
	private Long size;					//字段长度
	private String remarks;			//注释说明
	private Object defalut;			//默认值
	private Long charOctetLength;	//对于 char 类型，该长度是列中的最大字节数
	private Boolean isNotNull;	//是否不能为空
	private Boolean isAutoIncrement;	//是否自增
	private Boolean isPK;			//是否是主键
	
	
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
	public Object getDefalut() {
		return defalut;
	}
	public void setDefalut(Object defalut) {
		this.defalut = defalut;
	}
	public Long getCharOctetLength() {
		return charOctetLength;
	}
	public void setCharOctetLength(Long charOctetLength) {
		this.charOctetLength = charOctetLength;
	}
	public Boolean isNotNull() {
		return isNotNull;
	}
	public void isNotNull(Boolean isNotNull) {
		this.isNotNull = isNotNull;
	}
	public Boolean isAutoIncrement() {
		return isAutoIncrement;
	}
	public void isAutoIncrement(Boolean isAutoIncrement) {
		this.isAutoIncrement = isAutoIncrement;
	}
	public Boolean isPK() {
		return isPK;
	}
	public void isPK(Boolean isPK) {
		this.isPK = isPK;
	}
	
}
