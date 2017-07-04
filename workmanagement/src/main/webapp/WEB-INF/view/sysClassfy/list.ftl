<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>	
		<script type="text/javascript" >
			
			//删除地区
			function del(obj, id, name){
				var tip = "删除时会连带删除子行业信息，确定要删除 <span class='striking'>"+obj.name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/sysClassfy/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭弹窗
							layer.close(option_index);
							if(data.flag){
								//删除页面上的数据
								$(obj).parent().parent().remove();
								//删除子地区
								delSubArea(id)
								//判断上级地区是否还有子地区
								if($("."+pid).length==0){
									$("#"+pid).remove();
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
			function openPP(obj,id){
			   var obj = $(obj);
			   var strCode=obj.parent().prev().prev().prev().text();
			   setLayer('修改行业','${request.getContextPath()}/admin/sysClassfy/add.jhtml?id='+id); $('.layui-layer-shade').height($(window).height());
			}
			function openArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("name")==0){ //未展开
					//显示正在操作弹窗
					var option_index = layer.load(0,{
						shade: [0.5,'#fff']//0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("name",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysOrgType/getInstitutionsType.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						//关闭弹窗
						layer.close(option_index);
						if(result!=null){
							var subs = result.subSysOrgType;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subSysOrgType!=null && sub.subSysOrgType.length!=0){
									icon = '<div id="'+sub.sys_org_type_id+'" name="0" class="open-shrink" onclick="openArea(this,'+sub.sys_org_type_id+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								
								var tr = $("<tr name='"+sub.sys_org_type_id+"' class='"+id+"'></tr>");
								var name = "<td style='padding-left:"+spacing+"'>"
											+icon
											+sub.sys_org_type_name
											+"</td>";
								var code = "<td>"+sub.sys_org_type_code+"</td>";
								var remark = "<td>"+sub.sys_org_type_notes+"</td>";
								var option = '<td>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="setLayer(\'修改机构类别\',\'${request.getContextPath()}/admin/sysOrgType/add.jhtml?id='+sub.sysIndustryd+'\');$(\'.layui-layer-shade\').height($(window).height());">修 改</a>'
											 +'<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return del(this,'+id+','+sub.sysIndustryId+',\''+sub.sysIndustryName+'\')">删 除</a>'
											 +'</td>';
								
								tr.append(name).append(code).append(remark).append(option);
								
								ptr.after(tr);
							}
						}
					});
				}else{ //已展开
					//设置为收缩状态
					obj.attr("name",0);
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
			//全部导出
			function down(){
				window.open('${request.getContextPath()}/admin/sysClassfy/exportAll.jhtml?name=${sysIndustryName}');
			}
			//下载模板
			function downModel(){
				window.open('${request.getContextPath()}/admin/sysClassfy/downLoad.jhtml');
			}
			//上传
			
			$(function(){
			//回显
				var msg = "${msg}";
				if(msg!=""){
					layer.alert(msg,{
						icon:("保存成功"==msg?2:1),
						shade:0.3
					});
				}
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
				});
				if($("#sysclassName").val() != ""){
					$(".fuck").hide();
				}
				$("#sysclassName").focus(function(){
					$(".fuck").hide();
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(".fuck").show();
					}
				});
				$(".fuck").click(function(){
					$(".inputOtherCondition").focus();
				}) 
				$("#inquire").click(function(){
					var search=$("#sysclassName").val();
					if (checkTChineseM(search) == 0) {
	                    layer.alert("请输入正确的查询条件", {icon: 2, shade: 0.3, shouldClose: true});
	                    return false;
                	}
					$("#selectExcel").submit(); 
				});
				
			}); 
		</script>
		<title>行业分类</title>
	</head>
	<body class="eachInformationSearch">
	<form id="searchForm" method="post">
		<input   name="name"  type="hidden" value="${sysIndustryName}"  />
		<input   name="url"  type="hidden" value="${url}"  />
	</form>
	
	<#if msgString??>
		<input type="hidden" id="err" value="${msgString?size} " />
	<#else>
		<input type="hidden" id="err" value="0" />
	</#if>
	<#if  msgStr??>
	<input type="hidden" id="errList" value="${msgStr?size}" />
		<#else>
		<input type="hidden" id="errList" value="0" />
	</#if>
		<#if reportIndexErrorList?? !>
			<div id="errorDiv" class="showListBox" style="width: 80%;">
				<table cellpadding="0" cellspacing="0">
							<caption class="titleFont1 titleFont1Ex">导入失败，请修正后操作</caption>
							<tr class="firstTRFont">
								<td width="80">序号</td>
								<td width="120">有误项名称</td>
								<td width="100">上报值</td>
								<td width="200">说明</td>
							</tr>
							<#list reportIndexErrorList as item>
								<tr>
									<td>${item_index+1}</td>
									<td>${item.reportIndexErrorName}</td>
									<td>${item.reportIndexErrorValue}</td>
									<td>${item.reportIndexErrorNotes}</td>
								</tr>
							</#list>
				</table>
				<div class="showBtnBox">
					<input id="closeBut" type="text" class="sureBtn textCenter" value="关 闭"/>
				</div>
			</div>
		</#if>
		
		<div class="queryInputBox">
			<div style="zoom: 1;" class="verticalMiddle">
 			    <div  class="verticalMiddle ">
 			    	
 			    <div>
					<input onclick="setLayer('新增行业','${request.getContextPath()}/admin/sysClassfy/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"
					type="button" value="新增行业" class="sureBtn" style="margin-left:30px"/>
					
				<form  id="upload" action="${request.getContextPath()}/admin/sysClassfy/upLoad.jhtml" method="post" enctype="multipart/form-data"    style="display: inline-block;*zoom=1;*display:inline" class="marginL20">
						<input class="inputSty" id="file" type="file" name="file" value="上传"  />
						<input type="submit" value="导入类别" class="sureBtn" style="margin-left:0px"/>
				</form>
 			    <form id="download" action="${request.getContextPath()}/admin/sysClassfy/downLoad.jhtml" method="post" enctype="multipart/form-data" style="display: inline-block;*zoom=1;*display:inline;"> 
				     <input onclick="downModel()" type="button" value="下载模板" class="sureBtn sureBtnEx"/>
			    </form>
 			     <div class="marginT10">
					<form id="form" action="${request.getContextPath()}/admin/sysClassfy/exportAll.jhtml?" method="post" style="display: inline-block;*zoom:1;*display:inline;">
 			     	</form>	
 			     		<input onclick="down()" type="button" value="全部导出" class="sureBtn sureBtnEx" style="margin-left: 25px;"/>
 			     	
					<form id="selectExcel" action="${request.getContextPath()}/admin/sysClassfy/list.jhtml" method="post" style="display: inline-block;*zoom:1;*display:inline;position:relative;margin-left:20px"  >
					 	<span class="fuck">请输入行业类别名称查询</span>
				     	<input class="inputSty inputOtherCondition" type="text" name="name" id="sysclassName"  value="${sysIndustryName}"/>
				     	<input type="submit" id="inquire" class="sureBtn" value="查  询" style="margin-left:0px" />
					</form> 
					
				  
				</div>
				<div class="marginT10">
				    <span class="warmFont inlineBlock fontSize12 marginL30">注：按照实际的机构类别名称和代码进行管理操作；无法删除被使用的机构类别。</span>
				</div>
				
					
				
			</div>
			
			<div class="listBox">
				
				<table cellpadding="0" cellspacing="0" >
					<caption class="titleFont1 titleFont1Ex">经济行业列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="100">行业代码</td>
							<td width="150">类别名称</td>
							<td width="200">备注</td>
							<td width="150">操作</td>
						</tr>
						<#list sysClassFyModel as it>
							<tr>
								<td>${it.sysIndustryCode}</td>
								<td>${it.sysIndustryName}</td>
								<td>${it.sysIndustryNotes}</td>
								<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline" <a class="changeFont fontSize12 cursorPointer hasUnderline"
								onclick="openPP(this,${it.sysIndustryId})"  >修 改</a>
								<a class="delFont fontSize12 cursorPointer hasUnderline" name="${it.sysIndustryName}" id="delName" href="javascript:" onclick="return del(this, ${it.sysIndustryId})">删 除</a>
								<a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysClassfy/export.jhtml?id=${it.sysIndustryId}">导出</a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
				
				<#if (sysClassFyModel?? && sysClassFyModel?size > 0)>
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
			 $(function(){
				if($("#err").val()!=0){
					layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 					  closeBtn: 0, //不显示关闭按钮
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgString as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
				}
			 })
			 $(function(){
				if($("#errList").val()!=0){
					layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 					  closeBtn: 0, //不显示关闭按钮
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgStr as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
				}
			 })
		//IE下 背景全屏
	    window.onresize = function(){
			$('.layui-layer-shade').height($(window).height());
		} 
	</script>
</html>
