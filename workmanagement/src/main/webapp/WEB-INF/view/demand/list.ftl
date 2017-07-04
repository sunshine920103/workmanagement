<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>
		<script type="text/javascript" >
		 
			//关闭上级区域弹出框
	        function closePop(){
	            $("#covered").hide();
	            $("#poplayer").hide();
	        }
			//打开弹窗
			function openPop(num){
	            $("#covered").show();
	            $("#poplayer").show();
	            if(num==0){
	            	$(".jglb").show();
	            	$(".bs").hide();
	            	if($("#treeDemo").html()==""){
	            	var loading = layer.load(); 
					$.ajax({
						url:'${request.getContextPath()}/admin/reportedDelete/getOrgList.jhtml',
						dataType:'json',
						success:function(nodes){
							layer.close(loading);
							var str1 = [];
							for (var i = 0; i < nodes.length; i++) {
								var obj= new Object();
								 obj.id= nodes[i].id;
								 obj.name= nodes[i].name;
								 obj.pId= nodes[i].parent;
								 str1.push(obj);
							}
							orgtype(str1);
						}
					});
					}
	            }
	            if(num==1){
	            	$(".jglb").hide();
	            	$(".bs").show();
	            }
	        }
			//确认选择机构
			function confirmSel1(clear) {
				closePop();
				var htext=$("#hideorg input").val();
				var hteid=$("#hideorg input").attr("id");
				if(clear == 1) {
					$("#openPop1").text("请选择机构");
					$("#open1").val("");
				} else { 
					$("#openPop1").text(htext);
					$("#open1").val(hteid); 
				}  
			}
			//确认选择标识
			function confirmSel2(clear) {
				closePop();
				var che=$(".bs input[type='checkbox']")
				var idstr=""; 
				var num=0;
				for (var i = 0; i < che.length; i++) {
					if(che.eq(i).is(':checked')){
						idstr+=che.eq(i).attr("id")+','
						num++
					}
				}
				 
				if(clear == 1) {
					$("#openPop2").text("请选择指标");
					$("#idstr").val("");
				} else {  
					$("#idstr").val(idstr); 
					$("#openPop2").html("已选择  <span style='color:red;'> "+num+"</span>个指标"); 
				}  
			}
			$(function(){
				if("${typeId}"!=""){
					var str="${typeId}".split(",");
					for (var i = 0; i < str.length; i++) {
						$("#"+str[i]).attr("checked","checked");
					}
					$("#idstr").val("${typeId}"); 
				}
				
				
			})
			
			function exportExcl(){
			<#if (comShow?? && comShow?size>0)>
				var recode= '';
				var recode1 ='';
				var orgIds ='';
				var typeId ='';
				if($("#recode").val()!='')
					recode = $("#recode").val();
				if($("#recode1").val()!='')
					recode1 = $("#recode1").val();
					
				if($("#orgIds").val()!='')
					orgIds = $("#orgIds").val();
					
				if($("#typeId").val()!='')
					typeId = $("#typeId").val();
						window.location.href = "${request.getContextPath()}/admin/demand/reportExcle.jhtml?recode="+recode+"&recode1="+recode1+"&orgIds="+orgIds+"&typeId="+typeId;
					<#else>
					alert("导出数据为空！")
				</#if>
			}
		</script>
		<title>企业标识查询</title>
	</head>
	
	<body class="eachInformationSearch marginT0">
		<form id="searchForm" method="post">
		    <input type="hidden" id="recode" name="recode" value="${recode}" />
		    <input type="hidden" id="recode1" name="recode1" value="${recode1}" />
		    <input type="hidden" id="orgIds" name="orgIds"   value="${orgIds}" />
		    <input type="hidden" id="typeId" name="typeId"  value="${typeId}" />
		</form>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox jglb">
		        <div class="titleFont1">
		            <span>机构列表</span>
		        </div>
		        <div class="listBox" style="overflow: auto;padding:10px;margin:0;">
		            <div class="zTreeDemoBackground left">
		                <ul id="treeDemo" class="ztree"></ul>
		            </div>
		        </div>
		        <p class="hide" id="hideorg">
		        	 <#list identiFication.subOrgs as so>
		                            <span class='org_name'>
		                                <input class='org_name' type='hidden' name='orgIds' value='${so.sys_org_id}' id='${so.sys_org_id}'/>
		                            ${so.sys_org_name}
		                            </span>
		                        </#list>
		        </p>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn hide" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		        </div>
		    </div>
		    <div class="borderBox bs">
		        <div class="titleFont1">
		            <span>企业标识列表</span>
		        </div>
		        <div class="listBox" style="overflow: auto;padding:10px;margin:0;">
		            <ul>
		            <#list iden as ident>
	  				<li>
	  					<input type="checkbox"   id="${ident.sys_identification_id}"   value="${ident.sys_identification_id}"/>
	  					<span>${ident.sys_identification_name}</span>
	  				</li>	
	  				</#list>
	  				</ul>
		        </div>
		     
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn hide" onclick="confirmSel2(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
		        </div>
		    </div>
	    </div>  
		<div class="queryInputBox">
			<form id="form" method="post" action="${request.getContextPath()}/admin/demand/list.jhtml">
			<div class="verticalMiddle" style="margin-left:30px ;">
				归档时间：  
				<input name="recode" id="begin" autocomplete="off" class="laydate-icon inputSty fontSize12" value="${recode}"> ~ 
				<input name="recode1" id="end" autocomplete="off" class="laydate-icon inputSty fontSize12" value="${recode1}">
  					
  					
  					标识：<a id="openPop2" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"  onclick="openPop(1)">请选择标识 </a> 

  	                <input type="hidden" name="typeId" id="idstr" value="" />
  				</div>
			<div class="verticalMiddle" style="margin-left:30px ;margin-top: 20px;">
				<span> 标识机构：<a id="openPop1" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
		                       onclick="openPop(0)">请选择机构
		                    </a>
		        </span>
		   </div>
		    <input type="submit" class="sureBtn sureBtnEx marginL20" value="查 询" style="margin-left: 30px;margin-top:10px;"> <input type="button" onclick="exportExcl()" class="sureBtn sureBtnEx marginL20" value="导 出" style="margin-left: 10px;margin-top:10px;">
			</form>
			<div class="listBox marginT20">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">查询列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="100">序号</td>
			                <td width="200">企业码</td>
			                <td width="200">企业名称</td>
			                <td width="200">联系电话</td>
			                <td width="200">法定代表人</td>
			                <td width="200">注册地</td>
			                <td width="200">标识</td>
						</tr>
						
						<#list comShow as com>
							<tr>
							<td width="100">${com_index+1}</td>
			                <td width="200"><#if com.codeCredit??>${com.codeCredit}<#else>${com.codeOrg}</#if></td>
			                <td width="200">${com.qymc}</td>
			                <td width="200">${com.index_jbxx_lxdh}</td>
			                <td width="200">${com.index_ggxx_xm}</td>
			                <td width="200">${com.index_jbxx_qyzs}</td>
			                <td width="200">
			                <#if com.dicId??>
								<#list iden as di>
									<#list com.dicId?split(',') as i>
										<#if di.sys_identification_id == i>
											${di.sys_identification_name}&nbsp;
										</#if>
									</#list>
								</#list>
							</#if>
			                </td>
						</tr>
						</#list>
					</tbody>
				</table>
				<#if (comShow?? && comShow?size > 0)>
						<#include "/fragment/paginationbar.ftl"/>
					<#else>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>
			</div>
			 
		</div>
		<script type="text/javascript">
			  var start = {
				elem: '#begin',
				format: 'YYYY-MM-DD',
				//min: laydate.now(), //设定最小日期为当前日期
				// max: laydate.now(), //最大日期
				istime: true,
				istoday: true,
				choose: function(datas){
					 end.min = datas; //开始日选好后，重置结束日的最小日期
					 end.start = datas //将结束日的初始值设定为开始日
				}
			};
			var end = {
				elem: '#end',
				format: 'YYYY-MM-DD',
				//min: laydate.now(),
				//max: laydate.now(),
				istime: true,
				istoday: true,
				choose: function(datas){
					start.max = datas; //结束日选好后，重置开始日的最大日期
				}
			};
			laydate(start);
			laydate(end);
		</script>
	</body> 
</html>
