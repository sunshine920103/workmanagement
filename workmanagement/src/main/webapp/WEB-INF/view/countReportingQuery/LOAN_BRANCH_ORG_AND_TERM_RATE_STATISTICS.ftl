<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>表4</title>
		<link rel="stylesheet" href="${request.getContextPath()}/assets/css/tableCss.css" />
		<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
		<script src="${request.getContextPath()}/assets/js/lay/layer/layer.js"></script>
		<script type="text/javascript" >
    	//回显
		var msg = "${msg}";
		if(msg!==""){
			alertInfo(msg,false);
		}
    	$(function(){
    		//关闭父窗口的加载弹窗
    		parent.window.closeLoging();
    		$("#title").text(parent.window.getSelAreaName()+"贷款分机构分期限利率情况统计表");
    	});
		</script>
	</head>
	 
	<body>
		<div class="headBox" style="width:800px">
			<p class="title0" id="title" >**市贷款分机构分期限利率情况统计表</p>
			<p class="title1">
				<span class="spanCenter">${fm_begin} - ${fm_end} </span>
				<span class="spanRight">单位：%</span>
			</p>
		</div>
		<table class="mainTable" cellpadding="0" cellspacing="0" style="width:800px">
			<tr>
				<td rowspan="2" style="width: 150px;">
					<span>金融机构</span>
				</td>
				<td colspan="2">6个月（含）以内</td>
				<td colspan="2">6个月-1年（含）</td>
				<td colspan="2">1-3年（含）</td>
				<td colspan="2">3-5年（含）</td>
				<td colspan="2">5年以上</td>
				<td colspan="2">合计</td>
			</tr>
			<tr>
				<td>人民币</td>
				<td>外币</td>
				<td>人民币</td>
				<td>外币</td>
				<td>人民币</td>
				<td>外币</td>
				<td>人民币</td>
				<td>外币</td>
				<td>人民币</td>
				<td>外币</td>
				<td>人民币</td>
				<td>外币</td>
			</tr>
			<#list zh as re>
			<tr>
				<td class="fontBold">合计</td>
				<td>${re.rmb_hy_6?string("0.##")}</td>
				<td>${re.wb_hy_6?string("0.##")}</td>
				<td>${re.rmb_hy_1?string("0.##")}</td>
				<td>${re.wb_hy_1?string("0.##")}</td>
				<td>${re.rmb_hy_3?string("0.##")}</td>
				<td>${re.wb_hy_3?string("0.##")}</td>
				<td>${re.rmb_hy_5?string("0.##")}</td>
				<td>${re.wb_hy_5?string("0.##")}</td>
				<td>${re.rmb_hy_d5?string("0.##")}</td>
				<td>${re.wb_hy_d5?string("0.##")}</td>
				<td>${(re.rmb_hy_6+re.rmb_hy_1+re.rmb_hy_3+re.rmb_hy_5+re.rmb_hy_d5)?string("0.##")}</td>
				<td>${(re.wb_hy_6+re.wb_hy_1+re.wb_hy_3+re.wb_hy_5+re.wb_hy_d5)?string("0.##")}</td>
			</tr>
			</#list>
			<#list res as re>
			
			<tr>	
				<td><b>${re.onename}</b></td>
				<#list yhzh as yh>
				<#if re.code == yh.cod>
				<td>${yh.rmb_hy_6?string("0.##")}</td>
				<td>${yh.wb_hy_6?string("0.##")}</td>
				<td>${yh.rmb_hy_1?string("0.##")}</td>
				<td>${yh.wb_hy_1?string("0.##")}</td>
				<td>${yh.rmb_hy_3?string("0.##")}</td>
				<td>${yh.wb_hy_3?string("0.##")}</td>
				<td>${yh.rmb_hy_5?string("0.##")}</td>
				<td>${yh.wb_hy_5?string("0.##")}</td>
				<td>${yh.rmb_hy_d5?string("0.##")}</td>
				<td>${yh.wb_hy_d5?string("0.##")}</td>
				<td>${(yh.rmb_hy_6+yh.rmb_hy_1+yh.rmb_hy_3+yh.rmb_hy_5+yh.rmb_hy_d5)?string("0.##")}</td>
				<td>${(yh.wb_hy_6+yh.wb_hy_1+yh.wb_hy_3+yh.wb_hy_5+yh.wb_hy_d5)?string("0.##")}</td>
				</#if>
				</#list>
			</tr>
			<#list results as map>
			<#if map.code1 == re.code>
			<tr>	
				<td>${map.twoname}</td>
				<td>${map.rmbll6?string("0.##")}</td>
				<td>${map.wbll6?string("0.##")}</td>
				<td>${map.rmbll1N?string("0.##")}</td>
				<td>${map.wbll1N?string("0.##")}</td>
				<td>${map.rmbll3?string("0.##")}</td>
				<td>${map.wbll3N?string("0.##")}</td>
				<td>${map.rmbll5N?string("0.##")}</td>
				<td>${map.wbll5N?string("0.##")}</td>
				<td>${map.rmblhl?string("0.##")}</td>
				<td>${map.wbll?string("0.##")}</td>
				<td>${(map.rmbll6+map.rmbll1N+map.rmbll3+map.rmbll5N+map.rmblhl)?string("0.##")}</td>
				<td>${(map.wbll6+map.wbll1N+map.wbll3N+map.wbll5N+map.wbll)?string("0.##")}</td>
			</tr>
			</#if>
			</#list>
			</#list>
		</table>
	<div class="textCenter">
			<input type="button" name="" class="closeBtn closeThisLayer" value="关 闭" />
		</div>
	</body>
	<script type="text/javascript">
		$(function(){
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});
		})
	</script>
</html>
