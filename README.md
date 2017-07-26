Utils
====
工具库大致分为几个模块<br>
基础/jsoup爬虫/文件/gson封装<br>
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
