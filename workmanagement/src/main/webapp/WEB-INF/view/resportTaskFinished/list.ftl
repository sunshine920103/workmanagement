<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/> 
		<script type="text/javascript">
			$(function(){
				$("#submi").click(function(){
					var search=$("#taskName").val();
					if(checkChineseNoSpe(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			    } 
					$("#selectExcel").submit(); 
				})
			})
		</script>
		<title>任务完成情况统计</title>
	</head>
	<body class="eachInformationSearch">
	<form id="searchForm" method="post">
		<input   name="taskType"  type="hidden" value="${taskType}"  />
		<input   name="task_begin"  type="hidden" value="${task_begin}"  />
		<input   name="task_end"  type="hidden" value="${task_end}"  />
		<input   name="task_name"  type="hidden" value="${task_name}"  />
		<input   name="url"  type="hidden" value="${url}"  />
	</form>
		<div class="listBox fontSize12" style="margin-bottom: 0px;">
		
			<form  id="selectExcel"  action="${request.getContextPath()}/admin/resportTaskFinished/list.jhtml" method="post">
			    <div class="marginT10">
			    	<label class="fontSize12">任务状态：</label>
			    	<select id="taskType" name="taskType" class="inputSty">
			    		<option selected value=3>全部</option>
			    		<option <#if taskType==0>selected</#if> value=0>未完成</option>
			    		<option <#if taskType==2>selected</#if> value=2>逾期</option>
			    		<option <#if taskType==1>selected</#if> value=1>已完成</option>
			    	</select>
			    </div>
			    <div class="marginT10 fontSize12">
			    	结束时间：
			    	<input class="laydate-icon inputSty fontSize12" style="width: 120px;" id="task_begin"  name="task_begin" value="${task_begin}"> ~
					<input class="laydate-icon inputSty fontSize12" id="task_end" style="width: 120px;" name="task_end" value="${task_end}">
			    </div>
			    <div class="marginT10 fontSize12">
			    	<label for="taskName" class="fontSize12">任务名称：<input id="taskName" type="text" name="task_name" value="${task_name}" class="inputSty"/></label>			    	
			    	<input type="button" id="submi" class="sureBtn sureBtnEx" value="查  询" />
			    </div>
		    </form>
		</div>
		<div class="listBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">任务完成查询列表</caption>
			
				<tr class="firstTRFont">
					<td class="txc" width="50">序号</td>
					<td class="txc" width="120">任务名称</td>
					<td class="txc" width="150">任务结束时间</td>
					<td width="100">任务类型</td>
					<td class="txc" width="100">任务状态</td>
					<td class="txc" width="150">任务机构</td>
				</tr>
				<#list reportTaskPushSetList as item>
				<tr>
					<td class="txc" width="50">${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
					<td class="txc" width="120">${item.reportTaskPushSetName}</td>
					<td class="txc" width="150"><#if item.reportTaskPushListEndTime??>${item.reportTaskPushListEndTime?string('yyyy-MM-dd')}</#if></td>
					<#if item.reportTaskPushSetType==0>
						<td width="100">WORD报送</td>
					<#else>
						<td width="100">excel报送</td>
					</#if>
					<#if item.reportTaskPushStatus==0>
						<td class="txc" width="100">完成</td>
					<#elseif item.reportTaskPushStatus==1>
						<td class="txc" width="100">完成</td>
					<#else>
						<td class="txc" width="100">完成</td>
					</#if>
					<td class="txc" width="150">${item.sysOrgName}</td>
				</tr>
				</#list>
			</table>
			<#if (reportTaskPushSetList?? && reportTaskPushSetList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table cellspacing="0" cellpadding="0" class="noBorderT">
						<tr class="firstTRFont">
							<td style="text-align: center;">暂无数据</td>
						</tr>
					</table>
				</#if>
		</div>
	<script>
        var start = {
  			elem: '#task_begin',
  			format: 'YYYY-MM-DD',
  			//min: laydate.now(), //设定最小日期为当前日期
 			// max: laydate.now(), //最大日期
  			istime: true,
  			istoday: true,
  			choose: function(datas){
    			 end.min = datas; //开始日选好后，重置结束日的最小日期
    			 end.start = datas //将结束日的初始值设定为开始日
  			}
		};
		var end = {
  			elem: '#task_end',
  			format: 'YYYY-MM-DD',
  			//min: laydate.now(),
//  			max: laydate.now(),
  			istime: true,
  			istoday: true,
  			choose: function(datas){
    			start.max = datas; //结束日选好后，重置开始日的最大日期
  			}
		};
		laydate(start);
		laydate(end);
	</script>	
	</body>
</html>
