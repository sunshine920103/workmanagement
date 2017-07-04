<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
    
     
			
	
        //删除
        function del(dicContentId, obj) {
            layer.confirm('确定删除吗？', {
					  btn: ['确定','取消'] //按钮
					},function(){
						if (dicContentId == null || dicContentId == "") {//删除不是数据库数据的操作
	                    $(obj).parents("tr").remove();
	                } else {//删除是数据库的数据的操作
	                    var dicId = $("input[name=ddpid]").val();
	                    $.post("${request.contextPath}/admin/dic/isDelete.jhtml", {
	                        'dicContentId': dicContentId,
	                        "dicId": dicId
	                    }, function (result) {
	                       
	                        if (result.isIng == true) {
	                            layer.alert("操作成功",{icon:1,shade:0.3,shouldClose:true});
	                            $(obj).parents("tr").remove();
	                            return false;
	                        } else if(result.isIng == false){
	                             layer.alert(result.msg,{icon:2,shade:0.3,shouldClose:true});
	                              return false;
	                        }
	                    });
	                }
					})
        }
		
		 //修改
        //修改
        function Alter(dicContentId, obj) {
           layer.confirm('确定修改吗？', {
					  btn: ['确定','取消'] //按钮
					},function(){
						if (dicContentId == null || dicContentId == "") {//删除不是数据库数据的操作
	                    	 
		                } else {//删除是数据库的数据的操作 
		                	 var dicContentValue=$(obj).parent().parent().find('input').val();
		                    var dicId = $("input[name=ddpid]").val();
		                 
		                    $.post("${request.contextPath}/admin/dic/updDicContent.jhtml", {
		                        'contentId': dicContentId,
		                        "dicId": dicId,
		                        "dicContentValue":dicContentValue
		                    }, function (result) {
		                    	if(result=='操作成功'){
		                    	   layer.alert(result,{icon:1,shade:0.3,shouldClose:true});
		                    	}else{
		                    	   layer.alert(result,{icon:2,shade:0.3,shouldClose:true});
		                    	}
		                    });
		                }
					})
        }
		
		 
        var isError = false;
        $(function () {
            //回显
            var msg = "${msg}";
            if (msg != "") {
                layer.alert(msg, {
                    icon: (msg == "操作成功") ? 1 : 2,
                    shade: 0.3,
                    shadeClose: true
                });
                window.location.href = "${request.contextPath}/admin/dic/list.jhtml";
            }
            //新增
            $("#addDicCode").click(function () {
                var obj = $(this);
                var ddpid = $("input[name=ddpid]").val();
                var dicContentCode = $.trim($(this).parent().prev().find("input").val());
                var dicContentValue = $.trim($(this).parent().prev().prev().find("input").val());
                //先检查输入的值是否符合规定
                if (checkChineseNoSpe(dicContentValue) == 0) {
                    layer.alert("指标值输入不合法", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                if (checkStrAddNum(dicContentCode) == 0) {
                    layer.alert("字典代码输入不合法", {icon: 2, shade: 0.3, shouldClose: true});
                    return false;
                }
                //都符合规定之后进行后台验证
                if (dicContentCode != "" && dicContentValue != "") {
                    if (ddpid != "") {//如果这不是新增操作
                        $.post("${request.contextPath}/admin/dic/addVer.jhtml", {
                            code: dicContentCode,
                            ddpid: ddpid,
                            dicContentValue: dicContentValue
                        }, function (result) {
                            if (result.addVer == false) {//发现和数据库有重名
                                if (result.code == false) {
                                    layer.alert("字典代码唯一，不允许重复", {
                                        icon: (msg == "操作成功") ? 1 : 2,
                                        shade: 0.3,
                                        shadeClose: true
                                    });
                                } else {
                                    layer.alert("指标值唯一，不允许重复", {
                                        icon: (msg == "操作成功") ? 1 : 2,
                                        shade: 0.3,
                                        shadeClose: true
                                    });
                                }
                            } else {//发现和数据库没有重名
                                $("[name='dicContentValue']").each(function () {
                                    if (dicContentValue == $.trim($(this).val())) {
                                        isError = true;
                                        layer.alert("指标值唯一，不允许重复", {
                                            icon: (msg == "操作成功") ? 1 : 2,
                                            shade: 0.3,
                                            shadeClose: true
                                        });
                                        return false;
                                    } else {
                                        isError = false;
                                    }
                                });
                                if (isError == false) {
                                    $("[name='dicContentCode']").each(function () {
                                        if (dicContentCode == $.trim($(this).val())) {
                                            isError = true;
                                            layer.alert("字典代码唯一，不允许重复", {
                                                icon: (msg == "操作成功") ? 1 : 2,
                                                shade: 0.3,
                                                shadeClose: true
                                            });
                                            return false;
                                        } else {
                                            isError = false;
                                        }
                                    });
                                }
                                if (isError == false) {
                                    var html = "";
                                    html += "<tr><td><input class='inputSty' type='text' name='dicContentValue' value=" + dicContentValue + "></td><td><input class='inputSty' type='text' name='dicContentCode' value=" + dicContentCode + "></td><td><a class='delFont fontBold cursorPointer hasUnderline fontSize12' href='javascript:void(0)' onclick=" + "" + "del('${item.id}',this)" + "" + ">删 除</a></td></tr>";
                                    obj.parent().parent().before(html);
                                }
                            }
                        });
                    } else {//这是修改操作
                        $("[name='dicContentValue']").each(function () {
                            if (dicContentValue == $.trim($(this).val())) {
                                isError = true;
                                layer.alert("指标值唯一，不允许重复", {
                                    icon: (msg == "操作成功") ? 1 : 2,
                                    shade: 0.3,
                                    shadeClose: true
                                });
                                return false;
                            } else {
                                isError = false;
                            }
                        });
                        if (isError == false) {
                            $("[name='dicContentCode']").each(function () {
                                if (dicContentCode == $.trim($(this).val())) {
                                    isError = true;
                                    layer.alert("字典代码唯一，不允许重复", {
                                        icon: (msg == "操作成功") ? 1 : 2,
                                        shade: 0.3,
                                        shadeClose: true
                                    });
                                    return false;
                                } else {
                                    isError = false;
                                }
                            });
                        }
                        if (isError == false) {
                            var html = "";
                            html += "<tr><td><input class='inputSty' type='text' name='dicContentValue' value=" + dicContentValue + "></td><td><input class='inputSty' type='text' name='dicContentCode' value=" + dicContentCode + "></td><td><a class='delFont fontBold cursorPointer hasUnderline fontSize12' href='javascript:void(0)' onclick=" + "" + "del('${item.id}',this)" + "" + ">删 除</a></td></tr>";
                            obj.parent().parent().before(html);
                        }
                    }
                } else {
                    layer.alert("请填写完整", {
                        icon: (msg == "操作成功") ? 1 : 2,
                        shade: 0.3,
                        shadeClose: true
                    });
                }
                $(this).parent().prev().find("input").val("");
                $(this).parent().prev().prev().find("input").val("");
            });
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑>
            var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引

            $('.closeThisLayer').on('click', function () {
                parent.layer.close(index); //执行关闭
            });

            $("#formVer").click(function () {
                var name = $.trim($("input[name=dicName]").val());
                var ddpid = $("input[name=ddpid]").val();
                if (name == "") { 
                    layer.alert("请输入字典名称",{icon:2,shade:0.3,shouldClose:true});
                    return false;
                } 
                if (inputname(name) == 0) {
                    layer.alert("字典名称不合法",{icon:2,shade:0.3,shouldClose:true});
					return false;
                }
                if (checkChineseNoSpe50(ddpid) == 0) {
                    layer.alert("备注不合法",{icon:2,shade:0.3,shouldClose:true});
					return false;
                }
                var lastname = $("#addData tr:last").find("input:eq(0)").val();
                var lastCode = $("#addData tr:last").find("input:eq(1)").val();

                if (lastCode != "" && lastname == "") {
                    layer.alert("指标值不能为空",{icon:2,shade:0.3,shouldClose:true});
					return false;
                }
                
              
                
                if (lastname != "" && lastCode == "") {
                    layer.alert("字典代码不能为空",{icon:2,shade:0.3,shouldClose:true});
                    return false;
                }
                if (lastname != "" && lastCode != "") {
                    $("#addDicCode").click();
                } else {
                    if (lastname == "" && lastCode == "") {
                        var loading = layer.load();
                        $.post("${request.contextPath}/admin/dic/editSubmit.jhtml", $("#addData").serialize(), function (data) {
                            layer.close(loading);
                            var index = alertInfoFun(data.message, data.flag, function () {
                                if (data.flag) {
                                    parent.window.location.href = "${request.getContextPath()}/admin/dic/list.jhtml";
                                }
                                layer.close(index);
                            });
                        });
                    }
                }
            });
        });
    </script>
    <title>编辑数据字典</title>
</head>
<body>
<!--修改-->
<div>
    <div class="showListBox">

        <form id="addData" method="post" action="${request.contextPath}/admin/dic/editSubmit.jhtml">
            <input name="ddpid" type="text" value="${dic.dicId}" style="display: none;"/>

            <table id="edit" cellpadding="0" cellspacing="0">
            <#if dic == null>
                <caption class="titleFont1 titleFont1Ex">新增字典</caption>
            <#else>
                <caption class="titleFont1 titleFont1Ex">修改字典</caption>
            </#if>
                <tr>
                    <td width="50" class="noBorderL firstTD">
                    	<label class="mainOrange"> * </label>字典名称:
                    </td>
                    <td width="150" colspan="2" class="secondTD">
                    	<input class='inputSty' name="dicName" type="text"  value="${dic.dicName}"  onblur="onblurVal(this,30,50)"  title="必填，不能超过50个字" maxlength="50"/>
                    	<span style='font-size:12px;color:#787878;margin-left:10px '>（不能输入以数字开头且除了 ( ) + - _ 以外的其它特殊字符）</span>
                    </td>
                </tr>
                <tr>
                    <td class="noBorderL firstTD">备注:</td>
                    <td colspan="2" class="secondTD">
                        <input class='inputSty allnoteVal' name="dicNotes" type="text"  onblur="onblurVal(this,14,50)"   value="${dic.dicNotes}"  maxlength="50"/>
                    </td>
                </tr>
                <tr>
                    <td class="warmFont fontSize12 textCenter" colspan="3">注：字典代码和指标值必须是唯一，无法删除使用的指标值。</td>
                </tr>
                <tr>
                    <td style="width:40%;"><span>指标值 </span></td>
                    <td style="width:40%;"><span>字典代码 </span></td>
                    <td style="width:20%;">操作</td>
                </tr>

            <#list dicContentList as item>
                <tr>
                    <td><input type="text" name="" id="" class="inputSty" value="${item.dicContentValue}" /></td>
                    <td>${item.dicContentCode}</td>
                    <td><a class="delFont fontBold cursorPointer hasUnderline fontSize12" href="javascript:void(0);"
                           onclick="del(${item.dicContentId},this)">删 除</a>
                    <a class="delFont fontBold cursorPointer hasUnderline fontSize12" href="javascript:void(0);"
                           onclick="Alter(${item.dicContentId},this)">修 改</a>
                    </td>
                </tr>
            </#list>

                <tr>
                    <td><input class='inputSty ' type="text"    title="必填，不能超过50个字"  maxlength="50"/></td>
                    <td><input class='inputSty ' type="text"   title="必填，不能超过50个字符"  maxlength="50"></td>
                    <td><input class='changeFont noBorder fontSize12 hasUnderline cursorPointer bgTransparent'
                               type="button" id="addDicCode" value="新 增"></td>
                </tr>
            </table>
            <div class="showBtnBox">
                <input class="cancleBtn closeThisLayer" type="button" value="取 消"/>
                <input class="sureBtn" id="formVer" type="button" value="确 认"/>
            </div>

        </form>
    </div>
</div>
</body>
<script type="text/javascript">
</script>
</html>