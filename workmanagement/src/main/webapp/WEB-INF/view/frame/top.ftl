<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
</head>
   <style>
   		.top p{
   			float:right;
   			height:50px;
   			line-height:50px;
   		}
    </style>
<script type="text/javascript">
$(function(){	
	setInterval("getCurDate()",1000);
	
	//顶部导航切换
	$(".nav li a").click(function(){
		$(".nav li a.selected").removeClass("selected")
		$(this).addClass("selected");
	})	
	
	var current = 0;
    $("#target").on("click",function(){
    	current = (current+180)%360;
        this.style.transform = 'rotate('+current+'deg)';
        this.style.filter = "progid:DXImageTransform.Microsoft.BasicImage(rotation=2)";
        $(".loginMenuShow").toggle();
    }) 
    $(".loginMenuShow span") .hover(
		 function(){
	                $(this).css("background", "#F1F1F1");
	            },
	            function(){
	                $(this).css("background", "#969696");
	     }
    )
    
    
})	

  function pwdModify(obj,pwdSrc){
    	//获取tab名称
	
	//获取tab链接
	var href = pwdSrc;
	//获取右框架row
	var row=$(window.parent.frames['rightFrameset']).rows;
	var rowArr = [];
	var names = $(window.parent.frames['rightFrameset']).find("frame").attr("name");
	
	var rframe = parent.document.getElementById("rightFrame") ;  
	rframe.src = href;  
   		
		
		
	
   }

</script>

<body>
	<div class="top" style="height:50px;background-size:100%;background: #139DD9;">
		<p style="width:321px;letter-spacing:2px;float:left;background-size:100%;
			background:url(${request.getContextPath()}/assets/images/log1.png) repeat-x 0 0; 
			_background:none;
			_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${request.getContextPath()}/assets/images/logo2.png',sizingMethod='crop');
		">
		</p>
		<p style="width:230px;float:left;margin-top: 3px;">
			<span id="time" class="time" style="float:none;color:#fff;margin-top:5px;font-size:14px;padding-left:10px;">2016-10-23 09:45 星期六</span>
		</p>
		
		<p style="padding: 0 20px">
			
			<label style="float:none;"><img style="border: none;padding: 0;margin-top: 9px;float: left;" src="${request.getContextPath()}/assets/images/login-user.png" height="30px" width="35px" alt=""/>
			<span style="display:inline-block;color:#fff;padding-left:2px;margin-right:10px; margin-top:5px;font-size:14px;">${user.username}</span></label> 
			<a href="javascript:void(0)" onclick="javascript:exit('${request.getContextPath()}/admin/loginout.jhtml');"  style="margin-left:10px;color:#fff;margin-left:10px;">退出登录</a>
			
		</p>
		<p style="width:120px;">
			<#--<span style="display:inline-block;width: 18px; height:18px; vertical-align: middle;margin: 14px 10px 0 0;float:left;border:none;padding:0;
			background-size:100%;
			background:url(${request.getContextPath()}/assets/images/position.png) repeat-x 0 0; 
			_background:none;
			_filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${request.getContextPath()}/assets/images/position.png',sizingMethod='crop');
			"></span>-->
			<#--<span style="width:90px;  height:50px;line-height:50px;overflow: hidden;display:inline-block;float:left;color:#fff;padding:0;font-size:14px;" title="崇左市质监局"></span>-->
		</p>
		<p style="width:120px;margin-right: -160px;">
			<a id="index" href="#" onclick="pwdModify(this,'${request.getContextPath()}/admin/myPanel/index.jhtml')"><img src="${request.getContextPath()}/assets/images/index.png" style="border: none;padding: 0;margin-top: 9px;margin-right: 40px;"/></a>
		</p>
	</div>
</body>


</html>
