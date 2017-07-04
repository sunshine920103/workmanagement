<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <style type="text/css">
        .coda4a4a4 {
            background: #ff0000;
        }

        .blue139dd9 {
            background: #139dd9;
        }

    </style>
    <script type="text/javascript">
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
                layer.alert(msg);
            }

            $(".groupList").height($(window).height() - 50);

            if ($(".inputOtherCondition").val() != "") {
                $(".fuck").hide();
            }
            $(".inputOtherCondition").focus(function () {
                $(".fuck").hide();
            }).blur(function () {
                if ($.trim($(this).val()) == "") {
                    $(".fuck").show();
                }
            });
            $(".fuck").click(function () {
                $(".inputOtherCondition").focus();
            })

        });
        window.onresize = function () {
            $(".groupList").height($(window).height() - 50);
            $('.layui-layer-shade').height($(window).height());
        };
        $(function () {
            $(".groupList").height($(window).height() - 50);

            if ($(".inputOtherCondition").val() != "") {
                $(".fuck").hide();
            }
            $(".inputOtherCondition").focus(function () {
                $(".fuck").hide();
            }).blur(function () {
                if ($.trim($(this).val()) == "") {
                    $(".fuck").show();
                }
            });
            $(".fuck").click(function () {
                $(".inputOtherCondition").focus();
            })
        });


        //删除
        function del(obj) {
            layer.confirm('你确定要删除吗？', {
                btn: ['确定', '取消'], //按钮
            }, function () {
                $.post("${request.getContextPath()}/admin/reportVerifySet/delOne.jhtml", {'sysCheckId': obj}, function (msg) {
                    layer.msg(msg, {icon: 1});
                    setTimeout(function () {
                        window.location.href = "${request.getContextPath()}/admin/reportVerifySet/index.jhtml";
                    }, "2000");
                });

            });
        }
        $(function () {
            var openclose2 = $("#openclose2").val();
            var openclose1 = $("#openclose1").val();
            if (openclose1 == "关 闭") {
                $("#openclose1").addClass("coda4a4a4")
            } else {
                $("#openclose1").addClass("blue139dd9")
            }
            if (openclose2 == "关 闭") {
                $("#openclose2").addClass("coda4a4a4")
            } else {
                $("#openclose2").addClass("blue139dd9")
            }
            $("#openclose1").click(function () {
                var openclose1 = $("#openclose1").val();
                $.post("${request.getContextPath()}/admin/reportVerifySet/changeCheck.jhtml", {"type": 0},
                        function () {
                            if (openclose1 == "关 闭") {
                                $("#openclose1").removeClass("coda4a4a4")
                                $("#openclose1").val("开 启");
                                $("#openclose1").addClass("blue139dd9")
                            } else {
                                $("#openclose1").removeClass("blue139dd9")
                                $("#openclose1").val("关 闭");
                                $("#openclose1").addClass("coda4a4a4")
                            }
                            layer.msg("操作成功", {icon: 1});
                        });
            });
            $("#openclose2").click(function () {
                var openclose2 = $("#openclose2").val();
                $.post("${request.getContextPath()}/admin/reportVerifySet/changeCheck.jhtml", {"type": 1},
                        function () {
                            if (openclose2 == "关 闭") {
                                $("#openclose2").removeClass("coda4a4a4")
                                $("#openclose2").val("开 启");
                                $("#openclose2").addClass("blue139dd9")
                            } else {
                                $("#openclose2").removeClass("blue139dd9")
                                $("#openclose2").val("关 闭");
                                $("#openclose2").addClass("coda4a4a4")
                            }
                            layer.msg("操作成功", {icon: 1});
                        });
            });
        })
    </script>
    <title>校验</title>
</head>
<body>
<div class="userManageBox eachInformationSearch ">
    <div class="queryInputBox">
        <div class="verticalMiddle">
            <input style="margin-left:30px ;"
                   onclick="setLayer('新增校验','${request.getContextPath()}/admin/reportVerifySet/addOrUpdate.jhtml');$('.layui-layer-shade').height($(window).height());"
                   type="button" value="新增校验" class="sureBtn sureBtnEx"/>
        <#if isAdmin??>
        <#--超级管理员能看到的按钮-->
            <label style="margin-left:30px ;">
                统一社会信用代码校验:
            </label>
            <input style="margin-left:0px ;" name="1" id="openclose1" type="button"
                   value="${(isOpenCodeCredit=='true')?string("关 闭","开 启")}"
                   class="sureBtn sureBtnEx"/>
            <label style="margin-left:30px">
                组织机构代码校验:
            </label>
            <input style="margin-left:0px ;" name="1" id="openclose2" type="button"
                   value="${(isOpenCodeOrg=='true')?string("关 闭","开 启")}"
                   class="sureBtn sureBtnEx">
        </#if>
        </div>


        <div class="listBox">
            <table cellpadding="0" cellspacing="0">
                <caption class="titleFont1 titleFont1Ex">校验列表</caption>
                <tbody>
                <tr class="firstTRFont">
                    <td width="30">序号</td>
                    <td width="150">指标大类</td>
                    <td width="150">校验值</td>
                    <td width="250">公式</td>
                    <td width="120">添加时间</td>
                    <td width="120">校验有效期</td>
                    <td width="120">说明</td>
                    <td width="150">操作</td>
                </tr>
                <#list sysCheckList as sys>
                    <#if sys.sysCheckItems =='崇左市:基本信息'|| sys.sysCheckItems =='崇左市:行政处罚信息'||sys.sysCheckItems =='崇左市:行政许可信息'>
                <tr>
                    <td>${sys_index+1}</td>
                    <td>${sys.sysCheckItems}</td>
                    <td>${sys.sysCheckCont}</td>
                    <td>${sys.sysCheckFormula}</td>
                    <td>
                        <span>${(sys.sysCheckCTime?string("yyyy-MM-dd hh:mm:ss"))!}</span>
                    </td>
                    <td>
                        <span>${(sys.sysCheckSDate?string("yyyy-MM-dd"))!}</span>
                        ~
                        <span>
                            <#if sys.sysCheckEDate??>
                            ${(sys.sysCheckEDate?string("yyyy-MM-dd"))!}
                            <#else >
                                永久
                            </#if>
                    </span>
                    </td>
                    <td>${sys.sysCheckExplain}</td>
                    <td>
                        <#if sys.sysCheckCTime?? >
                        <#--如果有值表示是本地区的-->
                            <a class="changeFont fontSize12 cursorPointer hasUnderline"
                               onclick="setLayer('修改校验','${request.getContextPath()}/admin/reportVerifySet/addOrUpdate.jhtml?sysCheckId=${sys.sysCheckId}');
                                       $('.layui-layer-shade').height($(window).height());">修 改</a>

                            <a class="delFont fontSize12 cursorPointer hasUnderline"
                               onclick="del(${sys.sysCheckId})">删 除</a>
                        <#else >
                            <span class=" fontSize12 hasUnderline "
                                  style="color:#787878;text-decoration:none;">修 改</span>
                            <span class=" fontSize12 hasUnderline "
                                  style="color:#787878;text-decoration:none;">删 除</span>
                        </#if>
                    </td>
                </tr>
                    </#if>
                </#list>
                </tbody>
            </table>
        <#if (sysCheckList?? && sysCheckList?size > 0)>
            <#include "/fragment/paginationbar.ftl"/>
        </#if>
        </div>
    </div>
</div>
<form id="searchForm" method="post" action="${request.getContextPath()}/admin/reportVerifySet/index.jhtml">
    <input type="hidden" name="beginTime" value="${beginTime}"/>
    <input type="hidden" name="endTime" value="${endTime}"/>
    <input type="hidden" name="operateType" value="${operateType}"/>
    <input type="hidden" name="someSelect" value="${someSelect}">
    <input type="hidden" name="orgName" value="${orgName}">
    <input type="hidden" name="menuName" value="${menuName}">
</form>
</body>

</html>
