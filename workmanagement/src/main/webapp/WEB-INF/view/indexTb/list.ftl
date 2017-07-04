<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		var msg = "${msg}";
		if(msg != "") {
            layer.alert(msg);
		}
		$("#skip").bind("change",function(){ 
		    window.location.href='${request.contextPath}/admin/indexTb/add.jhtml?pageNo='+$(this).val()+'';
	  }); 
	  	var province_2 = '${pageUtil.pageNo}';
		$("#skip option[value='"+province_2+"']").attr("selected",true);
		var index= "${indexTb.indexId}";
		$("#"+index).attr("style","color:rgb(56,165,226);");
		
		//获取高度
		$("#max").height($(window).height()-50);
			
		if($("#search").val()!=""){
			$(".fuck").hide();
		}
		$("#search").focus(function(){
			$(".fuck").hide();
		}).blur(function(){
			if ($.trim($(this).val()) == "") {
                 $(".fuck").show();
            }
		});
		$(".fuck").click(function () {
            $("#search").focus();
       });
	
	});
	//指标置为无效操作
	function invalidIndex1(indexId,sys_org_id,obj){
		var nowOrg ="${sysOrg.sys_org_id}";
		var a = $(obj).parent().prev().prev().text();
		if(nowOrg!=sys_org_id){
			layer.alert("该指标不是当前机构所创建，您无权将此指标置为无效",{icon:2,shade:0.3,shouldClose:true});
		}else {
			if(a == "有效"){			
				layer.confirm("确认要将此指标大类置为无效？",{btn: ['确定','取消']},function(){
					layer.msg("已将该指标大类置为无效",{icon:1});
					$(obj).text("置为有效");
					$(obj).parent().prev().prev().text("无效");					
					$.post("${request.contextPath}/admin/indexTb/change.jhtml", {'indexId': indexId,'a':a}, function(data){
						if(data.message != ""){
							layer.alert(data.message);
							layer.close(loading);
							parent.window.location.href = "${request.getContextPath()}/admin/indexTb/list.jhtml";
						}
					});
					
				});				
			}else if(a=="无效"){
				layer.confirm("确认要将此指标大类置为有效？",{btn: ['确定','取消']},function(){
					layer.msg("已将该指标大类置为有效",{icon:1});
					$(obj).text("置为无效");
					$(obj).parent().prev().prev().text("有效");				
					
					$.post("${request.contextPath}/admin/indexTb/change.jhtml", {'indexId': indexId,'a':a}, function(data){
						if(data.message != ""){
							layer.alert(data.message);
							layer.close(loading);
							parent.window.location.href = "${request.getContextPath()}/admin/indexTb/list.jhtml";
						}
					});
				});
			}
		}	
	}
	
	function isAddIndex(obj){
			setLayer('新增指标大类','${request.getContextPath()}/admin/indexTb/add.jhtml');
			$(obj).blur();
	}
	
	function delIndex(indexId,obj){
				layer.alert("确定要删除该指标吗", {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	//关闭确认弹窗
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
						$.post("${request.contextPath}/admin/indexTb/delIndex.jhtml", { indexId: indexId}, function(data){
							close_wait(option_index);
							if(data.flag){  
									//删除页面上的数据
									$(obj).parent().parent().remove();
							} 
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						})
					}
				});
				return false;
	}
	
	
	
	
	//指标项置为无效操作
	function invalidIndex(indexItemId,indexId,indexOrg){
		var nowOrg ="${nowOrg}";
		if(nowOrg!=indexOrg){
			layer.alert("您无权将此指标大类置为无效",{icon:2,shade:0.3,shouldClose:true});
		}else{
			var bool = confirm("确认要将此指标项置为无效？该操作不能恢复",'提示');
			
			if(bool){
				$.post("${request.contextPath}/admin/indexTb/submit.jhtml", { indexId: indexId,indexItemId: indexItemId}, function(result){
					if(result.message != ""){
						layer.alert(result.message);
						if(result.message != "该指标项的指标大类已经无效，不允许此操作"){
							$("#indexItemInvalid").remove();
							$("#"+ indexItemId).html("无效");
						}
					}
				});
			}
		}
	}
		window.onresize = function(){
			$("#max").height($(window).height()-50);
			$('.layui-layer-shade').height($(window).height());
		} 
			
		//显示上级区域弹出框
			function openArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
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
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								
								var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
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

		//显示上级区域弹出框
			function openPop(){
				$("#covered").show();
				$("#poplayer").show();
			}
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			//选择上级区域
			function selUpstream(obj){
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
			//确认选择
			function confirmSel(obj){
				var seleced = $(".seleced");
				if(seleced.length==0){
					alert("您还没有选择上级区域");
					$(obj).blur();
				}else{
					closePop();
					var area = $(seleced.find("label"));
					$("#openPop").text(area.text());
					$("input[name='aId']").val(seleced.attr("id"));
				}
			}
</script>
		<title>指标列表</title>
	</head>
	<body>

	<#-- 弹出框 -->
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox">
	        	<div class="titleFont1">区域列表</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0">
						<tr>
							<td style="font-size:12px" id="${area.sysAreaId}" onclick="selUpstream(this)">
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label class="fontSize12">${area.sysAreaName}</label>
							</td>
						</tr>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel(this)"/>
	        	</div>
	        </div>  
	    </div>
		<!--查询指标大类  -->
		<div id="more" class="noBorder">
			<div class="verticalMiddle marginT20 marginL30">
				<form id="mohuSearch" action="${request.contextPath}/admin/indexTb/search.jhtml" method="post" style="position: relative;">
					<input type="hidden" value="" name="aId"/>
					<#--<#if area.sysAreaId ==1>
					选择区域：<a id="openPop" href="javascript:void(0);" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()">请选择区域</a>					
					</#if>-->
					<div style="position: relative;display: inline-block;*display: inline;*zoom: 1;">
						<span class="fuck">请输入指标大类名称</span>
						<input  id ="search" type="text"  name="words"  class="inputSty"/>					
						<input type="button" id="inquire" class="sureBtn sureBtnEx"  value="查 询" >
						<#--<input id="addmin" class="bigBtn" type="button" value="新增指标大类" onclick="isAddIndex(this)"/>-->
					</div>
					<input type="hidden" name="queryProductTemplateAreaId" value="${indexTb.indexRegionId}" />
				</form>
			</div>
		</div>
		
		<!-- 默认获取的一个指标项 列表      -->
		<div class="eachInformationSearch">
		<div id="min" class="listBox">
			<table cellpadding="0" cellspacing="0">
			<caption class="titleFont1 titleFont1Ex">指标大类列表</caption>
				<tr class="firstTRFont">
					<td width="150">指标大类名称 </td>
                    <td width="150">识别码</td>
					<td width="150">所属区域</td>
					<td width="80">指标状态</td>
					<td width="100">指标类型</td>
					<td width="140">操作</td>
				</tr>
				<#list list as li>
                <#if li.indexName=='行政处罚信息' || li.indexName=='行政许可信息'|| li.indexName=='基本信息'>
				<tr>
					<td width="" class="hide">${li.indexId}</td>					
					<#if li.sysAreaName == "四川省">
					<td width="">${li.indexName}</td>
					<#else >
					<td width="">${li.sysAreaName}-${li.indexName}</td>
					</#if>			
					<td width="">${li.indexCode}</td>
					<td width="">${li.sysAreaName}</td>
					<#if li.indexUsed == 0>
						<td  width="">无效</td>
					<#else>
						<td  width="">有效</td>
					</#if>
					<#if li.indexType == 0>
						<td width="">基本信息</td>
					<#else>
						<td width="">业务信息</td>
					</#if>
					<td width="">
						<a class="changeFont fontSize12 hasUnderline cursorPointer" onclick="setLayerall('查看指标详情','${request.contextPath}/admin/indexTb/index.jhtml?indexId=${li.indexId}')">查看</a>
					<#if li.indexCode !="index_jbxx">	
						<a class="delFont fontSize12  hasUnderline " onclick="invalidIndex1(${li.indexId},${li.sys_org_id},this)">置为${(li.indexUsed==0)?string("有效","无效")}</a>
						<a class="delFont fontSize12  hasUnderline " onclick="delIndex(${li.indexId},this)">删除</a>				
					<#else>				
					</#if>		
					</td>
				</tr>
                </#if>
				</#list>
			</table>
			<#include "/fragment/paginationbar.ftl"/>
		</div>
		</div>
		<form id="searchForm" method="post">
			<input  id ="hidArea" name="aId"  type="hidden" value="${area.sysAreaId}"  />
			<input  id ="hidwords" name="words"  type="hidden" value="${words}"  />
		</form>
	</body>
	<script type="text/javascript">
		$(function(){
			$(".invalid").click(function(){
				$(this).siblings(".valid").show();
				$(this).hide();
			})
			$(".valid").click(function(){
				$(this).siblings(".invalid").show();
				$(this).hide()
			})
			
			
			$("#inquire").click(function(){
				var search=$("#search").val();
				if(checkTChineseM(search)==0) {
					layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
	                return false;
		    	} 
				$("#mohuSearch").submit()
			})
		})
		
	
	</script>
</html>