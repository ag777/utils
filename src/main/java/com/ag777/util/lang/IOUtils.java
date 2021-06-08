package com.ag777.util.lang;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;

import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.function.ConsumerE;
import com.ag777.util.lang.interf.ProgressListener;

/**
 * IO操作工具类
 * <p>
 * 		有很多操作，比如文件，cmd命令，都是通过操作流来完成目的，为了避免重复及统一代码新建此类
 * </p>
 * <p>讲IO的文章:https://www.zhihu.com/question/382972191</>
 * @author ag777
 * @version create on 2017年06月16日,last modify at 2020年07月20日
 */
public class IOUtils {

	public static int BUFFSIZE = 1024;	//一次性读取的字节
	
	private IOUtils() {}
	
	
	/*--------------读取--------------------*/
	/**
	 * 关闭流或连接
	 * <p>
	 * jdk1.7引入了资源自动关闭的接口AutoCloseable。一些资源也实现了该接口，如preparedStatement、Connection、InputStream、outputStream等等资源接口。在使用的时候只需要把资源在try块中用小括号括起来就可以了。<br>
	 * 看了示例后感觉不够灵活
	 * </p>
	 * @param closeable closeable
	 */
	public static void close(AutoCloseable closeable) {
		try{
			if(closeable != null) {
				if(closeable instanceof Flushable) {
					((Flushable) closeable).flush();
				}
				closeable.close();
			}
		} catch(Exception ignored) {
		}
		
	}
	
	/**
	 * 关闭流或连接(批量)
	 * @param closeables closeables
	 */
	public static void close(AutoCloseable... closeables) {
		for (AutoCloseable closeable : closeables) {
			close(closeable);
		}
		
	}

	
	/**
	 * 从流中读取文本
	 * @param in in
	 * @param lineSparator lineSparator
	 * @param encoding encoding
	 * @return text
	 * @throws IOException IOException
	 */
	public static String readText(InputStream in, String lineSparator, String encoding) throws IOException {
		return readText(in, lineSparator, Charset.forName(encoding));
	}
	
	/**
	 * 从流中读取文本
	 * @param in in
	 * @param lineSparator lineSparator
	 * @param encoding encoding
	 * @return text
	 * @throws IOException IOException
	 */
	public static String readText(InputStream in, String lineSparator, Charset encoding) throws IOException {
		
		try{
			StringBuilder sb = null;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, encoding));
			String s;
			while((s  = procin.readLine()) !=null){
				if(sb == null) {
					sb = new StringBuilder();
				} else if(lineSparator != null) {	//在除了第一行后面的每一行前都加上分隔符
					sb.append(lineSparator);	//换行符
				}
				sb.append(s);
				
			}
			return sb!=null?sb.toString():"";
		} finally {
			close(in);
		}
	}
	
	/**
	 * 读取所有行
	 * @param in in
	 * @param encoding encoding
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<String> readLines(InputStream in, String encoding) throws IOException {
		return readLines(in,Charset.forName(encoding));
	}
	
	/**
	 * 读取所有行
	 * @param in in
	 * @param charset charset
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<String> readLines(InputStream in, Charset charset) throws IOException {
		
		try{
			List<String> lines = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				lines.add(s);
			}
			return lines;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 按行读取
	 * @param in 输入流
	 * @param consumer 消费者
	 * @param charset 编码
	 * @throws IOException IO异常
	 */
	public static void readLines(InputStream in, Consumer<String> consumer, Charset charset) throws IOException {
		try{
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				consumer.accept(s);
			}
		} finally {
			close(in);
		}
	}

	/**
	 * 按行读取
	 * @param in 输入流
	 * @param consumer 消费者
	 * @param charset 编码
	 * @throws Throwable 各种异常
	 */
	public static void readLinesWithException(InputStream in, ConsumerE<String> consumer, Charset charset) throws Throwable {
		try{
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				consumer.accept(s);
			}
		} finally {
			close(in);
		}
	}
	
	/**
	 * 读取字节数组
	 * @param in 输入流
	 * @return 所有字节
	 * @throws IOException 文件找不到(FileNotFoundException)或者读取异常
	 */
	public static byte[] readBytes(InputStream in) throws IOException {
		ByteArrayOutputStream bout = null;
		try{
			bout = new ByteArrayOutputStream(); 
	        byte[] buff = new byte[BUFFSIZE]; 
	        while(true) { 
	            int n = in.read(buff); 
	            if(n == -1) { break; } 
	            bout.write(buff,0,n); 
	        }
	        return bout.toByteArray();
		} finally {
			close(in, bout);
		}
	}
	
	/**
	 * 从流中寻找所有匹配串
	 * @param in in
	 * @param regex regex
	 * @param replacement replacement
	 * @param charset charset
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<String> findAll(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findAll(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找所有匹配串
	 * @param in in
	 * @param pattern pattern
	 * @param replacement replacement
	 * @param charset charset
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<String> findAll(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			List<String> result = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				result.addAll(RegexUtils.findAll(s, pattern, replacement));
			}
			return result;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从流中寻找所有Long型匹配串
	 * @param in in
	 * @param regex regex
	 * @param replacement replacement
	 * @param charset charset
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<Long> findAllLong(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findAllLong(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找所有Long型匹配串
	 * @param in in
	 * @param pattern pattern
	 * @param replacement replacement
	 * @param charset charset
	 * @return list
	 * @throws IOException IOException
	 */
	public static List<Long> findAllLong(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			List<Long> result = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				result.addAll(RegexUtils.findAllLong(s, pattern, replacement));
			}
			return result;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 根据条件查询单个符合条件的值,找到即停
	 * @param in 输入流
	 * @param finder 返回null则说明不需要该值，继续遍历下一行
	 * @param charset 字符编码
	 * @return 匹配结果
	 * @throws IOException IOException
	 */
	public static <T>T find(InputStream in, Function<String, T> finder, Charset charset) throws IOException {
		try{
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				T result = finder.apply(s);
				if(result != null) {
					return result;
				}
			}
		} finally {
			close(in);
		}
		return null;
	}
	
	/**
	 * 根据条件查询所有符合条件的值
	 * @param in 输入流
	 * @param finder 返回null则说明不需要该值，继续遍历下一行
	 * @param charset 字符编码
	 * @return 匹配结果
	 * @throws IOException IOException
	 */
	public static <T>List<T> findAll(InputStream in, Function<String, T> finder, Charset charset) throws IOException {
		try{
			List<T> list = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				T result = finder.apply(s);
				if(result != null) {
					list.add(result);
				}
			}
			return list;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in in
	 * @param regex regex
	 * @param replacement replacement
	 * @param charset charset
	 * @return 匹配结果
	 * @throws IOException IOException
	 */
	public static String find(InputStream in, String regex, String replacement, Charset charset) throws IOException {	
		return find(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in in
	 * @param pattern pattern
	 * @param replacement replacement
	 * @param charset charset
	 * @return 匹配结果
	 * @throws IOException IOException
	 */
	public static String find(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			String result;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				result = RegexUtils.find(s, pattern, replacement);
				if(result != null) {
					return result;
				}
			}
		} finally {
			close(in);
		}
		return null;
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in in
	 * @param regex regex
	 * @param replacement replacement
	 * @param charset charset
	 * @return long
	 * @throws IOException IOException
	 */
	public static Long findLong(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findLong(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in in
	 * @param pattern pattern
	 * @param replacement replacement
	 * @param charset charset
	 * @return long
	 * @throws IOException IOException
	 */
	public static Long findLong(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			Long result;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s;
			while((s  = procin.readLine()) !=null){
				result = RegexUtils.findLong(s, pattern, replacement);
				if(result != null) {
					return result;
				}
			}
		} finally {
			close(in);
		}
		return null;
	}
	
	/*--------------写入--------------------*/
	/**
	 * 将输入流写入输出流
	 * @param in in
	 * @param out out
	 * @param buffSize buffSize
	 * @throws IOException IOException
	 */
	public static void write(InputStream in, OutputStream out, int buffSize) throws IOException {
		try { 
			int byteCount;
			byte[] bytes = new byte[buffSize];

			while ((byteCount = in.read(bytes)) != -1) {
				out.write(bytes, 0, byteCount);
			}
         out.flush();
		} finally {
			close(in,out);
		}
	}
	
	/**
	 * 将输入流写入输出流(带进度监听)
	 * @param in in
	 * @param out out
	 * @param buffSize buffSize
	 * @param listener listener
	 * @throws IOException IOException
	 */
	public static void write(InputStream in, OutputStream out, int buffSize, ProgressListener listener)
			throws IOException {
		if (listener == null) {
			write(in, out, buffSize);
			return;
		}
		try {
			int cur = 0;
			int total = in.available();
			listener.update(cur, total, false);

			int byteCount;
			byte[] bytes = new byte[buffSize];

			while ((byteCount = in.read(bytes)) != -1) {
				out.write(bytes, 0, byteCount);
				cur += byteCount;
				listener.update(cur, total, false);
			}

			out.flush();
			listener.update(cur, total, true);
		} finally {
			close(in, out);
		}
	}
	
	/**
	 * 将内容转化为ByteArrayInputStream写出到输出流
	 * @param content content
	 * @param out out
	 * @param charset charset
	 * @param buffSize buffSize
	 * @throws IOException IOException
	 */
	public static void write(String content, OutputStream out, Charset charset, int buffSize) throws IOException {
		write(
				new ByteArrayInputStream(content.getBytes(charset)), out, buffSize);
	}
	
	/**
	 * 拼接每一行并转化为ByteArrayInputStream写出到输出流
	 * @param lines lines
	 * @param out out
	 * @param charset charset
	 * @param buffSize buffSize
	 * @throws IOException IOException
	 */
	public static void write(List<String> lines, OutputStream out, Charset charset, int buffSize) throws IOException {
		write(ListUtils.toString(lines, SystemUtils.lineSeparator()), out, charset, buffSize);
	}
	
}