<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>	
		<meta charset="UTF-8"> 
	</head>
	<body class="eachInformationSearch">
		<form id="searchForm" method="post">
			<input   name="name"  type="hidden" value="${sysIndustryName}"  />
			<input   name="url"  type="hidden" value="${url}"  />
			<input type=hidden  name="qb" value="aaa"  />
		</form>
		<div class="queryInputBox" id="div1">
		
			<form id="form" action="${request.getContextPath()}/admin/sysClassfy/classifyInfo.jhtml"  method="post" style="display: inline-block;*zoom:1;*display:inline;position:relative;margin-left:20px"  >
					<span class="fuck">请输入行业类别名或行业编码</span>
				    <input class="inputSty inputOtherCondition" type="text" name="name" id="sysclassName"  value="${sysIndustryName}"/>
				    <input type="button"  onclick="subm()" class="sureBtn" value="查  询" style="margin-left:0px" />
				    <input type=hidden  name="qb" value="aaa"  />
			</form> 
			<input onclick="down()" type="button" value="导出" class="sureBtn sureBtnEx" style="margin-left: 25px;"/>
			<div class="listBox" style="margin-left: 20px;">
				<table cellpadding="0" cellspacing="0" id="dataList">
					<tr class="firstTRFont" >
						<td width="400">编码</td>
						<td width="400">行业名称</td>
					</tr>
					<#list sysClassFyModel as it>
						<tr>
							<td>${it.sysIndustryCode}</td>
							<td>${it.sysIndustryName}</td>
						</tr>
					</#list>
				</table>
		<#if (sysClassFyModel?? && sysClassFyModel?size > 0)>
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

<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>	
<script type="text/javascript" > 
				 function subm(){
					var search=$("#sysclassName").val();
				//	if(search=="") {
				//		layer.alert("请输入查询内容",{ icon:2, shade:0.3, shouldClose:true }); 
		      //          return false;
			  //  	} 
					if(checkTChineseM(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			    	} 
					$("#form").submit()
				} 
		//回显
			var msg = "${msg}";
			if(msg!=""){
				layer.alert(msg,{
					icon:("保存成功"==msg?2:1),
					shade:0.3
				});
			}
			$("#upload").submit(function(){
				var fileName=$("#file").val();
				if(fileName == "") {
			        layer.alert("请选择excel文件", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
			        return false;
			   }
				var reg=/(.*).(xls|xlsx)$/; 
				 
				if(!reg.test(fileName)) {
			        layer.alert("文件不是excel格式", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
			        return false;
			    }
				loading = layer.load();
			});  
			
			//关闭错误列表
			$("#closeBut").click(function(){
				$("#errorDiv").hide();
			});
			if($("#sysclassName").val() != ""){
				$(".fuck").hide();
			}
			$("#sysclassName").focus(function(){
				$(".fuck").hide();
			}).blur(function(){
				if($.trim($(this).val())==""){
					$(".fuck").show();
				}
			});
			$(".fuck").click(function(){
				$(".inputOtherCondition").focus();
			}) 
		//全部导出
		function down(){
			
			 var len = $("#dataList tr").length;
	            if (len > 1) {
						window.open('${request.getContextPath()}/admin/sysClassfy/exportAll1.jhtml?name=${sysIndustryName}+&qb=${qb}');
					
	            } else {
	            layer.alert("没有搜索结果");
	            } 

		}
</script>
