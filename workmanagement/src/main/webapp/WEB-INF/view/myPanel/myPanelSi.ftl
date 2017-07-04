<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body class="showListBox">
	<form id="searchForm" method="post"></form>
		<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">司法信息（本机构客户在其他机构最新的司法信息）</caption>
					<tr class="firstTRFont">
					<td style="width:5%;" class="noBorderL">编号</td>
					<td style="width:20%;">信用码/机构码</td>
					<td style="width:25%;">企业名称</td>
					<td style="width:20%;">案由</td>
					<td style="width:20%;">受理法院</td>
					<td style="width:10%;">日期</td>
					</tr>
					<#list indexSfxxList as sfxx>
					<tr>
					<td class="noBorderL">${sfxx_index+1}</td>
					<td>${sfxx.CODE_CREDIT}/${sfxx.CODE_ORG}</td>
					<td>${sfxx.INDEX_JBXX_QYMC}</td>
					<td>${sfxx.INDEX_SFXX_AY}</td>
					<td>${sfxx.INDEX_SFXX_LAFY}</td>
					<td>${(sfxx.INDEX_SFXX_LARQ?string("yyyy-MM-dd")!)}</td>
					</tr> 
					</#list>
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
