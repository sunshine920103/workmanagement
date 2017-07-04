<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8"/>
		<script type="text/javascript">
		
	$(function(){

		if($("input[name='indexName']").val()!=0){
					$("input[name='indexName']").attr("readonly","readonly");
					$("input[name='indexName']").css({"border":"none"});
				}
		else{
			$("input[name='indexName']").css({"border":"1px solid #dadada"});
		}
		  
	});
	
	
			//显示上级区域弹出框
			function openArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("id")==0){ //未展开
					//显示正在加载子区域弹窗
					var tc_index = layer.load(0,{
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("id",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						//关闭加载提示弹窗
						layer.close(tc_index);
						if(result!=null && result.sysAreaType==0){
							var subs = result.subArea;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subArea!=null && sub.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}			
								var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
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
			//显示上级区域弹出框
			function openPop(){
				$("#covered").show();
				$("#poplayer").show();
			}
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			//选择上级区域
			function selUpstream(obj){
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
			//确认选择
			function confirmSel(){
				var seleced = $(".seleced");
				if(seleced.length==0){
					alert("您还没有选择上级区域");
				}else{
					closePop();
					var area = $(seleced.find("label"));
					$("#openPop").text(area.text());
					$("#parent").val(seleced.attr("id"));
					$("input[name='queryProductTemplateAreaName']").val(area.text());
					$("input[name='queryProductTemplateAreaId']").val(seleced.attr("id"));
				}
			}
			
</script>
		<title>新增指标分类列表</title>
	</head>
	<body>
	<#-- 弹出框 -->
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox">
	        	<div class="titleFont1">地区列表</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0">
						<tr>
							<td style="font-size:12px" id="${area.sysAreaId}" onclick="selUpstream(this)">
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label class="fontSize12">${area.sysAreaName}</label>
							</td>
						</tr>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel()"/>
	        	</div>
	        </div>  
	    </div>
		<#-- 新增框 -->
	    <div class="showListBox">
	<form id="form" method="post"  >
		<div >
			<table class="centerTable" cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">新增指标大类</caption>
				<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>指标大类名称：</td>
						<td style="width:65%;" class="secondTD"><input id="indexName" name="indexName" class="inputSty allnameVal" type="text" value="${indexTb.indexName}" onblur="onblurVal(this,13,50)" title="必填，不能超过50个字" maxlength="50"/></td>
				</tr>
				<tr>
						<td class="chooseCustomProductQueryTermsTD1st noBorderL firstTD">所属区域</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<#if indexTb!=null>
							<input id="area" style="font-size:12px;color:rgb(56,165,226);"  readonly="readonly" name="queryProductTemplateAreaName" value="${indexTb.indexRegion}" />
							 <a id="openPop" href="javascript:void(0);" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()"> 选择区域</a>
							<input type="hidden" name="queryProductTemplateAreaId" value="${indexTb.indexRegionId}" />
							<#else>
								<#if area.sysAreaName=="四川省"> 
									<input id="area"  readonly="readonly" name="queryProductTemplateAreaName" value="四川省" />
								<#else>
									<input id="area"  readonly="readonly" name="queryProductTemplateAreaName" value="${area.sysAreaName}" />
								</#if>
								<#if area.sysAreaName=="四川省"> 
							    <a id="openPop" href="javascript:void(0);" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()"> 选择区域</a>
								</#if>
								<input type="hidden" name="areaId" value="${dutyAreaId}" />
							</#if>	
						</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD"><label class="mainOrange"> * </label>指标类型：</td>
					<td class="secondTD">
						<label for="indexType1"><input type="radio" name="indexType" value="1" id="indexType1"/>业务信息</label>
						<label for="indexType0"><input type="radio" name="indexType" value="0" id="indexType0"/>基本信息</label>
						<span  style='font-size:12px;color:#787878;margin-left:10px '>
							(业务信息=增量信息、基本信息=全量信息)
						</span>
					</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD"><label class="mainOrange"> * </label>指标大类是否启用：</td>
					<td class="secondTD">
						<label for="indexUsed0"><input type="radio" name="indexUsed" value="1" id="indexUsed0"/>是</label>
						<label for="indexUsed1"><input type="radio" name="indexUsed" value="0" id="indexUsed1"/>否</label>
					</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">序号</td>
					<td><input name="indexNumber" type="text" class="inputSty allnumberVal" value="${num}" onblur="onblurVal(this,0,5)" title="请填入不超过5位数字" maxlength="5"></td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">指标说明：</td>
					<td class="secondTD">
						<textarea name="indexNotes" class="fontSize12 textareaSty allinstructionsVal" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)" maxlength="50">${indexTb.indexNotes}</textarea>
					</td>
				</tr>
			</table>
			<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
				</div>
		</div>
	</form>
	</div>
	<script>
	$(function(){                    
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});			
		$("#submitBtn").click(function(){		
				var indexName = $("#indexName").val();
				var indexType = $("input[name='indexType']");
				var indexUsed = $("input[name='indexUsed']");
				var indexNumber = $("input[name='indexNumber']").val();
				var indexNotes = $("textarea[name='indexNotes']").val();
				if(checkChineseNoSpe(indexName)==1) {
					layer.alert("指标大类名称不能为空",{ icon:2, shade:0.3, shouldClose:true });
	                $("#indexName").focus();
	                return false;
		        }
				if(checkChineseNoSpe(indexName)==0) {
					layer.alert("指标大类名称输入不合法",{ icon:2, shade:0.3, shouldClose:true });
	                $("#indexName").focus();
	                return false;
		       	}
				if(!indexType.is(":checked")){
					layer.alert("请选择指标类型",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				if(!indexUsed.is(":checked")){
					layer.alert("请选择指标大类是否启用",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				if(checkNumber(indexNumber)==0){
					layer.alert("请填入数字",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				if(indexNotes.length>50){
					layer.alert("请填入不超过50个字的说明",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				var loading = layer.load();
					$.post("${request.getContextPath()}/admin/indexTb/save.jhtml",$("#form").serialize(),function(data){
						
						var index = alertInfoFun(data.message, data.flag, function(){
							layer.close(loading);
							if(data.flag){
								if($("#indexId").val()!=""){
									parent.window.location.href = "${request.getContextPath()}/admin/indexTb/list.jhtml?indexId="+data.data+"";
								}else{
									parent.window.location.href = "${request.getContextPath()}/admin/indexTb/list.jhtml";
								}
							}
							layer.close(index);
						});
					});	
		});
	})
	</script>
</body>
</html>