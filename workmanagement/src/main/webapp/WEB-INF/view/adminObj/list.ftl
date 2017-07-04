<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
	<meta charset="UTF-8">
	
	<script type="text/javascript">
		$(function(){
	//回显
	var msg = "${msg}";
	if(msg!= "") {
		alertInfo(msg);
	}
			$("#search1").click(function(){
			if(BusinessCreditCode($("#defaultIndexItemCode").val())==0){
				 layer.alert("统一社会信用代码/组织机构代码/身份证号不合法",{icon:2,shade:0.3,shouldClose:true});
			      return false;  
			}
			if(checkChineseNoSpe($("#orgName").val())==0){
				 layer.alert("企业名称不合法",{icon:2,shade:0.3,shouldClose:true});
			     return false;  
			}
			
			var timeReport=$("#gdTime").val()
			var indexId=$("#indexId").val()
			var defaultIndexItemCode=$("#defaultIndexItemCode").val()
			var orgName=$("#orgName").val()
			$("#formId").submit();	
		});	
		$("#edit").click(function(){
				window.location.href="${request.contextPath}/admin/adminObj/edit.jhtml";
			})
		});

		function  objManage(obj,recordDate,defaultIndexItemId,operateTime,indexId,sysOrgId,reportOrgId,dtbId,sysOperateId){
			location.href="${request.getContextPath()}/admin/adminObj/revise.jhtml?indexId="+indexId+"&sysOrgId="+sysOrgId+"&operateTime="+operateTime+"&defaultId="+defaultIndexItemId+"&recordDate="+recordDate+"&reportOrgId="+reportOrgId+"&dtbId="+dtbId+"&sysOperateId="+sysOperateId;
	
		
		}
		function  objManage1(obj,recordDate,defaultIndexItemId,operateTime,indexId,sysOrgId,reportOrgId,dtbId,sysOperateId){
			location.href="${request.getContextPath()}/admin/adminObj/showOperate.jhtml?indexId="+indexId+"&sysOrgId="+sysOrgId+"&operateTime="+operateTime+"&defaultId="+defaultIndexItemId+"&recordDate="+recordDate+"&reportOrgId="+reportOrgId+"&dtbId="+dtbId+"&sysOperateId="+sysOperateId;
		}
		  //关闭上级区域弹出框
	    function closePop(){
	        $("#covered").hide();
	        $("#poplayer").hide();
	    }
	     //打开弹窗
	    function openPop(){
	        $("#covered").show();
	        $("#poplayer").show();
	    } 
	        
	    function selUpstream(obj){
	      $.each($(".seleced"),function(){
	          $(this).removeClass("seleced");
	      });
	      $(obj).addClass("seleced");
	    }
	    
	   
		
		 function confirmSel2(clear){
	       var seleced = $(".seleced");
	       closePop();
	       var area = $(seleced.find("label")).text();
	       if(clear===1||seleced.length==0){
	          $("#indexSelect").text("选择指标大类");
	          $("#indexId").val("");
	       }else{
	          $("#indexSelect").text(area);
			  $("#indexId").val(seleced.attr("id"));
	       }
	    }
	   //模糊查询指标大类
	    function test(obj){
		    var v=$("#searchInput1").val();
		    	$.post("${request.getContextPath()}/admin/adminObj/editmohu.jhtml",{indexName:v},function(result){
				str_json=eval("("+result+")");
				var con="";
				$('#searchTable tr').remove();
				$.each(str_json,function(a,b){
				var tr ="<tr><td id='"+b.indexId+"' onclick='selUpstream(this)'><label>"+b.indexName+"</label></td></tr>";
				$("#searchTable").append(tr);
				});
		     });
	    }
</script>
<title>金融机构异议处理</title>
</head>
	<body >
		<!--描述：弹出框-->
	<div id="covered"></div>
	<div id="poplayer">
	
	  <div class="borderBox">
	        <div class="titleFont1">
	            <span>指标大类列表</span>
	        </div>
	        <div class="listBox">
	        	<div>
	        		<input id="searchInput1" class="inputSty" type="text" value="" style="width: 140px;"/>
	        		<input id="searchBtn1" type="submit" value="搜 索" onclick="test(this)" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
	        	</div>
	            <table cellpadding="0" cellspacing="0" id="searchTable">
				<#list indexTb as it>
	                <tr>
	                    <td level="1" id="${it.indexId}" onclick="selUpstream(this)">
	                        <label>${it.indexName}</label>
	                    </td>
	                </tr>
				</#list>
	            </table>
	        </div>
	        <div class="btnBox">
	            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
	            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
	        </div>
	    </div>
	</div>
	<div class="eachInformationSearch">
		
	
	<form id="searchForm" method="post">
		<input   name="indexId"  type="hidden" value="${indexId}"  />
		<input   name="defaultIndexItemCode"  type="hidden" value="${defaultIndexItemCode}"  />
		<input   name="orgName"  type="hidden" value="${orgName}"  />
		<input   name="struts"  type="hidden" value="${struts}"  />
		<input   name="time"  type="hidden" value="${time}"  />
		<input   name="selectId"  type="hidden" value="${selectId}"  />
	</form>
	<form id="editForm" method="post" enctype="multipart/form-data" action="${request.contextPath}/admin/adminObj/reportList.jhtml"></form>
	<div class="queryInputBox" style="margin-bottom:10px;">
		<div class="listBox fontSize12" style="margin-top: 20px;margin-bottom: 10px;">
			<form id="formId"  method="post" action="${request.contextPath}/admin/adminObj/list.jhtml">
				<div class="marginT10">
					<span class="fontSize12">选择指标大类：</span>
					<#if indexName??>
						<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()"  id="indexSelect">${indexName}</a>
					<#else>
						<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()"  id="indexSelect">选择指标大类</a>
					</#if>
	             	<input type="hidden" id="indexId" name="indexId" value=""/>
	             	<input type="hidden" id="selectId" name="selectId" value="3333"/>
	             	<span class="fontSize12 marginL20">处理状态：</span>
					<select name="status" id="status" class="inputSty"  >
						<option value="">请选择</option>
						<option <#if (struts==0) >selected=selected</#if> value="0">待处理</option>
						<option <#if (struts==1) >selected=selected</#if> value="1">处理中</option>
						<option <#if (struts==2) >selected=selected</#if> value="2">已处理</option>
					</select>
	             	<span class="fontSize12 marginL20">推送时间：</span><input id="gdTime" name="time" autocomplete="off" value="${time}" class="inputSty fontSize12" onclick="laydate({istime: false, format: 'YYYY-MM-DD',max:laydate.now()})"/>
				</div>
				<div class="marginT10">
					<span class="fontSize12">统一社会信用代码/组织机构代码/身份证：</span><input class="inputSty" type="text" value="${defaultIndexItemCode}" id="defaultIndexItemCode"  name="defaultIndexItemCode" />
					<span class="fontSize12 marginL20">企业名称：</span><input class="inputSty" type="text" value="${orgName}"  id="orgName" name="orgName" />
					<input type="button" class="sureBtn sureBtnEx" id="search1" value="查 询" style="margin-left: 0px;"/>
				</div>
			</form>
		</div>	
		<input id="edit" class="sureBtn" type="button" value="异议处理" style="margin-left:30px;">	
	<div class="listBox">
		<table cellpadding="0" cellspacing="0">
			<caption class="titleFont1 titleFont1Ex">异议列表</caption>
			<tbody>
				<tr class="firstTRFont">
					<td width="50">序号</td>
					<#--<td width="100">企业二码</td>
					<td width="150">企业名称</td>-->
					<td width="100">指标大类</td>
					<#--<td width="150">推送机构</td>-->
					<!--<td width="100">推送时间</td>-->
					<td width="150">报送机构</td>
					<td width="100">报送时间</td>
					<td width="100">状 态</td>
					<td width="100">操作</td>
				</tr>
			<#list sysOperateList as sol>
				<tr>
            		<td>${(1 + sol_index) + (page.getPageSize() * page.getCurrentPage())}</td>
            		<#--<td>${sol.codeCredit}/${sol.codeOrg}</td>-->
            		<#--<td>${sol.jbxxQimc}</td>        -->
					<td>${sol.indexName}</td>
					<#--<td>${sol.sysOrgName}</td>-->
					<!--<td>${(sol.sysOperateTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>-->
					<td>${sol.sysReportOrgName}</td>
					<td>${(sol.recordDate?string("yyyy-MM-dd"))!} </td>
					<td>
					<#if sol.sysOperateStatus==1>
					处理中
					<#elseif sol.sysOperateStatus==2>
					已处理
					<#else>
					待处理
					</#if>
					</td>
					<td>
					<#if orgId == sol.reportOrgId && sol.sysOperateStatus==1>
						<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('异议处理','${request.getContextPath()}/admin/adminObj/revise.jhtml?sysOperateId=${sol.sysOperateId}&dtbId=${sol.dataId}&reportOrgId=${sol.reportOrgId}&recordDate=${(sol.recordDate?string("yyyy-MM-dd"))!}&defaultId=${sol.defaultIndexItemId}&operateTime=${(sol.sysOperateTime?string("yyyy-MM-dd"))!}&sysOrgId=${sol.sysOrgId}&indexId=${sol.indexItemId}&qb=2')">异议处理</a>
					<#elseif sol.sysOperateStatus==0>
						<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('异议处理','${request.getContextPath()}/admin/adminObj/showOperate.jhtml?sysOperateId=${sol.sysOperateId}&dtbId=${sol.dataId}&reportOrgId=${sol.reportOrgId}&recordDate=${(sol.recordDate?string("yyyy-MM-dd"))!}&defaultId=${sol.defaultIndexItemId}&operateTime=${(sol.sysOperateTime?string("yyyy-MM-dd"))!}&sysOrgId=${sol.sysOrgId}&indexId=${sol.indexItemId}')">异议处理</a>
					<#else>
					</#if>
					</td>
				</tr>			
			</#list>
			</tbody>
		</table>
		<#if (sysOperateList?? && sysOperateList?size > 0)>
			<#include "/fragment/paginationbar.ftl"/>
		<#else>
			<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
				<tr class="firstTRFontColor">
					<td style="text-align: center;font-weight: bold;" >暂无信息</td>
				</tr>
			</table>	    	
		</#if>
	</div>
</body>
</html>
