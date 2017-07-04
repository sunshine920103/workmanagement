<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		//回显
		var msg = "${msg}"; 
		if(msg!=""){
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
		}
	});
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
<title>历史报送记录</title>
	</head>
	<body class="eachInformationSearch">
		<div class="queryInputBox">
			
				<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">企业相关报送记录列表</caption>
					<tr class="firstTRFont">
						<td width="50">序号</td>
						<td width="100">指标大类</td>
						<td width="100">录入机构</td>
						<td width="100">归档时间</td>
						<td width="100">录入时间</td>
						<td width="80">操作</td>
					</tr>
					<tr class="firstTRFont">
						<td width="50"></td>
						<td width="100"></td>
						<td width="100"></td>
						<td width="100"></td>
						<td width="100"></td>
						<td width="80">
							<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('异议处理','${request.getContextPath()}/admin/adminObj/show.jhtml?')">异议处理</a>
						</td>
					</tr>
					
				</table>
				<#if (queryMajorIddataValues?? && queryMajorIddataValues?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>	    	
				</#if>
			</div>
		</div>
	</body>
</html>