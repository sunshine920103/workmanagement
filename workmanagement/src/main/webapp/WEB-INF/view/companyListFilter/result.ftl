<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
	<script src="${request.getContextPath()}/assets/js/jquery.cookie.js"></script> 
    <title></title>
    <style>
        .queryResult{
            width: 800px;
            margin: 100px auto;
            border: 1px solid #DADADA;
            padding: 20px;
        }
        .queryHead{
            width: 100%;
            height: 30px;
            line-height: 30px;
            font-weight: bold;
            text-align: center;
            margin-bottom: 10px;
        }
        .queryResult table{
            width: 100%;
            border-top: 1px solid #dadada;
            border-left: 1px solid #dadada;
            font-size: 12px;
        }
        .queryResult th{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
            text-align: left;
        }
        .queryResult td{
            padding: 5px 10px;
            border-right: 1px solid #dadada;
            border-bottom: 1px solid #dadada;
        }
        .titleFont1 {
            padding-left: 10px;
            height: 30px;
            line-height: 30px;
            font-size: 12px;
            text-align: left;
            border: 1px solid #dadada;
            border-bottom: none;
        }
        .queryResult .left{
            text-align: left;
        }
        .queryResult .right{
            text-align: right;
        }
        .queryDetails{
            padding: 10px 3px;
        }
        .flip-box{
            width: 100%;
            height: 50px;
            margin-top: 20px;
        }
        .flip-page{
            height: 30px;
            float: right;
        }
        .flip-page li{
            float: left;
            margin-right: 15px;
            list-style: none;
        }
        .flip-page a{
            color: #3366CC;
        }
        .queryBtn{
        	margin-top: 30px;
            width: 100%;
            height:50px;
            text-align: center;
        }
        .queryBtn button{
            width: 120px;
            height: 30px;
            margin: 0 20px;
            outline: none;
            border: none ;
            cursor: pointer;
            color: white;
            border-radius: 4px;
        }
        .queryBtn .cancel{
            background-color: #a4a4a4;
        }
        .queryBtn .query{
            background-color: rgb(56,165,226);
        }
        /*#trPanel span:hover{
        	cursor:pointer;
        }*/
       #trPanel .sort{
       		
       }
       .positionR{
       	position: relative;
       }
       .positionA{
       	position: absolute;
       }
    </style>
    <script type="text/javascript">
    	$(function(){
    		mySetFlag($(".redFlagCredit"),"${userKeyCredit}");
			mySetFlag($(".redFlagOrg"),"${userKeyOrg}");
			
			$("#downLoadExcel").click(function(){
				if("${defaultIndexItemCustomList[0]}"!=""){
					window.location.href='${request.getContextPath()}/admin/companyListFilter/downLoadConditionList.jhtml';
				}else{
					alertInfo("查询结果为空，无法导出excel")
//					layer.alert("",{
//						icon: 2,
//						time:15000,
//						shade:0.3,
//						shadeClose:true
//					});
				}
			})
			$("#trPanel .sort").parent().css("width","15");
			$("#trPanel .sort").parent().css("display","inline-block");
//			$("#trPanel .sort").parents("td").css("height","11px");
			$("#trPanel .sort").parents("td").css("line-height","11px");
//			$("#trPanel .sort").parents("td").css("overflow","hidden");
			$("#trPanel span").mouseover(function(){
				$(this).css("cursor","pointer");
			});
			$("#trPanel div").click(function(){
				sortTable($(this).find("span:eq(0)"), 'trPanel');
				$("#trPanel span").css("color","black");
				if($(this).find("span:eq(0)").attr("color")=="rgb(56,165,226)"){
					$(this).find("span:eq(0)").next().removeAttr("color");
					$(this).find("span:eq(0)").next().css("color","rgb(56,165,226)");
					$("#trPanel span").attr("color","black");
				}else{
					$(this).find("span:eq(0)").removeAttr("color");
					$(this).find("span:eq(0)").css("color","rgb(56,165,226)");
					$("#trPanel span").attr("color","rgb(56,165,226)");
				}
			});
    	})
    	/**
 * 设置文本关键字标红
 * @param objs 文本需要被标红的对象
 * @param flag objs需要被标红的字符
 */
function mySetFlag(objs, flag){
	$.each(objs,function(i,v){
		var str = $(this).text();
		var flagTwo=flag;
		var html= "";
		if(str!=""&&flag!=""){
			if(flag.substring(0,1)=="*" && flag.substring(flag.length-1,flag.length)=="*"){//为*xx*时
				flag=flag.substring(1,flag.length-1);
				if(str.indexOf(flag)!= -1){
					var idx= str.indexOf(flag);
					html= str.substring(0,idx)+"<span style='font-size:16px;color:red;'>"+flag+"</span>"+str.substring(idx+flag.length);
				}
			}
			else if(flag.substring(0,1)=="*"){
				flag=flag.substring(1);
				if(str.substring(str.length-flag.length,str.length)==flag){//是否以关键字结尾
					html= str.substring(0,str.lastIndexOf(flag))+"<span style='font-size:16px;color:red;'>"+flag+"</span>";
				}
			}else if(flag.substring(flag.length-1,flag.length)=="*"){
					flag=flag.substring(0,flag.length-1);
					if(str.substring(0,flag.length)==flag){//是否以关键字开头
						html= "<span style='font-size:16px;color:red;'>"+flag+"</span>"+str.substring(flag.length);
					}
			}else{
				html=str;
			}
			$(this).html(html)
		}
		flag=flagTwo;
	});
}

 //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
		function sortTable(el, tbodyId, compareFun) {
				// 添加其它列的状态
				var nowTd = $(el);
				if(!nowTd.is('td')) {
					nowTd = nowTd.closest('td')
				}
				nowTd.siblings('td').each(function() {
					if($(this).find('[data-dirct]').length) {
						$(this).find('[data-dirct]').attr('data-dirct', '');
					} else {
						$(this).attr('data-dirct', '');
					}
				});
				var nowDirct = $(el).attr('data-dirct');
				var table = $(el).closest('table');
				var tbody = $('#' + tbodyId)
				if(!nowDirct) {
					nowDirct = 'asc';
					$(el).attr('data-dirct', nowDirct);
				} else {
					$(el).attr('data-dirct', nowDirct == 'asc' ? 'desc' : 'asc');
					reverse();
					setSeq();
					return;
				}

				function reverse() {
					var trs2 = table.find('tr:not(:first)');
					for(var i = trs2.length - 2; i >= 0; i--) {
						trs2.eq(i).appendTo(tbody);
					}
				}

				function setSeq() {
					var tsq = table.find('.sequence');
					for(var i = 0; i < tsq.length; i++) {
						tsq.eq(i).text((i + 1));
					}
				}

				function getTdVal(td) {
					var val = td.attr('data-val');
					if(!val) {
						val = $.trim(td.text());
					}
					if(/^[\d\.]+$/.test(val)) {
						val = 1 * val;
					}
					return val;
				}
				if(!compareFun) {
					compareFun = function(str1, str2) {
						if(typeof str1 == "number" && typeof str2 == "number") {
							return str1 - str2;
						} else {
							str1 = '' + str1;
							str2 = '' + str2;
							return str1.localeCompare(str2);
						}
					}
				}
				// 得到所有tr 得到单元格位置
				var trs = $(el).closest('table').find('tr:not(:first)');
				var index = $(el).closest('td').index();
				for(var i = 0; i < trs.length - 1; i++) {
					for(var j = 0; j < trs.length - 1 - i; j++) {
						var str1 = getTdVal(trs.eq(j).find('td').eq(index));
						var str2 = getTdVal(trs.eq(j + 1).find('td').eq(index));
						if(compareFun(str1, str2) > 0) {
							trs.eq(j + 1).after(trs.eq(j));
							var tmp = trs[j + 1];
							trs[j + 1] = trs[j];
							trs[j] = tmp;
						}
					}
				}
				setSeq();
			} 
			//传值cookie
		$(function(){  
			$(':checkbox').click(function(){
				var str=''; 
				var chec=$(this).val(); 
				if($(this).is(':checked')){
					str=$.cookie("chox")+chec+','; 
					$.cookie("chox",str, { expires: 1 }); 
					
					//正常取消选择cookie里删除
					str=$.cookie("noche");  
					str=str.replace(chec+',','');  
					$.cookie("noche",str, { expires: 1 });
				}else if($.cookie("allche")==1){   
					str=$.cookie("noche")+chec+','; 
					$.cookie("noche",str, { expires: 1 });   
				}else if($.cookie("allche")==''){
					//正常取消选择cookie里删除
					str=$.cookie("chox");  
					str=str.replace(chec+',','');  
					$.cookie("chox",str, { expires: 1 });  
				}
			})
			
			//回显，加载 
			var ACD=$.cookie("chox");
			var allche=$.cookie("allche"); 
			var noche=$.cookie("noche");
			
				if(allche=='' && ACD!=''){
					var strs=ACD.split(",");
					var newche=$(':checkbox'); 
					for (var j = 0; j < strs.length; j++) {
						for (var i = 0; i < newche.length; i++) {
							 if(strs[j]==newche.eq(i).val()){
							 	$(':checkbox').eq(i).prop("checked", true);
							 }
						} 
					} 
				}else if(allche=='1' && noche ==" "){
					$(':checkbox').attr("checked", true);
				}else if(allche=='1'  && noche !=" "){
					var strs=noche.split(",");
					var newche=$(':checkbox'); 
					for (var j = 0; j < strs.length; j++) {
						for (var i = 0; i < newche.length; i++) { 
							 if(strs[j]!=newche.eq(i).val()){
							 	$(':checkbox').eq(i).prop("checked", true);
							 }else{
							 	$(':checkbox').eq(i).prop("checked", false);
							 }
						} 
					} 
				}
		 
		})
		
		
		
    </script>
</head>
<body>
<form id="searchForm" method="post">
		</form>
	<form action="${request.getContextPath()}/admin/companyListFilter/getQyxx.jhtml" id="postid" method="post">
		 
            <input type='hidden' value='' name ='check' id="checkeaaa" >
            <input type='hidden' value='' name ='id'  id="checkeaaaid">
	</form>
    <div class="queryResult showListBox">
      <p class="queryHead">企业名单筛选结果</p>
        <table cellpadding="0" cellspacing="0">
            <caption class="titleFont1 titleFont1Ex">条件筛选结果列表</caption>
            <#list conditionList as item >
            	<tr>
	                <td width="200" class="noBorderL firstTD">${item.reportIndexErrorName}</td>
	                <td width="500" class="secondTD" style="color: red;">${item.reportIndexErrorNotes}</td>
	            </tr>
            </#list>
        </table>
        
        <table style=" margin-top: 0px;border-top: 0px;" cellpadding="0" cellspacing="0" >
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
		</table>
        <div style="margin-bottom: 30px;">
        	
        </div>
        <form action="${request.getContextPath()}/admin/companyListFilter/getQyxx.jhtml" id="formtab" method="post">
        <table cellpadding="0" cellspacing="0">
        	<caption class="titleFont1 titleFont1Ex">企业名单列表</caption>
            <tbody id="trPanel">  <tr> 
           
            		<td width="100">
            		 	<p  onclick="CheckedRev()" style='color:#f00' class='cursorPointer'>全选 </p>
					</td>
	                <td width="100">序号</td>
	                <td width="200"><div class="positionR"><span class="sort positionA fontSize12" style="top: -15px;">▲</span><span class="positionA fontSize12" style="top: -3px;">▼</span></div>企业二码</td>

	                <td width="200"><div class="positionR"><span class="sort positionA fontSize12" style="top: -15px;">▲</span><span class="positionA fontSize12" style="top: -3px;">▼</span></div>企业名称</td>
	                <td width="200"><div class="positionR"><span class="sort positionA fontSize12" style="top: -15px;">▲</span><span class="positionA fontSize12" style="top: -3px;">▼</span></div>联系电话</td>
	                <td width="200"><div class="positionR"><span class="sort positionA fontSize12" style="top: -15px;">▲</span><span class="positionA fontSize12" style="top: -3px;">▼</span></div>法定代表人</td>
	                <td width="200"><div class="positionR"><span class="sort positionA fontSize12" style="top: -15px;">▲</span><span class="positionA fontSize12" style="top: -3px;">▼</span></div>注册地</td>

	            </tr>
            <#list defaultIndexItemCustomList as item>
            	<tr>
            		<td width="100"><input type="checkbox" name ="check" value="${item.defaultItemId}"/></td>
	            	<td>${(1 + item_index) + (page.getPageSize() * page.getCurrentPage())}</td>
            		<td class="redFlagCredit"><#if item.codeCredit??>${item.codeCredit}<#else>${item.codeOrg}</#if></td>
            		<td> 
            			<a class="${item.defaultItemId}" onmouseover="showIdent(${item.defaultItemId})"  onmouseout="layer.closeAll('tips');"> ${item.qymc}</a>
            		
            		</td>
            		<td>${item.lxdh}</td>
            		<td>${item.fddbr}</td>
            		<td>${item.qyzs}</td>
            	</tr>
            </#list>
        </table>
        </form>
        <!-- flip-page-->

            <#if (defaultIndexItemCustomList?? && defaultIndexItemCustomList?size > 0)>
				<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; margin-top: 0px;" cellpadding="0" cellspacing="0" >
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>
				</#if>
				
        <div class="queryBtn" style="clear:both;">
        	 <button type="button" class="sureBtn" onclick="CheckedRev2();">清空</button>
            <button type="button" class="sureBtn" onclick="window.location.href='${request.getContextPath()}/admin/companyListFilter/list.jhtml'">关闭</button>
            <button type="button" class="sureBtn" id="downLoadExcel">导出EXCEL文件</button>
            <button type="button" class="sureBtn" onclick="formtab()">企业标识</button>
           
        </div>
    </div>
    
     <script>
     
     	function formtab(){
			//回显，加载 
			var chox=$.cookie("chox");
			var allche=$.cookie("allche");   
			var noche=$.cookie("noche");
			var check='';
			var allc='';
			if(chox == "" && allche == "" && noche == "" ){
				layer.alert("请至少勾选一个企业",{icon:2,shade:0.3,shouldClose:true});
				return false;
			}
			if(chox != "" && allche == ""){  
				 check=chox;
				 allc= "";
			}else if(allche == "1"){ 
				 allc=1;
				 check=noche;
			}   
			$("#checkeaaa").val(check);
			$("#checkeaaaid").val(allc);
			$('#postid').submit();
		}
    	//全选 
		function CheckedRev(){ 
				$(':checkbox').prop("checked", true);
				$.cookie("allche","1", { expires: 1 }); 
				$.cookie("chox","", { expires: 1 });  //清空数据 
				$.cookie("noche","", { expires: 1 });  //清空数据 
		}
		
		//清空选择
		function CheckedRev2(){
			$(':checkbox').prop("checked", false);
			 $.cookie("allche","", { expires: 1 });  //清空选择状态
			 $.cookie("chox","", { expires: 1 });  //清空数据
			 $.cookie("noche","", { expires: 1 });  //清空数据 
		}
		
		//鼠标移入悬浮事见
		function showIdent(id){ 
				$.post("${request.getContextPath()}/admin/demand/getIden.jhtml",{defaultId:id},function(data1){
						if(data1==""){
							data1="暂无数据"
						}
						layer.tips(data1,"."+id,{tips:[2, '#f00']},{time: 0});
				})
			}
		 </script>
</body>
</html>