package com.workmanagement.controller;

import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.MyUserDetails;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.model.SysOrg;
import com.workmanagement.service.DicContentService;
import com.workmanagement.service.DicService;
import com.workmanagement.service.IndexItemAliasService;
import com.workmanagement.service.IndexItemTbService;
import com.workmanagement.service.IndexTbService;
import com.workmanagement.service.RelateInfoService;
import com.workmanagement.service.ReportExcelTemplateService;
import com.workmanagement.service.ReportIndexService;
import com.workmanagement.service.SysAreaService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.service.SysOrgService;
import com.workmanagement.util.ChineseFirstLetterUtil;
import com.workmanagement.util.PageSupport;
import com.workmanagement.util.RedisKeys;
import com.workmanagement.util.RedisUtil;

/**
 *
 * @author 作者 wqs
 * @date 创建时间：2017年3月14日 上午9:52:35 指标设置
 */
@Controller
@RequestMapping("/admin/indexTb")
public class IndexTbController {

	@Autowired
	private IndexTbService indexTbService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	private DicService dicService;
	@Autowired
	private IndexItemAliasService indexItemAliasService;
	@Autowired
	private SysManageLogService sysManageLogService;
    @Autowired
    private RelateInfoService relateInfoService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private ReportIndexService reportIndexService;
	@Autowired
	private ReportExcelTemplateService reportExcelTemplateService;
	/**
	 * 显示指标
	 */
	@RequestMapping("/list")
	public String list(Integer sysAreaId, Model model, HttpServletRequest request) {
		// 获取机构
		SysOrg so = getOrgRedis();
		if (so != null) {
			// 如果区域为空，就根据机构所在地址查询
			int areaId = so.getSys_area_id();
			SysArea a=sysAreaService.getUpOrThisSysArea(areaId);
			areaId=a.getSysAreaId();
			model.addAttribute("sysOrg", so);
			// 根据机构地址id，查询地址名称
			String key = RedisKeys.SYS_AREA + a.getSysAreaId();
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if (area == null) {
				area = sysAreaService.queryAreaById(areaId);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("area", area);
			// 分页
			PageSupport ps = PageSupport.initPageSupport(request);
			List<IndexTb> list;
			// 判断当前登录是否为四川省
			if (areaId == 1) {
				list = indexTbService.queryAll(ps);
			} else {
				list = indexTbService.queryAll2(ps, a.getSysAreaId());
			}
			request.setAttribute("list", list);
			model.addAttribute("list", list);
		}
		return "indexTb/list";
	}

    /**
     * 管理员查看的查询结果集
     *
     * @return
     */
    @RequestMapping("/sysQuery")
    public ModelAndView sysQuery(HttpServletRequest request, HttpSession session) throws Exception {
        ModelAndView view = new ModelAndView("indexTb/copyList");
        Object objSql = request.getAttribute("querySql");
        String querySql = null;
        if (objSql == null) {
            querySql = String.valueOf(session.getAttribute("query_sql"));
        } else {
            querySql = String.valueOf(objSql);
            session.setAttribute("query_sql", querySql);
        }
        String sql = StringUtils.replace(querySql, "|", " ");
        PageSupport ps = PageSupport.initPageSupport(request);
        List<Map<String, Object>> list = relateInfoService.queryMoreData(ps, sql);
        view.addObject("list", list);
        return view;
    }
	
	/**
	 * 指标大类模糊查询
	 */
	@RequestMapping("/search")
	public String search(Model model, String words, Integer aId,HttpServletRequest request) {
		SysOrg so = getOrgRedis();
		if (so != null) {
			// 如果区域为空，就根据机构所在地址查询
			int areaId = so.getSys_area_id();
			// 根据机构地址id，查询地址名称
			String key = RedisKeys.SYS_AREA + areaId;
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if (area == null) {
				area = sysAreaService.queryAreaById(areaId);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("area", area);
			// 分页
			PageSupport ps = PageSupport.initPageSupport(request);
			List<IndexTb> list;
			// 判断当前登录是否为四川省
//			if(words ==null){
//				words ="";
//			}
			if("".equals(words)){
				words = null;
			}
			model.addAttribute("words", words);
			if (areaId == 1 ) {
//				if(StringUtils.isBlank(words)){
//					list = indexTbService.queryAll(ps);
//				}else{					
					list = indexTbService.mohuQueryAll(ps, words,aId);
//				}
				String s="";
				if(aId !=null){
					s= " and s.SYS_AREA_ID = "+aId ; 
				}
				StringBuffer sql = new StringBuffer(" select * from (SELECT i.*,a.SYS_AREA_NAME as sysAreaName FROM INDEX_TB i LEFT JOIN SYS_AREA_TB a ON a.SYS_AREA_ID = i.SYS_AREA_ID WHERE a.SYS_AREA_ID =1 OR a.SYS_AREA_UPID=1 ) s"+ 
						" where s.index_name like '%'|| '"+words+"' ||'%' "+s+" order by index_number ");
				String sql0 = StringUtils.replace(sql.toString(), " ", "|");
				if(!CollectionUtils.isEmpty(list)){					
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, null, null, null, null, new Date(), 
							4, list!=null?list.size():0, "查询条件:"+(StringUtils.isBlank(words)?"":"关键字："+words)+(aId ==null?"":",地区id:"+aId), 
							sql0, "/admin/indexTb/sysQuery.jhtml",null , null, true),request);
				}else{
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, null, null, null, null, new Date(), 
							4, 0, "查询条件:"+(StringUtils.isBlank(words)?"":"关键字："+words)+(aId ==null?"":",地区id:"+aId), 
							sql0, "/admin/indexTb/sysQuery.jhtml",null , null, true),request);
				}				
			} else {
				List<Integer> ids=sysAreaService.getAllUpAreaIds(areaId);
				list = indexTbService.mohuQueryAll2(ps, ids, words);
				StringBuffer sql = new StringBuffer("select * from ( SELECT i.*,a.SYS_AREA_NAME as sysAreaName FROM INDEX_TB i LEFT JOIN SYS_AREA_TB a ON a.SYS_AREA_ID = i.SYS_AREA_ID WHERE a.SYS_AREA_ID = "+
						+areaId+" OR a.SYS_AREA_UPID=0 ) s where s.index_name like '%'|| "+words+" ||'%' order by index_number");
				String sql0 = StringUtils.replace(sql.toString(), " ", "|");
				if(!CollectionUtils.isEmpty(list)){					
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, null, null, null, null, new Date(), 
							4, list!=null?list.size():0,"查询条件:"+(StringUtils.isBlank(words)?"":"关键字："+words), 
							sql0, "/admin/indexTb/sysQuery.jhtml",null , null, true),request);
				}else{
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, null, null, null, null, new Date(), 
							4, 0,"查询条件:"+(StringUtils.isBlank(words)?"":"关键字："+words), 
							sql0, "/admin/indexTb/sysQuery.jhtml",null , null, true),request);
				}
			}
			request.setAttribute("list", list);
			model.addAttribute("list", list);
		}
		return "indexTb/list";
	}

	/**
	 * 将指标置为无效
	 */
	@RequestMapping(value = "/change", method = RequestMethod.POST)
	public JsonResWrapper change(Integer indexId, String a,HttpServletRequest request) {
		JsonResWrapper jrw = new JsonResWrapper();
		try {
			if ("有效".equals(a)) {// 当前为有效，需将指标大类置为无效
				Integer status = 0;
				indexTbService.change(indexId, status);
				jrw.setFlag(true);
				jrw.setMessage("该指标大类已禁用");
				sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, indexId, null, "1", "0", new Date(), 
						3, 1, null, null, null,null , null, true),request);
			} else {// 当前为无效,将指标大类置为有效
				Integer status = 1;
				indexTbService.change(indexId, status);
				jrw.setFlag(true);
				jrw.setMessage("该指标大类已启用");
				sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, indexId, null, "0", "1", new Date(), 
						3, 1, null, null, null,null , null, true),request);
			}
		} catch (Exception e) {
			jrw.setFlag(false);
			jrw.setMessage("系统错误，操作失败");
			if("有效".equals(a)){
				sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, indexId, null, "1", "0", new Date(), 
						3, 1, null, null, null,null , null, false),request);
			}else{
				sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, indexId, null, "0", "1", new Date(), 
						3, 1, null, null, null,null , null, false),request);
			}
		}
		return jrw;
	}

	/**
	 * 保存 指标大类
	 */
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	@ResponseBody
	public JsonResWrapper save(Model model, HttpServletRequest request, HttpServletResponse respons,
			@RequestParam(required = false) Integer indexId, @RequestParam(required = false) String indexName,
			@RequestParam(required = false) Integer sysAreaId, @RequestParam(required = false) Integer indexType, // 0
			@RequestParam(required = false) String queryProductTemplateAreaName,
			@RequestParam(required = false) Integer areaId,
			@RequestParam(required = false) String indexNumber, @RequestParam(required = false) Integer indexUsed, // 是否启用，0否，1是
			@RequestParam(required = false) String indexNotes) throws ParseException {
		JsonResWrapper jrw = new JsonResWrapper();

		//首先根据指标名字查询，如果重名则提示
		IndexTb indexTb1 = indexTbService.queryByNameAndAreaId(indexName,areaId);
		if(indexTb1!=null){
			jrw.setFlag(false);
			jrw.setMessage("本地区已存在该指标");
			return jrw;
		}
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// 根据机构ID获取机构缓存
		SysOrg so = getOrgRedis();
		//
		if(areaId==null){
			areaId = so.getSys_area_id();
		}
		//当前登陆机构所在地区
		SysArea a= sysAreaService.getUpOrThisSysArea(so.getSys_area_id());
		String areaKey =RedisKeys.SYS_AREA+ a.getSysAreaId();
		SysArea area =RedisUtil.getObjData(areaKey, SysArea.class);
		if(area==null){
			area =sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(areaKey, area);
		}
		IndexTb indexTb = new IndexTb();

		if (indexId == null) {// 如果指标id为空，则为新增操作
			try {
				// 根据指标大类名称对应的数据库表名查询该指标大类是否已存在
				StringBuffer indexCode =new StringBuffer( "index_" + ChineseFirstLetterUtil.getPinYinHeadChar(indexName).toLowerCase());
				//如果不是四川省登陆则加上地区名字
				if(userDetails.getSys_user_id() !=1){
					indexCode.append("_").append(ChineseFirstLetterUtil.getPinYinHeadChar(a.getSysAreaName()).toLowerCase());
				}
				//根据指标代号和地区查询
				IndexTb T0 = indexTbService.queryByCodeAndAreaId(indexCode.toString(),areaId);
				int m=1;
				if (T0 != null) {
					indexCode.append("_"+m);
					T0 = indexTbService.queryByCodeAndAreaId(indexCode.toString(),areaId);
					m++;
				}

				// 第一步：将指标名称中文首字母转成指标代码,构建数据库表名
				indexTb.setIndexCode(indexCode.toString());// 数据库表
				String tbName = indexCode.toString().toUpperCase() + "_TB";// 数据库表名
				String tbId = indexCode + "_ID";// 主键
				indexTbService.createTable(tbName, tbId);
				StringBuffer sb = new StringBuffer("CREATE INDEX "+indexCode.toString().toUpperCase()+"_DEFAULT"+
						" ON ADMINISTRATOR."+tbName+"(DEFAULT_INDEX_ITEM_ID)" ); 
				try{					
					reportIndexService.updateBySql(sb.toString());
				}catch(Exception e){
					jrw.setFlag(false);
					jrw.setMessage("创建表对应的索引出现异常");
					return jrw;
				}
				
				//第二步：保存指标信息
				indexTb.setIndexUsed(indexUsed); // 是否启用
				indexTb.setIndexName(indexName); // 指标大类名称
				indexTb.setIndexType(indexType); // 指标类型 0：基本信息 1：业务信息
				indexTb.setSys_org_id(so.getSys_org_id()); // 创建机构id
				indexTb.setSys_user_id(userDetails.getSys_user_id()); // 创建者id
				if (indexNumber != null && !"".equals(indexNumber)) {
					indexTb.setIndexNumber(Integer.valueOf(indexNumber)); // 序号
				} else {
					indexTb.setIndexNumber(1);
				}
				indexTb.setIndexNotes(indexNotes); // 备注
				indexTb.setSysAreaId(areaId); // 地区id
				indexTbService.save(indexTb);			
				jrw.setFlag(true);
				jrw.setMessage("保存成功");
				IndexTb it = indexTbService.queryByCodeAndAreaId(indexCode.toString(),areaId);
				sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, it.getIndexId(), null, null, indexTb.toString(), new Date(), 
						1, 1, null, null, null,null , null, true),request);
			} catch (Exception e) {
				jrw.setFlag(false);
				jrw.setMessage("系统错误");
			}
		} else {// 指标id不为空，更新操作,指标只有新增操作，无更新
			indexTbService.updateIndex(indexTb);
			//sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, null, null, null, null, new Date(), 1, 1, null, null, null,null , null, false));
		}
		return jrw;
	}

	/**
	 * 指标大类删除
	 */
	@RequestMapping(value="/delIndex")
	@ResponseBody
	public JsonResWrapper delIndex(@RequestParam(required =false)Integer indexId){
		JsonResWrapper jrw = new JsonResWrapper();
		IndexTb it= indexTbService.queryById(indexId);
		SysOrg so = getOrgRedis();
		Integer nowAreaId = so.getSys_area_id();
		//登陆地区自己以及上级地区ids
		List<Integer> ids =sysAreaService.getAllUpAreaIds(nowAreaId);
		ids.remove(0);//移除自己
		
		if(ids.contains(it.getSysAreaId())){//如果该指标是上级地区所创建则不能删除
			jrw.setFlag(false);
			jrw.setMessage("不能删除上级地区所建的指标");
			return jrw;
		}
		Integer excelCount=reportExcelTemplateService.queryIndexIdByCount(indexId);
		if(excelCount>0){
			jrw.setFlag(false);
			jrw.setMessage("不能删除已创建Excel模板的指标");
			return jrw;
		}
		String tbName = it.getIndexCode()+"_tb";
		//删除之前先做一次查询，如果表中有数据则不允许删除
		StringBuffer sql = new StringBuffer("select count(*) from "+tbName);
		Integer count = reportIndexService.countBySql(sql.toString());
		if(count >0){
			jrw.setFlag(false);
			jrw.setMessage("该指标内有数据，不允许删除");
		}else{
			StringBuffer sql2 = new StringBuffer("drop table "+tbName);
			try {
				reportIndexService.updateBySql(sql2.toString());
				indexTbService.del(indexId);
				indexItemTbService.deleteIndexItemTb(indexId);
				jrw.setFlag(true);
				jrw.setMessage("删除指标大类成功");
			} catch (Exception e) {
				jrw.setFlag(false);
				jrw.setMessage("删除指标大类失败");
				e.printStackTrace();
			}
		}
		return jrw;
	}

	/**
	 * 新增 指标大类 页面
	 */
	@RequestMapping("/add")
	public String add(Model model) {
		// 根据机构ID获取机构缓存
		SysOrg so = getOrgRedis();
		model.addAttribute("so", so);
		if (so != null) {
			// 如果区域为空，就根据机构所在地址查询
			int areaId = so.getSys_area_id();
			SysArea a=sysAreaService.getUpOrThisSysArea(areaId);
			areaId=a.getSysAreaId();
			// 根据机构地址id，查询地址名称
			String key = RedisKeys.SYS_AREA + a.getSysAreaId();
			SysArea area = RedisUtil.getObjData(key, SysArea.class);
			if (area == null) {
				area = sysAreaService.queryAreaById(areaId);
				RedisUtil.setData(key, area);
			}
			model.addAttribute("area", area);
			Integer num;
			if(area.getSysAreaUpid()==0){
				//四川省能查看全部的指标个数
				 num=indexTbService.countAll();			
			}else{
				//登录市所建和四川省建的指标个数
				 num=indexTbService.countAll2(areaId);							
			}
			model.addAttribute("num", num);
		}
		return "indexTb/add";
	}
	/**
	 * 进行提升指标的表操作
	 */
	@RequestMapping("/saveCopyl")
	@ResponseBody
	public JsonResWrapper saveCopyl(String indexstring,Integer[] indexItemId,Model model,String newIndexName,Integer indexId){
		JsonResWrapper jrw = new JsonResWrapper();	
		String[] newIndexItemName =indexstring.split(",");
		boolean mark=false;
		boolean mark1=false;
		try{			
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			// 根据机构ID获取机构缓存
			String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
			SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
			// 如果没有缓存，则查询并载入缓存
			if (so == null) {
				so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
				RedisUtil.setData(orgKey, so);
			}
			//原市级指标
			IndexTb indexTb = indexTbService.queryById(indexId);
			Integer indexNumber=indexTbService.countAll();
			StringBuffer indexCode = new StringBuffer("index_" + ChineseFirstLetterUtil.getPinYinHeadChar(newIndexName).toLowerCase());
			String newTbName=indexCode+"_tb";
			IndexTb it= indexTbService.queryByNameAndAreaId(newIndexName, so.getSys_area_id());			
			if(it!=null){
				jrw.setFlag(false);
				jrw.setMessage("该指标名成已存在，请更换");
				return jrw;
			}
			//根据指标代号和地区查询
			IndexTb T0 = indexTbService.queryByCodeAndAreaId(indexCode.toString(),so.getSys_area_id());
			int m=1;
			if (T0 != null) {
				indexCode.append("_"+m);
				T0 = indexTbService.queryByCodeAndAreaId(indexCode.toString(),so.getSys_area_id());
				m++;
			}
			IndexTb inde = new IndexTb();
			inde.setIndexCode(indexCode.toString());
			inde.setIndexUsed(1); // 是否启用
			inde.setIndexName(newIndexName); // 指标大类名称
			inde.setIndexType(indexTb.getIndexType()); // 指标类型 0：基本信息 1：业务信息
			inde.setSys_org_id(so.getSys_org_id()); // 创建机构id
			inde.setSys_user_id(userDetails.getSys_user_id()); // 创建者id
			if (indexNumber != null && !"".equals(indexNumber)) {
				inde.setIndexNumber(Integer.valueOf(indexNumber)); // 序号
			} else {
				inde.setIndexNumber(1);
			}
			inde.setIndexNotes(""); // 备注
			inde.setSysAreaId(so.getSys_area_id()); // 地区id
			try{				
				indexTbService.save(inde);
				mark=true;
			}catch(Exception e){
				e.printStackTrace();
				jrw.setFlag(false);
				jrw.setMessage("保存新指标出错");
				return jrw;
			}
			IndexTb newIndex = indexTbService.getIndexTbbyIndexCode(indexCode.toString());
			StringBuffer sb =new StringBuffer("create table "+newTbName+"(");
			sb.append(indexCode+"_id integer not null GENERATED ALWAYS AS IDENTITY ( START WITH +1"+  
					" INCREMENT BY +1 "+ 
					" MINVALUE -2147483648"+  
					" MAXVALUE +2147483647"+  
					" NO CYCLE"+  
					" CACHE 20"+  
					" NO ORDER ) , ");
			sb.append("DEFAULT_INDEX_ITEM_ID INTEGER NOT NULL,");
			sb.append("SYS_ORG_ID INTEGER NOT NULL,");
			sb.append("SYS_AREA_ID INTEGER NOT NULL,");
			sb.append("RECORD_DATE DATE NOT NULL,");
			sb.append("SUBMIT_TIME TIMESTAMP  NOT NULL,");
			Integer indexAreaId = indexTb.getSysAreaId();
			List<Integer> areaids = new ArrayList<>();
			areaids.add(indexAreaId);
			List<IndexItemTb> itemList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
			int a =0;
			if(mark){//表示指标大类保存成功才能再去保存指标项
				try{
					for(int i=0;i<itemList.size();i++){
						for(int k=0;k<indexItemId.length;k++){
							if(itemList.get(i).getIndexItemId().intValue()==indexItemId[k].intValue()){
								IndexItemTb oldItem = itemList.get(k);
								String itemName=newIndexItemName[k];
								String newitemCode=indexCode+"_"+ChineseFirstLetterUtil.getPinYinHeadChar(itemName).toLowerCase();
								if(oldItem.getIndexItemType()==0){//字符
									sb.append(newitemCode+" varchar("+oldItem.getVarLength()+")," );
								}else if(oldItem.getIndexItemType()==1){//时间
									sb.append(newitemCode+" timeStamp ," );
								}else if(oldItem.getIndexItemType()==2){//数值
									sb.append(newitemCode+" double ," );
								}else{//字典
									sb.append(newitemCode+" varchar(255)," );
								}
								IndexItemTb item = new IndexItemTb();
								item.setIndexId(newIndex.getIndexId());	
								item.setIndexItemName(itemName);//指标项名
								item.setIndexItemCode(newitemCode); // 指标项code
								item.setIndexItemNumber(oldItem.getIndexItemNumber()); // 序号
								if(oldItem.getIndexItemNumber()==null){							
									a=0;
								}else{
									a=oldItem.getIndexItemNumber();
								}
								item.setSysAreaId(so.getSys_area_id()); // （外键）所属区域id
								item.setIndexItemUsed(oldItem.getIndexItemUsed()); // 是否启用，0否1（默认）是
								item.setIndexItemType(oldItem.getIndexItemType()); // 数据类型，0字符，1时间，2数值,3数据字典
								item.setVarLength(oldItem.getVarLength());// 设置字符长度
								item.setDicId(oldItem.getDicId()); // (外键)数据字典id
								item.setIndexItemImportUnique(oldItem.getIndexItemImportUnique()); // 是否识别码0否（默认）1是
								item.setIndexItemEmpty(oldItem.getIndexItemEmpty()); // 是否可以为空，0否，1 是（默认）
								item.setIndexItemNetId(oldItem.getIndexItemNetId()); // 网络标识
								item.setIndexItemNotes(""); // 备注
								item.setSys_user_id(userDetails.getSys_user_id()); // （外键）创建者id
								item.setSys_org_id(so.getSys_org_id()); // （外键）创建机构id
								indexItemTbService.insertIndexItemTb(item);
							}
						}					
					}
					mark1=true;
				}catch (Exception e) {
					e.printStackTrace();
					jrw.setFlag(false);
					jrw.setMessage("第"+(a+1)+"行指标项保存出错");
					return jrw;
				}
			}
			int len=sb.length();
			sb.delete(len-1, len);
			sb.append(")");
			Map<String, String> sqlmap = new HashMap<>();
			sqlmap.put("sql", sb.toString());	
			try{
				if(mark && mark1){					
					indexItemTbService.addColumnBySql(sqlmap);				
				}else if(mark && !mark1){
					jrw.setFlag(false);
					jrw.setMessage("保存指标项出错");
					return jrw;
				}
			}catch(Exception e){
				e.printStackTrace();
				jrw.setFlag(false);
				jrw.setMessage("创建新指标对应的数据库表出错");
				return jrw;
			}
			jrw.setFlag(true);
			jrw.setMessage("操作成功，已将建成对应的省级指标。是否需要将之前指标的数据导入新指标？如果选择“取消”，则需要手工导入");
			IndexTb newIt= indexTbService.getIndexTbbyIndexCode(indexCode.toString());
			model.addAttribute("newIndexId", newIt.getIndexId());
			Map<String,Object> map = new HashMap<>();
			map.put("newIndexId", newIt.getIndexId());
			map.put("oldItemIds", indexItemId);
			jrw.setData(map);
		}catch(Exception e){
			jrw.setFlag(false);
			jrw.setMessage("操作失败，系统错误");
		}
		return jrw;
	}
	
	/**
	 * 向新表导入数据
	 */
	@RequestMapping("/insertToNewTable")
	@ResponseBody
	public JsonResWrapper insertToNewTable(String oldItemIdss,String newIndexId,Integer oldIndexId){
		JsonResWrapper jrw = new JsonResWrapper();
		String[] itemIds=oldItemIdss.split(",");
		IndexTb oldIndex = indexTbService.queryById(oldIndexId);
		List<IndexItemTb> olditems = indexItemTbService.queryIndexItemTbsByIndexId(oldIndexId);		
		IndexTb newIndex = indexTbService.queryById(Integer.valueOf(newIndexId));
		List<IndexItemTb> newitems = indexItemTbService.queryIndexItemTbsByIndexId(Integer.valueOf(newIndexId));	
		String oldTbName=oldIndex.getIndexCode()+"_tb";
		String newTbName=newIndex.getIndexCode()+"_tb";
		StringBuffer sql = new StringBuffer("INSERT INTO "+newTbName+"(DEFAULT_INDEX_ITEM_ID,SYS_ORG_ID,SYS_AREA_ID,"+
				"RECORD_DATE,SUBMIT_TIME");
		for (IndexItemTb indexItemTb : newitems) {
			sql.append(","+indexItemTb.getIndexItemCode());
		}
		sql.append(") select DEFAULT_INDEX_ITEM_ID,SYS_ORG_ID,SYS_AREA_ID,RECORD_DATE,SUBMIT_TIME");
		for (int i=0;i<olditems.size();i++) {
			for(int j=0;j<itemIds.length;j++){
				if(olditems.get(i).getIndexItemId().intValue()==Integer.parseInt(itemIds[j]))
				sql.append(","+olditems.get(j).getIndexItemCode());
			}
		}
		sql.append(" from "+oldTbName);
		
		Map<String, String> sqlmap = new HashMap<>();
		sqlmap.put("sql", sql.toString());		
		try {
			indexItemTbService.addColumnBySql(sqlmap);	
			jrw.setFlag(true);
			jrw.setMessage("导入成功");
		} catch (Exception e) {
			e.printStackTrace();
			jrw.setFlag(false);
			jrw.setMessage("导入失败");			
		}
		return jrw;
	}
	
	/**
	 * 提为省级指标
	 */
	@RequestMapping("/copyl")
	public String copyl(Integer indexId, Model model){
		//原市级指标
		IndexTb indexTb = indexTbService.queryById(indexId);
		model.addAttribute("indexTb", indexTb);
		Integer indexAreaId = indexTb.getSysAreaId();
		List<Integer> areaids = new ArrayList<>();
		areaids.add(indexAreaId);
		//List<IndexItemTb> itemList = indexItemTbService.queryItemsByAreaIds(indexId, areaids);
		List<IndexItemTb> itemList = indexItemTbService.queryIndexItemTbsByIndexId(indexId);
		//原市级指标的指标项集合，应该也全是该市的指标项
		model.addAttribute("itemList", itemList);
		return "indexTb/copyl";
	}

	/**
	 * 指标详情页面
	 */
	@RequestMapping("/index")
	public String index(Integer indexId, Model model) {
		IndexTb indexTb = indexTbService.queryById(indexId);
		model.addAttribute("indexTb", indexTb);
		// 登录区域的id
		Integer aId = getOrgRedis().getSys_area_id();
//		SysArea a= sysAreaService.getUpOrThisSysArea(aId);
//		aId=a.getSysAreaId();
		//获取当前地区以及上\下级地区的id
		List<Integer> areaids= sysAreaService.getAllUpAreaIds(aId);
		List<Integer> dwonAreaids =sysAreaService.getAllSubAreaIds(aId);
		dwonAreaids.remove(0);
		//如果登陆地区为该指标所在地区的上级，则可以将其提为省级指标
		if(dwonAreaids.contains(indexTb.getSysAreaId())){
			model.addAttribute("flag", true);
		}
		areaids.addAll(dwonAreaids);
		List<IndexItemTb> itemList = indexItemTbService.queryItemsByAreaIds(indexId, areaids);	
		for (IndexItemTb indexItemTb : itemList) {
				Integer indexItemId = indexItemTb.getIndexItemId();
				// 通过指标项id和区域id查询别名
				IndexItemAlias indexItemAlias = indexItemAliasService
						.selectByIndexItemIdAndAreaId(indexItemId, aId);
				if (indexItemAlias != null) {
					indexItemTb.setIndexItemName(indexItemAlias.getIndexItemAliasName());
				}
				SysArea upa=sysAreaService.queryParentAreasById(indexItemTb.getSysAreaId());
				if(CollectionUtils.isNotEmpty(upa.getSubArea())){					
					upa=upa.getSubArea().get(0);
				}
				if("1".equals(upa.getSysAreaType())){//
					indexItemTb.setShi(upa.getSysAreaName());
				}
		}
		model.addAttribute("itemList", itemList);
		return "indexTb/index";
	}

		/**
		 * 修改前校验
		 */
		@RequestMapping("/checkArea")
		@ResponseBody
		public JsonResWrapper checkArea(@RequestParam(required = false)Integer indexId, Model model,
				@RequestParam(required = false) Integer indexItemId){
			JsonResWrapper jrw = new JsonResWrapper();
			IndexItemTb indexitem = indexItemTbService.queryIndexItemTbById(indexItemId);
			Integer nowAid= getOrgRedis().getSys_area_id();//当前登录区域
			if (indexitem.getSysAreaId().intValue() != nowAid.intValue()) {//如果指标项的区域id和当前登录地区id不相同
				jrw.setFlag(false);
				jrw.setMessage("该指标项不属于当前区域，不允许修改");
			}else{
				jrw.setFlag(true);
			}
			return jrw;
		}
		
		/**
		 * 指标新增前校验：下级地区可以在上级和同级地区所建的指标内新建指标项，
		 * 上级地区不能在下级地区所建的指标内新建指标
		 */
		@RequestMapping("/checkBeforeAdd")
		@ResponseBody
		public JsonResWrapper checkBeforeAdd(@RequestParam(required = false)Integer indexId){
			JsonResWrapper jrw = new JsonResWrapper();
			Integer nowAid= getOrgRedis().getSys_area_id();//当前登录区域
			//获取当前登录区域上级和自己的ids
			List<Integer> ids=sysAreaService.getAllUpAreaIds(nowAid);
			IndexTb it = indexTbService.queryById(indexId);
			if(!ids.contains(it.getSysAreaId())){
				jrw.setFlag(false);
				jrw.setMessage("上级地区不能在下级地区所建的指标内新建指标");
			}else{
				jrw.setFlag(true);
			}
			return jrw;
		}
		
		
		/**
		 * 查看、修改指标项
		 * 
		 * @throws Exception
		 */
		@RequestMapping(value = "/lookOrModifyItem")
		public String lookOrModifyItem(@RequestParam(required = false) Integer indexItemId, Model model) throws Exception {
			model.addAttribute("typ", 1);
			// 查看、修改
			IndexItemTb indexitem = indexItemTbService.queryIndexItemTbById(indexItemId);
			SysOrg so = getOrgRedis();
			//获取当前地区以及上级地区的id
			List<Integer> areaids= sysAreaService.getAllUpAreaIds(so.getSys_area_id());
			Integer itemOrgId=indexitem.getSys_org_id();
			//如果当前登陆机构与要查看的指标项的所建机构相同
			if(so.getSys_org_id().intValue() == itemOrgId){
				model.addAttribute("orgFlag", 1);
			}else{
				model.addAttribute("orgFlag", 0);
			}
			Integer nowAid= so.getSys_area_id();//当前登录区域
				// 通过指标项id和区域id查询别名
				IndexItemAlias indexItemAlias = indexItemAliasService
						.selectByIndexItemIdAndAreaId(indexItemId, nowAid);
				if (indexItemAlias != null) {
					indexitem.setIndexItemAliasName(indexItemAlias.getIndexItemAliasName());
				}
				model.addAttribute("indexitem", indexitem);
				// 根据指标项id查询对应的指标大类
				IndexTb indexT = indexTbService.queryById(indexitem.getIndexId());
				model.addAttribute("indexT", indexT);
				// 根据指标大类的id查询所属区域
				Integer areaId = indexitem.getSysAreaId();
				String key = RedisKeys.SYS_AREA + areaId;
				SysArea area = RedisUtil.getObjData(key, SysArea.class);
				if (area == null) {
					area=sysAreaService.queryAreaById(areaId);
					RedisUtil.setData(key, area);
				}
				
				model.addAttribute("area", area);
				if (indexitem.getIndexItemType() == 3) {// 如果数据类型为字典
					Dic dic = dicService.getDicByDicId(indexitem.getDicId());
					model.addAttribute("dic", dic);
				}
				
				List<Dic> dicList = dicService.getDicsBySysAreaIdHaveThree(null, areaids);
				model.addAttribute("dicList", dicList);
				return "indexTb/edit";
		}

	/**
	 * 新增指标项页面
	 * @throws Exception
	 */
	@RequestMapping("/addSon")
	public String addson(Model model, Integer indexId) throws Exception {
		model.addAttribute("typ", 0);
		IndexTb indexT = indexTbService.queryById(indexId);
		model.addAttribute("indexT", indexT);
		SysOrg so=getOrgRedis();
		//获取当前地区以及上级地区的id
		List<Integer> areaids= sysAreaService.getAllUpAreaIds(so.getSys_area_id());
		List<IndexItemTb> itemList=indexItemTbService.queryItemsByAreaIds(indexId,areaids);
		Integer num= itemList.size();
		model.addAttribute("num", num+3);
		
		// 根据指标大类的id查询所属区域
//		Integer areaId = indexT.getSysAreaId();
//		SysArea a=sysAreaService.getUpOrThisSysArea(so.getSys_area_id());
		String key = RedisKeys.SYS_AREA + so.getSys_area_id();
		SysArea area = RedisUtil.getObjData(key, SysArea.class);
		if (area == null) {
			area=sysAreaService.queryAreaById(so.getSys_area_id());
			RedisUtil.setData(key, area);
		}
		
		model.addAttribute("area", area);
		List<Dic> dicList = dicService.getDicsBySysAreaIdHaveThree(null, areaids);
		dicService.reSetDIcName(dicList);
		model.addAttribute("dicList", dicList);						
		return "indexTb/edit";
	}
	
/**
	 * 保存指标项
	 */
	@RequestMapping(value = "/saveIndexItem")
	@ResponseBody
	public JsonResWrapper saveIndexItem(@RequestParam(required = false) Integer typ,
			@RequestParam(required = false) Integer theDicId, @RequestParam(required = false) Integer indexItemId,
			@RequestParam(required = false) Integer indexId, @RequestParam(required = false) String indexName,
			@RequestParam(required = false) String indexItemName,
			@RequestParam(required = false) Integer indexItemNumber,
			@RequestParam(required = false) Integer indexItemUsed,
			@RequestParam(required = false) Integer indexItemType,
			@RequestParam(required = false) Integer indexItemImportUnique,
			@RequestParam(required = false) Integer indexItemEmpty,
			@RequestParam(required = false) String[] indexItemNetId,
			@RequestParam(required = false) String indexItemNotes,
			@RequestParam(required = false) String indexItemAliasName,
			@RequestParam(required = false) Integer varLength, HttpServletRequest request,
			HttpServletResponse response) {
		JsonResWrapper jrw = new JsonResWrapper();
		try {
			SysOrg so = getOrgRedis();// 获取机构信息
			MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			//查询该指标下该指标项中文名是否存在
			IndexItemTb iit0=indexItemTbService.getIndexItemsByIndexIdAndName(indexId, indexItemName);
			if(iit0!=null){
				jrw.setFlag(false);
				jrw.setMessage("该指标项名称已使用，请更换");
				return jrw;
			}
			//先查询别名名字是否已使用，若已使用则返回消息
			IndexItemAlias iia = indexItemAliasService.selectByAliasNameAndAreaId(indexId, indexItemAliasName, so.getSys_area_id());
			if(iia !=null && iia.getIndexItemId().intValue() != indexItemId.intValue()){
				jrw.setFlag(false);
				jrw.setMessage("该指标项的别名已使用，请更换");
				return jrw;
			}
			IndexTb iT = indexTbService.queryById(indexId);
			if (typ == 0) { // =================================新--增===================================
				Integer areaId = iT.getSysAreaId();//指标所属区域id
				List<Integer> aids=sysAreaService.getAllSubAreaIds(areaId.intValue());//指标自己及下级地区id
				//如果登陆区域不在指标所属区域或下级区域在，则不可以对其新增指标项
				if (!aids.contains(so.getSys_area_id().intValue())) {
					jrw.setFlag(false);
					jrw.setMessage("当前登录区域是该指标大类的所属区域的上级地区，不能新增指标项");
					return jrw;
				}				
				// 根据指标大类的id查询所属区域
				String key = RedisKeys.SYS_AREA + areaId;
				//指标大类所属地区
				SysArea area = RedisUtil.getObjData(key, SysArea.class);
				if (area == null) {
					area =sysAreaService.queryAreaById(areaId);
					RedisUtil.setData(key, area);
				}
				IndexItemTb item = new IndexItemTb();
				item.setIndexId(indexId);// （外键）指标大类id
				item.setIndexItemName(indexItemName); // 指标项名称
				// 将指标项名称中文首字母转成指标项代码,构建数据库列名
				String indexNameCode = iT.getIndexCode();
				String key2 = RedisKeys.SYS_AREA + so.getSys_area_id();
				//指标项所属地区(当前登陆机构区域)
				SysArea area2 = RedisUtil.getObjData(key2, SysArea.class);
				if (area2 == null) {
					area2 =sysAreaService.queryAreaById(so.getSys_area_id());
					RedisUtil.setData(key2, area2);
				}	
				String code=iT.getIndexCode() ;
				//获取登陆地区的上级地区id
				List<Integer> ups=sysAreaService.getAllUpAreaIds(area2.getSysAreaId());
				//remove自己
				ups.remove(0);
				if(ups.contains(areaId)){//指标为登陆地区的上级地区所创建
					//追加登陆地区的名字
					if("0".equals(area.getSysAreaType()) &&  "2".equals(area2.getSysAreaType())
							){//如果指标是四川建的并且当前登陆为县
						SysArea upa = sysAreaService.queryParentAreasById(area2.getSysAreaId());
						String upName= upa.getSubArea().get(0).getSysAreaName();
						code =code +"_"+ChineseFirstLetterUtil.getPinYinHeadChar(upName).toLowerCase()+
								"_"+ChineseFirstLetterUtil.getPinYinHeadChar(area2.getSysAreaName()).toLowerCase(); 
					}else{						
						code =code +"_"+ChineseFirstLetterUtil.getPinYinHeadChar(area2.getSysAreaName()).toLowerCase(); 
					}
				}
				code = code+"_"+ChineseFirstLetterUtil.getPinYinHeadChar(indexItemName).toLowerCase();
				// 根据 指标项代号 查询指标项是否已经存在
				IndexItemTb it0 = indexItemTbService.getIndexItemTbByCode(code);
				int m=1;
				while(it0!=null){
					code= code+"_"+m;
					it0 = indexItemTbService.getIndexItemTbByCode(code);
					m++;
				}
				item.setIndexItemCode(code); // 指标项code
				item.setIndexItemNumber(indexItemNumber); // 序号
				item.setSysAreaId(so.getSys_area_id()); // （外键）所属区域id
				item.setIndexItemUsed(indexItemUsed); // 是否启用，0否1（默认）是
				item.setIndexItemType(indexItemType); // 数据类型，0字符，1时间，2数值,3数据字典
				if (indexItemType == 0) {
					item.setVarLength(varLength);// 设置字符长度
				} else if (indexItemType == 3) {
					item.setDicId(theDicId); // (外键)数据字典id
				}
				item.setIndexItemImportUnique(indexItemImportUnique); // 是否识别码0否（默认）1是
				item.setIndexItemEmpty(indexItemEmpty); // 是否可以为空，0否，1 是（默认）
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < indexItemNetId.length; i++) {
					if ((i + 1) == indexItemNetId.length) {
						sb.append(indexItemNetId[i]);
					} else {
						sb.append(indexItemNetId[i]).append(",");
					}
				}
				sb.substring(0, sb.length() - 1);
				item.setIndexItemNetId(sb.toString()); // 网络标识
				item.setIndexItemNotes(indexItemNotes + " "); // 备注
				item.setSys_user_id(userDetails.getSys_user_id()); // （外键）创建者id
				item.setSys_org_id(so.getSys_org_id()); // （外键）创建机构id
				item.setIndexItemId(null);
				
				// 保存前验证
				if (indexItemType == 3) {// 如果数据类型为字典，则必须选择一个字典类型
					if (item.getDicId() == null) {
						jrw.setFlag(false);
						jrw.setMessage("请选择具体的字典类型");
						return jrw;
					}
				}
				// 保存指标项
				try{	
					//先建表对应的字段
					indexItemTbService.insertColumn(indexNameCode,code,indexItemType,varLength*2,indexItemEmpty);// 新增列
					//再保存到指标项表
					indexItemTbService.insertIndexItemTb(item);
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, 
							indexId, indexItemId, null, item.toString(), new Date(), 
							1, 1, null, null, null, null, null, true),request);
					if (!StringUtils.isBlank(indexItemAliasName)) {// 将别名保存在别名表中
						// 查询刚保存的这个指标项的id，用于保存别名
						IndexItemTb i = indexItemTbService.getIndexItemTbByCode(code);
						IndexItemAlias alias = new IndexItemAlias();
						alias.setIndexId(indexId);
						alias.setIndexItemAliasName(indexItemAliasName);
						alias.setIndexItemId(i.getIndexItemId());
						alias.setSysAreaId(so.getSys_area_id());
						indexItemAliasService.save(alias);
					}
				}catch(Exception e){					
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, 
							indexId, indexItemId, null, item.toString(), new Date(), 
							1, 1, null, null, null, null, null, false),request);
					jrw.setFlag(false);
					jrw.setMessage("保存失败");
					return jrw;
				}
			} else {// =====================================修---改======================================
				IndexItemTb iit = indexItemTbService.queryIndexItemTbById(indexItemId);
				String oldValue =iit.toString();
				
					IndexTb it = indexTbService.queryById(iit.getIndexId());
					String tbName=it.getIndexCode()+"_tb";
					String U = tbName.toUpperCase();
					if(iit.getIndexItemType()==0 &&iit.getVarLength().intValue()!=varLength){						
						StringBuffer sql2 =new StringBuffer("ALTER TABLE "+
								tbName +" alter COLUMN "+ iit.getIndexItemCode() + " SET DATA TYPE VARCHAR("+varLength*2+")");
					Map<String, String> sqlmap2 = new HashMap<>();
					Map<String, String> sqlmap3 = new HashMap<>();
					sqlmap2.put("sql", sql2.toString());
					try {
					indexItemTbService.addColumnBySql(sqlmap2);
					sqlmap3.put("sql", "CALL SYSPROC.ADMIN_CMD('REORG TABLE ADMINISTRATOR."+U+"')");
					indexItemTbService.addColumnBySql(sqlmap3);
					} catch (Exception e) {
						e.printStackTrace();
						jrw.setFlag(false);
						jrw.setMessage("更新失败");
						return jrw;
					}
					}			
				// 通过指标项id和区域id查询别名
				IndexItemAlias indexItemAlias = indexItemAliasService
						.selectByIndexItemIdAndAreaId(iit.getIndexItemId(), so.getSys_area_id());
				if(indexItemAlias ==null && !StringUtils.isBlank(indexItemAliasName)){									
					IndexItemAlias alias = new IndexItemAlias();
					alias.setIndexItemAliasName(indexItemAliasName);
					alias.setIndexItemId(iit.getIndexItemId());
					alias.setSysAreaId(so.getSys_area_id());
					alias.setIndexId(indexId);
					indexItemAliasService.save(alias);
				}else{
					if(indexItemAlias !=null){						
						if(!indexItemAlias.getIndexItemAliasName().equals(indexItemAliasName) 
								&& !StringUtils.isBlank(indexItemAliasName)){
							indexItemAliasService.update(indexItemAlias.getIndexItemAliasId(), indexItemAliasName);
						}else if(!indexItemAlias.getIndexItemAliasName().equals(indexItemAliasName) 
								&& StringUtils.isBlank(indexItemAliasName)){
							indexItemAliasService.deleteById(indexItemAlias.getIndexItemAliasId());
						}
					}
				}
				// 更新指标项
				iit.setIndexItemEmpty(indexItemEmpty);
				iit.setIndexItemNotes(indexItemNotes);
				iit.setIndexItemImportUnique(indexItemImportUnique);
				iit.setIndexItemUsed(indexItemUsed);
				iit.setIndexItemNumber(indexItemNumber);
				iit.setVarLength(varLength);
				try{					
					indexItemTbService.updateIndexItemTb(iit);
					jrw.setFlag(true);
					jrw.setMessage("操作成功");
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, 
							indexId, indexItemId, oldValue, iit.toString(), new Date(), 
							3, 1, null, null, null, null, null, true),request);
					return jrw;
				}catch(Exception e){
					jrw.setFlag(false);
					jrw.setMessage("更新失败");
					sysManageLogService.insertSysManageLogTb(new SysManageLog("指标设置", null, 
							indexId, indexItemId, oldValue, iit.toString(), new Date(), 
							3, 1, null, null, null, null, null, false),request);
					return jrw;
				}
			}
			jrw.setFlag(true);
			jrw.setMessage("保存成功");
		} catch (Exception e) {
			e.printStackTrace();
			jrw.setFlag(false);
			jrw.setMessage("出错");
		}
		return jrw;
	}

	
	
	/**
	 * 导出指标项到excel
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/exportIndexItem")
	@ResponseBody
	public void exportIndexItem(Integer indexId, HttpServletResponse response, HttpServletRequest request) throws Exception {
			IndexTb indexTb = indexTbService.queryById(indexId);
			//获取启用的指标项
			//获取当前地区以及上级地区的id
			SysOrg so=getOrgRedis();
			List<Integer> areaids= sysAreaService.getAllUpAreaIds(so.getSys_area_id());
			List<IndexItemTb> itemList = indexItemTbService.getIndexIntemsIsUsedByIdAndAreaIds(indexId, areaids);
			String[] colNames = new String[]{"编号","指标大类名称","指标项名称","指标项说明",
					"数据类型","长度及精度","允许取值范围","是否是识别码","能否为空","网路标识（A人行，B局域网，C互联网）"};
			String title = indexTb.getIndexName()+"指标项";
			// 声明一个工作薄
			HSSFWorkbook wb = new HSSFWorkbook();
			// 得到一个POI的工具类  
	        CreationHelper factory = wb.getCreationHelper();
	        // 生成一个样式
			HSSFCellStyle style = wb.createCellStyle();
			HSSFCellStyle style2 = wb.createCellStyle();
			// 样式字体居中
			style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			style2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			//垂直居中
			style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			style2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			// 生成一个字体
			HSSFFont font = wb.createFont();
			font.setColor(HSSFColor.VIOLET.index);
			font.setFontHeightInPoints((short) 12);
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			// 把字体应用到当前的样式
			style.setFont(font);
			// 声明一个sheet并命名
			HSSFSheet sheet = wb.createSheet(indexTb.getIndexName());
			// 定义单元格宽度
			sheet.setDefaultColumnWidth((short) 25);
//			// 创建第一行
//			HSSFRow row0 = sheet.createRow(0);
//			HSSFCell cell0 = row0.createCell((short) 0);
//			cell0.setCellValue(indexTb.getIndexName());
//			cell0.setCellStyle(style);
			sheet.addMergedRegion(new CellRangeAddress(1,itemList.size(),1,1));
			// 创建第一行,列名
			HSSFRow row = sheet.createRow(0);
			HSSFCell cell = null;
			for (int i = 0; i < colNames.length; i++) {
				cell = row.createCell((short) i);
				cell.setCellValue(colNames[i]);
				cell.setCellStyle(style);
			}
			//第二行开始为每一个指标项信息
			for(int k =0;k<itemList.size();k++){
				IndexItemTb item = itemList.get(k); 
				HSSFRow rowi = sheet.createRow(k+1);
				for (int i = 0; i < colNames.length; i++) {
					HSSFCell celli = rowi.createCell((short) i);
					switch(i){
						case 0://编号
							celli.setCellValue(k+1);
							break;
						case 1://指标大类名称
							celli.setCellValue(indexTb.getIndexName());
							break;
						case 2://指标项名称
							celli.setCellValue(item.getIndexItemName());
							break;
						case 3://指标项名称
							celli.setCellValue(item.getIndexItemNotes());
							break;
						case 4://数据类型
							String type = null;
							if(item.getIndexItemType() ==0)
								type ="字符";
							if(item.getIndexItemType() ==1)
								type ="时间";
							if(item.getIndexItemType() ==2)
								type ="数值";
							if(item.getIndexItemType() ==3)
								type ="字典";
							celli.setCellValue(type);
							break;
						case 5://长度及精度
							if(item.getIndexItemType() ==0)
								celli.setCellValue(item.getVarLength()==null?0:item.getVarLength());
							else
								celli.setCellValue("");
							break;
						case 6://允许取值范围 ,是否为字典
							if(item.getIndexItemType() ==3)
								celli.setCellValue("代码型数据项（见字典表）");
							else
								celli.setCellValue("");
							break;
						case 7://是否是识别码
							if(item.getIndexItemImportUnique() ==1)
								celli.setCellValue("识别码，唯一");
							else
								celli.setCellValue("否");
							break;
						case 8://能否为空
							if(item.getIndexItemEmpty()==0)
								celli.setCellValue("不可为空");
							else
								celli.setCellValue("可以为空");
							break;
						default://网路标识（A人行，B局域网，C互联网）
							String vd = null;
							if(item.getIndexItemNetId().equals("0"))
								vd = "A";
							else if(item.getIndexItemNetId().equals("1"))
								vd = "B";
							else if(item.getIndexItemNetId().equals("2"))
								vd = "C";
							else if(item.getIndexItemNetId().equals("0,1"))
								vd = "A,B";
							else if(item.getIndexItemNetId().equals("0,2"))
								vd = "A,C";
							else if(item.getIndexItemNetId().equals("1,2"))
								vd = "B,C";
							else if(item.getIndexItemNetId().equals("0,1,2"))
								vd = "A,B,C";
							celli.setCellValue(vd);
							break;
					}
					celli.setCellStyle(style2);
				}
			}
			try {
				response.setContentType("application/vnd.ms-excel"); 
				response.setCharacterEncoding("utf-8");
		        response.setHeader("Content-disposition", "attachment;filename="+new String(title.getBytes("gbk"), "iso8859-1")+".xls"); 
		        OutputStream ouputStream = response.getOutputStream(); 
		        wb.write(ouputStream);    
		        ouputStream.flush();    
		        ouputStream.close();    
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	/**
	 * 查询所有数据字典
	 * 
	 * @throws Exception
	 */
	@RequestMapping(value = "/getAllDic", method = RequestMethod.POST)
	public List<Dic> getAllDic() throws Exception {
		List<Dic> dicList = dicService.getAllDic(null);
		return dicList;
	}

	/**
	 * 根据指标大类id返回指标项集合(json)
	 * 
	 * @param indexId
	 * @return
	 */
	@RequestMapping("/getIndexItemTbListJson")
	@ResponseBody
	public Map<String, Object> getIndexItemTbListJson(@RequestParam(required = false) String indexCode) {
		Map<String, Object> map = new HashMap<String, Object>();
		SysOrg so=getOrgRedis();
		
		//获取当前地区以及上级地区的id
		List<Integer> areaids= sysAreaService.getAllUpAreaIds(so.getSys_area_id());
		List<IndexItemTb> indexItemTbList=indexItemTbService.queryItemsByAreaIds(indexTbService.queryIdByCode(indexCode).getIndexId(),areaids);
		map.put("indexItemTbList", indexItemTbList);
		return map;
	}

	/**
	 * 获取机构，先走缓存，如果缓存没有，则将查询结果加入缓存
	 * 
	 * @return SysOrg
	 */
	private SysOrg getOrgRedis() {
		MyUserDetails userDetails = (MyUserDetails) SecurityContextHolder.getContext().getAuthentication()
				.getPrincipal();
		// 根据机构ID获取机构缓存
		String orgKey = RedisKeys.SYS_ORG + userDetails.getSys_org_id();
		SysOrg so = RedisUtil.getObjData(orgKey, SysOrg.class);
		// 如果没有缓存，则查询并载入缓存
		if (so == null) {
			so = sysOrgService.queryInstitutionsById(userDetails.getSys_org_id());
			RedisUtil.setData(orgKey, so);
		}
		return so;
	}

}
