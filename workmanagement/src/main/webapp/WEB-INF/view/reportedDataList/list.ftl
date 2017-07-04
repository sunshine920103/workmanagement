<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
		<title>已报数据列表</title>
	<script type="text/javascript">
		$(function() {
			//回显
			var msg = "${msg}";
			if(msg != "") {
				alertInfo(msg);
				}
			
//			//提示信息
//			if($("#key").val()!=""){
//				$(".fuck").hide();
//			}
//			$("#key").focus(function(){
//				$(".fuck").hide();
//			}).blur(function(){
//				if ($.trim($(this).val()) == "") {
//                  $(".fuck").show();
//              }
//			});			
//			$(".fuck").click(function () {
//              $("#key").focus();
//          });
			
		})
		//状态：无效或有效
		function updataStatus(obj, id, name, reportIndexStatus) {
			var tip;
			if($(obj).attr("name") == "有效") {
				tip = "确定要将状态改为有效嘛？";
			} else {
				tip = "确定要将状态改为无效吗？";
			}
			if(confirm(tip)) {
				var url = "${request.getContextPath()}/admin/reportIndex/updataStatus.jhtml";
				$.post(url, { id: id }, function(data) {
					if(data.status == 1) {
						$(obj).text("");
						$(obj).parent().prev().text("无效");
					} else {
						alertInfo(data.msg);
					}
				});
			}
		}
		
		//打开弹窗
			function openPop(num){
	            $("#covered").show();
	            $("#poplayer").show();
	            if(num==1){
	                $(".xzjg").show(); 
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
	        }
		
		//关闭上级区域弹出框
		function closePop() {
			$("#covered").hide();
			$("#poplayer").hide();
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
	 
			
			
	</script>
	</head>
	<body>
	<form id="searchForm" method="post">
		<input   name="orgId"  type="hidden" value="${orgId}"  />
		<input   name="beginTime"  type="hidden" value="${beginTime}"  />
		<input   name="endTime"  type="hidden" value="${endTime}"  />
		<input   name="indexName"  type="hidden" value="${indexName}"  />
		<input   name="url"  type="hidden" value="${url}"  />
	</form>
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox xzjg">
		        <div class="titleFont1">
		            <span>机构列表</span>
		        </div>
		        <div></div>
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
	         
	    </div>
		<div class="rightPart eachInformationSearch reportExcelTemplateTable">
			<form id="searchForm" method="post" action="${request.getContextPath()}/admin/reportedDataList/list.jhtml">
				<div class="marginL30 marginT20">
					<div>
						<span class="inlineBlock fontSize12">选择机构:</span>
						<input type="hidden" name="orgId" id="open1" value="${orgId}" />
                		<a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">${(orgId??)?string(orgName,'请选择机构')}</a>
					</div>
					<div class="marginT10">
						<span class="inlineBlock fontSize12">归档时间:</span>
						<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id ="begin" name="beginTime" value="${beginTime}"> ~ 
	                	<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end" name="endTime" value="${endTime}">
					</div>
					<div class="marginT10" style="position: relative;">
						<span class="inlineBlock fontSize12">模板名称或指标大类:</span>
						<select name="indexName" class="inputSty">
							<option>请选择</option>
							<#list template as tem>
							<option <#if tem.REPORT_INDEX_TEMPLATE==indexName>selected</#if>>${tem.REPORT_INDEX_TEMPLATE}</option>
							</#list>
						</select>
						<input type="submit"  class="sureBtn sureBtnEx marginL20" value="查 询" style="margin-left: 0px;"/>
					</div>
				</div>
			</form>
				<div id="min" class="listBox" style="margin-top: 10px;">
					<table cellpadding="0" cellspacing="0">
						<tr class="firstTRFont">
							<td style="width:50;">序号</td>
							<td style="width:100;">报送形式</td>
							<td style="width:150;">报送模板或指标大类</td>
							<td style="width:150;">报送机构</td>
							<td style="width:100;">归档日期</td>
							<td style="width:100;">操作时间</td>
							<td style="width:50;">状态</td>
							<td style="width:50;">操作</td>
						</tr>
						<#list reportIndexs as it>
								<tr>
	            					<td>${(1 + it_index) + (page.getPageSize() * page.getCurrentPage())}</td>
									<td>
										<#if it.REPORT_INDEX_METHOD == 0>
											WORD报送
										<#elseif it.REPORT_INDEX_METHOD ==1>
											excel报送
										<#elseif it.REPORT_INDEX_METHOD ==2>
											手工录入
										</#if>
									</td>
									<td>${it.REPORT_INDEX_TEMPLATE}</td>
									<td>${it.REPORT_INDEX_ORG_NAME}</td>
									<td>${(it.REPORT_INDEX_TIME?string("yyyy-MM-dd"))!} </td>
									<td>${(it.REPORT_INDEX_SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!} </td>
									<td>
										<#if it.REPORT_INDEX_STATUS==0>
											有效
										<#elseif it.REPORT_INDEX_STATUS==2>
											已删除
										</#if>
									</td>
									<td>
										<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayer('查看','${request.getContextPath()}/admin/reportedDataList/show.jhtml?id=${it.REPORT_INDEX_ID}')">查 看</a>
									</td>
								</tr>
							</#list>
						</table>
						<table cellspacing="0" cellpadding="0" class="noBorderT">
						<#if (reportIndexs?? && reportIndexs?size > 0)>
							<#include "/fragment/paginationbar.ftl"/>
						<#else>
							<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
								<tr class="firstTRFont">
									<td style="text-align: center;font-weight: bold;" >暂无信息</td>
								</tr>
							</table>    	
						</#if>
						</table>
				</div>
			
		</div>
		<script>
	        var start = {
	  			elem: '#begin',
	  			format: 'YYYY-MM-DD',
	  			//min: laydate.now(), //设定最小日期为当前日期
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
			function test(){
			var searchVal = $.trim($("#searchInputOrg").val());
				var searchLoading = layer.load();
				var tabel = $("#searchTable1");
				$.post("${request.getContextPath()}/admin/reportedDataList/getOrgByName.jhtml",{name:searchVal},function(data){
					tabel.html("");
					$.each(data,function(i,v){
						var tr = $("<tr></tr>");
						var td = $("<td id='"+v.sys_org_id+"' onclick='selUpstream(this)'><label>"+v.sys_org_name+"</label></td>");
						tr.append(td);
						tabel.append(tr);
					});
					layer.close(searchLoading);
				});
			}
	
	</script>
	</body>
</html>