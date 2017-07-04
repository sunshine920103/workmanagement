<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
    	<#include "/fragment/common.ftl"/>
		<title></title>
	</head>
	<script type="text/javascript">
		$(function(){
		
				<#if typeId??>
					<#list typeId as i>
						$("#${i}").attr("checked","checked")
					</#list>
				</#if>
			function  query(){
				var code=$("input[name='code']").val();
				var name=$("input[name='name']").val();
				
				if(BusinessCreditCode(code)==0){
					layer.alert("企业二码输入错误",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				if(checkChineseNoSpe(name)==0) {
					layer.alert("企业名称输入不合法",{icon:2,shade:0.3,shouldClose:true});
	                return false;
		       }
			}
			
		})
		
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
	</script>
	<body class="eachInformationSearch">
	<form id="searchForm" method="post">
	</form>
		<div class="showListBox noBorder">
			<form action="" method="post" action ="${request.getContextPath()}/admin/companyShow/list.jhtml">
				<div class="filter-content showListBox noBorder" id="indexItemDiv">
					<table cellpadding="0" cellspacing="0" style="margin-left: 0px;">
						<caption class="titleFont1 titleFont1Ex">企业标识查询</caption>
						<tr>
							<td class="noBorderL firstTD" width="200">统一社会信用代码/组织机构代码/身份证号</td>
							<td class="secondTD" width="400"><input name="code" type="text" class="inputSty allcorgcodeVal" value="${code}" onblur="onblurVal(this,25,18)"></td>
						</tr>
						<tr>
							<td class="noBorderL firstTD" >企业名称</td>
							<td class="secondTD" ><input name="name" type="text" class="inputSty allnameVal"  onblur="onblurVal(this,13,50)"></td>
						</tr>
						<tr>
							<td class="noBorderL firstTD" >企业状态</td>
							<td>
							<#list dic as di>
								<label for="${di.dicContentId}"><input type="checkbox" class="checke" name="typeId" id="${di.dicContentId}" value="${di.dicContentId}"/>${di.dicContentValue}</label>
							</#list>
							</td>
						</tr>
					</table>
					
				</div>
				<div  class="showBtnBox" style=" border: none;">
					<input type="submit"  value="查 询" class="sureBtn"/> 
					<input 
				onclick="setLayer('新增企业标示','${request.getContextPath()}/admin/comPanyShow/add.jhtml');$('.layui-layer-shade').height($(window).height());"
				type="button" value="新增" class="sureBtn" style="margin-left:30px"/>
				</div>
			</form>
		</div>
		<div>
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<tbody>
							<tr class="firstTRFont">
							<td>统一社会信用代码/组织机构代码/身份证号</td>
							<td>企业名称</td>
							<td>状态</td>
							<td>标识机构</td>
							<td>操作</td>
						</tr>
						<#list list as li>
						<tr>
							<td>${li.codeCredit}</td>
							<td>${li.qymc}</td>
							<td>
								<#if li.dicId??>
									<#list dic as di>
										<#list li.dicId?split(',') as i>
											<#if di.dicContentId == i>
												${di.dicContentValue}
											</#if>
										</#list>
									</#list>
								</#if>
							</td>
							<td class="orgid">${li.sysOrgId}</td>
							<td><a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('企业标识详情','${request.getContextPath()}/admin/comPanyShow/add.jhtml?id=${li.sysComPanyShowId}')">详 情</a></td>
						
						</tr>
						</#list>
					</tbody>
				</table>
			</div>
		</div>
		
				
	<#include "/fragment/paginationbar.ftl"/>
	</body>
</html>
