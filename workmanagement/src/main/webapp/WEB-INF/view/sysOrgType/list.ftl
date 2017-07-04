<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>		
		<script type="text/javascript" >
			
			//删除地区
			function del(obj, pid, id, name){
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
				    	var url = "${request.getContextPath()}/admin/sysOrgType/del.jhtml";
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
					var url = "${request.getContextPath()}/admin/sysOrgType/getType.jhtml";
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
								var remark = "<td>"+sub.sys_org_type_notes+"</td>";
								var option = '<td>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="setLayer(\'修改机构类别\',\'${request.getContextPath()}/admin/sysOrgType/add.jhtml?id='+sub.sys_org_type_id+'\');$(\'.layui-layer-shade\').height($(window).height());">修 改</a>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrgType/export.jhtml?id='+sub.sys_org_type_id+'">导 出</a>'
											 +'<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return del(this,'+id+','+sub.sys_org_type_id+',\''+sub.sys_org_type_name+'\')">删 除</a>'
											 +'</td>';
								
								tr.append(name).append(remark).append(option);
								
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
			function exp(){
				location.href="${request.getContextPath()}/admin/sysOrgType/exportAll.jhtml?orgTypeName=${orgTypeName}";
			}
			
			
			$(function(){
				$("#inquire").click(function(){
					var search=$("#key").val();
					if(checkTChineseM(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			    	} 
					$("#selectExcel").submit()
				})
			})
		</script>
		<title>机构分类列表</title>
	</head>
	<body class="eachInformationSearch">
	<form id="searchForm" method="post">
		<input   name="orgTypeName"  type="hidden" value="${orgTypeName}"  />
	</form>
		<div class="queryInputBox">
			<div class="verticalMiddle">
				<input 
				onclick="setLayer('新增类别/总部','${request.getContextPath()}/admin/sysOrgType/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"
				type="button" value="新增类别/总部" class="sureBtn" style="margin-left:30px;width: 100px;"/>
				
				<form id="selectExcel" action="${request.getContextPath()}/admin/sysOrgType/list.jhtml" method="post" style="display: inline-block;*zoom=1;*display:inline;position: relative;position:relative;" class="marginL20">	
					
						<input type="hidden" name="methodTextVal" id="methodTextVal" value="${methodTextVal}">
						<input  id="key" name="orgTypeName" class="inputSty inputOtherCondition" value="${orgTypeName}" />
						<span class="fuck">请输机构类别及总部名称查询</span>
						<input type="button" id="inquire" class="sureBtn" value="查  询" style="margin-left:0px"/>
			 		
				</form>
				<input  id="exp" type="button" onclick="exp()" value="全部导出" class="sureBtn"/>
				
			</div>	
			<div >
			
				<span class="warmFont inlineBlock marginL30 marginT10 fontSize12">注：按照实际的机构类别及总部名称和代码进行管理操作；无法删除被使用的机构类别。</span>
			</div>
				
			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">机构类别/总部列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="250">机构类别及总部名称</td>
							<td width="200">备注</td>
							<td width="150">操作</td>
						</tr>
						<#list its as it>
							<tr>
								<td>
									<#if (it.subSysOrgType?? && it.subSysOrgType?size > 0) >
										<div id="${it.sys_org_type_id}" name="0" class="open-shrink" onclick="openArea(this, ${it.sys_org_type_id})">
											<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
										</div>
									</#if>
									${it.sys_org_type_name}
								</td>
								<td>${it.sys_org_type_notes}</td>
								<td>
									<a class="changeFont fontSize12 cursorPointer hasUnderline" 
										onclick="setLayer('修改机构类别','${request.getContextPath()}/admin/sysOrgType/add.jhtml?id=${it.sys_org_type_id}');$('.layui-layer-shade').height($(window).height());" 
										>修 改</a><a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysOrgType/export.jhtml?id=${it.sys_org_type_id}">导出</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${it.sys_org_type_upid?default('null')}, ${it.sys_org_type_id}, '${it.sys_org_type_name}')">删 除</a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
				<#if !(its?? && its?size > 0)>
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
		$(function(){
				
				
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
				
				
			})
	</script>
</html>
