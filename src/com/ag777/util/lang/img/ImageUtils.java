package com.ag777.util.lang.img;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.exception.Assert;
import com.ag777.util.lang.exception.model.ImageNotSupportException;

/**
 * 图片处理工具类
 * <p>
 * 对于原生<code>ImageIO</code>的封装
 * 参考资料:https://blog.csdn.net/u012454773/article/details/50735266
 * </p>
 * 
 * @author ag777
 * @version create on 2018年05月08日,last modify at 2018年05月08日
 */
public class ImageUtils {
	
	public static final String PNG = "png";
	public static final String JEPG = "JPEG";	//其实就是jpg啦
	
	private ImageUtils() {}
	
	/**
	 * 获取图片格式
	 * 
	 * @param path
	 * @return
	 * @throws IllegalArgumentException 文件不存在等异常
	 * @throws ImageNotSupportException 图片文件格式不支持异常(一般文件就不是图片)
	 * @throws IOException IO相关异常
	 */
	public static String getType(String path) throws IllegalArgumentException, ImageNotSupportException, IOException {
		ImageInputStream iis = null;
		try {
			File file = new File(path);
			Assert.notExisted(file, "文件["+path+"]不存在");
			// create an image input stream from the specified fileDD
			iis = ImageIO.createImageInputStream(file);
	 
	        // get all currently registered readers that recognize the image format
	        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
	 
	        if (!iter.hasNext()) {
	            throw new ImageNotSupportException("不支持的图片格式:"+path);
	        }
	 
	        // get the first reader
	        ImageReader reader = iter.next();
	 
	        return reader.getFormatName();
		} catch (IOException ex) {
			 throw ex;
		} finally {
			// close stream
			IOUtils.close(iis);
		}
       
	}
	

	/**
	 * 转换图片格式
	 * 
	 * @param srcPath
	 * @param destPath
	 * @param type
	 * @return 
	 * @throws IllegalArgumentException 文件不存在等异常
	 * @throws ImageNotSupportException 图片文件格式不支持异常(一般文件就不是图片)
	 * @throws IOException IO相关异常
	 */
	public static File transform(String srcPath, String destPath, String type) throws IllegalArgumentException, ImageNotSupportException, IOException  {
		BufferedImage bi = null;
		try {
			File file = new File(srcPath);
			Assert.notExisted(file, "文件["+srcPath+"]不存在");
			bi=ImageIO.read(file);  
			if(bi == null) {
				 throw new ImageNotSupportException("不支持的图片格式:"+srcPath);
			}
            //将读取的图片写到本地  
            File f2 = new File(destPath);  
            f2.getParentFile().mkdirs();
            ImageIO.write(bi, type, f2); 
            return new File(destPath);
		} catch (IOException ex) {
			 throw ex;
		} finally {
		}
	}
	
	/**
	 * 图片裁剪
	 * <p>
	 * Java Image I/O API 提供了为编写复 杂程序的能力。  
     * 为了利用API的高级特性，应用程序应当直接使用类ImageReader和 ImageWriter读写图片  
	 * </p>
	 * 
	 * @param srcPath
	 * @param destPath
	 * @param imgType
	 * @param helper
	 * @throws IllegalArgumentException
	 * @throws ImageNotSupportException
	 * @throws IOException
	 */
    public static File complexRWImage(String srcPath, String destPath, String imgType, ComplexHelper helper) throws IllegalArgumentException, ImageNotSupportException, IOException {  
    	ImageInputStream is = null;
    	ImageOutputStream os=null;
    	try {  
            /**********************读取图片*********************************/  
            File file = new File(srcPath); 
            Assert.notExisted(file, "文件["+srcPath+"]不存在");
            is = ImageIO.createImageInputStream(file);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(is);	//网上不是这么写的，感觉这样用了两次is,当时网上的写法需要指定格式
            if (!iter.hasNext()) {
	            throw new ImageNotSupportException("不支持的图片格式:"+srcPath);
	        }
            ImageReader reader = iter.next();  

            /*  
             * 一旦有了输入源，可以把它与一个ImageReader对象关联起来.  
             * 如果输入源文件包含多张图片，而程序不保证按顺序读取时，第二个参数应该设置为 false。  
             * 对于那些只允许存储一张图片的文件格式，永远传递true是合理的  
            */  
            reader.setInput(is, true);
            
            /*  
             * 如果需要更多的控制，可以向read()方法传递一个ImageReadParam类型的参数。  
             * 一个 ImageReadParam对象可以让程序更好的利用内存。  
             * 它不仅允许指定一个感兴趣的区域，还 可以指定一个抽样因子，用于向下采样.  
             * */  
            ImageReadParam param=reader.getDefaultReadParam();  
            int imageIndex=0;  
            int width = reader.getWidth(imageIndex);
            int height = reader.getHeight(imageIndex);
            Rectangle rectangle=helper.getRectangle(width, height);
            param.setSourceRegion(rectangle);  
            BufferedImage bi=reader.read(0, param);  
              
            /**********************写图片*********************************/  
            Iterator<ImageWriter> writes=ImageIO.getImageWritersByFormatName(imgType);  
            ImageWriter imageWriter=writes.next();  
            file=new File(destPath);
            file.getParentFile().mkdirs();
            os=ImageIO.createImageOutputStream(file);  
            imageWriter.setOutput(os);  
            imageWriter.write(bi);
            return new File(destPath);
        } catch (IOException ex) {  
            throw ex;
        }  finally {
        	IOUtils.close(is);
        	IOUtils.close(os);
        }
    } 
    
    public static void main(String[] args) throws IOException, IllegalArgumentException, ImageNotSupportException {
		String srcPath = "e:\\ad.png";
		String destPath = "e:\\a\\ad.jpg";
		System.out.println(getType(srcPath));
		transform(srcPath, destPath, JEPG);
		System.out.println(getType(destPath));
//		complexRWImage(srcPath, destPath, JEPG, (width,height)->{
//			return new Rectangle(0, 0, width, height/2);
//		});
		
	}
    
    /**
     * 裁剪辅助类
     * @author ag777
     *
     */
    public interface ComplexHelper {
    	public Rectangle getRectangle(int width, int height);
    }
}
