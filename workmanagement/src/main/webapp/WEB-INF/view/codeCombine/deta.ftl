<!DOCTYPE HTML>
<html>
<head>
		<#include "/fragment/common.ftl"/>
		<title>二码合并详情</title>
</head>
<body class="eachInformationSearch">
	<form id="searchForm" method="post"><input type="hidden" name="time" value="${time}" /></form>
	<div class="showListBox">
				<table>
					<caption class="titleFont1 titleFont1Ex">二码合并详情列表</caption>
					<tbody>
					<tr class="firstTRFont">
					<td width="50">序号</td>
					<td width="100">统一社会识别码</td>
					<td width="100">组织机构代码</td>
					<td width="100">企业名称</td>
				</tr>
				<#list list as li >
					<tr>
					<td width="50">${li_index+1}</td>
					<td width="100">${li.codeCredit}</td>
					<td width="100">${li.codeOrg}</td>
					<td width="100">${li.qymc}</td>
				</tr>
				</#list>
					</tbody>
				</table>
				<div class="showBtnBox">
					<input type="button"  class="cancleBtn closeThisLayer" value="关 闭"  />
				</div>
				
	</div>
</body>
	<script>
	 $(function(){
			var i=0
			$(".orgid").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getOrgName.jhtml",{id:orgId},function(data1){
						 $(".orgid").eq(i).text(data1)
						 i++
					})
			})
    })

	$(function(){
		
		//取当前页面名称(带后缀名) 
		var strUrl=location.href;
		var arrUrl=strUrl.split("/"); 
		var strPage=arrUrl[arrUrl.length-1];
		
		//判断页面该页面的名字
		if(strPage.indexOf("queryCode")>=0){		
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
		  });   
		}else{			
			$('.closeThisLayer').on('click', function(){
				window.location.href="${request.getContextPath()}/admin/codeCombine/list.jhtml?";
			});			
		}
			
	});
	</script>
</html>
