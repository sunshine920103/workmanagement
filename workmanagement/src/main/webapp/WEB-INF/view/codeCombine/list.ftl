<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
		//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
			var loading;
			//回显
			var msg = "${msg}";
			if(msg != "") {
				alertInfo(msg);

				layer.close(loading);
			}
			//下载
			function download(){
		        var url='${request.getContextPath()}/admin/codeCombine/downLoad.jhtml';
		        window.open(url);
		    }
			//上传
			
			$(function(){
			//关闭错误列表
				$("#closeBut").click(function(){
					$("#errorDiv").hide();
				})
			});
				
				
				
				  function ajaxSubmitForm() {
		            var value = $("#file").val();
		            if (value=="") {
		            	layer.alert("请先选择Excle文件", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
//		                alertInfo("请先选择Excle文件");
		                return false;
		            }
		            if (!value.match(/.xls|..xlsx/i)) {
		            	layer.alert("文件格式错误", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
//		                alertInfo("文件格式错误");
		                return false;
		            }
		          $("#upload").submit();
		        }
				
			$(function(){
				var err= "${err}";
				if(err!=null&&err!=""){
					layer.alert(err, {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				}
			});
		</script>
		<title>企业二码合并</title>
	</head>
	<body class="eachInformationSearch">
		<div>
			<div class="queryInputBox marginT10" style="margin-bottom:10px">
				
				<form  id="upload" method="post"  enctype="multipart/form-data" action="${request.getContextPath()}/admin/codeCombine/importExcle.jhtml" style="display: inline-block;*zoom=1;*display:inline" class="marginL20">
						<input class="inputSty" id="file" type="file" name="file" value="上传"  style="margin-left: 10px;"/>
						<input type="button" id="importDe" value="导入二码" class="sureBtn" style="margin-left:0px" onclick="ajaxSubmitForm()"/>   
				</form>
				<input type="button" value="下载模板" class="sureBtn" onclick="download()"/>
			</div>
			<div class="listBox">
				<table>
					<caption class="titleFont1 titleFont1Ex">二码合并列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="80">机构</td>
							<td width="100">合并数量</td>
							<td width="100">提交时间</td>
							<td width="100">操作</td>
						</tr>
						<#list list as li>
						<tr>
							<td width="50">${li_index+1}</td>
							<td width="80">${li.sysManageLogOrgName}</td>
							<td width="100">${li.sysManageLogNewValue} 条</td>
							<td width="100">${li.sysManageLogTime?string('yyyy-MM-dd HH:mm:ss')}</td>
							<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline" 
								onclick="setLayer('二码合并详情','${request.getContextPath()}/admin/codeCombine/queryCode.jhtml?time=${li.sysManageLogTime?string('yyyy-MM-dd HH:mm:ss')}')"
								>详情</a>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
				<#if (dicExchangeLastList?? && dicExchangeLastList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>
				</#if>
				
			</div>
		</div>
	</body>
</html>
