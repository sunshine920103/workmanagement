<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <meta charset="UTF-8">
    <title></title>
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
        .taskAdd{
            width: 800px;
            margin: 100px auto;
            border: 1px solid #c1c1c1;
            padding: 20px;
        }
        .queryHead{
            width: 100%;
            height: 30px;
            line-height: 30px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
        .taskAdd table{
            width: 100%;
            border-top: 1px solid #dadada;
            border-left: 1px solid #dadada;
            font-size: 12px;
            margin-bottom: 20px;
        }
        .taskAdd th{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            text-align: left;
        }
        .taskAdd td{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
        }
        .titleFont1 {
            padding-left: 10px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            text-align: left;
            border: 1px solid #dadada;
            border-bottom: none;
        }
        .taskAdd .left{
            text-align: left;
        }
        .taskAdd .right{
            text-align: right;
        }
        .taskAdd .taskCycle div{
            float: left;
            margin-right: 20px;
        }
        .dutyBankTitle{
            width: 100%;
            height: 30px;
            margin: 10px 0;
            padding-top: 10px;
            text-align: center;
            font-size: 14px;
        }
        .queryBtn{
            width: 100%;
            height:50px;
            text-align: center;
        }
        .queryBtn button{
            width: 150px;
            height: 30px;
            margin: 0 20px;
            outline: none;
            border: none ;
            cursor: pointer;
            color: white;
        }
        .queryBtn .cancel{
            background-color: #a4a4a4;
        }
        .queryBtn .query{
            background-color: #f57041;
        }
        .bankList{
            width: 300px;
            height: 500px;
            border: 1px solid #dadada;
            float: left;
        }
        .bankList p,.bankChecked p{
            width: 210px;
            height: 25px;
            line-height: 25px;
            padding: 5px 10px;
            border-bottom: 1px solid #dadada;
        }
        .bankList div{
            padding: 5px 10px;
        }
        .choiceBtn{
            width: 60px;
            padding: 150px 10px;
            float: left;
        }
        .choiceBtn input{
            margin-top: 30px;
            cursor: pointer;
        }
        .bankChecked{
            width: 230px;
            height: 500px;
            border: 1px solid #dadada;
            float: left;
            overflow:auto;
        }
    </style>
    <script type="text/javascript">
    	var html="";
    	var indexNum=-1;
		$(function(){
			$("#max").height($(window).height()-50);
			//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.alert(msg, {
						icon: (msg=="操作成功")?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
			$("#rightMove").click(function(){//右移
				if(html!=""){
					$("#leftTabel").append(html);
					$("#rightTabel tr:eq("+indexNum+")").find("a").attr("id","left");
				}
				html="";
			})
			$("#leftMove").click(function(){//左移
				if(indexNum!= -1){
					var leftText= $("#leftTabel tr:eq("+indexNum+")").find("a").text();
					$("#rightTabel tr td a").each(function(){
						if($(this).text()==leftText){
							$(this).attr("id","right");
						}
					});
					$("#leftTabel tr:eq("+indexNum+")").remove();
					html="";
				}
			})
			$("#weekSelect").click(function(){//周选择
				$("#weekInput").val($(this).val());
			})
			$("#monthSelect").click(function(){//月选择
				$("#monthInput").val($(this).val());
			})
			$("#seasonSelect").click(function(){//季_月选择
				$("#seasonInput").val($(this).val()+$("#seasonTwoSelect").val());
			})
			$("#seasonTwoSelect").click(function(){//季_日选择
				$("#seasonInput").val($("#seasonSelect").val()+$(this).val());
			})
			var reportTaskPushSetCycle= "${reportTaskPushSet.reportTaskPushSetCycle}";
			if(reportTaskPushSetCycle.indexOf("季")!=-1){
				var monthStr=reportTaskPushSetCycle.substring(0,reportTaskPushSetCycle.indexOf("月")+1);
				var dayStr=reportTaskPushSetCycle.substring(reportTaskPushSetCycle.indexOf("月")+1);
				$("#seasonInput").attr("checked","checked");
				$("#seasonSelect option").each(function(){
					if($(this).val()==monthStr){
						$(this).attr("selected","selected");
					}
				});
				$("#seasonTwoSelect option").each(function(){
					if($(this).val()==dayStr){
						$(this).attr("selected","selected");
					}
				});
				$("#seasonInput").val(reportTaskPushSetCycle);
			}else if(reportTaskPushSetCycle.indexOf("月")!=-1){
				$("#monthInput").attr("checked","checked");
				$("#monthSelect option").each(function(){
					if($(this).val()==reportTaskPushSetCycle){
						$(this).attr("selected","selected");
						$("#monthInput").val($(this).val());
					}
				});
			}else if(reportTaskPushSetCycle.indexOf("周")!=-1){
				$("#weekInput").attr("checked","checked");
				$("#weekSelect option").each(function(){
					if($(this).val()==reportTaskPushSetCycle){
						$(this).attr("selected","selected");
						$("#weekInput").val($(this).val());
					}
				});
			}else{
				$("#nothingInput").attr("checked","checked");
			}
			//报送方式
			$("[name=reportTaskPushSetMethod]").change(function(){
				var reportTaskPushSetMethod=$(this).val();
				var url = "${request.getContextPath()}/admin/reportTaskPushSet/getReportTaskPushSetMethodListByMethodIdJson.jhtml";
				$.post(url,{reportTaskPushSetMethod:reportTaskPushSetMethod},function(data){
					if(data.reportTaskPushSetTemplateList!=null){
						var reportTaskPushSetTemplateList=data.reportTaskPushSetTemplateList;
						var html="";
						for(var i = 0; i < reportTaskPushSetTemplateList.length; i++){
							html+="<option>"+reportTaskPushSetTemplateList[i]+"</option>"
						}
						$("[name=reportTaskPushSetTemplate]").html(html);
					}
				});
			});
			
		})
			/********************************************************
			 * 打开上级机构弹出框
			 * obj ： 被点击的元素（this）
			 * id : 需要查询子机构的机构ID
			 */
			function openInstitutions(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
				
				if(obj.attr("name")==0){ //未展开
					//显示正在操作域弹窗
					var option_index = layer.load(0,{
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("name",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysOrg/getInstitutions.jhtml";
					$.post(url,{_:Math.random(),id:id},function(result){
						//关闭弹窗
						layer.close(option_index);
						if(result!=null){
							var subs = result.subSysOrg;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subSysOrg!=null && sub.subSysOrg.length!=0){
									icon = '<div id="'+sub.sys_org_id+'" name="0" class="open-shrink" onclick="openInstitutions(this,'+sub.sys_org_id+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var tr = $("<tr name='"+sub.sys_org_id+"' class='"+id+"'></tr>");
								var name = "<td style='padding-left:"+spacing+"'>"
											+icon
											+'<a href="javascript:void(0);" class="fontSize12" id="right" onclick="orgNameClick(this,'+sub.sys_org_id+')">'+sub.sys_org_name+'</a>'
											+"</td>";
								tr.append(name);
								ptr.after(tr);
							}
						}
					});
				}else{ //已展开
					//设置为收缩状态
					obj.attr("name",0);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",20);
					//删除子区域
					delInstitutions(id);
				}
			}
			//删除机构类型管理子区域
			function delInstitutions(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					//删除子区域
					$(this).remove();
					//递归删除子区域
					delInstitutions(pid);
				});
			}
			//选中机构
			function orgNameClick(obj,orgid){
				var obj=$(obj);
				indexNum=obj.parents("tr").index();
				var tdId= obj.attr("id");
				//变色
				$("form").find("tr").siblings().find("td").css("background-color","white");
				obj.parent().css("background-color","#c2e5ef");
				var tableId= obj.parents("table").attr("id");
				if(tableId=="rightTabel"){
					$("#rightMove").attr("disabled",false);
					$("#leftMove").attr("disabled",true);
				}else{
					$("#leftMove").attr("disabled",false);
					$("#rightMove").attr("disabled",true);
				}
				if(tdId=="right"){
					html="<tr>"
						+"<td><input type='hidden' name='orgIds' value="+orgid+">"
						+"<a id='left' href='javascript:void(0);' class='fontSize12' onclick='orgNameClick(this,"+orgid+")'>"+obj.text()+"</a></td>"
					html+="</tr>"
				}else{
					html="";
				}
			}
		</script>
</head>
<body>
<div class="showListBox">
    <form id="addData" method="post" action="${request.contextPath}/admin/reportTaskPushSet/editSubmit.jhtml">
    <table cellpadding="0" cellspacing="0">
        <caption class="titleFont1 titleFont1Ex">增加任务推送</caption>
        <tr>
            <td style="width:35%;" class="noBorderL firstTD"><label class="mainOrange"> * </label>任务名称</td>
            <td style="width:65%;" class="secondTD">
            	<input name="reportTaskPushSetId" type="hidden" value="${reportTaskPushSet.reportTaskPushSetId}">
                <input class="inputSty allnameVal" name="reportTaskPushSetName" type="text" value="${reportTaskPushSet.reportTaskPushSetName}" onblur="onblurVal(this,1,20)">
            </td>
        </tr>
        <tr class="taskCycle">
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>任务周期</td>
            <td class="secondTD">
                <div><input class="verticalMiddle" id="nothingInput" type="radio" name="reportTaskPushSetCycle" value="无"><span class="inlineBlock fontSize12 paddingLR10">无</span></div>
                <div>
                    <input class="verticalMiddle" id="monthInput" type="radio" name="reportTaskPushSetCycle" value="每月"><span class="inlineBlock fontSize12 paddingLR10">每月</span>
                    <select id="monthSelect" class="inputSty" style="width: 120px;">
                    	<#list 1..31 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="每月1日" selected="selected">1日</option>
		                    	<#else>
		                    		<option value="每月1日">1日</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="每月${num}日">${num}日</option>
	                    	</#if>
						</#list>
                    </select>
                </div>
                <div>
                    <input id="weekInput" class="verticalMiddle" type="radio" name="reportTaskPushSetCycle" value="每周"><span class="inlineBlock fontSize12 paddingLR10">每周</span>
                    <select id="weekSelect" class="inputSty" style="width: 120px;">
                    	<#if reportTaskPushSet??>
                    		<option value="每周一" selected="selected">一</option>
                    	<#else>
                    		<option value="每周一">一</option>
                    	</#if>
						<option value="每周二">二</option>
						<option value="每周三">三</option>
						<option value="每周四">四</option>
						<option value="每周五">五</option>
						<option value="每周六">六</option>
						<option value="每周日">日</option>
                    </select>
                </div>
                <div>
                    <input id="seasonInput" class="verticalMiddle" type="radio" name="reportTaskPushSetCycle" value="每季"><span class="inlineBlock fontSize12 paddingLR10">每季</span>
                    <select id="seasonSelect" class="inputSty" style="width: 120px;">
                    	<#list 1..12 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="每季1月" selected="selected">每季1月</option>
		                    	<#else>
		                    		<option value="每季1月">每季1月</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="每季${num}月">每季${num}月</option>
	                    	</#if>
						</#list>
                    </select>
                	<select id="seasonTwoSelect" class="inputSty" style="width: 120px;">
                    	<#list 1..31 as num>
                    		<#if num_index == 0>
	                    		<#if reportTaskPushSet??>
		                    		<option value="1日" selected="selected">1日</option>
		                    	<#else>
		                    		<option value="1日">1日</option>
		                    	</#if>
	                    	</#if>
	                    	<#if num_index gt 0>
	                    		<option value="${num}日">${num}日</option>
	                    	</#if>
						</#list>
                    </select>
                </div>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>状态</td>
            <td class="secondTD">
            	<#if reportTaskPushSet.reportTaskPushSetStatus==1>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="0"><span class="inlineBlock fontSize12 paddingLR10">正常</span>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="1" checked="checked"><span class="inlineBlock fontSize12 paddingLR10">禁用</span>
            	<#else>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="0" checked="checked"><span class="inlineBlock fontSize12 paddingLR10">正常</span>
            		<input class="verticalMiddle" type="radio" name="reportTaskPushSetStatus" value="1"><span class="inlineBlock fontSize12 paddingLR10">禁用</span>
            	</#if>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>涉及数据报送</td>
            <td class="secondTD">
                <select name="reportTaskPushSetMethod" class="inputSty">
                	<#list reportTaskPushSetMethodList as item>
                		<#if reportTaskPushSetMethodIdList[item_index]==reportTaskPushSet.reportTaskPushSetMethod>
                			<option value="${reportTaskPushSetMethodIdList[item_index]}" selected="selected">${item}</option>
                		<#else>
                			<option value="${reportTaskPushSetMethodIdList[item_index]}">${item}</option>
                		</#if>
                	</#list>
                </select>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>涉及报送模板</td>
            <td class="secondTD">
                <select name="reportTaskPushSetTemplate" class="inputSty">
                	<#if reportTaskPushSetTemplateList ?? !>
	                	<#list reportTaskPushSetTemplateList as item>
	                		<#if item==reportTaskPushSet.reportTaskPushSetTemplate>
	                			<option selected="selected">${item}xx</option>
	                		<#else>
	                			<option>${item}</option>
	                		</#if>
	                	</#list>
                	<#else>
                		<#list allIndexName as item>
                			<option>${item}</option>
                		</#list>
                	</#if>
                </select>
            </td>
        </tr>
        <tr>
            <td class="noBorderL firstTD"><label class="mainOrange"> * </label>任务发布机构</td>
            <td class="secondTD">
            	<input type="hidden" name="reportTaskPushSetOrgId" value="${reportTaskPushSet.reportTaskPushSetOrgId}" />
              	<input readonly="readonly" name="reportTaskPushSetOrgName" type="text" value="${reportTaskPushSet.reportTaskPushSetOrgName}" />
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <p class="dutyBankTitle">任务负责机构列表</p>
                <div style="width: 250px;height: 500px;float:left;border:1px solid #dadada;margin-left:10px;overflow:auto;">
                    <p style="border-bottom:1px solid #dadada;padding:5px 10px;height:25px;line-height:25px;">机构列表</p>
                    <div id="" style="overflow: auto;width:250px;">
                    	<table id="rightTabel" cellpadding="0" cellspacing="0">
							<#list is as i>
								<tr>
									<td>
										<#if (i.subSysOrg?? && i.subSysOrg?size > 0) >
											<div id="${i.sys_org_id}" name="0" class="open-shrink" onclick="openInstitutions(this, ${i.sys_org_id})">
												<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
											</div>
										</#if>
										<a href="javascript:void(0);" id="right" class="fontSize12" onclick="orgNameClick(this,${i.sys_org_id})">${i.sys_org_name}</a>
									</td>
								</tr>
							</#list>
						</table>
                    </div>
                </div>
                <div class="choiceBtn">
                    <input class="sureBtn sureBtnEx" type="button" id="rightMove" value="添加>>" style="width: 60px;"> <br>
                    <input class="sureBtn sureBtnEx" type="button" id="leftMove" value="<<删除" style="width: 60px;">
                </div>
                <div class="bankChecked" style="overflow: auto;">
                    <p>已选机构列表</p>
                    <div>
                    	<table id="leftTabel">
                    		<#list sysOrgList as item>
                    			<tr>
	                    			<td>
	                    				<input type="hidden" name="orgIds" value="${item.sys_org_id}">
	                    				<a href="javascript:void(0);" id="left" class="fontSize12" onclick="orgNameClick(this,${item.sys_org_id})">${item.sys_org_name}</a>
	                    			</td>
	                    		</tr>
                    		</#list>
                    	</table>
                    </div>
                </div>
            </td>
        </tr>
    </table>
    </form>
    <div class="showBtnBox">
    	<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
    	<input id="formVer" type="button" class="sureBtn" value="确 认" />
    </div>
</div>
</body>
<script type="text/javascript">
$(function(){
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});
		$("#formVer").click(function(){
				var name = $("[name=reportTaskPushSetName]").val();
				var orgList= $("#leftTabel").text();
				if($.trim(name)==""){
					layer.alert("请填写名称", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
				}
				else if($.trim(orgList)==""){
					layer.alert("机构列表未选择", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
					});
				}
				else{
					//为周、月、季赋初始值
					var reportTaskPushSetCycle= $("[name=reportTaskPushSetCycle]:checked").val();
					if(reportTaskPushSetCycle.length==2){
						var nextVal= $("[name=reportTaskPushSetCycle]:checked").next().next().val();
						if(reportTaskPushSetCycle.indexOf("季")!=-1){
							nextVal+=$("[name=reportTaskPushSetCycle]:checked").next().next().next().val()
						}
						$("[name=reportTaskPushSetCycle]").val(nextVal);
					}
					
					var loading = layer.load();
					$.post("${request.contextPath}/admin/reportTaskPushSet/editSubmit.jhtml",$("#addData").serialize(),function(data){
						layer.close(loading);
						var index = alertInfoFun(data.message, data.flag, function(){
							if(data.flag){
								parent.window.location.href = "${request.getContextPath()}/admin/reportTaskPushSet/list.jhtml";
							}
							layer.close(index);
						});
					});
				}
			});	
	});
</script>
</html>