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
			
			<form id="form" action="${request.getContextPath()}/admin/goverType/save.jhtml" method="post">
			
				<input name="sysGovTypeId" type="hidden" value="${it.sysGovTypeId}"/>
				
				<table cellpadding="0" cellspacing="0">
					<#if it == null>
						<caption class="titleFont1 titleFont1Ex">新增政府类别</caption>
					<#else>
						<caption class="titleFont1 titleFont1Ex">修改政府类别</caption>
					</#if>
					
					<tr>
						<td class="noBorderL firstTD" width="400"><label class="mainOrange" > * </label>政府类别名称</td>
						<td class="secondTD" width="800">
							<input id="sysGovTypeName" name="sysGovTypeName" class="inputSty required" value="${it.sysGovTypeName}" onblur="onblurVal(this,20,20)" title="必填，不能超过20个字"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>政府类别代码</td>
						<td class="secondTD">
							<input id="sysGovTypeCode" name="sysGovTypeCode" class="inputSty required" value="${it.sysGovTypeCode}" onblur="onblurVal(this,22,20)"  title="必填，输入4位数字与字母的组合" />
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">备注</td>
						<td class="secondTD">
							<textarea name="sysGovTypeNotes" class="fontSize12 textareaSty" style="border:1px solid #dadada;" onblur="onblurVal(this,14,50)">${it.sysGovTypeNotes}</textarea>
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
			
			if(checkChineseNoSpe12($("input[name='sysGovTypeName']").val())==1) {
//				layer.confirm("政府类别名称不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府类别名称不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovTypeName']").focus();
                return false;
	        }
			if(checkChineseNoSpe12($("input[name='sysGovTypeName']").val())==0) {
//				layer.confirm("政府类别名称输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府类别名称输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovTypeName']").focus();
                return false;
	        }
			
			if(testGovCode($("input[name='sysGovTypeCode']").val())==1) {
//				layer.confirm("政府类别代码不能为空", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府类别代码不能为空",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovTypeCode']").focus();
                return false;
	        }
			if(testGovCode($("input[name='sysGovTypeCode']").val())==0) {
//				layer.confirm("政府类别代码输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("政府类别代码输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovTypeCode']").focus();
                return false;
	        }
			if($("textarea[name='sysGovTypeNotes']").val() != "" && checkChineseNoSpe50($("textarea[name='sysGovTypeNotes']").val()) == 0) {
//				layer.confirm("备注输入不合法", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				layer.alert("备注输入不合法",{
					icon:2,
					shade:0.3,
					shouldClose:true
				});
                $("input[name='sysGovTypeNotes']").focus();
                return false;
	        }
			
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/goverType/save.jhtml",$("#form").serialize(),function(data){
				layer.close(loading);
				var index = alertInfoFun(data.message, data.flag, function(){
					if(data.flag){
						layer.load();
						parent.window.location.href = "${request.getContextPath()}/admin/goverType/list.jhtml";
					}
					layer.close(index);
				});
			});
		});
		
		//搜索按钮
		$("#searchBtn").click(function(){
			var searchVal = $.trim($("#searchInput").val());
			var searchLoading = layer.load();
			var tabel = $("#searchTable");
			$.post("${request.getContextPath()}/admin/goverType/goverTypeByName.jhtml",{name:searchVal},function(data){
				tabel.html("");
				$.each(data,function(i,v){
					var tr = $("<tr></tr>");
					var td = $("<td id='"+v.sysGovTypeId+"' onclick='selUpstream(this)'><label>"+v.sysGovTypeName+"</label></td>");
					tr.append(td);
					tabel.append(tr);
					$.trim($("#searchInput").val(""));
				});
				layer.close(searchLoading);
			});
		});
		
	})
		
	</script>	
</html>
