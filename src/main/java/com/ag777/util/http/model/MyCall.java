package com.ag777.util.http.model;

import com.ag777.util.http.HttpUtils;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.exception.model.JsonSyntaxException;
import com.ag777.util.lang.interf.Disposable;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 有关Call的工具类(二次封装okhttp3)
 * <p>
 * 		发送请求并从结果中提取需要信息
 *	 	请防止二次调用
 * </p>
 * 
 * @author ag777
 * @version create on 2018年03月30日,last modify at 2023年04月03日
 */
public class MyCall implements Disposable, AutoCloseable {
	
	private Call call;
	private Response response;
	
	public MyCall(Call call) {
		this.call = call;
	}
	
	public Call getCall() {
		return call;
	}
	
	public Response getResponse() {
		return response;
	}
	
	/**
	 * 取消请求
	 */
	public void cancel() {
		if(call != null) {
			call.cancel();
		}
	}
	
	/**
	 * 获取返回码
	 * @return http请求码
	 */
	public Integer code() {
		if(response == null) {
			return null;
		}
		return response.code();
	}
	
	/**
	 * 获取返回头对应的map
	 * @return 请求头map
	 */
	public Map<String, Object> headers() {
		if(response == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		Headers headers = response.headers();
		for (String name : headers.names()) {
			String value = headers.get(name);
			map.put(name, value);
		}
		return map;
	}
	
	/**
	 * 发送请求并获取返回的封装
	 * @return Response
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 */
	public Response executeForResponse() throws SocketTimeoutException, IOException {
		response = HttpUtils.execute(call);
		return response;
	}
	
	/**
	 * 请求并获取返回码
	 * @return http请求码
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 */
	public Integer executeForCode() throws SocketTimeoutException, IOException {
		executeForResponse();
		return HttpUtils.responseCode(response);
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * @return 响应体中的字符串
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 */
	public Optional<String> executeForStr() throws SocketTimeoutException, IOException{
		executeForResponse();
		return HttpUtils.responseStr(response);
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * <p>
	 *  不论返回什么强制获取字符串
	 * </p>
	 * 
	 * @return 响应体中的字符串
	 * @throws SocketTimeoutException 一般为连不上接口
	 * @throws IOException 其他异常
	 */
	public Optional<String> executeForStrForce() throws SocketTimeoutException, IOException {
		executeForResponse();
		return HttpUtils.responseStrForce(response);
	}
	
	/**
	 * 发送请求并转化为map
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @return 响应体中的Map
	 * @throws SocketTimeoutException 一般为连不上接口
	 * @throws IOException 其他异常
	 */
	public Optional<Map<String, Object>> executeForMap() throws SocketTimeoutException, IOException {
		executeForResponse();
		return HttpUtils.responseMap(response);
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * <p>
	 *  不论返回什么强制转化为json
	 * </p>
	 * 
	 * @return 响应体中的Map
	 * @throws SocketTimeoutException 一般为连不上接口
	 * @throws IOException 其他异常
	 */
	public Optional<Map<String, Object>> executeForMapForce() throws SocketTimeoutException, IOException{
		executeForResponse();
		return HttpUtils.responseMapForce(response);
	}
	
	/**
	 * 发送请求并转为为javaBean
	 * <p>
	 * 只有response.isSuccessful()时才有返回,否则抛出异常
	 * 	转化失败会也会抛出异常
	 * </p>
	 * 
	 * @param clazz clazz
	 * @return 响应体中的对象
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 * @throws JsonSyntaxException json转化异常
	 */
	public <T>Optional<T> executeForObj(Class<T> clazz) throws SocketTimeoutException, IOException, JsonSyntaxException {
		executeForResponse();
		return HttpUtils.responseObj(response, clazz);
	}
	
	/**
	 * 发送请求并转为为javaBean
	 * <p>
	 * 不论返回什么强制转化为对象
	 * 	转化失败会也会抛出异常
	 * </p>
	 * 
	 * @param clazz clazz
	 * @return 响应体中的对象
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 * @throws JsonSyntaxException json转化异常
	 */
	public <T>Optional<T> executeForObjForce(Class<T> clazz) throws SocketTimeoutException, IOException, JsonSyntaxException  {
		executeForResponse();
		return HttpUtils.responseObjForce(response, clazz);
	}
	
	/**
	 * 发送请求并转为为javaBean
	 * <p>
	 * 只有response.isSuccessful()时才有返回,否则抛出异常
	 * 	转化失败会也会抛出异常
	 * </p>
	 * 
	 * @param type type
	 * @return 响应体中的对象
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 * @throws JsonSyntaxException json转化异常
	 */
	public <T>Optional<T> executeForObj(Type type) throws SocketTimeoutException, IOException, JsonSyntaxException {
		executeForResponse();
		return HttpUtils.responseObj(response, type);
	}
	
	/**
	 * 发送请求并转为为javaBean
	 * <p>
	 * 不论返回什么强制转化为对象
	 * 	转化失败会也会抛出异常
	 * </p>
	 * 
	 * @param type type
	 * @return 响应体中的对象
	 * @throws SocketTimeoutException SocketTimeoutException
	 * @throws IOException IOException
	 * @throws JsonSyntaxException json转化异常
	 */
	public <T>Optional<T> executeForObjForce(Type type) throws SocketTimeoutException, IOException, JsonSyntaxException  {
		executeForResponse();
		return HttpUtils.responseObjForce(response, type);
	}
	
	/**
	 * 发送请求并得到返回流
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @return 响应体中的流
	 * @throws SocketTimeoutException 一般为连不上接口
	 * @throws IOException 其他异常
	 */
	public Optional<InputStream> executeForInputStream() throws SocketTimeoutException, IOException {
		executeForResponse();
		return HttpUtils.responseInputStream(response);
	}
	
	/**
	 * 发送请求，并将请求流保存成本地文件
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @param targetPath targetPath
	 * @return 响应体中的流
	 * @throws SocketTimeoutException 一般为连不上接口
	 * @throws IOException 其他异常
	 */
	public  Optional<File> executeForFile(String targetPath) throws SocketTimeoutException, IOException {
		executeForResponse();
		return HttpUtils.responseFile(response, targetPath);
	}

	@Override
	public void dispose() {
		try {
			close();
		} catch (Exception ignored) {
		}
	}

	@Override
	public void close() {
		call=null;
		IOUtils.close(response);
		response = null;
	}
}