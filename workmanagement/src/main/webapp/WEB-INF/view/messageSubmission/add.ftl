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
                }, function () {
                    if (msg == "上报成功") {
                        var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
                        parent.layer.close(index); //执行关闭
                        parent.window.location.href = "${request.getContextPath()}/admin/messageSubmission/list.jhtml";
                    } else {
                        window.location.href = "${request.getContextPath()}/admin/messageSubmission/add.jhtml";
                    }
                });


            }

            $("#gdTime").click(function () {
                setLayDate("#gdTime");
            });
            //判定是否为IE6浏览器,是的话,那就改变宽度吧
            if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
                $("#sureWidth").width(550 + "px");
            }
            //上传验证
            $("#upLoad").submit(function () {
                var fileVal = $("[name='file']").val();
                var timeValue = $("[name=time]").val();
                var indexId = $('#openPop1').attr('name');
                var myDate = getNowFormatDate();
                var operateAuthFile=$("#operateAuthFile").val();
                var reg = /(.*).(txt)$/;
				 
                if (indexId == "" || indexId == undefined) {
                    layer.alert("请选择要报送的指标大类", {
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
                } else if (!reg.test(operateAuthFile)) {
                    $('.layui-layer-shade').height($(window).height());
                    layer.alert("上传文件不是txt格式", {
                        icon: 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                    return false;
                }
                loading = layer.load();
            });
        });

        //下载excel模板
        function downLoad() {
            var indexId = $('#openPop1').attr('name');
            if (indexId) {
                window.location.href = "${request.getContextPath()}/admin/messageSubmission/downLoad.jhtml?indexId=" + indexId;
            } else {
                layer.alert("请先选择指标大类", {
                    icon: 2,
                    shade: 0.3,
                    shadeClose: true
                });
            }
        }

        //指标大类弹出
        function openPop() {
            $("#poplayer").show();
            $("#covered").show();
        }

        //关闭上级区域弹出框
        function closePop() {
            $("#poplayer").hide();
            $("#covered").hide();
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
            var seleced = $(".seleced");

            closePop();
            $("#openPop1").text(area);
            var area = $(seleced.find("label")).text();
            if (clear == 1 || seleced.length == 0) {
                $("#openPop1").text("请选择指标大类");
            } else {
                $("#openPop1").text(area);
            }
        }
    </script>
    <title>地区列表</title>
</head>
<body>
<#-- 弹出框 -->
	<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox ">
        <div class="titleFont1">
            <span>指标大类列表</span>
        </div>
        <div></div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0">
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
</div>
<#-- 新增框 -->
<div class="showListBox">
	
    <form id="upLoad" method="post" enctype="multipart/form-data"
          action="${request.getContextPath()}/admin/messageSubmission/upLoad.jhtml">
        <p style="font-size: 11px;color: #868686;text-align: left;margin:10px auto;width: 96%;">
            数据之间用"|"分隔，支持后台去除文字前后(不包括中间)的多个"Tab"以及"空格"，没有数据的空缺请用按键"空格"代替不能使用"Tab"代替，如果最后一列没有数据则需要使用"空格"占位在那里再在后面添加一个"|"正确格式如："数据|数据|&nbsp;&nbsp;&nbsp;|"(为3列数据)或者"数据|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;数据"(为3列数据)</p>
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">
                新增报文报送
            </caption>
            <tr>
                <td class="noBorderL firstTD" width="200">指标大类：</td>
                <td class="secondTD" width="400">
                    <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                       onclick="openPop()">请选择指标大类</a>
                </td>
            </tr>
            <tr>
                <td class="noBorderL firstTD" width="200">归档时间：</td>
                <td width="400">
                    <input id="gdTime" name="time" autocomplete="off" class="laydate-icon inputSty fontSize12">
                </td>
            </tr>
            <tr>
                <td class="noBorderL firstTD" width="200">上传文件：</td>
                <td width="400">
                    <input id="operateAuthFile" type="file" name="file" class="inputSty"/>
                </td>
            </tr>
        </table>
        <div class="showBtnBox">
            <input type="button" class="cancleBtn closeThisLayer" value="取 消"/>
            <input type="submit" class="bigBtn" value="提交报送内容" style="margin-left: 0px;"/>
            <input type="button" onclick="downLoad()" class="sureBtn sureBtnEx marginL20" value="下载模板"/>
        </div>
    </form>
</div>
</body>

<script>
    var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
    $('.closeThisLayer').on('click', function () {
        parent.layer.close(index); //执行关闭
    });
    var sysAreaNameold = $("#sysAreaName").val();
    var sysAreaCodeold = $("#sysAreaCode").val();
    var sysAreaNotesold = $("textarea[name='sysAreaNotes']").val();
    $("#submitBtn").click(function () {

        var sysAreaName = $("#sysAreaName").val();
        var sysAreaCode = $("#sysAreaCode").val();
        var sysAreaNotes = $("textarea[name='sysAreaNotes']").val();

    <#if poptype != "add" >
        if (sysAreaNameold == sysAreaName && sysAreaCodeold == sysAreaCode && sysAreaNotesold == sysAreaNotes) {
            parent.layer.close(index); //执行关闭
            return false;
        }
    </#if>
        if (checkChineseNoSpe(sysAreaName) == 1) {
            layer.alert("区域名称不能为空", {icon: 2, shade: 0.3, shouldClose: true});
            $("#sysAreaName").focus();
            return false;
        }
        if (checkChineseNoSpe(sysAreaName) == 0) {
            layer.alert("区域名称输入不合法", {icon: 2, shade: 0.3, shouldClose: true});
            $("#sysAreaName").focus();
            return false;
        }
        if (administrativeCode(sysAreaCode) == 1) {
            layer.alert("行政代码不能为空", {icon: 2, shade: 0.3, shouldClose: true});
            $("#sysAreaCode").focus();
            return false;
        }
        if (administrativeCode(sysAreaCode) == 0) {
            layer.alert("行政代码输入不合法", {icon: 2, shade: 0.3, shouldClose: true});
            $("#sysAreaCode").focus();
            return false;
        }

        var loading = layer.load();
        $.post("${request.getContextPath()}/admin/sysArea/save.jhtml?poptype=${poptype}", $("#form").serialize(), function (data) {
            layer.close(loading);
            var index = alertInfoFun(data.message, data.flag, function () {
                if (data.flag) {
                    layer.load();
                    parent.window.location.href = "${request.getContextPath()}/admin/sysArea/list.jhtml";
                }
                layer.close(index);
            });
        });

    });

    if ($(".inputOtherCondition").val() != "") {
        $(this).next(".fuck").hide();
    }
    $(".inputOtherCondition").focus(function () {
        $(this).next(".fuck").hide();
    }).blur(function () {
        if ($.trim($(this).val()) == "") {
            $(this).next(".fuck").show();
        }
    });
    $(".fuck").click(function () {
        $(this).prev(".inputOtherCondition").focus();
    })
</script>
</html>
