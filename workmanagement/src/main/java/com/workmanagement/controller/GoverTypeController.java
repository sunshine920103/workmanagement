package com.workmanagement.controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.google.gson.Gson;
import com.workmanagement.json.JsonResWrapper;
import com.workmanagement.model.GoverType;
import com.workmanagement.model.SysManageLog;
import com.workmanagement.service.GoverTypeService;
import com.workmanagement.service.SysManageLogService;
import com.workmanagement.util.DataUtil;
import com.workmanagement.util.ExcelExport;
import com.workmanagement.util.LoggerUtil;
import com.workmanagement.util.PageSupport;

/**
 * 政府部门类型
 * @author tianhao
 *
 */
@Controller
@RequestMapping(value="/admin/goverType")
public class GoverTypeController {
	
	@Autowired
	private GoverTypeService goverTypeService;
	
	@Autowired
	private SysManageLogService sysManageLogService;

	/**
	 * 政府部门类型列表页面
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/list")
	public String list(HttpServletRequest request, Model model,
			@RequestParam(required=false) String sysGovTypeName,@RequestParam(required=false) Integer url) throws Exception{
		PageSupport ps = PageSupport.initPageSupport(request);
		GoverType gt=new GoverType();
		gt.setSysGovTypeName(sysGovTypeName);
		List<GoverType> its = DataUtil.isEmpty(goverTypeService.queryGoverTypeList(ps,gt));
		if(sysGovTypeName!=null && !"".equals(sysGovTypeName)){
			if(url==null){
				List<GoverType> govTypeList = DataUtil.isEmpty(goverTypeService.queryGoverTypeList(null,gt));
				String sql="SELECT * FROM SYS_GOV_TYPE_TB WHERE 1=1 AND SYS_GOV_TYPE_NAME LIKE '%"+sysGovTypeName+"%' ORDER BY SYS_GOV_TYPE_CODE";
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门类型");
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(govTypeList.size());
				sm.setSysManageLogOperateType(SysManageLog.SELECT_SYSMANAGElOG);
				sm.setSysManageLogQueryUserCondition(sysGovTypeName+"(通过名称模糊查询)");
				sm.setSysManageLogQuerySql(sql);
				sm.setSysManageLogUrl("/admin/goverType/list.jhtml?sysGovTypeName="+sysGovTypeName+"&url=1");
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}
		model.addAttribute("sysGovTypeName", sysGovTypeName);
		model.addAttribute("its", its);
		return "goverType/list";
	}
	
	/**
	 * 增加、修改政府部门类型
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/add")
	public String add(HttpServletRequest request,Model model,
			@RequestParam(required=false) Integer id) throws Exception{
		if(id!=null){
			GoverType it=goverTypeService.queryGoverTypeById(id);
			model.addAttribute("it", it);
		}
		GoverType gt=new GoverType();
		List<GoverType> its = DataUtil.isEmpty(goverTypeService.queryGoverTypeByNameList(gt));
		model.addAttribute("its", its);
		return "goverType/add";
	}
	
	/**
	 * 增加、修改政府部门类型
	 * @param reqeust
	 * @return
	 */
	@RequestMapping(value="/save")
	@ResponseBody
	public JsonResWrapper save(Model model,HttpServletRequest request,
			@RequestParam(required=false) Integer sysGovTypeId,
			@RequestParam(required=false) Integer sysGovTypeUpid,
			@RequestParam(required=false) String sysGovTypeUpname,
			@RequestParam(required=false) String sysGovTypeUpcode,
			@RequestParam(required=false) String sysGovTypeName,
			@RequestParam(required=false) String sysGovTypeCode,
			@RequestParam(required=false) String sysGovTypeNotes) throws Exception{
		JsonResWrapper jrw = new JsonResWrapper();
		GoverType gt=new GoverType();
		gt.setSysGovTypeId(sysGovTypeId);
		gt.setSysGovTypeName(sysGovTypeName);
		gt.setSysGovTypeCode(sysGovTypeCode);
		gt.setSysGovTypeNotes(sysGovTypeNotes);
		gt.setSysGovTypeUpid(sysGovTypeUpid);
		//判断政府部门名称和代码是否为空
		if(StringUtils.isBlank(sysGovTypeName) || StringUtils.isBlank(sysGovTypeCode)){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，参数缺失");
			return jrw;
		}
		//判断是否选择自己为自己的上级
		if(sysGovTypeUpid!=null && sysGovTypeId!=null){
			GoverType sum=goverTypeService.queryGoverTypeById(sysGovTypeId);
			GoverType num=goverTypeService.queryGoverTypeById(sysGovTypeUpid);
			if(sum.getSysGovTypeUpid()==sysGovTypeUpid){
				if(sum.getSysGovTypeUpid()==num.getSysGovTypeUpid()){
					jrw.setFlag(false);
					jrw.setMessage("保存失败，不能选择自己为自己的上级");
					return jrw;
				}
			}
		}
		//判断政府部门类型名称是否重复
		int nameCount=goverTypeService.queryGoverTypeByNameCount(gt);
		if(nameCount>0){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，政府部门类型名称重复");
			return jrw;
		}
		//判断政府部门类型编码是否重复
		int codeCount=goverTypeService.queryGoverTypeByCodeCount(gt);
		if(codeCount>0){
			jrw.setFlag(false);
			jrw.setMessage("保存失败，政府部门类型编码重复");
			return jrw;
		}
		//判断与上级类型编码是否匹配
		if(sysGovTypeUpid!=null && !"".equals(sysGovTypeUpid)){
			if(sysGovTypeUpcode!=null){
				if(sysGovTypeCode.length()>=4){
					String govTypeCode=sysGovTypeCode.substring(0, 4);
					if(!govTypeCode.equals(sysGovTypeUpcode)){
						jrw.setFlag(false);
						jrw.setMessage("保存失败，与上级类型编码不匹配");
						return jrw;
					}
				}else{
					jrw.setFlag(false);
					jrw.setMessage("保存失败，政府类型编码不符合要求");
					return jrw;
				}
			}
		}
		SysManageLog sm=new SysManageLog();
		//判断是新增还是修改
		if(sysGovTypeId==null){
			if(gt.getSysGovTypeUpid()==null){
				gt.setSysGovTypeUpid(0);
			}
			goverTypeService.saveGoverType(gt);
			sm.setSysManageLogOperateType(SysManageLog.INSERT_SYSMANAGElOG);
		}else{
			int count1=goverTypeService.queryGoverTypeByIdCount(sysGovTypeId);
			//判断政府部门类型是否被使用
			if(count1 > 0){
				jrw.setFlag(false);
				jrw.setMessage("保存失败，该政府部门类型已被使用");
				return jrw;
			}
			goverTypeService.updateGoverType(gt);
			sm.setSysManageLogOperateType(SysManageLog.UPDATE_SYSMANAGElOG);
		}
		sm.setSysManageLogMenuName("政府部门类型");
		sm.setSysManageLogResult(true);
		sm.setSysManageLogCount(1);
		sysManageLogService.insertSysManageLogTb(sm, request);
		jrw.setMessage("保存成功");
		return jrw;
	}
	
	/**
	 * 通过ID删除政府部门类型
	 * @param id 机构类别ID
	 * @return
	 */
	@RequestMapping("/del")
	@ResponseBody
	public JsonResWrapper del(@RequestParam(required=false) Integer id) throws Exception{
		JsonResWrapper jrw = new JsonResWrapper();
		GoverType it=goverTypeService.queryGoverTypeById(id);
		if(id==null || it==null){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，参数缺失");
			return jrw;
		}
		int count=goverTypeService.queryGoverTypeByIdCount(id);
		//判断政府部门类型是否被使用
		if(count > 0){
			jrw.setFlag(false);
			jrw.setMessage("删除失败，该政府部门类型已被使用");
			return jrw;
		}
		goverTypeService.delGoverTypeById(id);
		jrw.setMessage("删除成功");
		return jrw;
	}
	
	/**
	 * 通过政府类别名称查找政府代码
	 * @param name
	 * @return
	 */
	@RequestMapping("/getcode")
	@ResponseBody
	public void getcode(@RequestParam(required=false) String name,HttpServletResponse response) throws IOException{
		//查找机构类别代码
		String code=goverTypeService.queryGoverTypeCodeByName(name);
		Gson gson=new Gson();
		response.getWriter().write(gson.toJson(code));
	}
	
	/**
	 * 导出全部数据EXCEL
	 * @param model
	 * @param 
	 * @return
	 */
	@RequestMapping("/exportAll")
	@ResponseBody
	public void downFile(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(required=false) String name) {
		try{
			
			List<GoverType> list;
			if(name!=null || !"".equals(name)){
				list = goverTypeService.queryTypeAll(name);
			}else{
				list = goverTypeService.queryTypeAll(null);
			}
			String[] rowNames = { "政府类型名称", "政府类型编码", "备注"};
			String[] propertyNames = { "sysGovTypeName", "sysGovTypeCode", "sysGovTypeNotes"};
			
			if(list!=null){
				// 生成excel
				ExcelExport<GoverType> excelExport = new ExcelExport<>();
				excelExport.setTitle("政府部门类型");
				excelExport.setRowNames(rowNames);
				excelExport.setPropertyNames(propertyNames);
				excelExport.setList(list);
				String url=excelExport.exportExcel(request,response);
				
				SysManageLog sm=new SysManageLog();
				sm.setSysManageLogMenuName("政府部门类型");
				sm.setSysManageLogOperateType(SysManageLog.EXPORT_SYSMANAGElOG);
				sm.setSysManageLogResult(true);
				sm.setSysManageLogCount(list.size());
				sm.setSysManageLogFile(url);
				sysManageLogService.insertSysManageLogTb(sm, request);
			}
		}catch (Exception e){
			LoggerUtil.error(e);
		}
	}
	
	/**
	 * 模糊查询
	 * @param model
	 * @param 
	 * @return
	 * @throws IOException 
	 */
	@RequestMapping("/goverTypeByName")
	@ResponseBody
	public List<GoverType> goverTypeByName(HttpServletRequest request,@RequestParam(required=false) String name) throws Exception{
		GoverType gt=new GoverType();
		gt.setSysGovTypeName(name);
		List<GoverType> sos = goverTypeService.queryGoverTypeByNameList(gt);
		return sos;
	}
}
