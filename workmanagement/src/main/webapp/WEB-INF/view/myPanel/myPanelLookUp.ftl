<!DOCTYPE html>
<html>
	<head>
		<title>我的面板的查看</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body>
		<div class="showListBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">贷款逾期记录详情</caption>
					<tbody>
					
					
					<#list a as a>
						<tr>
							<td width="300" class="noBorderL firstTD">贷款拮据号</td>
							<td width="400" class="secondTD">${a.INDEX_YHDK_DKJJH}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">贷款合同号</td>
							<td class="secondTD">${a.INDEX_YHDK_DKHTH}</td>
						</tr>
					<tr>
							<td class="noBorderL firstTD">贷款企业组织机构代码/统一社会信用代码</td>
							<td class="secondTD">${a.CODE_ORG}/${a.CODE_CREDIT}</td>
						</tr>
					
						<tr>
							<td class="noBorderL firstTD">贷款企业名称</td>
							<td class="secondTD">${a.INDEX_JBXX_QYMC}</td>
						</tr>
						
						<tr>
							<td class="noBorderL firstTD">贷款业务发生地金融机构</td>
							<td class="secondTD">${a.INDEX_YHDK_DKYWFSDJRJG}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">合同有效状态</td>
							<td class="secondTD">${a.INDEX_YHDK_HTYXZT}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">合同利率（%）</td>
							<td class="secondTD">${a.INDEX_YHDK_HTLL}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">合同金额（万元）</td>
							<td class="secondTD">${a.INDEX_YHDK_HTJE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">合同金额币种</td>
							<td class="secondTD">${a.INDEX_YHDK_HTJEBZ}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">贷款日期</td>
							<td class="secondTD">
							${(a.INDEX_YHDK_DKRQ?string("yyyy-MM-dd HH:mm:ss"))!}
							
							</td>
							
						</tr>
						<tr>
							<td class="noBorderL firstTD">到期日</td>
							<td class="secondTD">
							${(a.INDEX_YHDK_DQR?string("yyyy-MM-dd HH:mm:ss"))!}
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">贷款余额（万元）</td>
							<td class="secondTD">${a.INDEX_YHDK_DKYE}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">贷款余额币种</td>
							<td class="secondTD">${a.INDEX_YHDK_DKYEBZ}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">担保公司</td>
							<td class="secondTD">${a.INDEX_YHDK_DBGS}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">担保方式</td>
							<td class="secondTD">${a.INDEX_YHDK_DBFS}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">五级分类</td>
							<td class="secondTD">${a.INDEX_YHDK_WJFL}</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">展期</td>
							<td class="secondTD">
							${(a.INDEX_YHDK_ZQ?string("yyyy-MM-dd HH:mm:ss"))!}
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">展期日期</td>
							<td class="secondTD">
							${(a.INDEX_YHDK_ZQRQ?string("yyyy-MM-dd HH:mm:ss"))!}
							</td>
						</tr>
						<tr>
							<td class="noBorderL firstTD">欠息金额（万元）</td>
							<td class="secondTD">${a.INDEX_YHDK_QXJE}</td>
						</tr>
						</#list>
					</tbody>
				</table>
				
				
				
				
				<div class="showBtnBox">
					<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
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
