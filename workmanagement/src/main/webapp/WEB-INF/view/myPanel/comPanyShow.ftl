<!DOCTYPE html>
<html>
	<head>
		<title>二码信息确认</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body class="showListBox">
	<form id="searchForm" method="post"></form>
		<table cellpadding="0" cellspacing="0">
		  <tr class="firstTRFont">
          <td class="noBorderL" width="50">编号</td>
          <td width="150" colspan="2">说明</td>
      </tr>
      <#list DefaultIndexItemCombine as def>
              <tr>
                  <td class="noBorderL" style="width:6%;">${def_index+1}</td>
                  </td>
	                  <#if def.code_credit_orgid == sessionUser.sys_org_id && def.stuta == 2>
	                  <td>
	                  		当前企业统一社会信用代码:<span class="credit">${def.code_credit_id}</span>已添加组织机构码:<span class="code">${def.code_org_id}</span>
	                  </td>
	                  </#if>
	                  <#if def.code_org_orgid == sessionUser.sys_org_id && def.org_stuta == 2>
	                  <td>	
	                  	 当前组织码:<span class="code" style="color: #ff0000;" >${def.code_org_id}</span>已被统一社会信用代码:<span class="credit" style="color: #ff0000;" > ${def.code_credit_id}</span>合并
	                  </td>
                  </#if>

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
			parent.window.location.href="${request.getContextPath()}/admin/myPanel/myPanel.jhtml";
    		parent.layer.close(index); //执行关闭
    		
		});
	})
	 $(function(){
			var i=0
			$(".code").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getcode.jhtml",{id:orgId},function(data1){
						 $(".code").eq(i).text(data1)
						 i++
					})
			})
 })
$(function(){
			var i=0
			$(".credit").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getcride.jhtml",{id:orgId},function(data1){
						 $(".credit").eq(i).text(data1)
						 i++
					})
			})
 })
	</script>
</html>
