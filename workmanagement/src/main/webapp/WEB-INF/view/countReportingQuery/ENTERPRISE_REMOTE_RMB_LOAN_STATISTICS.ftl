<!DOCTYPE html>
<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>表10</title>
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
        .title0{
        	text-align: center;
        	height: 50px;
        	line-height: 40px;
        	font-weight: bord;
        	font-size: 16px;
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
    		$("#title").text(parent.window.getSelAreaName()+"企业异地人民币贷款情况统计表");
    	});
    </script>
</head>
<body>

<p class="taskList textLeft fontBold">查询结果：</p>
<p class="title0"  id="title" >企业异地人民币贷款情况统计表</p>
 <div class="conditionShow">
            <span>
            	${fm_begin} - ${fm_end}
            </span>
            <p>单位：万元</p>
        </div>
<table cellpadding="0" cellspacing="0">
    <thead>
        <tr>
            <th class="txc">地区</th>
            <th class="txc">异地贷款</th>
        </tr>
    </thead>
    
    <tbody>
    	<#if result?size != 0 >
	    	<#list result as obj>
		        <tr>
		            <td class="txc">
		            	<#if obj.INDEX_YDDK_YDSS!=null>
		            		${obj.INDEX_YDDK_YDSS}/
		            		${obj.INDEX_YDDK_YDSQ}
		            	<#else>
		            		${obj.INDEX_YDDK_YDSQ}
		            	</#if>
		            </td>
		            <td class="txc">${obj.SU * 0.0001}</td>
		        </tr>
			</#list>
		<#else>
			<tr>
	            <td class="txc" colspan=2 style="height:50px">暂无数据</td>
	        </tr>
		</#if>
    </tbody>
</table>

</body>
</html>