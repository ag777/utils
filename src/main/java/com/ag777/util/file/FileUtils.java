package com.ag777.util.file;

import com.ag777.util.file.model.FileAnnotation;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.*;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.model.Charsets;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 文件操作工具类
 * 
 * @author ag777
 * @version create on 2020年08月04日,last modify at 2024年12月25日
 */
public class FileUtils {
	public static final Pattern P_EXTENSION_LONG = Pattern.compile("(?<=\\.)[\\w\\d]{1,5}(.[\\w\\d]{1,5})*$");
	public static final Pattern P_EXTENSION_SHORT = Pattern.compile("(?<=\\.)[\\w\\d]{1,5}$");

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
	 * @param fileName 文件名
	 * @return 拓展名(长，如tar.gz)
	 */
	public static String getLongExtension(String fileName) {
		return getExtension(P_EXTENSION_LONG, fileName);
	}

	/**
	 * @param fileName 文件名
	 * @return 拓展名(短，如gz)
	 */
	public static String getShortExtension(String fileName) {
		return getExtension(P_EXTENSION_SHORT, fileName);
	}

	/**
	 * 获取文件拓展名
	 * @param pattern 拓展名提取正则(不包含.)
	 * @param fileName 文件名
	 * @return 文件拓展名
	 */
	private static String getExtension(Pattern pattern, String fileName) {
		fileName = fileName.toLowerCase();
		Matcher m = pattern.matcher(fileName);
		if (m.find()) {
			return m.group();
		}
		return "";
	}

	/**
	 * {@code
	 * a.tar.gz, zip => a.zip
	 * }
	 * @param fileName 文件名
	 * @param replacement 替换字符串
	 * @return 替换后的文件名
	 */
	public static String replaceLongExtension(String fileName, String replacement) {
		return replaceExtension(P_EXTENSION_LONG, fileName, replacement);
	}

	/**
	 * {@code
	 * a.tar.gz, zip => a.tar.zip
	 * }
	 * @param fileName 文件名
	 * @param replacement 替换字符串
	 * @return 替换后的文件名
	 */
	public static String replaceShortExtension(String fileName, String replacement) {
		return replaceExtension(P_EXTENSION_SHORT, fileName, replacement);
	}

	/**
	 * 替换文件拓展名
	 * @param pattern 拓展名提取正则(不包含.)
	 * @param fileName 文件名
	 * @param replacement 替换字符串
	 * @return 替换后的文件名
	 */
	private static String replaceExtension(Pattern pattern, String fileName, String replacement) {
		fileName = fileName.toLowerCase();
		Matcher m = pattern.matcher(fileName);
		if (!StringUtils.isEmpty(replacement)) { // 需要替换成新的后缀名
			if (replacement.startsWith(".")) {
				replacement = replacement.substring(1);
			}
			if (m.find()) {
				return fileName.substring(0, m.start())+replacement+fileName.substring(m.end());
			}
			// 文档没有后缀名时，直接加后缀名
			return fileName + "." + replacement;
		} else {    // 需要删除后缀名
			if (m.find()) {
				return fileName.substring(0, m.start()-1);
			}
			// 文档本来就没有后缀名，并且也不需要加后缀名
			return fileName;
		}

	}

	/**
	 *
	 * @param path 文件
	 * @return 获取文件属性
	 * @throws IOException 读取文件异常
	 */
	public static BasicFileAttributes getAttr(Path path) throws IOException {
		BasicFileAttributeView basicView = Files.getFileAttributeView(path, BasicFileAttributeView.class, LinkOption.NOFOLLOW_LINKS);
		return basicView.readAttributes();
	}

    /**
     * 遍历所有子文件(夹)，寻找符合要求的文件(夹)
     * @param basePath 基础路径
     * @param filter 过滤器, [文件，相对路径]返回true则加入列表
     * @return 文件列表
     */
    public static List<File> findSubFile(String basePath, BiPredicate<Path, Path> filter) {
		List<File> fileList = ListUtils.newArrayList();
		try {
			scanSubFiles(
					Paths.get(basePath),
					(path, relativizePath)->{
						if (filter.test(path, relativizePath)) {
							fileList.add(path.toFile());
						}
					}

			);
		} catch (IOException e) {
			// 忽略异常
		}
		return fileList;
	}

	/**
	 * 读取文件为字节数组
	 * @param filePath filePath
	 * @return byte[]
	 * @throws IOException IOException
	 */
	public static byte[] readBytes(String filePath) throws IOException {
		return readBytes(new File(filePath));
	}

	/**
	 * 取文件为字节数组
	 * @param file 文件
	 * @return byte[]
	 * @throws IOException IOException
	 */
	public static byte[] readBytes(File file) throws IOException {
		try {
			FileInputStream fis = new FileInputStream(file);
			return IOUtils.readBytes(fis);
		} catch (FileNotFoundException ex) {
			throw new FileNotFoundException(StringUtils.concat("文件[", file.getAbsolutePath(), "]不存在"));
		} catch (IOException ex) {
			throw new IOException(StringUtils.concat("读取文件[",file.getAbsolutePath(),"]时发生错误!"), ex);
		}
	}

	/**
	 * @param file 文件
	 * @param charset charset
	 * @return 文件内容
	 * @throws IOException io异常
	 */
	public static String readText(File file, Charset charset) throws IOException {
		if(charset == null) {
			charset = FILE_READING_CHARSET;
		}
		byte[] bytes = readBytes(file);
		return new String(bytes, charset);
	}

    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @return 所有行
     * @throws IOException IOException
     */
    public static String readText(String filePath) throws IOException {
       return readText(filePath, FILE_READING_CHARSET);
    }
    
    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param charset charset
     * @return 所有行
     * @throws IOException IOException
     */
    public static String readText(String filePath, Charset charset) throws IOException {
        try {
        	if(charset == null) {
        		charset = FILE_READING_CHARSET;
        	}
        	FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.readText(fis, charset);
        } catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        }
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath 文件路径
     * @return 所有行
     * @throws IOException IOException
     */
    public static List<String> readLines(String filePath) throws IOException {
    	return readLines(filePath, FILE_READING_CHARSET);
    }
    
    /**
     * 从io流中读取行,去除bom头,防止影响外部解析文件内容
     * @param in in
     * @param encoding encoding
     * @return 所有行
     * @throws IOException IOException
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
     * @param filePath filePath
     * @param encoding encoding
     * @return 所有行
     * @throws IOException IOException
     */
    public static List<String> readLines(String filePath, String encoding) throws IOException {
    	return readLines(filePath, Charset.forName(encoding));
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath filePath
     * @param charset charset
     * @return 所有行
     * @throws IOException IOException
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
     * @param filePath filePath
     * @param filter filter
     * @param charset charset
     * @throws IOException IOException
     */
    public static void readLinesByScaner(String filePath, Consumer<String> filter, Charset charset) throws IOException {
		FileInputStream in = null;
		Scanner sc = null;
		try {
			in = new FileInputStream(filePath);
			sc = new Scanner(in, charset.toString());
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				filter.accept(line);
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
     * @return 所有行(排除注释和空行)
     * @throws IOException IOException
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
     * @param filter 参数为当前行内容,结果返回null则删除该行，其余则替换掉源内容
     * @throws IOException IOException
     */
    public static void replaceAllByLines(String filePath, Function<String, String> filter) throws IOException {
    	replaceAllByLines(filePath, filePath, filter);
    }
    
    /**
     * 逐行替换文件中的内容,另存
     * @param srcPath 源路径
     * @param targetPath 目标路径
     * @param filter 参数为当前行内容,结果返回null则删除该行，其余则替换掉源内容
     * @throws IOException IOException
     */
    public static void replaceAllByLines(String srcPath, String targetPath, Function<String, String> filter) throws IOException {
    	List<String> newLines = ListUtils.newArrayList();
    	if(StringUtils.isBlank(srcPath)) {
    		throw new IOException("文件名为空");
    	}
    	if(filter == null) {
    		return;
    	}
    	List<String> lines = readLines(srcPath);
    	for (String line : lines) {
    		String temp = filter.apply(line);
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
     * @throws IOException IOException
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
     * @throws IOException IOException
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
     * @throws IOException IOException
     */
    public static void replaceFirstByLines(String filePath, String regex, String replacement) throws IOException {
    	Pattern pattern = Pattern.compile(regex);
    	replaceByLines(filePath, pattern, replacement, false);
    }
    
    /**
     * 替换文件的内容(全文档，包括换行符)
     * @param filePath filePath
     * @param regex regex
     * @param replacement replacement
     * @param isReplaceAll isReplaceAll
     * @throws IOException IOException
     */
    public static void replaceByWhole(String filePath, String regex, String replacement, boolean isReplaceAll) throws IOException {
    	String content = readText(filePath);
    	if(isReplaceAll) {
			content = content.replaceAll(regex, replacement);
    	} else {
			content = content.replaceFirst(regex, replacement);
    	}
    	write(filePath,content, null);
    }
    
    /**
     * 替换文件中所有匹配的内容(全文档，包括换行符)
     * @param filePath filePath
     * @param regex regex
     * @param replacement replacement
     * @throws IOException IOException
     */
    public static void replaceAllByWhole(String filePath, String regex, String replacement) throws IOException {
    	replaceByWhole(filePath, regex, replacement, true);
    }
    
    /**
     * 替换文件中第一处匹配的内容(全文档，包括换行符)
     * @param filePath filePath
     * @param regex regex
     * @param replacement replacement
     * @throws IOException IOException
     */
    public static void replaceFirstByWhole(String filePath, String regex, String replacement) throws IOException {
    	replaceByWhole(filePath, regex, replacement, false);
    }
    
    /**
     * 根据条件查询单个符合条件的值,找到即停
     * @param filePath 文件路径
     * @param finder 返回null则说明不需要该值，继续遍历下一行
     * @return 查询结果
     * @throws IOException IOException
     */
    public static <T>T find(String filePath, Function<String, T> finder) throws IOException {
    	return find(filePath, finder, FILE_READING_CHARSET);
    }
    
    /**
     * 根据条件查询单个符合条件的值,找到即停
     * @param filePath 文件路径
     * @param finder 返回null则说明不需要该值，继续遍历下一行
     * @param charset 字符编码
     * @return 查询结果
     * @throws IOException IOException
     */
    public static <T>T find(String filePath, Function<String, T> finder, Charset charset) throws IOException {
    	try {
	    	FileInputStream fis = new FileInputStream(filePath);
	    	return IOUtils.find(fis, finder, charset);
    	} catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        } 
    }
    
    /**
     * 根据条件查询所有符合条件的值
     * @param filePath 文件路径
     * @param finder 返回null则说明不需要该值，继续遍历下一行
     * @return 查询结果
     * @throws IOException IOException
     */
    public static <T>List<T> findAll(String filePath, Function<String, T> finder) throws IOException {
    	return findAll(filePath, finder, FILE_READING_CHARSET);
    }
    
    /**
     * 根据条件查询所有符合条件的值
     * @param filePath 文件路径
     * @param finder 返回null则说明不需要该值，继续遍历下一行
     * @param charset 字符编码
     * @return 查询结果
     * @throws IOException IOException
     */
    public static <T>List<T> findAll(String filePath, Function<String, T> finder, Charset charset) throws IOException {
    	try {
	    	FileInputStream fis = new FileInputStream(filePath);
	    	return IOUtils.findAll(fis, finder, charset);
    	} catch (FileNotFoundException ex) {
            throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
        } catch (IOException ex) {
            throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
        } 
    }
    
    /**
     * 从文件内容中定位信息并以一定格式返回
     * @param filePath 文件路径
     * @param regex 匹配用的正则表达式
     * @param replacement 替换式
     * @return 查询结果
     * @throws IOException IOException
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
     * @param filePath filePath
     * @param regex regex
     * @param replacement replacement
     * @return 查询结果
     * @throws IOException IOException
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
     * @param filePath filePath
     * @param content content
     * @param charset charset
     * @return 文件
     * @throws IOException IOException
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
     * @param filePath filePath
     * @param lines lines
     * @param charset charset
     * @return 文件
     * @throws IOException IOException
     */
    public static File write(String filePath, List<String> lines, Charset charset) throws IOException {
    	String content = ListUtils.toString(lines, SystemUtils.lineSeparator());
    	return write(filePath, content, charset);
    }

    /**
     * 将内容追加到文件尾部
     * <p>
     * 	使用RandomAccessFile实现
     * 
     * @param filePath 文件路径
     * @param content 文件内容
     */
    public static void appendFileContent(String filePath, String content) throws IOException {
        FileWriter writer = null;  
        try {
        	new File(filePath).getParentFile().mkdirs();
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件     
            writer = new FileWriter(filePath, true);     
            writer.write(content);
        }  finally {
        	IOUtils.close(writer);
        }
    }
    
    /**
     * 将内容追加到文件尾部
     * <p>
     * 	使用RandomAccessFile实现
     * </p>
     * @param filePath filePath
     * @param lines lines
     */
    public static void appendFileContent(String filePath, List<String> lines) throws IOException {
    	appendFileContent(filePath, ListUtils.toString(lines, System.lineSeparator()));
    }

    
    /**
     * 将流写文件(写)
     * @param in in
     * @param filePath filePath
     * @return 文件
     * @throws IOException IOException
     */
    public static File write(InputStream in, String filePath) throws IOException {
    	OutputStream out = null;
        
        try {
            out = getOutputStream(filePath);
            IOUtils.write(in, out, BUFFSIZE);
            return new File(filePath);
        } catch (IOException e) {
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
     * @param filePath filePath
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
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
     * @param file file
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
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
     * @param filePath filePath
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
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
     * @param file file
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static FileOutputStream getOutputStream(File file) throws FileNotFoundException {
    	makeDir(file.getParent(), true);	//创建父路径
    	return new FileOutputStream(file);
    }

    /**
     * 获取BufferedReader(读)
     * @param filePath filePath
     * @param charset charset
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static BufferedReader getBufferedReader(String filePath, Charset charset) throws FileNotFoundException {
    	if(charset == null) {
    		charset = FILE_READING_CHARSET;
    	}
    	FileInputStream in = FileUtils.getInputStream(filePath);
    	return new BufferedReader(new InputStreamReader(in, charset));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath filePath
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static BufferedInputStream getBufferedInputStream(String filePath) throws FileNotFoundException {
    	return new BufferedInputStream(getInputStream(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param file file
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static BufferedInputStream getBufferedInputStream(File file) throws FileNotFoundException {
    	return new BufferedInputStream(getInputStream(file));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath filePath
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static FileInputStream getInputStream(String filePath) throws FileNotFoundException {
    	return getInputStream(new File(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param file file
     * @return 流
     * @throws FileNotFoundException FileNotFoundException
     */
    public static FileInputStream getInputStream(File file) throws FileNotFoundException {
    	return new FileInputStream(file);
    }
    
    
    //--文件操作
	/**
	 * 移动文件或者文件夹,如从e:/aa/到f:/bb/aa/
	 * 遇到已经存在的文件会进行覆盖
	 * <p>
	 * 为什么不能用renameTo,请参考文章https://blog.csdn.net/findmyself_for_world/article/details/41648095
	 * 简单来说,renameTo不能跨文件系统移动文件,比如C盘是NTFS格式,E盘是FAT32格式，在这两个盘移动文件就会返回false
	 * 相对的，用Files包下的方法就没这种问题,对应本工具包的FileNioUtils中的move方法
	 * </p>
	 *
	 * @param sourcePath 源文件或文件夹的路径
	 * @param targetPath 目标文件或文件夹的路径
	 * @throws IOException 如果在移动过程中发生输入输出错误
	 */
	public static void move(String sourcePath, String targetPath) throws IOException {
		move(sourcePath, targetPath, true);
	}

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
	 * @param sourcePath source
	 * @param targetPath target
	 * @param overrideWhenExists 覆盖目标文件
	 * @throws IllegalArgumentException 文件路径为空
	 * @throws NoSuchFileException      源文件不存在
	 */
	public static void move(String sourcePath, String targetPath, boolean overrideWhenExists) throws IllegalArgumentException, IOException {
		Path source = Paths.get(sourcePath);
		Path target = Paths.get(targetPath);
		// 检查源文件夹是否存在，不存在则抛出异常
		if (!Files.exists(source)) {
			throw new NoSuchFileException(StringUtils.concat("源文件夹[", source, "]不存在"));
		}
		if (Files.isRegularFile(source)) {
			// 创建目标父文件夹
			Files.createDirectories(target.getParent());
			moveFile(source, target, overrideWhenExists);
		} else if (Files.isDirectory(source)) {
			// 创建目标文件夹
			Files.createDirectories(target);
			moveFolder(source, target, overrideWhenExists);
		}
		delete(sourcePath);
	}

	/**
	 * 将指定源路径下的文件或目录复制到目标路径下
	 * 此方法调用应避免在高并发环境下使用，因为它可能会导致性能瓶颈
	 * 遇到已经存在的文件会进行覆盖
	 *
	 * @param sourcePath 源文件或目录的路径
	 * @param targetPath 目标文件或目录的路径
	 * @throws IOException 如果复制过程中发生错误
	 */
	public static void copy(String sourcePath, String targetPath) throws IOException {
		// 调用带有一个额外参数的 copy 方法，默认值为 false，表示不覆盖已存在文件
		copy(sourcePath, targetPath, true);
	}

	/**
	 * 复制文件或文件夹
	 * @param sourcePath source
	 * @param targetPath target
	 * @throws IllegalArgumentException 文件路径为空,源文件不存在
	 * @throws NoSuchFileException NoSuchFileException
	 */
	public static void copy(String sourcePath, String targetPath, boolean overrideWhenExists) throws IllegalArgumentException, IOException {
		Path source = Paths.get(sourcePath);
		Path target = Paths.get(targetPath);
		// 检查源文件夹是否存在，不存在则抛出异常
		if (!Files.exists(source)) {
			throw new NoSuchFileException(StringUtils.concat("源文件夹[", source, "]不存在"));
		}
		if (Files.isRegularFile(source)) {
			// 创建目标父文件夹
			Files.createDirectories(target.getParent());
			copyFile(source, target, overrideWhenExists);
		} else if (Files.isDirectory(source)) {
			// 创建目标文件夹
			Files.createDirectories(target);
			copyFolder(source, target, overrideWhenExists);
		}
	}



	/**
	 * 递归扫描指定目录下的所有子文件
	 *
	 * @param dir 目标目录，必须是一个存在的目录
	 * @param fileWalker 实现了 FileWalker 接口的实例，用于处理扫描到的文件
	 * @throws IOException 如果扫描过程中发生 IO 错误
	 * @throws NoSuchFileException 如果指定的目录不存在
	 */
	public static void scanSubFiles(Path dir, FileWalker fileWalker) throws IOException {
		// 检查目录是否存在，不存在则抛出异常
		if (!Files.exists(dir)) {
			throw new NoSuchFileException(StringUtils.concat("源文件夹[", dir.toString(), "]不存在"));
		}
		// 使用 Files.walk 递归地遍历文件夹
		try (Stream<Path> walk = Files.walk(dir)) {
			// 将 Stream 转换为迭代器，手动遍历
			Iterator<Path> iterator = walk.iterator();
			while (iterator.hasNext()) {
				Path path = iterator.next();
				// 获取相对路径
				Path relativizePath = dir.relativize(path);
				// 调用 FileWalker 接口的 visit 方法，直接抛出异常
				fileWalker.visit(path, relativizePath);
			}
		}
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
	    if (files != null) {
			for (File f : files) {
				if (!delete(f)) {
					return false;
				}
			}
		}

	    file.delete();  
	    return true;
	}
    public static boolean delete(String path) {
    	return delete(new File(path));
    }

	/**	过滤掉字符串中的非法字符，以符合文件名规范，非法字符以下划线替换
	 * @param fileName fileName
	 * @return 替换后的文件名
	 */
	public static String replaceUnSupportedChar(String fileName) {
		if(StringUtils.isBlank(fileName)) {
			return fileName;
		}
		Pattern FilePattern = Pattern.compile("[\\\\/:*?\"<>|]");
		return FilePattern.matcher(fileName).replaceAll("_");
	}

	/**
	 * 替换文件名
	 * <p><pre>{@code
	 * replaceName("a.tar.gz", "b")=>"b.gz"
	 * }</pre>
	 * 
	 * @param wholeFileName wholeFileName
	 * @param newName newName
	 * @return 替换结果
	 */
    public static String replaceName(String wholeFileName, String newName) {
    	return wholeFileName.replaceFirst("^.+?(\\.[^\\.]+)?$", newName+"$1");
    }
    
    /**
     * 替换文件名(保留长拓展名)
     * <p><pre>{@code
     * replaceNameWithLongSuffix("a.tar.gz", "b")=>"b.tar.gz"
     * }</pre>
     * @param wholeFileName wholeFileName
     * @param newName newName
     * @return 替换结果
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
	 * @return 拓展名
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
	 * @param filePath filePath
	 * @return 拓展名
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
     * @return 路径
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
     * @param filePath filePath
     * @return 是否存在
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
	 * @param source 源文件路径
	 * @param target 目标路径
	 * @param overrideWhenExists 存在则覆盖
	 * @throws IllegalArgumentException IllegalArgumentException
	 * @throws NoSuchFileException      NoSuchFileException
	 * @throws IOException              IOException
	 */
	private static void moveFile(Path source, Path target, boolean overrideWhenExists) throws IOException, IllegalArgumentException {
		// 移动文件或目录
		if (overrideWhenExists) {
			// 当目标已存在且允许覆盖时，使用替换已存在的方式进行复制
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
		} else if (!Files.exists(target)) {
			// 当目标不存在时，直接进行复制
			Files.move(source, target);
		}
	}

	/**
	 * 移动文件夹及其子文件夹
	 * <p>
	 * 实质是复制文件到对应的位置,成功后删除源文件
	 * </p>
	 *
	 * @param source 源文件夹,如: d:/tmp
	 * @param target 目标文件夹,如: e:/tmp
	 * @param overrideWhenExists 存在则覆盖
	 * @throws IllegalArgumentException IllegalArgumentException
	 * @throws NoSuchFileException      NoSuchFileException
	 * @throws IOException              IOException
	 */
	private static void moveFolder(Path source, Path target, boolean overrideWhenExists) throws IOException, IllegalArgumentException {
		scanSubFiles(source, (p, rp)->{
			Path targetEntry = target.resolve(rp);
			// 只处理文件，跳过目录
			if (Files.isDirectory(targetEntry)) {
				Files.createDirectories(targetEntry);
				return;
			}
			// 移动
			moveFile(p, targetEntry, overrideWhenExists);
		});
	}

	/**
	 * 复制单个文件带进度条
	 * <p>
	 * 进度监听参数传null则单做普通的单个文件复制使用，不如直接调用copy方法
	 * </p>
	 *
	 * @param source source
	 * @param target target
	 * @throws IllegalArgumentException 文件路径为空
	 * @throws NoSuchFileException      源文件不存在，源文件不是个文件(可能是文件夹)
	 * @throws IOException              io异常
	 */
	private static void copyFile(Path source, Path target, boolean overrideWhenExists) throws IllegalArgumentException, IOException {
		// 移动文件或目录
		if (overrideWhenExists) {
			// 当目标已存在且允许覆盖时，使用替换已存在的方式进行复制
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		} else if (!Files.exists(target)) {
			// 当目标不存在时，直接进行复制
			Files.copy(source, target);
		}

	}

	/**
	 * 复制文件夹及其内容
	 *
	 * @param source           源文件夹的路径
	 * @param target           目标文件夹的路径
	 * @param overrideWhenExists 当目标文件或文件夹已存在时是否覆盖
	 * @throws IOException      如果发生I/O错误
	 * @throws IllegalArgumentException 如果源文件夹不存在或目标是文件而不是目录
	 */
	private static void copyFolder(Path source, Path target, boolean overrideWhenExists) throws IOException, IllegalArgumentException {
		// 遍历源文件夹的所有子文件和子目录
		scanSubFiles(source, (p, rp)->{
			Path targetEntry = target.resolve(rp);
			// 只处理文件，跳过目录
			if (Files.isDirectory(targetEntry)) {
				Files.createDirectories(targetEntry);
				return;
			}
			// 移动文件，如果已存在且允许覆盖，则覆盖原有文件
			copyFile(p, targetEntry, overrideWhenExists);
		});
	}
	
	/**
	 * 获取文件md5值
	 * @param filePath filePath
	 * @return md5
	 * @throws IOException IOException
	 * @throws NoSuchAlgorithmException NoSuchAlgorithmException
	 */
	public static String md5(String filePath) throws IOException, NoSuchAlgorithmException {
        InputStream fis = null;
        try {
        	fis = getInputStream(filePath);
        	MessageDigest md = MessageDigest.getInstance("MD5");
	        byte[] buffer = new byte[1024];
	        int length;
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

	@FunctionalInterface
	public interface FileWalker {
		void visit(Path path, Path relativizePath) throws IOException;
	}
	
}