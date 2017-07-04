package com.workmanagement.util;

import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * 生成并下载txt文件
 *
 * @author danny
 */
public class CreateTxtFile {
    /**
     * @param canUsedCode 是否启用了禁止使用组织机构代码
     */
    public static void creatTxtFile(HttpServletRequest request, HttpServletResponse response, IndexTb indexTb, List<IndexItemTb> indexItemTbs, final boolean canUsedCode) {
        String fileName = indexTb.getIndexName() + ".txt";//文件原名
        // 先读取原有文件内容，然后进行写入操作
        StringBuilder sbf = new StringBuilder();
        if (canUsedCode) {
            sbf.append("统一社会信用代码|组织机构代码|");
        } else {
            sbf.append("统一社会信用代码|");
        }
        if (CollectionUtils.isNotEmpty(indexItemTbs)) {
            for (IndexItemTb indexItemTb : indexItemTbs) {
                sbf.append(indexItemTb.getIndexItemName()).append("|");
            }
        }
        String sbfStr = sbf.toString();
        sbfStr = sbfStr.substring(0, sbfStr.lastIndexOf("|"));
        //设置文件MIME类型
        response.setContentType(request.getSession().getServletContext().getMimeType(fileName));
        try {
            fileName = new String(fileName.getBytes("gbk"), "ISO_8859_1");
        } catch (UnsupportedEncodingException e1) {
            LoggerUtil.error(e1);
        }
        //设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        OutputStream out = null;
        try {
            out = response.getOutputStream();
            IOUtils.write(sbfStr, out, "utf-8");
            out.flush();
        } catch (IOException e) {
            LoggerUtil.error(e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
