package com.ag777.util.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import com.ag777.util.lang.IOUtils;
import com.ag777.util.lang.collection.ListUtils;

/**
 * 压缩工具类
 */

public class ZipUtils {

    private static final int BUFF_SIZE = 2024;

    /**
     * 防止压缩文件名中文乱码
     */
    private static final Charset ZIP_ENCODING = Charset.forName("GBK");

    /**
     * 私有的构造方法
     */
    private ZipUtils(){}

    /**
     * 解压
     * @param zipFilePath 压缩文件
     * @param unzipPath 解压路径
     * @return return true if success
     */
    public static boolean unzip(String zipFilePath, String unzipPath) {
    	ZipFile zipFile = null;
        try{
            zipFile = new ZipFile(zipFilePath);
            Enumeration<?> emu = zipFile.entries();
            
            while(emu.hasMoreElements()) {
                ZipEntry entry = (ZipEntry)emu.nextElement();
                if(entry.isDirectory()) {
                    new File(unzipPath + "/" + entry.getName()).mkdirs();
                    continue;
                }

                BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
                File file = new File(unzipPath + "/" + entry.getName());
                File parent = file.getParentFile();
                if(parent != null && (!parent.exists())) {
                    parent.mkdirs();
                }

                FileOutputStream fos = new FileOutputStream(file);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                IOUtils.write(bis, bos, BUFF_SIZE);
               
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
        	IOUtils.close(zipFile);
        }
    }
    
    /**
     * 压缩文件
     * @param srcFile 需要压缩的文件
     * @param zipFile 压缩后的文件(.zip后缀需要自己添加)
     * @param overwrite 当文件已存在时是否覆盖
     * @throws IOException
     */
    public static void zipFile(File srcFile, File zipFile, boolean overwrite) throws IOException {
        if (zipFile == null || srcFile == null) {
            throw new IllegalArgumentException("zipFile和srcFile不能为空!");
        }
        if (!overwrite && zipFile.exists()) {
            return;
        }
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }

        FileInputStream in = new FileInputStream(srcFile);
        ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile), ZIP_ENCODING);
        ZipEntry zipEntry = new ZipEntry(srcFile.getName());	//压缩包里面的文件名
        zipOutput.putNextEntry(zipEntry);
        IOUtils.write(in, zipOutput, BUFF_SIZE);
    }

    /**
     * 压缩一个文件。若同名文件存在，则覆盖之。
     * @param srcFile 需要压缩的文件
     * @param zipFile 压缩生成的文件
     * @throws IOException
     */
    public static void zipFile(File srcFile, File zipFile) throws IOException {
        zipFile(srcFile, zipFile, true);
    }

    /**
     * 与压缩一个文件。生成的文件名为【源文件名.zip】，若有同名文件，则覆盖之。
     * @param srcFile 需要压缩的文件
     * @throws IOException 压缩生成的文件
     */
    public static void zipFile(File srcFile) throws IOException {
        zipFile(srcFile, new File(srcFile.getAbsolutePath() + ".zip"), true);
    }

    /**
     * 压缩一个目录。
     * @param srcDir 需要压缩的目录
     * @param zipFile 压缩后的文件
     * @param overwrite 是否覆盖已存在文件
     * @throws IOException
     */
    public static void zipDirectory(File srcDir, File zipFile, boolean overwrite) throws IOException {
        if (zipFile == null || srcDir == null) {
            throw new IllegalArgumentException("zipFile和srcDir不能为空!");
        }
        if (!overwrite && zipFile.exists()) {
            return;
        }
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }

        ZipOutputStream zipOutput = new ZipOutputStream(new FileOutputStream(zipFile), ZIP_ENCODING);
        zipDirectory(zipOutput, srcDir, srcDir.getName());
        zipOutput.close();
    }

    /**
     * 压缩目录的辅助方法。递归检查子目录。
     * @param zipOutput zip输出流
     * @param file 当前文件
     * @param base 当前文件在压缩包里的绝对名称
     * @throws IOException
     */
    private static void zipDirectory(ZipOutputStream zipOutput, File file, String base) throws IOException {
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            zipOutput.putNextEntry(new ZipEntry(base + "/"));
            base = (base.length() == 0) ? "" : base + "/";
            for (File childFile: fileList) {
                zipDirectory(zipOutput, childFile, base + childFile.getName());
            }
        }
        else {
            zipOutput.putNextEntry(new ZipEntry(base));
            FileInputStream fileInputStream = new FileInputStream(file);
            int length;
            byte[] buffer = new byte[BUFF_SIZE];
            while ( (length = fileInputStream.read(buffer)) != -1) {
                zipOutput.write(buffer, 0, length);
                zipOutput.flush();
            }
            fileInputStream.close();
        }
    }

    /**
     * 压缩一个目录。如果存在同名文件，则会覆盖
     * @param srcDir 需要压缩 的目录
     * @param zipFile 压缩产生的文件
     * @throws IOException
     */
    public static void zipDirectory(File srcDir, File zipFile) throws IOException {
        zipDirectory(srcDir, zipFile, true);
    }

    /**
     * 压缩一个目录。压缩输出的文件名为(目录名.zip)
     * @param srcDir 需要压缩的目录
     * @throws IOException
     */
    public static void zipDirectory(File srcDir) throws IOException {
        zipDirectory(srcDir, new File(srcDir.getAbsolutePath()+".zip"), true);
    }

    /**
     * 对一个文件数组进行压缩。数组长度必须大于0
     * @param files 文件数组。长度必须大于0
     * @param zipFile 压缩后产生的文件
     * @param overwrite 如存在同名文件，是否覆盖
     * @throws IOException
     */
    public static void zipFiles(File[] files, File zipFile, boolean overwrite) throws IOException {
        if (ListUtils.isEmpty(files)) {
            throw new IllegalArgumentException("需要压缩的文件列表不能为空!");
        }
        if (!overwrite && zipFile.exists()) {
            return;
        }
        if (!zipFile.exists()) {
            zipFile.createNewFile();
        }

        ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(zipFile), ZIP_ENCODING);
        byte[] buffer = new byte[2048];
        int length;
        for (File file: files) {
            FileInputStream fileInputStream = new FileInputStream(file);
            zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
            while ((length = fileInputStream.read(buffer)) != -1) {
                zipOutputStream.write(buffer, 0, length);
                zipOutputStream.flush();
            }
            fileInputStream.close();
        }

        zipOutputStream.close();
    }

    /**
     * 对一个文件列表进行压缩。列表长度长度必须大于0
     * @param fileList 文件列表。长度必须大于0
     * @param zipFile 压缩后产生的文件
     * @param overwrite 如果已经存在同名文件，是否覆盖。
     * @throws IOException
     */
    public static void zipFiles(List<File> fileList, File zipFile, boolean overwrite) throws IOException {
        zipFiles(fileList.toArray(new File[fileList.size()]), zipFile, overwrite);
    }

    /**
     * 对一个文件列表进行压缩，列表长度长度必须大于0。如存在同名目标文件，则会覆盖它。
     * @param fileList 文件列表。长度必须大于0
     * @param zipFile  压缩后生成的文件
     * @throws IOException
     */
    public static void zipFiles(List<File> fileList, File zipFile) throws IOException {
        zipFiles(fileList.toArray(new File[fileList.size()]), zipFile, true);
    }

    /**
     * 对一个文件数组进行压缩，数组长度必须大于0。如存在同名目标文件，则会覆盖它。
     * @param files 文件数组。长度必须大于0
     * @param zipFile 压缩后生成的文件
     * @throws IOException
     */
    public static void zipFiles(File[] files, File zipFile) throws IOException {
        zipFiles(files, zipFile, true);
    }

    /**
     * 测试方法
     * @param args 参数
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        zipDirectory(new File("F:\\临时\\中文\\"));
    }
}