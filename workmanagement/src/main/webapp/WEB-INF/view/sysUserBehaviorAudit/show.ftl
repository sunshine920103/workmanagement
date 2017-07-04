<!DOCTYPE html>
<html>
<head>

<#include "/fragment/common.ftl"/>
    <title>查看用户行为审计</title>
</head>
<body>
<div class="showListBox">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">用户行为审计详情</caption>
        <tbody>
        <#list sysUserLog as sys>
            <#if sys_index == 0>
            <tr>
                <td style="width:40%;" class="noBorderL firstTD">机构名称</td>
                <td style="width:60%;" class="secondTD">${sys.sysUserLogOrgName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">操作对象</td>
                <td class="secondTD">${sys.sysUserLogEnterpriseCode}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">ip地址</td>
                <td class="secondTD">${sys.sysUserLogIp}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">登录名</td>
                <td class="secondTD">${sys.sysUserLogUserName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">操作类型</td>
                <td class="secondTD">
                    <#if sys.sysUserLogOperateType == 1>
                        增加
                    <#elseif sys.sysUserLogOperateType == 2>
                        删除
                    <#elseif sys.sysUserLogOperateType == 3>
                        修改
                    <#elseif sys.sysUserLogOperateType == 4>
                        查询
                    <#elseif sys.sysUserLogOperateType == 5>
                        导入
                    <#elseif sys.sysUserLogOperateType == 6>
                        导出
                    <#elseif sys.sysUserLogOperateType == 7>
                        打印
                    <#elseif sys.sysUserLogOperateType == 8>
                        登陆
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">用户角色</td>
                <td class="secondTD">${sys.sysUserLogRoleName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">操作菜单</td>
                <td class="secondTD">${sys.sysUserLogMenuName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">指标大类</td>
                <td class="secondTD">${sys.indexName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">指标项</td>
                <td class="secondTD">${sys.indexItemName}</td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">原值</td>
                <td class="secondTD">${sys.sysUserLogOldValue}</td>
            </tr>
            <tr>
                <td class="cnoBorderL firstTD">修改值</td>
                <td class="secondTD">${sys.sysUserLogNewValue}</td>
            </tr>
            <tr>
                <td class="cnoBorderL firstTD">数据条数</td>
                <td class="secondTD">${sys.sysUserLogCount}</td>
            </tr>
            <tr>
                <td class="cnoBorderL firstTD">搜索条件</td>
                <td class="secondTD">${sys.sysUserLogQueryUserCondition}</td>
            </tr>
            <tr>
							<td class="noBorderL firstTD">操作IP</td>
							<td class="secondTD">
								${sys.sysUserLogIp}
							</td>
			</tr>
            <tr>
                <td class="noBorderL firstTD">操作结果</td>
                <td class="secondTD">
                    <#if sys.sysUserLogResult == true>
                        成功
                    <#else>
                        失败
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="cnoBorderL firstTD">搜索结果</td>
                <td class="secondTD">
                    <#if sys.sysUserLogUrl??>
                        <a class="changeFont fontSize12"
                           href="${request.getContextPath()}/admin/sysUserBehaviorAudit/forward.jhtml?sysId=${sys.sysUserLogId}">点击查看</a>
                    <#else >
                        <span class="changeFont fontSize12">暂无</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="cnoBorderL firstTD">导入导出文件</td>
                <td class="secondTD">
                    <#if sys.sysUserLogFile??>
                        <a class="changeFont fontSize12"
                           href="${request.getContextPath()}/admin/sysUserBehaviorAudit/downFile.jhtml?type=1&sysId=${sys.sysUserLogId}">点击查看</a>
                    <#else >
                        <span class="changeFont fontSize12">暂无</span>
                    </#if>
                </td>
            </tr>
            <tr>
                <td class="noBorderL firstTD">授权文件</td>
                <td class="secondTD">
                    <#if sys.sysUserLogAuthFile??>
                        <a class="changeFont fontSize12"
                           href="${request.getContextPath()}/admin/sysUserBehaviorAudit/downFile.jhtml?type=2&sysId=${sys.sysUserLogId}">点击查看</a>
                    <#else >
                        <span class="changeFont fontSize12">暂无</span>
                    </#if>
                </td>
            </tr>
            <#--<tr>-->
            <#--<td class="noBorderL firstTD">IP地址</td>-->
            <#--<td class="secondTD">${SysUserLog.SYS_USER_LOG_IP}</td>-->
            <#--</tr>-->
            <tr>
                <td class="noBorderL firstTD">操作时间</td>
                <td class="secondTD">${(sys.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            </tr>
            </#if>
        </#list>
        </tbody>
    </table>
    <div class="showBtnBox">
        <input class="sureBtn closeThisLayer" type="button" value="关闭"/>
    </div>
</div>
</body>
<script>
    $(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('.closeThisLayer').on('click', function () {
            parent.layer.close(index); //执行关闭
        });
    })
</script>
</html>
