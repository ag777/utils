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
 * @version create on 2018年03月30日,last modify at 2018年05月21日
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
	
	/**
	 * 构造函数
	 * <p>
	 * 如果两个参数都不需要自定义，并且没有取消请求的需求，不如用HttpEasy工具类做请求来的方便
	 * </p>
	 * 
	 * @param client 默认为HttpUtils.client()
	 * @param tag 请求的统一tag,用于请求分组管理,可以传null
	 */
	public HttpHelper(OkHttpClient client, Object tag) {
		if(client == null) {
			client = HttpUtils.client();
		}
		this.client = client;
		this.tag = tag;
	}
	
	/**
	 * 返回自定义client的HttpHelper
	 * @param client 默认为HttpUtils.client()
	 * @return
	 */
	public static HttpHelper client(OkHttpClient client) {
		return new HttpHelper(client, null);
	}
	
	/**
	 * 返回自定义tag的HttpHelper
	 * @param tag
	 * @return
	 */
	public static HttpHelper tag(Object tag) {
		return new HttpHelper(null, tag);
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
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap, tag);
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
		Call call = HttpUtils.getByClient(client, url, headers, tag);
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
	
	/**===================DELETE请求===========================*/
	/**
	 * delete请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException
	 */
	public <K,V>MyCall delete(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.deleteByClient(client, url, paramMap, headerMap, tag);
		return new MyCall(call);
	}
	
	/**===================PUT请求===========================*/
	/**
	 * put请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException
	 */
	public <K,V>MyCall put(String url, Map<K, V> paramMap, Map<K,V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.putByClient(client, url, paramMap, headerMap, tag);
		return new MyCall(call);
	}
	
	/**===================HEAD请求===========================*/
	/**
	 * head请求
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException
	 */
	public <K,V>MyCall head(String url, Map<K, V> paramMap, Map<K, V> headerMap) throws IllegalArgumentException {
		Call call = HttpUtils.headByClient(client, url, paramMap, headerMap, tag);
		return new MyCall(call);
	}
	
	/**===================文件上传下载=========================== */
	
	/**
	 * 带进度条的文件下载
	 * @param url
	 * @param paramMap
	 * @param headerMap
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 一般为url异常，比如没有http(s):\\的前缀
	 */
	public <K, V>MyCall downLoad(String url, Map<K, V> paramMap, Map<K,V> headerMap,  ProgressResponseBody.ProgressListener listener) throws IllegalArgumentException {
		OkHttpClient client = HttpUtils.builderWithProgress(this.client.newBuilder(), listener).build();
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap, tag);
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
	
	/**
	 * post请求带附件
	 * @param url
	 * @param fileMap 文件及其上传名称对应map
	 * @param fileKey 请求体里对应的key
	 * @param params
	 * @param headerMap
	 * @return
	 * @throws IllegalArgumentException
	 * @throws FileNotFoundException
	 */
	public <K, V>MyCall postMultiFiles(String url, Map<File, String> fileMap, String fileKey, Map<K, V> params, Map<K, V> headerMap) throws IllegalArgumentException, FileNotFoundException {
		Call call = HttpUtils.postMultiFilesByClient(client, url, fileMap, fileKey, params, headerMap, tag);
		return new MyCall(call);
	}
}
