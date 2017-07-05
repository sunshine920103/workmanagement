<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >



		</script>
		<title>不合规数据列表</title>
	</head>
	<body>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
                <tr>
                    <td class=" firstTD" style="width: 40%;">行政相对人名称</td>
                    <td class="secondTD">崇左市金帆船务有限公司</td>
                </tr>
                <tr>
                    <td class=" firstTD">项目名称</td>
                    <td class="secondTD">桂金帆659</td>
                </tr>
                <tr>
                    <td class=" firstTD">行政相对人代码</td>
                    <td class="secondTD">9134010078</td>
                </tr>
                <tr>
                    <td class=" firstTD">许可内容</td>
                    <td class="secondTD">国籍登记</td>
                </tr>
                <tr>
                    <td class=" firstTD">法定代表人姓名</td>
                    <td class="secondTD">李传云</td>
                </tr>
                <tr>
                    <td class=" firstTD">许可机关</td>
                    <td class="secondTD">崇左市交通运输局</td>
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
