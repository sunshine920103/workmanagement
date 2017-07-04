package com.workmanagement.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class DateFormatter
{
	
	public static final String DATE = "yyyy-MM-dd";
	public static final String DATE_TIME = "yyyy-MM-dd HH:mm:ss";
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(DATE);
	public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat(DATE_TIME);
	
	/** 
	 * 判断时间是否在时间段内 
	 *  
	 * @param date 
	 *            当前时间 yyyy-MM-dd HH:mm:ss 
	 * @param strDateBegin 
	 *            开始时间 00:00
	 * @param strDateEnd 
	 *            结束时间 00:05
	 * @return 在时间段内返回true
	 */  
	public static boolean isInDate(Date date, String strDateBegin, String strDateEnd) {  
		
		if(date==null || StringUtils.isBlank(strDateBegin) || StringUtils.isBlank(strDateEnd)){
			return false;
		}
		
	    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");  
	    String strDate = sdf.format(date);  
	    // 截取当前时间时分秒  
	    int strDateH = Integer.parseInt(strDate.substring(0, 2));  
	    int strDateM = Integer.parseInt(strDate.substring(3, 5));  
	    // 截取开始时间时分秒  
	    int strDateBeginH = Integer.parseInt(strDateBegin.substring(0, 2));  
	    int strDateBeginM = Integer.parseInt(strDateBegin.substring(3, 5));  
	    // 截取结束时间时分秒  
	    int strDateEndH = Integer.parseInt(strDateEnd.substring(0, 2));  
	    int strDateEndM = Integer.parseInt(strDateEnd.substring(3, 5));  
	    
	    if(strDateH >= strDateBeginH && strDateH <= strDateEndH){
	    	
	    	//判断开始时间和结束时间的小时是否一样
	    	if(strDateBeginH == strDateEndH){ //是
	    		
	    		//
	    		if(strDateH == strDateBeginH){
	    			if(strDateM >= strDateBeginM && strDateM <= strDateEndM){
	    				return true;
	    			}
	    		}else{
	    			return false;
	    		}
	    		
	    	}else{ //否
	    		
	    		if(strDateH == strDateBeginH){
	    			
	    			if(strDateM >= strDateBeginM){
	    				return true;
	    			}else{
	    				return false;
	    			}
	    			
	    		}else if(strDateH == strDateEndH){
	    			
	    			if(strDateM <= strDateEndM){
	    				return true;
	    			}else{
	    				return false;
	    			}
	    			
	    		}else{
	    			return true;
	    		}
	    		
	    	}
	    }else{
    		return false;
    	}
	    
		return false;
	} 
	
	/**
	 * 判断d1和d2是否是     同年月， 是返回true
	 * @param d1 数据时间
	 * @param d2 查询时间
	 * @return
	 * @throws ParseException
	 */
	public static boolean isThisYearMonth(Date d1, Date d2) throws ParseException{
		if(d1==null || d2==null){
			return false;
		}
		String s1 = formatByStr(d1, "yyyyMM");
		String s2 = formatByStr(d2, "yyyyMM");
		return s1.equals(s2);
	}
	
	/**
	 * 判断d1是否比d2   差一年并同月， 是返回true
	 * @param d1 数据时间
	 * @param d2 查询时间
	 * @return
	 * @throws ParseException
	 */
	public static boolean isLastYearMonth(Date d1, Date d2) throws ParseException{
		if(d1==null || d2==null){
			return false;
		}
		int y1 = Integer.parseInt(formatByStr(d1, "yyyy"));
		int m1 = Integer.parseInt(formatByStr(d1, "MM"));
	
		int y2 = Integer.parseInt(formatByStr(d2, "yyyy"))-1;
		int m2 = Integer.parseInt(formatByStr(d2, "MM"));
		
		return (y1==y2 && m1==m2);
	}
	
	/**
	 * 判断日期是否为今年（yyyy）的日期， 是返回true
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static boolean isThisYear(Date date) throws ParseException{
		if(date==null){
			return false;
		}
		String thisYear = formatByStr(new Date(), "yyyy");
		String other = formatByStr(date, "yyyy");
		return thisYear.equals(other);
	}
	
	/**
	 * 判断日期是否为去年（yyyy）的日期， 是返回true
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static boolean isLastYear(Date date) throws ParseException{
		
		if(date==null){
			return false;
		}
		
		String thisYear = formatByStr(new Date(), "yyyy");
		String other = formatByStr(date, "yyyy");
		
		int t = Integer.parseInt(thisYear) - 1;
		int o = Integer.parseInt(other);
		
		return t==o;
	}

	/**
	 * 将日期的时分秒转为 00:00:00
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date lowDate(Date date) throws ParseException{
		
		if(date==null){
			return null;
		}
		
		String lowDate = formatDate(date) + " 00:00:00";
		return parseByStr(lowDate,DATE_TIME);
	}

	/**
	 * 将日期的时分秒转为 23:59:59
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date hightDate(Date date) throws ParseException{
		
		if(date==null){
			return null;
		}
		
		String lowDate = formatDate(date) + " 23:59:59";
		return parseByStr(lowDate,DATE_TIME);
	}
	
	/**
	 * @param dt
	 * @return
	 */
	public static Date stringToDate(String dt) {
		
		if(StringUtils.isBlank(dt)){
			return null;
		}
		
		return stringToDate(dt, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date stringToDate(String dt, String format) {
		if (!StringUtils.isBlank(dt) && !dt.equals("null")) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			try {
				return formatter.parse(dt);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * @param dt
	 * @param formater
	 * @return
	 */
	public static String dateToString(Date dt, String formater) {
		if (StringUtils.isBlank(formater))
			formater = "yyyy-MM-dd HH:mm:ss";
		if (dt != null) {
			SimpleDateFormat formatter = new SimpleDateFormat(formater);
			return formatter.format(dt);
		}
		return null;
	}
	
	/**
	 * 获得一天的开始时间
	 * 
	 * @param date 指定日期
	 * @param cursor 指定日期的前后日期，如cursor为-1，则为date的前一天；为1，则为后一天
	 * @return
	 */
	public static long getStartTimestampInOneday(Date date, int cursor) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cursor != 0)
			cal.add(Calendar.DATE, cursor);
		String dateStr = DateFormatter.dateToString(cal.getTime(), "yyyy-MM-dd");
		return DateFormatter.stringToDate(dateStr + " 0:0:0").getTime();
	}
	
	/**
	 * 获得一天的结束时间
	 * 
	 * @param date 指定日期
	 * @param cursor 指定日期的前后日期，如cursor为-1，则为date的前一天；为1，则为后一天
	 * @return
	 */
	public static long getEndTimestampInOneday(Date date, int cursor) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (cursor != 0)
			cal.add(Calendar.DATE, cursor);
		String dateStr = DateFormatter.dateToString(cal.getTime(), "yyyy-MM-dd");
		return DateFormatter.stringToDate(dateStr + " 23:59:59").getTime();
	}
	
	/**
	 * 时间显示格式转换201212151235转为20121215
	 * @param updateTime2
	 * @return
	 */
	public static long updateTime(long updateTime2) {
		
		return updateTime2/10000;
	}
	/**
	 * 二级分类List组合String
	 * @param categorys
	 * @return
	 */
	public static String resourceList(List<String> resource) {
		String categorysAll = "";
		for(int i=0;i<6;i++){
			if(resource.size()>i){
				categorysAll+=	resource.get(i)+"/";
			}
			
		}
		if(categorysAll.length()>0){
			categorysAll=categorysAll.substring(0, categorysAll.length()-1);
		}
		if(resource.size()>5){
			categorysAll+="......";
		}
		return  categorysAll ;
	}

	
	public static String timestampToString(String timestamp, String formater) {
		if (StringUtils.isBlank(timestamp) || timestamp.equals("null"))
			return "";
		if (StringUtils.isBlank(formater))
			formater = "yyyy-MM-dd HH:mm:ss";
		Long tsl = Long.valueOf(timestamp);
		Timestamp ts = new Timestamp(tsl);
		SimpleDateFormat formatter = new SimpleDateFormat(formater);
		return formatter.format(ts);
	}
	
	/**
	 * 计算d1 到 d2 相差多少时间
	 * @param d1 未来的时间
	 * @param d2 现在的时间
	 * @return 数组下标 0 天 1 时 2 分 3 秒
	 */
	public static long[] dateDiff(Date d1, Date d2) throws ParseException {
		long nd = 1000*24*60*60;//一天的毫秒数
		long nh = 1000*60*60;//一小时的毫秒数
		long nm = 1000*60;//一分钟的毫秒数
		long ns = 1000;//一秒钟的毫秒数
		//获得两个时间的毫秒时间差异
		long diff = d1.getTime() - d2.getTime();
		long day = diff/nd;//计算差多少天
		long hour = diff%nd/nh;//计算差多少小时
		long min = diff%nd%nh/nm;//计算差多少分钟
		long sec = diff%nd%nh%nm/ns;//计算差多少秒
		return new long[]{day,hour,min,sec};
	}
	
	/**
	 * 判断 start 是否大于 end
	 * @param start
	 * @param end
	 * @return
	 * @throws ParseException 
	 */
	public static boolean startThanEnd(Date start, Date end) throws ParseException{
		return start.getTime() > end.getTime();
	}
	
	/**
	 * 获取指定分钟后的日期
	 * @param date
	 * @return
	 */
	public static long getMinute(Date date, Long minute) {
		minute = minute == null ? 0 : minute;
		long curren = date.getTime();
		curren += minute * 60 * 1000;
		return curren;
	}
	
	/**
	 * 获取指定分钟前的日期
	 * @param date
	 * @return
	 */
	public static long getPreMinute(Date date, Long minute) {
		minute = minute == null ? 0 : minute;
		long curren = date.getTime();
		curren -= minute * 60 * 1000;
		return curren;
	}
	
	/**
	 * 通过传入的日期格式  解析 字符串
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date parseByStr(String date, String format) throws ParseException{
		return new SimpleDateFormat(format).parse(date);
	}
	
	/**
	 * 通过传入的日期格式  格式化 日期
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static String formatByStr(Date date, String format){
		return new SimpleDateFormat(format).format(date);
	}
	
	/**
	 * 将传入日期格式化为  yyyy-MM-dd 的字符串
	 * @param date
	 * @return
	 */
	public static String formatDate(Date date){
		return FORMAT_DATE.format(date);
	}

	/**
	 * 将传入日期格式化为  yyyy-MM-dd HH:mm:ss 的字符串
	 * @param date
	 * @return
	 */
	public static String formatDateTime(Date date){
		return FORMAT_DATE_TIME.format(date);
	}
	
	/**
	 * 将传入的date解析为format格式的日期
	 * @param date
	 * @param format
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String date, String format) throws ParseException{
		if(StringUtils.isBlank(date) || StringUtils.isBlank(format)){
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(date);
	}
	
	/**
	 * 将传入的字符串解析为 yyyy-MM-dd 的日期
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDate(String date) throws ParseException{
		if(StringUtils.isBlank(date)){
			return null;
		}
		return FORMAT_DATE.parse(date);
	}

	/**
	 * 将传入的字符串解析为 yyyy-MM-dd HH:mm:ss 的日期
	 * @param date
	 * @return
	 * @throws ParseException
	 */
	public static Date formatDateTime(String date) throws ParseException{
		return FORMAT_DATE_TIME.parse(date);
	}
	
	/**
	 * 获取指定日期指定天数后的日期
	 * @param date 指定的时间
	 * @param later 指定的天数
	 * @return
	 */
	public static Date getLaterDay(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current + later * 24 * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期指定天数前的日期
	 * @param date 指定的时间
	 * @param later 指定的天数
	 * @return
	 */
	public static Date getPreviouslyDay(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current - later * 24 * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期指定小时后的日期
	 * @param date 指定的时间
	 * @param later 指定的小时
	 * @return
	 */
	public static Date getLaterHour(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current + later * 60 * 60 * 1000);
	}

	/**
	 * 获取指定日期指定小时前的日期
	 * @param date 指定的时间
	 * @param later 指定的小时
	 * @return
	 */
	public static Date getPreviouslyHour(Date date, Long later){
		later = later == null ? 0 : later;
		long current = date.getTime();
		return new Date(current - later * 60 * 60 * 1000);
	}
	
	/**
	  * 得到本周周一
	  * @return 
	  */
	 public static Date getMondayOfWeek() {
		 Calendar c = Calendar.getInstance();
		 int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		 if (day_of_week == 0){
			 day_of_week = 7;
		 }
		 c.add(Calendar.DATE, -day_of_week + 1);
		 return c.getTime();
	 }
	
	/**
	 * 获取指定日期是星期几
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  //一周第一天是否为星期天
		  boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
		  //获取周几
		  int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		  //若一周第一天为星期天，则-1
		  if (isFirstSunday) {
		  		weekDay = weekDay - 1;
			  	if (weekDay == 0) {
			  		weekDay = 7;
			  	}
		  }
		  
		  return weekDay;
	}
	
	/**
	 * 获取指定日期的本周第一天
	 * @param date
	 * @return
	 */
	public static Date getNowWeekMonday(Date date) {    
		Calendar cal = Calendar.getInstance();    
        cal.setTime(date);    
        cal.add(Calendar.DAY_OF_MONTH, -1); //解决周日会出现 并到下一周的情况    
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);        
        return cal.getTime();    
    }
	
	/**
	  * 得到本周周日
	  * @return 
	  */
	 public static Date getSundayOfWeek() {
		 Calendar c = Calendar.getInstance();
		 int day_of_week = c.get(Calendar.DAY_OF_WEEK) - 1;
		 if (day_of_week == 0){
			 day_of_week = 7;
	 	 }
		 c.add(Calendar.DATE, -day_of_week + 7);
		 return c.getTime();
	 }
	 
	 /**
	 * 获取当前月的第一天
	 * @return
	 */
	public static Date getFirstDayOfMonth(){
		Calendar c = Calendar.getInstance();    
        c.add(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天 
        return c.getTime();
	}

	/**
	 * 获取当前月的最后一天
	 * @return
	 */
	public static Date getLastDayOfMonth(){
		Calendar ca = Calendar.getInstance();    
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));  
		return ca.getTime();
	}
	
	/**
	 * 获取指定日期是星期几
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date) {
		  Calendar cal = Calendar.getInstance();
		  cal.setTime(date);
		  //一周第一天是否为星期天
		  boolean isFirstSunday = (cal.getFirstDayOfWeek() == Calendar.SUNDAY);
		  //获取周几
		  int weekDay = cal.get(Calendar.DAY_OF_WEEK);
		  //若一周第一天为星期天，则-1
		  if (isFirstSunday) {
		  		weekDay = weekDay - 1;
			  	if (weekDay == 0) {
			  		weekDay = 7;
			  	}
		  }
		  return weekDay;
	}
	
	/** 
     * 获取某年最后一天日期 
     * @param year 年份 
     * @return Date 
     */  
    public static Date getYearLast(int year){  
        Calendar calendar = Calendar.getInstance();  
        calendar.clear();  
        calendar.set(Calendar.YEAR, year);  
        calendar.roll(Calendar.DAY_OF_YEAR, -1);  
        Date currYearLast = calendar.getTime();  
          
        return currYearLast;  
    }  
}
