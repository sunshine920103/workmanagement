<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" > 
        function openPop() {
            	$("#poplayer").show();
            	$("#covered").show(); 
            	if($("#treeDemo").html()==""){
		            var loading = layer.load();
					$.ajax({
					url:'${request.getContextPath()}/admin/manualEntry/getOrgList.jhtml',
					dataType:'json',
					success:function(nodes){
						layer.close(loading);
						 var str1 = [];
	                    for (var i = 0; i < nodes.length; i++) {
	                        var obj = new Object();
	                        obj.id = nodes[i].id;
	                        obj.name = nodes[i].name;
	                        obj.pId = nodes[i].parent;
	                        str1.push(obj);
	                    }
	                    if ($("#hideorg span").length != 0) {
	                        var lodid = $("#hideorg span input")
	                        for (var i = 0; i < str1.length; i++) {
	                            for (var j = 0; j < lodid.length; j++) {
	                                if (str1[i].id == lodid.eq(j).val()) {
	                                    str1[i].checked = true;
	                                    str1[i].open = true;
	                                }
	                            }
	                        }
	                    }
	                    orgtypeZ(str1);
					}
					});
				}
	     }
		//关闭上级区域弹出框
        function closePop() {
            $("#poplayer").hide();
            $("#covered").hide();
        }

 
       	//确认选择
        function confirmSel1() {
            var html = $("#hideorg").html();
            $("#orgShow").html(html);
            closePop();
        }
        
        
        
		</script>
		<title>标识新增</title>
	</head>
	<body>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox">
		        <div class="titleFont1">
		            <span>机构列表</span>
		        </div>
		        <div class="listBox" style="overflow: auto;padding:10px 0;">
		            <div class="zTreeDemoBackground left">
		                <ul id="treeDemo" class="ztree"></ul>
		            </div>
		        </div>
		        <p class="hide" id="hideorg">
		        	 <#list identiFication.subOrgs as so>
		                            <span class='org_name'>
		                                <input class='org_name' type='hidden' name='orgIds' value='${so.sys_org_id}' id='${so.sys_org_id}'/>
		                            ${so.sys_org_name}
		                            </span>
		                        </#list>
		        </p>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn hide" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		        </div>
		    </div>
	    </div>  
	
		<#-- 新增框 -->
		<div class="showListBox">
			<form id="form"  method="post">			
			
			<input name="idenId" type="hidden" value="${identiFication.sys_identification_id}"/>
			<table cellpadding="0" cellspacing="0">
					
						<caption  class="titleFont1 titleFont1Ex">
							<#if identiFication??>
								 标识修改
								 <#else>
							标识新增
							</#if>
						</caption>
					<tr>
						<td  class="noBorderL firstTD" style="width:25%;"><label class="mainOrange"> * </label>标识名称</td>
						
						<td class="secondTDnd">
							<input name="sys_org_name" type="text" class="inputSty required allnameVal" value="${identiFication.sys_identification_name}" onblur="onblurVal(this,13,30)" title="必填，不能超过30个字"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>覆盖机构</td>
						<td class="secondTD" style="position: relative;">
						<p id="orgShow">
		                        <#list identiFication.subOrgs as so>
		                            <span class='org_name'>
		                                <input class='org_name' type='hidden' name='orgIds' value='${so.sys_org_id}' id='${so.sys_org_id}'/>
		                            ${so.sys_org_name}
		                            </span>
		                        </#list>
		                    </p>
		                    <a id="openPop1" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
		                       onclick="openPop()">请选择机构
		                    </a>
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
			
			$("#submitBtn").click(function(){
				var sys_org_name=$("input[name='sys_org_name']").val()
				if(sys_org_name==""){
					layer.alert("标识名称不能为空",{icon:2,shade:0.3,shouldClose:true});
	              	 return false;
				} 
				if(checkChineseNoSpe(sys_org_name)==0) {
						layer.alert("标识名称输入不合法",{icon:2,shade:0.3,shouldClose:true}); 
		                return false;
			   }  
			   if($("#orgShow span").length == 0 ) {
					 layer.alert("请选择机构",{icon:2,shade:0.3,shouldClose:true}); 
		             return false;
			   }
	       		var url = "${request.getContextPath()}/admin/identification/save.jhtml";
	       		var loading = layer.load();
	        	$.post(url,$("#form").serialize(),function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){
						if(data.flag){
							layer.load();
							parent.window.location.href = "${request.getContextPath()}/admin/identification/list.jhtml";
						}
						layer.close(index);
					});
				});   
        	})
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});
	</script>	
</html>
