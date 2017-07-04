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
            if (msg != "") {
                layer.alert(msg, {
                    icon: (msg == "上报成功") ? 1 : 2,
                    shade: 0.3,
                    shadeClose: true
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

            //为模板名称赋初始值
            $("#indexListDiv li a:eq(0)").css("color", "rgb(56,165,226)");

        });

        //指标大类弹出
        function openPop(num) {
            $("#poplayer").show();
            $("#covered").show();
            $("#poplayer").children(".zIndex").removeClass("zIndex");
            if (num == 1) {
                $(".zbdl").show();
                $(".jglb").hide();
                $(".zbdl").addClass("zIndex");
            } else {
                $(".jglb").show();
                $(".zbdl").hide();
                $(".jglb").addClass("zIndex");
                if($("#treeDemo").html()==""){
                var loading = layer.load();
	            $.ajax({
	                url: '${request.getContextPath()}/admin/messageSubmission/getSysOrgs.jhtml',
	                dataType: 'json',
	                success: function (nodes) {
	                	layer.close(loading);
	                    var str1 = [];
	                    for (var i = 0; i < nodes.length; i++) {
	                        var obj = new Object();
	                        obj.id = nodes[i].id;
	                        obj.name = nodes[i].name;
	                        obj.pId = nodes[i].parent;
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
            $('#upLoad').attr('action', "${request.getContextPath()}/admin/messageSubmission/upLoad.jhtml?indexId=" + obj.id);
        }

        //确认选择
        function confirmSel1(clear) {
            var seleced = $(".zbdl .seleced");
            closePop();
            var area = $(seleced.find("label")).text();
            if (clear == 1 || seleced.length == 0) {
                $("#openPop1").text("请选择指标大类");
                $("input[name='indexName']").val("")
            } else {
                $("#openPop1").text(area);
                $("input[name='indexName']").val(area)
            }

        }
        //确认选择机构
        function confirmSel2(clear) {
            closePop();
            var htext = $("#hideorg input").val()
            var hteid = $("#hideorg input").attr("id")
            if (clear == 1) {
                $("#openPop2").text("请选择机构");
                $("input[name='sysOrgId']").val("")
            } else {
                $("#openPop2").text(htext);
                $("input[name='sysOrgId']").val(hteid)
            }


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
            <span>指标大类</span>
        </div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0" id="cycle_j">
            <#list indexTbList as i>
                <tr>
                    <td id="${i.indexId}" onclick='selUpstream(this)'>
                        <label>${i.indexName}</label>
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
<form id="searchForm" method="post" action="${request.getContextPath()}/admin/messageSubmission/searchData.jhtml">
    <input type="hidden" name="indexName" value="${indexName}"/>
    <input type="hidden" name="sysOrgId" value="${sysOrg.sys_org_id}"/>
    <input type="hidden" name="recordDate" value="${recordDate}"/>
    <input type="hidden" name="status" value="${status}">
    <input type="hidden" name="reportSubmitTime" value="${reportSubmitTime}">
</form>
<div class="rightPart eachInformationSearch" id="sureWidth">
    <div class="queryInputBox ">
        <div style="text-align: left;">
            <input type="button" class="sureBtn sureBtnEx" style="margin-left: 30px;width: 85px;" value="进行报文报送"
                   onclick="setLayer('进行报文报送','${request.getContextPath()}/admin/messageSubmission/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"/>
        </div>
        <div class="margin2030">
            <form id="searchData" method="post"
                  action="${request.getContextPath()}/admin/messageSubmission/searchData.jhtml">
                <p>
                    <label>指标大类：
                        <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                           onclick="openPop(1)"><#if indexName==null>请选择指标大类<#else >${indexName}</#if></a>
                    </label>
                    <label> 送报机构：
                        <a id="openPop2" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                           onclick="openPop(2)"><#if sysOrg==null>请选择机构<#else >${sysOrg.sys_org_name}</#if></a>
                    </label>
                    <input type="hidden" name="indexName" value="${indexName}"/>
                    <input type="hidden" name="sysOrgId" value="${sysOrg.sys_org_id}"/>
                </p>
                <p style="margin: 10px auto;">
                    <label>
                        归档时间：
                        <input id="gdTime" name="recordDate" autocomplete="off" class="  inputSty fontSize12"
                               value="${recordDate}">
                    </label>
                     <label>
                        报送时间：
                         <input id="gdTime1" name="reportSubmitTime" autocomplete="off"
                                class="  inputSty fontSize12"
                                value="${reportSubmitTime}">
                    </label>
                    <label> 状态：
                        <select name="status" class="inputSty">
                            <option value="">请选择</option>
                        <#if status == 0>
                            <option value="0" selected>上传成功</option>
                        <#else >
                            <option value="0">上传成功</option>
                        </#if>
                        <#if status == 1>
                            <option value="1" selected>上传失败</option>
                        <#else >
                            <option value="1">上传失败</option>
                        </#if>
                        </select>
                    </label>
                </p>
                <label>
                    <input id="searchDataButton" type="button" class="sureBtn sureBtnEx " style="margin-left:0 ;"
                           value="筛选"/>
                </label>
            </form>
        </div>
    </div>
    <!-- 默认获取的一个小指标 列表       -->
    <div id="min" class="listBox">
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">报送记录列表</caption>
            <tr class="firstTRFont">
                <td width="40">序号</td>
                <td width="150">送报机构</td>
                <td width="150">指标大类</td>
                <td width="150">归档日期</td>
                <td width="150">报送时间</td>
                <td width="50">状态</td>
                <td width="80">操作</td>
            </tr>
        <#list reportIndexList as item>
            <tr>
                <td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
                <td>${item.reportIndexOrgName}</td>
                <td>${item.reportIndexTemplate}</td>
                <td>${(item.reportIndexTime?string("yyyy-MM-dd"))!}</td>
                <td>${(item.reportIndexSubmitTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                <td>
                    <#if item.reportIndexStatus = 0>
                        <span class="changeFont fontSize12">上报成功</span>
                    <#else>
                        <span class="delFont fontSize12">上报失败</span>
                    </#if>
                </td>
                <td>
                    <a class="changeFont fontSize12 hasUnderline cursorPointer"
                       onclick="setLayer('查看','${request.getContextPath()}/admin/messageSubmission/show.jhtml?reportIndexId=${item.reportIndexId}');$('.layui-layer-shade').height($(window).height());"
                    >查 看</a>
                </td>
            </tr>
        </#list>
        </table>
    <#if (reportIndexList?? && reportIndexList?size > 0)>
        <#include "/fragment/paginationbar.ftl"/>
    </#if>
    </div>
</div>
</div>
</body>
</html>
