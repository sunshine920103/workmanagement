<!DOCTYPE html>
<html>
	<head>
		
		<#include "/fragment/common.ftl"/>
		<title>查看管理日志</title>
	</head>
	<body>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">管理日志详情</caption>
				<tbody>
				<#list sysUserLog as sys>
					<#if sys_index == 0>
						<tr>
							<td style="width:40%;" class="noBorderL firstTD">机构名称</td>
							<td style="width:60%;" class="secondTD">${sys.sysManageLogOrgName}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作对象</td>
							<td class="secondTD">${sys.sysManageLogEnterpriseCode}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">登录名称</td>
							<td class="secondTD">${sys.sysManageLogUserName}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作类型</td>
							<#if sys.sysManageLogOperateType == 1>
								<td class="secondTD">增加</td>
								<#elseif sys.sysManageLogOperateType == 2>
								<td class="secondTD">删除</td>
								<#elseif sys.sysManageLogOperateType == 3>
								<td class="secondTD">修改</td>
								<#elseif sys.sysManageLogOperateType == 4>
								<td class="secondTD">查询</td>
								<#elseif sys.sysManageLogOperateType == 5>
								<td class="secondTD">导入</td>
								<#elseif sys.sysManageLogOperateType == 6>
								<td class="secondTD">导出</td>
								<#elseif sys.sysManageLogOperateType == 7>
								<td class="secondTD">打印</td>
							</#if>
						</tr>
						<tr>
							<td class="noBorderL firstTD">用户角色</td>
							<td class="secondTD"><a href="">${sys.sysManageLogRoleName}</a></td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作菜单</td>
							<td class="secondTD">${sys.sysManageLogMenuName}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作的指标大类</td>
							<td class="secondTD">${sys.indexName}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作的指标项</td>
							<td class="secondTD">${sys.indexItemName}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">原值</td>
							<td class="secondTD">${sys.sysManageLogOldValue}</td>
						</tr>
						<tr>
							<td class="cnoBorderL firstTD">修改值</td>
							<td class="secondTD">${sys.sysManageLogNewValue}</td>
						</tr>
						<tr>
							<td class="cnoBorderL firstTD">数据条数</td>
							<td class="secondTD">${sys.sysManageLogCount}</td>
						</tr>
						<tr>
							<td class="cnoBorderL firstTD">搜索条件</td>
							<td class="secondTD">${sys.sysManageLogQueryUserCondition}</td>
						</tr>
						<tr>
							<td class="cnoBorderL firstTD">查看搜索结果</td>
							<td class="secondTD">
								<#if sys.sysManageLogUrl??>
                                    <a class="changeFont fontSize12"
								   href="${request.getContextPath()}/admin/sysManageLog/forward.jhtml?sysId=${sys.sysManageLogId}">点击查看</a>
									<#else >
                                        <span class="changeFont fontSize12">暂无</span>
								</#if>
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作结果</td>
							<td class="secondTD">
							<#if sys.sysManageLogResult == true>
								 成功
							<#else>
								 失败
							</#if>
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">操作IP</td>
							<td class="secondTD">
								${sys.sysManageLogIp}
							</td>
						</tr>
						 <tr>
			                <td class="cnoBorderL firstTD">导入导出文件</td>
			                <td class="secondTD">
			                    <#if sys.sysManageLogFile??>
			                        <a class="changeFont fontSize12"
			                           href="${request.getContextPath()}/admin/sysManageLog/downFile.jhtml?type=1&url=${sys.sysManageLogFile}">点击查看</a>
			                    <#else >
			                        <span class="changeFont fontSize12">暂无</span>
			                    </#if>
			                </td>
			            </tr>
						<tr>
							<td class="noBorderL firstTD">授权文件</td>
							<td class="secondTD">
							<#if sys.sysManageLogAuthFile!="">
								<a class="changeFont fontSize12" href="${request.getContextPath()}/admin/sysManageLog/downFile.jhtml?type=2&url=${sys.sysManageLogFile}">点击查看</a>
							<#else>
								<span class="changeFont fontSize12">暂无</span>
							</#if>
							</td>
						</tr>
						
						<tr>
							<td class="noBorderL firstTD">操作时间</td>
							<td class="secondTD">${(sys.sysManageLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
						</tr>
					</#if>
				</#list>
				</tbody>
			</table>
			<div class="showBtnBox">
				<input class="sureBtn closeThisLayer" type="button" value="关闭"/>
			</div>
		</div>
	</body>
	<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
    		parent.layer.close(index); //执行关闭
		});
	})
	</script>
</html>
