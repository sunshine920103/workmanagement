<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        $(function () {
            $("#downLoadExcel").click(function () {
                if ("${defaultIndexItemCustomList[0]}" != "") {
                    window.location.href = '${request.getContextPath()}/admin/relatedInfo/excelOut.jhtml';
                } else {
                    layer.alert("查询结果为空，无法导出excel", {
                        icon: 2,
                        time: 15000,
                        shade: 0.3,
                        shadeClose: true
                    });
                }
            });
            $("#showImg").click(function () {
                if ("${defaultIndexItemCustomList[0]}" != "") {
                <#--setLayer("查看图谱","${request.getContextPath()}/admin/relatedInfo/relatedImg.jhtml");-->
                    window.location.href = '${request.getContextPath()}/admin/relatedInfo/relatedImg.jhtml';
                <#--layer.open({-->
                <#--type: 2,-->
                <#--title: "查看图谱",-->
                <#--shade: 0.6,-->
                <#--shadeClose:false,-->
                <#--area: ['80%', '450px'],-->
                <#--content: '${request.getContextPath()}/admin/relatedInfo/relatedImg.jhtml'-->
                <#--});-->
                } else {
//					alertInfo("查询结果为空，无法查看图谱");
                    layer.alert("查询结果为空，无法查看图谱", {
                        icon: 2,
//						time:15000,
                        shade: 0.3,
                        shadeClose: true
                    });
                }
            })
        });
    </script>
    <title>结果</title>
</head>
<body>

<div class="showListBox">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">关联信息查询结果</caption>
        <tbody>
        <tr>
            <td width="250" class="noBorderL firstTD">统一社会信用代码</td>
            <td width="400" class="secondTD">${defaultIndexItemCustom.codeCredit}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">组织机构代码</td>
            <td class="secondTD">${defaultIndexItemCustom.codeOrg}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">企业名称</td>
            <td class="secondTD">${defaultIndexItemCustom.qymc}</td>
        </tr>
        <tr>
            <td class="cnoBorderL firstTD">法定代表人</td>
            <td class="secondTD">${defaultIndexItemCustom.fddbr}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">截止报送时间</td>
            <td class="secondTD">${indexDwdbxxRecordTime}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">关联类型</td>
            <td class="secondTD">${screen}</td>
        </tr>
        </tbody>
    </table>
    <div class="showBtnBox">
        <input class="cancleBtn closeThisLayer" type="button" value="关 闭"/>
        <input class="sureBtn marginR40" data-id="1" type="button" id="showImg" value="查看图谱"/>
        <input class="sureBtn" id="downLoadExcel" data-id="1" type="button" value="导出excel"/>
    </div>
</div>
<div class="showListBox excelContent">

    <table cellpadding="0" cellspacing="0">
        <tbody>
        <tr class="firstTRFont">
            <td width="60">企业地区</td>
            <td width="150" class="noBorderL">统一社会信用代码</td>
            <td width="100">组织机构代码</td>
            <td width="100">企业名称</td>
            <td width="80">关联关系</td>
            <td width="100">操作</td>
        </tr>
        <#if defaultIndexItemCustomList?size gt 0 >
            <#list defaultIndexItemCustomList as item>
            <tr>
                <td>${item.isSon}</td>
                <td class="noBorderL">${item.codeCredit}</td>
                <td>${item.codeOrg}</td>
                <td>${item.qymc}</td>
                <td>${item.relatedType}</td>
                <td>
                    <a onclick="setLayer('信用报告','${request.getContextPath()}/admin/creditReportQuery/list.jhtml?cre=${item.codeCredit}&org=${item.codeOrg}&qymc=${item.qymc}&code=${item.qyzs}')"
                       class="changeFont fontSize12 cursorPointer hasUnderline">信用报告</a>
                    <a onclick="setLayer('信用评分','${request.getContextPath()}/admin/creditScoreQuery/list.jhtml?cre=${item.codeCredit}&org=${item.codeOrg}&code=${item.qyzs}')"
                       class="changeFont fontSize12 cursorPointer hasUnderline">信用评分</a>
                </td>
            </tr>
            </#list>
        <#else>
        <tr>
            <td colspan="6" style="text-align: center;">无</td>
        </tr>
        </#if>
        </tbody>
    </table>

    <!--<table cellspacing="0" cellpadding="0" class="marginT10 noBorderT">
        <tr><td class="firstTRFont textCenter">暂无数据</td></tr>
    </table>-->

    <div class="showBtnBox">
        <input class="cancleBtn closeExcelContent" type="button" value="关 闭"/>
    </div>
</div>
</body>
<script>
    $(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('.closeThisLayer').on('click', function () {
            if ("${returnNum}" == "returnNum") {
                history.go(-2);
            } else {
                parent.layer.close(index); //执行关闭
            }
        });

        //点击关闭  excel消失
        $(".closeExcelContent").click(function () {
            $(".excelContent").hide();
        })
    })
</script>
</html>
