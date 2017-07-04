<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <style type="text/css">
        .eachInformationSearch .listBox table td {
            padding: 6px 5px;
        }

        td a {
            font-size: 12px;
        }

    </style>
    <script type="text/javascript">
        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        };

        //导出excel
        function fsubmit() {

            $.post("${request.getContextPath()}/admin/sysUserBehaviorAudit/isOverflow.jhtml", function (data) {
                if (data == "操作成功") {
                    window.location.href = "${request.contextPath}/admin/sysUserBehaviorAudit/excel.jhtml";
                    return false;
                }
                layer.alert(data, {
                    icon: (data == "操作成功") ? 1 : 2,
                    shade: 0.3,
                    shadeClose: true
                });
            });
        }


        function openPop(num) {
            $("#covered").show();
            $("#poplayer").show();
            if (num == 1) {
                $(".xzjg").show();
                $(".xzcd").hide();
				if($("#treeDemo").html()==""){
				 var loading = layer.load();
            	$.ajax({
                url: '${request.getContextPath()}/admin/sysUserBehaviorAudit/getSysOrgs.jhtml',
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
            } else if (num == 2) {
                $(".xzjg").hide();
                $(".xzcd").show();
            }
        }

        //关闭上级区域弹出框
        function closePop() {
            $("#covered").hide();
            $("#poplayer").hide();
            $(".seleced").removeClass("seleced");
        }
        function selUpstream(obj) {
            $.each($(".seleced"), function () {
                $(this).removeClass("seleced");
            });
            $(obj).addClass("seleced");
        }

        //确认选择机构
        function confirmSel1(clear) {
            closePop();
            var htext = $("#hideorg input").val();
            if (clear == 1) {
                $("#openPop1").text("请选择机构");
                $("#open1").val("");
            } else {
                $("#openPop1").text(htext);
                $("#open1").val(htext);
            }
        }

        function confirmSel2(clear) {
            var seleced = $(".xzcd .seleced");
            closePop();
            var area = $(seleced.find("label")).text();
            if (clear === 1 || seleced.length == 0) {
                $("#openPop2").text("请选择菜单");
                $("#open2").val("");
            } else {
                $("#open2").val(area);
                $("#openPop2").text(area);
            }
        }


        $(function () {
            $("#selectSome").click(function () {
                var stime = $("#begin").val();
                var etime = $("#end").val();
                if (stime == "") {
                    layer.alert("请选择开始时间");
                    return false;
                }

                if (etime == "") {
                    layer.alert("请选择结束时间");
                    return false;
                }
                var search = $("#key").val();
                if (checkTChineseM(search) == 0) {
                    layer.alert("请输入正确的查询条件", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                $("#toSelect").submit();
            });
        });

      
    </script>
</head>
<body>
<#-- 弹出框 -->
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
    <div class="borderBox xzcd">
        <div class="titleFont1">
            <span>菜单列表</span>
        </div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0">
            <#list sysMenus as it>

                    <tr>
                        <td level="1" id="${it.sys_menu_name}" onclick="selUpstream(this)">
                            <label>${it.sys_menu_name}</label>
                        </td>
                    </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>
</div>
<form id="searchForm" method="post" action="${request.getContextPath()}/admin/sysUserBehaviorAudit/selectLog.jhtml">
    <input type="hidden" name="beginTime" value="${beginTime}"/>
    <input type="hidden" name="endTime" value="${endTime}"/>
    <input type="hidden" name="operateType" value="${operateType}"/>
    <input type="hidden" name="someSelect" value="${someSelect}">
    <input type="hidden" name="orgName" value="${orgName}">
    <input type="hidden" name="menuName" value="${menuName}">
</form>
<div class="rightPart floatLeft  eachInformationSearch">
    <div class="listBox" style="margin-bottom:10px">
    <#-- 分页查询需要使用该表单 -->
        <form id="toSelect" method="post"
              action="${request.getContextPath()}/admin/sysUserBehaviorAudit/selectLog.jhtml">
            <div class="marginT20 marginB10 fontSize12 fl">
                <span class=" fontSize12">操作机构 ：</span>
                <input type="hidden" name="orgName" id="open1" value="${orgName}"/>
                <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                   onclick="openPop(1)">
                <#if orgName=null>请选择机构<#else >${orgName}</#if></a>
                <span class=" fontSize12 marginL20">操作菜单 ：</span>
                <a id="openPop2" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                   onclick="openPop(2)">
                <#if menuName=null>请选择菜单<#else >${menuName}</#if></a>
                <input type="hidden" name="menuName" id="open2" value="${menuName}"/>
            </div>
            <div class="marginT10 fontSize12">
                操作时间：
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="begin"
                       name="beginTime" value="${beginTime}"> 至
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end"
                       name="endTime" value="${endTime}">
                <lable class=" fontSize12 marginL20">操作类型：</lable>
                <select class="inputSty" name="operateType">
                    <option value="">请选择</option>
                <#if operateType == 1>
                    <option value="1" selected>增加</option>
                <#else >
                    <option value="1">增加</option>
                </#if>
                <#if operateType == 2>
                    <option value="2" selected>删除</option>
                <#else >
                    <option value="2">删除</option>
                </#if>
                <#if operateType == 3>
                    <option value="3" selected>修改</option>
                <#else >
                    <option value="3">修改</option>
                </#if>
                <#if operateType == 4>
                    <option value="4" selected>查询</option>
                <#else >
                    <option value="4">查询</option>
                </#if>
                <#if operateType == 5>
                    <option value="5" selected>导入</option>
                <#else >
                    <option value="5">导入</option>
                </#if>
                <#if operateType == 6>
                    <option value="6" selected>导出</option>
                <#else >
                    <option value="6">导出</option>
                </#if>
                <#if operateType == 7>
                    <option value="7" selected>打印</option>
                <#else >
                    <option value="7">打印</option>
                </#if>
                <#if operateType == 8>
                    <option value="8" selected>登陆</option>
                <#else >
                    <option value="8">登陆</option>
                </#if>
                </select>
            </div>
            <div class="marginT10 fontSize12" style="position: relative;">
            <#if someSelect=null>
                <span class="fuck">登录名或操作对象</span>
            </#if>
                <input id="key" name="someSelect" class="inputSty" value="${someSelect}"/>
                <input id="selectSome" type="button" class="sureBtn sureBtnEx " value="查  询"/>
                <input onclick="fsubmit()" type="button" class="sureBtn sureBtnEx marginL20 " value="导出记录"/>
            </div>
        </form>
    </div>

    <div class="listBox">
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">用户行为审计列表</caption>
            <tr class="firstTRFont">
                <td width="50">序号</td>
                <td width="150">机构</td>
                <td width="50">登录名</td>
                <td width="180">操作对象</td>
                <td width="80">原值</td>
                <td width="80">现值</td>
                <td width="150">菜单</td>
                <td width="50">操作类型</td>
                <td width="100">操作IP</td>
                <td width="120">操作时间</td>
                <td width="60">操作内容</td>
            </tr>
        <#list sysUserLogs as sys>
            <tr>
                <td>${(1 + sys_index) + (page.getPageSize() * page.getCurrentPage())}</td>
                <td>${sys.sysUserLogOrgName}</td>
                <td>${sys.sysUserLogUserName}</td>
                <td class="stoplength">${sys.sysUserLogEnterpriseCode}</td>
              
                <td class="stoplength"><a class="openmous"
                                          onmouseover="if($(this).text().split('').length>3){layer.tips('${sys.sysUserLogOldValue}', this,{time: 0});}"
                                          onmouseout="layer.closeAll('tips');">${sys.sysUserLogOldValue}</a></td>
                <td class="stoplength"><a class="openmous"
                                          onmouseover="if($(this).text().split('').length>3){layer.tips('${sys.sysUserLogNewValue}', this,{time: 0});}"
                                          onmouseout="layer.closeAll('tips');">${sys.sysUserLogNewValue}</a></td>
                <td>${sys.sysUserLogMenuName}</td>
                <td class="secondTD">
                    <#if sys.sysUserLogOperateType == 1>
                        增加
                    <#elseif sys.sysUserLogOperateType == 2>
                        删除
                    <#elseif sys.sysUserLogOperateType == 3>
                        修改
                    <#elseif sys.sysUserLogOperateType == 4>
                        查询
                    <#elseif sys.sysUserLogOperateType == 5>
                        导入
                    <#elseif sys.sysUserLogOperateType == 6>
                        导出
                    <#elseif sys.sysUserLogOperateType == 7>
                        打印
                    <#elseif sys.sysUserLogOperateType == 8>
                        登陆
                    </#if>
                </td>
                  <td class="stoplength">${sys.sysUserLogIp}</td>
                <td>${(sys.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                <td>
                    <a class="changeFont fontSize12 hasUnderline cursorPointer"
                       onclick="setLayer('查看用户行为审计','${request.getContextPath()}/admin/sysUserBehaviorAudit/show.jhtml?id=${sys.sysUserLogId}')">查看<br/>详情</a>
                </td>
            </tr>
        </#list>
        </table>
    <#if (sysUserLogs?? && sysUserLogs?size > 0)>
        <#include "/fragment/paginationbar.ftl"/>
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
    $(function () {
        for (var i = 0; i < $(".openmous").length; i++) {
            if ($(".openmous").eq(i).text().length > 3) {
                $(".openmous").eq(i).text($(".openmous").eq(i).text().substring(0, 3));
                $(".openmous").eq(i).html($(".openmous").eq(i).html() + '…');
            }
        }

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
        $("#max").height($(window).height() - 50);
    })
</script>
</body>
</html>