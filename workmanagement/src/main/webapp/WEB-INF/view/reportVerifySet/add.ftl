<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <title></title>
    <style type="text/css">
        .tdSty {
            padding: 2px 5px;
            border: 1px solid #dadada;
            height: 21px;
            font-size: 11px;
            width: 150px;
        }

        .eachInformationSearch .listBox table td {
            padding: 6px 5px;
        }

        .buttonVal {
            padding: 5px;
        }

        .addBG {
            cursor: auto;
            color: #139dd9;
        }

        .org_name {
            font-size: 11px;
            display: inline-block;
            *display: inline;
            *zoom: 1;
            padding: 1px 3px;
            border: 1px solid #CCCCCC;
            margin-right: 5px;
            margin-bottom: 3px;
        }

        .TYPE_name {
            font-size: 11px;
            padding: 2px;
            text-align: center;
            border: 1px solid #CCCCCC;
            margin-bottom: 3px;
        }

        * {
            font-size: 12px;
        }
    </style>
    <script>
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
                alertInfo(msg);
                $('.layui-layer-shade').height($(window).height());
                layer.close(loading);
            }
        });
        function openPop() {
            $("#covered").show();
            $("#poplayer").show();

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
        }

        //确认选择
        function confirmSel(clear) {
            var seleced = $(".seleced");
            closePop();
            var area = $(seleced.find("label")).text();
            if (clear == 1||seleced.length==0) {
                $("#openPop").text("选择指标大类");
            } else {
                $("#openPop").text(area);
                var indexId = $(seleced.find("label")).attr('id');
                var type = $("#check").val()

                $.post("${request.getContextPath()}/admin/reportVerifySet/getItems.jhtml", {
                    'indexId': indexId, 'type': type
                }, function (data) {
                    $("#add_zbx li").remove();
                    for (var i = 0; i < data.length; i++) {
                        $("#add_zbx").append("<li class='cursorPointer TYPE_name'    onclick='add(" + i + ")'>" + data[i].indexItemName + "</li>")
                    }
                });
                $('#add_text').val("");

                $('#indexId').val(indexId);//给隐藏框放入id
            }
        }
        $(function () {
            for (var i = 0; i < $(".buttonVal").length; i++) {
                $(".buttonVal").eq(i).attr("disabled", false)
            }
            if ($("#check").val() == 0) {
                for (var i = 0; i < $(".buttonVal").length - 3; i++) {
                    $(".buttonVal").eq(i).attr("disabled", "disabled")
                }
                $(".fuck").show();
            } else {
                $(".buttonVal").eq($(".buttonVal").length - 1).attr("disabled", "disabled")
                $(".fuck").hide();
            }

            $("#check").change(function () {
                for (var i = 0; i < $(".buttonVal").length; i++) {
                    $(".buttonVal").eq(i).attr("disabled", false)
                }
                if ($("#check").val() == 0) {
                    for (var i = 0; i < $(".buttonVal").length - 3; i++) {
                        $(".buttonVal").eq(i).attr("disabled", "disabled")
                    }
                    $(".fuck").show();
                } else {
                    $(".fuck").hide();
                    $(".buttonVal").eq($(".buttonVal").length - 1).attr("disabled", "disabled")
                }
                if ($('#indexId').val() != "") {
                    var indexId = $('#indexId').val();
                    var type = $("#check").val();
                    $.post("${request.getContextPath()}/admin/reportVerifySet/getItems.jhtml",
                            {'indexId': indexId, 'type': type}, function (data) {
                                $("#add_zbx li").remove();
                                for (var i = 0; i < data.length; i++) {
                                    $("#add_zbx").append("<li class='cursorPointer TYPE_name'    onclick='add(" + i + ")'>" + data[i].indexItemName + "</li>")
                                }
                            }
                    );
                    $('#indexId').val(indexId);//给隐藏框放入id
                }
                $('#add_text').val("");
            })

        });
        //确认选择
        function confirmSel1(clear) {
            var seleced = $(".seleced");
            closePop();
            var area = $(seleced.find("label")).text();
            $("#openPop1").text(area);
            var sysOrgId = $(seleced.find("label")).attr('id');
            $('#sysOrgId').val(sysOrgId);//给隐藏框放入id
        }

        function add(i) {
            $("#add_zbx li").eq(i).css("color", "#139dd9");
            var btnVal = $("#add_zbx li").eq(i).text();//trim函数，去掉空格
            $("#add_text").insertAtCaret(btnVal);

        }

        //插入光标方法
        (function ($) {
            $.fn.extend({
                insertAtCaret: function (myValue) {
                    var $t = $(this)[0];
                    if (document.selection) {
                        this.focus();
                        sel = document.selection.createRange();
                        sel.text = myValue;
                        this.focus();
                    } else if ($t.selectionStart || $t.selectionStart == '0') {
                        var startPos = $t.selectionStart;
                        var endPos = $t.selectionEnd;
                        var scrollTop = $t.scrollTop;
                        $t.value = $t.value.substring(0, startPos) + myValue + $t.value.substring(endPos, $t.value.length);
                        this.focus();
                        $t.selectionStart = startPos + myValue.length;
                        $t.selectionEnd = startPos + myValue.length;
                        $t.scrollTop = scrollTop;
                    } else {
                        this.value += myValue;
                        this.focus();
                    }
                }
            })
        })(jQuery);


        $(function () {
            //插入运算符 
            $(".buttonVal").click(function () {
                $("#add_text").insertAtCaret($(this).val());
            });

            $("#empty").click(function () {
                $('#add_text').val("");
            });
        });

        $(function () {
            //计算公式提交前的验证
            $("#checkValue").click(function () {
                var formula = $('#add_text').val();//文本框输入的内容
                var indexId = $('#indexId').val();
                var start = $('#start').val();

                if (start == "") {
                    layer.alert("请选择开始时间", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                if (!indexId) {
                    layer.alert("请先选择指标大类", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                if (!formula) {
                    layer.alert("请先填写公式", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                var type = $("#check").val();
                $.post("${request.getContextPath()}/admin/reportVerifySet/checkValue.jhtml", {
                    'indexId': indexId,
                    'formula': formula,
                    'type': type
                }, function (data) {
//						layer.close(loading);
                    if (data) {
                        layer.alert("验证通过", {icon: 1, shade: 0.3, shouldClose: true});
                        $("#submitBtn").show();
                    } else {
                        layer.alert("公式错误", {icon: 2, shade: 0.3, shouldClose: true});
                    }
                });
            });
            $('#submitBtn').click(function () {//点击提交
                var formula = $('#add_text').val();//文本框输入的内容
                var indexId = $('#indexId').val();
                var type = $("#check").val();
                $.post("${request.getContextPath()}/admin/reportVerifySet/checkValue.jhtml", {
                    'indexId': indexId,
                    'formula': formula,
                    'type': type
                }, function (data) {
                    if (data) {
                        $.post("${request.getContextPath()}/admin/reportVerifySet/submit.jhtml", $("#form").serialize(), function (data) {
                            layer.alert(data);
                            setTimeout(function () {
                                var index = parent.layer.getFrameIndex(window.name);//获取当前窗体索引
                                parent.window.location.href = "${request.getContextPath()}/admin/reportVerifySet/index.jhtml";
                                parent.layer.close(index); //执行关闭
                            }, "2000");
                        });
                    } else {
                        layer.alert("公式错误", {icon: 2, shade: 0.3, shouldClose: true});
                    }
                });
            });
        });


    </script>
</head>
<body>
<#-- 弹出框 -->
<div id="covered"></div>
<div id="poplayer" style="position: fixed;">
    <div class="borderBox zblb">
        <div class="titleFont1">
            <span>指标列表</span>
        </div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0">
            <#list indexTbList as li>
                <tr>
                    <td level="1" onclick="selUpstream(this)">
                        <label id="${li.indexId}">${li.indexName}</label>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn hide" onclick="confirmSel(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel()"/>
        </div>
    </div>
</div>
<div class="showListBox">
    <form id="form" method="post">
        <input type="hidden" id="indexId" name="indexId" value="${indexTb.indexId}">
        <input type="hidden" id="sysCheckId" name="sysCheckId" value="${sysCheck.sysCheckId}">
        <table cellpadding="0" cellspacing="0">
            <tbody id="table">
            <tr>
                <td width="150">
                    归档时间：
                    <input class="laydate-icon inputSty" style="width: 120px;display: inline-block;" id="start"
                           name="sTime" value="${(sysCheck.sysCheckSDate?string("yyyy-MM-dd"))!}"/>至
                    <input class="laydate-icon inputSty" style="width: 120px;display: inline-block;" id="end"
                           name="eTime" value="${(sysCheck.sysCheckEDate?string("yyyy-MM-dd"))!}"/>
                    <label>
                        <select class="inputSty" id="check">
                        <#if type == 0>
                            <option value="1">数值校验</option>
                            <option value="0" selected>字符校验</option>
                        <#else >
                            <option value="1">数值校验</option>
                            <option value="0">字符校验</option>
                        </#if>
                        </select>
                    </label>
                </td>
                <td width="150">
                    <div class="marginT10">
                        <span class=" fontSize12">指标大类 ：</span>
                        <a id="openPop" class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                           onclick="openPop(0)">
                        <#if indexTb.indexName??>${indexTb.indexName}<#else >请选择指标大类</#if>
                        </a>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div style="width: 100%;height: 150px;display: inline-block;*zoom:1;*display:inline;position:relative;">
                    	<span class="fuck">
                    		例(1):AandB: A 不能空并且 B 也不能空<br/>
							例(2):AorB:A 不能空或者 B 不能空<br/>
							例(3):AandBorC:以 or 为界分成2部分,or 左边的或右边至少一边通过校验<br/>
							例(4):$A:A 的值必须是正规的身份证号码<br/>
                    	</span>
                        <textarea id="add_text" class="inputOtherCondition" name="formula" rows="5" cols="50"
                                  class="fontSize12 textareaSty"
                                  style="border:1px solid #dadada;height:130px;padding:10px;width: 80%;">${sysCheck.sysCheckFormula}</textarea>
                    </div>
                </td>
                <td rowspan="2">
                    <div style="width: 100%;height: 30px;line-height: 30px;border-bottom:1px solid #03B3F6;text-align: center;">
                        指标项
                    </div>
                    <div style="width: 100%;height: 170px;overflow: auto;">
                        <ul id="add_zbx">
                        <#list indexItemTbList as iit>
                            <li class='cursorPointer' onclick='add(${iit_index})'>${iit.indexItemName}</li>
                        </#list>
                        </ul>
                    </div>
                </td>
            </tr>
            <tr>
                <td>
                    <div>
                        <input class="buttonVal" type="button" value=" + "/>
                        <input class="buttonVal" type="button" value=" - "/>
                        <input class="buttonVal" type="button" value=" * "/>
                        <input class="buttonVal" type="button" value=" / "/>&nbsp;&nbsp;&nbsp;
                        <input class="buttonVal" type="button" value=" = "/>
                        <input class="buttonVal" type="button" value=" >= "/>
                        <input class="buttonVal" type="button" value=" <= "/>
                        <input class="buttonVal" type="button" value=" &lt "/>
                        <input class="buttonVal" type="button" value=" &gt "/>&nbsp;&nbsp;&nbsp;
                        <input class="buttonVal" type="button" value=" ( "/>
                        <input class="buttonVal" type="button" value=" ) "/>&nbsp;&nbsp;&nbsp;
                        <input class="buttonVal" type="button" value=" != "/>
                        <input class="buttonVal" type="button" value=" and "/>
                        <input class="buttonVal" type="button" value=" or "/>&nbsp;&nbsp;&nbsp;
                        <input class="buttonVal" type="button" value=" $ "/>
                        <span style='font-size:12px;color:#787878; '>(如果应用于身份证校验请在指标项前面加$)</span>
                    </div>
                    <div style="margin-top: 5px;">
                        <input class="sureBtn sureBtnEx " type="button" id="empty"
                               value="清空"/>
                    </div>
                </td>
            </tr>
            <tr>
                <td colspan="2">
                    <input id="checkValue" type="button" class="sureBtn sureBtnEx" value="验证"/>
                </td>
            </tr>
            <tr>
                <td colspan="2">说明:</td>
            </tr>
            <tr>
                <td colspan="2"> 
					<textarea name="sysCheckExplain" class="fontSize12 textareaSty"
                              maxlength="50" style="border:1px solid #dadada;height:30px;padding:10px"
                    >${sysCheck.sysCheckExplain}</textarea>
                </td>
            </tr>
            </tbody>
        </table>
        <div class="showBtnBox">
            <input type="button" class="cancleBtn closeThisLayer" value="取 消"/>
            <input id="submitBtn" type="button" class="sureBtn hide" value="确 认"/>
        </div>
    </form>
</div>

<script type="text/javascript">
    $(function () {
    	
	        if ($(".inputOtherCondition").val() != "") {
	            $(".fuck").hide();
	        }
	        $(".inputOtherCondition").focus(function () {
	            $(".fuck").hide();
	        }).blur(function () { 
		            if ($.trim($(this).val()) == "" &&$("#check").val() == 0) {
		                $(".fuck").show();
		            } 
	        });
	        $(".fuck").click(function () {
	            $(".inputOtherCondition").focus();
	        })
	      
    });
    function openInstitutions(obj, id) {
        obj = $(obj);
        //子区域的缩进
        var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
        if (obj.attr("id") == 0) { //未展开
            //设置为展开状态
            obj.attr("id", 1);
            //将图标设置为展开图标
            $(obj.find("img")[0]).css("right", 5);
            var loading = wait();
            //获取父区域的tr
            var ptr = obj.parent().parent();
            var url = "${request.getContextPath()}/admin/sysOrgType/getSubSysOrgTypes.jhtml";
            $.get(url, {_: Math.random(), typeId: id}, function (result) {
                layer.close(loading);
                if (result != null) {
                    var subs = result.subSysOrg;
                    for (var i = 0; i < subs.length; i++) {
                        //子地区
                        var sub = subs[i];
                        //展开图标
                        var icon = "";
                        if (sub.subSysOrgType != null && sub.subSysOrgType.length != 0) {
                            icon = '<div id="0" class="open-shrink" onclick="openInstitutions(this,' + sub.sys_org_type_id + ')">'
                                    + '<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
                                    + '</div>'
                        }
                        var name = "<tr><td name='" + sub.sys_org_type_id + "' onclick='selUpstream(this)' style='padding-left:" + spacing + "'>"
                                + icon
                                + "<label class='fontSize12 pointer'>" + sub.sys_org_type_name + "</label>"
                                + "</td></tr>";
                        ptr.after(name);
                    }
                }
            });
        } else { //已展开
            //设置为收缩状态
            obj.attr("id", 0);
            //将图标设置为收缩图标
            $(obj.find("img")[0]).css("right", 20);
            //删除子区域
            delInstitutions(id);
        }
    }

    //删除机构类型管理子区域
    function delInstitutions(id) {
        $.each($("." + id), function (i, v) {
            var pid = v.attributes.getNamedItem("name").nodeValue;
            //删除子区域
            $(this).remove();
            //递归删除子区域
            delInstitutions(pid);
        });
    }
    $(function () {
        var index = parent.layer.getFrameIndex(window.name);//获取当前窗体索引		
        $('.closeThisLayer').on('click', function () {
        <#--parent.window.location.href = "${request.getContextPath()}/admin/reportVerifySet/index.jhtml";-->
            parent.layer.close(index); //执行关闭
        });
        var start = {
            elem: '#start',
            format: 'YYYY-MM-DD',
            //min: laydate.now(), //设定最小日期为当前日期
            //max: '2099-06-16 23:59:59', //最大日期
            istime: true,
            choose: function (datas) {
                end.min = datas; //开始日选好后，重置结束日的最小日期
                end.start = datas; //将结束日的初始值设定为开始日
            }
        };
        var end = {
            elem: '#end',
            format: 'YYYY-MM-DD',
            //min: laydate.now(),
            //max: '2099-06-16 23:59:59',
            istime: true,
            choose: function (datas) {
                start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        laydate(start);
        laydate(end);
    });
</script>
</body>
</html>
