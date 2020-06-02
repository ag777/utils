package com.ag777.util.http.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class MyCookieJar implements CookieJar{

	//Cookie缓存区
	private final Map<String, List<Cookie>> cookiesMap = new HashMap<String, List<Cookie>>();
	@Override
	public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
		//移除相同的url的Cookie
		String host = arg0.host();
		List<Cookie> cookiesList = cookiesMap.get(host);
		if (cookiesList != null){
			cookiesMap.remove(host);
		}
		//再重新天添加
		cookiesMap.put(host, arg1);
	}
	
	@Override
	public List<Cookie> loadForRequest(HttpUrl arg0) {
		List<Cookie> cookiesList = cookiesMap.get(arg0.host());
		//注：这里不能返回null，否则会报NULLException的错误。
		//原因：当Request 连接到网络的时候，OkHttp会调用loadForRequest()
		return cookiesList != null ? cookiesList : new ArrayList<Cookie>();
	}
	
}
