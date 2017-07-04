<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			$(function(){
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg, {
						icon: msg="操作成功"?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
			});
		</script>
		<title>excel错误或成功列表</title>
	</head>
	<body>
		<div class="showListBox">
		<table>
			<#list reportIndexs as reportIndex>
				<table cellpadding="0" cellspacing="0">
			<#if reportIndex.REPORT_INDEX_STATUS == 0>
				<caption class="titleFont1 titleFont1Ex">报送成功</caption>
			<#else>
				<caption class="titleFont1 titleFont1Ex">报送失败</caption>
			</#if>
					<tr>
						<td width="200" class="noBorderL firstTD">模板名称</td>
						<td width="500" class="secondTD">${reportIndex.REPORT_INDEX_TEMPLATE}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">数据量(条)</td>
						<td class="secondTD">${reportIndex.REPORT_INDEX_NUMBERS}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">报送文件</td>
						<td class="secondTD"><a class="changeFont fontSize12" href="${request.getContextPath()}/admin/reportedDelete/down.jhtml?reportIndexId=${reportIndex.REPORT_INDEX_ID}&reportIndexName=${reportIndex.REPORT_INDEX_TEMPLATE}&menuName=数据删除">下 载</a></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">状态</td>
						<td class="secondTD">
							<#if reportIndex.REPORT_INDEX_STATUS = 0> 
								<span class="changeFont fontSize12">有效</span>
							<#elseif reportIndex.REPORT_INDEX_STATUS = 1>
								<span class="delFont fontSize12">无效</span>
							<#elseif reportIndex.REPORT_INDEX_STATUS = 2>
								<span class="delFont fontSize12">已删除</span>
							<#elseif reportIndex.REPORT_INDEX_STATUS = 3>
								<span class="delFont fontSize12">上报失败</span>
							</#if>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">报送机构</td>
						<td class="secondTD">${reportIndex.REPORT_INDEX_ORG_NAME}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">归档时间</td>
						<td class="secondTD">${(reportIndex.REPORT_INDEX_TIME?string("yyyy-MM-dd"))!} </td>
					</tr>
					
						<tr>
						<td class="noBorderL firstTD">操作时间</td>
						<td class="secondTD">${(reportIndex.REPORT_INDEX_SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!} </td>
					</tr>
					</#list>
				</table>
				<div class="showBtnBox">
					<input type="button"  class="sureBtn closeThisLayer"  value="关 闭" />
				</div>
		</div>
	</body>
	<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		$("#submitBtn").click(function(){
				var loading = layer.load();
				$.post("${request.getContextPath()}/admin/reportIndex/inTabel.jhtml?reportIndexId=${reportIndex.reportIndexId}",function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){
						if(data.flag){
							parent.window.location.href = "${request.getContextPath()}/admin/reportIndex/list.jhtml?reportExcelTemplateId=${reportExcelTemplateId}";
						}
						layer.close(index);
					});
				});
		});
	})
	</script>
</html>
