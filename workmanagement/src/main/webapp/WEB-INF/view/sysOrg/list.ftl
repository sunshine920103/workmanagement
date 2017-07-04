<!DOCTYPE HTML>
<html>
	<head>
		<meta charset="UTF-8"/>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
			 $(function(){
				if($("#err").val()!=0){
					layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgString as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
				}
			 })
			//删除机构
			function del(obj, pid, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
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
				    	var url = "${request.getContextPath()}/admin/sysOrg/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭正在操作弹窗
							layer.close(option_index);
							if(data.flag){
								//删除页面上的数据
								$(obj).parent().parent().remove();
								//判断上级机构是否还有子机构，如果没有则删除展开图标
								if($("."+pid).length==0){
									$("#"+pid).remove();
								}
								//删除子机构
								delInstitutions(id)
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
			}	
			 function openInstitutionsType(obj, id,orgName){
					obj = $(obj);
					//子区域的缩进
					var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
					
					if(obj.attr("name")==0){ //未展开
						//设置为展开状态
						obj.attr("name",1);
						//将图标设置为展开图标
						$(obj.find("img")[0]).css("right",5);
						var loading = wait();
						
						//获取父区域的tr
						var ptr = obj.parent().parent();
						var url = "${request.getContextPath()}/admin/sysOrgType/getInstitutionsType.jhtml?orgName="+orgName;
						$.get(url,{_:Math.random(),id:id},function(result){
							layer.close(loading);
							if(result!=null){
								var subs = result.subSysOrgType;
								var subOrgs=result.subSysOrg;
								for(var i = 0; i < subOrgs.length; i++){
									//子地区
									var sub = subOrgs[i];
									
									//展开图标
									var icon = "";
									if(sub.subSysOrg!=null && sub.subSysOrg.length!=0){
										icon = '<div id="'+sub.sys_org_id+'A" name="0" class="open-shrink" onclick="openInstitutions(this,\''+sub.sys_org_id+'A\')">'
											   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
											   +'</div>'
									}
									
									
									var tr = $("<tr name='"+sub.sys_org_id+"A' class='"+id+"'></tr>");
									var name = "<td width='50'> <input type='checkbox' onclick='test(this)' class='checkedbox1' value='"+sub.sys_org_id+"'></td>"+
												"<td width='50'> <input type='checkbox' onclick='test(this)' class='checkedbox2' value='"+sub.sys_org_id+"'></td>"+
											    "<td   style='padding-left:"+spacing+"'>"
												+icon
												+"<span class='fll fontSize12'>"+sub.sys_org_name+"</span>"
												+"</td>";
									var code = "<td>"+sub.sys_org_financial_code+"</td>";
									var status="";
									var num="";
									if(${role}==1){
										if(sub.sys_org_current_limit_query_times!=0&&sub.sys_org_current_query_times==sub.sys_org_current_limit_query_times){
											num='<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return reset(this,'+sub.sys_org_id+',\''+sub.sys_org_name+'\')">查询次数清零</a>'
										}else{
											num='<span class="fontSize12 hasUnderline" style="color:#787878">查询次数清零</span>'
										}
										if(sub.sys_org_status==0){
											status='<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return unlock(this,'+sub.sys_org_id+',\''+sub.sys_org_name+'\')">解锁</a>'
										}
									}
									var option = '<td>'
												 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="setLayer(\'查看机构\',\'${request.getContextPath()}/admin/sysOrg/add.jhtml?id='+sub.sys_org_id+'\');$(\'.layui-layer-shade\').height($(window).height());">查 看</a>'
												 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrg/export.jhtml?id='+sub.sys_org_id+'">导出</a>'
												 +'<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return del(this,'+id+','+sub.sys_org_id+',\''+sub.sys_org_name+'\')">删 除</a>'
												 +num
												 +status
												 +'</td>';
									tr.append(name).append(code).append(option);
									ptr.after(tr);
								}
								for(var i = 0; i < subs.length; i++){
									//子地区
									var sub = subs[i];
									//展开图标
									var icon = "";
									if((sub.subSysOrgType!=null && sub.subSysOrgType.length!=0)||(sub.subSysOrg!=null && sub.subSysOrg.length!=0)){
										icon = '<div id="'+sub.sys_org_type_id+'" name="0"  class="open-shrink" onclick="openInstitutionsType(this,'+sub.sys_org_type_id+',\''+orgName+'\')">'
											   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
											   +'</div>'
									}
									
									
									var tr = $("<tr name='"+sub.sys_org_type_id+"' class='"+id+"'></tr>");
									var name = "<td colspan='2'></td><td    style='padding-left:"+spacing+"'>"
												+icon
												+"<span class='fll fontSize12'>"+sub.sys_org_type_name+"</span>"
												+"</td>";
									var code = "<td></td>";
									var option = "<td></td>";
									tr.append(name).append(code).append(option);
									ptr.after(tr);
								}
								
								
							}
						});
					}else if(obj.attr("name")==1){ //已展开
						//设置为收缩状态
						obj.attr("name",2);
						//将图标设置为收缩图标
						$(obj.find("img")[0]).css("right",20);
						//删除子区域
						delInstitutionsType(id);
					}else{
						obj.attr("name",1);
						//将图标设置为收缩图标
						$(obj.find("img")[0]).css("right",5);
						//删除子区域
						opentype(id);
					}
				}
				
				//删除机构类型管理子区域
				function delInstitutionsType(id){
					$.each($("."+id),function(i, v){
						var pid = v.attributes.getNamedItem("name").nodeValue;
						
						//删除子区域
						$(this).hide();

						//递归删除子区域
						delInstitutionsType(pid);
					});
				}
				
				
				//删除机构类型管理子区域
				function opentype(id){
					$.each($("."+id),function(i, v){
						var pid = v.attributes.getNamedItem("name").nodeValue;
						
							//删除子区域
							$(this).show(); 
							
							/*if(pid.indexOf("A")!=-1){
								var prid=pid.substring(0,pid.indexOf("A"));
							}else{
								var prid=pid
							}*/
							if($("#"+pid).attr('name')==1	){
								opentype(pid);
							} 
								
							 
					});
				}
			/********************************************************
			 * 打开上级机构弹出框
			 * obj ： 被点击的元素（this）
			 * id : 需要查询子机构的机构ID
			 */
			function openInstitutions(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 30 + "px";
				
				if(obj.attr("name")==0){ //未展开
					//显示正在操作域弹窗
					var option_index = layer.load(0,{
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("name",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var a=id.substring(0,id.indexOf("A"));
					var url = "${request.getContextPath()}/admin/sysOrg/getInstitutions.jhtml";
					$.get(url,{_:Math.random(),id:a},function(result){
						//关闭弹窗
						layer.close(option_index);
						if(result!=null){
							var subs = result.subSysOrg;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subSysOrg!=null && sub.subSysOrg.length!=0){
									icon = '<div id="'+sub.sys_org_id+'A" name="0" class="open-shrink" onclick="openInstitutions(this,\''+sub.sys_org_id+'A\');">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var tr = $("<tr name='"+sub.sys_org_id+"A' class='"+id+"'></tr>");
								var name = "<td width='50'> <input type='checkbox' onclick='test(this)' class='checkedbox1' value='"+sub.sys_org_id+"'></td>"+
											"<td width='50'> <input type='checkbox' onclick='test(this)' class='checkedbox2' value='"+sub.sys_org_id+"'></td>"+
											"<td style='padding-left:"+spacing+"'>"
											+icon
											+sub.sys_org_name
											+"</td>";
								var code = "<td>"+sub.sys_org_financial_code+"</td>";
								var num = "";
								var status="";
								if(${role}==1){
									if(sub.sys_org_current_limit_query_times!=0&&sub.sys_org_current_query_times==sub.sys_org_current_limit_query_times){
										num='<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return reset(this,'+sub.sys_org_id+',\''+sub.sys_org_name+'\')">查询次数清零</a>'
									}else{
										num='<span class="fontSize12 hasUnderline" style="color:#787878">查询次数清零</span>'
									}
									if(sub.sys_org_status==0){
										status='<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return unlock(this,'+sub.sys_org_id+',\''+sub.sys_org_name+'\')">解锁</a>'
									}
								}
								var option = '<td>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="setLayer(\'查看机构\',\'${request.getContextPath()}/admin/sysOrg/add.jhtml?id='+sub.sys_org_id+'\');$(\'.layui-layer-shade\').height($(window).height());">查 看</a>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrg/export.jhtml?id='+sub.sys_org_id+'">导出</a>'
											 +'<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return del(this,'+a+','+sub.sys_org_id+',\''+sub.sys_org_name+'\')">删 除</a>'
											 +num
											 +status
											 +'</td>';
								
								tr.append(name).append(code).append(option);
								
								ptr.after(tr);
							}
						}
					});
				}else if(obj.attr("name")==1){ //已展开
					//设置为收缩状态
					obj.attr("name",2);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",20);
					//删除子区域
					delInstitutions(id);
				}else{
					
					//设置为收缩状态
					obj.attr("name",1);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",5);
					//删除子区域
					opentutions(id);
					
				}
			}
			//删除机构类型管理子区域
			function delInstitutions(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					//删除子区域
					$(this).hide();

					//递归删除子区域
					delInstitutions(pid);
				});
			}
			
			//删除机构类型管理子区域
			function opentutions(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					//删除子区域
					$(this).show();

					/*if(pid.indexOf("A")!=-1){
						var prid=pid.substring(0,pid.indexOf("A"));
					}else{
						var prid=pid
					}*/
					if($("#"+pid).attr('name')==1	){
						opentutions(pid);
					} 
					 
				});
			}
			
			function exportModel(){
				window.open("${request.getContextPath()}/admin/sysOrg/exportModel.jhtml");
			}
			//上传
			
			$(function(){
				$("#upload").submit(function(){
					var fileName=$("#file").val();
					if(fileName == "") {
				        layer.alert("请选择excel文件", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				        return false;
				    }
					var reg=/(.*).(xls|xlsx)$/; 
				 
					if(!reg.test(fileName)) {
				        layer.alert("文件不是excel格式", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				        return false;
				    }
					loading = layer.load();
				});
				//关闭错误列表
				$("#closeBut").click(function(){
					$("#errorDiv").hide();
				})
				
			});
			function exp(){
				var num1=[]
				var num2=[]
				$(".checkedbox1:checked").each(function(){
					var str1=$(this).val()
					num1.push(str1)
				})
				$(".checkedbox2:checked").each(function(){
					var str2=$(this).val()
					num2.push(str2)
				}) 
				location.href="${request.getContextPath()}/admin/sysOrg/exportAll.jhtml?orgName=${orgName}&num1="+num1.join(",")+"&num2="+num2.join(",");
			}
			
			
			
			function openorg(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("name")==0){ //未展开
					//设置为展开状态
					obj.attr("name",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					$("."+id).show();
					
					
				}else{ //已展开
					//设置为收缩状态
					obj.attr("name",0);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",20);
					//删除子区域
					$("."+id).hide();
				}
			}
			//清空查询次数
			function reset(obj,id,name){
				var tip = "确定要清空 <span class='striking'>"+name+"</span>的查询次数 吗";
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
				    	var url = "${request.getContextPath()}/admin/sysOrg/reset.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭正在操作弹窗
							layer.close(option_index);
							if(data.flag){
								$(obj).after('<span class="fontSize12" style="color:#787878">查询次数清零</span>')
								$(obj).remove() 
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
			}
			//解锁
			function unlock(obj,id,name){
				var tip = "确定要解锁 <span class='striking'>"+name+"</span> 吗";
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
				    	var url = "${request.getContextPath()}/admin/sysOrg/unlock.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭正在操作弹窗
							layer.close(option_index);
							if(data.flag){
								$(obj).remove() 
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
		<title>机构列表</title>
	</head>
	
	<body class="eachInformationSearch">
		<form id="searchForm" method="post">
			<input   name="orgName"  type="hidden" value="${orgName}"  />
			<input   name="url"  type="hidden" value="${url}"  />
		</form>
	<#if msgString??>
		<input type="hidden" id="err" value="${msgString?size}">
	<#else>
		<input type="hidden" id="err" value="0">
	</#if>
		<div class="queryInputBox">
			<div  class="verticalMiddle">
				<input 
				onclick="setLayer('新增机构','${request.getContextPath()}/admin/sysOrg/add.jhtml');$('.layui-layer-shade').height($(window).height());"
				 type="button" value="新增机构" class="sureBtn" style="margin-left: 30px;"/>
				 
				 <form  id="upload" method="post"  enctype="multipart/form-data" action="${request.getContextPath()}/admin/sysOrg/excelReader.jhtml" style="display: inline-block;*zoom=1;*display:inline" class="marginL20">
						<input class="inputSty" id="file" type="file" name="file" value="上传"  />
						<input type="submit" value="导入机构" class="sureBtn" style="margin-left:0px"/>
				</form>
					<input type="button" onclick="exportModel()" value="下载模板" class="sureBtn"/>	
 			</div>
 			<div  class="marginT10">
				<input type="button" onclick="exp()" value="全部导出" id="exp"  class="sureBtn" style="margin-left:30px"/>
 				<form id="selectExcel" action="${request.getContextPath()}/admin/sysOrg/list.jhtml" method="post" class="marginL20" style="display: inline-block;*zoom=1;*display:inline;position: relative;">
						<#-- 当前分组方式, 只用来回显 -->
						<input type="hidden" name="methodTextVal" id="methodTextVal" value="${methodTextVal}"/>
						<span class="fuck">请输机构名称查询</span>
						<input  name="orgName" id="search" class="inputSty inputOtherCondition" value="${orgName}" />
						<input type="button" id="inquire" class="sureBtn" value="查   询" style="margin-left:0px"/>
				</form>
			</div>
 			
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">机构列表</caption>
					<thead>
						<tr class="firstTRFont">
							<#if query==1>
								
							<#else>
								<td width="50">包含子机构</td>
								<td width="50">不包含子机构</td>
							</#if>
							<td width="300">机构名称</td>
							<#if query==1>
								<td width="300">机构类别</td>
							</#if>
							<td width="120">机构编码</td>
							<td width="150">操作</td>
						</tr>
					</thead>
					<tbody>
					<#--<#list type as ty>
						<tr>
							<td colspan="2"></td>
							<td colspan="3"><div id="${ty.sys_org_type_id}" name="0" class="open-shrink" onclick="openorg(this, ${ty.sys_org_type_id})">
											<img  src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
										</div>
										<span class="fll fontSize12">${ty.sys_org_type_name}</span></td>
							
						</tr>
						<#list is as i>
						<#if ty.sys_org_type_id=i.sys_org_type_id>
							<tr  class="${ty.sys_org_type_id} hide">
								<td width="50"> <input type="checkbox" class="checkedbox1" value="${i.sys_org_id}"></td>
								<td width="50"> <input type="checkbox" class="checkedbox2" value="${i.sys_org_id}"></td>
								<td>
									<#if (i.subSysOrg?? && i.subSysOrg?size > 0) >
										<div style="margin-left:15px;" id="${i.sys_org_id}" name="0" class="open-shrink" onclick="openInstitutions(this, ${i.sys_org_id},${ty.sys_org_type_id});">
											<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
										</div>
									</#if>
									<span <#if (i.subSysOrg?size = 0) >style="margin-left:15px;"</#if> class="fll fontSize12" >${i.sys_org_name}</span>
									</td>
								<td id="code">${i.sys_org_financial_code}</td>
								<td>
									<a class="changeFont fontSize12 cursorPointer hasUnderline"
									onclick='setLayer("查看机构","${request.getContextPath()}/admin/sysOrg/add.jhtml?id=${i.sys_org_id}");$(".layui-layer-shade").height($(window).height());'
									 >查 看</a><a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrg/export.jhtml?id=${i.sys_org_id}">导出</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${i.sys_org_upid?default('-1')}, ${i.sys_org_id}, '${i.sys_org_name}')">删 除</a>
								</td>
							</tr>
						</#if>
						</#list>
						</#list>-->
						<#if query==1>
							<#list is as i>
								<tr>
									<td>
										<span  class="fll fontSize12" >${i.sys_org_name}</span>
									</td>
									<td>${i.sys_org_type_name}</td>
									<td>${i.sys_org_financial_code}</td>
									<td>
										<a class="changeFont fontSize12 cursorPointer hasUnderline"
										onclick='setLayer("查看机构","${request.getContextPath()}/admin/sysOrg/add.jhtml?id=${i.sys_org_id}");$(".layui-layer-shade").height($(window).height());'
										 >查 看</a><a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrg/export.jhtml?id=${i.sys_org_id}">导出</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${i.sys_org_upid?default('-1')}, ${i.sys_org_id}, '${i.sys_org_name}')">删 除</a>
										 <#if role==1>
										 	<#if i.sys_org_current_limit_query_times!=0&&i.sys_org_current_query_times==i.sys_org_current_limit_query_times>
										 		<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return reset(this,'${i.sys_org_id}','${i.sys_org_name}')">查询次数清零</a>
										 	<#else>
										 		<span class="fontSize12 hasUnderline" style="color:#787878">查询次数清零</span>
										 	</#if>
										 	<#if i.sys_org_status==0>
										 		<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return unlock(this,'${i.sys_org_id}','${i.sys_org_name}')">解锁</a>
										 	</#if>
										 </#if>
									</td>
								</tr>
							</#list>
						<#else>
							<#list type as i>
								<tr>
									<td colspan="2"></td>
									<td>
										<div id="${i.sys_org_type_id}" name="0" class="open-shrink" onclick="openInstitutionsType(this, ${i.sys_org_type_id},'${orgName}');">
											<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
										</div>
										<span  class="fll fontSize12" >${i.sys_org_type_name}</span>
										</td>
									<td id="code"></td>
									<td>
									</td>
								</tr>
							</#list>
						</#if>
					</tbody>
				</table>
				<table cellspacing="0" cellpadding="0" class="noBorderT">
				<#if query==1>
					<#if (is?? && is?size > 0)>
						<#include "/fragment/paginationbar.ftl"/>
					<#else>
						<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
							<tr class="firstTRFont">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>    	
					</#if>
				</#if>
				</table>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
			var loading;
			//回显
			var msg = "${msg}";
			if(msg != "") {
				alertInfo(msg);
//				layer.alert(msg,{
//					icon: (msg=="操作成功")?1:2,
//					shade:0.3,
//					shadeClose:true
//				});
				layer.close(loading);
			}
//			layer.alert(msg, {
//					shade:0.3,
//				    time: 5000, //20s后自动关闭
//				    btn: ['确定', '取消'],
//				    yes: function(index){
//				    	layer.close(index);
//				  	}
//			  	});
		$(function(){
				//$(".groupList").height($(window).height()-50);
				
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
				
				
				$("input[type='checkbox']").click(function(){
					var  str=$(this).parent().siblings().children("input[type='checkbox']")
						     str.attr("checked",false)
					
				})
				
				/*$("#exp").click(function(){
					var num = $("tbody").children().length;
					var arr=[];
					for(var i=1;i<=num;i++){
						var code = $("tr").eq(i).find("#code").text();
						arr.push(code);
					}
					location.href="${request.getContextPath()}/admin/sysOrg/exportAll.jhtml?arr="+arr.join(",");
				});*/

			});
		function test(obj){
			var  str=$(obj).parent().siblings().children("input[type='checkbox']")
				     str.attr("checked",false)
		}
	</script>
</html>
