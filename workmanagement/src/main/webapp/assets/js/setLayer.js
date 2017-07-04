onblurVal

//后面灰色提示
//后面灰色提示
$(function(){
		var str = "";
		//所有名称
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（不能输入以数字开头且除了 ( ) + - _ 以外的其它特殊字符）</span>"
		$(".allnameVal").after(str);
		//不超过五位数字
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入不超过5位数字）</span>"
		$(".allnumberVal").after(str);
		//请输入数字字母组合
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入数字字母组合）</span>"
		$(".allnumzmVal").after(str);
		//请输入数字字母-及组合
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入字母  数字  - + _ ( )及组合）</span>"
		$(".allnamezmVal").after(str);
		
		//备注
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入不超过50个字的备注）</span>"
		$(".allnoteVal").after(str);
		//20个字的中文
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入不超过20个字的中文）</span>"
		$(".allbzVal").after(str);
		//20个字的英文代码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入不超过20个字的英文不区分大小写）</span>"
		$(".alldmVal").after(str);
		//请填入小数或者整数
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入小数或者整数）</span>"
		$(".alldicExchangeVal").after(str);
		//20个字的中文代码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入不超过50个字的中文）</span>"
		$(".allChineseVal").after(str);
		//14个字的机构编码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入14位机构编码）</span>"
		$(".allorgcodeVal").after(str);
		
		//电话号码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入电话号码，手机座机都可以）</span>"
		$(".alltalVal").after(str);
		//
		//地址
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请填入机构地址）</span>"
		$(".alldressVal").after(str);
		
		//请输入10位组织机构码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入10位组织机构码）</span>"
		$(".allorgnumVal").after(str);
		//请输入18位统计社会信用码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入18位统计社会信用码）</span>"
		$(".allcreditcodeVal").after(str);
		//请输入10位组织机构码或者请输入18位统计社会信用码
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '></span>"
		$(".allcorgcodeVal").after(str);
		//请输入许可证名称
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入许可证名称）</span>"
		$(".allturecardVal").after(str);
		//入资金数
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入资金数，如1万2,1.2万，12000）</span>"
		$(".allmonnyVal").after(str);
		//请输入以字母开头的字母和数字的组合
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入以字母开头的字母和数字的组合）</span>"
		$(".allletterVal").after(str);
		//请输入18位身份证
		str = "<span style='font-size:12px;color:#787878;margin-left:10px '>（请输入18位身份证）</span>"
		$(".allidcardVal").after(str);
		
		
		
})

//包含了验证js  ，设置弹出框  ，设置时间控件

			//统一组织的输入框验证
			function checkTYNoLT18(theObj){
		    	var reg = /^[0-9a-zA-Z-]{0,18}$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}  
			
			//企业名单筛选组织
			function checkZZNoChoose(theObj){
		    	var reg = /^[\*0-9a-zA-Z-][0-9a-zA-Z-]{0,8}[\*0-9a-zA-Z-]$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(theObj == "**"){
					return 3;
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
			//企业名单筛选统一
			function checkTYNoChoose(theObj){
		    	var reg = /^[\*0-9\-a-zA-Z][0-9a-zA-Z]{0,16}[\*0-9\-a-zA-Z]$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(theObj == "**"){
					return 3;
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
			
			//企业二码验证
			function BusinessCreditCode(theObj){
		    	var reg = /(^[a-zA-Z0-9]{8}[\-]{1}[a-zA-Z0-9]{1}$)|(^[0-9a-zA-Z]{18}$)/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
			
			
			
			
			
			//长度不为0的数字
			function checkNo0(theObj){
		    	var reg = /[1-9][0-9]{0,11}/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 

			//统一社会信用码|组织机构代码模糊查询
			function checkTYNo(theObj){
		    	var reg = /^[\*0-9\-a-zA-Z]{0,18}$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}  
		    
		    //汉字模糊查询
			function checkTChineseM(theObj){
		    	var reg = /^([a-zA-Z\-0-9\u4E00-\u9FA5]+)$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}  
		    
			
			//是否为资金
			function checkPrice(theObj) {  
				var reg = /^[1-9][万0-9，.]{0,8}$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			} 
			
			
			//是否为数字
			function checkNumber(theObj) {  
				var reg = /^([0-9]{0,20})$/; 
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}  
			
			//是否为汉字
			function checkChinese(theObj){
				 
				var reg = /^([\u4E00-\u9FA5]{0,50})$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//汉字 字母 数字  不以数字开头，包含特定字符
			
			function checkChineseNoSpe(theObj){
				var reg = /^[a-zA-Z\u4e00-\u9fa5\(\)\（\）\-\_\+][a-zA-Z\d\u4e00-\u9fa5\(\)\（\）\-\_\+]{0,49}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			
			// 包含特定字符查询
			
			function thetype(theObj){
				var reg = /^[a-zA-Z\d\u4e00-\u9fa5\(\)\（\）\-\+\_]{0,49}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			//汉字 字母 数字  不以数字开头，包含特定字符
			
			function indextype(theObj){
				var reg = /^[a-zA-Z\u4e00-\u9fa5\（\）\_]{0,19}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//是否为汉字 字母 数字  不能有特殊字符
			
			function checkChineseNoSpe12(theObj){
				var reg = /^([a-zA-Z\u4e00-\u9fa5\(\)\（\）\、]{0,30})$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			
			function checkChineseNoSpe50(theObj){
				var reg =/^([\s\S]{0,50})$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//是否为数字+字母+特殊符号
			function checkStrAddNumSpe(theObj){	
				var reg = /^[0-9a-zA-Z\-\_\~\!\@\#\$\%\^\&\*\(\)\=\+]{0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}

			
			
			//是否为正确的电话号码
			function checkTel(theObj){
				var reg = /^1\d{10}$|^(0\d{2,3}-?|\(0\d{2,3}\))?[1-9]\d{4,7}(-\d{1,8})?$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			// 是否为手机号码
			function checkPhone(theObj){
				var reg = /^1\d{10}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			
			
			//是否为正确的身份证号码
			function checkIDCard(theObj){
				var reg = /^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//是否为英文
			function checkEnglish(theObj){
				var reg = /^[a-zA-Z]{0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			
			//是否小数或整数
			function checkFloat(theObj){
				var reg = /^[0-9]+([.]{1}[0-9]+){0,1}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//明确的统一社会信用码
			function checkSureTY(theObj){
				var reg = /^[\*0-9a-zA-Z-]{18}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//明确的组织机构码
			function checkSureZZ(theObj){
				var reg = /^[\*0-9a-zA-Z-]{10}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//不能输入英文的单引号 双引号，不然老子代码会出错，操
			function checkQuotes(theObj){
				var reg = /^[^\'\"]*$/;
				var r = theObj.match(reg); 
				if(r==null){
					return 0;
				}
				else{	
					return 1;
				}
			}
			//
			function checkName(theObj){
				var reg = /^[0-9a-zA-Z\-\_\(\)\+]{0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){					
					return 1;
				}
				if(r==null){
					return 0;
				}
				else{	
					return 2;
				}
			}
						
			//是否为数字+字母
			function checkStrAddNum(theObj){	
				var reg = /^[0-9a-zA-Z]{0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
			
			//不以数字开头的字符串
			function checkNoDataHeader(theObj){
				var reg = /^[a-zA-Z\u4e00-\u9fa5]+([0-9a-zA-Z\u4e00-\u9fa5]){0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			
			//以字母开头的字母后面接数字的组合
			function checkNoDataHeader12(theObj){
				var reg = /^[a-zA-Z]+([0-9]){0,20}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			
			//4位以字母开头的字母后面接数字的组合 政府类别代码
			function testGovCode(theObj){
				var reg = /^[a-zA-Z]+([0-9]){3}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			
			
			//9位纯数字 行政代码
			function administrativeCode(theObj){
				var reg = /^[0-9]{6,9}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			//9位纯数字 行政代码
			function administrativeCode6(theObj){
				var reg = /^[0-9]{6}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			//9位纯数字 行政代码
			function administrativeCode9(theObj){
				var reg = /^[0-9]{9}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
				
			}
			//14位以字母开头的字母后面接数字的组合 政府类别代码
			function checkSysCode(theObj){
				var reg = /^[a-zA-Z]+([0-9]){13}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1;
				}
				if(r==null){
					return 0;
				}else{
					return 2;
				}
			
			}
			//判断是否为汇率
			function checkExchangeRate(theObj){
				var reg = /^(?!^[0](\.{1}[0]{1,2}){0,1}$)(?:[1-9]{1}[0-9]{0,10}|[0]{1})(\.{1}[0-9]{1,4}){0,1}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{	
					return 2;
				}
			}
			
			//数据字典验证 
			function inputname(theObj){
				var reg = /^[a-zA-Z\u4e00-\u9fa5\(\)\-\_\+][a-zA-Z\d\u4e00-\u9fa5\(\)\-\_\+]{0,50}$/;
				var r = theObj.match(reg); 
				if(theObj == ""){
					return 1
				}
				if(r==null){
					return 0;
				}
				else{				
					return 2;
				}
			}
		 
			//不能输入英文的单引号 双引号，不然老子代码会出错，操
			function checklt50(theObj){
				var reg = /^.{0,50}$/;
				var r = theObj.match(reg); 
				if(r==null){
					return 0;
				}
				else{	
					return 1;
				}
			}
			
			//验证长度
			//theObj1 = 当前输入框
			//theObj2 = 要求的字符长度
			function checkLength(theObj1,theObj2){
				if(theObj2 != 0){
						var length1 = theObj1.val().length; 
						var length2 = theObj2;
						if(length1>length2){
							if(theObj1.nextAll(".fontLength").length == 0){
								var str = "";
								str = "<span class='fontLength'>最多输入"+ theObj2 +"个字符</span>"
								theObj1.after(str);
							}	
						}
						else{
							theObj1.nextAll(".fontLength").remove();
						}
				
					}
				else{
					
				}
			}
			
			function judgeNum(obj,num1,num2){
				if($(obj).val()<num1||$(obj).val()>num2){
					if($(obj).nextAll(".redWarm6").length == 0){
						var str = "";
						str = "<span class='redWarm6'>输入的值应小于"+num2+",大于"+num1+"</span>"
						$(obj).after(str);
					}
				}
				else{
					$(obj).nextAll(".redWarm6").remove();
				}
			}
			
			
			//num1=-2判断是否为数字字母-组合
			//num1 == -1 不为0的数字
			//num1 == 0  判断是否为数字
			//num1 == 1 判断是够为汉字
			//num1 == 2 判断是否为数字字母组合
			//num1 == 3 判断是否为电话
			//num1 == 4 判断是否为统一码模糊查询
			//num1 == 5 判断是否为身份证
			//num1 == 6 英文字母
			//num1 == 7 判断是否为小数或整数
			//num1 == 8 判断是否为资金数
			//num1 == 9 汉字模糊查询
			//num1 == 10  数字字母特殊符号
			//num1 == 11 社会信用码
			//num1 == 12 组织机构代码
			//num1 == 13 汉字,数字,字母 ,除 ( ) （ ） - _ 以外的其它特殊字符,不能以数字开头
			//num1 == 14 50位一下的备注
			//num1 == 15 组织机构模糊 企业监测筛选
			//num1 == 16 统一社会模糊 企业监测筛选
			//num1 == 17 不以数字开头的字符串
			//num1 == 18判断是否为汇率
			//num1 == 19判断是否为手机号码
			//num1 == 21 以字母开头的字母和数字的组合  行业代码
			//num1 == 20   名称验证
			//num1 == 24 9位纯数字的行政代码
			//num1 == 100 不进行任何判断
			//num1 == 30字典验证
			//num2 number   需要限制的最大长度
			//num2 == 0 不进行所需位数的判定
			
			function onblurVal(obj,num1,num2){
				//再次注意这个坑
				var obj = $(obj);
				var getVal = obj.val();
//				if(num1 !=100||num1 != 14){
//					getVal = getVal.replace(/\s/g, "");
//				}
					if(num1 == -2){
						if(checkName(getVal)==0){
							if(obj.nextAll(".redWarm3").length == 0){
								var str = "";
								str = "<span class='redWarm3'>请输入字母  数字  - _ + ( )及组合</span>"
								obj.after(str);
							}
							return false;
						}
						else{
							obj.nextAll(".redWarm3").remove();
						}
					}
					
					
					
					if(checkQuotes(getVal) == 0){
						if(obj.nextAll(".redWarm5").length == 0){
							var str = "";
							str = "<span class='redWarm5'>不能输入英文的单引号或双引号</span>"
							obj.after(str);
						}
						
						return false;
						
					}
					else{
						obj.nextAll(".redWarm5").remove();
					}
					
					//生成 “请输入不为0的数字”提示
					if(num1 == -1){
						if(checkNo0(getVal) == 0){
							if(obj.nextAll(".redWarm1").length == 0){
								var str = "";
								str = "<span class='redWarm1'>请输入大于0的数字</span>"
								obj.after(str);
							}
							
							return false;
						}
						else{
							obj.nextAll(".redWarm1").remove();
						}
					}
					
					
				
				//生成 “请输入数字”提示
				if(num1 == 0){
					if(checkNumber(getVal) == 0){
						if(obj.nextAll(".redWarm1").length == 0){
							var str = "";
							str = "<span class='redWarm1'>请输入数字</span>"
							obj.after(str);
						}	
						
						return false;
					}
					else{
						obj.nextAll(".redWarm1").remove();
					}
				}
				
				//生成“请输入汉字提示”
				if(num1 == 1){
					if(checkChinese(getVal) == 0){
						if(obj.nextAll(".redWarm2").length == 0){
							var str = "";
							str = "<span class='redWarm2'>请输入中文</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm2").remove();
					}
				}
				
				//生成“请输入数字字母组合”提示
				if(num1 == 2){
					if(checkStrAddNum(getVal) == 0){
						if(obj.nextAll(".redWarm3").length == 0){
							var str = "";
							str = "<span class='redWarm3'>请输入字母、数字或组合</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm3").remove();
					}
				}
						
				//生成“请输入正确的电话信息”
				if(num1 == 3){
					if(checkTel(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的电话信息</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//请输入统一社会信用码
				if(num1 == 4){
					if(checkTYNo(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入18位社会信用码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
			
				//请输入正确的身份证号码
				if(num1 == 5){
					if(checkIDCard(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的身份证号码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//请输入英文字母
				if(num1 == 6){
					if(checkEnglish(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入英文，不区分大小写</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//请输入整数或小数
				if(num1 == 7){
					if(checkFloat(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入整数或小数</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				
				//请输入资金数
				if(num1 == 8){
					if(checkPrice(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入资金数，如1万2,1.2万，12000</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
		
			
				//请输入汉字模糊查询
				if(num1 == 9){
					if(checkTChineseM(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入中文，英文，*，-等</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}

			//num1 == 11 社会信用码
			//num1 == 12 组织机构代码
				//请输入数字或字母或组合
				if(num1 == 10){
					if(checkStrAddNumSpe(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入数字，字母，或部分特殊符号</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 11 社会信用码
				if(num1 == 11){
					if(checkSureTY(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入18位统计社会信用码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 12  组织机构码
				if(num1 == 12){
					if(checkSureZZ(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入10位组织机构码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 13  请输入中文，英文，数字，不能输入特殊符号
				if(num1 == 13){
					if(checkChineseNoSpe(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的名称</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 14  请输入中文，英文，数字，能输入特殊符号  50位
				if(num1 == 14){
					if(checkChineseNoSpe50(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入中文，英文，数字，或特殊符号</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				
				
				//企业监测筛选  组织
				if(num1 == 15){
					if(checkZZNoChoose(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入合法的模糊查询</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//企业监测筛选   统一
				if(num1 == 16){
					if(checkTYNoChoose(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入合法的模糊查询</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				

				//num1 == 17   不以数字开头的字符串
				if(num1 == 17){
					if(checkNoDataHeader(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入不以数字开头且不含特殊字符的字符串</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 18   是否为汇率
				if(num1 == 18){
					if(checkExchangeRate(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的汇率，整数位≤11位，小数位≤4位</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 19   是否为手机号码
				if(num1 == 19){
					if(checkPhone(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的手机号码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
		 
				
				
				//num1 == 21 以字母开头的字母和数字的组合
				if(num1 == 21){
					if(checkNoDataHeader12(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入以字母开头的字母和数字的组合</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//num1 == 22 4位以字母开头的字母和数字的组合
				if(num1 == 22){
					if(testGovCode(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入4位以字母开头的字母和数字的组合</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				
				//num1 == 23 14位以字母开头的字母和数字的组合
				if(num1 == 23){
					if(checkSysCode(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入14位以字母开头的字母和数字的组合</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				
				//num1 == 24 6~9位纯数字的行政代码
				if(num1 == 24){
					if(administrativeCode(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入6~9位纯数字的行政代码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				//num1 == 25 企业二码验证
				if(num1 == 25){
					if(BusinessCreditCode(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确的统一社会信用码/组织机构代码</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				//26
				if(num1 == 26){
					if(indextype(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确名称</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				
				//num1 == 30 字典验证
				if(num1 == 30){
					if(inputname(getVal) == 0){
						if(obj.nextAll(".redWarm4").length == 0){
							var str = "";
							str = "<span class='redWarm4'>请输入正确字典名字</span>"
							obj.after(str);
						}
						
						return false;
					}
					else{
						obj.nextAll(".redWarm4").remove();
					}
				}
				
				// 以下是不能超过多少位
				checkLength(obj,num2);
			}
			
			function judgeNum(obj,num1,num2){
				if($(obj).val()<num1||$(obj).val()>num2){
					if($(obj).nextAll(".redWarm6").length == 0){
						var str = "";
						str = "<span class='redWarm6'>输入的值应小于"+num2+",大于"+num1+"</span>"
						$(obj).after(str);
					}
				}
				else{
					$(obj).nextAll(".redWarm6").remove();
				}
			}

			function setLayer(str1,str2){
				 layer.open({
			  		type: 2,
			  		title: str1,
//			  		shade: 0.6,
//			  		shadeClose:false,
			  		area: ['90%', '500px'],
			  		maxmin: true,
			  		content: str2});
	
			}
			function setLayerall(str1,str2){
				 layer.open({
			  		type: 2,
			  		title: str1,
//			  		shade: 0.6,
//			  		shadeClose:false,
			  		area: ['100%', '100%'],
			  		maxmin: true,
			  		content: str2});
	
			}
			
			//时间控件设置，传入元素id或者class
			function setLayDate(elem){
				laydate({
					elem:""+elem+"",
					event:"click",
					format:"YYYY-MM-DD",
					max:laydate.now(),
					istime:false,
					isclear:true,
					istoday:true,
					festival:true,
					fixed: false
				});	
			}
			$(function(){
				if($(".laydate-icon").val() == ""){
					var myDate = new Date();
					var dateInner = "";
					var needM0 = "";
					var needD0 = "";
					if(myDate.getMonth()+1<10){
						needM0 = "0"
					}
					if(myDate.getDate()<10){
						needD0 = "0"
					}
					
					dateInner = myDate.getFullYear()+"-"+needM0+(myDate.getMonth()+1)+"-"+needD0+myDate.getDate();
					$(".laydate-icon").val(dateInner);
				}
			})
