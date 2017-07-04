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
			<table cellpadding="0" cellspacing="0">
				<#if reportIndex.reportIndexStatus == 0>
				<caption class="titleFont1 titleFont1Ex">EXCEL报送成功</caption>
				<#else>
				<caption class="titleFont1 titleFont1Ex">EXCEL报送失败</caption>
				</#if>
				<tr>
					<td width="200" class="noBorderL firstTD">EXCEL模板名称</td>
					<td width="500" class="secondTD">${reportIndex.reportIndexTemplate}</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">上报文件中数据量(条)</td>
					<td class="secondTD">${reportIndex.reportIndexNumbers}</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">报送文件</td>
					<td class="secondTD">
					<#if reportIndex.reportIndexPath??>
						<a class="changeFont fontSize12" href="${request.getContextPath()}/admin/reportIndex/downLoadFile.jhtml?reportIndexId=${reportIndex.reportIndexId}">下 载</a>
					<#else>
						无
					</#if>
					</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">报送错误消息文件</td>
					<#if reportIndex.errorExcelPath??>
						<td class="secondTD"><a class="changeFont fontSize12" href="${request.getContextPath()}/admin/reportIndex/downLoadErrorFile.jhtml?reportIndexId=${reportIndex.reportIndexId}">下 载错误文件</a></td>
					<#else>
						<td class="secondTD">无</td>
					</#if>
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
					<td class="secondTD">${(reportIndex.reportIndexTime?string("yyyy-MM-dd"))!} </td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">操作时间</td>
					<td class="secondTD">${(reportIndex.reportIndexSubmitTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
				</tr>
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
