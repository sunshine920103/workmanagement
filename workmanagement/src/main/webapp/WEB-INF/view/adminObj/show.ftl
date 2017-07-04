<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8">
		<style type="text/css">
			.message{
				padding: 2px 5px;
				border: 1px solid #dadada;
				height: 20px;
				font-size: 11px;
				width:180px;
			}
		</style>
		<script type="text/javascript">	
				$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg,{
						icon: (msg=="操作成功")?1:2,
						shade:0.3,
						shadeClose:true
					});
				}
			
			});
			
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
								var area = subs[i];
								//展开图标
								var icon = "";
								if(area.subArea!=null && area.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+area.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var tr = $("<tr name='"+area.sysAreaId+"' class='"+id+"'></tr>");
								var name = null;
								
								if(area.sysAreaType==="3"){
									name = "<td id='"+area.sysAreaId+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+area.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ area.sysAreaType +"' />"
											+"</td>";
								}else{
									name = "<td id='"+area.sysAreaId+"' onclick='selUpstream5(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+area.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ area.sysAreaType +"' />"
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
				//删除地区管理子区域
				function delSubArea(id){
					$.each($("."+id),function(i, v){
						var pid = v.attributes.getNamedItem("name").nodeValue;
						
						//删除子区域
						$(this).remove();
				
						//递归删除子区域
						delSubArea(pid);
					});
				}
			//点击异议处理之后
			function objManage(){
				//获取的服务中心的信息
				//var server =document.getElementsByName("service");
				//var itemCode =document.getElementsByName("itemId");
				var itemCode =$("input[name='itemId']");
				var server =$("input[name='service']");
				var defaultIndexItemCreditId =$("input[name='defaultIndexItemCodeCredit']").val();
				var defaultIndexItemOrgId =$("input[name='defaultIndexItemCodeOrg']").val();
				var indexId=$("#end").val();
				var ing=$("#ing").val();
				
				var server_val=[];
				for(var i=0;i<server.length;i++){
					server_val.push(server[i].value);
				}
				var server_va=",";
				for(s in server_val){
					server_va+=server_val[s]+",";
				}
				var itemCode_val=[];
				for(var i=0;i<itemCode.length;i++){
					itemCode_val.push(itemCode[i].value);
				}
				var itemCode_va=",";
				//alert(server_val.length)
				for(s in server_val){
					itemCode_va+=itemCode_val[s]+",";
				}
				//alert(itemCode_va);
				var indexId=$("#end").val();
				var ing=$("#ing").val();
				var valuePlay_val=[];
				var check_val=[];
				//obj=document.getElementsByName("ckName");
				var obj=$(".ckName");
				for(k in obj){
					if(obj[k].checked){
						check_val.push(obj[k].value);
						valuePlay_val.push(obj.eq(k).prev().val());
					}
				}
					var check=",";
					for(s in check_val){
						check+=check_val[s]+",";
					}
					var valuePlay=",";
					for(s in valuePlay_val){
						valuePlay+=valuePlay_val[s]+",";
					}
					if(check_val.length>0){
						$.post("${request.getContextPath()}/admin/adminObj/operate.jhtml",{check:check,indexId:indexId,ing:ing,yycl:1,server_val:server_va,itemCode_val:itemCode_va,defaultIndexItemid:defaultIndexItemOrgId,defaultIndexItemCreditId:defaultIndexItemCreditId,valuePlay:valuePlay,a:1},function(data){
						
							layer.alert("异议处理成功",{
							icon: 1,
							shade:0.3,
							shadeClose:true
						});
					
						});
					}else{
						layer.alert("请至少勾选一个异议操作",{
							icon: 2,
							shade:0.3,
							shadeClose:true
						});
					}
			}
			
		</script>
		<title></title>
	</head>
	<body>
		<div class="showListBox noBorder" >
			<table  cellspacing="0" cellpadding="0">
				<#if myIndexItemTbList?? !>
					<tr>
						<td  width='300' class='noBorderL firstTD'>统一社会信用代码<input type="hidden" name="indexId" value="${indexId}"/><input type="hidden" name="majorId" value="${majorId}"/></td>
						<#if indexName = "基本信息">
							<td  width='400' class="secondTD">
								<input type="text" class="inputSty"  value="${defaultIndexItem.codeCredit}" />
								<input type="hidden" name="defaultIndexItemId" value="${defaultIndexItem.defaultIndexItemId}">
								<input type="hidden" name="defaultIndexItemCodeCredit" value="${defaultIndexItem.defaultIndexItemId}">
								<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorDefaultmsg[0]}</span>
							</td>
						<#else>
							<td  width='400' class="secondTD">
								<input  type="text"       value="${defaultIndexItem.codeCredit}" />
								<input type="hidden" name="defaultIndexItemId" value="${defaultIndexItem.defaultIndexItemId}">
								<input type="hidden" name="defaultIndexItemCodeCredit" value="${defaultIndexItem.defaultIndexItemId}">
							</td>
						</#if>
					</tr>
					<tr>
						<td class='noBorderL firstTD'>组织机构代码</td>
						<#if indexName = "基本信息">
							<td class="secondTD">
								<input class="inputSty" type="text"  value="${defaultIndexItem.codeOrg}" />
								<input type="hidden" name="defaultIndexItemCodeOrg" value="${defaultIndexItem.defaultIndexItemId}">
								<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorDefaultmsg[1]}</span>
							</td>
						<#else>
							<td class="secondTD">
								<input type="hidden" name="defaultIndexItemCodeOrg" value="${defaultIndexItem.defaultIndexItemId}">
								<input type="text"  value="${defaultIndexItem.codeOrg}" />
							</td>
						</#if>
					</tr>
					<#list myIndexItemTbList as item> 
						<tr>
							<td class='noBorderL firstTD'>${item.indexItemName}</td>
							<td  class="secondTD">
								<#if item.indexItemType==1>
									<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />
									<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
									<input type="hidden"   value="${myValueList[item_index]}"   />
									<input id="itemId"  type="checkbox"  name="ckName" class="ckName" value="${item.indexItemId}" />
									<input type="hidden" name="itemId"  value="${item.indexItemId}" />
									<input id="itemCode" type="hidden"  name="itemCode" value="${item.indexItemCode}"   />
									服务中心说明：
									<input id="itemCode" type="text"  name="service" value="" class="message"  />
								<#else>
									<#if item.indexItemType==3 >
										<#if item.dicType==4 >
												<select class="inputSty" style="width:192px;height:27px;" disabled="disabled"  name="indexItemCode" > 
															<#list dicContentList as d >
																<#if d.dicId==item.dicId>
																	<option value="${d.dicContentCode}" <#if d.dicContentValue==myValueList[item_index]>selected</#if>>${d.dicContentValue}</option>
																</#if>
														</#list>
												</select>
											<input id="itemCode" type="hidden"  name="itemCode" value="${item.indexItemCode}"   />
											<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
											<input type="hidden"   value="${myValueList[item_index]}"   />
											<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"   />
											服务中心说明：
											<input id="itemCode" type="text"  name="service" value=""  class="message" />
										<#elseif item.dicType==3>
											<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />	 
											<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
											<input type="hidden"   value="${myValueList[item_index]}"   />
											<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"   />
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
											<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
											服务中心说明：
											<input id="itemCode" type="text"  name="service" value="" class="message"  />
										<#elseif item.dicType==2>	
											<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />
											<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
											<input type="hidden"   value="${myValueList[item_index]}"   />
											<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"  />
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
											<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
											服务中心说明：
											<input id="itemCode" type="text"  name="service" value=""  class="message" />
										<#elseif item.dicType==5>	
											<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />
											<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
											<input type="hidden"   value="${myValueList[item_index]}"   />
											<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"  />
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
											<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
											服务中心说明：
											<input id="itemCode" type="text"  name="service" value=""  class="message" />
										<#else>	
											<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />
											<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
											<input type="hidden"   value="${myValueList[item_index]}"   />
											<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"    />
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
											<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
											服务中心说明：
											<input id="itemCode" type="text"  name="service" value="" class="message"  />
										</#if>		
									<#else>	
										<input  style="width:182px;height:21px;"   disabled="disabled" class="inputSty fontSize12" type="text"   value="${myValueList[item_index]}"  />
										<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
										<input type="hidden"   value="${myValueList[item_index]}"   />
										<input id="itemId" type="checkbox" name="ckName" class="ckName"  value="${item.indexItemId}"   />
										<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										<input id="itemCode" type="hidden"  name="itemId" value="${item.indexItemId}"   />
										服务中心说明：
										<input id="itemCode" type="text"  name="service" value=""   class="message"/>
									</#if>
								</#if>
							</td>
						</tr>
						
					</#list>
				</#if>
			</table>
			<div class="showBtnBox">
				<input type="button" value="异议处理"  style="background: #d4281b;" class="sureBtn sureBtnEx" onclick="objManage()">
				<input type="button" value="终 止"   style="background: #d4281b;"  class="sureBtn sureBtnEx closeThisLayers" onclick="stop()">
				<input type="hidden" value="${indexTbId}" class="sureBtn" id="end">
				<input id="dtbid" type="hidden"  value="${dtbid}"   />
				<input id="IndexTableName" type="hidden"  value="${IndexTableName}"   />
				<input id="orgCreditCode" type="hidden"  value="${orgCreditCode}"   />
				<input id="reportOrgId" type="hidden"  value="${reportOrgId}"   />
				<input id="defaultId" type="hidden"  value="${defaultId}"   />
				<input type="button" value="继 续"   class="sureBtn sureBtnEx" onclick="end()">
	        	<input id="ing" type="hidden"  value="${ing}" />
	        	<input type="button" value="取 消" class="cancleBtn closeThisLayer" >
	        </div>
		</div>
		
		<div id="covered"></div>  
	<#-- 弹出框 -->
		<div id="covered"></div>
		<div id="poplayer">
		    <div class="borderBox hylb">
		        <div class="titleFont1">
		            <span>行业列表</span>
		        </div>
		        <div> </div>
		        <div class="listBox">
		        	<div>
		        		<input id="searchInput1" class="inputSty" type="text" value="" style="width: 140px;"/>
		        		<input id="searchBtn1" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
		            <table cellpadding="0" cellspacing="0" id="searchTable1">
		            <#list sysClassFyModel as it>
		                <tr>
		                    <td level="1" id="${it.sysIndustryCode}" onclick="selUpstream1(this)">
		                        <label>${it.sysIndustryName}</label>
		                    </td>
		                </tr>
		            </#list>
		            </table>
		        </div>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		        </div>
		    </div>
		    <div class="borderBox jglb">
		        <div class="titleFont1">
		            <span>机构列表</span>
		        </div>
		        <div class="listBox">
		        	<div>
		        		<input id="searchInput2" class="inputSty" type="text" value="" style="width: 140px;"/>
		        		<input id="searchBtn2" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
		            <table cellpadding="0" cellspacing="0" id="searchTable2">
		            <#list sysOrgList as it>
		                <tr>
		                    <td level="1" id="${it.sys_org_financial_code}" onclick="selUpstream2(this)">
		                        <label>${it.sys_org_name}</label>
		                    </td>
		                </tr>
		            </#list>
		            </table>
		        </div>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
		        </div>
		    </div>
		     <div class="borderBox dqlb">
		        <div class="titleFont1">
		            <span>行政区域</span>
		        </div>
		        <div class="listBox">
		            <table cellpadding="0" cellspacing="0" id="searchTable2">
		            <#list areaList as area>
		                <tr>
		                   
		                    <td level="1" id="${area.sysAreaCode}" onclick="selUpstream5(this)">
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="${area.sysAreaId}" name="0" class="open-shrink" onclick="openArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<span class="fll fontSize12">${area.sysAreaName}</span>
							</td>
		                </tr>
		            </#list>
		            </table>
		        </div>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel5()"/>
		        </div>
		    </div>
		    <div class="borderBox zflb">
		        <div class="titleFont1">
		            <span>政府列表</span>
		        </div>
		        <div class="listBox">
		        	<div>
		        		<input id="searchInput3" class="inputSty" type="text" value="" style="width: 140px;"/>
		        		<input id="searchBtn3" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
		            <table cellpadding="0" cellspacing="0" id="searchTable3">
		            <#list sysGoverList as it>
		                <tr>
		                    <td level="1" id="${it.sysGovFinancialCode}" onclick="selUpstream3(this)">
		                        <label>${it.sysGovName}</label>
		                    </td>
		                </tr>
		            </#list>
		            </table>
		        </div>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel3()"/>
		        </div>
		    </div>
		</div>
		<script type="text/javascript">
				//页面加载的时候全部禁用 
			$(".ckName").siblings(".inputSty").attr("disabled","disabled"); 
	
			//点击继续按钮，将这条数据的状态更改为继续状态，存入数据库中
			function end(){
				var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
				var indexId=$("#end").val();
				var defaultId=$("#defaultId").val();
				var reportOrgId=$("#reportOrgId").val();
				var orgCreditCode=$("#orgCreditCode").val();
				var IndexTableName=$("#IndexTableName").val();
				var dtbid=$("#dtbid").val();
				var ing=$("#ing").val();
				var defaultIndexItemCreditId =$("input[name='defaultIndexItemCodeCredit']").val();
				var defaultIndexItemOrgId =$("input[name='defaultIndexItemCodeOrg']").val();
				
				var valuePlay_val=[];
				var check_val=[];
				//obj=document.getElementsByName("ckName");
				var obj=$(".ckName");
				for(k in obj){
					if(obj[k].checked){
						check_val.push(obj[k].value);
						valuePlay_val.push(obj.eq(k).prev().val());
					}
				}
					var check=",";
					for(s in check_val){
						check+=check_val[s]+",";
					}
					var valuePlay=",";
					for(s in valuePlay_val){
						valuePlay+=valuePlay_val[s]+",";
					}
				$.post("${request.getContextPath()}/admin/adminObj/list.jhtml",{ing:ing,defaultIndexItemId:defaultIndexItemOrgId,defaultIndexItemCreditId:defaultIndexItemCreditId,valuePlay:valuePlay},function(data){
					if(data){
							layer.alert("推送成功",{
								icon: 1,
								shade:0.3,
								shadeClose:true
							},function(){
								parent.layer.close(index); //执行关闭
							});
							
					}else{
							layer.alert("请勾选存在异议的指标项再进行推送任务",{
								icon: 2,
								shade:0.3,
								shadeClose:true
							});
					}
				});
//					parent.window.location.href="${request.getContextPath()}/admin/adminObj/show.jhtml?ing="+ing+"&defaultId="+defaultId+"&orgCreditCode="+orgCreditCode+"&reportOrgId="+reportOrgId+"&id="+dtbid+"&IndexTableName="+IndexTableName+"&ceshi="+1;
			}
			
			
			
			//点击终止，获取被修改指标项的id及下属数据，存入数据库进行更新
			function stop(){
				//获取动态表id
				var dtbid=$("#dtbid").val();
				var ing=$("#ing").val();
				//获取的报数机构信息
				//var org =document.getElementsByName("org");
				var org =$("input[name='org']");
				//获取的服务中心的信息
				//var server =document.getElementsByName("service");
				var server =$("input[name='service']");
				//获取的信息主体的 信息
				//var maininfo =document.getElementsByName("message");
				var maininfo =$("input[name='message']");
				//获取的修改字段的值
				//var indexItemCode =document.getElementsByName("indexItemCode");
				var indexItemCode =$("input[name='indexItemCode']");
				//获取所有指标项的
				//var itemCode =document.getElementsByName("itemCode");
				var itemCode =$("input[name='itemCode']");
				
				var defaultIndexItemCreditId =$("input[name='defaultIndexItemCodeCredit']").val();
				var defaultIndexItemOrgId =$("input[name='defaultIndexItemCodeOrg']").val();
				
				var itemCode_val=[];
				var indexItemCode_val=[];
				var org_val=[];
				var server_val=[];
				var maininfo_val=[];
				
					for(var i=0;i<itemCode.length;i++){
						itemCode_val.push(itemCode[i].value);
					}
					for(var i=0;i<indexItemCode.length;i++){
						indexItemCode_val.push(indexItemCode[i].value);
					}
					for(var i=0;i<org.length;i++){
						org_val.push(org[i].value);
					}
					for(var i=0;i<server.length;i++){
						server_val.push(server[i].value);
					}
					for(var i=0;i<maininfo.length;i++){
						maininfo_val.push(maininfo[i].value);
					}
					
				var indexId=$("#end").val();
				var check_val=[];
				var valuePlay_val=[];
				//obj=document.getElementsByName("ckName");
				var obj = $("input[name='ckName']");
				//obj=$(".ckName");
				for(k in obj){
					if(obj[k].checked){
						check_val.push(obj[k].value);
						valuePlay_val.push(obj.eq(k).prev().val());
					}
				}
					var valuePlay=",";
					for(s in valuePlay_val){
						valuePlay+=valuePlay_val[s]+",";
					}
					var itemCode_va=",";
					for(s in indexItemCode_val){
						itemCode_va+=itemCode_val[s]+",";
					}
					var indexItemCode_va=",";
					for(s in indexItemCode_val){
						indexItemCode_va+=indexItemCode_val[s]+",";
					}
					var check=",";
					for(s in check_val){
						check+=check_val[s]+",";
					}
					var org_va=",";
					for(s in org_val){
						org_va+=org_val[s]+",";
					}
					var server_va=",";
					for(s in server_val){
						server_va+=server_val[s]+",";
					}
					var maininfo_va=",";
					for(s in maininfo_val){
						maininfo_va+=maininfo_val[s]+",";
					}
					
					$.post("${request.getContextPath()}/admin/adminObj/operate.jhtml",{check:check,indexId:indexId,maininfo_val:maininfo_va,server_val:server_va,org_val:org_va,indexItemCode_val:indexItemCode_va,itemCode_val:itemCode_va,dtbid:dtbid,ing:ing,yycl:2,a:9,defaultIndexItemid:defaultIndexItemOrgId,defaultIndexItemCreditId:defaultIndexItemCreditId},function(data){
						layer.alert("操作成功",{icon:1,shade:0.3,shouldClose:true},function(){
							parent.window.location.href="${request.contextPath}/admin/adminObj/list.jhtml";
						});
					});
			}
				
				//取消按钮
				
				var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
				$('.closeThisLayer').on('click', function(){
	    				parent.layer.close(index); //执行关闭
				});
	
		</script>
	</body>
</html>
