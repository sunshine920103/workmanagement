package com.workmanagement.timer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.workmanagement.model.ReportTaskPushList;
import com.workmanagement.model.ReportTaskPushSet;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.ReportExcelTemplateService;
import com.workmanagement.service.ReportTaskPushListService;
import com.workmanagement.service.ReportTaskPushSetService;
import com.workmanagement.service.ReportedDeleteService;
import com.workmanagement.util.RedisUtil;

import redis.clients.jedis.Jedis;

@Controller
public class AutoTimer {
	@Autowired
	private ReportTaskPushSetService reportTaskPushSetService;
	@Autowired
	private ReportTaskPushListService reportTaskPushListService;
	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	@Autowired
	private ReportedDeleteService reportedDeleteService;
	protected Logger log = Logger.getLogger(AutoTimer.class);
	public AutoTimer() {

	}

	/**
	 * 定时执行
	 * 每天24点刷新任务
	 * @throws Exception 
	 */
	public void run() throws Exception {
		Jedis jedis = RedisUtil.getJedis();
		jedis.flushAll();
		List<ReportTaskPushList> queryList=reportTaskPushListService.queryList();
		for (int i = 0; i < queryList.size(); i++) {
			Integer type=reportTaskPushSetService.queryReportTaskPushSetById(queryList.get(i).getReportTaskPushSetId()).getReportTaskPushSetType();
			String indexCode=null;
			String cycle=reportTaskPushSetService.queryReportTaskPushSetById(queryList.get(i).getReportTaskPushSetId()).getReportTaskPushSetCycle();
			SimpleDateFormat  df=new SimpleDateFormat ("yyyy-MM-dd");
			Date date=queryList.get(i).getReportTaskPushListEndTime();
			String endTime=df.format(date);
			Date parse=null;
			String reportTaskPushEndTimeStr=df.format(date);
			String reportTaskPushEndTimeDay = reportTaskPushEndTimeStr.substring(reportTaskPushEndTimeStr.lastIndexOf("-")+1);// 几日
			if (cycle.indexOf("季") != -1) {
				String setDay = cycle.substring(cycle.indexOf("季") + 3, cycle.indexOf("日"));// 几日
					if(Integer.parseInt(setDay)>Integer.parseInt(reportTaskPushEndTimeDay)){
						setDay=reportTaskPushEndTimeDay;
					}
					String starDate=reportTaskPushEndTimeStr.substring(0, reportTaskPushEndTimeStr.lastIndexOf("-")+1)+setDay;
					parse=df.parse(starDate);
			} else if (cycle.indexOf("单") != -1) {
				String setDay = cycle.substring(cycle.indexOf("次") + 1, cycle.indexOf("日"));// 几日
				if(Integer.parseInt(setDay)>Integer.parseInt(reportTaskPushEndTimeDay)){
					setDay=reportTaskPushEndTimeDay;
				}
				String starDate=reportTaskPushEndTimeStr.substring(0, reportTaskPushEndTimeStr.lastIndexOf("-")+1)+setDay;
				parse=df.parse(starDate);
			} else if (cycle.indexOf("月") != -1) {
				String setDay = cycle.substring(cycle.indexOf("月") + 1, cycle.indexOf("日"));// 几日
				if(Integer.parseInt(setDay)>Integer.parseInt(reportTaskPushEndTimeDay)){
					setDay=reportTaskPushEndTimeDay;
				}
				String starDate=reportTaskPushEndTimeStr.substring(0, reportTaskPushEndTimeStr.lastIndexOf("-")+1)+setDay;
				parse=df.parse(starDate);
			}
			String beginTime=df.format(parse);
			Integer orgId=queryList.get(i).getSysOrgId();
			Integer tempLateId= reportTaskPushSetService.queryReportTaskPushSetById(queryList.get(i).getReportTaskPushSetId()).getReportTaskPushSetTempLateId();
			if(type==0){
				indexCode=indexTbService.queryById(tempLateId).getIndexCode();
			}
			if(type==1){
				Integer indexid=reportExcelTemplateService.queryReportExcelTemplateById(tempLateId).getIndexId();
				indexCode=indexTbService.queryById(indexid).getIndexCode();
			}
			String sql="SELECT * FROM "+indexCode+"_tb ctb WHERE ctb.sys_org_id="+orgId+" AND ctb.record_date BETWEEN '"+beginTime+"' AND '"+endTime+"'";
			Map<String, Object> sqlMap = new HashMap<String, Object>();
			sqlMap.put("sql", sql);
			List<Map<String, Object>> indexCodeList=reportedDeleteService.queryResult(sqlMap);
			if(indexCodeList!=null&&indexCodeList.size()>0){
				queryList.get(i).setReportTaskPushStatus(1);
				reportTaskPushListService.updateOrSave(queryList.get(i));
			}else{
				if(daysBetween(new Date(), date)<=-1){
					queryList.get(i).setReportTaskPushStatus(2);
					reportTaskPushListService.updateOrSave(queryList.get(i));
				}else{
					queryList.get(i).setReportTaskPushStatus(0);
					reportTaskPushListService.updateOrSave(queryList.get(i));
				}
			}
		}
		List<ReportTaskPushList> queryReportTaskPushLists = reportTaskPushListService.queryReportTaskPushListByStatus(0);// 查询未完成的任务
		for (int i = 0; i < queryReportTaskPushLists.size(); i++) {
			try {
				Integer setId=queryReportTaskPushLists.get(i).getReportTaskPushSetId();
				ReportTaskPushSet reportTaskPushSet=reportTaskPushSetService.queryReportTaskPushSetById(setId);
				updateTashPushList(queryReportTaskPushLists.get(i),reportTaskPushSet);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		List<ReportTaskPushSet> reportTaskPushSets= reportTaskPushSetService.queryReportTaskPushSetsAll();
		for (ReportTaskPushSet reportTaskPushSet : reportTaskPushSets) {
			if(reportTaskPushSet.getReportTaskPushSetStatus()==1){
				continue;
			}
			if(reportTaskPushSet.getReportTaskPushSetEndTime()!=null){
				if (new Date().getTime()>reportTaskPushSet.getReportTaskPushSetEndTime().getTime()) {//是否逾期
					String cycle = reportTaskPushSet.getReportTaskPushSetCycle();//周期
					Date reportTaskPushEndTime = reportTaskPushSet.getReportTaskPushSetEndTime();// 截止时间
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					int daysBetween = daysBetween(new Date(), reportTaskPushEndTime);//比较天数差
					String reportTaskPushEndTimeStr=sdf.format(reportTaskPushEndTime);
					//任务的末尾天数
					String reportTaskPushEndTimeDay = reportTaskPushEndTimeStr.substring(reportTaskPushEndTimeStr.lastIndexOf("-")+1);// 几日
					Date parse = null;
					if (cycle.indexOf("季") != -1) {
						String setDay = cycle.substring(cycle.indexOf("-") + 1, cycle.lastIndexOf("日"));// 几日
						if (daysBetween == -1) {// 截止日期过后一天时，重新赋值截止日期
							parse = subMonth(reportTaskPushEndTime, 3);
							String parseStr=sdf.format(parse);
							if (! setDay.equals(reportTaskPushEndTimeDay)) {//如果末尾天数不相等则按照setDay
								String newDateStr =parseStr.substring(0, parseStr.lastIndexOf("-")+1)+setDay;
								parse=sdf.parse(newDateStr);
							}
							reportTaskPushSet.setReportTaskPushSetEndTime(parse);
							reportTaskPushSetService.updateOrSave(reportTaskPushSet);
							String [] ids=reportTaskPushSet.getSysOrgExcuteIds().split(",");
							addTashPushList(ids,reportTaskPushSet);
						}
					} /*else if (cycle.indexOf("周") != -1) {
						//判断是否为当周第一天
						if (daysBetween == -1) {
							parse = subDay(reportTaskPushEndTime, 7);
							reportTaskPushSet.setReportTaskPushSetEndTime(parse);
							reportTaskPushSetService.updateOrSave(reportTaskPushSet);
							String [] ids=reportTaskPushSet.getSysOrgExcuteIds().split(",");
							addTashPushList(ids,reportTaskPushSet);
						}
					}*/ else if (cycle.indexOf("月") != -1) {
						String setDay = cycle.substring(cycle.indexOf("-") + 1, cycle.lastIndexOf("日"));// 几日
						//判断是否为当月第一天
						if (daysBetween == -1) {
							parse = subMonth(reportTaskPushEndTime, 1);
							String parseStr=sdf.format(parse);
							if (! setDay.equals(reportTaskPushEndTimeDay)) {//如果末尾天数不相等则按照setDay
								String newDateStr =parseStr.substring(0, parseStr.lastIndexOf("-")+1)+setDay;
								parse=sdf.parse(newDateStr);
							}
							reportTaskPushSet.setReportTaskPushSetEndTime(parse);
							reportTaskPushSetService.updateOrSave(reportTaskPushSet);
							String [] ids=reportTaskPushSet.getSysOrgExcuteIds().split(",");
							addTashPushList(ids,reportTaskPushSet);
						}
					}
				}
			}
		}
	}
	private void updateTashPushList(ReportTaskPushList reportTaskPushList,ReportTaskPushSet reportTaskPushSet) throws ParseException {
		
		String cycle = reportTaskPushSet.getReportTaskPushSetCycle();//周期
		Date reportTaskPushEndTime = reportTaskPushSet.getReportTaskPushSetEndTime();// 截止时间
		if (reportTaskPushEndTime==null) {
			return;
		}
		Integer daysBetween=daysBetween(new Date(),reportTaskPushEndTime);
			if (cycle.indexOf("季") != -1) {
				//判断是否为当月第一天
				if (daysBetween==-1) {//是否逾期
					reportTaskPushList.setReportTaskPushStatus(2);
					reportTaskPushListService.updateOrSave(reportTaskPushList);
				}
			} else if (cycle.indexOf("单") != -1) {
				//判断是否为当月第一天
				if (daysBetween==-1) {//是否逾期
					reportTaskPushList.setReportTaskPushStatus(2);
					reportTaskPushListService.updateOrSave(reportTaskPushList);
				}
			} else if (cycle.indexOf("月") != -1) {
				//判断是否为当月第一天
				if (daysBetween==-1) {//是否逾期
					reportTaskPushList.setReportTaskPushStatus(2);
					reportTaskPushListService.updateOrSave(reportTaskPushList);
				}
			}
			
	}

	/**
	 * 比较时间相差的天数 大于 逾期 小于等于 未逾期
	 */
	public static int daysBetween(Date smdate, Date bdate) {
		Calendar cal = Calendar.getInstance();
		long time1 = 0;
		long time2 = 0;
		try {
			cal.setTime(smdate);
			time1 = cal.getTimeInMillis();
			cal.setTime(bdate);
			time2 = cal.getTimeInMillis();
		} catch (Exception e) {
			e.printStackTrace();
		}
		long between_days = (time2 - time1) / (1000 * 3600 * 24);
		return Integer.parseInt(String.valueOf(between_days));
	}

	/**
	 * 根据日期获得星期
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfDate(Date date) {
		int[] weekDaysCode = { 7, 1, 2, 3, 4, 5, 6 };// 0表示周日
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		return weekDaysCode[intWeek];
	}
	
	/**
	 * 日期中该月最后一天日期
	 * 
	 * @param args
	 */
	public static int getLastDay(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String format = sdf.format(date);
		String year = format.substring(0, format.indexOf("-"));
		format = format.substring(format.indexOf("-") + 1);
		String month = format.substring(0, format.indexOf("-"));
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		cal.set(Calendar.MONTH, Integer.parseInt(month) - 1);// 月份0开始算
		int dateOfMonth = cal.getActualMaximum(Calendar.DATE);
		return dateOfMonth;
	}
	
	/**
	 * 传入具体日期 ，返回具体日期加x个月。
	 */
	public static Date subMonth(Date date, int month) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.MONTH, +month);
		Date dt1 = rightNow.getTime();
		return dt1;
	}
	/**
	 * 传入具体日期 ，返回具体日期加x日。
	 */
	public static Date subDay(Date date, int day) {
		Calendar calendar = new GregorianCalendar();
		calendar.setTime(date);
		calendar.add(calendar.DATE, day);// 把日期往后增加一天.整数往后推,负数往前移动
		date = calendar.getTime(); // 这个时间就是日期往后推一天的结果
		return date;
	}
	public int getWeekDay(String weekDay) {
		int week = 0;
		if (weekDay.equals("日")) {
			week = 7;
		} else if (weekDay.equals("一")) {
			week = 1;
		} else if (weekDay.equals("二")) {
			week = 2;
		} else if (weekDay.equals("三")) {
			week = 3;
		} else if (weekDay.equals("四")) {
			week = 4;
		} else if (weekDay.equals("五")) {
			week = 5;
		} else if (weekDay.equals("六")) {
			week = 6;
		}
		return week;
	}
	public void addTashPushList(String[] orgIds, ReportTaskPushSet reportTaskPushSet) throws ParseException {
		for (int i = 0; i < orgIds.length; i++) {
			// 为任务列表添加值
			ReportTaskPushList reportTaskPushList = new ReportTaskPushList();
			reportTaskPushList.setReportTaskPushSetId(reportTaskPushSet.getReportTaskPushSetId());
			reportTaskPushList.setReportTaskPushListEndTime(reportTaskPushSet.getReportTaskPushSetEndTime());
			reportTaskPushList.setSysOrgId(Integer.parseInt(orgIds[i].trim()));
			reportTaskPushList.setReportTaskPushStatus(0);
			reportTaskPushListService.updateOrSave(reportTaskPushList);
		}
	}
}
