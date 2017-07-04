<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>系统使用情况统计表</title>
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
    		
    		$("#title").text(parent.window.getSelAreaName()+"系统使用情况统计表");
    	});
    </script>
</head>
<body>

	<p class="taskList textLeft fontBold">查询结果：</p>
	<p id="title" class="title0">系统使用情况统计表</p>
	<table cellpadding="0" cellspacing="0">
	    <caption class="titleFont1">
	    	
	        <div class="conditionShow">
	            <span>
	            	${fm_begin} - ${fm_end}
	            </span>
	            <p>单位：户、次</p>
	        </div>
	    </caption>
	    <thead>
	        <tr>
	            <th class="txc">项目</th>
	            <th class="txc">大型企业</th>
	            <th class="txc">中型企业</th>
	            <th class="txc">小型企业</th>
	            <th class="txc">微型企业</th>
	            <th class="txc">其他</th>
	            <th class="txc">合计</th>
	        </tr>
	    </thead>
	    
	    <tbody>
	    	<#list results as result>
	    		<tr>
		            <td class="txc"><b>${result.type}</b></td>
		            <td class="txc">${result.dx}</td>
		            <td class="txc">${result.zx}</td>
		            <td class="txc">${result.xx}</td>
		            <td class="txc">${result.wx}</td>
		            <td class="txc">${result.qt}</td>
		            <td class="txc">${result.dx + result.zx + result.xx + result.wx + result.qt}</td>
		        </tr>
	    	</#list>
	    </tbody>
	</table>

</body>
</html>