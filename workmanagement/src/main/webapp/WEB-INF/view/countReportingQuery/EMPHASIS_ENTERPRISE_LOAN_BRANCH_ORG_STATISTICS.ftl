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
    		$("#title").text(parent.window.getSelAreaName()+"重点关注企业贷款分机构情况统计表");
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
			<p id="title" class="title0">重点关注企业贷款分机构情况统计表</p>	
			<table cellpadding="0" cellspacing="0">
		        <caption class="titleFont1">
		            <div class="conditionShow">
		                <span>
		                	${fm_begin} - ${fm_end}
		                </span>
		                <p>单位：万元/万美元</p>
		            </div>
		        </caption>
		        <thead>
			        <tr>
			            <th rowspan="2" class="tableTop" style="width: 130px;">
			                <img src="${request.getContextPath()}/assets/images/table-line.png" alt="" style="width: 130px;">
			                <p class="tableTopRight">企业群</p>
			                <p class="tableTopLeft">机构</p>
			                <#list qyq as map>
				            	<th colspan="2" class="txc">${map}</th>
				        	</#list>
			            </th>			            
			        </tr>
			        <tr>
			            <#list qyq as map>
				            <th class="txc">人民币</th>
		            		<th class="txc">外币</th>
				        </#list>
				     </tr>   
		        </thead>
		        <tbody>
					<#-- 计算总计的变量 -->
					<#list rlist as rli>
						<tr>
							<td><b>${rli.djg}</b></td>	
							<#list rli.zhi as zh>
							<#list zh?keys as k>
								<#list qyq as q>
								<#if q==k>
								<#list zh[k] as z >
									<td>${(z.rmb * 0.0001)?string("0.##")}</td>
									<td>${(z.wb *0.0001)?string("0.##")}</td>
									</#list>
								</#if>
							</#list>
							</#list>
						</#list>
						</tr>
						
					<#list res as map>
						<#if rli.code == map.code>
						<tr>
						<td>${map.jg}</td>
						<#list map.zhi as zh>
							<#list zh?keys as k>
								<#list qyq as q>
								<#if q==k>
								<#list zh[k] as z >
									<td>${(z.rmb * 0.0001)?string("0.##")}</td>
									<td>${(z.wb *0.0001)?string("0.##")}</td>
									</#list>
								</#if>
								
							</#list>
							</#list>
							
						</#list>
						</tr>
						</#if>
				    </#list>
					 </#list>
		        </tbody>
		    </table>
</body>
</html>