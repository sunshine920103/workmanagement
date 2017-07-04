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
		
			//显示上级区域弹出框
			function openPop(num){
				$("#covered").show();
				$("#poplayer").show();
				if(num==1){
					$(".zfbm").hide();
					$(".zflb").show();
				}else if(num==0){
					$(".zflb").hide();
					$(".zfbm").show();
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
				var seleced = $(".zfbm .seleced");
				var level = parseInt($(seleced[0]).attr("level"));
				
			
					closePop();
					var area = $(seleced.find("label")).text();
					if(clear===1||seleced.length==0){
						$("#openPop").text("选择上级政府部门");
						$("#sysGovUpid").val("");
						$("#sysGovUpname").val("");		
						$("#sysGovUpcode").val("");	
					}else{
						$("#openPop").text(area);
						$("#sysGovUpid").val(seleced.attr("id"));
						$("#sysGovUpname").val(area);
						$.ajax({
			    			url:'${request.getContextPath()}/admin/sysGover/getcode.jhtml',
			    			data:{"name":area,"govType":"0"},
			    			success:function(d){
			    				eval("var d="+d);
			    				$("#sysGovUpcode").val(d);
			    			}
			    		});
					}
				
			}
			//确认选择
			function confirmSel1(clear){
				var seleced = $(".zflb .seleced");
				
				var level = parseInt($(seleced[1]).attr("level"));
				
			
					closePop();
					$("#openPop1").text(area);
					var area = $(seleced.find("label")).text();
					if(clear===1||seleced.length==0){
						$("#openPop1").text("选择政府部门类型");
						$("#sysGovTypeId").val("");	
						$("#sysGovFinancialCode").val("");
						$("#sysGovTypeCode").val("");
					}else{
						$("#openPop1").text(area);
						$("#sysGovTypeId").val(seleced.attr("id"));
						$.ajax({
			    			url:'${request.getContextPath()}/admin/sysGover/getcode.jhtml',
			    			data:{"name":area,"govType":"1"},
			    			success:function(d){
			    				eval("var d="+d);
			    				$("#sysGovFinancialCode").val(d);
			    				$("#sysGovTypeCode").val(d);
			    			}
			    		});
					}
				
			}

			

		</script>
	</head>
	
	
	<body class="customProductQuery">
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox zfbm">
	        	<div class="titleFont1">
	        		<span>政府部门列表</span>
	        	</div>
	        	<div class="listBox">
	        		<div>
	        			<input id="searchInput" class="inputSty" type="text" value="" style="width: 150px;"/>
	        			<input id="searchBtn" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
	        		</div>
	        		<table cellpadding="0" cellspacing="0" id="searchTable">
						<#list its as it>
							<tr>
								<td level="1" id="${it.sysGovId}" onclick="selUpstream(this)">
									<label>${it.sysGovName}</label>
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
	        <div class="borderBox zflb">
	        	<div class="titleFont1">
	        		<span>政府部门类型列表</span>
	        	</div>
	        	<div class="listBox">
	        		<div>
	        			<input id="searchInput1" class="inputSty" type="text" value="" style="width: 150px;"/>
	        			<input id="searchBtn1" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
	        		</div>
	        		<table cellpadding="0" cellspacing="0" id="searchTable1">
						<#list gts as it>
							<tr>
								<td level="1" id="${it.sysGovTypeId}" onclick="selUpstream(this)">
									<label>${it.sysGovTypeName}</label>
								</td>
							</tr>
						</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	        		<input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
	        		<input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
	        	</div>
	        	
	        </div>  
	    </div> 
	
		<#-- 新增框 -->
		<div class="showListBox">
			<form id="form" action="${request.getContextPath()}/admin/sysOrg/save.jhtml" method="post">
			
				<input name="sysGovId" type="hidden" value="${it.sysGovId}"/>
				
				<table class="centerTable" cellpadding="0" cellspacing="0">				
					<#if it == null>
						<caption class="titleFont1 titleFont1Ex">新增政府部门</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改政府部门</caption>
					</#if>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD">上级政府部门</td>
						<td style="width:65%;" class="secondTD">
							<input id="sysGovUpname" name="sysGovUpname" type="hidden" value="${it.sysGovUpname}"/>
							<#if it == null>
								<input id="sysGovUpcode" name="sysGovUpcode" type="hidden" value=""/>
							<#else>
								<input id="sysGovUpcode" name="sysGovUpcode" type="hidden" value="${it.sysGovUpcode}"/>
							</#if>
							<input id="sysGovUpid" name="sysGovUpid" type="hidden" value="${it.sysGovUpid}"/>
							<a id="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop(0)">${(it.sysGovUpname??)?string(it.sysGovUpname,'选择上级政府部门')}</a>
						</td>
					</tr>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>政府部门类型</td>
						<td style="width:65%;" class="secondTD">
							<#if it == null>
								<input id="sysGovTypeCode" name="sysGovTypeCode" type="hidden" value=""/>
							<#else>
								<input id="sysGovTypeCode" name="sysGovTypeCode" type="hidden" value="${it.sysGovTypeCode}"/>
							</#if>
							<input id="sysGovTypeId" name="sysGovTypeId" type="hidden" value="${it.sysGovTypeId}"/>
							<a id="openPop1" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop(1)">${(it.sysGovTypeName??)?string(it.sysGovTypeName,'选择政府部门类型')}</a>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>政府部门名称</td>
						<td class="secondTD">
							<input id="sysGovName" name="sysGovName" class="inputSty required" value="${it.sysGovName}" onblur="onblurVal(this,20,20)" title="必填，不能超过20个字"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>政府部门编码</td>
						<td class="secondTD">
							<input id="sysGovFinancialCode" name="sysGovFinancialCode" class="inputSty required" value="${it.sysGovFinancialCode}" onblur="onblurVal(this,23,14)" title="必填，请输入14位数字、字母或组合"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sysOrgNotes" class="fontSize12 textareaSty" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)">${it.sysOrgNotes}</textarea>
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
			if($("#sysGovTypeId").val()=="") {
				layer.alert("请选择政府部门类型",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
//				layer.confirm("请选择政府部门类型", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
                return false;
	        }
			if(checkChineseNoSpe12($("input[name='sysGovName']").val())==1) {
//				layer.confirm("政府部门名称不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府部门名称不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovName']").focus();
                return false;
	        }
			if(checkChineseNoSpe12($("input[name='sysGovName']").val())==0) {
//				layer.confirm("政府部门名称输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府部门名称输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovName']").focus();
                return false;
	        }
			
			if(checkSysCode($("input[name='sysGovFinancialCode']").val())==1) {
//				layer.confirm("政府部门代码不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府部门代码不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovFinancialCode']").focus();
                return false;
	        }
			if(checkSysCode($("input[name='sysGovFinancialCode']").val())==0) {
//				layer.confirm("政府部门代码输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府部门代码输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovFinancialCode']").focus();
                return false;
	       }
			
			if($("textarea[name='sysOrgNotes']").val() != "" && checkChineseNoSpe50($("textarea[name='sysOrgNotes']").val()) == 0) {
//				layer.confirm("备注输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("备注输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("textarea[name='sysOrgNotes']").focus();
                return false;
	        }
			
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/sysGover/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						parent.window.location.href = "${request.getContextPath()}/admin/sysGover/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
		
		//搜索按钮
		$("#searchBtn").click(function(){
			var searchVal = $.trim($("#searchInput").val());
			var searchLoading = layer.load();
			var tabel = $("#searchTable");
			$.post("${request.getContextPath()}/admin/sysGover/sysGoverByName.jhtml",{name:searchVal},function(data){
				tabel.html("");
				$.each(data,function(i,v){
					var tr = $("<tr></tr>");
					var td = $("<td id='"+v.sysGovId+"' onclick='selUpstream(this)'><label>"+v.sysGovName+"</label></td>");
					tr.append(td);
					tabel.append(tr);
					$.trim($("#searchInput").val(""));
				});
				layer.close(searchLoading);
			});
		});
		
		//搜索按钮
		$("#searchBtn1").click(function(){
			var searchVal = $.trim($("#searchInput1").val());
			var searchLoading = layer.load();
			var tabel = $("#searchTable1");
			$.post("${request.getContextPath()}/admin/goverType/goverTypeByName.jhtml",{name:searchVal},function(data){
				tabel.html("");
				$.each(data,function(i,v){
					var tr = $("<tr></tr>");
					var td = $("<td id='"+v.sysGovTypeId+"' onclick='selUpstream(this)'><label>"+v.sysGovTypeName+"</label></td>");
					tr.append(td);
					tabel.append(tr);
					$.trim($("#searchInput1").val(""));
				});
				layer.close(searchLoading);
			});
		});
	</script>	
</html>