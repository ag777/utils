package com.ag777.util.file;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.lang.model.Charsets;

/**
 * Ini 文件读写工具类
 * 
 * @author ag777
 * @version create on 2017年11月03日,last modify at 2017年11月08日
 */
public class IniUtils {
	/* 区块 */
	private LinkedHashMap<String, Section> sectionMap;
	
	//--构造函数
	
	public IniUtils() {
		sectionMap = MapUtils.newLinkedHashMap();
	}
	public IniUtils(String filePath) throws IOException {
		this(filePath, Charsets.utf8());
	}
	public IniUtils(String filePath, Charset charset) throws IOException {
		this();
		if(charset == null) {
			charset = Charsets.utf8();
		}
		List<String> lines = FileUtils.readLines(filePath, charset);
		initByLines(lines);
	}
	public IniUtils(InputStream inputStream) throws IOException{
		this(inputStream, Charsets.utf8());
	}
	public IniUtils(InputStream inputStream, Charset charset) throws IOException {
		this();
		if(charset == null) {
			charset = Charsets.utf8();
		}
		List<String> lines = IOUtils.readLines(inputStream, charset.toString());
		initByLines(lines);
	}
	public IniUtils(List<String> lines) {
		this();
		initByLines(lines);
	}
	
	
	//--取值
	/**
	 * 获取所有标签
	 * @return
	 */
	public List<String> getSectionList() {
		List<String> list = ListUtils.newArrayList();
		Iterator<String> itor = sectionMap.keySet().iterator();
		while(itor.hasNext()) {
			list.add(itor.next());
		}
		return list;
	}
	/**
	 * 获取某个标签下所有键
	 * @param sectionKey
	 * @return
	 */
	public List<String> getKeyList(String sectionKey) {
		Section section = MapUtils.get(sectionMap, sectionKey);
		if(section == null) {
			return ListUtils.newArrayList();
		} else {
			return section.keyList();
		}
	}
	/**
	 * 获取值
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<String> getValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).get());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public String getValue(String sectionKey, String valueKey, String defaultValue) {
		Optional<String> value = getValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转换为Integer类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Integer> getIntValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).intValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值并转换为Integer类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Integer getIntValue(String sectionKey, String valueKey, Integer defaultValue) {
		Optional<Integer> value = getIntValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转化为Long类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Long> getLongValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).longValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值并转化为Long类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Long getLongValue(String sectionKey, String valueKey, Long defaultValue) {
		Optional<Long> value = getLongValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转化为Float类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Float> getFloatValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).floatValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值并转化为Float类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Float getFloatValue(String sectionKey, String valueKey, Float defaultValue) {
		Optional<Float> value = getFloatValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转化为Double类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Double> getDoubleValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).doubleValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return null;
	}
	
	/**
	 * 获取值并转化为Double类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Double getDoubleValue(String sectionKey, String valueKey, Double defaultValue) {
		Optional<Double> value = getDoubleValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转化为Boolean类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Boolean> getBooleanValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).booleanValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值并转化为Boolean类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBooleanValue(String sectionKey, String valueKey, Boolean defaultValue) {
		Optional<Boolean> value = getBooleanValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 获取值并转化为Date类型
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public Optional<Date> getDateValue(String sectionKey, String valueKey) {
		try {
			return Optional.ofNullable(section(sectionKey).value(valueKey).dateValue());
		} catch(Exception ex) {
//			Console.err(ex.getMessage());
		}
		return Optional.empty();
	}
	
	/**
	 * 获取值并转化为Double类型
	 * @param sectionKey
	 * @param valueKey
	 * @param defaultValue
	 * @return
	 */
	public Date getDateValue(String sectionKey, String valueKey, Date defaultValue) {
		Optional<Date> value = getDateValue(sectionKey, valueKey);
		if(value.isPresent()) {
			return value.get();
		}
		return defaultValue;
	}
	
	/**
	 * 通过标签名获取对应标签，不存在抛出异常
	 * @param section
	 * @return
	 */
	public Section section(String section) {
		Section result = MapUtils.get(sectionMap, section);
		if(result == null) {
			throw new RuntimeException(
					StringUtils.concat("section[", section, "] not found"));
		}
		return result;
	}
	
	//--修改
	/**
	 * 插入新section(标签),旧的同名标签会被顶替
	 * @param section
	 * @return
	 */
	public IniUtils addSection(Section section) {
		sectionMap.put(section.name, section);
		return this;
	}
	
	/**
	 * 替换原有的值,不存在会抛出异常
	 * @param sectionKey
	 * @param valueKey
	 * @param value
	 * @return
	 */
	public IniUtils update(String sectionKey, String valueKey, Object value) {
		section(sectionKey).put(valueKey, value);
		return this;
	}
	
	/**
	 * 不存在则新建，存在则替换
	 * @param sectionKey
	 * @param valueKey
	 * @param value
	 * @return
	 */
	public IniUtils addOrUpadate(String sectionKey, String valueKey, Object value) {
		Section section = MapUtils.get(sectionMap, sectionKey);
		if(section == null) {
			section = newSection(sectionKey);
			sectionMap.put(sectionKey, section);
		}
		section.put(valueKey, value);	//存在则覆盖，没有则插入
		return this;
	}
	
	/**
	 * 删除某个标签下的键
	 * @param sectionKey
	 * @param valueKey
	 * @return
	 */
	public IniUtils removeKey(String sectionKey, String valueKey) {
		Section section = MapUtils.get(sectionMap, sectionKey);
		if(section != null) {
			section.removeKey(valueKey);
		}
		return this;
	}
	
	/**
	 * 删除某个标签
	 * @param sectionKey
	 * @return
	 */
	public IniUtils removeSection(String sectionKey) {
		if(sectionMap.containsKey(sectionKey)) {
			sectionMap.remove(sectionKey);
		}
		return this;
	}
	
	//--builder
	/**
	 * 创建section(内部类只能这么创建)
	 * @param sectionKey
	 * @return
	 */
	public Section newSection(String sectionKey) {
		return new Section(sectionKey);
	}
	
	/**
	 * 创建section(内部类只能这么创建)
	 * @param sectionKey
	 * @param noteList
	 * @return
	 */
	public Section newSection(String sectionKey, List<String> noteList) {
		return new Section(sectionKey).noteList(noteList);
	}
	
	
	//--转化
	/**
	 * 转化为行列表
	 * @return
	 */
	public List<String> toLines() {
		List<String> lines = ListUtils.newArrayList();
		if(sectionMap == null || sectionMap.isEmpty()) {
			return lines;
		}
		sectionMap.forEach((secKey,section)->{	//标签名，标签具体内容
			if(!ListUtils.isEmpty(section.noteList)) {
				section.noteList.forEach(noteLine->{	//标签前的注释
					lines.add("#"+noteLine);
				});
			}
			lines.add(StringUtils.concat('[', secKey, "]"));	//标签名
			if(!MapUtils.isEmpty(section.valueMap)) {
				section.valueMap.forEach((key, val)->{	//键值对
					if(!ListUtils.isEmpty(val.noteList)) {
						val.noteList.forEach(noteLine->{
							lines.add("#"+noteLine);
						});
					}
					lines.add(StringUtils.concat(key, "=", val));
				});
			}
			lines.add("");	//标签(模块之间隔出一行空行)
		});
		
		return lines;
	}
	
	/**
	 * 转化为行列表
	 * <p>
	 * 会在每个键前面加上标签名做区分
	 * </p>
	 * @return
	 */
	public List<String> toPropertiesLines() {
		List<String> lines = ListUtils.newArrayList();
		if(sectionMap == null || sectionMap.isEmpty()) {
			return lines;
		}
		sectionMap.forEach((secKey,section)->{	//标签名，标签具体内容
			if(!ListUtils.isEmpty(section.noteList)) {
				section.noteList.forEach(noteLine->{	//标签前的注释
					lines.add("#"+noteLine);
				});
			}
			
			if(!MapUtils.isEmpty(section.valueMap)) {
				section.valueMap.forEach((key, val)->{	//键值对
					if(!ListUtils.isEmpty(val.noteList)) {
						val.noteList.forEach(noteLine->{
							lines.add("#"+noteLine);
						});
					}
					lines.add(StringUtils.concat(secKey, ".", key, "=", val));
				});
			}
			lines.add("");	//标签(模块之间隔出一行空行)
		});
		
		return lines;
	}
	
	/**
	 * 保存到文件
	 * @param filePath
	 * @param charset
	 * @throws IOException
	 */
	public void save(String filePath, Charset charset) throws IOException {
		FileUtils.write(filePath, toLines(), charset.toString(), true);
	}
	
	/**
	 * 保存到文件
	 * @param filePath
	 * @param charset
	 * @throws IOException
	 */
	public void save(String filePath) throws IOException {
		FileUtils.write(filePath, toLines(), Charsets.UTF_8, true);
	}
	
	/**
	 * 保存为properties文件
	 * @param filePath
	 * @throws IOException
	 */
	public void saveAsProperties(String filePath) throws IOException {
		FileUtils.write(filePath, toPropertiesLines(), Charsets.UTF_8, true);
	}
	
	//--内部方法
	/**
	 * 通过内容行来初始化ini工具类对象
	 */
	private void initByLines(List<String> lines) {
		if(lines == null) {
			return;
		}
		
		List<String> noteList = ListUtils.newArrayList();
		Section sectionTmp = null;	//临时指针
		/*正则区*/
		Pattern p_note = Pattern.compile("^\\#(.*)$");				//注释
		Pattern p_section = Pattern.compile("^\\[([^\\]]+)\\]$");	//模块名
		Pattern p_pair = Pattern.compile("^([^=]+)=([^=]*)$");		//键值对(key一定要有，值可以没有，否则会被忽略)
		
		for (String line : lines) {
			line = line.trim();
			String note = RegexUtils.find(line, p_note, "$1");
			if(note != null) {	//这是一个注释行
				noteList.add(note.trim());
			}
			String sectionKey = RegexUtils.find(line, p_section, "$1");
			if(sectionKey != null) {	//这是一个模块行[xx] xx为模块名
				sectionKey = sectionKey.trim();
				sectionTmp = newSection(sectionKey, noteList);
				sectionMap.put(sectionKey, sectionTmp);
				noteList = ListUtils.newArrayList();	//清空note
			}
			String keyValue = RegexUtils.find(line, p_pair, "$1=$2");
			if(keyValue != null) {
				if(sectionTmp == null) {
					throw new RuntimeException("存在键值对没有归属模块");
				} else {
					String[] group = keyValue.split("=");
					sectionTmp.put(group[0].trim(), group.length>1?group[1].trim():"", noteList);
					noteList = ListUtils.newArrayList();		//清空note
				}
			}
			
		}
	}
	
	/**
	 * 内部区块类
	 * 
	 */
	public class Section {
		/* 区块关键字 */
		private String name;
		/* 区块注释 */
		private List<String> noteList;
		/*键值对*/
		private LinkedHashMap<String, Value> valueMap = MapUtils.newLinkedHashMap();

		public Section(String name) {
			this.name = name;
		}
		
		public String name() {
			return name;
		}
		
		private Section noteList(List<String> noteList) {
			this.noteList = noteList;
			return Section.this;
		}
		
		public List<String> noteList() {
			return noteList;
		}
		
		public List<String> keyList() {
			List<String> list = ListUtils.newArrayList();
			Iterator<String> itor = valueMap.keySet().iterator();
			while(itor.hasNext()) {
				list.add(itor.next());
			}
			return list;
		}
		
		public Section put(String key, Object value) {
			if(value == null) {
				value = "";
			}
			valueMap.put(key, new Value(value.toString(), null));
			return Section.this;
		}
		public Section put(String key, String value, List<String> noteList) {
			valueMap.put(key, new Value(value, noteList));
			return Section.this;
		}
		public Section removeKey(String key) {
			if(valueMap.containsKey(key)) {
				valueMap.remove(key);
			}
			return Section.this;
		}
		public Map<String, Value> valueMap() {
			return valueMap;
		}
		
		/**
		 * 通过键获取对应的值，不存在抛出异常
		 * @param key
		 * @return
		 */
		public Value value(String key) {
			Value result = MapUtils.get(valueMap, key);
			if(result == null) {
				throw new RuntimeException(
						StringUtils.concat("key[", key,"] not found in section[", name, ']'));
			}
			return result;
		}
	}
	
	/**
	 * 内部值类
	 *
	 */
	public class Value {
		/*值*/
		private String value;
		/* 区块注释 */
		private List<String> noteList;
		
		public Value(String value) {
			this.value = value;
		}
		public Value(String value, List<String> noteList) {
			this.value = value;
			this.noteList = noteList;
		}
		
		public List<String> noteList() {
			return noteList;
		}
		
		public String get() {
			return value;
		}
		
		public IniUtils set(String value) {
			this.value = value;
			return IniUtils.this;
		}
		@Override
		public String toString() {
			return value;
		}
		public Integer intValue() {
			return StringUtils.toInteger(value);
		}
		public Long longValue() {
			return StringUtils.toLong(value);
		}
		public Float floatValue() {
			return StringUtils.toFloat(value);
		}
		public Double doubleValue() {
			return StringUtils.toDouble(value);
		}
		public Boolean booleanValue() {
			return  StringUtils.toBoolean(value);
		}
		public Date dateValue() {
			return StringUtils.toDate(value);
		}
	}
	
	
	public static void main(String[] args) throws IOException {
		IniUtils iu = new IniUtils();
		String path = "e:\\xx.ini";	//测试路径
		String propertiesPath = "e:\\xx.properties";	//测试路径
		iu.addSection(
				iu.newSection("真理")
					.noteList(null)
					.put("dev", "true", null))
		.addSection(
				iu.newSection("第二块")
					.put("first", "ww", ListUtils.of("我是注释"))
					.put("date", "2017-11-07", ListUtils.of("注释1","时间测试")))
					.save(path);
		IniUtils iu2 = new IniUtils(path);
		Date date = iu2.getDateValue("第二块", "date").get();
		System.out.println(date.getTime());
		
		iu2.update("真理", "dev", null)
			.addOrUpadate("第二块", "pi", 3.14)
			.addOrUpadate("test", "path", "e:\\a")
			.removeKey("第二块", "first").save(path);
		IniUtils iu3 = new IniUtils(path);
		System.out.println(iu3.getValue("真理", "dev").get());
		System.out.println(iu3.getValue("test", "path").get());
		System.out.println(iu3.getFloatValue("第二块", "pi").get());
		iu3.saveAsProperties(propertiesPath);
	}
	
}
