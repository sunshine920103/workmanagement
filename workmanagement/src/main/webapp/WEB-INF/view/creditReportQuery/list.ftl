<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
			var creditCode ='${creditCode}';
			var orgCode = '${orgCode}';
			var industryName = '${industryName}';
			var time = '${time}';
			var querytime ='${querytime}';
			 
			    
			if(creditCode!=''||orgCode!=''||time!=''||industryName!=''){
				var orgId = ${sessionUser.sys_org_id};
					var loading = layer.load();
					$.post("${request.getContextPath()}/admin/menuAdd/getOrgCode.jhtml",{id:orgId},function(data1){
							
							$.post("${request.getContextPath()}/admin/companyInfoQuery/getUrl.jhtml",function(data2){
										layer.close(loading);
										var Time=time.replace(/-/g,"");
										var type = '${t}';
										var url = "";
											if(orgCode==""){
										url=data2+"/bi4/showreport.do?resid=EBI$12$T9NL6YTV2FUS7D09RUB8XB0UV59CUED9$1$"+type+".doc&showparams=true&hideexport=true&id=${sessionUser.username}&pw=${sessionUser.password}&calcnow=true&@tyshxydm="+creditCode+"&@qymc="+industryName+"&@jg="+data1+"&@rq="+Time+"&@dq=${code}&encode=true&@querytime="+querytime;
										
										}else if(creditCode==""){
												url=data2+"/bi4/showreport.do?resid=EBI$12$T9NL6YTV2FUS7D09RUB8XB0UV59CUED9$1$"+type+".doc&showparams=true&hideexport=true&id=${sessionUser.username}&pw=${sessionUser.password}&calcnow=true&@zzjgdm="+orgCode+"&@qymc="+industryName+"&@jg="+data1+"&@rq="+Time+"&@dq=${code}&encode=true&@querytime="+querytime;
										
										}else{	
											url=data2+"/bi4/showreport.do?resid=EBI$12$T9NL6YTV2FUS7D09RUB8XB0UV59CUED9$1$"+type+".doc&showparams=true&hideexport=true&id=${sessionUser.username}&pw=${sessionUser.password}&calcnow=true&@zzjgdm="+orgCode+"&@tyshxydm="+creditCode+"&@qymc="+industryName+"&@jg="+data1+"&@rq="+Time+"&@dq=${code}&encode=true&@querytime="+querytime;
										}
										window.location.href = url;
								});
							
					});
				
			}
	});


	
	

	$(function(){
		
		$("#add").click(function(){
			window.location.href='${request.contextPath}/admin/indexParent/add.jhtml';
		})
		$("#skip").bind("change",function(){ 
		    window.location.href='${request.contextPath}/admin/indexParent/add.jhtml?pageNo='+$(this).val()+'';
	  }); 
	  	var province_2 = '${pageUtil.pageNo}';
		$("#skip option[value='"+province_2+"']").attr("selected",true);
		//标红处理
//		var content = $("#code").val();
//      setFlag($(".redMark"),content);
	$("#closeBut").click(function(){
	$("table input").each(function(){
		$(this).val("")
	})
})

//组织机构代码隐藏
        $(function(){
        	var str="${pass}";
        	if(str=="0"){
        		$("#orgcode1").hide();
        	}
        });
		
		//提交验证
		$("#submitBtn").click(function(){
			var orgId = ${sessionUser.sys_org_id};
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/creditReportQuery/getLimit.jhtml",{id:orgId},function(data){
				layer.close(loading);
				if(data!=""){
					layer.alert(data,{icon:2,shade:0.3,shouldClose:true});
					return false;
				}
				var creditCode = $("input[name='creditCode']").val();
				var orgCode = $("input[name='orgCode']").val();
				var industryName = $("input[name='industryName']").val();
				var time = $("input[name='time']").val();
				var serviceName = $("input[name='serviceName']").val();
				var phone = $("input[name='phone']").val();
				
				
				if(creditCode==""&&orgCode==""){
					layer.alert("请输入组织机构代码或统一社会信用代码",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}else if(creditCode==""&&orgCode!=""){
					if(testOrgCode(orgCode) == 0 ){
						layer.alert("组织机构代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
						return false;
					}
				}else if(creditCode!=""&&orgCode==""){
					if(regCreditCode(creditCode) == 0 ){
						layer.alert("统一社会信用代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
						return false;
					}
				}else if(creditCode!=""&&orgCode!=""){
					if(testOrgCode(orgCode) == 0 ){
						layer.alert("组织机构代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
						return false;
					}
					if(regCreditCode(creditCode) == 0 ){
						layer.alert("统一社会信用代码格式不正确",{icon:2,shade:0.3,shouldClose:true});
						return false;
					}
				}
	
					
 
				if(checkChineseNoSpe(industryName)==0){
					 layer.alert("企业名称不合法",{icon:2,shade:0.3,shouldClose:true});
				     return false;  
				}
 
				if(time == ""){
					layer.alert("时间不能为空",{icon:2,shade:0.3,shouldClose:true});
	            	$("input[name='time']").focus();
	            	return false;
				}
				<#if cred== 1>
				var reg=/(.*).(jpg|bmp|gif|pdf|jpeg|png)$/; 
				var operateAuthFile=$("#file").val()
					if(operateAuthFile == ""){
						layer.alert("授权文件不能为空",{icon:2,shade:0.3,shouldClose:true});
	            	 return false;
					}
					
					if(!reg.test(operateAuthFile)) {
						layer.alert("请上传'.pdf'、'.jpg'、'.jpeg'、'.gif'、'.png'或'.bmp'格式文件文件",{icon:2,shade:0.3,shouldClose:true});
					     return false;  
					}
				</#if> 
				
				$("#userForm").submit();
				layer.load();
			});
			
		});
			
		
	});
        
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
</script>
<title>信用报告查询</title>
</head>
<body class="eachInformationSearch">
		<div class="showListBox noBorder">
			<form id="userForm" name="form" enctype="multipart/form-data" action="${request.getContextPath()}/admin/creditReportQuery/search.jhtml" method="post">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">信用报告查询</caption>
					<tbody>
					<tr >
						<td class="noBorderL firstTD" width="400">统一社会信用代码</td>
						<td width="400"><input class="inputSty allcreditcodeVal" id="creditCode" name="creditCode" value="${cre}" onblur="onblurVal(this,11,0)" type="text" maxlength="18" onKeyDown="if(this.value.length > 18){ return false }"/></td>
					</tr>
					<tr id="orgcode1" >
						<td class="noBorderL firstTD">组织机构代码</td>
						<td><input class="inputSty allorgnumVal" id="orgCode" name="orgCode" type="text" value="${org}" onblur="onblurVal(this,12,0)" maxlength="10" onKeyDown="if(this.value.length > 10){ return false }"/></td>
					</tr>
					<tr >	
						<td class="noBorderL firstTD">企业名称</td>
						<td><input class="inputSty allnameVal" id="industryName" name="industryName"  value="${qymc}" type="text" onblur="onblurVal(this,13,50)" maxlength="50" onKeyDown="if(this.value.length > 50){ return false }"/></td>
					</tr> 
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>时间</td>
						<td><input class="laydate-icon inputSty" id="time" name="time" autocomplete="off" onclick="laydate({istime: false, format: 'YYYY-MM-DD'})" value=""/></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>信用报告类型</td>
						<td>
							<select name="t" id ="type" class="inputSty">
								<#list type as ty>
									<option  value="${ty.FILENAME_}">${ty.CAPTION_}</option>
								</#list>
							</select>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>查询原因</td>
						<td>
							<select name="reason" class="inputSty">
							<#list dic as d>
									<option value="${d.dicContentCode}">${d.dicContentValue}</option>
								</#list>
							</select>
						</td>
					</tr>
					<#if cred == 1>
						<tr>
							<td class="noBorderL firstTD"><label class="mainOrange"> * </label>授权文件</td>
							<td><input type="file" id="file" name="file" class="inputSty" value="" /></td>
						</tr>
					</#if>
					<input type="hidden" name = "code" value ="${code}"/>
					</tbody>
				</table>
				<div class="showBtnBox"  style="margin-top: 10px;margin-bottom: 10px;">
					<input id="closeBut" type="button" class="cancleBtn" value="清 空"/>
					<input id="submitBtn" type="button" class="sureBtn" value="查 询"/>
				</div>
			</form>
		</div>
</body>
</html>