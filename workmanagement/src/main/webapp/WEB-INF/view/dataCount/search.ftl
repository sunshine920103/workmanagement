<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        };
        $(function () {
            //回显
            var msg = "${msg}";
            if(msg != "") {
                layer.alert(msg,{
                    icon: (msg=="上报成功")?1:2,
                    shade:0.3,
                    shadeClose:true
                });
            }

            $("#gdTime").click(function () {
                setLayDate("#gdTime");
            });
            $("#gdTime1").click(function () {
                setLayDate("#gdTime1");
            });
            //判定是否为IE6浏览器,是的话,那就改变宽度吧
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                $("#sureWidth").width(550 + "px");
            }
            //设置左侧栏的高度
            $("#max").height($(window).height() - 50);      
            //为模板名称赋初始值
            $("#indexListDiv li a:eq(0)").css("color", "rgb(56,165,226)");
        //指标大类弹出
        })
        function openPop(num) {
            $("#covered").show();
            $("#poplayer").show();
            $("#poplayer").children(".zIndex").removeClass("zIndex");
			if(num==1){
				$(".zbdl").show();
				$(".jglb").hide();
                $(".zbdl").addClass("zIndex");
			}else{
				$(".zbdl").hide();
				$(".jglb").show();
                $(".jglb").addClass("zIndex");
                if($("#treeDemo").html()==""){
                var loading = layer.load(); 
				$.ajax({
					url:'${request.getContextPath()}/admin/reportTaskPushSet/getOrgList.jhtml',
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

        function selUpstream(obj) {
            $.each($(".seleced"), function () {
                $(this).removeClass("seleced");
            });
            $(obj).addClass("seleced");
            $('#openPop1').attr('name', obj.id);
        }

        //确认选择
        function confirmSel1(clear) {
            var seleced = $(".zbdl .seleced");
            closePop();
            var temp = $(seleced.find("label")).text();
            if (clear == 1 || seleced.length == 0) {
                $("#openPop1").text("请选择报送模板");
            } else {
                $("#openPop1").text(temp);        
                $("input[name='tempName']").val(temp);        
            }
        }
 
        
        
         //确认选择机构
			function confirmSel2(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#openPop2").text("请选择机构");
					$("input[name='orgName']").val("");
                	$("input[name='orgId']").val("");
				} else { 
					$("#openPop2").text(htext); 
					$("input[name='orgName']").val(htext);
                	$("input[name='orgId']").val(hteid);
				} 
				

			}
         	function openAdd(){
         		window.open("${request.getContextPath()}/admin/reportIndex/add.jhtml","_blank","location=no,status=no,width=600,height=500,top=100px,left=100px");
        }
        
    </script>
    <title>报文报送列表</title>
</head>
<body>
<#-- 弹出框 -->
<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox zbdl">			
		<div class="titleFont1">
			<span>模板列表</span>
		</div>
		<div class="listBox">
	        <table cellpadding="0" cellspacing="0" id="cycle_j">
				<#list Tlist as i>
                    <#if i.reportExcelTemplateName =='崇左市-基本信息'|| i.reportExcelTemplateName =='崇左市-行政处罚信息'||i.reportExcelTemplateName =='崇左市-行政许可信息'>
	    			<tr>
	                    <td id="${i.reportExcelTemplateId}" onclick='selUpstream(this)'>
	                        <label>${i.reportExcelTemplateName}</label>
	                    </td>
	    			</tr>
                    </#if>
			 	</#list>
	        </table>
		</div>
		<div class="btnBox">
			<input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
			<input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
			<input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		</div>
    </div>
    <div class="borderBox jglb">
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
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>
</div>
<div class="rightPart eachInformationSearch" id="sureWidth">
    <div class="queryInputBox ">
    	<div style="text-align: left;">

        </div>
        <div class="margin2030">
        <form id="searchForm" method="post">
			<input  id ="hidArea" name="tempName"  type="hidden" value="${tempName}"  />
			<input  id ="hidArea" name="orgName"  type="hidden" value="${orgName}"  />
			<input  id ="hidArea" name="orgId"  type="hidden" value="${orgId}"  />
			<input  id ="hidArea" name="reportDate"  type="hidden" value="${reportDate}"  />
			<input  id ="hidArea" name="status"  type="hidden" value="${status}"  />
			<input  id ="hidArea" name="submitTime"  type="hidden" value="${submitTime}"  />
		</form>
		<form id="search" method="post" action="">
		 <p>
                    <label>报送项：
                        <select name="status" class="inputSty">
                            <option value="1">行政许可</option>

                            <option value="0" selected>行政处罚</option>
                            <option value="2" selected>全部</option>

                        </select>
                    </label>
                    <#--<label> 送报机构：
                         <a id="openPop2" name="orgName" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)">
						  <#if orgName==null>请选机构<#else>${orgName}</#if></a>
						  <input id = "orgNameId" type="hidden" name="orgName" value="${orgName}"/>
						  <input id = "orgId" type="hidden" name="orgId" value="${orgId}"/>
                    </label>-->
                </p>
         <p style="margin: 10px auto;">
                    <label>
                        归档时间：
                        <input id="gdTime" name="reportDate" autocomplete="off" class="inputSty fontSize12"
                               value="${reportDate}">
                    </label>
                     <label>
                        报送时间：
                        <input id="gdTime1" name="submitTime" autocomplete="off" class="inputSty fontSize12"
                               value="${submitTime}">
                    </label>
                    <label> 报送形式：
                        <select name="status" class="inputSty">
		                        <option value="0" selected>EXCEL报送</option>
		                        <option value="1">WORD报送</option>
		                        <option value="2" selected>其他</option>

						</select>
                    </label>
                </p>
            <label>
                    <input type="submit"  class="sureBtn sureBtnEx " value="查询" style="margin-left: 0;"/ > 
              </label>
             
        </form>
	</div>
        <div>

			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">报送记录列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td style="width:5%;">序号</td>
		                    <td style="width:30%;">报送形式</td>
		                    <td style="width:19%">报送事项</td>
		                    <td style="width:13%;">报送日期</td>
		                    <td style="width:10%;">操作</td>
						</tr>

						<tr>
            				<td>${li_index+1}</td>
							<td>EXCEL报送</td>
							<td>行政许可</td>
							<td>2017-07-01 17:12:38</td>
							<td>
								<a class="changeFont fontSize12 hasUnderline cursorPointer" 
								onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show.jhtml');$('.layui-layer-shade').height($(window).height());"
								>查 看</a>
							</td>
						</tr>
                        <tr>
                            <td>${li_index+1}</td>
                            <td>EWORD报送</td>
                            <td>行政处罚</td>
                            <td>2017-07-02 17:12:38</td>
                            <td>
                                <a class="changeFont fontSize12 hasUnderline cursorPointer"
                                   onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                        >查 看</a>
                            </td>
                        </tr>

					</tbody>
				</table>

			</div>
		</div>
		</div>
	</body>

</html>
