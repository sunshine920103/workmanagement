package com.workmanagement.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.UUID;

/**
 * 文件上传
 *
 * @author danny
 */
public class UpLoadFile {
    /**
     * 上传到目录并返回并文件名
     *
     * @param file
     * @return
     */
    public static String upLoadFile(MultipartFile file) {
        String fileName = null;
        try {
            String originalFilename = file.getOriginalFilename();//原文件名
            String path = SettingUtils.getCommonSetting("upload.file.path");//读取存储目录
            String subDir = "/" + DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA), "yyyy-MM");//分级目录
            String newPath = path + subDir;
            FileUtils.forceMkdir(new File(newPath));//创建父目录
            String newFileName = "/" + UUID.randomUUID() + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));//新的文件名
            fileName = newPath + newFileName;//完整的路径
            File myfile = new File(fileName);
            //写文件
            file.transferTo(myfile);
        } catch (IOException e) {
            LoggerUtil.error(e);
        }
        return fileName;
    }
}
