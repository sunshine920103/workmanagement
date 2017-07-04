<!DOCTYPE HTML>
<html>

	<head>
		<#include "/fragment/common.ftl"/>
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>
		<script type="text/javascript">
		function inputIsNull(){
			if($("input:radio[name='indexs']:checked").length == 0){
				alert("指标项未选择");
					return false;
			}else{
				layer.load(0);
				return true;
			}
		}
			$(function() {
				//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.msg(msg, {
						icon: (msg=="操作成功")?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
				$("input[name='industry']").val($(this).find(":selected").text());
				$("#industryid").change(function() {
					$("input[name='industry']").val($(this).find(":selected").text());
				});
				//保存
				$("#keyentErprisesSetForm").submit(function(){
					var name = $("input[name='name']").val();
//					var Size = $("input[name='Size']").val();
					var keyentErprisesSetOrg = $("input[name='keyentErprisesSetOrg']").val();
					if($.trim(name) == "" ||  $.trim(keyentErprisesSetOrg) == "") {
						layer.msg("请填写完整", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
						return false;
					}
					var fileName = $("#file").val();
					var reg=/(.*).(xls|xlsx)$/; 
				 
						if(!reg.test(fileName)) {
					        layer.alert("文件不是excel格式", {
								icon: 2,
								shade: 0.3,
								shadeClose: true
							});
					        return false;
					   } 
					
				});
			});
		</script>
		<title>选择重点企业监测群查询指标</title>
	</head>
	<body>
		<form id="searchForm" method="post">
		</form>
		<#-- 新增框 -->
			<div class="showListBox">
				
				<form id="keyentErprisesSetForm" enctype="multipart/form-data" action="${request.getContextPath()}/admin/keyEnterpriseQuery/allList.jhtml" method="post">

					<input name="id" type="hidden" value="${keyentErprisesSet.keyentErprisesSetId}" />
					<table id="table" cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex" style="text-align:center;">选择重点企业监测群查询指标</caption>
					<input type="hidden" name="id" value="keyentErprisesSet.keyentErprisesSetId"/>
						<tr>
							<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>重点企业监测群名称</td>
							<td width="500" class="secondTD">
								<input readonly="readonly" name="name" class="fontSize12" style="width: 200px;" value="${keyentErprisesSet.keyentErprisesSetName}" />
							</td>
						</tr>
						<!--<tr>
							<td class="noBorderL firstTD">行业</td>
							<td class="secondTD">
								<input readonly="readonly" class="fontSize12" name="hangye" value="${keyentErprisesSet.keyentErprisesSetTrade}"/>
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">规模</td>
							<td class="secondTD">
								<input readonly="readonly" class="fontSize12" name="Size" value="${keyentErprisesSet.keyentErprisesSetSize}" />
							</td>
						</tr>-->
						<tr>
							<td class="noBorderL firstTD">企业数量</td>
							<td class="secondTD">
								<input readonly="readonly" class="fontSize12" name="qieyeNum" value="${qieyeNum}" />
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">上传机构</td>
							<td class="secondTD">
								<input readonly="readonly" class="fontSize12" style="width: 200px;"  name="keyentErprisesSetOrg" value="${keyentErprisesSet.keyentErprisesSetOrgName}" />
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">归档时间</td>
							<td class="secondTD">
								<input readonly="readonly" class="fontSize12" name="keyentErprisesSetRecordTime" value="${(keyentErprisesSet.keyentErprisesSetRecordTime?string('yyyy-MM-dd'))!}" />
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD"><label class="mainOrange"> * </label>选择指标大类</td>
							<td class="secondTD">
								<#list indexTbs as ips>
								<label style="display:inline-block;padding-right:20px;">
									<input class="verticalMiddle" type="radio" name="indexs" value="${ips.indexCode}"><span class="paddingR10 paddingL5 fontSize12 inlineBlock marginT5">${ips.indexName}</span>
								</label>
								<!--<span class="inlineBlock">
									<input class="verticalMiddle" type="checkbox" name="indexs" value="${ips.indexName}"><span class="fontSize12 inlineBlock margin52050">${ips.indexName}</span>
								</span>-->
								</#list>
							</td>
						</tr>
						
					</table>
					<div id="ajaxresult"></div>
					<div class="showBtnBox">
						<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
						<input type="submit" class="sureBtn" onclick="return inputIsNull();" value="确 认" />
					</div>
				</form>
			</div>
	</body>
<script>
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
	$('.closeThisLayer').on('click', function(){
    	parent.layer.close(index); //执行关闭
	});
	</script>
</html>