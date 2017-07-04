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
			
			function sub(){
				if($("input[name='chack']").is(":checked")){
					$("#upload").submit();
					
				}else{
					var msg = "操作失败！";
					layer.alert(msg,{
						icon:msg,
						shade:0.3,
						shadeClose:true
					});
				}
				
			}
				//全选反选
		function CheckedRev(){
			var arr = $(':checkbox'); 
			for(var i=1;i<arr.length;i++){ 
			arr[i].checked = ! arr[i].checked; 
			}
		} 
		</script>
		<title>二码确认</title>
	</head>
	<body class="eachInformationSearch">
		<div class="showListBox">
			<div class="marginT10">
			<form  id="upload" method="post"  action="${request.getContextPath()}/admin/codeCombine/hebin.jhtml?">
				<table>
					<caption class="titleFont1 titleFont1Ex">二码合并确认列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50" ><input type ="checkbox" onclick="CheckedRev()"/>全选</td>
							<td width="80">序号</td>
							<td width="100">统一社会信用代码</td>
							<td width="100">组织机构代码</td>
							<td width="100">企业名称</td>
						</tr>
						<#if list?size!=0>
							<#list list as li>
								<tr>
									<td width="50" rowspan="2" ><input type="checkbox"  name="chack" value="${li[0].defaultIndexItemId}~${li[1].defaultIndexItemId}"></td>
									<td width="80"rowspan="2" >${li_index+1}</td>
									<td width="100">${li[0].codeCredit}</td>
									<td width="100">${li[0].codeOrg}</td>
									<td width="100">${li[0].qymc}</td>
									<td></td>
								</tr>
								<tr>
									<td width="100">${li[1].codeCredit}</td>
									<td width="100">${li[1].codeOrg}</td>
									<td width="100">${li[1].qymc}</td>
									<td></td>
								</tr>
							</#list>
							<#else>
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" colspan="5" >暂无需要合并的二码信息</td>
							</tr>
						</#if>
					</tbody>
				</table>
				<div class="showBtnBox">
					<input type="button"  class="cancleBtn closeThisLayer" value="取 消" />
					<input type="button" id="formVer" class="sureBtn" value="确 认" onclick="sub()"/>
				</div>
				
				</form>
		</div>
	</body>
	<script>
	$(function(){
		//	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		//    	parent.layer.close(index); 
				window.location.href="${request.getContextPath()}/admin/codeCombine/list.jhtml";
			});//执行关闭
	});
	$(function(){
		var str="${errList?size}";
		
		if(str!=0){
			layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">错误列表</caption><#list errList as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
		}
	});
	</script>
</html>
