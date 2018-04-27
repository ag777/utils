package com.ag777.util.db.model;

public enum OracleRole {

	NORMAL {
		@Override
		public String getName() {
			return "normal";
		}
	},
	SYSDBA {
		@Override
		public String getName() {
			return "sysdba";
		}
	},
	SYSOPER {
		@Override
		public String getName() {
			return "sysoper";
		}
	};
	
	public String getName() {
		return null;
	}
}
