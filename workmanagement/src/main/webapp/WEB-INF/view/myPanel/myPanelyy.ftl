<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
	<meta charset="UTF-8">
	
	<script type="text/javascript">

</script>
<title>金融机构异议处理</title>
</head>
	<body >
	<form id="searchForm" method="post"></form>
	<div class="eachInformationSearch">
	<div class="listBox">
		<table cellpadding="0" cellspacing="0">
			<caption class="titleFont1 titleFont1Ex">异议列表</caption>
			<tbody>
				<tr class="firstTRFont">
					<td width="50">序号</td>
					<#--<td width="100">企业二码</td>
					<td width="150">企业名称</td>-->
					<td width="100">指标大类</td>
					<#--<td width="150">推送机构</td>-->
					<td width="150">录入机构</td>
					<td width="100">录入时间</td>
					<td width="100">状 态</td>
					<td width="100">操作</td>
				</tr>
			<#list sysOperateList as sol>
				<tr>
            	<td>${(1 + sol_index) + (page.getPageSize() * page.getCurrentPage())}</td>
            		<#--<td>${sol.codeCredit}/${sol.codeOrg}</td>
            		<td>${sol.jbxxQimc}</td>	-->
					<td>${sol.indexName}</td>
					<#--<td>${sol.sysOrgName}</td>-->
					<td>${sol.sysReportOrgName}</td>
					<td>${(sol.sysOperateTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
					<td>
					<#if sol.sysOperateStatus==1>
					处理中
					<#elseif sol.sysOperateStatus==2>
					已处理
					<#else>
					待处理
					</#if>
					</td>
					
					<#if OrgId == sol.reportOrgId >
					<#if sol.sysOperateStatus==0  >
					<td>
						<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="objManage(this,'${(sol.recordDate?string("yyyy-MM-dd"))!}','${sol.defaultIndexItemId}','${(sol.sysOperateTime?string("yyyy-MM-dd"))!}','${sol.indexItemId}','${sol.sysOrgId}','${sol.reportOrgId}')">异议处理</a>
					</td>
					<#elseif sol.sysOperateStatus==1>
						<td>
							<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="objManage(this,'${(sol.recordDate?string("yyyy-MM-dd"))!}','${sol.defaultIndexItemId}','${(sol.sysOperateTime?string("yyyy-MM-dd"))!}','${sol.indexItemId}','${sol.sysOrgId}','${sol.reportOrgId}')">异议处理</a>
						</td>
					<#else>
						<td>
						</td>
					</#if>
					<#else>
					<td>
					</td>
					</#if>
				</tr>			
			</#list>
			</tbody>
		</table>
		<#if (sysOperateList?? && sysOperateList?size > 0)>
			<#include "/fragment/paginationbar.ftl"/>
		<#else>
			<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
				<tr class="firstTRFontColor">
					<td style="text-align: center;font-weight: bold;" >暂无信息</td>
				</tr>
			</table>	    	
		</#if>
	</div>
</body>
</html>
