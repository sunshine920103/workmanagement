<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
			    //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        };
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
                layer.alert(msg, {
                    icon: (msg == "操作成功") ? 1 : 2,
                    shade: 0.3,
                    shadeClose: true
                },function(){
                      var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
                      parent.layer.close(index); //执行关闭
                      parent.window.location.href = "${request.getContextPath()}/admin/reportIndex/list.jhtml";
						 window.close();
                });
            }
            $("#gdTime").click(function () {
                setLayDate("#gdTime");
            });
            //判定是否为IE6浏览器,是的话,那就改变宽度吧
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                $("#sureWidth").width(550 + "px");
            }
            //设置左侧栏的高度
            $("#max").height($(window).height() - 50);
            //为模板名称赋初始值
            $("#indexListDiv li a:eq(0)").css("color", "rgb(56,165,226)");

            var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		    $('.closeThisLayer').on('click', function () {
		       parent.layer.close(index); //执行关闭
		       window.close();
		    });


		    //
		    //
		     //上传验证
            $("#bigBtn").click(function () {
                var fileVal = $("[name='file']").val();
                var timeValue = $("[name=time]").val();
                var indexId = $("input[name='name1']").val();
                var myDate = getNowFormatDate();
                var reg=/(.*).(xls|xlsx)$/;
                if (indexId == "" || indexId == undefined) {
                    layer.alert("请选择模板", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                } else if (timeValue == "") {
                    layer.alert("请选择归档时间", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                } else if (timeValue > myDate) {
                    layer.alert("归档时间不能大于当前时间", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                } else if (fileVal == "") {
                    $('.layui-layer-shade').height($(window).height());
                    layer.alert("请选择上传文件", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                  
                } else if (!reg.test(fileVal)) {
                    $('.layui-layer-shade').height($(window).height());
                    layer.alert("上传文件不是excel格式", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                } 
                $("#upLoad").submit();
               	 var ide=$("#indexId").val(); 
	                	layer.open({
							  type: 2,
							  title: false,
							  area: ['300px', '100px'],
							  shade: 0.4,
							  closeBtn:0,
							  shadeClose: false, 
							  content: ['${request.getContextPath()}/admin/reportIndex/status.jhtml?tempId='+ide, 'no']
							});	 
            }); 
    	});
		//确认选择
		function confirmSel1(clear){
			var seleced = $(".seleced");
			closePop();			
			var mobanName = $(seleced.find("label")).text();
			var val = $(seleced.find("label")).val();
			if(clear==1||seleced.length==0){
					$("#openPop1").text("请选择报送模版");
					$("#fileId1").val("");
			}else{
                $("#openPop1").text(mobanName);
                $("#indexId").val(seleced.attr("id"));
                $("#fileId2").val(seleced.attr("id"));
                $("#fileId1").val(mobanName);
            }
		}
        //下载excel模板
		function downLoad(){
			var ide=$("#indexId").val();
			if(ide == ''){
				layer.alert("请先选择模版",{
					icon:2,
					shade:0.3,
					shoudClose:true
				});
				return false;
			}
			window.location.href="${request.getContextPath()}/admin/reportExcelTemplate/downLoad.jhtml?id="+ide;
		}
		
		function openPop(){
			$("#covered").show();
			$("#poplayer").show();
		}
		
		//关闭上级区域弹出框
		function closePop(){
			$("#covered").hide();
			$("#poplayer").hide();
		}
		function selUpstream(obj){
			$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
			});
			$(obj).addClass("seleced");
		}   
		</script>
	</head>
	<body>
<#-- 弹出框 -->
<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox ">
        <div class="titleFont1">
            <span>报送模版列表</span>
        </div>
        <div></div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0">
            <#list list as i>
                <tr>
                    <td id="${i.reportExcelTemplateId}" onclick='selUpstream(this)'>
                        <label>${i.reportExcelTemplateName}</label>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
        </div>
    </div>
</div>
		<#-- 新增框 -->
		<div class="showListBox">
			<form id="upLoad" method="post" enctype="multipart/form-data" action="${request.getContextPath()}/admin/reportIndex/upLoad.jhtml" class="marginL30">
			<table cellpadding="0" cellspacing="0">
						<caption  class="titleFont1 titleFont1Ex">
							新增excel报送
						</caption>
					<tr>
						<td  class="noBorderL firstTD" style="width:200;">报送模版：</td>
						<td class="secondTD" style="width:400;">
							  <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop()">请选择报送模版</a>
						</td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD" width="200">归档时间： </td>
						<td width="400">
						    <input id="gdTime" name="time" autocomplete="off" class="laydate-icon inputSty fontSize12">
						</td>
					</tr>
					<tr>
						<td  class="noBorderL firstTD" width="200">上传文件： </td>
						<td width="400">
						   <input type="file" name="file" class="inputSty"/>
						</td>
					</tr>
				</table>
				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input type="button" class="bigBtn" value="提交报送内容" id="bigBtn" style="margin-left: 0px;"/>
					<input type="hidden" class="sureBtn sureBtnEx" id="indexId" />
					<input type="button" onclick="downLoad()" class="sureBtn sureBtnEx marginL20" value="下载模板"/>
					<input type="hidden" name="name1" class=" inlineBlock inputSty"  value="" id="fileId1" >
					<input type="hidden" name="temptId" class=" inlineBlock inputSty"  value="" id="fileId2" >
				</div>
			</form>
		</div>
		<#--<div>-->
			<#--<input type="button" name="openStep" value="报送耗时详情" onclick ="window.location.href='${request.getContextPath()}/admin/reportIndex/openstep.jhtml.jhtml'" >-->
		<#--</div>-->
	</body>	
</html>
