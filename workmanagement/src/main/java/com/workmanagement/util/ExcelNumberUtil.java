package com.workmanagement.util;

public class ExcelNumberUtil {

	public static String  toLetter(int i){
		return excelColIndexToStr(i);		
	}
	
	 public static int excelColStrToNum(String colStr, int length) {
	        int num = 0;
	        int result = 0;
	        for(int i = 0; i < length; i++) {
	            char ch = colStr.charAt(length - i - 1);
	            num = (int)(ch - 'A' + 1) ;
	            num *= Math.pow(26, i);
	            result += num;
	        }
	        return result;
	    }
	 public static String excelColIndexToStr(int columnIndex) {
	        if (columnIndex <= 0) {
	            return null;
	        }
	        String columnStr = "";
	        columnIndex--;
	        do {
	            if (columnStr.length() > 0) {
	                columnIndex--;
	            }
	            columnStr = ((char) (columnIndex % 26 + (int) 'A')) + columnStr;
	            columnIndex = (int) ((columnIndex - columnIndex % 26) / 26);
	        } while (columnIndex > 0);
	        return columnStr;
	    }
}
