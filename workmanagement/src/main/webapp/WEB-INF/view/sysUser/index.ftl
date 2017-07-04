<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >

			//删除子区域
			function delSubTree(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();

					//递归删除子区域
					delSubTree(pid);
				});
			}
			
			//切换分组方式 ，1 机构 2 角色
			function switchMethod(obj, id){
				var waitIndex = wait();
			
				//删除分组方式的选中标记
				$.each($(".method"),function(i,v){
					$(v).removeClass("seleced-parent");
				});
				//删除分组列表的选中标记
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});				
				//添加分组方式的选中标记
				$(obj).addClass("seleced-parent");
				
				//根据分组方式显示分组列表
				if(id==1){
					$("#jg").show();
					$("#js").hide();
				}else{
					$("#js").show();
					$("#jg").hide();
				}
				$("#methodText").text($(".seleced-parent").text());

				$("#method").val(id);
				$("#mid").val("");
				$("#methodTextVal").val($(".seleced-parent").text());
				$("#searchForm").submit();
				
				layer.close(waitIndex);
			}
		
			
			
			//重置查询
			function resetForm(){
				var waitIndex = wait();
				myFrameName.location.href = "${request.getContextPath()}/admin/sysUser/list.jhtml";
				layer.close(waitIndex);
			}
			
			//导出excel
			function exportExcel(){
				var key = $("#key").val();
				var key = $("#key").val();
				var roleName = $("#roleName").val();
				var orgName = $("#orgName").val();	
				window.location.href = "${request.getContextPath()}/admin/sysUser/export.jhtml?key=" + key + "&roleName=" + roleName + "&orgName=" + orgName;
			
			}
			
			$(function(){
				$(".groupList").height($(window).height()-50);
				
				if($(".inputOtherCondition").val() != ""){
					$(".fuck").hide();
				}
				$(".inputOtherCondition").focus(function(){
					$(".fuck").hide();
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(".fuck").show();
					}
				});
				$(".fuck").click(function(){
					$(".inputOtherCondition").focus();
				})
				
			})
			window.onresize = function(){
				$(".groupList").height($(window).height()-50);
				$('.layui-layer-shade').height($(window).height());
			} 
			function openPop(num){
				$("#covered").show();
				$("#poplayer").show();
				if(num==0){
					$(".xzjg").hide();
					$(".xzjs").show();
					
				}else if(num==1){
					$(".xzjs").hide();
					$(".xzjg").show();
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
				}
			}
			
			
			//关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			function selUpstream1(obj){
				$.each($(".xzjg .seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
			function selUpstream2(obj){
				$.each($(".xzjs .seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
		
		
			//确认选择
			function confirmSel1(clear){
				var seleced = $(".xzjs .seleced");
				 var area = $(seleced.find("label")).text();
					closePop();
					
					if(clear==1||seleced.length==0){
						$("#openPop0").text("请选择角色");
						
						$("#roleName").val("");
					}else{
						$("#openPop0").text(area);
						$("#roleName").val(area);
						
 
					}
				
			}
 
	 //选择分组列表
        function selUpstream(obj, id) {
            $.each($(".seleced"), function() {
                $(this).removeClass("seleced");
            });
            $(obj).addClass("seleced");

            $("#methodText").text($(obj).find("label").text());
            $("#methodTextVal").val($(obj).find("label").text());
            $("#mid").val(id);

            $("#begin").val("");
            $("#end").val("");
            $("#day").val("");
            $("#key").val("");

        }
        
        
       //确认选择机构
			function confirmSel2(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#openPop1").text("请选择机构");
					$("#orgName").val("");
				} else { 
					$("#openPop1").text(htext);
					$("#orgName").val(hteid); 
				}  
			}

			$(function(){
				$("#btnSearch").click(function(){
					var search=$("#key").val();
					if (checkTChineseM(search) == 0) {
	                    layer.alert("请输入正确的查询条件", {icon: 2, shade: 0.3, shouldClose: true});
	                    return false;
                	}
				});
				$("#searchFor").submit();
			})
			
		</script>
		<title>用户管理列表</title>
	</head>
	<body>
<form id="searchForm" method="post">
    <input type="hidden" name="roleName" value="${roleName}" />
    <input type="hidden" name="orgName"   value="${orgName}" />
    <input type="hidden" name="key"  value="${key}" />
</form>
		<#-- 弹出框 -->
	    <div id="covered"></div>  
	    <div id="poplayer">  
	        <div class="hide borderBox xzjs">
	        	<div class="titleFont1">
	        		<span>角色列表</span>
        		</div>
	        	<div class="listBox" >
					
	        		<!--下面新添了一个table，js在最下面-->
	        		<table cellpadding="0" cellspacing="0" id="searchTableRole">
						<#list srs as i>
			                <tr>
			                    <td level="1" id="${i.sys_role_id}" onclick="selUpstream2(this)">
			                        <label>${i.sys_role_name}</label>
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
	        <div class="borderBox  xzjg">
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
	
		<div class="userManageBox marginLR30">
			<div class="rightList  eachInformationSearch ">
				<div class="listBox" style="margin-left: 0px; margin-right: 0px; margin-bottom:0px">
					
						<div class="margin20" style="margin-bottom:10px">
								<span class=" fontSize12">机构列表 ：</span><a id="openPop1" class="marginT20 inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">请选择机构</a>
								<span class=" fontSize12">角色列表 ：</span><a id="openPop0" class="marginT20 inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(0)">请选择角色</a>
						</div>
						
						
						<div>
						<input  onclick="setLayer('新增用户','${request.getContextPath()}/admin/sysUser/add.jhtml?posi=1');$('.layui-layer-shade').height($(window).height());$(this).blur();" type="button" value="新增用户" class="sureBtn sureBtnEx"/>
						<form id="searchFor" method="post" action="${request.getContextPath()}/admin/sysUser/list.jhtml" target="myFrameName" style="display:inline-block;*zoom=1;*display:inline;position:relative;" class="marginL20">
							<#-- 当前分组方式, 只用来回显 -->
							<input type="hidden" name="methodTextVal" id="methodTextVal" value="${methodTextVal}"/>
							<span   class="fuck">请输入登录名或姓名</span>
							<input id="key" name="key" class="inputSty inputOtherCondition" value="" />
							<input type="submit" id="btnSearch" class="sureBtn sureBtnEx" value="查  询" style="margin-left:0px"/>
							<#-- 分组方式 -->
							<input type="hidden" name="method" id="method" value="${method}"/>
							<#-- 分组列表 -->
							<input type="hidden" name="mid" id="mid" value="${mid}"/>
							<input type="hidden" name="orgName" id="orgName" value="${orgName}"/>
							<input type="hidden" name="roleName" id="roleName" value="${roleName}"/>
						</form>
						<input onclick="exportExcel()" type="button" value="导   出" class="sureBtn sureBtnEx marginL20"/>
					</div>
				
					<div class="marginT10">
						<#-- 分页查询需要使用该表单 -->
						
						<p class="warmFont fontSize12">注：锁定状态的用户将无法登陆系统，对用户重置后，才能正常使用；用户默认和重置后的密码为8个6。</p>
					</div>
				</div>
			
				<div class=" listBox" style="margin-left: 0px; margin-right: 0px;margin-top:0px;">
					<iframe id="myFrameName" name="myFrameName" src="${request.getContextPath()}/admin/sysUser/list.jhtml" name="rightFrame" frameborder="0" style="width:100%;height:580px"></iframe>
				</div>
			</div>
		</div>
	</body>

</html>
