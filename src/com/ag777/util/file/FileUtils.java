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
import java.util.List;
import com.ag777.util.lang.Console;
import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.RegexUtils;

/**
 * Author: xsf,ag777
 * Time: created at 2016/4/25.
 * last modify time: 2017/08/01.
 * Email: xsf_uestc_ncl@163.com
 */
public class FileUtils {
    private static String FILE_WRITING_ENCODING = "UTF-8";
    private static String FILE_READING_ENCODING = "UTF-8";
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
     * @throws Exception
     */
    public static String readText(String filePath, String lineSparator) throws Exception {

        try {
        	FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.readText(fis, lineSparator, FILE_READING_ENCODING);
        } catch (FileNotFoundException ex) {
            throw new Exception("要读取的文件没有找到!", ex);
        } catch (IOException ex) {
            throw new Exception("读取文件时错误!", ex);
        }
    }
    
    /**
     * 读取文件中的所有行
     * @param filePath 文件路径
     * @return
     * @throws Exception
     */
    public static List<String> readLines(String filePath) throws Exception {
    	 try {
         	FileInputStream fis = new FileInputStream(filePath);
             return IOUtils.readLines(fis, FILE_READING_ENCODING);
         } catch (FileNotFoundException ex) {
             throw new Exception("要读取的文件没有找到!", ex);
         } catch (IOException ex) {
             throw new Exception("读取文件时错误!", ex);
         }
    }

    /**
     * 从文件内容中定位信息并以一定格式返回
     * @param filePath 文件路径
     * @param regex 匹配用的正则表达式
     * @param replacement 替换式
     * @return
     * @throws Exception
     */
    public static String findText(String filePath,String regex, String replacement) throws Exception {
    	
		try {
            FileInputStream fis = new FileInputStream(filePath);
            return IOUtils.find(fis, regex, replacement, FILE_READING_ENCODING);	//关闭流的操作里面都做了
        } catch (FileNotFoundException ex) {
            throw new Exception("要读取的文件没有找到!", ex);
        } catch (IOException ex) {
            throw new Exception("读取文件时错误!", ex);
        } 
    }
    
    /**
     * 查找文件中所有匹配的字符串
     * @param _sFileName
     * @param regex
     * @param replacement
     * @return
     * @throws Exception
     */
    public static List<String> findAllText(String _sFileName,String regex, String replacement) throws Exception {
    	
		try {
            FileInputStream fis = new FileInputStream(_sFileName);
            return IOUtils.findAll(fis, regex, replacement, FILE_READING_ENCODING);
        } catch (FileNotFoundException ex) {
            throw new Exception("要读取的文件没有找到!", ex);
        } catch (IOException ex) {
            throw new Exception("读取文件时错误!", ex);
        } 
    }
    
    /**
     * 将内容写入文件
     * @param path
     * @param content
     * @param encoding
     * @param isOverride
     * @return
     * @throws Exception
     */
    public static File write(String path, String content, String encoding, boolean isOverride) throws Exception {
        if (isEmpty(encoding)) {
            encoding = FILE_WRITING_ENCODING;
        }
        InputStream is = new ByteArrayInputStream(content.getBytes(encoding));
        return write(is, path, isOverride);
    }

    /**
     * 将流写文件
     * @param is
     * @param path
     * @param isOverride
     * @return
     * @throws Exception
     */
    public static File write(InputStream in, String path, boolean isOverride) throws Exception {
        String sPath = extractFilePath(path);
        if (!pathExists(sPath)) {
            makeDir(sPath, true);
        }
        
        if (!isOverride && fileExists(path)) {
            return new File(path);
        }
        
        try {
            File file = new File(path);
            FileOutputStream out = new FileOutputStream(file);
            IOUtils.write(in, out, BUFFSIZE);
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("写文件错误", e);
        } 
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
     * @param path	要删除的文件/文件夹路径
     */
    public static boolean delete(File path) {  
	    if (!path.exists())  
	        return true;  
	    if (path.isFile()) {  
	        if(path.delete()) {
	        	return true;
	        }  
	        Console.err("文件:"+path+"删除失败");
	        return false;  
	    }  
	    File[] files = path.listFiles();  
	    for (int i = 0; i < files.length; i++) {  
	        if(!delete(files[i])) {
	        	return false;
	        }
	    }  
	    path.delete();  
	    return true;
	}
    public static boolean delete(String path) {
    	return delete(new File(path));
    }

	
	/**
	 * 获取文件拓展名
	 * @param fileName 源文件路径
	 * @return
	 */
	public static String getFilePrefix(String fileName) {
		//String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
	    //return prefix;
		return RegexUtils.find(fileName, "\\.([^\\.]*)$","$1","");
	}	
	
    /**
     * 移除字符串中的BOM前缀
     *
     * @param _sLine 需要处理的字符串
     * @return 移除BOM后的字符串.
     */
	public static String removeBomHeaderIfExists(String _sLine) {
        if (_sLine == null) {
            return null;
        }
        String line = _sLine;
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
     * @param _sFilePathName 文件路径
     * @return
     */
    public static String extractFilePath(String _sFilePathName) {
        int nPos = _sFilePathName.lastIndexOf('/');
        if (nPos < 0) {
            nPos = _sFilePathName.lastIndexOf('\\');
        }

        return (nPos >= 0 ? _sFilePathName.substring(0, nPos + 1) : "");
    }

    /**
     * 检查指定文件的路径是否存在
     *
     * @param _sPathFileName 文件名称(含路径）
     * @return 若存在，则返回true；否则，返回false
     */
    public static boolean pathExists(String _sPathFileName) {
        String sPath = extractFilePath(_sPathFileName);
        return fileExists(sPath);
    }

    public static boolean fileExists(String _sPathFileName) {
        File file = new File(_sPathFileName);
        return file.exists();
    }

    /**
     * 创建目录
     *
     * @param _sDir             目录名称
     * @param _bCreateParentDir 如果父目录不存在，是否创建父目录
     * @return
     */
    public static boolean makeDir(String _sDir, boolean _bCreateParentDir) {
        boolean zResult = false;
        File file = new File(_sDir);
        if (_bCreateParentDir)
            zResult = file.mkdirs(); // 如果父目录不存在，则创建所有必需的父目录
        else
            zResult = file.mkdir(); // 如果父目录不存在，不做处理
        if (!zResult)
            zResult = file.exists();
        return zResult;
    }
   
    /*----------内部工具-----------*/
    public static boolean isEmpty(String src) {
    	return src == null || src.isEmpty();
    }
    
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
				Console.err("源文件:"+source+"不存在");
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
		} catch(Exception ex) {
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
			Console.err("源文件夹:"+source+"不存在");
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
					Console.err("移动文件:"+newSource+"失败");
					return false;
				}
			}
		}
		return true;
	}
}
