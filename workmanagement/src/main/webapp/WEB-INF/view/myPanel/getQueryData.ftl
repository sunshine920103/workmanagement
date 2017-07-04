<!DOCTYPE html>
<html>
<head>
    <title>更多信息</title>
<#include "/fragment/common.ftl"/>
</head>
<body>
<form id="searchForm" method="post"></form>
<!--更多系统消息开始-->
<div class="showListBox">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">系统消息</caption>
        <tr class="firstTRFont">
            <td width="20">序号</td>
            <td width="150">机构</td>
            <td width="50">登录名</td>
            <td width="400">搜索条件</td>
            <td width="150">菜单</td>
            <td width="120">操作时间</td>
        </tr>
    <#list logList as sys>
        <tr>
            <td>${(1 + sys_index) + (page.getPageSize() * page.getCurrentPage())}</td>
            <td>${sys.sysUserLogOrgName}</td>
            <td>${sys.sysUserLogUserName}</td>
            <td>${sys.sysUserLogQueryUserCondition}</td>
            <td>${sys.sysUserLogMenuName}</td>
            <td>${(sys.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
        </tr>
    </#list>
        </tbody>
    </table>

    <!--分页-->
<#include "/fragment/paginationbar.ftl"/>
    <div class="showBtnBox">
        <input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
    </div>
</div>
<!--更多系统消息结束-->
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
