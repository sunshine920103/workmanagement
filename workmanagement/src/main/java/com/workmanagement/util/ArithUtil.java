/**
 * 
 */
package com.workmanagement.util;

import java.math.BigDecimal;

/**
 * @author Administrator
 *
 */
public class ArithUtil {
	/**
	 * 提供精确的乘法运算。
	 * 
	 * @param v1 被乘数
	 * @param v2 乘数
	 * @return 两个参数的积
	 */
	public static int mul(String v1, String v2) {
		BigDecimal b1 = new BigDecimal(v1);
		BigDecimal b2 = new BigDecimal(v2);
		return b1.multiply(b2).intValue();
	}

}
