<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			$(function(){
				var msg = "${msg}";
				if(msg != "") {
					alertInfo(msg);
					$('.layui-layer-shade').height($(window).height());
//					layer.alert(msg, {
//						icon: msg="操作成功"?1:2,
//						shade: 0.3,
//						shadeClose: true
//					});
				}
			});
		</script>
		<title>展示</title>
	</head>
	<body>
		<div class="showListBox">
			<#if sysUserLog??>
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">报送成功</caption>
					<tr>
						<td width="200" class="noBorderL firstTD">模板名称</td>
						<td width="500" class="secondTD">${indexTb.indexName}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">数据量(条)</td>
						<td class="secondTD">1</td>
					</tr>
					<#if sysUserLog.sysUserLogAuthFile??!>
					<tr>
						<td class="noBorderL firstTD">授权文件</td>
						<td class="secondTD"><a class="changeFont" href="${request.getContextPath()}/admin/manualEntry/downLoadFile.jhtml?fileName=${sysUserLog.sysUserLogAuthFile}&name=${indexTb.indexName}">下载</a></td>
					</tr>
					</#if>
					<tr>
						<td class="noBorderL firstTD">状态</td>
						<td class="secondTD">
							<#if sysUserLog.sysUserLogResult = 0> 
								<span class="changeFont fontSize12">失败</span>
							<#else>
								<span class="delFont fontSize12">成功</span>
							</#if>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">报送机构</td>
						<td class="secondTD">${sysUserLog.sysUserLogOrgName}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">归档时间</td>
						<td class="secondTD">${(sysUserLog.sysUserLogTime?string("yyyy-MM-dd"))!} </td>
					</tr>
						<tr>
						<td class="noBorderL firstTD">操作时间</td>
						<td class="secondTD">${(sysUserLog.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
					</tr>
				</table>
			<#else>	
				<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
					<tr class="firstTRFontColor">
						<td style="text-align: center;font-weight: bold;" >暂无信息</td>
					</tr>
				</table>
			</#if>
				<div class="showBtnBox">
					<input type="button"  class="sureBtn closeThisLayer" value="关 闭" />
				</div>
		</div>
	</body>
	<script type="text/javascript">
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
	})
		
	</script>
</html>
