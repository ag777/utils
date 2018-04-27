package com.ag777.util.db.model;

public enum DbDriver {

	MYSQL {
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
