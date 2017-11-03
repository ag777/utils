package com.ag777.util.file;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.model.Charsets;

/**
 * Ini 文件读取工具类
 * 
 * @author 
 * @version create on 2017年11月03日,last modify at 2017年11月03日
 */
public class IniUtil {
	/* 文件路径 */
	private Path path;
	/* 文本行 */
	private List<String> lines;
	/* 字符集 */
	private Charset charset = Charsets.utf8();
	/* 区块 */
	private Map<String, Section> sections = new LinkedHashMap<String, Section>();

	public Map<String, Section> getSections() {
		return sections;
	}

	public void setSections(Map<String, Section> sections) {
		this.sections = sections;
	}

	public Charset getCharset() {
		return charset;
	}

	public void setCharset(Charset charset) {
		this.charset = charset;
	}

	public List<String> getLines() {
		return lines;
	}

	public void setLines(List<String> lines) {
		this.lines = lines;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	/**
	 * 内部区块类
	 * 
	 */
	public class Section {
		/* 区块关键字 */
		private String key;
		/* 区块注释 */
		private List<String> note = new ArrayList<String>();
		/* 区块中的键值对集合 */
		private List<Value> values = new ArrayList<Value>();

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public List<String> getNote() {
			return note;
		}

		public void setNote(List<String> note) {
			this.note = note;
		}

		public List<Value> getValues() {
			return values;
		}

		public void setValues(List<Value> values) {
			this.values = values;
		}

		/**
		 * 获得键值对对象
		 * 
		 * @param key
		 *            String 键值对key
		 * @return Value 键值对对象/null
		 */
		public Value getValue(String key) {
			for (Value v : this.getValues()) {
				if (v.getKey().equals(key)) {
					return v;
				}
			}
			return null;
		}

		/**
		 * 获得键值对值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return String 字符串
		 */
		public String get(String key) {
			Value v = getValue(key);
			return v != null ? v.getValue() : null;
		}

		/**
		 * 获得Long类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return Long Long/null
		 */
		public Long getLong(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueToLong() : null;
		}

		/**
		 * 获得long类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return long long/false
		 */
		public long getlong(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueTolong() : 0;
		}

		/**
		 * 获得Boolean类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return Boolean true/false/null
		 */
		public Boolean getBoolean(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueToBoolean() : null;
		}

		/**
		 * 获得boolean类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return boolean true/false
		 */
		public boolean getboolean(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueToboolean() : false;
		}

		/**
		 * 获得Integer类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return Integer/null
		 */
		public Integer getInteger(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueToInteger() : null;
		}

		/**
		 * 获得int类型的值
		 * 
		 * @param key
		 *            String 键值对key
		 * @return int int/0
		 */
		public int getInt(String key) {
			Value v = getValue(key);
			return v != null ? v.getValueToInt() : 0;
		}
	}

	/**
	 * 内部键值对类
	 * 
	 */
	public class Value {
		/* 注释 */
		private List<String> note = new ArrayList<String>();
		/* 关键字 */
		private String key;
		/* 具体值 */
		private String value;

		public Value() {
		}

		public Value(String key, String value) {
			this.key = key;
			this.value = value;
		}

		public Value(String key, String value, String[] note) {
			this(key, value);
			if (note != null && note.length > 0) {
				for (String n : note) {
					this.getNote().add(n);
				}
			}
		}

		public Value(String key, String value, List<String> note) {
			this.key = key;
			this.value = value;
			this.note = note;
		}

		public List<String> getNote() {
			return note;
		}

		public void setNote(List<String> note) {
			this.note = note;
		}

		public void setNote(String[] note) {
			if (note != null && note.length > 0) {
				List<String> list = new ArrayList<String>();
				for (String n : note) {
					list.add(n);
				}
				this.note = list;
			}
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		/**
		 * 转换值为Boolean类型
		 * 
		 * @return Boolean true/false/null
		 */
		public Boolean getValueToBoolean() {
			try {
				return Boolean.parseBoolean(this.getValue());
			} catch (ClassCastException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 转换值为Boolean类型
		 * 
		 * @return boolean true/false
		 */
		public boolean getValueToboolean() {
			Boolean bl = this.getValueToboolean();
			return bl != null ? bl : false;
		}

		/**
		 * 转换值为Long类型
		 * 
		 * @return Long Long/null
		 */
		public Long getValueToLong() {
			try {
				return Long.parseLong(this.getValue());
			} catch (ClassCastException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 转换值为long类型
		 * 
		 * @return long Long/0
		 */
		public long getValueTolong() {
			Long l = this.getValueToLong();
			return l != null ? l : 0;
		}

		/**
		 * 转换值为Integer类型
		 * 
		 * @return Integer Integer/null
		 */
		public Integer getValueToInteger() {
			try {
				return Integer.parseInt(this.getValue());
			} catch (ClassCastException e) {
				e.printStackTrace();
				return null;
			}
		}

		/**
		 * 转换值为int类型
		 * 
		 * @return int int/0
		 */
		public int getValueToInt() {
			try {
				return Integer.parseInt(this.getValue());
			} catch (ClassCastException e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	/**
	 * 用路径获取Ini文件，默认编号UTF-8
	 * 
	 * @param path
	 *            Path 路径，例如Paths.get("e:/file.ini")
	 * @throws IOException
	 */
	public IniUtil(Path path) throws IOException {
		this(path, Charsets.utf8());
	}

	/**
	 * 用路径获取Ini文件
	 * 
	 * @param path
	 *            Path 路径，例如Paths.get("e:/file.ini")
	 * @param charset
	 *            字符集 Charsets.UTF_8
	 * @throws IOException
	 */
	public IniUtil(Path path, Charset charset) throws IOException {
		this.path = path;
		this.charset = charset;
		this.lines = Files.readAllLines(this.path, this.charset);
		initByLines();
	}

	/**
	 * 外部传递文件行进行初始化ini对象
	 * 
	 * @param lines
	 *            List<String> 外部文件行
	 */
	public IniUtil(List<String> lines) {
		this.lines = lines;
		initByLines();
	}

	/**
	 * 外部传递文件行进行初始化ini对象
	 * 
	 * @param lines
	 *            String[] 外部文件行
	 */
	public IniUtil(String[] lines) {
		if (lines != null && lines.length > 0) {
			this.lines = new ArrayList<String>();
			for (String line : lines) {
				this.lines.add(line);
			}
			initByLines();
		}
	}

	/**
	 * 通过内容行来初始化ini工具类对象
	 */
	private void initByLines() {
		List<String> note = new ArrayList<String>();
		String skey = null, vkey = null;
		for (String line : lines) {
			line = line.trim();
			if (line.matches("^\\#.*$")) {
				/* note */
				note.add(line.substring(line.indexOf("#") + 1).trim());
			} else if (line.matches("^\\[\\S+\\]$")) {
				/* section */
				String key = line.replaceFirst("^\\[(\\S+)\\]$", "$1");
				skey = key;
				if (sections.get(skey) == null) {
					Section section = new Section();
					section.setKey(skey);
					section.getNote().addAll(note);
					sections.put(skey, section);
				}
				note.clear();
			} else if (line.matches("^\\S+=.*$")) {
				/* key,value */
				String key = line.substring(0, line.indexOf("=")).trim();
				vkey = key;
				Value value = new Value();
				value.setKey(vkey);
				value.getNote().addAll(note);
				value.setValue(loadConvert(line
						.substring(line.indexOf("=") + 1).trim()));
				sections.get(skey).values.add(value);
				note.clear();
			}
		}
	}

	/**
	 * 添加值
	 * 
	 * @param sectionKey
	 *            String 区块关键字
	 * @param sectionNote
	 *            String[] 区块注释
	 * @param valueKey
	 *            String 键值对key
	 * @param value
	 *            String 键值对value
	 * @param valueNote
	 *            String[] 键值对注释
	 */
	public void set(String sectionKey, String[] sectionNote, String valueKey,
			String value, String[] valueNote) {
		if (this.getSections().get(sectionKey) == null) {
			Section section = new Section();
			section.setKey(sectionKey);
			this.getSections().put(sectionKey, section);
		}
		if (sectionNote != null && sectionNote.length > 0) {
			for (String note : sectionNote) {
				this.getSections().get(sectionKey).getNote().add(note);
			}
		}
		/* 判断值是否存在 */
		int valueIndex = -1;
		for (int i = 0, size = this.getSections().get(sectionKey).getValues()
				.size(); i < size; i++) {
			if (this.getSections().get(sectionKey).getValues().get(i).getKey()
					.equals(valueKey)) {
				valueIndex = i;
			}
		}
		if (valueIndex != -1) {
			this.getSections().get(sectionKey).getValues().get(valueIndex)
					.setValue(value);
			this.getSections().get(sectionKey).getValues().get(valueIndex)
					.setNote(valueNote);
		} else {
			this.getSections().get(sectionKey).getValues()
					.add(new Value(valueKey, value, valueNote));
		}
	}

	/**
	 * 设置区块内容
	 * 
	 * @param section
	 *            Section 区块对象
	 */
	public void setSection(Section section) {
		if (section != null)
			this.getSections().put(section.getKey(), section);
	}

	/**
	 * 设置键值对值
	 * 
	 * @param sectionKey
	 *            String 区块关键字
	 * @param valueKey
	 *            String 键值对key
	 * @param value
	 *            String 键值对value
	 */
	public void setValue(String sectionKey, String valueKey, String value) {
		this.set(sectionKey, new String[] {}, valueKey, value, new String[] {});
	}

	/**
	 * 设置键值对
	 * 
	 * @param sectionKey
	 *            String 区块关键字
	 * @param valueKey
	 *            String 键值对key
	 * @param value
	 *            String 键值对value
	 * @param valueNote
	 *            String[] 键值对注释
	 */
	public void setValue(String sectionKey, String valueKey, String value,
			String[] valueNote) {
		this.set(sectionKey, new String[] {}, valueKey, value, valueNote);
	}

	/**
	 * 设置键值对
	 * 
	 * @param sectionKey
	 *            String 区块关键字
	 * @param val
	 *            Value 键值对对象
	 */
	public void setValue(String sectionKey, Value val) {
		if (this.getSections().get(sectionKey) == null) {
			Section section = new Section();
			section.setKey(sectionKey);
			this.getSections().put(sectionKey, section);
		}
		this.getSections().get(sectionKey).getValues().add(val);
	}

	/**
	 * 保存，默认存储为读文件的位置
	 */
	public void save() {
		this.save(this.path);
	}

	/**
	 * 保存
	 * 
	 * @param path
	 *            Path 保存路径，例如new
	 *            IniUtil(Paths.get("e:/file.ini")).save(Paths.get
	 *            ("e:/file1.ini"));
	 */
	public void save(Path path) {
		List<String> list = new ArrayList<>();
		for (Map.Entry<String, Section> entry : sections.entrySet()) {
			/* 先写注释 */
			for (String note : entry.getValue().note) {
				list.add(StringUtils.concat("#", " ", note));
			}
			/* 写区块 */
			list.add(StringUtils.concat("[", entry.getKey(), "]"));
			/* 写区块的值 */
			for (Value val : entry.getValue().getValues()) {
				/* 写注释 */
				for (String note : val.note) {
					list.add(StringUtils.concat("#", " ", note));
				}
				/* 写值 */
				list.add(StringUtils.concat(val.getKey(), "=", saveConvert(val.getValue())));
			}
		}
		try {
			Files.write(path, list, charset);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 加载内容转换器
	 * 
	 * @param str
	 * @return
	 */
	private String loadConvert(String str) {
		int len = str.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = str.charAt(x);
			if (aChar == '\\') {
				if (x + 1 >= len) {
					outBuffer.append(aChar);
					break;
				}
				aChar = str.charAt(++x);
				if (aChar == 't')
					aChar = '\t';
				else if (aChar == 'r')
					aChar = '\r';
				else if (aChar == 'n')
					aChar = '\n';
				else if (aChar == 'f')
					aChar = '\f';
				else {
					aChar = str.charAt(--x);
				}
			}
			outBuffer.append(aChar);
		}
		return outBuffer.toString();
	}

	/**
	 * 存储内容转换器
	 * 
	 * @param str
	 * @return
	 */
	private String saveConvert(String str) {
		int len = str.length();
		int bufLen = len * 2;
		if (bufLen < 0) {
			bufLen = Integer.MAX_VALUE;
		}
		StringBuffer outBuffer = new StringBuffer(bufLen);

		for (int x = 0; x < len; x++) {
			char aChar = str.charAt(x);
			if ((aChar > 61) && (aChar < 127)) {
				if (aChar == '\\') {
					outBuffer.append('\\');
					outBuffer.append('\\');
					continue;
				}
				outBuffer.append(aChar);
				continue;
			}
			switch (aChar) {
			case '\t':
				outBuffer.append('\\');
				outBuffer.append('t');
				break;
			case '\n':
				outBuffer.append('\\');
				outBuffer.append('n');
				break;
			case '\r':
				outBuffer.append('\\');
				outBuffer.append('r');
				break;
			case '\f':
				outBuffer.append('\\');
				outBuffer.append('f');
				break;
			default:
				outBuffer.append(aChar);
			}
		}
		return outBuffer.toString();
	}

	/**
	 * 获得键值对对象
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return Value 键值对对象
	 */
	public Value getValue(String sectionKey, String valueKey) {
		return this.getSections().get(sectionKey).getValue(valueKey);
	}

	/**
	 * 获得String类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return String
	 */
	public String get(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValue();
	}

	/**
	 * 获得Boolean类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return Boolean true/false/null
	 */
	public Boolean getBoolean(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueToBoolean();
	}

	/**
	 * 获得boolean类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return boolean true/false
	 */
	public boolean getboolean(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueToboolean();
	}

	/**
	 * 获得Long类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return Long Long/null
	 */
	public Long getLong(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueToLong();
	}

	/**
	 * 获得long类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return long long/0
	 */
	public long getlong(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueTolong();
	}

	/**
	 * 获得Integer类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return Integer Integer/null
	 */
	public Integer getInteger(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueToInteger();
	}

	/**
	 * 获得int类型值
	 * 
	 * @param sectionKey
	 *            String 区块key
	 * @param valueKey
	 *            String 键值对key
	 * @return int int/0
	 */
	public int getInt(String sectionKey, String valueKey) {
		return this.getValue(sectionKey, valueKey).getValueToInt();
	}

	/**
	 * 获得区块对象
	 * 
	 * @param key
	 *            String 区块key
	 * @return Section 区块对象
	 */
	public Section getSection(String key) {
		return this.getSections().get(key);
	}

	public static void main(String[] args) {
		try {
			IniUtil ini = new IniUtil(Paths.get("e:/file.ini"));
			// ini.setValue("cc", "aa", "bb中国");
			// ini.setValue("cc", "ed", "中国人哈哈", new
			// String[]{"注释1","sdf","注释思念"});
			ini.setValue("cc", "ed", "中国人21\r\n哈哈\\\\", new String[] { "注释1",
					"s2df", "注释思念" });
			System.out.println(ini.get("cc", "ed"));
			// ini.set("cc",new String[]{"section note"}, "ed", "中国123哈哈", new
			// String[]{"注释2","sdf33","注释思念33"});
			ini.save(Paths.get("e:/file1.ini"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
