<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body class="showListBox">
	<form id="searchForm" method="post"></form>
	<table cellpadding="0" cellspacing="0" >
	  <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td class="noBorderL" style="width:6%;">序号</td>
                    <td style="width:35%;">任务名称</td>
                    <td style="width:25%;">任务结束时间</td>
                    <td style="width:14%;">任务类型</td>
                    <td style="width:14%;">任务状态</td>
                </tr>
                <#list report as r>
                    <#if r_index < 5 >
                        <tr>
                            <td class="noBorderL">${r_index+1}</td>
                            <td>${r.reportTaskPushSetName}</td>
                            <td>${(r.reportTaskPushListEndTime?string("yyyy-MM-dd"))!}</td>

                            <#if r_index/2 == 0>
                                <td>WORD</td>
                            <#else>
                                <td>EXCEL</td>
                            </#if>
                            <#if r.reportTaskPushStatus == 0>
                                <td class="delFont">未完成</td>
                            </#if>
                            <#if r.reportTaskPushStatus == 1>
                                <td class="delFont">已完成</td>
                            </#if>
                            <#if r.reportTaskPushStatus == 2>
                                <td class="delFont">逾期</td>
                            </#if>

                        </tr>
                    </#if>
                </#list>
	</table>

				<!--分页-->
				<#include "/fragment/paginationbar.ftl"/>
					
				<div class="showBtnBox">
					<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
				</div>
	</body>
	<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
    		parent.layer.close(index); //执行关闭
		});
	})
	</script>
</html>
