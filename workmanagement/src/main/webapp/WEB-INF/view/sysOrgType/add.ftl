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
			});
			//显示弹出框
			function openPop(type){
				$.each($("#poplayer .hide"),function(i,v){
					$(v).hide();
				});
				$.each($("#"+type).find("img"),function(i,v){
					$(v).show();
				});
				$("#covered").show();
				$("#poplayer").show();
				$("#"+type).show();
				
				//判断是否显示重置按钮
				var cancelBtn = $("#cancel");
				var resetBtn = $("#reset");
				if(type=="sjjg"){
					cancelBtn.css("margin-right","0px");
					resetBtn.show();
					resetBtn.unbind().click(function(){
						clearSel('sjjgA', '选择上级机构')
					});
				}else if(type=="zzqy"){
					cancelBtn.css("margin-right","0px");
					resetBtn.show();
					resetBtn.unbind().click(function(){
						clearSel('zzqyA', '选择区域')
					});
				}else if(type=="jgdz"){
					cancelBtn.css("margin-right","20px");
					resetBtn.hide();
				}else{
					cancelBtn.css("margin-right","20px");
					resetBtn.hide();
				}
			}
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			
			//选择上级机构类别
			function selUpstream(obj){
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
			//确认选择
			function confirmSel(clear){
				var seleced = $(".seleced");
				var level = parseInt($(seleced[0]).attr("level"));
				closePop();
				var area = $(seleced.find("label")).text();
				if(clear===1){
					$("a[name='openPop']").text("选择上级机构类别");
					$("#sys_org_type_upid").val("");
					$("#sys_org_type_upname").val("");			
				}else{
					$("a[name='openPop']").text(area);
					$("#sys_org_type_upid").val(seleced.attr("id"));
					$("#sys_org_type_upname").val(area);
				}
			}
			function openArea(obj, id, level){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("id")==0){ //未展开
					//显示等待弹窗
					var waitIndex = wait();
					//设置为展开状态
					obj.attr("id",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					//获取父区域的tr
					var ptr = obj.parent().parent();
					//获取机构类型信息的接口地址
					var url = "${request.getContextPath()}/admin/sysOrgType/getType.jhtml";
					
					$.get(url,{_:Math.random(),id:id},function(result){
						//关闭等待弹窗
						layer.close(waitIndex);
						
						if(result!=null){
							var subs = result.subSysOrgType;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subSysOrgType!=null && sub.subSysOrgType.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sys_org_type_id+','+(level+1)+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								
								var tr = $("<tr name='"+sub.sys_org_type_id+"' class='"+id+"'></tr>");
								var name = "<td level='"+(level+1)+"' id='"+sub.sys_org_type_id+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label>"+sub.sys_org_type_name+"</label>"
											+"</td>";
								
								tr.append(name);
								
								ptr.after(tr);
							}
						}
					});
				}else{ //已展开
					//设置为收缩状态
					obj.attr("id",0);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",20);
					//删除子区域
					delSubArea(id);
				}
			}
			
			//删除子区域
			function delSubArea(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();

					//递归删除子区域
					delSubArea(pid);
				});
			}
			function category00(num,org){
				$("#category a").remove();
				str='"'+org+'"'
				if(num==0){
					$("#category").append("<a id='jglbA' name='openPop' style='color:rgb(56,165,226);padding:5px;font-size:12px' onclick='openPop("+str+")'>${(it.sys_org_type_id??)?string(it.sys_org_type_name,'选择上级机构类别')}</a>");
				}else if(num==1){
					$("#category").append("<a id='zflbA' name='openPop' style='color:rgb(56,165,226);padding:5px;font-size:12px' onclick='openPop("+str+")'>${(it.sys_org_type_id??)?string(it.sys_org_type_name,'选择上级机构类别')}</a>");
				}
			}
		</script>

	</head>
	<body>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
		    <div id="jglb" class="hide borderBox" >
		    	<div class="titleFont1">机构类别及总部列表</div>
		    	<div class="listBox" style="border-bottom: none;">
		    		<table cellpadding="0" cellspacing="0" id="searchTable1">
						<#list its as it>
						<tr>
							<td level="1" id="${it.sys_org_type_id}" onclick="selUpstream(this)">
								<#if (it.subSysOrgType?? && it.subSysOrgType?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${it.sys_org_type_id}, 1)">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label>${it.sys_org_type_name}</label>
							</td>
						</tr>
						</#list> 
						
					</table>
		    	</div>
	    	</div>
			<div id="zflb" class="hide borderBox" >
		    	<div class="titleFont1">机构类别及总部列表</div>
		    	<div class="listBox" style="border-bottom: none;">
		    		<table id="searchTable2">
						<#list gov as it>
						<tr>
							<td level="1" id="${it.sys_org_type_id}" onclick="selUpstream(this)">
								<#if (it.subSysOrgType?? && it.subSysOrgType?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${it.sys_org_type_id}, 1)">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label>${it.sys_org_type_name}</label>
							</td>
						</tr>
						</#list> 
						
					</table>
		    	</div>
			</div>
			<div class="btnBox">
	    		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	    		<input type="button" value="重 置" class="resetBtn" onclick="confirmSel(1)"/>
	    		<input type="button" value="确 认" class="button sureBtn" onclick="confirmSel()"/>
    		</div>
	    </div>
		<#-- 新增框 -->
		<div class="showListBox">
			
			<form id="form"  method="post">
			
				<input name="sys_org_type_id" type="hidden" value="${it.sys_org_type_id}"/>
				
				<table cellpadding="0" cellspacing="0">
					<#if it == null>
						<caption class="titleFont1 titleFont1Ex">新增机构类/及总部</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改机构类/及总部</caption>
					</#if>
				</tr>
					<tr>
						<td class="noBorderL firstTD" width="400"><label class="mainOrange"> * </label>机构类型</td>
						<td class="secondTD" width="800">
							<#if it == null>
							<label for="category0" onclick="category00(0,'jglb')"><input type="radio" name="type" id="category0" value="0" checked="checked" />银行机构</label> 
							<label for="category1" onclick="category00(1,'zflb')"><input type="radio" name="type" id="category1" value="1" />其他机构</label>
							<#else>
								<#if it.sys_org_type_type==0>
									<div><input type="hidden" name="type"  value="0" />银行机构<div>
								<#else>
									<div><input type="hidden" name="type"  value="1" />其他机构<div>
								</#if>
							</#if>
						</td>
					</tr>
					<tr>
					<td style="width:35%;" class="noBorderL firstTD">上级机构类别</td>
					<td style="width:65%;" class="secondTD" id="category">
						<input id="sys_org_type_upname" name="sys_org_type_upname" type="hidden" value="${it.sys_org_type_upname}"/>
						<input id="sys_org_type_upid" name="sys_org_type_upid" type="hidden" value="${it.sys_org_type_upid}"/>
						<#if it.sys_org_type_type==1>
							<a id="zflbA" name="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop('zflb')">${(it.sys_org_type_upname??)?string(it.sys_org_type_upname,'选择上级机构类别')}</a>
						<#else>
							<a id="jglbA" name="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop('jglb')">${(it.sys_org_type_upname??)?string(it.sys_org_type_upname,'选择上级机构类别')}</a>
						</#if>
					</td>
					<tr>
						<td class="noBorderL firstTD" width="400"><label class="mainOrange"> * </label>机构类别/总部名称</td>
						<td class="secondTD" width="800">
							<input id="sys_org_type_name" name="sys_org_type_name" class="inputSty required allnameVal" value="${it.sys_org_type_name}" onblur="onblurVal(this,13,30)" title="必填，不能超过30个字" maxlength="30"/>
						</td>
					</tr>
					<#-- 
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>类别代码</td>
						<td class="secondTD">
							<input id="sys_org_type_code" name="sys_org_type_code" class="inputSty required allletterVal" value="${it.sys_org_type_code}" onblur="onblurVal(this,21,20)" title="必填，不能超过20个字"/>
						</td>
					</tr>
					-->
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sys_org_type_notes" class="fontSize12 textareaSty" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)"  maxlength="50">${it.sys_org_type_notes}</textarea>
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
	
	$(function(){
		
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		
		
		
		$("#submitBtn").click(function(){
			if(checkChineseNoSpe($("input[name='sys_org_type_name']").val())==1) {
				layer.alert("机构类别名称不能为空",{icon:2,shade:0.3,shouldClose:true});
                $("input[name='sys_org_type_name']").focus();
                return false;
	        }
			if(checkChineseNoSpe($("input[name='sys_org_type_name']").val())==0) {
				layer.alert("机构类别名称输入不合法",{icon:2,shade:0.3,shouldClose:true});
                $("input[name='sys_org_type_name']").focus();
                return false;
	        }
			
			/*if(checkNoDataHeader12($("input[name='sys_org_type_code']").val())==1) {
				layer.alert("机构类别代码不能为空",{icon:2,shade:0.3,shouldClose:true});
                $("input[name='sys_org_type_code']").focus();
                return false;
	        }*/
			/*if(checkNoDataHeader12($("input[name='sys_org_type_code']").val())==0) {
				layer.alert("机构类别代码输入不合法",{icon:2,shade:0.3,shouldClose:true});
                $("input[name='sys_org_type_code']").focus();
                return false;
	        }
			*/
			if($("textarea[name='sys_org_type_notes']").val() != "" && checkChineseNoSpe50($("textarea[name='sys_org_type_notes']").val()) == 0) {

				layer.alert("备注输入不合法",{icon:2,shade:0.3,shouldClose:true});
                $("textarea[name='sys_org_type_notes']").focus();
                return false;
	        }
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/sysOrgType/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						parent.window.location.href = "${request.getContextPath()}/admin/sysOrgType/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
		
	});
	//搜索按钮
		$("#searchBtn").click(function(){
			var searchVal = $.trim($("#searchInput").val());
				var searchLoading = layer.load();
				var tabel = $("#searchTable");
				$.post("${request.getContextPath()}/admin/sysOrgType/getOrgTypeByName.jhtml",{name:searchVal},function(data){
					tabel.html("");
					$.each(data,function(i,v){
						var tr = $("<tr></tr>");
						var td = $("<td id='"+v.sys_org_type_id+"' onclick='selUpstream(this)'><label>"+v.sys_org_type_name+"</label></td>");
						tr.append(td);
						tabel.append(tr);
					});
					layer.close(searchLoading);
				});
		});
	</script>	
</html>
