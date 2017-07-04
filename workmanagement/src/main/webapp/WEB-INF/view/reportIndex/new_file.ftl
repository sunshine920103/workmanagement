<!DOCTYPE html>

<html>
	<head>
		<meta charset="UTF-8"> 
		<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
	</head>
	<body> 
		<div style="padding:10px;text-align:center;">
			<img style="width:100px;height:20px;display:inline"  src="${request.getContextPath()}/assets/images/loading-3.gif" />
			<div style="text-align:center;padding-top:7px"><span id="text" style="color:red;font-size:14px;bold:bolder;">数据导入开始...</span></div>
		</div>
	</body>
	<script type="text/javascript">	
		$(function(){
			var timer = setTimeout(getTxt,15000);
		});
		function getTxt(){
			setTimeout(getTxt,15000);			
			$.ajax({
				url:"${request.getContextPath()}/admin/reportIndex/flash.jhtml",
				cache:false,
				data:{subtime:${subtime}},
				success:function(data){
					if(data.message == 'null'){
						$("#text").text("");
					}else{
						$("#text").text(data.message);
					}
				}
			});
		}
		
	</script>
</html>
