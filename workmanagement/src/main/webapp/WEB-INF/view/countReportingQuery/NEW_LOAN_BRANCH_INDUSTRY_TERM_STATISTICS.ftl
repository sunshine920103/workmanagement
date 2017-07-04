<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>新发放贷款分行业分期限情况统计表</title>
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
    		
    		$("#title").text(parent.window.getSelAreaName()+"新发放贷款分行业分期限情况统计表");
    		
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
	<p id="title" class="title0">新发放贷款分行业分期限情况统计表</p>	
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
	                <p class="tableTopRight">期限</p>
	                <p class="tableTopLeft">行业</p>
	            </th>
	            <th colspan="2" class="txc">6个月（含）以内</th>
	            <th colspan="2" class="txc">6个月-1年（含） </th>
	            <th colspan="2" class="txc">1-3年（含）</th>
	            <th colspan="2" class="txc">3-5年（含）</th>
	            <th colspan="2" class="txc">5年以上</th>
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
	    	<#assign six_months_rmb_count=0 />
	    	<#assign six_months_wb_count=0 />
	    	<#assign six_months_to_one_years_rmb_count=0 />
	    	<#assign six_months_to_one_years_wb_count=0 />
	    	<#assign one_years_to_three_years_rmb_count=0 />
	    	<#assign one_years_to_three_years_wb_count=0 />
	    	<#assign three_years_to_five_years_rmb_count=0 />
	    	<#assign three_years_to_five_years_wb_count=0 />
	    	<#assign more_than_five_years_rmb_count=0 />
	    	<#assign more_than_five_years_wb_count=0 />
	    	<#assign hj_rmb_count=0 />
	    	<#assign hj_wb_count=0 />
	    	<#list results?reverse as result>
	        	<#assign six_months_rmb_count=((six_months_rmb_count + result.six_months_rmb * 0.0001)) />
		    	<#assign six_months_wb_count=((six_months_wb_count + result.six_months_wb * 0.0001)) />
		    	<#assign six_months_to_one_years_rmb_count=((six_months_to_one_years_rmb_count + result.six_months_to_one_years_rmb * 0.0001)) />
		    	<#assign six_months_to_one_years_wb_count=((six_months_to_one_years_wb_count + result.six_months_to_one_years_wb * 0.0001)) />
		    	<#assign one_years_to_three_years_rmb_count=((one_years_to_three_years_rmb_count + result.one_years_to_three_years_rmb * 0.0001)) />
		    	<#assign one_years_to_three_years_wb_count=((one_years_to_three_years_wb_count + result.one_years_to_three_years_wb * 0.0001)) />
		    	<#assign three_years_to_five_years_rmb_count=((three_years_to_five_years_rmb_count + result.three_years_to_five_years_rmb * 0.0001)) />
		    	<#assign three_years_to_five_years_wb_count=((three_years_to_five_years_wb_count + result.three_years_to_five_years_wb * 0.0001)) />
		    	<#assign more_than_five_years_rmb_count=((more_than_five_years_rmb_count + result.more_than_five_years_rmb * 0.0001)) />
		    	<#assign more_than_five_years_wb_count=((more_than_five_years_wb_count + result.more_than_five_years_wb * 0.0001)) />
		    	<#assign hj_rmb_count=((hj_rmb_count + result.hj_rmb * 0.0001)) />
		    	<#assign hj_wb_count=((hj_wb_count + result.hj_wb * 0.0001)) />
		    </#list>
		    
	        <tr>
	            <td class="txc"><b>合计</b></td>
	            <td class="txc num">${six_months_rmb_count}</td>
	            <td class="txc num">${six_months_wb_count}</td>
	            <td class="txc num">${six_months_to_one_years_rmb_count}</td>
	            <td class="txc num">${six_months_to_one_years_wb_count}</td>
	            <td class="txc num">${one_years_to_three_years_rmb_count}</td>
	            <td class="txc num">${one_years_to_three_years_wb_count}</td>
	            <td class="txc num">${three_years_to_five_years_rmb_count}</td>
	            <td class="txc num">${three_years_to_five_years_wb_count}</td>
	            <td class="txc num">${more_than_five_years_rmb_count}</td>
	            <td class="txc num">${more_than_five_years_wb_count}</td>
	            <td class="txc num">${hj_rmb_count}</td>
	            <td class="txc num">${hj_wb_count}</td>
	        </tr>
	        <#list results?reverse as result>
	        	<#if (result.hj_rmb != 0 || result.hj_wb != 0) >
			        <tr>
		        		<td class="txc">${result.jjhy}</td>
			            <td class="txc num">${(result.six_months_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.six_months_wb * 0.0001)}</td>
			            <td class="txc num">${(result.six_months_to_one_years_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.six_months_to_one_years_wb * 0.0001)}</td>
			            <td class="txc num">${(result.one_years_to_three_years_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.one_years_to_three_years_wb * 0.0001)}</td>
			            <td class="txc num">${(result.three_years_to_five_years_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.three_years_to_five_years_wb * 0.0001)}</td>
			            <td class="txc num">${(result.more_than_five_years_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.more_than_five_years_wb * 0.0001)}</td>
			            <td class="txc num">${(result.hj_rmb * 0.0001)}</td>
			            <td class="txc num">${(result.hj_wb * 0.0001)}</td>
			        </tr>
				</#if>
			</#list>
	    </tbody>
	</table>

</body>
</html>