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
						icon: (msg=="操作成功")?1:2,
						time:15000,
						shade:0.3,
						shadeClose:true
					});
				}
				//默认第一个指标大类颜色为蓝色
				var indexId="${indexTb.indexId}";	
				if(indexId==""){
					$("#indexTbsListUl a").css("color","black");
					$("#indexTbsListUl a:eq(0)").css("color","rgb(56,165,226)");
				}
				
				//全选
				$("#selectAll").click(function(){
					var arr = $(".selectType");
					for(var i=0;i<arr.length;i++){ 
						arr[i].checked = ! arr[i].checked; 
											
					}
					$(".selectType").each(function(){
						if($(this).is(":checked")){
							$(this).parent().next().children("[name='checkedInput']").removeAttr("disabled");
						}else{
							$(this).parent().next().children("[name='checkedInput']").attr("disabled","disabled");
						}
					});
							
				});
				
				$(".selectType").click(function(){
					if($(this).is(":checked")){
						$(this).parent().next().children("[name='checkedInput']").removeAttr("disabled");
					}
					else{
							$(this).parent().next().children("[name='checkedInput']").attr("disabled","disabled");
					}
				});
			
				var len = $("[name='checkedInput']").length;
				var regInput = /^[1-9]$|^[1-9][0-9]$/;
				for(var i=0;i<len;i++){
					$("[name='checkedInput']")[i].onkeyup=function(i){
						return function(){
							var num = $("[name='checkedInput']")[i].value;
							if(regInput.test(num)==false){
								$("[name='checkedInput']")[i].value="";
								return false;
							}
						}
					}(i);
					$("[name='checkedInput']")[i].onafterpaste=function(i){
						return function(){
							var num = $("[name='checkedInput']")[i].value;
							if(regInput.test(num)==false){
								$("[name='checkedInput']")[i].value="";
								return false;
							}
						}
					}(i);
				}
			});

				


//			//显示上级区域弹出框
			function openPop(){
				$("#covered").show();
				$("#poplayer").show();
				//判断浏览器是否为IE6  IE6 
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
				    $(".shouldHide").hide();
				}
				
			}

						
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
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
								name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
										+icon
										+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
										+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
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
			//确认选择
			function confirmSel(){
				var seleced = $(".seleced");
				if(seleced.length==0){
					layer.alert("您还没有选择区域",{
						icon:2,
						shade:0.3
					});
				}else{
					closePop();
					var area = $(seleced.find("label"));
					$("#reportExcelTemplateAreaName").val(area.text());
					$("#sysAreaId").val(seleced.attr("id"));
				}
			}
			
			//单击指标大类显示指标项
			function indexPearent(id,obj){
				$("#selectAll").removeAttr("checked");
				$("#indexId").val(id);
				var obj=$(obj);
//				obj.parent().siblings().find("a").css("color","black");
				$("#indexTbsListUl a").css("color","black");
				obj.css("color","rgb(56,165,226)");
				obj.siblings().css("color","black");
				var url = "${request.getContextPath()}/admin/reportExcelTemplate/getIndexItemTbsJson.jhtml";
				$.post(url,{id:id},function(result){
						if(result!=null){
							var list = result.indexItemTbList;	
							var html="";
							for(var i = 0; i < list.length; i++){
								//指标项
								var indexItemTb = list[i];
								
									if(indexItemTb.indexItemEmpty==0){
										html +='<tr><td width="200"><input class="verticalMiddle"  type="checkbox"  checked="checked" disabled="disabled"  ><input class="verticalMiddle" name="indexItemCodes" type="hidden"    value='+indexItemTb.indexItemCode+'><span class="fontSize12 inlineBlock">'+indexItemTb.indexItemName+'</span><td width="250">序号：<input name="checkedInput" value="'+(i+1)+'" style="width:100px;height:20px;border:1px solid #CCC"/></tr>'
									}else{
										html +='<tr><td width="200"><input class="verticalMiddle selectType" name="indexItemCodes" type="checkbox" value='+indexItemTb.indexItemCode+'><span class="fontSize12 inlineBlock">'+indexItemTb.indexItemName+'</span><td width="250">序号：<input name="checkedInput" value="'+(i+1)+'" style="width:100px;height:20px;border:1px solid #CCC" disabled="disabled" /></tr>'
									}
									
							}
							$("#indexItemTbUl table").html(html);
						}
						
						
						var len = $("[name='checkedInput']").length;
						var regInput = /^[1-9]$|^[1-9][0-9]$/;
						for(var i=0;i<len;i++){
							$("[name='checkedInput']")[i].onkeyup=function(i){
								return function(){
									var num = $("[name='checkedInput']")[i].value;
									if(regInput.test(num)==false){
										$("[name='checkedInput']")[i].value="";
										return false;
									}
								}
							}(i);
							$("[name='checkedInput']")[i].onafterpaste=function(i){
								return function(){
									var num = $("[name='checkedInput']")[i].value;
									if(regInput.test(num)==false){
										$("[name='checkedInput']")[i].value="";
										return false;
									}
								}
							}(i);
						}
						$(".selectType").click(function(){
					if($(this).is(":checked")){
						$(this).parent().next().children("[name='checkedInput']").removeAttr("disabled");
					}
					else{
							$(this).parent().next().children("[name='checkedInput']").attr("disabled","disabled");
					}
				});
						
						
				});
				
			}
			
			
			
			
		</script>

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
			<form id="addData" action="${request.getContextPath()}/admin/reportExcelTemplate/editSubmit.jhtml" method="post">
				<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">增加模板</caption>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>模板名称</td>
						<td style="width:65%;" class="secondTD">
							<input  id="name" name="reportExcelTemplateName" class="inputSty allnameVal" value="${reportExcelTemplate.reportExcelTemplateName}" onblur="onblurVal(this,13,50)" maxlength="50"/>
							<input name="indexId" id="indexId" type="hidden" value="${allIndexId[0]}"/>
						</td>
					</tr>
                    <tr>
                        <td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>状态</td>
                        <td style="width:65%;" class="secondTD">
                            <input   type="radio" value="正常" checked="true" >正常</input>
                            <input   type="radio" value="禁用">禁用 </input>
                        </td>
                    </tr>
					<tr>
						<td  class="noBorderL firstTD"><label class="mainOrange"> * </label>所属区域</td>
						<td class="secondTD">
							<input readonly="readonly" style="font-size: 12px;" id="reportExcelTemplateAreaName" name="reportExcelTemplateAreaName" value="" />
							<a id="openPop" href="javascript:void(0);" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()"> 选择区域</a>
							<input type="hidden" id="sysAreaId" name="sysAreaId" value="" />
						</td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD">选择指标大类</td>
						<td class="secondTD">
                            <input   class="verticalMiddle" type="checkbox" checked="checked"  >
                            <span class="fontSize12  inlineBlock">行政许可信息</span>
                            <input   class="verticalMiddle" type="checkbox" >
                            <span class="fontSize12  inlineBlock">对外投资信息</span>
						</td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD">选择指标项</td>
						<td class="secondTD">
							<ul>
								<#--<span class="inlineBlock"><input class="verticalMiddle" type="checkbox" id="selectAll"><span class="fontSize12 inlineBlock margin52050 delFont"><b>全选</b></span></span>-->
								<input name="indexItemCodes" class="verticalMiddle" type="hidden" value="code_credit">
								<span class="inlineBlock"><input class="verticalMiddle" checked="checked" type="checkbox" disabled="disabled"><span class="fontSize12 inlineBlock margin52050">统一社会信用代码</span></span>
								<input type="hidden" id="orgSwitch" name="orgSwitch" value="${orgSwitch}"/>
								<#if orgSwitch==1>
								<input name="indexItemCodes" class="verticalMiddle" type="hidden" value="code_org">
								<span class="inlineBlock"><input class="verticalMiddle" checked="checked" type="checkbox" disabled="disabled"><span class="fontSize12 inlineBlock margin52050">组织机构代码</span></span>
								</#if>
                                </br>
                                <input   class="verticalMiddle" type="checkbox"   >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">企业名称</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">续存状态</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">行政区划</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">经营范围</span>
                                </br>
                                <input   class="verticalMiddle" type="checkbox"  >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">经营地址</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">企业住所</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">通讯地址</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">联系电话</span>
                                </br>
                                <input   class="verticalMiddle" type="checkbox"   >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">经济行业</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">规模特征</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">机构类型</span>
                                <input   class="verticalMiddle" type="checkbox" >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">注册时间</span>
                                </br>
                                <input   class="verticalMiddle" type="checkbox"  >
                                <span class="fontSize12  inlineBlock" style="margin-right: 10px">从业人数</span>
							</ul>
							<ul id="indexItemTbUl">
								<table border="" cellspacing="" cellpadding="" style="width: 100%;margin-top: 5px;">
								<#list indexItemTbList as item>
										<#assign num=0/>
										<#if num!=item.indexItemId>
											
												<#if item.indexItemEmpty==0>
												<tr>
													<td width="200"> 
														<input   class="verticalMiddle" type="checkbox" checked="checked"  disabled="disabled"   >
														<input name="indexItemCodes" class="verticalMiddle" type="hidden"   value="${item.indexItemCode}">
														<span class="fontSize12  inlineBlock">${item.indexItemName}</span>
													</td>
													<td width="250">
														序号：<input name="checkedInput" value="${item_index+1}" style="width:100px;height:20px;border:1px solid #CCC" maxlength=2/>
													</td>
												</tr>
												<#else>
												<tr>
													<td width="200">
														<input name="indexItemCodes" class="verticalMiddle selectType" type="checkbox" value="${item.indexItemCode}">
														<span class="fontSize12  inlineBlock">${item.indexItemName}</span>
													</td>
													<td width="250"> 
														序号：<input name="checkedInput" value="${item_index+1}" style="width:100px;height:20px;border:1px solid #CCC" disabled="disabled" maxlength=2/>
													</td>
												</tr>
												</#if>		
										</#if>
								</#list>
								</table>
							</ul>
						</td>
					</tr>
				</table>
				
				<div class="showBtnBox">
					<input type="button" onclick="window.location.href='${request.getContextPath()}/admin/reportExcelTemplate/list.jhtml'"
						class="cancleBtn closeThisLayer" value="取 消" />
					<input type="button" id="formVer" class="sureBtn" value="确 认" />
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
		//保存
		$("#formVer").click(function(){
			var name = $("#name").val();
			var reportExcelTemplateAreaName = $("[name='reportExcelTemplateAreaName']").val();
			var valIpt = $(":checked").parent().next().children("[name='checkedInput']");
			var orgSwitch=$("#orgSwitch").val();
			if($.trim(name)==""){
				layer.alert("EXCEL模板名称不能为空",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}
			if(checkChineseNoSpe(name) == 0){
				layer.alert("EXCEL模板名称输入不合法",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}
			if(reportExcelTemplateAreaName==""){
				layer.alert("请选择区域",{icon:2,shade:0.3,shouldClose:true});
				return false;
			} 
			if(orgSwitch==1){
				if($(":checked").size() <=2){ 
					layer.alert("请至少选择一个指标项",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
			}else{
				if($(":checked").size() <=1){ 
					layer.alert("请至少选择一个指标项",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
			}
			for(var i=0;i<valIpt.length;i++){
				var val = valIpt[i].value;
				if($.trim(val)==""){
					layer.alert("序号不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
			} 
			var loading = layer.load();
			$.post("${request.contextPath}/admin/reportExcelTemplate/editSubmit.jhtml",$("#addData").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						parent.window.location.href = "${request.getContextPath()}/admin/reportExcelTemplate/list.jhtml";							
					}
					layer.close(index);
				});
			});
		});
	});
	</script>
</html>