 <!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg,{
						icon: (msg=="操作成功")?1:2,
						shade:0.3,
						shadeClose:true
					});
				}
			
			});
			
			function testDic(obj){
				var val = $(obj).val();
				var reg = /^\d+(\.\d+)?$/;
				if(reg.test(val)==false){
					if($(obj).next(".redWarm3").length==0){
						var str = "";
						str = "<span class='redWarm3'>请输入正确的汇率格式</span>";
						$(obj).after(str);
					}
					return false;
				}else{
					$(obj).nextAll(".redWarm3").remove();
				}
			}
		</script>
		<title>增加修改汇率</title>
	</head>
	<body>
		<div class="showListBox">
			
			<form id="addData" action="${request.getContextPath()}/admin/dicExchangeLast/editSubmit.jhtml?" method="post">
		
				<table>
					<#if dicExchangeLast == null>
						<caption class="titleFont1 titleFont1Ex">增加汇率</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改汇率</caption>
					</#if>
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>币种</td>
						<td style="width:65%;" class="secondTD">
							<input id="dicExchangeName" name="dicExchangeName" class="inputSty allbzVal" value="${dicExchangeLast.dicExchangeName}" onblur="onblurVal(this,1,20)" maxlength="20" />
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>代码</td>
						<td class="secondTD">
							<input id="dicExchangeCode" name="dicExchangeCode" class="inputSty alldmVal" value="${dicExchangeLast.dicExchangeCode}" onblur="onblurVal(this,6,20)"  maxlength="20" />
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>汇率</td>
						<td class="secondTD">
							<input  id="dicExchangeValue" name="dicExchangeValue" class="inputSty alldicExchangeVal" value="${dicExchangeLast.dicExchangeValue}" onblur="testDic(this)"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>时间</td>
						<td class="secondTD">
							<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="dicExchangeTime" onclick="laydate({istime: false, format: 'YYYY-MM-DD',max: laydate.now()})" name="dicExchangeTime" value="${dicExchangeLast.dicExchangeTime?substring(0,10)}" >
						</td>
					</tr>
				</table>
				<p class="warmFont marginB20  margin2030 fontSize12">（汇率为币种与人民币的比值）</p>
				<div class="showBtnBox">
					<input type="button"  class="cancleBtn closeThisLayer" value="取 消" />
					<input type="button" id="formVer" class="sureBtn" value="确 认" />
				</div>
			</form>
		</div>
	</body>
	<script>
	$(function(){
		
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
	$('.closeThisLayer').on('click', function(){
    	parent.layer.close(index); //执行关闭
	});
	$("#formVer").click(function(){
				var dicExchangeName = $("#dicExchangeName").val();
				var dicExchangeValue = $("#dicExchangeValue").val();
				var dicExchangeCode = $("#dicExchangeCode").val();
				var dicExchangeTime = $("#dicExchangeTime").val();
//				var verify=/^\d+(\.\d+)?$/;
				if($.trim(dicExchangeName)==""){					
					layer.alert("请填写币种", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
				if(checkChinese(dicExchangeName)== 0){
					layer.alert("请填写正确的币种名称", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
				if($.trim(dicExchangeCode)==""){
					layer.alert("请填写代码", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;	
				}
				if(checkEnglish(dicExchangeCode)==""){
//					alertInfo("代码输入不合法");					
					layer.alert("代码输入不合法", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
			
				if(checkExchangeRate(dicExchangeValue)== 1){
//					alertInfo("汇率不能为空");					
					layer.alert("汇率不能为空", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
//				if(checkExchangeRate(dicExchangeValue)== 0){
//					alertInfo("汇率输入不合法");
				if(testDic($("#dicExchangeValue"))==false){
					layer.alert("汇率输入不合法", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
				
				if(dicExchangeValue==0||dicExchangeValue==0.0){
//					alertInfo("汇率不能为0");

					layer.alert("汇率不能为0", {
						icon:2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}
//				else if(verify.test(dicExchangeValue)==false){
//					layer.msg("汇率为数值类型，长度为11位整数到4位小数", {
//						icon:2,
//						shade: 0.3,
//						shadeClose: true
//					});
//				}
//				else{
					var loading = layer.load();
					$.post("${request.getContextPath()}/admin/dicExchangeLast/editSubmit.jhtml",$("#addData").serialize(),function(data){
						layer.close(loading);
						var index = alertInfoFun(data.message, data.flag, function(){
							if(data.flag){
								parent.window.location.href = "${request.getContextPath()}/admin/dicExchangeLast/list.jhtml";
							}
							layer.close(index);
						});
					});
//				}
		});
		
		
		
	});
	</script>
</html>
