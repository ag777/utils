package com.ag777.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.lang.model.Charsets;

/**
 * 针对属性文件的读写操作工具类
 * 
 * @author ag777
 * @version create on 2017年11月10日,last modify at 2018年11月20日
 */
public class PropertyHelper{

	public LinkedHashMap<String, KeyValue> keyValueMap;
	
	public PropertyHelper() {
		keyValueMap = MapUtils.newLinkedHashMap();
	}
	
	public PropertyHelper(String filePath) throws IOException {
		this(filePath, null);
	}
	
	public PropertyHelper(String filePath, Charset charset) throws IOException {
		this();
		if(charset == null) {
			charset = Charsets.UTF_8;
		}
		initByLine(FileUtils.readLines(filePath, charset));
	}
	
	public PropertyHelper(InputStream is) throws IOException {
		this(is, Charsets.UTF_8);
	}
	
	public PropertyHelper(InputStream is, Charset encoding) throws IOException {
		this();
		if(encoding == null) {
			encoding = Charsets.UTF_8;
		}
		initByLine(IOUtils.readLines(is, encoding));
	}
	
	public PropertyHelper(List<String> lines) {
		this();
		initByLine(lines);
	}
	
	//--查
	/**
	 * 获取键列表
	 * @return
	 */
	public List<String> keyList() {
		List<String> list = ListUtils.newArrayList();
		Iterator<String> itor = keyValueMap.keySet().iterator();
		while(itor.hasNext()) {
			list.add(itor.next());
		}
		return list;
	}
	
	/**
	 * 判断是否存在键
	 * @param key
	 * @return
	 */
	public boolean containKey(String key) {
		return keyValueMap.containsKey(key);
	}
	
	/**
	 * 获取key对应值
	 * @param key
	 * @return
	 */
	public Optional<String> getValue(String key) {
		try {
			return Optional.ofNullable(value(key).get());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public String getValue(String key, String defaultValue) {
		Optional<String> result = getValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为Long型
	 * @param key
	 * @return
	 */
	public Optional<Long> getLongValue(String key) {
		try {
			return Optional.ofNullable(value(key).getLong());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public Long getLongValue(String key, Long defaultValue) {
		Optional<Long> result = getLongValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为Integer型
	 * @param key
	 * @return
	 */
	public Optional<Integer> getIntValue(String key) {
		try {
			return Optional.ofNullable(value(key).getInt());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public Integer getIntValue(String key, Integer defaultValue) {
		Optional<Integer> result = getIntValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为Float型
	 * @param key
	 * @return
	 */
	public Optional<Float> getFloatValue(String key) {
		try {
			return Optional.ofNullable(value(key).getFloat());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public Float getFloatValue(String key, Float defaultValue) {
		Optional<Float> result = getFloatValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为Double型
	 * @param key
	 * @return
	 */
	public Optional<Double> getDoubleValue(String key) {
		try {
			return Optional.ofNullable(value(key).getDouble());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public Double getDoubleValue(String key, Double defaultValue) {
		Optional<Double> result = getDoubleValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为Boolean型
	 * @param key
	 * @return
	 */
	public Optional<Boolean> getBooleanValue(String key) {
		try {
			return Optional.ofNullable(value(key).getBoolean());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public Boolean getBooleanValue(String key, Boolean defaultValue) {
		Optional<Boolean> result = getBooleanValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取key对应值并转为java.util.Date型
	 * @param key
	 * @return
	 */
	public Optional<java.util.Date> getDateValue(String key) {
		try {
			return Optional.ofNullable(value(key).getDate());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	public java.util.Date getDateValue(String key, java.util.Date defaultValue) {
		Optional<java.util.Date> result = getDateValue(key);
		if(result.isPresent()) {
			return result.get();
		}
		return defaultValue;
	}
	
	/**
	 * 通过key获取对应的值，不存在抛出RuntimeException异常
	 * @param key
	 * @return
	 */
	public KeyValue value(String key) {
		KeyValue item = MapUtils.get(keyValueMap, key);
		if(item == null) {
			throw new RuntimeException(
					StringUtils.concat("key[", key, "] not found"));
		}
		return item;
	}
	
	//--修改
	/**
	 * 插入或更新
	 * @param key
	 * @param value
	 * @return
	 */
	public PropertyHelper addOrUpdate(String key, Object value) {
		KeyValue item = MapUtils.get(keyValueMap, key);
		if(item != null) {
			item.set(value);
		} else {
			keyValueMap.put(key, new KeyValue(key, value));
		}
		return this;
	}
	
	/**
	 * 插入或更新(带注释)
	 * @param key
	 * @param value
	 * @param noteList
	 * @return
	 */
	public PropertyHelper addOrUpdate(String key, Object value, List<String> noteList) {
		KeyValue item = MapUtils.get(keyValueMap, key);
		if(item != null) {
			item.set(value).noteList(noteList);
		} else {
			keyValueMap.put(key, new KeyValue(key, value).noteList(noteList));
		}
		return this;
	}
	
	/**
	 * 更新值，如果键不存在则不作操作
	 * @param key
	 * @param value
	 * @return
	 */
	public PropertyHelper update(String key, String value) {
		KeyValue item = MapUtils.get(keyValueMap, key);
		if(item != null) {
			item.set(value);
		}
		return this;
	}
	
	public PropertyHelper update(String key, String value, List<String> noteList) {
		KeyValue item = MapUtils.get(keyValueMap, key);
		if(item != null) {
			item.set(value).noteList(noteList);
		}
		return this;
	}
	
	/**
	 * 删除key(包括上方的注释)
	 * @param key
	 * @return
	 */
	public PropertyHelper remove(String key) {
		keyValueMap.remove(key);
		return this;
	}
	
	//--转化
	public List<String> toLines() {
		List<String> lines = ListUtils.newArrayList();
		if(MapUtils.isEmpty(keyValueMap)) {
			return lines;
		}
		
		keyValueMap.forEach((key, keyValue)->{
			if(!ListUtils.isEmpty(keyValue.noteList)) {
				lines.add("");	//在注释前插入空行
				keyValue.noteList.forEach(noteLine->{	//注释行
					lines.add("#"+noteLine);
				});
			}
			lines.add(
					StringUtils.concat(key, '=', keyValue.value));
		});
		return lines;
	}
	
	/**
	 * 转化为iniHelper
	 * @param defaultSection
	 * @return
	 */
	public IniHelper toIniHelper(String defaultSection) {
		IniHelper ih = new IniHelper();
		if(MapUtils.isEmpty(keyValueMap)) {
			return ih;
		}
		keyValueMap.forEach((key, item)->{
			String sectionKey = "";	//标签名
			String keyTmp = "";	//去除标签名后的key
			String[] group = key.split("\\.",2);	//最多分两组
			if(group.length>1) {	//存在分组 比如a.b 分组名为a
				sectionKey = group[0];
				keyTmp = group[1];
				
			} else {	//不存在分组名 比如a
				sectionKey = defaultSection;
				keyTmp = group[0];
			}
			
			ih.addOrUpadate(sectionKey, keyTmp, item.get());	//插入键值对
			try {
				ih.section(sectionKey).value(keyTmp).noteList(item.noteList);	//更新注释
			} catch(Exception ex) {
			}
		});
		return ih;
	}
	
	//--输出
	/**
	 * 保存到文件
	 * @param filePath
	 * @param charset
	 * @throws IOException
	 */
	public void save(String filePath, Charset charset) throws IOException {
		FileUtils.write(filePath, toLines(), charset);
	}
	
	/**
	 * 保存到文件
	 * @param filePath
	 * @throws IOException
	 */
	public void save(String filePath) throws IOException {
		save(filePath, Charsets.UTF_8);
	}
	
	/**
	 * 将内容写出到io流
	 * @param os
	 * @throws IOException
	 */
	public void save(OutputStream os) throws IOException {
		save(os, Charsets.UTF_8);
	}
	
	/**
	 * 将内容写出到io流
	 * @param os
	 * @throws IOException
	 */
	public void save(OutputStream os, Charset charset) throws IOException {
		IOUtils.write(toLines(), os, charset, IOUtils.BUFFSIZE);
	}
	
	/**
	 * 保存到相对src的位置下
	 * @param path
	 * @param clazz
	 * @throws IOException
	 */
	public void saveBaseSrcPath(String path, Class<?> clazz) throws IOException {
		FileUtils.write(PathUtils.srcPath(clazz)+path, toLines(), Charsets.UTF_8);
	}
	
	
	//--内部方法
	/**
	 * 通过内容行来初始化property工具类对象
	 * @param lines
	 */
	private void initByLine(List<String> lines) {
		if(lines == null) {
			return;
		}
		
		List<String> noteList = ListUtils.newArrayList();
		/*正则区*/
		Pattern p_note = Pattern.compile("^\\#(.*)$");				//注释
		Pattern p_pair = Pattern.compile("^([^=]+)=([^=]*)$");		//键值对(key一定要有，值可以没有，否则会被忽略)
		
		for (String line : lines) {
			line = line.trim();
			String note = RegexUtils.find(line, p_note, "$1");
			if(note != null) {	//这是一个注释行
				noteList.add(note.trim());
			}
			
			String keyValue = RegexUtils.find(line, p_pair, "$1=$2");
			if(keyValue != null) {		//这是一个键值对行
				String[] group = keyValue.split("=");
				String key = group[0].trim();
				keyValueMap.put(key, new KeyValue(key, group.length>1?group[1].trim():"").noteList(noteList));
				noteList = ListUtils.newArrayList();	//清空注释
			}
		}
	}

	
	public class KeyValue {
		private List<String> noteList;
		private String key;
		private String value;
		
		public KeyValue(String key, Object value) {
			if(value == null) {
				value = "";
			}
			this.key = key;
			this.value = value.toString();
		}
		
		public KeyValue set(Object obj) {
			if(obj == null) {
				value = "";
			} else {
				value = obj.toString();
			}
			return KeyValue.this;
		}
		
		public String key() {
			return key;
		}
		
		public String get() {
			return value;
		}
		
		public Long getLong() {
			return StringUtils.toLong(value);
		}
		
		public Integer getInt() {
			return StringUtils.toInt(value);
		}
		
		public Float getFloat() {
			return StringUtils.toFloat(value);
		}
		
		public Double getDouble() {
			return StringUtils.toDouble(value);
		}
		
		public Boolean getBoolean() {
			return StringUtils.toBoolean(value);
		}
		
		public java.util.Date getDate() {
			return StringUtils.toDate(value);
		}
		
		public KeyValue noteList(List<String> noteList) {
			if(noteList == null) {
				noteList = ListUtils.newArrayList();
			}
			this.noteList = noteList;
			return KeyValue.this;
		}
		
		public List<String> noteList() {
			return noteList;
		}
		
	}
	
	public static void main(String[] args) throws IOException {
		PropertyHelper ph = new PropertyHelper();
		String path = "e:\\xx.properties";	//测试路径
		String iniPath = "e:\\xx.ini";	//测试路径
		ph.addOrUpdate("a.b", "值a.b", ListUtils.of("待改值"))
			.addOrUpdate("a.c", "值a.c", ListUtils.of("不变的注释"))
			.addOrUpdate("num", 33, ListUtils.of("我是说明","数值","啊哈哈"))
			.save(path);
		
		PropertyHelper ph2 = new PropertyHelper(path);
		System.out.println(ph2.getIntValue("num").get());
		ph2.addOrUpdate("a.b", "值a.b改", null)
			.addOrUpdate("c.d", "新值", ListUtils.of("我是说明")).save(path);
		
		PropertyHelper ph3 = new PropertyHelper(path);
		System.out.println(ph3.getValue("c.d", "未知"));
		ph3.toIniHelper("default").save(iniPath);
	}
}
