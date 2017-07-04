<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <meta charset="UTF-8">
    <script type="text/javascript">
        function subm() {
            var search = $("#dicName").val();
            if (checkTChineseM(search) == 0) {
                layer.alert("请输入正确的查询条件", {icon: 2, shade: 0.3, shouldClose: true});
                return false;
            }
            $("#selectDic").submit();

        }

        function down() {
            var len = $("#dataList tr").length;
            if (len > 1) {
                window.location.href = "${request.getContextPath()}/admin/dic/outSearch.jhtml";
            } else {
                layer.alert("没有搜索结果");
            }
        }
    </script>
    <title></title>
</head>
<body>
<div class="eachInformationSearch">
    <form id="searchForm" method="post">
        <input name="dicName" class="inputSty inputOtherCondition" type="hidden" value="${dicName}"/>
    </form>
    <div class="queryInputBox">

        <form id="selectDic" method="post" action="${request.getContextPath()}/admin/dic/search.jhtml" class="marginL20"
              style="display: inline-block;*zoom=1;*display:inline;position: relative;">
            <span class="fuck">请输入字典名称</span>
            <input id="dicName" name="dicName" class="inputSty inputOtherCondition" type="text" value="${dicName}"/>
            <input onclick="subm()" type="button" class="sureBtn" style="margin-left: 0px;" value="查 询"/>
        </form>
        <input onclick="down()" type="button" value="导出" class="sureBtn sureBtnEx" style="margin-left: 25px;"/>
        <div class="listBox" style="margin-left: 20px;">
            <table id="dataList" cellpadding="0" cellspacing="0">
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
                    <td>${item.dicName}</td>
                    <td>${item.dicNotes}</td>
                    <td>
                        <a class="changeFont fontSize12 cursorPointer hasUnderline"
                           onclick="setLayer('查看字典','${request.contextPath}/admin/dic/edit.jhtml?show=show&id=${item.dicId}');">查
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