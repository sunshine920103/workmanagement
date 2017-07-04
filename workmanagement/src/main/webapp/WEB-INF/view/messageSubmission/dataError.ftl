<!DOCTYPE html>
<html>
<head>
    <title>数据报送错误</title>
<#include "/fragment/common.ftl"/>
</head>
<body>
<div class="showListBox excelContent">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">报文报送数据有误，上传失败，请重新修改后上报(错误行数是从TXT文本的第一行开始)</caption>
        <tbody>
        <tr class="firstTRFont">
            <td width="50" class="noBorderL">序号</td>
            <td width="500">错误信息</td>
        </tr>
        <#list messages as mes>
        <tr>
            <td>${mes_index+1}</td>
            <td>${mes}</td>
        </tr>
        </#list>
        </tbody>
    </table>
    <div class="showBtnBox">
        <input id="outMsg" class="sureBtn closeThisLayer" type="button" value="导 出">
        <input id="close" class="sureBtn closeThisLayer" type="button" value="关 闭"/>
    </div>
</div>
</body>
<script>
    $(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('#close').on('click', function () {
            parent.layer.close(index); //执行关闭
            parent.window.location.href = "${request.getContextPath()}/admin/messageSubmission/list.jhtml";
        });
        $('#outMsg').on('click', function () {
            window.location.href = "${request.getContextPath()}/admin/messageSubmission/outMsg.jhtml";
        })
    })
</script>
</html>
