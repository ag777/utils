package com.ag777.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 针对属性文件的读写操作工具类
 * @Description 针对属性文件的读写操作工具类
 * @version 1.0 
 * @date 2015年4月4日 下午9:26:56 last modify 2017-08-03
 */
public class PropertyUtils extends java.util.Properties{

	private static final long serialVersionUID = 5668981040083393897L;

	/**
	 * 加载文件
	 * @param filePath
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public PropertyUtils load(String filePath) throws FileNotFoundException, IOException {
		load(new FileInputStream(new File(filePath)));
		return this;
	}
	
	public String get(String key) {
		return getProperty(key);
	}
	
	public String get(String key, String defaultValue) {
		return getProperty(key, defaultValue);
	}
	
	public Integer getInt(String key) {
		return getInt(key, null);
	}
	
	public Integer getInt(String key, Integer defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			return Integer.parseInt(value.trim());
		}
		return defaultValue;
	}
	
	public Long getLong(String key) {
		return getLong(key, null);
	}
	
	public Long getLong(String key, Long defaultValue) {
		String value = getProperty(key);
		if (value != null) {
			return Long.parseLong(value.trim());
		}
		return defaultValue;
	}
	
	public Boolean getBoolean(String key) {
		return getBoolean(key, null);
	}
	
	public Boolean getBoolean(String key, Boolean defaultValue) {
		String value = getProperty(key);
		if (value != null && !"".equals(value)) {
			value = value.toLowerCase().trim();
			if ("true".equals(value)) {
				return true;
			} else if ("false".equals(value)) {
				return false;
			}
			throw new RuntimeException("The value can not parse to Boolean : " + value);
		}
		return defaultValue;
	}
	
	public boolean containsKey(String key) {
		return containsKey(key);
	}
	
	@Override
	public String getProperty(String key) {
		String temp = super.getProperty(key);
		if(temp != null) {
			temp.trim();
		}
		return temp;
	}
}
