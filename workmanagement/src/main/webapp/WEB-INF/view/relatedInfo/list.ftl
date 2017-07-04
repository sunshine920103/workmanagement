<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
                layer.alert(msg, {
                    icon: (msg == "操作成功") ? 1 : 2,
//						time:15000,
                    shade: 0.3,
                    shadeClose: true
                });
//					alertInfo(msg);
            }
            $("#queryEnterpriseForm").submit(function () {
                var enterpriseCode = $("[name=enterpriseCode]").val();
                if (enterpriseCode == "统一社会信用代码 | 组织机构代码" || enterpriseCode == "") {
                    layer.alert("请输入：统一社会信用代码或组织机构代码", {
                        icon: 2,
                        time: 15000,
                        shade: 0.3,
                        shadeClose: true
                    });
//						alertInfo("请填写所有查询条件");
                    return false;
                }
                if (enterpriseCode.length != 18 && enterpriseCode.length != 10) {
//				    	alertInfo("请输入准确的统一社会信用码或组织机构代码");
                    layer.alert("请输入准确的统一社会信用代码或组织机构代码", {
                        icon: 2,
                        time: 15000,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                }
            });

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

        //提交前验证
        function checkfy(form) {
            if (form.enterpriseCode.value.length != 18 && form.enterpriseCode.value.length != 10) {
                layer.alert("请输入准确的统一社会信用代码或组织机构代码", {
                    icon: 2,
                    time: 15000,
                    shade: 0.3,
                    shadeClose: true
                });
//	    	layer.confirm("请输入准确的统一社会信用码或组织机构代码", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
                return false;
            }

            if (checkTYNoLT18(form.enterpriseCode.value) == 0) {
                layer.alert("查询内容输入不合法", {
                    icon: 2,
                    time: 15000,
                    shade: 0.3,
                    shadeClose: true
                });
//			layer.confirm("查询内容输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
                return false;
            }
            return true;
        }

    </script>
    <title>关联信息查询</title>
</head>
<body class="eachInformationSearch">
<div class="queryInputBox marginLR30" style="margin-bottom: 0px;">
    <form id="queryEnterpriseForm" action="${request.getContextPath()}/admin/relatedInfo/query.jhtml" method="post"
          style="position: relative;">
        <span class="fuck">统一社会信用代码/组织机构代码</span>
        <input name="enterpriseCode" class="inputSty" type="text" id="key"/>
        <input class="sureBtn" type="submit" value="查询企业" onclick="return checkfy(this.form);"
               style="margin-left: 0px;"/>
    </form>
</div>
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
