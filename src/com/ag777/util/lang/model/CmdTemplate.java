package com.ag777.util.lang.model;

/**
 * 部分cmd命令存放类
 * 
 * @author ag777
 *	
 */
public class CmdTemplate {

	public static final String SHELL_CPU_ID = "dmidecode -t 4 | grep ID |sort -u |awk -F': ' '{print $2}'";		//获取cpu_id,一行一个,linux和麒麟系统通用
	public static final String SHELL_MAC = "ifconfig -a|grep \"HWaddr\"|awk {'print $5'}";							//获取mac地址,一行一个
	
	public static final String KYLIN_SHELL_MAC = "ifconfig -a|grep \"ether\"|awk {'print $2'}";					//获取mac地址,一行一个,麒麟系统
	
}
