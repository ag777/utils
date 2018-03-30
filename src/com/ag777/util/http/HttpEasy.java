package com.ag777.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * @version create on 2018年03月30日,last modify at 2018年03月30日
 */
public class HttpEasy {
	
	private HttpEasy() {}
	
	/**===================GET请求===========================*/
	
	public static <K, V>Optional<String> get(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap);
		return callForStr(call);
	}
	
	public static <K, V>Optional<String> getForce(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap);
		return callForStrForce(call);
	}
	
	//-get-map
	public static <K, V>Optional<Map<String, Object>> getForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap);
		return callForMap(call);
	}
	
	public static <K, V>Optional<Map<String, Object>> getForMapForce(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.getByClient(null, url, paramMap, headerMap);
		return callForMapForce(call);
	}
	
	/**===================POST请求===========================*/
	
	public static <K, V>Optional<String> postJson(String url, String json, Map<K,V> headerMap)  {
		Call call = HttpUtils.postJsonByClient(null, url, json, headerMap);
		return callForStr(call);
	}
	
	public static <K, V>Optional<String> postJsonForce(String url, String json, Map<K,V> headerMap)  {
		Call call = HttpUtils.postJsonByClient(null, url, json, headerMap);
		return callForStrForce(call);
	}
	
	public static <K, V>Optional<String> post(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap);
		return callForStr(call);
	}
	
	public static <K, V>Optional<String> postForce(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap);
		return callForStrForce(call);
	}
	
	//-post-map
	
	public static <K, V>Optional<Map<String, Object>> postJsonForMap(String url, String json, Map<K,V> headerMap)  {
		Call call = HttpUtils.postJsonByClient(null, url, json, headerMap);
		return callForMap(call);
	}
	
	public static <K, V>Optional<Map<String, Object>> postJsonForMapForce(String url, String json, Map<K,V> headerMap)  {
		Call call = HttpUtils.postJsonByClient(null, url, json, headerMap);
		return callForMapForce(call);
	}
	
	public static <K, V>Optional<Map<String, Object>> postForMap(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap);
		return callForMap(call);
	}
	
	public static <K, V>Optional<Map<String, Object>> postForMapForce(String url, Map<K, V> paramMap, Map<K,V> headerMap)  {
		Call call = HttpUtils.postByClient(null, url, paramMap, headerMap);
		return callForMapForce(call);
	}
	
	/**===================上传/下载文件===========================*/
	
	/**
	 * 
	 * @param url
	 * @param files
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static <K, V>Optional<String> postMultiFiles(String url, File[] files, Map<K, V> paramMap, Map<K,V> headerMap) throws FileNotFoundException  {
		Call call = HttpUtils.postMultiFilesByClient(null, url, files, paramMap, headerMap);
		return callForStr(call);
	}
	
	public static Optional<File> downLoad(String url, String targetPath, ProgressResponseBody.ProgressListener listener)  {
		OkHttpClient client = HttpUtils.clientWithProgress(null, listener);
		Call call = HttpUtils.getByClient(client, url);
		return callForFile(call, targetPath);
	}
	
	/**===================内部方法===========================*/
	
	private static Optional<String> callForStr(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseStr(response);
		} catch(IOException ex) {
			return Optional.empty();
		}
	}
	
	private static Optional<String> callForStrForce(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseStrForce(response);
		} catch(IOException ex) {
			return Optional.empty();
		}
	}
	
	private static Optional<Map<String, Object>> callForMap(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseMap(response);
		} catch(IOException ex) {
			return Optional.empty();
		}
	}
	
	private static Optional<Map<String, Object>> callForMapForce(Call call)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseMapForce(response);
		} catch(IOException ex) {
			return Optional.empty();
		}
	}
	
	private static Optional<File> callForFile(Call call, String targetPath)  {
		try {
			Response response = HttpUtils.execute(call);
			return HttpUtils.responseFile(response, targetPath);
		} catch(IOException ex) {
			return Optional.empty();
		}
	}
}
