<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
		
		</script>
		<title>excel错误或成功列表</title>
	</head>
	<body>
		<div class="showListBox">
		<table>
				<table cellpadding="0" cellspacing="0">
			
				<caption class="titleFont1 titleFont1Ex">报送成功</caption>
				<#list indexItem as it>
					<tr>
						<td width="200" class="noBorderL firstTD">${it.indexItemName}</td>
						<td width="500" class="secondTD">${indexTb[it.indexItemCode]}</td>
					</tr>
				</#list>
		 </table>
				<div class="showBtnBox">
					<input type="button"  class="sureBtn closeThisLayer"  value="关 闭" />
					<input type="button" id="submitBtn" class="sureBtn"  value="删除" />
				</div>
		</div>
	</body>
	<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		$("#submitBtn").click(function(){
				var loading = layer.load();
				var url = "${request.getContextPath()}/admin/reportedDelete/deleteId.jhtml";
						$.post(url,{_:Math.random(),indexId:${indexId},id:${id}},function(data){
							//关闭弹窗
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							},function(){
								parent.layer.close(index); //执行关闭0
								if(data.flag){
									parent.window.location.href="${request.getContextPath()}/admin/reportedDelete/list.jhtml"
								}
							});
							
							
						});
		});
	})
	</script>
</html>
