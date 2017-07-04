<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		
		<title>excel错误说明列表</title>
	</head>
	<body class="showListBox excelContent">
		<div class="listBox">
			<p class="warmFont">报送数据有误，上报失败，请重新检查后上报</p>
			
				<table  cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">有误列表</caption>
					<tr class="firstTRFont">
						<td width="80">错误信息</td>
						<td width="80">错误信息总条数：${errorList?size}</td>
					</tr>
					
					<#list errorList as msg>
						<tr>
							<td colspan="2">${msg}</td>
						</tr>
					</#list>
				</table>
		</div>
		<div class="showBtnBox">
		<input id="outMsg" class="sureBtn closeThisLayer" type="button" value="导 出">
        <input id="close" class="sureBtn closeThisLayer" type="button" value="关 闭"/>
    	</div>
	</body>
	<script type="text/javascript" >
	$(function () {
        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
        $('#close').on('click', function () {
            parent.layer.close(index); //执行关闭
        });
        $('#outMsg').on('click', function () {
            window.location.href = "${request.getContextPath()}/admin/reportIndex/outMsg.jhtml";
        })
    })
	</script>
</html>
