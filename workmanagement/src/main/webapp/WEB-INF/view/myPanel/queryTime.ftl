<!DOCTYPE html>
<html>
	<head>
		<title>查询次数统计</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body>
		<div class="showListBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">查询次数统计</caption>
					<tbody>
					<#list all as a>
						<tr>
							<td width="100" class="noBorderL">本月${a.SYS_USER_LOG_MENU_NAME}</td>
							<td width="100">${a.FRIST}</td>
							<td width="100">前3个月${a.SYS_USER_LOG_MENU_NAME}</td>
							<td width="100">${a.LA}</td>
						</tr>
						</#list>
					</tbody>
					
				</table>
				
				<div class="showBtnBox">
					<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
				</div>
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
