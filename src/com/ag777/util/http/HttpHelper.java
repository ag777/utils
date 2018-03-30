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
 * @version create on 2018年03月30日,last modify at 2018年03月30日
 */
public class HttpHelper {
	
	public static HttpHelper mInstance = null;
	
	public static HttpHelper getInstance() {
		if(mInstance == null) {
			synchronized (HttpHelper.class) {
				if(mInstance == null) {
					mInstance = new HttpHelper(HttpUtils.client());
				}
			}
		}
		return mInstance;
	}

	
	public OkHttpClient client;
	
	public HttpHelper(OkHttpClient client) {
		this.client = client;
	}
	
	/**===================GET请求===========================*/
	
	public MyCall get(String url) {
		return get(url, null, null);
	}
	
	public <K, V>MyCall get(String url, Map<K, V> paramMap) {
		return get(url, paramMap, null);
	}
	
	public <K, V>MyCall get(String url, Map<K, V> paramMap, Map<K,V> headerMap) {
		Call call = HttpUtils.getByClient(client, url, paramMap, headerMap);
		return new MyCall(call);
	}
	
	public <K,V>MyCall get(String url, Headers headers) {
		Call call = HttpUtils.getByClient(client, url, headers);
		return new MyCall(call);
	}
	
	/**===================POST请求===========================*/
	
	public <K,V>MyCall postJson(String url, String json, Map<K,V> headerMap) {
		Call call = HttpUtils.postJsonByClient(client, url, json, headerMap);
		return new MyCall(call);
	}
	
	public <K,V>MyCall post(String url, Map<K, V> paramMap, Map<K,V> headerMap) {
		Call call = HttpUtils.postByClient(client, url, paramMap, headerMap);
		return new MyCall(call);
	}
	
	/**
	 * post请求
	 * @param url
	 * @param body
	 * @param headers
	 * @return
	 */
	public MyCall post(String url, RequestBody body, Headers headers) {
		Call call = HttpUtils.postByClient(client, url, body, headers);
		return new MyCall(call);
	}
	
	/**===================文件上传下载=========================== */
	
	/**
	 * 带进度条的文件下载
	 * @param url
	 * @param listener
	 * @return
	 */
	public MyCall downLoad(String url,  ProgressResponseBody.ProgressListener listener) {
		OkHttpClient client = HttpUtils.clientWithProgress(this.client.newBuilder(), listener);
		Call call = HttpUtils.getByClient(client, url);
		return new MyCall(call);
	}
	
	/**
	 * post请求带附件
	 * @param url
	 * @param files
	 * @param params
	 * @param headerMap
	 * @return
	 * @throws FileNotFoundException
	 */
	public <K, V>MyCall postMultiFiles(String url, File[] files, Map<K, V> params, Map<K, V> headerMap) throws FileNotFoundException {
		Call call = HttpUtils.postMultiFilesByClient(client, url, files, params, headerMap);
		return new MyCall(call);
	}
}
