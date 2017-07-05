<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<#include "/fragment/common.ftl"/>
		<title>指标详情页面</title>
		<script type="text/javascript">
		$(function(){
		//回显
		var msg = "${msg}";
		if(msg != "") {
			layer.alert(msg,{
				icon: (msg=="操作成功")?1:2,
				shade:0.3,
				shadeClose:true
			},function(){
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			
		    	parent.layer.close(index); //执行关闭
				
			});
		}
		});
			function isAddIndexItem(indexId,obj){
				if($("#invalid").html() == "无效"){
					layer.alert("该指标大类已无效，无法新增指标项",{icon:2,shade:0.3,shouldClose:true});
				}else{
					$.post("${request.getContextPath()}/admin/indexTb/checkBeforeAdd.jhtml",{indexId:indexId},function(data){	
							var loading = layer.load();			
							layer.close(loading);
							if(!data.flag){
								var index = alertInfoFun(data.message, data.flag, function(){	
									layer.close(index);						
								});
							}else{
								setLayer('新增指标项','${request.getContextPath()}/admin/indexTb/addSon.jhtml?indexId=${indexTb.indexId}');
								//parent.window.location.href = "${request.getContextPath()}/admin/indexTb/lookOrModifyItem.jhtml?indexItemId="+indexItemId;
							}
					});
					
				}
			}
			
			function exportIndexItem(indexId){
			$("#ikk").val(indexId);
			 $.ajax({
				 	type: "POST",
				 	url:"${request.getContextPath()}/admin/indexTb/exportIndexItem.jhtml",
				 	data:$('#kk').serialize(),              
				 	error: function(request) {
				 		alert("Connection error");},
				 	success: function(data) { 
				 		//window.location.href = "${request.getContextPath()}/admin/indexTb/download.jhtml?ke="+data;
				 	}
				 });
			}
			
			function exportExcel(indexId){
				window.location.href = "${request.getContextPath()}/admin/indexTb/exportIndexItem.jhtml?indexId=" + indexId ;
			}
					
	
		</script>
	</head>
	<body>
	<form id="kk" method="post" >
		<input id="ikk" type="hidden" value="" name="indexId"></input>
	</form>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0" >
				<caption class="titleFont1 titleFont1Ex">指标大类详情</caption>
					<tr style="display: none;">
						<td>指标大类名称</td>
						<td id="id" name="id">${indexTb.indexId}</td>
					</tr>
					<tr>
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>指标大类名称：</td>
						<td id="indexName" width="500" class="secondTD">${indexTb.indexName}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>识别码：</td>
						<td class="secondTD">${indexTb.indexCode}<span class="fontSize12 marginL20" style="color: #666666;">注：指标大类的识别码全局唯一且不支持修改。</span></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>覆盖区域：</td>
						<td class="secondTD">${indexTb.sysAreaName}</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>指标状态：</td>
						<#if indexTb.indexUsed == 1>
							<td id="invalid" class="secondTD">有效</td>
						<#else>
							<td id="invalid" class="secondTD">无效</td>
						</#if>
					</tr>
					<tr>
						<td class="noBorderL firstTD">描述</td>
						<td class="secondTD">崇左市32家市级单位及7个区县行政许可信息</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">指标类型</td>
						<td class="secondTD">
							<#if indexTb.indexType == 1>
								<label for="type0"><input type="radio" disabled="disabled" name="zblx" checked="checked" id="type0"/>行政许可</label>
								<label for="type1"><input type="radio" disabled="disabled" name="zblx" id="type1"/>行政处罚</label>
							<#else>
								<label for="type0"><input type="radio" disabled="disabled" name="zblx" id="type0"/>行政许可</label>
								<label for="type1"><input type="radio" disabled="disabled" name="zblx" checked="checked" id="type1"/>行政处罚</label>
							</#if>
							<#--<span  style='font-size:12px;color:#787878;margin-left:10px '>
							(业务信息=增量信息、基本信息=全量信息)
							</span>-->
						</td>
					</tr>
			</table>
			
			<div class="verticalMiddle marginT20 marginLR30">
			<#if flag>
				<input type="button" class="bigBtn  marginB20" value="复制为省级指标" onclick="setLayer('复制为省级指标','${request.getContextPath()}/admin/indexTb/copyl.jhtml?indexId=${indexTb.indexId}');"/>
			</#if>	
				<input type="button" class="bigBtn  marginB20 marginR30" value="新增指标项" onclick="isAddIndexItem(${indexTb.indexId},this)"/>				
				<input type="button" class="bigBtn  marginB20" value="指标项导出" onclick="exportExcel(${indexTb.indexId})"/>
			</div>
		</div>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0" >
				<caption class="titleFont1 titleFont1Ex">指标项列表</caption>
					<tr>
						<td>指标项名称</td>
						<td>状态</td>
						<td>数据类型</td>
						<td>所属区域</td>
						<#--<td>是否识别码</td>-->
						<td>是否可以为空</td>
						<td>操作</td>
					</tr>
				<#list itemList as item>
					<tr>
						<#if item.sysAreaName=="四川省">
							<td>${item.indexItemName}</td>
						<#else>
							<td><#if item.shi??>${item.shi}-</#if>${item.sysAreaName}-${item.indexItemName}</td>
						</#if>
						<#if item.indexItemUsed == 0 >
							<td>无效</td>
						<#else>
							<td>启用</td>
						</#if>						
						<#if item.indexItemType == 0 >
							<td>字符</td>
						<#elseif item.indexItemType == 1>
							<td>时间</td>
						<#elseif item.indexItemType == 2>
							<td>数值</td>
						<#else>
							<td>字典</td>
						</#if>
						<td>崇左市</td>
						<#--<td>
							<#if item.indexItemImportUnique==0>
								否
							<#else>
								是
							</#if>
						</td>-->
						<td>
							<#--<#if item.indexItemEmpty==0>-->
								否
							<#--<#else>
								是
							</#if>-->
						</td>
						<td>
							<span class="changeFont fontSize12 cursorPointer hasUnderline"  
							onclick="setLayer('修改指标项','${request.getContextPath()}/admin/indexTb/lookOrModifyItem.jhtml?indexItemId=${item.indexItemId}');">修 改</span> 
						</td>
					</tr>
				</#list>
			</table>
			<div class="showBtnBox">
                <input  class="cancleBtn closeThisLayer" type="button" value="关 闭" />
            </div>
		</div>
	</body>
	<script type="text/javascript">
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});	
	</script>
</html>
