<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
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
        select{
            margin-right: 5px;
        }
        .reportingQuery{
            
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
        .queryBtn{
            width: 120px;
            height: 30px;
            display: block;
            margin: 20px auto;
            cursor: pointer;
            border: none;
            background-color: rgb(56,165,226);
            color: white;
            border-radius: 4px;
        }
    </style>
    
    
    <script type="text/javascript" >
    	$(function(){
    		//回显
    		var msg = "${msg}";
    		if(msg!==""){
    			alertInfo(msg,false);
    		}
    		
    		//初始化日期
    		var start = {
			  elem: '#start',
			  format: 'YYYY-MM-DD',
			    max:laydate.now(),
				istime:false,
				isclear:true,
				istoday:true,
				festival:true,
				fixed: false,
			  choose: function(datas){
			     end.min = datas; //开始日选好后，重置结束日的最小日期
			     end.start = datas //将结束日的初始值设定为开始日
			  }
			};
			var end = {
			    elem: '#end',
			    format: 'YYYY-MM-DD',
			    max:laydate.now(),
				istime:false,
				isclear:true,
				istoday:true,
				festival:true,
				fixed: false,
			    choose: function(datas){
			    start.max = datas; //结束日选好后，重置开始日的最大日期
			  }
			};
			laydate(start);
			laydate(end);
    	});
    
		//显示统计区域弹出框
		function openPop(){
			$("#covered").show();
			$("#poplayer").show();
			//判断浏览器是否为IE6  IE6  我日你妈
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
				    $(".shouldHide").hide();
				}
		}
		
		//关闭统计区域弹出框
		function closePop(){
			$("#covered").hide();
			$("#poplayer").hide();
			//判断浏览器是否为IE6
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
				    $(".shouldHide").show();
				}
		}
		
		//选择统计区域
		function selUpstream(obj){
			$.each($(".seleced"),function(){
				$(this).removeClass("seleced");
			});
			$(obj).addClass("seleced");
		}
		
		//确认选择
		function confirmSel(){
			var seleced = $(".seleced");
			if(seleced.length==0){
				layer.alert("您还没有选择统计区域",{
					icon:2,
					shade:0.3
				});
			}else{
				var selId = seleced.attr("id");
				closePop();
				var area = $(seleced.find("label"));
				var areaName = area.text();
				$("#openPop").text(areaName);
				$("#area").val(selId);
				$("#areaName").val(areaName);
			}
		}
		
		function openArea(obj, id){
			obj = $(obj);
			//子区域的缩进
			var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 32 + "px";
			
			if(obj.attr("id")==0){ //未展开
				//显示正在加载子区域弹窗
				var tc_index = layer.load(0,{
					shade: [0.5,'#fff'] //0.1透明度的白色背景
				});
				//设置为展开状态
				obj.attr("id",1);
				//将图标设置为展开图标
				$(obj.find("img")[0]).css("right",5);
				
				//获取父区域的tr
				var ptr = obj.parent().parent();
				var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
				$.get(url,{_:Math.random(),id:id},function(result){
					//关闭加载提示弹窗
					layer.close(tc_index);
					if(result!=null){
						var subs = result.subArea;
						for(var i = 0; i < subs.length; i++){
							//子地区
							var sub = subs[i];
							//展开图标
							var icon = "";
							if(sub.subArea!=null && sub.subArea.length!=0){
								icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sys_area_id+')">'
									   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
									   +'</div>'
							}
							
							
							var tr = $("<tr name='"+sub.sys_area_id+"' class='"+id+"'></tr>");
							var name = "<td id='"+sub.sys_area_id+"' onclick='selUpstream(this)' style='border: none;padding-left:"+spacing+"'>"
										+icon
										+"<label class='fontSize12'>"+sub.sys_area_name+"</label>"
										+"</td>";
							
							tr.append(name);
							
							ptr.after(tr);
						}
					}
				});
			}else{ //已展开
				//设置为收缩状态
				obj.attr("id",0);
				//将图标设置为收缩图标
				$(obj.find("img")[0]).css("right",20);
				//删除子区域
				delSubArea(id);
			}
		}
		
		//删除子区域
		function delSubArea(id){
			$.each($("."+id),function(i, v){
				var pid = v.attributes.getNamedItem("name").nodeValue;
				
				//删除子区域
				$(this).remove();
		
				//递归删除子区域
				delSubArea(pid);
			});
		}
		
		//查询
		function searchForm(){
			var begin = $("#begin").val();
			var end = $("#end").val();
			var area = $("#area").val();
			
			if(begin=="" || end=="" || area==""){
				alertInfo("请填写所有查询条件");
				return false;
			}
			wait();
			return true;
		}
		
		//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
	</script>

</head>
<body>

<div class="reportingQuery">
	<#-- 弹出框 -->
    <div id="covered"></div>  
    <div id="poplayer">  
        <div class="borderBox">
        	<div class="titleFont1">地区列表</div>
        	<div class="listBox">
        		<table cellpadding="0" cellspacing="0" style="border: none;">
					<tr>
						<td id="${sa.sys_area_id}" onclick="selUpstream(this)" style="border: none;padding: 0px;">
							<#if (sa.subArea?? && sa.subArea?size > 0) >
								<div id="0" class="open-shrink" onclick="openArea(this, ${sa.sys_area_id})">
									<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
								</div>
							</#if>
							<label class="fontSize12">${sa.sys_area_name}</label>
						</td>
					</tr>
				</table>
        	</div>
        	<div class="btnBox">
        		<input type="button" value="取 消"  class="cancleBtn" onclick="closePop()"/>
        		<input type="button" value="确 认"  class="sureBtn" onclick="confirmSel()"/>
        	</div>
        </div>  
    </div>  






   <div class="showListBox" style="border: none;">
	<form method="post" action="${request.getContextPath()}/admin/countReportingQuery/list.jhtml">
	    <table cellpadding="0" cellspacing="0" style="margin-left: 0px;">
	        <caption class="titleFont1 titleFont1Ex">选择查询条件</caption>
	        <tr>
	            <td class="noBorderL firstTD">统计报表模板</td>
	            <td class="secondTD">
	                <select name="template" class="inputSty shouldHide">
	                	<#list tmplates as t>
	                   		<option <#if t.queryTotalTemplateId==template>selected=selected</#if> value="${t.queryTotalTemplateId}">${t.queryTotalTemplateName}</option>
	                    </#list>
	                </select>
	            </td>
	        </tr>
	        <tr>
	            <td class="noBorderL firstTD">统计时间</td>
	            <td class="secondTD">
	            	<input id="start" autocomplete="off" class="laydate-icon inputSty fontSize12" name="begin" value="${begin}">
	                 ~
	                <input id="end" autocomplete="off" class="laydate-icon inputSty fontSize12" name="end" value="${end}">
	            </td>
	        </tr>
	        <tr>
	            <td class="noBorderL firstTD">统计区域</td>
	            <td class="secondTD">
	            	<input type="hidden" id="areaName" name="areaName" value="${areaName}" />
	            	<input type="hidden" id="area" name="area" value="${area}"/>
	                <a id="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()">${(areaName!="")?string(areaName,'点击选择')}</a>
	            </td>
	        </tr>
	    </table>
    	<input type="submit" onclick="return searchForm()" class="queryBtn" value="开始查询" />
	</form>
   </div> 
    
    
    <#if results??>
	    <p class="taskList textLeft fontBold">查询结果：</p>
	</#if>
    
    <!--贷款分机构情况统计表-->
    <#if (results??)&&template==1>
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
		        <tr>
		            <td><b>合计</b></td>
		            <td class="txc">${rmb_ydhs_count}</td>
		            <td class="txc">${rmb_dkye_count}</td>
		            <td class="txc">${wb_ydhs_count}</td>
		            <td class="txc">${wb_dkye_count}</td>
		            <td class="txc">${ben_wai_ydhs_count}</td>
		            <td class="txc">${ben_wai_dkye_count}</td>
		        </tr>
		        <#list results?reverse as result>
			        <tr>
			        	<#if result.one>
		        			<td><b>${result.org_type_name}小计</b></td>
			        	<#else>
			        		<td>${result.org_type_name}</td>
			        	</#if>
			            <td class="txc">${result.rmb_ydhs}</td>
			            <td class="txc">${result.rmb_dkye}</td>
			            <td class="txc">${result.wb_ydhs}</td>
			            <td class="txc">${result.wb_dkye}</td>
			            <td class="txc">${result.ben_wai_ydhs}</td>
			            <td class="txc">${result.ben_wai_dkye}</td>
			        </tr>
			    </#list>
	        </tbody>
	    </table>
    </#if>



    <!--贷款分机构分规模情况统计表-->
    <#if template==2>
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
		        <tr>
		            <td><b>合计</b></td>
		            <td class="txc">${dxqy_rmb_dkye_count}</td>
		            <td class="txc">${dxqy_wb_dkye_count}</td>
		            <td class="txc">${zxqy_rmb_dkye_count}</td>
		            <td class="txc">${zxqy_wb_dkye_count}</td>
		            <td class="txc">${xxqy_rmb_dkye_count}</td>
		            <td class="txc">${xxqy_wb_dkye_count}</td>
		            <td class="txc">${wxqy_rmb_dkye_count}</td>
		            <td class="txc">${wxqy_wb_dkye_count}</td>
		            <td class="txc">${qt_rmb_dkye_count}</td>
		            <td class="txc">${qt_wb_dkye_count}</td>
		            <td class="txc">${hj_rmb_dkye_count}</td>
		            <td class="txc">${hj_wb_dkye_count}</td>
		        </tr>
		        <#list results?reverse as result>
			        <tr>
			            <#if result.one>
		        			<td><b>${result.org_type_name}</b></td>
			        	<#else>
			        		<td>${result.org_type_name}</td>
			        	</#if>
			            <td class="txc">${result.dxqy_rmb_dkye}</td>
			            <td class="txc">${result.dxqy_wb_dkye}</td>
			            <td class="txc">${result.zxqy_rmb_dkye}</td>
			            <td class="txc">${result.zxqy_wb_dkye}</td>
			            <td class="txc">${result.xxqy_rmb_dkye}</td>
			            <td class="txc">${result.xxqy_wb_dkye}</td>
			            <td class="txc">${result.wxqy_rmb_dkye}</td>
			            <td class="txc">${result.wxqy_wb_dkye}</td>
			            <td class="txc">${result.qt_rmb_dkye}</td>
			            <td class="txc">${result.qt_wb_dkye}</td>
			            <td class="txc">${result.hj_rmb_dkye}</td>
			            <td class="txc">${result.hj_wb_dkye}</td>
			        </tr>
				</#list>
	        </tbody>
	    </table>
	</#if>
	    

    <!--贷款分机构分期限情况统计表-->
    <#if template==3>
	    <table cellpadding="0" cellspacing="0">
	        <caption class="titleFont1">
	            <div class="conditionShow">
	                <span>20** 年 ** 月 ** 日 — 20** 年 ** 月 ** 日 </span>
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
	            <th colspan="2">
	                6个月（含）以内
	            </th>
	            <th colspan="2">
	                6个月-1年（含）
	            </th>
	            <th colspan="2">
	                1-3年（含）
	            </th>
	            <th colspan="2">
	                3-5年（含）
	            </th>
	            <th colspan="2">
	                5年以上
	            </th>
	            <th colspan="2">
	                合计
	            </th>
	        </tr>
	        <tr>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	        </tr>
	        </thead>
	        <tbody>
	        <tr>
	            <td><b>合计</b></td>
	            <td>32423</td>
	            <td>22222</td>
	            <td>43434</td>
	            <td>435333</td>
	            <td>45345</td>
	            <td>465455</td>
	            <td>45345</td>
	            <td>465455</td>
	            <td>45345</td>
	            <td>465455</td>
	            <td>45345</td>
	            <td>465455</td>
	        </tr>
	        <tr>
	            <td>农、林、牧、渔业</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        <tr>
	            <td>采矿业</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        </tbody>
	    </table>
	</if>
	</#if>
	

    <!--贷款分机构分期限利率情况统计表-->
    <#if template==4>
	    <table cellpadding="0" cellspacing="0">
	        <caption class="titleFont1">
	            <div class="conditionShow">
	                <span>20** 年 ** 月 ** 日—20** 年 ** 月 ** 日</span>
	                <p>单位：万元/万美元</p>
	            </div>
	        </caption>
	        <thead>
	        <tr>
	            <th rowspan="2" class="tableTop" style="width: 130px;">
	                <img src="${request.getContextPath()}/assets/images/table-line.png" alt="" style="width: 130px;">
	                <p class="tableTopRight">企业群</p>
	                <p class="tableTopLeft">机构</p>
	            </th>
	            <th colspan="2">
	                企业群体1
	            </th>
	            <th colspan="2">
	                企业群体2
	            </th>
	            <th colspan="2">
	                企业群体2
	            </th>
	        </tr>
	        <tr>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	            <th>
	                人民币
	            </th>
	            <th>
	                外币
	            </th>
	        </tr>
	        </thead>
	        <tbody>
	        <tr>
	            <td><b>合计</b></td>
	            <td>32423</td>
	            <td>22222222</td>
	            <td>43434</td>
	            <td>4354333333</td>
	            <td>45345</td>
	            <td>4654655555</td>
	        </tr>
	        <tr>
	            <td><b>中资银行小计</b></td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        <tr>
	            <td>中国农业发展银行</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        </tbody>
	    </table>
    </#if>
	
    <!--新发放贷款分行业分期限情况统计表-->
    <#if template==5>
	    <table cellpadding="0" cellspacing="0">
	        <caption class="titleFont1">
	            <div class="conditionShow">
	                <span>20** 年 ** 月 ** 日—20** 年 ** 月 ** 日</span>
	                <p>单位：万元/万美元</p>
	            </div>
	        </caption>
	        <thead>
	        <tr>
	            <th rowspan="2" class="tableTop" style="width: 130px;">
	                <img src="${request.getContextPath()}/assets/images/table-line.png" alt="" style="width: 130px;">
	                <p class="tableTopRight">季度</p>
	                <p class="tableTopLeft">企业群</p>
	            </th>
	            <th colspan="2">
	                一季度末
	            </th>
	            <th colspan="2">
	                二季度末
	            </th>
	            <th colspan="2">
	                三季度末
	            </th>
	            <th colspan="2">
	                四季度末
	            </th>
	        </tr>
	        <tr>
	            <th>
	                户数
	            </th>
	            <th>
	                余额
	            </th>
	            <th>
	                户数
	            </th>
	            <th>
	                余额
	            </th>
	            <th>
	                户数
	            </th>
	            <th>
	                余额
	            </th>
	            <th>
	                户数
	            </th>
	            <th>
	                余额
	            </th>
	        </thead>
	        <tbody>
	        <tr>
	            <td><b>合计</b></td>
	            <td>32423</td>
	            <td>22222222</td>
	            <td>43434</td>
	            <td>4354333333</td>
	            <td>45345</td>
	            <td>4654655555</td>
	            <td>45345</td>
	            <td>4654655555</td>
	        </tr>
	        <tr>
	            <td>企业群体1</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        <tr>
	            <td>企业群体2</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	            <td>&nbsp;</td>
	        </tr>
	        </tbody>
	    </table>
	</#if>
	
	<#-- 新发放贷款分行业分期限利率情况统计表 -->
	<#if template==6>
	</#if>
	
	<#-- 重点关注企业贷款分机构情况统计表 -->
	<#if template==7>
	
		<#if (keyents?? && keyents?size > 0)>
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
			            </th>
			            
			            <#list keyents as key>
				            <th colspan="2" class="txc">${key.keyentErprisesSetName}</th>
				        </#list>
			        </tr>
			        <tr>
		        	 	<#list keyents as key>
			             	<th class="txc">人民币</th>
		            		<th class="txc">外币</th>
				        </#list>
			        </tr>
		        </thead>
		        <tbody>
		        <tr>
		            <td><b>合计</b></td>
		            <td>32423</td>
		            <td>22222222</td>
		            <td>43434</td>
		            <td>4354333333</td>
		            <td>45345</td>
		            <td>4654655555</td>
		        </tr>
		        <tr>
		            <td><b>中资银行小计</b></td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		        </tr>
		        <tr>
		            <td>中国农业发展银行</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		            <td>&nbsp;</td>
		        </tr>
		        </tbody>
		    </table>
		<#else>
			未查询到企业群信息
		</#if>
	</#if>
	
	<#-- 重点关注企业贷款分机构利率情况统计表 -->
	<#if template==8>
	</#if>
	
	<#-- 重点关注企业本外币贷款时序统计表 -->
	<#if template==9>
	</#if>
	
	<#-- 企业异地人民币贷款情况统计表 -->
	<#if template==10>
	</#if>
	
	<#-- 企业进出口情况统计表 -->
	<#if template==11>
	</#if>
	
	<#-- 企业纳税情况统计表 -->
	<#if template==12>
	</#if>
	
	<#-- 系统使用情况统计表 -->
	<#if template==13>
	</#if>
</div>

</body>
</html>