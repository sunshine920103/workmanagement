<!DOCTYPE html>
<html>

<head>
<#include "/fragment/common.ftl"/>
<style type="text/css">
	
	.eachInformationSearch .listBox table td{padding: 6px 5px;}
</style>
    <script type="text/javascript">
    
    $(function(){
				//回显
				var msg = "${msg}";
				if(msg!=""){
					alert(meg);
				}
			
			})
    $(function(){
    	<#if types!=null>
	  		 var type =${types};
		    $(".inputSty").eq(type).attr("selected",true);
		</#if>
		    
		  //IE下 背景全屏
        window.onresize = function(){
            $('.layui-layer-shade').height($(window).height());
        }
        
       	 
            
            //查询点击事件
            $('#selectLog').click(function () { 
            	var search=$("#key").val();
					if(checkTChineseM(search)==0) {
						layer.alert("请输入正确的查询条件",{ icon:2, shade:0.3, shouldClose:true }); 
		                return false;
			  		 }  
               $('#sear').submit();
            });
            
            
            $("#openPop1").text()
        
        
        
        
    });
       function delSubTree(id) {
            $.each($("." + id), function(i, v) {
                var pid = v.attributes.getNamedItem("name").nodeValue;

                //删除子区域
                $(this).remove();

                //递归删除子区域
                delSubTree(pid);
            });
        }






        //导出excel
        function fsubmit() {
          var methodTextVal = $("#met").val();
				var key = $("#ke").val();
				var begin = $("#beg").val();
				var end = $("#en").val();	
				var menu = $("#men").val();
				var types = $("#typ").val();	
				window.location.href = "${request.getContextPath()}/admin/sysManageLog/excl.jhtml?methodTextVal=" + methodTextVal + "&key=" + key + "&begin=" + begin+ "&end=" + end + "&menu=" + menu+ "&types=" + types;
			
        }

        function openPop(num){
            $("#covered").show();
            $("#poplayer").show();
            if(num==1){
                $(".xzjg").show();
                $(".xzcd").hide();
                if($("#treeDemo").html()==""){
				 var loading = layer.load();
				$.ajax({
				url:'${request.getContextPath()}/admin/sysUserBehaviorAudit/getSysOrgs.jhtml',
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
            }else if(num==2){
                $(".xzjg").hide();
                $(".xzcd").show();
            }
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
     
        

        function confirmSel2(clear){
            var seleced = $(".xzcd .seleced");

            closePop();
            $("#openPop2").text(area);
            var area = $(seleced.find("label")).text();
            if(clear===1){
                $("#openPop2").text("选择菜单");
                $("#open2").attr("name","");
            }else{
            	if(area==""){
            		$("#openPop2").text("选择菜单");
            	}else{
            	$("#open2").val(area);
                $("#openPop2").text(area);
               }
            }

        }
        

    
       //确认选择机构
			function confirmSel1(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#openPop1").text("请选择机构");
					  $("#open1").val("");
				} else { 
					$("#openPop1").text(htext);
					$("#open1").val(hteid); 
				}  
			}

 
    </script>
</head>

<body>
<form id="searchForm" method="post">
    <input type="hidden" id="beg" name="begin" value="${begin}" />
    <input type="hidden" id="en" name="end"   value="${end}" />
    <input type="hidden" id="ke" name="key"  value="${key}" />
    <input type="hidden" id="typ" name="types" value="${types }"/>
    <input type="hidden" id="met" name="methodTextVal" value="${methodTextVal.sys_org_id}"/>
    <input type="hidden" id="men" name="menu" value="${menu}"/>
</form>

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
			<#list me as it>
                <#if   it.sys_menu_name=="指标设置" || it.sys_menu_name=="地区维护" || it.sys_menu_name=="校验管理" || it.sys_menu_name=="EXCEL模板设置" || it.sys_menu_name=="权限管理" || it.sys_menu_name=="用户管理" || it.sys_menu_name=="管理日志" || it.sys_menu_name=="密码管理" || it.sys_menu_name=="其他管理" || it.sys_menu_name=="数据备份" || it.sys_menu_name=="批量导出" || it.sys_menu_name=="任务管理" || it.sys_menu_name=="任务完成情况统计" || it.sys_menu_name=="报文报送" || it.sys_menu_name=="手工修改" || it.sys_menu_name=="已报数据" || it.sys_menu_name=="数据删除" || it.sys_menu_name=="人行异议处理" || it.sys_menu_name=="机构异议处理" >
                    <tr>
                        <td level="1" id="${it.sys_menu_id}" onclick="selUpstream(this)">
                            <label>${it.sys_menu_name}</label>
                        </td>
                    </tr>
                </#if>
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

<div class="rightPart floatLeft  eachInformationSearch">

    <div class="listBox" style="margin-bottom:10px">
	<#-- 分页查询需要使用该表单 -->
        <form id="sear" method="post" action="${request.getContextPath()}/admin/sysManageLog/list.jhtml">
            <div class="marginT20 fontSize12 fl">
            	
                <span class=" fontSize12">操作机构 ：</span>
                <input type="hidden" name="methodTextVal" id="open1" value="${methodTextVal.sys_org_id}" />
                <a id="openPop1" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">
                <#if methodTextVal != null>
     				${methodTextVal.sys_org_name}
     			<#else>
     				      选择机构
     			</#if>  
                </a>
                <input type="hidden" name="menu" id="open2" value="${menu}" />
                <span class=" fontSize12 marginL20">操作菜单 ：</span><a id="openPop2" class=" inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)">
     			<#if menu != null>
     				${menu}
     			<#else>
     				选择菜单
     			</#if>           
                </a>
            
            </div>
            <div class="marginT10 fontSize12">
                操作时间：
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id ="begin"  name="begin" value="${begin}"> 至
                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end"  name="end" value="${end}">
            	<lable class=" fontSize12 marginL30">操作类型：</lable>
                <select class="inputSty"  name="types">
                    <option value="0">请选择</option>
                    <option value="1">增加</option>
                    <option value="2">删除</option>
                    <option value="3">修改</option>
                    <option value="4">查询</option>
                    <option value="5">导入</option>
                    <option value="6">导出</option>
                    <option value="7">打印</option>
                </select>
            </div>
                
            <div class="marginT10 fontSize12" style="position: relative;">
			
			
                <span class="fuck">登录名或操作对象</span>
			
                <input id="key" type="text" name="key" class="inputSty" value="${key}" />
                <input type="button" id="selectLog" class="sureBtn sureBtnEx " value="查  询"/>
                <input onclick="fsubmit()" type="button" class="sureBtn sureBtnEx marginL20 " value="导出记录" />
            </div>

        </form>
    </div>

    <div class="listBox">
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">管理日志列表</caption>
			<tr class="firstTRFont">
                <td width="50">序号</td>
                <td  width="150">机构</td>
                <td width="50">用户</td>
                <td width="180">操作对象</td>
                 
                <td width="80">原值</td>
                <td width="80">现值</td>
                <td width="120">菜单</td>
                <td width="50">操作类型</td>
                <td width="100">操作IP</td>
                <td width="120">操作时间</td>
               
                <td width="60">操作内容</td>
							
            </tr>
  
			<#list als as sys>
				 <tr>
                <td>${sys_index+1}</td>
                <td>${sys.sysManageLogOrgName}</td>
                <td>${sys.sysManageLogUserName}</td>
                <td>${sys.sysManageLogEnterpriseCode}</td>
                
                <td class="stoplength"><a class="openmous" onmouseover="if($(this).text().split('').length>3){layer.tips('${sys.sysManageLogOldValue}',this, {time: 0});}" onmouseout="layer.closeAll('tips');">${sys.sysManageLogOldValue}</a></td>
                <td class="stoplength"><a class="openmous" onmouseover="if($(this).text().split('').length>3){layer.tips('${sys.sysManageLogNewValue}',this,{time: 0});}" onmouseout="layer.closeAll('tips');">${sys.sysManageLogNewValue}</a></td>
                <td>${sys.sysManageLogMenuName}</td>
                			<#if sys.sysManageLogOperateType == 1>
								<td class="secondTD">增加</td>
								<#elseif sys.sysManageLogOperateType == 2>
								<td class="secondTD">删除</td>
								<#elseif sys.sysManageLogOperateType == 3>
								<td class="secondTD">修改</td>
								<#elseif sys.sysManageLogOperateType == 4>
								<td class="secondTD">查询</td>
								<#elseif sys.sysManageLogOperateType == 5>
								<td class="secondTD">导入</td>
								<#elseif sys.sysManageLogOperateType == 6>
								<td class="secondTD">导出</td>
							</#if>
				<td > ${sys.sysManageLogIp} </td>
                <td>${(sys.sysManageLogTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
                
                <td>
                    <a class="changeFont fontSize12 hasUnderline cursorPointer"
                       onclick="setLayer('查看管理日志','${request.getContextPath()}/admin/sysManageLog/detail.jhtml?id=${sys.sysManageLogId}')">查看详情</a>
                </td>
            </tr>
			</#list>
        </table>
	<#include "/fragment/paginationbar.ftl"/>
    </div>
</div>
</div>
</body>
<script type="text/javascript">

        var start = {
  			elem: '#begin',
  			format: 'YYYY-MM-DD',
 //			min: laydate.now(), //设定最小日期为当前日期
  			max: laydate.now(), //最大日期
  			istime: true,
 			istoday: true,
  			choose: function(datas){
     			end.min = datas; //开始日选好后，重置结束日的最小日期
     			end.start = datas //将结束日的初始值设定为开始日
  			}
		};
		var end = {
	  		elem: '#end',
	  		format: 'YYYY-MM-DD',
	  		//min: laydate.now(),
	  		max: laydate.now(),
	  		istime: true,
	  		istoday: true,
	  		choose: function(datas){
	    		start.max = datas; //结束日选好后，重置开始日的最大日期
	  		}
		};
		laydate(start);
		laydate(end);
		
	$(function(){
    	for (var i = 0; i < $(".openmous").length; i++) {
    		if($(".openmous").eq(i).text().length>3){
    			$(".openmous").eq(i).text($(".openmous").eq(i).text().substring(0,3));
    			$(".openmous").eq(i).html($(".openmous").eq(i).html()+'…');
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
</html>
