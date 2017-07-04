<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>		
		<script type="text/javascript" >
			
			//删除政府类型
			function del(obj, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/menuAdd/delete.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭弹窗
							layer.close(option_index);
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
		<title>政府部门类型</title>
	</head>
	<body class="eachInformationSearch">
		<form id="searchForm" method="post"></form>
		<div class="queryInputBox">
			<div class="verticalMiddle">
				<input onclick="setLayer('新增菜单','${request.getContextPath()}/admin/menuAdd/add.jhtml');$('.layui-layer-shade').height($(window).height());" type="button" value="新增菜单" class="sureBtn" style="margin-left:30px"/>
			</div>
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">新增菜单列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="200">菜单名称</td>
							<td width="200">URL</td>
							<td width="100">操作</td>
						</tr>
						<#list menu as me>
						<tr>
							<td width="50">${me_index+1}</td>
							<td width="200">${me.sys_menu_name}</td>
							<td width="200">${me.sys_menu_path}</td>
							<td width="100"><a class="changeFont fontSize12 cursorPointer hasUnderline"  onclick="setLayer('修改菜单','${request.getContextPath()}/admin/menuAdd/add.jhtml?id=${me.sys_menu_id}')">修改</a>   <a class="delFont fontSize12 cursorPointer hasUnderline"  onclick="del(this,${me.sys_menu_id},'${me.sys_menu_name}')">删除</a></td>
						</tr>
						</#list>
					</tbody>
					
				</table>
				
				<#if (menu?? && menu?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
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
	</script>
</html>
