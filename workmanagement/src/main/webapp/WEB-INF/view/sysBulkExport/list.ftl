<!DOCTYPE html>
<html lang="en">
<head>
<#include "/fragment/common.ftl"/>
</head>
<body>
<#-- 弹出框 -->
<div id="covered"></div>  
<div id="poplayer">
    <div id="zbdl" class="hide borderBox">
        <div class="titleFont1">
            <span>指标大类</span>
        </div>
        <div class="listBox">
            <table id="zhibiao" cellpadding="0" cellspacing="0">

            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1(this)"/>
        </div>
    </div>
    <div id="jglb" class="hide borderBox">
        <div class="titleFont1">
            <span>机构列表</span>
        </div>
        <div class="listBox" style="overflow: auto;padding:10px 0;">
            <div class="zTreeDemoBackground left">
                <ul id="treeDemo" class="ztree"></ul>
            </div>
        </div>
        <p class="hide" id="hideorg"></p>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel3(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel3()"/>
        </div>
    </div>
    <div id="dqlb" class="hide borderBox">
        <div class="titleFont1">
            <span>地区列表</span>
        </div>
        <div class="listBox">
            <table>
            <#list sysAreaList as a>
                <tr>
                    <td name="${a.sysAreaId}" onclick="selUpstream(this)">
                        <#if a.sysAreaName?ends_with("县")>
                        <#else >
                            <#if (a.subArea?? && a.subArea?size > 0) >
                                <div id="0" class="open-shrink" onclick="openArea(this, ${a.sysAreaId})">
                                    <img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
                                </div>
                            </#if>
                        </#if>
                        <label class="fontSize12">${a.sysAreaName}</label>
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

<div class="eachInformationSearch">
    <div class="queryInputBox" style="margin-bottom:10px;margin-left: 10px;">
        <input type="button" value="导出全部指标大类" class="sureBtn" onclick="outAlldata()" style="width: 110px;"/>

    </div>

    <div class="listBox">
        <form action="${request.getContextPath()}/admin/sysBulkExport/outSomeData.jhtml" method="post" id="timeSub">
            <table cellpadding="0" cellspacing="0">
                <tbody>
                <tr>
                    <td width="100">
                        归档时间:&nbsp;&nbsp;
                        <input id="begin" class="laydate-icon inputSty fontSize12" style="width: 120px;" name="sTime">
                        ~
                        <input id="end" class="laydate-icon inputSty fontSize12" style="width: 120px;" name="eTime">
                    </td>
                    <td width="100">
                       <#-- 标识:&nbsp;&nbsp;
                        <span><input type="radio" class="checkbox verticalMiddle" name="netId" value="0"/><span
                                class="fontSize12">&nbsp;人行</span></span>
                        <span><input type="radio" class="checkbox verticalMiddle" name="netId" value="1"/><span
                                class="fontSize12">&nbsp;局域网</span></span>
                        <span><input type="radio" class="checkbox verticalMiddle" name="netId" value="2"/><span
                                class="fontSize12">&nbsp;互联网</span></span>-->
                    </td>
                </tr>
                <tr>
                    <td>
                    <input type="hidden" name="areaId" id="areaId" value="1"/>
                    <#-- <a id="zzqyA" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px"
                       onclick="openPop(2)">选择地区</a>-->
                        <lable>
                            指标大类:&nbsp;&nbsp;
                            <input type="hidden" name="indexId" id="indexId" value=""/>
                            <a id="szjgA" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px"
                               onclick="openPop(1)">选择指标大类</a>
                        </lable>
                    </td>
                    <td>

                        <lable>机构:&nbsp;&nbsp;
                            <input type="hidden" name="orgId" id="orgId" value=""/>
                            <a id="orgShow" href="javascript:" style="color:rgb(56,165,226);padding:5px;font-size:12px"
                               onclick="openPop(3)">选择机构</a>
                        </lable>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" style="text-align: center;">
                        <input type="button" value="导出" class="sureBtn sureBtnEx" onclick="timeChenck()"/>
                    </td>
                </tr>
                </tbody>
            </table>
        </form>
    </div>
</div>
</body>
<script type="text/javascript">

    var start = {
        elem: '#begin',
        format: 'YYYY-MM-DD',
        //min: laydate.now(), //设定最小日期为当前日期
        // max: laydate.now(), //最大日期
        istime: true,
        istoday: true,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#end',
        format: 'YYYY-MM-DD',
        //min: laydate.now(),
        //max: laydate.now(),
        istime: true,
        istoday: true,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);
    function outAlldata() {
        window.location.href = "${request.contextPath}/admin/sysBulkExport/outAllData.jhtml";
    }
    $(function () {
        //回显
        var msg = "${msg}";
        if (msg != "") {
            layer.alert(msg, {
                icon: ("导入成功" == msg ? 1 : 2),
                shade: 0.3
            });
        }
    });

    function timeChenck() {
        var time = $("#begin").val();
        if (time != "") {
            $("#timeSub").submit();
        } else {
            layer.alert("开始时间不能为空!");
        }
    }
    //显示弹出框
    function openPop(num) {
    	$("#covered").show();
        $("#poplayer").show();
        if (num == 1) {
            confirmSel2()
            var areaid = $("#areaId").val();
            if (areaid == "" || !areaid) {
                layer.alert("请先选择地区");
                $("#poplayer").hide();
                $("#covered").hide();
            } else {
                $("#zbdl").show();
                $("#dqlb").hide();
                $("#jglb").hide();
            }
        } else if (num == 2) {
            $("#zbdl").hide();
            $("#dqlb").show();
            $("#jglb").hide();
        } else if (num == 3) { //选择机构
            var areaid = $("#areaId").val();
            if (areaid == "" || !areaid) {
                layer.alert("请先选择地区");
                $("#poplayer").hide();
                 $("#covered").hide();
            } else {
                $("#zbdl").hide();
                $("#dqlb").hide();
                $("#jglb").show();
            }
        }
    }

    //关闭上级区域弹出框
    function closePop() {
    	
        $("#covered").hide();
        $("#poplayer").hide();
        if (navigator.userAgent.indexOf("MSIE 6.0") > 0) {
            $(".shouldHide").show();
        }
    }

    //选择对象
    function selUpstream(obj) {
        delSelFlag();
        $(obj).addClass("seleced");
    }


    //确认选择
    function confirmSel1(clear) {
        var seleced = $("#zbdl .seleced");
        closePop();
        var area = $(seleced.find("label")).text();
        if (clear == 1 || seleced.length == 0) {
            $("#szjgA").text("选择指标大类");
            $("#indexId").val("");
        } else {
            var value = seleced.attr("name");
            $("#szjgA").text(area);
            $("#indexId").val(value);
        }
    }

    //删除选择标记
    function delSelFlag() {
        $.each($(".seleced"), function () {
            $(this).removeClass("seleced");
        });
    }

    //确认选择
    function confirmSel2(clear) {
        var seleced = $("#dqlb .seleced");
        //closePop();

        var area = $(seleced.find("label")).text();
        if (clear == 1 || seleced.length == 0) {
            $("#zzqyA").text("选择地区");
            $("#areaId").val(1);
        } else {//表示是确定
            var value = 1;
            $("#zzqyA").text(area);
            $("#areaId").val(value);
        }
        var areaid = $("#areaId").val();
        $("#jgxzA").text("选择机构");
        $("#jgxzA").val("");
        var loading = layer.load();  
        $.post("${request.getContextPath()}/admin/sysBulkExport/getSysOrgByArea.jhtml",
                {"areaId": areaid}, function (data) {
                	layer.close(loading);
                    var str1 = [];
                    for (var i = 0; i < data.length; i++) {
                        var obj = new Object();
                        obj.id = data[i].id;
                        obj.name = data[i].name;
                        obj.pId = data[i].parent;
                        if(obj.id=='A80' || obj.pId=='A80')
                        {
                         str1.push(obj);
                        }
                    }
                    orgtype(str1);
         });
        $.post("${request.getContextPath()}/admin/sysBulkExport/getIndexTbByArea.jhtml", {"areaId": areaid}, function (data) {
            var html = "";
            for (var i = 0; i < data.length; i++) {
                html += '<tr><td level="1" name="' + data[i].indexId + '"' +
                        ' onclick="selUpstream(this)"><label>' + data[i].indexName + '</label></td></tr>';
            }
            $("#zhibiao").append(html);
        });
    }

    //确认选择
    function confirmSel3(clear) {
      closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					 $("#orgShow").text("选择机构");
         		$("#orgId").val("");
				
				} else { 
       		 $("#orgShow").text(htext);
         		$("#orgId").val(hteid); 
        }
    }
    /**********************************************************************************
     * 打开地区管理弹框（职责区域）
     * obj ： 被点击的元素（this）
     * id : 需要查询子地区的地区ID
     */
    function openArea(obj, id) {
        obj = $(obj);
        //子区域的缩进
        var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";

        if (obj.attr("id") == 0) { //未展开
            //显示正在加载子区域弹窗
            var tc_index = layer.load(0, {
                shade: [0.5, '#fff'] //0.1透明度的白色背景
            });
            //设置为展开状态
            obj.attr("id", 1);
            //将图标设置为展开图标
            $(obj.find("img")[0]).css("right", 5);

            //获取父区域的tr
            var ptr = obj.parent().parent();
            var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
            $.get(url, {_: Math.random(), id: id}, function (result) {
                //关闭加载提示弹窗
                layer.close(tc_index);
                if (result != null) {
                    var subs = result.subArea;
                    for (var i = 0; i < subs.length; i++) {
                        //子地区
                        var sub = subs[i];
                        //展开图标
                        var icon = "";
                        if (sub.subArea != null && sub.subArea.length != 0) {
                            icon = '<div id="0" class="open-shrink" onclick="openArea(this,' + sub.sysAreaId + ')">'
                                    + '<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
                                    + '</div>'
                        }

                        var tr = $("<tr name='" + sub.sysAreaId + "' class='" + id + "'></tr>");
                        var name = null;

                        if (sub.sysAreaType === "3") {
                            name = "<td name='" + sub.sysAreaId + "' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:" + spacing + "'>"
                                    + icon
                                    + "<label class='fontSize12'>" + sub.sysAreaName + "</label>"
                                    + "<input type='hidden' value='" + sub.sysAreaType + "' />"
                                    + "</td>";
                        } else {
                            name = "<td name='" + sub.sysAreaId + "' onclick='selUpstream(this)' style='padding-left:" + spacing + "'>"
                                    + icon
                                    + "<label class='fontSize12'>" + sub.sysAreaName + "</label>"
                                    + "<input type='hidden' value='" + sub.sysAreaType + "' />"
                                    + "</td>";
                        }
                        tr.append(name);
                        ptr.after(tr);
                    }
                }
            });
        } else { //已展开
            //设置为收缩状态
            obj.attr("id", 0);
            //将图标设置为收缩图标
            $(obj.find("img")[0]).css("right", 20);
            //删除子区域
            delSubArea(id);
        }
    }

    //删除子区域
    function delSubArea(id) {
        $.each($("." + id), function (i, v) {
            var pid = v.attributes.getNamedItem("name").nodeValue;
            //删除子区域
            $(this).remove();
            //递归删除子区域
            delSubArea(pid);
        });
    }
</script>
</html>