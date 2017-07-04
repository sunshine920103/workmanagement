<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		//回显
		var msg = "${msg}";
		if(msg!= "") {
//			layer.alert(msg,{
//				icon:(msg=="操作成功")?1:2,
//				time:15000,
//				shade:0.3,
//				shadeClose:true
//			});
			alertInfo(msg);
		}
		$("#addM").click(function(){
			window.location.href="${request.contextPath}/admin/manualEntry/add.jhtml";
		})
	});

	//状态：无 效或有 效
	function updataStatus(obj,id,name,status){
		var tip;
		if($(obj).attr("name")=="有 效"){
			tip = "确定要有 效 <" + name + "> 吗";
		}else{
			tip = "确定要无 效 <" + name + "> 吗";
		}
		if(confirm(tip,'提示')){
			var url = "${request.getContextPath()}/admin/manualEntry/updataStatus.jhtml";
			$.post(url,{id:id,reportIndexMethod:2},function(data){
				if(data.status==1){
					$(obj).text("");
					$(obj).parent().prev().text("无效");
				}else{
					alertInfo(data.msg);
				}
			});
		}
	}

//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
	<title>手工修改列表</title>
</head>
<body class="eachInformationSearch">
	<form id="searchForm" method="post"></form>
	<div class="queryInputBox">
		<div class="verticalMiddle"><input id="addM" class="sureBtn" type="button" value="手工修改" style="margin-left: 30px;">
		<span class="warmFont inlineBlock marginL20">注：手工修改是按照指标大类的指标项进行录入操作，只用于修改数据。</span>
		</div>
		<!-- 手工修改列表 -->
		<div class="listBox">
			<table  cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">手工修改列表</caption>
				<tr class="firstTRFont">
					<td width="50">序号</td>
					<td  width="100">指标大类</td>
					<td  width="100">录入机构</td>
					<td width="100">录入时间</td>
					<td width="100">操作</td>
				</tr>
				<#list sysUserLog as sul>
					<tr>
	            		<td>${(1 + sul_index) + (page.getPageSize() * page.getCurrentPage())}</td>
						<td>${sul.indexName}</td>
						<td>${sul.sysUserLogOrgName}</td>
						<td>${(sul.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
						<td>
							<a class="changeFont fontSize12 cursorPointer hasUnderline" href="javascript:void(0);" onclick="setLayer('查看手工修改','${request.getContextPath()}/admin/manualEntry/show.jhtml?sysUserLogId=${sul.sysUserLogId}')">查 看</a>
						</td>
					</tr>			
				</#list>
				<!-- 获取所有手工修改数据 -->
			</table>
			<#if (sysUserLog?? && sysUserLog?size > 0)>
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