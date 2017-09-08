package com.ag777.util.file;

import java.util.regex.Pattern;

public enum FileAnnotation {
	TYPE_CODE_LINE {	// //
		@Override
		public boolean isLineOnly() {
			return true;
		}
		@Override
		public String startSign() {
			return "//";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("//.*");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("//.*");
		}
		@Override
		public String toString() {
			return "//注释内容";
		}
	},
	TYPE_CODE_PARAGRAPH {			// /* */
		@Override
		public String startSign() {
			return "/*";
		}
		@Override
		public String endSign() {
			return "*/";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("/\\*.*\\*/");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("/\\*.*");
		}
		@Override
		public Pattern endPattern() {
			return Pattern.compile(".*\\*/");
		}
		@Override
		public String toString() {
			return "/*注释内容*/";
		}
	},
	TYPE_CODE_PARAGRAPH_DOC {	// /** */
		@Override
		public String startSign() {
			return "/**";
		}
		@Override
		public String endSign() {
			return "*/";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("/\\*\\*.*\\*/");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("/\\*\\*.*");
		}
		@Override
		public Pattern endPattern() {
			return Pattern.compile(".*\\*/");
		}
		@Override
		public String toString() {
			return "/**注释内容*/";
		}
	},
	TYPE_XML_PARAGRAPH {				// <!--注释内容-->
		@Override
		public String startSign() {
			return "<!--";
		}
		@Override
		public String endSign() {
			return "-->";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("\\<!--.*--\\>");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("\\<!--.*");
		}
		@Override
		public Pattern endPattern() {
			return Pattern.compile(".*--\\>");
		}
		@Override
		public String toString() {
			return "<!--注释内容-->";
		}
	},
	TYPE_XML_LINE {							// #
		@Override
		public boolean isLineOnly() {
			return true;
		}
		@Override
		public String startSign() {
			return "#";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("#.*");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("#.*");
		}
		@Override
		public String toString() {
			return "#注释内容";
		}
	},
	TYPE_WEB_PARAGRAPH {				//<%----%>
		@Override
		public String startSign() {
			return "<%--";
		}
		@Override
		public String endSign() {
			return "--%>";
		}
		@Override
		public Pattern pattern() {
			return Pattern.compile("\\<%--.*--\\>");
		}
		@Override
		public Pattern startPattern() {
			return Pattern.compile("\\<%--.*");
		}
		@Override
		public Pattern endPattern() {
			return Pattern.compile(".%--\\>");
		}
		@Override
		public String toString() {
			return "<%--注释内容--%>";
		}
	};
	
	/**
	 * 是否只作用于一行
	 * @return
	 */
	public boolean isLineOnly() {
		return false;
	}
	
	public String startSign() {
		return null;
	}
	
	public String endSign() {
		return null;
	}
	
	public Pattern startPattern() {
		return null;
	}
	
	public Pattern endPattern() {
		return null;
	}
	public Pattern pattern() {
		return null;
	}
	
	public String toString() {
		return null;
	}
}
