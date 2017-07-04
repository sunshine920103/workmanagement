<!DOCTYPE html>
<html>
	<head>
		<title>修改密码</title>
		<#include "/fragment/common.ftl"/>
		<script href="${request.getContextPath()}/assets/js/common.js"></script>
		
		<script>
			function submitFun(){
			    var pwd0 = /^[0-9]{8,16}$/;
			   	var pwd1 = /^[a-zA-Z]{8,16}$/;
			   	var pwd2 = /^(?=.*\d)(?=.*[a-zA-Z])[\da-zA-Z]{8,16}$/; 
			   	var pwd3 = /^(?=.*\d)(?=.*[a-zA-Z])(?=.*[`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、])[\da-zA-Z`~!@#$%^&*()_\-+=<>?:"{}|,.\/;'\\[\]·~！@#￥%……&*（）——\-+={}|《》？：“”【】、；‘’，。、]{8,16}$/;
			   	
				
				var oldPwd = $.trim($("#oldPwd").val());
				var newPwd = $.trim($("#newPwd").val());
				var rePwd = $.trim($("#rePwd").val());
				var pass ="${pass}"; 
				if(oldPwd===""){
//					alertInfo("原密码不能为空",false);
					layer.alert("原密码不能为空", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(newPwd===""){
//					alertInfo("新密码不能为空",false);
					layer.alert("新密码不能为空", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(oldPwd==newPwd){
//					alertInfo("原密码和新密码一致",false);
					layer.alert("原密码和新密码一致", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(newPwd!=rePwd){
//					alertInfo("新密码和确认密码不一致",false);	
					layer.alert("新密码和确认密码不一致", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(pass=="0"&& !pwd0.test(newPwd)){
//					 alertInfo("请输入8~16位数字",false);
					layer.alert("请输入8~16位数字", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;	
				}else if(pass=="1"&& !pwd1.test(newPwd)){
//					 alertInfo("请输入8~16位字母",false);
					layer.alert("请输入8~16位数字", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(pass=="2"&& !pwd2.test(newPwd)){
//					 alertInfo("请输入8~16位字母和数字的组合",false);
					layer.alert("请输入8~16位字母和数字的组合", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else if(pass=="3"&& !pwd3.test(newPwd)){
//					 alertInfo("请输入8~16位字母和数字的组合",false);
					layer.alert("请输入8~16位必须包含有字母数字特殊符的组合", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
					return false;
				}else{ 
					var loadIndex = layer.load();
					$.post("${request.getContextPath()}/admin/sysPwd/modify_pwd.jhtml",{_:Math.random(),oldPwd:oldPwd,newPwd:newPwd,rePwd:rePwd},function(data){
						layer.close(loadIndex);
						var alertIndex = alertInfoFun(data.message,data.flag,function(){
							if(data.flag){
								window.location.href = "${request.getContextPath()}/logout.jhtml";
							}
							layer.close(alertIndex);
						});
					});
				}
			}
		</script>
	</head>
	<body>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">修改密码</caption>
				<tbody>
					<tr>
						<td style="width:40%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>原密码</td>
						<td style="width:60%;" class="secondTD">
							<input id="oldPwd" type="password" class="inputSty" onpaste="return false" oncontextmenu="return false"   
  
oncopy="return false" oncut="return false"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>新密码</td>
						<td class="secondTD">
							<input id="newPwd" type="password" class="inputSty" onpaste="return false" oncontextmenu="return false"   
  
oncopy="return false" oncut="return false"/>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>确认密码</td>
						<td class="secondTD">
							<input id="rePwd" type="password" class="inputSty" onpaste="return false" oncontextmenu="return false"   
  
oncopy="return false" oncut="return false"/>
						</td>
					</tr>
				</tbody>
			</table>
			<div class="showBtnBox">
				<input onclick="submitFun()" class="sureBtn"  type="button" value="确 定"/>
			</div>
		</div>
	</body>
</html>

