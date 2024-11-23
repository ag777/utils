package com.ag777.util.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.interf.Disposable;

/**
 * 文件追加辅助类
 * 
 * @author ag777
 * @version create on 2020年05月29日,last modify at 2024年11月23日
 */
public class FileAppendHelper implements AutoCloseable, Disposable {

	private Path to;
	private Charset charset;
	private volatile BufferedWriter writer;	//该变量不可直接使用，调用getWritter()方法获取

	/**
	 * 构造函数，初始化FileAppendHelper对象，默认使用UTF-8字符集
	 * @param file 要写入的文件对象
	 */
	public FileAppendHelper(File file) {
	    this.charset = StandardCharsets.UTF_8;
	    this.to = file.toPath();
	}

	/**
	 * 构造函数，初始化FileAppendHelper对象，允许指定字符集
	 * @param file 要写入的文件对象
	 * @param charset 使用的字符集
	 */
	public FileAppendHelper(File file, Charset charset) {
	    this.charset = charset;
	    this.to = file.toPath();
	}

	/**
	 * 追加一行内容到文件中，自动添加换行符
	 * @param content 要追加的内容
	 * @return FileAppendHelper对象，支持链式调用
	 * @throws IOException 如果写入文件时发生I/O错误
	 */
	public FileAppendHelper appendLine(String content) throws IOException {
	    return append(content+"\r\n");
	}

	/**
	 * 追加内容到文件中
	 * @param content 要追加的内容
	 * @return FileAppendHelper对象，支持链式调用
	 * @throws IOException 如果写入文件时发生I/O错误
	 */
	public FileAppendHelper append(String content) throws IOException {
	    getWriter().write(content);
	    return this;
	}

	/**
	 * 获取BufferedWriter对象，用于写入指定路径的文件
	 * @param path 文件路径
	 * @param charset 字符集
	 * @return BufferedWriter对象，用于写入文件
	 * @throws IOException 如果创建BufferedWriter时发生I/O错误
	 */
	public static BufferedWriter getBufferedWriter(Path path, Charset charset) throws IOException {
	    return Files.newBufferedWriter(path, charset,
	            StandardOpenOption.CREATE, StandardOpenOption.APPEND); // 追加
	}

	/**
	 * 获取当前对象的BufferedWriter对象，如果未初始化则进行初始化
	 * @return BufferedWriter对象，用于写入文件
	 * @throws IOException 如果初始化BufferedWriter时发生I/O错误
	 */
	private BufferedWriter getWriter() throws IOException {
	    if(writer == null) {
	        synchronized (FileAppendHelper.class) {
	            if(writer == null) {
	                writer = getBufferedWriter(to, charset);
	            }
	        }
	    }
	    return writer;
	}

	/**
	 * 刷新文件写入器，确保所有数据写入文件
	 * @throws IOException 如果刷新写入器时发生I/O错误
	 */
	public void flush() throws IOException {
	    if (writer != null) {
	        writer.flush();
	    }
	}

	/**
	 * 释放资源，关闭文件写入器，并设置相关对象为null
	 */
	@Override
	public void dispose() {
	    try {
	        close();
	    } catch (Exception e) {
	    }
	    to = null;
	    charset = null;
	    writer = null;
	}

	/**
	 * 关闭文件写入器，释放系统资源
	 * @throws IOException 如果关闭写入器时发生I/O错误
	 */
	@Override
	public void close() {
	    IOUtils.close(writer);
	}
	
}
