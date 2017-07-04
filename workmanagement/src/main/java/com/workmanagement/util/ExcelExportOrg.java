package com.workmanagement.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFComment;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.RichTextString;
import org.springframework.beans.factory.annotation.Autowired;

import com.workmanagement.model.IndexItemTb;
import com.workmanagement.model.ReportExcelTemplate;
import com.workmanagement.service.IndexItemTbService;
/**
 * excel导出
 * @author renyang
 * @see title：表名		例："xxx表/xxx模板"
 * @see rowNames：列名数组(表头) 		例：{"名称","性别"}
 * @see propertyNames：属性名数组,可为空 		例：{"name","sex"} 
 * @see list<T>: 	数据集，要导出的list集合,可为空
 */
public class ExcelExportOrg<T> {
	// 要生成的文件名
	private String title;
	// 随机生成的文件名
	private String newFileName;
	// 列名数组
	private String[] rowNames;
	// 列头提示信息
	private String[] prompt;
	//设置数据字典动态长度
	private Integer row;
	// 属性名数组
	private String[] propertyNames;
	// 数据集
	private List<T> list;
	// 单个对象
	private Object obj;
	//下载后是否删除文件，默认删除
	private Boolean isDelete=true;
	//是否只生成不下载,默认生成及下载
	private Boolean onlyGenerate=false;
	//是否为excel模板导出 可为空
	private ReportExcelTemplate reportExcelTemplate;
	@Autowired
	private IndexItemTbService indexItemTbService;
	
	
	/**
	 * 导出无对象的excel list<list>
	 * @param response
	 */
	public void exportExcel(HttpServletResponse response ){
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个sheet并命名
		HSSFSheet sheet = wb.createSheet(title);
		// 定义单元格宽度
		sheet.setDefaultColumnWidth((short) 25);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		for (int i = 0; i < rowNames.length; i++) {
			cell = row.createCell((short) i);
			cell.setCellValue(rowNames[i]);
			cell.setCellStyle(style);
		}
		// 向单元格里填充数据
		if (list!=null) {
			int rowNum = 1;
			for(T infos : list){
				List info = (List) infos;
				setData(info, sheet, style, rowNum++);
			}
		}
		
		try {
			String filename=null;
            filename= new String(title.getBytes("gbk"), "ISO_8859_1");
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename="+filename+".xls");    
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void setData(List info, HSSFSheet sheet, HSSFCellStyle style, int rowNum) {
		// TODO Auto-generated method stub
		HSSFRow row = sheet.createRow(rowNum);
		int num=0;
		
		for(int i = 0; i < info.size();i++){
			Object val = info.get(i);
			String textName = "";
			if(val!=null){
				String className = val.getClass().getName();
				if("java.util.Date".equals(className)){
					textName = DateFormatter.formatDateTime((Date)val);
				}else if("java.lang.Integer".equals(className)){
					textName = ((Integer) val).toString();
				}else if("java.lang.String".equals(className)){
					textName = val.toString();
				}else if("java.lang.Double".equals(className)){
					textName = ((Double) val).toString();
				}else if("java.lang.Float".equals(className)){
					textName = ((Float) val).toString();
				}else if("java.sql.Timestamp".equals(className)){
					textName = DateFormatter.formatDateTime((Date) val);
				}else if("java.lang.Boolean".equals(className)){
					textName = (Boolean)val?"是":"否";
				}
			}
			
			HSSFCell createCell = row.createCell(num++);
			createCell.setCellValue(textName);
			createCell.setCellStyle(style);
		}
	}
	/**
	 * 导出无对象的excel
	 * @param response
	 */
	public void exportMapExcel(HttpServletResponse response ){
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个sheet并命名
		HSSFSheet sheet = wb.createSheet(title);
		// 定义单元格宽度
		sheet.setDefaultColumnWidth((short) 25);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		for (int i = 0; i < rowNames.length; i++) {
			cell = row.createCell((short) i);
			cell.setCellValue(rowNames[i]);
			cell.setCellStyle(style);
		}
		// 向单元格里填充数据
		if (list!=null) {
			int rowNum = 1;
			for(Object infos : list){
				Map<String, Object> info = (Map<String, Object>) infos;
				setMapData(info, sheet, style, rowNum++);
			}
		}
		
		try {
			String filename=null;
            filename= new String(title.getBytes("gbk"), "ISO_8859_1");
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename="+filename+".xls");    
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close();    
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 通过 MAP 设置数据
	 * @param object
	 * @param sheet
	 * @param style
	 * @param rowNum 行数
	 */
	private void setMapData(Map<String, Object> map, HSSFSheet sheet, HSSFCellStyle style, Integer rowNum){
		HSSFRow row = sheet.createRow(rowNum);
		int num=0;
		for (String propertyName : propertyNames) {
			Object val = map.get(propertyName);
				
			String textName = "";
			if(val!=null){
				String className = val.getClass().getName();
				if("java.util.Date".equals(className)){
					textName = DateFormatter.formatDateTime((Date)val);
				}else if("java.lang.Integer".equals(className)){
					textName = ((Integer) val).toString();
				}else if("java.lang.String".equals(className)){
					textName = val.toString();
				}else if("java.lang.Double".equals(className)){
					textName = ((Double) val).toString();
				}else if("java.lang.Float".equals(className)){
					textName = ((Float) val).toString();
				}else if("java.sql.Timestamp".equals(className)){
					textName = DateFormatter.formatDateTime((Date) val);
				}else if("java.lang.Boolean".equals(className)){
					textName = (Boolean)val?"是":"否";
				}
			}
			
			HSSFCell createCell = row.createCell(num++);
			createCell.setCellValue(textName);
			createCell.setCellStyle(style);
		}
	}
	
	
	
	
	
	/**
	 * 导出单个对象， 如果对象里面包含集合属性，需要把集合的字段名放在 propertyNames 的最后， propertyNames要和 rowNames对应
	 * 如：rowNames = {"名称","年龄}; 
	 * 	   propertyNames = {"name", "age", list}；
	 * 导出对象里的集合时，集合里装的只能是和导出对象一样的对象
	 * @param response
	 */
	public String exportObjctExcel(HttpServletResponse response ) {
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个sheet并命名
		HSSFSheet sheet = wb.createSheet(title);
		// 定义单元格宽度
		sheet.setDefaultColumnWidth((short) 25);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		for (int i = 0; i < rowNames.length; i++) {
			cell = row.createCell((short) i);
			cell.setCellValue(rowNames[i]);
			cell.setCellStyle(style);
		}
		// 向单元格里填充数据
		if (obj!=null) {
			creatData(obj, sheet, style, 1);
		}
		
		try {
			String path = SettingUtils.getCommonSetting("upload.file.temp.path");
			File myfile=new File(path);
			if (!myfile.exists()) {
				myfile.mkdirs();
			}
			// 新的名称
			newFileName = "/"+UUID.randomUUID()+newFileName+".xls";
			FileOutputStream out = new FileOutputStream(path+newFileName);
			wb.write(out);
			out.flush();
			out.close();
			String filename=null;
            filename= new String(title.getBytes("gbk"), "ISO_8859_1");
			response.setContentType("application/vnd.ms-excel");    
	        response.setHeader("Content-disposition", "attachment;filename="+filename+".xls"); 
	        OutputStream ouputStream = response.getOutputStream();    
	        wb.write(ouputStream);    
	        ouputStream.flush();    
	        ouputStream.close(); 
	        return path+newFileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建数据
	 * @param object
	 * @param row
	 * @param style
	 */
	private int creatData(Object object, HSSFSheet sheet, HSSFCellStyle style, Integer rowNum){
		HSSFRow row = sheet.createRow(rowNum);
		Class classT = object.getClass();
		int num=0;
		for (String propertyName : propertyNames) {
			String getMethodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
			Method getMethod;
			Object val = null;
			try {
				getMethod = classT.getMethod(getMethodName, new Class[] {});
				try {
					val = getMethod.invoke(object, new Object[] {});
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					e.printStackTrace();
				}
				
				String textName = "";
				if(val!=null){
					String className = val.getClass().getName();
					//判断数据的类型
					if("java.util.ArrayList".equals(className)){
						List list = (ArrayList)val;
						for(Object o : list){
							rowNum = creatData(o, sheet, style, ++rowNum);
						}
					}else if("java.util.Date".equals(className)){
						textName = DateFormatter.formatDateTime((Date)val);
					}else if("java.lang.Integer".equals(className)){
						textName = ((Integer) val)==0?"机构":"政府";
					}else if("java.lang.String".equals(className)){
						textName = val.toString();
					}else if("java.lang.Boolean".equals(className)){
						textName = (Boolean)val?"是":"否";
					}
				}
				
				HSSFCell createCell = row.createCell(num++);
				createCell.setCellValue(textName);
				createCell.setCellStyle(style);
			} catch (Exception e) {
				e.printStackTrace();
			} 			
		}
		return rowNum;
	}
	
	
	/**
	 * 导出集合数据
	 * @param request
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public String exportExcel(HttpServletRequest request,HttpServletResponse response ) {
		// 声明一个工作薄
		HSSFWorkbook wb = new HSSFWorkbook();
		// 声明一个单子并命名
		HSSFSheet sheet = wb.createSheet(title);
		// 得到一个POI的工具类  
        CreationHelper factory = wb.getCreationHelper();
		// 给单子名称一个长度
		sheet.setDefaultColumnWidth((short) 25);
		// 生成一个样式
		HSSFCellStyle style = wb.createCellStyle();
		// 创建第一行（表头）
		HSSFRow row = sheet.createRow(0);
		// 样式字体居中
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		// 给表头第一行一次创建单元格
		HSSFCell cell = row.createCell((short) 0);
		//设置单元格格式为文本格式
		HSSFCellStyle textStyle = wb.createCellStyle();
		HSSFDataFormat format = wb.createDataFormat();
		textStyle.setDataFormat(format.getFormat("@"));
		textStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		//设置单元格格式为整数格式
		HSSFCellStyle numStyle = wb.createCellStyle();
		HSSFDataFormat numDf = wb.createDataFormat(); 
		numStyle.setDataFormat(numDf.getFormat("@"));
		numStyle.setDataFormat(numDf.getBuiltinFormat("0"));//数据格式只显示整数
		numStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		//设置单元格格式为小数格式
		HSSFCellStyle accuracyStyle = wb.createCellStyle();
		HSSFDataFormat accuracyStyleDf = wb.createDataFormat(); 
		accuracyStyle.setDataFormat(accuracyStyleDf.getFormat("@"));
		accuracyStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		
		//为excel模板导出时
		String[] codeSplit =null;
        if (reportExcelTemplate!=null) {
        	String reportExcelTemplateIndexItemSet = reportExcelTemplate.getReportExcelTemplateIndexItemSet();
        	codeSplit = reportExcelTemplateIndexItemSet.split(",");
        }
		for (int i = 0; i < rowNames.length; i++) {
			//为excel模板导出时
	        if (codeSplit!=null) {
	        	IndexItemTb indexItemTb = indexItemTbService.getIndexItemTbByCode(codeSplit[i].trim());
	        	if (indexItemTb.getIndexItemType().intValue()==0) {//判断是否为文本
	        		sheet.setDefaultColumnStyle((short) i,textStyle);//列格式为文本
	        		sheet.setColumnWidth(i, 21 * 256); //列宽
				}else if(indexItemTb.getIndexItemType().intValue()==2){//判断是否为数字
					if (indexItemTb.getIndexItemAccuracy()==null ||indexItemTb.getIndexItemAccuracy().intValue()==0) {//是否有精度
						sheet.setDefaultColumnStyle((short) i,numStyle);//列格式为数字
		        		sheet.setColumnWidth(i, 21 * 256); //列宽
					}else{
						int indexItemAccuracy = indexItemTb.getIndexItemAccuracy().intValue();
						String length="0.";
						for (int j = 0; j < indexItemAccuracy; j++) {
							length+="0";
						}
						accuracyStyle.setDataFormat(accuracyStyleDf.getBuiltinFormat(length));//保留两位小数点
						sheet.setDefaultColumnStyle((short) i,accuracyStyle);//列格式为数字
		        		sheet.setColumnWidth(i, 21 * 256); //列宽
					}
				}
			}
	        cell = row.createCell((short) i);
			cell.setCellValue(rowNames[i]);
			cell.setCellStyle(style);
			if(i==1){
			// 得到一个换图的对象  
	        Drawing drawing = sheet.createDrawingPatriarch();  
	        // ClientAnchor是附属在WorkSheet上的一个对象，  其固定在一个单元格的左上角和右下角.  
	        ClientAnchor anchor = factory.createClientAnchor();
	        anchor.setCol1(cell.getColumnIndex());  
	        //宽度
	        anchor.setCol2(cell.getColumnIndex() + 1);  
	        anchor.setRow1(cell.getRowIndex());  
	        //高度
	        anchor.setRow2(cell.getRowIndex() + getRow()+2);
			// 对这个单元格加上注解  
	        Comment comment = drawing.createCellComment(anchor);  
	        RichTextString str = factory.createRichTextString(prompt[i]);  
	        comment.setString(str);  
	        cell.setCellComment(comment);
			}
//			if(i==1){
//			  HSSFPatriarch patr = sheet.createDrawingPatriarch();
//		      // 定义注释的大小和位置，详见文档
//		      HSSFComment comment = patr.createComment(new HSSFClientAnchor(0,0,0,0, (short)4, 0 ,(short) 6, 8));
//		      // 设置注释内容
//		      comment.setString(new HSSFRichTextString(prompt[i]));
//		      cell.setCellComment(comment);
//			}
		}
        
		// 向单元格里填充数据
		if (list!=null) {
			for (short i = 0; i < list.size(); i++) {
				row = sheet.createRow(i + 1);
				Class classT = list.get(i).getClass();
				int num=-1;
				for (String propertyName : propertyNames) {
					num++;
					String getMethodName = "get" + propertyName.substring(0, 1).toUpperCase() 
							+ propertyName.substring(1);
					Method getMethod;
					Object val = null;
					try {
						getMethod = classT.getMethod(getMethodName, new Class[] {});
						try {
							val = getMethod.invoke(list.get(i), new Object[] {});
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String textVal = null;
						if (val== null) {
							textVal = "";
						}else if(val.toString().equals("null")){
							textVal = "";
						}
						else{
							textVal = val.toString();
						}
						HSSFCell createCell = row.createCell(num);
						createCell.setCellValue(textVal);
						createCell.setCellStyle(style);
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}
			}
		}
		try {
			// 导出
//			String path= request.getSession().getServletContext().getRealPath("/")+"file\\";
			String path = SettingUtils.getCommonSetting("upload.file.temp.path");
			File myfile=new File(path);
			if (!myfile.exists()) {
				myfile.mkdirs();
			}
			// 新的名称
			newFileName = "/"+UUID.randomUUID()+newFileName+".xls";
			FileOutputStream out = new FileOutputStream(path+newFileName);
			wb.write(out);
			out.flush();
			out.close();
			// 读取
			File file =null;
			if (onlyGenerate==false) {// 下载
				file = new File(path+newFileName);
				if (file == null || !file.exists()) {
					return null;
				}
				OutputStream outTwo = null;
				try {
					//处理文件中文乱码
					response.reset();
					//设置文件MIME类型  
			        response.setContentType(request.getSession().getServletContext().getMimeType(".xls")); 
					String filename=null;
		            filename= new String(title.getBytes("gbk"), "ISO_8859_1");
		            response.setHeader("Content-disposition","attachment; filename="+filename+".xls");
					outTwo = response.getOutputStream();
					outTwo.write(FileUtils.readFileToByteArray(file));
					outTwo.flush();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (outTwo != null) {
						try {
							outTwo.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// 下载完成后是否删除
//			 判断目录或文件是否存在
//			 路径为文件且不为空则进行删除
			/*if(isDelete){
				if (file.isFile() && file.exists()) {
					file.delete();
				}
			}*/
			return path+newFileName;
			//弹窗提示
//			JOptionPane.showMessageDialog(null, "导出成功!");
		} catch (FileNotFoundException e) {
//			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		} catch (IOException e) {
//			JOptionPane.showMessageDialog(null, "导出失败!");
			e.printStackTrace();
		}
		return null;
	}

	public ExcelExportOrg() {
		super();
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String[] getRowNames() {
		return rowNames;
	}

	public void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}
	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}
	
	public Object getObj() {
		return obj;
	}
	public void setObj(T obj) {
		this.obj = obj;
	}

	public Boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Boolean getOnlyGenerate() {
		return onlyGenerate;
	}

	public void setOnlyGenerate(Boolean onlyGenerate) {
		this.onlyGenerate = onlyGenerate;
	}

	public String getNewFileName() {
		return newFileName;
	}

	public ReportExcelTemplate getReportExcelTemplate() {
		return reportExcelTemplate;
	}

	public void setReportExcelTemplate(ReportExcelTemplate reportExcelTemplate) {
		this.reportExcelTemplate = reportExcelTemplate;
	}

	public void setIndexItemTbService(IndexItemTbService indexItemTbService) {
		this.indexItemTbService = indexItemTbService;
	}
	public String[] getPrompt() {
		return prompt;
	}
	public void setPrompt(String[] prompt) {
		this.prompt = prompt;
	}
	public Integer getRow() {
		return row;
	}
	public void setRow(Integer row) {
		this.row = row;
	}
	
	
}
