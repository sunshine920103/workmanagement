<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN"
"http://www.w3.org/TR/html4/strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" lang="en">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>表8</title>
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
    		$("#title").text(parent.window.getSelAreaName()+"重点关注企业贷款分机构利率情况统计表");
    	});
</script>
	</head>
	<body>
		<div class="headBox">
			<p  id="title"  class="title0">**市重点关注企业贷款分机构利率情况统计表</p>
			<p class="title1">
				<span class="spanCenter">${fm_begin} - ${fm_end}</span>
				<span class="spanRight">单位：%</span>
			</p>
		</div>
		<table class="mainTable" cellpadding="0" cellspacing="0">
		
			<tr>
				<td rowspan="2" style="width: 150px;">
					<span>企业群</span>
					<span>机构</span>
					<img src=""/>
				</td>
			<#list results as re>
				<td colspan="2">${re.name}</td>
			</#list>
		 	</tr>
			<tr>
			<#list results as re>
				<td>人民币</td>
				<td>外币</td>
			</#list>
			</tr>
			<tr>
				<td class="fontBold">合计</td>
				<#list results as re>
				<td>${re.rmb_hj_sy?string("0.##")}</td>
				<td>${re.wb_hj_sy?string("0.##")}</td>
				</#list>
			</tr>
			<#list result as m>
			<#list list as li>
			<tr>
				<td class="fontBold">${m.bank}小计</td>
				<#list results as re>
				<td>${re.rmb_hj?string("0.##")}</td>
				<td>${re.wb_hj?string("0.##")}</td>
				</#list>
			</tr>
			
			<#if m.name==li.name >
			<tr>
				<td>${m.yhlb}</td>
				<#list results as re>
				<td>${re.rmbll6?string("0.##")}</td>
				<td>${re.wbll6?string("0.##")}</td>
				</#list>
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
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});
		})
	</script>
</html>
