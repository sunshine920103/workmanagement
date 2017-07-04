<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg, {
						icon: (msg=="操作成功")?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
			});
			
			//修改状态
			function updataStatus(obj, id, name, oid){
				var	tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	//关闭确认弹窗
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
			    		var url = "${request.getContextPath()}/admin/reportExcelTemplate/delExcel.jhtml";
						$.post(url,{_:Math.random(),id:id,sysOrgId:oid},function(data){
							var index = alertInfoFun(data.message, data.flag, function(){
								if(data.flag){
									window.location.href = "${request.getContextPath()}/admin/reportExcelTemplate/list.jhtml";
								}else if(!data.flag){
									window.location.href = "${request.getContextPath()}/admin/reportExcelTemplate/list.jhtml";
								}
							});
						});
				  	}
			  	});
			}
			
			$(function(){
				$("#inquire").click(function(){
					var search=$("#search").val();
					if(checkTChineseM(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			    	} 
					$("#selectExcel").submit()
				})
			})
		</script>
		<title>模板设置</title>
	</head>
	<body class="eachInformationSearch">
		<form id="searchForm" method="post">
			<input name="excelName" type="hidden" value="${excelName}" />
		</form>
		<div>
			<div class="queryInputBox" style="margin-bottom:10px">
				<input 
				onclick="setLayer('新增模板','${request.getContextPath()}/admin/reportExcelTemplate/edit.jhtml');$(this).blur();"
				type="button" value="新增模板" class="bigBtn" style="margin-left:30px"/>
				<form id="selectExcel" action="${request.getContextPath()}/admin/reportExcelTemplate/list.jhtml" method="post" class="marginL20" style="display: inline-block;*zoom=1;*display:inline;position: relative;">
					<#-- 当前分组方式, 只用来回显 -->
					<span class="fuck">请输模板名称查询</span>
					<input  name="excelName" id="search" class="inputSty inputOtherCondition" value="${excelName}" />
					<input type="button" id="inquire" class="sureBtn" value="查   询" style="margin-left:0px"/>
				</form>
			</div>
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">模板列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="250">模板名称</td>
							<td width="100">指标大类</td>
							<td width="100">所属区域</td>
							<td width="100">状态</td>
							<td width="150">操作</td>
						</tr>
						<#list ret as item>
                            <#if item.indexName =='基本信息'|| item.indexName =='行政处罚信息'||item.indexName =='行政许可信息'>
							<tr>
	            				<td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
								<td>${item.reportExcelTemplateName}</td>
								<td>${item.indexName}</td>
								<td>${item.reportExcelTemplateAreaName}</td>
								<td>
									<#if item.reportExcelTemplateStatus = 0> 
									  正 常
									 <#else>
									 禁 用
									</#if>
								</td>
								<td>
									<a class="changeFont" href="${request.getContextPath()}/admin/reportExcelTemplate/downLoad.jhtml?id=${item.reportExcelTemplateId}">下 载</a> 
									<a class="delFont" href="javascript:void(0);" onclick="updataStatus(this,'${item.reportExcelTemplateId}','${item.reportExcelTemplateName}','${item.sysOrgId}')">删除</a>
								</td>
							</tr>
                            </#if>
						</#list>
					</tbody>
				</table>
				<#if (ret?? && ret?size > 0)>
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
	</script>
</html>
