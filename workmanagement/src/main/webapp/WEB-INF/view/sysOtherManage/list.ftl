<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <meta charset="UTF-8">
    <title>其他管理</title>
    <script type="text/javascript">
		$(function(){
			var loading;
			//回显
			var msg = "${msg}"; 
			if(msg!=""){
				layer.alert(msg,{icon:2,shade:0.3,shouldClose:true});
				$('.layui-layer-shade').height($(window).height());
				layer.close(loading);
			}
		});
		
		//IE下 背景全屏
	    window.onresize = function(){
			$('.layui-layer-shade').height($(window).height());
		}
    </script>
</head>
<body>
<#--<div class="eachInformationSearch">
	<div class="queryInputBox verticalMiddle">
		<span class="warmFont inlineBlock marginL20 fontSize12">用户登录限制</span>
    	<input class="sureBtn" type="button" value="修 改" onclick="setLayer('修改登录限制','${request.getContextPath()}/admin/sysOtherManage/update.jhtml?id=${stm.sysSetId}')" />
	</div>
</div>-->
<div class="showListBox" style="margin-top: 200px;" >
    <#--<table cellpadding="0" cellspacing="0" style="margin-left:0px ;">-->
        <#--<tbody>
        <tr>
            <td width="300" class="noBorderL firstTD">工作日系统使用时间</td>
            <td width="500" class="secondTD">
                <p class="fontSize12">${stm.sysSetStime}～${stm.sysSetEtime}</p>
                <span class="fontSize12 warmFont">时间格式为小时：分钟 ，例如09：00</span>
                <label class="fontSize12 warmFont">(不在该时段内禁止登录)</label>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">超期登录限制</td>
            <td class="secondTD">
                <p class="fontSize12">${stm.sysSetLoginOverdue}天</p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">登录错误次数限制</td>
            <td class="secondTD">
                <p class="fontSize12">${stm.sysSetLoginNum}次</p>
            </td>
        </tr>
        <!--<tr>
            <td class="noBorderL firstTD">前三月信息查询平均次数限制</td>
            <td class="secondTD">
                <p class="fontSize12">
                <#if stm.sysSetQueryLimitSwitch==0>
                	<label for="search_times_off"><input type="radio" name="search_times" id="search_times_off" checked disabled />关闭</label>
                <#else>
                	<label for="search_times_on"><input type="radio" name="search_times" id="search_times_on" checked disabled />开启</label>
                </#if>
                </p>
            </td>
        </tr>&ndash;&gt;
        <tr>
            <td class="noBorderL firstTD">组织机构代码</td>
            <td class="secondTD">
                <p class="fontSize12">
                <#if stm.sysSetOrgSwitch==0>
               		<label for="code_off"><input type="radio" name="code" id="code_off" checked disabled />关闭</label>
                <#else>
                	<label for="code_on"><input type="radio" name="code" checked id="code_on" disabled />开启</label>
                </#if>
                </p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">密码规则</td>
            <td class="secondTD">
                <p class="fontSize12">
                <select id="sysSetQwdRule" name="sysSetQwdRule" class="inputSty textCenter" disabled >
                <#if stm.sysSetQwdRule==0>
                	<option value="0" selected >数字</option>
                <#elseif stm.sysSetQwdRule==1>
                	<option value="1" selected >字母</option>
                <#elseif stm.sysSetQwdRule==2>
                	<option value="2" selected >数字和字母</option>
                <#else>
                	<option value="3" selected >数字、字母和特殊字符</option>
                </#if>
                </select>
                </p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">手工修改-授权文件</td>
            <td class="secondTD">
                <p class="fontSize12">
                <#if stm.authFileSwitch==0>
               		<label for="code_off"><input type="radio" name="authFile" id="authFile_off" checked disabled />关闭</label>
                <#else>
                	<label for="code_on"><input type="radio" name="authFile" checked id="authFile_on" disabled />开启</label>
                </#if>
                </p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">异议处理-授权文件</td>
            <td class="secondTD">
                <p class="fontSize12">
                	<#if stm.operateAuthFileSwitch==0>
	               		<label for="code_off"><input type="radio" name="operateAuthFileSwitch" id="operate_off" checked disabled />关闭</label>
	                <#else>
	                	<label for="code_on"><input type="radio" name="operateAuthFileSwitch" checked id="operate_on" disabled />开启</label>
	                </#if>
                </p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">信用报告-授权文件</td>
            <td class="secondTD">
                <p class="fontSize12">
                	<#if stm.creditReportAuthFileSwitch==0>
	               		<label for="code_off"><input type="radio" name="creditReportAuthFileSwitch" id="creditReport_off" checked disabled />关闭</label>
	                <#else>
	                	<label for="code_on"><input type="radio" name="creditReportAuthFileSwitch" checked id="creditReport_on" disabled />开启</label>
	                </#if>
                </p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">查询次数限制-月数</td>
            <td class="secondTD">
                <p class="fontSize12">${stm.monthLimit}个月</p>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD">查询次数限制-倍数</td>
            <td class="secondTD">
                <p class="fontSize12">${stm.multipleLimit}倍</p>
            </td>
        </tr>
        </tbody>-->
            <div class="showBtnBox" style="height: auto;margin: 20px auto;">
                <label style="padding-right: 100px;">手工录入授权文件开关：</label>
                        <input type="radio" style="padding-right: 50px;">打开（强制上传授权文件）</input>
                        <input type="radio" style="padding-right: 50px;">关闭</input>
                        <input  onclick="submitFun()" class="sureBtn" type="button" value="确 定">
                    </div>

</div>
</body>
</html>