<!DOCTYPE HTML>
<html style="overflow: hidden;">
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
		
		
			//删除用户
			function del(obj, id, name){
				var flag = $(obj).attr("name");			
				sta = flag;
				var tip = "确定要"+(flag=="1"?"停用":"启用")+" <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
				    	var url = "${request.getContextPath()}/admin/sysUser/del.jhtml";
						$.post(url,{_:Math.random(),id:id,statu:sta},function(data){
							//关闭弹窗
							close_wait(option_index);
							
							if(data.flag){
								//删除页面上的数据
								$(obj).attr("name",flag=="1"?0:1);
								var lockType = $(obj).prev().attr("name");
								if(lockType == "1"){
									$(obj).text(flag=="0"?"停 用":"启 用");
									$(obj).parent().parent().prev().text(flag=="0"?"启 用":"停 用"); 
								}else{
									$(obj).text(flag=="0"?"停 用":"启 用");
								}
								
								//$(obj).parent().parent().prev().text(flag=="0"?"启 用":"停 用"); 
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  });
			}	
			$(function(){
				$(".groupList").height($(window).height()-50);
				
				/*
				var content = "${key}";
                setFlag($(".redFlag"),content);
                */
			})
			
			//重置用户密码
			function resetPwd(id, name){
				var tip = "确定要重置 <span class='striking'>"+name+"</span> 的密码吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
				    	var url = "${request.getContextPath()}/admin/sysUser/resetPwd.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭弹窗
							close_wait(option_index);
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
				return false;
			}
            //重置查询次数
			function resetCount(id, name){
				var tip = "确定要重置 <span class='striking'>"+name+"</span> 的查询次数吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
                        close_wait(option_index);
                        layer.alert("重置成功!",{
                            icon:data.flag?1:2,
                            shade:0.3
                        });

				  	}
			  	});
				return false;
			}
			//锁定用户
			function lock(obj, id, name){
		    	var flag = $(obj).attr("name")==1;
				var tip = "确定要"+(flag?"锁定":"解锁")+" <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
				    	var url = "${request.getContextPath()}/admin/sysUser/lock.jhtml";
						$.post(url,{_:Math.random(), id:id, status:!flag},function(data){
							//关闭弹窗
							close_wait(option_index);
							if(data.flag){
								$(obj).attr("name",flag?0:1);
								var onType = $(obj).next().attr("name");
								if(onType=="1"){
									$(obj).text(!flag?"锁 定":"解 锁");
									$(obj).parent().parent().prev().text(!flag?"启 用":"停 用");
								}else{
									$(obj).text(!flag?"锁 定":"解 锁");
								}
									
								
								
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
			
			$(function(){
				$(".userid").each(function(){
					var str=$(this).val();
					$.post("${request.getContextPath()}/admin/sysUser/getUserOrg.jhtml",{id:str},function(data){
						
						if(data!=${sessionUser.sys_org_id}){
							$("."+str+"A").show();
							
						}else{
							$("."+str+"").show();
						}
					})
				}) 
			})
		</script>
		<title>用户管理列表</title>
	</head>
	<body>
		<form id="searchForm" method="post">
		    <input type="hidden" name="roleName" value="${roleName}" />
		    <input type="hidden" name="orgName"   value="${orgName}" />
		    <input type="hidden" name="key"  value="${key}" />
		</form>
		<div class="userManageBox" >
			<div class="rightList floatLeft width100 eachInformationSearch" style="margin-top:0px">
				<div class="listBox hide">
					<div class="marginTB10">
						<#-- 分页查询需要使用该表单 -->
						<form id="searchForm" method="post" action="${request.getContextPath()}/admin/sysUser/list.jhtml" target="myFrameName">
							<#-- 分组方式 -->
							<input type="hidden" name="method" id="method" value="${method}"/>
							<#-- 分组列表 -->
							<input type="hidden" name="mid" id="mid" value="${mid}"/>
							<#-- 当前分组方式, 只用来回显 -->
							<input type="hidden" name="methodTextVal" id="methodTextVal" value="${methodTextVal}"/>
							<input id="key" name="key" class="inputSty" value="" />
							<input type="submit" class="smallBtn" value="查  询" />
						</form>
						<p class="warmFont fontSize12 marginTB1020">注：锁定状态的用户将无法登陆系统，对用户重置后，才能正常使用；用户默认和重置后的密码为8个6。</p>
					</div>
				</div>
				
				<div class="listBox" style="margin-left:0px;margin-right:0px;">
					<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">用户列表<caption>
						<thead>
							<tr class="firstTRFont">
								<td width="100">登录名</td>
								<td width="100">姓名</td>
								<td width="100">角色</td>
								<#--<td width="200">机构</td>-->
								<td width="50">状态</td>
								<td width="400">操作</td>
							</tr>
						</thead>
						<tbody>
							<#list sus as su>
								<tr>
									<td class="redFlag"><span class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看用户','${request.getContextPath()}/admin/sysUser/edit.jhtml?id=${su.sys_user_id}');$('.layui-layer-shade').height($(window).height());">${su.username}</span></td>
									<td class="redFlag">${su.sys_user_name}</td>
									<td>${su.sys_user_role_name}</td> 

									<#--<td>${su.sys_user_org_name}</td> -->
									<td><#if su.sys_delete==1 && su.enabled >启 用<#else>
										<#if su.sys_delete==0>
											停 用
										</#if>
										<#if !su.enabled>
											停 用
										</#if>
										</#if>
										</td>
									<td>
										<#if sr.sys_role_id!=1>
											<div class="${su.sys_create_user_id} hide">
												<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看','${request.getContextPath()}/admin/sysUser/add.jhtml?posi=2&id=${su.sys_user_id}');$('.layui-layer-shade').height($(window).height());">修 改</a>
												<a class="delFont fontSize12 hasUnderline cursorPointer" name="${su.enabled?string(1,0)}" href="javascript:" onclick="return lock(this, ${su.sys_user_id}, '${su.sys_user_name}')">${su.enabled?string("锁 定","解 锁")}</a>
												<a class="delFont fontSize12 hasUnderline cursorPointer" name="${su.sys_delete}" href="javascript:" onclick="return del(this, ${su.sys_user_id}, '${su.sys_user_name}')"><#if su.sys_delete==1>停用<#else>启用</#if></a>
												<a class="delFont fontSize12 hasUnderline cursorPointer" href="javascript:" onclick="return resetPwd(${su.sys_user_id}, '${su.sys_user_name}')">重置密码</a>
												<a class="delFont fontSize12 hasUnderline cursorPointer" href="javascript:" onclick="return resetCount(${su.sys_user_id}, '${su.sys_user_name}')">重置查询次数</a>

												<input type="hidden" value="${su.sys_create_user_id}" class="userid" />
											</div>
											<div class="${su.sys_create_user_id}A hide">
												<span class=" fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">修 改</span>
												<span class="  fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">${su.enabled?string("锁 定","解 锁")}</span>
												<span class="  fontSize12 hasUnderline " style="color:#787878;text-decoration:none;"><#if su.sys_delete==1>停 用<#else>启 用</#if></span>
												<span class="  fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">重置密码</span>
												<span class="  fontSize12 hasUnderline " style="color:#787878;text-decoration:none;">重置查询次数</span>
											</div>
										</#if>
									</td>
								</tr>
							</#list>
							
							<input type="hidden" value="${sus?size}" id="size" />
						</tbody>
					</table>
					<#if (sus?? && sus?size > 0)>
						<#include "/fragment/paginationbar.ftl"/>
					<#else>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
		</script>
	</body>
		
</html>
