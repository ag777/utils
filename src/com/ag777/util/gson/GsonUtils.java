package com.ag777.util.gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.ag777.util.lang.interf.JsonUtilsInterf;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @Description gson统一管理类，全局保持一个gson对象
 * @author ag777
 * Time: created at 2017/5/27. last modify at 2017/7/26.
 * Mark: 
 */
public class GsonUtils implements JsonUtilsInterf{
	
	private static GsonUtils mInstance;
	
	private GsonBuilder builder;
	private Gson gson;
	
	private GsonUtils() {
		builder = getDefaultBuilder();
	}
	
	private GsonUtils(GsonBuilder builder) {
		this.builder = builder;
	}
	
	/*==================入口函数========================*/
	/**
	 * 获取一般情况下的gson
	 * @return
	 */
	public static GsonUtils get() {
		if(mInstance == null) {
			synchronized(GsonUtils.class) {
				if(mInstance == null) {
					mInstance = new GsonUtils();
				}
			}
		}
		return mInstance;
	}
	/**
	 * 获取默认的gson
	 * @return
	 */
	public static GsonUtils def() {
		return new GsonUtils(new GsonBuilder());
	}
	/**
	 * 自定义构建gson
	 * @param builder
	 * @return
	 */
	public static GsonUtils custom(GsonBuilder builder) {
		return new GsonUtils(builder);
	}
	
	/*==================下面添加配置(可拓展)========================*/
	/**
	 * 定制时间格式
	 * @param pattern
	 * @return
	 */
	public GsonUtils dateFormat(String pattern) {
		return new GsonUtils(builder.setDateFormat(pattern));
	}
	
	/**
	 * null值也参与序列化
	 * @return
	 */
	public GsonUtils serializeNulls() {
		return new GsonUtils(builder.serializeNulls());
	}
	
	/**
	 * 禁此序列化内部类 
	 * @return
	 */
	public GsonUtils disableInnerClassSerialization() {
		return new GsonUtils(builder.disableInnerClassSerialization());
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param baseType
	 * @param typeAdapter
	 * @return
	 */
	public GsonUtils registerTypeAdapter(Class<?> baseType, Object typeAdapter) {
		return new GsonUtils(builder.registerTypeHierarchyAdapter(baseType, typeAdapter));
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param type
	 * @param typeAdapter
	 * @return
	 */
	public GsonUtils registerTypeAdapter(Type type, Object typeAdapter) {
		return new GsonUtils(builder.registerTypeAdapter(type, typeAdapter));
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param factory
	 * @return
	 */
	public GsonUtils registerTypeAdapterFactory(TypeAdapterFactory factory) {
		return new GsonUtils(builder.registerTypeAdapterFactory(factory));
	}
	
	/*==================内部方法========================*/
	private Gson gson() {
		if(gson == null) {
			synchronized(GsonUtils.class) {
				if(gson == null) {
					gson = builder.create();
				}
			}
		}
		return gson;
	}
	
	/*==================工具方法========================*/
	/**
	 * 获取该工具默认的builder
	 * @return
	 */
	public static GsonBuilder getDefaultBuilder() {
		return new GsonBuilder()
				.disableHtmlEscaping()	//html标签不转义 (避免符号被转义)
				.setDateFormat("yyyy-MM-dd HH:mm:ss")	//序列化和反序化时将时间以此形式输出
				.registerTypeAdapter(
						new TypeToken<Map<String, Object>>() {}.getType(), 
						new MapTypeAdapter()
				)
				.registerTypeAdapter(
						new TypeToken<List<Object>>() {}.getType(), 
						new MapTypeAdapter()
				)
				.registerTypeAdapter(Class.class, new ClassTypeAdapter());
	}
	
	/**
	 * 转换任意类为json串（类型不支持会报错，这里不做捕获也不做抛出, 就当业务有问题应当报错,免得写try-catch）
	 * @param obj
	 * @return
	 */
	@Override
	public String toJson(Object obj) {
		return gson().toJson(obj);
	}
	
	/**
	 * 转化json串为map
	 * @param json
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public Map<String, Object> toMap(String json) {
		try {
			return gson().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
		} catch(Exception ex) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> toListMap(String json) {
		try {
			return gson().fromJson(json, new TypeToken<List<Map<String, Object>>>() {}.getType());
		} catch(Exception ex) {
			return null;
		}
	}
	
	/**
	 * 转化json串为javaBean
	 * @param json
	 * @param classOfT
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public <T> T fromJson(String json,Class<T> classOfT) {
		try {
			return gson().fromJson(json, (Type) classOfT);
		} catch(Exception ex) {
			return null;
		}
	}
	
	/**
	 * 转化json串为javaBean，类型不支持时会报错
	 * @param json
	 * @param type 例:new TypeToken<Map<String, Object>>() {}.getType()
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public <T> T fromJson(String json, Type type) {
		try {
			return gson().fromJson(json, type);
		} catch(Exception ex) {
			return null;
		}
	}
	
	
	/*=================辅助类============*/
	/**
	 * @Description 重载json转换类,主要目的是为了防止转为map时double型变量错误地转换为long型变量
	 * @author ag777
	 * Time: created at 2017/6/6. last modify at 2017/6/6.
	 */
	public static class MapTypeAdapter extends TypeAdapter<Object> {

		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<Object>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;

			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<String, Object>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;

			case STRING:
				return in.nextString();

			case NUMBER:
				/**
				 * 改写数字的处理逻辑，将数字值分为整型与浮点型。
				 * 流程（规则）:
				 * 1.先读取字符串，并转为为double类型
				 * 2.如果字符串包含小数点,则直接返回double类型
				 * 3.如果数值大于Long型的最大值，则返回double类型
				 * 4.其余情况均返回Long型
				 */
				String temp = in.nextString();
				
				double dbNum = Double.parseDouble(temp);
				if(temp.contains(".")) {
					return dbNum;
				}
				
				// 数字超过long的最大值，返回浮点类型
				if (dbNum > Long.MAX_VALUE) {
					return dbNum;
				}
				return (long)dbNum;
			case BOOLEAN:
				return in.nextBoolean();

			case NULL:
				in.nextNull();
				return null;

			default:
				throw new IllegalStateException();
			}
		}

		@Override
		public void write(JsonWriter out, Object value) throws IOException {
			// 序列化无需实现
		}

	}
	
	/**
	 * 实现Class类型变量的解析器
	 * @author ag777
	 *
	 */
	public static class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {

		@Override
		public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
				throws JsonParseException {
			try {
				return Class.forName(json.getAsString());
			} catch (ClassNotFoundException e) {
			}
			return null;
		}

		@Override
		public JsonElement serialize(Class<?> clazz, Type typeOfSrc, JsonSerializationContext context) {
			return clazz==null?null:new JsonPrimitive(clazz.getName());
		}
		
	}
	
	/*=================测试(示例)方法============*/
	public static void main(String[] args) {
//		String json = "{\"success\":true, \"a\":20, \"b\":20.0, \"c\":20.1}";
//		Map<String, Object> map = GsonUtils.get().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
//		System.out.println(
//				map.get("a").getClass().getName()+"||"+
//				map.get("b").getClass().getName()+"||"+
//				map.get("c").getClass().getName());
//		System.out.println(GsonUtils.get().toJson(map));
		
		Map<String, Object> map = new HashMap<>();
		map.put("a", new Date());
		map.put("b", null);
		System.out.println(GsonUtils.get().toJson(map));
		System.out.println(GsonUtils.get()
				.dateFormat("yyyyMMdd")
				.serializeNulls()
				.toJson(map));	//会产生没用的中间产物
		
		System.out.println(GsonUtils.custom(
				GsonUtils.getDefaultBuilder()
					.setDateFormat("yyyy_MM")
				).toJson(map));
		
	}
	
}
