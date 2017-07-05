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

            var myChart = echarts.init(document.getElementById('main'));

            var option = {
                title : {
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                legend: {
                    orient: 'vertical',
                    left: 'left',
                    data: ['字段不能同时为空','字段超长','状态说明不符合规范','字段为空','时间格式不符合标准']
                },
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius : '55%',
                        center: ['60%', '40%'],
                        data:[
                            {value:1, name:'字段不能同时为空'},
                            {value:2, name:'字段超长'},
                            {value:3, name:'状态说明不符合规范'},
                            {value:12, name:'字段为空'},
                            {value:1, name:'时间格式不符合标准'}
                        ],
                        itemStyle: {
                            emphasis: {
                                shadowBlur: 10,
                                shadowOffsetX: 0,
                                shadowColor: 'rgba(0, 0, 0, 0.5)'
                            }
                        }
                    }
                ]
            };

            myChart.setOption(option);
            
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
        <div class="margin2030">
            <form id="search" method="post">
                <p>
                    <label> 报送项：
                        <select id="sendops" class="inputSty">
                            <option value="0">行政许可</option>
                            <option value="1" selected>行政处罚</option>
                            <option value="2">全部</option>
                        </select>
                    </label>
                    <label> 数据质量状态：
                        <select id="quaops" class="inputSty">
                            <option value="0">合规</option>
                            <option value="1" selected>不合规</option>
                            <option value="2">全部</option>
                        </select>
                    </label>
                </p ><br>
                <p>
                    <label>
                        统计时间：
                        <input id="gdTime" name="reportDate" autocomplete="off" class="inputSty fontSize12"
                               value="${reportDate}">
                    </label>
                    <label>
                        <input id="gdTime1" name="submitTime" autocomplete="off" class="inputSty fontSize12"
                               value="${submitTime}">
                    </label>
                </p >
                <label>
                    <input type="button"  class="sureBtn sureBtnEx " value="查询" style="margin-left: 0;"/ >
                </label>
            </form><br>
            <p>
                <label>行政许可：
                    <span  style="font-weight: bold;">76</span>项
                </label>&nbsp&nbsp
                <label> 行政处罚：
                    <span  style="font-weight: bold;">31</span>项
                </label>&nbsp&nbsp
                <label> 合计：
                    <span  style="font-weight: bold;">107</span>项
                </label>
            </p><br>
            <label>
                <a  class="sureBtn sureBtnEx " href="${request.getContextPath()}/assets/不合规信息列表模板.xlsx" style="margin-left: 0;padding: 5px 20px;">导出</a>
            </label>
	</div>
        <div id="main" style="width: 800px;height:400px;padding-left: 50px"></div>
        <div>

			<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">不合规数据列表</caption>
					<tbody>
                    <tr class="firstTRFont">
                        <td style="width:5%;">序号</td>
                        <td style="width:15%;">行政相对人名称</td>
                        <td style="width:19%">项目名称</td>
                        <td style="width:13%;">行政相对人代码</td>
                        <td style="width:13%;">许可内容</td>
                        <td style="width:10%;">法定代表人姓名</td>
                        <td style="width:10%;">许可机关</td>
                        <td >操作</td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左市金帆船务有限公司</td>
                        <td>医师</td>
                        <td>9134010078</td>
                        <td>国籍登记</td>
                        <td>李传云</td>
                        <td>
                            崇左市交通运输局
                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>

						<tr>
            				<td>${li_index+1}</td>
							<td>周银铭</td>
							<td>医师</td>
							<td></td>
							<td>医师变更</td>
							<td></td>
							<td>
                                崇左市江州区卫计委
							</td>
                            <td>
                                <a class="changeFont fontSize12 hasUnderline cursorPointer"
                                   onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                        >查 看</a>
                            </td>
						</tr>
                    </tr>

                    <tr>
                        <td>${li_index+1}</td>
                        <td>马廷廷</td>
                        <td>医师</td>
                        <td></td>
                        <td>医师注册</td>
                        <td></td>
                        <td>
                            崇左市江州区卫计委
                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>凭祥市华航船务有限责任公司</td>
                        <td>桂凭祥货6198</td>
                        <td>9134012469</td>
                        <td>国籍登记
                        </td>
                        <td>王水银
                        </td>
                        <td>
                            崇左市交通运输局

                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左市江洲区仟富贵有限公司
                        </td>
                        <td>公共场所
                        </td>
                        <td></td>
                        <td>公共浴室
                        </td>
                        <td>张瑞标
                        </td>
                        <td>
                            崇左市江州区卫计委
                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左韵晨酒店管理有限公司
                        </td>
                        <td>公共场所
                        </td>
                        <td></td>
                        <td>宾馆开业
                        </td>
                        <td>王健
                        </td>
                        <td>
                            崇左市江州区卫计委
                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左中华职业学校
                            </td>
                        <td>学校举办者变更
                        </td>
                        <td></td>
                        <td>变更安徽中华职业学校
                        </td>
                        <td></td>
                        <td>
                            崇左市教育局

                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左电气工程职业技术学院
                        </td>
                        <td>公办高职高专院校
                        </td>
                        <td></td>
                        <td>高职高专院校章程核准
                        </td>
                        <td></td>
                        <td>
                            崇左市教育局

                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>崇左白厦职业学校

                        </td>
                        <td>变更学校类别
                        </td>
                        <td></td>
                        <td>高职高专院校章程核准
                        </td>
                        <td></td>
                        <td>
                            崇左市教育局

                        </td>
                        <td>
                            <a class="changeFont fontSize12 hasUnderline cursorPointer"
                               onclick="setLayer('查看','${request.getContextPath()}/admin/dataCount/show2.jhtml');$('.layui-layer-shade').height($(window).height());"
                                    >查 看</a>
                        </td>
                    </tr>
                    <tr>
                        <td>${li_index+1}</td>
                        <td>广西广播影视职业学院

                        </td>
                        <td>公办高职高专院校
                        </td>
                        <td></td>
                        <td>高职高专院校章程核准
                        </td>
                        <td></td>
                        <td>
                            崇左市教育局

                        </td>
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
