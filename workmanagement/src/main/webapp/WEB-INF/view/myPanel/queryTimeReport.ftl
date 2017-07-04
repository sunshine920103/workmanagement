<!DOCTYPE html>
<html>
	<head>
		<title>行政处罚详情</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body>
		<div class="showListBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">行政处罚详情</caption>
					<tbody>
					<#list xzcfxx as a>
				
					<tr>
							<td width="300" class="noBorderL firstTD">贷款企业组织机构代码/统一社会信用代码</td>
							<td width="400" class="secondTD">${a.CODE_ORG}/${a.CODE_CREDIT}</td>
						</tr>
					
					
						<tr>
							<td class="noBorderL firstTD">行政处罚文书号</td>
							<td class="secondTD">${a.INDEX_XZCFXX_XZCFSH}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">处罚时间</td>
							<td class="secondTD">
							
							${(a.INDEX_XZCFXX_CFSJ?string("yyyy-MM-dd HH:mm:ss"))!}
						</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">违法或违规行为描述</td>
							<td class="secondTD">${a.INDEX_XZCFXX_WFHWGXWMS}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">裁定处罚部门</td>
							<td class="secondTD">${a.INDEX_XZCFXX_CDCFBM}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">涉及金额</td>
							<td class="secondTD">${a.INDEX_XZCFXX_SJJE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">处罚金额</td>
							<td class="secondTD">${a.INDEX_XZCFXX_CFJE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">处罚决定</td>
							<td class="secondTD">${a.INDEX_XZCFXX_CFJD}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">整改情况</td>
							<td class="secondTD">${a.INDEX_XZCFXX_ZGQK}</td>
						</tr>
					</#list>
					</tbody>
					
				</table>
				
				
				<!--<table cellspacing="0" cellpadding="0" class="marginT10 noBorderT">
					<tr><td class="firstTRFont textCenter">暂无数据</td></tr>
				</table>-->
				
				<!--分页-->
				<!--<#include "/fragment/paginationbar.ftl"/>-->
				
				
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
