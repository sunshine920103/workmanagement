<!DOCTYPE html>
<html lang="en">
<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>重点关注企业贷款分机构情况统计表</title>
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
    		$("#title").text(parent.window.getSelAreaName()+"企业进出口情况统计表");   
    		//将金额转换成万元 并保留两位数字 （没有四舍五入）
			$(".num").each(function(){
			    $this=$(this);
			    var money = $this.html();
			    var money2 = money.toString();
			   	if(money2.indexOf(".")!=-1){
			   		money2 =  money2.substr(0,money2.indexOf(".")+3); 
			   	}
			    $this.html(money2);
			});
    	});
    </script>
</head>
<body>
		<p class="taskList textLeft fontBold">查询结果：</p>
		<p id="title" class="title0">企业进出口情况统计表</p>
		<table class="mainTable" cellpadding="0" cellspacing="0" style="width:800px">
		<caption class="titleFont1">
		<div class="conditionShow" >
			 <div class="conditionShow">
	            <span>
	            	${fm_begin} - ${fm_end}
	            </span>
	            <p>单位：万美元</p>
	        </div>
		</div>
		</caption>
			<thead>
			  <tr>
				<td>行业分类</td>
				<td>本年进口商品总价</td>
				<td>本年出口商品总价</td>
				<td>外汇收入</td>
				<td>外汇支出</td>
			  </tr>
			</thead>
			<tbody>	
		    	<#assign sum_bnjkspzj=0 />
	    		<#assign sum_bnckspzj=0 />
	    		<#assign sum_whsr=0 />
	    		<#assign sum_whzc=0 />	    			
	            		<tr>
	            		<td><b>合计</b></td>
	            			<#list result as map>
	           					<#assign sum_bnjkspzj=(sum_bnjkspzj + map.bnjkspzj * 0.0001) />
	        					<#assign sum_bnckspzj=(sum_bnckspzj + map.bnckspzj * 0.0001) />
	        				 	<#assign sum_whsr=(sum_whsr + map.whsr * 0.0001) />
	        					<#assign sum_whzc=(sum_whzc + map.whzc * 0.0001) />    				
	        				</#list>
	        				<td class="txc num">${sum_bnjkspzj}</td>
        					<td class="txc num">${sum_bnckspzj}</td>
	            			<td class="txc num">${sum_whsr}</td>
	            			<td class="txc num">${sum_whzc}</td>
	        			</tr>
	    	<#list result as map>
	    		<tr>
	    	    <td class="txc num">${map.jjhy}</td>
	        	<td class="txc num">${(map.bnjkspzj * 0.0001)}</td>
	            <td class="txc num">${(map.bnckspzj * 0.0001)}</td>
	            <td class="txc num">${(map.whsr * 0.0001)}</td>
	            <td class="txc num">${(map.whzc * 0.0001)}</td>
	            </tr>
		    </#list>
		     
			</tbody>
		</table>
	<div class="textCenter">
			<input type="button" name="" class="closeBtn closeThisLayer" value="关 闭" />
	</div>
</body>
</html>