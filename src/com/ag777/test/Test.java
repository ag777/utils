package com.ag777.test;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.plaf.ListUI;

import org.joda.time.LocalDate;

import com.ag777.util.file.FileUtils;
import com.ag777.util.file.PropertyUtils;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.DateUtils;
import com.ag777.util.lang.ListHelper;
import com.ag777.util.lang.MapHelper;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.other.AlgorithmUtils;

public class Test {

	private String a;
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
		Map<Integer, Integer> map = new MapHelper<Integer, Integer>().put(1, 3).put(3, 15).getMap();
		
		Console.log(AlgorithmUtils.statisticsString(map, 16));
	}
    
    
   
}
