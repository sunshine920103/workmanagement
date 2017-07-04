<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		var loading;
		//回显
		var msg = "${msg}"; 
		if(msg!=""){
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
			
			layer.close(loading);
		}
		$("#del").click(function(){
			$(this).parent().remove();
		})
		//时间控件
		$("#timeControler").click(function(){
			setLayDate("#timeControler");
		})
		
		//为模板名称赋初始值
		var myIndexId= "${indexId}";
		if(myIndexId==""){
			$("[name='indexId']").val("${indexTbs[0].indexId}");
		}else{
			$("[name='indexId']").val("${indexId}");
		}
		
		//二码样式，去掉/;
		var twoCode = $("#twoCode").text(); 
		if(twoCode.length==19){		
			$("#twoCode").html(twoCode.substr(0,18));
		}
		if(twoCode.length==11){
			$(".twoCode").html(twoCode.substr(1,10));
		}
		
	});
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
<title>手工录入添加</title>
	</head>
	<body class="showListBox paddingB30 noBorder">
		<table cellpadding="0" cellspacing="0" >
			<caption class="titleFont1 titleFont1Ex">企业数据列表</caption>
			<tr>
				<td width="50">操作</td>
				<td  width="200">统一社会信用代码/组织机构代码/身份证号</td>
				<td  width="100">指标大类</td>
				<td width="100">所属区域</td>
				<td width="100">报送时间</td>
			</tr>
			<#list dataValues as rps>
			<#list rps?keys as itemKey>
			<#if itemKey_index==0>
			<tr>
				<td>
					<a href="${request.getContextPath()}/admin/manualEntry/update.jhtml?indexId=${indexTb.indexId}&majorId=${rps[code]}" class="changeFont fontSize12 cursorPointer hasUnderline" style="text-decoration:underline">修改</a> 
				</td>
				<td  id="twoCode">${rps.CODE_CREDIT}/${rps.CODE_ORG}</td>
				<td >${indexTb.indexName}</td>
				<td >${rps.SYS_AREA_NAME}</td>
				<td>${(rps.SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!}</td>
			</tr>
			</#if>
			</#list>
			</#list>
		</table>
	</body>
</html>