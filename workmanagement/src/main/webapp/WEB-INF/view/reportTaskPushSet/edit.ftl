<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <meta charset="UTF-8">
    <title></title>
    <style>
    
		    .laydate_table { 
		display: none;
		} 
        body{
            font-family: "微软雅黑";
            padding: 20px;
            margin: 0;
            font-size: 14px;
        }
        p,img,ul,li{
            margin: 0;
            padding: 0;
        }
        .taskAdd{
            width: 800px;
            margin: 100px auto;
            border: 1px solid #c1c1c1;
            padding: 20px;
        }
        .queryHead{
            width: 100%;
            height: 30px;
            line-height: 30px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
        .taskAdd table{
            width: 100%;
            border-top: 1px solid #dadada;
            border-left: 1px solid #dadada;
            font-size: 12px;
            margin-bottom: 20px;
        }
        .taskAdd th{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            text-align: left;
        }
        .taskAdd td{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
        }
        .titleFont1 {
            padding-left: 10px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            text-align: left;
            border: 1px solid #dadada;
            border-bottom: none;
        }
        .taskAdd .left{
            text-align: left;
        }
        .taskAdd .right{
            text-align: right;
        }
        .taskAdd .taskCycle div{
            float: left;
            margin-right: 20px;
        }
        .dutyBankTitle{
            width: 100%;
            height: 30px;
            margin: 10px 0;
            padding-top: 10px;
            text-align: center;
            font-size: 14px;
        }
        .queryBtn{
            width: 100%;
            height:50px;
            text-align: center;
        }
        .queryBtn button{
            width: 150px;
            height: 30px;
            margin: 0 20px;
            outline: none;
            border: none ;
            cursor: pointer;
            color: white;
        }
        .queryBtn .cancel{
            background-color: #a4a4a4;
        }
        .queryBtn .query{
            background-color: #f57041;
        }
        .bankList{
            width: 300px;
            height: 500px;
            border: 1px solid #dadada;
            float: left;
        }
        .bankList p,.bankChecked p{
            width: 210px;
            height: 25px;
            line-height: 25px;
            padding: 5px 10px;
            border-bottom: 1px solid #dadada;
        }
        .bankList div{
            padding: 5px 10px;
        }
        .choiceBtn{
            width: 60px;
            padding: 150px 10px;
            float: left;
        }
        .choiceBtn input{
            margin-top: 30px;
            cursor: pointer;
        }
        .bankChecked{
            width: 230px;
            height: 500px;
            border: 1px solid #dadada;
            float: left;
            overflow:auto;
        }
        .underline{
        	text-decoration:underline;
        }
        .addTaskBtn{
        	padding: 3px 8px;
        	border: none;
        	border-radius: 4px;
        	background: rgb(56,165,226);
        	color: white;
        	font-size: 12px;
        }
        #myOrgUl li,#pearentOrgUl li,.styleUL li{
        	padding: 3px 5px;
        	margin-bottom: 5px;
        	
        }
        #myOrgUl li input,.styleUL li input{
        	vertical-align: middle;
        	margin-right: 5px;
        }
        #myOrgUl li span,.styleUL li span{
        	font-size: 12px;
        }
        #pearentOrgUl li{}
        
        #orgShow .org_name{
        	font-size: 11px;
        	display: inline-block;
        	*display: inline;
        	*zoom: 1;
        	padding: 1px 3px;
        	border: 1px solid #CCCCCC;
        	margin-right: 5px;
        	margin-bottom: 3px;
        }
    </style>
    <script type="text/javascript">
    	var html="";
    	var indexNum=-1;
		$(function(){
			$("#max").height($(window).height()-50);
			//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg, {
						icon: (msg=="操作成功")?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
			$("#onetask").click(function(){//单次选择
				$("#weekInput").val($(this).val()+"-"+$("#onetask1").val());
			})
			$("#onetask1").click(function(){//单次选择
				$("#weekInput").val($("#onetask").val()+"-"+$(this).val());
			})
			$("#monthSelect").click(function(){//月选择
				$("#monthInput").val($(this).val()+"-"+$("#monthSelect1").val());
			})
			$("#monthSelect1").click(function(){//月选择
				$("#monthInput").val($("#monthSelect").val()+"-"+$(this).val());
			})
			$("#seasonSelect").click(function(){//季_月选择
				$("#seasonInput").val($(this).val()+$("#seasonTwoSelect").val()+"-"+$("#seasonTwoSelect1").val());
			})
			$("#seasonTwoSelect").click(function(){//季_日选择
				$("#seasonInput").val($("#seasonSelect").val()+$(this).val()+"-"+$("#seasonTwoSelect1").val());
			})
			$("#seasonTwoSelect1").click(function(){//季_日选择
				$("#seasonInput").val($("#seasonSelect").val()+$("#seasonTwoSelect").val()+"-"+$(this).val());
			})
			var reportTaskPushSetCycle= "${reportTaskPushSet.reportTaskPushSetCycle}";
			if(reportTaskPushSetCycle.indexOf("季")!=-1){
				var monthStr=reportTaskPushSetCycle.substring(0,reportTaskPushSetCycle.indexOf("季")+3);
				var dayStr=reportTaskPushSetCycle.substring(reportTaskPushSetCycle.indexOf("季")+3,reportTaskPushSetCycle.indexOf("-"));
				var dayStr1=reportTaskPushSetCycle.substring(reportTaskPushSetCycle.indexOf("-")+1);
				$("#seasonInput").attr("checked","checked");
				$("#seasonSelect option").each(function(){
					if($(this).val()==monthStr){
						$(this).attr("selected","selected");
					}
				});
				$("#seasonTwoSelect option").each(function(){
					if($(this).val()==dayStr){
						$(this).attr("selected","selected");
					}
				});
				$("#seasonTwoSelect1 option").each(function(){
					if($(this).val()==dayStr1){
						$(this).attr("selected","selected");
					}
				});
				$("#seasonInput").val(reportTaskPushSetCycle);
			}else if(reportTaskPushSetCycle.indexOf("月")!=-1){
				$("#monthInput").attr("checked","checked");
				$("#monthSelect option").each(function(){
					if($(this).val()==reportTaskPushSetCycle.substring(0,reportTaskPushSetCycle.indexOf("-"))){
						$(this).attr("selected","selected");
						$("#monthInput").val(reportTaskPushSetCycle);
					}
				});
				$("#monthSelect1 option").each(function(){
					if($(this).val()==reportTaskPushSetCycle.substring(reportTaskPushSetCycle.indexOf("-")+1)){
						$(this).attr("selected","selected");
						$("#monthInput").val(reportTaskPushSetCycle);
					}
				});
			}else if(reportTaskPushSetCycle.indexOf("单")!=-1){
				$("#weekInput").attr("checked","checked");
				$("#onetask option").each(function(){
					if($(this).val()==reportTaskPushSetCycle.substring(0,reportTaskPushSetCycle.indexOf("-"))){
						$(this).attr("selected","selected");
						$("#weekInput").val(reportTaskPushSetCycle);
					}
				});
				$("#onetask1 option").each(function(){
					if($(this).val()==reportTaskPushSetCycle.substring(reportTaskPushSetCycle.indexOf("-")+1)){
						$(this).attr("selected","selected");
						$("#weekInput").val(reportTaskPushSetCycle);
					}
				});
			}else{
				$("#nothingInput").attr("checked","checked");
			}
			//报送方式
			$("[name=reportTaskPushSetMethod]").change(function(){
				var reportTaskPushSetMethod=$(this).val();
				var url = "${request.getContextPath()}/admin/reportTaskPushSet/getReportTaskPushSetMethodListByMethodIdJson.jhtml";
				$.post(url,{reportTaskPushSetMethod:reportTaskPushSetMethod},function(data){
					if(data.reportTaskPushSetTemplateList!=null){
						var reportTaskPushSetTemplateList=data.reportTaskPushSetTemplateList;
						var html="";
						for(var i = 0; i < reportTaskPushSetTemplateList.length; i++){
							html+="<option>"+reportTaskPushSetTemplateList[i]+"</option>"
						}
						$("[name=reportTaskPushSetTemplate]").html(html);
					}
				});
			});
			
		})
		function orgPearent(orgId,obj){
			var obj=$(obj);
			obj.parents("tr").nextAll().each(function(){
				var trId=$(this).attr("id");
				if(trId=="childTr"){
					obj.parents("tr").next().remove();
				}
			});
			obj.parents("tr").find("a").css("color","black");
			obj.css("color","rgb(56,165,226)");
			obj.siblings().css("color","black");
			var url = "${request.getContextPath()}/admin/reportTaskPushSet/getChildOrgJson.jhtml";
			$.post(url,{orgId:orgId},function(result){
				if(result.childOrgList!=null){
					var childOrgList = result.childOrgList;	
					var html="<tr id='childTr'><td class='noBorderL firstTD'>下级机构目录</td><td><ul class='styleUL'>";
					for(var i = 0; i < childOrgList.length; i++){
						var sysOrg = childOrgList[i];	
						html+="<li><input class='addTaskBtn' type='button' value='添加' onclick='myOrg(this,"+sysOrg.sys_org_id+")'><a class='fontSize12' onclick='orgPearent("+sysOrg.sys_org_id+",this)'>"+sysOrg.sys_org_name+"</a></li>"
					}
					html+="</ul></td></tr>";
					obj.parents("tr").after(html);
				}
			});
		}
		function myOrg(obj,orgId){
			var isRepetition=false;
			$("#myOrgUl li input").each(function(){
				if($(this).val()==orgId){
					isRepetition=true;
					return false;
				}
			});
			if(isRepetition==false){
				var obj=$(obj);
				var orgName=obj.next().text();
				var html="<li><input name='orgIds' type='checkbox' checked='checked' value="+orgId+"><span>"+orgName+"</span></li>";
				$("#myOrgUl").append(html);
			}
		}
		
		
		//打开弹窗选择机构
		function openPop(){
            $("#covered").show();
            $("#poplayer").show();
            if($("#treeDemo").html()==""){
            var loading = layer.load();
			$.ajax({
				url:'${request.getContextPath()}/admin/reportTaskPushSet/getOrgList.jhtml',
				dataType:'json',
				success:function(nodes){
					layer.close(loading);
					var str1 = [];
					for (var i = 0; i < nodes.length; i++) {
						var obj= new Object();
						 obj.id= nodes[i].id;
						 obj.name= nodes[i].name;
						 obj.pId= nodes[i].parent;
                        if(obj.id=='A80' || obj.pId=='A80')
                        {
                            str1.push(obj);
                        }
					}
                    /*var obj= new Object();
                    obj.id= "A80";
                    obj.name= "崇左市人民政府";
                    obj.pId= "A";
                    str1.push(obj);*/

					if($("#hideorg span").length!=0){
						var lodid =	$("#hideorg span input")
						for (var i = 0; i < str1.length; i++) {
							for(var j = 0; j < lodid.length; j++){ 
								if(str1[i].id==lodid.eq(j).val()){
									str1[i].checked=true;
									str1[i].open=true;
								}
							}
						}
					}
					orgtypeZ(str1);
				}
			});
			}
       }
		
		
		
		function selUpstream(obj){
			if($(obj).attr("checked")=="checked")  
			{  
				$(obj).removeAttr("checked"); 
			}else{  
				$(obj).attr("checked","checked");
			}
       }
		
		//关闭上级区域弹出框
        function closePop(){
            $("#covered").hide();
            $("#poplayer").hide();
        }
        //重置按钮
        function confirmSel1(){
        	$("input[type='checkbox']:checked").each(function(){
        		$(this).removeAttr("checked");
        	});
        	closePop();
        	$("#orgShow").html("");
        }
        
        //确认选择
        function confirmSel2(){  
        	var html=$("#hideorg").html()
			$("#orgShow").html(html);
			closePop();
        }
        //验证名称
        function testName(obj){
        	var reg = /^([a-zA-Z\d\u4e00-\u9fa5\(\)\（\）]{0,30})$/;
        	var val = $(obj).val();
        	if(reg.test(val)==false){
        		if($(obj).nextAll(".redWarm4").length==0){
        			var str = "";
					str = "<span class='redWarm4'>请输入中文，英文，数字，不能输入特殊符号</span>"
					$(obj).after(str);
        		}
        		$(obj).focus();
        		return false;
        	}else{
        		$(obj).nextAll(".redWarm4").remove();
        	}
        }
             
    	

		
		</script>
</head>
<body>
<!--弹出框 -->
<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox xzjg">
        <div class="titleFont1">
            <span>机构列表</span>
        </div>
        <div class="listBox" style="overflow: auto;">
            	<div class="zTreeDemoBackground left">
					<ul id="treeDemo" class="ztree"></ul>
				</div>
        </div>
        <p class="hide" id="hideorg">
        		<#list sysOrgList as item>
            		<span class="org_name" name="orgIds" value=${item.sys_org_id}>
            		<input type="hidden" name="orgIds" value="${item.sys_org_id}" />${item.sys_org_name}</span>
           		</#list>
        </p>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1()"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>  
</div>



<div class="showListBox">
    <form id="addData" method="post" action="${request.contextPath}/admin/reportTaskPushSet/editSubmit.jhtml">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">增加任务推送</caption>
        <tr>
            <td style="width:23%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>任务名称</td>
            <td style="width:77%;" class="secondTD">
            	<input name="reportTaskPushSetId" type="hidden" value="${reportTaskPushSet.reportTaskPushSetId}">
                <input class="inputSty allnameVal" name="reportTaskPushSetName" type="text" value="${reportTaskPushSet.reportTaskPushSetName}" onblur="testName(this)" maxlength="30">
            </td>
        </tr>
        <tr>
            <td style="width:23%;" class="noBorderL firstTD">开始时间</td>
            <td style="width:77%;" class="secondTD" >
            	<div style="position: relative;width: 100%;height: 27px;">
            		<input class=" inputSty fontSize12" readOnly="true"  style="width: 80px;height:18px;position: absolute;top: 0;left: 0;height: 18px;z-index: 999;" id="strtime"
                       name="beginTimes" value="${beginTimes}"> 
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 100px;position: absolute;top: 0;left: 0;z-index: 10;" id="begin"
                       name="beginTime" value="${beginTime}"> 
            	</div>
            </td>
        </tr>
        <tr class="taskCycle">
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>任务周期</td>
            <td class="secondTD">
               <!-- <div><input class="verticalMiddle" id="nothingInput" type="radio" name="reportTaskPushSetCycle" value="无"><span class="inlineBlock fontSize12 paddingLR10">无</span></div>-->
               <div>
                    <input id="weekInput" class="verticalMiddle" type="radio" checked="checked" name="reportTaskPushSetCycle" value="单次"><span class="inlineBlock fontSize12 paddingLR10">单次</span>
                     <select id="onetask" class="inputSty" style="width: 120px;">
                    	<#list 1..31 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="单次1日" selected="selected">1日</option>
		                    	<#else>
		                    		<option value="单次1日">1日</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="单次${num}日">${num}日</option>
	                    	</#if>
						</#list>
                    </select><span class="inputSty">至</span>
                    <select id="onetask1" class="inputSty" style="width: 120px;">
                	<#list 1..31 as num>
                		<#if num_index == 0>
                    		<#if reportTaskPushSet??>
	                    		<option value="1日" selected="selected">1日</option>
	                    	<#else>
	                    		<option value="1日">1日</option>
	                    	</#if>
                    	</#if>
                    	<#if num_index gt 0>
                    		<option value="${num}日">${num}日</option>
                    	</#if>
					</#list>
                </select>
                </div>
               <div>
                    <input class="verticalMiddle" id="monthInput" type="radio" name="reportTaskPushSetCycle" value="每月"><span class="inlineBlock fontSize12 paddingLR10">每月</span>
                    <select id="monthSelect" class="inputSty" style="width: 120px;">
                    	<#list 1..31 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="每月1日" selected="selected">1日</option>
		                    	<#else>
		                    		<option value="每月1日">1日</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="每月${num}日">${num}日</option>
	                    	</#if>
						</#list>
                    </select><span class="inputSty">至</span>
                    <select id="monthSelect1" class="inputSty" style="width: 120px;">
                	<#list 1..31 as num>
                		<#if num_index == 0>
                    		<#if reportTaskPushSet??>
	                    		<option value="1日" selected="selected">1日</option>
	                    	<#else>
	                    		<option value="1日">1日</option>
	                    	</#if>
                    	</#if>
                    	<#if num_index gt 0>
                    		<option value="${num}日">${num}日</option>
                    	</#if>
					</#list>
                </select>
                </div>
                
                <div>
                    <input id="seasonInput" class="verticalMiddle" type="radio" name="reportTaskPushSetCycle" value="每季"><span class="inlineBlock fontSize12 paddingLR10">每季</span>
                    <select id="seasonSelect" class="inputSty" style="width: 120px;">
                    	
		                 <option value="每季季初" selected="selected">季初</option>
		                 <option value="每季季中">季中</option>
	                    <option value="每季季末">季末</option>
                    </select>
                	<select id="seasonTwoSelect" class="inputSty" style="width: 120px;">
                    	<#list 1..31 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="1日" selected="selected">1日</option>
		                    	<#else>
		                    		<option value="1日">1日</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="${num}日">${num}日</option>
	                    	</#if>
						</#list>
                    </select><span class="inputSty">至</span>
                    <select id="seasonTwoSelect1" class="inputSty" style="width: 120px;">
                	<#list 1..31 as num>
                		<#if num_index == 0>
                    		<#if reportTaskPushSet??>
	                    		<option value="1日" selected="selected">1日</option>
	                    	<#else>
	                    		<option value="1日">1日</option>
	                    	</#if>
                    	</#if>
                    	<#if num_index gt 0>
                    		<option value="${num}日">${num}日</option>
                    	</#if>
					</#list>
                </select>
                </div>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>状态</td>
            <td class="secondTD">
            	<#if reportTaskPushSet.reportTaskPushSetStatus==1>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="0"><span class="inlineBlock fontSize12 paddingLR10">正常</span>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="1" checked="checked"><span class="inlineBlock fontSize12 paddingLR10">禁用</span>
            	<#else>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="0" checked="checked"><span class="inlineBlock fontSize12 paddingLR10">正常</span>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="1"><span class="inlineBlock fontSize12 paddingLR10">禁用</span>
            	</#if>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>涉及数据报送</td>
            <td class="secondTD">
                <select name="reportTaskPushSetMethod" class="inputSty">
                	<#list reportTaskPushSetMethodList as item>
                		<#if reportTaskPushSetMethodIdList[item_index]==reportTaskPushSet.reportTaskPushSetType>
                			<option value="${reportTaskPushSetMethodIdList[item_index]}" selected="selected">${item}</option>
                		<#else>
                			<option value="${reportTaskPushSetMethodIdList[item_index]}">${item}</option>
                		</#if>
                	</#list>
                </select>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>涉及报送模板</td>
            <td class="secondTD">
                <select name="reportTaskPushSetTemplate" class="inputSty">
                	<#if reportTaskPushSetTemplateList ?? !>
	                	<#list reportTaskPushSetTemplateList as item>
	                		<#if item==reportTaskPushSet.reportTaskPushSetTemplate>
	                			<option selected="selected">${item}</option>
	                		<#else>
	                			<option>${item}</option>
	                		</#if>
	                	</#list>
                	<#else>
                		<#list allIndexName as item>
                			<option>${item}</option>
                		</#list>
                	</#if>
                </select>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>任务发布机构</td>
            <td class="secondTD">
            	<input type="hidden" name="reportTaskPushSetOrgId" value="${reportTaskPushSet.sysOrgCreateId}" />
              	<input readonly="readonly" name="reportTaskPushSetOrgName" type="text" value="崇左市发改委" />
            </td>
        </tr>
        <tr>
        	<td class="noBorderL firstTD"><label class="mainOrange"> * </label>任务负责机构列表</td>
            <td>
            <p id="orgShow">
            	<#list sysOrgList as item>
            		<span class="org_name" name="orgIds" value=${item.sys_org_id}>
            		<input type="hidden" name="orgIds" value="${item.sys_org_id}" />${item.sys_org_name}</span>
           		 </#list>
            </p>
            	<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()">选择机构</a>
            </td>
        </tr>
    </table>
    </form>
    <div class="showBtnBox">
    	<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
    	<input id="formVer" type="button" class="sureBtn" value="确 认" />
    </div>
</div>
</body>
<script type="text/javascript">
$(function(){
	
	
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		$("#formVer").click(function(){
				var name = $("[name=reportTaskPushSetName]").val();
				var reg = /^([a-zA-Z\d\u4e00-\u9fa5\(\)\（\）]{1,30})$/;
			
				var orgList= $("#leftTabel").text();
				if($.trim(name)==""){
					layer.alert("请填写名称", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
				}else if(reg.test(name)==false){
					if($("[name=reportTaskPushSetName]").nextAll(".redWarm4").length==0){
						var str = "";
						str = "<span class='redWarm4'>请输入中文，英文，数字，不能输入特殊符号</span>"
						$("[name=reportTaskPushSetName]").after(str);
						$("[name=reportTaskPushSetName]").focus();
						return false;
					}
				}else if($("#orgShow").size() ==0){
					alertInfo("请至少选择一个机构");
					return false;
				}
				else{
					//为单次、月、季赋初始值
					var reportTaskPushSetCycle= $("[name=reportTaskPushSetCycle]:checked").val();
					if(reportTaskPushSetCycle.length==2){
						var nextVal= $("[name=reportTaskPushSetCycle]:checked").next().next().val();
						if(reportTaskPushSetCycle.indexOf("季")!=-1){
							nextVal+=$("[name=reportTaskPushSetCycle]:checked").next().next().next().val()+"-"+$("#seasonTwoSelect1").val()
						}else if(reportTaskPushSetCycle.indexOf("月")!=-1){
							nextVal+="-"+$("#monthSelect1").val()
						}else if(reportTaskPushSetCycle.indexOf("单")!=-1){
							nextVal+="-"+$("#onetask1").val()
						}
						$("[name=reportTaskPushSetCycle]").val(nextVal);
					}
					if(reportTaskPushSetCycle.indexOf("季")!=-1){
						var starDay=$("#seasonTwoSelect").val().substring(0,$("#seasonTwoSelect").val().indexOf("日"));
						var endDay=$("#seasonTwoSelect1").val().substring(0,$("#seasonTwoSelect1").val().indexOf("日"));
						if(parseInt(starDay)>parseInt(endDay)){
							alertInfo("开始日期不能大于结束日期");
							return false;
						}
					}else if(reportTaskPushSetCycle.indexOf("月")!=-1){
						var starDay=$("#monthSelect").val().substring(2,$("#monthSelect").val().indexOf("日"));
						var endDay=$("#monthSelect1").val().substring(0,$("#monthSelect1").val().indexOf("日"));
						if(parseInt(starDay)>parseInt(endDay)){
							alertInfo("开始日期不能大于结束日期");
							return false;
						}
					}else if(reportTaskPushSetCycle.indexOf("单")!=-1){
						var starDay=$("#onetask").val().substring(2,$("#onetask").val().indexOf("日"));
						var endDay=$("#onetask1").val().substring(0,$("#onetask1").val().indexOf("日"));
						if(parseInt(starDay)>parseInt(endDay)){
							alertInfo("开始日期不能大于结束日期");
							return false;
						}
					}
					var loading = layer.load();
					$.post("${request.contextPath}/admin/reportTaskPushSet/editSubmit.jhtml",$("#addData").serialize(),function(data){
						layer.close(loading);
						var index = alertInfoFun(data.message, data.flag, function(){
							if(data.flag){
								parent.window.location.href = "${request.getContextPath()}/admin/reportTaskPushSet/list.jhtml";
							}
							layer.close(index);
						});
					});
				}
		});	
	});
	$(function(){
	var start = {
  			elem: '#begin',
  			format: 'YYYY-MM',
  			istime: false,
  			isclear: false, 
  			istoday: false,
  			min:laydate.now(),
  			choose: function(datas){
  				datas = datas.substring(0, 7);
  				$("#strtime").val(datas)
    			
  			}
	};
	
		laydate(start);
	datas = $("#begin").val().substring(0, 7);
	$("#strtime").val(datas)
	})
</script>
</html>