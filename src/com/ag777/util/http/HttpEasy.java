package com.ag777.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Map;
import java.util.Optional;

import com.ag777.util.http.model.ProgressResponseBody;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * 简单的http请求工具类(二次封装okhttp3)
 * <p>
 * 		依赖于HttpUtils
 * </p>
 * 
 * @author ag777
 * @version create on 2018年03月30日,last modify at 2018年05月31日
 */
public class HttpEasy {
	
	private HttpEasy() {}
	
	/**===================GET请求===========================*/
	
	/**
	 * 发送get请求
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
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
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<Map<String, Object>> getForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}
	
	/**===================POST请求===========================*/
	
	/**
	 * post请求向服务端发送json串
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url
	 * @param json
	 * @param paramMap 放在url里的参数
	 * @param headerMap
	 * @return
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
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
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
	 * @param url
	 * @param json
	 * @param paramMap 放在请求头里的参数
	 * @param headerMap
	 * @return
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
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<Map<String, Object>> postForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap, null);
		return callForMapForce(call);
	}
	
	/**===================上传/下载文件===========================*/
	
	/**
	 * 向接口提交表单并附带文件(支持一次多个)
	 * <p>
	 * 	不论接口返回是否是200都去获取返回字符串
	 * </p>
	 * 
	 * @param url
	 * @param files
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 * @throws FileNotFoundException 
	 */
	public static <K, V>Optional<String> postMultiFiles(String url, File[] files, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException, FileNotFoundException  {
		Call call = HttpUtils.postMultiFilesByClient(null, url, files, paramMap, headerMap, null);
		return callForStrForce(call);
	}
	
	/**
	 * get请求获取文件流
	 * <p>
	 * 	只有在接口返回200时获取返回流
	 * </p>
	 * 
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @param targetPath
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<InputStream> downLoadForStream(String url, Map<K, V> paramMap, Map<K,V> headerMap, String targetPath, ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
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
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @param targetPath
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public static <K, V>Optional<File> downLoad(String url, Map<K, V> paramMap, Map<K,V> headerMap, String targetPath, ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
		OkHttpClient client = HttpUtils.builderWithProgress(null, listener).build();
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap, null);
		return callForFile(call, targetPath);
	}
	
	/**===================内部方法===========================*/
	/**
	 * 从结果中强制获取字符串
	 * 
	 * @param call
	 * @return
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
	 * @param call
	 * @return
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
	 * @param call
	 * @return
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
	 * @param call
	 * @param targetPath
	 * @return
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
