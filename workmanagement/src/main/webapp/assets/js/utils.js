/**
 * 是否是手机号码
 * @param value
 * @param param
 * @returns
 */
function isPhone(value, param){
	var reg = /^1[3-9][0-9]{9}$/;
    return reg.test(value);
}

/**
 * 是否是座机号码
 * @param value
 * @param param
 * @returns
 */
function isTel(value, param){
	var reg = "^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$"
	return reg.test(value);
}

/**
 * 校验是否是手机或座机号码
 * @param value
 * @returns
 */
function isTP(value){
	var p = /^1[3-9][0-9]{9}$/;
	var t = /^(0[0-9]{2,3}\-)?([2-9][0-9]{6,7})+(\-[0-9]{1,4})?$/;
	return (p.test(value)||t.test(value));
}

//日期计算
Date.prototype.DateAdd = function(strInterval, Number) {   
    var dtTmp = this;  
    switch (strInterval) {   
        case 's' :return new Date(Date.parse(dtTmp) + (1000 * Number));  
        case 'n' :return new Date(Date.parse(dtTmp) + (60000 * Number));  
        case 'h' :return new Date(Date.parse(dtTmp) + (3600000 * Number));  
        case 'd' :return new Date(Date.parse(dtTmp) + (86400000 * Number));  
        case 'w' :return new Date(Date.parse(dtTmp) + ((86400000 * 7) * Number));  
        case 'q' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number*3, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
        case 'm' :return new Date(dtTmp.getFullYear(), (dtTmp.getMonth()) + Number, dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
        case 'y' :return new Date((dtTmp.getFullYear() + Number), dtTmp.getMonth(), dtTmp.getDate(), dtTmp.getHours(), dtTmp.getMinutes(), dtTmp.getSeconds());  
    }  
}  

//| 求两个时间的天数差 日期格式为 YYYY-MM-dd   
//+---------------------------------------------------  
function daysBetween(DateOne,DateTwo)  
{   
  var OneMonth = DateOne.substring(5,DateOne.lastIndexOf ('-'));  
  var OneDay = DateOne.substring(DateOne.length,DateOne.lastIndexOf ('-')+1);  
  var OneYear = DateOne.substring(0,DateOne.indexOf ('-'));  

  var TwoMonth = DateTwo.substring(5,DateTwo.lastIndexOf ('-'));  
  var TwoDay = DateTwo.substring(DateTwo.length,DateTwo.lastIndexOf ('-')+1);  
  var TwoYear = DateTwo.substring(0,DateTwo.indexOf ('-'));  

  var cha=((Date.parse(OneMonth+'/'+OneDay+'/'+OneYear)- Date.parse(TwoMonth+'/'+TwoDay+'/'+TwoYear))/86400000);   
  return Math.abs(cha);  
}  

//| 字符串转成日期类型   
function stringToDate(DateStr)  
{   
	var converted = Date.parse(DateStr);  
	var myDate = new Date(converted);  
	if (isNaN(myDate))  
	{   
		  //| 格式 YYYY-MM-dd HH:mm:ss
	    var arys = DateStr.split('-');  //年、月、日、小时、分钟、秒
	    var day = arys[2].split(" "); //日、小时、分钟、秒
	    if(day.length==2){
	  	  var hms = day[1].split(":"); //小时、分钟、秒
	  	  if(hms.length==3){
	  		  myDate = new Date(arys[0],--arys[1],day[0],hms[0],hms[1],hms[2]);  
	  	  }else{
	  		  myDate = new Date(arys[0],--arys[1],day[0],hms[0],hms[1]);  
	  	  }
	    }else{
	  	  myDate = new Date(arys[0],--arys[1],arys[2]);  
	    }
	}  
	return myDate;  
}  

//日期格式化 new Date().format("yyyy年MM月dd日 hh:mm");
Date.prototype.format = function(format){ 
	var o = { 
	"M+" : this.getMonth()+1, //month 
	"d+" : this.getDate(), //day 
	"h+" : this.getHours(), //hour 
	"m+" : this.getMinutes(), //minute 
	"s+" : this.getSeconds(), //second 
	"q+" : Math.floor((this.getMonth()+3)/3), //quarter 
	"S" : this.getMilliseconds() //millisecond 
	} 

	if(/(y+)/.test(format)) { 
	format = format.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
	} 

	for(var k in o) { 
	if(new RegExp("("+ k +")").test(format)) { 
	format = format.replace(RegExp.$1, RegExp.$1.length==1 ? o[k] : ("00"+ o[k]).substr((""+ o[k]).length)); 
	} 
	} 
	return format; 
}
//当前时间的 yyyy年MM月dd日 hh:mm 格式
function getNowFormatDate() {
			    var date = new Date();
			    var seperator1 = "-";
			    var seperator2 = ":";
			    var month = date.getMonth() + 1;
			    var strDate = date.getDate();
			    if (month >= 1 && month <= 9) {
			        month = "0" + month;
			    }
			    if (strDate >= 0 && strDate <= 9) {
			        strDate = "0" + strDate;
			    }
			    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
			            + " " + date.getHours() + seperator2 + date.getMinutes()
			            + seperator2 + date.getSeconds();
			    return currentdate;
}
//当前时间的 yyyy年MM月dd日 格式
function getNowDate() {
			    var date = new Date();
			    var seperator1 = "-";
			    var seperator2 = ":";
			    var month = date.getMonth() + 1;
			    var strDate = date.getDate();
			    if (month >= 1 && month <= 9) {
			        month = "0" + month;
			    }
			    if (strDate >= 0 && strDate <= 9) {
			        strDate = "0" + strDate;
			    }
			    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate;
			    return currentdate;
}
/**
 * 只校验身份证格式
 * @param card
 * @returns {Boolean}
 */
function isCardNo(card)  
{  
   // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X  
   var reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  
   return reg.test(card);
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

/**
 * 合格返回true
 * @param obj
 * @returns {Boolean}
 */
function checkImgFile(obj) {
	var filePath = $(obj).val();
	var pix = filePath.split("\.");
	var extension = pix[pix.length-1];
	if (extension !='jpg' && extension != 'jpeg' && extension != 'png') {
		return false;
	}
	return true;
}