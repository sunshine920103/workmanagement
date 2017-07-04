<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg!=""){
					layer.alert(msg,{
						icon:("保存成功"==msg?1:2),
						shade:0.3
					});
				}
			});
		
			
			
		</script>

	</head>
	<body>
		
	
		<#-- 新增框 -->
		<div class="showListBox">
			
			<form id="form" action="${request.getContextPath()}/admin/sysClassFy/save.jhtml" method="post">
				<input name="sysIndustryId" type="hidden" value="${it.sysIndustryCode}"/>
				<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">新增行业分类</caption>
					
					<tr >
						<td width="200" class="noBorderL firstTD"><label class="mainOrange"> * </label>行业类别名称</td>
						<td width="400" class="secondTD">
							<input id="sysIndustryName" name="sysIndustryName" class="inputSty required allnameVal" value="${it.sysIndustryName}" onblur="onblurVal(this,13,50)" title="必填，不能超过30个字" maxlength="30"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>行业类别代码</td>
						<td class="secondTD">
							<input id="sysIndustryCode"  name="sysIndustryCode" class="inputSty required allletterVal" value="${it.sysIndustryCode}" onblur="onblurVal(this,21,20)" title="必填，不能超过20个字" maxlength="20"></input>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sysIndustryNotes" class="fontSize12 textareaSty" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)" maxlength="50">${it.sysIndustryNotes}</textarea>
						</td>
					</tr>
				</table>
				
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
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
		
		
		
		$("#submitBtn").click(function(){
			if(checkChineseNoSpe12($("input[name='sysIndustryName']").val())==1) {
//				layer.confirm("行业类别名称不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("行业类别名称不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysIndustryName']").focus();
                return false;
	        }
			if(checkChineseNoSpe12($("input[name='sysIndustryName']").val())==0) {
//				layer.confirm("行业类别名称输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("行业类别名称输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
               $("input[name='sysIndustryName']").focus();
                return false;
	        }
			
			if(checkNoDataHeader12($("input[name='sysIndustryCode']").val()) ==1){
//				layer.confirm("行业类别代码不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("行业类别代码不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
               $("input[name='sysIndustryCode']").focus();
                return false;
	        }
			if(checkNoDataHeader12($("input[name='sysIndustryCode']").val())==0) {
//				layer.confirm("行业类别代码输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("行业类别代码输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysIndustryCode']").focus();
                return false;
	        }
			if($("textarea[name='sysIndustryNotes']").val() != "" && checkChineseNoSpe50($("textarea[name='sysIndustryNotes']").val()) == 0) {
//				layer.confirm("备注输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("备注输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("textarea[name='sysIndustryNotes']").focus();
                return false;
	        }
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/sysClassfy/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						parent.window.location.href = "${request.getContextPath()}/admin/sysClassfy/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
		
	})
		
	</script>	
</html>
