package com.ag777.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import com.ag777.util.http.model.MyCall;
import com.ag777.util.http.model.ProgressResponseBody;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * 有关http请求的方法类(二次封装okhttp3)
 * <p>
 * 		依赖于HttpUtils
 * </p>
 * 
 * @author ag777
 * @version create on 2018年03月30日,last modify at 2018年04月03日
 */
public class HttpHelper {
	
	private static HttpHelper mInstance = null;
	
	public static HttpHelper getInstance() {
		if(mInstance == null) {
			synchronized (HttpHelper.class) {
				if(mInstance == null) {
					mInstance = new HttpHelper(HttpUtils.client(), null);
				}
			}
		}
		return mInstance;
	}
	
	private OkHttpClient client;
	private Object tag;
	
	public HttpHelper(OkHttpClient client, Object tag) {
		this.client = client;
		this.tag = tag;
	}
	
	/**===================其他方法===========================*/
	/**
	 * 取消tag对应的所有请求
	 * 该tag不应与其余tag重复
	 */
	public void cancelAll() {
		HttpUtils.cancelAll(client, tag);
	}
	
	/**===================GET请求===========================*/
	
	public MyCall get(String url) throws IllegalArgumentException {
		return get(url, null, null);
	}
	
	public <K, V>MyCall get(String url, Map<K, V> paramMap) throws IllegalArgumentException {
		return get(url, paramMap, null);
	}
	
	public <K, V>MyCall get(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap);
		return new MyCall(call);
	}
	
	/**
	 * get请求
	 * @param url
	 * @param headers
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public <K,V>MyCall get(String url, Headers headers) throws IllegalArgumentException {
		Call call = HttpUtils.getByClient(client, url, headers);
		return new MyCall(call);
	}
	
	/**===================POST请求===========================*/
	
	public <K,V>MyCall postJson(String url, String json, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postJsonByClient(client, url, json, paramMap, headerMap, tag);
		return new MyCall(call);
	}
	
	public <K,V>MyCall post(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.postByClient(client, url, paramMap, headerMap, tag);
		return new MyCall(call);
	}
	
	/**
	 * post请求
	 * @param url
	 * @param body
	 * @param headers
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public MyCall post(String url, RequestBody body, Headers headers) throws IllegalArgumentException {
		Call call = HttpUtils.postByClient(client, url, body, headers, tag);
		return new MyCall(call);
	}
	
	/**===================文件上传下载=========================== */
	
	/**
	 * 带进度条的文件下载
	 * @param url
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public MyCall downLoad(String url,  ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
		OkHttpClient client = HttpUtils.clientWithProgress(this.client.newBuilder(), listener);
		Call call = HttpUtils.getByClient(client, url, tag);
		return new MyCall(call);
	}
	
	/**
	 * post请求带附件
	 * @param url
	 * @param files
	 * @param params
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 * @throws FileNotFoundException
	 */
	public <K, V>MyCall postMultiFiles(String url, File[] files, Map<K, V> params, Map<K, V> headerMap) throws IllegalArgumentException, FileNotFoundException {
		Call call = HttpUtils.postMultiFilesByClient(client, url, files, params, headerMap, tag);
		return new MyCall(call);
	}
}
