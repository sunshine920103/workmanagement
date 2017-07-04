package com.workmanagement.service;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.tools.ant.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.workmanagement.dao.IndexItemAliasDao;
import com.workmanagement.dao.ReportIndexDao;
import com.workmanagement.dao.SysCheckDao;
import com.workmanagement.enums.BothEnum;
import com.workmanagement.model.DefaultIndexItem;
import com.workmanagement.model.Dic;
import com.workmanagement.model.DicContent;
import com.workmanagement.model.IndexItemAlias;
import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.IndexTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.model.SysArea;
import com.workmanagement.model.SysClassFyModel;
import com.workmanagement.model.SysOrg;
import com.workmanagement.util.ExcelNumberUtil;
import com.workmanagement.util.UpLoadFile;

/**
 * excel报送
 * 
 * @author wqs
 */
@Service
public class ExcelReportServiceImpl implements ExcelReportService {

	@Autowired
	private IndexItemTbService indexItemTbService;
	@Autowired
	private DefaultIndexItemService defaultIndexItemService;
	@Autowired
	private SysCheckService sysCheckService;
	@Autowired
	private DicContentService dicContentService;
	@Autowired
	private ReportIndexDao reportIndexDao;
	@Autowired
	private SysClassFyService sysClassFyService;
	@Autowired
	private SysOrgService sysOrgService;
	@Autowired
	private SysAreaService sysAreaService;
	@Autowired
	private IndexItemAliasDao indexItemAliasDao;
	@Autowired
	private DicService dicService;
	@Autowired
	private SysCheckDao sysCheckDao;

	/**
	 * 报送基本信息保存二码
	 */
	@Override
	public void saveDafults(int rowCount,String tempTbName, List<String> errorList, SysArea a, List<Map<String, Object>> qymcMsg) {
		List<Map<String, Object>> excelList = reportIndexDao.selectAllFormTempTb(tempTbName);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		for (int i = 0; i < excelList.size(); i++) {
			Map<String, Object> map = excelList.get(i);
			pool.execute(new saveDefaultInner(rowCount,map, errorList, i, a));
		}
		pool.shutdown();// 停止接收新线程
		while (true) {
			if (pool.isTerminated()) {// 线程全部结束
				break;
			}
		}
	}

	private class saveDefaultInner implements Runnable {
		private Map<String, Object> map;
		private List<String> errorList;
		private int i;
		private SysArea a;
		private int rowCount;

		public saveDefaultInner(int rowCount,Map<String, Object> map, List<String> errorList, int i, SysArea a) {
			super();
			this.map = map;
			this.errorList = errorList;
			this.i = i;
			this.a = a;
			this.rowCount= rowCount;
		}
		
		@Override
		public void run() {
				// 将每一条数据的二码与二码表对比，校验二码是否存在
				String codeCredit = (String) map.get("CODE_CREDIT");
				String codeOrg = (String) map.get("CODE_ORG");
				String qymc = (String) map.get("INDEX_JBXX_QYMC");
				if (!StringUtils.isBlank(codeCredit) && StringUtils.isBlank(codeOrg)) { // 只填写了社会信用码
					try {
						List<DefaultIndexItem> list = defaultIndexItemService.getByCredit(codeCredit, a.getSysAreaId());
						if (CollectionUtils.isEmpty(list)) { // 如果为空，表示二码表中没有这个企业，需要将该企业的二码信息存进二码表
							if (!sysCheckService.checkCodeCredit(codeCredit)) {
								errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行数据填写的统一社会信用代码输入错误:"+codeCredit);
							} else {
								// 保存进二码表
								DefaultIndexItem d = new DefaultIndexItem();
								d.setCodeCredit(codeCredit);
								d.setQymc(qymc);
								d.setSys_area_id(a.getSysAreaId());
								try {
									defaultIndexItemService.dinsert(d);
								} catch (Exception e) {
									errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行数据根据统一社会信用代码："+codeCredit+"保存二码失败：可能原因企业名称太长");
								}
							}
						}
					} catch (Exception e) {
						errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行，根据统一社会信用代码："+codeCredit+"查询二码表出现异常");
					}
					
				} else if (StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // excel只填写了组织机构码
					List<DefaultIndexItem> dl = null;
					try {
						dl = defaultIndexItemService.getByCodeOrg(codeOrg, a.getSysAreaId());
						if (CollectionUtils.isEmpty(dl)) { // 如果为空，表示二码表中没有这个企业，需要将该企业的二码信息存进二码表
							if (!sysCheckService.checkCodeOrg(codeOrg)) {
								errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行组织机构代码输入错误");
							} else {
								// 保存进二码表
								DefaultIndexItem d = new DefaultIndexItem();
								d.setCodeOrg(codeOrg);
								d.setQymc(qymc);
								d.setSys_area_id(a.getSysAreaId());
								try {
									defaultIndexItemService.dinsert(d);
								} catch (Exception e) {
									errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行数据根据组织机构代码："+codeOrg+"保存二码失败:可能原因企业名称太长");
								}
							}
						}
					} catch (Exception e) {
						errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行查询组织机构代码出现异常");
					}
				} else if (!StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // excel中二码都有的情况
					errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行，不能同时用统一社会信用代码和组织机构代码进行报送,请删除组织机构代码再重新上报");
				} else {// 社会信用码为空,且组织机构码为空，返回提示消息
					errorList.add("第" + getcol(i + 3,rowCount)+ (i + 3) + "行数据必须填写社会信用码或组织机构码");
				}
		}

	}

	private class InnerClass implements Runnable {
		private Map<String,String> amapVtC;
		private Map<String,String> amapCtV;
		private Map<String,String> imapVtC;
		private Map<String,String> imapCtV;
		private Map<String,String> omapVtC;
		private Map<String,String> omapCtV;
		private Map<String,Object> dmapVtC;
		private Map<String,Object> dmapCtV;		
		private int rowCount;
		private int cellCount;
		private Row row;
		private int r;
		private List<String> indexNames;
		private List<String> columnList;
		private String tempTbName;
		private SysOrg so;
		private List<String> errorList;
		private List<IndexItemTb> itemsList;
		private String[] isSuccess;
		private SysArea a;
		private Date reportDate;
		private Date submit;
		private SimpleDateFormat sdf;

		public InnerClass(Map<String,String> amapVtC,Map<String,String> amapCtV,
				Map<String,Object> dmapVtC,Map<String,Object> dmapCtV,
				Map<String,String> omapVtC,Map<String,String> omapCtV,
				Map<String,String> imapVtC,Map<String,String> imapCtV,
				int rowCount,int cellCount, Row row, int r, List<String> indexNames, List<String> columnList,
				String tempTbName, SysOrg so, List<String> errorList, List<IndexItemTb> itemsList, String[] isSuccess,
				SysArea a, Date reportDate, Date submit, SimpleDateFormat sdf) {
			super();
			this.amapVtC = amapVtC;
			this.amapCtV = amapCtV;
			this.imapVtC = imapVtC;
			this.imapCtV = imapCtV;
			this.omapVtC = omapVtC;
			this.omapCtV = omapCtV;
			this.dmapVtC = dmapVtC;
			this.dmapCtV = dmapCtV;
			this.rowCount = rowCount;
			this.cellCount = cellCount;
			this.row = row;
			this.r = r;
			this.indexNames = indexNames;
			this.columnList = columnList;
			this.tempTbName = tempTbName;
			this.so = so;
			this.errorList = errorList;
			this.itemsList = itemsList;
			this.isSuccess = isSuccess;
			this.a = a;
			this.reportDate = reportDate;
			this.submit = submit;
			this.sdf = sdf;
		}

		@Override
		public void run() {
			List<Object> dataList = new ArrayList<>();// 数据值，按excel中的顺序存放
			for (int c = 0; c < cellCount; c++) {// 遍历每一列
				Cell cell = row.getCell(c);
				if (cell == null) {
					cell = row.createCell(c);
					cell.setCellValue("");
				}
				String cellValue = null;
				int cellType = cell.getCellType();
				switch (cellType) {
				case Cell.CELL_TYPE_STRING: // 文本
					cellValue = cell.getStringCellValue().trim();
					if ("".equals(cellValue)) {
						cellValue = null;
					}
					break;
				case Cell.CELL_TYPE_NUMERIC: // 数字、日期
					if (DateUtil.isCellDateFormatted(cell)) {
						cellValue = sdf.format(cell.getDateCellValue()); // 日期型
					} else {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						cellValue = cell.getStringCellValue();
					}
					break;
				case Cell.CELL_TYPE_BLANK: // 空白
					cellValue = null;
				default:
				}
				// r==2开始
				if (c == 0) {
					dataList.add(cellValue);
				} else if (c == 1 && "CODE_ORG".equals(indexNames.get(2))) {
					dataList.add(cellValue);
				} else {
					if(StringUtils.isBlank((String)dataList.get(0)) && StringUtils.isBlank((String)dataList.get(1))){
						break;
					}
					IndexItemTb iit = itemsList.get(c - 2);
					if (iit != null) {
						// 校验数据是否可以为空
						Integer isEmpty = iit.getIndexItemEmpty();
						Integer type = iit.getIndexItemType();// 数据类型，0字符，1时间，2数值,3数据字典
						if (isEmpty == 0 && StringUtils.isBlank(cellValue)) {// 不可以为空,如果单元格数据为空
							errorList.add("第" + getcol(r + 1, rowCount)+ (r + 1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列数据不可以为空");
						}
						// 时间和字符长度
						if ((type == 0 || type == 1) && StringUtils.isNotBlank(cellValue)) {
							cellValue = checkDateAndLenth(rowCount,tempTbName, iit, cellValue, r, c, errorList);
							cellValue = cellValue.replace("'", "‘");
						}
						// 校验数据字典
						if (type == 3 && StringUtils.isNotBlank(cellValue)) {
							cellValue = checkDic(amapVtC,amapCtV,dmapVtC,dmapCtV,omapVtC,omapCtV,imapVtC,imapCtV,
									rowCount,a, tempTbName, cellValue, iit, errorList, r, c, isSuccess);
						}
						if (type == 2 && StringUtils.isNotBlank(cellValue)) {// 校验数值
							Double v = null;
							try {
								v = Double.valueOf(cellValue);
							} catch (Exception e) {
								errorList
										.add("第" + getcol(r + 1, rowCount)+ (r + 1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列数值类型填写有误");
								continue;
							}
							dataList.add(v);
							continue;
						}
					}
					dataList.add(cellValue);
				}
			} // 此位置已将一条数据装进list中
			if(StringUtils.isNotBlank((String)dataList.get(0)) || StringUtils.isNotBlank((String)dataList.get(1))){
				if (CollectionUtils.isEmpty(errorList)) {
					dataList.add(0);
					dataList.add(r + 1);
					dataList.add(0);
					dataList.add(0);
					dataList.add(so.getSys_org_id());
					dataList.add(so.getSys_area_id());
					dataList.add(reportDate);
					dataList.add(submit);
					try {
						// 将每一条数据装进临时表
						reportIndexDao.insertToTemp(tempTbName, columnList, dataList);
					} catch (Exception e) {
						errorList.add("第"+getcol(r+1,rowCount)+ (r + 1)+"行数据入临时表失败");
						e.printStackTrace();
					}
				}
			}
		}

	}

	/**
	 * 从excel中读取数据存进临时表
	 */
	@Override
	public String getExcelToTemp(ReportExcelTemplate rt,List<IndexItemTb> itemsList, SysArea a, int[] nums, Date submit, String tempTbName,
			Map<String, String> columnMap, List<Map<String, Object>> qymcMsg, MultipartFile file,
			List<String> indexNames, List<String> errorList, IndexTb it, Date reportDate, SysOrg so,
			List<String> uniques, List<Map<String, Object>> repeat, List<Integer> reNum, String[] isSuccess) {
		String filePath = UpLoadFile.upLoadFile(file);
		List<String> columnList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		ExecutorService pool = Executors.newFixedThreadPool(4);
		try {// 同时支持Excel 2003、2007
			File excelFile = new File(filePath); // 创建文件对象
			FileInputStream is = new FileInputStream(excelFile); // 文件流
			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel 2003/2007/2010  都是可以处理的
			Sheet sheet = workbook.getSheetAt(0);
			Row row0 = sheet.getRow(0);// 第一行：指标大类名
			Row row1 = sheet.getRow(1);// 第二行：列名
			String set = rt.getReportExcelTemplateIndexItemSet();
			String[] codes = set.split(",");
			int cellCount = codes.length;// 获取总列数
			int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数（包括空行）
			nums[0] = rowCount - 2;
			
			//校验模板指标大类名称是否匹配
			isIndexTb(it,row0,cellCount,errorList);
			if(CollectionUtils.isEmpty(errorList)){
				//查询所有字典的vaule-code
				Map<String,String> amapVtC = new HashMap<String,String>();
				Map<String,String> amapCtV = new HashMap<String,String>();
				Map<String,Object> dmapCtV = new HashMap<String,Object>();
				Map<String,Object> dmapVtC = new HashMap<String,Object>();
				Map<String,String> omapCtV = new HashMap<String,String>();
				Map<String,String> omapVtC = new HashMap<String,String>();
				Map<String,String> imapCtV = new HashMap<String,String>();
				Map<String,String> imapVtC = new HashMap<String,String>();
				valueToCode(amapVtC, amapCtV, dmapVtC, dmapCtV, omapVtC, omapCtV, imapVtC, imapCtV);
				firstRow(codes,uniques, itemsList, errorList, it, so, tempTbName, columnMap, indexNames, sdf, row1, cellCount,
						columnList);
				// =============================遍历每一行,从第三行开始为数据=====================
				for (int r = 2; r < rowCount; r++) {// 遍历每一行
					Row row = sheet.getRow(r);
					if (row == null) {
						continue;
					}
					pool.execute(new InnerClass(amapVtC,amapCtV,dmapVtC,dmapCtV,omapVtC,omapCtV,imapVtC,imapCtV,
							rowCount,cellCount, row, r, indexNames, columnList, tempTbName, so, errorList,
							itemsList, isSuccess, a, reportDate, submit, sdf));
				} // 所有数据已经装进List<map>，所有数据都已装进临时表
				pool.shutdown();// 停止接收新线程
				while (true) {
					if (pool.isTerminated()) {// 线程全部结束
						break;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess[0] = "1";
		}
		return filePath;
	}

	private void valueToCode(
			Map<String,String> amapVtC,Map<String,String> amapCtV,
			Map<String,Object> dmapVtC,Map<String,Object> dmapCtV,
			Map<String,String> omapVtC,Map<String,String> omapCtV,
			Map<String,String> imapVtC,Map<String,String> imapCtV
			){
		List<Map<String,Object>> dicVTC =  reportIndexDao.queryAllDicValueToCode();
		List<Map<String,String>> areaVTC =  reportIndexDao.queryAllAreaValueToCode();
		List<Map<String,String>> orgVTC =  reportIndexDao.queryAllOrgValueToCode();
		List<Map<String,String>> indusVTC =  reportIndexDao.queryAllIndusValueToCode();	
//		for(Map<String,String> map:areaVTC){
//			amapVtC.put(map.get("key"),map.get("value"));
//		}
		for(Map<String,String> map:areaVTC){
			amapCtV.put(map.get("value"),map.get("key"));
		}
		for(Map<String,String> map:orgVTC){
			omapVtC.put(map.get("key"),map.get("value"));
		}
		for(Map<String,String> map:orgVTC){
			omapCtV.put(map.get("value"),map.get("key"));
		}
		for(Map<String,Object> map:dicVTC){
			dmapVtC.put((String)map.get("key")+map.get("dicId"),(String)map.get("value"));
		}
		for(Map<String,Object> map:dicVTC){
			dmapCtV.put((String)map.get("value")+map.get("dicId"),(String)map.get("key"));
		}
//		for(Map<String,String> map:indusVTC){
//			imapVtC.put(map.get("key"),map.get("value"));
//		}
		for(Map<String,String> map:indusVTC){
			imapCtV.put(map.get("value"),map.get("key"));
		}
	}
	
	private String checkDic(
			Map<String,String> amapVtC,Map<String,String> amapCtV,
				Map<String,Object> dmapVtC,Map<String,Object> dmapCtV,
				Map<String,String> omapVtC,Map<String,String> omapCtV,
				Map<String,String> imapVtC,Map<String,String> imapCtV,
				int rowCount,SysArea a, String tempTbName, String cellValue, IndexItemTb iit, List<String> errorList,
				int r, int c, String[] isSuccess) {
			try {
				if (StringUtils.isNotBlank(cellValue)) {
					String dicName = dicService.getDicByDicId(iit.getDicId()).getDicName();
					if (!cellValue.matches("[0-9a-zA-Z]*")){// 当前为中文
						if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicClassFy.getDicName())) {// 经济行业
							List<SysClassFyModel> list = sysClassFyService.queryListByName(cellValue);
							if (CollectionUtils.isEmpty(list)) {
								errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
										+ "列,经济行业填写的中文名有误或者不存在，请检查");
							} else if (list.size() == 1) {
								cellValue = list.get(0).getSysIndustryCode();
							} else {
								cellValue = list.get(0).getSysIndustryCode();
								for (int i = 1; i < list.size(); i++) {
									if (cellValue.length() < list.get(i).getSysIndustryCode().length()) {
										cellValue = list.get(i).getSysIndustryCode();
									}
								}
							}
						} else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicSysArea.getDicName())) { // 地区
							List<SysArea> list = sysAreaService.queryAreaByName(cellValue);
							boolean flag = false;
							if (CollectionUtils.isEmpty(list)) {
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,地区填写的中文名有误或者不存在，请检查");
							} else if (list.size() == 1) {
								cellValue = list.get(0).getSysAreaCode();
							} else {
								for (SysArea sysArea : list) {
									if (sysArea.getSubArea().get(0).getSysAreaId().intValue() == a.getSysAreaId()
											.intValue()) {
										cellValue = sysArea.getSysAreaCode();
										flag = true;
									}
								}
								if (!flag) {
									errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
											+ "列,地区填写的中文名不存在或者对应多个代号，建议填写该地区对应的代号进行报送");
								}
							}
						} else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicOrgAndGov.getDicName())) {// 业务发生地金融机构或者政府部门
	//						List<SysOrg> list = sysOrgService.queryInstitutionsByCodeAndName(null, cellValue);
	//						if (CollectionUtils.isEmpty(list)) {
	//							errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
	//									+ "列,该机构或政府部门填写的中文名有误或者不存在，请检查");
	//						} else {
	//							cellValue = list.get(0).getSys_org_financial_code();
	//						}
							String code = omapVtC.get(cellValue);
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,该机构或政府部门填写的中文名有误或者不存在，请检查");					
							}else{
								cellValue = code;
							}
						} else {
							// 查询数据字典
	//						DicContent dc = dicContentService.getDicIdByDicContentValueAndDicId(cellValue, iit.getDicId());
	//						if (dc == null) {
	//							errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
	//									+ "列,该数据字典类型填写的中文名有误或者不存在，请检查");
	//						} else {
	//							cellValue = dc.getDicContentCode();
	//						}
							String code = (String)dmapVtC.get(cellValue+iit.getDicId());
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,该数据字典类型填写的中文名有误或者不存在，请检查");					
							}else{
								cellValue = code;
							}
						}
					} else {// 如果当前的值是代号，则判断值是否在数据库中存在
						if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicClassFy.getDicName())) {// 经济行业
	//						SysClassFyModel f = sysClassFyService.queryModelByCode(cellValue);
	//						if (f == null) {
	//							errorList.add(
	//									"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,经济行业填写的代号有误或者不存在，请检查");
	//						} else {
	//							cellValue = f.getSysIndustryCode();
	//						}
							String code = imapCtV.get(cellValue);
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,经济行业填写的代号有误或者不存在，请检查");					
							}
						} else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicSysArea.getDicName())) { // 地区
	//						SysArea area = sysAreaService.queryAreaByCode(cellValue);
	//						if (area == null) {
	//							errorList.add(
	//									"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,地区填写的代号有误或者不存在，请检查");
	//						} else {
	//							cellValue = area.getSysAreaCode();
	//						}
							String code = amapCtV.get(cellValue);
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,地区填写的代号有误或者不存在，请检查");					
							}
						} else if (StringUtils.equalsIgnoreCase(dicName, BothEnum.dicOrgAndGov.getDicName())) {// 业务发生地金融机构
	//						List<SysOrg> list = sysOrgService.queryInstitutionsByCodeAndName(cellValue, null);
	//						if (CollectionUtils.isEmpty(list)) {
	//							errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
	//									+ "列,该机构或政府部门填写的代号有误或者不存在，请检查");
	//						} else {
	//							cellValue = list.get(0).getSys_org_financial_code();
	//						}
							String code = omapCtV.get(cellValue);
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,该机构或政府部门填写的代号有误或者不存在，请检查");					
							}
						} else {
							// 查询数据字典
	//						DicContent dc = dicContentService.getDicContentByDicIDAndCode(cellValue, iit.getDicId());
	//						if (dc == null) {
	//							errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
	//									+ "列,该数据字典类型填写的代号有误或者不存在，请检查");
	//						} else {
	//							cellValue = dc.getDicContentCode();
	//						}
							String code = (String) dmapCtV.get(cellValue+iit.getDicId());
							if(StringUtils.isBlank(code)){
								errorList.add(
										"第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列,该数据字典类型填写的代号有误或者不存在，请检查");					
							}
						}
					}
				}
			} catch (Exception e) {
				isSuccess[0] = "1";
				errorList.add("第" + getcol(r + 1,rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列的字典类型的值在校验过程中出现异常");
			}
			return cellValue;
		}

	private void isIndexTb(IndexTb it,Row row,int cellCount,List<String> errorList){
		for(int c = 0; c < cellCount; c++){
			Cell cell = row.getCell(c);
			if (cell == null) {
				cell = row.createCell(c);
				cell.setCellValue("");
			}
			String cellValue = null;
			int cellType = cell.getCellType();
			if (cellType ==Cell.CELL_TYPE_STRING || cellType == Cell.CELL_TYPE_BLANK) {
				cellValue = cell.getStringCellValue().trim();
			}else{
				errorList.add("导入的excel文件和模板不匹配，或是第一列单元格有除指标名以外的其他内容");
				break;
			}
			if(!it.getIndexName().equals(cellValue) && StringUtils.isNotBlank(cellValue)){
				errorList.add("导入的excel文件和模板不匹配，或是第一列单元格有除指标名以外的其他内容");
				break;
			}
		}
	}
	
	private void firstRow(String[] codes,List<String> uniques, List<IndexItemTb> itemsList, List<String> errorList, IndexTb it,
			SysOrg so, String tempTbName, Map<String, String> columnMap, List<String> indexNames, SimpleDateFormat sdf,
			Row row, int cellCount, List<String> columnList) {
		for (int c = 0; c < cellCount; c++) {// 遍历每一列
			Cell cell = row.getCell(c);
			if (cell == null) {
				cell = row.createCell(c);
				cell.setCellValue("");
			}
			String cellValue = null;
			int cellType = cell.getCellType();
			switch (cellType) {
			case Cell.CELL_TYPE_STRING: // 文本
				cellValue = cell.getStringCellValue().trim();
				if ("".equals(cellValue)) {
					cellValue = null;
				}
				break;
			case Cell.CELL_TYPE_NUMERIC: // 数字、日期
				if (DateUtil.isCellDateFormatted(cell)) {
					cellValue = sdf.format(cell.getDateCellValue()); // 日期型
				} else {
					cell.setCellType(Cell.CELL_TYPE_STRING);
					cellValue = cell.getStringCellValue();
				}
				break;
			case Cell.CELL_TYPE_BLANK: // 空白
				cellValue = null;
			default:
			}
			if(cellValue  == null){
				continue;
			}
			if ("统一社会信用代码".equals(cellValue)) {
				indexNames.add("DEFAULT_INDEX_ITEM_ID");
				cellValue = "CODE_CREDIT";
				columnList.add(cellValue.toUpperCase());
				columnMap.put(cellValue.toUpperCase(), cellValue.toUpperCase());
				columnMap.put("DEFAULT_INDEX_ITEM_ID", "DEFAULT_INDEX_ITEM_ID");
			} else if ("组织机构代码".equals(cellValue)) {
				cellValue = "CODE_ORG";
				columnList.add(cellValue.toUpperCase());
				columnMap.put(cellValue.toUpperCase(), cellValue.toUpperCase());
			} else {
				// 先查询别名表中这个指标项的中文名是否是别名，如果是别名，则替换成对应的原名称
				cellValue = checkAlias(tempTbName, it, cellValue, errorList, c, uniques, so);
				if(!codes[c].trim().equals(cellValue)){
					errorList.add("模板有误请重新下载");
					break;
				}
				columnList.add(cellValue.toUpperCase());
				columnMap.put(cellValue.toUpperCase(), cellValue.toUpperCase());
				IndexItemTb iit = indexItemTbService.getIndexItemTbByCode(cellValue);
				itemsList.add(iit);
			}
			indexNames.add(cellValue.toUpperCase());// 在列名集合中加入列
		}
		columnList.add("STATUS");
		columnList.add("NUM");
		columnList.add("ISINSERT");
		columnList.add("DEFAULT_INDEX_ITEM_ID");
		columnList.add("SYS_ORG_ID");
		columnList.add("SYS_AREA_ID");
		columnList.add("RECORD_DATE");
		columnList.add("SUBMIT_TIME");
	}

	private class getDefaultIdInner implements Runnable {
		private int rowCount; 
		private SysCheckCache scc;
		private List<IndexItemTb> itemsList;
		private int i;
		private IndexTb it;
		private List<String> errorList;
		private SysArea a;
		private Map<String, Object> map;
		private List<Map<String, Object>> qymcMsg;
		private Date reportDate;
		private List<String> indexNames;
		private SysOrg so;
		private String[] isSuccess;
		private StringBuffer areaIds;
		private String tempTbName;

		public getDefaultIdInner(int rowCount,SysCheckCache scc, List<IndexItemTb> itemsList, int i, IndexTb it,
				List<String> errorList, SysArea a, Map<String, Object> map, List<Map<String, Object>> qymcMsg,
				Date reportDate, List<String> indexNames, SysOrg so, String[] isSuccess, StringBuffer areaIds,
				String tempTbName) {
			super();
			this.rowCount = rowCount;
			this.scc = scc;
			this.itemsList = itemsList;
			this.i = i;
			this.it = it;
			this.errorList = errorList;
			this.a = a;
			this.map = map;
			this.qymcMsg = qymcMsg;
			this.reportDate = reportDate;
			this.indexNames = indexNames;
			this.so = so;
			this.isSuccess = isSuccess;
			this.areaIds = areaIds;
			this.tempTbName = tempTbName;
		}

		@Override
		public void run() {
			Integer defaultIndexItemId = checkOneData(rowCount,scc, itemsList, i, it, errorList, a, map, qymcMsg, reportDate,
					indexNames, so, isSuccess, areaIds);
			map.put("DEFAULT_INDEX_ITEM_ID", defaultIndexItemId);
			// 更新临时表中的二码id
			reportIndexDao.updateTempDafaultId(tempTbName, i + 3, defaultIndexItemId);
		}

	}
	/**
	 * 校验数据,将临时表数据设置二码id
	 */
	public void tocheck(int rowCount,List<IndexItemTb> itemsList, SysArea a, StringBuffer areaIds, Date submit, String tempTbName,
			List<Map<String, Object>> qymcMsg, List<String> indexNames, List<String> errorList, IndexTb it,
			Date reportDate, SysOrg so, List<String> uniques, List<Map<String, Object>> repeat, List<Integer> reNum,
			String[] isSuccess) {
		List<Map<String, Object>> excelList = reportIndexDao.selectAllFormTempTb(tempTbName);
		SysCheckCache scc = new SysCheckCache(sysCheckDao, sysAreaService, itemsList, a.getSysAreaId(), it.getIndexId(),
				reportDate);
		ExecutorService pool = Executors.newFixedThreadPool(4);
		for (int i = 0; i < excelList.size(); i++) {
			Map<String, Object> map = excelList.get(i);
			pool.execute(new getDefaultIdInner(rowCount,scc, itemsList, i, it, errorList, a, map, qymcMsg, reportDate,
					indexNames, so, isSuccess, areaIds, tempTbName));
		}
		pool.shutdown();
		while (true) {
			if (pool.isTerminated()) {
				break;
			}
		}
		
//		/**
//		 *校验一批数据中的二码是否有重 
//		 */
//		try{
//			if(CollectionUtils.isEmpty(uniques)){				
//				checkDefaultIdIsUnique(excelList,tempTbName,repeat,errorList);
//			}
//		}catch(Exception e){
//			errorList.add("校验一批数据中的二码是否有重,出现异常");
//		}
		
		/**
		 * 唯一标识的校验
		 */
		try{			
			if(CollectionUtils.isNotEmpty(uniques)){			
				checkUnique(excelList,uniques, tempTbName, reNum, repeat, it, errorList);
			}
		}catch(Exception e){
			e.printStackTrace();
			errorList.add("校验唯一标识出错，请检查该指标的唯一标识是否设置有误");
		}
		/**
		 * 进行授信和贷款的校验
		 */
//		try {
//			if (CollectionUtils.isEmpty(errorList) && StringUtils.equals(it.getIndexCode(), "index_yxdk")
//					|| StringUtils.equals(it.getIndexCode(), "index_yxsx")) {
//				sysCheckService.checkDkAndSx(null, excelList, reportDate, errorList, it);
//			}
//		} catch (Exception e) {
//			errorList.add("校验银行贷款出错");
//		}
	}

	/**
	 * 二码转换校验
	 */
	private Integer checkOneData(int rowCount,SysCheckCache scc, List<IndexItemTb> itemsList, int r, IndexTb it,
			List<String> errorList, SysArea a, Map<String, Object> map, List<Map<String, Object>> qymcMsg,
			Date reportDate, List<String> indexNames, SysOrg so, String[] isSuccess, StringBuffer areaIds) {
		Integer defaultIndexItemId = 0;
		try {
			if ("index_jbxx".equals(it.getIndexCode())) {// ===================基本信息表================
				// 将每一条数据的二码与二码表对比，校验二码是否存在//===================================
				String codeCredit = (String) map.get("CODE_CREDIT");
				String codeOrg = (String) map.get("CODE_ORG");
				String qymc = (String) map.get("INDEX_JBXX_QYMC");
				if (!StringUtils.isBlank(codeCredit) && StringUtils.isBlank(codeOrg)) { // 只填写了社会信用码
					DefaultIndexItem dii = null;
					try {
						List<DefaultIndexItem> list = defaultIndexItemService.getByCredit(codeCredit, a.getSysAreaId());
						if (CollectionUtils.isEmpty(list)) { // 如果为空，表示二码表中没有这个企业，需要将该企业的二码信息存进二码表
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据根据统一社会信用代码："+codeCredit+"在二码表中没有查询到对应二码信息");
						} else {// 通过 社会信用码 查询到了 一个企业，
							dii = list.get(0);
							defaultIndexItemId = dii.getDefaultIndexItemId();
							if (!dii.getQymc().equals(qymc)) {// 如果基本信息中的企业名称和二码表对应的企业名称不相同,提示企业名称不同
								Map<String, Object> ma = new HashMap<>();
								ma.put("dii", dii);
								ma.put("newName", qymc);
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								ma.put("record_date", sdf2.format(reportDate));
								ma.put("data", map);
								ma.put("msg", "第" + getcol(r + 3, rowCount)+ (r + 3) + "行基本信息：二码表中的企业名称为：" + dii.getQymc() + "，本次报送的企业名称为:" + qymc
										+ "，勾选则替换。");
								qymcMsg.add(ma);
							}
							map.put("INDEX_JBXX_QYMC", dii.getQymc());
							map.put("DEFAULT_INDEX_ITEM_ID", defaultIndexItemId);
						}
					} catch (Exception e) {
						errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，根据统一社会信用代码"+codeCredit+"查询二码表出错");
					}
				} else if (StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // excel只填写了组织机构码
					List<DefaultIndexItem> dl = null;
					try {
						dl = defaultIndexItemService.getByCodeOrg(codeOrg, a.getSysAreaId());
						if (CollectionUtils.isEmpty(dl)) { // 如果为空，表示二码表中没有这个企业，需要将该企业的二码信息存进二码表
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据根据组织机构代码:"+codeOrg+"在二码表中没有查询到对应二码信息");
						} else if (dl.size() == 1) { // 表示二码表只存了组织机构码
							defaultIndexItemId = dl.get(0).getDefaultIndexItemId();// 得到二码表id
							if (!dl.get(0).getQymc().equals(qymc)) {
								Map<String, Object> ma = new HashMap<>();
								ma.put("dii", dl.get(0));
								ma.put("newName", qymc);
								SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
								ma.put("record_date", sdf2.format(reportDate));
								ma.put("data", map);
								ma.put("msg", "第" + getcol(r + 3, rowCount)+ (r + 3) + "行基本信息：二码表中的企业名称为：" + dl.get(0).getQymc() + "，本次报送的企业名称为:"
										+ qymc + "，勾选则替换。");
								qymcMsg.add(ma);
							}
							map.put("INDEX_JBXX_QYMC", dl.get(0).getQymc());
							map.put("DEFAULT_INDEX_ITEM_ID", defaultIndexItemId);
						} else { // 表示二码表中有合并前和合并后两条数据,此时，上报的信息中没有社会信用代码，必须让先填写再能上报
							if (StringUtils.isBlank(dl.get(0).getCodeCredit())) {
								codeCredit = dl.get(1).getCodeCredit();
							} else {
								codeCredit = dl.get(0).getCodeCredit();
							}
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行当前报送组织机构代码是："+codeOrg+",企业二码表中对应的统一社会信用代码是：" + codeCredit + ",请用社会信用码上报");
						}
					} catch (Exception e) {
						errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行查询组织机构码:"+codeOrg+"出错");
					}
				} else if (!StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // excel中二码都有的情况
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，不能同时用统一社会信用代码和组织机构代码进行报送,请删除组织机构代码再重新上报");
				} else {// 社会信用码为空,且组织机构码为空，返回提示消息
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据必须填写社会信用码或组织机构码");
				}
				try{					
					check(rowCount,scc, itemsList, indexNames, so, it, reportDate, errorList, isSuccess, r, map);
				}catch(Exception e){
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据在进行规则校验出现异常");
				}
			} else { // =========================非基本信息表的其他表================================
						// 将每一条数据的二码与二码表对比，校验二码是否存在
				String codeCredit = (String) map.get("CODE_CREDIT");
				String codeOrg = (String) map.get("CODE_ORG");
				String str = DateFormatUtils.format(reportDate, "yyyy-MM-dd");
				if (!StringUtils.isBlank(codeCredit) && StringUtils.isBlank(codeOrg)) { // 只填写了社会信用码
					// 首先在基本信息表中查询该企业是否在该机构的所在地区报送过基本信息
					DefaultIndexItem dii = null;
					try {
						List<DefaultIndexItem> list = defaultIndexItemService.getByCredit(codeCredit, a.getSysAreaId());
						if (CollectionUtils.isEmpty(list)) { // 如果为空，表示二码表中没有这个企业，提示先录入该企业的基本信息
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行的数据根据统一社会信用代码"+codeCredit+"没有查询到对应的二码，请先录入该这条数据对应的企业基本信息");
						} else {// 通过 社会信用码 在二码表中查询到了 一个企业，
							dii= list.get(0);
							defaultIndexItemId = dii.getDefaultIndexItemId();
							// 查询是否在报送业务信息前上报过基本信息
							StringBuffer sb = new StringBuffer("select count(*) from index_jbxx_tb where "
									+ "default_index_item_id =" + defaultIndexItemId + " and record_date<= '" + str
									+ "' and sys_area_id in " + areaIds);
							Integer count = reportIndexDao.countBySql(sb.toString());
							if (count == 0) {
								errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，统一社会信用代码:"+codeCredit+"没有在所选的归档时间前上报本地区对应的企业信息，请先在本地区录入该这条数据对应的企业基本信息");
							} else {
								map.put("DEFAULT_INDEX_ITEM_ID", defaultIndexItemId);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行,根据统一社会信用代码："+codeCredit+"查询二码表单个对象出错");
					}
				} else if (StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // 只填写了组织机构码
					List<DefaultIndexItem> dl = null;
					try {
						dl = defaultIndexItemService.getByCodeOrg(codeOrg, a.getSysAreaId());
						if (CollectionUtils.isEmpty(dl)) { // 如果为空，表示二码表中没有这个企业，提示先录入该企业的基本信息
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行,根据组织机构代码："+codeOrg+"没有查询到对应的二码，请先录入该这条数据对应的企业基本信息");
						} else if (dl.size() == 1) { // 表示只存了组织机构码
							defaultIndexItemId = dl.get(0).getDefaultIndexItemId();// 得到二码表id
							// 查询是否在报送业务信息前上报过基本信息
							StringBuffer sb = new StringBuffer("select count(*) from index_jbxx_tb where "
									+ "default_index_item_id =" + defaultIndexItemId + " and record_date<= '" + str
									+ "' and sys_area_id in " + areaIds.toString());
							Integer count = reportIndexDao.countBySql(sb.toString());
							if (count == 0) {
								errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，统一社会信用代码:"+codeOrg+"没有在所选的归档时间前上报本地区对应的企业信息，请先在本地区录入该这条数据对应的企业基本信息");
							} else {
								map.put("DEFAULT_INDEX_ITEM_ID", defaultIndexItemId);
							}
						} else { // 表示二码表中有合并前和合并后两条数据,此时，上报的信息中没有社会信用代码，必须让先填写再能上报
							errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据的社会信用码是：" + codeCredit + ",请补充再上报");
						}
					} catch (Exception e) {
						errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，根据组织机构代码:"+codeOrg+"查询二码表出错");
					}
					
				} else if (!StringUtils.isBlank(codeCredit) && !StringUtils.isBlank(codeOrg)) { // 二码都有的情况
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行，不能同时用统一社会信用代码和组织机构代码进行报送,请删除组织机构代码再重新上报");
				} else {// 社会信用码为空,且组织机构码为空，返回提示消息
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据必须填写社会信用码或组织机构码");
				}
				try{					
					check(rowCount,scc, itemsList, indexNames, so, it, reportDate, errorList, isSuccess, r, map);
				}catch(Exception e){
					errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行数据在进行规则校验出现异常");
				}
			} 
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess[0] = "1";
			errorList.add("第" + getcol(r + 3, rowCount)+ (r + 3) + "行的数据对应二码转换异常");
		}
		return defaultIndexItemId;
	}

	/*@Test
	public void test() {
//		List<String> list =new ArrayList<>();
//		list.add("2");
//		list.add("3");
//		list.add(0, "1");
//		String osnName = System.getProperty("os.name");
//		System.out.println(osnName);
		List<Map<String,String>> list =  reportIndexDao.queryAllAreaValueToCode();
		System.out.println(list);
	}*/
	
	
	private class checkUpdateOrInsert implements Runnable {
		private List<IndexItemTb> itemList;
		private SysArea a;
		private IndexTb it;
		private Map<String, Object> map;
		private String time;
		private List<String> uniques;
		private String tbName;
		private StringBuffer areaIds;
		private String tempTbName;
		private int k;
		private SysOrg so;
		private List<String> indexNames;
		private String submitt;
		private List<String> updateList;
		private List<Integer> areaids;

		public checkUpdateOrInsert(List<IndexItemTb> itemList,SysArea a,IndexTb it, Map<String, Object> map, String time, List<String> uniques,
				String tbName, StringBuffer areaIds, String tempTbName, int k, SysOrg so, List<String> indexNames,
				String submitt, List<String> updateList, List<Integer> areaids) {
			super();
			this.itemList = itemList;
			this.a = a;
			this.it = it;
			this.map = map;
			this.time = time;
			this.uniques = uniques;
			this.tbName = tbName;
			this.areaIds = areaIds;
			this.tempTbName = tempTbName;
			this.k = k;
			this.so = so;
			this.indexNames = indexNames;
			this.submitt = submitt;
			this.updateList = updateList;
			this.areaids = areaids;
		}

		@Override
		public void run() {
			List<String> names = new ArrayList<>();
			List<Object> values = new ArrayList<>();
			if (it.getIndexType() == 0) {
				// 基本信息类
				// 组装这条报送sql之前，
				// 查询这条信息是否有对应的：如果在同一时间上报数据两码+标识码+所在区域相同，则该条数据进行覆盖操作，不同则进行新增操作
				names.add("record_date");
				values.add("'" + time + "'");
				names.add("default_index_item_id");
				values.add(map.get("DEFAULT_INDEX_ITEM_ID"));
				for (String s : uniques) {
					names.add(s);
					values.add("'" + map.get(s.toUpperCase()) + "'");
				}
				StringBuffer sql0 = new StringBuffer("select count(*) from " + tbName + " where 1=1 ");
				for (int i = 0; i < names.size(); i++) {
					sql0.append(" and " + names.get(i) + " = " + values.get(i) + " ");
				}
				sql0.append(" and sys_area_id in" + areaIds.toString());
				Integer count = reportIndexDao.countBySql(sql0.toString());
				if (count != 0) {// 如果查询出的条数不为0，则做更新操作
					// 将临时表中的对用数据的是否为新增数据的标识改成1，默认0新增
					reportIndexDao.updateIsInsert(tempTbName, k + 3);
					StringBuffer sql1 = new StringBuffer("select default_index_item_id from " + tbName + " where 1=1 ");
					for (int i = 0; i < names.size(); i++) {
						sql1.append(" and " + names.get(i) + " = " + values.get(i) + " ");
					}
					StringBuffer sql2 = new StringBuffer(
							"update " + tbName + " set sys_area_id = " + so.getSys_area_id() + ", ");
					for (int i = 0; i < indexNames.size(); i++) {
						if (!"CODE_CREDIT".equals(indexNames.get(i)) && !"CODE_ORG".equals(indexNames.get(i))) {// 去除二码
							for (IndexItemTb item : itemList) {
								if (item.getIndexItemCode().equals(indexNames.get(i).toLowerCase())) {
									Integer te = item.getIndexItemType();
									if (te == 0 || te == 3) {// 判断指标数据类型:0字符，1时间，2数值,3数据字典
										String s = (String) map.get(indexNames.get(i));
										if (StringUtils.isNotBlank(s)) {
											s = s.replace("'", "’");
											sql2.append(indexNames.get(i) + "='" + s + "',");
										} else {
											sql2.append(indexNames.get(i) + "= null,");
										}
									} else if (te == 1) {
										Object o = map.get(indexNames.get(i));
										if (o == null) {
											sql2.append(indexNames.get(i) + " = null,");
										} else {
											String s = DateUtils.format((Date) o, "yyyy-MM-dd");
											sql2.append(indexNames.get(i) + "='" + s + "',");
										}
									} else if (te == 2) {
										Object o = map.get(indexNames.get(i));
										if (o == null) {
											sql2.append(indexNames.get(i) + " = null,");
										} else {
											sql2.append(indexNames.get(i) + "=" + o + ",");
										}
									}
								}
							}
						}
					}
					sql2.append("submit_time = '" + submitt + "' ");
					sql2.append("where default_index_item_id in (" + sql1 + ")");
					for (String s : uniques) {
						sql2.append(" and " + s + " = " + "'" + map.get(s.toUpperCase()) + "'");
					}
					sql2.append(" and  record_date='" + time + "'");
					updateList.add(sql2.toString());
				}
			} else {
				// ======================业务信息：同一时间上报数据两码+标识码+所属区域+所属机构相同==========================
				names.add("record_date");
				values.add(time);
				Integer defaultId = (Integer) map.get("DEFAULT_INDEX_ITEM_ID");
				// 根据二码id查询该企业所有的二码id
				// 查询社会信用码对应的二码id,如果社会信用码为空则返回组织机构码对应的二码id
				// 一个企业在不同地区相关联的二码id
				String dids = defaultIndexItemService.getOtherAreaDefaultIds(defaultId);
				//
				DefaultIndexItem d = defaultIndexItemService.getById(defaultId);
				List<DefaultIndexItem> dlist = defaultIndexItemService.getByCredit(d.getCodeCredit(), a.getSysAreaId());
				DefaultIndexItem d1 = null;  
				if (CollectionUtils.isEmpty(dlist)) {
					List<DefaultIndexItem> li = defaultIndexItemService.getByCodeOrg(d.getCodeOrg(),
							a.getSysAreaId());
					if (StringUtils.isBlank(li.get(0).getCodeCredit())) {
						d1 = li.get(0);
					} else {
						d1 = li.get(1);
					}
				}else{
					d1 = dlist.get(0);
				}
				map.put("DEFAULT_INDEX_ITEM_ID", d1.getDefaultIndexItemId());
				for (String s : uniques) {
					names.add(s);
					values.add("'" + map.get(s.toUpperCase()) + "'");
				}
				StringBuffer sql0 = new StringBuffer("select count(*) from " + tbName + " where 1=1 ");
				for (int i = 0; i < names.size(); i++) {
					if (i == 0) {
						sql0.append(" and " + names.get(i) + " = '" + values.get(i) + "' ");
					} else {
						sql0.append(" and " + names.get(i) + " = " + values.get(i) + " ");
					}
				}
				sql0.append("and  default_index_item_id in" + dids);
				Integer count = reportIndexDao.countBySql(sql0.toString());
				if (count != 0) {// 如果查询出的条数不为0，则做更新操作
					// 将临时表中的对用数据的是否为新增数据的标识改成1，默认0新增
					reportIndexDao.updateIsInsert(tempTbName, k + 3);
					StringBuffer sql1 = new StringBuffer("select default_index_item_id from " + tbName + " where 1=1 ");
					for (int i = 0; i < names.size(); i++) {
						if (i == 0) {
							sql1.append(" and " + names.get(i) + " = '" + values.get(i) + "' ");
						} else {
							sql1.append(" and " + names.get(i) + " = " + values.get(i) + " ");
						}
					}
					StringBuffer sql2 = new StringBuffer("update " + tbName + " set ");
					for (int i = 0; i < indexNames.size(); i++) {
						if (!"CODE_CREDIT".equals(indexNames.get(i)) && !"CODE_ORG".equals(indexNames.get(i))) {// 去除二码
							for (IndexItemTb item : itemList) {
								if (item.getIndexItemCode().equals(indexNames.get(i).toLowerCase())) {
									Integer te = item.getIndexItemType();
									if (te == 0 || te == 3) {// 判断指标数据类型:0字符，1时间，2数值,3数据字典
										String s = (String) map.get(indexNames.get(i));
										if (StringUtils.isNotBlank(s)) {
											s = s.replace("'", "");
											sql2.append(indexNames.get(i) + "='" + s + "',");
										} else {
											sql2.append(indexNames.get(i) + "= null,");
										}
									} else if (te == 1) {
										Object o = map.get(indexNames.get(i));
										if (o == null) {
											sql2.append(indexNames.get(i) + " = null,");
										} else {
											String s = DateUtils.format((Date) o, "yyyy-MM-dd");
											sql2.append(indexNames.get(i) + "='" + s + "',");
										}
									} else if (te == 2) {
										Object o = map.get(indexNames.get(i));
										if (o == null) {
											sql2.append(indexNames.get(i) + " = null,");
										} else {
											sql2.append(indexNames.get(i) + "=" + o + ",");
										}
									}
								}
							}
						}
					}
					sql2.append("submit_time = '" + submitt + "' ,default_index_item_id= " + d1.getDefaultIndexItemId()
							+ " ,sys_org_id = " + so.getSys_org_id() + " ,sys_area_id = " + so.getSys_area_id());
					sql2.append(" where default_index_item_id in (" + sql1 + ") ");
					for (String s : uniques) {
						sql2.append(" and " + s + " = " + "'" + map.get(s.toUpperCase()) + "'");
					}
					sql2.append(" and  record_date='" + time + "'");
					updateList.add(sql2.toString());
				}
			}

		}

	}

	@Override
	public void insertSqlAndReport(SysArea a, StringBuffer areaIds, Map<String, String> columnMap, Date submit,
			String tbName, String tempTbName, List<String> indexNames, SysOrg so, IndexTb it, String time,
			List<String> uniques, String[] isSuccess, List<String> updateList) {
		// 报送,几条数据就几条sql
		try {
			List<Map<String, Object>> excelList = reportIndexDao.selectAllFormTempTb(tempTbName);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String submitt = sdf.format(submit);
			List<Integer> areaids = sysAreaService.getAllUpAreaIds(so.getSys_area_id());
			List<IndexItemTb> itemList = indexItemTbService.queryItemsByAreaIds(it.getIndexId(), areaids);
			ExecutorService pool = Executors.newFixedThreadPool(4);
			for (int k = 0; k < excelList.size(); k++) {
				Map<String, Object> map = excelList.get(k);
				pool.execute(new checkUpdateOrInsert(itemList,a,it, map, time, uniques, tbName, areaIds, tempTbName, k, so,
						indexNames, submitt, updateList, areaids));
			}
			pool.shutdown();
			while (true) {
				if (pool.isTerminated()) {
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			isSuccess[0] = "1";
		}
	}

	@Override
	public void insertTempTable(ReportExcelTemplate rt, IndexTb it, String tempTbName) throws Exception {
		String[] excelFiled = rt.getReportExcelTemplateIndexItemSet().split(",");
		List<IndexItemTb> items = indexItemTbService.queryIndexItemTbsByIndexId(it.getIndexId());
//		StringBuffer sb = new StringBuffer("DECLARE GLOBAL TEMPORARY TABLE " + tempTbName + " ( ");
		StringBuffer sb = new StringBuffer("create TABLE " + tempTbName + " ( ");
		sb.append("code_credit varchar(50),");
		sb.append("code_org varchar(50),");
		for (int i = 0; i < items.size(); i++) {
			for (int k = 0; k < excelFiled.length; k++) {
				if (items.get(i).getIndexItemCode().equals(excelFiled[k].trim())) {
					if (items.get(i).getIndexItemType() == 0) {// 0字符
						int len = items.get(i).getVarLength();
						sb.append(items.get(i).getIndexItemCode() + " varchar(" + len * 2 + "),");
					} else if (items.get(i).getIndexItemType() == 1) {// 1时间
						sb.append(items.get(i).getIndexItemCode() + " timestamp,");
					} else if (items.get(i).getIndexItemType() == 2) {// 2数值
						sb.append(items.get(i).getIndexItemCode() + " double,");
					} else {// 3字典
						sb.append(items.get(i).getIndexItemCode() + " varchar(100),");
					}
					break;
				}
			}
		}
		sb.append("status Integer,");// 状态
		sb.append("num Integer,"); // excel中的行号
		sb.append("isinsert Integer,"); // 是否新增，默认0新增
		sb.append("default_index_item_id Integer ,"); // 二码值
		sb.append("sys_org_id Integer ,"); // 机构id
		sb.append("sys_area_id Integer ,"); // 地区id
		sb.append("record_date Date ,"); // 归档时间
//		sb.append("submit_time TIMESTAMP ) WITH REPLACE ON COMMIT PRESERVE ROWS IN XWXMTEMP"); // 提交时间
		sb.append("submit_time TIMESTAMP ) IN XWTEMP"); // 提交时间
		reportIndexDao.updateBySql(sb.toString());
	}

	@Override
	public Integer isTempTableExists(String tmepTbName) {
		return reportIndexDao.isTempTableExists(tmepTbName);
	}

	/**
	 * 检查别名
	 */
	private String checkAlias(String tempTbName, IndexTb it, String cellValue, List<String> errorList, int c,
			List<String> uniques, SysOrg so) {
		IndexItemAlias alias = indexItemAliasDao.selectByAliasNameAndAreaId(it.getIndexId(), cellValue,
				so.getSys_area_id());
		IndexItemTb iit = null;
		if (alias != null) {
			iit = indexItemTbService.queryIndexItemTbById(alias.getIndexItemId());
		} else {
			// 根据指标大类和指标项获取
			iit = indexItemTbService.getIndexItemsByIndexIdAndName(it.getIndexId(), cellValue);
		}
		if (iit != null) {
			cellValue = iit.getIndexItemCode();
			if (iit.getIndexItemImportUnique() == 1) {// 如果是识别码
				uniques.add(cellValue);
			}
		} else {
			errorList.add("EXCEL文件中第 2 行,第" + ExcelNumberUtil.toLetter(c + 1) + "列指标项"+cellValue+"中文名有误，请检查");
		}
		return cellValue;
	}

	/**
	 * 校验字符长度和时间格式
	 * 
	 * @param tempTbName
	 * @param iit
	 * @param cellValue
	 * @param r
	 * @param c
	 * @param errorList
	 * @return
	 */
	private String checkDateAndLenth(int rowCount, String tempTbName, IndexItemTb iit, String cellValue, int r, int c,
			List<String> errorList) {
		if (iit.getIndexItemType() == 1) {// 日期
			if (!StringUtils.isBlank(cellValue)) {
				if (cellValue.matches("[0-9]{8}")) {
					SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date = sd.parse(cellValue);
						cellValue = sdf2.format(date);
					} catch (ParseException e) {
						errorList.add("第" + getcol(r + 1, rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
								+ "列日期格式不对,请使用yyyy-MM-dd（yyyy/MM/dd、yyyyMMdd）格式");
						e.printStackTrace();
					}
				}else if(cellValue.matches("[0-9]{4}[//][0-9]{2}[//][0-9]{2}")){
					SimpleDateFormat sd = new SimpleDateFormat("yyyy/MM/dd");
					SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date date = sd.parse(cellValue);
						cellValue = sdf2.format(date);
					} catch (ParseException e) {
						errorList.add("第" + getcol(r + 1, rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
						+ "列日期格式不对,请使用yyyy-MM-dd（yyyy/MM/dd、yyyyMMdd）格式");
						e.printStackTrace();
					}
				}else if (!cellValue.matches("[0-9]{4}[-—][0-9]{2}[-—][0-9]{2}")
						) {
					errorList.add("第" + getcol(r + 1, rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1)
							+ "列日期格式不对,请使用yyyy-MM-dd（yyyy/MM/dd、yyyyMMdd）格式");
				}
			}
		} else if (iit.getIndexItemType() == 0) {// 字符
			if (cellValue.getBytes().length > iit.getVarLength()) {
				errorList.add("第" + getcol(r + 1, rowCount)+ (r+1) + "行,第" + ExcelNumberUtil.toLetter(c + 1) + "列数据太长，请缩短内容，或者增加对应指标项的字符长度");
			}
		}
		return cellValue;
	}

	/**
	 * 校验check表
	 */
	private void check(int rowCount, SysCheckCache scc, List<IndexItemTb> itemList, List<String> indexNames, SysOrg so, IndexTb it,
			Date reportDate, List<String> errorList, String[] isSuccess, int r, Map<String, Object> map) {
		try {
			// 数据集
			List<Object> dataList = new ArrayList<Object>();
			for (String s : indexNames) {
				if (!"DEFAULT_INDEX_ITEM_ID".equals(s) && !"CODE_CREDIT".equals(s) && !"CODE_ORG".equals(s)) {
					dataList.add(map.get(s));
				}
			}
			// ===================校验check表=============
			boolean flag = false;
			try {
				flag = scc.getCheckData(dataList);
				if (flag == false) {
					errorList.add("第" + getcol(r + 3,rowCount)+ (r+3) + "行数据不符合校验规则，请检查");
				}
			} catch (Exception e) {
				errorList.add("第" + getcol(r + 3,rowCount)+ (r+3) + "行数据在校验时出错");
				e.printStackTrace();
			}
		} catch (Exception e) {
			isSuccess[0] = "1";
			e.printStackTrace();
		}
	}

	/**
	 * 校验excel中统一社会信用代码或是组织机构代码对应的二码是否唯一
	 * @param excelList
	 * @param tempTbName
	 * @param repeat
	 * @param errorList
	 */
	private void checkDefaultIdIsUnique(List<Map<String, Object>> excelList, String tempTbName,
			List<Map<String, Object>> repeat, List<String> errorList){
		List<Map<String, Object>> newexcelList = new ArrayList<Map<String, Object>>();
		newexcelList.addAll(excelList);
		for (int i = 0; i < newexcelList.size(); i++) {
			boolean flag = false;
			Integer defaultId = (Integer) newexcelList.get(i).get("DEFAULT_INDEX_ITEM_ID");// 当前这条数据的二码值
			for (int j = i + 1; j < newexcelList.size(); j++) {
				Integer nextNefaultId = (Integer) newexcelList.get(j).get("DEFAULT_INDEX_ITEM_ID");// 当前这条数据的二码值
				if (defaultId.intValue() == nextNefaultId.intValue()) {// 当前这条数据和下一条数据相等
					flag = true;
					repeat.add(newexcelList.get(j));// 将下一条数据装进重复数据集合
					newexcelList.remove(j);
					j--;
				}
			}
			if (flag == true) {
				List<Integer> list = new ArrayList<>();
				repeat.add(0, newexcelList.get(i));
				StringBuffer ss = new StringBuffer("第");
				for (Map<String, Object> map : repeat) {
					ss.append((Integer) map.get("NUM") + ",");
					list.add((Integer) map.get("NUM"));
				}					
				errorList.add(ss + "行的统一社会信用代码或是组织机构代码对应的二码重复，表示这是同一家企业");
				for (Integer integer : list) {
					reportIndexDao.updateStatus(tempTbName, integer);
				}
				repeat.clear();
			}
		}
	}
	
	/**
	 * 唯一标识的校验
	 * @throws Exception 
	 */
	private void checkUnique(List<Map<String, Object>> excelList,List<String> uniques, String tempTbName, List<Integer> reNum,
			List<Map<String, Object>> repeat, IndexTb it, List<String> errorList) throws Exception {
		// 校验唯一标识
		for(String s : uniques){
			int a = isTempTableExists(tempTbName+"_u");
			if(a!=0){//筛选的临时表是否还在
				reportIndexDao.deleteTempTb(tempTbName+"_u");
			}
		//创建筛选的临时表，装第一次筛的数据
		StringBuffer sb = new StringBuffer("create table "+ tempTbName+"_u like "+tempTbName);
		reportIndexDao.insertIndex(sb.toString());
		//第一层重复数据装进上面的临时表	
		reportIndexDao.insertTou(tempTbName+"_u",tempTbName);
		//重第二层临时表中查唯一标识是否重复
			List<Map<String,Object>> nlist = reportIndexDao.queryRepeatUnique(tempTbName+"_u",s);
			//创建筛选的临时表，装第一次筛的数据
			StringBuffer sb2 = new StringBuffer("drop table "+ tempTbName+"_u" );
			reportIndexDao.insertIndex(sb2.toString());
			IndexItemTb itb = indexItemTbService.getIndexItemTbByCode(s);
			int t = itb.getIndexItemType();//数据类型，0字符，1时间，2数值,3数据字典
			IndexItemAlias alias = indexItemAliasDao.selectByAliasNameAndAreaId(it.getIndexId(),
					itb.getIndexItemName(), itb.getSysAreaId());
			String name = null;
			if (alias == null) {
				name = itb.getIndexItemName();
			} else {
				name = alias.getIndexItemAliasName();
			}
			System.out.println();
			try{
			for (int i = 0; i < nlist.size(); i++) {
				boolean flag = false;
				String val = (String) nlist.get(i).get(s.toUpperCase());// 当前这条数据的标识列的值
				Integer defaultId = (Integer) nlist.get(i).get("DEFAULT_INDEX_ITEM_ID");// 当前这条数据的二码值
				for (int j = i + 1; j < nlist.size(); j++) {
					String nextval = (String) nlist.get(j).get(s.toUpperCase());// 获取下一条数据
					Integer nextNefaultId = (Integer) nlist.get(j).get("DEFAULT_INDEX_ITEM_ID");// 当前这条数据的二码值
					if (val.equals(nextval) && defaultId.intValue() == nextNefaultId.intValue() ) {// 当前这条数据和下一条数据相等
						flag = true;
						repeat.add(nlist.get(j));// 将下一条数据装进重复数据集合
						nlist.remove(j);
						j--;
					}
				}
				if (flag == true) {
					List<Integer> list = new ArrayList<>();
					repeat.add(0, nlist.get(i));
					StringBuffer ss = new StringBuffer("第");
					for (Map<String, Object> map : repeat) {
						ss.append((Integer) map.get("NUM") + ",");
						list.add((Integer) map.get("NUM"));
					}					
					errorList.add(ss + "行的数据的唯一标识重复了。列名：" + name);
					for (Integer integer : list) {
						reportIndexDao.updateStatus(tempTbName, integer);
					}
					repeat.clear();
				}
			}
			}catch(Exception e){
				errorList.add("指标项："+name+"，设置成了唯一标识，其数据类型不可为时间、字典、数值，请检查");
			}
		}		
	}

	@Override
	public Integer getDataNum(String tempTbName) {
		return reportIndexDao.getDataNum(tempTbName);
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

	@Override
	public void checkCreditUnique(String tempTbName, List<String> errorList) {
		List<Map<String,Object>> orglist = reportIndexDao.queryRepeatCredit(tempTbName);
		List<Map<String, Object>> repeat = new ArrayList<>();
		 if(CollectionUtils.isNotEmpty(orglist)){
			 for (int i = 0; i < orglist.size(); i++) {
					boolean flag = false;
					String codeOrg = (String) orglist.get(i).get("codeCredit");// 当前这条数据的二码值
					for (int j = i + 1; j < orglist.size(); j++) {
						String nextCodeOrg = (String) orglist.get(j).get("codeCredit");// 当前这条数据的二码值
						if (codeOrg.equals(nextCodeOrg)) {// 当前这条数据和下一条数据相等
							flag = true;
							repeat.add(orglist.get(j));// 将下一条数据装进重复数据集合
							orglist.remove(j);
							j--;
						}
					}
					if (flag == true){
						repeat.add(0, orglist.get(i));
						StringBuffer ss = new StringBuffer("第");
						for (Map<String, Object> map : repeat){
							ss.append((Integer) map.get("num") + ",");
						}					
						errorList.add(ss + "行的统一社会信用代码:"+codeOrg+"重复。该指标未指定唯一标识，所以EXCEL中的统一社会信用代码不可重复");
						repeat.clear();
					}
				}
		 }
	}
	
	@Override
	public void checkOrgUnique(String tempTbName, List<String> errorList) {
		List<Map<String,Object>> orglist = reportIndexDao.queryRepeatOrg(tempTbName);
		List<Map<String, Object>> repeat = new ArrayList<>();
		 if(CollectionUtils.isNotEmpty(orglist)){
			 for (int i = 0; i < orglist.size(); i++) {
					boolean flag = false;
					String codeOrg = (String) orglist.get(i).get("codeOrg");// 当前这条数据的二码值
					for (int j = i + 1; j < orglist.size(); j++) {
						String nextCodeOrg = (String) orglist.get(j).get("codeOrg");// 当前这条数据的二码值
						if (codeOrg.equals(nextCodeOrg)) {// 当前这条数据和下一条数据相等
							flag = true;
							repeat.add(orglist.get(j));// 将下一条数据装进重复数据集合
							orglist.remove(j);
							j--;
						}
					}
					if (flag == true){
						repeat.add(0, orglist.get(i));
						StringBuffer ss = new StringBuffer("第");
						for (Map<String, Object> map : repeat){
							ss.append((Integer) map.get("num") + ",");
						}					
						errorList.add(ss + "行的组织机构代码:"+codeOrg+"重复。该指标未指定唯一标识，所以EXCEL中的组织机构代码不可重复");
						repeat.clear();
					}
				}
		 }		
	}	
}
