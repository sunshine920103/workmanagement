<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
//		            layer.alert(msg,{
//						icon: (msg=="操作成功")?1:2,
//						time:15000,
//						shade:0.3,
//						shadeClose:true
//					});
                alertInfo(msg);
            }

            if ($("#key").val() != "") {
                $(".fuck").hide();
            }
            $("#key").focus(function () {
                $(".fuck").hide();
            }).blur(function () {
                if ($.trim($(this).val()) == "") {
                    $(".fuck").show();
                }
            });
            $(".fuck").click(function () {
                $("#key").focus();
            })
        });

        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        }

    </script>
    <title>关联信息查询</title>
</head>
<body class="eachInformationSearch">
<div id="enterpriseListDiv" class="listBox">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">查询企业结果列表</caption>
        <tbody>
        <tr class="firstTRFont">
            <td width="200">地区</td>
            <td width="200">统一社会信用代码</td>
            <td width="200">组织机构代码</td>
            <td width="200">企业名称</td>
            <td width="120">法定代表人</td>
            <td width="80">操作</td>
        </tr>
        <#if queryDefaultIndexItemCustomList??>
            <#list queryDefaultIndexItemCustomList as diic>
            <tr data-id="1">
                <td>${diic.isSon}</td>
                <td>${diic.codeCredit}</td>
                <td>${diic.codeOrg}</td>
                <td>${diic.qymc}</td>
                <td>${diic.fddbr}</td>
                <td>
                    <a onclick="setLayer(' ','${request.getContextPath()}/admin/relatedInfo/chooseType.jhtml?defaultIndexItemId=${diic.defaultIndexItemId}')"
                       class="changeFont fontSize12 cursorPointer hasUnderline">查 询</a></td>
            </tr>
            </#list>
        </#if>
        </tbody>
    </table>
</div>
</body>

</html>
