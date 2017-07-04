<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8"> 
	</head>
	<body class="eachInformationSearch">
		<form id="searchForm" method="post">
			<input   name="orgNameOrcode"  type="hidden" value="${orgNameOrcode}"  />
		</form>
		<div class="queryInputBox" style="margin-top: 20px;"> 
			<form id="search" action="${request.getContextPath()}/admin/sysOrg/orgInfoList.jhtml" method="post" class="marginL20" style="display: inline-block;*zoom=1;*display:inline;position: relative;">
				<span class="fuck">请输入机构名或编码</span>
				<input class="inputSty inputOtherCondition" name="orgNameOrcode" type="text" value="${orgNameOrcode}" id="org">
				<input type="button" onclick="subm()" class="sureBtn" style="margin-left: 0px;" value="查 询" />
			</form>
			<input onclick="down()" type="button" value="导出" class="sureBtn sureBtnEx" style="margin-left: 25px;"/>
			<div class="listBox" style="margin-left: 20px;">
				<table cellpadding="0" cellspacing="0" id="dataList">
					<tr class="firstTRFont" >
						<td width="400">机构名称</td>
						<td width="400">编码</td>
					</tr>
					<#list orgList as it>
					<tr>
						<td>${it.sys_org_name}</td>
						<td>${it.sys_org_financial_code}</td>
					</tr>
					</#list>
				</table>
				<#if (orgList?? && orgList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
						<tr class="firstTRFont">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>    	
				</#if>
			</div>
		</div>
	</body>
	<script type="text/javascript"> 
        if ($(".inputOtherCondition").val() != "") {
            $(".fuck").hide();
        }
        $(".inputOtherCondition").focus(function () {
            $(".fuck").hide();
        }).blur(function () {
            if ($.trim($(this).val()) == "") {
                $(".fuck").show();
            }
        });
        $(".fuck").click(function () {
            $(".inputOtherCondition").focus();
        })
        
   
        function subm(){
        	 var search=$("#org").val();
        	if(search=="") {
				 layer.alert("请输入查询内容",{ icon:2, shade:0.3, shouldClose:true }); 
		         return false;
			} 
			 if(checkTChineseM(search)==0) {
				 layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		         return false;
			 } 
			 $("#search").submit()
        } 
		//全部导出
			function down(){
				 var len = $("#dataList tr").length;
	            if (len > 1) {
	               location.href='${request.getContextPath()}/admin/sysOrg/downAll.jhtml?orgNameOrcode=${orgNameOrcode}';
	            } else {
	                layer.alert("没有搜索结果");
	            }
				
			}
	</script>
</html>
