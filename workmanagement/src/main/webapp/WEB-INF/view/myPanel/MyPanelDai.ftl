<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body class="showListBox">
	<form id="searchForm" method="post"></form>
		<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">贷款逾期情况（本机构客户在其他机构最新的逾期情况）</caption>
					<tr class="firstTRFont">
					<td style="width:10%;" class="noBorderL">编号</td>
					<td style="width:30%;">信用码/机构码</td>
					<td style="width:40%;">企业名称</td>
					<td style="width:20%;">到期时间</td>
					</tr>
					<#list indexYhdkList as yhdk>
						
						<tr>
						<td class="noBorderL"> ${yhdk_index+1} </td>
						<td>${yhdk.CODE_CREDIT}/${yhdk.CODE_ORG}</td>
						<td>${yhdk.INDEX_JBXX_QYMC}</td>
						<td>${yhdk.INDEX_YXDK_DQR}</td>
								</tr>
					</#list>  
				</table>
				

				<!--分页-->
				<#include "/fragment/paginationbar.ftl"/>
					
				<div class="showBtnBox">
					<input class="cancleBtn closeThisLayer" type="button" value="关 闭"/>
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
