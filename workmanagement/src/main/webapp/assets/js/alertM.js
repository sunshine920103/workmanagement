// JavaScript Document
var amrt=false;
function alertM(content,opt){
	//alert("调用alertM");
	opt= $.extend({
		time:3000,
		title:'提示',
		width:400,
		height:'auto',
		btnC:true,
		btnY:true,
		btnYT:'确定',
		btnN:false,
		btnNT:'取消',
		cF:function(){},
		yF:function(){},
		nF:function(){},
		rF:function(){}
	},opt||{});
	//alert框的布局
	var w={
		height: $(document).height(),
		left: $(window).width()/2-opt.width/2,
		top: $(window).height()/2+$(window).scrollTop()
	};
	
	opt.h=function(){
		$('<div id="hbg" style="height:'+w.height+'px;"></div>').appendTo('body').fadeTo('fast',0.6);
		return opt;
	}
	//布局
	opt.s=function(){
		var str=['<div id="alertM" style="left:',w.left,'px;height:',opt.height,'px;width:',opt.width,'px;overflow:hidden"><h5 id="alertT">',opt.title,'</h5>'];
		if(opt.btnC)
			str.push('<a id="alertR" title="关闭" href="javascript:void(0)">&times;</a>');
			str.push('<div id="alertP">',content,'</div>');
		if(opt.btnY||opt.btnN){
			str.push('<div id="alertBtns">');
			if(opt.btnY)
				str.push('<a id="alertY" href="javascript:void(0)">',opt.btnYT,'</a>');
			if(opt.btnN)
				str.push('<a id="alertN" href="javascript:void(0)">',opt.btnNT,'</a>');
			str.push('</div>');
		}
		str.push('</div>');
		w.top=w.top-$(str.join('')).appendTo('body').height()/2-99;
		$('#alertM').css('top',w.top);
		return opt;
	}
	
	opt.a=function(){
		$('#alertM').animate({top:w.top+50,opacity:'show'},opt.b)
	}
	opt.b=function(){
		$('#alertM').show().css('top',w.top+50);
		//$('#alertT').drag('#alertM');
		$('#alertR').click(function(){
			if(opt.cF()!=false)
				opt.r()
		});
		$('#alertY').click(function(){
			if(opt.yF()!=false)
				opt.r();
		});
		$('#alertN').click(function(){
			if(opt.nF()!=false)
				opt.r();
		});
	}
	if($('#alertM').length>0){
		$('#alertM').remove();
		opt.s().b();
	}else
		opt.h().s().a();
	if(amrt);
		clearTimeout(amrt);
	amrt=false;
	opt.r=function(){
		$('#alertM').animate({top:w.top+100,opacity:'hide'},function(){
			$('#hbg').fadeOut(function(){
				$(this).remove();
				opt.rF();
			});
			$(this).remove();
		});
		if(amrt);
			clearTimeout(amrt);
		amrt=false;
		return opt;
	}
	if (!isNaN(opt.time)) {
		amrt=setTimeout(function(){opt.r()}, opt.time);
	}
}
