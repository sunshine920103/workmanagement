<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <style>
        /*li无样式*/
		li{
			list-sytle:none;
			float: left;
		}
        .filter-condition{
            margin: 10px 20px;
            width: 800px;
            word-break:break-all;
        }
        .summaryInfo{
        	font-size: 14px;
        	font-weight: bold;
        	float:left;
        }
        .filter-condition a{
            padding:5px 10px;
            color: #333;
            text-decoration: none;
            display: inline-block;
        }

        .anchor a{
        	font-size: 12px;
        	color:rgb(56,165,226);
        	margin-left:5px;
        }
    </style>
    <#include "/fragment/common.ftl"/>
    <script type="text/javascript">
    	//企业二码验证
			function testOrgCode(theObj){
		   		 var regOrgCode = /^[a-zA-Z0-9]{8}[\-]{1}[a-zA-Z0-9]{1}$/;
				var r = theObj.match(regOrgCode); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
       function regCreditCode(theObj){
		   		 var regCreditCode = /^[0-9a-zA-Z]{18}$/;
				var r = theObj.match(regCreditCode); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
     //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
      
         //组织机构代码隐藏
        $(function(){
        	var str="${pass}";
        	if(str=="0"){
        		$("#orgcode").hide();
        	}
        });

    	$(function(){
    		for (var i = 0; i < $(".numtd").length; i++) {
    			var tablewidth=$(".numtd").eq(i).children().length*100;
    			var listbox =$(".numtd").eq(0).parent().parent().parent().width();
    			if(listbox<tablewidth){
    				
    				$(".numtd").eq(i).parent().parent().css("width",tablewidth+"px");
    			}else {
    				$(".numtd").eq(i).parent().parent().css("width","100%");
    			}
    		
    		}
    	})
    	//提交验证
    	function checkfy(){
    		var codeCredit=$("[name=indexItemCode]").eq(0).val();
    		var codeOrg=$("[name=indexItemCode]").eq(1).val();
    		if(codeCredit==""&&codeOrg==""){
				layer.alert("请输入组织机构代码\统一社会信用代码",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}else if(codeCredit==""&&codeOrg!=""){
				if(testOrgCode(codeOrg) == 0 ){
				layer.alert("组织机构代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
				return false;
				}
			}else if(codeCredit!=""&&codeOrg==""){
				if(regCreditCode(codeCredit) == 0 ){
					layer.alert("统一社会信用代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
			}else if(codeCredit!=""&&codeOrg!=""){
				if(testOrgCode(codeOrg) == 0 ){
					layer.alert("组织机构代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				if(regCreditCode(codeCredit) == 0 ){
					layer.alert("统一社会信用代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
			}
    	}
    
    </script>
</head>
<body class="eachInformationSearch">

<form action="${request.getContextPath()}/admin/companyInfoQuery/list.jhtml" method="post" id="companyForm">
	<div class="filter-content showListBox noBorder" id="indexItemDiv">
		<table cellpadding="0" cellspacing="0" style="margin-left: 0px;" class="abc">
			<tr>
				<td class="noBorderL firstTD" width="200">统一社会信用代码</td>
				<td class="secondTD"  width="400"><input name="indexItemCode" type="text" class="inputSty allcreditcodeVal" onblur="onblurVal(this,16,18)" maxlength="18" value="${indexItemCode[0]}" onKeyDown="if(this.value.length > 18){ return false }"></td>
			</tr>
			<tr id="orgcode">
				<td class="noBorderL firstTD">组织机构代码</td>
				<td class="secondTD"><input class="inputSty allorgnumVal" name="indexItemCode" type="text" onblur="onblurVal(this,15,10)" maxlength="10" value="${indexItemCode[1]}" onKeyDown="if(this.value.length > 10){ return false }"></td>
			</tr>
			<tr>
				<td class="noBorderL firstTD">归档时间</td>
				<td class="secondTD"><input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="begin"
                       name="beginTime" value="${beginTime}"> 至
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end"
                       name="endTime" value="${endTime}"></td>
			</tr>
		</table>
	</div>
	<div class="showListBox" style=" border: none;">
		<div class="showBtnBox" style=" margin-left: 0px;">
		    <button type="reset" class="cancleBtn">清空</button>
		    <button type="submit" class="sureBtn" onclick="return checkfy();">查询</button>
		</div>
	</div>
</form>


	<!--列表-->
	<!--循环 -->
		<#if ComPanyShow??>
		<div class="listBox" style="overflow: auto;margin-top:50px;">
		<table>
		<caption class="titleFont1 titleFont1Ex">企业标识</caption>
		<tr>
			<td>编号</td>
			<td>标识名</td>
			<td>标识机构</td>
		</tr>
		<#list ComPanyShow as com>
			<tr>
				<td>${com_index+1}</td>
					<td>
					<#list com["DIC_ID"]?split(',') as i>
						<#list dic as di>
							<#if di.sys_identification_id == i>
									${di.sys_identification_name}
							</#if>
						</#list>
					</#list>
				</td>
				<td class="orgid">${com["SYS_ORG_ID"]}</td>
			</tr>
		</#list>
			</table>
			</div>
		</#if>
		<#list indexTb as indexTb>
			<div class="listBox" style="overflow: auto;margin-top:50px;">
			<table cellpadding="0"  cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">${indexTb.indexName}</caption>
				<tr class="firstTRFont numtd">
					<#list indexItems[indexTb.indexCode] as item>
						<td style="width: 200px;">${item.indexItemName}</td>
					</#list>
					<td style="width: 200px;">归档时间</td>
					<td style="width: 200px;">报送机构</td>
				</tr>
				<#list indexItem[indexTb.indexCode+""] as list>
					<tr>
						<#list list as li>
							<#list indexItems[indexTb.indexCode+""] as item>
									<td style="width: 200px;">${li[item.indexItemCode?upper_case]}</td>
							</#list>
							<td style="width: 200px;">${li["RECORD_DATE"]}</td>
							<td class="orgid" style="width: 200px;">${li["SYS_ORG_ID"]}</td>
						</#list>
						
					</tr>
					
				</#list>
			</table>
			</div>
		</#list>
		${err}
</body>
<script type="text/javascript">
	 var start = {
        elem: '#begin',
        format: 'YYYY-MM-DD',
        //min: laydate.now(), //设定最小日期为当前日期
        max: laydate.now(), //最大日期
        istime: true,
        istoday: true,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas; //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#end',
        format: 'YYYY-MM-DD',
        //min: laydate.now(),
        max: laydate.now(),
        istime: true,
        istoday: true,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);
    
    $(function(){
			var i=0
			$(".orgid").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getOrgName.jhtml",{id:orgId},function(data1){
						 $(".orgid").eq(i).text(data1)
						 i++
					})
			})
    })
    				
</script>
</html>