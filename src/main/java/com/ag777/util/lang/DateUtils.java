package com.ag777.util.lang;

import com.ag777.util.lang.exception.Assert;
import org.joda.time.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;
import java.util.*;

/**
 * 有关 <code>List</code> 列表工具类。
 * <p>
 * 	需要jar包:
 * <ul>
 * <li>joda-time-xxx.jar</li>
 * </ul>
 * 
 * <p>
 * 	巧妙的考勤统计:http://www.01happy.com/mysql-bit_count-bit_or/<br>
 * <pre>{@code
 * 	SELECT year,month,BIT_COUNT(BIT_OR(1<<day)) AS days FROM t1
       GROUP BY year,month;
    }</pre>
    <ul>
    <li>更新日志:https://www.joda.org/joda-time/changes-report.html</li>
    </ul>
 * 
 * @author ag777
 * @version create on 2016年07月07日,last modify at 2023年05月04日
 */
public class DateUtils {

	public static final String DEFAULT_TEMPLATE_DATE = "yyyy-MM-dd";		//日期标准格式
	public static final String DEFAULT_TEMPLATE_TIME = "yyyy-MM-dd HH:mm:ss";	//时间标准格式
	public static final String DEFAULT_TEMPLATE_MONTH = "yyyy-MM";	//月份标准格式

	public static final DateTimeFormatter FORMATTER_DATE = DateTimeFormat.forPattern(DEFAULT_TEMPLATE_DATE);
	public static final DateTimeFormatter FORMATTER_TIME = DateTimeFormat.forPattern(DEFAULT_TEMPLATE_TIME);
	public static final DateTimeFormatter FORMATTER_MONTH = DateTimeFormat.forPattern(DEFAULT_TEMPLATE_MONTH);

	public enum TimeUnit {	//时间单位枚举
		SECOND,MINUTE,HOUR,DAY,WEEK,MONTH,YEAR
	}
	
	private DateUtils(){}
	
	/*==============转换==============*/
	
	/**
	 * 字符串转DateTime
	 * @param date date
	 * @param template template
	 * @return DateTime
	 */
	public static DateTime toDateTime(String date, String template){
		if(StringUtils.isBlank(date)) {
			return null;
		}
		try {
			DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
			return toDateTime(date, formatter);
		} catch(Exception ex) {
//			ex.printStackTrace();
		}
		return null;
	}
	public static DateTime toDateTime(String date, DateTimeFormatter formatter){
		try {
			return DateTime.parse(date, formatter);
		} catch(Exception ex) {
//			ex.printStackTrace();
		}
		return null;
	}
	//重载
	public static DateTime toDateTime(String date){
		return toDateTime(date, FORMATTER_TIME);
	}
	
	/**
	 * LocalDate转DateTime
	 * @param ld ld
	 * @return DateTime
	 */
	public static DateTime toDateTime(LocalDate ld){
		if(ld == null) {
			return null;
		}
		return ld.toDateTimeAtStartOfDay();
	}
	
	/**
	 * 字符串转LocalDate
	 * @param date date
	 * @param template template
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(String date, String template){
		if(StringUtils.isBlank(date)) {
			return null;
		}
		try {
			DateTimeFormatter format = DateTimeFormat.forPattern(template);
			return toLocalDate(date, format);
		} catch(Exception ex) {
//			ex.printStackTrace();
		}
		return null;
	}
	public static LocalDate toLocalDate(String date, DateTimeFormatter formatter){
		return LocalDate.parse(date, formatter);
	}
	//重载
	public static LocalDate toLocalDate(String date){
		return toLocalDate(date, FORMATTER_DATE);
	}
	
	/**
	 * DateTime转LocalDate
	 * @param dt dt
	 * @return LocalDate
	 */
	public static LocalDate toLocalDate(DateTime dt){
		if(dt == null) {
			return null;
		}
		return dt.toLocalDate();
	}
	
	/**
	 * 获取某个时间的long型值
	 * @param year year
	 * @param month month
	 * @param day day
	 * @return 时间戳
	 */
	public static long toLong(int year,int month,int day) {
		return new LocalDate(year,month,day).toDate().getTime();
	}

	/**
	 * 转换long型时间为字符串
	 * @param time time
	 * @param formatter 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(long time,DateTimeFormatter formatter) {
		return new DateTime(time).toString(formatter);
	}

	/**
	 * 转换long型时间为字符串
	 * @param time time
	 * @param template template
	 * @return 时间(字符串)
	 */
	public static String toString(long time,String template) {
		return new DateTime(time).toString(template);
	}

	/**
	 * 转换long型时间为字符串
	 * @param cld 时间
	 * @param formatter 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(Calendar cld,DateTimeFormatter formatter) {
		if(cld == null) {
			return null;
		}
		return new DateTime(cld).toString(formatter);
	}

	/**
	 * 转换Calendar型时间为字符串
	 * @param cld cld
	 * @param template template
	 * @return 时间(字符串)
	 */
	public static String toString(Calendar cld,String template) {
		if(cld == null) {
			return null;
		}
		return new DateTime(cld).toString(template);
	}

	/**
	 * 转换Date型时间为字符串
	 * @param date 时间
	 * @param formatter 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(java.util.Date date,DateTimeFormatter formatter) {
		if (date == null) {
			return null;
		}
		return new DateTime(date).toString(formatter);
	}

	/**
	 * 转换Date型时间为字符串
	 * @param date 时间
	 * @param template 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(java.util.Date date,String template) {
		if (date == null) {
			return null;
		}
		return new DateTime(date).toString(template);
	}

	/**
	 * 转换Timestamp型时间为字符串
	 * @param ts 时间
	 * @param formatter 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(Timestamp ts, DateTimeFormatter formatter) {
		if (ts == null) {
			return null;
		}
		return new DateTime(ts).toString(formatter);
	}

	/**
	 * 转换Timestamp型时间为字符串
	 * @param ts 时间
	 * @param template 时间格式配置
	 * @return 时间(字符串)
	 */
	public static String toString(Timestamp ts, String template) {
		if (ts == null) {
			return null;
		}
		return new DateTime(ts).toString(template);
	}

	/**
	 * 转化时间格式
	 * @param date 时间
	 * @param formatterSrc 源时间格式
	 * @param formatterTarget 目标时间格式
	 * @return 时间(字符串)
	 */
	public static String toString(String date, DateTimeFormatter formatterSrc, DateTimeFormatter formatterTarget) {
		DateTime dt = toDateTime(date, formatterSrc);
		if(dt == null) {
			return null;
		}
		return dt.toString(formatterTarget);
	}

	/**
	 * 转化时间格式
	 * @param date 时间
	 * @param templateSrc 源时间格式
	 * @param templateTarget 目标时间格式
	 * @return 时间(字符串)
	 */
	public static String toString(String date, String templateSrc, String templateTarget) {
		DateTime dt = toDateTime(date, templateSrc);
		if(dt == null) {
			return null;
		}
		return dt.toString(templateTarget);
	}

	public static String toString(DateTime dt, DateTimeFormatter formatter) {
		if(dt == null) {
			return null;
		}
		return dt.toString(formatter);
	}

	/**
	 * 等同于dt.toString(FORMATTER_TIME);
	 * @param dt dt
	 * @return 时间(字符串)
	 */
	public static String toString(DateTime dt) {
		return toString(dt, FORMATTER_TIME);
	}

	public static String toString(LocalDate ld, DateTimeFormatter formatter) {
		if(ld == null) {
			return null;
		}
		return ld.toString(formatter);
	}

	/**
	 * 等同于ld.toString(FORMATTER_DATE);
	 * @param ld ld
	 * @return 日期(字符串)
	 */
	public static String toString(LocalDate ld) {
		return toString(ld, FORMATTER_DATE);
	}

	public static Calendar toCalendar(String date, DateTimeFormatter formatter) {
		if(StringUtils.isBlank(date)) {
			return null;
		}
		DateTime dt = toDateTime(date, formatter);
		if(dt!=null){
			return dt.toCalendar(null);
		}
		return null;
	}

	/**
	 * 转换字符串型时间为Calendar
	 * @param date date
	 * @param template template
	 * @return  失败返回null
	 */
	public static Calendar toCalendar(String date,String template) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return toCalendar(date, formatter);
	}
	/**
	 * 转换为Date
	 * @param date 日期
	 * @param template 格式
	 * @return Date
	 */
	public static java.util.Date toDate(String date,String template) {
		if(StringUtils.isBlank(date)) {
			return null;
		}
		DateTime dt = toDateTime(date, template);
		if(dt!=null){
			return dt.toDate();
		}
		return null;
	}
	/**
	 * 转换为TimeStamp
	 * @param date 日期
	 * @param template 格式
	 * @return Timestamp
	 */
	public static Timestamp toTimeStamp(String date, String template) {
		if(StringUtils.isBlank(date)) {
			return null;
		}
		DateTime dt = toDateTime(date, template);
		if(dt!=null){
			return new Timestamp(dt.getMillis());
		}
		return null;
	}
	
	/**
	 * 转换日期的显示格式
	 * @param date	日期
	 * @param templateSrc	源格式
	 * @param templateDest	目标格式
	 * @return 时间(字符串)
	 */
	public String format(String date, String templateSrc, String templateDest) {
		return toDateTime(date, templateSrc).toString(templateDest);
	}
	
	/*==============遍历==============*/
	
	/**
	 * 获取一年内的所有月份列表
	 * @param year year
	 * @param formatter 日期格式化配置
	 * @return 月份列表
	 */
	public static List<String> getMonthListOfYear(int year, DateTimeFormatter formatter){
		LocalDate now = new LocalDate();
		LocalDate ld = now.year().setCopy(year).monthOfYear().setCopy(1);

		List<String> list = new ArrayList<>();
		while(ld.getYear() == year){
			list.add(ld.toString(formatter));
			ld = ld.plusMonths(1);
		}

		return list;
	}
	//重载
	public static List<String> getMonthListOfYear(int year){
		return getMonthListOfYear(year, FORMATTER_MONTH);
	}

	/**
	 * 遍历起止时间间固定间隔的时间(通用)
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param template template
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param viewer 可以为null
	 */
	public static void ergodiceDateList(String startDate,String endDate,String template,TimeUnit unit,Viewer<DateTime> viewer) {
		DateTime start = toDateTime(startDate, template);
		DateTime end = toDateTime(endDate, template);
		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		
		while(!start.isAfter(end)){
			if(viewer != null) {
				viewer.doView(new DateTime(start));	//查看副本以防影响遍历结果
			}
			
			start = plusToCopy(start, unit, 1);
		}
		
	}
	
	/**
	 * 获取起止时间间固定间隔的时间列表(通用)
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param templateSrc templateSrc
	 * @param templateDest templateDest
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param filter 返回true则写入列表,为null则都写入列表
	 * @return 时间列表
	 */
	public static List<String> getList(String startDate,String endDate,String templateSrc,String templateDest,TimeUnit unit, Filter<DateTime> filter) {
		List<String> list = new ArrayList<>();
		DateTime start = toDateTime(startDate, templateSrc);
		DateTime end = toDateTime(endDate, templateSrc);
		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		while(!start.isAfter(end)){
				
			if(filter == null || filter.doFilter(new DateTime(start))) {		//过滤 || 建立副本以防影响遍历结果
				String item = start.toString(templateDest);
				list.add(item);
			}
			
			start = plusToCopy(start, unit, 1);
		}
		
		return list;
	}
	
	/**
	 * 获取起止时间间固定间隔的时间列表(通用)
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param formatterSrc 源日期格式配置
	 * @param formatterDest 目标日期格式配置
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param editor 返回null则表示不写入列表
	 * @return 时间列表
	 */
	public static List<String> getList(String startDate,String endDate,DateTimeFormatter formatterSrc,DateTimeFormatter formatterDest,TimeUnit unit, Editor<DateTime> editor) {
		List<String> list = new ArrayList<>();
		DateTime start = toDateTime(startDate, formatterSrc);
		DateTime end = toDateTime(endDate, formatterDest);
		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		while(!start.isAfter(end)){
			DateTime temp = new DateTime(start);	//建立副本以防影响遍历结果
			DateTime result = editor.doEdit(temp);
			if(result != null) {
				String item = result.toString(formatterDest);
				list.add(item);
			}
			start = plusToCopy(start, unit, 1);
		}
		
		return list;
	}
	//重载
	public static List<String> getList(String startDate,String endDate,String templateSrc,String templateDest,TimeUnit unit, Editor<DateTime> editor) {
		DateTimeFormatter formatterSrc = DateTimeFormat.forPattern(templateSrc);
		DateTimeFormatter formatterDest = DateTimeFormat.forPattern(templateDest);
		return getList(startDate, endDate, formatterSrc, formatterDest, unit, editor);
	}
	//重载
	public static List<String> getList(String startDate,String endDate,String templateSrc,String templateDest,TimeUnit unit) {
		return getList(startDate, endDate, templateSrc, templateDest, unit, (Filter<DateTime>)null);
	}
	//重载
	public static List<String> getList(String startDate,String endDate,DateTimeFormatter formatterSrc,DateTimeFormatter formatterDest,TimeUnit unit) {
		return getList(startDate, endDate, formatterSrc, formatterDest, unit, null);
	}
	
	/**
	 * 获取起止时间间固定间隔的时间列表(通用)
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param templateSrc templateSrc
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param editor 返回null则表示不写入列表
	 * @return 列表项为editor1的返回值,null不加入列表
	 */
	public static <T>List<T> getList(String startDate,String endDate,String templateSrc,TimeUnit unit, Editor1<DateTime,T> editor) {
		List<T> list = new ArrayList<>();
		DateTime start = toDateTime(startDate, templateSrc);
		DateTime end = toDateTime(endDate, templateSrc);
		
		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		
		while(!start.isAfter(end)){
			DateTime temp = new DateTime(start);	//建立副本以防影响遍历结果
			T item = editor.doEdit(temp);
			if(item != null) {
				list.add(item);
			}
			start = plusToCopy(start, unit, 1);
		}
		return list;
	}
	

	/**
	 * 获取两个时间间隔(天)内的所有日期
	 * @param startDate		开始日期
	 * @param endDate		结束日期
	 * @param templateSrc	源日期格式("yyyy-MM-dd")
	 * @param templateDest	目标日期格式("yyyy-MM-dd")
	 * @return 日期列表
	 */
	public static List<String> getDateList(String startDate,String endDate,String templateSrc, String templateDest){
		return getList(startDate, endDate, templateSrc, templateDest, TimeUnit.DAY);
	}
	//重载
	public static List<String> getDateList(String startDate,String endDate, DateTimeFormatter formatterSrc, DateTimeFormatter formatterDest){
		return getList(startDate, endDate, formatterSrc, formatterDest, TimeUnit.DAY);
	}
	//重载
	public static List<String> getDateList(String startDate,String endDate,String template){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getDateList(startDate, endDate, formatter, FORMATTER_DATE);
	}
	//重载
	public static List<String> getDateList(String startDate,String endDate, DateTimeFormatter formatter){
		return getDateList(startDate, endDate, formatter, FORMATTER_DATE);
	}
	//重载
	public static List<String> getDateList(String startDate,String endDate){
		return getDateList(startDate, endDate, FORMATTER_DATE);
	}
	
	/**
	 * 获取两个时间间隔(天)内的所有日期(排除周末)
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param formatterSrc 源日期格式配置
	 * @param formatterDest 目标日期格式配置
	 * @return 日期列表
	 */
	public static List<String> getDateListWithoutWeekend(String startDate, String endDate, DateTimeFormatter formatterSrc, DateTimeFormatter formatterDest){
		List<String> list = new ArrayList<>();

		LocalDate start = toLocalDate(startDate, formatterSrc);
		LocalDate end = toLocalDate(endDate, formatterDest);

		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		
		while(!start.isAfter(end)){
			
			if(!isWeekend(start)) {	//非周末
				list.add(start.toString(formatterDest));
			}
			start = start.plusDays(1);
		}

		return list;
	}
	//重载
	public static List<String> getDateListWithoutWeekend(String startDate, String endDate, String templateSrc, String templateDest){
		DateTimeFormatter formatterSrc = DateTimeFormat.forPattern(templateSrc);
		DateTimeFormatter formatterDest = DateTimeFormat.forPattern(templateDest);
		return getDateListWithoutWeekend(startDate, endDate, formatterSrc, formatterDest);
	}
	//重载
	public static List<String> getDateListWithoutWeekend(String startDate, String endDate, String template){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getDateListWithoutWeekend(startDate, endDate, formatter);
	}
	//重载
	public static List<String> getDateListWithoutWeekend(String startDate, String endDate, DateTimeFormatter formatter){
		return getDateListWithoutWeekend(startDate, endDate, formatter, FORMATTER_DATE);
	}
	//重载
	public static List<String> getDateListWithoutWeekend(String startDate, String endDate){
		return getDateListWithoutWeekend(startDate, endDate, FORMATTER_DATE);
	}

	/**
	 * 获取两个时间间隔(天)内的周末日期
	 * @param startDate startDate
	 * @param endDate endDate
	 * @param formatterSrc 源日期格式配置
	 * @param formatterDest 目标日期格式配置
	 * @return 日期列表
	 */
	public static List<String> getWeekendDateList(String startDate, String endDate, DateTimeFormatter formatterSrc, DateTimeFormatter formatterDest){
		List<String> list = new ArrayList<>();

		LocalDate start = toLocalDate(startDate, formatterSrc);
		LocalDate end = toLocalDate(endDate, formatterDest);
		
		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		
		while(!start.isAfter(end)){
			if(isWeekend(start)){	//周末
				list.add(start.toString(formatterDest));
			}
			start = start.plusDays(1);
		}
		return list;
	}
	// 重载
	public static List<String> getWeekendDateList(String startDate, String endDate, String templateSrc, String templateDest){
		DateTimeFormatter formatterSrc = DateTimeFormat.forPattern(templateSrc);
		DateTimeFormatter formatterDest = DateTimeFormat.forPattern(templateDest);
		return getWeekendDateList(startDate, endDate, formatterSrc, formatterDest);
	}
	// 重载
	public static List<String> getWeekendDateList(String startDate, String endDate, String template){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getWeekendDateList(startDate, endDate, formatter);
	}
	// 重载
	public static List<String> getWeekendDateList(String startDate, String endDate, DateTimeFormatter formatter){
		return getWeekendDateList(startDate, endDate, formatter, FORMATTER_DATE);
	}
	// 重载
	public static List<String> getWeekendDateList(String startDate, String endDate){
		return getWeekendDateList(startDate, endDate, FORMATTER_DATE);
	}
	
	
	/**
	 * 获取两个时间间隔内的所有月份（包含首尾时间）
	 * @param startDate 开始日期
	 * @param endDate	结束日期
	 * @param formatterSrc 源日期格式配置
	 * @param formatterDest 目标日期格式配置
	 * @return 月份列表
	 */
	public static List<String> getMonthList(String startDate,String endDate, DateTimeFormatter formatterSrc, DateTimeFormatter formatterDest){
		List<String> list = new ArrayList<>();

		LocalDate start = toLocalDate(startDate, formatterSrc).dayOfMonth().withMinimumValue();
		LocalDate end = toLocalDate(endDate, formatterSrc).dayOfMonth().withMinimumValue();

		Assert.notNull(start, "开始时间参数转换失败:"+startDate);
		Assert.notNull(end, "结束时间参数转换失败:"+endDate);
		while(!start.isAfter(end)){
			list.add(start.toString(formatterDest));
			start = start.plusMonths(1);
		}
		return list;
	}
	// 重载
	public static List<String> getMonthList(String startDate,String endDate,String templateSrc, String templateDest){
		DateTimeFormatter formatterSrc = DateTimeFormat.forPattern(templateSrc);
		DateTimeFormatter formatterDest = DateTimeFormat.forPattern(templateDest);
		return getMonthList(startDate,endDate,formatterSrc,formatterDest);
	}
	// 重载
	public static List<String> getMonthList(String startDate,String endDate,String template){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getMonthList(startDate, endDate, formatter);
	}
	public static List<String> getMonthList(String startDate,String endDate,DateTimeFormatter formatter){
		return getMonthList(startDate, endDate, formatter, FORMATTER_MONTH);
	}
	// 重载,默认传进来的时间格式为DEFAULT_TEMPLATE(天)
	public static List<String> getMonthList(String startDate,String endDate){
		return getMonthList(startDate, endDate, FORMATTER_MONTH);
	}
	
	
	/*==============获取==============*/
	
	/**
	 * 获取当前时间
	 * @param formatter 时间格式配置
	 * @return 时间
	 */
	public static String getNow(DateTimeFormatter formatter){
		return new DateTime().toString(formatter);
	}
	/**
	 * 获取当前时间
	 * @param template 如"yyyy-MM-dd HH:mm:ss"
	 * @return 时间
	 */
	public static String getNow(String template){
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return new DateTime().toString(formatter);
	}

	/**
	 * 获取今天日期
	 * @return 日期
	 */
	public static String getToday(){
		return getNow(FORMATTER_DATE);
	}
	
	
	/**
	 * 获取某天的开始时间00:00:00
	 * @param date date
	 * @param formatter 日期格式配置
	 * @return 时间
	 */
	public static String getBeginOfDay(String date,DateTimeFormatter formatter) {
		return getMinimumToCopy(toDateTime(date, formatter), TimeUnit.DAY)
				.toString(FORMATTER_TIME);
	}
	public static String getBeginOfDay(String date,String template) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getBeginOfDay(date, formatter);
	}
	//重载
	public static String getBeginOfDay(String date) {
		return getBeginOfDay(date, FORMATTER_DATE);
	}
	
	/**
	 * 获取某天的结束时间23:59:59
	 * @param date date
	 * @param formatter 日期格式配置
	 * @return 时间
	 */
	public static String getEndOfDay(String date,DateTimeFormatter formatter) {
		return getMaximumToCopy(toDateTime(date, formatter), TimeUnit.DAY)
				.toString(FORMATTER_TIME);
	}
	public static String getEndOfDay(String date,String template) {
		DateTimeFormatter formatter = DateTimeFormat.forPattern(template);
		return getEndOfDay(date, formatter);
	}
	//重载
	public static String getEndOfDay(String date) {
		return getEndOfDay(date, FORMATTER_DATE);
	}
	
	/**
	 * @param ld ld
	 * @return 日期是否为周末
	 */
	public static boolean isWeekend(LocalDate ld) {
		return ld.getDayOfWeek() == 6||ld.getDayOfWeek() == 7;
	}
	//重载
	public static boolean isWeekend(String date, DateTimeFormatter formatter) {
		LocalDate ld = toLocalDate(date,formatter);
		return isWeekend(ld);
	}
	//重载
	public static boolean isWeekend(String date, String template) {
		LocalDate ld = toLocalDate(date,template);
		return isWeekend(ld);
	}
	//重载
	public static boolean isWeekend(String date) {
		return isWeekend(date, FORMATTER_DATE);
	}
	
	/**
	 * 时间增长
	 * @param time time
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param step 步长,可以为负数
	 * @return DateTime
	 */
	public static DateTime plusToCopy(DateTime time, TimeUnit unit, int step) {	//没做空指针判断
		if(unit == TimeUnit.SECOND) {
			return time.plusSeconds(step);
		}else if(unit == TimeUnit.MINUTE) {
			return time.plusMinutes(step);
		}else if(unit == TimeUnit.HOUR) {
			return time.plusHours(step);
		}else if(unit == TimeUnit.DAY) {
			return time.plusDays(step);
		}else if(unit == TimeUnit.WEEK) {
			return time.plusWeeks(step);
		}else if(unit == TimeUnit.MONTH) {
			return time.plusMonths(step);
		}else if(unit == TimeUnit.YEAR) {
			return time.plusYears(step);
		}
		return new DateTime(time);
	}
	
	/**
	 * 日期增长,单位写了不支持的(如:秒),则复制源数据返回
	 * @param time time
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示每次增加一秒
	 * @param step 步长,可以为负数
	 * @return LocalDate
	 */
	public static LocalDate plusToCopy(LocalDate time, TimeUnit unit, int step) {
		Assert.notNull(time, "参数time不能为空");
		
		if(unit == TimeUnit.DAY) {
			return time.plusDays(step);
		}else if(unit == TimeUnit.WEEK) {
			return time.plusWeeks(step);
		}else if(unit == TimeUnit.MONTH) {
			return time.plusMonths(step);
		}else if(unit == TimeUnit.YEAR) {
			return time.plusYears(step);
		}
		return new LocalDate(time);
	}
	
	/**====================================日期比较===================================**/
	/**
	 * yyyy-MM-dd格式的日期是否小于目标日期
	 * @param target 日期
	 * @param compare 目标日期
	 * @return 日期是否小于目标日期
	 */
	public static boolean isDateBefore(String target, String compare) {
		LocalDate ldTarget = toLocalDate(target);
		LocalDate ldCompare = toLocalDate(compare);
		
		Assert.notNull(ldTarget, "原始时间参数转换失败:"+target);
		Assert.notNull(ldTarget, "对比时间参数转换失败:"+compare);
		
		return ldTarget.isBefore(ldCompare);
	}
	
	/**
	 * yyyy-MM-dd格式的日期是否大于目标日期
	 * @param target 日期
	 * @param compare 目标日期
	 * @return 日期是否大于目标日期
	 */
	public static boolean isDateAfter(String target, String compare) {
		LocalDate ldTarget = toLocalDate(target);
		LocalDate ldCompare = toLocalDate(compare);

		Assert.notNull(ldTarget, "原始时间参数转换失败:"+target);
		Assert.notNull(ldTarget, "对比时间参数转换失败:"+compare);
		
		return ldTarget.isAfter(ldCompare);
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @param template template
	 * @return target 时间是否在 compare 时间之前
	 */
	public static boolean isBefore(String target, String compare, String template) {
		DateTime dt1 = toDateTime(target, template);
		DateTime dt2 = toDateTime(compare, template);
		
		Assert.notNull(dt1, "原始时间参数转换失败:"+target);
		Assert.notNull(dt2, "对比时间参数转换失败:"+compare);
		
		return isBefore(dt1, dt2);
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @param template template
	 * @return target 时间是否不在 compare 时间之前(大等于)
	 */
	public static boolean isNotBefore(String target, String compare, String template) {
		DateTime dt1 = toDateTime(target, template);
		DateTime dt2 = toDateTime(compare, template);

		Assert.notNull(dt1, "原始时间参数转换失败:"+target);
		Assert.notNull(dt2, "对比时间参数转换失败:"+compare);
		
		return isNotBefore(dt1, dt2);
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @param template template
	 * @return target 时间是否不在 compare 时间之后(小等于)
	 */
	public static boolean isNotAfter(String target, String compare, String template) {
		DateTime dt1 = toDateTime(target, template);
		DateTime dt2 = toDateTime(compare, template);
		
		Assert.notNull(dt1, "原始时间参数转换失败:"+target);
		Assert.notNull(dt2, "对比时间参数转换失败:"+compare);
		
		return isNotAfter(dt1, dt2);
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @param template template
	 * @return target 时间是否在 compare 时间之后
	 */
	public static boolean isAfter(String target, String compare, String template) {
		DateTime dt1 = toDateTime(target, template);
		DateTime dt2 = toDateTime(compare, template);

		Assert.notNull(dt1, "原始时间参数转换失败:"+target);
		Assert.notNull(dt2, "对比时间参数转换失败:"+compare);
		
		return isAfter(dt1, dt2);
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @return target 时间是否在 compare 时间之前
	 */
	public static boolean isBefore(DateTime target,DateTime compare) {
		Assert.notNull(target, "原始时间参数不能为空");
		Assert.notNull(compare, "对比时间参数不能为空");
		
		if(target.compareTo(compare) < 0) {	//1 大于 0 等于 -1 小于
			return true;
		}
		return false;
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @return target 时间是否在 compare 时间之后
	 */
	public static boolean isAfter(DateTime target,DateTime compare) {
		Assert.notNull(target, "原始时间参数不能为空");
		Assert.notNull(compare, "对比时间参数不能为空");
		
		if (target.compareTo(compare) > 0) {	// 1 大于 0 等于 -1 小于
			return true;
		}
		return false;
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @return target 时间是否不在 compare 时间之前(大等于)
	 */
	public static boolean isNotBefore(DateTime target,DateTime compare) {
		Assert.notNull(target, "原始时间参数不能为空");
		Assert.notNull(compare, "对比时间参数不能为空");
		
		if (target.compareTo(compare) < 0) {	// 1 大于 0 等于 -1 小于
			return false;
		}
		return true;
	}
	
	/**
	 * @param target target
	 * @param compare compare
	 * @return target 时间是否不在 compare 时间之后(小等于)
	 */
	public static boolean isNotAfter(DateTime target,DateTime compare) {
		Assert.notNull(target, "原始时间参数不能为空");
		Assert.notNull(compare, "对比时间参数不能为空");
		
		if (target.compareTo(compare) > 0) {	//1 大于 0 等于 -1 小于
			return false;
		}
		return true;
	}
	
	/**
	 * 获取两个时间差(通用)
	 * 注意00:15和01:00之间的分钟差 相当于00:00和01:00之间的分钟差,所以结果是1分钟,相差的时间为1分钟
	 * @param start start
	 * @param end end
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示计算另个时间差多少秒
	 * @return 时间差
	 */
	public static int between(DateTime start, DateTime end, TimeUnit unit) {
		Assert.notNull(start, "开始时间参数不能为空");
		Assert.notNull(end, "结束时间参数不能为空");
		
		if(unit == TimeUnit.SECOND) {
			return Seconds.secondsBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getSeconds();
		}else if(unit == TimeUnit.MINUTE) {
			return Minutes.minutesBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getMinutes();
		}else if(unit == TimeUnit.HOUR) {
			return Hours.hoursBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getHours();
		}else if(unit == TimeUnit.DAY) {
			return Days.daysBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getDays();
		}else if(unit == TimeUnit.WEEK) {
			return Weeks.weeksBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getWeeks();
		}else if(unit == TimeUnit.MONTH) {
			return Months.monthsBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getMonths();
		}else if(unit == TimeUnit.YEAR) {
			return Years.yearsBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getYears();
		}
		return 0;
	}
	/**
	 * 获取两个时间差(通用)
	 * 注意00:15和01:00之间的分钟差 相当于00:00和01:00之间的分钟差,所以结果是1分钟,相差的时间为1分钟
	 * @param start	开始时间
	 * @param end	结束时间
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示计算另个时间差多少秒
	 * @return 时间差
	 */
	public static int between(LocalDate start, LocalDate end, TimeUnit unit) {
		Assert.notNull(start, "开始时间参数不能为空");
		Assert.notNull(end, "结束时间参数不能为空");
		
		if(unit == TimeUnit.DAY) {
			return Days.daysBetween(start,
					end).getDays();	//已经是最小化时间了没必要再做处理了
		}else if(unit == TimeUnit.WEEK) {
			return Weeks.weeksBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getWeeks();
		}else if(unit == TimeUnit.MONTH) {
			return Months.monthsBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getMonths();
		}else if(unit == TimeUnit.YEAR) {
			return Years.yearsBetween(getMinimumToCopy(start, unit),
					getMinimumToCopy(end, unit)).getYears();
		}
		return 0;
	}
	
	/**
	 * 获取两个时间差(通用)
	 * 注意00:15和01:00之间的分钟差 相当于00:00和01:00之间的分钟差,所以结果是1分钟,相差的时间为1分钟
	 * @param startTime	开始时间
	 * @param startTime		结束时间
	 * @param template		时间格式("yyyy-MM-dd")
	 * @param unit 时间单位/遍历的步长,如TimeUnit.SECONDS表示计算另个时间差多少秒
	 * @return 时间差
	 */
	public static int between(String startTime,String endTime,String template,TimeUnit unit) {
		
		if(unit == TimeUnit.DAY || unit == TimeUnit.WEEK || unit == TimeUnit.MONTH || unit == TimeUnit.YEAR) {
			LocalDate start = toLocalDate(startTime, template);
			LocalDate end = toLocalDate(endTime, template);
			
			Assert.notNull(startTime, "开始时间参数转换失败:"+startTime);
			Assert.notNull(endTime, "结束时间参数转换失败:"+endTime);
			
			return between(start, end, unit);
		}else {
			DateTime start = toDateTime(startTime, template);	
			DateTime end = toDateTime(endTime, template);
			
			Assert.notNull(startTime, "开始时间参数转换失败:"+startTime);
			Assert.notNull(endTime, "结束时间参数转换失败:"+endTime);
			
			return between(start, end, unit);
		}
		
		
	}
	
	
	/**
	 * 清空某个时间单位底下的时间(最小值),递归清空
	 * @param dt	原始时间
	 * @param unit 	时间单位，这个单位下的时间将会被设置为最小值(0)
	 * @return DateTime
	 */
	public static DateTime getMinimumToCopy(DateTime dt, TimeUnit unit) {
		Assert.notNull(dt, "参数dt不能为空");
		Assert.notNull(unit, "参数unit不能为空");
		
		if(unit == TimeUnit.SECOND) {
			return dt.millisOfSecond().withMinimumValue();	//最终根递归节点
		}else if(unit == TimeUnit.MINUTE) {
			return getMinimumToCopy(dt.secondOfMinute().withMinimumValue(), TimeUnit.SECOND);
		}else if(unit == TimeUnit.HOUR) {
			return getMinimumToCopy(dt.minuteOfHour().withMinimumValue(), TimeUnit.MINUTE);
		}else if(unit == TimeUnit.DAY) {
			return dt.millisOfDay().withMinimumValue();		//最终根递归节点
		}else if(unit == TimeUnit.WEEK) {
			return getMinimumToCopy(dt.dayOfWeek().withMinimumValue(), TimeUnit.DAY);
		}else if(unit == TimeUnit.MONTH) {
			return getMinimumToCopy(dt.dayOfMonth().withMinimumValue(), TimeUnit.DAY);
		}else if(unit == TimeUnit.YEAR) {
			return getMinimumToCopy(dt.dayOfYear().withMinimumValue(), TimeUnit.DAY);
		}
		return new DateTime(dt);
	}
	
	/**
	 * 清空某个时间单位底下的时间(最小值),递归清空
	 * @param ld	原始时间
	 * @param unit	时间单位，这个单位下的时间将会被设置为最小值(0)
	 * @return LocalDate
	 */
	public static LocalDate getMinimumToCopy(LocalDate ld, TimeUnit unit) {
		Assert.notNull(ld, "参数ld不能为空");
		Assert.notNull(unit, "参数unit不能为空");
		
		if(unit == TimeUnit.WEEK) {
			return ld.dayOfWeek().withMinimumValue();
		}else if(unit == TimeUnit.MONTH) {
			return ld.dayOfMonth().withMinimumValue();
		}else if(unit == TimeUnit.YEAR) {
			return ld.dayOfYear().withMinimumValue();
		}
		return new LocalDate(ld);
	}
	
	/**
	 * 最大化某个时间单位底下的时间(最小值),递归赋值,比如23点底下的最大时间为23:59:59.999
	 * @param dt	原始时间
	 * @param unit	时间单位,这个时间下的单位将会被设置为最大值
	 * @return DateTime
	 */
	public static DateTime getMaximumToCopy(DateTime dt, TimeUnit unit) {
		Assert.notNull(dt, "参数dt不能为空");
		Assert.notNull(unit, "参数unit不能为空");
		
		if(unit == TimeUnit.SECOND) {
			return dt.millisOfSecond().withMaximumValue();	//最终根递归节点
		}else if(unit == TimeUnit.MINUTE) {
			return getMinimumToCopy(dt.secondOfMinute().withMaximumValue(), TimeUnit.SECOND);
		}else if(unit == TimeUnit.HOUR) {
			return getMinimumToCopy(dt.minuteOfHour().withMaximumValue(), TimeUnit.MINUTE);
		}else if(unit == TimeUnit.DAY) {
			return dt.millisOfDay().withMaximumValue();		//最终根递归节点
		}else if(unit == TimeUnit.WEEK) {
			return getMinimumToCopy(dt.dayOfWeek().withMaximumValue(), TimeUnit.DAY);
		}else if(unit == TimeUnit.MONTH) {
			return getMinimumToCopy(dt.dayOfMonth().withMaximumValue(), TimeUnit.DAY);
		}else if(unit == TimeUnit.YEAR) {
			return getMinimumToCopy(dt.dayOfYear().withMaximumValue(), TimeUnit.DAY);
		}
		return new DateTime(dt);
	}
	
	/**
	 * 最大化某个时间单位底下的时间(最小值),递归赋值,比如1月份最大时间为1-31
	 * @param ld	原始时间
	 * @param unit	时间单位,这个时间下的单位将会被设置为最大值
	 * @return LocalDate
	 */
	public static LocalDate getMaximumToCopy(LocalDate ld, TimeUnit unit) {
		Assert.notNull(ld, "参数ld不能为空");
		Assert.notNull(unit, "参数unit不能为空");
		
		if(unit == TimeUnit.WEEK) {
			return ld.dayOfWeek().withMaximumValue();
		}else if(unit == TimeUnit.MONTH) {
			return ld.dayOfMonth().withMaximumValue();
		}else if(unit == TimeUnit.YEAR) {
			return ld.dayOfYear().withMaximumValue();
		}
		return new LocalDate(ld);
	}
	
	/*----日期统计----*/
	/**
	 * 转换日期集合为统计字符串（可以给考勤统计模块用）
	 * 例子:传{"2017-06-20","2017-06-22"},"2017-06-20", "2017-06-23",返回1010
	 * 实现方式:二进制位与
	 * @param dateList		日期列表
	 * @param startDateStr	开始日期
	 * @param endDateStr	结束日期
	 * @return 二进制字符串
	 */
	public static String dateStatistics(List<String> dateList, String startDateStr,String endDateStr) {
		LocalDate startDate = toLocalDate(startDateStr);
		LocalDate endDate = toLocalDate(endDateStr);
		
		Assert.notNull(startDate, "开始时间参数转换失败:"+startDateStr);
		Assert.notNull(endDateStr, "结束时间参数转换失败:"+endDateStr);
		
		long interval = between(startDate, endDate, TimeUnit.DAY);	//首尾日期差
		long num = 1L <<interval;
		
		for (String date : dateList) {
			if(date != null) {
				int dayBetween = between(startDate, toLocalDate(date), TimeUnit.DAY);
				if(dayBetween>-1) {
					num = num | (1L <<dayBetween);
				}
			}
		}
		String statisticsStr = Long.toBinaryString(num);	//十进制转二进制
		StringBuilder sb = new StringBuilder(statisticsStr);
		if(!dateList.contains(endDateStr)) {	//不包含最后一天
			sb.replace(0, 1, "0");	//替换第一个字符为0(也就是清空endDate对应的日期数据)
		}
		return sb.reverse().toString();
	}

	/**
	 * 转换日期集合为统计字符串（可以给考勤统计模块用）
	 * @param dateList 		日期列表
	 * @param holidayList	假期列表
	 * @param startDateStr	开始时间
	 * @param endDateStr	结束时间
	 * @return 二进制字符串
	 */
	public static String dateStatistics(List<String> dateList, List<String> holidayList, String startDateStr,String endDateStr) {
		LocalDate startDate = toLocalDate(startDateStr);
		LocalDate endDate = toLocalDate(endDateStr);
		
		Assert.notNull(startDate, "开始时间参数转换失败:"+startDateStr);
		Assert.notNull(endDateStr, "结束时间参数转换失败:"+endDateStr);
		
		long interval = between(startDate, endDate, TimeUnit.DAY);	//首尾日期差
		long num = 1L <<(interval*3);
		
		for (String date : dateList) {
			if(date != null) {
				int dayBetween = between(startDate, toLocalDate(date), TimeUnit.DAY)*3;
				if(dayBetween>-1) {
					num = num | (1L <<dayBetween);
				}
			}
		}
		for (String holiday : holidayList) {
			if(holiday != null) {
				int dayBetween = between(startDate, toLocalDate(holiday), TimeUnit.DAY)*3;
				if(dayBetween>-1) {
					num = num | (2L <<dayBetween);
				}
			}
		}
		String statisticsStr = Long.toOctalString(num);	//十进制转八进制
		StringBuilder sb = new StringBuilder(statisticsStr);
		if(!dateList.contains(endDateStr)) {	//不包含最后一天
			sb.replace(0, 1, "0");	//替换第一个字符为0(也就是清空enddate对应的日期数据)
		}
		return sb.reverse().toString();
	}
	
	/**====================================配合控件===================================**/
	
	public static class DateRange {
		private static final String SEPARATOR = " 至 ";
		/**
		 * 配合前端daterange控件,拆分dateRange字符串，填入map
		 * @param dateRange 如'2016-07-07 至 2016-07-08'
		 * @param params params
		 */
		public static void fillDateRange(String dateRange,String separator, String template, Map<String, Object> params){
			String[] dateGroup = getDateRange(dateRange, separator, template);
			params.put("startDate", dateGroup[0]);
			params.put("endDate", dateGroup[1]);
		}
		
		//重载
		public static void fillDateRange(String dateRange, Map<String, Object> params){
			String[] dateGroup = getDateRange(dateRange);
			params.put("startDate", dateGroup[0]);
			params.put("endDate", dateGroup[1]);
		}
		
		/**
		 * 获取时间区间数组
		 * 返回[开始时间,结束时间]
		 */
		public static String[] getDateRange(String dateRange,String separator, String template){
			String[] dateGroup = dateRange.split(separator);
			String startDate = dateGroup[0];
			String endDate = dateGroup[1];
			if(!"yyyy-MM-dd".equals(template)) {
				startDate = toLocalDate(startDate,template).toString(FORMATTER_DATE);
				endDate = toLocalDate(endDate, template).toString(FORMATTER_DATE);
			}
			dateGroup[0] = startDate;
			dateGroup[1] = endDate;
			return dateGroup;
		}
		//重载
		/**
		 * 获取时间区间数组
		 */
		public static String[] getDateRange(String dateRange){
			return dateRange.split(SEPARATOR);
		}
		/**
		 * 通过区间获取时间列表
		 */
		public static List<String> getDateList(String dateRange,String separator, String template) {
			String[] group = getDateRange(dateRange,separator,template);
			String startDate = group[0];
			String endDate = group[1];
			return DateUtils.getDateList(startDate, endDate);
		}
		//重载
		public static List<String> getDateList(String dateRange) {
			String[] group = getDateRange(dateRange);
			String startDate = group[0];
			String endDate = group[1];
			return DateUtils.getDateList(startDate, endDate);
		}
		
		/**
		 * 将map中yyyy-MM-dd格式的起止时间转换为yyyy-MM-dd HH:mm:ss的起止时间
		 * @param params params
		 */
		public static void convertStartDateAndEndDate(Map<String, Object> params) {
			String reg = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
			if(params.containsKey("startDate") && ((String)params.get("startDate")).matches(reg)) {	//格式为yyyy-MM-dd
				params.put("startDate", 
						DateUtils.toDateTime((String) params.get("startDate"), "yyyy-MM-dd")
						.secondOfDay().withMinimumValue().toString("yyyy-MM-dd HH:mm:ss"));
			}
			if(params.containsKey("endDate") && ((String)params.get("endDate")).matches(reg)) {	//格式为yyyy-MM-dd
				params.put("endDate", 
						DateUtils.toDateTime((String) params.get("endDate"), "yyyy-MM-dd")
						.secondOfDay().withMaximumValue().toString("yyyy-MM-dd HH:mm:ss"));
			}
		}
	}
	
	public static class Date {
		/**
		 * 
		 * @param date yyyy-MM-dd
		 * @return {"startDate": xxx, "endDate": xxx}
		 */
		public static Map<String, Object> getStartTimeAndEndTime(String date) {
			HashMap<String, Object> params = new HashMap<>();
			if(date == null) {
				return params;
			}
			params.put("startDate", getStartDate(date));
			params.put("endDate", getEndDate(date));
			return params;
		}
		
		/**
		 * 根据map中date的key填充startDate和endDate
		 * @param date date
		 * @param params params
		 * @return {"startDate": xxx, "endDate": xxx}
		 */
		public static Map<String, Object> fillStartTimeAndEndTime(String date,Map<String, Object> params) {
			if(date == null) {
				return params;
			}
			params.put("startDate", getStartDate(date));
			params.put("endDate", getEndDate(date));
			return params;
		}
		
		/**
		 * 获取开始时间
		 * @param date yyyy-MM-dd
		 */
		private static String getStartDate(String date) {
			return date+" 00:00:00";
		}
		
		/**
		 * 获取结束时间
		 * @param date yyyy-MM-dd
		 */
		private static String getEndDate(String date) {
			return date+" 23:59:59";
		}
	}
	
	/**====================================辅助类===================================**/
	
	public interface Viewer<T> {
		void doView(T item);
	}
	
	public interface Filter<T> {
		boolean doFilter(T item);		//匹配则返回true
	}
	
	public interface Editor<T> extends Editor1<T, T> {
		@Override
		T doEdit(T item);
	}
	
	public interface Editor1<T, V> {
		V doEdit(T item);		
	}
	
	public static int betweenMonth(LocalDate startDate,LocalDate endDate) {
		return Months.monthsBetween(startDate, endDate).getMonths();
	}
	
	public static void main(String[] args) {
//		List<String> list = getList("2017-04-01", "2017-04-14", "yyyy-MM-dd", "yyyy-MM-dd", TimeUnit.DAY,(Filter<DateTime>)null);
//		for (String date : list) {
//			System.out.println(date);
//		}
//		System.out.println(toLocalDate("2017-02-03").toString(DEFAULT_TEMPLATE_TIME));
//		System.out.println(toDateTime("23:01","mm:ss").toString(DEFAULT_TEMPLATE_TIME));
//		LocalDateTime ldt = new LocalDateTime();
//		
//		System.out.println(ldt.toString(DEFAULT_TEMPLATE_TIME));
//		System.out.println(DateUtils.getMinimumToCopy(
//				DateUtils.toDateTime("23:02","mm:ss"),
//				TimeUnit.MINUTE).toString("mm:ss"));
//		Console.log(
//				betweenMonth(toLocalDate("2017-01-03"), toLocalDate("2017-04-04")));
		System.out.println(between(toLocalDate("2019-03-01"), toLocalDate("2019-12-25"), TimeUnit.DAY)+1);
	}
}