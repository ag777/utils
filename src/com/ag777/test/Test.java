package com.ag777.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.plaf.ListUI;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import com.ag777.util.Utils;
import com.ag777.util.db.DbHelper;
import com.ag777.util.file.FileUtils;
import com.ag777.util.file.PropertyUtils;
import com.ag777.util.gson.GsonUtils;
import com.ag777.util.http.HttpUtils;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.DateUtils;
import com.ag777.util.lang.DateUtils.TimeUnit;
import com.ag777.util.lang.exception.model.ExceptionHelper;
import com.ag777.util.lang.Formatter;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.SystemUtils;
import com.ag777.util.lang.collection.CollectionAndMapUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.collection.MapUtils;
import com.ag777.util.lang.reflection.ClassUtils;
import com.ag777.util.lang.reflection.ReflectionHelper;
import com.ag777.util.other.AlgorithmUtils;
import com.google.gson.reflect.TypeToken;

public class Test {

	private static String a;
	private Double d;
	
	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
	
	public Double getD() {
		return d;
	}

	public void setD(Double d) {
		this.d = d;
	}

	public static void doSthErr(String in) {
		Integer.parseInt(in);
	}
	
	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
//		Test t = new Test();
//		t.setA("123");
//		t.setD(2d);
//		
//		Map<String, Object> map = new HashMap<>();
//		map.put("3", t);
//		
//		System.out.println(new Gson().toJson(map));
//		System.out.println(GsonUtils.get().toJson(map));
//		ExceptionHelper exHelper = new ExceptionHelper(Test.class.getPackage().getName());
//		try {
//			Test.doSthErr("");
//		} catch(Exception ex) {
//			
//			String errMsg = exHelper.getErrMsg(ex);
//			System.out.println(errMsg);
//			Map<String, Object> map = GsonUtils.get().toMap(errMsg);
//			System.out.println(GsonUtils.get().toJson(map));
//		}
		
//		JSite s = new JSite();
//		s.setUrl("www.baidu.com");
//			Rule r = new Rule();
//			r.setFun("attr");
//			r.setParam("ok");
//			r.setRegex(".?+");
//		s.setTitle(r);
//		Map<String, RuleInterf> map = ReflectionHelper.get(JSite.class).getFieldMap(s, RuleInterf.class);
//		System.out.println(GsonUtils.get().serializeNulls().toJson(map));
		
//		try {
//			int i=1;
//			throw new RuntimeErrorException(new Error("哇xx"));
//		} catch(Exception ex) {
//			System.out.println("捕获异常");
//		}
//		Map<String, Object> map = ReflectionHelper.get(ReflectionHelper.class).getMethodMap();
//		map.forEach((k,v)->{
//			if(v instanceof Map) {
//				((Map) v).forEach((k2,v2)-> {
//					if(v2 instanceof Class) {
//						System.out.println(k+"|||"+k2+"||"+((Class) v2).getName());
//					}
//					
//				});
//			}
//		});
//		System.out.println(GsonUtils.get().toJson(map));
		
		
		
//		Map<String, Object> map = MapUtils.put("getFieldMap", MapUtils.put("returnType", Map.class))
//					.put("get", MapUtils.put("returnType", ReflectionHelper.class))
//					.put("initFieldList", MapUtils.put("returnType", void.class))
//					.put("d", MapUtils.put("returnType", List.class)
//				);
//		String json = GsonUtils.get().toJson(map);
//		System.out.println(json);
		
//		ReflectionHelper.get(Gson.class).getMethodList(new ReflectionHelper.Filter() {
//
//			@Override
//			public boolean dofilter(Method method, Class<?> returnType, int i, Class<?>[] parameterTypes) {
//				System.out.println(method.getName()+"||"+returnType+"||"+GsonUtils.get().toJson(parameterTypes));
//				return true;
//			}
//		});
//		Map<Map<String, Boolean>, Integer> list = new HashMap<>();
//		System.out.println(
//				ReflectionHelper.getTypeOfTByType(
//						new TypeToken<Map<Map<String, Boolean>, Integer>>() {}.getType())
//				.getTypeName());
//		System.out.println(list.getClass().getGenericSuperclass().getTypeName());
//		getSuperClassGenricType(list.getClass(), 0);
//		System.out.println(TypeToken.get(list.getClass()).getParameterized(rawType, typeArguments));
//		System.out.println(
//				ReflectionHelper.getTypeOfTByType(
//						TypeToken.get(list.getClass()).getType())
//				.getTypeName());
		
//		GsonUtils.get().fromJson("{}", new TypeToken<Map<Map<String, Boolean>, Integer>>() {}.getType());
//		Type t = new TypeToken<List<List<Integer>>>() {}.getType();
//		System.out.println(t.getTypeName());
		
//		HttpUtils.doGet("https://www.baidu.com/", new HttpUtils.Callback() {
//
//			@Override
//			public void onSuccess(String result) throws IOException {
//				System.out.println(result);
//			}
//
//			@Override
//			public void onFailure(Exception exception) {
//				
//			}
//		});
//		Type type = new TypeToken<List<String>>(){}.getType();
//		System.out.println(new TypeToken<String>(){}.getRawType().isAssignableFrom(String.class));
		
//		File f = HttpUtils.downLoad("https://i.pximg.net/c/150x150/img-master/img/2017/06/02/16/39/57/63183759_p0_master1200.jpg", "E:\\a.jpg");
//		System.out.println(f.exists());
//		String a = "eno16777736: flags=4163<UP,BROADCAST,RUNNING,MULTICAST>  mtu 1500";
//		String nn = RegexUtils.get(a, "eno([^:]*):\\s", "$1");
//		System.out.println(nn);
		
//		ContantUtils.osType();
//		System.out.println(ContantUtils.isKylin());
//		System.out.println("\\r\\n");
//		System.out.println("\r\n".equals(null));
//		System.out.println(ContantUtils.javaHome());
//		System.out.println(ContantUtils.javaVersion());
//		String s = FileUtils.findText("E:\\issue", "^\\s*([^\\s]*).*release","$1");
//		if("NeoKylin".equals(s)) {
//			System.out.println("啊哈哈");
//		}
//		List<Object> list = ListUtils.empty();
//		
//		System.out.println(
//				 
//				ListUtils.toString(list, "||"));
//		System.out.println(FileUtils.getFilePrefix("asd"));
//		Console.log(
//				RegexUtils.get("a.s11", "\\.([^\\.]*)$","$1",""));
		
//		System.out.println(FileUtils.copy("F:\\temp\\a\\b", "F:\\temp\\a\\z"));
//		Console.log(CMDUtils.doCmdForList("ipconfig", null,"本地([^:]*):","$1"));
//		String s = "eth0      Link encap:Ethernet  HWaddr 00:50:56:99:FE:F3";
//		System.out.println(
//				RegexUtils.get(s, "^eth(\\d+)\\s", "$1"));
		
//		HttpUtils.downLoad("https://i.pximg.net/c/150x150/img-master/img/2017/06/14/16/13/18/63379679_p0_master1200.jpg",
//				"E:\\a.png");
//		System.out.println(
//				Formatter.storage(SystemUtils.totalMemory()*8));
//		SystemUtils.addShutdownHook(new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				System.out.println(123);
//			}
//		}));
//		Integer a = null;
//		System.out.println(a.getClass());
//		String a = "asdbssqweq";
//		Console.log(
//				RegexUtils.findAll(a, "s(.+?)", "$1"));
		
//		System.out.println(FileUtils.copy("F:\\temp\\a\\b", "F:\\temp\\a\\x"));
//		System.out.println(FileUtils.delete("F:\\temp\\a\\x"));
//		String a = "a.b.c0";
//		System.out.println(a.replace(".", ","));
//		PropertyUtils pu = new PropertyUtils();
//		pu.load(new FileInputStream(new File("E:\\a.properties")));
//		System.err.println(pu.get("a"));
//		String a = "1,1,,";
//		System.out.println(a.split("\\,").length);
//		Console.log(a.split("\\,",0));
//		String a = "";
//		System.out.println(new LocalDate().monthOfYear().addToCopy(-1).toString("yyyyMM"));
//		Map<String, Object> a = new HashMap<>();
//		a.put("a", 1);
//		a.put("b", 2);
//		a.put("c", true);
//		Map<String, Object> b = new LinkedHashMap<>();
//		b.put("a", 1);
//		b.put("b", 2);
//		b.put("c", true);
//		Map<String, Object> c = new TreeMap<>();
//		c.put("a", 1);
//		c.put("b", 2);
//		c.put("c", true);
//		Console.log(a);
//		Console.log(b);
//		Console.log(c);
//		List a = new ArrayList<>();
//		List b = new ArrayList<>();
//		Console.log(new ListHelper<>(a).getList());
//		int a = (1<<0);
//		System.out.println(a);
//		
//		System.out.println(src);
//		System.out.println(RegexUtils.count(src, "1"));
//		int a = -1>>-2;
//		System.out.println(a);
		
		/**
		 * 签到统计
		 */
//		List<String> dateList = ListHelper.addAllItem(
//				"2017-06-20","2017-06-22").getList();
//		
//		List<String> holidayList = ListHelper.addAllItem(
//				"2017-06-22").getList();
//		//统计签到字符串
//		String src = DateUtils.dateStatistics(dateList, holidayList, "2017-06-20", "2017-06-23");
//		Console.log(src);
//		List<String> dateList2 = DateUtils.getDateList("2017-06-20", "2017-06-23");
//		//缺勤日期
//		List<String> absenceList = new ArrayList<>();
//		for(int i=0;i<dateList2.size();i++) {
//			String date = dateList2.get(i);
//			if('0' == src.charAt(i)) {
//				absenceList.add(date);
//			}
//		}
		
//		Console.log(absenceList);
//		
//		//连续签到天数
//		String a = "111011110110011";
//		System.out.println(RegexUtils.find(a, "1*$").length());
		
		/**
		 * 算法测试
		 */
//		Map<Integer, Integer> map = new MapHelper<Integer, Integer>().put(1, 3).put(3, 15).getMap();
//		
//		Console.log(AlgorithmUtils.statisticsString(map, 16));
		
		/**
		 * 时间差计算测试
		 */
//		int difference = DateUtils.between("10:33:14", "10:34:15", "HH:mm:ss", TimeUnit.SECOND);
//		System.out.println(difference);
		
//		Console.log("啦啦啦");
		//代理
//		JsoupUtils u = JsoupUtils
//			.connect(
//					"https://www.google.co.jp/search?q=123&oq=123&aqs=chrome..69i57j69i60l4j69i59.383j0j7&sourceid=chrome&ie=UTF-8",
//					"127.0.0.1",8099);
//		System.out.println(u.getHtml());
//		Console.log(RegexUtils.match("13000000008", RegexRule.PHONE.MOBILE));
//		System.out.println(new DateTime().dayOfMonth().withMinimumValue().toString("d"));
//		String[] strs = new String[]{"a\\sqltemplate\\a.sql","a/a/a/a/a/sqltemplate/b.sql","sqltemplate/b.sql"};
//		for (String item : strs) {
//			System.out.println(item);
//			System.out.println("(.+\\\\)?sqltemplate\\\\.+\\.sql");
//				boolean a = RegexUtils.match(item, "(.+\\\\)?sqltemplate\\\\.+\\.sql");
//				System.out.println(a);
//		}
//		"*/.*\\sqltemplate.*\\.sql"
//		ClassLoader l = Thread.currentThread().getContextClassLoader();
//		Enumeration<URL> es = l.getResources("a.sql");
//		while(es.hasMoreElements()) {
//            URL url = (URL)es.nextElement();
//            System.out.println(url.getPath());
//		}
//		Test t = new Test();
//		t.setA("123");
//		t.setD(2d);
//		ReflectionHelper<Test> u = new ReflectionHelper<>(Test.class);
//		System.out.println(u.getFieldMap(t).get("a"));
//		String[] a = {"a","b"};
//		List list = Arrays.asList(a);
//		list.add("a");
		
//		String a = "http://www.baidu.com:80/:443";
//		a = a.replaceFirst("(http://|https://)([^/]+)(:80|:443)(?!\\d)", "$3");
//		System.out.println(a);
//		String a = "atabase";
//		System.out.println(a.matches("^(host|web|database)$"));
		
//		Pattern a = Pattern.compile("^(a|b|c)$");
//		System.out.println(a.matcher("a").matches());
//		System.out.println(a.matcher("b").matches());
//		System.out.println(a.matcher("d").matches());
//		System.out.println(a.matcher("c").matches());
//		Pattern.matches("", "");
//		Map<String, Object> map = GsonUtils.get().toMap("{\"scan_thread_num\":\"60\",\"report_paranoia\":\"2\",\"tcp_scan_type\":1,\"task_id\":\"0\",\"online_scan_type\":\"1\",\"online_scan_port\":\"80,443,139,445,3389,22,23,1433,1521,1533,3306,5000,5236,50000\",\"debug_flag\":0,\"max_scan_host_num\":75,\"param_model_id\":8,\"vul_scan_time\":\"120\",\"max_scan_thread_num\":200,\"udp_port_time\":\"300\",\"is_refuse_concur\":1,\"scan_risk\":\"c,h,m,l,i,\",\"crack_count\":\"250\",\"tcp_port_time\":\"300\",\"is_rely_scan\":1,\"scan_param_id\":101,\"tcp_port_scope\":\"1433,1521,1533,3306,5000,5236,50000\",\"scan_host_num\":\"15\",\"is_safe_scan\":1,\"online_timeout\":\"4\",\"is_force_scan\":0,\"udp_port_scope\":\"none\",\"udp_scope_type\":\"4\",\"tcp_scope_type\":\"3\"}");
//		map.forEach((k,v)->{
//			
//		});
//		Pattern p = Pattern.compile(".*");
//		System.out.println(p.toString());
//		String s = FileUtils.readText("F:\\temp\\临时\\model.txt", "\n");
//		System.out.println("\r\n".equals(SystemUtils.lineSeparator()));
//		Matcher m1 = Pattern.compile("<foreach>([\\s\\S]+?)</foreach>").matcher(s);
//		while(m1.find()) {
//			System.out.println("找到:"+m1.group(1));
//		}
		
//		System.out.println("".substring(0));
		
//		Pattern p = Pattern.compile("#\\{(.+?)\\}");
//		Matcher m = p.matcher(s);
//		
//		while(m.find()) {
////			System.out.println(m.group(1));
//			s = s.replace(m.group(), m.group(1));
//		}
//		System.out.println(s);
//		Matcher m = Pattern.compile("#\\{item\\.(.+?)\\}").matcher("#{item.so}");
//		while(m.find()) {
//			System.out.println(m.group(1));
//		}
		
		
		//少前后勤数据获取
//		List<List<Object>> list = new ArrayList<>();
//		JsoupUtils u = JsoupUtils.connect("http://en.gfwiki.com/wiki/Logistic_Support#Chapter_00");
//		u.getDoc()
//			.select("table.gf-table")
//			.select("tr")
//			.forEach((tr)->{
//				if(tr.children().size() == 7 && !tr.child(0).is("th")) {
//					String td1 = tr.child(0).html();	//number
//					String td2 = tr.child(1).html();	//mp
//					String td3 = tr.child(2).html();	//a
//					String td4 = tr.child(3).html();	//r
//					String td5 = tr.child(4).html();	//sp
//					StringBuilder sb_ar = new StringBuilder();
//					tr.child(5).select("img").forEach(img-> {
//						if(sb_ar.length() > 0) {
//							sb_ar.append(",");
//						}
//						String src = img.attr("src");
//						sb_ar.append(RegexUtils.find(src, "/.+?_(.+?).png/", "$1"));
//					});;//ar
//					String td7 = tr.child(6).html();	//timer
//					String[] nums = td1.split("-");
//					list.add(
//						new ListHelper<Object>()
//							.add(Integer.parseInt(nums[0]))
//							.add(Integer.parseInt(nums[1]))
//							.add(Integer.parseInt(td2))
//							.add(Integer.parseInt(td3))
//							.add(Integer.parseInt(td4))
//							.add(Integer.parseInt(td5))
//							.add(sb_ar.toString())
//							.add(td7).getList()
//						);
//				}
//			});
//		
//		System.out.println(GsonUtils.get().serializeNulls().toJson(list).replace("\"", "\\\""));
		
//		System.out.println(
//				DateUtils.between("2016-04-23", "2017-07-01", "yyyy-MM-dd", DateUtils.TimeUnit.DAY));
//		Console.showSourceMethod(true);
//		Console.log(
//				Formatter.formatJson(
//					GsonUtils.get().toJson(
//							new MapHelper<String,Object>()
//								.put("a", 1).getMap()
//							),
//					"\t")
//				);
		
//		int a = new MapHelper<String, Object>().put("a", 1).get("a", 0);
//		Console.log(
//				RegexUtils.find("c:\\a\\2017_07_26\\b.png", "\\\\(\\d{4})_(\\d{2})_(\\d{2})\\\\","$1-$2-$3"));
//		Console.log(
//			AlgorithmUtils.statisticsString(
//					new MapHelper<Integer, Integer>().put(1, 3).put(6, 2).put(2, 3).getMap(), 4));
		
//		ExceptionHelper eh = new ExceptionHelper("com.ag777.test");
//		try {
//			throw new NullPointerException();
//		} catch (Exception e) {
//			Console.log(eh.getErrMsg(e));
//			Console.log(ExceptionHelper.getStackTrace(e));
//		}
		
//		Console.log("这里会输出");
//		Utils.deviceMode(false);
//		Console.log("这里将不会输出");
		
//		String s = "asd  io";
//		Console.log(s.split("\\s+"));
		
//		Console.log("1",null);
//		Console.log("123");
		
//		Console.log(Utils.info());
//		Map<String, Object> ss = HttpUtils.doPost("http://localhost:8080/CodeapesWeb/authorization/login.do", 
//				new MapHelper<String, Object>().put("account", "admin").put("password", "123").getMap());
//		
//		Console.log(ss);
//		Console.log(Integer.parseInt("00"));
//		FileOutputStream os = new FileOutputStream(new File("e:/b.txt"));
//		os.write(2);
//		os.flush();
//		os.close();
		
//		String a = "aaa[bbb[ccc,ddd[eee,fff]],ggg[hhh,iii]]";
//		Console.log(
//				RegexUtils.findAll(a, ".+?\\[[^\\]]+?,[^\\]]+?\\]]"));
		
//		Date now=new Date(); 
//    	String todayDate=new SimpleDateFormat("HH:mm:ss").format(now); 
//    	String nowDate=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now);
//		System.out.println("INSERT INTO ndc_data_event(msg_id,client_id,org_uuid,msg_type,dest_ip,src_ip,site_url,threat_type,threat_grade,threat_count,start_time,end_time,msg_time)  "
//			    			+ "SELECT msg_id,client_id, org_uuid, msg_type, dest_ip, src_ip, site_url, threat_type, threat_grade, count(threat_count), MIN(start_time), MAX(end_time), MAX(msg_time) " 
//			    			+ "FROM ndc_data_event_buffe a WHERE a.msg_time <= '" + nowDate + "' GROUP BY a.client_id, a.org_uuid, a.msg_type, a.dest_ip, a.src_ip, a.site_url, a.threat_type, a.threat_grade");
//		"https://nj01ct01.baidupcs.com/file/a85c47749411778727c61142cb40b0cb?bkt=p3-1400a85c47749411778727c61142cb40b0cb974e28870000039064f4&fid=3590765830-250528-579917671018102&time=1505289118&sign=FDTAXGERLQBHSK-DCb740ccc5511e5e8fedcff06b081203-srDr%2BZdtkOlquFw9CPmKmJH%2BlL4%3D&to=63&size=59794676&sta_dx=59794676&sta_cs=0&sta_ft=zip&sta_ct=7&sta_mt=7&fm2=MH,Yangquan,Netizen-anywhere,,fujian,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=1400a85c47749411778727c61142cb40b0cb974e28870000039064f4&sl=75497550&expires=8h&rt=pr&r=451321751&mlogid=5929502566147908889&vuk=3590765830&vbdid=1140420958&fin=BaiduLBS_AndroidSDK_Sample.zip&fn=BaiduLBS_AndroidSDK_Sample.zip&rtype=1&iv=0&dp-logid=5929502566147908889&dp-callid=0.1.1&hps=1&tsl=95&csl=95&csign=frk7o3tlglMRUevXh9E4vW4Or44%3D&so=0&ut=6&uter=4&serv=0&uc=3317763485&ic=2080509762&ti=220620fd8d32b1153ea443aa0fa10943c5cd36d660821bda&by=themis"
//		String url = "https://nj01ct01.baidupcs.com/file/06c9e3c452f1f7633404da8753643428?bkt=p3-140006c9e3c452f1f7633404da8753643428585eb10e00000000aa94&fid=3590765830-250528-259170020929837&time=1505289353&sign=FDTAXGERLQBHSK-DCb740ccc5511e5e8fedcff06b081203-BWBLFpAQfi%2BNMZmfxP8Io1IWnYE%3D&to=63&size=43668&sta_dx=43668&sta_cs=1&sta_ft=rar&sta_ct=7&sta_mt=7&fm2=MH,Yangquan,Netizen-anywhere,,fujian,ct&newver=1&newfm=1&secfm=1&flow_ver=3&pkey=140006c9e3c452f1f7633404da8753643428585eb10e00000000aa94&sl=76283982&expires=8h&rt=pr&r=450479109&mlogid=5929565599386910482&vuk=3590765830&vbdid=1140420958&fin=spig.rar&fn=spig.rar&rtype=1&iv=0&dp-logid=5929565599386910482&dp-callid=0.1.1&hps=1&tsl=83&csl=83&csign=PY263LWZnpf6%2F9U4PPJ9emJQWyg%3D&so=0&ut=6&uter=4&serv=0&uc=3317763485&ic=2080509762&ti=220620fd8d32b1158953b0d572ef0d487ee58c14c49a928c&by=themis";
//		HttpUtils.downLoad(url,
//				"e://a.rar");
//		Console.setFormatMode(true);
//		Console.log(Utils.info());
		
//		Object a = new int[]{1,2,3};
//		Object b = new Integer[]{1,23};
		
//		List<String> a = ListUtils.toList("2,3,4,5", ",");
//		for(int i=0;i<a.size();i++) {
//			a.set(i, "asd");
//		}
//		Console.log(a);
//		Pattern.compile("**");
		
//		List<Integer> list = ListUtils.toList(new Integer[]{1,2,3});
//		Console.log(list);
//
//		ListUtils.remove(list, 2);
//		Console.log(list);
		
//		String json = "{\"a\":1}";
//		Map<String, String> map = GsonUtils.get().fromJson(json, new TypeToken<Map<String, String>>() {}.getType());//MapUtils.toMapByJson();
//		Iterator<String> itor = map.keySet().iterator();
//		while(itor.hasNext()) {
//			String key = itor.next();
//			System.out.println(map.get(key).getClass());
//		}
//		Console.log(map);
		
//		Long a = 2147483648l;
//		System.out.println(a.intValue());
//		System.out.println(Integer.parseInt("2147483647"));
		
//		String a = "aacaa";
//		Matcher matcher = RegexUtils.getMatcher(a, "a([^a]+?)a");
//		while(matcher.find()) {
//			System.out.println(matcher.group(1));
//		}
		
//		Object obj = Array.newInstance(int.class, 3);
//		Console.log(obj);
//		System.out.println(obj.getClass().equals(int[].class));
//		System.out.println(obj.getClass().isArray());
		
		int[] array = (int[]) CollectionAndMapUtils.newArray(int.class, 3);//(int[]) Array.newInstance(int.class, 3);
		Console.log(array);
		System.out.println(array.getClass().getName());
		System.out.println(array.getClass().isArray());
		
//		System.out.println(int.class.equals(Integer.class));
	}
   
    
   
}
