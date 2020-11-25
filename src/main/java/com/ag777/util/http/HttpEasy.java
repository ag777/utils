package com.ag777.util.http;

import com.ag777.util.http.model.ProgressResponseBody;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

/**
 * 简单的http请求工具类(二次封装okhttp3)
 * <p>
 * 		依赖于HttpUtils
 * </p>
 * 
 * @author ag777
 * @version create on 2018年03月30日,last modify at 2020年11月25日
 */
public class HttpEasy {
	
	private HttpEasy() {}
	
	//===================GET请求===========================*/
	
	/**
	 * 发送get请求
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url url
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap 请求头
	 * @return String
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<String> get(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	//-get-map
	/**
	 * 发送get请求获取map
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url url
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap 请求头
	 * @return map
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<Map<String, Object>> getForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}
	
	//===================POST请求===========================*/
	
	/**
	 * post请求向服务端发送json串
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 *
	 * @param url url
	 * @param json json
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap 请求头
	 * @return 字符串
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<String> postJson(String url, String json, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postJsonByClient(null, url, json, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	/**
	 * 发送post请求
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 *
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return 字符串
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<String> post(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	//-post-map
	/**
	 * post请求向服务端发送json串并将返回字符串转化为map
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url url
	 * @param json json
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap 请求头
	 * @return map对象
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<Map<String, Object>> postJsonForMap(String url, String json, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postJsonByClient(null, url, json, paramMap, headerMap, null);
		return callForMapForce(call);
	}
	
	/**
	 * post请求并将返回字符串转化为map
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 *
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return map对象
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<Map<String, Object>> postForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}


	//===================DELETE请求===========================
	/**
	 * delete请求
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return 字符串
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K,V> Optional<String> delete(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.deleteByClient(null, url, paramMap, headerMap, null);
		return callForStrForce(call);
	}

	/**
	 * delete请求
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return map对象
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K,V> Optional<Map<String, Object>> deleteForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.deleteByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}

	//===================PUT请求===========================*/
	/**
	 * put请求向服务端发送json串
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 *
	 * @param url url
	 * @param json json
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap 请求头
	 * @return 字符串
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<String> putJson(String url, String json, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.putJsonByClient(null, url, json, paramMap, headerMap, null);
		return callForStrForce(call);
	}

	/**
	 * put请求
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return 字符串
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K,V>Optional<String> put(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.putByClient(null, url, paramMap, headerMap, null);
		return callForStrForce(call);
	}

	/**
	 * put请求
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return map对象
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K,V> Optional<Map<String, Object>> putForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.putByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}
	
	//===================上传/下载文件===========================*/
	
	/**
	 * 向接口提交表单并附带文件(支持一次多个)
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url url
	 * @param files 文件
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return string
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 * @throws FileNotFoundException 找不到文件
	 */
	public static <K, V>Optional<String> postMultiFiles(String url, File[] files, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException, FileNotFoundException  {
		Call call = HttpUtils.postMultiFilesByClient(null, url, files, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	/**
	 * 
	 * @param url url
	 * @param fileMap 文件及其上传名称对应map
	 * @param fileKey 请求体里对应的key
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @return string
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 * @throws FileNotFoundException 找不到文件
	 */
	public static <K, V>Optional<String> postMultiFiles(String url, Map<File, String> fileMap, String fileKey, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException, FileNotFoundException  {
		Call call = HttpUtils.postMultiFilesByClient(null, url, fileMap, fileKey, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	/**
	 * get请求获取文件流
	 * <p>
	 * 	只有在接口返回200时获取返回流
	 * </p>
	 * 
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @param listener 进度监听
	 * @return io流,请自行实现读取操作
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<InputStream> downLoadForStream(String url, Map<K, V> paramMap, Map<K,V> headerMap, ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
		OkHttpClient client = HttpUtils.builderWithProgress(null, listener).build();
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap, null);
		return callForInputStream(call);
	}
	
	/**
	 * get请求获取返回流并保存到指定路径
	 * <p>
	 * 	只有在接口返回200时获取返回流
	 * 保存后回去判断文件是否存在，不存在时返回Optional.empty()
	 * </p>
	 * 
	 * @param url url
	 * @param paramMap 请求参数
	 * @param headerMap 请求头
	 * @param targetPath 本地用来存放文件的路径
	 * @param listener 进度监听
	 * @return 文件对象
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<File> downLoad(String url, Map<K, V> paramMap, Map<K,V> headerMap, String targetPath, ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
		OkHttpClient client = HttpUtils.builderWithProgress(null, listener).build();
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap, null);
		return callForFile(call, targetPath);
	}
	
	//===================内部方法===========================
	/**
	 * 从结果中强制获取字符串
	 * 
	 * @param call call
	 * @return Optional<String>
	 */
	private static Optional<String> callForStrForce(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseStrForce(response);
		} catch(Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * 从结果中强制获取字符串并转成map
	 * 
	 * @param call call
	 * @return Optional<Map<String, Object>>
	 */
	private static Optional<Map<String, Object>> callForMapForce(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseMapForce(response);
		} catch(Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * 从结果中获取流
	 * 
	 * @param call call
	 * @return Optional<InputStream>
	 */
	private static Optional<InputStream> callForInputStream(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseInputStream(response);
		} catch(Exception ex) {
			return Optional.empty();
		}
	}
	
	/**
	 * 从结果中获取流,保存到指定路径并返回
	 * <p>
	 * 	保存失败时返回Optional.empty()
	 * </p>
	 * 
	 * @param call call
	 * @param targetPath 本地用来存放文件的路径
	 * @return Optional<File>
	 */
	private static Optional<File> callForFile(Call call, String targetPath)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseFile(response, targetPath);
		} catch(Exception ex) {
			return Optional.empty();
		}
	}
}
