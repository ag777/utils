package com.ag777.util.lang.img;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageOutputStream;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.StringUtils;
import com.ag777.util.lang.exception.Assert;
import com.ag777.util.lang.exception.model.ImageNotSupportException;
import com.sun.imageio.plugins.gif.GIFImageReader;
import com.sun.imageio.plugins.gif.GIFImageReaderSpi;
import com.sun.imageio.plugins.png.PNGImageWriter;
import com.sun.imageio.plugins.png.PNGImageWriterSpi;

/**
 * 图片处理工具类
 * <p>
 * 对于原生<code>ImageIO</code>的封装
 * 参考资料:https://blog.csdn.net/u012454773/article/details/50735266
 * </p>
 * 
 * @author ag777
 * @version create on 2018年05月08日,last modify at 2019年01月10日
 */
public class ImageUtils {
	
	public static final String PNG = "png";
	public static final String JEPG = "JPEG";	//其实就是jpg啦
	
	private ImageUtils() {}
	
	/**
	 * 获取图片格式
	 * 
	 * @param filePath
	 * @return
	 * @throws IllegalArgumentException 文件不存在等异常
	 * @throws ImageNotSupportException 图片文件格式不支持异常(一般文件就不是图片)
	 * @throws IOException IO相关异常
	 */
	public static String getType(String filePath) throws IllegalArgumentException, ImageNotSupportException, IOException {
		ImageInputStream iis = null;
		try {
			File file = new File(filePath);
			Assert.notExisted(file, "文件["+filePath+"]不存在");
			// create an image input stream from the specified fileDD
			iis = ImageIO.createImageInputStream(file);
	 
	        // get all currently registered readers that recognize the image format
	        Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
	 
	        if (!iter.hasNext()) {
	            throw new ImageNotSupportException("不支持的图片格式:"+filePath);
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
	 * 获取图片宽高
	 * <p>
	 * 数组第一个值使宽度，第二个值是高度
	 * </p>
	 * 
	 * @param filePath
	 * @return
	 * @throws IllegalArgumentException 文件不存在等异常
	 * @throws ImageNotSupportException 图片文件格式不支持异常(一般文件就不是图片)
	 * @throws IOException IO相关异常
	 */
	public static int[] getWidthAndHeight(String filePath) throws IllegalArgumentException, ImageNotSupportException, IOException {
		BufferedImage bi = null;
		try {
			File file = new File(filePath);
			Assert.notExisted(file, "文件["+ filePath+"]不存在");
			bi=ImageIO.read(file);  
			if(bi == null) {
				 throw new ImageNotSupportException("不支持的图片格式:"+filePath);
			}
           int width = bi.getWidth();
           int height = bi.getHeight();
           return new int[]{width, height};
		} catch (IOException ex) {
			 throw ex;
		} finally {
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
    
    /**
     * 缩放至固定大小(未测试)
     * <p>
     * 宽高不能同时为0，其中一项为0代表该项为按比例缩放
     * </p>
     * 
     * @param srcPath
     * @param destPath
     * @param w 传0则需要固定高度,等比缩放
     * @param h 传0则需要固定宽度,等比缩放
     * @throws IllegalArgumentException
     * @throws ImageNotSupportException 
     * @throws IOException 
     */
    public static void scare(String srcPath, String destPath, int w, int h)  throws IllegalArgumentException, ImageNotSupportException, IOException {
        
        double wr=0,hr=0;
        File srcFile = new File(srcPath);
        Assert.notExisted(srcFile, "文件["+srcPath+"]不存在");
        File destFile = new File(destPath);

        BufferedImage bufImg = ImageIO.read(srcFile); //读取图片
        if(bufImg == null) {
			 throw new ImageNotSupportException("不支持的图片格式:"+srcPath);
		}
        
        if(w ==0 || h==0) {
        	int width = bufImg.getWidth();
        	int height = bufImg.getHeight();
        	if(w == 0) {	//计算宽度,四舍五入
        		w = Math.round((width * h * 1f)/height);
        	} else if(h == 0) {	//计算高度,四舍五入
        		h = Math.round((height * w * 1f)/width);
        	}
        }  
        
		Image Itemp = bufImg.getScaledInstance(w, h, BufferedImage.SCALE_SMOOTH);//设置缩放目标图片模板
        
        wr=w*1.0/bufImg.getWidth();     //获取缩放比例
        hr=h*1.0 / bufImg.getHeight();

        AffineTransformOp ato = new AffineTransformOp(AffineTransform.getScaleInstance(wr, hr), null);
        Itemp = ato.filter(bufImg, null);
        try {
        	destFile.getParentFile().mkdirs();
            ImageIO.write((BufferedImage) Itemp,destPath.substring(destPath.lastIndexOf(".")+1), destFile); //写入缩减后的图片
        } catch (IOException ex) {
            throw ex;
        }
    }
    
    /**
     * 压缩图片质量
     * <p>
     * 代码地址:http://www.ibooker.cc/article/109/detail
     * </p>
     * 
     * @param bufferedImage
     * @param targetPath
     * @param quality
     * @throws IOException
     */
    public static void zoomBufferedImageByQuality(BufferedImage bufferedImage, String targetPath, float quality) throws IOException {
		// 得到指定Format图片的writer
		Iterator<ImageWriter> iter = ImageIO.getImageWritersByFormatName("jpeg");// 得到迭代器
		ImageWriter writer = (ImageWriter) iter.next(); // 得到writer

		// 得到指定writer的输出参数设置(ImageWriteParam)
		ImageWriteParam iwp = writer.getDefaultWriteParam();
		iwp.setCompressionMode(ImageWriteParam.MODE_EXPLICIT); // 设置可否压缩
		iwp.setCompressionQuality(quality); // 设置压缩质量参数，0~1，1为最高质量
		iwp.setProgressiveMode(ImageWriteParam.MODE_DISABLED);
		ColorModel colorModel = ColorModel.getRGBdefault();
		// 指定压缩时使用的色彩模式
		iwp.setDestinationType(new ImageTypeSpecifier(colorModel, colorModel.createCompatibleSampleModel(16, 16)));
		// 开始打包图片，写入byte[]
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // 取得内存输出流
		IIOImage iIamge = new IIOImage(bufferedImage, null, null);
		// 此处因为ImageWriter中用来接收write信息的output要求必须是ImageOutput
		// 通过ImageIo中的静态方法，得到byteArrayOutputStream的ImageOutput
		writer.setOutput(ImageIO.createImageOutputStream(byteArrayOutputStream));
		writer.write(null, iIamge, iwp);

		// 获取压缩后的btye
		byte[] tempByte = byteArrayOutputStream.toByteArray();
		// 创建输出文件，outputPath输出文件路径，imgStyle目标文件格式（png）
		File outFile = new File(targetPath);
		FileOutputStream fos = new FileOutputStream(outFile);
		try {
		fos.write(tempByte);
		} finally {
			IOUtils.close(fos);
		}
	}
    
    /**
	 * 解析gif文件的每一帧到特定文件夹下(导出格式为png)
	 * <p>
	 * 实测导出的图片会失真
	 * </p>
	 * @param gifPath
	 * @param targetDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
    @Deprecated
	public static void splitGif(String gifPath, String targetDir) throws FileNotFoundException, IOException {
		splitGif(getFileImageInputStream(gifPath), targetDir);
	}
	
	/**
	 * 解析gif文件的每一帧到特定文件夹下(导出格式为png)
	 * <p>
	 * 实测导出的图片会失真
	 * </p>
	 * @param in
	 * @param targetDir
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	@Deprecated
	public static void splitGif(FileImageInputStream in, String targetDir) throws FileNotFoundException, IOException {
		try {
			FileImageOutputStream out = null;
			//加载gif解析工具
			ImageReaderSpi readerSpi = new GIFImageReaderSpi();
			GIFImageReader gifReader = (GIFImageReader) readerSpi.createReaderInstance();
			gifReader.setInput(in);
			
			//创建输出路径
			new File(targetDir).mkdirs();
			
			//解析每一帧
			int num = gifReader.getNumImages(true);
			
			ImageWriterSpi writerSpi = new PNGImageWriterSpi();
			PNGImageWriter writer = (PNGImageWriter) writerSpi.createWriterInstance();
			for (int i = 0; i < num; i++) {
				String targetPath = StringUtils.concat(targetDir, i, ".png");
				try {
					out = getFileImageOutputStream(targetPath);
					writer.setOutput(out);
					// 读取读取帧的图片
					writer.write(gifReader.read(i));
				} finally {
					IOUtils.close(out);
				}
				
			}
		} finally {
			IOUtils.close(in);
		}

	}
	
	public static FileImageInputStream getFileImageInputStream(String imgPath) throws FileNotFoundException, IOException {
		return new FileImageInputStream(new File(imgPath));
	}
	
	public static FileImageOutputStream getFileImageOutputStream(String imgPath) throws FileNotFoundException, IOException {
		return new FileImageOutputStream(new File(imgPath));
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
