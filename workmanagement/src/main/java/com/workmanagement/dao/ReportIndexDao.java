package com.workmanagement.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.workmanagement.model.ReportIndex;

public interface ReportIndexDao {

    //以下为封装map使用的全部key，value全部用object
    public static final String TABLE_NAME = "tableName";//数据库的表名，（指标大类的名字）
    public static final String TITLE = "title";//列名字，（
    public static final String TITLES = "titles";//多个列名字，
    public static final String VALUE = "value";//单个值，
    public static final String VALUES = "values";//多个值，
    //报送类型
    public static final int TXT_SUBMIT = 0;//报文报送
    public static final int EXCEL_SUBMIT = 1;//excel报送
    public static final int HAND_SUBMIT = 2;//手工报送
    public static final int MONTH_SUBMIT = 3;//月季报送
    //报送状态
    public static final int STSTUS_SUCCES = 0;//有效
    public static final int STSTUS_ERROR = 1;//上报失败


    /**
     * 根据主键查询一条数据
     *
     * @param id
     * @return
     */
    ReportIndex getReportIndexById(Integer id) throws Exception;

    /**
     * 查询：同一个模板，同一个机构，同一天是否上报有成功记录
     */
    List<ReportIndex> queryReporByTempOrgDate(@Param("time")String time,
    		@Param("temptId")Integer temptId,@Param("orgId")Integer orgId);
    
    /**
     * 根据主键删除一条数据
     *
     * @param id
     */
    Integer deleteById(@Param("reportIndexId") Integer id) throws Exception;

    /**
     * 报送数据
     */
    void reportInfo(@Param("sqlMap") Map<String, String> sqlMap);

    /**
     * 查询数据条数
     */
    Integer countBySql(@Param("sql") String sql);
    

    /**
     * 查询数据条数
     */
    Map<String, Object> getMap(@Param("sql") String sql) throws Exception;

    /**
     * 更新数据
     */
    Integer updateBySql(@Param("sql") String sql) throws Exception;
    
    /**
     * 执行ddl
     */
    void insertIndex(@Param("sql") String sql) throws Exception;

    /**
     * 增加一条数据
     *
     * @param reportIndex
     */
    Integer insertOne(ReportIndex reportIndex) throws Exception;

    /**
     * 更新一条数据
     *
     * @param reportIndex
     */
    Integer updateById(ReportIndex reportIndex) throws Exception;

    /**
     * 往指定的指标大类表里添加数据
     *
     * @param map
     * @return
     * @throws Exception
     */
    Integer addSomeDataToIndexTb(Map<String, Object> map) throws Exception;

    /**
     * 根据指定的报送方法查找列表
     *
     * @param reportIndexMethod
     * @return
     * @throws Exception
     */
    List<ReportIndex> getReportsByMethod(Integer reportIndexMethod) throws Exception;

    /**
     * 根据表名和字段查看这个值是否唯一
     *
     * @param map
     * @return
     * @throws Exception
     */
    Integer selectThisCellDataIsHaved(Map<String, String> map) throws Exception;

    /**
     * 查询所有数据
     *
     * @return
     */
    List<ReportIndex> queryReportIndexs();

    /**
     * 添加数据到报送记录表
     *
     * @param reportIndex
     */
    void insert(ReportIndex reportIndex);

    /**
     * 跟新数据到报送记录表
     *
     * @param reportIndex
     */
    void update(ReportIndex reportIndex);

    /**
     * 通过id查询
     *
     * @return
     */
    ReportIndex queryReportIndexsById(Integer id);

    List<ReportIndex> queryReportIndexsByName(@Param("indexName") String indexName);

    /**
     * 根据类别以及机构ids查看信息
     *
     * @param map
     * @return
     * @throws Exception
     */
    List<ReportIndex> getReportsByMethodAndOrgIds(Map<String, Object> map) throws Exception;

    /**
     * 根据条件查询数据
     * @param map
     * @return
     */
    List<ReportIndex> getDataBySome(Map<String, Object> map);
    
    /**
     * 向临时表插入数据
     */
    void insertToTemp(@Param("tbName")String tbName,@Param("columnList") List<String> columnList,
    		@Param("dataList")List<Object> dataList);
    /**
     * 按照excel中的行号选取对应的临时表中的每条数据
     */
    Map<String,Object> selectOneFormTempTb(@Param("tbName")String tbName,@Param("num")Integer num);
    
    /**
     * 将临时表中的全部数据取出
     */
    List<Map<String,Object>> selectAllFormTempTb(@Param("tbName")String tbName);
    
    /**
     * 更新临时表的二码id
     */
    
    void updateTempDafaultId(@Param("tbName")String tbName,@Param("num")Integer num
    		,@Param("defaultIndexItemId")Integer defaultIndexItemId);
    
    /**
     * 删除动态表
     */
    void deleteTempTb(@Param("tempTbName")String tempTbName);
    
    /**
     * 查询动态表数据状态
     */
    Integer queryStatusNum(@Param("tempTbName")String tempTbName,@Param("status")Integer status);
    
	/**
	 * 获取excel中的数据条数
	 */
	Integer getDataNum(@Param("tempTbName")String  tempTbName);
    
    /**
	 * 创建临时表前先判断是否存在
	 */
	Integer isTempTableExists(@Param("tempTbName")String tempTbName);
    
    /**
     * 将临时表中的对用数据的是否为新增数据的标识改成1，默认0新增
     */
    void updateIsInsert(@Param("tempTbName")String tempTbName,@Param("num")Integer num);
    
    /**
     * 标识将临时表中对应的excel中的该条数据为错误数据
     */
    void updateStatus(@Param("tempTbName")String tempTbName,@Param("num")Integer num);
    
    /**
     * 唯一标识校验
     */
    void uniqueCheck(@Param("tempTbName")String tempTbName,@Param("uniqueCode")String uniqueCode);
    
    /**
     * 查询地区的名称对代号
     */
   List<Map<String,String>> queryAllAreaValueToCode();
   
  /**
   * 查询行业的名称对代号
   */
 List<Map<String,String>> queryAllIndusValueToCode();
 
/**
 * 查询机构的名称对代号
 */
List<Map<String,String>> queryAllOrgValueToCode();

/**
 * 查询机构的名称对代号
 */
List<Map<String,Object>> queryAllDicValueToCode();

/**
 * 用sql查重
 */
List<Map<String,Object>> queryRepeatOrg(@Param("tempTbName")String tempTbName);

List<Map<String,Object>> queryRepeatCredit(@Param("tempTbName")String tempTbName);

List<Map<String,Object>> queryRepeatUnique(@Param("tempTbName")String tempTbName,@Param("unique")String unique);
void insertTou(@Param("tempu")String tempu,@Param("tempTbName")String tempTbName);

}
