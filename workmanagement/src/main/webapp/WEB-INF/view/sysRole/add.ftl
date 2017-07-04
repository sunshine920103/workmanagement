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
	<body style="height:100%">
		<#-- 新增框 -->
		<div  class="showListBox">
			
			<form id="form" action="${request.getContextPath()}/admin/sysRole/save.jhtml" method="post">
				<#-- 角色ID -->
				<input name="sys_role_id" type="hidden" value="${sr.sys_role_id}"/>
				<#-- 我的面板, 默认每个角色都会有 -->
				<input type="checkbox" name="duties" value="1" checked="checked" style="display:none"/>
			
				<table cellpadding="0" cellspacing="0" >
						<caption class="titleFont1 titleFont1Ex">新增角色</caption>
					
					
					<tr>
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>角色名称</td>
						<td width="500" class="secondTD">
							<input id="sys_role_name" name="sys_role_name" class="inputSty required allnameVal" value="${sr.sys_role_name}" onblur="onblurVal(this,13,50)" maxlength="50" title="必填，不能超过50个字"/>
						</td>
					</tr>
					<tr>
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>角色类别</td>
						<td width="500" class="secondTD">
						<select class="inputSty" name="sys_role_type"  id="sys_role_type" onchange="test(this)"> 
								<option></option>
								<#if areaType==1 || areaType==0>
									<option <#if (sr.sys_role_type==1) >selected=selected</#if> value="1" >管理员</option>
									<option <#if (sr.sys_role_type==2) >selected=selected</#if> value="2">查询员</option>
									<option <#if (sr.sys_role_type==3) >selected=selected</#if> value="3" >报送员</option>
								</#if>
								<option <#if (sr.sys_role_type==4) >selected=selected</#if> value="4"  >机构管理员</option>
								<option <#if (sr.sys_role_type==5) >selected=selected</#if> value="5"  >机构查询员</option>
								<option <#if (sr.sys_role_type==6) >selected=selected</#if> value="6"  >机构报送员</option>
								
								<#if areaType==1 || areaType==0>
									<option <#if (sr.sys_role_type==7) >selected=selected</#if> value="7"  >异议处理员</option>
								</#if>	
								<option <#if (sr.sys_role_type==8) >selected=selected</#if> value="8" >机构异议处理</option>
								<option <#if (sr.sys_role_type==9) >selected=selected</#if> value="9" >其他机构查询员</option>
								<option <#if (sr.sys_role_type==10) >selected=selected</#if> value="10" >其他机构报送员</option>
						</select>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<input name="sys_role_notes" class="inputSty fontSize12" value="${sr.sys_role_notes}" onblur="onblurVal(this,14,50)"  maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td style="text-align:center" colspan="2" >角色职责</td>
					
					</tr>
				</table>
				<div id="con">
					<table cellpadding="0" cellspacing="0"  style="margin:0 auto;border-top: none;">
						
					</table>
				</div>
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
				</div>
			</form>
		</div>
	</body>
	<script>
    Array.prototype.contains = function(obj) {
            var i = this.length;
            while (i--) {
                if (this[i] === obj) {
                    return true;
                }
            }
            return false;
        }
	function test(obj){
		var v=$(obj).val()
		$.ajax({
	    	 url:'${request.getContextPath()}/admin/sysRole/getcode.jhtml',
	    	 data:{"lbid":v},
	    	 success:function(result){
	    	str_json=eval("("+result+")");
	    	var con="";
             var menu_array=["指标设置","地区维护","校验管理","EXCEL模板设置","权限管理","用户管理","管理日志","密码管理","其他管理","数据备份","批量导出","任务管理","任务完成情况统计","报文报送","手工修改","已报数据","数据删除","人行异议处理","机构异议处理"];
			$.each(str_json,function(a,b) {
                $('#con table tr').remove();
                if (b.sys_menu_name != '信息查询') {
                    con += "<tr><td width='200'  class='noBorderL firstTD'>" + b.sys_menu_name + "<input type='checkbox' name='duties' value='" + b.sys_menu_id + "'style='display:none' <#if b.checked>checked=true</#if> /></td><td class='secondTD'>";
                    $.each(b.subMenus, function (m, n) {
                        if(menu_array.contains(n.sys_menu_name))
                        {
                            con += "<span class='inlineBlock'><#if sr.menuIds??><input class='subDuties' style='vertical-align:middle;' onchange='selDuties(this)' type='checkbox' name='duties' value='"+ n.sys_menu_id+"' <#if (sub.checked || sr.menuIds?seq_contains("+n.sys_menu_id+"))>checked=true</#if> /><#else><input  class='subDuties' style='vertical-align:middle;' onchange='selDuties(this)' type='checkbox' name='duties' value='"+ n.sys_menu_id+"' <#if (sub.checked)>checked=true</#if> /></#if><span style='margin:5px 20px 5px 0px;font-size:12px;display:inline-block;'>" + n.sys_menu_name + "</span></span>";
                        }
                    });
                }
			});
			con +="</td></tr>";
			
				$('#con table').append(con);
	    	}
	    	 });
	}
				
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		
		$("#submitBtn").click(function(){
			var sys_role_name=$("#sys_role_name")
			var sys_role_type=$("#sys_role_type")
			if(checkChineseNoSpe(sys_role_name.val())==1) { 
				layer.alert("角色名称不能为空", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
                sys_role_name.focus();
                return false;
	        }
			if(checkChineseNoSpe(sys_role_name.val())==0) { 
                layer.alert("角色名称输入不合法", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
                sys_role_name.focus();
                return false;
	        }
			
			if(checkChineseNoSpe(sys_role_type.val())==1) { 
                layer.alert("角色类别不能为空", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
                sys_role_type.focus();
                return false;
	        }
			
			if(checkChineseNoSpe50(sys_role_type.val()) == 0) { 
                layer.alert("角色名称不能为空", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
                sys_role_type.focus();
                return false;
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