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
                    shade: 0.3,
                    shadeClose: true
                });
            }
            $("#addDic").click(function () {
                setLayer('增加字典', '${request.contextPath}/admin/dic/add.jhtml');
                $(this).blur();
            });
            $('#exportAll').click(function () {
                $.post("${request.contextPath}/admin/dic/isAdmin.jhtml", function (data) {
                    if (data == "操作成功") {
                        window.location.href = "${request.contextPath}/admin/dic/exportAll.jhtml";
                        return false;
                    }
                    layer.alert(data, {
                        icon: (data == "操作成功") ? 1 : 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                });
            });
        });
        function excelOne(obj) {
            $.post("${request.contextPath}/admin/dic/isAdmin.jhtml", function (data) {
                if (data == "操作成功") {
                    window.location.href = "${request.contextPath}/admin/dic/exportOne.jhtml?ddpid=" + obj;
                    return false;
                }
                layer.alert(data, {
                    icon: (data == "操作成功") ? 1 : 2,
                    shade: 0.3,
                    shadeClose: true
                });
            });
        }

        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        }
        function delet(ddpid) {
            layer.confirm('确定删除吗？', {btn: ['确认', '取消']}, function () {
                window.location.href = "${request.contextPath}/admin/dic/delete.jhtml?ddpid=" + ddpid;
            })
        }

    </script>
    <title>数据字典列表</title>
</head>
<body class="eachInformationSearch">
<form id="searchForm" method="post"></form>
<div>
    <!--增加-->
    <div class="queryInputBox verticalMiddle" style="margin-bottom:10px">
        <input id="addDic" class="sureBtn " type="button" value="增加字典" style="margin-left:30px"/>
        <input id="exportAll" class="sureBtn" type="button" value="全部导出"/>
        <span class="warmFont inlineBlock marginL20 fontSize12">注：字典名称必须是唯一，无法删除使用的字典。</span>
    </div>
    <!--列表-->
    <div class="listBox">
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">数据字典列表</caption>
            <tr class="firstTRFont">
                <td width="20">序号</td>
                <td width="200">字典名称</td>
                <td width="50">备注</td>
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
                    <#if item.isThisArea??>
                        <a class="changeFont fontSize12 cursorPointer hasUnderline"
                           onclick="setLayer('修改字典','${request.contextPath}/admin/dic/edit.jhtml?id=${item.dicId}')">修 改</a>
                        <a class="delFont fontSize12 cursorPointer hasUnderline"
                           onclick="delet(${item.dicId})">删 除</a>
                    <#else >
                    	<span class=" fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">修 改</span>
                         <span class=" fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">删 除</span>
                    </#if>
                    <a class="delFont fontSize12 cursorPointer hasUnderline" onclick="excelOne(${item.dicId})">导 出</a>
                </td>
            </tr>
        </#list>
        </table>
    <#if (dicList?? && dicList?size > 0)>
        <#include "/fragment/paginationbar.ftl"/>
    <#else>
        <table style="border-top: 0px; " cellpadding="0" cellspacing="0">
            <tr class="firstTRFont">
                <td style="text-align: center;font-weight: bold;">暂无信息</td>
            </tr>
        </table>
    </#if>
    </div>
</div>
</body>
</html>