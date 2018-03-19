package com.ag777.util.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import com.ag777.util.Utils;
import com.ag777.util.file.FileUtils;
import com.ag777.util.http.model.ProgressResponseBody;
import com.ag777.util.http.model.SSLSocketClient;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.collection.MapUtils;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.FormBody.Builder;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 工具包用的通用方法类(二次封装okhttp3)
 * <p>
 * 		6/8:尝试通过反射机制参数callback<T>来转换结果为类达成优雅代码的目的,没成功,原因如下:
 * 		1.直接用反射从参数中取泛型的类型只实现了一个递归获取的方法（已删）
 * 		2.通过gson的typetoken类来获取T的类型失败，原因应该是java在编译时擦除泛型类型导致的
 * </p>
 * 
 * @author ag777
 * @version last modify at 2018年03月16日
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
	 * <p>
	 * 	用默认client的下载
	 * </p>
	 * @param url
	 * @param targetPath
	 * @return 失败返回null
	 */
	public static File downLoad(String url, String targetPath) {
		return downLoad(url, targetPath, client());
	}
	
	/**
	 * 下载文件 同步请求，占用主线程，OkHttp一般在安卓上用异步请求做事情的
	 * <p>
	 *  listener用于监听进度
	 * </p>
	 * @param url
	 * @param targetPath
	 * @param listener
	 * @return
	 */
	public static File downLoad(String url, String targetPath, ProgressResponseBody.ProgressListener listener) {
		OkHttpClient client = client().newBuilder()
	        .addNetworkInterceptor(new Interceptor() {
	            @Override
	            public Response intercept(Chain chain) throws IOException {
	                Response response = chain.proceed(chain.request());
	                //这里将ResponseBody包装成我们的ProgressResponseBody
	                return response.newBuilder()
	                        .body(new ProgressResponseBody(response.body(),listener))
	                        .build();
	            }
	        })
	        .build();
		return downLoad(url, targetPath, client);
	}

	/**
	 * 下载文件 同步请求，占用主线程，OkHttp一般在安卓上用异步请求做事情的
	 * <p>
	 * 用自定义的client下载
	 * </p>
	 * @param url
	 * @param targetPath
	 * @return 失败返回null
	 */
	public static File downLoad(String url, String targetPath, OkHttpClient client) {
		//创建一个Request
		final Request request = new Request.Builder()
	        .url(url)
	        .build();
		
		//new call
		Call call = client.newCall(request); 
		
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
	public static Optional<Map<String, Object>> doPostJSON(String url, String json) {
		//参数
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		
		Request request = new Request.Builder()
	        .url(url)
	        .post(requestBody)
	        .build();

		return callForMap(request);
	}
	
	/**
	 * post发送json串，返回map
	 * @param url
	 * @param json
	 * @param headerMap
	 * @return
	 */
	public static Optional<Map<String, Object>> doPostJSON(String url, String json, Map<String, Object> headerMap) {
		//参数
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		Request request = new Request.Builder()
	        .url(url)
	        .headers(getHeaders(headerMap))
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
	public static <K,V>Optional<String> doPost(String url, Map<K, V> params) {
		
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		 
		 return call(request);
	}
	
	/**
	 * post请求获取结果
	 * @param url
	 * @param params
	 * @param headerMap
	 * @return
	 */
	public static <K,V>Optional<String> doPost(String url, Map<K, V> params, Map<String, Object> headerMap) {
		
		 Request request = new Request.Builder()
	                .url(url)
	                .headers(getHeaders(headerMap))
	                .post(getRequestBody(params))
	                .build();
		 
		 return call(request);
	}
	
	/**
	 * post请求获取map
	 * @param url
	 * @param params
	 * @return
	 */
	public static <K,V>Optional<Map<String, Object>> doPostForMap(String url, Map<K, V> params) {
		
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
	 * @param headerMap
	 * @return
	 */
	public static <K,V>Optional<Map<String, Object>> doPostForMap(String url, Map<K, V> params, Map<String, Object> headerMap) {
		
		 Request request = new Request.Builder()
	                .url(url)
	                .headers(getHeaders(headerMap))
	                .post(getRequestBody(params))
	                .build();
		 
		 return callForMap(request);
	}
	
	
	/**
	 * post请求获取List<Map>
	 * @param url
	 * @param params
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doPostForListMap(String url, Map<String, Object> params) {
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		 
		return callForListMap(request);
	}
	
	/**
	 * post请求获取List<Map>
	 * @param url
	 * @param params
	 * @param headerMap
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doPostForListMap(String url, Map<String, Object> params, Map<String, Object> headerMap) {
		 Request request = new Request.Builder()
	                .url(url)
	                .headers(getHeaders(headerMap))
	                .post(getRequestBody(params))
	                .build();
		 
		return callForListMap(request);
	}
	
	//--异步
	/**
	 * get请求获取结果
	 * @param url
	 * @param params
	 * @param callback
	 */
	public static <K, V>void doPost(String url, Map<K, V> params, Callback callback) {
		 Request request = new Request.Builder()
	                .url(url)
	                .post(getRequestBody(params))
	                .build();
		call(request, callback);
	}
	
	/**
	 * get请求获取结果
	 * @param url
	 * @param params
	 * @param headerMap
	 * @param callback
	 */
	public static <K, V>void doPost(String url, Map<K, V> params, Map<String, Object> headerMap, Callback callback) {
		 Request request = new Request.Builder()
	                .url(url)
	                .headers(getHeaders(headerMap))
	                .post(getRequestBody(params))
	                .build();
		call(request, callback);
	}
	
	
	/**
	 * post发送json串
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
	
	/**
	 * post发送json串
	 * @param url
	 * @param json
	 * @param headerMap
	 * @param callback
	 */
	public static void doPostJSON(String url, String json, Map<String, Object> headerMap, Callback callback) {
		//参数
		RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json);
		
		Request request = new Request.Builder()
	        .url(url)
	        .headers(getHeaders(headerMap))
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
	public static Optional<String> doGet(String url) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return call(requestBuilder.build());
	}
	
	/**
	 * get请求获取结果
	 * @param url
	 * @param headers 请求头
	 * @return
	 */
	public static Optional<String> doGet(String url, Headers headers) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return call(requestBuilder.headers(headers).build());
	}
	
	/**
	 * get请求(带参数)
	 * @param url
	 * @param params
	 * @return
	 */
	public static Optional<String> doGet(String url, Map<String, Object> params) {
		return doGet(
				getGetUrl(url, params));
	}
	
	/**
	 * get请求(带参数和请求头)
	 * @param url
	 * @param params
	 * @param headerMap
	 * @return
	 */
	public static Optional<String> doGet(String url, Map<String, Object> params, Map<String, Object> headerMap) {
		return doGet(
				getGetUrl(url, params), getHeaders(headerMap));
	}
	
	/**
	 * get请求获取map
	 * @param url
	 * @return
	 */
	public static Optional<Map<String, Object>> doGetForMap(String url) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return callForMap(requestBuilder.build());
	}
	
	/**
	 * get请求获取map
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Optional<Map<String, Object>> doGetForMap(String url, Headers headers) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return callForMap(requestBuilder.headers(headers).build());
	}
	
	/**
	 * get请求(带参数)获取map
	 * @param url
	 * @param params
	 * @return
	 */
	public static Optional<Map<String, Object>> doGetForMap(String url, Map<String, Object> params) {
		return doGetForMap(
				getGetUrl(url, params));
	}
	
	/**
	 * get请求(带参数和请求头)获取map
	 * @param url
	 * @param params
	 * @param headerMap
	 * @return
	 */
	public static Optional<Map<String, Object>> doGetForMap(String url, Map<String, Object> params, Map<String, Object> headerMap) {
		return doGetForMap(
				getGetUrl(url, params),getHeaders(headerMap));
	}
	
	/**
	 * get请求获取List<Map>
	 * @param url
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doGetForListMap(String url) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return callForListMap(requestBuilder.build());
	}
	
	/**
	 * get请求获取List<Map>
	 * @param url
	 * @param headers
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doGetForListMap(String url, Headers headers) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		return callForListMap(requestBuilder.build());
	}
	
	/**
	 * get请求(带参数)获取List<Map>
	 * @param url
	 * @param params
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doGetForListMap(String url, Map<String, Object> params) {
		return doGetForListMap(
				getGetUrl(url, params));
	}
	
	/**
	 * get请求(带参数和请求头)获取List<Map>
	 * @param url
	 * @param params
	 * @param headerMap
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> doGetForListMap(String url, Map<String, Object> params, Map<String, Object> headerMap) {
		return doGetForListMap(
				getGetUrl(url, params), getHeaders(headerMap));
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
	 * get请求获取结果
	 * @param url
	 * @param params
	 * @param headerMap
	 * @param callback
	 */
	public static void doGet(String url, Map<String, Object> params, Map<String, Object> headerMap, Callback callback) {
		doGet(
				getGetUrl(url, params), getHeaders(headerMap), callback);
	}
	
	/**
	 * get请求获取结果
	 * @param url
	 * @param callback
	 */
	public static void doGet(String url, Callback callback) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		call(requestBuilder.build(), callback);
	}
	
	/**
	 * get请求获取结果
	 * @param url
	 * @param headers
	 * @param callback
	 */
	public static void doGet(String url, Headers headers, Callback callback) {
		Request.Builder requestBuilder = new Request.Builder().url(url);
		call(requestBuilder.headers(headers).build(), callback);
	}
	
	/**
	 * post上传附件和表单
	 * @param url
	 * @param files
	 * @param params
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static <K, V>Optional<String> uploadMultiFiles(String url, File[] files, Map<K, V> params) throws FileNotFoundException {
		
		Request request = new Request.Builder().url(url)
										.post(getRequestBody(files, params)).build();

		return call(request);
	}
	
	/**
	 * post上传附件和表单
	 * @param url
	 * @param files
	 * @param params
	 * @param headerMap
	 * @return
	 * @throws FileNotFoundException
	 */
	public static <K, V>Optional<String> uploadMultiFiles(String url, File[] files, Map<K, V> params, Map<K, V> headerMap) throws FileNotFoundException {
		
		Request request = new Request.Builder().url(url)
				.headers(getHeaders(headerMap))
				.post(getRequestBody(files, params)).build();

		return call(request);
	}

	/**
	 * post上传附件和表单
	 * @param url
	 * @param files
	 * @param params
	 * @return
	 * @throws FileNotFoundException 
	 */
	public static <K, V>Optional<Map<String, Object>> uploadMultiFilesForMap(String url, File[] files, Map<K, V> params, Map<K, V> headerMap) throws FileNotFoundException {
		Optional<String> result = uploadMultiFiles(url, files, params, headerMap);
		if(result.isPresent()) {
			try {
				return Optional.ofNullable(Utils.jsonUtils().toMap(result.get()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}
	
	public static <K, V>Optional<Map<String, Object>> uploadMultiFilesForMap(String url, File[] files, Map<K, V> params) throws FileNotFoundException {
		Optional<String> result = uploadMultiFiles(url, files, params);
		if(result.isPresent()) {
			try {
				return Optional.ofNullable(Utils.jsonUtils().toMap(result.get()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return Optional.empty();
	}
	
	/**
	 * 构造请求头
	 * @param headerMap
	 * @return
	 */
	public static <K,V>Headers getHeaders(Map<K, V> headerMap) {
		if(headerMap == null || headerMap.isEmpty()) {
			return null;
		}
		okhttp3.Headers.Builder builder = new Headers.Builder();
		Iterator<K> itor = headerMap.keySet().iterator();
		while(itor.hasNext()) {
			K key = itor.next();
			V value = headerMap.get(key);
			builder.add(key.toString(), value!=null?value.toString():"");
		}
		return builder.build();
	}
	
	/**
	 * 请求并获取结果字符串(同步请求)
	 * @param request
	 * @return
	 */
	public static Optional<String> call(Request request) {
		Call call = client().newCall(request); 
		try {
			Response response = call.execute();
			if(response.isSuccessful()) {
				return Optional.of(response.body().string());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	/**
	 * 请求并获取结果字符串(异步请求)
	 * @param request
	 * @return
	 */
	public static void call(Request request, final Callback callback) {
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
	 * @throws Exception 
	 */
	public static Optional<Map<String, Object>> callForMap(Request request) {
		try {
			Optional<String> result = call(request);
			if(result.isPresent()) {
				return Optional.ofNullable(Utils.jsonUtils().toMap(result.get()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Optional.empty();
	}
	
	/**
	 * 请求并获取结果List<Map<String, Object>>(同步请求)
	 * @param request
	 * @return
	 */
	public static Optional<List<Map<String, Object>>> callForListMap(Request request) {
		try {
			Optional<String> result = call(request);
			if(result.isPresent()) {
				return Optional.ofNullable(Utils.jsonUtils().toListMap(result.get()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return  Optional.empty();
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
			String value = params.get(key)==null?"":params.get(key).toString();
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
	
	/**
	 * 通过参数构建请求体
	 * 
	 * <p>
	 * 	请事先对附件的存在性进行验证
	 * </p>
	 * 
	 * @param params
	 * @return
	 * @throws FileNotFoundException 
	 */
	private static <K,V> RequestBody getRequestBody(File[] files, Map<K, V> params) throws FileNotFoundException {
		okhttp3.MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
		/*附件部分*/
		if(!ListUtils.isEmpty(files)) {
			for (File file : files) {
				if(file == null) {
					throw new FileNotFoundException(
							StringUtils.concat("文件上传失败:","文件不能为空"));
				}
				if(!file.exists()) {
					throw new FileNotFoundException(
							StringUtils.concat("文件上传失败:","文件[",file.getPath(),"]未找到"));
				}
				if(!file.isFile()) {
					throw new FileNotFoundException(
							StringUtils.concat("文件上传失败:","文件[",file.getPath(),"]不是个文件"));
				}
				RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
				builder = builder.addFormDataPart("file", file.getName(), fileBody);									
			}
		}
		/*表单部分*/
		if(!MapUtils.isEmpty(params)) {
			Iterator<K> itor = params.keySet().iterator();
			while(itor.hasNext()) {
				 K key = itor.next();
				 V value = params.get(key);
				 builder.addFormDataPart(key.toString(), value==null?null:value.toString());
			}
			
			/*不能这么写，这样写jfinal只能通过getPara("params")获得到参数，还得自己解析
			 * if(!MapUtils.isEmpty(params)) {
				builder.addPart(Headers.of(
			            "Content-Disposition",
			            "form-data; name=\"params\""),
						getRequestBody(params));
			}*/
		}
		
		return  builder.build();
	}
	
	
	/**==============辅助类======================*/
	public interface Callback {
		void onSuccess(String result) throws IOException;
		void onFailure(Exception exception);
	}
	
}
