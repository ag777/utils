package com.ag777.util.lang.model;

/**
 * 正则规则
 * 
 * @author LinCH
 *
 */
public class RegexRule {

	/**
	 * 统一社会信用代码（Unified social credit code ）
		统一代码为18位，统一代码由十八位的数字或大写英文字母（不适用I、O、Z、S、V）组成，由五个部分组成：
		第一部分（第1位）为登记管理部门代码，9表示工商部门；(数字或大写英文字母)
		第二部分（第2位）为机构类别代码;(数字或大写英文字母)
		第三部分（第3-8位）为登记管理机关行政区划码；(数字)
		第四部分（第9-17位）为全国组织机构代码；(数字或大写英文字母)
		第五部分（第18位）为校验码(数字或大写英文字母)
		例如:91350100M000100Y43
	 */
	public static String USCC = "[0-9A-HJ-NPQRTUWXY]{2}\\d{6}[0-9A-HJ-NPQRTUWXY]{10}";
	
	/**
	 * 邮箱
	 */
	public static class Email {
		public static final String BASIC = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
		/** 与jQuery.validator.js中的ip规则对应	*/
		public static final String EMAIL = "^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$";
	}

	
	public static void main(String[] args) {
		System.out.println(Email.EMAIL);
	}
	/**
	 * IP格式
	 */
	public static class Ip {

		public static final String IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
		/** ipv4 + : + 端口, 格式 ： 127.0.0.1:8080	*/
		public static final String IPV4_Port = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}:\\d{0,5}$";
		/**	标准的ipv6格式	*/
		public static final String IPV6_STD = "^(?:[0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}$";
		/** 16进制的ipv6格式	*/
		public static final String IPV6_HEX = "^((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)::((?:[0-9A-Fa-f]{1,4}(?::[0-9A-Fa-f]{1,4})*)?)$";
		/** 所有的ipv6	*/
		public static final String IPV6 = IPV6_STD + "|" + IPV6_HEX;
		/** 小写域名匹配	*/
		public static final String DOMAIN = "^((https?|ftp|news):\\/\\/)?([a-z]([a-z0-9\\-]*[\\.。])+([a-z]{2}|aero|arpa|biz|com|coop|edu|gov|info|int|jobs|mil|museum|name|nato|net|org|pro|travel)|(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5]))(\\/[a-z0-9_\\-\\.~]+)*(\\/([a-z0-9_\\-\\.]*)(\\?[a-z0-9+_\\-\\.%=&]*)?)?(#[a-z][a-z0-9_]*)?$";
		/** 掩码	*/
		public static final String MASK = "^(254|252|248|240|224|192|128|0)\\.0\\.0\\.0|255\\.(254|252|248|240|224|192|128|0)\\.0\\.0|255\\.255\\.(254|252|248|240|224|192|128|0)\\.0|255\\.255\\.255\\.(254|252|248|240|224|192|128|0)$";
		/** 所有的ip格式	*/
		public static final String IP_ADDR = IPV4 + "|" + IPV6;
		/** 与jQuery.validator.js中的ip规则对应	*/
		public static final String IP = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\.(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
	}
	
	/**
	 * 端口号
	 */
	public static class Port {
		public static final String DEFAULT = "^([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5])(-([1-9]\\d{0,3}|[1-5]\\d{4}|6[0-5]{2}[0-3][0-5]))?$";	//1-65535
	}
	
	/**
	 * 电话号码
	 */
	public static class PHONE {
		/** 与jQuery.validator.js中的isMobile规则对应	*/
		public static final String MOBILE = "^1\\d{10}$";
		/** 与jQuery.validator.js中的isPhone规则对应	*/
		public static final String PHONE = "^((0\\d{2,3})-)?(\\d{7,8})$";
		/** 与jQuery.validator.js中的isPhoneOrMobile规则对应	*/
		public static final String PHONE_OR_MOBILE = PHONE + "|" + MOBILE;
		
	}
	
	/**
	 * 身份证号
	 */
	public static class IDNumber {
		public static final String DEFAULT = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";	//18位身份证号
	}
}
