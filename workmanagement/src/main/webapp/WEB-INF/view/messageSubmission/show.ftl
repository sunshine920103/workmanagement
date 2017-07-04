<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        $(function () {
            var msg = "${msg}";
            if (msg != "") {
                alertInfo(msg);
                $('.layui-layer-shade').height($(window).height());
            }
        });
    </script>
    <title>excel错误或成功列表</title>
</head>
<body>
<div class="showListBox">
    <table cellpadding="0" cellspacing="0">
        <tr>
            <td width="200" class="noBorderL firstTD">指标大类</td>
            <td width="500" class="secondTD">${reportIndex.reportIndexTemplate}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">数据量(条)</td>
            <td class="secondTD">${reportIndex.reportIndexNumbers}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">报送文件</td>
            <td class="secondTD">
                <a class="changeFont"
                   href="${request.getContextPath()}/admin/messageSubmission/downLoadFile.jhtml?reportIndexId=${reportIndex.reportIndexId}">
                    下载</a>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">状态</td>
            <td class="secondTD">
            <#if reportIndex.reportIndexStatus = 0>
                <span class="changeFont fontSize12">上报成功</span>
            <#else>
                <span class="delFont fontSize12">上报失败</span>
            </#if>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">报送机构</td>
            <td class="secondTD">${reportIndex.reportIndexOrgName}</td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">归档时间</td>
            <td class="secondTD">${(reportIndex.reportIndexTime?string("yyyy-MM-dd"))!}</td>
        </tr>
    <#if reportIndex.reportIndexSubmitTime??>
        <tr>
            <td class="noBorderL firstTD">操作时间</td>
            <td class="secondTD">${(reportIndex.reportIndexSubmitTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
        </tr>
    </#if>
        <tr>
            <td class="noBorderL firstTD">报送错误记录</td>
            <td class="secondTD">
            <#if reportIndex.errorExcelPath??>
                <a class="changeFont fontSize12"
                   href="${request.getContextPath()}/admin/messageSubmission/downError.jhtml?id=${reportIndex.reportIndexId}">点击查看</a>
            <#else >
                <span class="changeFont fontSize12">暂无</span>
            </#if>
            </td>
        </tr>
    </table>
    <div class="showBtnBox">
        <input type="button" class="cancleBtn closeThisLayer" value="关 闭"/>
    </div>
</div>
</body>
<script>
    $(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('.closeThisLayer').on('click', function () {
            parent.layer.close(index); //执行关闭
        });
        $('.sureInLib').on('click', function () {
            parent.layer.close(index); //执行关闭
        });
    })
</script>
</html>
