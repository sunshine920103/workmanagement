package com.workmanagement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class ExcelReaderWithNoObject<T> {
	// 实体类
		private T t;
		// 列名数组（表头）,用来判断文件是否符合规范
		private String[] rowNames;
		// 属性名数组
		private String[] propertyNames;
		private String[][] rowZhi;

		/**
		 * 返回实体类对应的list集合，需在服务层中调用保存方法(比如：save())进行保存到数据库
		 * 现用于重点企业检测上传验证
		 * @param file
		 * @param request
		 * @return
		 */
		public List<String[][]> excelReader(MultipartFile file, HttpServletRequest request) {
			List<String[][]> list = new ArrayList<>();
			String originalFilename = file.getOriginalFilename();
			String savePath = null;
			String newFileName = null;
			String subDir = "";
			request.setAttribute("msg", "");
			if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")) {
				request.setAttribute("msg", "文件不是excel类型");
			} else {
				// ExcelReader excelReader=new ExcelReader();
				// excelReader.excelReader(request);
				if (file != null && originalFilename != null && originalFilename.length() > 0) {
//					// 存储的物理路径
					savePath = request.getSession().getServletContext().getRealPath("/") + "file\\";
//					savePath = SettingUtils.getCommonSetting("upload.file.path");
//					subDir = "/" + DataUtil.buildDirName(2);
					File myfile = new File(savePath + subDir);
					if (!myfile.exists()) {
						myfile.mkdirs();
					}
					// 新的名称
					newFileName = UUID.randomUUID() + originalFilename.substring(originalFilename.lastIndexOf("."));
					// 新的名称
//					newFileName = "/" + UUID.randomUUID() + System.currentTimeMillis()
//							+ originalFilename.substring(originalFilename.lastIndexOf("."));
					// 新文件
					File newFile = new File(savePath +subDir + newFileName);
					// 将内存中的数据写入磁盘
					try {
						file.transferTo(newFile);
					} catch (IllegalStateException e) {
						LoggerUtil.error(e);
						e.printStackTrace();
					} catch (IOException e) {
						LoggerUtil.error(e);
						e.printStackTrace();
					}
				}
			}
			// 导入
			SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			DecimalFormat df = new DecimalFormat("0");
			try {
				// 同时支持Excel 2003、2007
				File excelFile = new File(savePath + newFileName); // 创建文件对象
				FileInputStream is = new FileInputStream(excelFile); // 文件流
				Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel 2003/2007/2010 都是可以处理的
				int sheetCount = workbook.getNumberOfSheets(); // Sheet的数量
				
				// 遍历每个Sheet
				outterLoop: // 标号
				for (int s = 0; s < sheetCount; s++) {
					Sheet sheet = workbook.getSheetAt(s);
					int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数
					if(rowCount==0){
						request.setAttribute("msg", "没有数据");
					}
					rowZhi=new String[rowCount-1][];
					// 遍历每一行
					for (int r = 0; r < rowCount; r++) {
						boolean cOne=false; 
						boolean cTwo=false; 
						Row row = sheet.getRow(r);
						int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
						if(cellCount!=3&&r == 0){
							request.setAttribute("msg", "列数不符合要求");
						}
						if(r>0){
							rowZhi[r-1] = new String[3];  
						}
						// 创建Class对象
						Class objClass = Object.class;
						// 创建实例对象
						Object object = objClass.newInstance();
						// 遍历每一列
						for (int c = 0; c < 3; c++) {
							//获取一个sheet中的
							Cell cell = row.getCell(c);
							String cellValue = null;
							String paramType = "";
							if(cell==null){
								cellValue="";
							}else{
								int cellType = cell.getCellType();
								// 参数类型
								switch (cellType) {
								case Cell.CELL_TYPE_STRING: // 文本
									cellValue = cell.getStringCellValue();
									paramType = "class java.lang.String";
									break;
								case Cell.CELL_TYPE_NUMERIC: // 数字、日期
									if (DateUtil.isCellDateFormatted(cell)) {
										cellValue = fmt.format(cell.getDateCellValue()); // 日期型
										paramType = "class java.util.Date";
									} else {
//										request.setAttribute("msg", "组织机构代码或统一社会信用代码不能为纯数字");
										cellValue = df.format(cell.getNumericCellValue()).trim(); // 数字,将科学计数法转换为数值
										cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
										paramType = "class java.lang.Double";
									}
									break;
								case Cell.CELL_TYPE_BOOLEAN: // 布尔型
									cellValue = String.valueOf(cell.getBooleanCellValue());
									paramType = "class java.lang.Boolean";
									break;
								case Cell.CELL_TYPE_BLANK: // 空白
									paramType ="";
									break;
								case Cell.CELL_TYPE_ERROR: // 错误
									paramType ="";
									break;
								case Cell.CELL_TYPE_FORMULA: // 公式
									paramType ="";
									break;
								default:
									paramType ="";
								}
								System.out.print(cellValue + "    ");
								// 判断列名
							}
							if (r == 0) {
								if (!rowNames[c].equals(cellValue)) {
									request.setAttribute("msg", "第" + (c + 1) + "列,列名不符合规范！应为:" + rowNames[c] + "");
									break outterLoop;
								}
							}
							// 从第二行开始读入
							
							// 判断参数类型
							if (r> 0) {
								rowZhi[r-1][c] = cellValue; 
								if (c == 1 && (cellValue == null || cellValue == "")) {
									cOne = true;
								}
								if (c == 2 && (cellValue == null || cellValue == "")) {
//									request.setAttribute("msg",  "第"+(r+1)+"行统一社会信用代码不能为空");
									cTwo = true;
//									break outterLoop;
								}
								if(c == 0 && (cellValue == null || cellValue == "")){
									request.setAttribute("msg", "第"+(r+1)+"行企业名称值不能为空");
									break outterLoop;
								}
								if(cOne&&cTwo){
									request.setAttribute("msg",  "第"+(r+1)+"行统一社会信用码和组织机构代码不能都为空");
									break outterLoop;
								}
								else{
									if(c == 1&&!cOne){//组织机构代码不为空
										if(cellValue.length() != 10){
											request.setAttribute("msg",  "第"+(r+1)+"行组织机构代码长度不为10");
											break outterLoop;
										}
										if(isNumAndLetterAndLine(cellValue) == false){
											request.setAttribute("msg",  "第"+(r+1)+"行:组织机构代码只能为数字和字母，或数字和字母的组合 且倒数第二位必须为'-'");
											break outterLoop;
										}
									}
									
									if(c == 2 && !cTwo){//信用码不为空
										if (isNumAndLetter(cellValue) == false){
											request.setAttribute("msg",  "第"+(r+1)+"行:统一社会信用码只能为数字和字母，或数字和字母的组合");
											break outterLoop;
										}
										if(cellValue.length() != 18){
											request.setAttribute("msg",  "第"+(r+1)+"行统一社会信用代码长度不为18");
											break outterLoop;
										}
									}
								}
								// 所有属性
								Field[] fields = objClass.getDeclaredFields();
								for (Field field : fields) {
									if (field.getName().equals(propertyNames[c])) {
										//属性类型
										String fieldType=field.getType().toString();
										//判断类型，这里Double包含Integer
										if (!fieldType.equals(paramType)&&!(fieldType.equals("class java.lang.Double")||fieldType.equals("class java.lang.Integer"))&&paramType.equals("class java.lang.Double")) {
											String paramT=field.getType().getName();
											if (paramT.equals("java.lang.String")) {
												paramT="字符";
											}else if(paramT.equals("java.lang.Double")){
												paramT="整数/小数";
											}else if(paramT.equals("java.lang.Date")){
												paramT="时间";
											}else if(paramT.equals("java.lang.Boolean")){
												paramT="布尔";
											}else if(paramT.equals("java.lang.Integer")){
												paramT="整数";
											}
											request.setAttribute("msg", "第" + (r + 1) + "行" + (c + 1) + "列,对应的参数类型不符合规范！应为:"+paramT);
											break outterLoop;
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
												setMethod.invoke(object, Double.valueOf(cellValue));
											}else if(typeName.equals("java.util.Date")){
												setMethod.invoke(object, fmt.format(cellValue));
											}else if(typeName.equals("java.lang.Boolean")){
												setMethod.invoke(object, Boolean.valueOf(cellValue));
											}else{
												request.setAttribute("msg", "未知的错误类型！");
												break outterLoop;
											}
											break;
										}
									}
								}
							}
						}
						System.out.println();
					}
					list.add(rowZhi);
				}
			} catch (Exception e) {
				LoggerUtil.error(e);
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
		 * 数字和字母
		 * 
		 * @param s
		 * @return
		 */
		public boolean isNumAndLetter(String s) {
			if (s != null && !"".equals(s.trim()))
				return s.matches("^[0-9a-zA-Z]*$");
			else
				return false;
		}
		/**
		 * 数字和字母及中划线
		 * 
		 * @param s
		 * @return
		 */
		public boolean isNumAndLetterAndLine(String s) {
			if (s != null && !"".equals(s.trim()))
				return s.matches("^[0-9a-zA-Z]{8}-[0-9a-zA-Z]{1}$");
			else
				return false;
		}
		public ExcelReaderWithNoObject() {
			super();
			// TODO Auto-generated constructor stub
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
