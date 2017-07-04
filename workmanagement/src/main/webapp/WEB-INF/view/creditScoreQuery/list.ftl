<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">


//组织机构代码隐藏
   $(function(){
        	var str="${pass}";
        	if(str=="0"){
        		$("#orgcode1").hide();
        	}
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
	
	
		//打开弹窗
		function openPop(){
		    $("#covered").show();
		    $("#poplayer").show(); 
		    $("li").remove( ) 
		    var html=''
		    $.post("${request.getContextPath()}/admin/NewlyIncreased/getGroup.jhtml",function(data1){
		    	
		    	 	for(var i =0;i<data1.length;i++){
		    	 		html+='<li id="'+data1[i].sys_importent_enterprise_group_id+'" onclick="selUpstream(this)"> <span>'+data1[i].sys_importent_enterprise_group_name+'</span> </li>'
		    	 		
		    	 	}
		    	 	
		    	 	
		    	 
					
		    	 	$("ul").append(html)
		    });
		    
		}
		//关闭上级区域弹出框
		function closePop(){
			$("#covered").hide();
			$("#poplayer").hide();
		}
		function selUpstream(obj){
			$.each($(" .seleced"),function(){
				$(this).removeClass("seleced");
			});
			$(obj).addClass("seleced");
		}
		function confirmSel2(clear){
			var seleced = $(" .seleced");
				closePop();
			var area = $(seleced.find("span")).text();
			var str = $(seleced).attr("id");
			if(clear==1||seleced.length==0){
				$("#openPop1").text("请选择企业群");	 
				
			}else{
				$("#openPop1").text(area);  
				  $.post("${request.getContextPath()}/admin/NewlyIncreased/getGroupId.jhtml",{id:str},function(data1){
				  	
				  		var num1="";
				  		for(var i =0;i<data1.length;i++){
				  			for(var j =0;j<data1[i].default_index_item_id.length;j++){
				  			
				  				
				  				if(data1[i].default_index_item_id[j].codeCredit==null || data1[i].default_index_item_id[j].codeCredit==""){
				  					
				  					num1+=data1[i].default_index_item_id[j].codeOrg+"|";
				  				}else{
				  					
				  					num1+=data1[i].default_index_item_id[j].codeCredit+"|";
				  				} 
				  			}
				  			
				  		
				  		}  
				  		$("#zzjgdm").val(num1);
				  });
			}
					
		}
		
		
	$(function(){
		$("#change").change(function(){
			$("#creditCode").val("");
			$("#orgCode").val("");
			$("#openPop1").text("请选择企业群") 
			$("tr").show()
			if($("#change").val()==0){ 
				$("tr").eq(4).hide() 
			}else if($("#change").val()==1){
				$("tr").eq(3).hide()
				$("tr").eq(2).hide()
			}
		})
	})
</script>
<title>信用报告查询</title>
</head>
<body class="eachInformationSearch">
<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">
		    <div class="borderBox">
		        <div class="titleFont1">
		            <span>企业群</span>
		        </div>
		        <div class="listBox" style="overflow: auto;padding:10px 0;">
		           
			     <ul>       
	  			</ul>
		        </div> 
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/> 
		            <input type="button" value="重 置" class="resetBtn hide" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
		        </div>
		    </div>
	    </div>  
		<div class="showListBox noBorder">
				<table cellpadding="0" cellspacing="0">
					<tbody>
					<tr>
						<td width="200" class="noBorderL firstTD">选择类型</td>
						<td width="200">
							<select   id="change" class="inputSty"> 
									<option  value="0">单企业查询</option>
								 	<option  value="1">批量企业查询</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>信用评分类型</td>
						<td>
							<select name="t" id ="type" class="inputSty">
								<#list type as ty>
									<option  value="${ty.FILENAME_}">${ty.CAPTION_}</option>
								</#list>
							</select>
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD" width="400">统一社会信用代码</td>
						<td width="400"><input class="inputSty allcreditcodeVal" value="${cre}" id="creditCode" name="creditCode" onblur="onblurVal(this,11,0)" type="text" maxlength="18" onKeyDown="if(this.value.length > 18){ return false }"/></td>
					</tr>
					<tr id="orgcode1" >
						<td class="noBorderL firstTD">组织机构代码</td>
						<td><input class="inputSty allorgnumVal" id="orgCode" value="${org}" name="orgCode" type="text"  onblur="onblurVal(this,12,0)" maxlength="10" onKeyDown="if(this.value.length > 10){ return false }"/></td>
					</tr>
					<tr class="hide">
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>企业群</td>
						<td><a id="openPop1" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop();"   >请选择企业群 </a>
		                </td>
					</tr> 
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>评分时间</td>
						<td><input class="laydate-icon inputSty" id="stime"    autocomplete="off" onclick="laydate({istime: false, format: 'YYYY-MM-DD'})" value=""/></td>
					</tr>
					
					<tr>
						<td class="noBorderL firstTD">计划执行时间</td>
						<td><input class="inputSty" id="settime" name="time" autocomplete="off" onclick="laydate({istime: true,istoday: false, format: 'YYYY-MM-DD hh:mm:ss'})" value=""/></td>
					</tr>
					
				</tbody>
				</table>
				<div class="showBtnBox"  style="margin-top: 10px;margin-bottom: 10px;">
					<input id="closeBut" type="button" class="cancleBtn" value="清 空"/>
					<input id="submitBtn" type="button" class="sureBtn" value="查 询"/>
				</div>
				<form  action="${url}/bi4/scheduletask.do" method="post"> 
					<input type="hidden" name="rptid" id="rptid"  value="" />
					<input type="hidden" name="@rq" id="rq"  value="" />
					<input type="hidden" name="@qywydm" id="zzjgdm"  value="" /> 
					<input type="hidden" name="time" id="time"  value="" />
					<input type="hidden" name="id" id="id"  value="" />
					<input type="hidden" name="pw" id="pw"  value="" />
					<input type="hidden" name="@jg" id="jg"   value="" />
					<input type="hidden" name="@dq" id="dq"  value="" />
					<input type="hidden" name="oprtime" id="oprtime" value="" />
					<input type="hidden" name="action" id="" value="add" />
				</form>
		</div>

	
</body>
<script type="text/javascript"> 
	$(function(){ 
			var orgId = ${sessionUser.sys_org_id};
				$.post("${request.getContextPath()}/admin/menuAdd/getOrgCode.jhtml",{id:orgId},function(data1){  
					$("#jg").val(data1) 
				}) 
		$("#submitBtn").click(function(){ 
			var creditCode=$("#creditCode").val(),
				orgCode=$("#orgCode").val(),
				stime=$("#stime").val(),
				settime=$("#settime").val(),
				orgCode=$("#orgCode").val(),
				type=$("#type").val();
				if(settime!=''){
					settime = new Date(settime).getTime();
				}
			var sTime=stime.replace(/-/g,"");
				$("#rptid").val("EBI$12$T9NL6YTV2FUS7D09RUB8XB0UV59CUED9$1$"+type+".rpttpl");
				$("#rq").val(sTime); 
				$("#time").val(settime); 
				$("#id").val("${sessionUser.username}") 
				$("#pw").val("${sessionUser.password}") 
				$("#dq").val("${code}");
				$("#oprtime").val(new Date().getTime())
				
				
				
				if($("#change").val()==0){
					if(creditCode != "" ){
						$("#zzjgdm").val(creditCode); 
					}else if(orgCode != ""){
						$("#zzjgdm").val(orgCode); 
					}
			 
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
				}else{ 
					if($("#openPop1").text()=="请选择企业群"){
						layer.alert("选择企业群",{icon:2,shade:0.3,shouldClose:true});
						return false;
					}
				}
				if(stime == "" ){
						layer.alert("评分时间不能为空",{icon:2,shade:0.3,shouldClose:true});
						return false;
				}
		
			$("form").submit() 
		})
	})
</script>
</html>