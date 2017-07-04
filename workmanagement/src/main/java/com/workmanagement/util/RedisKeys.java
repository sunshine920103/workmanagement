package com.workmanagement.util;

/**
 * 用于管理redis保存缓存时的key
 * @author justi
 *
 */
public class RedisKeys {
	
	/**
	 * ============================================================================
	 * =================================信息查询===================================
	 * ============================================================================
	 */
	
	/**
	 * 信用评分查询模版
	 */
	public static final String QUERY_COPE_TEMPLATE = "QUERY_COPE_TEMPLATE";
	
	/**
	 * 统计报表查询模版 (用户id)
	 */
	public static final String QUERY_TOTAL_TEMPLATE = "QUERY_TOTAL_TEMPLATE";
	
	
	
	/**
	 * ============================================================================
	 * =================================数据报送===================================
	 * ============================================================================
	 */
	
	/**
	 * excel报送_excel模板(用户id)
	 */
	public static final String REPORT_INDEX_TEMPLATE ="REPORT_INDEX_TEMPLATE";
	
	
	/**
	 * excel模板(用户id)
	 */
	public static final String REPORT_INDEX_TEMPLATE_LIST ="REPORT_INDEX_TEMPLATE_LIST";
	/**
	 * excel模板记录总数(用户id)
	 */
	public static final String REPORT_INDEX_TEMPLATE_LIST_TOTAL ="REPORT_INDEX_TEMPLATE_LIST_TOTAL";
	
	
	
	
	
	/**
	 * ============================================================================
	 * =================================基础维护===================================
	 * ============================================================================
	 */
	
	/**
	 * 指标大类集合
	 */
	public static final String INDEX_LIST = "INDEX_LIST";
	/**
	 * 指标大类集合覆盖范围
	 */
	public static final String INDEX_LIST_AREA = "INDEX_LIST_AREA";
	
	/**
	 * 信用产品底纹
	 */
	public static final String CREDIT_PRODUCT_BG = "CREDIT_PRODUCT_BG";
	/**
	 * 信用产品底纹总记录数
	 */
	public static final String CREDIT_PRODUCT_BG_TOTAL = "CREDIT_PRODUCT_BG_TOTAL";
	
	/**
	 * 信用产品模板(机构id)
	 */
	public static final String QUERY_PRODUCT_TEMPLATE="QUERY_PRODUCT_TEMPLATE";
	/**
	 * 信用产品模板总记录数
	 */
	public static final String QUERY_PRODUCT_TEMPLATE_TOTAL="QUERY_PRODUCT_TEMPLATE_TOTAL";
	
	
	
	
	
	/**
	 * ============================================================================
	 * =================================系统管理===================================
	 * ============================================================================
	 */
	
	/**
	 * 地区管理(地区ID)
	 * 
	 * 缓存地区对象
	 */
	public static final String SYS_AREA = "SYS_AREA";
	
	
	/**
	 * 地区的下级地区的ids
	 */
	public static final String SYS_AREA_DOWN_IDS = "SYS_AREA_DOWN_IDS";
	
	/**
	 * 只装一级下级地区
	 */
	public static final String SYS_AREA_DOWN = "SYS_AREA_DOWN";
	
	/**
	 * 全国
	 */
	public static final String SYS_AREA_ALL = "SYS_AREA_ALL";
	
	/**
	 * 全国
	 */
	public static final String SYS_AREA_ALL_WITHSUB = "SYS_AREA_ALL_WITHSUB";
	
	/**
	 *含上级地区的 
	 */
	public static final String SYS_AREA_UP = "SYS_AREA_UP";
	
	/**
	 * 地区管理(地区ID)
	 * 
	 * 将该地区及子地区的ID缓存进一个StringBuffer对象中
	 */
	public static final String SYS_AREA_SB_CHILD = "SYS_AREA_SB_CHILD";
	/**
	 * 地区管理(地区ID)
	 * 
	 * 将该地区及父地区和子地区的ID缓存进一个StringBuffer对象中
	 */
	public static final String SYS_AREA_SB_PARENT = "SYS_AREA_SB_PARENT";
	
	
	
	/**
	 * 机构管理(机构ID)
	 * 
	 * 缓存机构对象
	 */
	public static final String SYS_ORG = "SYS_ORG";
	/**
	 * 机构管理（用户ID）
	 * 
	 * 根据用户所在机构的     职责区域(如果有)  和    上下级      查询的列表
	 */
	public static final String SYS_ORG_LIST_USER = "SYS_ORG_LIST_USER";
	/**
	 * 机构管理(机构ID)
	 * 
	 * 将该机构及子机构的ID缓存进一个StringBuffer对象中
	 */
	public static final String SYS_ORG_SB = "SYS_ORG_SB";
	
	
	
	/**
	 * 机构类别(机构类别ID)
	 */
	public static final String SYS_ORG_TYPE = "SYS_ORG_TYPE";
	/**
	 * 机构类别列表
	 */
	public static final String SYS_ORG_TYPE_LIST = "SYS_ORG_TYPE_LIST";
	

	
	
	/**
	 * 角色管理（用户ID）
	 */
	public static final String SYS_ROLE = "SYS_ROLE";

	
	
	
	/**
	 * 用户管理
	 */
	public static final String SYS_USER = "SYS_USER";
	/**
	 * 用户管理机构
	 */
	public static final String SYS_USER_ORG = "SYS_USER_ORG";



	/**
	 * ============================================================================
	 * =================================系统管理===================================
	 * ============================================================================
	 */

	/**
	 * 用户行为审计
	 */
	public static final String SYS_USER_LOG = "SYS_USER_LOG";
	

	/**
	 * 其他管理
	 */
	public static final String SYS_SET = "SYS_SET";
	
}
