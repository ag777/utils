Utils
====
工具库大致分为几个模块<br>
基础/jsoup爬虫/文件/gson封装<br>
一.基础模块<br>
----
-CMDUtils【命令行执行工具类】<br>
【说明】经过几个项目的历练总结出的工具类，能够避免长时间命令执行造成的程序挂起，并简单封装了压缩解压等基本命令供调用<br>
【例子】linux环境下调用CMDUtils.doCmd("ifconfig");<br>

#-CodingUtils【编码转化工具类】<br>
【说明】仅做整合，非本人所写，不多做说明经<br>

#-Console【统一输出打印类】<br>
【说明】只是统一控制台打印的出口,方便后期维护,安卓可以修改该工具类的输出为Log.i或者一些第三方的输出库<br>

#-DateUtils【日期工具类】<br>
【说明】二次封装joda-time库，所以需要引入joda-time.jar包,功能广泛<br>
【例子】DateUtils.between("2016-04-23", "2017-07-01", "yyyy-MM-dd", DateUtils.TimeUnit.DAY)计算出两天的日期差为434天<br>

#-Formatter【格式化工具类】<br>
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
  {
    "a" : 1
  }
