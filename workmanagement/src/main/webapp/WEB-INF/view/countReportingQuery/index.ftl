<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <#include "/fragment/common.ftl"/>
    <style>
        body{
            font-family: "微软雅黑";
            padding: 20px;
            margin: 0;
            font-size: 14px;
        }
        p,img,ul,li{
            margin: 0;
            padding: 0;
        }
        select{
            margin-right: 5px;
        }
        .queryBtn{
            width: 120px;
            height: 30px;
            display: block;
            margin: 20px auto;
            cursor: pointer;
            border: none;
            background-color: rgb(56,165,226);
            color: white;
            border-radius: 4px;
        }
    </style>
    
    
    <script type="text/javascript" >
    
    	$(function(){
    		//回显
    		var msg = "${msg}";
    		if(msg!==""){
    			alertInfo(msg,false);
    		}
    		
    		//初始化日期
    		start = {
			  	elem: '#start',
			  	format: 'YYYY-MM-DD',
			    max:laydate.now(),
				istime:false,
				isclear:true,
				istoday:true,
				festival:true,
				fixed: false,
			  	choose: function(datas){
			     	end.min = datas; //开始日选好后，重置结束日的最小日期
			     	end.start = datas //将结束日的初始值设定为开始日
			  	}
			};
			end = {
			    elem: '#end',
			    format: 'YYYY-MM-DD',
			    max:laydate.now(),
				istime:false,
				isclear:true,
				istoday:true,
				festival:true,
				fixed: false,
			    choose: function(datas){
			    	start.max = datas; //结束日选好后，重置开始日的最大日期
			  	}
			};
			laydate(start);
			laydate(end);
    	});
    
		//显示统计区域弹出框
		function openPop(){
			$("#covered").show();
			$("#poplayer").show();
			//判断浏览器是否为IE6  IE6  我日你妈
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
				    $(".shouldHide").hide();
				}
		}
		
		//关闭统计区域弹出框
		function closePop(){
			$("#covered").hide();
			$("#poplayer").hide();
			//判断浏览器是否为IE6  IE6  我日你妈
				if(navigator.userAgent.indexOf("MSIE 6.0") > 0)
				{
				    $(".shouldHide").show();
				}
		}
		
		//选择统计区域
		function selUpstream(obj){
			$.each($(".seleced"),function(){
				$(this).removeClass("seleced");
			});
			$(obj).addClass("seleced");
		}
		
		//确认选择
		function confirmSel(){
			var seleced = $(".seleced");
			if(seleced.length==0){
				layer.alert("您还没有选择统计区域",{
					icon:2,
					shade:0.3
				});
			}else{
				var selId = seleced.attr("id");
				closePop();
				var area = $(seleced.find("label"));
				var areaName = area.text();
				$("#openPop").text(areaName);
				$("#area").val(selId);
				$("#areaName").val(areaName);
			}
		}
		
		function openArea(obj, id){
			obj = $(obj);
			//子区域的缩进
			var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 32 + "px";
			
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
								icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sys_area_id+')">'
									   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
									   +'</div>'
							}
							
							
							var tr = $("<tr name='"+sub.sys_area_id+"' class='"+id+"'></tr>");
							var name = "<td id='"+sub.sys_area_id+"' onclick='selUpstream(this)' style='border: none;padding-left:"+spacing+"'>"
										+icon
										+"<label class='fontSize12'>"+sub.sys_area_name+"</label>"
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
		
		//查询
		var waitIndex;
		function searchForm(){
			var begin = $("#start").val();
			
			var end = $("#end").val();
			var endTime = $("#end").filter(":visible").length;
			
			var area = $("#area").val();
			var areaTr = $("#areaTr").filter(":visible").length;
			
			
			if(begin==""){
				alertInfo("请填写所有查询条件");
				return false;
			}
			
			//判断结束时间是否是必填项
			if(endTime==1){
				if(end==""){
					alertInfo("请填写所有查询条件");
					return false;
				}
			}
			
			//判断统计区域是否是必填项
			if(areaTr==1){
				if(area==""){
					alertInfo("请填写所有查询条件");
					return false;
				}
			}
			
			waitIndex = wait();
			return true;
		}
		
		//用于关闭加载中弹窗
		function closeLoging(){
			layer.close(waitIndex);
		}
		
		//获取选择的区域名称
		function getSelAreaName(){
			return $("#openPop").text();
		}
		
		//IE下 背景全屏
	    window.onresize = function(){
			$('.layui-layer-shade').height($(window).height());
		} 
		
		//根据模板隐藏查询条件
		function templateChnage(obj){
			var val = $(obj).val();
			var endTime = $("#end");
			
			if(val==1){ //贷款分机构情况统计表
			
				endTime.hide();
				endTime.prev().hide();
				
				end.format = 'YYYY-MM-DD';
		     	start.format = 'YYYY-MM-DD';
		     	var strDate = new Date().format("yyyy-MM-dd");
		     	$("#start").val(strDate);
		     	$("#end").val(strDate);
		     	
			}else if(val==9){ //重点关注企业本外币贷款时序统计表
			
				endTime.hide();
				endTime.prev().hide();
				
		     	end.format = 'YYYY-MM';
		     	start.format = 'YYYY-MM';
		     	var sDate = $("#start").val().split("-");
		     	var eDate = $("#end").val().split("-");
		     	$("#start").val(sDate[0] + "-" + sDate[1]);
		     	$("#end").val(eDate[0] + "-" + eDate[1]);
		     	
			}else{ //其他所有
			
				endTime.show();
				endTime.prev().show();
				
				end.format = 'YYYY-MM-DD';
		     	start.format = 'YYYY-MM-DD';
		     	var strDate = new Date().format("yyyy-MM-dd");
		     	$("#start").val(strDate);
		     	$("#end").val(strDate);
		     	
			}
		}
	</script>

</head>
<body>

<div class="reportingQuery">
	<#-- 弹出框 -->
    <div id="covered"></div>  
    <div id="poplayer">  
        <div class="borderBox">
        	<div class="titleFont1">地区列表</div>
        	<div class="listBox">
        		<table cellpadding="0" cellspacing="0" style="border: none;">
					<tr>
						<td id="${sa.sys_area_id}" onclick="selUpstream(this)" style="border: none;padding: 0px;">
							<#if (sa.subArea?? && sa.subArea?size > 0) >
								<div id="0" class="open-shrink" onclick="openArea(this, ${sa.sys_area_id})">
									<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
								</div>
							</#if>
							<label class="fontSize12">${sa.sys_area_name}</label>
						</td>
					</tr>
				</table>
        	</div>
        	<div class="btnBox">
        		<input type="button" value="取 消"  class="cancleBtn" onclick="closePop()"/>
        		<input type="button" value="确 认"  class="sureBtn" onclick="confirmSel()"/>
        	</div>
        </div>  
	</div>  


   	<div class="showListBox" style="border: none;">
		<form target="myFrameName" method="post" action="${request.getContextPath()}/admin/countReportingQuery/result.jhtml">
		    <table cellpadding="0" cellspacing="0" style="margin-left: 0px;">
		        <caption class="titleFont1 titleFont1Ex">选择查询条件</caption>
		        <tr>
		            <td class="noBorderL firstTD">统计报表模板</td>
		            <td class="secondTD">
		                <select id="templateList" name="template" class="inputSty shouldHide" onchange="templateChnage(this)">
		                	<#list tmplates as t>
		                   		<option <#if t.queryTotalTemplateId==template>selected=selected</#if> value="${t.queryTotalTemplateId}">${t.queryTotalTemplateName}</option>
		                    </#list>
		                </select>
		            </td>
		        </tr>
		        <tr>
		            <td class="noBorderL firstTD">统计时间</td>
		            <td class="secondTD">
		            	<input id="start" autocomplete="off" class="laydate-icon inputSty fontSize12" name="begin" value="${begin}">
		                <span> ~ </span>
		                <input id="end" autocomplete="off" class="laydate-icon inputSty fontSize12" name="end" value="${end}">
		            </td>
		        </tr>
		        <tr id="areaTr">
		            <td class="noBorderL firstTD">统计区域</td>
		            <td class="secondTD">
		            	<input type="hidden" id="areaName" name="areaName" value="${areaName}" />
		            	<input type="hidden" id="area" name="area" value="${area}"/>
		                <a id="openPop" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()">${(areaName!="")?string(areaName,'点击选择')}</a>
		            </td>
		        </tr>
		    </table>
    		<input type="submit" onclick="return searchForm()" class="queryBtn" value="开始查询" />
		</form>
   	</div> 
   	
	<iframe id="myFrameName" name="myFrameName" name="rightFrame" frameborder="0" style="width:100%;height:600px"></iframe>
</div>

</body>
</html>