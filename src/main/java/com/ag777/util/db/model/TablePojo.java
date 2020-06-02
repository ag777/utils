package com.ag777.util.db.model;

/**
 * 数据库字段信息存放pojo
 * 
 * @author ag777
 * @version 
 */
public class TablePojo {

	private String cat; 		//1. 表所在的编目(可能为空) 
	private String schem; 	//2. 表所在的模式(可能为空) 
	private String name; 	//3. 表名称
	private String type; 		//4. 表类型,典型的有 "TABLE", "VIEW", "SYSTEM TABLE", "GLOBAL TEMPORARY", "LOCAL  TEMPORARY", "ALIAS", "SYNONYM". 
	private String remark; 	//5. 解释性的备注
	private String typeCat; 	//6. 编目类型(may be null) 
	private String typeSchem; 	//7. 模式类型(may be null) 
	private String typeName; 	//8. 类型名称(may be null) 
//	private String SELF_REFERENCING_COL_NAME; 		//9. name of the designated "identifier" column of a typed table (may be null) 
//	private String REF_GENERATION; 		//10. specifies how values in SELF_REFERENCING_COL_NAME are created.它的值有："SYSTEM"、"USER"、"DERIVED"，也可能为空。
	public String getCat() {
		return cat;
	}
	public void setCat(String cat) {
		this.cat = cat;
	}
	public String getSchem() {
		return schem;
	}
	public void setSchem(String schem) {
		this.schem = schem;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getTypeCat() {
		return typeCat;
	}
	public void setTypeCat(String typeCat) {
		this.typeCat = typeCat;
	}
	public String getTypeSchem() {
		return typeSchem;
	}
	public void setTypeSchem(String typeSchem) {
		this.typeSchem = typeSchem;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
}
