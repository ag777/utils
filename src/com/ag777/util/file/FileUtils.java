package com.ag777.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.NoSuchFileException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ag777.util.file.model.FileAnnotation;
import com.ag777.util.file.model.ProgressListener;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.SystemUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.exception.Assert;
import com.ag777.util.lang.filter.StringFilter;
import com.ag777.util.lang.model.Charsets;

/**
 * 文件操作工具类
 * 
 * @author ag777
 * @version create on 2017年04月25日,last modify at 2018年11月14日
 */
public class FileUtils {
    private static Charset FILE_WRITING_CHARSET = Charsets.UTF_8;
    private static Charset FILE_READING_CHARSET = Charsets.UTF_8;
    private static int BUFFSIZE = 1024;	//一次性读取的字节

    public static Charset encodingRead() {
    	return FILE_READING_CHARSET;
    }
    public static void encodingRead(Charset charset) {
    	FILE_READING_CHARSET = charset;
    }
    public static Charset encodingWrite() {
    	return FILE_WRITING_CHARSET;
    }
    public static void encodingWrite(Charset charset) {
    	FILE_WRITING_CHARSET = charset;
    }
    
    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param lineSparator 换行时插入的字符
     * @return
     * @throws IOException
     */
    public static String readText(String filePath, String lineSparator) throws IOException {
       return readText(filePath, lineSparator, FILE_READING_CHARSET);
    }
    
    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param lineSparator 换行时插入的字符
     * @param charset
     * @return
     * @throws IOException
     */
    public static String readText(String filePath, String lineSparator, Charset charset) throws IOException {

        try {
        	if(charset == null) {
        		charset = FILE_READING_CHARSET;
        	}
        	FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.readText(fis, lineSparator, charset);
        } catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        }
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String filePath) throws IOException {
    	return readLines(filePath, FILE_READING_CHARSET);
    }
    
    /**
     * 从io流中读取行,去除bom头,防止影响外部解析文件内容
     * @param in
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(InputStream in,  Charset encoding) throws IOException {
    	List<String> lines = IOUtils.readLines(in, encoding);
    	if(!ListUtils.isEmpty(lines)) {
    		lines.set(0, removeBomHeaderIfExists(lines.get(0)));
    	}
    	return lines;
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String filePath, String encoding) throws IOException {
    	return readLines(filePath, Charset.forName(encoding));
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath
     * @param charset
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String filePath, Charset charset) throws IOException {
    	 try {
    		 if(charset == null) {
    			 charset = FILE_READING_CHARSET;
         	}
    		 FileInputStream fis = new FileInputStream(filePath);
    		 return readLines(fis, charset);
          } catch (FileNotFoundException ex) {
              throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
          } catch (IOException ex) {
              throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
          }
    }
    
    /**
     * 大文件逐行读取
     * @param filePath
     * @param filter 返回值无关，直接返回null就好
     * @param charset
     * @throws IOException
     */
    public static void readLinesByScaner(String filePath, StringFilter filter, Charset charset) throws IOException {
		FileInputStream in = null;
		Scanner sc = null;
		try {
			in = new FileInputStream(filePath);
			sc = new Scanner(in, charset.toString());
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				filter.doFilter(line);
			}
			// note that Scanner suppresses exceptions
		    if (sc.ioException() != null) {
		        throw sc.ioException();
		    }
		} finally {
			IOUtils.close(in, sc);
		}
	}
    
    /**
     * 读取文件中的所有行(排除注释和空行)
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static List<String> readLinesWithoutAnnotation(String filePath, FileAnnotation[] annotations) throws IOException {
    	List<String> resultList = new ArrayList<>(); 
    	List<String> lineList = readLines(filePath);
    	if(annotations != null && annotations.length > 0) {
    		FileAnnotation curAnnotation = null;
    		
    		Map<FileAnnotation, Pattern[]> cpMap = new HashMap<>();
    		for (FileAnnotation annotation : annotations) {
    			Pattern[] ps;
    			if(annotation.isLineOnly()) {
    				ps = new Pattern[2];
    				ps[0] = annotation.pattern();
    				ps[1] = annotation.startPattern();
    			} else {
    				ps = new Pattern[3];
    				ps[0] = annotation.pattern();
    				ps[1] = annotation.startPattern();
    				ps[2] = annotation.endPattern();
    			}
				cpMap.put(annotation, ps);
			}
    		for (String line : lineList) {
    			if(curAnnotation != null) {	//已经被标记了
    				Matcher matcher = cpMap.get(curAnnotation)[2].matcher(line);
    				if(matcher.find()) {	//如果该行存在对应的尾标注,则删除未标注前的部分
    					line = matcher.replaceAll("");
    					curAnnotation = null;
    				} else {	//没有尾标注，则为无效行，直接删除
    					line = null;
    				}
    			} else {	//未被标记
    				Matcher m = null;
    				for (FileAnnotation annotation : annotations) {	//轮询标识,找出最靠前的标注并记录
    					Matcher matcher = cpMap.get(annotation)[1].matcher(line);
    					if(matcher.find()) {
    						int start = matcher.start();
    						if(m == null || start < m.start()) {
    							m = matcher;
    							curAnnotation = annotation;
    						}
    					}
    				}
    				if(curAnnotation != null) {
    					Matcher matcher = cpMap.get(curAnnotation)[0].matcher(line);		//是否被首尾标注都在该行
						if(matcher.find()) {		//如果找到直接替换为空字符串
							line = matcher.replaceAll("");
							curAnnotation = null;
						} else {	//只有首标注，说明尾标注在后面的行
							line = m.replaceAll("");
							if(curAnnotation.isLineOnly()) {	//如果注释为单行注释，则取消标记
								curAnnotation = null;
							}
						}
						
    				}
    			}
    			
    			if(line !=null && !line.trim().isEmpty()) {	//去除空行
					resultList.add(line);
				}
			}
    	 }
    	 return resultList;
    }

    /**
     * 逐行替换文件中的内容
     * @param filePath 文件路径
     * @param stringFilter 参数为当前行内容,结果返回null则删除该行，其余则替换掉源内容
     * @throws IOException
     */
    public static void replaceAllByLines(String filePath, StringFilter stringFilter) throws IOException {
    	replaceAllByLines(filePath, filePath, stringFilter);
    }
    
    /**
     * 逐行替换文件中的内容,另存
     * @param filePath 文件路径
     * @param stringFilter 参数为当前行内容,结果返回null则删除该行，其余则替换掉源内容
     * @throws IOException
     */
    public static void replaceAllByLines(String srcPath, String targetPath, StringFilter stringFilter) throws IOException {
    	List<String> newLines = ListUtils.newArrayList();
    	if(StringUtils.isBlank(srcPath)) {
    		throw new IOException("文件名为空");
    	}
    	if(stringFilter == null) {
    		return;
    	}
    	List<String> lines = readLines(srcPath);
    	for (String line : lines) {
    		String temp = stringFilter.doFilter(line);
    		if(temp != null) {
    			newLines.add(temp);
    		}
		}
    	
    	write(targetPath, newLines, null);
    }
    
    /**
     * 替换文件内容(逐行匹配)
     * @param filePath 文件路径
     * @param pattern 正则
     * @param replacement 替换式
     * @param isReplaceAll 是否替换全部匹配
     * @throws IOException
     */
    public static void replaceByLines(String filePath, Pattern pattern, String replacement, boolean isReplaceAll) throws IOException {
    	if(StringUtils.isBlank(filePath)) {
    		throw new IOException("文件名为空");
    	} else if(pattern == null) {
    		throw new IOException("正则表达式不正确");
    	} 
    	List<String> lines = readLines(filePath);
    	for(int i=0; i<lines.size(); i++) {
    		String line = lines.get(i);
    		Matcher matcher = pattern.matcher(line);
			if(matcher.find()) {
				if(isReplaceAll) {
					lines.set(i, matcher.replaceAll(replacement));
				} else {
					lines.set(i, matcher.replaceFirst(replacement));
					break;
				}
			}
    	}
    	write(filePath, lines, null);
    }
    
    /**
     * 替换文件中所有匹配内容(逐行匹配)
     * @param filePath 文件路径
     * @param regex 正则
     * @param replacement 替换式
     * @throws IOException
     */
    public static void replaceAllByLines(String filePath, String regex, String replacement) throws IOException {
    	Pattern pattern = Pattern.compile(regex);
    	replaceByLines(filePath, pattern, replacement, true);
    }
    
    /**
     * 替换文件中第一处匹配内容(逐行匹配，匹配到则终止循环)
     * @param filePath 文件路径
     * @param regex 正则
     * @param replacement 替换式
     * @throws IOException
     */
    public static void replaceFirstByLines(String filePath, String regex, String replacement) throws IOException {
    	Pattern pattern = Pattern.compile(regex);
    	replaceByLines(filePath, pattern, replacement, false);
    }
    
    /**
     * 替换文件的内容(全文档，包括换行符)
     * @param filePath
     * @param regex
     * @param replacement
     * @param isReplaceAll
     * @throws IOException
     */
    public static void replaceByWhole(String filePath, String regex, String replacement, boolean isReplaceAll) throws IOException {
    	String content = readText(filePath, SystemUtils.lineSeparator());
    	if(isReplaceAll) {
    		content.replaceAll(regex, replacement);
    	} else {
    		content.replaceFirst(regex, replacement);
    	}
    	write(filePath,content, null);
    }
    
    /**
     * 替换文件中所有匹配的内容(全文档，包括换行符)
     * @param filePath
     * @param regex
     * @param replacement
     * @throws IOException
     */
    public static void replaceAllByWhole(String filePath, String regex, String replacement) throws IOException {
    	replaceByWhole(filePath, regex, replacement, true);
    }
    
    /**
     * 替换文件中第一处匹配的内容(全文档，包括换行符)
     * @param filePath
     * @param regex
     * @param replacement
     * @throws IOException
     */
    public static void replaceFirstByWhole(String filePath, String regex, String replacement) throws IOException {
    	replaceByWhole(filePath, regex, replacement, false);
    }
    
    
    /**
     * 从文件内容中定位信息并以一定格式返回
     * @param filePath 文件路径
     * @param regex 匹配用的正则表达式
     * @param replacement 替换式
     * @return
     * @throws IOException
     */
    public static String findText(String filePath,String regex, String replacement) throws IOException {
    	
		try {
            FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.find(fis, regex, replacement, FILE_READING_CHARSET);	//关闭流的操作里面都做了
        } catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        } 
    }
    
    /**
     * 查找文件中所有匹配的字符串
     * @param filePath
     * @param regex
     * @param replacement
     * @return
     * @throws IOException
     */
    public static List<String> findAllText(String filePath,String regex, String replacement) throws IOException {
    	
		try {
            FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.findAll(fis, regex, replacement, FILE_READING_CHARSET);
        } catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        } 
    }
    
    /**
     * 将内容写入文件
     * @param filePath
     * @param content
     * @param charset
     * @return
     * @throws IOException
     */
    public static File write(String filePath, String content, Charset charset) throws IOException {
        if (charset == null) {
        	charset = FILE_WRITING_CHARSET;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(charset));
        return write(is, filePath);
    }
    
    /**
     * 将所有行写入文件
     * @param filePath
     * @param lines
     * @param charset
     * @return
     * @throws IOException
     */
    public static File write(String filePath, List<String> lines, Charset charset) throws IOException {
    	String content = ListUtils.toString(lines, SystemUtils.lineSeparator());
    	return write(filePath, content, charset);
    }

    /**
     * 将内容追加到文件尾部
     * <p>
     * 	使用RandomAccessFile实现
     * </p>
     * @param fileName
     * @param content
     * @return 
     */
    public static boolean appendFileContent(String filePath, String content) {
        FileWriter writer = null;  
        try {
        	new File(filePath).getParentFile().mkdirs();
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(filePath, true);     
            writer.write(content);
            return true;
        } catch (IOException e) {     
            e.printStackTrace();     
        } finally {     
        	IOUtils.close(writer);
        }   
        return false;
    }
    
    /**
     * 将内容追加到文件尾部
     * <p>
     * 	使用RandomAccessFile实现
     * </p>
     * @param fileName
     * @param content
     * @return 
     */
    public static boolean appendFileContent(String filePath, List<String> lines) {
    	return appendFileContent(filePath, ListUtils.toString(lines, "\r\n"));
    }

    
    /**
     * 将流写文件(写)
     * @param is
     * @param filePath
     * @param isOverride
     * @return
     * @throws IOException
     */
    public static File write(InputStream in, String filePath) throws IOException {
    	OutputStream out = null;
        
        try {
            out = getOutputStream(filePath);
            IOUtils.write(in, out, BUFFSIZE);
            return new File(filePath);
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(StringUtils.concat("写入文件[",filePath,"]时发生错误!"), e);
        } finally {
        	IOUtils.close(out);
        }
    }
    
    //--输入/输出流
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedOutputStream getBufferedOutputStream(String filePath) throws FileNotFoundException {
    	return new BufferedOutputStream(getOutputStream(filePath));
    }
    
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedOutputStream getBufferedOutputStream(File file) throws FileNotFoundException {
    	return new BufferedOutputStream(getOutputStream(file));
    }
    
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getOutputStream(String filePath) throws FileNotFoundException {
    	return getOutputStream(new File(filePath));
    }
    
    /**
     * 通过文件路径获取输出流
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileOutputStream getOutputStream(File file) throws FileNotFoundException {
    	makeDir(file.getParent(), true);	//创建父路径
    	return new FileOutputStream(file);
    }

    /**
     * 获取BufferedReader(读)
     * @param filePath
     * @param charset
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedReader getBufferedReader(String filePath, Charset charset) throws FileNotFoundException {
    	if(charset != null) {
    		charset = FILE_READING_CHARSET;
    	}
    	FileInputStream in = FileUtils.getInputStream(filePath);
    	return new BufferedReader(new InputStreamReader(in, charset));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedInputStream getBufferedInputStream(String filePath) throws FileNotFoundException {
    	return new BufferedInputStream(getInputStream(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedInputStream getBufferedInputStream(File file) throws FileNotFoundException {
    	return new BufferedInputStream(getInputStream(file));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static FileInputStream getInputStream(String filePath) throws FileNotFoundException {
    	return getInputStream(new File(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static FileInputStream getInputStream(File file) throws FileNotFoundException {
    	return new FileInputStream(file);
    }
    
    
    //--文件操作
    /**
     * 移动文件或者文件夹,如从e:/aa/到f:/bb/aa/
	 * <p>
	 * 实质是复制文件到对应的位置,成功后删除源文件
	 * </p>
	 * <p>
	 * 为什么不能用renameTo,请参考文章https://blog.csdn.net/findmyself_for_world/article/details/41648095
	 * 简单来说,renameTo不能跨文件系统移动文件,比如C盘是NTFS格式,E盘是FAT32格式，在这两个盘移动文件就会返回false
	 * 相对的，用Files包下的方法就没这种问题,对应本工具包的FileNioUtils中的move方法
	 * </p>
	 * 
     * @param source
     * @param target
     * @return
     * @throws IllegalArgumentException 文件路径为空
     * @throws NoSuchFileException 源文件不存在
     */
	public static boolean move(String source, String target) throws IllegalArgumentException, NoSuchFileException {
		Assert.notBlank(source, "源文件路径不能为空");
		Assert.notBlank(target, "目标文件路径不能为空");
		File srcFile = new File(source);
		Assert.notExisted(srcFile, NoSuchFileException.class, "源文件["+source+"]不存在");
		if(srcFile.isFile()) {
			return moveFile(source, target);
		} else if(srcFile.isDirectory()) {
			return moveFolder(source, target);
		}
		return false;
	}
    
	/**
	 * 移动单个文件带进度监听
	 * <p>
	 * 实质是复制文件到对应的位置,成功后删除源文件
	 * </p>
	 * 
	 * @param source
	 * @param target
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 文件路径为空
	 * @throws NoSuchFileException 源文件不存在，源文件不是个文件(可能是文件夹)
	 */
	public static boolean moveFileWithProgress(String source, String target, ProgressListener listener) throws IllegalArgumentException, NoSuchFileException {
		if(copyFileWithProgress(source, target, listener)) {
			new File(source).delete();
			return true;
		}
		return false;
	}
	
	/**
	 * 复制文件或文件夹
	 * @param source
	 * @param target
	 * @return
	 * @throws IllegalArgumentException 文件路径为空,源文件不存在
	 * @throws NoSuchFileException 
	 */
	public static boolean copy(String source, String target) throws IllegalArgumentException, NoSuchFileException {
		Assert.notBlank(source, "源文件路径不能为空");
		Assert.notBlank(target, "目标文件路径不能为空");
		File srcFile = new File(source);
		Assert.notExisted(srcFile, "源文件["+source+"]不存在");
		if(srcFile.isFile()) {
			return copyFile(source, target);
		} else if(srcFile.isDirectory()) {
			return copyFolder(source, target);
		}
		return false;
	}
	
	/**
	 * 复制单个文件带进度条
	 * <p>
	 * 进度监听参数传null则单做普通的单个文件复制使用，不如直接调用copy方法
	 * </p>
	 * 
	 * @param source
	 * @param target
	 * @param listener
	 * @return
	 * @throws IllegalArgumentException 文件路径为空
	 * @throws NoSuchFileException 源文件不存在，源文件不是个文件(可能是文件夹)
	 */
	public static boolean copyFileWithProgress(String source, String target, ProgressListener listener) throws IllegalArgumentException, NoSuchFileException {
		Assert.notBlank(source, "源文件路径不能为空");
		Assert.notBlank(target, "目标文件路径不能为空");
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File fin = new File(source);
		Assert.notExisted(fin, "源文件["+source+"]不存在");
		if(!fin.isFile()) {
			throw new NoSuchFileException("copyFileWithProgress方法只支持复制单个文件:["+source+"]");
		}
		try {
			File fout = new File(target);
			if (!fout.exists()) {
				File parent = fout.getParentFile(); // 得到父文件夹
				if (!parent.exists()) {
					parent.mkdirs();
				}
				fout.createNewFile();
			}
			bis = new BufferedInputStream(
					new FileInputStream(fin));
			bos = new BufferedOutputStream(
					new FileOutputStream(fout));
			IOUtils.write(bis, bos, BUFFSIZE, listener);
			return true;
		} catch(IOException ex) {
			ex.printStackTrace();
		} 
		return false;
	}
	
	/**
     * 递归删除文件夹及文件夹下的文件
     * @param file	要删除的文件/文件夹
     */
    public static boolean delete(File file) {  
	    if (!file.exists())  
	        return true;  
	    if (file.isFile()) {  
	        if(file.delete()) {
	        	return true;
	        }  
	        Console.err(StringUtils.concat("文件[", file.getPath()+"]删除失败"));
	        return false;  
	    }  
	    File[] files = file.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        if(!delete(files[i])) {
	        	return false;
	        }
	    }  
	    file.delete();  
	    return true;
	}
    public static boolean delete(String path) {
    	return delete(new File(path));
    }

	/**
	 * 替换文件名
	 * <p>
	 * replaceName("a.tar.gz", "b")=>"b.gz"
	 * </p>
	 * 
	 * @param wholeFileName
	 * @param newName
	 * @return
	 */
    public static String replaceName(String wholeFileName, String newName) {
    	return wholeFileName.replaceFirst("^.+?(\\.[^\\.]+)?$", newName+"$1");
    }
    
    /**
     * 替换文件名(保留长拓展名)
     * <p>
     * replaceNameWithLongSuffix("a.tar.gz", "b")=>"b.tar.gz"
     * </p>
     * @param wholeFileName
     * @param newName
     * @return
     */
    public static String replaceNameWithLongSuffix(String wholeFileName, String newName) {
    	return wholeFileName.replaceFirst("^[^\\.]*(.+)?$", newName+"$1");
    }
    
	/**
	 * 获取文件拓展名
	 * <p>
	 * a.tar.gz获取到的是.gz
	 * </p>
	 * 
	 * @param filePath 源文件路径
	 * @return
	 */
	public static String getFileSuffix(String filePath) {
		return RegexUtils.find(filePath, "\\.([^\\.]*)$","$1","");
	}	
	
	/**
	 * 获取文件尾缀(长)
	 * <p>
	 * 比如a.tar.gz获取到的是tar.gz
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFileLongSuffix(String filePath) {
		return RegexUtils.find(filePath, "\\.(.*)$","$1","");
	}
	
	
    /**
     * 移除字符串中的BOM前缀
     *
     * @param content 需要处理的字符串
     * @return 移除BOM后的字符串.
     */
	public static String removeBomHeaderIfExists(String content) {
        if (content == null) {
            return null;
        }
        String line = content;
        if (line.length() > 0) {
            char ch = line.charAt(0);
            // 使用while是因为用一些工具看到过某些文件前几个字节都是0xfffe.
            // 0xfeff,0xfffe是字节序的不同处理.JVM中,一般是0xfeff
            while ((ch == 0xfeff || ch == 0xfffe)) {
                line = line.substring(1);
                if (line.length() == 0) {
                    break;
                }
                ch = line.charAt(0);
            }
        }
        return line;
    }

    /**
     * 从文件的完整路径名（路径+文件名）中提取 路径（包括：Drive+Directroy )
     *
     * @param filePath 文件路径
     * @return
     */
    public static String extractFilePath(String filePath) {
        int nPos = filePath.lastIndexOf('/');
        if (nPos < 0) {
            nPos = filePath.lastIndexOf('\\');
        }

        return (nPos >= 0 ? filePath.substring(0, nPos + 1) : "");
    }

    /**
     * 检查指定文件的路径是否存在
     *
     * @param filePath 文件名称(含路径）
     * @return 若存在，则返回true；否则，返回false
     */
    public static boolean pathExists(String filePath) {
        String sPath = extractFilePath(filePath);
        return fileExists(sPath);
    }

    /**
     * 判断文件是否存在
     * @param filePath
     * @return
     */
    public static boolean fileExists(String filePath) {
    	if(filePath == null || filePath.isEmpty()) {
    		return false;
    	}
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 创建目录
     *
     * @param dirPath             目录名称
     * @param needCreateParentDir 如果父目录不存在，是否创建父目录
     * @return
     */
    public static void makeDir(String dirPath, boolean needCreateParentDir) {
    	if(dirPath == null) {
    		return;
    	}
        File file = new File(dirPath);
        if (needCreateParentDir)
           file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
        else
           file.mkdir(); // 如果父目录不存在，不做处理
    }
   
    /*----------内部工具-----------*/
    /**
	 * 移动单个文件
	 * <p>
	 * 实质是复制文件到对应的位置,成功后删除源文件
	 * </p>
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
     * @throws IllegalArgumentException 
     * @throws NoSuchFileException 
	 * @throws IOException
	 */
	private static boolean moveFile(String source, String target) throws NoSuchFileException, IllegalArgumentException {
		if(moveFileWithProgress(source, target, null)) {
			delete(source);
			return true;
		}
		return false;
	}
	
	/**
	 * 移动文件夹及其子文件夹
	 * <p>
	 * 实质是复制文件到对应的位置,成功后删除源文件
	 * </p>
	 * 
	 * @param source 源文件夹,如: d:/tmp
	 * @param target 目标文件夹,如: e:/tmp
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFileException 
	 * @throws IOException
	 */
	private static boolean moveFolder(String source, String target) throws NoSuchFileException, IllegalArgumentException {
		File f1 = new File(source);
		File f2 = new File(target);
		if (!f1.exists()) {
			Console.err(StringUtils.concat("源文件夹[", source,"]不存在"));
			return false;
		}
		if ((!f2.exists()) && (f1.isDirectory())) {
			f2.mkdirs();
		}
		String[] fileList = f1.list();
		if (!ListUtils.isEmpty(fileList)) {
			for (String file : fileList) {
				String newSource = f1.getAbsolutePath() + File.separator + file;
				String newTarget = f2.getAbsolutePath() + File.separator + file;
				if (new File(newSource).isDirectory()) {
					if(!moveFolder(newSource, newTarget)) {
						return false;
					}
				} else {
					if(!moveFile(newSource, newTarget)) {
						Console.err(StringUtils.concat("移动文件[", newSource,"]失败"));
						return false;
					}
				}
			}
		}
		f1.delete();	//删除源文件夹
		return true;
	}
	/**
	 * 复制单个文件
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFileException 
	 * @throws IOException
	 */
	private static boolean copyFile(String source, String target) throws NoSuchFileException, IllegalArgumentException {
		return copyFileWithProgress(source, target, null);
	}
	
	/**
	 * 复制文件夹及其子文件夹
	 * 
	 * @param source 源文件夹,如: d:/tmp
	 * @param target 目标文件夹,如: e:/tmp
	 * @throws IllegalArgumentException 
	 * @throws NoSuchFileException 
	 * @throws IOException
	 */
	private static boolean copyFolder(String source, String target) throws NoSuchFileException, IllegalArgumentException {
		
		File f1 = new File(source);
		File f2 = new File(target);
		if (!f1.exists()) {
			Console.err(StringUtils.concat("源文件夹[", source,"]不存在"));
			return false;
		}
		if ((!f2.exists()) && (f1.isDirectory())) {
			f2.mkdirs();
		}
		String[] fileList = f1.list();
		if (ListUtils.isEmpty(fileList)) {
			return true;
		}
		for (String file : fileList) {
			String newSource = f1.getAbsolutePath() + File.separator + file;
			String newTarget = f2.getAbsolutePath() + File.separator + file;
			if (new File(newSource).isDirectory()) {
				if(!copyFolder(newSource, newTarget)) {
					return false;
				}
			} else {
				if(!copyFile(newSource, newTarget)) {
					Console.err(StringUtils.concat("移动文件[", newSource,"]失败"));
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 获取文件md5值
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 */
	public static String md5(String filePath) throws IOException, NoSuchAlgorithmException {
        InputStream fis = null;
        try {
        	fis = getInputStream(filePath);
        	MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] buffer = new byte[1024];
	        int length = -1;
	        while ((length = fis.read(buffer, 0, 1024)) != -1) {
	            md.update(buffer, 0, length);
	        }
	        BigInteger bigInt = new BigInteger(1, md.digest());
	        return bigInt.toString(16);
        }catch (IOException|NoSuchAlgorithmException ex) {
        	throw ex;
        } finally {
        	IOUtils.close(fis);
        }
	}
	
}
