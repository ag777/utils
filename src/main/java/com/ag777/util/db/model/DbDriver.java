package com.ag777.util.db.model;

public enum DbDriver {

	//mysql-connector-java 6
	MYSQL {
		@Override
		public String getName() {
			return "com.mysql.cj.jdbc.Driver";
		}
	},
	//mysql-connector-java 5
	MYSQL_OLD {
		@Override
		public String getName() {
			return "com.mysql.jdbc.Driver";
		}
	},
	ORACLE {
		@Override
		public String getName() {
			return "oracle.jdbc.driver.OracleDriver";
		}
	},
	SQLSERVER{
		@Override
		public String getName() {
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		}
	},
	DB2{
		@Override
		public String getName() {
			return "com.ibm.db2.jcc.DB2Driver";
		}
	},
	SQLITE {
		@Override
		public String getName() {
			return "org.sqlite.JDBC";
		}
	};
	public String getName() {
		return null;
	}
}
