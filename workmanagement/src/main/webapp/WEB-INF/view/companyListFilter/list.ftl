<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title></title>
    <style>
        /*li无样式*/
		li{
			list-sytle:none;
			float: left;
		}
        .filter-condition{
            margin: 10px 20px;
            width: 800px;
            word-break:break-all;
        }
        .summaryInfo{
        	font-size: 14px;
        	font-weight: bold;
        	float:left;
        }
        .filter-condition a{
            padding:5px 10px;
            color: #333;
            text-decoration: none;
            display: inline-block;
        }

        .anchor a{
        	font-size: 12px;
        	color:rgb(56,165,226);
        	margin-left:5px;
        }
        
      	li{width: 100%;}
    </style>
    <#include "/fragment/common.ftl"/>
   <script src="${request.getContextPath()}/assets/js/jquery.cookie.js"></script> 
</head>
<body class="customProductQuery">
 <div id="covered"></div>  
 <div id="poplayer">  
	 	<div  class="jglb  borderBox">
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
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel()"/>
		     </div>
       </div>
        <div class="borderBox  hyfl">
	        	<div class="titleFont1">
	        		<span>行业列表</span>
        		</div>
	        	<div class="listBox"   >
	        		<div>
		        		<input id="searchInput" class="inputSty" type="text" value="" style="width: 150px;"/>
		        		<input   type="button" value="搜 索" class="sureBtn searchBtn" style="width: 65px; height: 30px; margin-top:10px; text-align: center;"/>
		        	</div>
	        		<table cellpadding="0" cellspacing="0" class="tablehid" >
						 
					</table>
	        	</div>
	        	<div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
		    	</div>
		</div>
		<div class="borderBox  dqlb">
	        		<div class="titleFont1">地区列表</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0"> 
					</table>
	      		 </div>
	        	<div class="btnBox">
		            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
		            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
		            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
		    	</div>
		</div>
</div>  
<a name="pageTop"></a>
<div class="company-note margin2030">
    <p class="warmFont fontSize12 marginB10">注：&nbsp;&nbsp;1）文字模糊查询，例如：*AAAA、AAAA*、*AAAA*（“*”号出现的位置代表需要模糊查询的位置）；</p>
    <p class="marginLR30 warmFont fontSize12 marginB10" style="line-height: 20px;">
        2）数字区间查询，例如：1000-、-1000、100-1000（“-”号出现在后面代表查询x大于1000；“-”号 <br>
        &nbsp;&nbsp;&nbsp;&nbsp;出现在前面代表查询x小于1000；“-”号出现在中间代表查询X大于100小于1000）；
    </p>
    <p class="marginLR30 warmFont fontSize12 marginB10">
        3）如无“*”、“-”号，代表精确查询。
    </p>
</div>
<div class="filter-condition">
	<#list allIndexName as item>
		<a class="fontSize12 cursorPointer" onclick="indexPearent('${allIndexCode[item_index]}','${item}',this)">${item}</a>
	</#list>
</div>
<form action="${request.getContextPath()}/admin/companyListFilter/query.jhtml" method="post" id="companyForm">
	<div class="filter-content showListBox noBorder" id="indexItemDiv">
		<table cellpadding="0" cellspacing="0" style="margin-left: 0px;">
			<tr>
				<td  width="300" class="noBorderL firstTD">统一社会信用代码</td>
				<td  width="500" class="secondTD"><input name="indexItemCode" type="text" class="inputSty" onblur="onblurVal(this,16,18)" maxlength="18" onKeyDown="if(this.value.length > 18){ return false }"></td>
			</tr>
			<tr id="orgcode">
				<td class="noBorderL firstTD">组织机构代码</td>
				<td class="secondTD"><input class="inputSty" name="indexItemCode" type="text" onblur="onblurVal(this,15,10)" maxlength="10" onKeyDown="if(this.value.length > 10){ return false }"></td>
			</tr>
		</table>
		
	</div>
	
	<div class="showListBox" style=" border: none;">
		<div class="showBtnBox" style="width: 70%; margin-left: 0px;">
		    <button type="reset" class="cancleBtn">清空</button>
		    <button type="submit" class="sureBtn" onclick="return checkfy(this.form);">查询</button>
		</div>
	</div>
		
	
</form> 
<a name="pageEnd"></a>
 <script type="text/javascript">
 	//申明变量
 			var This;
    //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
        $(function(){
            var a = $(".filter-condition").find("a");
            a.hover(
                function(){
                    $(this).css("background", "#F1F1F1");
                },
                function(){
                    $(this).css("background", "none");
                }
            )
            //回显
			var msg = "${msg}";
			if(msg != "") {
				alertInfo(msg);

			}
        });
        
        //组织机构代码隐藏 
        	var str="${pass}";
        	if(str=="0"){
        		$("#orgcode").hide();
        	} 
         
     //关闭上级区域弹出框
			function closePop(){
				$("#covered").hide();
				$("#poplayer").hide();
			}
			//选择上级区域
			function selUpstream(obj){
				$.each($(".seleced"),function(){
					$(this).removeClass("seleced");
				});
				$(obj).addClass("seleced");
			}
			
			//确认选择机构
			function confirmSel(clear) {
				closePop();
				This.next("input").remove() ;
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1||htext=='') {
					This.text("请选择机构"); 
				} else { 
					This.text(htext);
					$.post("${request.getContextPath()}/admin/menuAdd/getOrgCode.jhtml",{id:hteid},function(data1){
						This.prev("input").val(data1)   
					}) 
				}  
			}
			
			//确认选择行业
			function confirmSel2(clear) { 
				closePop(); 
				var seleced = $(".hyfl .seleced"); 
				var area1 = $(seleced.find("label")).text();
				var str = $(seleced).attr('id'); 
				This.next("input").remove() ;
				if(clear == 1||seleced.length==0) {
					This.text("请选行业"); 
				} else {
					This.text(area1);  
					This.prev("input").val(str)  
				}
			
				
			} 
//确认选择
			function confirmSel1(clear){
				This.next("input").remove() ;
				var seleced = $(".seleced"); //被选中的区域
				var type = $(seleced.find("input[type=hidden]")).val(); //被选地区码 
				closePop();
				 var area = $(seleced.find("label"));
				    var areaName = area.text(); 
					if(clear==1||seleced.length==0){
						This.text("请选择地区");  
					}else{ 
						This.text(areaName); 
						This.prev("input").val(type) 
					} 
			}
   function openArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px"; 
				if(obj.attr("id")==0){ //未展开
					//显示正在加载子区域弹窗
					var tc_index = layer.load(0,{
						shade: [0.5,'#fff'] //0.1透明度的白色背景
					});
					//设置为展开状态
					obj.attr("id",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						//关闭加载提示弹窗
						layer.close(tc_index);
						if(result!=null){
							var subs = result.subArea;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subArea!=null && sub.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sysAreaId+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
								var name = null;
								
								if(sub.sysAreaType==="3"){
									name = "<td id='"+sub.sysAreaId+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ sub.sysAreaCode +"' />"
											+"</td>";
								}else{
									name = "<td id='"+sub.sysAreaId+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
											+"<input type='hidden' value='"+ sub.sysAreaCode +"' />"
											+"</td>";
								}
								
								tr.append(name);
								
								ptr.after(tr);
							}
						}
					});
				}else{ //已展开
					//设置为收缩状态
					obj.attr("id",0);
					//将图标设置为收缩图标
					$(obj.find("img")[0]).css("right",20);
					//删除子区域
					delSubArea(id);
				}
			}
			
			//删除子区域
			function delSubArea(id){
				$.each($("."+id),function(i, v){
					var pid = v.attributes.getNamedItem("name").nodeValue;
					
					//删除子区域
					$(this).remove();
			
					//递归删除子区域
					delSubArea(pid);
				});
			}
    
        //数据字典
        function dicValue(dicId,obj){ 
        	
        	$.post("${request.getContextPath()}/admin/dic/getDicType.jhtml",{id:dicId},function(num){ 
        		if(num==1){ 
        			$("#covered").show();
	           		$("#poplayer").show();
	           		$(".hyfl").hide();
	           		$(".jglb").show();
	           		$(".dqlb").hide();
	            	$("#hideorg input").remove();
	            	$("#treeDemo li").remove();
	            	var loading = layer.load(); 
					$.ajax({ url:'${request.getContextPath()}/admin/manualEntry/getOrgList.jhtml', dataType:'json', success:function(nodes){ 
						var str1 = [];
						layer.close(loading);
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
        		}else if(num==2){
        			$("#covered").show();
		           	$("#poplayer").show(); 
		           	$(".hyfl").show();
	           		$(".jglb").hide();
	           		$(".dqlb").hide();
		           	$(".hyfl .tablehid tr").remove();
	        		$.post("${request.getContextPath()}/admin/dic/getDicClassFy.jhtml",function(result){ 
	        			var html="";
	        			for (var i = 0; i < result.length; i++) {
		        			html+='<tr> <td id="'+result[i].sysIndustryCode+'"  onclick="selUpstream(this)" >  <label class="fontSize12 pointer">'+result[i].sysIndustryName+'</label> </td> </tr>'
	        			}
	        			$(".hyfl .tablehid").append(html)
	        		});
        		}else if(num ==3){
        			$("#covered").show();
		           	$("#poplayer").show(); 
		           	$(".hyfl").hide();
	           		$(".jglb").hide();
	           		$(".dqlb").show();
	           		$(".dqlb  table tr").remove();
        			$.post("${request.getContextPath()}/admin/dic/getArea.jhtml",function(result){ 
							var str='<tr> <td id="'+result.sysAreaId+'" onclick="selUpstream(this)">  '+
									'<div id="0" class="open-shrink" onclick="openArea(this, '+result.sysAreaId+')">'+
									'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/> </div> '+
								'<label class="fontSize12">'+result.sysAreaName+'</label> <input  type="hidden" value="'+result.sysAreaCode+'" /> </td> </tr>'
				 
						$(".dqlb  table").append(str)
        			});
        		}else if(num==4){
        			var url = "${request.getContextPath()}/admin/dic/getDicContentsByDicIdJson.jhtml";
		        	$.post(url,{dicId:dicId},function(result){
		        		if(result.dicContentList!=null){
		        			var dicDiv="<div style='position: absolute;border: 1px solid blue;'></div>";
		        			$(obj).parent().append(dicDiv);
		        			var html="";
				        		html+="<select class='inputSty' name='indexItemCode'>";
				        		html+="<option value=''>请选择</option>";
											for(var i = 0; i < result.dicContentList.length; i++){
											 	var dicContent=result.dicContentList[i];
											 	html+="<option value="+dicContent.dicContentCode+">"+dicContent.dicContentValue+"</option>";
											}
								html+="</select>";
							var top= $(obj).offset().top;
							var left= $(obj).offset().left;
		//					$("body div:last").css("top",top);
		//					$("body div:last").css("left",left);
							$(obj).parent().html(html);
		        		}
		        	});
        		}else{
        			
        			alert("错误");
        		}
        	}); 
        	This=$(obj) 
        }
        
        //清空当前的输入框内容
        function clearThisPart(obj){
        	var parentsObj = $(obj).closest("tbody");
        	parentsObj.find("input").val(" ");
//      		parentsObj.find("select").selectedIndex = 0;
        }
        
         //单击指标大类显示指标项
			function indexPearent(indexCode,indexName,obj){
				var obj=$(obj);
				//只允许单击一次
				obj.parent().siblings().find("a").css("color","black");
				$(".stillColor").css("color","rgb(56,165,226)");
				obj.css("color","rgb(56,165,226)");
				if(obj.attr("id")==null){
					var url = "${request.getContextPath()}/admin/indexTb/getIndexItemTbListJson.jhtml";
					$.post(url,{indexCode:indexCode},function(result){
							if(result!=null){
								var html="<table cellpadding='0' cellspacing='0' style='margin-left:0px;'>";
								html += "<tr>"
									            +"<td colspan='2'>"
									                +"<a name='"+indexName+"' class='summaryInfo'>"+indexName+"</a>"
									                +"<span class='anchor floatRight solueFloat '>"
									                	+"<a class='stillColor hasUnderline' onclick='clearThisPart(this);'>清 空</a>"
									                    +"<a class='stillColor hasUnderline' href='#pageEnd'>END</a>"
									                    +"<a class='stillColor hasUnderline' href='#pageTop'>TOP</a>"
									                +"</span>"
									                +"<input class='inputSty' type='hidden' name='tables' value="+indexCode+">"
									            +"</td>"
									   +"</tr>"
	//								   +"<tr>"
	//								            +"<td class='noBorderL firstTD'>统一社会信用码</td>"
	//								            +"<td class='secondTD'>"
	//								                +"<input type='text'>"
	//								            +"</td>"
	//								    +"</tr>"
	//								    +"<tr>"
	//								            +"<td class='noBorderL firstTD'>组织机构代码</td>"
	//								            +"<td class='secondTD'>"
	//								                +"<input type='text'>"
	//								            +"</td>"
	//								    +"</tr>"
								for(var i = 0; i < result.indexItemTbList.length; i++){
									var indexItem=result.indexItemTbList[i];
									if(indexItem.indexItemType==1){
										html +="<tr>"
									            +"<td class='noBorderL firstTD' width=300>"+indexItem.indexItemName+"</td>"
									            +"<td class='secondTD' width=500>"
									                +"<input class='laydate-icon inputSty fontSize12' type='text' name='indexItemCode' autocomplete='off' onclick =\"laydate({istime: false,max:laydate.now(),format:\'YYYY-MM-DD\'})\">~"
									                +"<input class='laydate-icon inputSty fontSize12' type='text' name='indexItemCode' autocomplete='off' onclick =\"laydate({istime: false,max:laydate.now(),format:\'YYYY-MM-DD\'})\">"
									            +"</td>"
									        +"</tr>"
									}else{
										if(indexItem.dicId!=null){
											html +="<tr>"
									            +"<td class='noBorderL firstTD' width=300>"+indexItem.indexItemName+"</td>"
									            +"<td class='secondTD' width=500>"
									            	+"<input name='indexItemCode' type='hidden'>"
									                +"<a href='javascript:void(0);' class='fontSize12 changeFont stillColor' onclick='dicValue("+indexItem.dicId+",this)'>点击选择</a>"
									            +"</td>"
									        +"</tr>"
										}else{
											html +="<tr>"
									            +"<td class='noBorderL firstTD' width=300>"+indexItem.indexItemName+"</td>"
									            +"<td class='secondTD' width=500>"
									                +"<input class='inputSty' name='indexItemCode' type='text'>"
									            +"</td>"
									        +"</tr>"
										}
									}
								}
								html+="</table>";
								$("#indexItemDiv").show();
								$("#indexItemDiv").append(html);
								obj.attr("id","isClick");
								location.hash="#"+indexName;
							}
					});
				};
			}
    	
    	//提交验证
    	function checkfy(form){
    		var str=0;
    		var num=0;
    		var num1=0;
    		$("input[type='text']").each(function(){
    			if($.trim($(this).val())!=""){
    				str++
    			}
    		})
    		$("input[type='hidden']").each(function(){
    			if($.trim($(this).val())!=""  && $(this).attr("name")!="tables" ){
    				num1++
    			}
    		})
    		$("select").each(function(){ 
    			if($(this).val()!=""){
    				num++ 
    			}
    		})
    	 
    		if(str==0 && num==0 && num1==0){
    			layer.alert("请输入查询条件",{ icon:2, shade:0.3, shouldClose:true });
    			 return false;
    		}
    		if(checkTYNoChoose($("[name=indexItemCode]").eq(0).val())==0) {
				layer.alert("统一社会信用代码输入不合法",{ icon:2, shade:0.3, shouldClose:true });  
	            $("[name=indexItemCode]").eq(0).focus();
	            return false;
		    }
    		if(checkZZNoChoose($("[name=indexItemCode]").eq(1).val())==0) {
    			layer.alert("组织机构代码输入不合法",{ icon:2, shade:0.3, shouldClose:true });
	            $("[name=indexItemCode]").eq(1).focus();
	            return false;
		    }
    		if(checkTYNoChoose($("[name=indexItemCode]").eq(0).val())==3) {
    			layer.alert("统一社会信用代码输入不合法",{ icon:2, shade:0.3, shouldClose:true }); 
	            $("[name=indexItemCode]").eq(0).focus();
	            return false;
		    }
    		if(checkZZNoChoose($("[name=indexItemCode]").eq(1).val())==3) {
    			layer.alert("组织机构代码输入不合法",{ icon:2, shade:0.3, shouldClose:true }); 
	            $("[name=indexItemCode]").eq(1).focus();
	            return false;
		    }
    		//去除空表
    		$("[name=tables]").each(function(){
    			var isAllNull=true;
    			$(this).parents("table").find("[name='indexItemCode']").each(function(){
    				if($(this).val()!=""){
    					isAllNull=false;
    				}
    			});	
    			if(isAllNull==true){
    				$(this).parents("table").remove();
    			}
    		});
    		var loading = layer.load(); 
    		return true;
    	}
    	//搜索按钮
			
			
			$(".searchBtn").click(function(){
				$(".search").remove(".search");
				var  label = $(this).parent().parent().find("label"); 
				var  tablehid = $(this).parent().parent().find(".tablehid"); 
				var  orgid = label.parent().attr("id")
					 tablehid.hide();
				 	 $(this).parent().parent().append("<table class='search'></table>") 
				var  num=[];  
				for (var i = 0; i < label.length; i++) {
					var obj= new Object();
						obj.id= label.eq(i).parent().attr("id");
						obj.name= label.eq(i).text(); 
						num.push(obj);  
				}  ;
				var val= $(this).parent().children("input[type='text']").val()
				var html=""
				for (var i = 0; i < num.length; i++) {
					var str=num[i].name;;
					if(str.indexOf(val) != -1){
						html+=" <tr> <td id="+num[i].id +" onclick='selUpstream(this)'>  <label class='fontSize12'>"+num[i].name+"</label> </td> </tr>"
					}
				} 
				$(".search").append(html);
			})
			
			
			
			$.cookie("chox",'', { expires: 1 });
			$.cookie("noche",'', { expires: 1 });
			$.cookie("allche",'', { expires: 1 });
    </script>
</body>
</html>