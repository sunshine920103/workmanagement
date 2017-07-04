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
    <title>报文报送列表</title>
</head>
<body>
<form id="searchForm" method="post" action="${request.getContextPath()}/admin/messageSubmission/sysQuery.jhtml">
</form>
<div class="rightPart" id="sureWidth">
    <div class="eachInformationSearch">
        <!-- 默认获取的一个小指标 列表       -->
        <div id="min" class="listBox">
            <table cellpadding="0" cellspacing="0">
                <caption class="titleFont1 titleFont1Ex">报送记录列表</caption>
                <tr class="firstTRFont">
                    <td style="width:5%;">序号</td>
                    <td style="width:30%;">送报机构</td>
                    <td style="width:20%">指标大类</td>
                    <td style="width:15%;">归档日期</td>
                    <td style="width:15%;">状态</td>
                    <td style="width:10%;">操作</td>
                </tr>
            <#list reportIndexList as item>
                <tr>
                    <td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
                    <td>${item.REPORT_INDEX_ORG_NAME}</td>
                    <td>${item.REPORT_INDEX_TEMPLATE}</td>
                    <td>${(item.REPORT_INDEX_TIME?string("yyyy-MM-dd"))!}</td>
                    <td>
                        <#if item.REPORT_INDEX_STATUS = 0>
                            <span class="changeFont fontSize12">上报成功</span>
                        <#else>
                            <span class="delFont fontSize12">上报失败</span>
                        </#if>
                    </td>
                    <td>
                        <a class="changeFont fontSize12 hasUnderline cursorPointer"
                           onclick="setLayer('查看','${request.getContextPath()}/admin/messageSubmission/show.jhtml?reportIndexId=${item.REPORT_INDEX_ID}');$('.layui-layer-shade').height($(window).height());"
                        >查 看</a>
                    </td>
                </tr>
            </#list>
            </table>
        <#if (reportIndexList?? && reportIndexList?size > 0)>
            <#include "/fragment/paginationbar.ftl"/>
        </#if>
        </div>
    </div>
</div>
</body>
</html>
