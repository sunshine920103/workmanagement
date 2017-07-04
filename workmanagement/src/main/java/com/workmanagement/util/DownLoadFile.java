package com.workmanagement.util;

import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件下载
 *
 * @author lzl
 */
public class DownLoadFile {
    public static void downLoadFile(String path, String newName, HttpServletRequest request, HttpServletResponse response) {
        newName += path.substring(path.indexOf("."));//比如：/opt/files/file/userUploadTextFile/719f7aef-a949-466e-847d-30d5940cc2221490875445826.txt
        //设置文件MIME类型
        response.reset();
        response.setContentType(request.getSession().getServletContext().getMimeType(path));
        try {
            newName = new String(newName.getBytes("gbk"), "ISO_8859_1");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        //设置Content-Disposition
        response.reset();
        response.setHeader("Content-Disposition", "attachment;filename=" + newName);
        InputStream in = null;
        OutputStream out = null;
        try {
            in = new FileInputStream(path);
            out = response.getOutputStream();
            IOUtils.copy(in, out);
            out.flush();
        } catch (IOException e) {
            LoggerUtil.error(e);
        } finally {
            IOUtils.closeQuietly(out);
            IOUtils.closeQuietly(in);
        }
    }
}
