<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/> 
</head>
<body>
<div class="eachInformationSearch">
    <form id="searchForm" method="post"></form>
    <div class="queryInputBox">
        <div class="listBox" style="margin-left: 20px;">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td width="100">序号</td>
                    <td width="200">字典名称</td>
                    <td width="200">备注</td>
                    <td width="80">操作</td>
                </tr>
            <#list dicList as item>
                <tr>
                <#--<td>${(1 + ps_index) + page.getPageSize() * (page.getCurrentPage() - 1)}</td>-->
                    <td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
                    <td>${item.DIC_NAME}</td>
                    <td>${item.DIC_NOTES}</td>
                    <td>
                        <a class="changeFont fontSize12 cursorPointer hasUnderline"
                           onclick="setLayer('查看字典','${request.contextPath}/admin/dic/edit.jhtml?show=show&id=${item.DIC_ID}');">查
                            看</a>
                    </td>
                </tr>
            </#list>
            </table>
        <#if dicList??>
            <#if (dicList?size > 0)>
                <#include "/fragment/paginationbar.ftl"/>
            <#else>
                <table style="border-top: 0px; " cellpadding="0" cellspacing="0">
                    <tr class="firstTRFont">
                        <td style="text-align: center;font-weight: bold;">暂无信息</td>
                    </tr>
                </table>
            </#if>
        </#if>
        </div>
    </div>
</div>
</body>
</html>
<script type="text/javascript">
    $(function () {
        if ($(".inputOtherCondition").val() != "") {
            $(".fuck").hide();
        }
        $(".inputOtherCondition").focus(function () {
            $(".fuck").hide();
        }).blur(function () {
            if ($.trim($(this).val()) == "") {
                $(".fuck").show();
            }
        });
        $(".fuck").click(function () {
            $(".inputOtherCondition").focus();
        })
    });
</script>