<!DOCTYPE html>
<html lang="en">
<head>	
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>表8</title>
		<link rel="stylesheet" href="${request.getContextPath()}/assets/css/tableCss.css" />
		<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
		<script src="${request.getContextPath()}/assets/js/lay/layer/layer.js"></script>
    <#include "/fragment/common.ftl"/>
    <style>
        body{
            font-family: "微软雅黑";
            padding: 20px;
            margin: 0;
            font-size: 14px;
        }
        p,img,ul,li{
            margin: 0;
            padding: 0;
        }
        .taskList{
            padding: 0 3px 10px 3px;
        }
        .conditionShow span{
            width: 70%;
            float: left;
       		text-align: center;
       		display:inline;
			overflow: hidden;
        }
        .conditionShow p{
            width: 20%;
            text-align: right;
            float: right;
            padding-right: 10px;
            display:inline;
			overflow: hidden;
        }
        table{
            width: 100%;
            border-top: 1px solid #dadada;
            border-left: 1px solid #dadada;
            font-size: 12px;
        }
        th{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            text-align: left;
            font-size: 12px;
        }
        td{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            font-size: 12px;
        }
        .titleFont1 {
            padding-left: 10px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            text-align: left;
            border: 1px solid #dadada;
            border-bottom: none;
        }
        .tableTop{
            padding: 0;
            position: relative;
        }
        .tableTopRight{ 
            position: absolute;
            top: 10px;
            right:10px;
        }
        .tableTopLeft{
            position: absolute;
            bottom: 10px;
            left:10px;
        }
    </style>
    
    
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
    		$("#title").text(parent.window.getSelAreaName()+"重点关注企业本外币贷款时序统计表");
    	
    	});
    </script>
</head>
<body>
	<div class="headBox">
			<p class="title0" id="title">**市重点关注企业本外币贷款时序统计表</p>
			<p class="title1">
				<span class="spanCenter">${fm_begin}      </span>
				<span class="spanRight">单位：户、万元</span>
			</p>
		</div>
		
		<table class="mainTable" cellpadding="0" cellspacing="0">
			<tr>
				<td rowspan="2" style="width: 150px;">
					<span>季度</span>
					<span>企业群</span>
					<img src=""/>
				</td>
				<#list 1..4 as t>
					<#if t<=len>
					
						<td colspan="2">${t}季度末</td>
					</#if>
				</#list>
			</tr>
			<tr>
				<#list 1..4 as t>
					<#if t<=len>
					
				<td>户数</td>
				<td>余额</td>
					</#if>
				</#list>
			</tr>
			<#list res as re>
				<tr>
					<td>${re.name}</td>
					<#list 1..4 as t>
						<#if t<=len>
							<#if re[t+'']?size gt 0>
								<#list re[t+''] as r>
									<td>${r.hs?size}</td>
									<td>${(r.je*0.0001)?string("0.00")}</td>
								</#list>
								<#else>
									<td>0</td>
									<td>0</td>
								</#if>
						</#if>
					</#list>
				</tr>
			</#list>
			
		</table>
	<div class="textCenter">
			<input type="button" name="" class="closeBtn closeThisLayer" value="关 闭" />
		</div>
</body>
</html>