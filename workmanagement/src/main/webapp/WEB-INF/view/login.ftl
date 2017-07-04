<!DOCTYPE HTML>
<html>
<head>
	<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="Expires" content="-1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>欢迎登录后台管理系统</title>
	<link rel="stylesheet" href="${request.getContextPath()}/assets/css/style.css" />
	<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
	<script src="${request.getContextPath()}/assets/js/lay/layer/layer.js"></script> 
	<script src="${request.getContextPath()}/assets/js/layer/mobile/layer.js"></script> 
	<link rel="stylesheet" href="${request.getContextPath()}/assets/js/layer/mobile/need/layer.css" />
	<script src="${request.getContextPath()}/assets/js/layer/layer.js"></script> 
	<script src="${request.getContextPath()}/assets/js/setLayer.js"></script>
	<style>
	<style>
		body{
			overflow: hidden;
		}
	 	.head{
	        width: 100%;
	        height: 50px;
	       	background:url(${request.getContextPath()}/assets/images/bg.png);
	       	padding-top:10px;
	    }
	    .head div{
	    	display:inline-block;
	    	zoom:1;
	        height: 60px;
	        width:500px;
	        margin: 0 0 0 15%;
	        background-size: 100%;
	    }
	     .loginCon{
	        width: 100%;
	        background: blue;
	    }
	    .loginCon .title{
	        width: 300px;
	        float: left;
	        margin-top: 150px;
	        margin-left: 25%;
	    }
	    .loginBox{
	        width: 300px;
	        height: 250px;
	        background: #fff;
	        float: left;
	        margin-top: 120px;
	        margin-left: 200px;
	    }
	    .sysName{
	        margin: 0;
	        padding: 0;
	        width: 100%;
	        height: 35px;
	        line-height:35px;
	        text-align: center;
	        background: #4897e7;
	        font-size: 14px;
	        color: #fff;
	        margin-right:0;
	        margin-bottom: 10px;
	        letter-spacing:1px;
	    }
	    .loginUser{
	        width: 230px;
	        height: 25px;
	        margin: 30px auto 20px;
	    }
	    .loginUser img{
	        width: 29px;
	        height: 27px;
	        float: left;
	        margin: 0;
	    }
	    .loginUser input{
	        width: 190px;
	        height: 25px;
	        float: left;
	        padding: 0 2px;
	        border: 1px solid #c1c1c1;
	        border-left: none;
	        font-size:12px;
	        line-height:23px;
	        background:#dfdfdf
	    }
	    .loginPwd{
	        width: 230px;
	        height: 25px;
	        margin: 0 auto;
	    }
	    .loginPwd img{
	        width: 29px;
	        height: 27px;
	        float: left;
	    }
	    .loginPwd input{
	        width: 190px;
	        height: 25px;
	        float: left;
	        padding: 0 2px;
	        border: 1px solid #c1c1c1;
	        border-left: none;
	        font-size:12px;
	        line-height:23px;
	        background:#dfdfdf
	    }
	    .bgBox{
	    	width:100%;
	    	position:absolute;
	    	z-index:-1;
	    }
	    .bgBox img{
	    	height:100%;
	    	height:100%;
	    }
	    .loginFontImg{
	    	width: 600px;
	    	height:77px;
	    	position: absolute;
	    	top: 100px;
	    	left: 50%;
	    	margin-left:-300px; 
			background-size: 100%;
	    }
	    .loginContent{
	    	width: 400px;
	    	height: 250px;
	    	background: #fff;
	    	position: absolute;
	    	top: 220px;
	    	left: 50%;
	    	margin-left:-200px;
	    }
	    .warmBox{
	    	width:200px;
	    	margin:10px auto 0px;
	    }
	    .warmBox span{
	    	font-size:12px;
	    	color: red;
	    	display:inline-block;
	    	width:200px;
	    	height:20px;
	    	overflow:hidden;
	    }
	    .copyRight{
	    	text-align: center;
	    	margin-top: 20px;
	    	font-size: 12px;
	    }
	    .inputbox1 input{
	    	position: relative;
	    	z-index: 99;
	    	background:#fff;
	    	color:black;
	    	border:1px solid #939393;
	    }
</style>
</head>
<script type="text/javascript"> 
	
	if (top.location != self.location) {  
		alert("登录超时请重新登录") 
		top.location = '${request.getContextPath()}/login.jhtml'; 
	}
//	$(function(){
		
//	});
	$(function() {	
		$(".bgBox").height($(window).height());
		$(".bgBox").width($(window).width());	
		$(".bgBox img").height($(window).height());
		$(".bgBox img").width($(window).width());
		$("#j_username").focus();
		
		function loginSubmit(){
			if($("#j_username").val() == ""){
					$("#j_username").focus();
					return false; 
				}
			if($("#j_password").val() == ""){
				$("#j_password").focus();
				return false; 
			}
			if($("#j_username").val() != "" && $("#j_password").val() != ""){
				document.loginForm.submit(); 
			}
		}
		
		$(document).keyup(function(event){
			keyNum=event.keyCode;
			if(event.keyCode==13){
				$("#submitButton").trigger("click");
			}
		})
		
		var tips = $(".warmBox span").text();
		if(tips!=""&&/.*[\u4e00-\u9fa5]+.*$/.test(tips)==false) 
		{ 
			$(".warmBox span").text( "登录错误，请重新输入");
		}
		
//		layer.closeAll();
		var errNum = '${errNum}';
		var txtMsg = $(".warmBox span").text();
		if(txtMsg =="用户名或密码错误，请重新输入!"&&errNum!=""){
		//	layer.alert(errNum,{icon:2,shade:0.3,shouldClose:true});
			$(".warmBox span").text( errNum);
		}
		
	});

	window.onresize = function(){
		$(".bgBox").height($(window).height());
		$(".bgBox").width($(window).width());
		$(".bgBox img").height($(window).height());
		$(".bgBox img").width($(window).width());
	}
	$(function(){
		$.post("${request.getContextPath()}/admin/companyInfoQuery/getUrl.jhtml",function(data2){
			var url1=data2+"/bi4/esmain/login.do?action=logout";
			$.ajax({
			        url: url1,
			        type: 'GET',
			        dataType: 'JSONP',
			        success: function (data) {
			        }
			    });
			
		})
		
		var tips = $(".warmBox span").text();
		if(tips!=""&&/.*[\u4e00-\u9fa5]+.*$/.test(tips)==false) 
		{ 
			$(".warmBox span").text( "登录错误，请重新输入");
		}	
		
	})
	

	
</script>
<body>
	<div class="head" >
		<div style=" background:url(${request.getContextPath()}/assets/images/login-logo.png) no-repeat 0 0;  _background:none; _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${request.getContextPath()}/assets/images/login-logo.png',sizingMethod='crop');"></div>	
	</div>
	<div class="bgBox">
		<img src="${request.getContextPath()}/assets/images/login-bg.png"  />
	</div>
	<div class="loginbody">
	    <div class="loginbox">
	        <form id="loginForm" class="submitBox" name="loginForm" action="${request.getContextPath()}/j_spring_security_check" method="post">
			    <div class="loginFontImg" style=" background:url(${request.getContextPath()}/assets/images/login-word.png) no-repeat 0 0;  _background:none; _filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='${request.getContextPath()}/assets/images/login-word.png',sizingMethod='crop');">
			    </div>
		        <div class="loginContent">
		            <p class="sysName">您好，请登录(d.v.2.0.2.0)</p>
		            <div class="loginUser">
	                    <img src="${request.getContextPath()}/assets/images/login-user.png" alt=""/>
	                    <input type="text" placeholder="请输入登录名" id="j_username" name="j_username"  value=""/>
	                </div>
	                <div class="loginPwd">
	                	<img src="${request.getContextPath()}/assets/images/login-pwd.png" alt=""/>
	                    <input type="password" placeholder="请输入密码"  id="j_password" name="j_password"  onpaste="return false" oncontextmenu="return false"   
  
oncopy="return false" oncut="return false" value=""/>
	                </div>
		            <div class="warmBox">
						<span>${Session.SPRING_SECURITY_LAST_EXCEPTION}</span>
		            </div>
		            <div class="inputbox1">
		                <input type="submit" id="submitButton" class="submit submit2"  value="登 录" onclick="loginSubmit();return false;" />
		            </div>
	        		<div class="copyRight">copyright 2017 崇左市发改委 all right reserved</div>
	           </div>	            
	        </form>
	    </div>
	</div>
</body>
</html>