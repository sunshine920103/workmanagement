package com.workmanagement.model;

import java.io.Serializable;
import java.sql.Clob;
import java.util.Date;
import java.util.List;

/**
 * 机构管理
 * @author renyang
 *
 */
public class SysOrg implements Serializable {

	private static final long serialVersionUID = 5022461215360986609L;
	//对应数据库的字段名
	private Integer sys_org_id; //机构id
	private Integer sys_org_upid;//上级机构ID
	private String sys_org_financial_code;//金融机构编码，不为空
	private String sys_org_name;//机构名称，不为空
	private Integer sys_org_type_id;//机构类别ID，不为空
	private Integer sys_area_id;//所属区域ID
	private String sys_org_phone;//电话
	private String sys_org_address;//地址
	private String sys_org_representative; //法定代表人/负责人
	private String sys_org_finance_operator;//金融机构经办人
	private String sys_org_finance_operator_phone;//经办人联系电话
	private String sys_org_code;//组织机构代码
	private String sys_org_credit_code;//统一社会信用代码
	private String sys_org_licence;//许可证
	private String sys_org_reg_capital;//注册资本
	private Date sys_org_issuance_day;//实际开立日期
	private String sys_org_code_number;//代码证编号
	private String sys_org_reg_number;//登记号
	private String sys_org_notes;//备注
	private Integer sys_dic_id;//数据字典id
	private Boolean sys_org_used;//是否使用 0 否(默认) 1 是
	private Integer sys_org_current_query_times;//本机构本月查询次数
	private Integer sys_org_current_limit_query_times;//本机构本月限制查询次数
	private Integer sys_org_type;//0表示机构,1表示政府
	private String sys_org_service_center_call;//服务中心电话
	private String sys_org_service_center_name;//服务中心名称
	private String sys_org_credit_report; //信用报告底纹
	private String sys_org_logo;
	private Integer sys_org_affiliation_area_id;//所属区域市ID
	private Date sys_org_time;//时间
	private Integer sys_org_status;//状态 0为锁定 1为解锁
	
	private List<SysOrg> subSysOrg;
	private String sys_org_upname;
	private String sys_org_type_name;
	private String sys_org_address_area_name;
	private String sys_org_typeName;
	private String used;
	private String date;
	
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public String getSys_org_upname() {
		return sys_org_upname;
	}
	public void setSys_org_upname(String sys_org_upname) {
		this.sys_org_upname = sys_org_upname;
	}
	public String getSys_org_type_name() {
		return sys_org_type_name;
	}
	public void setSys_org_type_name(String sys_org_type_name) {
		this.sys_org_type_name = sys_org_type_name;
	}
	public String getSys_org_address_area_name() {
		return sys_org_address_area_name;
	}
	public void setSys_org_address_area_name(String sys_org_address_area_name) {
		this.sys_org_address_area_name = sys_org_address_area_name;
	}
	public List<SysOrg> getSubSysOrg() {
		return subSysOrg;
	}
	public void setSubSysOrg(List<SysOrg> subSysOrg) {
		this.subSysOrg = subSysOrg;
	}
	public Integer getSys_org_id() {
		return sys_org_id;
	}
	public void setSys_org_id(Integer sys_org_id) {
		this.sys_org_id = sys_org_id;
	}
	public Integer getSys_org_upid() {
		return sys_org_upid;
	}
	public void setSys_org_upid(Integer sys_org_upid) {
		this.sys_org_upid = sys_org_upid;
	}
	public String getSys_org_financial_code() {
		return sys_org_financial_code;
	}
	public void setSys_org_financial_code(String sys_org_financial_code) {
		this.sys_org_financial_code = sys_org_financial_code;
	}
	public String getSys_org_name() {
		return sys_org_name;
	}
	public void setSys_org_name(String sys_org_name) {
		this.sys_org_name = sys_org_name;
	}
	public Integer getSys_org_type_id() {
		return sys_org_type_id;
	}
	public void setSys_org_type_id(Integer sys_org_type_id) {
		this.sys_org_type_id = sys_org_type_id;
	}
	public Integer getSys_area_id() {
		return sys_area_id;
	}
	public void setSys_area_id(Integer sys_area_id) {
		this.sys_area_id = sys_area_id;
	}
	public String getSys_org_phone() {
		return sys_org_phone;
	}
	public void setSys_org_phone(String sys_org_phone) {
		this.sys_org_phone = sys_org_phone;
	}
	public String getSys_org_address() {
		return sys_org_address;
	}
	public void setSys_org_address(String sys_org_address) {
		this.sys_org_address = sys_org_address;
	}
	public String getSys_org_representative() {
		return sys_org_representative;
	}
	public void setSys_org_representative(String sys_org_representative) {
		this.sys_org_representative = sys_org_representative;
	}
	public String getSys_org_finance_operator() {
		return sys_org_finance_operator;
	}
	public void setSys_org_finance_operator(String sys_org_finance_operator) {
		this.sys_org_finance_operator = sys_org_finance_operator;
	}
	public String getSys_org_finance_operator_phone() {
		return sys_org_finance_operator_phone;
	}
	public void setSys_org_finance_operator_phone(String sys_org_finance_operator_phone) {
		this.sys_org_finance_operator_phone = sys_org_finance_operator_phone;
	}
	public String getSys_org_code() {
		return sys_org_code;
	}
	public void setSys_org_code(String sys_org_code) {
		this.sys_org_code = sys_org_code;
	}
	public String getSys_org_credit_code() {
		return sys_org_credit_code;
	}
	public void setSys_org_credit_code(String sys_org_credit_code) {
		this.sys_org_credit_code = sys_org_credit_code;
	}
	public String getSys_org_licence() {
		return sys_org_licence;
	}
	public void setSys_org_licence(String sys_org_licence) {
		this.sys_org_licence = sys_org_licence;
	}
	public String getSys_org_reg_capital() {
		return sys_org_reg_capital;
	}
	public void setSys_org_reg_capital(String sys_org_reg_capital) {
		this.sys_org_reg_capital = sys_org_reg_capital;
	}
	public Date getSys_org_issuance_day() {
		return sys_org_issuance_day;
	}
	public void setSys_org_issuance_day(Date sys_org_issuance_day) {
		this.sys_org_issuance_day = sys_org_issuance_day;
	}
	public String getSys_org_code_number() {
		return sys_org_code_number;
	}
	public void setSys_org_code_number(String sys_org_code_number) {
		this.sys_org_code_number = sys_org_code_number;
	}
	public String getSys_org_reg_number() {
		return sys_org_reg_number;
	}
	public void setSys_org_reg_number(String sys_org_reg_number) {
		this.sys_org_reg_number = sys_org_reg_number;
	}
	public String getSys_org_notes() {
		return sys_org_notes;
	}
	public void setSys_org_notes(String sys_org_notes) {
		this.sys_org_notes = sys_org_notes;
	}
	public Boolean getSys_org_used() {
		return sys_org_used;
	}
	public void setSys_org_used(Boolean sys_org_used) {
		this.sys_org_used = sys_org_used;
	}
	public Integer getSys_org_current_query_times() {
		return sys_org_current_query_times;
	}
	public void setSys_org_current_query_times(Integer sys_org_current_query_times) {
		this.sys_org_current_query_times = sys_org_current_query_times;
	}
	public Integer getSys_org_current_limit_query_times() {
		return sys_org_current_limit_query_times;
	}
	public void setSys_org_current_limit_query_times(Integer sys_org_current_limit_query_times) {
		this.sys_org_current_limit_query_times = sys_org_current_limit_query_times;
	}
	public Integer getSys_dic_id() {
		return sys_dic_id;
	}
	public void setSys_dic_id(Integer sys_dic_id) {
		this.sys_dic_id = sys_dic_id;
	}
	public Integer getSys_org_type() {
		return sys_org_type;
	}
	public void setSys_org_type(Integer sys_org_type) {
		this.sys_org_type = sys_org_type;
	}
	public String getSys_org_typeName() {
		return sys_org_typeName;
	}
	public void setSys_org_typeName(String sys_org_typeName) {
		this.sys_org_typeName = sys_org_typeName;
	}
	public String getSys_org_service_center_call() {
		return sys_org_service_center_call;
	}
	public void setSys_org_service_center_call(String sys_org_service_center_call) {
		this.sys_org_service_center_call = sys_org_service_center_call;
	}
	public String getSys_org_service_center_name() {
		return sys_org_service_center_name;
	}
	public void setSys_org_service_center_name(String sys_org_service_center_name) {
		this.sys_org_service_center_name = sys_org_service_center_name;
	}
	public String getSys_org_credit_report() {
		return sys_org_credit_report;
	}
	public void setSys_org_credit_report(String sys_org_credit_report) {
		this.sys_org_credit_report = sys_org_credit_report;
	}
	public String getSys_org_logo() {
		return sys_org_logo;
	}
	public void setSys_org_logo(String sys_org_logo) {
		this.sys_org_logo = sys_org_logo;
	}
	public Integer getSys_org_affiliation_area_id() {
		return sys_org_affiliation_area_id;
	}
	public void setSys_org_affiliation_area_id(Integer sys_org_affiliation_area_id) {
		this.sys_org_affiliation_area_id = sys_org_affiliation_area_id;
	}
	
	public Date getSys_org_time() {
		return sys_org_time;
	}
	public void setSys_org_time(Date sys_org_time) {
		this.sys_org_time = sys_org_time;
	}
	public Integer getSys_org_status() {
		return sys_org_status;
	}
	public void setSys_org_status(Integer sys_org_status) {
		this.sys_org_status = sys_org_status;
	}
	public SysOrg(Integer sys_org_id, Integer sys_org_upid, String sys_org_upname,String sys_org_financial_code, String sys_org_name,
			Integer sys_org_type_id,String sys_org_type_name, Integer sys_area_id, String sys_org_address_area_name,String sys_org_phone, String sys_org_address,
			String sys_org_representative, String sys_org_finance_operator, String sys_org_finance_operator_phone,
			String sys_org_code, String sys_org_credit_code, String sys_org_licence, String sys_org_reg_capital,
			Date sys_org_issuance_day, String sys_org_code_number, String sys_org_reg_number, String sys_org_notes,
			Integer sys_org_type, String sys_org_service_center_call,
			String sys_org_service_center_name) {
		super();
		this.sys_org_id = sys_org_id;
		this.sys_org_upid = sys_org_upid;
		this.sys_org_upname = sys_org_upname;
		this.sys_org_financial_code = sys_org_financial_code;
		this.sys_org_name = sys_org_name;
		this.sys_org_type_id = sys_org_type_id;
		this.sys_org_type_name = sys_org_type_name;
		this.sys_area_id = sys_area_id;
		this.sys_org_address_area_name=sys_org_address_area_name;
		this.sys_org_phone = sys_org_phone;
		this.sys_org_address = sys_org_address;
		this.sys_org_representative = sys_org_representative;
		this.sys_org_finance_operator = sys_org_finance_operator;
		this.sys_org_finance_operator_phone = sys_org_finance_operator_phone;
		this.sys_org_code = sys_org_code;
		this.sys_org_credit_code = sys_org_credit_code;
		this.sys_org_licence = sys_org_licence;
		this.sys_org_reg_capital = sys_org_reg_capital;
		this.sys_org_issuance_day = sys_org_issuance_day;
		this.sys_org_code_number = sys_org_code_number;
		this.sys_org_reg_number = sys_org_reg_number;
		this.sys_org_notes = sys_org_notes;
		this.sys_org_type = sys_org_type;
		this.sys_org_service_center_call = sys_org_service_center_call;
		this.sys_org_service_center_name = sys_org_service_center_name;
	}
	public SysOrg() {
		super();
	}
	

}
