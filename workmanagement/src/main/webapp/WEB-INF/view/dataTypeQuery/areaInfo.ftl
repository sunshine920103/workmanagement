<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8">
		<title></title>
		<script type="text/javascript">
			function exportToExcel(){ 
	            var len = $("#dataList tr").length;
	            if (len > 1) {
					window.location.href="${request.getContextPath()}/admin/dataTypeQuery/exportTo.jhtml?condition="+"${condition}";
	            } else {
	                layer.alert("没有搜索结果");
	            } 
			}
			
		 
		</script>
	</head>
	<body>
		<div class="eachInformationSearch">
			<div class="queryInputBox" style="margin-top: 20px;">
					
				<form id ="search" method="post" action="${request.contextPath}/admin/dataTypeQuery/search.jhtml" style="display: inline-block;*zoom:1;*display:inline;position:relative;margin-left:20px">
					<span class="fuck">请输入地区名或编码</span>
					<input name="condition" class="inputSty inputOtherCondition" type="text" value="${condition}" id="area">
					<input type="button"  onclick="subm()" class="sureBtn" style="margin-left: 0px;" value="查 询" />
				</form>	
				<input onclick="exportToExcel()" type="button" value="导出" class="sureBtn sureBtnEx" style="margin-left: 25px;"/>
				<div class="listBox" style="margin-left: 20px;">
					<table cellpadding="0" cellspacing="0" id="dataList">
						<tr class="firstTRFont" >
							<td width="400">编码</td>
							<td width="400">地区名称</td>
						</tr>
						<#list list as li>
						<tr>
							<td>${li.sysAreaCode}</td>
							<td>${li.sysAreaName}</td>
						</tr>
						</#list>
					</table>
					<#include "/fragment/paginationbar.ftl"/>
				</div>
			</div>
			<form id="searchForm" method="post">
				<input  id ="hidArea" name="condition"  type="hidden" value="${condition}"  />
			</form>
		</div>
	</body>
</html>
<script type="text/javascript"> 
		if($(".inputOtherCondition").val() != ""){
			$(".fuck").hide();
		}
		$(".inputOtherCondition").focus(function(){
			$(".fuck").hide();
		}).blur(function(){
			if($.trim($(this).val())==""){
				$(".fuck").show();
			}
		});
		$(".fuck").click(function(){
			$(".inputOtherCondition").focus();
		})  
		
		function subm(){
				var search=$("#area").val();
				 
				if(checkTChineseM(search)==0) {
					layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
	                return false;
		    	} 
			$("#search").submit()
			} 
</script>
