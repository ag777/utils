package com.ag777.util.gson;

import com.ag777.util.gson.model.TypeFactory;
import com.ag777.util.lang.exception.model.JsonSyntaxException;
import com.ag777.util.lang.interf.JsonUtilsInterf;
import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * gson统一管理类，全局保持一个gson对象
 * <p>
 * 需要jar包:
 * <ul>
 * <li>gson-xxx.jar</li>
 * </ul>
 * GSON更新日志:<a href="https://github.com/google/gson/blob/master/CHANGELOG.md">...</a>
 *
 * @author ag777
 * @version create on 2017年05月27日,last modify at 2024年06月09日
 */
public class GsonUtils implements JsonUtilsInterf {
	
	private static volatile GsonUtils mInstance;

    private final GsonBuilder builder;
    private volatile Gson gson;
	
	private GsonUtils() {
		builder = getDefaultBuilder();
	}
	
	public GsonUtils(GsonBuilder builder) {
		this.builder = builder;
	}
	
	/*==================入口函数========================*/
	/**
	 * 获取一般情况下的gson
	 * @return GsonUtils
	 */
	public static GsonUtils get() {
		if (mInstance == null) {
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
	 * @return GsonUtils
	 */
	public static GsonUtils def() {
		return new GsonUtils(new GsonBuilder());
	}
	/**
	 * 自定义构建gson
	 * @param builder builder
	 * @return GsonUtils
	 */
	public static GsonUtils custom(GsonBuilder builder) {
		return new GsonUtils(builder);
	}
	
	/*==================下面添加配置(可拓展)========================*/

	/**
	 * 修改默认的配置, 最好在程序刚加载的时候执行
	 * @param builderModifier 默认的构造器修改方法
	 */
	public void init(Consumer<GsonBuilder> builderModifier) {
		synchronized (GsonUtils.class) {
			builderModifier.accept(builder);
			gson = builder.create();
		}

	}

	/**
	 * 定制时间格式
	 * @param pattern pattern
	 * @return GsonUtils
	 */
	public GsonUtils dateFormat(String pattern) {
		return new GsonUtils(builder.setDateFormat(pattern));
	}
	
	/**
	 * null值也参与序列化
	 * @return GsonUtils
	 */
	public GsonUtils serializeNulls() {
		return new GsonUtils(builder.serializeNulls());
	}
	
	/**
	 * 格式化输出
	 * @return GsonUtils
	 */
	public GsonUtils prettyPrinting() {
		return new GsonUtils(builder.setPrettyPrinting());
	}
	
	/**
	 * 禁此序列化内部类 
	 * @return GsonUtils
	 */
	public GsonUtils disableInnerClassSerialization() {
		return new GsonUtils(builder.disableInnerClassSerialization());
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param baseType baseType
	 * @param typeAdapter typeAdapter
	 * @return GsonUtils
	 */
	public GsonUtils registerTypeAdapter(Class<?> baseType, Object typeAdapter) {
		return new GsonUtils(builder.registerTypeHierarchyAdapter(baseType, typeAdapter));
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param type type
	 * @param typeAdapter typeAdapter
	 * @return GsonUtils
	 */
	public GsonUtils registerTypeAdapter(Type type, Object typeAdapter) {
		return new GsonUtils(builder.registerTypeAdapter(type, typeAdapter));
	}
	
	/**
	 * 自定义实现序列化方法
	 * @param factory factory
	 * @return GsonUtils
	 */
	public GsonUtils registerTypeAdapterFactory(TypeAdapterFactory factory) {
		return new GsonUtils(builder.registerTypeAdapterFactory(factory));
	}
	
	/*==================内部方法========================*/
	private Gson gson() {
		if (gson == null) {
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
	 * @return GsonBuilder
	 */
	public static GsonBuilder getDefaultBuilder() {
		MapTypeAdapter objAdapter = new MapTypeAdapter();
		return new GsonBuilder()
				.disableHtmlEscaping()	//html标签不转义 (避免符号被转义)
				.setDateFormat("yyyy-MM-dd HH:mm:ss")	//序列化和反序化时将时间以此形式输出
				.registerTypeAdapter(
						new TypeToken<Map<String, Object>>() {}.getType(), 
						objAdapter
				)
				.registerTypeAdapter(
						new TypeToken<List<Object>>() {}.getType(), 
						objAdapter
				)
				.registerTypeAdapter(Class.class, new ClassTypeAdapter());
	}
	
	/**
	 * 转换json串为JsonObject
	 * @param json json
	 * @return JsonObject
	 * @throws JsonSyntaxException json解析异常
	 */
	public static JsonObject toJsonObjectWithException(String json) throws JsonSyntaxException {
		/*源码说明(下同):No need to instantiate this class, use the static methods instead.*/
		try {
			return JsonParser.parseString(json).getAsJsonObject();
		} catch (Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	/**
	 * 转换json对象为JsonArray
	 * @param json json
	 * @return JsonArray
	 * @throws JsonSyntaxException json解析异常
	 */
	public static JsonArray toJsonArrayWithException(String json) throws JsonSyntaxException {
		try {
			return JsonParser.parseString(json).getAsJsonArray();
		} catch (Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	/**
	 * 格式化字符串
	 * <p>
	 * 每次都new一个用于格式化的新gson
	 * </p>
	 * @param src src
	 * @return String
	 */
	@Override
	public String prettyFormat(String src) {
		if (src == null || src.isEmpty()) {
			return src;
		}
		JsonReader reader = new JsonReader(new StringReader(src));
		reader.setLenient(true);
		JsonElement jsonEl = JsonParser.parseReader(reader);
		return toPrettyJson(jsonEl);

	}
	
	/**
	 * 转换任意类为json串（类型不支持会报错，这里不做捕获也不做抛出, 就当业务有问题应当报错,免得写try-catch）
	 * @param obj obj
	 * @return json串，当obj为null的时候返回null
	 */
	@Override
	public String toJson(Object obj) {
		if (obj == null) {
			return null;
		}
		return gson().toJson(obj);
	}

	/**
	 * 将对象转换为JSON字符串。
	 * <p>
	 * 此方法提供了一个通用的接口，用于将任何给定的对象转换为JSON格式的字符串。
	 * 它特别处理了对象为null的情况，避免了因尝试转换null而导致的异常。
	 * 使用Gson库的toJson方法进行实际的转换操作。
	 *
	 * @param obj       要转换为JSON的对象。可以是任何类型的对象，如果为null，则直接返回null。
	 * @param typeOfSrc 对象的类型信息，用于更精确地控制JSON转换过程。这可以是类类型、泛型类型等。
	 * @return 转换后的JSON字符串，如果输入对象为null，则返回null。
	 */
	public String toJson(Object obj, Type typeOfSrc) {
		// 检查对象是否为null，避免不必要的转换尝试
		if (obj == null) {
			return null;
		}
		// 使用Gson实例将对象转换为JSON字符串
		return gson().toJson(obj, typeOfSrc);
	}

	/**
	 *
	 * @param obj 任意对象
	 * @return 格式化的json串
	 */
	public String toPrettyJson(Object obj)  {
		if (obj == null) {
			return null;
		}
		return prettyPrinting().toJson(obj);

	}
	
	/**
	 * 转化json串为map
	 * @param json json
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public Map<String, Object> toMap(String json) {
		try {
			return toMapWithException(json);
		} catch (Exception e) {
			return null;
		}
	}

	public Map<String, Object> toMap(JsonElement json) {
		try {
			return toMapWithException(json);
		} catch (Exception e) {
			return null;
		}
	}

	public <K, V>Map<K, V> toMap(String json, Class<K> classOfK, Class<V> classOfV) {
		try {
			return toMapWithException(json, classOfK, classOfV);
		} catch (Exception e) {
			return null;
		}
	}

	public <K, V>Map<K, V> toMap(JsonElement json, Class<K> classOfK, Class<V> classOfV) {
		try {
			return toMapWithException(json, classOfK, classOfV);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> toMapWithException(String json) throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeFactory(Map.class, String.class, Object.class));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public Map<String, Object> toMapWithException(JsonElement json) throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeFactory(Map.class, String.class, Object.class));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public <K, V>Map<K, V> toMapWithException(String json, Class<K> classOfK, Class<V> classOfV) throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeFactory(Map.class, classOfK, classOfV));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public <K, V>Map<K, V> toMapWithException(JsonElement json, Class<K> classOfK, Class<V> classOfV) throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeFactory(Map.class, classOfK, classOfV));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	@Override
	public List<Map<String, Object>> toListMap(String json) {
		try {
			return toListMapWithException(json);
		} catch (Exception e) {
			return null;
		}
	}

	public List<Map<String, Object>> toListMap(JsonElement json) {
		try {
			return toListMapWithException(json);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public List<Map<String, Object>> toListMapWithException(String json)  throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeToken<List<Map<String, Object>>>() {}.getType());
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public List<Map<String, Object>> toListMapWithException(JsonElement json)  throws JsonSyntaxException {
		try {
			return fromJsonWithException(json, new TypeToken<List<Map<String, Object>>>() {}.getType());
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	/**
	 * 转化json为对象列表
	 * @param json json
	 * @param classOfT classOfT
	 * @return List
	 */
	@Override
	public <T>List<T> toList(String json, Class<T> classOfT) {
		try {
			return toListWithException(json, classOfT);
		} catch (JsonSyntaxException ignored) {
		}
		return null;
	}

	public <T>List<T> toList(JsonElement json, Class<T> classOfT) {
		try {
			return toListWithException(json, classOfT);
		} catch (JsonSyntaxException ignored) {
		}
		return null;
	}
	
	/**
	 * 转化json为对象列表
	 * <p><pre>{@code
	 * 利用ParameterizedType类获取对应List<T>的Type作为转化媒介
	 * }</pre>
	 * 
	 * @param json json
	 * @param classOfT classOfT
	 * @return List
	 * @throws JsonSyntaxException JsonSyntaxException
	 */
	@Override
	public <T>List<T> toListWithException(String json, Class<T> classOfT) throws JsonSyntaxException {
		try {
			return gson().fromJson(json, new TypeFactory(List.class, classOfT));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public <T>List<T> toListWithException(JsonElement json, Class<T> classOfT) throws JsonSyntaxException {
		try {
			return gson().fromJson(json, new TypeFactory(List.class, classOfT));
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	/**
	 * 转换对象为JsonElement
	 * 
	 * @param obj obj
	 * @return JsonElement
	 */
	public JsonElement toJsonTree(Object obj) {
		try {
			return toJsonTreeWithException(obj);
		} catch (JsonSyntaxException ignored) {
		}
		return null;
	}

	/**
	 * 将对象转换为JsonElement树形结构。
	 * 此方法尝试将给定的对象转换为JsonElement，如果转换失败，则返回null。
	 * 使用者应该通过捕获JsonSyntaxException来处理可能的转换异常。
	 *
	 * @param obj 要转换为JsonElement的Java对象。
	 * @param typeOfSrc Java对象的类型信息，用于复杂对象的类型指示。
	 * @return 成功转换后的JsonElement，如果转换失败则返回null。
	 */
	public JsonElement toJsonTree(Object obj, Type typeOfSrc) {
	    try {
	        // 尝试将对象转换为JsonElement，可能抛出JsonSyntaxException。
	        return toJsonTreeWithException(obj, typeOfSrc);
	    } catch (JsonSyntaxException ignored) {
	        // 捕获并忽略JsonSyntaxException，允许方法在异常情况下优雅地返回null。
	    }
	    return null;
	}
	
	/**
	 * 转换对象为JsonElement
	 * @param obj obj
	 * @return JsonElement
	 * @throws JsonSyntaxException JsonSyntaxException
	 */
	public JsonElement toJsonTreeWithException(Object obj)  throws JsonSyntaxException {
		try {
			return gson().toJsonTree(obj);
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	/**
	 * 将对象转换为JsonElement，处理可能的异常。
	 * <p>
	 * 此方法提供了一种将任意对象转换为JsonElement的安全方式。它使用Gson库进行转换，并在转换过程中捕获任何异常。
	 * 如果转换失败，将抛出JsonSyntaxException，以便调用者可以了解转换过程中发生的问题。
	 *
	 * @param obj 要转换为Json的Java对象。
	 * @param typeOfSrc Java对象的类型信息，用于更精确的转换。
	 * @return 转换成功的JsonElement。
	 * @throws JsonSyntaxException 如果转换过程中发生错误，则抛出此异常。
	 */
	public JsonElement toJsonTreeWithException(Object obj, Type typeOfSrc) throws JsonSyntaxException {
	    try {
	        // 尝试使用Gson将对象转换为JsonElement。
	        return gson().toJsonTree(obj, typeOfSrc);
	    } catch (Exception ex) {
	        // 捕获转换过程中可能出现的任何异常，并抛出JsonSyntaxException。
	        throw new JsonSyntaxException(ex);
	    }
	}
	
	
	/**
	 * 转化json串为javaBean,效率低
	 * @param json json
	 * @param classOfT classOfT
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public <T> T fromJson(String json,Class<T> classOfT) {
		try {
			return fromJsonWithException(json, classOfT);
		} catch(Exception ex) {
			return null;
		}
	}

	public <T> T fromJson(JsonElement json,Class<T> classOfT) {
		try {
			return fromJsonWithException(json, classOfT);
		} catch(Exception ex) {
			return null;
		}
	}
	
	@Override
	public <T> T fromJsonWithException(String json,Class<T> classOfT) throws JsonSyntaxException{
		try {
			return gson().fromJson(json, (Type) classOfT);
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public <T> T fromJsonWithException(JsonElement json,Class<T> classOfT) throws JsonSyntaxException{
		try {
			return gson().fromJson(json, (Type) classOfT);
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}
	
	/**
	 * 转化json串为javaBean，类型不支持时会报错
	 * @param json json
	 * @param type 例:<pre>{@code new TypeToken<Map<String, Object>>() {}.getType() }</pre>
	 * @return 返回null则表示json转换失败
	 */
	@Override
	public <T> T fromJson(String json, Type type) {
		try {
			return fromJsonWithException(json, type);
		} catch(Exception ex) {
			return null;
		}
	}

	public <T> T fromJson(JsonElement json, Type type) {
		try {
			return fromJsonWithException(json, type);
		} catch(Exception ex) {
			return null;
		}
	}
	
	@Override
	public <T> T fromJsonWithException(String json, Type type) throws JsonSyntaxException{
		try {
			return gson().fromJson(json ,type);
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	public <T> T fromJsonWithException(JsonElement json, Type type) throws JsonSyntaxException{
		try {
			return gson().fromJson(json ,type);
		} catch(Exception ex) {
			throw new JsonSyntaxException(ex);
		}
	}

	/*=================辅助类============*/
	/**
	 * 重载json转换类,主要目的是为了防止转为map时double型变量错误地转换为long型变量
	 * @author ag777
	 * Time: created at 2017/6/6. last modify at 2018/8/10.
	 */
	public static class MapTypeAdapter extends TypeAdapter<Object> {

		private final TypeAdapter<Object> defaultAdapter = new Gson().getAdapter(Object.class);
		
		@Override
		public Object read(JsonReader in) throws IOException {
			JsonToken token = in.peek();
			switch (token) {
			case BEGIN_ARRAY:
				List<Object> list = new ArrayList<>();
				in.beginArray();
				while (in.hasNext()) {
					list.add(read(in));
				}
				in.endArray();
				return list;

			case BEGIN_OBJECT:
				Map<String, Object> map = new LinkedTreeMap<>();
				in.beginObject();
				while (in.hasNext()) {
					map.put(in.nextName(), read(in));
				}
				in.endObject();
				return map;

			case STRING:
				return in.nextString();

			case NUMBER:
				/*
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
				return Long.parseLong(temp);
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
			/*
			 * 坑:如果不写,内部类包含对应的成员变量时将无法转化为json串
			 * 这里使用原生gson的typeAdapter来代理进行解析,希望有更好的解决方案
			 */
			defaultAdapter.write(out, value);
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
			} catch (ClassNotFoundException ignored) {
			}
			return null;
		}

		@Override
		public JsonElement serialize(Class<?> clazz, Type typeOfSrc, JsonSerializationContext context) {
			return clazz==null?null:new JsonPrimitive(clazz.getName());
		}
		
	}
	
	/*=================测试(示例)方法============*/
	public static void main(String[] args) throws Exception {
		String json = "{\"success\":true, \"a\":20, \"b\":20.0, \"c\":20.1}";
		Map<String, Object> map = GsonUtils.get().fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
//		System.out.println(
//				map.get("a").getClass().getName()+"||"+
//				map.get("b").getClass().getName()+"||"+
//				map.get("c").getClass().getName());
//		System.out.println(GsonUtils.get().toJson(map));
		
//		Map<String, Object> map = new HashMap<>();
//		map.put("a", new Date());
//		map.put("b", null);
//		System.out.println(GsonUtils.get().toJson(map));
//		System.out.println(GsonUtils.get()
//				.dateFormat("yyyyMMdd")
//				.serializeNulls()
//				.toJson(map));	//会产生没用的中间产物
//		
//		System.out.println(GsonUtils.custom(
//				GsonUtils.getDefaultBuilder()
//					.setDateFormat("yyyy_MM")
//				).toJson(map));
//		System.out.println(GsonUtils.get().toMapWithException(null));
//		System.out.println(GsonUtils.get().prettyFormat("{\"a\":1"));

		System.out.println(GsonUtils.get().toPrettyJson(map));
	}
	
}