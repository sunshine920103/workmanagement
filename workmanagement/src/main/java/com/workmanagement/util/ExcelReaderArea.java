package com.workmanagement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wqs
 * @date 创建时间：2017年3月12日 下午5:19:20
 * @version 1.0
 * @parameter
 * @see t:实体类 传入对象实例,不需要为对象赋值。如 setT(new Student());
 * @see rowNames: 列名数组（表头）,用来判断文件是否符合规范 如：{"名称","性别"}
 * @see ropertyNames: 属性名数组 如：{"name","sex"}
 * @return
 */
public class ExcelReaderArea<T> {

	private T t; // 实体类
	private String[] rowNames; // 列名数组（表头）,用来判断文件是否符合规范
	private String[] propertyNames; // 属性名数组

	public ExcelReaderArea() {
		super();
	}

	/**
	 * 获取机构码和信用码和企业名称
	 * 
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
				savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
				File myfile = new File(savePath);
				if (!myfile.exists()) {
					myfile.mkdirs();
				}
				// 新的名称
				newFileName = "/" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
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
				for (int r = 1; r < rowCount; r++) {  //从第二行开始是数据
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
								// 整数
								cellValue = String.valueOf((int) number);
							}
							break;
						}
						// 获取当前类的字段
						Field[] fields = objClass.getDeclaredFields();
						for (Field field : fields) {
							if (field.getName().equals(propertyNames[c])) {
								String propertyName = propertyNames[c];
								String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
										+ propertyName.substring(1);
								Method setMethod = objClass.getMethod(setMethodName, field.getType());
								setMethod.invoke(object, cellValue);
							}
						}
						// 获取父类的字段
						fields = objClass.getSuperclass().getDeclaredFields();
						for (Field field : fields) {
							if (field.getName().equals(propertyNames[c])) {
								String propertyName = propertyNames[c];
								String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
										+ propertyName.substring(1);
								Method setMethod = objClass.getMethod(setMethodName, field.getType());
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
		System.out.println(f.getName());
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
	public List<T> excelReader(MultipartFile file, HttpServletRequest request,String filePath) {
		List<T> list = new ArrayList<>();
		String originalFilename = file.getOriginalFilename();
		String savePath = null;
		String newFileName = null;
		if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")) {
			request.setAttribute("msg", "文件不是excel类型");
			return null;
		} else {
			if (file != null && originalFilename != null && originalFilename.length() > 0) {
				savePath = SettingUtils.getCommonSetting("upload.file.temp.path");
				File myfile = new File(savePath);
				if (!myfile.exists()) {
					myfile.mkdirs();
				}
				// 新的名称
				newFileName = "/" + UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
				// 新文件
				File newFile = new File(savePath + newFileName);
				filePath  = savePath + newFileName;
				// 将内存中的数据写入磁盘
				try {
					file.transferTo(newFile);
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// 导入
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			// 同时支持Excel 2003、2007
			File excelFile = new File(savePath + newFileName); // 创建文件对象
			FileInputStream is = new FileInputStream(excelFile); // 文件流
			Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel   2003/2007/2010   都是可以处理的
			Sheet sheet = workbook.getSheetAt(0);
			HSSFRow row = (HSSFRow)sheet.getRow(0);
			if (row == null) {
				request.setAttribute("msg", "文件内容不能为空");
				return null;
			}
			int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
			int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数（包括空行）
			//int rowCount = sheet.getLastRowNum();
			if (rowCount < 2) {
				request.setAttribute("msg", "数据不能为空");
				return null;
			}
			// 遍历每一行
			outterLoop: // 标号
			for (int r = 0; r < rowCount; r++) {
				row = (HSSFRow)sheet.getRow(r);
				if (row == null) {
					request.setAttribute("msg", "第" + (r + 1) + "行为空白行，请修正");
					return null;
				}
				// 创建Class对象
				Class objClass = t.getClass();
				// 创建实例对象
				Object object = objClass.newInstance();
				// 遍历每一列
				for (int c = 0; c < cellCount; c++) {
					HSSFCell cell = row.getCell(c);
					if (r==0 && cell == null) {
						request.setAttribute("msg", "第" + (r + 1) + "行" + (c + 1) + "列为空，请修正");
						break outterLoop;
					}
					int cellType ;
					if(cell != null ){	
						 cellType = cell.getCellType();
					}else{
						 cellType = 3;
					}
					if(c==0 && cellType==3){
						break  outterLoop;
					}
					String cellValue = null;
					// 参数类型
					String paramType = null;
					switch (cellType) {
					case Cell.CELL_TYPE_STRING: // 文本
						cellValue =cell.getRichStringCellValue().toString();
						//cellValue = cellValue.replace("\\u00A0", "");
						//去除Excel中的空格
						cellValue = cellValue.replace(" ", "");
						cellValue = cellValue.trim();					
						paramType = "class java.lang.String";
						break;
					case Cell.CELL_TYPE_NUMERIC: // 数字、日期
						if (DateUtil.isCellDateFormatted(cell)) {
							cellValue = fmt.format(cell.getDateCellValue()); // 日期型
							paramType = "class java.util.Date";
						} else {
							cellValue = (int)cell.getNumericCellValue()+""; // 数字
							paramType = "class java.lang.String";
						}
						break;
					case Cell.CELL_TYPE_BOOLEAN: // 布尔型
						cellValue = String.valueOf(cell.getBooleanCellValue());
						paramType = "class java.lang.Boolean";
						break;
					case Cell.CELL_TYPE_BLANK: // 空白
						cellValue = "";
						paramType = "class java.lang.String";
						break;
					case Cell.CELL_TYPE_ERROR: // 错误
						paramType = "";
						break;
					case Cell.CELL_TYPE_FORMULA: // 公式
						paramType = "";
						break;
					default:
						paramType = "";
					}
					// 判断列名
					if (r == 0) {
						if (!cellValue.trim().equals(rowNames[c])) {
							request.setAttribute("msg", "第" + (c + 1) + "列,列名不符合规范！应为:" + rowNames[c] + "");
							return null;
						}
					}
					// 从第二行开始读入
					// 判断参数类型
					if (r > 0) { // 所有属性
						Field[] fields = objClass.getDeclaredFields();
						for (Field field : fields) {
							if (field.getName().equals(propertyNames[c])) { // 属性类型
								String fieldType = field.getType().toString();
								// 判断类型，这里Double包含Integer
								if (!fieldType.equals(paramType)) {
									String paramT = field.getType().getName();
									if (paramT.equals("java.lang.String")) {
										paramT = "字符";
									} else if (paramT.equals("java.lang.Double")) {
										paramT = "整数或小数";
									} else if (paramT.equals("java.lang.Date")) {
										paramT = "时间";
									} else if (paramT.equals("java.lang.Boolean")) {
										paramT = "布尔";
									} else if (paramT.equals("java.lang.Integer")) {
										paramT = "整数";
									}
									request.setAttribute("msg",
											"第" + (r + 1) + "行" + (c + 1) + "列,对应的参数类型不符合规范！应为:" + paramT);
									return null;
								} else {
									String propertyName = propertyNames[c];
									String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
											+ propertyName.substring(1);
									Method setMethod;
									setMethod = objClass.getMethod(setMethodName, field.getType());
									String typeName = field.getType().getName();
									if (typeName.equals("java.lang.String")) {
										setMethod.invoke(object, cellValue.trim());
									} else if (typeName.equals("java.lang.Double")) {
										setMethod.invoke(object, Double.valueOf(cellValue));
									} else if (typeName.equals("java.util.Date")) {
										setMethod.invoke(object, fmt.format(cellValue));
									} else if (typeName.equals("java.lang.Boolean")) {
										setMethod.invoke(object, Boolean.valueOf(cellValue));
									} else {
										request.setAttribute("msg", "未知的错误类型！");
										setMethod.invoke(object, cellValue);
										return null;
									}
									break;
								}
							}
						}
					}
				}
				if (r > 0) {
					list.add((T) object);
					System.out.println();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 完成后删除
		File f = new File(savePath + newFileName);
		//System.out.println(f.getName());
		if (f.isFile() && f.exists()){
			f.delete();
		}
		return list;
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
}
