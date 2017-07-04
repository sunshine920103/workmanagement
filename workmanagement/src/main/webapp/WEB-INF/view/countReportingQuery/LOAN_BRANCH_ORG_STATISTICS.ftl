<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>表1</title>
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
    
    
    <script type="text/javascript" >
    	//回显
		var msg = "${msg}";
		if(msg!==""){
			alertInfo(msg,false);
		}
    	$(function(){
    		//关闭父窗口的加载弹窗
    		parent.window.closeLoging();
    		
    		$("#title").text(parent.window.getSelAreaName()+"贷款分机构情况统计表");
    	});
    </script>
</head>
<body>
<div class="headBox">
			<p class="title0" id="title">**市贷款分机构情况统计表</p>
			<p class="title1">
				<span class="spanCenter">${fm_begin}</span>
				<span class="spanRight">单位：万元/万美元</span>
			</p>
		</div>
		
<!--贷款分机构情况统计表-->
	    <table cellpadding="0" cellspacing="0">
	       
	        <thead>
		        <tr>
		            <th rowspan="2" class="tableTop" style="width: 130px;">
		                <img src="${request.getContextPath()}/assets/images/table-line.png" alt="" style="width: 130px;">
		                <p class="tableTopRight">币种</p>
		                <p class="tableTopLeft">机构</p>
		            </th>
		            <th class="txc" colspan="2">人民币</th>
		            <th class="txc" colspan="2">外币</th>
		            <th class="txc" colspan="2"> 本外币合计</th>
		        </tr>
		        <tr>
		            <th class="txc">有贷户数</th>
		            <th class="txc">贷款余额</th>
		            <th class="txc">有贷户数</th>
		            <th class="txc">贷款余额</th>
		            <th class="txc">有贷户数</th>
		            <th class="txc">贷款余额</th>
		        </tr>
	        </thead>
	        
	        <tbody>
	        <#list res as re>
	        	<#if re.byd?size!=0>
		        <tr>
		            <td><b>${re.name}</b></td>
		            <td class="txc">${re.ryd?size}</td>
		            <td class="txc">
						<#if re.rye !=0>
		            		${(re.rye/10000)?string("0.00")}
		            	<#else>
		            		${re.rye}
		            	</#if>
					</td>
		            <td class="txc">${re.wyd?size}</td>
		            <td class="txc">
						<#if re.wye !=0>
		            		${(re.wye/10000)?string("0.00")}
		            	<#else>
		            		${re.wye}
		            	</#if>
					</td>
		            <td class="txc">${re.byd?size}</td>
		            <td class="txc">
		            	<#if re.bye !=0>
		            		${(re.bye/10000)?string("0.00")}
		            	<#else>
		            		${re.bye}
		            	</#if>
		            </td>
		        </tr>
		        <#list result as reslts>
		        	
			        <#if reslts.code == re.code>
			        	 <tr>
				           <td>${reslts.name}</td>
		            <td class="txc">${reslts.ryd?size}</td>
		            <td class="txc">
						<#if reslts.rye !=0>
		            		${(reslts.rye/10000)?string("0.00")}
		            	<#else>
		            		${reslts.rye}
		            	</#if>
					</td>
		            <td class="txc">${reslts.wyd?size}</td>
		            <td class="txc">
						<#if reslts.wye !=0>
		            		${(reslts.wye/10000)?string("0.00")}
		            	<#else>
		            		${reslts.wye}
		            	</#if>
					</td>
		            <td class="txc">${reslts.byd?size}</td>
		            <td class="txc">
		            	<#if reslts.bye !=0>
		            		${(reslts.bye/10000)?string("#.00")}
		            	<#else>
		            		${reslts.bye}
		            	</#if>
		            </td>
				        </tr>
			        </#if>
		        </#list>
		        </#if>
		   </#list>
	        </tbody>
	    </table>

</body>
</html>