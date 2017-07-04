<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <title></title>
    <style>
        .queryResult{
            width: 800px;
            margin: 100px auto;
            border: 1px solid #DADADA;
            padding: 20px;
        }
        .queryHead{
            width: 100%;
            height: 30px;
            line-height: 30px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
        .queryResult table{
            width: 100%;
            border-top: 1px solid #dadada;
            border-left: 1px solid #dadada;
            font-size: 12px;
        }
        .queryResult th{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            text-align: left;
        }
        .queryResult td{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
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
        .queryResult .left{
            text-align: left;
        }
        .queryResult .right{
            text-align: right;
        }
        .queryDetails{
            padding: 10px 3px;
        }
        .flip-box{
            width: 100%;
            height: 50px;
            margin-top: 20px;
        }
        .flip-page{
            height: 30px;
            float: right;
        }
        .flip-page li{
            float: left;
            margin-right: 15px;
            list-style: none;
        }
        .flip-page a{
            color: #3366CC;
        }
        .queryBtn{
        	margin-top: 30px;
            width: 100%;
            height:50px;
            text-align: center;
        }
        .queryBtn button{
            width: 120px;
            height: 30px;
            margin: 0 20px;
            outline: none;
            border: none ;
            cursor: pointer;
            color: white;
            border-radius: 4px;
        }
        .queryBtn .cancel{
            background-color: #a4a4a4;
        }
        .queryBtn .query{
            background-color: rgb(56,165,226);
        }
        /*#trPanel span:hover{
        	cursor:pointer;
        }*/
       #trPanel .sort{
       		
       }
       .positionR{
       	position: relative;
       }
       .positionA{
       	position: absolute;
       }
       .org_name{
        	font-size: 11px;
        	display: inline-block;
        	*display: inline;
        	*zoom: 1;
        	padding: 1px 3px;
        	border: 1px solid #CCCCCC;
        	margin-right: 5px;
        	margin-bottom: 3px;
        }
    </style>
    <script type="text/javascript">
    	$(function(){ 
			$("#downLoadExcel").click(function(){
				var time = $("#timeReport").val();
				var num = $(".tbsid").length;
				var cked;
				
				var str=[];
				var tbsid=[];
				for( i = 0;i < num; i ++){
					var chedstr="";
					cked = $(".tbsid").eq(i).parent().find("input[name='iden']") ;
					for(j=0;j<cked.length;j++){
						if(cked.eq(j).is(':checked')){
							chedstr+=cked.eq(j).val()+";";
						}
					}
					if(chedstr!=""){
						tbsid.push($(".tbsid").eq(i).val());
						str.push(chedstr);
					} 
				}
				
				var url = "${request.getContextPath()}/admin/identification/addIden.jhtml";
	       		var loading = layer.load();
	        	$.post(url,{defId:str.join(","),identId:tbsid.join(","),recode:time,areaId:${area.sysAreaId}},function(data){
					layer.close(loading);
					var index = alertInfoFun(data.message, data.flag, function(){ 
						layer.close(index);
						window.location.href='${request.getContextPath()}/admin/companyListFilter/list.jhtml';
					});
				});   
			})
    	})
 //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
			//全选反选
		function CheckedRev(){
		var arr = $(':checkbox'); 
		for(var i=1;i<arr.length;i++){ 
		arr[i].checked = ! arr[i].checked; 
		}
		}
    </script>
</head>
<body>
	
	<form action="${request.getContextPath()}/admin/companyListFilter/result.jhtml" id="searchForm" method="post"></form>
    <div class="queryResult showListBox">
        <p class="queryHead">企业标识处理</p>
        <table cellpadding="0" cellspacing="0">
            	<tr>
	                <td width="200" class="noBorderL firstTD">当前区域</td>
	                <td width="500" class="secondTD">
	                	${area.sysAreaName}
	                	<input type="hidden" name ="areaId" value="${area.sysAreaId}">
	                </td>
	            </tr>
	            <tr>
	                <td width="200" class="noBorderL firstTD">归档时间</td>
	                <td width="500" class="secondTD">
	                	<input class="laydate-icon inputSty" autocomplete="off" 
						id="timeReport" onclick="laydate({istime: false, format: 'YYYY-MM-DD'})" name="timeReport" value="">
	                </td>
	            </tr>
        </table> 
        <div style="margin-bottom: 30px;">
        </div>
        <table cellpadding="0" cellspacing="0">
        	<caption class="titleFont1 titleFont1Ex">企业名单列表</caption>
            <tbody id="trPanel">
            	<tr>
	                <td width="100">序号</td>
	                <td width="200">企业二码</td>
	                <td width="200">企业名称</td>
	                <td width="200">联系电话</td>
	                <td width="200">法定代表人</td>
	                <td width="200">注册地</td>
	                <td width="200">企业标识 <input type="checkbox"  onclick="CheckedRev()"  name="" id="" value="" /></td>
	            </tr>
            <#list defaultIndexItemCustomList as item>
            	<tr>
	            	<td> ${1 + item_index}</td>
            		<td class="redFlagCredit">${item.codeCredit}</td>
            		<td>${item.qymc}</td>
            		<td>${item.lxdh}</td>
            		<td>${item.fddbr}</td>
            		<td>${item.qyzs}</td>
            		<td>
            			<input type="hidden" name="tbsid" class="tbsid" value="${item.defaultItemId}" />
            		
            			<#list iden as iden>
            				 <label class="org_name">
            					<input type="checkbox" name="iden" id="" value="${iden.sys_identification_id}" /> ${iden.sys_identification_name}
            				</label>
            			</#list>
            		</td>
            	</tr>
            </#list>
        </table> 
        <div class="queryBtn" style="clear:both;">
            <button type="button" class="cancel" onclick="window.location.href='${request.getContextPath()}/admin/companyListFilter/list.jhtml'">关闭</button>
            <button type="button" class="query" id="downLoadExcel">保存</button> 
        </div>
    </div>
</body>
</html>