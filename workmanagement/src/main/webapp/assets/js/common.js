/**
 * 设置文本关键字标红
 * @param objs 文本需要被标红的对象
 * @param flag objs需要被标红的字符
 */
function setFlag(objs, flag){
	$.each(objs,function(i,v){
		var str = $(this).text();
		var arr = str.split(flag);
		var flagStr = [];
		for(var i = 0; i < arr.length; i++){
			flagStr.push(arr[i]);
			if(i!=arr.length-1){
				flagStr.push("<span class='delFont'>");
				flagStr.push(flag);
				flagStr.push("</span>");
			}
		}
		var content = $("<div style='font-size:12px'>"+flagStr.join("")+"</div>");
		$(this).text("").append(content);
	});
}

//通过ID获取DOM元素
function getEleById(id){
	return document.getElementById(id);
}

//获取系统时间，将时间以指定格式显示到页面。  
function getCurDate()
{
	 var d = new Date();
	 var week;
	 switch (d.getDay()){
	 case 1: week="星期一"; break;
	 case 2: week="星期二"; break;
	 case 3: week="星期三"; break;
	 case 4: week="星期四"; break;
	 case 5: week="星期五"; break;
	 case 6: week="星期六"; break;
	 default: week="星期天";
	 }
	 var years = d.getFullYear();
	 var month = add_zero(d.getMonth()+1);
	 var days = add_zero(d.getDate());
	 var hours = add_zero(d.getHours());
	 var minutes = add_zero(d.getMinutes());
	 var seconds=add_zero(d.getSeconds());
	 var ndate = years+"年"+month+"月"+days+"日 "+hours+":"+minutes+":"+seconds+" "+week;
	 document.getElementById("time").innerHTML= ndate;
}


function add_zero(temp)
{
 if(temp<10) return "0"+temp;
 else return temp;
}


  
function menuLinkTo(path) {
	parent.document.getElementById('benchBody').src=path;
}

function exit(obj) {
	parent.window.location.href= obj;
}

function textOnfocus(obj) {
	if (obj.value == '输入文本...') {
		obj.value = "";
	}
}

function textOnblur(obj) {
	if ($.trim(obj.value) == '') {
		obj.value = "输入文本...";
	}
}

function getCodeNum(str) {
	var reg = /[1-9][0-9]*/g;
	return reg.exec(str);
}

function getCode(str) {
	var reg = /[a-zA-Z]+/g;
	return reg.exec(str)[0];
}

function isEmail(str) {
	emailReg = /^[\w\.\-]+@([\w\-]+\.)+[a-z]{2,4}$/;
	return emailReg.test(str);
}

//验证电话号码
function isPhoneNumber(str) {
	patrn = /^[\d\+\-\s]+$/;
    if(!patrn.exec(str)) {  
        return false;  
    }  
    return true;  
}

//验证手机号码
function isCellPhoneNumber(str) {
	patrn = /^[0-9]{11}$/;  
    if(!patrn.exec(str)) {  
        return false;  
    }  
    return true;
}

function isNumber(str) {
	numberReg = /^\d+$/;
	return numberReg.test(str);
}

function isPirce(str){
    var p =/^[1-9](\d+(\.\d{1,2})?)?$/;
    var p1=/^[0-9](\.\d{1,2})?$/;
    return p.test(str) || p1.test(str);
}

function isUsername(str) {
	var usernameReg = /^\w+$/;
	return usernameReg.test(str);
}


var Wi = [ 7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2, 1 ];    // 加权因子   
var ValideCode = [ 1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2 ];            // 身份证验证位值.10代表X   
function IdCardValidate(idCard) { 
    idCard = trim(idCard.replace(/ /g, ""));               //去掉字符串头尾空格                     
    if (idCard.length == 15) {   
        return isValidityBrithBy15IdCard(idCard);       //进行15位身份证的验证    
    } else if (idCard.length == 18) {   
        var a_idCard = idCard.split("");                // 得到身份证数组   
        if(isValidityBrithBy18IdCard(idCard)&&isTrueValidateCodeBy18IdCard(a_idCard)){   //进行18位身份证的基本验证和第18位的验证
            return true;   
        }else {   
            return false;   
        }   
    } else {   
        return false;   
    }   
}   
/**  
 * 判断身份证号码为18位时最后的验证位是否正确  
 * @param a_idCard 身份证号码数组  
 * @return  
 */  
function isTrueValidateCodeBy18IdCard(a_idCard) {   
    var sum = 0;                             // 声明加权求和变量   
    if (a_idCard[17].toLowerCase() == 'x') {   
        a_idCard[17] = 10;                    // 将最后位为x的验证码替换为10方便后续操作   
    }   
    for ( var i = 0; i < 17; i++) {   
        sum += Wi[i] * a_idCard[i];            // 加权求和   
    }   
    valCodePosition = sum % 11;                // 得到验证码所位置   
    if (a_idCard[17] == ValideCode[valCodePosition]) {   
        return true;   
    } else {   
        return false;   
    }   
}   
/**  
  * 验证18位数身份证号码中的生日是否是有效生日  
  * @param idCard 18位书身份证字符串  
  * @return  
  */  
function isValidityBrithBy18IdCard(idCard18){   
    var year =  idCard18.substring(6,10);   
    var month = idCard18.substring(10,12);   
    var day = idCard18.substring(12,14);   
    var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
    // 这里用getFullYear()获取年份，避免千年虫问题   
    if(temp_date.getFullYear()!=parseFloat(year)   
          ||temp_date.getMonth()!=parseFloat(month)-1   
          ||temp_date.getDate()!=parseFloat(day)){   
            return false;   
    }else{   
        return true;   
    }   
}   
  /**  
   * 验证15位数身份证号码中的生日是否是有效生日  
   * @param idCard15 15位书身份证字符串  
   * @return  
   */  
  function isValidityBrithBy15IdCard(idCard15){   
      var year =  idCard15.substring(6,8);   
      var month = idCard15.substring(8,10);   
      var day = idCard15.substring(10,12);   
      var temp_date = new Date(year,parseFloat(month)-1,parseFloat(day));   
      // 对于老身份证中的你年龄则不需考虑千年虫问题而使用getYear()方法   
      if(temp_date.getYear()!=parseFloat(year)   
              ||temp_date.getMonth()!=parseFloat(month)-1   
              ||temp_date.getDate()!=parseFloat(day)){   
                return false;   
        }else{   
            return true;   
        }   
  }   
//去掉字符串头尾空格   
function trim(str) {   
    return str.replace(/(^\s*)|(\s*$)/g, "");   
}

function checkImgFile(obj) {
	var filePath = $(obj).val();
	var pix = filePath.split("\.");
	var extension = pix[pix.length-1];
	//<![CDATA[
	if (extension !='jpg' && extension != 'jpeg' && extension != 'png') {
	//]]>
		$(obj).val("");
		alert("仅支持jpg格式图片！");
	}
}

/**
 * 显示正在操作弹窗
 * @returns
 */
function wait(){
	var id = layer.load(0,{
		shade: [0.5,'#fff'] //0.1透明度的白色背景
	});
	return id;
}

/**
 * 关闭弹窗
 */
function close_wait(id){
	layer.close(id);
}

/**
 * 提示弹窗
 * msg : 提示信息
 * flag : 显示图标， true / false
 */
function alertInfo(msg,flag){
	layer.confirm(msg, {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
}

/**
 * 提示弹窗，带回调函数
 * msg : 提示信息
 * flag : 显示图标， true / false
 * fun : 回调函数
 * 返回值 ： 弹窗的索引，用于关闭弹窗。  layer.close(alertIndex);
 */
function alertInfoFun(msg, flag, fun){
	var alertIndex = layer.alert(msg,{
			closeBtn: 0,
			icon:flag?1:2,
			shade:0.3,
			yes:fun
	});
	return alertIndex;
}

//提交表单
function submitForm(){
	var flag = true;
	//校验非空元素
	$.each($(".required"),function(i,v){
		if($.trim(v.value)==""){
			flag = false;
			layer.alert(v.title,{
				icon:2,
				shade:0.3
			});
			return false;
		}
	});
	if(flag){
		layer.load(0, {
			shade: [0.5,'#fff'] //0.1透明度的白色背景
		});
		$("#form").submit();
	}
}

//分页跳转
function paginationLink(actionUrl) {
	var waitIndex = wait();
	$("#searchForm").attr("action", actionUrl);
	$("#searchForm").submit();
}
