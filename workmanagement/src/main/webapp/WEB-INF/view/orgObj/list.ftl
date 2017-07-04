<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
<meta charset="UTF-8">
<script type="text/javascript">
	$(function(){
		var loading;
		//回显
		var msg = "${msg}";
		if(msg!= "") {
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
			layer.close(loading);
		}
		var msgs="${msgs}";
		if(msgs!= "") {
			alertInfo(msgs);
			$('.layui-layer-shade').height($(window).height());
			layer.close(loading);
		}
		$("#edit").click(function(){
			$("#editForm").submit();
		});
		
		//搜索按钮
		$("#searchBtn").click(function(){
			var searchVal = $.trim($("#searchInput").val());
			var searchLoading = layer.load();
			var tabel = $("#searchTable");
			$.post("${request.getContextPath()}/admin/manualEntry/indexTbByName.jhtml",{name:searchVal},function(data){
				tabel.html("");
				$.each(data,function(i,v){
					var tr = $("<tr></tr>");
					var td = $("<td id='"+v.indexId+"' onclick='selUpstream(this)'><label>"+v.indexName+"</label></td>");
					tr.append(td);
					tabel.append(tr);
					$.trim($("#searchInput").val(""));
				});
				layer.close(searchLoading);
			});
		});
		
		$("#reporate").click(function(){
			if(BusinessCreditCode($("#defaultIndexItemCode").val())==0){
				 layer.alert("统一社会信用代码/组织机构代码/身份证号不合法",{icon:2,shade:0.3,shouldClose:true});
			      return false;  
			}
			if(checkChineseNoSpe($("#jbxxQymc").val())==0){
				 layer.alert("企业名称不合法",{icon:2,shade:0.3,shouldClose:true});
			     return false;  
			}
		});	
	});

	function  objManage(obj,recordDate,defaultIndexItemId,operateId,indexId,sysOrgId,majorId){
		location.href="${request.getContextPath()}/admin/orgObj/revise.jhtml?indexId="+indexId+"&sysOrgId="+sysOrgId+"&operateId="+operateId+"&defaultId="+defaultIndexItemId+"&recordDate="+recordDate+"&majorId="+majorId;
	}
	
    //指标大类弹出
    function openPop() {
        $("#covered").show();
        $("#poplayer").show();
    }
    //关闭上级区域弹出框
    function closePop() {
        $("#covered").hide();
        $("#poplayer").hide();
    }
    function selUpstream(obj){
      $.each($(".seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }
    //确认选择
    function confirmSel(clear) {
        var seleced = $(".seleced");
        closePop();
        var area = $(seleced.find("label")).text();
        if (clear == 1 || seleced.length == 0) {
            $("#openPop").text("请选择指标大类");
            $("#indexId").val("");
        } else {
            $("#openPop").text(area);
            $("#indexId").val(seleced.attr("id"));
        }
    }
</script>
<title>金融机构异议处理</title>
</head>
<body>
	<!--描述：弹出框-->
	<div id="covered"></div>
	<div id="poplayer">
	    <div class="borderBox">
	        <div class="titleFont1">
	            <span>指标大类列表</span>
	        </div>
	        <div></div>
	        <div class="listBox">
	        	<div>
	        		<input id="searchInput" class="inputSty" type="text" value="" style="width: 140px;"/>
	        		<input id="searchBtn" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
	        	</div>
	            <table cellpadding="0" cellspacing="0" id="searchTable">
	            <#list indexTbs as i>
	                <tr>
	                    <td id="${i.indexId}" onclick='selUpstream(this)'>
	                        <label>${i.indexName}</label>
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
	</div>	
	<form id="searchForm" method="post">
		<input type="hidden" name="jbxxQymc" value="${jbxxQymc}"/>
		<input type="hidden" name="defaultIndexItemCode" value="${defaultIndexItemCode}"/>
		<input type="hidden" name="indexId" value="${indexId}"/>
		<input type="hidden" name="recordDate" value="${recordDate}"/>
		<input type="hidden" name="selectId" value="${selectId}"/>
		<input type="hidden" name="status" value="${status}"/>
	</form>
	<form id="editForm" method="post" enctype="multipart/form-data" action="${request.contextPath}/admin/orgObj/edit.jhtml"></form>
	<div class="eachInformationSearch">
	<div class="queryInputBox" style="margin-bottom:10px;">
		<div class="listBox" style="margin-top: 20px;margin-bottom: 10px;">
			<form method="post" id="reporateForm" action="${request.contextPath}/admin/orgObj/list.jhtml">
				<div class="marginT10">
					<span class="fontSize12">选择指标大类：</span>
					<#if indexName??>
						<a id="openPop" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()">${indexName}</a>
					<#else>
						<a id="openPop" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()">请选择指标大类</a>
					</#if>
					<input type="hidden" id="selectId" name="selectId" value="1"/>
					<input type="hidden" id="indexId" name="indexId" value=""/>
					<span class="fontSize12 marginL20">处理状态：</span>
					<select name="status" id="status" class="inputSty">
						<option value="">请选择</option>
						<option value="0" <#if status=='0'>selected</#if>>待处理</option>
						<option value="1" <#if status=='1'>selected</#if>>处理中</option>
						<option value="2" <#if status=='2'>selected</#if>>已处理</option>
					</select>
	             	<span class="fontSize12 marginL20">推送时间：</span><input id="gdTime" name="recordDate" id="recordDate" autocomplete="off" class="inputSty fontSize12" onclick="laydate({istime: false, format: 'YYYY-MM-DD',max:laydate.now()})" value="${recordDate}">
				</div>
				<div class="marginT10">
					<span class="fontSize12">统一社会信用代码/组织机构代码/身份证号：</span><input class="inputSty" id="defaultIndexItemCode" name="defaultIndexItemCode" type="text" value="${defaultIndexItemCode}"/>
					<span class="fontSize12 marginL20">企业名称：</span><input class="inputSty" id="jbxxQymc" name="jbxxQymc" type="text" value="${jbxxQymc}"/>
					<input type="submit" class="sureBtn" id="reporate" value="查 询" style="margin-left:0px;" />
				</div>
			</form>
		</div>
		<input id="edit" class="sureBtn" type="button" value="异议处理" style="margin-left:30px;">
	</div>
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
					<td width="100">状态</td>
					<td width="100">操作</td>
				</tr>
			<#list sysOperateList as sol>
				<tr>
            		<td>${(1 + sol_index) + (page.getPageSize() * page.getCurrentPage())}</td>
            		<#--<td>${sol.codeCredit}/${sol.codeOrg}</td>
            		<td>${sol.jbxxQimc}</td>        -->
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
					<#if sol.sysOperateStatus==0 || sol.sysOperateStatus==1>
						<#if sol.sysOperateStatus==1>
							<#if sol.reportOrgId==orgId>
								<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="objManage(this,'${(sol.recordDate?string("yyyy-MM-dd"))!}','${sol.defaultIndexItemId}','${sol.sysOperateId}','${sol.indexItemId}','${sol.reportOrgId}','${sol.dataId}')">异议处理</a>
							<#else>
							</#if>
						<#else>
							<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="objManage(this,'${(sol.recordDate?string("yyyy-MM-dd"))!}','${sol.defaultIndexItemId}','${sol.sysOperateId}','${sol.indexItemId}','${sol.reportOrgId}','${sol.dataId}')">异议处理</a>
						</#if>
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
	</div>
</body>
</html>
