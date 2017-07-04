<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;IE=EDGE">
<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg!=""){
					layer.alert(msg,{
						icon:("保存成功"==msg?1:2),
						shade:0.3
					});
				}
			})
		
			
			function ckeckName(obj,str1,str2){
				if(str1 == str2){
					$(obj).attr("checked","checked")
				}
			}
			//选择职责
			function selDuties(obj){
				obj = $(obj);
				var selNum = obj.parent().parent().find("input[class=subDuties]:checked").length;
				if(selNum > 0){
					obj.parent().parent().prev().find("input:checkbox")[0].checked = true;
				}else{
					obj.parent().parent().prev().find("input:checkbox")[0].checked = false;
				}
			}
		</script>
		<title>新增/修改角色</title>
	</head>
	<body  style="height:100%">
		<#-- 新增框 -->
		<div  class="showListBox">
			
			<form id="form" action="${request.getContextPath()}/admin/sysRole/save.jhtml" method="post">
				<#-- 角色ID -->
				<input name="sys_role_id" type="hidden" value="${sr.sys_role_id}"/>
				<#-- 我的面板, 默认每个角色都会有 -->
				<input type="checkbox" name="duties" value="1" checked="checked" style="display:none"/>
			
				<table cellpadding="0" cellspacing="0" id="sys_role">
					
						<caption class="titleFont1 titleFont1Ex">修改角色</caption>
					<tr>
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>角色名称</td>
						<td width="500" class="secondTD">
							<input id="sys_role_name" name="sys_role_name"  disabled="disabled"  class="inputSty required allnameVal" value="${sr.sys_role_name}" onblur="onblurVal(this,13,50)" title="必填，不能超过50个字" maxlength="50"/>
							<input id="sys_role_name" name="sys_role_name"  type="hidden"  class="inputSty required" value="${sr.sys_role_name}" onblur="onblurVal(this,13,50)" title="必填，不能超过50个字" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>角色类别</td>
						<td width="500" class="secondTD">
								<#if sr.sys_role_type==1>管理员
								<#elseif sr.sys_role_type==2>查询员
								<#elseif sr.sys_role_type==3>报送员
								<#elseif sr.sys_role_type==4>机构管理员
								<#elseif sr.sys_role_type==6>机构报送员
								<#elseif sr.sys_role_type==7>异议处理员
								<#elseif sr.sys_role_type==5>查询员
								<#elseif sr.sys_role_type==9>其他机构查询员
								<#elseif sr.sys_role_type==10>其他机构报送员
								<#else >金融机构机构异议处理
								</#if>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<input name="sys_role_notes" class="inputSty fontSize12" value="${sr.sys_role_notes}" onblur="onblurVal(this,14,50)" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td style="text-align:center" colspan="2" >角色职责</td>
					</tr>
					</table>
					
					<table id="con" cellpadding="0" cellspacing="0" style="margin:0 auto;border-top: none;">
					<#list sms as sm>
                            <#if sm.sys_menu_name != "信息查询">
								<tr>

                                    <td class="noBorderL firstTD" width="200" >
                                        ${sm.sys_menu_name}
                                        <input type="checkbox" name="duties" value="${sm.sys_menu_id}" style="display:none" <#list smsMenuChild as sm1> <#if (sm1.sys_menu_name==sm.sys_menu_name)>checked="checked"</#if> </#list> />
                                    </td>

								<td class="secondTD" >
									<#list sm.subMenus as sub>
                                        <#if   sub.sys_menu_name=="指标设置" || sub.sys_menu_name=="地区维护" || sub.sys_menu_name=="校验管理" || sub.sys_menu_name=="EXCEL模板设置" || sub.sys_menu_name=="权限管理" || sub.sys_menu_name=="用户管理" || sub.sys_menu_name=="管理日志" || sub.sys_menu_name=="密码管理" || sub.sys_menu_name=="其他管理" || sub.sys_menu_name=="数据备份" || sub.sys_menu_name=="批量导出" || sub.sys_menu_name=="任务管理" || sub.sys_menu_name=="任务完成情况统计" || sub.sys_menu_name=="报文报送" || sub.sys_menu_name=="手工修改" || sub.sys_menu_name=="已报数据" || sub.sys_menu_name=="数据删除" || sub.sys_menu_name=="人行异议处理" || sub.sys_menu_name=="机构异议处理" >
                                            <span class="inlineBlock">
                                                <#if sr.menuIds??>
                                                    <input class="subDuties" style="vertical-align:middle;"  type="checkbox" name="duties" value="${sub.sys_menu_id}"  <#list smsMenuChild as sm1>  <#if (sub.checked || sr.menuIds?seq_contains(sub.sys_menu_id))>checked=true</#if> </#list>/>
                                                <#else>
                                                    <input class="subDuties" style="vertical-align:middle;" onchange="selDuties(this)" type="checkbox" name="duties" value="${sub.sys_menu_id}"  <#list smsMenuChild as sm1> <#if (sm1.sys_menu_name==sub.sys_menu_name)>checked="checked"</#if> </#list>/>
                                                </#if>
                                                <span style="margin:5px 20px 5px 0px;font-size:12px;display:inline-block;">${sub.sys_menu_name}</span>
                                            </span>
                                        </#if>
									</#list>
								</td>
							</tr>
                        </#if>
					</#list>
				</table>
				
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
				</div>
			</form>
		</div>
	</body>
	<script>
	function test(val){
		$.ajax({
			url:'${request.getContextPath()}/admin/sysRole/getcode.jhtml',
			data:{"lbid":val.value},
				success:function(d){
			    	eval("var d="+d);
			    	$("#sys_org_type_code").val(d);
			    }
		});
	}
				
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		
		$("#submitBtn").click(function(){
//			if(checkChineseNoSpe(form.sys_role_name.value)==1) {
//				layer.confirm("角色名称不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
//                form.sys_role_name.focus();
//                return false;
//	        }
//			if(checkChineseNoSpe(form.sys_role_name.value)==0) {
//				layer.confirm("角色名称输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
 //               form.sys_role_name.focus();
 //               return false;
//	        }
			if(form.sys_role_notes.value != "" && checkChineseNoSpe50(form.sys_role_notes.value) == 0) {
				layer.confirm("备注输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
                form.sys_role_notes.focus();
                return false;
	        }
			
//			if(checkChineseNoSpe(form.sys_role_type.value)==1) {
//				layer.confirm("角色职责不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
//                form.sys_role_type.focus();
//                return false;
//	        }
			
			//选择职责
			function selDuties(obj){
				obj = $(obj);
				var selNum = obj.parent().parent().find("input[class=subDuties]:checked").length;
				if(selNum > 0){
					obj.parent().parent().prev().find("input:checkbox")[0].checked = true;
				}else{
					obj.parent().parent().prev().find("input:checkbox")[0].checked = false;
				}
			}
			
			
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/sysRole/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						parent.window.location.href = "${request.getContextPath()}/admin/sysRole/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
		
	})
		
	</script>
</html>
