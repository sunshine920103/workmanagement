<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<style>
			li{
				list-style-type:none;
				/*float: left;*/
			}
			
		</style>
		<script type="text/javascript" >
			$(function(){
				
			});
		</script>
		<title>查看</title>
	</head>
	<body>
		<div class="showListBox">
			<table>
				<tr>
					<td>任务名称</td>
					<td>${reportTaskPushSet.reportTaskPushSetName}</td>
				</tr>
				<tr>
					<td>任务周期</td>
					<td>${reportTaskPushSet.reportTaskPushSetCycle}</td>
				</tr>
				<tr>
					<td>状态</td>
					<td>
						<#if reportTaskPushSet.reportTaskPushSetStatus==1>
		            		禁用
		            	<#else>
		            		正常
		            	</#if>
					</td>
				</tr>
				<tr>
					<td>涉及报送方式</td>
					<td>
						<#if reportTaskPushSet.reportTaskPushSetType==0>
							WORD报送
						<#elseif reportTaskPushSet.reportTaskPushSetType==1 >
							excel报送
						</#if>
					</td>
				</tr>
				<tr>
					<td>涉及报送模板</td>
					<td>${reportTaskPushSet.reportTaskPushSetTemplate}</td>
				</tr>
				<tr>
					<td>任务发布机构</td>
					<td>${reportTaskPushSet.reportTaskPushSetOrgName}</td>
				</tr>
				<tr>
					<td>已选机构列表</td>
					<td>
						<ul>
							<#list sysOrgList as item>
								<li class="fontSize12">${item.sys_org_name}</li>
							</#list>
						</ul>
					</td>
				</tr>
			</table>
				<div class="showBtnBox">
					<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
				</div>	
		</div>
	</body>
	<script>
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
	</script>
</html>
