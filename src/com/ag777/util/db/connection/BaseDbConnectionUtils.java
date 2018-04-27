package com.ag777.util.db.connection;

import java.util.Map;
import java.util.Properties;

/**
 * 数据库连接工具类-基类
 * <p>
 * 提供一些通用方法
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月24日,last modify at 2018年04月25日
 */
public abstract class BaseDbConnectionUtils {

	protected static Properties getProperties(Map<String, Object> propMap) {
		Properties props = new Properties();
		if(propMap != null) {
			propMap.forEach((k,v)->{
				if(v != null) {
					props.put(k, v.toString());
				}
			});
		}
		return props;
	}
	
	public abstract int getDefaultPort();
}
