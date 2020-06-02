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
 * @version create on 2020年05月29日,last modify at 2020年05月29日
 */
public class FileAppendHelper implements AutoCloseable, Disposable {

	private Path to;
	private Charset charset;
	private BufferedWriter writer;	//该变量不可直接使用，调用getWritter()方法获取

	public FileAppendHelper(File file) {
		this.charset = StandardCharsets.UTF_8;
		this.to = file.toPath();
	}
	
	public FileAppendHelper appendLine(String content) throws IOException {
		return append(content+"\r\n");
	}
	
	public FileAppendHelper append(String content) throws IOException {
		getWritter().write(content);
		return this;
	}
	
	public static BufferedWriter getBufferedWriter(Path path, Charset charset) throws IOException {
    	return Files.newBufferedWriter(path, charset,
    			StandardOpenOption.CREATE, StandardOpenOption.APPEND); // 追加
    }
	
	private BufferedWriter getWritter() throws IOException {
		if(writer == null) {
			synchronized (FileAppendHelper.class) {
				if(writer == null) {
					writer = getBufferedWriter(to, charset);
				}
				
			}
		}
		return writer;
	}

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

	@Override
	public void close() {
		IOUtils.close(writer);
	}
	
}
