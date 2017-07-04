package com.workmanagement.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.workmanagement.dao.ReportExcelTemplateDao;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.*;
import com.workmanagement.service.*;
import com.workmanagement.util.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequestMapping("/admin/reportIndex")
public class ReportIndexController {

    @Autowired
    private ReportExcelTemplateService reportExcelTemplateService;
    @Autowired
    private SysOrgService sysOrgService;
    @Autowired
    private ReportIndexService reportIndexService;
    @Autowired
    private IndexTbService indexTbService;
    @Autowired
    private ExcelReportService excelReportService;
    @Autowired
    private SysUserLogService sysUserLogService;
    @Autowired
    private SysAreaService sysAreaService;
    @Autowired
    private SysRoleService sysRoleService;
    @Resource(name = "subDataGet")
    private SubDataGet subDataGet;
    @Autowired
    private ReportExcelTemplateDao reportExcelTemplateDao;
    @Autowired
    private ManualEntryService manualEntryService;
    @Autowired
    private RelateInfoService relateInfoService;
    @Autowired
    private SysOrgTypeService sysOrgTypeService;

    /**
     * 主页面，显示按钮、已报数据
     *
     * @throws Exception
     */
    @RequestMapping(value = "list")
    public String list(Model model, HttpServletRequest request) throws Exception {
        //1、人行报送人员可以查看本区域及下级区域其他人报送的EXCEL文件，并支持下载
        //2、金融机构报送人员可以查看本机构及下级机构其他人报送的EXCEL文件，并支持下载
        PageSupport ps = PageSupport.initPageSupport(request);
        List<ReportIndex> list = new ArrayList<>();
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SysRole role = sysRoleService.querySystemRoleById(userDetails.getSys_role_id());
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        String areaKey = RedisKeys.SYS_AREA_UP + so.getSys_area_id();
        SysArea area = RedisUtil.getObjData(areaKey, SysArea.class);
        if (area == null) {
            area = sysAreaService.queryParentAreasById(so.getSys_area_id());
            RedisUtil.setData(areaKey, area);
        }
        if (userDetails.getSys_role_id() == 1) {//超级管理员
            list = reportIndexService.queryReportIndexs(ps);
        } else if (role.getSys_role_type() == 6) {//金融机构报送员
            List<Integer> ids = new ArrayList<>();
            subDataGet.getAllOrgIds(so, ids);
            list = reportIndexService.getReportsByMethodAndOrgIds(ps, ReportIndexDao.EXCEL_SUBMIT, ids);
        } else if (role.getSys_role_type() == 3) {//人行报送员
            List<Integer> areaIds = sysAreaService.getAllSubAreaIds(area.getSysAreaId());
            List<Integer> ids = sysOrgService.getSysOrgIdsByAreaIds(areaIds);
            list = reportIndexService.getReportsByMethodAndOrgIds(ps, ReportIndexDao.EXCEL_SUBMIT, ids);
        }
        // 报送信息
        model.addAttribute("list", list);
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询管辖地区及子地区的ID
        List<Integer> areaIds = new ArrayList<>();
        List<Integer> idup = sysAreaService.getAllUpAreaIds(area.getSysAreaId());
        List<Integer> idsub = sysAreaService.getAllSubAreaIds(area.getSysAreaId());
        areaIds.addAll(idup);
        areaIds.addAll(idsub);
        Set<Integer> setAreaIds = new HashSet<>();
        setAreaIds.addAll(areaIds);
        map.put("areaId", setAreaIds);
        List<ReportExcelTemplate> reportExcelTemplateList = DataUtil.isEmpty(reportExcelTemplateService.queryReportExcelTemplateList(map, null));
        for (int i = 0; i < reportExcelTemplateList.size(); i++) {
            if (reportExcelTemplateList.get(i).getSysAreaId() != 1) {
                SysArea area1 = sysAreaService.queryAreaById(reportExcelTemplateList.get(i).getSysAreaId());
                String name = area1.getSysAreaName() + "-" + reportExcelTemplateList.get(i).getReportExcelTemplateName();
                reportExcelTemplateList.get(i).setReportExcelTemplateName(name);
            }
        }
        //模版
        model.addAttribute("Tlist", reportExcelTemplateList);
        return "reportIndex/list";
    }


    /**
     * 新增报送页面
     */
    @RequestMapping(value = "/add")
    public String add(Model model) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        // 获取管辖区域
        String areaKey = RedisKeys.SYS_AREA_UP + so.getSys_area_id();
        SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
        if (sa == null) {
            sa = sysAreaService.queryParentAreasById(so.getSys_area_id());
            RedisUtil.setData(areaKey, sa);
        }
        Map<String, Object> map = new HashMap<String, Object>();
        // 查询管辖地区及子地区的ID
        List<Integer> areaIds = new ArrayList<>();
        List<Integer> idup = sysAreaService.getAllUpAreaIds(sa.getSysAreaId());
        List<Integer> idsub = sysAreaService.getAllSubAreaIds(sa.getSysAreaId());
        areaIds.addAll(idup);
        areaIds.addAll(idsub);
        Set<Integer> setAreaIds = new HashSet<>();
        setAreaIds.addAll(areaIds);
        map.put("areaId", setAreaIds);
        List<ReportExcelTemplate> reportExcelTemplateList = DataUtil.isEmpty(reportExcelTemplateDao.queryExcelTempsWitchIndexIsUsing(map));
        for (int i = 0; i < reportExcelTemplateList.size(); i++) {
            if (reportExcelTemplateList.get(i).getSysAreaId() != 1) {
                SysArea area1 = sysAreaService.queryAreaById(reportExcelTemplateList.get(i).getSysAreaId());
                String name = area1.getSysAreaName() + "-" + reportExcelTemplateList.get(i).getReportExcelTemplateName();
                reportExcelTemplateList.get(i).setReportExcelTemplateName(name);
            }
        }
        model.addAttribute("list", reportExcelTemplateList);
        return "reportIndex/add";
    }

    /**
     * 管理员查看的查询结果集
     *
     * @return
     */
    @RequestMapping("/sysQuery")
    public ModelAndView sysQuery(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("reportIndex/copyList");
        Object objSql = request.getAttribute("querySql");
        String querySql = null;
        if (objSql == null) {
            querySql = String.valueOf(session.getAttribute("reportIndex_sql"));
        } else {
            querySql = String.valueOf(objSql);
            session.setAttribute("reportIndex_sql", querySql);
        }
        String sql = StringUtils.replace(querySql, "|", " ");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sql);
        view.addObject("reportIndexList", list);
        return view;
    }
    /**
     *excel上传状态 
     */
    @RequestMapping(value = "/status")
    public String status(HttpServletRequest request,HttpSession session,
    		@RequestParam(required = false) Integer temptId,Model model){
    	Date subtime= new Date();
    	model.addAttribute("subtime", subtime.getTime());
    	return "reportIndex/new_file";
    }
    /**
     * 装session
     */
    @RequestMapping(value = "/flash")
    @ResponseBody
    public JsonResWrapper flash(HttpServletRequest request,HttpSession session,Model model,long subtime
    		){
//    	Date date1 = new Date();
//		long s1 = date1.getTime()-subtime-TimeZone.getDefault().getRawOffset(); //毫秒数  	  		
//		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。+"已耗时："+hms
//		String hms = formatter.format(s1);  
    	JsonResWrapper jrw= new JsonResWrapper();
    	jrw.setFlag(true);
    	jrw.setData(request.getSession().getAttribute("statuName"));
    	jrw.setMessage(request.getSession().getAttribute("statuName")+"");
	    model.addAttribute("key", request.getSession().getAttribute("statuName"));
	    return jrw;
    }
	/*@Test
	public void test() {
//		List<String> list =new ArrayList<>();
//		list.add("2");
//		list.add("3");
//		list.add(0, "1");
//		String osnName = System.getProperty("os.name");
//		System.out.println(osnName);
		long  ms =  2000-TimeZone.getDefault().getRawOffset(); ;//毫秒数
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
		String hms = formatter.format(ms);
		System.out.println(hms);

	}*/
    
    @RequestMapping(value = "/openstep")
    public String openstep(HttpServletRequest request,HttpSession session,Model model
    		){
	    model.addAttribute("step1", request.getSession().getAttribute("step1"));
	    model.addAttribute("step2", request.getSession().getAttribute("step2"));
	    model.addAttribute("step3", request.getSession().getAttribute("step3"));
	    model.addAttribute("step4", request.getSession().getAttribute("step4"));
	    model.addAttribute("step5", request.getSession().getAttribute("step5"));
	    return "reportIndex/step";
    }
	/**
	 * test/upLoad
	 */
    @RequestMapping(value = "/testupLoad")
    public void testupLoad(HttpSession session,Model model, MultipartFile file, HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String time, @RequestParam(required = false) String name1,
			@RequestParam(required = false) Integer temptId ) throws Exception{
    	request.getSession().removeAttribute("statuName");
		request.getSession().setAttribute("statuName", "开始解析数据...");
		String tempTbName=null;
		// 获取用户信息
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// 通过缓存的单个机构的key，获取缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if (so == null) {
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		int[] nums = {0};
		//提交时间
		Date submit=new Date();
		//归档日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = sdf.parse(time);
		//指标类型
		ReportExcelTemplate rt=reportExcelTemplateService.queryReportExcelTemplateById(temptId);
		Integer indexId =rt.getIndexId();
		IndexTb it= indexTbService.queryById(indexId);
		tempTbName = "TEMP_"+it.getIndexId()+"_"+userDetails.getSys_user_id();//临时表名
		String tempIndexName ="INDEX_"+it.getIndexId()+"_"+userDetails.getSys_user_id();//临时表索引名 
		//创建临时表前先判断是否存在
		int o = 0;
			//创建excel对应的临时表
			try{
				o=excelReportService.isTempTableExists(tempTbName);//如果可执行表示表还存在，则需要先删除
				if(o!=0){//动态表还存在，上次报送还未完成
					reportIndexService.deleteTempTb(tempTbName);
				}
				excelReportService.insertTempTable(rt,it,tempTbName);//重新创建临时表
				StringBuffer sb = new StringBuffer("CREATE INDEX "+tempIndexName+
						" ON ADMINISTRATOR."+tempTbName+"(NUM)" ); 
				reportIndexService.insertIndex(sb.toString());//创建临时表索引
			}catch(Exception e){
				e.printStackTrace();
				model.addAttribute("msg", "创建临时表失败");
			}
			List<String> errorList = new ArrayList<>(); //错误消息集合
			String[] isSuccess={"0"} ; //检验文件是否成功， =="1"则代表出现异常
			List<Map<String, Object>> qymcMsg = new ArrayList<>(); //报送基本信息，企业名称信息
			List<IndexItemTb> itemsList = new ArrayList<>();
		    Map<String,String> columnMap = new HashMap<>();
		    request.getSession().removeAttribute("statuName");
		    request.getSession().setAttribute("statuName", "正在校验EXCEL中的数据格式...");
			//获取地区到市
			SysArea a=sysAreaService.queryAreaById3(so.getSys_org_affiliation_area_id()!=null?so.getSys_org_affiliation_area_id():so.getSys_area_id()); 
		    String orgKey_ = RedisKeys.SYS_AREA_DOWN_IDS + a.getSysAreaId();
	        StringBuffer areaIds = RedisUtil.getObjData(orgKey_, StringBuffer.class);
	        if (areaIds ==null) {
	        	List<Integer> arealist = sysAreaService.getAllSubAreaIds(a.getSysAreaId());
	        	areaIds = new StringBuffer("("); 
	        	for (int i=0;i< arealist.size();i++) {
	        		if (i == arealist.size() - 1) {
	        			areaIds.append(arealist.get(i)+")");
	        		} else {
	        			areaIds.append(arealist.get(i)+",");
	        		}
	        	}
	            RedisUtil.setData(orgKey, areaIds);
	        }
	        List<String> indexNames = new ArrayList<>(); //指标项代号集合
			List<String> uniques = new ArrayList<>();	//装识别码		
			List<Map<String ,Object>> repeat = new ArrayList<>(); //唯一标识验重辅助集合
		    List<Integer> reNum = new ArrayList<>(); 
			excelReportService.getExcelToTemp(rt,itemsList,a,nums,submit,tempTbName,columnMap,qymcMsg,file, 
					indexNames, errorList,it,reportDate,so,uniques,repeat,reNum,isSuccess);
    }
    
	/**
	 * 导入excel
	 */
	@RequestMapping(value = "/upLoad")
	public String upLoad(HttpSession session,Model model, MultipartFile file, HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required = false) String time, @RequestParam(required = false) String name1,
			@RequestParam(required = false) Integer temptId ) throws Exception{
		request.getSession().removeAttribute("statuName");
		request.getSession().setAttribute("statuName", "开始解析数据...");
		String tempTbName=null;
		ReportIndex reportIndex = new ReportIndex();
		// 获取用户信息
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// 通过缓存的单个机构的key，获取缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		if (so == null) {
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		int[] nums = {0};
		String  filePath= null;
		//提交时间
		Date submit=new Date();
		//归档日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = sdf.parse(time);
		//指标类型
		ReportExcelTemplate rt=reportExcelTemplateService.queryReportExcelTemplateById(temptId);
		Integer indexId =rt.getIndexId();
		IndexTb it= indexTbService.queryById(indexId);
		tempTbName = "TEMP_"+it.getIndexId()+"_"+userDetails.getSys_user_id();//临时表名
		String tempIndexName ="INDEX_"+it.getIndexId()+"_"+userDetails.getSys_user_id();//临时表索引名 
		//创建临时表前先判断是否存在
		int a = 0;
		try {
			//创建excel对应的临时表
			try{
				a=excelReportService.isTempTableExists(tempTbName);//如果可执行表示表还存在，则需要先删除
				if(a!=0){//动态表还存在，上次报送还未完成
					reportIndexService.deleteTempTb(tempTbName);
				}
				excelReportService.insertTempTable(rt,it,tempTbName);//重新创建临时表
				StringBuffer sb = new StringBuffer("CREATE INDEX "+tempIndexName+
						" ON ADMINISTRATOR."+tempTbName+"(NUM)" ); 
				reportIndexService.insertIndex(sb.toString());//创建临时表索引
			}catch(Exception e){
				e.printStackTrace();
				model.addAttribute("msg", "创建临时表失败");
				return "reportIndex/add";
			}
			List<String> errorList = new ArrayList<>(); //错误消息集合
			String[] isSuccess={"0"} ; //检验文件是否成功， =="1"则代表出现异常
			List<Map<String, Object>> qymcMsg = new ArrayList<>(); //报送基本信息，企业名称信息
			List<String> updateList = new ArrayList<>();	//装update的数据	
			String[] status={"1"} ;
			//报送数据
			filePath = report(rt,nums,reportIndex,session,tempTbName, qymcMsg, file,errorList, it,reportDate, so,isSuccess, submit,time, 
					updateList, name1, userDetails, request, status);
			request.getSession().removeAttribute("statuName");
			String errorPath = null;
			//有误数据条数
			int count= reportIndexService.queryStatusNum(tempTbName,1);
			if (CollectionUtils.isNotEmpty(errorList)) {
			    DicExcelOut<String> excelOut = new DicExcelOut<>();
			    excelOut.setRowNames(new String[]{"错误信息"});
			    excelOut.setList(errorList);
			    errorPath = excelOut.outStringNotRespones();
			}		
			reportIndex.setReportIndexMethod(1);// 报送方式：0.报文报送 1.excel报送 2.手工录入 3.月季报送
			reportIndex.setReportIndexTemplate(name1);// 报送模板名称
			reportIndex.setSysOrgId(so.getSys_org_id());// （外键）机构id
			reportIndex.setReportIndexOrgName(so.getSys_org_name());// 机构名称
			reportIndex.setReportIndexSubmitTime(submit);// 提交时间
			reportIndex.setSysUserId(userDetails.getSys_user_id());// （外键）上报用户id
			reportIndex.setReportIndexOrgName(so.getSys_org_name());// 机构名
			reportIndex.setReportIndexTime(reportDate);
			reportIndex.setReportIndexNumbers(nums[0]);// 数量 条
			reportIndex.setReportIndexPath(filePath);// 报送文件目录			
			reportIndex.setErrorExcelPath(errorPath);// 报送错误文件目录			
			if(CollectionUtils.isEmpty(errorList) && count==0 ){
				status[0]="0";//0有效，1无效
				reportIndex.setReportIndexStatus(0);//上报成功
			}else{
				reportIndex.setReportIndexStatus(1);//上报失败			
			}
			// 保存报送的记录
			try {
				reportIndexService.insertOne(reportIndex);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			//查询临时表中的数据条数，判断excel导入是否出现异常
			int tempNum = excelReportService.getDataNum(tempTbName);
			//如果没有错误	
			if(CollectionUtils.isEmpty(qymcMsg) && CollectionUtils.isEmpty(errorList) 
					&& "0".equals(status[0]) && tempNum !=0){	
				request.getSession().setAttribute("statuName", "导入数据完成");
				sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, it.getIndexId(),
						null, null, null,submit, SysUserLogService.IMPORT,nums[0],
								null, null, null, filePath, null, true),request);
				model.addAttribute("msg", "操作成功");
				reportIndexService.deleteTempTb(tempTbName);
				request.getSession().removeAttribute("statuName");
				return "reportIndex/add";
			}else if(CollectionUtils.isNotEmpty(qymcMsg) && CollectionUtils.isEmpty(errorList)&& tempNum !=0){
				request.getSession().setAttribute("statuName", "导入数据完成");
				sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, it.getIndexId(),
				         null, null, null,submit, SysUserLogService.IMPORT,nums[0],
				         null, null, null, filePath, null, false),request);
				model.addAttribute("qymcMsg", qymcMsg);
				reportIndexService.deleteTempTb(tempTbName);
				request.getSession().removeAttribute("statuName");
				return "reportIndex/qymcMsg";
			}else if(CollectionUtils.isEmpty(errorList)&& tempNum ==0){
				request.getSession().setAttribute("statuName", "该文件没有填写数据或导入临时表出现异常，请重新导入");
				sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, it.getIndexId(),
				         null, null, null,submit, SysUserLogService.IMPORT,nums[0],
				         null, null, null, filePath, null, false),request);
				model.addAttribute("msg", "该文件没有填写数据或导入临时表出现异常，请重新导入");
				reportIndexService.deleteTempTb(tempTbName);
				request.getSession().removeAttribute("statuName");
				return "reportIndex/add";
			}else{
				request.getSession().setAttribute("statuName", "导入失败,文件内容有误");
				sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, it.getIndexId(),
				         null, null, null,submit, SysUserLogService.IMPORT,nums[0],
				         null, null, null, filePath, null, false),request);
				Collections.sort(errorList);
				model.addAttribute("msg", "导入失败,文件内容有误");
				session.setAttribute("messageSubmission_message", errorList);
				model.addAttribute("errorList", errorList);
				reportIndexService.deleteTempTb(tempTbName);
				request.getSession().removeAttribute("statuName");
				return "reportIndex/error";
			}
		} catch (Exception e) {
			request.getSession().setAttribute("statuName", "网络异常，导入失败，请重新导入");
			sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, it.getIndexId(),
			         null, null, null,submit, SysUserLogService.IMPORT,nums[0],
			         null, null, null, filePath, null, false),request);
			reportIndex.setReportIndexMethod(1);// 报送方式：0.报文报送 1.excel报送 2.手工录入 3.月季报送
			reportIndex.setReportIndexTemplate(name1);// 报送模板名称
			reportIndex.setSysOrgId(so.getSys_org_id());// （外键）机构id
			reportIndex.setReportIndexOrgName(so.getSys_org_name());// 机构名称
			reportIndex.setReportIndexSubmitTime(submit);// 提交时间
			reportIndex.setSysUserId(userDetails.getSys_user_id());// （外键）上报用户id
			reportIndex.setReportIndexOrgName(so.getSys_org_name());// 机构名
			reportIndex.setReportIndexTime(reportDate);
			reportIndex.setReportIndexNumbers(nums[0]);// 数量 条
			reportIndex.setReportIndexPath(filePath);// 报送文件目录			
			reportIndex.setErrorExcelPath(null);// 报送错误文件目录			
			reportIndex.setReportIndexStatus(1);//有效
			reportIndexService.insertOne(reportIndex);
			model.addAttribute("msg", "网络异常，导入失败，请重新导入");
			reportIndexService.deleteTempTb(tempTbName);
			request.getSession().removeAttribute("statuName");
			return "reportIndex/add";
		}
	}
	
	/**
	 * 报送
	 * @throws Exception 
	 */
	private String report(ReportExcelTemplate rt,int[] nums,ReportIndex reportIndex,HttpSession session,String tempTbName,List<Map<String, Object>> qymcMsg,MultipartFile file,
			List<String> errorList,IndexTb it,Date reportDate,SysOrg so
			,String[] isSuccess,Date submit,String time,List<String> updateList,
			String name1,MyUserDetails userDetails,HttpServletRequest request,String[] status) throws Exception{
		List<String> indexNames = new ArrayList<>(); //指标项代号集合
		List<String> uniques = new ArrayList<>();	//装识别码		
		List<Map<String ,Object>> repeat = new ArrayList<>(); //唯一标识验重辅助集合
	    List<Integer> reNum = new ArrayList<>(); 
	    List<IndexItemTb> itemsList = new ArrayList<>();
	    Map<String,String> columnMap = new HashMap<>();
	    request.getSession().removeAttribute("statuName");
	    request.getSession().setAttribute("statuName", "正在校验EXCEL中的数据格式...");
		//获取地区到市
		SysArea a=sysAreaService.queryAreaById3(so.getSys_org_affiliation_area_id()!=null?so.getSys_org_affiliation_area_id():so.getSys_area_id()); 
	    String orgKey = RedisKeys.SYS_AREA_DOWN_IDS + a.getSysAreaId();
        StringBuffer areaIds = RedisUtil.getObjData(orgKey, StringBuffer.class);
        if (areaIds ==null) {
        	List<Integer> arealist = sysAreaService.getAllSubAreaIds(a.getSysAreaId());
        	areaIds = new StringBuffer("("); 
        	for (int i=0;i< arealist.size();i++) {
        		if (i == arealist.size() - 1) {
        			areaIds.append(arealist.get(i)+")");
        		} else {
        			areaIds.append(arealist.get(i)+",");
        		}
        	}
            RedisUtil.setData(orgKey, areaIds);
        }
		//excel中的数据原样存进临时表
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss.ssss");//初始化Formatter的转换格式。
        Date d_1 = new Date();
		String filePath=excelReportService.getExcelToTemp(rt,itemsList,a,nums,submit,tempTbName,columnMap,qymcMsg,file, indexNames, errorList,it,reportDate,so,uniques
				,repeat,reNum,isSuccess);
		Date d_2 = new Date();
		long d_2_1 = d_2.getTime()-d_1.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  	  		
		String s_2_1 = formatter.format(d_2_1); 
		request.getSession().setAttribute("step1", "校验EXCEL数据并导入临时表耗时："+s_2_1);
		//校验基本信息统一社会信用代码或组织机构代码是否重复
		if(CollectionUtils.isEmpty(uniques)){			
			excelReportService.checkCreditUnique(tempTbName,errorList);
			excelReportService.checkOrgUnique(tempTbName,errorList);
		}
		Date d_2_a = new Date();
		long d_2_a_2 = d_2_a.getTime()-d_2.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  	  		
		String s_2_a_2 = formatter.format(d_2_a_2); 
		request.getSession().setAttribute("step1_a", "校验基本信息统一社会信用代码或组织机构代码是否重复："+s_2_a_2);
		//数据刚进入临时表，状态为0，表示正确
		int count0= reportIndexService.queryStatusNum(tempTbName,0);
		if(count0 ==0 && CollectionUtils.isEmpty(errorList)){//标识临时表中没有数据
			errorList.add("EXCEL中的有效数据条数为0");
		}
		request.getSession().removeAttribute("statuName");
		request.getSession().setAttribute("statuName", "校验EXCEL数据格式完成");
		//如果是基本信息，保存二码
		try{			
			if(it.getIndexCode().equals("index_jbxx") &&CollectionUtils.isEmpty(errorList)){
				request.getSession().removeAttribute("statuName");
				request.getSession().setAttribute("statuName", "正在校验统一社会信用代码或组织机构代码是否导入二码表...");
				excelReportService.saveDafults(nums[0]+2,tempTbName, errorList, a, qymcMsg);
			}	
		}catch(Exception e){
			errorList.add("保存基本信息对应的二码时出现网络通讯异常");
			e.printStackTrace();
		}		
		Date d_3 = new Date();
		long d_3_2 = d_3.getTime()-d_2.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  
		String s_3_2 = formatter.format(d_3_2); 
		request.getSession().setAttribute("step2", "校验基本信息是否保存二码耗时："+s_3_2);
		//校验数据,将临时表数据设置二码id
		try{			
			if(CollectionUtils.isEmpty(errorList)){	
				request.getSession().removeAttribute("statuName");
				request.getSession().setAttribute("statuName", "正在校验数据规则...");
				excelReportService.tocheck(nums[0]+2,itemsList,a,areaIds,submit, tempTbName, qymcMsg, indexNames, errorList, it, reportDate, so, uniques, repeat, reNum, isSuccess);
				request.getSession().removeAttribute("statuName");
				request.getSession().setAttribute("statuName", "校验数据规则完成");
			}
		}catch(Exception e){
			errorList.add("统一社会信用代码或组织机构代码转换二码对应ID阶段出现异常");
			e.printStackTrace();
		}
		Date d_4 = new Date();
		long d_4_3 = d_4.getTime()-d_3.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  
		String s_4_3 = formatter.format(d_4_3); 
		request.getSession().setAttribute("step3", "校验数据规则耗时："+s_4_3);
		//获取数据库表名
		String  tbName= it.getIndexCode()+"_tb";
		try{			
			if(CollectionUtils.isEmpty(errorList) && !"1".equals(isSuccess[0])){
				request.getSession().removeAttribute("statuName");
				request.getSession().setAttribute("statuName", "正在校验数据是否更新...");
				excelReportService.insertSqlAndReport(a,areaIds,columnMap,submit,tbName,tempTbName,indexNames,so,it,time,uniques,isSuccess,updateList);				
				request.getSession().removeAttribute("statuName");
				request.getSession().setAttribute("statuName", "校验数据是否更新完成");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		Date d_5 = new Date();
		long d_5_4 = d_5.getTime()-d_4.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  
		String s_5_4 = formatter.format(d_5_4); 
		request.getSession().setAttribute("step4", "校验数据是否更新耗时："+s_5_4);
		//临时表中是否存在有误数据
		int count= reportIndexService.queryStatusNum(tempTbName,1);
		request.getSession().removeAttribute("statuName");
		request.getSession().setAttribute("statuName", "正在导入数据...");
		ExecutorService pool = Executors.newFixedThreadPool(4);
		if(CollectionUtils.isEmpty(errorList)&&CollectionUtils.isNotEmpty(updateList) && count ==0){				
			for (int i=0;i<updateList.size();i++) {
				String sql =  updateList.get(i);
				pool.execute(new updateInner(nums[0]+2,sql, errorList, i));
			}
			pool.shutdown();
			while(true){
				if(pool.isTerminated()){
					break;
				}
			}
		}
		if(CollectionUtils.isEmpty(errorList)&&count ==0){	
			try{				
				tempToIndexTb(tempTbName, tbName, indexNames);								
			}catch(Exception e){
				errorList.add("临时表数据导入动态表执行异常");
			}
		}
		Date d_6 = new Date();
		long d_6_5 = d_6.getTime()-d_5.getTime()-TimeZone.getDefault().getRawOffset(); //毫秒数  
		String s_6_5 = formatter.format(d_6_5); 
		request.getSession().setAttribute("step5", "数据入库耗时："+s_6_5);
		return filePath;
	}

	private class updateInner implements Runnable {
		private int rowCount;
		private String sql; 
		private List<String> errorList;
		private int i;
		
		public updateInner(int rowCount,String sql, List<String> errorList, int i) {
			super();
			this.rowCount = rowCount;
			this.sql = sql;
			this.errorList = errorList;
			this.i = i;
		}

		@Override
		public void run() {
			try{
				reportIndexService.updateBySql(sql);
			}catch (Exception e) {
				e.printStackTrace();
				errorList.add("第"+getcol(i+3,rowCount)+(i+3)+"行数据保存失败");
			}
		}
	}

	/**
     * 高级查询报送记录
     */
    @RequestMapping(value = "/search")
    public String search(Model model,
                         @RequestParam(required = false) String tempName,//模版名称
                         @RequestParam(required = false) Integer orgId,//机构id
                         @RequestParam(required = false) String orgName,//机构名称
                         @RequestParam(required = false) String reportDate,//归档时间
                         @RequestParam(required = false) Integer status,//报送结果
                         @RequestParam(required = false) String submitTime,//报送结果
                         HttpServletRequest request) throws Exception {
        tempName = StringUtils.trimToNull(tempName);
        reportDate = StringUtils.trimToNull(reportDate);
        submitTime = StringUtils.trimToNull(submitTime);
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
        SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
        if (so == null) {
            so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
            RedisUtil.setData(orgKey, so);
        }
        List<Integer> ids = new ArrayList<>();
        if (orgId != null) {
            ids.add(orgId);
        } else {
            subDataGet.getAllOrgIds(so, ids);
        }
        PageSupport ps = PageSupport.initPageSupport(request);
        List<ReportIndex> list = reportIndexService.getDataBySome(ps, ids, ReportIndexDao.EXCEL_SUBMIT, tempName, status, reportDate,submitTime);
        model.addAttribute("list", list);
        model.addAttribute("tempName", tempName);
        model.addAttribute("orgName", orgName);
        model.addAttribute("orgId", orgId);
        model.addAttribute("reportDate", reportDate);
        model.addAttribute("status", status);
        model.addAttribute("submitTime", submitTime);
        List<SysOrg> orgList = getSysOrg(userDetails);
        //机构
        model.addAttribute("orgList", orgList);
        Map<String, Object> map = new HashMap<>();

        // 查询管辖地区及子地区的ID
        String areaKey = RedisKeys.SYS_AREA + so.getSys_area_id();
        SysArea area = RedisUtil.getObjData(areaKey, SysArea.class);
        if (area == null) {
            area = sysAreaService.queryAreaById(so.getSys_area_id());
            RedisUtil.setData(areaKey, area);
        }
        StringBuffer sb = new StringBuffer();
        //DataUtil.getParentAreaIds(area, sb, sysAreaService);// 获取该地区及父地区的ID
        DataUtil.getChildAreaIds(area, sb);// 获取该地区及子地区的ID
        String[] areaids = sb.toString().split(",");
        map.put("areaId", areaids);
        List<ReportExcelTemplate> reportExcelTemplateList = null;
        StringBuilder qc = new StringBuilder();
        if (StringUtils.isNotBlank(tempName)) {
            qc.append("模板名称：" + tempName);
        }
        if (StringUtils.isNotBlank(orgName)) {
            qc.append("机构：" + orgName);
        }
        if (StringUtils.isNotBlank(reportDate)) {
            qc.append("归档时间：" + reportDate);
        }
        String s = null;
        if (status != null) {
            if (status == 0) {
                s = "上报成功";
            } else {
                s = "上报失败";
            }
        }
        if (StringUtils.isNotBlank(s)) {
            qc.append("报送结果：" + status);
        }
        StringBuilder sql = new StringBuilder();
        sql.append("select * from report_index_tb WHERE sys_org_id IN(");
        for (int i = 0; i < ids.size(); i++) {
            if (i != ids.size() - 1) {
                sql.append(ids.get(i) + ",");
            } else {
                sql.append(ids.get(i) + ")");
            }
        }
        sql.append(" AND report_index_method= 1 ");
        if (StringUtils.isNotBlank(tempName)) {
            sql.append(" AND report_index_template='" + tempName + "' ");
        }
        if (status != null) {
            if (status == 1) {
                sql.append(" AND report_index_status=1 ");
            } else {
                sql.append(" AND report_index_status=0 ");
            }
        }
        if (StringUtils.isNotBlank(reportDate)) {
            sql.append(" AND to_char(report_index_time,'YYYY-MM-DD') = '" + reportDate + "' ");
        }
        sql.append(" ORDER BY report_index_id DESC ");
        try {
            reportExcelTemplateList = DataUtil.isEmpty(reportExcelTemplateService.queryReportExcelTemplateList(map, null));
            sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, null, null,
                    null, null, new Date(), SysUserLogService.SELECT, reportExcelTemplateList.size(), qc.toString(),
                    sql.toString(), "/admin/reportIndex/sysQuery.jhtml", null, null, true), request);
        } catch (Exception e) {
            sysUserLogService.insertOneLog(new SysUserLog("EXCEL报送", null, null, null,
                    null, null, new Date(), SysUserLogService.SELECT, reportExcelTemplateList.size(), qc.toString(),
                    sql.toString(), "/admin/reportIndex/sysQuery.jhtml", null, null, false), request);
        }
        //模版
        model.addAttribute("Tlist", reportExcelTemplateList);
        return "reportIndex/list";
    }

    /**
     * 查询选中的已报数据的详细信息
     */
    @RequestMapping(value = "/show")
    public String show(@RequestParam(required = false) Integer reportIndexId, Model model) {
        ReportIndex reportIndex = reportIndexService.queryReportIndexsById(reportIndexId);
        model.addAttribute("reportIndex", reportIndex);
        return "reportIndex/show";
    }

    /**
     * 下载文件
     */
    @RequestMapping("/downLoadFile")
    @ResponseBody
    public void downLoadFile(Integer reportIndexId, HttpServletRequest request, HttpServletResponse response) {
        ReportIndex report = reportIndexService.queryReportIndexsById(reportIndexId);
        String path = report.getReportIndexPath();
        DownLoadFile.downLoadFile(path, report.getReportIndexTemplate() + "报送文件", request, response);
    }

    /**
     * 下载文件
     */
    @RequestMapping("/downLoadErrorFile")
    @ResponseBody
    public void downLoadErrorFile(Integer reportIndexId, HttpServletRequest request, HttpServletResponse response) {
        ReportIndex report = reportIndexService.queryReportIndexsById(reportIndexId);
        String path = report.getErrorExcelPath();
        DownLoadFile.downLoadFile(path, report.getReportIndexTemplate() + "报送错误消息文件", request, response);
    }

    /**
     * 倒入前查询：同一个模板，同一个机构，同一天是否上报有成功记录，如果有的话则提示已上报过
     */
    @RequestMapping(value = "/checkReport")
    @ResponseBody
    public JsonResWrapper checkReport(
            @RequestParam(required = false) String time,
            @RequestParam(required = false) Integer temptId) {
        MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        JsonResWrapper jrw = new JsonResWrapper();
        //查询：同一个模板，同一个机构，同一天是否上报有成功记录
        List<ReportIndex> list = reportIndexService.queryReporByTempOrgDate(time, temptId, userDetails.getSys_org_id());
        if (CollectionUtils.isNotEmpty(list)) {
            jrw.setFlag(false);
            jrw.setMessage("该指标今天已成功上报过数据，是否重新上报！");
        } else {
            jrw.setFlag(true);
            jrw.setMessage("该指标今天还未成功上报过数据，是否继续上报？");
        }
        return jrw;
    }


    /**
     * 导出错误信息成 excel 文件
     */
    @RequestMapping("/outMsg")
    public void outMsg(HttpSession session, HttpServletRequest request, HttpServletResponse response) throws NoSuchMethodException {
        List<String> message = (List<String>) session.getAttribute("messageSubmission_message");
        if (CollectionUtils.isEmpty(message)) {
            message = new ArrayList<>();
            message.add("看到这句话表示你遇到 bug 了,请联系管理员.");
        }
        if (CollectionUtils.isNotEmpty(message)) {
            DicExcelOut<String> excelOut = new DicExcelOut<>();
            excelOut.setTitle("上报错误信息");
            excelOut.setRowNames(new String[]{"错误信息"});
            excelOut.setList(message);
            excelOut.outString(response, request);
        }
    }
    
	/**
	 * 基本信息名称替换的方法
	 * @param 
	 * @return
	 */
	@RequestMapping("/changeName")
	@ResponseBody
	public String changeName(@RequestParam(required=false) String reco,
			@RequestParam(required=false) String newn,
			@RequestParam(required=false) String oldn,
			@RequestParam(required=false) String defa
			){
		String msg=null;
		List<String>  sqls1=new ArrayList<String>();
		String[] record_dates=reco.split(",");
		String[] newNames=newn.split(",");
		String[] defaultIds=defa.split(",");
		System.out.println();
		for (int i=0;i< defaultIds.length ;i++) {			
			StringBuffer sb =new StringBuffer("update DEFAULT_INDEX_ITEM_TB set QYMC='"+newNames[i]+"'");
			sb.append(" where DEFAULT_INDEX_ITEM_ID = "+defaultIds[i]);
			sqls1.add(sb.toString());
		}
		for (int i=0;i< defaultIds.length ;i++) {			
			StringBuffer sb =new StringBuffer("update index_jbxx_tb set index_jbxx_qymc='"+newNames[i]+"'");
			sb.append(" where DEFAULT_INDEX_ITEM_ID = "+defaultIds[i] +" and record_date='"+record_dates[i]+"'");
			sqls1.add(sb.toString());
		}
		for(int i=0;i<sqls1.size();i++){
			try {
				reportIndexService.updateBySql(sqls1.get(i));
				msg="操作成功";
			} catch (Exception e) {
				e.printStackTrace();
				msg="操作失败";
			}
		}
		if(!"操作成功".equals(msg)){
			msg="操作失败";
		}
		return msg;
	}
	
	/**
	 * 查询机构列表
	 * @param model
	 * @param 
	 * @param 
	 * @return
	 */
	@RequestMapping("/getOrgList")
	@ResponseBody
	public void getOrgByName(HttpServletResponse response){
		// 登录用户session
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		Map<String, Object> map = new HashMap<String, Object>();
		// 获取管辖区域
		String areaKey=RedisKeys.SYS_AREA+so.getSys_area_id();
		SysArea sa = RedisUtil.getObjData(areaKey, SysArea.class);
		if(sa==null){
			sa = sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, sa);
		}
		// 查询管辖地区及子地区的ID
		StringBuffer sb = new StringBuffer();
		DataUtil.getChildAreaIds(sa, sb);// 获取改地区及子地区的ID
		String[] ids = sb.toString().split(",");
		map.put("areaId", ids);
			map.put("orgType", 3);
		List<SysOrg> sysOrgs=manualEntryService.querySysOrgAll(map);
		Set<Integer> set=new HashSet<>();
		for (int i = 0; i < sysOrgs.size(); i++) {
			set.add(sysOrgs.get(i).getSys_org_type_id());
		}
		Map<String, Object> orgTypeMap = new HashMap<String, Object>();
		orgTypeMap.put("typeIds", set);
		List<SysOrgType> sysOrgType=sysOrgTypeService.queryTypeList(orgTypeMap);
		List<ZtreeVo> ztreeVo = DataUtil.getZtreeTwo(sysOrgs, sysOrgType);
		Gson gson=new Gson();
		try {
			response.getWriter().write(gson.toJson(ztreeVo));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 临时表入动态表
	 * @throws Exception 
	 */
	private void tempToIndexTb(String tempTbName,String tbName,List<String> indexNames) throws Exception{
		StringBuffer sb = new StringBuffer();
		sb.append("insert into "+tbName+"(default_index_item_id,sys_org_id,"
				+ "sys_area_id,record_date,");
		for(int i=3;i<indexNames.size();i++){
			sb.append(indexNames.get(i)+",");
		}
		sb.append("submit_time ) select default_index_item_id,sys_org_id,sys_area_id,record_date,");
		for(int i=3;i<indexNames.size();i++){
			sb.append(indexNames.get(i)+",");
		}
		sb.append("submit_time from "+tempTbName+" where isinsert = 0");
		reportIndexService.updateBySql(sb.toString());
	}
	
	private List<SysOrg> getSysOrg(MyUserDetails userDetails) {
        Integer userType = userDetails.getSysRole().getSys_role_type(); //当前用户的用户类型
        List<SysOrg> sysOrgs = null;
        SysOrg sysOrg = userDetails.getSysOrg();
        String key = RedisKeys.SYS_USER_LOG + userType + "messageSubmission" + sysOrg.getSys_org_id() + sysOrg.getSys_area_id();
        if (RedisUtil.isEmpty(key)) {
            sysOrgs = new ArrayList<>();
            if (userType != 10) {
                //政府报送员,只能看到本机构报送的数据
                List<Integer> list = sysAreaService.getAllSubAreaIds(sysOrg.getSys_area_id());
                if (CollectionUtils.isNotEmpty(list)) {
                    String[] a = new String[list.size()];
                    for (int x = 0; x < list.size(); x++) {
                        a[x] = String.valueOf(list.get(x));
                    }
                    List<SysOrg> sysOrgList = sysOrgService.querySysOrgByAreaIds(a);//根据地区搜出所有的机构
                    //再来去重复
                    if (sysOrg.getSubSysOrg() != null) {
                        List<SysOrg> lsls = new ArrayList<>();
                        subDataGet.getAllOrgs(sysOrg, lsls);
                        for (SysOrg lsl : lsls) {
                            for (int xx = 0; xx < sysOrgList.size(); xx++) {
                                if (StringUtils.equals(sysOrgList.get(xx).getSys_org_name(), lsl.getSys_org_name())) {
                                    sysOrgList.remove(xx);
                                    xx--;
                                }
                            }
                        }
                    }
                    sysOrgs.add(sysOrg);
                    sysOrgs.addAll(sysOrgList);
                }
            } else {
                sysOrg.setSubSysOrg(null);
                sysOrgs.add(sysOrg);
            }
            RedisUtil.setData(key, sysOrgs);
        } else {
            Type type = new TypeToken<List<SysOrg>>() {
            }.getType();
            sysOrgs = RedisUtil.getListData(key, type);
        }
        return sysOrgs;
    }
	
	
	private String getcol(int i,int num){
		String len = String.valueOf(num);//总位数	
		int weishu  = len.length();
		String now = String.valueOf(i);//当前位数
		int the = now.length();
		String o =null;
		if(weishu == 1){
			o ="";
		}else if(weishu ==2){
			if(the ==1){
				o="0";
			}else{
				o="";
			}
		}else if(weishu ==3){
			if(the ==1){
				o="00";
			}else if(the ==2){
				o="0";
			}else{
				o="";
			}
		}else if(weishu ==4){
			if(the ==1){
				o="000";
			}else if(the ==2){
				o="00";
			}else if(the ==3){
				o="0";
			}else{
				o="";
			}
		}else if(weishu ==5){
			if(the ==1){
				o="0000";
			}else if(the ==2){
				o="000";
			}else if(the ==3){
				o="00";
			}else if(the ==4){
				o="0";
			}else{
				o="";
			}
		}
		return o;
	}
	
}
