<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
    </script>
    <title>基本信息企业名称差异列表</title>
</head>
<body class="showListBox excelContent">
<div class="listBox">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">基本信息企业名称差异列表</caption>
        <tr class="firstTRFont">
            <td width="80"><input type="checkbox" onclick="CheckedRev()">全选/反选</td>
            <td width="80">统一社会信用代码</td>
            <td width="80">组织机构代码</td>
            <td width="80">原值</td>
            <td width="80">上报值</td>
        </tr>
    <#list changeIndexItem as cii>
        <tr>
            <td width="80">
                <input type="checkbox">
                <input name="id" type="hidden" value="${cii.defaultIndexItemId}">
            </td>
            <td width="80"><input value="${cii.codeCredit}"></td>
            <td width="80"><input value="${cii.codeOrg}"></td>
            <td width="80"><input name="oleName" value="${cii.qymc}"></td>
            <td width="80"><input name="newName" value="${cii.defaultIndexItemTime}"></td>
        </tr>
    </#list>
    </table>
</div>
<div class="showBtnBox">
    <input class="sureBtn" type="button" value="替换" onclick="saveThis()"/>
    <input id="goOut" class="sureBtn" type="button" value="离开页面"/>
</div>
</body>
<script>
var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
    $('.closeThisLayer').on('click', function () {
        parent.layer.close(index); //执行关闭
    });

    $(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('#goOut').on('click', function () {
            parent.layer.close(index); //执行关闭
            parent.window.location.href = "${request.getContextPath()}/admin/messageSubmission/list.jhtml";
        });

        //回显
        var msg = "${msg}";
        if (msg != "") {
            layer.alert(msg, {
                icon: (msg == "操作成功") ? 1 : 2,
                shade: 0.3,
                shadeClose: true
            });
        }
    });

    function saveThis() {
        var chid = "";
        var chnewName = "";
        $("input[type='checkbox']").each(function () {
            if ($(this).is(':checked')) {
                var id = $(this).siblings().val();
                var name = $(this).parent().siblings().children("input[name='newName']").val();
                chid += (id + ",");
                chnewName += (name + ",");
            }
        });
        $.post("${request.getContextPath()}/admin/messageSubmission/saveIndexName.jhtml",
                {"id": chid, "newName": chnewName}, function (msg) {
                    layer.alert(msg, {
                        icon: (msg == "操作成功") ? 1 : 2,
                        shade: 0.3,
                        shadeClose: true
                    },function(){
                    	 parent.layer.close(index); //执行关闭
                    });
                }
        )
    }

    //全选反选
    function CheckedRev() {
        $(':checkbox').each(function () {
            this.checked = !this.checked;
        });
        this.checked = !this.checked;
    }
    
</script>
<html>