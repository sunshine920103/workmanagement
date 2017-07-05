<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			
			//显示上级区域弹出框
			function openPop(){
				$("#covered").show();
				$("#poplayer").show();
				//判断浏览器是否为IE6  
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0){
				    $(".shouldHide").hide();
				}
			}
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0){
				    $(".shouldHide").show();
				}
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
				var seleced = $(".seleced"); //被选中的区域
				var type = $(seleced.find("input[type=hidden]")).val(); //被选中区域的类型
				var areaType = "${a.sysAreaType}"; //当前登陆区域的类型
				if(seleced.length===0){
					layer.alert("您还没有选择上级区域",{
						icon:2,
						shade:0.3
					});
					return false;
				}else if(areaType!==""){ 
					type = parseInt(type);
					areaType = parseInt(areaType);
					if(type < areaType){  //被选中地区的类型 大于 当前登陆区域的类型才可以保存
						layer.alert("不能将区域移动到比自身等级低的区域",{
							icon:2,
							shade:0.3
						});
						return false;
					}					
				}
				closePop();

				var selId = seleced.attr("id");//被选中区域的id
				$("#sysAreaUpid").val(selId);
				
				var area = $(seleced.find("label"));
				var areaName = area.text();
				
				$("#openPop").text(areaName);
				$("#pname").val(areaName);
				
				//设置区域类型下拉框
				resetAreaType(""+type);
				//将选择的上级区域的code的规定长度放置在区域代号输入框
				getUpCode(selId);
			}
			//将选择的上级区域的code的规定长度放置在区域代号输入框
			function getUpCode(selId){
				var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
				var id = Number(selId);
				$.ajax({
					url:url,
					type:"post",
					data:{id:id},
					success:function(data){
						var code = data.sysAreaCode;
						if(data.sysAreaType == "0"){
							document.getElementById("sysAreaCode").value=code.substring(0,2);
						}else if(data.sysAreaType == "1"){
							document.getElementById("sysAreaCode").value=code.substring(0,4); 
						}else if(data.sysAreaType == "2"){
							 document.getElementById("sysAreaCode").value=code;
						}  
				}
				})
			}
			
			//根据上级区域类型重新设置区域类型
			function resetAreaType(type){
				var areaType = $("#areaType");
				var s = $('<option value="0">省、直辖市</option>');
				var ds = $('<option value="1">地市</option>');
				var qx = $('<option value="2">区县</option>');
				var xz = $('<option value="3">乡镇</option>');
				var code1='<input id="sysAreaCode" name="sysAreaCode" class="inputSty required" maxlength="6" value="${a.sysAreaCode?substring(0,2)}" onblur="onblurVal(this,24,6)" />'
				var code2='<input id="sysAreaCode" name="sysAreaCode" class="inputSty required" maxlength="9" value="${a.sysAreaCode?substring(0,2)}" onblur="onblurVal(this,24,9)" />'

				$("#Areacode").html("");
				areaType.html("");
				if(type === "0"){
					$("#Areacode").append(code1)
					areaType.append(ds);
				}else if(type === "1"){
					$("#Areacode").append(code1)
					areaType.append(qx);
				}else if(type === "2"){
					$("#Areacode").append(code2)
					areaType.append(xz);
				}else if(type === "" && "${a}" !== ""){
					areaType.append(s);
					$("#Areacode").append(code1)
				}else{
					areaType.append(ds);
					$("#Areacode").append(code1)
				}
			}
			
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
						if(result!=null){
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
								var name = null;
								
								if(sub.sysAreaType==="3"){
									name = "<td id='"+sub.sysAreaId+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
											+"</td>";
								}else{
									name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
											+"</td>";
								}
								
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
		</script>
		<title>地区列表</title>
	</head>
	<body>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox">
	        	<div class="titleFont1">地区列表</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0">
						<#list aList as a>
						<tr>
							<td id="${a.sysAreaId}" onclick="selUpstream(this)">
								<#if (a.subArea?? && a.subArea?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${a.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label class="fontSize12">${a.sysAreaName}</label>
								<input  type="hidden" value="${a.sysAreaType}" />
							</td>
						</tr>
						</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消"  class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认"  class="sureBtn" onclick="confirmSel()"/>
	        	</div>
	        </div>  
	    </div>  
	
		<#-- 新增框 -->
		<div class="showListBox">
			<form id="form"  method="post">			
			<input name="sysAreaId" type="hidden" value="${a.sysAreaId}"/>
			<table cellpadding="0" cellspacing="0">
						<caption  class="titleFont1 titleFont1Ex">
							<#if poptype == "add" >地区新增<#else>地区修改</#if>
						</caption>
					<tr>
						<td  class="noBorderL firstTD" style="width:25%;">上级区域</td>
						<#if poptype == "add" >
							<td width="500" class="secondTD">
								<input id="pname" name="pname" type="hidden" value=""/>
								<input id="sysAreaUpid" name="sysAreaUpid" type="hidden" value=""/>
								<a id="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()">选地区</a>
								<span class=" fontSize12 warmFont">不能选择最后一级的地区</span>
							</td>
						<#else>
							<td class="secondTD" style="width:75%;">
								<span   style="color:#ccc;padding:5px;font-size:12px" >
								<#if a.sysAreaUpid =='0'>顶级<#else>${a.pname}</#if>
								<input id="pname" name="pname" type="hidden" value="${a.pname}"/>
								<input id="sysAreaUpid" name="sysAreaUpid" type="hidden" value="${a.sysAreaUpid}"/>
								</span>
							</td>
						</#if>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>区域名称</td>
						<#if poptype == "add">
						<td class="secondTD" style="position: relative;">
							<input id="sysAreaName" name="sysAreaName" class="inputSty required inputOtherCondition" value="" maxlength="50" onblur="onblurVal(this,13,50)" title="必填，不能超过50个字"/>
							<span   style="position: absolute;left: 15px;top: 10px;" class="warmFont fontSize12 fuck">输入下一级地区（请填中文）</span>
						</td>
						<#else>
						<td class="secondTD" style="position: relative;">
							<input id="sysAreaName" name="sysAreaName" class="inputSty required inputOtherCondition allChineseVal" maxlength="50" value="${a.sysAreaName}" onblur="onblurVal(this,1,50)" title="必填，不能超过50个字"/>
						</td>
						</#if>
					</tr>
					<tr> 
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>行政代码</td>
						<#if poptype == "add">
							<#if a.sysAreaType == '0'>                                       
							<td class="secondTD" id="Areacode">  
							<input id="sysAreaCode" name="sysAreaCode" class="inputSty required" value="${a.sysAreaCode?substring(0,2)}" maxlength="6" onblur="onblurVal(this,246,6)" />
							</td>
							<#elseif a.sysAreaType == '1'>
							<td class="secondTD" id="Areacode"> 
							<input id="sysAreaCode" name="sysAreaCode" class="inputSty required" value="${a.sysAreaCode?substring(0,4)}" maxlength="6" onblur="onblurVal(this,246,6)" />
							</td>
							<#else>                                                                                                    
							<td class="secondTD" id="Areacode"> 
							<input id="sysAreaCode" name="sysAreaCode" class="inputSty required" value="${a.sysAreaCode}" maxlength="9" onblur="onblurVal(this,246,9)" />
							</td>
							</#if>
						<#else>
							<td class="secondTD" id="Areacode"> 
							<input id="sysAreaCode" name="sysAreaCode" class="inputSty required"  value="${a.sysAreaCode}" <#if a.sysAreaType == '3'>maxlength="9" onblur="onblurVal(this,249,9)" <#else>maxlength="6" onblur="onblurVal(this,246,6)"</#if>/>
							</td>
						</#if>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>区域类型</td>
						<#if poptype == "add">
						<td class="secondTD">
							<select id="areaType" class="shouldHide inputSty" name="sysAreaType">

								<option value="2">区县</option>     

							</select>
						</td>
						<#else>
							<td class="secondTD">
							<select id="areaType" class="shouldHide inputSty" name="sysAreaType">
								<#if a.sysAreaType == '0'>
								<option value="0">省、直辖市</option>
								<#elseif a.sysAreaType == '1'>
								<option value="1">地市</option>     
								<#elseif a.sysAreaType == '2'>
								<option value="2">区县</option>
								<#else>
								<option value="3">乡镇</option>
								</#if>
							</select>
						</td>
						</#if>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sysAreaNotes" class="fontSize12 textareaSty" maxlength="50" style="border:1px solid #dadada;height:130px;padding:10px" onblur="onblurVal(this,14,50)">${a.sysAreaNotes}</textarea>
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
			var sysAreaNameold = $("#sysAreaName").val();
				var sysAreaCodeold = $("#sysAreaCode").val();
				var sysAreaNotesold = $("textarea[name='sysAreaNotes']").val();
			$("#submitBtn").click(function(){
				
				var sysAreaName = $("#sysAreaName").val();
				var sysAreaCode = $("#sysAreaCode").val();
				var sysAreaNotes = $("textarea[name='sysAreaNotes']").val();
				
				<#if poptype != "add" >
					if(sysAreaNameold==sysAreaName&&sysAreaCodeold==sysAreaCode&&sysAreaNotesold==sysAreaNotes){
						parent.layer.close(index); //执行关闭
						return false;
					}
				</#if>
				if(checkChineseNoSpe(sysAreaName)==1) {
					layer.alert("区域名称不能为空",{icon:2,shade:0.3,shouldClose:true});
	                $("#sysAreaName").focus();
	              	 return false;
		        }
				if(checkChineseNoSpe(sysAreaName)==0) {
						layer.alert("区域名称输入不合法",{icon:2,shade:0.3,shouldClose:true});
		                $("#sysAreaName").focus();
		                return false;
			    }
				if(administrativeCode(sysAreaCode)==1) {
					layer.alert("行政代码不能为空",{icon:2,shade:0.3,shouldClose:true});
	                $("#sysAreaCode").focus();
	                return false;
		        }
				if(administrativeCode(sysAreaCode)==0) {
						layer.alert("行政代码输入不合法",{icon:2,shade:0.3,shouldClose:true});
		                $("#sysAreaCode").focus();
		                return false;
				};
				var loading = layer.load();
				$.post("${request.getContextPath()}/admin/sysArea/save.jhtml?poptype=${poptype}",$("#form").serialize(),function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){
						if(data.flag){
							layer.load();
							parent.window.location.href = "${request.getContextPath()}/admin/sysArea/list.jhtml";
						}
						layer.close(index);
					});
				});
			
			});
			
			if($(".inputOtherCondition").val() != ""){
				$(this).next(".fuck").hide();
			}
			$(".inputOtherCondition").focus(function(){
					$(this).next(".fuck").hide();
			}).blur(function(){
				if($.trim($(this).val())==""){
					$(this).next(".fuck").show();
				}
			});
			$(".fuck").click(function(){
				$(this).prev(".inputOtherCondition").focus();
			})
	</script>	
</html>
