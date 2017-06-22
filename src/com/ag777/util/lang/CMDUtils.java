package com.ag777.util.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

/**
 * @Description cmd命令执行辅助类(针对linux)
 * @author wanggz
 * Time: last modify at 2017/6/16.
 * Mark: 执行一个cmd命令会产生三个流（input/output/err），其中一个不处理就有可能产生程序挂起问题，永远不可能得到返回了
 */
public class CMDUtils {
	
	private static String DEFAULT_ENCODING = "utf-8";//"gb2312";
	
	public static String getReadEncoding() {
		return DEFAULT_ENCODING;
	}
	public static void setReadEncoding(String encoding) {
		DEFAULT_ENCODING = encoding;
	}
	
	private CMDUtils() {}
	
	/**
	 * 压缩文件
	 * 
	 * @param fileName
	 * @param regex
	 * @param basePath
	 * @return
	 */
	public static boolean tar(String fileName, String regex, String basePath) {
		return doCmd("tar zcf " + fileName + ".tar.gz " + regex, basePath);

	}

	/**
	 * 解压文件
	 * 
	 * @param fileName
	 * @param basePath
	 * @return
	 */
	public static boolean tarExtract(String fileName, String basePath) {
		return doCmd("tar zxvf " + fileName, basePath);
	}

	/**
	 * 解压文件
	 * 
	 * @param fileName
	 * @param basePath
	 *            命令执行目录
	 * @param targetPath
	 *            解压的目标路径
	 * @return
	 */
	public static boolean tarExtract(String fileName, String basePath, String targetPath) {
		return doCmd("tar zxvf " + fileName + " -C " + targetPath, basePath);
	}

	/**
	 * 复制文件
	 * 
	 * @param sourcePath
	 * @param targetPath
	 * @return
	 */
	public static boolean copy(String sourcePath, String targetPath) {
		return doCmd("cp -r " + sourcePath + " " + targetPath, targetPath);
	}

	/**
	 * 执行cmd命令(防进程挂起),只关心成功与否,不关心返回
	 * 
	 * @param cmd
	 *            cmd命令
	 * @param filePath
	 *            执行环境(路径)
	 * @return
	 */
	public synchronized static boolean doCmd(String cmd, String filePath) {
		try {
			Process shellPro = Runtime.getRuntime().exec(cmd, null, new File(filePath));
			try {
				InputStream fis = shellPro.getInputStream();
				final BufferedReader brError = new BufferedReader(
						new InputStreamReader(shellPro.getErrorStream(), "gb2312"));
				InputStreamReader isr = new InputStreamReader(fis, "gb2312");
				final BufferedReader br = new BufferedReader(isr);
				Thread t1 = new Thread() {
					public void run() {
						@SuppressWarnings("unused")
						String line = null;
						try {
							while ((line = brError.readLine()) != null) {
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (brError != null)
									brError.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				};
				Thread t2 = new Thread() {
					public void run() {
						@SuppressWarnings("unused")
						String line = null;
						try {
							while ((line = br.readLine()) != null) {
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
							try {
								if (br != null)
									br.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				};
				t1.start();
				t2.start();

				int exitValue = shellPro.waitFor();
				if (0 == exitValue) {
					return true;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
				if (null != shellPro) {
					shellPro = null;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 执行cmd命令获取返回的所有行
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	public static List<String> readLines(String cmd, String filePath) throws Exception {
		InputStream in = doCmdForStream(cmd, filePath);
		return IOUtils.readLines(in, DEFAULT_ENCODING);
	}
	
	/**
	 * 执行cmd获取结果
	 * @param cmd				cmd命令
	 * @param filePath			执行命令的路径
	 * @param lineSparator	每一行的末尾插入的字符，传null则不含换行符
	 * @return
	 * @throws IOException
	 */
	public static String readText(String cmd, String filePath, String lineSparator) throws Exception {
		InputStream in = doCmdForStream(cmd, filePath);
		return IOUtils.readText(in, lineSparator, DEFAULT_ENCODING);
	}
	
	/**
	 * 执行cmd命令，从返回结果中配合正则表达式拉取结果字符串
	 * 正则部分用的方法为RegexUtils.find(String, String)
	 * @param cmd
	 * @param filePath
	 * @param regex
	 * @param replacement 如$1-$2-$3
	 * @return
	 * @throws Exception
	 */
	public static String find(String cmd, String filePath, String regex, String replacement) throws Exception {
		InputStream in = doCmdForStream(cmd, filePath);
		return IOUtils.find(in, regex, replacement, DEFAULT_ENCODING);
	}
	
	/**
	 * 执行cmd命令，从返回结果中配合正则表达式拉取结果字符串,并转为Long
	 * 正则部分用的方法为RegexUtils.findLong(String, String)
	 * @param cmd
	 * @param filePath
	 * @param regex
	 * @param replacement
	 * @return
	 * @throws Exception
	 */
	public static Long findLong(String cmd, String filePath, String regex, String replacement) throws Exception {
		InputStream in = doCmdForStream(cmd, filePath);
		return IOUtils.findLong(in, regex, replacement, DEFAULT_ENCODING);
	}
	
	/**
	 * 执行cmd命令，从返回结果中配合正则表达式拉取所有匹配的结果字符串列表
	 * 正则部分用的方法为RegexUtils.get(String, String)
	 * @param cmd
	 * @param filePath
	 * @param regex
	 * @param replacement 如$1-$2-$3
	 * @return
	 * @throws Exception
	 */
	public static List<String> findAll(String cmd, String filePath, String regex, String replacement) throws Exception {
		InputStream in = doCmdForStream(cmd, filePath);
		return IOUtils.findAll(in, regex, replacement, DEFAULT_ENCODING);
	}
	
	/**
	 * 读取cmd返回时先关输出流，开子线程读错误流
	 * 关于OutputStream,程序用不到,直接在开始的时候就应该close掉.对于errStream应该分别用一个线程来读取出IO流中的内容. 
			注意:必须使用线程,否则依然会阻塞.
	 * 参考文章:http://xiaohuafyle.iteye.com/blog/1562786
	 * @param pro
	 */
	private static void pre(Process pro) {
		closeOutput(pro);
		readErr(pro);
	}

	
	/**
	 * 关闭输出流
	 * @param pro
	 */
	private static void closeOutput(Process pro) {
		try {
			pro.getOutputStream().close();
		} catch (IOException e) {
		}  
	}
	
	/**
	 * 执行cmd命令得到返回流
	 * @param cmd
	 * @param filePath
	 * @return
	 * @throws Exception
	 */
	private static InputStream doCmdForStream(String cmd, String filePath) throws Exception {
		Process pro = null;
		if(filePath == null) {
			pro = Runtime.getRuntime().exec(cmd);
		} else {
			pro = Runtime.getRuntime().exec(cmd, null, new File(filePath));// 执行删除默认路由命令
		}
		pre(pro);
		return pro.getInputStream();
	}
	
	/**
	 * 子线程读取错误流
	 * @param pro
	 */
	private static void readErr(Process pro) {
		try {
			Thread t = new Thread() {
				public void run() {
					try {
						IOUtils.readLines(pro.getErrorStream(), "gb2312");
					} catch (Exception e) {
					}
				}
			};
			t.start();
		} catch(Exception ex) {
		}
	}
}
