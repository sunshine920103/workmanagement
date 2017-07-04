<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
		<title>已报数据列表</title>
		<script type="text/javascript">
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					alertInfo(msg);
				}
				
			})
			
		</script>
	</head>
	<body>
		
		<form id="searchForm" method="post">
			<input type="hidden" name="orgId" value="${reportIndexList[0].reportIndexOrgId}" />
			<input type="hidden" name="reportedDate" value="${reportedDate}" />
		</form>
		<div class="rightPart floatLeft marginT20 eachInformationSearch">
			<div >
				<div class="margin2030" style="margin-right:0px;">
					<div>
						<span class=" fontSize12">当前选择机构:</span>
						<#if orgname?? !>
							<input style="width: 200px;" class=" fontSize12" readonly="readonly" type="text" value="${orgname}" />
						<#else>
							<input class=" fontSize12" readonly="readonly" type="text" value="所有" />
						</#if>
						<span class=" fontSize12 paddingLR10" id="orgName"></span> 
					</div>
				</div>
				<div id="min" class="listBox" style="margin-right: 0px;">
					<table cellpadding="0" cellspacing="0">
						<tr class="firstTRFont">
							<td style="width:5%;">序号</td>
							<td style="width:10%;">报送形式</td>
							<td style="width:12%;">报送模板或指标大类</td>
							<td style="width:20%;">报送机构</td>
							<td style="width:10%;">归档日期</td>
							<td style="width:10%;">操作时间</td>
							<td style="width:10%;">状态</td>
							<td style="width:13%;">操作</td>
						</tr>
							<#list reportIndexList as item>
								<tr>
								<#--<td>${(1 + ps_index) + page.getPageSize() * (page.getCurrentPage() - 1)}</td>-->
	            		<td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
									<td>
										<#if item.reportIndexMethod == 0>
											WORD报送
										<#elseif item.reportIndexMethod ==1>
											excel报送
										<#elseif item.reportIndexMethod ==2>
											手工录入
										</#if>
									</td>
									<td>${item.reportIndexTemplate}</td>
									<td>${item.reportIndexOrgName}</td>
									<td>${(item.reportIndexTime?string("yyyy-MM-dd"))!} </td>
									<td>${(item.reportIndexSubmitTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
									<td>
										<#if item.reportIndexStatus = 0> 
										 	有效
										<#elseif item.reportIndexStatus = 1>
										 	无效
										<#elseif item.reportIndexStatus = 2>
											未入库
										<#elseif item.reportIndexStatus = 3>
											上报失败
										</#if>
									</td>
									<td>
										<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看','${request.getContextPath()}/admin/reportIndex/show.jhtml?id=${item.reportIndexId}')">查 看</a>
										<a class="delFont fontSize12 hasUnderline cursorPointer" href="${request.getContextPath()}/admin/reportedDelete/delete.jhtml?reportIndexId=${item.reportIndexId}&orgId=${item.reportIndexOrgId}&reportedDate=${reportedDate}" onclick="return confirm('确定删除该次报送内容吗?');">删 除</a>
									</td>
								</tr>
							</#list>
						</table>
								<table cellspacing="0" cellpadding="0" class="noBorderT">
									<#if (reportIndexList?? && reportIndexList?size > 0)>
										<#include "/fragment/paginationbar.ftl"/>
									<#else>
										<table cellspacing="0" cellpadding="0" class="noBorderT">
											<tr class="firstTRFont">
												<td style="text-align: center;">暂无数据</td>
											</tr>
										</table>
									</#if>
								</table>
				</div>
			</div>
		</div>
		
	</body>
</html>