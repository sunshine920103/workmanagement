<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
		//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
			var loading;
			//回显
			var msg = "${msg}";
			if(msg != "") {
//				alertInfo(msg);
				layer.alert(msg,{
					icon: (msg=="操作成功")?1:2,
					shade:0.3,
					shadeClose:true
				});
				layer.close(loading);
			}
//			layer.alert(msg, {
//					shade:0.3,
//				    time: 5000, //20s后自动关闭
//				    btn: ['确定', '取消'],
//				    yes: function(index){
//				    	layer.close(index);
//				  	}
//			  	});

			//全部导出
			function downAll(){
				window.open('${request.getContextPath()}/admin/dicExchangeLast/exportAll.jhtml?qb=${qb}+&startTime=${startTime}+&endTime=${endTime}+&sysAreaId=${sysAreaId}');
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
		//确认选择
		function confirmSel(obj){
			var seleced = $(".seleced");
			//获取选择地区的id 
			if(seleced.length==0){
				alert("您还没有选择上级区域");
				$(obj).blur();
			}else{
				closePop();
				var area = $(seleced.find("label"));
				var areaid = $(seleced.find("td"));
				$("#openPop").text(area.text());
				$("#areaId").val(seleced[0].id);
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
		//选择地区
		function selUpstream(obj){
			      $.each($(".seleced"),function(){
			          $(this).removeClass("seleced");
			      });
			      $(obj).addClass("seleced");
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
								var area = subs[i];
								//展开图标
								var icon = "";
								if(area.subArea!=null && area.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+area.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var tr = $("<tr name='"+area.sysAreaId+"' class='"+id+"'></tr>");
								var name = null;
								
								if(area.sysAreaType==="3"){
									name = "<td id='"+area.sysAreaId+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+area.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ area.sysAreaType +"' />"
											+"</td>";
								}else{
									name = "<td id='"+area.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+area.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ area.sysAreaType +"' />"
											+"</td>";
								}
								
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
				//删除地区管理子区域
				function delSubArea(id){
					$.each($("."+id),function(i, v){
						var pid = v.attributes.getNamedItem("name").nodeValue;
						
						//删除子区域
						$(this).remove();
				
						//递归删除子区域
						delSubArea(pid);
					});
				}
			//下载
			function download(){
		        var url='${request.getContextPath()}/admin/dicExchangeLast/downLoad.jhtml';
		        window.open(url);
		    }
		    	 $(function(){
				if($("#err").val()!=0){
					layer.open({
 					 type: 1,
 					 skin: 'layui-layer-demo', //加上边框
 					 area: ['420px', '240px'], //宽高
 					  closeBtn: 0, //不显示关闭按钮
 						anim: 2,
  
  					shadeClose: true, //开启遮罩关闭
 					 
 					 content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgString as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
					});
				}
			 })
			//上传
			
			$(function(){
				$("#upload").submit(function(){
					var fileName=$("#file").val();
					if(fileName == "") {
				        layer.alert("请选择excel文件", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				        return false;
				    }
					
					var reg=/(.*).(xls|xlsx)$/; 
				 
						if(!reg.test(fileName)) {
					        layer.alert("文件不是excel格式", {
								icon: 2,
								shade: 0.3,
								shadeClose: true
							});
					        return false;
					   } 
					loading = layer.load();
				});
				//关闭错误列表
				$("#closeBut").click(function(){
					$("#errorDiv").hide();
				})
				
			});
		</script>
		<title>汇率维护列表</title>
	</head>
	<body>
		<!-- 描述：弹出框 -->
		<#-- 弹出框 -->
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="borderBox">
	        	<div class="titleFont1">区域列表</div>
	        	<div class="listBox" style="margin: 0px;" >
	        		<table cellpadding="0" cellspacing="0" style="border:none;">
	        		 <#list areaList as area>
						<tr>
							<td style="font-size:12px" id="${area.sysAreaId}"  onclick="selUpstream(this)">
								<#if (area.subArea?? && area.subArea?size > 0) >
									<div id="0" class="open-shrink" onclick="openArea(this, ${area.sysAreaId})">
										<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
									</div>
								</#if>
								<label class="fontSize12">${area.sysAreaName}</label>
							</td>
						</tr>
					</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消" class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认" class="sureBtn" onclick="confirmSel(this)"/>
	        	</div>
	        </div>  
	    </div>
		<div  class="eachInformationSearch">
		<form id="searchForm" method="post">
			<input   name="startTime"  type="hidden" value="${startTime}"  />
			<input   name="endTime"  type="hidden" value="${endTime}"  />
			<input   name="sysAreaId"  type="hidden" value="${sysAreaId}"  />
			<input   name="qb"  type="hidden" value="${qb}"  />
		</form>
		<#if msgString??>
			<input type="hidden" id="err" value="${msgString?size} " />
		<#else>
			<input type="hidden" id="err" value="0" />
		</#if>
		<#if reportIndexErrorList?? !>
			<div id="errorDiv" class="showListBox" style="width: 80%;">
				<table cellpadding="0" cellspacing="0">
							<caption class="titleFont1 titleFont1Ex">导入失败，请修正后操作</caption>
							<tr class="firstTRFont">
								<td width="80">序号</td>
								<td width="120">有误项名称</td>
								<td width="100">上报值</td>
								<td width="200">说明</td>
							</tr>
							<#list reportIndexErrorList as item>
								<tr>
									<td>${item_index+1}</td>
									<td>${item.reportIndexErrorName}</td>
									<td>${item.reportIndexErrorValue}</td>
									<td>${item.reportIndexErrorNotes}</td>
								</tr>
							</#list>
				</table>
				<div class="showBtnBox">
					<input id="closeBut" type="text" class="sureBtn textCenter" value="关 闭"/>
				</div>
			</div>
		</#if>
		
		<div>
			<div class="queryInputBox" style="margin-bottom:10px">
				<div class="marginT10">			     	
					<form id="form" action="${request.getContextPath()}/admin/dicExchangeLast/list.jhtml" method="post" style="display: inline-block;*zoom:1;*display:inline;position:relative;margin-left:30px"  >
						操作地区：
						<#if sysAreaName??>
                			<a id="openPop" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()" style="margin-right: 30px;">${sysAreaName}</a>
                		<#else>
                			<a id="openPop" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()" style="margin-right: 30px;">请选择区域</a>
                		</#if>
                		<input type="hidden"  id="areaId"  name="sysAreaId" value=""/>

						更新时间：
						<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="begin"  name="startTime" value="${startTime}"> 至
                		<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end" name="endTime" value="${endTime}"> 
					   	<input type="submit" class="sureBtn" value="查  询" style="margin-left:0px;" />
					</form> 
					<form id="form" action="${request.getContextPath()}/admin/dicExchangeLast/exportAll.jhtml?" method="post" style="display: inline-block;*zoom:1;*display:inline;">
 			     	</form>	
 			     		<input  type="button" onclick="downAll()"  value="全部导出" class="sureBtn sureBtnEx" style="margin-left: 20px;"/>
				  
				</div>
				<div>
					
					<span class="warmFont inlineBlock fontSize12 marginL30 marginT10">注：汇率为币种与人民币的比值，如果要导入的币种不存在列表中，则无法导入，请增加后再操作。</span>
				</div>
				
			</div>
			
			<div class="listBox">
				<table>
					<caption class="titleFont1 titleFont1Ex">汇率列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="50">序号</td>
							<td width="80">币种名称</td>
							<td width="100">汇率</td>
							<td width="100">代码</td>
							<td width="100">最后更新时间</td>
							<td width="100">所属区域</td>
						</tr>
						<#list dicExchangeLastList as item>
							<tr>
			            		<td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
								<td>${item.dicExchangeName}</td>
								
								<td>${item.dicExchangeValue}</td>
								<td>${item.dicExchangeCode}</td>
								<td>${item.dicExchangeTime?substring(0,10)} </td>
								<td>${item.sysAreaName} </td>
							</tr>
						</#list>
					</tbody>
				</table>
				<#if (dicExchangeLastList?? && dicExchangeLastList?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>
				</#if>
				
			</div>
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
		</script>
	</body>
</html>
