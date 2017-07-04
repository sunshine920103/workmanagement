package com.workmanagement.util;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * Created by lzm on 2017/5/6.
 */
public class ZipUtil {

    /**
     * 待改进的方法:                          勿用!!!!!!!!!!!!
     *
     * @param filename 指定生成的压缩文件的名称
     * @param files
     * @param request
     * @param response
     */
    @Deprecated
    public static void createZip(String filename, List<String> files, HttpServletRequest request
            , HttpServletResponse response) {
        String path = SettingUtils.getCommonSetting("upload.file.temp.path");//读取存储目录
        String subDir = "/" + DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA), "yyyy-MM");//分级目录
        String newPath = path + subDir;//指定生成的压缩文件所存放的目录
        File file = new File(newPath);
        String newFileName = newPath + "/" + filename + ".zip";
        File zipFile = new File(newFileName);
        InputStream input = null;
        ZipOutputStream zipOut = null;
        OutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(zipFile);
            zipOut = new ZipOutputStream(fileOutputStream);
            zipOut.setEncoding("utf-8");
            for (String file1 : files) {
                File fi = new File(file1);
                input = new FileInputStream(fi);
                BufferedInputStream bis = new BufferedInputStream(input);
                zipOut.putNextEntry(new ZipEntry(file.getPath()));
                byte[] buf = new byte[1024];
                int temp = 0;
                while ((temp = bis.read(buf)) != -1) {
                    zipOut.write(buf, 0, temp);
                }
                IOUtils.closeQuietly(input);
                org.apache.commons.io.FileUtils.deleteQuietly(fi);
            }
//            DownLoadFile.downLoadFile(newFileName, filename, request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            org.apache.commons.io.FileUtils.deleteQuietly(zipFile);
            IOUtils.closeQuietly(fileOutputStream);
            IOUtils.closeQuietly(zipOut);
            IOUtils.closeQuietly(input);
        }
    }

    /**
     * 功能：把 sourceDir 目录下的所有文件进行 zip 格式的压缩，保存为指定 zip 文件
     *
     * @param sourceDir
     */
    public static void zip(String sourceDir, HttpServletRequest request, HttpServletResponse response) {
        String zipFile = SettingUtils.getCommonSetting("upload.file.temp.path");
        String filename = String.valueOf(System.currentTimeMillis());
        zipFile += "/" + UUID.randomUUID() + System.currentTimeMillis() + ".zip";
        OutputStream os = null;
        BufferedOutputStream bos = null;
        ZipOutputStream zos = null;
        try {
            os = new FileOutputStream(zipFile);
            bos = new BufferedOutputStream(os);
            zos = new ZipOutputStream(bos);
            File file = new File(sourceDir);
            String basePath = null;
            if (file.isDirectory()) {
                basePath = file.getPath();
            } else {//直接压缩单个文件时，取父目录
                basePath = file.getParent();
            }
            zipFile(file, basePath, zos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(zos);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(os);
        }
        try {
            DownLoadFile.downLoadFile(zipFile, filename, request, response);//下载 zip 文件
            org.apache.commons.io.FileUtils.deleteQuietly(new File(zipFile));//删除服务器上的 zip 文件
            org.apache.commons.io.FileUtils.deleteDirectory(new File(sourceDir));//删除服务器上的 excel 文件
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 功能：执行文件压缩成zip文件
     *
     * @param source
     * @param basePath 待压缩文件根目录
     * @param zos
     */

    private static void zipFile(File source, String basePath, ZipOutputStream zos) {
        File[] files;
        if (source.isDirectory()) {
            files = source.listFiles();
        } else {
            files = new File[1];
            files[0] = source;
        }
        String pathName;//存相对路径(相对于待压缩的根目录)
        InputStream is = null;
        BufferedInputStream bis = null;
        try {
            for (File file : files) {
                if (file.isDirectory()) {
                    pathName = file.getPath().substring(basePath.length() + 1) + "/";
                    zos.putNextEntry(new ZipEntry(pathName));
                    zipFile(file, basePath, zos);
                } else {
                    pathName = file.getPath().substring(basePath.length() + 1);
                    is = new FileInputStream(file);
                    bis = new BufferedInputStream(is);
                    zos.putNextEntry(new ZipEntry(pathName));
                    byte[] buf = new byte[1024];
                    int length;
                    while ((length = bis.read(buf)) > 0) {
                        zos.write(buf, 0, length);
                    }
                    IOUtils.closeQuietly(bis);
                    IOUtils.closeQuietly(is);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);
            IOUtils.closeQuietly(zos);
        }
    }
}
