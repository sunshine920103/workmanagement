<!DOCTYPE html>
<html>
	<head>
		<title>XX企业信用评分明细</title>
		<#include "/fragment/common.ftl"/>
	</head>
	<body>
		<div class="showListBox">
			<table cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">${score.index_jbxx_qymc}企业信用评分明细</caption>
				<tbody>
				<tr >
					<td class="textCenter" colspan="2">信用状况<span>${score.credit}</span>分</td>
				</tr>
				<tr>
					<td width="250" class="noBorderL firstTD">统一社会信用代码</td>
					<td width="400" class="secondTD">${(score.shxym??)?string(score.shxym,'0')}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">出资情况</td>
					<td class="secondTD">
						${(score.index_zczb_czqk??)?string(score.index_zczb_czqk,"0")}分
						<#if score.index_zczb_msg??>
							<span class="delFont">
								${score.index_zczb_msg}
							</span>
						</#if>
					</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">信贷质量</td>
					<td class="secondTD">${(score.index_zczb_xdjl??)?string(score.index_zczb_xdjl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">缴税情况</td>
					<td class="secondTD">${(score.jsqk??)?string(score.jsqk,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">专利</td>
					<td class="secondTD">${(score.zl??)?string(score.zl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">社保</td>
					<td class="secondTD">${(score.sb??)?string(score.sb,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">公积金</td>
					<td class="secondTD">${(score.gjj??)?string(score.gjj,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">环保</td>
					<td class="secondTD">${(score.hb??)?string(score.hb,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">法院</td>
					<td class="secondTD">${(score.fy??)?string(score.fy,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">社会评价</td>
					<td class="secondTD">${(score.shpj??)?string(score.shpj,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">公共事业缴费</td>
					<td class="secondTD">${(score.ggsyjf??)?string(score.ggsyjf,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">行政处罚情况</td>
					<td class="secondTD">${(score.xzcfqk??)?string(score.xzcfqk,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">吸纳就业</td>
					<td class="secondTD">${(score.xnjy??)?string(score.xnjy,"0")}分</td>
				</tr>
				<tr  >
					<td class="textCenter" colspan="2">偿债能力<span>${(score.cznl??)?string(score.cznl,"0")}</span>分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">资产负债率</td>
					<td class="secondTD">${(score.zcfzl??)?string(score.zcfzl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">流动比率</td>
					<td class="secondTD">${(score.ldbl??)?string(score.ldbl,"0")}分</td>
				</tr>
				<tr  >
					<td class="textCenter" colspan="2">盈利能力<span>${(score.ylnl??)?string(score.ylnl,"0")}</span>分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">总资产利润率</td>
					<td class="secondTD">${(score.zzclrl??)?string(score.zzclrl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">所有者权益利润率</td>
					<td class="secondTD">${(score.syzqylrl??)?string(score.syzqylrl,"0")}分</td>
				</tr>
				<tr  >
					<td class="textCenter" colspan="2">运营能力<span>${(score.yynl??)?string(score.yynl,"0")}</span>分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">总资产周转率</td>
					<td class="secondTD">${(score.zzczzl??)?string(score.zzczzl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">流动资产周转率</td>
					<td class="secondTD">${(score.ldzczzl??)?string(score.ldzczzl,"0")}分</td>
				</tr>
				<tr >
					<td  class="textCenter" colspan="2">成长能力<span>${(score.chznl??)?string(score.chznl,"0")}</span>分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">营业收入增长率</td>
					<td class="secondTD">${(score.yysrzzl??)?string(score.yysrzzl,"0")}分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">资本保值增值率</td>
					<td class="secondTD">${(score.zbbzzzl??)?string(score.zbbzzzl,"0")}分</td>
				</tr>
				<tr  >
					<td class="textCenter" colspan="2">其他<span>${(score.qt??)?string(score.qt,"0")}</span>分</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">生产经营场所</td>
					<td class="secondTD">${(score.scjycs??)?string(score.scjycs,"0")}分</td>
				</tr>
				</tbody>
			</table>
			<div class="showBtnBox">
				<input class="sureBtn closeThisLayer" type="button" value="关 闭"/>
			</div>
		</div>
	<script>
	$(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
    		parent.layer.close(index); //执行关闭
		});
	})
	</script>
	</body>
</html>
