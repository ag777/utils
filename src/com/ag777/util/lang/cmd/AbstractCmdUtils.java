package com.ag777.util.lang.cmd;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.regex.Pattern;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.model.Charsets;

/**
 * 执行控制台(cmd/shell)命令的工具类
 * <p>
 * 		执行一个cmd命令会产生三个流（input/output/err），其中一个不处理就有可能产生程序挂起问题，永远不可能得到返回了
 * </p>
 * 
 * @author ag777
 * @version create on 2018年07月04日,last modify at 2018年07月25日
 */
public abstract class AbstractCmdUtils {

	private Charset charsetDefault;
	
	public AbstractCmdUtils() {
		this.charsetDefault = Charsets.UTF_8;//"gb2312";
	}
	
	public AbstractCmdUtils(Charset charset) {
		this.charsetDefault = charset;
	}
	
	public void setCharset(Charset charset) {
		this.charsetDefault = charset;
	}
	
	public Charset getCharset() {
		return charsetDefault;
	}
	
	
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param regex
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public String find(String cmd, String baseDir, String regex, String replacement) throws IOException {
		return find(cmd, baseDir, Pattern.compile(regex), replacement);
	}
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param pattern
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public String find(String cmd, String baseDir, Pattern pattern, String replacement) throws IOException {
		InputStream in = execForStream(cmd, baseDir);
		return IOUtils.find(in, pattern, replacement, charsetDefault);
	}
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param regex
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public Long findLong(String cmd, String baseDir, String regex, String replacement) throws IOException {
		return findLong(cmd, baseDir, Pattern.compile(regex), replacement);
	}
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param pattern
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public Long findLong(String cmd, String baseDir, Pattern pattern, String replacement) throws IOException {
		InputStream in = execForStream(cmd, baseDir);
		return IOUtils.findLong(in, pattern, replacement, charsetDefault);
	}
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param regex
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public List<String> findAll(String cmd, String baseDir, String regex, String replacement) throws IOException {
		return findAll(cmd, baseDir, Pattern.compile(regex), replacement);
	}
	
	/**
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param pattern
	 * @param replacement
	 * @return
	 * @throws IOException
	 */
	public List<String> findAll(String cmd, String baseDir, Pattern pattern, String replacement) throws IOException {
		InputStream in = execForStream(cmd, baseDir);
		return IOUtils.findAll(in, pattern, replacement, charsetDefault);
	}
	
	/**
	 * 执行cmd命令获取返回的所有行
	 * <p>
	 * 要小心
	 * 所有读取内容的方法都有可能导致卡死，原因是某些shell命令读取inputSteam时判断不了何时读取结束
	 * </p>
	 * @param cmd
	 * @param baseDir
	 * @return
	 * @throws IOException
	 */
	public List<String> readLines(String cmd, String baseDir) throws IOException {
		InputStream in = execForStream(cmd, baseDir);
		return IOUtils.readLines(in, charsetDefault);
	}
	
	/**
	 * 执行cmd命令获取返回
	 * 
	 * @param cmd
	 * @param baseDir
	 * @param lineSparator 行分隔符
	 * @return
	 * @throws IOException
	 */
	public String readText(String cmd, String baseDir, String lineSparator) throws IOException {
		InputStream in = execForStream(cmd, baseDir);
		return IOUtils.readText(in, lineSparator, charsetDefault);
	}
	
	/**
	 * 执行命令并返回IO流
	 * @param cmd
	 * @param baseDir
	 * @return
	 * @throws IOException
	 */
	public InputStream execForStream(String cmd, String baseDir) throws IOException {
		Process pro = getProcess(cmd, baseDir);
		return execByProcess(pro);
	}
	
	
	/**
	 * 执行cmd命令(防进程挂起),只关心成功与否,不关心返回
	 * @param cmd
	 * @param baseDir
	 * @return
	 */
	public  boolean exec(String cmd, String baseDir) {
		try {
			return execWithException(cmd, baseDir);
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 执行cmd命令(防进程挂起),只关心成功与否,不关心返回
	 * @param cmd
	 * @param baseDir
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public  boolean execWithException(String cmd, String baseDir) throws IOException, InterruptedException {
		Process shellPro = getProcess(cmd, baseDir);
		pre(shellPro);		//关闭输出流,子线程读取错误流
		readOutput(shellPro);	//子线程读取输入流
		int exitValue = shellPro.waitFor();
		return exitValue == 0;
	}
	
	public abstract Process getProcess(String cmd, String baseDir) throws IOException;
	
	/*===内部方法=======*/
	
	protected InputStream execByProcess(Process pro) {
		pre(pro);
		return pro.getInputStream();
	}
	/**
	 * 读取cmd返回时先关输出流，开子线程读错误流
	 * <p>
	 *	 关于OutputStream,程序用不到,直接在开始的时候就应该close掉.对于errStream应该分别用一个线程来读取出IO流中的内容. 
			注意:必须使用线程,否则依然会阻塞.
		</p>
		
	 * 参考文章:http://xiaohuafyle.iteye.com/blog/1562786
	 * @param pro
	 */
	protected static void pre(Process pro) {
		closeOutput(pro);
		readErr(pro);
	}
	
	/**
	 * 关闭输出流
	 * 
	 * @param pro
	 */
	protected static void closeOutput(Process pro) {
		try {
			pro.getOutputStream().close();
		} catch (IOException e) {
		}  
	}
	
	/**
	 * 子线程读取错误流
	 * 
	 * @param pro 进程
	 */
	protected static void readErr(Process pro) {
		try {
			Thread t = new Thread() {
				public void run() {
					try {
						IOUtils.readLines(pro.getErrorStream(), "gb2312");
					} catch (IOException e) {
					}
				}
			};
			t.start();
		} catch(Exception ex) {
		}
	}
	
	/**
	 * 子线程读取输出流
	 * @param pro
	 */
	protected static void readOutput(Process pro) {
		try {
			Thread t = new Thread() {
				public void run() {
					try {
						IOUtils.readLines(pro.getInputStream(), "gb2312");
					} catch (IOException e) {
					} finally {
					}
				}
			};
			t.start();
		} catch(Exception ex) {
		}
	}
}
