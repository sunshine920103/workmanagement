package com.workmanagement.timer;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.workmanagement.model.SysOrg;
import com.workmanagement.model.SysOtherManage;
import com.workmanagement.service.CompanyListFilterService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.service.SysOtherManageService;
@Component
public class SysAutoTimer implements SysAutoTimerService {
	 
	@Autowired
	SysOtherManageService SysOtherManageService;
	@Autowired
	SysOrgService sysOrgService;
	@Autowired
	CompanyListFilterService companyListFilterService;
	/**
	 * 定时执行
	 * 每月最后一天晚上11点执行
	 */
	@Scheduled(cron="0 0 1 1 * ?")
//	@Scheduled(cron ="0 */1 * * * ?")
	@Override
	public void show() throws Exception{
			List<SysOrg> org = sysOrgService.queryAll();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Map<String, Object> map = new HashMap<>();
			Date date = new Date();
			String newDate = sdf.format(date);
			for (SysOrg sysOrg : org) {

				if(sysOrg.getSys_org_upid()!=null){
					SysOtherManage s = SysOtherManageService.querySysOtherManageAreaId(sysOrg.getSys_org_affiliation_area_id());
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(sysOrg.getSys_org_time());
					calendar.add(calendar.MONDAY, +s.getMonthLimit());
					java.util.Date resultDate = calendar.getTime(); // 结果
					String oldDate = sdf.format(resultDate);
					if(StringUtils.isNumeric(oldDate) && StringUtils.isNumeric(newDate)){
						if(Integer.parseInt(newDate) >Integer.parseInt(oldDate)){
							SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
							calendar.add(calendar.MONDAY, -s.getMonthLimit());
							java.util.Date resultDate1 = calendar.getTime(); // 结果
							map.put("begin", sdf1.format(resultDate1)+"-00.00.00.000");
							map.put("end", sdf1.format(date)+"-23.59.59.999");
							map.put("orgId", sysOrg.getSys_org_id());
							Integer num = companyListFilterService.queryNum(map);
							if(num.intValue() == 0){
								sysOrg.setSys_org_status(0);
							}else{
								sysOrg.setSys_org_current_query_times(0);
								sysOrg.setSys_org_current_limit_query_times(new Long(Math.round(num*s.getMultipleLimit()/s.getMonthLimit())).intValue());
							}
							sysOrgService.saveInstitutions(sysOrg);
						}else{
							sysOrg.setSys_org_current_query_times(0);
							sysOrgService.saveInstitutions(sysOrg);
						}
					}
					
				}
			}
		
	}
}
