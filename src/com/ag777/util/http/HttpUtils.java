package com.ag777.util.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import com.ag777.util.Utils;
import com.ag777.util.file.FileUtils;
import com.ag777.util.http.bean.SSLSocketClient;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * http请求类(二次封装okhttp3)
 * @author ag777
 * Time: last modify at 2017/6/22.
 * MARK:
 * 		6/8:尝试通过反射机制参数callback<T>来转换结果为类达成优雅代码的目的,没成功,原因如下:
 * 		1.直接用反射从参数中取泛型的类型只实现了一个递归获取的方法（已删）
 * 		2.通过gson的typetoken类来获取T的类型失败，原因应该是java在编译时擦除泛型类型导致的
 */
public class HttpUtils {

	private static OkHttpClient mOkHttpClient;
	
	private HttpUtils() {}
	
	/**
	 * 生成并获取client对象,双锁校验
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static OkHttpClient client() {
		if(mOkHttpClient == null) {
			synchronized (HttpUtils.class) {
				if(mOkHttpClient == null) {
					mOkHttpClient = new OkHttpClient().newBuilder()  
		                    .connectTimeout(15, TimeUnit.SECONDS)  
		                    .readTimeout(15, TimeUnit.SECONDS)  
		                    .writeTimeout(15, TimeUnit.SECONDS)  
		                    .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())  
		                    .hostnameVerifier(SSLSocketClient.getHostnameVerifier())  
		                    .build();  
				}
			}
		}
		return mOkHttpClient;
	}
	
	/**
	 * 下载文件 同步请求，占用主线程，OkHttp一般在安卓上用异步请求做事情的
	 * @param url
	 * @param targetPath
	 * @return 失败返回null
	 */
	public static File downLoad(String url, String targetPath) {
		//创建一个Request
		final Request request = new Request.Builder()
	        .url(url)
	        .build();
		
		//new call
		Call call = client().newCall(request); 
		
		try {
			Response response = call.execute();
			if(response.isSuccessful()) {
				InputStream stream = response.body().byteStream();
				FileUtils.write(stream, targetPath, true);
				return new File(targetPath);
			}
			throw new Exception(response.code()+"||"+response.message());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**===================POST请求===========================*/
	
	/**
	 * post发送json串，返回map
	 * @param url
	 * @param json
	 * @return
	 */
	public static Map<String, Object> doPostJSON(String url, String json) {
		//参数
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		
		Request request = new Request.Builder()
	        .url(url)
	        .post(requestBody)
	        .build();

		return callForMap(request);
	}
	
	/**
	 * post请求获取结果
	 * @param url
	 * @param params
	 * @return
	 */
	public static <K,V>Map<String, Object> doPost(String url, Map<K, V> params) {
		
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		 
		 return callForMap(request);
	}
	
	/**
	 * post请求获取map
	 * @param url
	 * @param params
	 * @return
	 */
	public static <K,V>Map<String, Object> doPostForMap(String url, Map<K, V> params) {
		
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		 
		 return callForMap(request);
	}
	
	//--异步
	/**
	 * get请求获取结果
	 * @param url
	 * @return
	 */
	public static <K, V>void doPost(String url, Map<K, V> params, Callback callback) {
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		call(request, callback);
	}
	
	/**
	 * post发送json串，返回map
	 * @param url
	 * @param json
	 * @return
	 */
	public static void doPostJSON(String url, String json, Callback callback) {
		//参数
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		
		Request request = new Request.Builder()
	        .url(url)
	        .post(requestBody)
	        .build();

		call(request, callback);
	}
	
	/**==============GET请求======================*/
	//--同步
	/**
	 * get请求获取结果
	 * @param url
	 * @return
	 */
	public static String doGet(String url) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return call(requestBuilder.build());
	}
	
	/**
	 * get请求(带参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static String doGet(String url, Map<String, Object> params) {
		return doGet(
				getGetUrl(url, params));
	}
	
	/**
	 * get请求获取map
	 * @param url
	 * @return
	 */
	public static Map<String, Object> doGetForMap(String url) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return callForMap(requestBuilder.build());
	}
	
	/**
	 * get请求(带参数)获取map
	 * @param url
	 * @param params
	 * @return
	 */
	public static Map<String, Object> doGetForMap(String url, Map<String, Object> params) {
		return doGetForMap(
				getGetUrl(url, params));
	}
	
	//--异步
	/**
	 * get请求获取结果
	 * @param url
	 * @return
	 */
	public static void doGet(String url, Map<String, Object> params, Callback callback) {
		doGet(
				getGetUrl(url, params), callback);
	}
	
	/**
	 * 
	 * @param url
	 * @param callback
	 */
	public static void doGet(String url, Callback callback) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		call(requestBuilder.build(), callback);
	}
	
	/**==============内部方法======================*/
	/**
	 * 拼接get请求的url及参数
	 * @param url
	 * @param params
	 * @return
	 */
	private static String getGetUrl(String url, Map<String, Object> params) {
		if(params == null || url.isEmpty()) {
			return url;
		}
		StringBuilder tail = null;;
		Iterator<String> itor = params.keySet().iterator();
		while(itor.hasNext()) {
			if(tail == null) {
				tail = new StringBuilder();
			} else {
				tail.append('&');
			}
			String key = itor.next();
			String value = params.get(key).toString();
			tail.append(key)
				.append("=")
				.append(value);
		}
		if(tail != null && tail.length()>0) {
			 return url+"?"+tail.toString();
		}
		return url;
	}
	
	/**
	 * 请求并获取结果字符串(同步请求)
	 * @param request
	 * @return
	 */
	private static String call(Request request) {
		Call call = client().newCall(request); 
		try {
			Response response = call.execute();
			if(response.isSuccessful()) {
				return response.body().string();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 请求并获取结果字符串(异步请求)
	 * @param request
	 * @return
	 */
	private static void call(Request request, Callback callback) {
		Call call = client().newCall(request); 
		try {
			call.enqueue(new okhttp3.Callback() {

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					callback.onSuccess(response.body().string());
				}
				
				@Override
				public void onFailure(Call call, IOException ioException) {
					callback.onFailure(ioException);
				}
				
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 请求并获取结果map(同步请求)
	 * @param request
	 * @return
	 */
	private static Map<String, Object> callForMap(Request request) {
		try {
			String result = call(request);
			if(result != null) {
				return Utils.jsonUtils().toMap(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  null;
	}
	
	/**
	 * 通过参数构建请求体
	 * @param params
	 * @return
	 */
	private static <K,V> RequestBody getRequestBody(Map<K, V> params) {
		 Builder builder = new FormBody.Builder();
		 Iterator<K> itor = params.keySet().iterator();
		 while(itor.hasNext()) {
			 K key = itor.next();
			 V value = params.get(key);
			 builder.add(key.toString(), value==null?null:value.toString());
		 }
		 return  builder.build();
	}
	
	
	/**==============辅助类======================*/
	public interface Callback {
		void onSuccess(String result) throws IOException;
		void onFailure(Exception exception);
	}
	
}
