<!DOCTYPE html>
<html>
	<head>
		<title>信用评分查询结果</title>
		<#include "/fragment/common.ftl"/>
		
		<script>
			//查询单个评分
			function searchScore(index_jbxx_id, template_id, date){
				if(typeof index_jbxx_id == "undefined"){
					alertInfo("未获取到企业标识");
					return false;
				}
				var url = '${request.getContextPath()}/admin/creditScoreQuery/detailScore.jhtml?index_jbxx_id=' + index_jbxx_id + "&template_id=" + template_id + "&date=" +date + " 23:59:59";
				setLayer('查询',url);
			}
			
			function exportExcel(template_id){
				var size = $("#size").val();
				if(size<=0){
					alertInfo("查询结果为空，无法导出");				
				}else{
					location.href = '${request.getContextPath()}/admin/creditScoreQuery/export.jhtml?template_id=' + template_id + '&date=' + '${batchInfoDate}';
				}
			}
		</script>
	</head>
	<body>
		
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">查询企业结果列表</caption>
				  <tbody>
					<tr class="firstTRFont">
						<td width="200" class="noBorderL">统一社会信用代码</td>
						<td width="200">组织机构代码</td>
						<td width="200">企业名称</td>
						<td width="50">评分总分</td>
						<td width="80">操作</td>
					</tr>
					<#list enterprise as e>
						<tr>
							<td>${e.CODE_CREDIT}</td>
							<td>${e.CODE_ORG}</td>
							<td>${e.INDEX_JBXX_QYMC}</td>
							<td>${e.score}</td>
							<td><a href="javascript:" onclick="searchScore(${e.INDEX_JBXX_ID}, ${template_id}, '${date}')" class="changeFont fontSize12 cursorPointer hasUnderline">开始查询</a></td>
						</tr>
					</#list>
					
					<#if !(enterprise?? && enterprise?size > 0)>
						<tr class="firstTRFontColor">
							<td colspan="5" style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</#if>
					
					<input type="hidden" value="${enterprise?size}" id="size" />
				 </tbody>
			</table>
			
			<p class="warmFont marginT20">注：导出Excel文件中包含了明细的得分项</p>
			<div class="showBtnBox">
				<input class="cancleBtn closeThisLayer" type="button" value="关 闭"/>
				<input class="sureBtn" onclick="exportExcel(${template_id})" data-id="1" type="button" value="导出EXCEL文件"/>
			</div>
		</div>
<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
    		parent.layer.close(index); //执行关闭
		});
	})
	</script>
	</body>
</html>
