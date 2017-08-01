个人用的工具库
====
工具库大致分为几个模块<br>
基础/网络/其它<br>
全局
----
-Utils【工具包统一配置类】<br>
【说明】可以对工具包进行一些配置比如控制Console类的输出,替换工程的json转换器,cmd命令的读取编码等,建议这些操作在工程初始化的时候完成<br>
【例子】
```Java
	Console.log("这里会输出");
	Utils.deviceMode(false);
	Console.log("这里将不会输出");
```
【输出】这里会输出<br>

一.基础模块<br>
----

-CMDUtils【命令行执行工具类】<br>
【说明】经过几个项目的历练总结出的工具类，能够避免长时间命令执行造成的程序挂起，并简单封装了压缩解压等基本命令供调用<br>
【例子】linux环境下调用CMDUtils.doCmd("ifconfig");<br>

-CodingUtils【编码转化工具类】<br>
【说明】仅做整合，非本人所写，不多做说明经<br>

-Console【统一输出打印类】<br>
【说明】只是统一控制台打印的出口,方便后期维护,安卓可以修改该工具类的输出为Log.i或者一些第三方的输出库<br>

-DateUtils【日期工具类】<br>
【说明】二次封装joda-time库，所以需要引入joda-time.jar包,功能广泛<br>
【例子】DateUtils.between("2016-04-23", "2017-07-01", "yyyy-MM-dd", DateUtils.TimeUnit.DAY)计算出两天的日期差为434天<br>

-Formatter【格式化工具类】<br>
【说明】提供一些格式化方法,应对前端页面展示或者与项目中其他成员进行数据交互特殊需求的情况<br>
【例子】<br>
```Java
  Console.log(
    Formatter.formatJson(
      GsonUtils.get().toJson(
          new MapHelper<String,Object>()
            .put("a", 1).getMap()
          ),
      "\t")
  );
```
【输出】<br>
```
    com.ag777.test.Test【main】:
    {
        "a" : 1
    }
```

-IOUtils【IO操作工具类】<br>
【说明】本来想二次封装开源库okio，后来试了下..还是重新写比较好控制<br>

-ListHelper【列表操作工具类】<br>
【说明】就是为了支持链式操作，并且封装了一些基本操作方法，比如遍历并删除(每次写倒序挺麻烦的),注意数组转List用Arrays.asList()的话不能操作该列表<br>

-MapHelper【哈希表操作工具类】<br>
【说明】作用类似于ListHelper，多的就是对取数据的操作进行了封装<br>
【例子】<br>
```Java
  int a = new MapHelper<String, Object>().put("a", 1).get("a", 0);
```
结果a为1<br>

-RegexUtils【正则工具类】<br>
【说明】封装了一些常用的正则方法,从字符串中提取关键信息,其中包含了通过开源项目提取好用的方法<br>
【例子】<br>
```Java
  Console.log(
	RegexUtils.find("c:\\a\\2017_07_26\\b.png", "\\\\(\\d{4})_(\\d{2})_(\\d{2})\\\\","$1-$2-$3"));
```
【输出】2017-07-26<br>

-StringUtils【字符串处理工具类】<br>
【说明】包含一些对字符串处理的方法，几乎每个工具库都会有<br>

-SystemUtils【系统常变量获取工具】<br>
【说明】本来叫ConstantUtils,存放诸如换行符,路径分割符等作为项目统一获取这些常量的方法，后台加入了一些其他东西，改名为SystemUtils<br>
【例子】SystemUtils.isLinux()通过该方法可以判断运行的系统是否为linux系统(麒麟系统判断比较粗糙，尽量不要使用)<br>

-AlgorithmUtils【算法辅助类】<br>
【说明】目前只有一个方法，比较抽象，特殊用途(比如统计连续签到用)，可以无视<br>
```Java
	Console.log(
		AlgorithmUtils.statisticsString(
			new MapHelper<Integer, Integer>().put(1, 3).put(6, 2).put(2, 3).getMap(), 4));
```
【输出】2000330<br>

-ExceptionHelper异常【信息采集类】<br>
【说明】帮助从异常中提取信息，可以作为异常日志的信息提取类(web/安卓通用)<br>
【例子】<br>
```Java
	ExceptionHelper eh = new ExceptionHelper("com.ag777.test");
	try {
		throw new NullPointerException();
	} catch (Exception e) {
		Console.log(eh.getErrMsg(e));
		Console.log(ExceptionHelper.getStackTrace(e));
	}
```
【输出】<br>
```
{ "异常信息": "java.lang.NullPointerException", "异常发生位置": "com.ag777.test.Test", "方法": "main", "行数": 427 }
["java.lang.NullPointerException","com.ag777.test.Test.main(Test.java:427)"]
```

-ReflectionHelper【反射辅助类】<br>

-TreeUtils【树结构工具类】<br>

二.网络模块<br>
----
-HttpUtils【网络工具类】<br>
【说明】二次封装开源库okhttp,这个库常用语在安卓上单独或配合retrofit等网络框架食用,偶然间发现可以在web项目中使用(jfinal的微信框架用的就是这个),直接就拉过来用了<br>

-JsoupUtils【jsoup爬虫工具类】<br>
【说明】配合正则表达式和css选择器可以快速提取目标网页中的信息(灵感来源某开源库)，可以用于做网页客户端<br>

三.其它<br>
----
-FileUtils【文件操作工具类】<br>

-ExcelHelper【excel验证辅助类】<br>

-PropertyUtils【.property文件的读取辅助类】<br>

-GsonUtils【json转换辅助类】<br>
【说明】支持链式调用，支持自定义拓展,具体请看代码。建议在工程初始化的时候全局持有一个对象<br>

-DbHelper【数据库操作辅助类】<br>
【说明】对数据库操作简单的封装。现在数据库框架这么多，写这个就是为了平时抛开项目的一些小需求，比如需要做数据库的整理工作，平常自己用也是听方便的(直接建一个java工程就好了)。注意使用时需要引入对应数据库的jar包并替换掉默认连接mysql用的驱动类名(Utils.dbDriverClassName(xxx))<br>
