<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
<style type="text/css">
	.menuIdel{
		font-family: "微软雅黑";
		color: #C7C7C7;
		font-weight: 600;
		font-size: 16px;		
		*display: inline;
		*zoom: 1;
		display: inline-block;		
		width: 15px;
		height: 15px;
		text-align: center;
		line-height: 15px;
		margin-left: 20px;
	}
	.secMenu {display: none;}
	.secMenu li {width: 200px;margin:0; color: #dcdcdc;padding-top: 0px;padding-bottom:0px;text-align: left;cursor:pointer;}
	.menugson li:hover a{color: #38a5e2;}/*鼠标经过的颜色变化*/
	.menugson li p:hover {background:#fff;}
	.menugson li,.menuB{width: 200px;margin:0; color: #dcdcdc;padding-top: 8px;padding-bottom: 8px;text-align: left;cursor:pointer;}
	.menugson li a{margin-left:50px; font-size:12px;letter-spacing:1px;}
	.click_a a{color: #38a5e2;}
	#zhez{background-color:#000;opacity:0.3;filter: alpha(opacity=30);_height:expression(document.body.offsetHeight+"px");width: 100%;height: 100%;position:fixed;top: 0;left: 0;z-index: 999;}
</style>
</head>
<script type="text/javascript">

$(function() {
	//导航切换
	$(".menuA li label").click(function() {
		$('.secMenu').slideUp();
		$('.menugson').slideUp();
		
   		$(".menuIdel").each(function(){$(this).text("+")});
		
		$('.secMenu li.bgc-color').removeClass('bgc-color');
		$(".menuA li label.active").removeClass("active");
		$(this).addClass("active");
		var $ul = $(this).next('ul');
		if($ul.is(':visible')) {
			$(this).next('ul').slideUp();
			$(this).removeClass("active");
		} else {
			$(this).next('ul').slideDown();
		}
	});



	$('.secMenu li .menuB').click(function(){
		$('.secMenu .bgc-color').removeClass('bgc-color');
		$(this).addClass('bgc-color');
		if($(this).children("span").length==0){
			var url = $($(this).find("a")[0]).attr("href");
			var rframe = parent.document.getElementById("rightFrame") ;  
   			rframe.src = url;  
   		}
   		$('.menugson').slideUp();
   		$('.menuIdel').each(function(){$(this).text('+')});
   		
   		$('.menugson .bgc-color').removeClass('bgc-color');
   		$(".secMenu li .menuB a.active").removeClass("active");
   		$(this).addClass("active");
   		var $ul = $(this).next('ul');
   		if($ul.is(':visible')){
   			$(this).next('ul').slideUp();
   			$(this).removeClass("active");
   			$($(this).find(".menuIdel")).text("+");
   		}else{
   			$(this).next("ul").slideDown();
   			$($(this).find(".menuIdel")).text("-");
   		}		
   		
	});
	$('.menugson li').click(function(){
		$('.menugson .bgc-color').removeClass('bgc-color');
		$(".bgc-color").removeClass("bgc-color");
		$('.menugson li').removeClass("click_a");
		$(this).addClass("click_a");
		$(this).addClass("bgc-color");
	})
	//menu height
	//left高度随时浏览器改变而改变
	$('.menuA').css('height',$(window).height()+'px');
	window.onresize = function(){
		$('.menuA').css('height',$(window).height()+'px');
	}
	
});

$(function(){
	var orgId = ${sessionUser.sys_org_id};
	$.post("${request.getContextPath()}/admin/menuAdd/getOrgCode.jhtml",{id:orgId},function(data){
		$.post("${request.getContextPath()}/admin/companyInfoQuery/getUrl.jhtml",function(data2){
				$(".urlh").each(function(){
					var url=$(this).attr("href");
						url = data2+url+"&@jg="+data+"&showparams=true&calcnow=true";
					 $(this).attr("href",url);
				})
		})
			
	});

});
function tabs(obj){
    obj = $(obj);
    obj.siblings().removeClass('active');
    obj.addClass('active');
}

function closeTab(obj){
    obj = $(obj);
    obj.parent().remove();
}


$(function(){
	var orgId = ${sessionUser.sys_org_id};
	$.post("${request.getContextPath()}/admin/menuAdd/getOrgCode.jhtml",{id:orgId},function(data){
				$(".urlh").each(function(){
					var url=$(this).attr("href");
						url = url+"&@jg="+data+"&showparams=true&calcnow=true";
					 $(this).attr("href",url);
				})
	});
	$.post("${request.getContextPath()}/admin/companyInfoQuery/getUrl.jhtml",function(data2){ 
	})
});

 
</script> 

<body style="background:#fff;position:relative;">
	<div id="zhez" class="hide"  > </div>
	<div class="menuA" style="position: fixed;overflow:scroll;overflow-x:hidden;">
		<ul class="firMenu" style="border-top:1px solid #d2d2d2;">
			<#list sessionUser.menus as menu>
				<li>
					<#-- 一级标题 -->
					<#if menu.sys_menu_path?? >
					
						<#if submenu.sys_menu_path == "/">
						<label class="firLi"><img src="${request.getContextPath()}/${menu.sys_menu_icon}" />
								<p style="letter-spacing:2px">
									<a href="javascript:void(0);" target="rightFrame">${menu.sys_menu_name}zg</a>
								</p>
						</label>
						</#if>
					<#else>
					<#if menu.sys_menu_parent_id ==0 && menu.sys_menu_name!='信息查询'>
						<label>
							<a href="javascript:">
								<label class="firLi">
									<img src="${request.getContextPath()}/${menu.sys_menu_icon}" />
									<p style="width:80px;height:20px;float:left;letter-spacing:2px">${menu.sys_menu_name}</p>
									<img class="pointImg" src="${request.getContextPath()}/assets/images/img-up.png" style="width:12px;float:none;padding:0;margin-left:0px;margin-top:12px">
								</label>
							</a>
						</label>
						</#if>
					</#if>
					<#-- 子标题 -->
	        		<#if (menu.subMenus?size > 0) >
	    				<ul class="secMenu" style="border-bottom:1px solid #d2d2d2;">
	    				<#list menu.subMenus as submenu>
                            <#if submenu.sys_menu_name == "EXCEL模板设置">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">模板设置</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">模板设置</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">模板设置</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">模板设置</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
                            <#elseif submenu.sys_menu_name == "校验管理">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">校验设置</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">校验设置</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">校验设置</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">校验设置</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
                            <#elseif submenu.sys_menu_name == "其他管理">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">授权文件管理</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">授权文件管理</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">授权文件管理</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">授权文件管理</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
                            <#elseif submenu.sys_menu_name == "EXCEL报送">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">提请报送</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">提请报送</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">提请报送</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">提请报送</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
                            <#elseif submenu.sys_menu_name == "人行异议处理">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">平台管理员异议处理</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">平台管理员异议处理</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">平台管理员异议处理</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">平台管理员异议处理</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
                            <#elseif submenu.sys_menu_name == "机构异议处理">
                                <li>
                                    <#if submenu.sys_menu_path == "">
                                        <p class="menuB">
                                            <span class="menuIdel">+</span>
                                            <a href="javascript:void(0)" style="margin-left: 0px;">报送机构异议处理</a>
                                        </p>
                                    <#else>
                                        <p class="menuB">
                                            <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">报送机构异议处理</a>
                                        </p>
                                    </#if>
                                    <#if (submenu.subMenus?size > 0) >
                                        <ul class="menugson">
                                            <#list submenu.subMenus as gsubmenu>
                                                <#if gsubmenu.sys_menu_type == 1>
                                                    <li  >
                                                        <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">报送机构异议处理</a>
                                                    </li>
                                                <#else>
                                                    <li >
                                                        <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">报送机构异议处理</a>
                                                    </li>
                                                </#if>
                                            </#list>
                                        </ul>
                                    </#if>
                                </li>
	        				    <#elseif  submenu.sys_menu_name != "重点企业群" && submenu.sys_menu_name != "数据字典" && submenu.sys_menu_name != "汇率维护" && submenu.sys_menu_name != "机构管理" && submenu.sys_menu_name != "机构类别及总部维护" && submenu.sys_menu_name != "企业二码合并" && submenu.sys_menu_name != "行业分类管理"&& submenu.sys_menu_name != "标识管理"&& submenu.sys_menu_name != "用户行为审计"&& submenu.sys_menu_name != "菜单管理"&& submenu.sys_menu_name != "监管报表" && submenu.sys_menu_name != "报文报送"&& submenu.sys_menu_name != "数据类型查询" >
                                <li>
                                        <#if submenu.sys_menu_path == "">
                                            <p class="menuB">
                                                <span class="menuIdel">+</span>
                                                <a href="javascript:void(0)" style="margin-left: 0px;">${submenu.sys_menu_name}</a>
                                            </p>
                                        <#else>
                                            <p class="menuB">
                                                <a href="${request.getContextPath() + submenu.sys_menu_path}" target="rightFrame">${submenu.sys_menu_name}</a>
                                            </p>
                                        </#if>
                                        <#if (submenu.subMenus?size > 0) >
                                            <ul class="menugson">
                                                <#list submenu.subMenus as gsubmenu>
                                                    <#if gsubmenu.sys_menu_type == 1>
                                                        <li  >
                                                            <a style="display: block;*display: block;margin-right:20px; margin-left:60px;" class="urlh" href="${gsubmenu.sys_menu_path}&id=${sessionUser.username}&pw=${sessionUser.password}" target="rightFrame">${gsubmenu.sys_menu_name}</a>
                                                        </li>
                                                    <#else>
                                                        <li >
                                                            <a style="display: block;*display: block;margin-right:20px;  margin-left:60px;" href="${request.getContextPath() + gsubmenu.sys_menu_path}" target="rightFrame">${gsubmenu.sys_menu_name}</a>
                                                        </li>
                                                    </#if>
                                                </#list>
                                            </ul>
                                        </#if>
                                </li>
                            </#if>
	        			</#list>
	    				</ul>
	    			</#if>
				</li>
			</#list>
		</ul>
	</div>
	<div style="width:0;height:1000px;border-right:1px solid #d2d2d2;position:absolute;top:0;left:200px;"></div>
	
</body>
<script type="text/javascript"> 
	        //如果是第一次登录 firstBlood判定用户是否第一次登录
	        
	        $.post("${request.getContextPath()}/admin/myPanel/getPwd.jhtml",function(data){
				if(data){ $("#zhez").show(); };
			})
	      
</script>
</html>
