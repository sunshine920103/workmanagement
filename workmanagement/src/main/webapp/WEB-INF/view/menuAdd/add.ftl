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
		</script>
		<title>菜单</title>
	</head>
	<body>
		<#-- 新增框 -->
		<div class="showListBox">
			<form id="form"  method="post">			
			<input type="hidden" name ="tp" value="${menu.sys_menu_id}"/>
			<table cellpadding="0" cellspacing="0">
						<caption  class="titleFont1 titleFont1Ex">
							<#if poptype == "add" >新增菜单<#else>修改菜单</#if>
						</caption>
					<tr>
						<td  class="noBorderL firstTD" width="200"><label class="mainOrange"> * </label>菜单名称</td>
						<td width="500" class="secondTD">
							<input id="menuname" name="menuname" type="text"  class="inputSty allnameVal" onblur="onblurVal(this,13,50)" value="${menu.sys_menu_name}" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>上级菜单</td>
						<td class="secondTD" style="position: relative;">
							<select name="shangmenu" class="inputSty">
								<option <#if menu!=null>value="${parent.sys_menu_id}"</#if>>
									<#if menu==null>请选择上级菜单<#else>${parent.sys_menu_name}</#if>
								</option>
								<#list list as li>
									<#if menu!=null>
										<#if parent.sys_menu_id = li.sys_menu_id>
											<option value="${li.sys_menu_id}">${li.sys_menu_name}</option>
										<#else>
											<option value="${li.sys_menu_id}">${li.sys_menu_name}</option>
										</#if>
										<#else>
											<option value="${li.sys_menu_id}">${li.sys_menu_name}</option>
									</#if>
								</#list>
							</select>
						</td>
					</tr>
					<tr> 
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>URL</td>
						<td class="secondTD"> 
								<input id="menuurl" type="text" name="menuurl"  class="inputSty  " value="${menu.sys_menu_path}" />	
						</td>
					</tr>
					<tr> 
						<td class="noBorderL firstTD">权重</td>
						<td class="secondTD"> 
							<input id="weight" type="text" name="weight"  class="inputSty  " value="${menu.sys_menu_weight}" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"/>	注：权重为自然数，自然数越大，菜单则显示在越前面
						</td>
					</tr>
				</table>
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
				</div>
			</form>
		</div>
	</body>
	<script>	
		
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});
				var menuurlold = $("#menuurl").val();
				var menunameold = $("#menuname").val();
				var shangmenuold = $("select[name='shangmenu']").val();
				var weightold = $("#weight").val();
				var regWeight =/[0-9]/;
			$("#submitBtn").click(function(){
				
				var menuurl = $("#menuurl").val();
				var menuname = $("#menuname").val();
				var shangmenu = $("select[name='shangmenu']").val();
				var weight = $("#weight").val();
				
				<#if poptype != "add" >
					if(menuurlold==menuurl&&menunameold==menuname&&shangmenuold==shangmenu&&weightold==weight){
						parent.layer.close(index); //执行关闭
						return false;
					}
				</#if>
				if(menuname=="") {
					layer.alert("菜单名称不能为空",{icon:2,shade:0.3,shouldClose:true});
	              	 return false;
		        }
				if(checkChineseNoSpe(menuname)==0) {
					layer.alert("菜单名称不能为空",{icon:2,shade:0.3,shouldClose:true});
	              	 return false;
		        }
				if(menuurl=="") {
						layer.alert("菜单路径不能为空",{icon:2,shade:0.3,shouldClose:true});
		                return false;
			    }
				if(shangmenu==0) {
					layer.alert("请选择上级菜单",{icon:2,shade:0.3,shouldClose:true});
	                return false;
		        }
				if(weight!=""&&regWeight.test(weight)==false){
					layer.alert("权重格式不正确",{icon:2,shade:0.3,shouldClose:true});
	                return false;
				}
				var loading = layer.load();
				$.post("${request.getContextPath()}/admin/menuAdd/save.jhtml",$("#form").serialize(),function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){
						if(data.flag){     
							layer.load();
							parent.window.location.href = "${request.getContextPath()}/admin/menuAdd/list.jhtml";
						}
						layer.close(index);
					});
				});
			
			});
	</script>	
</html>
