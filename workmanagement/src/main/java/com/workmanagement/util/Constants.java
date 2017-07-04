/**
 * 
 */
package com.workmanagement.util;

/**
 * @author Administrator
 *
 */
public class Constants {
	/**
	 * 验证码类型：注册
	 */
	public static final Integer VALIDATE_CODE_TYPE_REG = 0;
	/**
	 * 验证码类型：忘记密码
	 */
	public static final Integer VALIDATE_CODE_TYPE_FORGOT = 1;
	
	/**
	 * 验证码类型：商户注册
	 */
	public static final Integer VALIDATE_CODE_TYPE_MERCHANT_REG = 2;
	/**
	 * 验证码类型：商户忘记密码
	 */
	public static final Integer VALIDATE_CODE_TYPE_MERCHANT_FORGOT = 3;
	
	/**
	 * 用户在session中的key名称
	 */
	public static final String SESSION_KEY_MEMBER = "sessionMember";
	/**
	 * 用户在session中的商户key名称
	 */
	public static final String SESSION_KEY_MERCHANT = "sessionMerchant";
}
