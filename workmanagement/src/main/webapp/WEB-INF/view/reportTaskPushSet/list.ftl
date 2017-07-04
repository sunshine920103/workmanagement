<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <meta charset="UTF-8">
    <title></title>
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
			})
			//状态：禁用或启用
			function updataStatus(obj,id,name,status){
				var tip;
				if($(obj).attr("name")=="启用"){
					tip = "确定要启用 <" + name + "> 吗";
				}else{
					tip = "确定要禁用 <" + name + "> 吗";
				}
				if(confirm(tip,'提示')){
					var url = "${request.getContextPath()}/admin/reportTaskPushSet/updataStatus.jhtml";
					$.post(url,{id:id},function(data){
							if(data.status==0){
								$(obj).text("禁 用");
								$(obj).attr("name","禁用");
								$(obj).parent().prev().text("正常");
								$(obj).css("color","red");
							}else{
								$(obj).text("启 用");
								$(obj).attr("name","启用");
								$(obj).css("color","#339999");
								$(obj).parent().prev().text("禁用");
							}
							
					});
				}
			}
			//删除任务
			function del(obj, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	var url = "${request.getContextPath()}/admin/reportTaskPushSet/del.jhtml";
						$.post(url,{reportTaskPushSetId:id},function(data){
							if(data.flag){
								//删除页面上的数据
								$(obj).parent().parent().remove();
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
				return false;
			}
    </script>
</head>
<body class="eachInformationSearch">
	<form id="searchForm" method="post"></form>
<div class="queryInputBox" style="margin-bottom: 0px;">
    <input type="button" onclick="setLayer('新增任务推送','${request.getContextPath()}/admin/reportTaskPushSet/edit.jhtml');$(this).blur();" value="新增任务推送" class="bigBtn" style="margin-left: 30px;"/>
    <span class="warmFont inlineBlock fontSize12 marginLR30 marginT10">注：任务推送作用于数据报送，任务周期为无时表示是一次性任务，任务周期为每季时表示从当前添加任务时间的本月几日开始计算，按照月加3进行任务触发。</span>
</div>
<div class="listBox">
    <table cellpadding="0" cellspacing="0">
    	<caption class="titleFont1 titleFont1Ex">任务推送列表</caption>
        <tbody>
        <tr class="firstTRFont">
            <td width="80">序号</td>
            <td width="150">任务名称</td>
            <td width="120">任务周期</td>
            <td width="80">状态</td>
            <td width="150">操作</td>
        </tr>
        <#list reportTaskPushSetList as item>
        	<tr>
        	<#--<td>${(1 + ps_index) + page.getPageSize() * (page.getCurrentPage() - 1)}</td>-->
	            <td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
	            <td>${item.reportTaskPushSetName}</td>
	            <td>${item.reportTaskPushSetCycle}</td>
	            <td>
					<#if item.reportTaskPushSetStatus == 0> 
						正常
					<#else>
						 禁用
					</#if>
				</td>
	            <td>
	                <a href="javascript:void(0)" onclick="setLayer('查看任务推送','${request.getContextPath()}/admin/reportTaskPushSet/show.jhtml?reportTaskPushSetId=${item.reportTaskPushSetId}')" class="changeFont fontSize12 cursorPointer hasUnderline">查 看</a>
	                <a href="javascript:void(0)" onclick="setLayer('修改任务推送','${request.getContextPath()}/admin/reportTaskPushSet/edit.jhtml?reportTaskPushSetId=${item.reportTaskPushSetId}')" class="changeFont fontSize12 cursorPointer hasUnderline">修 改</a>
					<a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:void(0)" onclick="return del(this,'${item.reportTaskPushSetId}', '${item.reportTaskPushSetName}')">删除任务</a>
	            </td>
	        </tr>
        </#list>
        
        </tbody>
    </table>
    			<#if (reportTaskPushSetList?? && reportTaskPushSetList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table cellspacing="0" cellpadding="0" class="noBorderT">
						<tr class="firstTRFont">
							<td style="text-align: center;">暂无数据</td>
						</tr>
					</table>
				</#if>
</div>
</body>
</html>