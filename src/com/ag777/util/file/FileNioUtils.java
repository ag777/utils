package com.ag777.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.stream.Stream;

import com.ag777.util.file.model.DeleteDirectory;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.IOUtils;

/**
 * 文件操作工具类
 * <p>
 * 使用nio包底下的Files工具类进行操作,能提高效率
 * 后续会逐渐完善去掉掉FileUtils
 * </p>
 * 
 * @author ag777
 * @version create on 2018年04月18日,last modify at 2018年04月20日
 */
public class FileNioUtils {

	private FileNioUtils() {}
	
	//--读写
	/**
	 * 读取所有行
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static Stream<String> lines(String filePath, Charset charset) throws IOException {
		Path path = getPath(filePath);
		return Files.lines(path, charset);
	}
	
	/**
	 * 读取文件所有行
	 * 
	 * @param filePath
	 * @param charset
	 * @return
	 * @throws IOException
	 */
	public static List<String> readLines(String filePath, Charset charset) throws IOException {
		Path path = getPath(filePath);
		return Files.readAllLines(path, charset);
	}
	
	/**
	 * 将所有行写出到文件
	 * @param filePath
	 * @param lines
	 * @param chartset
	 * @return
	 * @throws IOException
	 */
	public static Path write(String filePath, List<String> lines, Charset chartset) throws IOException {
		Path path = getPath(filePath);
		makeDir(path);
		return Files.write(path, lines, chartset, StandardOpenOption.CREATE_NEW);
	}
	
	/**
	 * 创建父目录
	 * @param path
	 * @return
	 */
	public static boolean makeDir(Path path) {
		if(path == null) {
			return true;
		}
		try {
			if(Files.isDirectory(path)) {	//如果是目录则直接创建
				Files.createDirectories(path);
			} else {	//如果是文件就创建父路径
				Path dir = path.getParent();	//path是根目录则得到null
				if(dir == null) {
					return true;
				}
				Files.createDirectories(dir);
			}
			return true;
		} catch(IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	//--文件操作
	/**
	 * 删除文件或文件件
	 * <p>
	 * 遇到删除不了的文件则停止删除
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean delete(String filePath) {
		Path src = getPath(filePath);
		if(!Files.exists(src)) {	//文件不存在
			return true;
		}
		if(Files.isDirectory(src)) {
			DeleteDirectory walker = new DeleteDirectory();
			try {
				
				Files.walkFileTree(src, walker);
				return true;
			} catch (IOException e) {
				if(walker != null) {
					Console.err("删除失败:"+walker.getCurPath().toAbsolutePath().toString());
				}
				e.printStackTrace();
			}
		} else {
			try {
				Files.delete(src);
				return true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return false;
	}
	
	/**
	 * 移动文件或者文件夹,如从e:/aa/到f:/bb/aa/
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @return 
	 */
	public static boolean move(String source, String target) {
		Path src = getPath(source);
		Path dest = getPath(target);
		if(Files.exists(src)) {
			if(Files.isDirectory(src)) {
				return moveFolder(src, dest);
			} else {
				return moveFile(src, dest);
			}
		} 
		Console.err("文件不存在:"+source);
		return false;
	}
	
	/**
	 * 复制文件
	 * <p>
	 * 注意复制目录是将该目录底下的文件复制到目标目录下，
	 * 比如要将/usr/a/目录移到/b/a/路径下要写copy("/usr/a/", "/b/a/");
	 * </p>
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @return
	 */
	public static boolean copy(String source, String target) {
		Path src = getPath(source);
		Path dest = getPath(target);
		makeDir(dest);
		if(Files.exists(src)) {
			if(Files.isDirectory(src)) {
				return copyFolder(src, dest);
			} else {
				return copyFile(src, dest);
			}
		} 
		Console.err("文件不存在:"+source);
		return false;
	}
	
	//--输入/输出流
	//-写
	/**
	 * 通过文件路径获取写出(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
	 * @param path
	 * @param charset
	 * @return
	 * @throws IOException
	 */
    public static BufferedWriter getBufferedOutputStream(Path path, Charset charset) throws IOException {
    	return Files.newBufferedWriter(path, charset);
    }
    
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param filePath
     * @return
     * @throws IOException 
     */
    public static BufferedOutputStream getBufferedOutputStream(String filePath) throws IOException {
    	return new BufferedOutputStream(getOutputStream(filePath));
    }
    
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param path
     * @return
     * @throws IOException 
     */
    public static BufferedOutputStream getBufferedOutputStream(Path path) throws IOException {
    	return new BufferedOutputStream(getOutputStream(path));
    }
    
    /**
     * 通过文件路径获取输出流(写)
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param filePath
     * @return
     * @throws IOException 
     */
    public static OutputStream getOutputStream(String filePath) throws IOException {
    	return getOutputStream(getPath(filePath));
    }
    
    /**
     * 通过文件路径获取输出流
     * <p>
     * 	该方法会帮忙创建文件父路径
     * </p>
     * 
     * @param path
     * @return
     * @throws IOException 
     */
    public static OutputStream getOutputStream(Path path) throws IOException {
    	makeDir(path);	//创建父路径
    	return Files.newOutputStream(path, StandardOpenOption.CREATE);
    }

    //-读
    /**
     * 获取读取器
     * @param path
     * @param charset
     * @return
     * @throws IOException
     */
    public static BufferedReader getBufferedReader(Path path, Charset charset) throws IOException {
    	return Files.newBufferedReader(path, charset);
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath
     * @return
     * @throws IOException 
     */
    public static BufferedInputStream getBufferedInputStream(String filePath) throws IOException {
    	return new BufferedInputStream(getInputStream(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param path
     * @return
     * @throws IOException 
     */
    public static BufferedInputStream getBufferedInputStream(Path path) throws IOException {
    	return new BufferedInputStream(getInputStream(path));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param filePath
     * @return
     * @throws IOException 
     */
    public static InputStream getInputStream(String filePath) throws IOException {
    	return getInputStream(getPath(filePath));
    }
    
    /**
     * 获取文件输入流(读)
     * 
     * @param path
     * @return
     * @throws IOException 
     */
    public static InputStream getInputStream(Path path) throws IOException {
    	return Files.newInputStream(path, StandardOpenOption.READ);
    }
    
    /*===================内部方法==================*/
    /**
     * 移动单个文件
     * @param source
     * @param target
     * @return
     */
    private static boolean moveFile(Path source, Path target) {
    	try {
			Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
    	return false;
    }
    /**
     * 移动文件夹
     * 
     * @param source
     * @param target
     * @return
     */
    private static boolean moveFolder(Path source, Path target) {
    	boolean flag = false;
    	DirectoryStream<Path> streamList = null;	//这个是个流，所以需要被关掉
    	try {
			//创建目录
			if(!Files.exists(target)) {
				Files.createDirectory(target);
			}
			streamList = Files.newDirectoryStream(source);  
	        for (Path pathSun : streamList){
	        	String targetSun = target.toString()+File.separator+pathSun.getFileName();
	        	if(Files.isDirectory(pathSun)) {
	        		moveFolder(pathSun, getPath(targetSun+File.separator));
	        	} else {
	        		moveFile(pathSun, getPath(targetSun));
	        	}
	        }
	        flag = true;
	    } catch (IOException e) {  
	         e.printStackTrace();  
	    } finally {
	    	IOUtils.close(streamList);
	    	if(flag) {	//只有在移动成功时才删除文件夹
	    		try {
					Files.delete(source);
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
		return flag;
    }
    
    /**
	 * 复制单个文件
	 * 
	 * @param source	源文件路径
	 * @param target	目标路径
	 * @throws IOException
	 */
	private static boolean copyFile(Path source, Path target) {
		try {
			Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 复制文件夹及其子文件夹
	 * 
	 * @param source 源文件夹,如: d:/tmp
	 * @param target 目标文件夹,如: e:/tmp
	 * @throws IOException
	 */
	private static boolean copyFolder(Path source, Path target) {
		boolean flag = false;
		DirectoryStream<Path> streamList = null;	//这个是个流，所以需要被关掉
		try {
			//创建目录
			if(!Files.exists(target)) {
				Files.createDirectory(target);
			}
			streamList = Files.newDirectoryStream(source);  
	        for (Path pathSun : streamList){
	        	String targetSun = target.toString()+File.separator+pathSun.getFileName();
	        	if(Files.isDirectory(pathSun)) {
	        		copyFolder(pathSun, getPath(targetSun+File.separator));
	        	} else {
	        		copyFile(pathSun, getPath(targetSun));
	        	}
	        }
	        flag = true;
	    } catch (IOException e) {  
	         e.printStackTrace();  
	    } finally {
	    	IOUtils.close(streamList);
	    }
		return flag;
	}
    
    /**
	 * 根据文件路径获取Path
	 * 
	 * @param filePath
	 * @return
	 */
	private static Path getPath(String filePath) {
		return Paths.get(filePath);
	}
	
}
