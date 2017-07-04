<!DOCTYPE html>
<html lang="en">
	<head>
		<meta charset="UTF-8">
		<#include "/fragment/common.ftl"/>
		<script>
			//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			}
			
			function out(ss) {
				var str = ss;
				if(str.length != 5) {
					return false;
				}
				if(str.indexOf(":") != -1) {
					var s = str.split(":");
					if(s.length != 2) {
						return false;
					}
					var n = Number(s[0]);
					var n1 = Number(s[1]);
					if(!isNaN(n) && !isNaN(n1)) {
						if(!(s[0] < 24 && s[0] > 0)) {
							return false;
						}
						if(!(s[1] < 60 && s[1] >= 0)) {
							return false;
						}
						return true;
					}
				}
			}
var index = parent.layer.getFrameIndex(window.name); 
			function sub(){
				var time = $("#o").val();
				var time1 = $("#o1").val();
				var day = $("#day").val();
				var number=$("#number").val();
//				var num=$("#num").val();
				var code=$("#code").val();
				var pwd=$("#sysSetQwdRule").val();
				var authFile=$("#authFile").val();
				var operateAuthFile=$("#operateAuthFile").val();
				var creditReportAuthFile=$("#creditReportAuthFile").val();
				var monthLimit=$("#monthLimit").val();
				var multipleLimit=$("#multipleLimit").val();
				var reg = /^(([1-9]+)|([0-9]+\.[0-9]{1}))$/;
				 
				if(day=="" ){
					layer.alert("超期登录限制不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false
				}
				if(number=="" ){
					layer.alert("登录错误次数限制不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false
				}
				if(monthLimit=="" ){
					layer.alert("查询次数限制-月数不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false
				}
				if(multipleLimit=="" ){
					layer.alert("查询次数限制-倍数不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false
				}
				
				
				if(!reg.test(multipleLimit)){
					layer.alert("查询次数限制-倍数不正确",{icon:2,shade:0.3,shouldClose:true});
					return false
				}
				
				if(out(time) && out(time1)&&!isNaN(Number(day))>=0&&!isNaN(Number(number))>=0) {
						document.getElementById("err").innerHTML = "时间格式为小时：分钟 ，例如09：00";
						document.getElementById("err1").innerHTML = "输入为正整数";
						document.getElementById("err2").innerHTML = "输入为正整数";

						//window.location.href = "${request.getContextPath()}/admin/sysOtherManage/updateSys.jhtml?sysSetId=" + ${stm.sysSetId} + "&sysSetStime=" + time + "&sysSetEtime=" + time1 + "&sysSetLoginOverdue=" + day + "&sysSetLoginNum=" + number+ "&sysSetQueryLimitSwitch=" + num+ "&sysSetOrgSwitch=" + code+ "&sysSetQwdRule=" + pwd;
						//parent.location.reload();
						//parent.layer.close(index); //执行关闭
						$.ajax({
			    			url:'${request.getContextPath()}/admin/sysOtherManage/updateSys.jhtml',
			    			data:{
			    				"sysSetId":${stm.sysSetId},
			    				"sysSetStime":time,
			    				"sysSetEtime":time1,
			    				"sysSetLoginOverdue":day,
			    				"sysSetLoginNum":number,
//			    				"sysSetQueryLimitSwitch":num,
			    				"sysSetOrgSwitch":code,
			    				"sysSetQwdRule":pwd,
			    				"authFileSwitch":authFile,
			    				"operateAuthFileSwitch":operateAuthFile,
			    				"creditReportAuthFileSwitch":creditReportAuthFile,
			    				"monthLimit":monthLimit,
			    				"multipleLimit":multipleLimit
			    			},
			    			success:function(map){
			    				if(map.msg=="操作成功"){
									layer.alert(map.msg,{icon:1,shade:0.3,shouldClose:true},function(){
										parent.location.reload();
										parent.layer.close(index); //执行关闭
										window.location.href = "${request.getContextPath()}/admin/sysOtherManage/index.jhtml";
									});
								}else{
									layer.alert(map.msg,{icon:2,shade:0.3,shouldClose:true});
								}
			    			}
			    		});
				}else{
					if(out(time) || out(time1)){
						document.getElementById("err").innerHTML = "时间日期错误！";
						document.getElementById("err").style.color = "red";
						return;
					}
				}
				
			}

			$(function() {

				//获取当前窗体索引

				var loading;
				//回显
				var msg = "${msg}"; 
				if(msg!=""){
					layer.alert(msg,{icon:2,shade:0.3,shouldClose:true});
					$('.layui-layer-shade').height($(window).height());
					layer.close(loading);
				}

				$('.closeThisLayer').on('click', function() {
					parent.layer.close(index); //执行关闭
				});
			});
			
//			//前三月信息查询平均次数限制
//			function confirmNum(clear){
//				$("#num").val(clear);
//			}
			//组织结构代码
			function confirmCode(clear){
				$("#code").val(clear);
			}
			//手工修改-授权文件
			function confirmAuthFile(clear){
				$("#authFile").val(clear);
			}
			//异议处理-授权文件
			function operateAuthFile(clear){
				$("#operateAuthFile").val(clear);
			}
			//信用报告-授权文件
			function creditReportAuthFile(clear){
				$("#creditReportAuthFile").val(clear);
			}
		</script>
	</head>

	<body>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">修改用户管理限制</caption>
				<tbody>
					<tr>
						<td width="200" class="noBorderL firstTD">工作日系统使用时间</td>
						<td width="500" class="secondTD">
							<input value="${stm.sysSetStime}" id="o" class="inputSty textCenter" style="width: 70px;" name="time"  />~
							<input value="${stm.sysSetEtime}" id="o1" class="inputSty textCenter" style="width: 70px;" name="time1" />
							<label id="err" class="fontSize12 warmFont">时间格式为小时：分钟 ，例如09：00</label>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">超期登录限制</td>

						<td class="secondTD">
							<input type="text" class="dayChoice inputSty" value="${stm.sysSetLoginOverdue}" id="day" name="day" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"> 天
							<label id="err1"  style="display: none;">输入为正整数</label>
						</td>
					</tr>
					<tr>
                        <td class="noBorderL firstTD">登录错误次数限制</td>
                        <td class="secondTD">
                            <input type="text" class="dayChoice inputSty" value="${stm.sysSetLoginNum}" id="number" name="number" onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"> 次
							<label id="err2"  style="display: none;">输入为正整数</label>             
                       </td>
                    </tr>
                    <!--<tr>
						<td class="noBorderL firstTD">前三月信息查询平均次数限制</td>
						<td class="secondTD">
							<input type="hidden" id="num" value="${stm.sysSetQueryLimitSwitch}"/>
							<#if stm.sysSetQueryLimitSwitch==0>
								<input class="verticalMiddle marginR10" type="radio" name="numLimit" id="num_on" value="1" onclick="confirmNum(1)"/><span class="fontSize12">开启</span>
								<input class="verticalMiddle marginR10 marginL20" type="radio" name="numLimit" id="num_off" checked value="0" onclick="confirmNum(0)"/><span class="fontSize12">关闭</span>
							<#else>
								<input class="verticalMiddle marginR10" type="radio" name="numLimit" id="num_on" checked value="1" onclick="confirmNum(1)"/><span class="fontSize12">开启</span>
								<input class="verticalMiddle marginR10 marginL20" type="radio" name="numLimit" id="num_off" value="0" onclick="confirmNum(0)"/><span class="fontSize12">关闭</span>
							</#if>
						</td>
					</tr>-->
                    <tr>
            			<td class="noBorderL firstTD">组织机构代码</td>
           				<td class="secondTD">
           					<input type="hidden" id="code" value="${stm.sysSetOrgSwitch}"/>
             		    	<#if stm.sysSetOrgSwitch==0>
             		    		<input class="verticalMiddle marginR10" type="radio" name="code" id="code_on" value="1" onclick="confirmCode(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="code"  checked id="code_off" value="0" onclick="confirmCode(0)"/><span class="fontSize12">关闭</span>
             		    	<#else>
             		    		<input class="verticalMiddle marginR10" type="radio" name="code"  checked id="code_on" value="1" onclick="confirmCode(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="code" id="code_off" value="0" onclick="confirmCode(0)"/><span class="fontSize12">关闭</span>
             		    	</#if>
           				 </td>
       				</tr>

                    <tr>
                        <td class="noBorderL firstTD">密码规则</td>
			            <td class="secondTD">
			                <p class="fontSize12">
			                <select id="sysSetQwdRule" name="sysSetQwdRule" class="inputSty textCenter">
			                <#if stm.sysSetQwdRule==0>
			                	<option value="0" selected >数字</option>
			                	<option value="1">字母</option>
			                	<option value="2">数字和字母</option>
			                	<option value="3">数字、字母和特殊字符</option>
			                <#elseif stm.sysSetQwdRule==1>
			                	<option value="0">数字</option>
			                	<option value="1" selected >字母</option>
			                	<option value="2">数字和字母</option>
			                	<option value="3">数字、字母和特殊字符</option>
			                <#elseif stm.sysSetQwdRule==2>
			                	<option value="0">数字</option>
			                	<option value="1">字母</option>
			                	<option value="2" selected >数字和字母</option>
			                	<option value="3">数字、字母和特殊字符</option>
			                <#else>
			                	<option value="0">数字</option>
			                	<option value="1">字母</option>
			                	<option value="2">数字和字母</option>
			                	<option value="3" selected >数字、字母和特殊字符</option>
			                </#if>
			                </select>
			                </p>
			            </td>
                    </tr>
                    <tr>
            			<td class="noBorderL firstTD">手工修改-授权文件</td>
           				<td class="secondTD">
           					<input type="hidden" id="authFile" value="${stm.authFileSwitch}"/>
             		    	<#if stm.authFileSwitch==0>
             		    		<input class="verticalMiddle marginR10" type="radio" name="authFile" id="authFile_on" value="1" onclick="confirmAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="authFile"  checked id="authFile_off" value="0" onclick="confirmAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	<#else>
             		    		<input class="verticalMiddle marginR10" type="radio" name="authFile"  checked id="authFile_on" value="1" onclick="confirmAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="authFile" id="authFile_off" value="0" onclick="confirmAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	</#if>
           				 </td>
       				</tr>
       				<tr>
            			<td class="noBorderL firstTD">异议处理-授权文件</td>
           				<td class="secondTD">
           					<input type="hidden" id="operateAuthFile" value="${stm.operateAuthFileSwitch}"/>
             		    	<#if stm.operateAuthFileSwitch==0>
             		    		<input class="verticalMiddle marginR10" type="radio" name="operateAuthFile" id="operate_on" value="1" onclick="operateAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="operateAuthFile"  checked id="operate_off" value="0" onclick="operateAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	<#else>
             		    		<input class="verticalMiddle marginR10" type="radio" name="operateAuthFile"  checked id="operate_on" value="1" onclick="operateAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="operateAuthFile" id="operate_off" value="0" onclick="operateAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	</#if>
           				 </td>
       				</tr>
       				<tr>
            			<td class="noBorderL firstTD">信用报告-授权文件</td>
           				<td class="secondTD">
           					<input type="hidden" id="creditReportAuthFile" value="${stm.creditReportAuthFileSwitch}"/>
             		    	<#if stm.creditReportAuthFileSwitch==0>
             		    		<input class="verticalMiddle marginR10" type="radio" name="creditReportAuthFile" id="creditReport_on" value="1" onclick="creditReportAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="creditReportAuthFile"  checked id="creditReport_off" value="0" onclick="creditReportAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	<#else>
             		    		<input class="verticalMiddle marginR10" type="radio" name="creditReportAuthFile"  checked id="creditReport_on" value="1" onclick="creditReportAuthFile(1)"/><span class="fontSize12">开启</span>
                				<input class="verticalMiddle marginR10 marginL20" type="radio" name="creditReportAuthFile" id="creditReport_off" value="0" onclick="creditReportAuthFile(0)"/><span class="fontSize12">关闭</span>
             		    	</#if>
           				 </td>
       				</tr>
       				<tr>
                        <td class="noBorderL firstTD">查询次数限制-月数</td>
                        <td class="secondTD">
                            <input type="text" class="dayChoice inputSty" value="${stm.monthLimit}" id="monthLimit" name="monthLimit" <#if roleId!=1>disabled</#if> onkeyup="this.value=this.value.replace(/\D/g,'')" onafterpaste="this.value=this.value.replace(/\D/g,'')"> 个月
							<label id="err2"  style="display: none;">输入为正整数</label>             
                       </td>
                    </tr>
                    <tr>
                        <td class="noBorderL firstTD">查询次数限制-倍数</td>
                        <td class="secondTD">
                            <input type="text" class="dayChoice inputSty" value="${stm.multipleLimit}" id="multipleLimit" name="multipleLimit"> 倍
							<label id="err2"  style="display: none;">输入为正整数</label>             
                       </td>
                    </tr>
				</tbody>
			</table>

			<div class="showBtnBox">
				<button type="button" class="cancleBtn closeThisLayer">取消</button>
				<button type="button" class="sureBtn" onclick="sub()">确定</button>
			</div>
		</div>
	</body>

</html>