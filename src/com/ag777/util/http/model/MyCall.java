package com.ag777.util.http.model;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

import com.ag777.util.http.HttpUtils;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 有关Call的工具类(二次封装okhttp3)
 * <p>
 * 		发送请求并从结果中提取需要信息
 *	 	请防止二次调用
 * </p>
 * 
 * @author ag777
 * @version create on 2018年03月30日,last modify at 2018年03月30日
 */
public class MyCall {
	
	private Call call;
	private Response response;
	
	public MyCall(Call call) {
		this.call = call;
	}
	
	public Call getCall() {
		return call;
	}
	
	/**
	 * 取消请求
	 */
	public void cancel() {
		if(call != null) {
			call.cancel();
		}
	}
	
	public Response executeForResponse() throws IOException {
		response = HttpUtils.execute(call);
		return response;
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @return
	 * @throws IOException 
	 */
	public Optional<String> executeForStr() throws IOException{
		executeForResponse();
		return HttpUtils.responseStr(response);
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * <p>
	 *  不论返回什么强制获取字符串
	 * </p>
	 * 
	 * @return
	 * @throws IOException
	 */
	public Optional<String> executeForStrForce() throws IOException {
		executeForResponse();
		return HttpUtils.responseStrForce(response);
	}
	
	/**
	 * 发送请求并转化为map
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @return
	 * @throws Exception 
	 */
	public Optional<Map<String, Object>> executeForMap() throws IOException {
		executeForResponse();
		return HttpUtils.responseMap(response);
	}
	
	/**
	 * 发送请求并得到返回字符串
	 * <p>
	 *  不论返回什么强制转化为json
	 * </p>
	 * 
	 * @return
	 * @throws IOException
	 */
	public Optional<Map<String, Object>> executeForMapForce() throws IOException{
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
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T>Optional<T> executeForObj(Class<T> clazz) throws Exception {
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
	 * @param clazz
	 * @return
	 * @throws Exception
	 */
	public <T>Optional<T> executeForObjForce(Class<T> clazz) throws Exception {
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
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public <T>Optional<T> executeForObj(Type type) throws Exception {
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
	 * @param type
	 * @return
	 * @throws Exception
	 */
	public <T>Optional<T> executeForObjForce(Type type) throws Exception {
		executeForResponse();
		return HttpUtils.responseObjForce(response, type);
	}
	
	/**
	 * 发送请求并得到返回流
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @return
	 * @throws Exception 
	 */
	public InputStream executeForInputStream() throws IOException {
		executeForResponse();
		return HttpUtils.responseInputStream(response);
	}
	
	/**
	 * 发送请求，并将请求流保存成本地文件
	 * <p>
	 * 	只有response.isSuccessful()时才有返回,否则抛出异常
	 * </p>
	 * 
	 * @param targetPath
	 * @return
	 * @throws Exception 
	 */
	public  Optional<File> executeForFile(String targetPath) throws IOException {
		executeForResponse();
		return HttpUtils.responseFile(response, targetPath);
	}
}
