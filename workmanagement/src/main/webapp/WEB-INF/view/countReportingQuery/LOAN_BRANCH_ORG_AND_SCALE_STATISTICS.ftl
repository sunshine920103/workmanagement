<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>贷款分机构分规模情况统计表</title>
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
    		
    		$("#title").text(parent.window.getSelAreaName()+"贷款分机构分规模情况统计表");
    		
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
	<p id="title" class="title0">贷款分机构分规模情况统计表</p>	
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
	                <p class="tableTopRight">规模</p>
	                <p class="tableTopLeft">机构</p>
	            </th>
	            <th colspan="2" class="txc">大型企业</th>
	            <th colspan="2" class="txc">中型企业 </th>
	            <th colspan="2" class="txc">小型企业</th>
	            <th colspan="2" class="txc">微型企业</th>
	            <th colspan="2" class="txc">其他</th>
	            <th colspan="2" class="txc">合计</th>
	        </tr>
	        <tr>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	            <th class="txc">人民币</th>
	            <th class="txc">外币</th>
	        </tr>
	    </thead>
	    <tbody>
	    	<#-- 计算总计 -->
	    	<#assign dx_rmb_count=0 />
	    	<#assign dx_wb_count=0 />
	    	<#assign zx_rmb_count=0 />
	    	<#assign zx_wb_count=0 />
	    	<#assign xx_rmb_count=0 />
	    	<#assign xx_wb_count=0 />
	    	<#assign wx_rmb_count=0 />
	    	<#assign wx_wb_count=0 />
	    	<#assign qt_rmb_count=0 />
	    	<#assign qt_wb_count=0 />
	    	<#assign hj_rmb_count=0 />
	    	<#assign hj_wb_count=0 />
	    	<#list results?reverse as result>
	    		<#if result.one>
		        	<#assign dx_rmb_count=((dx_rmb_count + result.dx_rmb * 0.0001)) />
			    	<#assign dx_wb_count=((dx_wb_count + result.dx_wb * 0.0001)) />
			    	<#assign zx_rmb_count=((zx_rmb_count + result.zx_rmb * 0.0001)) />
			    	<#assign zx_wb_count=((zx_wb_count + result.zx_wb * 0.0001)) />
			    	<#assign xx_rmb_count=((xx_rmb_count + result.xx_rmb * 0.0001)) />
			    	<#assign xx_wb_count=((xx_wb_count + result.xx_wb * 0.0001)) />
			    	<#assign wx_rmb_count=((wx_rmb_count + result.wx_rmb * 0.0001)) />
			    	<#assign wx_wb_count=((wx_wb_count + result.wx_wb * 0.0001)) />
			    	<#assign qt_rmb_count=((qt_rmb_count + result.qt_rmb * 0.0001)) />
			    	<#assign qt_wb_count=((qt_wb_count + result.qt_wb * 0.0001)) />
			    	<#assign hj_rmb_count=((hj_rmb_count + result.hj_rmb * 0.0001)) />
			    	<#assign hj_wb_count=((hj_wb_count + result.hj_wb * 0.0001)) />
			    </#if>
		    </#list>
		    
	        <tr>
	            <td class="txc"><b>合计</b></td>
	            <td class="txc num">${dx_rmb_count}</td>
	            <td class="txc num">${dx_wb_count}</td>
	            <td class="txc num">${zx_rmb_count}</td>
	            <td class="txc num">${zx_wb_count}</td>
	            <td class="txc num">${xx_rmb_count}</td>
	            <td class="txc num">${xx_wb_count}</td>
	            <td class="txc num">${wx_rmb_count}</td>
	            <td class="txc num">${wx_wb_count}</td>
	            <td class="txc num">${qt_rmb_count}</td>
	            <td class="txc num">${qt_wb_count}</td>
	            <td class="txc num">${hj_rmb_count}</td>
	            <td class="txc num">${hj_wb_count}</td>
	        </tr>
	        <#list results?reverse as result>
		        <#if (result.hj_rmb != 0 || result.hj_wb != 0) >
			        <tr>
			            <#if result.one>
		        			<td class="txc"><b>${result.org_type_name}</b></td>
			        	<#else>
			        		<td class="txc">${result.org_type_name}</td>
			        	</#if>
			            <td class="txc num">${(result.dx_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.dx_wb * 0.0001)}</td>
			            <td class="txc num">${(result.zx_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.zx_wb * 0.0001)}</td>
			            <td class="txc num">${(result.xx_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.xx_wb * 0.0001)}</td>
			            <td class="txc num">${(result.wx_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.wx_wb * 0.0001)}</td>
			            <td class="txc num">${(result.qt_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.qt_wb * 0.0001)}</td>
			            <td class="txc num">${(result.hj_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.hj_wb * 0.0001)}</td>
			        </tr>
				</#if>       
			</#list>
	    </tbody>
	</table>

</body>
</html>