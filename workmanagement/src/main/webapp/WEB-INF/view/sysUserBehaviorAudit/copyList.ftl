<!DOCTYPE HTML>
<html>

<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        };
    </script>
</head>
<body>
<form id="searchForm" method="post" action="${request.getContextPath()}/admin/sysUserBehaviorAudit/sysQuery.jhtml">
</form>
<div class="rightPart floatLeft  eachInformationSearch">
    <div class="listBox">
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">用户行为审计列表</caption>

            <tr class="firstTRFont">
                <td width="50">序号</td>
                <td width="240">机构</td>
                <td width="90">登录名</td>
                <td width="90">操作对象</td>
                <td width="90">ip地址</td>
                <td width="80">原值</td>
                <td width="80">现值</td>
                <td width="180">菜单</td>
                <td width="50">操作类型</td>
                <td width="120">操作时间</td>
                <td width="120">操作内容</td>
            </tr>
        <#list sysUserLogs as sys>
            <tr>
                <td>${(1 + sys_index) + (page.getPageSize() * page.getCurrentPage())}</td>
                <td>${sys.SYS_USER_LOG_ORG_NAME}</td>
                <td>${sys.SYS_USER_LOG_USER_NAME}</td>
                <td>${sys.SYS_USER_LOG_ENTERPRISE_CODE}</td>
                <td>${sys.SYS_USER_LOG_IP}</td>
                <td>${sys.SYS_USER_LOG_OLD_VALUE}</td>
                <td>${sys.SYS_USER_LOG_NEW_VALUE}</td>
                <td>${sys.SYS_USER_LOG_MENU_NAME}</td>
                <td class="secondTD">
                    <#if sys.SYS_USER_LOG_OPERATE_TYPE == 1>
                        增加
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 2>
                        删除
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 3>
                        修改
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 4>
                        查询
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 5>
                        导入
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 6>
                        导出
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 7>
                        打印
                    <#elseif sys.SYS_USER_LOG_OPERATE_TYPE == 8>
                        登陆
                    </#if>
                </td>
                <td>${(sys.SYS_USER_LOG_TIME?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                <td>
                    <a class="changeFont fontSize12 hasUnderline cursorPointer"
                       onclick="setLayer('查看用户行为审计','${request.getContextPath()}/admin/sysUserBehaviorAudit/show.jhtml?id=${sys.SYS_USER_LOG_ID}')">查看详情</a>
                </td>
            </tr>
        </#list>
        </table>
    <#if (sysUserLogs?? && sysUserLogs?size > 0)>
        <#include "/fragment/paginationbar.ftl"/>
    </#if>
    </div>
</div>
</body>

</html>
