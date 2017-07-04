<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<title>手工修改历史修改记录</title>
	<#include "/fragment/common.ftl"/>
</head>
<body>
	<form id="searchForm" method="post"></form>
	<div class="showListBox">
		<table  cellpadding="0" cellspacing="0">
			<caption class="titleFont1 titleFont1Ex">手工修改历史修改记录</caption>
			<tbody>
				<tr class="firstTRFont">
					<td>序号</td>
					<td style="width:10%;">指标大类</td>
					<td style="width:10%;">指标项</td>
					<td style="width:20%;">原始值</td>
					<td style="width:20%;">修改值</td>
					<td style="width:15%;">修改时间</td>
					<td style="width:15%;">修改机构</td>
					<td style="width:10%;">修改者</td>
				</tr>
				<#list querySysUserLog as item>
				<tr>
					<td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
					<td>${item.indexName}</td>
					<td>${item.indexItemName}</td>
					<td>${item.sysUserLogOldValue}</td>
					<td>${item.sysUserLogNewValue}</td>
					<td>${(item.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
					<td>${item.sysUserLogOrgName}</td>
					<td>${item.sysUserLogUserName}</td>
				</tr>
				</#list>
			</tbody>
		</table>	
		<#if (querySysUserLog?? && querySysUserLog?size > 0)>
				<#include "/fragment/paginationbar.ftl"/>
			<#else>
				<table cellspacing="0" cellpadding="0" class="noBorderT" style="margin-top: 0px;">
					<tr class="firstTRFont">
						<td style="text-align: center;">暂无数据</td>
					</tr>
				</table>
		</#if>
		<div class="showBtnBox">
			<input id="no" type="button" value="关 闭" class="sureBtn closeThisLayer"/>
		</div>
	</div>
</body>
<script>
	$(function(){
		$("#no").click(function(){
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			if(index){
				parent.layer.close(index); //执行关闭
			}
		})
		
	})
</script>
</html>
