<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
<meta charset="UTF-8">
<script type="text/javascript">		
	//点击终止之后
	function objSave(){ 
		$.post("${request.getContextPath()}/admin/orgObj/save.jhtml",$("form").serialize(),function(data){
			layer.load();
			if(data.msg=="操作成功"){
				//layer.alert(data.msg,{ icon: 1,  shade: 0.3}); 
				//parent.window.location.href ="${request.getContextPath()}/admin/orgObj/list.jhtml";
				layer.alert(data.msg,{icon:1,shade:0.3,shouldClose:true},function(){
					window.location.href ="${request.getContextPath()}/admin/orgObj/list.jhtml";
				});
			}else{
				layer.alert(data.msg,{icon:2,shade:0.3,shouldClose:true});
			}
		});
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
							name = "<td id='"+sub.sysAreaCode+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
									+icon
									+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
									+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
									+"</td>";
						}else{
							name = "<td id='"+sub.sysAreaCode+"' onclick='selUpstream5(this)' style='padding-left:"+spacing+"'>"
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
			$(".zflb").hide();
			$(".dqlb").hide();
			$(".hylb").addClass("zIndex");
        }else if(num==2){
            $(".hylb").hide();
            $(".jglb").show();
			$(".zflb").hide();
			$(".dqlb").hide();
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
			$(".zflb").show();
			$(".dqlb").hide();
			$(".zflb").addClass("zIndex");
        }else if(num==5){
            $(".hylb").hide();
            $(".jglb").hide();
			$(".zflb").hide();
			$(".dqlb").show();
			$(".dqlb").addClass("zIndex");
        }
    }
    
    function selUpstream1(obj){
      $.each($(".hylb .seleced"),function(){
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
    
    //确认行业选择
    function confirmSel1(){
      var seleced = $(".hylb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  $(".hy").val(area);
    }
    
    //确认政府选择
    function confirmSel3(){
      var seleced = $(".zflb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  $(".zf").val(area);
    }
    
    //确认机构选择
    function confirmSel2(){
		closePop();
		var htext=$("#hideorg input").val()
		var hteid=$("#hideorg input").attr("id") 
		$(".jg").val(htext); 
    }
    

    
    //地区
    function confirmSel5(){
      var seleced = $(".dqlb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  $(".dq").val(area);
    }
    
    
   
    //关闭弹出框
    function closePop(){
        $("#covered").hide();
        $("#poplayer").hide();
    }
</script>
<title>异议处理</title>
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
            <#list industryList as it>
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
        <div> </div>
        <div class="listBox">
        	<div>
        		<input id="searchInput3" class="inputSty" type="text" value="" style="width: 140px;"/>
        		<input id="searchBtn3" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
        	</div>
            <table cellpadding="0" cellspacing="0" id="searchTable3">
            <#list sysGoverList as it>
                <tr>
                    <td level="1" id="${it.sys_org_financial_code}" onclick="selUpstream3(this)">
                        <label>${it.sys_org_name}</label>
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
            <span>地区列表</span>
        </div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0" id="searchTable5">
            <#list areaList as it>
                <tr>
                    <td level="1" id="${it.sysAreaCode}" onclick="selUpstream5(this)">
                    	<#if (it.subArea?? && it.subArea?size > 0) >
							<div id="${it.sysAreaId}" name="0" class="open-shrink" onclick="openArea(this, ${it.sysAreaId})">
								<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
							</div>
						</#if>
                        <span class="fll fontSize12">${it.sysAreaName}</span>
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
	<form enctype="multipart/form-data" method="post" action="${request.contextPath}/admin/orgObj/save.jhtml">
	<table  cellspacing="0" cellpadding="0">
		<#if myIndexItemTbList?? !>
			<input type="hidden" id="indexId" name="indexId" value="${indexId}"/>
			<input type="hidden" id="majorId" name="majorId" value="${majorId}"/>
			<input type="hidden" id="defaultId" name="defaultId" value="${defaultId}"/>
			<input type="hidden" id="file" name="file" value="${file}"/>
			<input type="hidden" id="sysOrgId" name="sysOrgId" value="${sysOrgId}"/>
			<input type="hidden" id="operateId" name="operateId" value="${operateId}"/>
			<input type="hidden" id="recordDate" name="recordDate" value="${recordDate}"/>
			<input type="hidden" id="sysOperateList" name="sysOperateList" value="${sysOperateList}"/>
			<input type="hidden" id="sysOperateId" name="sysOperateId" value="${sysOperateId}"/>
			<input type="hidden" id="shouAndrevise" name="shouAndrevise" value="2"/>
			<input type="hidden" id="reportOrgId" name="reportOrgId" value="${mapSon.SYS_ORG_ID}"/>
			<tr>
				<td  width='300' class='noBorderL firstTD'>统一社会信用代码</td>
				<td  width='400' class="secondTD">
					<span class="fontSize12 paddingL5">${mapSon.CODE_CREDIT}</span>
				</td>
			</tr>
			<tr>
				<td class='noBorderL firstTD'>组织机构代码</td>
				<td class="secondTD">
					<span class="fontSize12 paddingL5">${mapSon.CODE_ORG}</span>
				</td>
			</tr>
			<#list myIndexItemTbList as item>
				<tr>
					<td class='noBorderL firstTD'>${item.indexItemName}</td>
					<td  class="secondTD">
						<#if item.indexItemType==1>
							<input  autocomplete="off" disabled="disabled" class="laydate-icon inputSty fontSize12" type="text" name="indexItemValue" value="${myValueList[item_index]}" onclick="laydate({istime: false,max: laydate.now(),format: 'YYYY-MM-DD'})" />
							<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
						<#elseif item.indexItemType==3>
							<#if item.dicType==4>
								<select class="inputSty" disabled="disabled" name="indexItemValue"> 
										<#list dicContentList as d>
											<#if d.dicId==item.dicId>
											<option value="${d.dicContentCode}" <#if d.dicContentValue==myValueList[item_index]>selected</#if>>${d.dicContentValue}</option>
											</#if>
										</#list>
								</select>
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							<#elseif item.dicType==5>
								<input class="inputSty dq" disabled="disabled" type="text" onclick="openPop(5)" name="indexItemValue" value="${myValueList[item_index]}" />
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							<#elseif item.dicType==3>
								<input class="inputSty zf" disabled="disabled" type="text" onclick="openPop(3)" name="indexItemValue" value="${myValueList[item_index]}" />
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							<#elseif item.dicType==2>
								<input class="inputSty jg" disabled="disabled" type="text" onclick="openPop(2)" name="indexItemValue" value="${myValueList[item_index]}" />
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							<#elseif item.dicType==1>
								<input class="inputSty hy" disabled="disabled" type="text" onclick="openPop(1)" name="indexItemValue" value="${myValueList[item_index]}" />
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							<#else>
								<input class="inputSty" disabled="disabled" type="text" name="indexItemValue" value="${myValueList[item_index]}" />
								<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
							</#if>
						<#else>
							<input class="inputSty" disabled="disabled" type="text" name="indexItemValue" value="${myValueList[item_index]}" />
							<input class="inputSty" disabled="disabled" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
								<#list myoperateList as op>
									<#if item.indexItemId==op.indexItemId>
										<#if op.mark==1>
											<input type="checkbox" name="ckName" class="ckName " value="${item.indexItemId}"   />
											<tr><td class='noBorderL firstTD' >报送机构说明</td><td class='secondTD'><input name='operateOrgdesc' value='${op.orgExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >信息主体说明</td><td class='secondTD'><input name='operateInformation' value='${op.maininfoExplain}' class='inputSty'  /></td></tr>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										<#else>
											<tr><td class='noBorderL firstTD' >服务中心说明</td><td class='secondTD'><input value='${op.serverExplain}' disabled="disabled" class='inputSty'/></td></tr>
										</#if>
									</#if>
								</#list>
						</#if>
					</td>
				</tr>
			</#list>
		</#if>
	</table>
	<div class="showBtnBox">
		<input type="button" value="终 止" style="background: #d4281b;" class="sureBtn sureBtnEx" onclick="objSave()">
    	<input type="button" value="取 消" class="cancleBtn closeThisLayer" >
    </div>
    </form> 
</div>
</body>
<script type="text/javascript">
	$(function(){
		var loading;
		//回显
		var msg = "${msg}"; 
		if(msg!=""){
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
			
			layer.close(loading);
		}
	
		//页面加载的时候全部禁用
		$(".ckName").each(function(){
			$(this).attr("checked","checked").siblings(".inputSty").removeAttr("disabled").parents("tr").first().css("color","rgb(255,0,0)");
		});
		
		//取消按钮
		$('.closeThisLayer').on('click', function(){
		  	location.href="${request.getContextPath()}/admin/orgObj/list.jhtml";
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
				var td = $("<td id='"+v.sys_org_financial_code+"' onclick='selUpstream3(this)'><label>"+v.sys_org_name+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput3").val(""));
			});
			layer.close(searchLoading);
		});
	});
</script>
</html>
