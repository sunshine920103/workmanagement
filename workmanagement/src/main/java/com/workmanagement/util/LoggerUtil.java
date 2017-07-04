package com.workmanagement.util;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.log4j.Logger;

/**
 * @Description 日志记录类
 */
public class LoggerUtil {
	
    public static Logger log = Logger.getLogger(LoggerUtil.class);
    
    /**
     * 打印警告
     * 
     * @param obj
     */
    public static void warn(Object obj) {
        try{
            /*** 获取输出信息的代码的位置 ***/
            String location = "\r\n===========这里打印了一个警告==========\r\n";
            /*** 是否是异常  ***/
            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.warn(location + str);
            } else {
                log.warn(location + obj.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印信息
     * 
     * @param obj
     */
    public static void info(Object obj) {
        try{
            /*** 获取输出信息的代码的位置 ***/
        	 String location = "\r\n===========这里打印了一个信息==========\r\n";
            /*** 是否是异常  ***/
            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.info(location + str);
            } else {
                log.info(location + obj.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 打印错误
     * 
     * @param obj
     */
    public static void error(Object obj) {
        try{
            /*** 获取输出信息的代码的位置 ***/
        	 String location = "\r\n===========这里打印了一个错误==========\r\n";
            
            /*** 是否是异常  ***/
            if (obj instanceof Exception) {
                Exception e = (Exception) obj;
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw, true));
                String str = sw.toString();
                log.error(location + str);
            } else {
                log.error(location + obj.toString());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 打印错误
     * 
     * @param obj
     */
    public static void text(String info) {
    	log.info(info);
    }
    
    
}