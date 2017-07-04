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
			$(function(){
				if($("#typeName").val()!=""){
					var str=$("#typeName").val()
					var loading = layer.load(); 
					$.ajax({
						url:'${request.getContextPath()}/admin/sysOrg/getOrgList.jhtml',
						dataType:'json',
						data:{"name":str},
						success:function(nodes){
							layer.close(loading);
							var str1 = [];
							for (var i = 0; i < nodes.length; i++) {
								var obj= new Object();
								 obj.id= nodes[i].id;
								 obj.name= nodes[i].name;
								 obj.pId= nodes[i].parent;
								 str1.push(obj);
							}
							orgtype(str1);
						}
					});
				}
			})

			/********************************************************
			 * 打开机构类型管理弹出框
			 * obj ： 被点击的元素（this）
			 * id : 需要查询子机构类型的机构类型ID
			 */
			function openInstitutionsType(obj, id){
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
					var url = "${request.getContextPath()}/admin/sysOrgType/getType.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						layer.close(loading);
						if(result!=null){
							var subs = result.subSysOrgType;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subSysOrgType!=null && sub.subSysOrgType.length!=0){
									icon = '<div name="0" class="open-shrink" onclick="openInstitutionsType(this,'+sub.sys_org_type_id+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
									var tr = $("<tr name='"+sub.sys_org_type_id+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.sys_org_type_id+"'  style='padding-left:"+spacing+"'>"
											+icon
											+"<label>"+sub.sys_org_type_name+"</label>"
											+"</td>";
								
								}else{
									var tr = $("<tr name='"+sub.sys_org_type_id+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.sys_org_type_id+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label>"+sub.sys_org_type_name+"</label>"
											+"</td>";
								
								}
								
								
						
								
								tr.append(name);
								
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
					delInstitutionsType(id);
				}
			}
			
			//删除机构类型管理子区域
			function delInstitutionsType(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();

					//递归删除子区域
					delInstitutionsType(pid);
				});
			}
			
	 
			/**********************************************************************************
			 * 打开地区管理弹框（机构地址）
			 * obj ： 被点击的元素（this）
			 * id : 需要查询子地区的地区ID
			 */
			function openInstiArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("id")==0){ //未展开
					//设置为展开状态
					obj.attr("id",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					var loading = wait();
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						layer.close(loading);
						if(result!=null){
							var subs = result.subArea;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subArea!=null && sub.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openInstiArea(this,'+sub.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								
								var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label>"+sub.sysAreaName+"</label>"
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
					delSubInstiArea(id);
				}
			}
			
			//删除地区管理子区域
			function delSubInstiArea(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();
			
					//递归删除子区域
					delSubArea(pid);
				});
			}
			
			//删除选择标记
			function delSelFlag(){
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});
			}
			
			//选择对象
			function selUpstream(obj){
				delSelFlag();
				$(obj).addClass("seleced");
			}
			//关闭上级区域弹出框
	        function closePop(){
	            $("#covered").hide();
	            $("#poplayer").hide();
	        }
			//打开弹窗
			function openPop(num){
	          
	            if(num==1){
	            	$("#jglb").show();
	                $("#sjjg").hide();
	                $("#jgdz").hide();
	   
	            }else if(num==2){
	            	if($("#typeId").val()==""){
	            		layer.alert("请先选择机构类别",{icon:2,shade:0.3,shouldClose:true});
	            		return false;
	            	}else{
	            		$("#jglb").hide();
		                $("#sjjg").show();
		                $("#jgdz").hide();
		                var area1=$("#jglbA").text();
		                var loading = layer.load(); 
						$.ajax({
							url:'${request.getContextPath()}/admin/sysOrg/getOrgList.jhtml',
							dataType:'json',
							data:{"name":area1},
							success:function(nodes){
								layer.close(loading);
								var str1 = [];
								for (var i = 0; i < nodes.length; i++) {
									var obj= new Object();
									 obj.id= nodes[i].id;
									 obj.name= nodes[i].name;
									 obj.pId= nodes[i].parent;
									 str1.push(obj);
								}
								orgtype(str1);
							}
						});
	            	}
	            	
	            }else{
	            	$("#jglb").hide();
	                $("#sjjg").hide();
	                $("#jgdz").show();
	            }
	            $("#covered").show();
	            $("#poplayer").show(); 
	        }
			
			
			//机构类别
			
			//请选择机构类别
			function confirmSel(clear) {
				var seleced = $("#jglb .seleced");
				closePop();
				var area1 = $(seleced.find("label")).text();
				var str = $(seleced).attr('id');
				
				if(clear == 1||seleced.length==0) {
					$("#jglbA").text("请选择机构类别");
					$("#typeName").val("");
					$("#typeId").val("");
				} else {
					$("#jglbA").text(area1);
					$("#typeName").val(area1);
					$("#typeId").val(str);
					$("#sjjgA").text("选择上级机构");
					$("#sys_org_upname").val("");
					$("#sys_org_upid").val("");
					$.ajax({
			    			url:'${request.getContextPath()}/admin/sysOrg/getType.jhtml',
			    			data:{"name":area1},
			    			success:function(d){
			    				$("#type").val(d);
			    				
			    			}
			   		 }); 
				}
			
			}
			
			function confirmSel1(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#sjjgA").text("请选择机构类别");
					$("#sys_org_upname").val("");
					$("#sys_org_upid").val("");
				} else { 
					$("#sjjgA").text(htext);
					$("#sys_org_upname").val(htext);
					$("#sys_org_upid").val(hteid);
				} 
				
			} 
			//确认选择机构
			function confirmSel2(clear) {
				closePop();
				var seleced = $("#jgdz .seleced"); 
				var area1 = $(seleced.find("label")).text();
				var str = $(seleced).attr('id'); 
				if(clear == 1||seleced.length==0) {
					$("#jgdzA").text("请选区域");
					$("#sys_area_id").val(""); 
				} else {
					$("#jgdzA").text(area1); 
					$("#sys_area_id").val(str);
				}
			
				
			} 
		</script>
		
		<title>查看/添加机构</title>
	</head>
	
	<body class="customProductQuery">
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	    	<div id="jglb" class="hide borderBox" >
	        	<div class="titleFont1">机构类别及总部列表</div>
	        	<div class="listBox" style="border-bottom: none;">
	        		<table cellpadding="0" cellspacing="0" id="searchTable1">
						<#list its as it>
						<tr>
							<td level="1" id="${it.sys_org_type_id}"  <#if !(it.subSysOrgType?? && it.subSysOrgType?size > 0)>  onclick="selUpstream(this)" </#if> >
								<#if (it.subSysOrgType?? && it.subSysOrgType?size > 0) >
									<div id="${i.sys_org_type_id}" name="0" class="open-shrink" onclick="openInstitutionsType(this, ${it.sys_org_type_id})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label>${it.sys_org_type_name}</label>
							</td>
						</tr>
						</#list> 
					</table>
	        	</div>
	        	<div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel()"/>
		    	</div>
        	</div> 
        	<div id="sjjg" class="hide borderBox">
	        	<div class="titleFont1">
	        		<span>上级机构列表</span>
        		</div>
	        	<div class="listBox" style="overflow: auto;">
		            	<div class="zTreeDemoBackground left">
							<ul id="treeDemo" class="ztree"></ul>
						</div>
		        </div>
		        <p class="hide" id="hideorg"></p>
	        	<div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		    	</div>
        	</div>
        	<div id="jgdz" class="hide borderBox">
	        	<div class="titleFont1">地区列表</div>
	        	<div class="listBox" style="border-bottom: none;">
	        		<table>
						<tr>
							<td id="${area.sysAreaId}"  onclick="selUpstream(this)">
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="0" class="open-shrink" onclick="openInstiArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label>${area.sysAreaName}</label>
							</td>
						</tr>
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
			<form id="form"  method="post">
			
				<input name="sys_org_id" type="hidden" value="${i.sys_org_id}"/>
				
				<table class="centerTable" cellpadding="0" cellspacing="0">
					<#if i == null>
						<caption class="titleFont1 titleFont1Ex">增加机构</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改机构</caption>
					</#if>
					
					
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>机构类别及总部</td>
						<td class="secondTDnd">
						<input id="typeName" name="typeName" type="hidden" class="required" value="${i.sys_org_type_name}"/>
						<input id="type" name="type" type="hidden" class="required" value="${i.sys_org_type}"/>
						<input id="typeId" name="sys_org_type_id" type="hidden" class="required" value="${i.sys_org_type_id}" title="机构类别或总部不能为空"/>
							<a id="jglbA" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop(1)">${(i.sys_org_type_id??)?string(i.sys_org_type_name,'选择机构类别')}</a>
							<span class=" fontSize12 warmFont" >必选，机构类别及总部,只能选择最后一级</span>
						</td>
					</tr>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>机构编码</td>
						<td style="width:65%;" class="secondTDnd">
							<input id="sys_org_financial_code" name="sys_org_financial_code" type="text" class="inputSty required allorgcodeVal" value="${i.sys_org_financial_code}" onblur="onblurVal(this,23,14)" title="必填，不能超过14个字" maxlength="14"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>机构名称</td>
						<td class="secondTDnd">
							<input id="sys_org_name" type="hidden" value="${i.sys_org_id}">
							<input name="sys_org_name" type="text" class="inputSty required allnameVal" value="${i.sys_org_name}" onblur="onblurVal(this,13,30)" title="必填，不能超过30个字" maxlength="30"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">上级机构</td>
						<td class="secondTDnd">
							<input id="sys_org_upname" name="sys_org_upname" type="hidden" value="${i.sys_org_upname}"/>
							<input id="sys_org_upid" name="sys_org_upid" type="hidden" value="${i.sys_org_upid}"/>
							<a id="sjjgA" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop(2)">${(i.sys_org_upid??)?string(i.sys_org_upname,'选择上级机构')}</a>
							<span class=" fontSize12 warmFont">不选择时，默认为总行</span>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>机构所在地</td>
						<td class="secondTDnd">
							<input id="sys_area_id" name="sys_area_id" type="hidden" class="required" value="${i.sys_area_id}" title="机构地址不能为空"/>
							<a id="jgdzA" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop(3)">${(i.sys_org_address_area_name??)?string(i.sys_org_address_area_name,'选择区域')}</a>
							<span class=" fontSize12 warmFont">必选，例：四川省-成都市-武侯区</span>
						</td>
					</tr>
					
					<tr>
						<td class="noBorderL firstTD">电话</td>
						<td class="secondTDnd">
							<input name="sys_org_phone" type="text" maxlength="20" class="inputSty alltalVal" value="${i.sys_org_phone}" onblur="onblurVal(this,3,0)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">地址</td>
						<td class="secondTDnd">
							<input name="sys_org_address" type="text" class="inputSty alldressVal" value="${i.sys_org_address}" onblur="onblurVal(this,13,50)" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">法定代表人/负责人</td>
						<td class="secondTDnd">
							<input name="sys_org_representative" type="text" class="inputSty allnameVal" value="${i.sys_org_representative}" onblur="onblurVal(this,13,50)" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">机构经办人</td>
						<td class="secondTDnd">
							<input name="sys_org_finance_operator" type="text" class="inputSty allnameVal" value="${i.sys_org_finance_operator}" onblur="onblurVal(this,13,50)" maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">经办人联系电话</td>
						<td class="secondTDnd">
							<input name="sys_org_finance_operator_phone" type="text" maxlength="20" class="inputSty alltalVal" value="${i.sys_org_finance_operator_phone}" onblur="onblurVal(this,3,0)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">组织机构代码</td>
						<td class="secondTDnd">
							<input name="sys_org_code" type="text" class="inputSty allorgnumVal" value="${i.sys_org_code}" onblur="onblurVal(this,12,0)" maxlength="10"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">统一社会信用代码</td>
						<td class="secondTDnd">
							<input name="sys_org_credit_code" type="text" class="inputSty allcreditcodeVal" value="${i.sys_org_credit_code}" onblur="onblurVal(this,11,0)" maxlength="18"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">许可证</td>
						<td class="secondTDnd">
							<input name="sys_org_licence" type="text" class="inputSty allturecardVal" value="${i.sys_org_licence}" onblur="onblurVal(this,10,20)"  maxlength="20"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">注册资本</td>
						<td class="secondTDnd">
							<input name="sys_org_reg_capital" type="text" class="inputSty allmonnyVal" value="${i.sys_org_reg_capital}" onblur="onblurVal(this,8,20)"  maxlength="20"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">实际开立日期</td>
						<td class="secondTDnd">
							<input name="sys_org_issuance_day" style="width:187px;height:15px" onclick="laydate({istime: false, format: 'YYYY-MM-DD', max:laydate.now()})" class="inputSty laydate-icon" value="${(i.sys_org_issuance_day?string('yyyy-MM-dd'))!''}">
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">代码证编号</td>
						<td class="secondTDnd">
							<input name="sys_org_code_number" type="text" class="inputSty allnumzmVal" value="${i.sys_org_code_number}" onblur="onblurVal(this,2,20)"  maxlength="20"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">登记号</td>
						<td class="secondTDnd">
							<input name="sys_org_reg_number" type="text" class="inputSty allnumzmVal" value="${i.sys_org_reg_number}" onblur="onblurVal(this,2,20)"  maxlength="20"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">服务中心名称</td>
						<td class="secondTDnd">
							<input name="sys_org_service_center_name" type="text" class="inputSty allnameVal" value="${i.sys_org_service_center_name}" onblur="onblurVal(this,13,50)"  maxlength="50"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">服务中心电话</td>
						<td class="secondTDnd">
							<input name="sys_org_service_center_call" type="text" class="inputSty alltalVal" value="${i.sys_org_service_center_call}" onblur="onblurVal(this,3,20)"  maxlength="20"/>
						</td>
					</tr>
					<!--<tr>
						<td class="noBorderL firstTD">上传信用报告底纹</td>
						<td class="secondTDnd">
							<input name="file" type="file" class="inputSty file" />
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">logo</td>
						<td class="secondTDnd">
							<input name="file1" type="file" class="inputSty file" />
						</td>
					</tr>-->
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTDnd">
							<textarea name="sys_org_notes" style="border: 1px solid #dadada;" class="textareaSty fontSize12" onblur="onblurVal(this,14,50)" maxlength="50">${i.sys_org_notes}</textarea>
						</td>
					</tr>
				</table>
				
				<div class="showBtnBox">
					<input type="button"  class="cancleBtn closeThisLayer" value="取 消" />
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
	
		$("#submitBtn").click(function(){
			
			if(checkSysCode($("input[name='sys_org_financial_code']").val())==1) {
				layer.alert("机构编码不能为空",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if(checkSysCode($("input[name='sys_org_financial_code']").val())==0) {
				layer.alert("机构编码输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	       }
			
			
			if(checkChineseNoSpe($("input[name='sys_org_name']").val())==1) {
				layer.alert("机构名称不能为空",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if(checkChineseNoSpe($("input[name='sys_org_name']").val())==0) {
				layer.alert("机构名称输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	       }
			if($("input[name='sys_org_type_id']").val() == "") {
				layer.alert("请选择机构类别及总部",{icon:2,shade:0.3,shouldClose:true});
                return false;
	       }
			
			if($("input[name='sys_area_id']").val() == "") {
				layer.alert("请选择机构地址",{icon:2,shade:0.3,shouldClose:true});
                return false;
	       }
			if($("input[name='sys_org_phone']").val()  != "" && checkTel($("input[name='sys_org_phone']").val()) == 0) {
				layer.alert("电话输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	       }
			
			if($("input[name='sys_org_representative']").val() != "" && checkChineseNoSpe($("input[name='sys_org_representative']").val()) == 0) {
				layer.alert("法定代表人/负责人输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			
			if($("input[name='sys_org_finance_operator']").val() != "" && checkChineseNoSpe($("input[name='sys_org_finance_operator']").val()) == 0) {
				layer.alert("金融机构经办人输入不合法",{icon:2,shade:0.3,shouldClose:true}) 
                return false;
	        }
			if($("input[name='sys_org_finance_operator_phone']").val() != "" && checkTel($("input[name='sys_org_finance_operator_phone']").val()) == 0) {
				layer.alert("经办人联系电话输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_code']").val() != "" && checkSureZZ($("input[name='sys_org_code']").val()) == 0) {
				layer.alert("组织机构代码输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_credit_code']").val() != "" && checkSureTY($("input[name='sys_org_credit_code']").val()) == 0) {
				layer.alert("统一社会信用代码输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_licence']").val() != "" && checkStrAddNumSpe($("input[name='sys_org_licence']").val()) == 0) {
				layer.alert("许可证输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_reg_capital']").val() != "" && checkPrice($("input[name='sys_org_reg_capital']").val()) == 0) {
				layer.alert("注册资本输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_code_number']").val() != "" && checkStrAddNum($("input[name='sys_org_code_number']").val()) == 0) {
				layer.alert("代码证编号输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	       }
			if($("input[name='sys_org_reg_number']").val() != "" && checkStrAddNum($("input[name='sys_org_reg_number']").val()) == 0) {
				layer.alert("登记号输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			
			if($("input[name='sys_org_address']").val() != "" && checklt50($("input[name='sys_org_address']").val()) == 0) {
				layer.alert("地址输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_service_center_name']").val() != "" && checkChineseNoSpe($("input[name='sys_org_service_center_name']").val()) == 0) {
				layer.alert("服务中心名称不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("input[name='sys_org_service_center_call']").val() != "" && checkTel($("input[name='sys_org_service_center_call']").val()) == 0) {
				layer.alert("服务中心电话不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("textarea[name='sys_org_notes']").val() != "" && checkChineseNoSpe50($("textarea[name='sys_org_notes']").val()) == 0) {
				layer.alert("备注输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("textarea[name='sys_org_notes']").val() != "" && checkChineseNoSpe50($("textarea[name='sys_org_notes']").val()) == 0) {
				layer.alert("备注输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("textarea[name='sys_org_notes']").val() != "" && checkChineseNoSpe50($("textarea[name='sys_org_notes']").val()) == 0) {
				layer.alert("备注输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
                return false;
	        }
			if($("#sys_org_upid").val()!=""&&$("#sys_org_name").val()!="" && $("#sys_org_upid").val()==$("#sys_org_name").val()){
					layer.alert("不能选择自己为上级机构",{icon:2,shade:0.3,shouldClose:true});
	                return false; 
			} 
			var loading = layer.load(); 
			$.post("${request.getContextPath()}/admin/sysOrg/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						parent.window.location.href = "${request.getContextPath()}/admin/sysOrg/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
	
	</script>	
</html>
