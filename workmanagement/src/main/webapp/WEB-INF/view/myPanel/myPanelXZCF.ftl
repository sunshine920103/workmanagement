<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body class="showListBox">
	<form id="searchForm" method="post"></form>
		<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">行政处罚情况</caption>
					<tr class="firstTRFont">
					<td style="width:6%;" class="noBorderL">编号</td>
					<td style="width:25%;">行政处罚文书号</td>
					<td style="width:25%;">企业名称</td>
					<td style="width:30%;">处罚部门</td>
					<td style="width:30%;">日期</td>
					</tr>
					<tr>
						<td class="noBorderL">${x_index+1}</td>
						<td>20170701012</td>
						<td>崇左市交通局</td>
						<td>崇左运输有限公司</td>
						<td>2017年1月20日</td>
					</tr>
				
				</table>
				
				<!--分页-->
				<#include "/fragment/paginationbar.ftl"/>
				
				<div class="showBtnBox">
					<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
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
