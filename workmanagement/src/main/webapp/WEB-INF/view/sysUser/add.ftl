
﻿<!DOCTYPE HTML>
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
			//显示上级区域弹出框
			function openPop(num){
				if(num==0){
					$(".xzjg").hide();
					$(".xzjs").show();
					
				}else if(num==1){
					if($("#useradd").val()==""){
						layer.alert("请先选择角色", {icon: 2,shade: 0.3,shadeClose: true});
						return false
					}else{
					$(".xzjs").hide();
					$(".xzjg").show();
					}
				}
				$("#covered").show();
				$("#poplayer").show();
			}
			
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			function selUpstream(obj){
				$.each($(" .seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
		
		
			function confirmSel2(clear){
				var seleced = $(".xzjg .seleced");
				
					closePop();
					var area = $(seleced.find("label")).text();
					var str = $(seleced).attr("id");
					if(clear==1||seleced.length==0){
						$("#openPop1").text("请选择机构");	
						$("#sys_user_org_val").val("");
						$("#sys_user_org_id").val("");
					}else{
						$("#openPop1").text(area);
						$("#sys_user_org_val").val(area);
						$("#sys_user_org_id").val(str);
					}
				
			}
			//确认选择
			function confirmSel1(clear){
			
				var seleced = $(".xzjs .seleced");
				var typeid=$(".xzjs .seleced .typeId").val();
					closePop();
					 var area = $(seleced.find("label")).text();
					  var str = $(seleced).attr("id");
					if(clear==1||seleced.length==0){
						$("#openPop0").text("请选择角色");
						$("#sys_user_role_val").val("");
						$("#sys_user_role_id").val("");
						$("#useradd").val("");
						
					}else{
						$("#openPop1").show();
						$(".munstr").html('');
						$("#openPop1").text("请选择机构");	
						$("#sys_user_org_val").val("");
						$("#sys_user_org_id").val("");
						$("#openPop0").text(area);
						$("#sys_user_role_val").val(area);
						$("#sys_user_role_id").val(str);
						$("#useradd").val(str);
						if(typeid==9||typeid==10){
							$("#gover").show();
							$("#org").hide();
							$("#org1").hide();
						}else if(typeid==4||typeid==1){
							$.post("${request.getContextPath()}/admin/sysUser/getRoleId.jhtml",{id:${sessionUser.sys_role_id}},function(data){
						
								if(data=='1'&&typeid==1){
									$("#gover").hide();
									$("#org").show();
									
									$("#org1").hide();
									
								}else if(data=='1'&&typeid==4){
									$("#gover").hide();
									$("#org").hide();
									
									$("#org1").show();
								}else{
									$("#gover").hide();
									$("#org").show();
									$("#org1").hide();
									
								}
							});
					}else{
						
						$("#openPop1").hide();
						$("#sys_user_org_val").val("${org.sys_org_name}");
						$("#sys_user_org_id").val("${org.sys_org_id}");
						$(".munstr").html(" ${org.sys_org_name}")
						
					}
					
				}
			}

		 
        

		</script>
		<title>用户管理添加查看修改</title>
	</head>
	
	<body>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="hide borderBox xzjs">
	        	<div class="titleFont1">
	        		<span>角色列表</span>
        		</div>
	        	<div class="listBox">
	        		<!--下面新添了一个table，js在最下面-->
	        		<table cellpadding="0" cellspacing="0" id="searchTableRole">
						<#list srs as i>
			                <tr>
			                    <td level="1" id="${i.sys_role_id}" onclick="selUpstream(this)">
			                    <input type ="hidden" class="typeId" value="${i.sys_role_type}">
			                        <label>${i.sys_role_name}</label>
			                    </td>
			                </tr>
						</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	        		<input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
	        		<input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
	        	</div>
        	</div>
	        <div class="borderBox  xzjg">
	        	<div class="titleFont1">
	        		<span>机构列表</span>
        		</div>
	        	<div class="listBox hide" id="org" >
	        		<div>
		        		<input id="searchInput" class="inputSty" type="text" value="" style="width: 150px;"/>
		        		<input   type="button" value="搜 索" class="sureBtn searchBtn" style="width: 65px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
	        		<table cellpadding="0" cellspacing="0" class="tablehid" >
						<#list orgKey as i>
							<#if i.sys_org_type !=1 && i.sys_org_id!= org.sys_org_id>
								<tr>
									<td id="${i.sys_org_id}"  onclick="selUpstream(this, ${i.sys_org_id})" > 
										<label class="fontSize12 pointer">${i.sys_org_name}</label>
									</td>
								</tr>
							</#if>
						</#list>
					</table>
	        	</div>
	        	<div class="listBox hide" id="org1" >
	        		<div>
		        		<input id="searchInput" class="inputSty" type="text" value="${orgName}" style="width: 150px;"/>
		        		<input    type="button" value="搜 索" class="sureBtn searchBtn" style="width: 65px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
	        		<table cellpadding="0" cellspacing="0" class="tablehid" >
						<#list is as i>
							<#if i.sys_org_type !=1 && i.sys_org_id!= org.sys_org_id>
								<#if org.sys_org_id != i.sys_org_upid>
									<tr>
										<td id="${i.sys_org_id}"  onclick="selUpstream(this, ${i.sys_org_id})" > 
											<label class="fontSize12 pointer">${i.sys_org_name}</label>
										</td>
									</tr>
								</#if>
							</#if>
						</#list>
					</table>
	        	</div>
	        	<div class="listBox hide" id="gover">
	        		<div>
		        		<input id="searchInput" class="inputSty" type="text" value="${orgName}" style="width: 150px;"/>
		        		<input   type="button" value="搜 索" class="sureBtn searchBtn" style="width: 65px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
	        		<table cellpadding="0" cellspacing="0" class="tablehid" >
						<#list is as i>
							<#if i.sys_org_type ==1>
				                <tr>
				                    <td  id="${i.sys_org_id}" onclick="selUpstream(this)">
				                        <label>${i.sys_org_name}</label>
				                    </td>
				                </tr>
				             </#if>
						</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	        		<input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
	        		<input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
	        	</div>
	        </div>
	    </div> 
		<#-- 新增框 -->
		<div class="showListBox"> 
			<form id="userForm" name="form" action="${request.getContextPath()}/admin/sysUser/save.jhtml" method="post"> 
				<input name="sys_user_id" type="hidden" value="${su.sys_user_id}"/>
				<input name="posi" type="hidden" value="${posi}"/>
				
				<table class="centerTable" cellpadding="0" cellspacing="0">
					<#if su == null>
						<caption class="titleFont1 titleFont1Ex">新增用户</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改用户</caption>
					</#if>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>登录名</td>
						<td style="width:65%;" class="secondTD">
							<#if su == null>
						<input id="username" name="username" class="inputSty allnamezmVal " value=""  onblur="onblurVal(this,-2,20)" maxlength="20"/>
							<#else>
								${su.username}
									<input id="username" name="username" type="hidden"   value="${su.username}"  />
								
							</#if>
							
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>姓名</td>
						<td class="secondTD">
							<input id="sys_user_name" name="sys_user_name" class="inputSty" value="${su.sys_user_name}" onblur="onblurVal(this,13,50)" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>身份证号码</td>
						<td class="secondTD">
							<input id="sys_user_card" name="sys_user_card" maxlength="18" class="inputSty allidcardVal" value="${su.sys_user_card}" onblur="onblurVal(this,5,0)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>手机号</td>
						<td class="secondTD">
							<input id="sys_user_contacts" name="sys_user_contacts" maxlength="11" class="inputSty " value="${su.sys_user_contacts}" onblur="onblurVal(this,19,0)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">办公电话</td>
						<td class="secondTD">
							<input id="sys_user_phone" name="sys_user_phone" class="inputSty alltalVal" maxlength="20" value="${su.sys_user_phone}" onblur="onblurVal(this,3,0)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>角色</td>
						<td class="secondTD">
												
							<a id="openPop0" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(0)">
							<#if su.sys_user_role_name!=null>
								${su.sys_user_role_name}
							<#else>
								请选择角色
							</#if>
							</a>
							<input id="sys_user_role_val" name="sys_user_role_name" type="hidden" value="${su.sys_user_role_name}"/>
							<input id="sys_user_role_id" name="sys_user_role_id" type="hidden" value="${su.sys_role_id}"/>	
							<!--选择机构必须先选择角色参数传递-->
							<input id="useradd"  type="hidden" value=""/>	
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>所在机构</td>
						<td class="secondTD">
							<input id="sys_user_org_val" name="sys_user_org_name" type="hidden" value="${su.sys_user_org_name}"/><input id="sys_user_org_id" name="sys_user_org_id" type="hidden" value="${su.sys_org_id}"/>							
							<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">
							<#if su.sys_user_org_name!=null>
								${su.sys_user_org_name}
							<#else>
								请选择机构
							</#if>
							</a>
							<span class='munstr'></span>
						</td>
					</tr>
					
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sys_user_notes" type="text" style="border: 1px solid #dadada;" class="textareaSty fontSize12" onblur="onblurVal(this,14,50)" maxlength="50">${su.sys_user_notes}</textarea>
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
		
			//搜索按钮
			
			
			$(".searchBtn").click(function(){
				$(".search").remove(".search");
				var  label = $(this).parent().parent().find("label"); 
				var  tablehid = $(this).parent().parent().find(".tablehid"); 
				var  orgid = label.parent().attr("id")
					 tablehid.hide();
				 	 $(this).parent().parent().append("<table class='search'></table>") 
				var  num=[];  
				for (var i = 0; i < label.length; i++) {
					var obj= new Object();
						obj.id= label.eq(i).parent().attr("id");
						obj.name= label.eq(i).text(); 
						num.push(obj);  
				}  ;
				var val= $(this).parent().children("input[type='text']").val()
				var html=""
				for (var i = 0; i < num.length; i++) {
					var str=num[i].name;;
					if(str.indexOf(val) != -1){
						html+=" <tr> <td id="+num[i].id +" onclick='selUpstream(this)'>  <label>"+num[i].name+"</label> </td> </tr>"
					}
				} 
				$(".search").append(html);
			}) 
		
		
		$("#submitBtn").click(function(){
				var code = $.trim($("#username").val());
				var name = $.trim($("#sys_user_name").val());
				var id_card = $.trim($("#sys_user_card").val());
				var contacts = $.trim($("#sys_user_contacts").val());
				var insti = $.trim($("#sys_user_org_id").val());
				var role = $.trim($("#sys_user_role_id").val());
				var phone = $.trim($("#sys_user_phone").val());
				 
			if(checkName(code)==1) {
				layer.alert("登录名不能为空", {icon: 2,shade: 0.3,shadeClose: true});
				$("#username").focus();
                return false;
	        }
			if(checkName(code)==0) {
				layer.alert("登录名输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
				$("#username").focus();
                return false;
	       	} 
			if(checkChineseNoSpe(name)==1) {
				layer.alert("姓名不能为空", {icon: 2,shade: 0.3,shadeClose: true});
				$("#sys_user_name").focus();
                return false;
	        }
			if(checkChineseNoSpe(name)==0) {
				layer.alert("姓名输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
				$("#sys_user_name").focus();
                return false;
	        }
			if(checkIDCard(id_card)==1) {
				layer.alert("身份证号码不能为空", {icon: 2,shade: 0.3,shadeClose: true});
				$("#sys_user_card").focus();
                return false;
	        }
			if(checkIDCard(id_card)==0) {
				layer.alert("身份证号码输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
                $("#sys_user_card").focus();
                return false;
	       }
			if(checkPhone(contacts)==1) {
				layer.alert("手机号不能为空", {icon: 2,shade: 0.3,shadeClose: true});
				 $("#sys_user_contacts").focus();
                return false;
	        }
			if(checkPhone(contacts)==0) {
				layer.alert("手机号输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
				 $("#sys_user_contacts").focus();
                return false;
	        }
			if(phone != "" && checkTel(phone) == 0) {
				layer.alert("办公电话输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
				$("#sys_user_phone").focus();
                return false;
	        }
			if($("#sys_user_role_val").val()== "") {
				layer.alert("请选择角色",{icon: 2,shade: 0.3,shadeClose: true});
                return false;
	       	}
			
			if($("#sys_user_org_val").val() == "") {
				layer.alert("请选择所在机构", {icon: 2,shade: 0.3,shadeClose: true});
                return false;
	        }
			
			
			if($("textarea[name='sys_user_notes']").val() != "" && checkChineseNoSpe50($("textarea[name='sys_user_notes']").val()) == 0) {

				layer.alert("备注输入不合法", {icon: 2,shade: 0.3,shadeClose: true});
				return false;
	        }
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/sysUser/save.jhtml",$("#userForm").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						if("${posi}"=="1"){
							parent.window.location.href = "${request.getContextPath()}/admin/sysUser/index.jhtml";
						}else{
							parent.window.location.href = "${request.getContextPath()}/admin/sysUser/list.jhtml";
						}
					}
					layer.close(index);
				});
			});
		});
		
	</script>
</html>
