<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
			 function exportModel(){
			 	  var url='${request.getContextPath()}/admin/NewlyIncreased/downLoad.jhtml';
		        window.open(url);
			 }
			 
			 function exportExcel(id){
			 	 
		        window.location.href = "${request.getContextPath()}/admin/NewlyIncreased/exportExcel.jhtml?id=" + id ;
			 }
			 
			$(function(){
				$("#inquire").click(function(){
					var search=$("#search").val();
					if(checkChineseNoSpe(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			    	} 
					$("#selectExcel").submit()
				})
			})
		</script>
		<title>重点企业名单查询</title>
	</head>
	
	<body class="eachInformationSearch">
	<form id="searchForm" method="post" >
    
</form>
		<div class="queryInputBox">
			<div  class="verticalMiddle">
				<div class="verticalMiddle">

			</div>
				<input onclick="setLayer('新增','${request.getContextPath()}/admin/NewlyIncreased/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur()" type="button" value="新 增" class="sureBtn" style="margin-left:30px"/>
				<input type="button" onclick="exportModel()" value="下载模板" class="sureBtn"/>	
 				<form id="selectExcel" action="${request.getContextPath()}/admin/NewlyIncreased/list.jhtml" method="post" class="marginL20" style="display: inline-block;*zoom=1;*display:inline;position: relative;">
					<input  name="orgName" value="${name}" id="search" class="inputSty inputOtherCondition" value="" />
					<span class="fuck">请输入重点企业群名称</span>
					<input type="submit" class="sureBtn" id="inquire" value="查  询" style="margin-left:0px"/>
				</form>	
			</div>
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">重点企业群列表</caption>
					<thead>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="150">重点企业群名称</td>
							<td width="150">创建时间</td>
							<td width="150">创建人</td>
							<td width="200">创建机构</td>
							<td width="100">操作</td>
						</tr>
					</thead>
					<tbody>
						<#list list as li>
							<tr>
							<td width="50">${li_index+1}</td>
							<td width="150">${li.sys_importent_enterprise_group_name}</td>
							<td width="150">${li.submit_time?string("yyyy-MM-dd")}</td>
							<td width="150">${li.username}</td>
							<td width="200">${li.sys_org_name}</td>
							<td width="100">
								<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="exportExcel(${li.sys_importent_enterprise_group_id})">导 出</a>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
					<#if (list?? && list?size > 0)>
						<#include "/fragment/paginationbar.ftl"/>
					<#else>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>	
			</div>
		</div>
	</body>
	<script type="text/javascript">
		//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
		
		$(function(){
				
				
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
				
		

			});
	</script>
</html>
