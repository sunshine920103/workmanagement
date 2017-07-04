<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8">
		<script type="text/javascript">	
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
			function openPop(num){
			        $("#covered").show();
			        $("#poplayer").show();
			        $("#poplayer").children(".zIndex").removeClass("zIndex");
			        if(num==1){
			            $(".hylb").show();
			            $(".jglb").hide();
						$(".dqlb").hide();
						$(".zflb").hide();
						$(".hylb").addClass("zIndex");
			        }else if(num==2){
			            $(".hylb").hide();
			            $(".jglb").show();
						$(".dqlb").hide();
						$(".zflb").hide();
						$(".jglb").addClass("zIndex");
						if($("#treeDemo").html()==""){
							var loading = layer.load();
							$.ajax({
								url:'${request.getContextPath()}/admin/manualEntry/getOrgLists.jhtml',
								dataType:'json',
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
			        }else if(num==3){
			            $(".hylb").hide();
			            $(".jglb").hide();
						$(".dqlb").hide();
						$(".zflb").show();
						$(".zflb").addClass("zIndex");
			        }else if(num==5){
			            $(".hylb").hide();
			            $(".jglb").hide();
						$(".dqlb").show();
						$(".zflb").hide();
						$(".dqlb").addClass("zIndex");
			        }
			    }
			    function selUpstream1(obj){
			      $.each($(".hylb .seleced"),function(){
			          $(this).removeClass("seleced");
			      });
			      $(obj).addClass("seleced");
			    }
			    
			    function selUpstream2(obj){
			      $.each($(".jglb .seleced"),function(){
			          $(this).removeClass("seleced");
			      });
			      $(obj).addClass("seleced");
			    }
			    
			   	function selUpstream3(obj){
				      $.each($(".zflb .seleced"),function(){
				          $(this).removeClass("seleced");
				      });
				      $(obj).addClass("seleced");
				    }
			    function selUpstream5(obj){
			      $.each($(".dqlb .seleced"),function(){
			          $(this).removeClass("seleced");
			      });
			      $(obj).addClass("seleced");
			    }
			    
			    //确认地区选择
			    function confirmSel5(){
			      var seleced = $(".dqlb .seleced");
			      closePop();
			      var area = $(seleced.find("label")).text(); 
			  	  var txt = seleced.text();
			  	  $(".dq").val(area);
			    }
			     //确认政府选择
			    function confirmSel3(){
			      var seleced = $(".zflb .seleced");
			      closePop();
			      var area = $(seleced.find("label")).text(); 
			  	  var txt = seleced.text();
			  	  $(".zf").val(area);
			    }
			    //确认行业选择
			    function confirmSel1(){
			      var seleced = $(".hylb .seleced");
			      closePop();
			      var area = $(seleced.find("label")).text(); 
			  	  var txt = seleced.text();
			  	  $(".hy").val(area);
			    }
			    
			
			     //确认选择机构
			function confirmSel2() {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				$(".jg").val(htext); 
			}
			 
			    
			    //关闭弹出框
			    function closePop(){
			        $("#covered").hide();
			        $("#poplayer").hide();
			    }
			//点击异议处理之后
			function objManage(){
				var indexId=$("#end").val();
				//归档时间
				//二码id
				//机构id
				var check_val=[];
				//var obj=document.getElementsByName("ckName"); //$("[name='ckName']")
				var obj=$(".ckName");
				for(k in obj){
					if(obj[k].checked){
						check_val.push(obj[k].value);
					}
				}
					var operateId=$("#operateId").val();
					var check=",";
					for(s in check_val){
						check+=check_val[s]+",";
					}
					$.post("${request.getContextPath()}/admin/adminObj/operate.jhtml",{check:check,indexId:indexId,yycl:1,ing:operateId},function(data){

					});
			}
		 
		</script>
		<title></title>
	</head>
	<body>
	
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
		       <div class="listBox" style="overflow: auto;">
				    <div class="zTreeDemoBackground left">
						<ul id="treeDemo" class="ztree"></ul>
					</div>
				</div>
				<p class="hide" id="hideorg"></p>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
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
		   
		</div>
		<div class="showListBox noBorder" >
			<table  cellspacing="0" cellpadding="0">
				<#if myIndexItemTbList?? !>
					<tr>
						<td  width='100' class='noBorderL firstTD'>统一社会信用代码<input type="hidden" name="indexId" value="${indexId}"/><input type="hidden" name="majorId" value="${majorId}"/></td>
						<#if indexName = "基本信息">
							<td  width='400' class="secondTD">
								<input type="text" class="inputSty" disabled="disabled" name="defaultIndexItemCodeCredit" value="${defaultIndexItem.codeCredit}" /><input type="hidden" name="defaultIndexItemId" value="${defaultIndexItem.defaultIndexItemId}">
								<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorDefaultmsg[0]}</span>
							</td>
						<#else>
							<td  width='400' class="secondTD">
								<input  type="text" class="inputSty" disabled="disabled" id="codeCredit" value="${defaultIndexItem.codeCredit}" /><input type="hidden" name="defaultIndexItemId" value="${defaultIndexItem.defaultIndexItemId}">
							</td>
						</#if>
					</tr>
					<tr>
						<td class='noBorderL firstTD'>组织机构代码</td>
						<#if indexName = "基本信息">
							<td class="secondTD">
								<input class="inputSty" type="text" disabled="disabled" name="defaultIndexItemCodeOrg" value="${defaultIndexItem.codeOrg}" />
								<span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorDefaultmsg[1]}</span>
							</td>
						<#else>
							<td class="secondTD">
								<input type="text" class="inputSty" disabled="disabled"  value="${defaultIndexItem.codeOrg}" />
							</td>
						</#if>
					</tr>
					<#list myIndexItemTbList as item>
					
						
						<tr>
							<td class='noBorderL firstTD'>${item.indexItemName}</td>
							<td  class="secondTD">
								<#if item.indexItemType==1>
									<input  autocomplete="off"  style="width:182px;height:21px;"   class="laydate-icon inputSty fontSize12" type="text" name="indexItemCode" value="${myValueList[item_index]}" onclick="laydate({istime: false,max: laydate.now(),format: 'YYYY-MM-DD'})" /><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
										<#list operateList as op>
											<#if item.indexItemId=op.INDEX_ITEM_ID>
												<#if op.MARK==1>
														<input id="itemId"  type="checkbox" name="ckName" class="ckName" value="${item.indexItemId}" />
														<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
														<#if type==8>
														<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
														</#if>
														<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
														<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
													<#else>
														<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
												</#if>
											</#if>
										</#list>
									<input id="itemCode" type="hidden"  name="itemCode" value="${item.indexItemCode}"   />
								<#else>
						
									
									<#if item.indexItemType==3 >
												
										<#if item.dicType==4 >
												<select class="inputSty" style="width:192px;height:27px;"  name="indexItemCode" > 
															<#list dicContentList as d >
																<#if d.dicId==item.dicId>
																	<option value="${d.dicContentValue}" <#if d.dicContentValue==myValueList[item_index]>selected</#if>>${d.dicContentValue}</option>
																</#if>
														</#list>
												</select>
													<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
													</#list>
													<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										<#elseif item.dicType==3>	
											<input class="inputSty zf" type="text" name="indexItemCode" value="${myValueList[item_index]}"  onclick="openPop(3)"/><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
													<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
													</#list>
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										
										<#elseif item.dicType==5>	
													<input class="inputSty dq" type="text" readonly="readonly" name="indexItemCode" value="${myValueList[item_index]}"  onclick="openPop(5)"/><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
													<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
													</#list>
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										<#elseif item.dicType==2>	
											<input class="inputSty jg" type="text" readonly="readonly" name="indexItemCode" value="${myValueList[item_index]}"  onclick="openPop(2)"/><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
												<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
												</#list>
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										<#else>	
											<input class="inputSty hy" type="text" readonly="readonly" name="indexItemCode" value="${myValueList[item_index]}"   onclick="openPop(1)"/><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
												<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
												</#list>
											<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
										</#if>		
									<#else>	
										<input class="inputSty" type="text" name="indexItemCode" value="${myValueList[item_index]}" /><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
											<#list operateList as op>
														<#if  item.indexItemId=op.INDEX_ITEM_ID>
														<#if op.MARK==1>
															<input id="itemId" type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
															<input id="operateId" type="hidden"   value="${op.SYS_OPERATE_ID}"   />
															<#if type==8>
																<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='${op.ORG_EXPLAIN}' class='inputSty'  /></td></tr>
															</#if>
															<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='${op.MAININFO_EXPLAIN}' class='inputSty' /></td></tr>
															<tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='${op.SERVER_EXPLAIN}' class='inputSty' disabled="disabled"/></td></tr>
														<#else>
															<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.SERVER_EXPLAIN}' disabled="disabled" class='inputSty'/></td></tr>
														</#if>
														</#if>
											</#list>
										<input id="itemCode" type="hidden" name="itemCode"  value="${item.indexItemCode}"   />
									</#if>
							</td>
						</tr>
						</#if>
					
					</#list>
				</#if>
			</table>
			<div class="showBtnBox">
				<input type="button" value="终 止"  style="background: #d4281b;" class="sureBtn sureBtnEx" onclick="stop()">
				<input type="hidden" value="${indexTbId}" class="sureBtn" id="end">
				<input id="dtbid" type="hidden"  value="${dtbId}"   />
				<input id="qb" type="hidden"  value="${qb}"   />
	        	<input type="button" value="取 消" class="cancleBtn closeThisLayer" >
	        </div>
		</div>
		<script type="text/javascript">
		
		
		
			index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			//点击继续按钮，将这条数据的状态更改为继续状态，存入数据库中
			function end(){
				var indexId=$("#end").val();
					var operateId=$("#operateId").val();
					$.post("${request.getContextPath()}/admin/adminObj/list.jhtml",{ing:operateId},function(data){
					window.location.href="${request.contextPath}/admin/adminObj/list.jhtml";
					});
			}
			//点击终止，获取被修改指标项的id及下属数据，存入数据库进行更新
			function stop(){
			//获取动态表id
			var dtbid=$("#dtbid").val();
			var qb=$("#qb").val();
			var operateId=$("#operateId").val();
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
				var indexItemCode =$("[name='indexItemCode']");
				//获取所有指标项的
				//var itemCode =document.getElementsByName("itemCode");
				var itemCode =$("[name='itemCode']");
				
				
				var itemCode_val=[];
				var indexItemCode_val=[];
				org_val=[];
				server_val=[];
				maininfo_val=[];
				
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
				check_val=[];
				//obj=document.getElementsByName("ckName");
				//obj=$(".ckName");
				obj =$("input[name='ckName']");
				for(k in obj){
					if(obj[k].checked){
						check_val.push(obj[k].value);
					}
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
					$.post("${request.getContextPath()}/admin/adminObj/operate.jhtml",{check:check,indexId:indexId,maininfo_val:maininfo_va,server_val:server_va,org_val:org_va,indexItemCode_val:indexItemCode_va,itemCode_val:itemCode_va,dtbid:dtbid,ing:operateId,yycl:2,a:1},function(data){
						layer.alert("操作成功",{icon:1,shade:0.3,shouldClose:true},function(){
							if(qb==1){
								parent.window.location.href="${request.contextPath}/admin/myPanel/index.jhtml";
						    }else{
								parent.window.location.href="${request.contextPath}/admin/adminObj/list.jhtml";
						    }
							
						}); 
					    
					});
			
			}
			$(function(){
				
				
				//页面加载的时候全部禁用
				$("[name = 'indexItemCode']").attr("disabled","disabled");
				$(".ckName").each(function(){
					$(this).attr("checked","checked").siblings(".inputSty").removeAttr("disabled").parents("tr").first().css("color","rgb(255,0,0)");
					//var str = "<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='message' value='' class='inputSty' /></td></tr><tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='org' value='' class='inputSty'  /></td></tr><tr><td class='noBorderL firstTD'  >服务中心说明</td><td class='secondTD'><input name='service' value='' class='inputSty'/></td></tr>";
					//$(this).parent().parent().after(str);	
				});

				
				//取消按钮
				$('.closeThisLayer').on('click', function(){
					parent.layer.close(index); //执行关闭
				});
			});
				//搜索按钮
	$("#searchBtn1").click(function(){
		var searchVal = $.trim($("#searchInput1").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable1");
		$.post("${request.getContextPath()}/admin/manualEntry/sysIndustrByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sysIndustryCode+"' onclick='selUpstream1(this)'><label>"+v.sysIndustryName+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput1").val(""));
			});
			layer.close(searchLoading);
		});
	});
	
	//搜索按钮
	$("#searchBtn2").click(function(){
		var searchVal = $.trim($("#searchInput2").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable2");
		$.post("${request.getContextPath()}/admin/manualEntry/sysOrgsByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sys_org_financial_code+"' onclick='selUpstream2(this)'><label>"+v.sys_org_name+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput2").val(""));
			});
			layer.close(searchLoading);
		});
	});
	
	//搜索按钮
	$("#searchBtn3").click(function(){
		var searchVal = $.trim($("#searchInput3").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable3");
		$.post("${request.getContextPath()}/admin/manualEntry/sysGoverByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sysGovFinancialCode+"' onclick='selUpstream3(this)'><label>"+v.sysGovName+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput3").val(""));
			});
			layer.close(searchLoading);
		});
	});
		</script>
	</body>
</html>
