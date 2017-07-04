<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
		<title>已报数据列表</title>
		<script type="text/javascript">
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					alertInfo(msg);
				}
				$(".fuck").show();
				if ($("#key").val() != "") {
                	$(".fuck").hide();
	            }
	            $("#key").focus(function () {
	                $(".fuck").hide();
	            }).blur(function () {
	                if ($.trim($(this).val()) == "") {
	                    $(".fuck").show();
	                }
	            });
	            $(".fuck").click(function () {
	                $("#key").focus();
	            });
				
				
			})
			
			//打开弹窗
			function openPop(num){
	            $("#covered").show();
	            $("#poplayer").show();
	              $("#poplayer").children(".zIndex").removeClass("zIndex");
	            if(num==1){
	                $(".xzzb").hide();
	                $(".xzjg").show();
					$(".xzjg").addClass("zIndex");
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
	            }else if(num==2){
	            	$(".xzjg").hide();
	                $(".xzzb").show();
	                $(".xzzb").addClass("zIndex");
	            }
	        }
			
			
			//关闭上级区域弹出框
	        function closePop(){
	            $("#covered").hide();
	            $("#poplayer").hide();
	        }
			
			//确认选择指标
			function confirmSel2(clear) {
				var seleced = $(".xzzb .seleced");
				closePop();
				var area1 = $(seleced.find("label")).text();
				var str = $(seleced).attr('id');
				
				if(clear == 1||seleced.length==0) {
					$("#openPop2").text("请选择指标大类");
					$("#open2").val("");
				} else {
					$("#openPop2").text(area1);
					$("#open2").val(str)
				}
			
			}
			
			 function selUpstream(obj){
            	$.each($(".seleced"),function(){
              	  $(this).removeClass("seleced");
            	});
            $(obj).addClass("seleced");
        }
			//确认选择机构
			function confirmSel1(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#openPop1").text("请选择机构");
					$("#open1").val("");
				} else { 
					$("#openPop1").text(htext);
					$("#open1").val(hteid); 
				} 
				
				
			}

			
			//删除任务
			function del(obj,cid,name,orgId,method,rid,indexId){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	//关闭确认弹窗
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/reportedDelete/delete.jhtml";
						$.post(url,{cid:cid,name:name,orgId:orgId,method:method,rid:rid,indexId:indexId},function(data){
							//关闭正在操作弹窗
							layer.close(option_index);
							if(data.flag){ 
								layer.alert(data.message,{ icon: 1,  shade: 0.3})
									$(obj).parent().parent().children(".reind").text("已删除");
									$(obj).remove();  
							}else{
								layer.alert(data.message,{icon:2, shade:0.3 });
							}
							
						});
				  	}
			  	});
			}
			
			
			//删除机构类型管理子区域
			function delInstitutionsType(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();

					//递归删除子区域
					delInstitutionsType(pid);
				});
			}
			
		
			
	 
		</script>
	</head>
	<body>
	<form id="searchForm" method="post">
		<input   name="orgId"  type="hidden" value="${orgId}"  />
		<input   name="indexId"  type="hidden" value="${indexId}"  />
		<input   name="beginTime"  type="hidden" value="${beginTime}"  />
		<input   name="endTime"  type="hidden" value="${endTime}"  />
		<input   name="orgCode"  type="hidden" value="${orgCode}"  />
		<input   name="url"  type="hidden" value="${url}"  />
	</form>
		<!--弹出框-->
		<div id="covered"></div>
		<div id="poplayer">
   
		    <div class="borderBox xzjg">
		        <div class="titleFont1">
		            <span>机构列表</span>
		        </div> 
		        <div class="listBox" style="overflow: auto;">
		            	<div class="zTreeDemoBackground left">
							<ul id="treeDemo" class="ztree"></ul>
						</div>
		        </div>
		        <p class="hide" id="hideorg"></p>
		        <div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		        </div>
	
	    	</div>
	    	
	    	<div class="borderBox xzzb">
	        	<div class="titleFont1">
		            <span>指标大类列表</span>
		        </div>
	        	<div></div>
		        <div class="listBox">
		        	<div>
		        		<input id="searchInputIndex" class="inputSty" type="text" value="" style="width: 140px;"/>
		        		<input id="searchBtnIndex" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
		            <table cellpadding="0" cellspacing="0" id="searchTable2" >
		            <#list indexTbList as item>
						<tr>
							<td id="${item.indexId}" onclick="selUpstream(this)">
								<label>${item.indexName}</label>
							</td>
						</tr>
					</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
	        		<input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel2()"/>
	        	</div>
        	</div>
    	</div>
    	
		<div class="rightPart floatLeft eachInformationSearch">
		<form id="searchForm" method="post" action="${request.getContextPath()}/admin/reportedDelete/list.jhtml">
			<div class="listBox" style="margin-bottom: 0px;">
				
			
			<div class="marginT20 marginB10 fontSize12 ">
                <span class=" fontSize12">机构：</span>
                <input type="hidden" name="orgId" id="open1" value="${orgId}" />
                <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">${(orgId??)?string(orgName,'请选择机构')}</a>
                <span class=" fontSize12">指标大类：</span>
                <input type="hidden" name="indexId" id="open2" value="${indexId}" />
                <a id="openPop2" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)">${(indexId??)?string(indexName,'选择指标大类')}</a>
            </div>
            <div class="marginT10">
                	<span class=" fontSize12">归档时间 ：</span>
	                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id ="begin" name="beginTime" value="${beginTime}"> ~
	                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end" name="endTime" value="${endTime}">
          	</div>
          	<div class="queryInputBox" style="margin-top: 10px;margin-bottom: 10px;position: relative;">          		
					 <span class="fuck">企业二码</span>
               		 <input id="key" name="orgCode" class="inputSty" value="${orgCode}" />
               		<input id="orgKey" type="hidden" value="${orgCode}" />
					<input type="submit" id="qruey" class="sureBtn sureBtnEx" value="查  询" style="margin-left: 0px;"/>
					<input type="button" class="sureBtn sureBtnEx" onclick="alldate()" value="批量删除" style="margin-left: 30px;"/>
          	</div>
          		
          	
          	</div>
          	
          	</form>
		</div>
		<div class="eachInformationSearch">
				<div id="min" class="listBox">
					<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">数据删除列表</caption>
						<tr class="firstTRFont">
							<td width="50"><input type="checkbox" onclick="CheckedRev()" ></td>
							<td width="50">序号</td>
							<td width="100">报送形式</td>
							<td width="150">报送模板或指标大类</td>
							<td width="100">报送机构</td>
							<td width="100">归档日期</td>
							<td width="100">操作时间</td>
							<td width="80">状态</td>
							<td width="120">操作</td>
						</tr>
							<#list reportIndexs as it>
								
								<tr>
								
									<td width="50">
										<input type="checkbox" >
										<input type="hidden" value="${it.REPORT_INDEX_ID}">
										<input type="hidden" value="${it.CID}">
										<input type="hidden" value="${it.SYS_ORG_ID}">
										<input type="hidden" value="${indexId}">
										<input type="hidden" value="${it.REPORT_INDEX_TEMPLATE}">
										<input type="hidden" value="${it.REPORT_INDEX_METHOD}">
									</td>
	            					<td>${(1 + it_index) + (page.getPageSize() * page.getCurrentPage())}</td>
									<td>
										<#if it.REPORT_INDEX_METHOD == 0>
											WORD报送
										<#elseif it.REPORT_INDEX_METHOD ==1>
											excel报送
										<#else>
											暂无
										</#if>
										
									</td>
									<td>
										<#if it.REPORT_INDEX_TEMPLATE??>
											${it.REPORT_INDEX_TEMPLATE}
										<#else>
											暂无
										</#if>
									</td>
									<td>${it.REPORT_INDEX_ORG_NAME}</td>
									<td>${(it.REPORT_INDEX_TIME?string("yyyy-MM-dd"))!} </td>
									<td>${(it.REPORT_INDEX_SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!} </td>
									<td class="reind">
										 <#if it.REPORT_INDEX_STATUS==2>
										已删除
										<#else>
										有效
										</#if>
									</td>
									<td>
									<#if orgCode="">
										<#if it.REPORT_INDEX_STATUS==0>
											<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看','${request.getContextPath()}/admin/reportedDelete/show.jhtml?id=${it.REPORT_INDEX_ID}')">查 看</a>	
											<a class="delFont fontSize12 hasUnderline cursorPointer"  onclick="return del(this,'${it.CID}','${it.REPORT_INDEX_TEMPLATE}','${it.SYS_ORG_ID}','${it.REPORT_INDEX_METHOD}','${it.REPORT_INDEX_ID}','${it.INDEX_ID}')">删 除</a>
										<#else>
											<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看','${request.getContextPath()}/admin/reportedDelete/show.jhtml?id=${it.REPORT_INDEX_ID}')">查 看</a>	
										</#if>
									<#else>
										<a class="delFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看详情','${request.getContextPath()}/admin/reportedDelete/select.jhtml?indexId=${indexId}&id=${it.CID}')">查看详情</a>
									</#if>
									</td>
								</tr>
							</#list>
						</table>
						<#if (reportIndexs?? && reportIndexs?size > 0)>
							<#include "/fragment/paginationbar.ftl"/>
						<#else>
							<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
								<tr class="firstTRFont">
									<td style="text-align: center;font-weight: bold;" >暂无信息</td>
								</tr>
							</table>    	
						</#if>
				</div>
			
		</div>
<script>
        var start = {
  			elem: '#begin',
  			format: 'YYYY-MM-DD',
 //			min: laydate.now(), //设定最小日期为当前日期
  			max: laydate.now(), //最大日期
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
	  		max: laydate.now(),
	  		istime: true,
	  		istoday: true,
	  		choose: function(datas){
	    		start.max = datas; //结束日选好后，重置开始日的最大日期
	  		}
		};
		laydate(start);
		laydate(end);
		//搜索按钮
		$("#searchBtnOrg").click(function(){
			var searchVal = $.trim($("#searchInputOrg").val());
				var searchLoading = layer.load();
				var tabel = $("#searchTable1");
				$.post("${request.getContextPath()}/admin/reportedDelete/getOrgByName.jhtml",{name:searchVal},function(data){
					tabel.html("");
					$.each(data,function(i,v){
						var tr = $("<tr></tr>");
						var td = $("<td id='"+v.sys_org_id+"' onclick='selUpstream(this)'><label>"+v.sys_org_name+"</label></td>");
						tr.append(td);
						tabel.append(tr);
					});
					layer.close(searchLoading);
				});
		});
		//搜索按钮
		$("#searchBtnIndex").click(function(){
			var searchVal = $.trim($("#searchInputIndex").val());
				var searchLoading = layer.load();
				var tabel = $("#searchTable2");
				$.post("${request.getContextPath()}/admin/reportedDelete/getIndexByName.jhtml",{name:searchVal},function(data){
					tabel.html("");
					$.each(data,function(i,v){
						var tr = $("<tr></tr>");
						var td = $("<td id='"+v.indexId+"' onclick='selUpstream(this)'><label>"+v.indexName+"</label></td>");
						tr.append(td);
						tabel.append(tr);
					});
					layer.close(searchLoading);
				});
		});
		$("#qruey").click(function(){
			if($("#open2").val()==""){
				layer.alert("请选择指标大类",{icon:2,shade:0.3,shouldClose:true});
                return false;
			}
			var txtSearch = $("#key").val();
			if(checkTChineseM(txtSearch)==0){
				layer.alert("请输入正确的查询条件", {icon: 2, shade: 0.3, shouldClose: true});
	              return false;
			}
			$("#searchForm").submit();
		})
		
		//全选反选
		function CheckedRev(){
		var arr = $(':checkbox'); 
		for(var i=1;i<arr.length;i++){ 
		arr[i].checked = ! arr[i].checked; 
		}
		}
		//多项删除
			
		function alldate(){
			var str=0
			var arr = $(':checkbox'); 
			for(var i=0;i<arr.length;i++){ 
				if(!arr[i].checked){
					str++
				}
			}
			if(arr.length==str){
				layer.alert("未选择删除项",{icon:2,shade:0.3,shouldClose:true});
				return false
			}
			
			var rids=[];
			var cids=[];
			var orgIds=[];
			var indexIds=[];
			var templates=[];
			var methods=[];
			for(var i=0;i<arr.length;i++){
				var arr1="";
				var obj= new Object();
				if(arr[i].checked){
					var str=arr.eq(i).parent().children("input[type='hidden']")
					rid=str.eq(0).val();
					cid=str.eq(1).val();
					orgId=str.eq(2).val();
					indexId=str.eq(3).val();
					template=str.eq(4).val();
					method=str.eq(5).val();
					//arr1+='rid:'+rid+',cid:'+cid+',orgId:'+orgId+',indexId:'+indexId+',template:'+template+',method:'+method
					rids.push(rid);
					cids.push(cid);
					orgIds.push(orgId);
					indexIds.push(indexId);
					templates.push(template);
					methods.push(method);
				}
				
			}
			var tip = "确定要删除吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	//关闭确认弹窗
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/reportedDelete/batchDele.jhtml";
						$.post(url,{rids:rids.join(","),cids:cids.join(","),orgIds:orgIds.join(","),indexIds:indexIds.join(","),templates:templates.join(","),methods:methods.join(",")},function(data){
							//关闭正在操作弹窗
							layer.close(option_index);
							if(data.flag){
								var orgCode=$("#orgKey").val();
								orgCode=$.trim(orgCode);
								if(orgCode==""){
									location.href="${request.contextPath}/admin/reportedDelete/list.jhtml";
								}else{
									//删除页面上的数据
									for(var i=0;i<arr.length;i++){
										if(arr[i].checked){
											arr.eq(i).parent().parent().remove();
										}
									}
								}
								
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
		}
</script>		
	</body>
</html>