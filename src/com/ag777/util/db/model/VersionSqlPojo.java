package com.ag777.util.db.model;

import java.util.ArrayList;
import java.util.List;

/**
 *  升级数据库用的存放版本对应sql的容器类
 * 
 * @author ag777
 * @version create on 2017年08月18日,last modify at 2017年08月18日
 */
public class VersionSqlPojo {


    /**
     * code : 1.0.0.0
     * ddlList : [{"sql":"","rollback":""}]
     * dmlList : [""]
     */

    private String code;
    /**
     * sql :
     * rollback :
     */

    private List<DdlListBean> ddlList;	//数据库结构操作是不能回滚的
    private List<String> dmlList;		//对数据操作是可以回滚的

    public VersionSqlPojo() {
        ddlList = new ArrayList<>();
        dmlList = new ArrayList<>();
    }
    
    public VersionSqlPojo(String code) {
        this();
        this.code = code;
    }
    
    public VersionSqlPojo addDdl(String sql, String rollBack) {
    	ddlList.add(new DdlListBean(sql, rollBack));
    	return this;
    }
    
    public VersionSqlPojo addDdl(String sql) {
    	ddlList.add(new DdlListBean(sql));
    	return this;
    }
    
    public VersionSqlPojo addDml(String sql) {
    	dmlList.add(sql);
    	return this;
    }

    public String getCode() {
        return code;
    }

    public VersionSqlPojo setCode(String code) {
        this.code = code;
        return this;
    }

    public List<DdlListBean> getDdlList() {
        return ddlList;
    }

    public void setDdlList(List<DdlListBean> ddlList) {
        this.ddlList = ddlList;
    }

    public List<String> getDmlList() {
        return dmlList;
    }

    public void setDmlList(List<String> dmlList) {
        this.dmlList = dmlList;
    }

    public static class DdlListBean {
        private String sql;
        private String rollback;
        private Boolean isForce;	//为true时代表这条sql必须执行成功，否则执行rollback（）预留字段

        public DdlListBean() {
        }

        public DdlListBean(String sql) {
            this.sql = sql;
        }

        public DdlListBean(String sql, String rollback) {
            this.sql = sql;
            this.rollback = rollback;
        }

        public DdlListBean(String sql, String rollback, Boolean isForce) {
			super();
			this.sql = sql;
			this.rollback = rollback;
			this.isForce = isForce;
		}

		public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
        }

        public String getRollback() {
            return rollback;
        }

        public void setRollback(String rollback) {
            this.rollback = rollback;
        }

		public Boolean getIsForce() {
			return isForce;
		}

		public void setIsForce(Boolean isForce) {
			this.isForce = isForce;
		}
    }
}
