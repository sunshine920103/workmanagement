package com.workmanagement.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;


/**
 * excel导入_月季报使用
 * 
 * @author danny
 * @param
 * 			<P>
 * @see t:实体类 传入对象实例,不需要为对象赋值。如 setT(new Student());
 * @see rowNames: 列名数组（表头）,用来判断文件是否符合规范 如：{"名称","性别"}
 * @see ropertyNames: 属性名数组 如：{"name","sex"}
 */

public class ExcelReaderThree<T> {
//	// 实体类
//	private T t;
//	// 错误信息集合
//	private List<ReportIndexError> reportIndexErrorList = new ArrayList<>();
//
//	/**
//	 * 返回实体类对应的list集合，需在服务层中调用保存方法(比如：save())进行保存到数据库
//	 * 
//	 * @param file
//	 * @param request
//	 * @return
//	 * @throws Exception 
//	 */
//	public List<T> excelReader(ReportMonthSeason reportMonthSeason,MultipartFile file, HttpServletRequest request, ReportIndex reportIndex) throws Exception {
//		List<T> list = new ArrayList<>();
//		try {
//			Field[] fields = t.getClass().getDeclaredFields();// 获取实体类的所有属性，返回Field数组
//			String originalFilename = file.getOriginalFilename();
//			String savePath = null;
//			String newFileName = null;
//			String subDir =null;
//			if (!originalFilename.endsWith(".xls") && !originalFilename.endsWith(".xlsx")) {
//				request.setAttribute("msg", "文件不是excel类型");
//				return null;
//			} else {
//				if (file != null && originalFilename != null && originalFilename.length() > 0) {
//					// 存储的物理路径
//					// savePath =
//					// request.getSession().getServletContext().getRealPath("/")
//					// + "file\\";
//					savePath = SettingUtils.getCommonSetting("upload.file.path");
//					subDir = "/userUploadMonthSeasonFile";
//					// 新的名称
//					newFileName = "/" + UUID.randomUUID() + System.currentTimeMillis()
//					+ originalFilename.substring(originalFilename.lastIndexOf("."));
//					reportIndex.setReportIndexPath(subDir + newFileName);// 保存文件名
//					// 新文件
//					File newFile = new File(savePath + subDir + newFileName);
//					if (!newFile.exists()) {
//						newFile.mkdirs();
//					}
//					// 将内存中的数据写入磁盘
//					try {
//						file.transferTo(newFile);
//					} catch (IllegalStateException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//			}
//			// 导入
//			SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
//			SimpleDateFormat fmtTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				// 同时支持Excel 2003、2007
//				File excelFile = new File(savePath +subDir+ newFileName); // 创建文件对象
//				FileInputStream is = new FileInputStream(excelFile); // 文件流
//				Workbook workbook = WorkbookFactory.create(is); // 这种方式 Excel
//																// 2003/2007/2010
//																// 都是可以处理的
//				Sheet sheet = workbook.getSheetAt(0);
//				Row row = sheet.getRow(0);
//				if (row == null) {
//					request.setAttribute("msg", "文件内容不能为空");
//					return null;
//				}
//				int cellCount = fields.length-8; // 获取总列数 减去serialVersionUID和主键、状态等
////				int cellCount = row.getPhysicalNumberOfCells(); // 获取总列数
//				int rowCount = sheet.getPhysicalNumberOfRows(); // 获取总行数（包括空行）
//				
//				int myRowCount = 0; // 获取总行数（不包括空行）
//				String blankRowsIndex = "";// 记录空行下标
//				for (int i = 0; i < rowCount; i++) {
//					boolean isBlank = true;
//					if (sheet.getRow(i)==null) {
//						row=null;
//						blankRowsIndex += (i + 1) + ",";
//					}else{
//						row = sheet.getRow(i);
//					}
//					if (row!=null) {
//						for (int j = 0; j < cellCount; j++) {
//							Cell blankCell = row.getCell(j);
//							boolean mergedRegion = isMergedRegion(sheet, i, j);
//							if (mergedRegion) {//是否为合并单元格
//								isBlank = false;
//								break;
//							}
//							if (blankCell != null) {
//								if (!blankCell.toString().trim().equals("")) {
//									isBlank = false;
//									break;
//								}
//							}
//						}
//					}
//					if (!blankRowsIndex.equals("") && isBlank == false) {
//						request.setAttribute("msg", "第" + blankRowsIndex + "行为空白行 请修正或删除后上报");
//						return null;
//					} else if (isBlank == false) {
//						myRowCount++;
//					} else {// 为空行时记录下标
//						blankRowsIndex += (i + 1) + ",";
//					}
//				}
//				rowCount = myRowCount;
//				//开始读取行
//				Integer beginRow = reportMonthSeason.getReportMonthSeasonTemplateBeginRow();
//				if (rowCount <beginRow) {
//					request.setAttribute("msg", "数据不能为空");
//					return null;
//				}
//				// 遍历每一行
//				for (int r = 0; r < rowCount; r++) {
//					if (r<beginRow-1) {
//						continue;
//					}
//					row = sheet.getRow(r);
//					if (row == null) {
//						request.setAttribute("msg", "第" + (r + 1) + "行为空白行，请修正");
//						return null;
//					}
//					// 创建Class对象
//					Class objClass = t.getClass();
//					// 创建实例对象
//					Object object = objClass.newInstance();
//					
//					// 遍历每一列
//					for (int c = 0; c < cellCount; c++) {
//						// 创建上报错误对象
//						ReportIndexError reportIndexError = null;
//						Cell cell = row.getCell(c);
//						if (cell == null) {
//							request.setAttribute("msg", "第" + (r + 1) + "行" + excelColIndexToStr((c + 1)) + "列为空，请修正或删除改行");
//							return null;
//						}
//						int cellType = cell.getCellType();
//						String cellValue = null;
//						// 参数类型
//						String paramType = null;
//						switch (cellType) {
//						case Cell.CELL_TYPE_STRING: // 文本
//							cellValue = cell.getStringCellValue();
//							paramType = "java.lang.String";
//							break;
//						case Cell.CELL_TYPE_NUMERIC: // 数字、日期
//							if (DateUtil.isCellDateFormatted(cell)) {
//								try {
//									cellValue = fmtTime.format(cell.getDateCellValue()); // 日期型
//								} catch (Exception e) {
//									cellValue = fmtDate.format(cell.getDateCellValue()); // 日期型2
//								}
//								paramType = "java.util.Date";
//							} else {
//								cellValue = String.valueOf(cell.getNumericCellValue()); // 数字
//								paramType = "java.lang.Double";
//							}
//							break;
//						case Cell.CELL_TYPE_BOOLEAN: // 布尔型
//							cellValue = String.valueOf(cell.getBooleanCellValue());
//							paramType = "java.lang.Boolean";
//							break;
//						case Cell.CELL_TYPE_BLANK: // 空白
//							paramType = "";
//							request.setAttribute("msg", "第" + (r + 1) + "行" + excelColIndexToStr((c + 1)) + "列为空，请修正或删除该行");
//							return null;
//						// break;
//						case Cell.CELL_TYPE_ERROR: // 错误
//							paramType = "";
//							break;
//						case Cell.CELL_TYPE_FORMULA: // 公式
//							paramType = "";
//							break;
//						default:
//							paramType = "";
//						}
//						// 判断参数类型
//						if (r >=beginRow-1) {
//								Field field=fields[c+8];//除去serialVersionUID和主键
//								// 属性类型
//								String paramT = field.getType().getName();
//								if (paramT.equals("java.lang.Integer")) {
//									if (cellValue.substring(cellValue.indexOf(".")+1).replace("0", "").equals("")) {
//										cellValue=cellValue.substring(0,cellValue.indexOf("."));
//										paramType="java.lang.Integer";
//									}
//								}
//								if (!paramT.equals(paramType)) {
//									if (paramT.equals("java.lang.String")) {
//										paramT = "字符";
//									} else if (paramT.equals("java.lang.Double")) {
//										paramT = "整数或小数";
//									} else if (paramT.equals("java.util.Date")) {
//										paramT = "时间";
//									} else if (paramT.equals("java.lang.Boolean")) {
//										paramT = "布尔";
//									} else if (paramT.equals("java.lang.Integer")) {
//										paramT = "整数";
//									}
//									reportIndexError = new ReportIndexError();
//									reportIndexError.setReportIndexId(reportIndex.getReportIndexId());
//									reportIndexError.setReportIndexErrorValue(cellValue);
//									reportIndexError.setReportIndexErrorNotes(
//											"第" + (r + 1) + "行" + excelColIndexToStr((c + 1)) + "列,数据类型有误,应为:" + paramT);
//								} else {
//									String propertyName = field.getName();
//									String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
//											+ propertyName.substring(1);
//									Method setMethod= objClass.getMethod(setMethodName, field.getType());
//									String typeName = field.getType().getName();
//									if (typeName.equals("java.lang.String")) {
//										setMethod.invoke(object, cellValue);
//									} else if (typeName.equals("java.lang.Double")) {
//										Double doubleValue = Double.valueOf(cellValue);
//										setMethod.invoke(object, doubleValue);
//									} else if (typeName.equals("java.util.Date")) {
//										try {
//											setMethod.invoke(object, fmtTime.parse(cellValue));
//										} catch (Exception e) {
//											setMethod.invoke(object, fmtDate.parse(cellValue));
//										}
//									} else if (typeName.equals("java.lang.Boolean")) {
//										setMethod.invoke(object, Boolean.valueOf(cellValue));
//									} else if(typeName.equals("java.lang.Integer")){
//										setMethod.invoke(object,Integer.valueOf(cellValue));
//									}else{
//										 request.setAttribute("msg", "未知的错误类型！");
//										 return null;
//									}
//								}
//						}
//						if (reportIndexError != null) {
//							reportIndexErrorList.add(reportIndexError);
//						}
//					}
//					prevInvoke(fields[2], objClass,fmtTime.format(reportIndex.getReportIndexSubmitTime()), object);
//					prevInvoke(fields[3], objClass,fmtDate.format(reportIndex.getReportIndexTime()), object);
//					prevInvoke(fields[4], objClass,reportIndex.getReportIndexUserName(), object);
//					prevInvoke(fields[5], objClass,reportIndex.getReportIndexOrgId().toString(), object);
//					prevInvoke(fields[6], objClass,"0", object);
//					prevInvoke(fields[7], objClass,reportIndex.getReportIndexId().toString(), object);
//					
//					list.add((T) object);
//				}
//			reportIndex.setReportIndexNumbers(list.size());
//			if (CollectionUtils.isEmpty(reportIndexErrorList)==false) {
//				request.setAttribute("msg", "上报失败,请查看详情");
//			}else{
//				request.setAttribute("msg", "操作成功");
//			}
//		} catch (Exception e) {
//			LoggerUtil.error(e);
//			throw new Exception("报送异常！");
//		}
//		return list;
//	}
//	/**
//	 * 给前6个属性赋值 如状态、报送时间等
//	 * @param field
//	 * @param objClass
//	 * @param cellValue
//	 * @param object
//	 */
//	public void prevInvoke(Field field,Class objClass,String cellValue,Object object){
//		String propertyName = field.getName();
//		String setMethodName = "set" + propertyName.substring(0, 1).toUpperCase()
//				+ propertyName.substring(1);
//		SimpleDateFormat fmtTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		SimpleDateFormat fmtDate = new SimpleDateFormat("yyyy-MM-dd");
//		try {
//			Method setMethod= objClass.getMethod(setMethodName, field.getType());
//			String typeName = field.getType().getName();
//			if (typeName.equals("java.lang.String")) {
//				setMethod.invoke(object, cellValue);
//			} else if (typeName.equals("java.lang.Double")) {
//				Double doubleValue = Double.valueOf(cellValue);
//				setMethod.invoke(object, doubleValue);
//			} else if (typeName.equals("java.util.Date")) {
//				try {
//					setMethod.invoke(object, fmtTime.parse(cellValue));
//				} catch (Exception e) {
//					setMethod.invoke(object, fmtDate.parse(cellValue));
//				}
//			} else if (typeName.equals("java.lang.Boolean")) {
//				setMethod.invoke(object, Boolean.valueOf(cellValue));
//			} else if(typeName.equals("java.lang.Integer")){
//				setMethod.invoke(object,Integer.valueOf(cellValue));
//			}
//		}  catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	// 将数字转换成字母  
//    public String excelColIndexToStr(int columnIndex) {
//        if (columnIndex <= 0) {
//            return null;
//        }
//        String columnStr = "";
//        columnIndex--;
//        do {
//            if (columnStr.length() > 0) {
//                columnIndex--;
//            }
//            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
//            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
//        } while (columnIndex > 0);
//        return columnStr;
//    }
//    
//    public ExcelReaderThree(T t ) {
//    	this.t=t;
//    }
//    
//    public List<ReportIndexError> getReportIndexErrorList() {
//    	return reportIndexErrorList;
//    }
//    /**   
//     * 判断指定的单元格是否是合并单元格   
//     * @param sheet    
//     * @param row 行下标   
//     * @param column 列下标   
//     * @return   
//     */    
//    private boolean isMergedRegion(Sheet sheet,int row ,int column) {    
//        int sheetMergeCount = sheet.getNumMergedRegions();    
//        for (int i = 0; i < sheetMergeCount; i++) {    
//            CellRangeAddress range = sheet.getMergedRegion(i);    
//            int firstColumn = range.getFirstColumn();    
//            int lastColumn = range.getLastColumn();    
//            int firstRow = range.getFirstRow();    
//            int lastRow = range.getLastRow();    
//            if(row >= firstRow && row <= lastRow){    
//                if(column >= firstColumn && column <= lastColumn){    
//                    return true;    
//                }    
//            }    
//        }    
//        return false;    
//    } 
}
