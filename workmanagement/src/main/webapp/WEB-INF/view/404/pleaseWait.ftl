<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>异常页面</title>
		<style>
			*{
				margin: 0px;
				padding: 0px;
				font-family: "微软雅黑";
				font-size: 14px;
			}
			#mainCenterDiv{
				height: 300px;
				padding: 30px;
			}
			.changeFont{
				font-size: 16px;
				color: rgb(56,165,226);
			}
			#mainCenterDiv table{
				width: 80%;
				border-left: 1px solid #dadada;
				border-top: 1px solid #dadada;
				margin: auto;
			}
			#mainCenterDiv table td{
				border-right: 1px solid #dadada;
				border-bottom: 1px solid #dadada;
				padding: 6px 8px;
				text-align: center;
			}
			.titleFont{
				border-top: 1px solid #dadada;
				border-left: 1px solid #dadada;
				border-right: 1px solid #dadada;
				padding: 6px 8px;
				text-align: left;
				color: #969696;
			}
			.paddingL50{
				padding-left: 50px;
			}
			.inlineBlock{
				display: inline-block;
			}
		</style>
		<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
	</head>
	<body>
		<div id="mainCenterDiv">
			<table cellspacing="0" cellpadding="0">
				<caption class="titleFont">异常报告</caption>
				<tr>
					<td rowspan="4"><img src="${request.getContextPath()}/assets/images/404.png"/></td>
					<td>原因</td>
					<td>因操作异常，此功能已被系统锁定。</td>
				</tr>
				<tr>
					<td>处理结果</td>
					<td><span class="changeFont"> 请联系系统管理员进行处理  </span></td>
				</tr>
				<!--<tr>
					<td>弱弱地建议</td>
					<td><span><a class="changeFont" href="${request.getContextPath()}/admin/myPanel/myPanel.jhtml"> 返回主页 </a></span><span class="inlineBlock paddingL50">联系管理员</span></td>
				</tr>-->
			</table>
		</div>
	</body>
	<script type="text/javascript">
		$(function(){
			$("#mainCenterDiv").css({"margin-top":($(window).height()-500)/2+"px"});
		})
	</script>
</html>
