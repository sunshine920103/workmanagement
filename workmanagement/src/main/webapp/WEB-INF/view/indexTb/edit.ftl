<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8">
		<title>编辑指标项</title>
	</head>
	<script type="text/javascript">
		//显示上级区域弹出框
			function openPop(num){
				$("#covered").show();
				$("#poplayer").show();
				if(num==0){
					$(".sjzd").show()
					$(".dqlb").hide()
				}else if(1){
					$(".dqlb").show()
					$(".sjzd").hide()
				}
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
	        function confirmSel(clear){
	            var seleced = $(".seleced");
	            closePop();
	            var	dic = $(seleced.find("label")).text();
	             var	dic1 = $(seleced.find("span")).text();
	           if(seleced.length==0){
					alert("您还没有选择数据字典");
				}else{
	                $("#openPop").text(dic);
	                $("#disc").val(dic1)
	            }
	
	        }
			function confirmSel1(){
				var seleced = $(".seleced");
				if(seleced.length==0){
					alert("您还没有选择上级区域");
				}else{
					closePop();
					var area = $(seleced.find("label"));
					$("#openPop1").text(area.text());
					$("#parent").val(seleced.attr("id"));
					$("input[name='queryProductTemplateAreaName']").val(area.text());
					$("input[name='queryProductTemplateAreaId']").val(seleced.attr("id"));
				}
			}
			
			$(function(){
				$("#tbCode0").click(function(){
					$("#Choose").show(); 
					$("#isEmpty1").prop("checked",'true'); 
					$("#isEmptylab0").hide();  
				})
				$("#tbCode1").click(function(){
					$("#Choose").show();
					$("#isEmptylab1").show();
					$("#isEmptylab0").show();
				})
				if($("#tbCode0").is(':checked')){ 
					$("#isEmptylab0").hide(); 
				}else{
					$("#isEmptylab0").show();
				}
			})
			$(function(){
				$("#addDic").click(function () {
				  parent.window.location.href = '${request.contextPath}/admin/dic/add.jhtml';
            	});
			})
	</script>
	<body>
		<#-- 弹出框 -->
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox dqlb">
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
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel1()"/>
	        	</div>
	        </div>  
	        <div class="borderBox sjzd">
	        	<div class="titleFont1">数据字典</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0">
	        		<#list dicList as li>
						<tr>
							<td style="font-size:12px"  onclick="selUpstream(this)">
								<span style="display:none" id="${li.dicId}">${li.dicId}</span><label id="${li.dicName}">${li.dicName}</label>
							</td>
						</tr>
	        		</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel()"/>
	        	</div>
	        </div>  
	    </div>
	<div>
		<div class="showListBox">
			<form id="addData" method="post" >
           		<input name="theDicId" type="hidden" value="" id="disc"/>
           		<input name="indexId" id="indexIdHindden" type="hidden" value="${indexT.indexId}"/>
           		<input name="indexName" type="hidden" value="${indexT.indexName}" />           		
            	<table id="edit" cellpadding="0" cellspacing="0">
					<#if typ == 0>
               			 <caption class="titleFont1 titleFont1Ex">新增指标项</caption>
					<#else>
               		 	 <caption class="titleFont1 titleFont1Ex">修改指标项</caption>
					</#if>
                	<tr>
                    	<td class="noBorderL firstTD" width="400"><label class="mainOrange"> * </label>指标项名称</td>
                    	<td width="800">
                    	<#if indexitem??> 
	                    		<input type="text" disabled="disabled" value="${indexitem.indexItemName}" name="indexItemName" class="inputSty" id="indexItemName"  />
                    	<#else> 
	                    		<input type="text" value="" name="indexItemName" class="inputSty" id="indexItemName" onblur="onblurVal(this,26,20)"/>  
                    	</#if>
                    		<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入汉字、字母和和特殊字符（）_ 的组合）</span>
                    	</td>
                	</tr>
               	 	<tr>
               	 		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>数据类型</td>
                    	<td class="td_click">
                    	<#if indexitem??  > 
                    			<label for="tbType0"><input type="radio" value="0" disabled="disabled" name="indexItemType" id="tbType0" <#if indexitem.indexItemType == "0"> checked="checked" </#if> />字符</label>
                    			<label for="tbType1"><input type="radio" value="1" disabled="disabled" name="indexItemType" id="tbType1" <#if indexitem.indexItemType == "1"> checked="checked" </#if> />时间</label>
                    			<label for="tbType2"><input type="radio" value="2" disabled="disabled" name="indexItemType" id="tbType2" <#if indexitem.indexItemType == "2"> checked="checked" </#if> />数值</label>
                    			<label for="tbType3"><input type="radio" value="3" disabled="disabled" name="indexItemType" id="tbType3" <#if indexitem.indexItemType == "3"> checked="checked" </#if> />数据字典 </label>
                    	<#else >
                    		<label for="tbType0"><input type="radio" value="0" name="indexItemType" id="tbType0" checked="checked"/>字符</label>
                    		<label for="tbType1"><input type="radio" value="1" name="indexItemType" id="tbType1"/>时间</label>
                    		<label for="tbType2"><input type="radio" value="2" name="indexItemType" id="tbType2"/>数值</label>
                    		<label for="tbType3"><input type="radio" value="3" name="indexItemType" id="tbType3"/>数据字典</label>
                    	</#if>
                    	</td>
                	</tr>
                	<#--<tr class="tr_hide">
                    <#if indexitem?? >
                		<#if indexitem.indexItemType == 0 && orgFlag==0>
                			<td class="noBorderL firstTD"><label class="mainOrange"> * </label><label class="b_clic">字符长度</label></td>             	 		          
                    		<td>
                    			<input type="text" name="varLength" readonly="readonly" class="inputSty String" onblur="onblurVal(this,0,5)" value="${indexitem.varLength}" />
                    		</td>
                    	<#elseif indexitem.indexItemType == 0 && orgFlag==1>
                    		<td class="noBorderL firstTD"><label class="mainOrange"> * </label><label class="b_clic">字符长度</label></td>             	 		          
                    		<td>
                    			<input type="text" name="varLength"  class="inputSty String" onblur="onblurVal(this,0,5)" value="${indexitem.varLength}" />
                    		</td>
                		<#elseif indexitem.indexItemType == 3 >
                			<td class="noBorderL firstTD"><label class="mainOrange"> * </label><label class="b_clic">数据字典</label></td>             	 		          
                    		<td>
                    			<span id="openPop"  style="color: #ccc;"  >${dic.dicName}</span> 
                    		</td>
                    		
                		</#if>
                	<#else>  
               	 		<td class="noBorderL firstTD"><label class="mainOrange"> * </label><label class="b_clic">字符长度</label></td>             	 		          
                    	<td>
                    		<label class="String1">
                    		<#if indexitem?? && orgFlag==1>
                    		<input type="text" name="varLength" class="inputSty " onblur="onblurVal(this,0,5)" value="${indexitem.varLength}"  />
                    		<#elseif indexitem?? && orgFlag==0>
                    		<input type="text" readonly="readonly" name="varLength" class="inputSty " onblur="onblurVal(this,0,5)" value="${indexitem.varLength}"  />
                    		<#else>
                    		<input type="text" name="varLength" class="inputSty " onblur="onblurVal(this,0,5)" value="255"  />
                    		</#if>
                    		</label>
                    		<label class="openPop1 hide">
                    			<span id="openPop"  style="color: #0088CC;"  onclick="openPop(0)">数据字典   </span> 
                    			<a id="addDic" style="color: red;margin-left:30px ">新增数据字典</a>
                    		</label>
                    	</td>
                	</#if>
                	</tr>-->
                	<tr>
                		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>是否为识别码</td>
                		<#if indexitem?? && orgFlag==1>
                			<td>
	                			<#if indexitem.indexItemImportUnique == "1">
	                				<label for="tbCode0"><input type="radio" value="1" name="indexItemImportUnique" id="tbCode0" checked="checked"/>是</label>
	                				<label for="tbCode1"><input type="radio" value="0" name="indexItemImportUnique" id="tbCode1"/>否</label> 
	                			<#else> 
	                				<#if indexitem.indexItemType == "0">
	                				<label for="tbCode0"><input type="radio" value="1" name="indexItemImportUnique" id="tbCode0" />是</label>
	                				<label for="tbCode1"><input type="radio" value="0" name="indexItemImportUnique" id="tbCode1" checked="checked" />否</label>
	                				<#else>  
	                					<label for="tbCode1"><input type="radio" value="0" name="indexItemImportUnique" id="tbCode1" checked="checked" />否</label>
	                				</#if>
	                			</#if>
                			</td>
                		<#elseif  indexitem?? && orgFlag==0>
                			<td>
                				<#if indexitem.indexItemImportUnique == "1"> 
                				<label for="tbCode0"><input  type="radio" value="1" name="indexItemImportUnique" id="tbCode0" checked="checked"/>是</label>
                				<label for="tbCode1"><input disabled="disabled" type="radio" value="0" name="indexItemImportUnique" id="tbCode1"/>否</label> 
                				<#else> 
                				<label for="tbCode0"><input disabled="disabled" type="radio" value="1" name="indexItemImportUnique" id="tbCode0" />是</label>
                				<label for="tbCode1"><input  type="radio" value="0" name="indexItemImportUnique" id="tbCode1" checked="checked" />否</label> 
                				</#if>
                			</td>
                		<#else>
                		<td>
                			<label for="tbCode0"><input type="radio" value="1" name="indexItemImportUnique" id="tbCode0" />是</label>
                			<label for="tbCode1"><input type="radio" value="0" name="indexItemImportUnique" id="tbCode1" checked="checked"/>否</label>
                		</td>
                		</#if>
                	</tr>
                	<tr id="Choose">
                		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>能否为空</td>
                		<#if indexitem?? && orgFlag==1>
                			<#if indexitem.indexItemEmpty == "1">
	                		<td>
	                			<label for="isEmpty0" id="isEmptylab0"><input type="radio" value="1" name="indexItemEmpty" id="isEmpty0" checked="checked"/>能</label>
	                			<label for="isEmpty1" id="isEmptylab1"><input type="radio" value="0" name="indexItemEmpty"  id="isEmpty1" />否</label>
	                		</td>
	                		<#else>
	                			<td>
	                			<label for="isEmpty0" id="isEmptylab0"><input type="radio" value="1" name="indexItemEmpty" id="isEmpty0" />能</label>
	                			<label for="isEmpty1" id="isEmptylab1"><input type="radio" value="0" name="indexItemEmpty" id="isEmpty1" checked="checked"/>否</label>
	                			</td>
	                		</#if>
                		<#elseif indexitem?? && orgFlag==0>
                			<#if indexitem.indexItemEmpty == "1">
	                		<td>
	                			<label for="isEmpty0" id="isEmptylab0"><input type="radio" value="1" name="indexItemEmpty" id="isEmpty0" checked="checked"/>能</label>
	                			<label for="isEmpty1" id="isEmptylab1"><input disabled="disabled" type="radio" value="0" name="indexItemEmpty"  id="isEmpty1" />否</label>
	                		</td>
	                		<#else>
	                			<td>
	                			<label for="isEmpty0" id="isEmptylab0"><input disabled="disabled" type="radio" value="1" name="indexItemEmpty" id="isEmpty0" />能</label>
	                			<label for="isEmpty1" id="isEmptylab1"><input type="radio" value="0" name="indexItemEmpty" id="isEmpty1" checked="checked"/>否</label>
	                			</td>
	                		</#if>
                		<#else>
                		<td>
                			<label for="isEmpty0" id="isEmptylab0"><input type="radio" value="1" name="indexItemEmpty" id="isEmpty0" checked="checked"/>能</label>
                			<label for="isEmpty1" id="isEmptylab1"><input type="radio" value="0" name="indexItemEmpty" id="isEmpty1" />否</label>
                		</td>
                		</#if>
                	</tr>
                	<tr>
                		<td class="chooseCustomProductQueryTermsTD1st noBorderL firstTD"><label class="mainOrange"> * </label>所属区域</td>
                		<#if indexitem??>
                		<td class="chooseCustomProductQueryTermsTD2nd" name="sysAreaName">崇左市</td>
                		<#else>
                		<td class="chooseCustomProductQueryTermsTD2nd" name="sysAreaName">崇左市</td>
                		</#if>
                	</tr>
                	<#--<tr>
                		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>网络标识</td>
                		<#if indexitem??>
                		<td>
                			<#if indexitem.indexItemNetId == "1">
	                			<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" />A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" checked="checked"/>B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2"/>C-互联网</label>
	                		<#elseif indexitem.indexItemNetId == "0">
                				<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" checked="checked"/>A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" />B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" />C-互联网</label>
                			<#elseif indexitem.indexItemNetId == "2">
                				<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" />A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" />B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" checked="checked"/>C-互联网</label>
	                		<#elseif indexitem.indexItemNetId == "0,1">
	                			<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" checked="checked"/>A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" checked="checked"/>B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" />C-互联网</label>
	                		<#elseif indexitem.indexItemNetId == "0,2">
	                			<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" checked="checked"/>A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" />B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" checked="checked"/>C-互联网</label>
	                		<#elseif indexitem.indexItemNetId == "1,2">
	                			<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" />A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" checked="checked"/>B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" checked="checked"/>C-互联网</label>
	                		<#else >
	                			<label for="tbWeb0"><input type="checkbox" value="0" disabled="disabled" name="indexItemNetId" id="tbWeb0" checked="checked"/>A-人行内网</label>
	                			<label for="tbWeb1"><input type="checkbox" value="1" disabled="disabled" name="indexItemNetId" id="tbWeb1" checked="checked"/>B-局域网</label>
	                			<label for="tbWeb2"><input type="checkbox" value="2" disabled="disabled" name="indexItemNetId" id="tbWeb2" checked="checked"/>C-互联网</label>
	                		</#if>
	                	</td>			
                		<#else>
                		<td>
                			<label for="tbWeb0"><input type="checkbox" value="0" name="indexItemNetId" id="tbWeb0" checked="checked"/>A-人行内网</label>
                			<label for="tbWeb1"><input type="checkbox" value="1" name="indexItemNetId" id="tbWeb1"/>B-局域网</label>
                			<label for="tbWeb2"><input type="checkbox" value="2" name="indexItemNetId" id="tbWeb2"/>C-互联网</label>
                		</td>
                		</#if>
                	</tr>-->
                	<tr>
                		<td class="noBorderL firstTD">别名</td>
                		<#if indexitem?? >
                		<td><input type="text" name="indexItemAliasName" id="indexItemAliasName"  value="${indexitem.indexItemAliasName}" class="inputSty allnameVal" onblur="onblurVal(this,13,50)"/></td>
                		<#else>
                		<td><input type="text" name="indexItemAliasName" id="indexItemAliasName" value="" class="inputSty allnameVal"  onblur="onblurVal(this,13,50)"/></td>
                		</#if>
                	</tr>
                    <tr>
                        <td class="noBorderL firstTD">序号</td>
                        <td><input type="text" name="indexItemAliasName"    value="" class="inputSty String"  onblur="onblurVal(this,13,50)"/></td>
                    </tr>
                	<!--<tr>
                		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>序号</td>
                		<#if indexitem?? && orgFlag==1>
                		<td><input type="text" name="indexItemNumber" id="indexItemNumber"  value="${indexitem.indexItemNumber}" class="inputSty"/></td>
                		<#elseif indexitem?? && orgFlag==0>
                		<td><input readonly="readonly" type="text" name="indexItemNumber" id="indexItemNumber"  value="${indexitem.indexItemNumber}" class="inputSty"/></td>
                		<#else>
                		<td><input type="text" name="indexItemNumber" id="indexItemNumber" value="${num}" class="inputSty"/></td>
                		</#if>
                	</tr>-->
                	<tr>
                		<td class="noBorderL firstTD"><label class="mainOrange"> * </label>指标项是否启用</td>
                		<td>
                			<#if indexitem?? &&  orgFlag==1>
                				<#if indexitem.indexItemUsed == "1">
                				<label for="isOn0"><input type="radio" name="indexItemUsed" id="isOn0" value="1" checked="checked"/>是</label>
                				<label for="isOn1"><input type="radio" name="indexItemUsed" id="isOn1" value="0" />否</label>
                				<#else>
                				<label for="isOn0"><input type="radio" name="indexItemUsed" id="isOn0" value="1" />是</label>
                				<label for="isOn1"><input type="radio" name="indexItemUsed" id="isOn1" value="0" checked="checked"/>否</label>
                				</#if>
                			<#elseif  indexitem?? &&  orgFlag==0>
                				<#if indexitem.indexItemUsed == "1">
                				<label for="isOn0"><input type="radio" name="indexItemUsed" id="isOn0" value="1" checked="checked"/>是</label>
                				<label for="isOn1"><input disabled="disabled" type="radio" name="indexItemUsed" id="isOn1" value="0" />否</label>
                				<#else>
                				<label for="isOn0"><input disabled="disabled" type="radio" name="indexItemUsed" id="isOn0" value="1" />是</label>
                				<label for="isOn1"><input type="radio" name="indexItemUsed" id="isOn1" value="0" checked="checked"/>否</label>
                				</#if>
                			<#else>
                			<label for="isOn0"><input type="radio" name="indexItemUsed" id="isOn0" value="1" checked="checked"/>是</label>
                			<label for="isOn1"><input type="radio" name="indexItemUsed" id="isOn1" value="0" />否</label>
                			</#if>
                		</td>
                	</tr>
                	<tr>
                		<td class="noBorderL firstTD">指标项说明</td>
                		<#if indexitem?? && orgFlag==1>
                		<td><textarea name="indexItemNotes"  class="fontSize12 textareaSty allinstructionsVal" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)">${indexitem.indexItemNotes}</textarea></td>
                		<#elseif indexitem?? && orgFlag==0>
                		<td><textarea readonly="readonly" name="indexItemNotes"  class="fontSize12 textareaSty allinstructionsVal" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)">${indexitem.indexItemNotes}</textarea></td>
                		<#else>
                		<td><textarea name="indexItemNotes" class="fontSize12 textareaSty allinstructionsVal" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)"></textarea></td>
                		</#if>
                	</tr>
            	</table>
            	<div class="showBtnBox">
                	<input  class="cancleBtn closeThisLayer" type="button" value="取 消" />
                	<input class="sureBtn"   id="submitBtn" type="button" value="确 认" />
            	</div>
            	<input name="indexItemIdd" type="hidden" value="${indexitem.indexItemId}" id="indexItemIdd"/>
            	<input name="typ" type="hidden" value="${typ}" id="indexItemIdd"/>
        	</form>
		</div>
	</div>
	<script type="text/javascript">
		
			if(${typ}==0){
				$(".td_click label").click(function(){
					if($(this).index()==0){
						$(".tr_hide").show();
						$(".String1").show();
						$(".openPop1").hide();
						$(".b_clic").text("字符长度")
						$("#tbCode0").parent().show();   
					}else if($(this).index()==1 || $(this).index()==2){
						$(".tr_hide").hide();
						$("#tbCode0").parent().hide();  
					}else if($(this).index()==3){
						$(".tr_hide").show();
						$(".String1").hide();
						$(".openPop1").show();
						$("#tbCode0").parent().hide(); 
						$(".b_clic").text("数据字典"); 
					} 
					$("#tbCode1").prop("checked",'true')
					$("#isEmptylab0").show();
				}) 
			}
		
		
		
		
		
		
		
		
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').click(function(){
		    	parent.layer.close(index); //执行关闭
			});	
			var varLength1 = $("[name='varLength']").val();
			$("#submitBtn").click(function(){
				var varLength2 = $("[name='varLength']").val(); 	 
				<#if typ != 0> 
					if((parseInt(varLength2))<(parseInt(varLength1))){
						layer.alert("字符长度不能小于当前长度",{ icon:2, shade:0.3, shouldClose:true });
						return false;
					}
				</#if> 
				var indexItemName = $("#indexItemName").val(); 
				var varLength = $("input[name='varLength']").val();
				var indexItemNotes = $("textarea[name='indexItemNotes']").val(); 
				if(indextype(indexItemName)==1) {
					layer.alert("指标项名称不能为空",{ icon:2, shade:0.3, shouldClose:true });
	                 $("#indexItemName").focus();
	                return false;
		        }
				if(indextype(indexItemName)==0) {
					layer.alert("指标项名称输入不合法",{ icon:2, shade:0.3, shouldClose:true });
	                $("#indexItemName").focus();
	                return false;
		       	}
			
			if($(":checkbox:checked").length==0){
				layer.alert("请选择网络标识",{icon:2,shade:0.3,shouldClose:true});
                $("[name='indexItemNetId']").focus();
                return false;
			}
			if($("[name='varLength']").length>0){
					if(checkNumber(varLength)==0){
							layer.alert("请填入数字",{icon:2,shade:0.3,shouldClose:true});
							return false;
					}
			} 
			if(indexItemNotes.length>50){
					layer.alert("请填入不超过50个字的说明",{icon:2,shade:0.3,shouldClose:true});
					return false;
			}
			var indexItemAliasName = $("#indexItemAliasName").val();
			if(checkChineseNoSpe(indexItemAliasName)==0) {
					layer.alert("别名输入不合法",{ icon:2, shade:0.3, shouldClose:true });
	                $("#indexItemName").focus();
	                return false;
		    }
			var loading = layer.load();							
			if($("#indexItemIdd") !=null ){
				$.post("${request.getContextPath()}/admin/indexTb/saveIndexItem.jhtml?indexItemId=${indexitem.indexItemId}",$("#addData").serialize(),function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){
						if(data.flag){
							layer.load();
							parent.window.location.href = "${request.getContextPath()}/admin/indexTb/index.jhtml?indexId=${indexT.indexId}";
						}
						layer.close(index);
					});
			});			
			}else{
				$.post("${request.getContextPath()}/admin/indexTb/saveIndexItem.jhtml",$("#addData").serialize(),function(data){
						layer.close(loading);
						var index = alertInfoFun(data.message, data.flag, function(){
							if(data.flag){
								layer.load();
								parent.window.location.href = "${request.getContextPath()}/admin/indexTb/index.jhtml?indexId=${indexT.indexId}";
							}
							layer.close(index);
						});
				});		
			}
		})
	</script>
	</body>
</html>
