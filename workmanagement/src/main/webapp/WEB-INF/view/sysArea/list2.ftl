<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>
		<script type="text/javascript" >
		$(function(){
				if($("#err").val()!=0){
					layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 					  closeBtn: 0, //不显示关闭按钮
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list errorList as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
				}
			 })
		
			$(function(){
			//回显
				var msg = "${msg}";
				if(msg!=""){
					layer.alert(msg,{
						icon:("导入成功"==msg?1:2),
						shade:0.3
					});
				}
				//上传验证
				$("#importform").submit(function(){
					var fileName=$("#file").val();
					if(fileName == "") {
				        layer.alert("请选择excel文件", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				        return false;
				    }
					if(!fileName.endsWith(".xls")&&!fileName.endsWith(".xlsx")) {
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
			})  
			
			//删除地区
			function del(obj, pid, id, name){
				var tip = "删除时会连带删除子地区，确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/sysArea/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭弹窗
							layer.close(option_index);
							if(data.flag){
								//删除页面上的数据
								$(obj).parent().parent().remove();
								//删除子地区
								delSubArea(id);
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
					//显示正在加载子区域弹窗
					var tc_index = layer.load(0,{
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("name",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						//关闭加载提示弹窗
						layer.close(tc_index);
						if(result!=null){
							var subs = result.subArea;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subArea!=null && sub.subArea.length!=0){
									icon = '<div id="'+sub.sysAreaId+'" name="0" class="open-shrink" onclick="openArea(this,'+sub.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var areaType = sub.sysAreaType;
								if(areaType === "0"){
									areaType = "省、直辖市";
								}else if(areaType === "1"){
									areaType = "地市";
								}else if(areaType === "2"){
									areaType = "区县";
								}else if(areaType === "3"){
									areaType = "乡镇";
								}
								
								var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
								var name = "<td class='noCenter' style='padding-left:"+spacing+"'>"
											+icon
											+sub.sysAreaName
											+"</td>";
								var code = "<td>"+sub.sysAreaCode+"</td>";
								var type = "<td>"+areaType+"</td>";
								var remark = "<td>"+sub.sysAreaNotes+"</td>";
								var option = '<td>'
											 +'<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="openPP(this)">修 改</a>'
											 +'<a class="delFont fontSize12 cursorPointer hasUnderline" onclick="return del(this,'+id+','+sub.sysAreaId+',\''+sub.sysAreaName+'\')">删 除</a>'
											 +'</td>';
								
								tr.append(name).append(code).append(type).append(remark).append(option);
								
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
			function exportExcel2(){
				window.location.href = "${request.getContextPath()}/admin/sysArea/exportArea2.jhtml";
			}
			
			function exportExcelTemplate(){
				window.location.href = "${request.getContextPath()}/admin/sysArea/exportAreaTemplate.jhtml" ;
			}
			function openPP(obj){
			   var obj = $(obj);
			   var strCode=obj.parent().prev().prev().prev().text();
			   setLayer('修改地区','${request.getContextPath()}/admin/sysArea/modify.jhtml?code='+strCode); $('.layui-layer-shade').height($(window).height());
			}
		</script>
		<title>地区列表</title>
	</head>
	
	<body class="eachInformationSearch marginT0">
	<input type="hidden" id="err" value="<#if errorList??>${errorList?size}</#if>">
		<div class="queryInputBox">
			<div class="verticalMiddle">
				<input onclick="setLayer('新增区域','${request.getContextPath()}/admin/sysArea/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"
  				type="button" value="新增区域" class="sureBtn" style="margin-left:30px"/>
  				<form id="importform" enctype="multipart/form-data" action="${request.getContextPath()}/admin/sysArea/importArea.jhtml" method="post" class="marginL20" style="display: inline-block;*zoom=1;*display:inline"> 
					<input type="file" name="file" class="inputSty" id="file">
					<input type="submit" id="submitBtn" class="sureBtn" value="导入区域" style="margin-left: 0px;">
				</form>
				<input type="button" value="下载模板" class="sureBtn" onclick="exportExcelTemplate()"/>
				<input type="button" value="全部导出" class="sureBtn" onclick="exportExcel2()"/>
			</div>
			<div class="search">
				
			</div>

			<div class="listBox marginT20">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">地区列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="200">区域名称</td>
							<td width="80">行政代码</td>
							<td width="120">区域类型</td>
							<td width="200">备注</td>
							<td width="100">操作</td>
						</tr>
						<#list aList as area>
							<tr name="${area.sysAreaId}">
							<td>
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="${area.sysAreaId}" name="0" class="open-shrink" onclick="openArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<span class="fll fontSize12">${area.sysAreaName}</span>
							</td>
							<td>${area.sysAreaCode}</td>
							<td class="areaType">${area.sysAreaType}</td>
							<td>${area.sysAreaNotes}</td>
							<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline"
								onclick="openPP(this)"
								 >修 改</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${area.sysAreaUpid?default('null')}, ${area.sysAreaId}, '${area.sysAreaName}')">删 除</a>
							</td>
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
		
		//将区域类型格式化
		$.each($(".areaType"),function(i,v){
			var val = $(v).text();
			if(val === "0"){
				$(v).text("省、直辖市");
			}else if(val === "1"){
				$(v).text("地市");
			}else if(val === "2"){
				$(v).text("区县");
			}else if(val === "3"){
				$(v).text("乡镇");
			}
		});
	</script>
</html>
