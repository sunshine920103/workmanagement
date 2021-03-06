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
			<table cellpadding="0" cellspacing="0">
                <tr>
                    <td class=" firstTD" style="width: 40%;">报送形式</td>
                    <td class="secondTD">EXCEL报送</td>
                </tr>
                <tr>
                    <td class=" firstTD">报送事项</td>
                    <td class="secondTD">行政许可</td>
                </tr>
                <tr>
                    <td class=" firstTD">上传文件中数据量(条)</td>
                    <td class="secondTD">1</td>
                </tr>
                <tr>
                    <td class=" firstTD">报送文件</td>
                    <td class="secondTD"><a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" href="${request.getContextPath()}/assets/报送数据.xlsx" >下载</a></td>
                </tr>
                <tr>
                    <td class=" firstTD">状态</td>
                    <td class="secondTD">上报成功</td>
                </tr>
                <tr>
                    <td class=" firstTD">报送机构</td>
                    <td class="secondTD">崇左市教育局</td>
                </tr>
                <tr>
                    <td class=" firstTD">报送时间</td>
                    <td class="secondTD">2017-07-01</td>
                </tr>
			</table>
			<div class="showBtnBox">
				<input type="button"  class="sureBtn closeThisLayer"  value="关 闭" />
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
			$.post("${request.getContextPath()}/admin/reportIndex/inTabel.jhtml?reportIndexId=${reportIndex.reportIndexId}",function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						parent.window.location.href = "${request.getContextPath()}/admin/reportIndex/list.jhtml?reportExcelTemplateId=${reportExcelTemplateId}";
					}
					layer.close(index);
				});
			});
		});
	})
	</script>
</html>
