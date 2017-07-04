package com.workmanagement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

import com.workmanagement.dao.sysOperateListDao;
import com.workmanagement.model.ReportIndexError;

/**
 * excel导入_通用
 * 
 * @author danny 
 * @param
 * 			<P>
 * @see t:实体类 传入对象实例,不需要为对象赋值。如 setT(new Student());
 * @see rowNames: 列名数组（表头）,用来判断文件是否符合规范 如：{"名称","性别"}
 * @see ropertyNames: 属性名数组 如：{"name","sex"}
 */

public class ExcelReaderTwoDic<T> {
	// 实体类
	private T t;
	// 列名数组（表头）,用来判断文件是否符合规范
	private String[] rowNames;
	// 属性名数组
	private String[] propertyNames;
	//错误信息集合
	private List<ReportIndexError> reportIndexErrorList=new ArrayList<>();
	/**
	 * 获取机构码和信用码和企业名称
	 * @param file
	 * @param request
	 * @return
	 * @throws Exception 
	 */
	public List<T> excelNoValidateReader(MultipartFile file, HttpServletRequest request) throws Exception {
		List<T> list = new ArrayList<>();
		String originalFilename = file.getOriginalFilename();
		String savePath = null;
		String newFileName = null;
		if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")) {
			throw new Exception("文件不是excel类型");
		} else {
			if (file != null && originalFilename != null && originalFilename.length() > 0) {
				// 存储的物理路径
//				savePath = request.getSession().getServletContext().getRealPath("/") + "file\\";
				savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
				File myfile=new File(savePath);
				if (!myfile.exists()) {
					myfile.mkdirs();
				}
				// 新的名称
				newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
				// 新文件
				File newFile = new File(savePath + newFileName);
				// 将内存中的数据写入磁盘
				try {
					file.transferTo(newFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
					throw e;
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
		}
		try {
			// 同时支持Excel 2003、2007
			File excelFile = new File(savePath + newFileName); // 创建文件对象
			FileInputStream is = new FileInputStream(excelFile); // 文件流
			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel
															// 2003/2007/2010
															// 都是可以处理的
			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
			// 遍历每个Sheet
			for (int s = 0; s < sheetCount; s++) {
				Sheet sheet = workbook.getSheetAt(s);
				int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
				rowCount = rowCount > 51 ? 51 : rowCount;
				// 遍历每一行, 从第二行开始 ，第一行是标题;  只查询前50条记录，其他的抛弃掉
				for (int r = 1; r < rowCount; r++) {
					Row row = sheet.getRow(r);
					int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
					// 创建Class对象
					Class objClass = t.getClass();
					// 创建实例对象
					Object object = objClass.newInstance();
					// 遍历每一列
					for (int c = 0; c < cellCount; c++) {
						Cell cell = row.getCell(c);
						String cellValue = null;
						switch (cell.getCellType()) {
							case Cell.CELL_TYPE_STRING: // 文本
								cellValue = cell.getStringCellValue();
								break;
							case Cell.CELL_TYPE_NUMERIC: // 数字、日期
								cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
								double number = cell.getNumericCellValue();
		                        if (number == (int) number) {
		                            //整数
		                        	cellValue = String.valueOf((int) number);
		                        } 
								break;
						}
						//获取当前类的字段
						Field[] fields = objClass.getDeclaredFields();
						for (Field field : fields) {
							if (field.getName().equals(propertyNames[c])) {
								String propertyName = propertyNames[c];
								String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
								Method setMethod = objClass.getMethod(setMethodName,field.getType());
								setMethod.invoke(object, cellValue);
							}
						}
						//获取父类的字段
						fields = objClass.getSuperclass().getDeclaredFields();
						for (Field field : fields) {
							if (field.getName().equals(propertyNames[c])) {
								String propertyName = propertyNames[c];
								String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
								Method setMethod = objClass.getMethod(setMethodName,field.getType());
								setMethod.invoke(object, cellValue);
							}
						}
					}
					list.add((T) object);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("上传文件解析错误");
		}
		// 完成后删除
		File f = new File(savePath + newFileName);
//		System.out.println(f.getName());
		if (f.isFile() && f.exists()) {
			f.delete();
		}
		return list;
	}

	/**
	 * 返回实体类对应的list集合，需在服务层中调用保存方法(比如：save())进行保存到数据库
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	public List<T> excelReader( HttpServletRequest request,String fileName) {
		List<T> list = new ArrayList<>();
//		String originalFilename = file.getOriginalFilename();
//		String savePath = null;
//		String newFileName = null;
//		if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")) {
//			request.setAttribute("msg", "文件不是excel类型");
//			return null;
//		} else {
//			// ExcelReader excelReader=new ExcelReader();
//			// excelReader.excelReader(request);
//			if (file != null && originalFilename != null && originalFilename.length() > 0) {
//				// 存储的物理路径
////				savePath = request.getSession().getServletContext().getRealPath("/") + "file\\";
//				savePath= SettingUtils.getCommonSetting("upload.file.temp.path");
//				File myfile=new File(savePath);
//				if (!myfile.exists()) {
//					myfile.mkdirs();
//				}
//				// 新的名称
//				newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
//				// 新文件
//				File newFile = new File(savePath + newFileName);
//				// 将内存中的数据写入磁盘
//				try {
//					file.transferTo(newFile);
//				} catch (IllegalStateException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		// 导入
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 同时支持Excel 2003、2007
			File excelFile = new File(fileName); // 创建文件对象
			FileInputStream is = new FileInputStream(excelFile); // 文件流
			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel
															// 2003/2007/2010
															// 都是可以处理的
//			int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(0);
			if (row==null) {
				request.setAttribute("msg", "文件内容不能为空");
				return null;
			}
			int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
			if(cellCount==getRowNames().length){
				//int cellCount = 18; // 获取总列数
				int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数（包括空行）
				int myRowCount = 0; // 获取总行数（包括空行）
				// 遍历每一列 找到空行并过滤
				String blankRowsIndex = "";// 记录空行下标
				for (int i = 0; i < rowCount; i++) {
					boolean isBlank = true;
					if (sheet.getRow(i)==null) {
						row=null;
						blankRowsIndex += (i + 1) + ",";
					}else{
						row = sheet.getRow(i);
					}
					if (row!=null) {
						for (int j = 0; j < cellCount; j++) {
							Cell blankCell = row.getCell(j);
							if (blankCell != null) {
								if (!blankCell.toString().trim().equals("")) {
									isBlank = false;
									break;
								}
							}
						}
					}
					if (!blankRowsIndex.equals("") && isBlank == false) {
						request.setAttribute("msg", "第" + blankRowsIndex + "行为空白行 请修正或删除后上报");
						return null;
					} else if (isBlank == false) {
						myRowCount++;
					} else {// 为空行时记录下标
						blankRowsIndex += (i + 1) + ",";
					}
				}
				rowCount = myRowCount;
				// 遍历每个Sheet
//				for (int s = 0; s < sheetCount; s++) {
					if (rowCount < 2) {
						request.setAttribute("msg", "数据不能为空");
						return null;
					}
					// 遍历每一行
					for (int r = 0; r < rowCount; r++) {
						row = sheet.getRow(r);
						if (row==null) {
							request.setAttribute("msg","第"+(r+1)+"行为空白行，请修正");
							return null;
						}
						// 创建Class对象
						Class objClass = t.getClass();
						// 创建实例对象
						Object object = objClass.newInstance();
						// 遍历每一列
						for (int c = 0; c < cellCount; c++) {
							// 创建上报错误对象
							//ReportIndexError reportIndexError = null;
							Cell cell = row.getCell(c);
							if(cell==null){
								request.setAttribute("msg","第"+(r+1)+"行"+(c+1)+"列为空，请修正或删除改行");
								return null;
							}
							int cellType = cell.getCellType();
							String cellValue = null;
							// 参数类型
							String paramType = null;
							if(c==2&& r>0 ){
//								String str=cell.getStringCellValue();
//								Integer.parseInt(str);
//										cellValue = String.valueOf(Integer.parseInt(str)); // 数字
								switch (cellType) {
								case Cell.CELL_TYPE_NUMERIC: // 数字、日期
									cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
									paramType = "class java.lang.Double";
									break;
								case Cell.CELL_TYPE_STRING: // 文本
									cellValue = cell.getStringCellValue(); // 数字
									paramType = "class java.lang.Double";
									break;
								}
								
//								if(Cell.CELL_TYPE_STRING==1){
//									cellValue = cell.getStringCellValue(); // 数字
//									paramType = "class java.lang.Double";
//								}else{
//									cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
//									paramType = "class java.lang.Double";
//								}
							}else if(c==3 && r>0){
								switch (cellType) {
								case Cell.CELL_TYPE_NUMERIC: // 数字、日期
									if (DateUtil.isCellDateFormatted(cell)) {
										cellValue = fmt.format(cell.getDateCellValue()); // 日期型
										paramType = "class java.util.Date";
									}else{
											String v=String.valueOf(cell.getNumericCellValue()).substring(0,1)+String.valueOf(cell.getNumericCellValue()).substring(2,9);
											String va=v.substring(0,4)+"-"+v.substring(4,6)+"-"+v.substring(6,8);
											java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
											Date date =  formatter.parse(va);
											cellValue = fmt.format(date); // 数字
											paramType = "class java.util.Date";
									}
									break;
								case Cell.CELL_TYPE_STRING: // 文本
									java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
									Date date =  formatter.parse(cell.getStringCellValue());
									cellValue = fmt.format(date); // 数字
									paramType = "class java.util.Date";
									break;
								}
							}else{
								switch (cellType) {
								case Cell.CELL_TYPE_STRING: // 文本
									cellValue = cell.getStringCellValue();
									paramType = "class java.lang.String";
									break;
								case Cell.CELL_TYPE_BOOLEAN: // 布尔型
									cellValue = String.valueOf(cell.getBooleanCellValue());
									paramType = "class java.lang.Boolean";
									break;
								case Cell.CELL_TYPE_BLANK: // 空白
									paramType ="";
									request.setAttribute("msg","第"+(r+1)+"行"+(c+1)+"列为空，请修正或删除该行");
									return null;
//									break;
								case Cell.CELL_TYPE_ERROR: // 错误
									paramType ="";
									break;
								case Cell.CELL_TYPE_FORMULA: // 公式
									paramType ="";
									break;
								default:
									paramType ="";
								}
							
							}
//							switch (cellType) {
//							case Cell.CELL_TYPE_STRING: // 文本
//								cellValue = cell.getStringCellValue();
//								paramType = "class java.lang.String";
//								break;
//							case Cell.CELL_TYPE_NUMERIC: // 数字、日期
//								if (DateUtil.isCellDateFormatted(cell)) {
//									cellValue = fmt.format(cell.getDateCellValue()); // 日期型
//									paramType = "class java.util.Date";
//								} else {
//									if(c==3){
//										String v=String.valueOf(cell.getNumericCellValue()).substring(0,1)+String.valueOf(cell.getNumericCellValue()).substring(2,9);
//										String va=v.substring(0,4)+"-"+v.substring(4,6)+"-"+v.substring(6,8);
//										java.text.SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd");
//										Date date =  formatter.parse(va);
//										cellValue = fmt.format(date); // 数字
//										paramType = "class java.util.Date";
//									}else{
//										cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
//										paramType = "class java.lang.Double";
//									}
//								}
//								break;
//							case Cell.CELL_TYPE_BOOLEAN: // 布尔型
//								cellValue = String.valueOf(cell.getBooleanCellValue());
//								paramType = "class java.lang.Boolean";
//								break;
//							case Cell.CELL_TYPE_BLANK: // 空白
//								paramType ="";
//								request.setAttribute("msg","第"+(r+1)+"行"+(c+1)+"列为空，请修正或删除该行");
//								return null;
////								break;
//							case Cell.CELL_TYPE_ERROR: // 错误
//								paramType ="";
//								break;
//							case Cell.CELL_TYPE_FORMULA: // 公式
//								paramType ="";
//								break;
//							default:
//								paramType ="";
//							}
//							System.out.print(cellValue + "    ");
							// 判断列名
							if (r == 0) {
								if (!cellValue.equals(rowNames[c])) {
									request.setAttribute("msg", "第" + (c + 1) + "列,列名不符合规范！应为:" + rowNames[c] + "");
									return null;
								}
							}
							// 从第二行开始读入
							// 判断参数类型
							if (r> 0) {
								// 所有属性
								Field[] fields = objClass.getDeclaredFields();
								for (Field field : fields) {
									if (field.getName().equals(propertyNames[c])) {
										//属性类型
										String fieldType=field.getType().toString();
										//判断类型，这里Double包含Integer
										if (!fieldType.equals(paramType)) {
											String paramT=field.getType().getName();
											if (paramT.equals("java.lang.String")) {
												paramT="字符";
											}else if(paramT.equals("java.lang.Double")){
												paramT="整数或小数";
											}else if(paramT.equals("java.lang.Date")){
												paramT="时间";
											}else if(paramT.equals("java.lang.Boolean")){
												paramT="布尔";
											}else if(paramT.equals("java.lang.Integer")){
												paramT="整数";
											}
											//reportIndexError = new ReportIndexError();
											//reportIndexError.setReportIndexErrorName(rowNames[c]);
											//reportIndexError.setReportIndexErrorValue(cellValue);
											//reportIndexError.setReportIndexErrorNotes("第" + (r + 1) + "行" + (c + 1) + "列,数据类型有误,应为:"+paramT);
										} else {
											String propertyName = propertyNames[c];
											String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
													+ propertyName.substring(1);
											Method setMethod;
											setMethod = objClass.getMethod(setMethodName,field.getType());
											String typeName = field.getType().getName();
											if (typeName.equals("java.lang.String")) {
												setMethod.invoke(object, cellValue);
											}else if(typeName.equals("java.lang.Double")){
												Double doubleValue = Double.valueOf(cellValue);
												if (isNum(doubleValue)==false) {
													//reportIndexError = new ReportIndexError();
													//reportIndexError.setReportIndexErrorName(rowNames[c]);
													//reportIndexError.setReportIndexErrorValue(cellValue);
													//reportIndexError.setReportIndexErrorNotes("第" + (r + 1) + "行" + (c + 1) + "列,精度不能超过4");
												}
												setMethod.invoke(object,doubleValue);
											}else if(typeName.equals("java.util.Date")){
												setMethod.invoke(object, fmt.parse(cellValue));
											}else if(typeName.equals("java.lang.Boolean")){
												setMethod.invoke(object, Boolean.valueOf(cellValue));
											}else{
//												request.setAttribute("msg", "未知的错误类型！");
//												return null;
												setMethod.invoke(object, cellValue);
											}
											break;
										}
									}
								}
							}
							/*if (reportIndexError!=null) {
								reportIndexErrorList.add(reportIndexError);
							}*/
						}
						if (r>0) {
							list.add((T) object);
						}
//						System.out.println();
					}
			}
		
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 完成后删除
		File f = new File(fileName);
//		if (f.isFile() && f.exists()) {
//			f.delete();
//		}
		return list;
	}

	public ExcelReaderTwoDic() {
		super();
		// TODO Auto-generated constructor stub
	}
	/**
	 * 判断数值是否合格
	 * @param value
	 * @return
	 */
	public boolean isNum(Double value){
		boolean isNum=true;
		String strValue = value.toString();
		if (strValue.indexOf(".")!= -1) {//有精度
			if (strValue.substring(strValue.indexOf(".")).length()>4) {
				isNum=false;
			}
		}
		return isNum;
	}
	public String[] getPropertyNames() {
		return propertyNames;
	}

	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	public String[] getRowNames() {
		return rowNames;
	}

	public void setRowNames(String[] rowNames) {
		this.rowNames = rowNames;
	}

	public T getT() {
		return t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public List<ReportIndexError> getReportIndexErrorList() {
		return reportIndexErrorList;
	}

	public void setReportIndexErrorList(List<ReportIndexError> reportIndexErrorList) {
		this.reportIndexErrorList = reportIndexErrorList;
	}
	

}
