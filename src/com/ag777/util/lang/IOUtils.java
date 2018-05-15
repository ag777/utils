package com.ag777.util.lang;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.ag777.util.file.model.ProgressListener;
import com.ag777.util.lang.collection.ListUtils;

/**
 * IO操作工具类
 * <p>
 * 		有很多操作，比如文件，cmd命令，都是通过操作流来完成目的，为了避免重复及统一代码新建此类
 * </p>
 * @author ag777
 * @version create on 2017年06月16日,last modify at 2018年05月15日
 */
public class IOUtils {

	public static int BUFFSIZE = 1024;	//一次性读取的字节
	
	private IOUtils() {}
	
	
	/*--------------读取--------------------*/
	/**
	 * 关闭流
	 * @param closeable
	 */
	public static void close(Closeable closeable) {
		try{
			if(closeable != null) {
				if(closeable instanceof OutputStream) {
					((OutputStream) closeable).flush();
				}
				closeable.close();
			}
		} catch(IOException ex) {
		}
		
	}
	
	/**
	 * 关闭流(批量)
	 * @param closeables
	 */
	public static void close(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			close(closeable);
		}
		
	}
	
	/**
	 * 从流中读取文本
	 * @param in
	 * @param lineSparator
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static String readText(InputStream in, String lineSparator, String encoding) throws IOException {
		return readText(in, lineSparator, Charset.forName(encoding));
	}
	
	/**
	 * 从流中读取文本
	 * @param in
	 * @param lineSparator
	 * @param encoding
	 * @return
	 * @throws IOException 
	 */
	public static String readText(InputStream in, String lineSparator, Charset encoding) throws IOException {
		
		try{
			StringBuilder sb = null;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, encoding));
			String s = null;
			while((s  = procin.readLine()) !=null){
				if(sb == null) {
					sb = new StringBuilder();
				} else if(lineSparator != null) {	//在除了第一行后面的每一行前都加上分隔符
					sb.append(lineSparator);	//换行符
				}
				sb.append(s);
				
			}
			return sb.toString();
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 读取所有行
	 * @param in
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(InputStream in, String encoding) throws IOException {
		return readLines(in,Charset.forName(encoding));
	}
	
	/**
	 * 读取所有行
	 * @param in
	 * @param encoding
	 * @return
	 * @throws IOException 
	 */
	public static List<String> readLines(InputStream in, Charset charset) throws IOException {
		
		try{
			List<String> lines = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s = null;
			while((s  = procin.readLine()) !=null){
				lines.add(s);
			}
			return lines;
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从流中寻找所有匹配串
	 * @param in
	 * @param regex
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static List<String> findAll(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findAll(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找所有匹配串
	 * @param in
	 * @param pattern
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static List<String> findAll(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			List<String> result = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s = null;
			while((s  = procin.readLine()) !=null){
				result.addAll(RegexUtils.findAll(s, pattern, replacement));
			}
			return result;
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从流中寻找所有Long型匹配串
	 * @param in
	 * @param regex
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static List<Long> findAllLong(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findAllLong(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找所有Long型匹配串
	 * @param in
	 * @param pattern
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static List<Long> findAllLong(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			List<Long> result = new ArrayList<>();
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s = null;
			while((s  = procin.readLine()) !=null){
				result.addAll(RegexUtils.findAllLong(s, pattern, replacement));
			}
			return result;
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in
	 * @param regex
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static String find(InputStream in, String regex, String replacement, Charset charset) throws IOException {	
		return find(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in
	 * @param pattern
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static String find(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			String result = null;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s = null;
			while((s  = procin.readLine()) !=null){
				result = RegexUtils.find(s, pattern, replacement);
				if(result != null) {
					return result;
				}
			}
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
		return null;
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in
	 * @param regex
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException 
	 */
	public static Long findLong(InputStream in, String regex, String replacement, Charset charset) throws IOException {
		return findLong(in, Pattern.compile(regex), replacement, charset);
	}
	
	/**
	 * 从流中寻找匹配串,找到一个立即返回
	 * @param in
	 * @param pattern
	 * @param replacement
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static Long findLong(InputStream in, Pattern pattern, String replacement, Charset charset) throws IOException {
		try{
			Long result = null;
			BufferedReader procin = new BufferedReader(new InputStreamReader(in, charset));
			String s = null;
			while((s  = procin.readLine()) !=null){
				result = RegexUtils.findLong(s, pattern, replacement);
				if(result != null) {
					return result;
				}
			}
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in);
		}
		return null;
	}
	
	/*--------------写入--------------------*/
	/**
	 * 将输入流写入输出流
	 * @param in
	 * @param out
	 * @param buffSize
	 * @throws IOException
	 */
	public static void write(InputStream in, OutputStream out, int buffSize) throws IOException {
		try { 
			int byteCount = 0;
			byte[] bytes = new byte[buffSize];

			while ((byteCount = in.read(bytes)) != -1) {
				out.write(bytes, 0, byteCount);
			}
         out.flush();
		} catch(IOException ex) {
			throw ex;
		} finally {
			close(in,out);
		}
	}
	
	/**
	 * 将输入流写入输出流(带进度监听)
	 * @param in
	 * @param out
	 * @param buffSize
	 * @param listener
	 * @throws IOException
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

			int byteCount = 0;
			byte[] bytes = new byte[buffSize];

			while ((byteCount = in.read(bytes)) != -1) {
				out.write(bytes, 0, byteCount);
				cur += byteCount;
				listener.update(cur, total, false);
			}

			out.flush();
			listener.update(cur, total, true);
		} catch (IOException ex) {
			throw ex;
		} finally {
			close(in, out);
		}
	}
	
	/**
	 * 将内容转化为ByteArrayInputStream写出到输出流
	 * @param content
	 * @param out
	 * @param charset
	 * @param buffSize
	 * @throws IOException
	 */
	public static void write(String content, OutputStream out, Charset charset, int buffSize) throws IOException {
		write(
				new ByteArrayInputStream(content.getBytes(charset)), out, buffSize);
	}
	
	/**
	 * 拼接每一行并转化为ByteArrayInputStream写出到输出流
	 * @param lines
	 * @param out
	 * @param charset
	 * @param buffSize
	 * @throws IOException
	 */
	public static void write(List<String> lines, OutputStream out, Charset charset, int buffSize) throws IOException {
		write(ListUtils.toString(lines, SystemUtils.lineSeparator()), out, charset, buffSize);
	}
	
}
