<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		
		<script type="text/javascript" >
			//回显
			var msg = "${msg}";
			if(msg != "") {
				layer.msg(msg, {
					icon: (msg=="操作成功")?1:2,
					shade: 0.3,
					shadeClose: true
				});
			}
			$(function(){
				setLayDate("#timeval");
				if($("#timeval").val() == ""){
					var myDate = new Date();
					var dateInner = "";
					var needM0 = "";
					var needD0 = "";
					if(myDate.getMonth()+1<10){
						needM0 = "0"
					}
					if(myDate.getDate()<10){
						needD0 = "0"
					}
					
					dateInner = myDate.getFullYear()+"-"+needM0+(myDate.getMonth()+1)+"-"+needD0+myDate.getDate();
					$("#timeval").val(dateInner);
				}
				$("#searchName").focus(function(){
				if($.trim($(this).val())=="请输入重点企业监测群名称"){
					$(this).val("");
				}
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(this).val("请输入重点企业监测群名称");
					}
				});

				$("input[value='查询']").click(function(){
					var searchName = $("#searchName").val();
					var timeval = $("#timeval").val();
					if(timeval=="" || timeval==null){
						layer.confirm("请填写所有查询条件", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
						$("#searchNamePost").val("");
						return false;
					}
	
//					$("#searchNamePost").val(searchName);
//					$("#searchtimeval").val(timeval);
					$("#searchForm").submit();
//					"${request.getContextPath()}/admin/keyEnterpriseQuery/list.jhtml";
				});
				
				var content = $("#searchName").val();
                setFlag($(".redMark"),content);
			});
         	 //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
		</script>
	</head>
	<body class="eachInformationSearch">
		
		<div >
			<form id="searchForm" method="post">
			<div class="queryInputBox marginLR30">
				<div>
					<#if time??&&time!="">
					<span class="paddingR10 fontSize12">归档时间 : </span><input id="timeval" name="time" class="inputSty fontSize12 laydate-icon" value='${time}'>
				<#else>
					<span class="paddingR10 fontSize12">归档时间 : </span><input id="timeval" name="time" class="inputSty fontSize12 laydate-icon">
				</#if>
				</div>
				<br />
			<#if searchName??&&searchName!="">
				<input class="inputKuang" id="searchName" name= "searchName" placeholder="" value='${searchName}' type="text" />
			<#else>
				<input class="inputKuang" id="searchName" name= "searchName" placeholder="" value='请输入重点企业监测群名称' type="text" />
			</#if>
			
			<input type="button" value="查询" class="sureBtn marginL20"/>
	
			</div>
			</form>
			<div class="listBox">
				<table  cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">重点企业监测群结果</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="300">重点企业监测群名称</td>
							<td width="100">上传机构</td>
							<!--<td width="100">行业</td>
							<td width="100">规模</td>-->
							<td width="100">操作</td>
						</tr>
						<#list keyentErprisesSetList as item>
							<tr>
								<td >${(1 + item_index) + page.getPageSize() * (page.getCurrentPage())}</td>
								<td class="redMark">${item.keyentErprisesSetName}</td>
								<td>${item.keyentErprisesSetOrgName}</td>
								<!--<td>${item.keyentErprisesSetTrade}</td>
								<td>${item.keyentErprisesSetSize}</td>-->
								<td>
									<a class="changeFont fontSize12 cursorPointer hasUnderline" 
									onclick="setLayer(' ','${request.getContextPath()}/admin/keyEnterpriseQuery/enterpriseList.jhtml?emid=${item.keyentErprisesSetId}&show=show')"
									>查 询</a> 
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
				<#if (keyentErprisesSetList?? && keyentErprisesSetList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>	    	
				</#if>
			</div>
		</div>
		
	</body>
</html>
