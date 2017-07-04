<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<style>
			/*li无样式*/
			li{
				list-sytle:none;
			}
			table ul li{
				float: left;
			}
		</style>
		<script type="text/javascript" >
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
					layer.msg(msg, {
						icon: (msg=="操作成功")?1:2,
						shade: 0.3,
						shadeClose: true
					});
				}
				//默认第一个指标大类背景色颜色为灰
				$("#indexTbsListUl li a:eq(0)").css("background-color","#dadada");
			});
//			var idArray = new Array();
//			var index=0;
			//单击指标大类显示指标项
			function indexPearent(indexId,obj){
				var obj=$(obj);
				//变色
				obj.parent().siblings().find("a").css("background-color","white");
				obj.css("background-color","#dadada");
				//点击过 就不执行ajax
				var isfalse=false;
//				for(var i =0; i<idArray.length; i++){
//					if(idArray[i]==indexId){
//						isfalse=true;
//					}
//				}
//				idArray[index]=indexId;
//				index++;
				if(isfalse==false){
					var url = "${request.getContextPath()}/admin/indexTb/getIndexItemTbListJson.jhtml";
					$.post(url,{indexId:indexId},function(result){
							if(result!=null){
								var list = result.indexItemTbList;	
								var html="";
								for(var i = 0; i < list.length; i++){
									//指标项
									html += '<li>'
											 +'<a href="javascript:void(0);" name="isfalse" onclick='+'indexSon('+'"'+list[i].indexItemCode+'"'+','+'"'+list[i].indexItemName+'"'+',this)'+'>'+list[i].indexItemName+''
											 +'<span>&nbsp;&nbsp;</span>'
											+'</li>'
								}
								$("#indexItemTbUl").html(html);
							}
						
					});
				}
			}
			function indexSon(indexItemCode,indexItemName,obj){
				var obj=$(obj);
				var name= obj.attr("name");
				if(name=="isfalse"){
					var html="";
					html += '<li>'
								 +'<input type="checkbox" name="indexItemCodes" checked="checked" value='+indexItemCode+'>'+indexItemName+''
								 +'<span>&nbsp;&nbsp;</span>'
							+'</li>'
					$("#myIndexItemTbListUl").append(html);
				}
				if(name=="isfalse"){
					obj.attr("name","istrue");
				}
			}
			//保存
			function save(){
				var name = $("#name").val();
				if($.trim(name)==""){
					alert("请填写EXCEL模板名称");
				}else{
					$("#reportExcelTemplateForm").submit();
				}
			}
			//显示上级区域弹出框
			function openPop(){
				$("#covered").show();
				$("#poplayer").show();
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
			//确认选择
			function confirmSel(){
				var seleced = $(".seleced");
				if(seleced.length==0){
					alert("您还没有选择上级区域");
				}else{
					closePop();
					var area = $(seleced.find("label"));
					$("#openPop").text(area.text());
					$("#parent").val(seleced.attr("id"));
					$("input[name='reportExcelTemplateAreaName']").val(area.text());
					$("input[name='reportExcelTemplateAreaId']").val(seleced.attr("id"));
				}
			}
			function openArea(obj, id){
				obj = $(obj);
				//子区域的缩进
				var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 32 + "px";
				if(obj.attr("id")==0){ //未展开
					//设置为展开状态
					obj.attr("id",1);
					//将图标设置为展开图标
					$(obj.find("img")[0]).css("right",5);
					
					//获取父区域的tr
					var ptr = obj.parent().parent();
					var url = "${request.getContextPath()}/admin/area/getArea.jhtml";
					$.get(url,{_:Math.random(),id:id},function(result){
						if(result!=null){
							var subs = result.subArea;
							for(var i = 0; i < subs.length; i++){
								//子地区
								var sub = subs[i];
								//展开图标
								var icon = "";
								if(sub.subArea!=null && sub.subArea.length!=0){
									icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.id+')">'
										   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
										   +'</div>'
								}
								
								
								var tr = $("<tr name='"+sub.id+"' class='"+id+"'></tr>");
								var name = "<td id='"+sub.id+"' onclick='selUpstream(this)' style='padding-left:"+spacing+"'>"
											+icon
											+"<label>"+sub.name+"</label>"
											+"</td>";
								
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
		</script>
		<title>excel模板编辑</title>
	<style>
		.sureBtn {
  				background-color: rgb(56,165,226);
  				color: white;
  				border-radius: 4px;		
			}
			.warmFont {
  				color: #909090;
			}
			* {
  margin: 0px;
  padding: 0px;
  font-family: "微软雅黑";
}
.mainOrange{
	color:rgb(245,112,65);
}
.cancleBtn {
  background-color: #a4a4a4;
  color: white;
  border-radius: 4px;
}
.titleFont1 {
  padding-left: 10px;
  height: 30px;
  line-height: 30px;
  font-size: 14px;
  text-align: left;
  color: #969696;
  border: 1px solid #dadada;
  border-bottom: none;
}

.inlineBlock{
	display: inline-block;
	padding: 10px;
}


.chooseCustomProductQueryBtnBox{
	margin: 10px 30px;
	
}
 .chooseCustomProductQueryBtnBox input {
  display: inline-block;
  width: 120px;
  height: 30px;
  line-height: 30px;
  margin: 0px 20px;
  outline: none;
  border: none ;
  cursor: pointer;
}
.marginB20{
	margin-bottom: 20px;
}
.changeFont{
color: #14a8f4;
  text-decoration: underline;
  cursor: pointer;
  margin-right: 10px;
}

.chooseCustomProductQueryBox {
  margin: 10px 30px 30px;
  text-align: center;
  border: 1px solid #dadada;
  font-size: 12px;
}
.chooseCustomProductQueryBox{border:none;}
.chooseCustomProductQueryBox table {
  margin: 30px auto;
  border-top: 1px solid #dadada;
  border-left: 1px solid #dadada;
}
.chooseCustomProductQueryBox table tbody {
  padding: 20px 30px;
}
.chooseCustomProductQueryBox table tr td {
  padding: 6px 8px;
  border-right: 1px solid #dadada;
  border-bottom: 1px solid #dadada;
  font-size: 12px;
}
.chooseCustomProductQueryBox table tr .chooseCustomProductQueryTermsTD1st {
  text-align: right;
}
.chooseCustomProductQueryBox table tr .chooseCustomProductQueryTermsTD2nd {
  text-align: left;
}
.chooseCustomProductQueryBox .chooseCustomProductQueryBtnBox {
  margin: 0px auto 20px;
  width: 100%;
  height: 50px;
  text-align: center;
}
.delFont{
	color: red;
	text-decoration: underline;
}
.clearMRBorder{
	margin-right: 0px;
	font-size: 12px;
	border: none;
	background: none;
}
.marginT10{
	margin-top: 10px;
}

#poplayer{
	width: 270px;
	height: 370px;
}
#poplayer .borderBox{
	border: 1px solid #dadada;
	width: 100%;
	height: 100%;
}
#poplayer .borderBox .titleFont1{
	border: none;
	border-bottom: 1px solid #dadada;
}
#poplayer .borderBox .listBox{
	height:255px;
	padding: 10px;
	overflow: auto;
}
#poplayer .borderBox .listBox table{
	font-size: 12px;
}
#poplayer .borderBox .btnBox{
	border-top:1px solid #dadada;
	padding:10px;
	text-align:center;
}
#poplayer .borderBox .btnBox .cancleBtn,#poplayer .borderBox .btnBox .sureBtn{
	width: 70px;
	height:30px;
	border: none;
	outline: none;
}
#poplayer .borderBox .btnBox .cancleBtn{
	margin-right:40px;
}
.changeFontCursor,#indexItemTbUl a{
	color:black;
	font-size:12px;
	padding:4px 6px;
}
.indexItemTbUl li{
	margin:5px;
}
/*悬浮变色*/
#indexItemTbUl a:hover,.changeFontCursor:hover{
	color: rgb(56,165,226);
}

.inputSty{
	padding:4px 8px;
	font-size:12px;
	border:1px solid #dadada;
	height:auto;
}
.inputStatus{
	vertical-align: middle;
	font-size:12px;
	padding:0px 5px;
	display:inline-block;
}
		</style>
	</head>
	<body>
		<div id="covered"></div>  
	    <div id="poplayer">  
	        <div  class="borderBox">
	        	<div class="titleFont1">地区列表</div>
	        	<div class="listBox">
	        		<table cellpadding="0" cellspacing="0">
						<#list areas as a>
							<tr>
								<td  id="${a.id}" onclick="selUpstream(this)">
									<#if (a.subArea?? && a.subArea?size > 0) >
										<div id="0" class="open-shrink" onclick="openArea(this, ${a.id})">
											<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
										</div>
									</#if>
									<label>${a.name}</label>
								</td>
							</tr>
						</#list>
					</table>
	        	</div>
	        	<div class="btnBox">
	        		<input type="button" value="取 消"  class="cancleBtn" onclick="closePop()"/>
	        		<input type="button" value="确 认"  class="sureBtn" onclick="confirmSel()"/>
	        	</div>
	        </div>  
	    </div>
		<#-- 新增框 -->
		<div class="chooseCustomProductQueryBox">
			
			<form id="reportExcelTemplateForm" action="${request.getContextPath()}/admin/reportExcelTemplate/editSubmit.jhtml" method="post">
				<input name="id" type="hidden" value="${excelTemplate.reportExcelTemplateId}"/>
				<table cellpadding="0" cellspacing="0">
				<#if reportExcelTemplate == null>
				<caption class="titleFont1">增加模板</caption>
			<#else>
				<caption class="titleFont1">修改模板</caption>
			</#if>
					<tr>
						<td style="width:35%;" class="chooseCustomProductQueryTermsTD1st"><label class="mainOrange"> * </label>EXCEL模板名称</td>
						<td style="width:65%;" class="chooseCustomProductQueryTermsTD2nd">
							<input  id="name" name="reportExcelTemplateName" class="inputSty allnameVal" value="${reportExcelTemplate.reportExcelTemplateName}"/>
							<input name="reportExcelTemplateId" type="hidden" value="${reportExcelTemplate.reportExcelTemplateId}"/>
						</td>
					</tr>
					
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st"><label class="mainOrange"> * </label>状态</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<#if reportExcelTemplate.status=1>
								<input class="inputStatus" name="status" value="0" type="radio" /> 正常
								<input class="inputStatus" name="status" value="1" type="radio" checked=true/> 禁用
							<#else>
								<input class="inputStatus" name="status" value="0" type="radio" checked=true/> 正常
								<input class="inputStatus" name="status" value="1" type="radio" /> 禁用
							</#if>
						</td>
					</tr>
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st">覆盖区域</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<input style="font-size:12px;" readonly="readonly" name="reportExcelTemplateAreaName" value="${reportExcelTemplate.reportExcelTemplateAreaName}" />
							<a id="openPop" href="javascript:void(0);" style="color:rgb(56,165,226);padding:5px;font-size:12px" onclick="openPop()"> 选择区域</a>
							<input type="hidden" name="reportExcelTemplateAreaId" value="${reportExcelTemplate.reportExcelTemplateAreaId}" />
						</td>
					</tr>
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st">选择指标大类</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<ul id="indexTbsListUl" class="indexItemTbUl">
								<#list indexTbsList as item>
									<li>
										<a href="javascript:void(0);" name="isfalse" class="changeFontCursor" onclick="indexPearent(${item.indexId},this)">${item.indexName}</a>
										<span>&nbsp;&nbsp;</span>
									</li>
								</#list>
							</ul>
						</td>
					</tr>
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st">选择指标项</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<ul id="indexItemTbUl" class="indexItemTbUl">
								<#list indexItemTbList as item>
									<li>
										<a href="javascript:void(0);" name="isfalse" onclick="indexSon('${item.indexItemCode}','${item.indexItemName}',this)">${item.indexItemName}</a>
										<span>&nbsp;&nbsp;</span>
									</li>
								</#list>
							</ul>
						</td>
					</tr>
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st">已选指标项</td>
						<td class="chooseCustomProductQueryTermsTD2nd">
							<ul id="myIndexItemTbListUl">
								<#if myIndexItemTbList ?? !>
									<#list myIndexItemTbList as item>
										<li>
											<input class="inputStatus" type="checkbox" name="indexItemCodes" checked="checked"value="${item.indexItemCode}"/><span style="font-size:12px;" >${item.indexItemName}</span>
											<span>&nbsp;&nbsp;</span>
										</li>
									</#list>
								</#if>
							</ul>
						</td>
					</tr>
				</table>
				<div class="chooseCustomProductQueryBtnBox">
					<input type="button" class="cancleBtn closeThisLayer"  value="取 消" />
					<input type="button" onclick="save()" class="sureBtn" value="确 认" />
				</div>
			</form>
		</div>
	</body>
	<script>
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
	$('.closeThisLayer').on('click', function(){
    	parent.layer.close(index); //执行关闭
	});
	</script>
</html>
