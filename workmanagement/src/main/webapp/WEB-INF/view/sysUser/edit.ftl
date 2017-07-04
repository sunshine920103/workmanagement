<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/> 
		<title>用户详情</title>
	</head> 
	<body>
		<#-- 新增框 -->
		<div class="showListBox">  
			<table class="centerTable" cellpadding="0" cellspacing="0"> 
						<caption class="titleFont1 titleFont1Ex">用户详情</caption> 
					<tr>
						<td style="width:35%;" class="noBorderL firstTD"> 登录名</td>
						<td style="width:65%;" class="secondTD">
							${su.username}
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"> 姓名</td>
						<td class="secondTD">
							${su.sys_user_name} 
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">身份证号码</td>
						<td class="secondTD">
							${su.sys_user_card} 
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">手机号</td>
						<td class="secondTD">
							${su.sys_user_contacts} 
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">办公电话</td>
						<td class="secondTD">
							${su.sys_user_phone}  
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"> 角色</td>
						<td class="secondTD">
							${sr.sys_role_name}
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"> 所在机构</td>
						<td class="secondTD">
							${su.sys_user_org_name}
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"> 所在区域</td>
						<td class="secondTD">
							${sa.sysAreaName}
						</td>
					</tr>
					
				</table>
				
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="关  闭" /> 
				</div>
		</div>
	</body>
	<script>
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		
	</script>
</html>
