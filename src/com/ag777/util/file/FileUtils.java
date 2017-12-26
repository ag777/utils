package com.ag777.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.ag777.util.file.model.FileAnnotation;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.RegexUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.SystemUtils;
import com.ag777.util.lang.collection.ListUtils;
import com.ag777.util.lang.filter.StringFilter;
import com.ag777.util.lang.model.Charsets;

/**
 * 文件操作工具类
 * 
 * @author ag777
 * @version create on 2017年04月25日,last modify at 2017年12月26日
 */
public class FileUtils {
    private static String FILE_WRITING_ENCODING = Charsets.UTF_8;
    private static String FILE_READING_ENCODING = Charsets.UTF_8;
    private static int BUFFSIZE = 1024;	//一次性读取的字节

    public static String encodingRead() {
    	return FILE_READING_ENCODING;
    }
    public static void encodingRead(String encoding) {
    	FILE_READING_ENCODING = encoding;
    }
    public static String encodingWrite() {
    	return FILE_WRITING_ENCODING;
    }
    public static void encodingWrite(String encoding) {
    	FILE_WRITING_ENCODING = encoding;
    }
    
    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param lineSparator 换行时插入的字符
     * @return
     * @throws IOException
     */
    public static String readText(String filePath, String lineSparator) throws IOException {
       return readText(filePath, lineSparator, Charset.forName(FILE_READING_ENCODING));
    }
    
    /**
     * 读取文件内容
     * @param filePath 文件路径
     * @param lineSparator 换行时插入的字符
     * @return
     * @throws IOException
     */
    public static String readText(String filePath, String lineSparator, Charset encoding) throws IOException {

        try {
        	if(encoding == null) {
        		encoding = Charset.forName(FILE_READING_ENCODING);
        	}
        	FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.readText(fis, lineSparator, encoding.toString());
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
    	return readLines(filePath, Charset.forName(FILE_READING_ENCODING));
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
     * @param encoding
     * @return
     * @throws IOException
     */
    public static List<String> readLines(String filePath, Charset encoding) throws IOException {
    	 try {
    		 if(encoding == null) {
         		encoding = Charset.forName(FILE_READING_ENCODING);
         	}
    		 FileInputStream fis = new FileInputStream(filePath);
    		 return IOUtils.readLines(fis, encoding);
          } catch (FileNotFoundException ex) {
              throw new IOException(StringUtils.concat("文件[", filePath, "]不存在"), ex);
          } catch (IOException ex) {
              throw new IOException(StringUtils.concat("读取文件[",filePath,"]时发生错误!"), ex);
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
    	
    	write(targetPath, newLines, null, true);
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
    	write(filePath, lines, null, true);
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
    	write(filePath,content, null, true);
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
            return IOUtils.find(fis, regex, replacement, FILE_READING_ENCODING);	//关闭流的操作里面都做了
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
            return IOUtils.findAll(fis, regex, replacement, FILE_READING_ENCODING);
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
     * @param encoding
     * @param isOverride
     * @return
     * @throws IOException
     */
    public static File write(String filePath, String content, String encoding, boolean isOverride) throws IOException {
        if (StringUtils.isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return write(is, filePath, isOverride);
    }
    
    /**
     * 将所有行写出到文件
     * @param filePath
     * @param lines
     * @param encoding
     * @param isOverride
     * @return
     * @throws IOException
     */
    public static File write(String filePath, List<String> lines, String encoding, boolean isOverride) throws IOException {
    	String content = ListUtils.toString(lines, SystemUtils.lineSeparator());
    	return write(filePath, content, encoding, isOverride);
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
    public static boolean appendFileContent(String fileName, String content) {
    	RandomAccessFile randomFile = null;
        try {
            // 打开一个随机访问文件流，按读写方式
        	randomFile = new RandomAccessFile(fileName, "rw");
            // 文件长度，字节数
            long fileLength = randomFile.length();
            //将写文件指针移到文件尾。
            randomFile.seek(fileLength);
            randomFile.writeBytes(content);
            randomFile.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	IOUtils.close(randomFile);
        }
        return false;
    }

    
    /**
     * 将流写文件
     * @param is
     * @param filePath
     * @param isOverride
     * @return
     * @throws IOException
     */
    public static File write(InputStream in, String filePath, boolean isOverride) throws IOException {
        String sPath = extractFilePath(filePath);
        if (!pathExists(sPath)) {
            makeDir(sPath, true);
        }
        
        if (!isOverride && fileExists(filePath)) {
            return new File(filePath);
        }
        
        try {
            File file = new File(filePath);
            FileOutputStream out = new FileOutputStream(file);
            IOUtils.write(in, out, BUFFSIZE);
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(StringUtils.concat("写入文件[",filePath,"]时发生错误!"), e);
        } 
    }

    /**
     * 通过文件路径获取输出流
     * 
     * @param filePath
     * @return
     * @throws FileNotFoundException
     */
    public static OutputStream getOutputStream(String filePath) throws FileNotFoundException {
    	File file = new File(filePath);
    	return new FileOutputStream(file);
    }

    public static FileInputStream getInputStream(String filePath) throws FileNotFoundException {
    	File file = new File(filePath);
    	return new FileInputStream(file);
    }
    
	/**
	 * 移动文件或者文件夹,如从e:/aa.txt到e:/tmp/aa.txt, 从e:/aa到e:/bb/aa
	 * @param source	源文件路径
	 * @param target	目标路径
	 */
	public static void move(String source, String target) {
		File sourceFile = new File(source);
		File targetFile = new File(target);
		try {
			sourceFile.renameTo(targetFile);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    
	/**
	 * 复制文件
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @return
	 */
	public static boolean copy(String source, String target) {
		File srcFile = new File(source);
		if(srcFile.exists()) {
			if(srcFile.isFile()) {
				return copyFile(source, target);
			} else if(srcFile.isDirectory()) {
				return copyFolder(source, target);
			}
		} 
		Console.err("文件不存在");
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
	 * 获取文件拓展名
	 * @param filePath 源文件路径
	 * @return
	 */
	public static String getFilePrefix(String filePath) {
		//String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
	    //return prefix;
		return RegexUtils.find(filePath, "\\.([^\\.]*)$","$1","");
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

    public static boolean fileExists(String filePath) {
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
    public static boolean makeDir(String dirPath, boolean needCreateParentDir) {
        boolean zResult = false;
        File file = new File(dirPath);
        if (needCreateParentDir)
            zResult = file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
        else
            zResult = file.mkdir(); // 如果父目录不存在，不做处理
        if (!zResult)
            zResult = file.exists();
        return zResult;
    }
   
    /*----------内部工具-----------*/
    
	/**
	 * 复制单个文件
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @throws IOException
	 */
	private static boolean copyFile(String source, String target) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			File fin = new File(source);
			File fout = new File(target);
			if (!fin.exists()) {
				Console.err(StringUtils.concat("源文件夹[", source,"]不存在"));
				return false;
			}
			if (!fout.exists()) {
				File parent = new File(fout.getParent()); // 得到父文件夹
				if (!parent.exists()) {
					parent.mkdirs();
				}
				fout.createNewFile();
			}
			bis = new BufferedInputStream(
					new FileInputStream(fin));
			bos = new BufferedOutputStream(
					new FileOutputStream(fout));
			IOUtils.write(bis, bos, BUFFSIZE);
			return true;
		} catch(IOException ex) {
			ex.printStackTrace();
		} 
		return false;
	}
	
	/**
	 * 复制文件夹及其子文件夹
	 * 
	 * @param source 源文件夹,如: d:/tmp
	 * @param target 目标文件夹,如: e:/tmp
	 * @throws IOException
	 */
	private static boolean copyFolder(String source, String target) {
		
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
		if (fileList == null) {
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
        FileInputStream fis = null;
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
