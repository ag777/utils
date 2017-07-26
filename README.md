工具库大致分为几个模块
基础/jsoup爬虫/文件/gson封装
一.基础模块
-CMDUtils【命令行执行工具类】
----
【说明】经过几个项目的历练总结出的工具类，能够避免长时间命令执行造成的程序挂起，并简单封装了压缩解压等基本命令供调用
【例子】linux环境下调用CMDUtils.doCmd("ifconfig");

#-CodingUtils【编码转化工具类】
【说明】仅做整合，非本人所写，不多做说明经

#-Console【统一输出打印类】
【说明】只是统一控制台打印的出口,方便后期维护,安卓可以修改该工具类的输出为Log.i或者一些第三方的输出库

#-DateUtils【日期工具类】
【说明】二次封装joda-time库，所以需要引入joda-time.jar包,功能广泛
【例子】DateUtils.between("2016-04-23", "2017-07-01", "yyyy-MM-dd", DateUtils.TimeUnit.DAY)计算出两天的日期差为434天

#-Formatter【格式化工具类】
【说明】提供一些格式化方法,应对前端页面展示或者与项目中其他成员进行数据交互特殊需求的情况
【例子】
      Console.log(
        Formatter.formatJson(
         GsonUtils.get().toJson(
             new MapHelper<String,Object>()
               .put("a", 1).getMap()
             ),
          "\t")
       );
【输出】
  {
    "a" : 1
  }
