<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			//删除地区
			function del(obj, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	var url = "${request.getContextPath()}/admin/sysRole/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
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
		<title>权限管理</title>
	</head>
	<body class="eachInformationSearch" >
		<form id="searchForm" method="post"></form>
		<div>
			<div class="queryInputBox verticalMiddle" style="margin-bottom:10px">
				<input onclick="setLayer('新增角色','${request.getContextPath()}/admin/sysRole/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"
				type="button" value="新增角色" class="sureBtn" style="margin-left:30px"/>
				<span class="warmFont inlineBlock fontSize12 marginL20">注：列表中角色职责一行显示不全时，请将鼠标移动上去会有完整的角色职责内容提示；同时，无法删除已使用的角色。</span>
			</div>
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">角色列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="100">角色名称</td>
							<td width="200">角色职责</td>
							<td width="100">备注</td>
							<td width="100">操作</td>
						</tr>
						<#list srs as sr>
							<tr>
								<td>${sr_index + 1}</td>
								<td>${sr.sys_role_name}</td>
								<td style="line-height: 24px;" title="${sr.sys_role_duties}">
									${sr.sys_role_duties}
								</td>
								<td>${sr.sys_role_notes}</td>
								<#if role==1 && roleId==1>
									<#if (sr_index + 1) gt 10>
										<td>
											<a class="changeFont fontSize12 cursorPointer hasUnderline"
											onclick="setLayer('修改角色','${request.getContextPath()}/admin/sysRole/update.jhtml?id=${sr.sys_role_id}');$('.layui-layer-shade').height($(window).height());$(this).blur;"
												>修 改</a>
											<a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${sr.sys_role_id}, '${sr.sys_role_name}')">删 除</a>
										</td>
									<#else>
										<td>
											<a class="changeFont fontSize12 cursorPointer hasUnderline"
											onclick="setLayer('修改角色','${request.getContextPath()}/admin/sysRole/update.jhtml?id=${sr.sys_role_id}');$('.layui-layer-shade').height($(window).height());$(this).blur;"
												>修 改</a>
										</td>
								</#if>
								<#else>
								<#if (sr_index + 1) gt 10>
									<td>
										<a class="changeFont fontSize12 cursorPointer hasUnderline"
										onclick="setLayer('修改角色','${request.getContextPath()}/admin/sysRole/update.jhtml?id=${sr.sys_role_id}');$('.layui-layer-shade').height($(window).height());$(this).blur;"
											>修 改</a>
										<a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${sr.sys_role_id}, '${sr.sys_role_name}')">删 除</a>
									</td>
								<#else>
									<td></td>
								</#if>
								</#if>
							</tr>
						</#list>
					</tbody>
				</table>
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
