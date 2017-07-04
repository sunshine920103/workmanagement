<!DOCTYPE html>
<html>
	<head>
	    <meta charset="UTF-8">
		<#include "/fragment/common.ftl"/>
		<title></title>
	</head>
	<body  class="eachInformationSearch">
		<div class="showListBox noBorder">
			<form  method="post" id="ser">
			
			
				<input type="hidden" name="sysComPanyShowId" value="${list.sysComPanyShowId}"/>
				<table cellpadding="0" cellspacing="0">
					<tr>
						<td width="200" class="noBorderL firstTD">统一社会信用代码</td>
						<td id="code" width="400" class="secondTD"><input name="codeCredit" class="inputSty allcreditcodeVal" value="${list.codeCredit}" onblur="onblurVal(this,11,0)"/></td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD">组织机构代码</td>
						<td id="code" class="secondTD"><input name="codeOrg" class="inputSty allorgnumVal" value="${list.codeOrg}" onblur="onblurVal(this,12,0)"/></td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD">企业名称</td>
						<td id="code" class="secondTD"><input name="qymc" class="inputSty allnameVal" value="${list.qymc}"  onblur="onblurVal(this,13,50)" /></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD" >企业状态</td>
						<td >
							<#list dic as di>
								<label for="${di.dicContentId}"><input type="checkbox" class="checke" name="typeId" id="${di.dicContentId}" value="${di.dicContentId}"/>${di.dicContentValue}</label>
							</#list>
						</td>
					</tr>
					<#if list??>
						<tr>
						<td class="noBorderL firstTD" >标识机构</td>
						<td class="orgid">
							${list.sysOrgId}
						</td>
					</tr>
					</#if>
				</table>
			
				<div class="showBtnBox">
					<input type="button" class="cancleBtn marginR30 closeThisLayer" value="取 消" >				
					<input type="button" class="sureBtn" id="sure" value="确 定">
				</div>
			</form>
		</div>
	</body>
	<script type="text/javascript">
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
		$(function(){                    
			var index = parent.layer.getFrameIndex(window.name);
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index);
			});	
		});
		//回显
				
		$(function(){
		var msg = "${msg}";
				if(msg!=""){
					layer.alert(msg,{
						icon:("保存成功"==msg?1:2),
						shade:0.3
					});
				}
				<#if list.dicId??>
					<#list list.dicId?split(',') as i>
						$("#${i}").attr("checked","checked")
					</#list>
				</#if>
			
		})
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
		$("#sure").click(function(){
			var codeCredit=$("input[name='codeCredit']").val();
			var codeOrg=$("input[name='codeOrg']").val();
			var qymc=$("input[name='qymc']").val();
			
			if(codeCredit==""&&codeOrg==""){
				layer.alert("请输入组织机构代码或统一社会信用代码",{icon:2,shade:0.3,shouldClose:true});
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
			if(qymc==""){
				layer.alert("企业名称不能为空",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}
			if(checkChineseNoSpe(qymc)==0){
				layer.alert("企业名称不合法",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}
			var loading = layer.load();
			$.post("${request.getContextPath()}/admin/comPanyShow/save.jhtml",$("#ser").serialize(),function(data){
				layer.close(loading);
				var  index = alertInfoFun(data.message,data.flag,function(){
					parent.window.location.href = "${request.getContextPath()}/admin/comPanyShow/list.jhtml";
					layer.close(index);
				});
			});
		})
	</script>
</html>
