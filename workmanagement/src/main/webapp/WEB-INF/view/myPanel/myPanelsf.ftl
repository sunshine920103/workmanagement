<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body>
		<div class="showListBox">
		
		
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">司法记录详情</caption>
					<tbody>
					<#list sfxx as a>
						<tr>
							<td width="300" class="noBorderL firstTD">案号</td>
							<td width="400" class="secondTD">${a.INDEX_SFXX_AH}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">立案法院</td>
							<td class="secondTD">${a.INDEX_SFXX_LAFY}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">立案日期</td>
							<td class="secondTD">${(a.INDEX_SFXX_LARQ?string("yyyy-MM-dd HH:mm:ss"))!}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">案由</td>
							<td class="secondTD">${a.INDEX_SFXX_AY}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">执行依据文书编号</td>
							<td class="secondTD">${a.INDEX_SFXX_ZXYJWSBH}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">执行标的</td>
							<td class="secondTD">${a.INDEX_SFXX_ZXBD}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">执行标的金额</td>
							<td class="secondTD">${a.INDEX_SFXX_ZXBDJE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">已执行标的</td>
							<td class="secondTD">${a.INDEX_SFXX_YZXBD}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">已执行标的金额</td>
							<td class="secondTD">${a.INDEX_SFXX_YZXBDJE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">执行标的日期</td>
							<td class="secondTD">
							${(a.INDEX_SFXX_SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!}
							
							</td>
							
						</tr>
						<tr>
							<td class="noBorderL firstTD">案件状态</td>
							<td class="secondTD">
							${a.INDEX_SFXX_STATUS}
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">结案日期</td>
							<td class="secondTD">${(a.INDEX_SFXX_JARQ?string("yyyy-MM-dd HH:mm:ss"))!}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">执行结案方式</td>
							<td class="secondTD">${a.INDEX_SFXX_ZXJAFS}</td>
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
