package com.workmanagement.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Created by lzm on 2017/3/11.
 */
public class DicExcelOut<T> {
    // 要生成的文件名
    private String title;
    // 随机生成的文件名
    private String newFileName;
    // 列名数组
    private String[] rowNames;
    // 属性名数组
    private String[] propertyNames;
    // 数据集
    private List<T> list;

    public DicExcelOut() {
    }

    /**
     * 导出集合数据
     * 合并第一列的
     *
     * @param request
     * @param response
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public String exportExcel(HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(title);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(25);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //为excel模板导出时
        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }

        // 向单元格里填充数据
        if (list != null) {
            List<Integer> li = new ArrayList<>();
            Integer oo = 1;
            for (int xxx = 1; xxx < list.size() + 1; xxx++) {//只循环遍历第一列
                //先在设置获取的行数数据
                Object clazz = list.get(xxx - 1);
                if (xxx == list.size()) {
                    li.add(oo);
                    break;
                }
                Object thisClazz = list.get(xxx);
                //再判断数据是否重复
                for (int i = 0; i < 1; i++) {
                    try {
                        String startName = BeanUtils.getProperty(clazz, propertyNames[i]);//获取这行数据
                        String thisName = BeanUtils.getProperty(thisClazz, propertyNames[i]);//获取下数据
                        if (StringUtils.equals(startName, thisName)) {//比较上一行和这一行的数据是否相等
                            oo += 1;//表示合并的格数
                        } else {//名字不等的话
                            li.add(oo);
                            oo = 1;
                        }
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                int rowcell = i + 1;//合并用来记录的
                int rowpage = rowcell;//记录合并的
                //只有第一列和第二列合并单元格
                if (i < 1) {
                    for (int n = 0; n < li.size(); n++) {//先把第一列合并出来
                        int length = li.get(n);//要合并的行数
                        int lastRow = rowpage + length - 1;//结束的行数
                        sheet.addMergedRegion(new CellRangeAddress(rowpage, lastRow, i, i));//先合并单元格，再创建列
                        rowpage += length;
                    }
                }
                if (i == 1) {
                    rowpage = rowcell - 1;
                    for (int n = 0; n < li.size(); n++) {//先把第一列合并出来
                        int length = li.get(n);//要合并的行数
                        int lastRow = rowpage + length - 1;//结束的行数
                        sheet.addMergedRegion(new CellRangeAddress(rowpage, lastRow, i, i));//先合并单元格，再创建列
                        rowpage += length;
                    }
                }
                row = sheet.createRow(rowcell);//从第二行开始创建行
                Object val = list.get(i);//获取到当前行的数据class
                for (int xx = 0; xx < propertyNames.length; xx++) {
                    Cell rowCell = row.createCell(xx);//创建这一列
                    String value = null;
                    try {
                        value = BeanUtils.getProperty(val, propertyNames[xx]);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    rowCell.setCellValue(StringUtils.isBlank(value) ? "" : value);
                    rowCell.setCellStyle(style);
                }
            }
        }
        return outExcel(wb, response, request);
    }

    /**
     * 导出用户行为日志
     * 合并第一列的
     *
     * @param request
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public String exportUserLog(HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(title);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(25);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }

        // 向单元格里填充数据
        String timess = "yyyy-MM-dd HH:mm:ss";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                row = sheet.createRow(i + 1);//从第二行开始创建行
                Object val = list.get(i);//获取到当前行的数据class
                for (int xx = 0; xx < propertyNames.length; xx++) {
                    Cell rowCell = row.createCell(xx);//创建这一列
                    String value = null;
                    try {
                        value = BeanUtils.getProperty(val, propertyNames[xx]);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    if (xx == 10) {
                        String type = "";
                        if (value != null) {
                            try {
                                Object oo = val.getClass().getMethod("get" + propertyNames[xx].substring(0, 1).toUpperCase() +
                                        propertyNames[xx].substring(1)).invoke(val);
                                type = DateFormatUtils.format((Date) oo, timess);
                            } catch (IllegalAccessException | InvocationTargetException e) {
                                e.printStackTrace();
                            }
                        }
                        rowCell.setCellValue(type);
                    } else if (xx == 11) {
                        String type = null;
                        if (value != null) {
                            switch (value) {
                                case "1": {
                                    type = "增加";
                                    break;
                                }
                                case "2": {
                                    type = "删除";
                                    break;
                                }
                                case "3": {
                                    type = "修改";
                                    break;
                                }
                                case "4": {
                                    type = "查询";
                                    break;
                                }
                                case "5": {
                                    type = "导入";
                                    break;
                                }
                                case "6": {
                                    type = "导出";
                                    break;
                                }
                                case "7": {
                                    type = "打印";
                                    break;
                                }
                                case "8": {
                                    type = "登陆";
                                    break;
                                }
                                default:
                                    type = "";
                                    break;
                            }
                        }
                        rowCell.setCellValue(type);
                    } else if (xx == 14) {
                        String type = null;
                        if (value != null) {
                            switch (value) {
                                case "false": {
                                    type = "失败";
                                    break;
                                }
                                case "true": {
                                    type = "成功";
                                    break;
                                }
                                default:
                                    type = "";
                                    break;
                            }
                        }
                        rowCell.setCellValue(StringUtils.isEmpty(type) ? "" : type);
                    } else {
                        rowCell.setCellValue(StringUtils.isEmpty(value) ? "" : value);
                    }
                    rowCell.setCellStyle(style);
                }
            }
        }
        return outExcel(wb, response, request);
    }

    /**
     * 导出用户行为日志
     * 合并第一列的
     *
     * @throws IllegalArgumentException
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    public String exportMap() throws NoSuchMethodException {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(title);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(25);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, rowNames.length - 1));//先合并单元格，再创建列
        row = sheet.createRow(0);
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(title);
        cell1.setCellStyle(style);

        row = sheet.createRow(1);
        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }

        // 向单元格里填充数据
        String timess = "yyyy-MM-dd";
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                Map<String, Object> map = (Map<String, Object>) list.get(i);
                row = sheet.createRow(i + 2);//从第三行开始创建行
                for (int xx = 0; xx < propertyNames.length; xx++) {
                    Cell rowCell = row.createCell(xx);//创建这一列
                    Object value = MapUtils.getObject(map, propertyNames[xx]);
                    if (StringUtils.equalsIgnoreCase("CODE_ORG", propertyNames[xx])) {
                        if (MapUtils.getObject(map, "CODE_CREDIT") == null) {
                            value = MapUtils.getObject(map, "CODE_ORG");
                        } else {
                            value = null;
                        }
                    }
                    String data = null;
                    if (value instanceof Date) {//如果是时间类型
                        data = DateFormatUtils.format((Date) value, timess);
                    } else {
                        if (value == null) {
                            data = null;
                        } else {
                            data = String.valueOf(value);
                        }
                    }
                    rowCell.setCellValue(data);
                    rowCell.setCellStyle(style);
                }
            }
        }
        return outFile(wb);
    }

    private String outFile(HSSFWorkbook wb) {
        String url = null;
        FileOutputStream out = null;
        try {
            String path = SettingUtils.getCommonSetting("upload.file.temp.path");//读取存储目录
            String subDir = "/" + DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA), "yyyy-MM");//分级目录
            String newPath = path + subDir;
            org.apache.commons.io.FileUtils.forceMkdir(new File(newPath));//创建父目录
            String newFileName = "/" + title + ".xls";//新的文件名
            path = newPath + newFileName;//完整的路径
            File myfile = new File(path);
            out = new FileOutputStream(myfile);
            wb.write(out);//先把数据写入文件
            url = newPath;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return url;
    }

    public String outInfo(HttpServletRequest request, HttpServletResponse response) {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(title);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(25);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }

        if (list != null) {
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                row = sheet.createRow(i + 1);//从第二行开始创建行
                Object val = list.get(i);//获取到当前行的数据class
                for (int xx = 0; xx < propertyNames.length; xx++) {
                    Cell rowCell = row.createCell(xx);//创建这一列
                    try {
                        String value = BeanUtils.getProperty(val, propertyNames[xx]);
                        rowCell.setCellValue(value);
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return outExcel(wb, response, request);
    }

    public void outString(HttpServletResponse response, HttpServletRequest request) {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(title);
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(100);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //为excel模板导出时
        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                row = sheet.createRow(i + 1);//从第二行开始创建行
                String val = (String) list.get(i);//获取到当前行的数据class
                Cell rowCell = row.createCell(0);//创建这一列
                rowCell.setCellValue(val);
            }
        }
        outExcel(wb, response, request);
    }

    public String outStringNotRespones() {
        // 声明一个工作薄
        HSSFWorkbook wb = new HSSFWorkbook();
        // 声明一个单子并命名
        Sheet sheet = wb.createSheet(String.valueOf(System.currentTimeMillis()) + UUID.randomUUID());
        // 给单子名称一个长度
        sheet.setDefaultColumnWidth(100);
        // 生成一个样式
        CellStyle style = wb.createCellStyle();
        // 创建第一行（表头）
        Row row = sheet.createRow(0);
        // 样式字体居中
        style.setAlignment(CellStyle.ALIGN_CENTER);//水平
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);//垂直
        // 给表头第一行一次创建单元格
        Cell cell = row.createCell(0);
        //设置单元格格式为文本格式
        CellStyle textStyle = wb.createCellStyle();
        DataFormat format = wb.createDataFormat();
        textStyle.setDataFormat(format.getFormat("@"));
        textStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为整数格式
        CellStyle numStyle = wb.createCellStyle();
        DataFormat numDf = wb.createDataFormat();
        numStyle.setDataFormat(numDf.getFormat("@"));
        numStyle.setDataFormat(numDf.getFormat("0"));//数据格式只显示整数
        numStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //设置单元格格式为小数格式
        CellStyle accuracyStyle = wb.createCellStyle();
        DataFormat accuracyStyleDf = wb.createDataFormat();
        accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
        accuracyStyle.setAlignment(CellStyle.ALIGN_CENTER);

        //为excel模板导出时
        for (int i = 0; i < rowNames.length; i++) {
            Cell cell2 = row.createCell(i);
            cell2.setCellValue(rowNames[i]);
            cell2.setCellStyle(style);
        }
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {//开始行数的添加数据
                row = sheet.createRow(i + 1);//从第二行开始创建行
                String val = (String) list.get(i);//获取到当前行的数据class
                Cell rowCell = row.createCell(0);//创建这一列
                rowCell.setCellValue(val);
            }
        }
        return outExcelNotResponse(wb);
    }


    private String outExcelNotResponse(HSSFWorkbook wb) {
        String url = null;
        FileOutputStream out = null;
        try {
            String path = SettingUtils.getCommonSetting("upload.file.path");//读取存储目录
            String subDir = "/" + DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA), "yyyy-MM");//分级目录
            String newPath = path + subDir;
            org.apache.commons.io.FileUtils.forceMkdir(new File(newPath));//创建父目录
            String newFileName = "/" + UUID.randomUUID() + System.currentTimeMillis() + ".xls";//新的文件名
            path = newPath + newFileName;//完整的路径
            File myfile = new File(path);
            out = new FileOutputStream(myfile);
            wb.write(out);//先把数据写入文件
            url = path;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(out);
        }
        return url;
    }

    private String outExcel(HSSFWorkbook wb, HttpServletResponse response, HttpServletRequest request) {
        String url = null;
        FileOutputStream out = null;
//        OutputStream outTwo = null;
        try {
            String path = SettingUtils.getCommonSetting("upload.file.path");//读取存储目录
            String subDir = "/" + DateFormatUtils.format(Calendar.getInstance(TimeZone.getDefault(), Locale.CHINA), "yyyy-MM");//分级目录
            String newPath = path + subDir;
            org.apache.commons.io.FileUtils.forceMkdir(new File(newPath));//创建父目录
            String newFileName = "/" + UUID.randomUUID() + System.currentTimeMillis() + ".xls";//新的文件名
            path = newPath + newFileName;//完整的路径
            File myfile = new File(path);
            out = new FileOutputStream(myfile);
            wb.write(out);//先把数据写入文件
            DownLoadFile.downLoadFile(path, title, request, response);
            url = path;

            // 导出
//            String path = SettingUtils.getCommonSetting("upload.file.path");
//            File myfile = new File(path);
//            if (!myfile.exists()) {
//                myfile.mkdirs();
//            }
//            // 新的名称
//            newFileName = "/" + UUID.randomUUID() + newFileName + ".xls";
//            out = new FileOutputStream(path + newFileName);
//            wb.write(out);
//            out.flush();
//            url = path + newFileName;
//            // 读取
//            File file = new File(path + newFileName);
//            //处理文件中文乱码
//            response.reset();
//            //设置文件MIME类型
//            response.setContentType(request.getSession().getServletContext().getMimeType(".xls"));
//            String filename = new String(title.getBytes("gbk"), "ISO_8859_1");
//            response.setHeader("Content-disposition", "attachment; filename=" + filename + ".xls");
//            outTwo = response.getOutputStream();
//            outTwo.write(org.apache.commons.io.FileUtils.readFileToByteArray(file));
//            outTwo.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            IOUtils.closeQuietly(outTwo);
            IOUtils.closeQuietly(out);
        }
        return url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getRowNames() {
        return rowNames;
    }

    public void setRowNames(String[] rowNames) {
        this.rowNames = rowNames;
    }

    public String[] getPropertyNames() {
        return propertyNames;
    }

    public void setPropertyNames(String[] propertyNames) {
        this.propertyNames = propertyNames;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public String getNewFileName() {
        return newFileName;
    }
}
