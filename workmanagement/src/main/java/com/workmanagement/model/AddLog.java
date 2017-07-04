package com.workmanagement.model;

import java.sql.Clob;

public class AddLog {
		private String querycondition;  //查询条件
		private String operresult;  //查询结果
		private String time;  //查询时间
		private Integer opertypeId;  //操作类型
		private String orgId;  //查询机构
		private String userId;  //操作用户
		private Integer sys_area_id;  //操作区域id
		
		private String value;
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		@Override
		public String toString() { 
			return "AddLog [querycondition=" + querycondition + ", operresult=" + operresult + ", time=" + time
					+ ", opertypeId=" + opertypeId + ", orgId=" + orgId + ", userId=" + userId + ", sys_area_id="
					+ sys_area_id + "]";
		}
		public String getQuerycondition() {
			return querycondition;
		}
		public void setQuerycondition(String querycondition) {
			this.querycondition = querycondition;
		}
		public String getOperresult() {
			return operresult;
		}
		public void setOperresult(String operresult) {
			this.operresult = operresult;
		}
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public Integer getOpertypeId() {
			return opertypeId;
		}
		public void setOpertypeId(Integer opertypeId) {
			this.opertypeId = opertypeId;
		}
		public String getOrgId() {
			return orgId;
		}
		public void setOrgId(String orgId) {
			this.orgId = orgId;
		}
		public String getUserId() {
			return userId;
		}
		public void setUserId(String userId) {
			this.userId = userId;
		}
		public Integer getSys_area_id() {
			return sys_area_id;
		}
		public void setSys_area_id(Integer sys_area_id) {
			this.sys_area_id = sys_area_id;
		}
		
}
