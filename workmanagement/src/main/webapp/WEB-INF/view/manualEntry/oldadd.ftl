<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		var loading;
		//回显
		var msg = "${msg}"; 
		if(msg!=""){
//			layer.alert(msg,{
//				icon: (msg=="操作成功")?1:2,
//				time:15000,
//				shade:0.3,
//				shadeClose:true
//			});
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
			
			layer.close(loading);
		}
		
		//时间控件
		$("#timeControler").click(function(){
			setLayDate("#timeControler");
		})
		
		
		//点击后去除提示信息
//		$("input[name='defaultIndexItemCode']").click(function(){
//			if($(this).val()=="输入正确后，方可搜索填充其他信息"){
//				$(this).val("");
//			}
//		});
		//验证信用码和社会码
		$("#search").click(function(){
			var defaultIndexItemCode= $("input[name='defaultIndexItemCode']").val();
			defaultIndexItemCode=$.trim(defaultIndexItemCode);
			var indexId=$("select").val();
			var timeReport=$("input[name='timeReport']").val();
//			var myDate =getNowFormatDate();
			if(checkSureTY(defaultIndexItemCode) == 2 || checkSureZZ(defaultIndexItemCode) == 2){}
			else{
				alertInfo("请输入准确的统一社会信用代码或组织机构代码");
				return false;
			}
			if(timeReport==""){
				timeReport=getNowDate();
				$("input[name='timeReport']").val(timeReport);
			}
			
				var url = "${request.getContextPath()}/admin/manualEntry/getIndexLastValueJson.jhtml";
				$.post(url,{indexId:indexId,defaultIndexItemCode:defaultIndexItemCode,timeReport:timeReport},function(result){
						if(result.valueMaxList!=null){
							var valueMaxList = result.valueMaxList;
							var indexItemTbList = result.indexItemTbList;
							var defaultIndexItem = result.defaultIndexItem;
							//为空则赋为""
							if(defaultIndexItem.codeOrg==null){
								defaultIndexItem.codeOrg="";
							}
							if(defaultIndexItem.codeCredit==null){
								defaultIndexItem.codeCredit="";
							}
							var html="";
							html+="<tr>"
									+"<td style='width:200px;'>操作</td>"
									+"<td style='width:200px;'>统一社会信用代码</td>"
									+"<td style='width:200px;'>组织机构代码</td>"
							for(var i = 0; i < indexItemTbList.length; i++){
								html+="<td style='width:200px;'>"+indexItemTbList[i].indexItemName+"</td>";
							}
							html+="</tr>"
							for(var i = 0; i < valueMaxList.length; i++){
								var sonValueList=valueMaxList[i];
								html+="<tr>"
										+"<td><a href='${request.getContextPath()}/admin/manualEntry/update.jhtml?indexId="+indexId+"&majorId="+sonValueList[sonValueList.length-1]+"&defaultIndexItemId="+defaultIndexItem.defaultIndexItemId+"'class='changeFont fontSize12 cursorPointer hasUnderline' style='text-decoration:underline'>修改</a></td>"
										+"<td>"+defaultIndexItem.codeCredit+"</td>"
										+"<td>"+defaultIndexItem.codeOrg+"</td>"
								for(var j = 0; j < sonValueList.length-1; j++){
									if(sonValueList[j]==null||sonValueList[j]=="null"){
										sonValueList[j]="";
									}
									html+="<td class='secondTD'>"+sonValueList[j]+"</td>"
								}
								html+="</tr>"
							}
							$("#valueMaxListTable").html(html);
							//显示历史修改
							$("#updateBtuDiv").html("<a href='${request.getContextPath()}/admin/manualEntry/changeHistory.jhtml?defaultIndexItemId="+defaultIndexItem.defaultIndexItemId+"' class='sureBtn sureBtnEx inlineBlock textCenter marginT20'>历史修改记录</a>");
						}else{
							//回显提示信息
							var msg= result.msg;
//							layer.alert(msg, {
//								icon: 2,
//								shade: 0.3,
//								shadeClose: true
//							});
							alertInfo(msg);
						}
				});
			
		});
		$("select").click(function(){
			//删除填充内容
			$("#indexLastValueTable").html("");
			var indexId= $(this).val();
			//为模板名称赋值
			$("[name='indexId']").val(indexId);
//			var url = "${request.getContextPath()}/admin/indexTb/getIndexItemTbListJson.jhtml";
//			$.post(url,{indexId:indexId},function(result){
//					if(result!=null){
//						var list = result.indexItemTbList;	
//						var html="";
//						for(var i = 0; i < list.length; i++){
//							html += '<tr>'
//										+'<td class="chooseCustomProductQueryTermsTD1st">'+ list[i].indexItemName+'</td>'
//										+'<td class="chooseCustomProductQueryTermsTD2nd"><input type="text"></td>'
//									+'</tr>'
//						}
//						$("#indexLastValueTable").html(html);
//					}
//			});
		});
		//为模板名称赋初始值
		var myIndexId= "${indexId}";
		if(myIndexId==""){
			$("[name='indexId']").val("${indexTbs[0].indexId}");
		}else{
			$("[name='indexId']").val("${indexId}");
		}
		//隐藏修改历史
		$("#changeHistoryDiv").hide();
		
	});
	//保存
	function save(){
		var operateAuthFile=$("[name='file']").val()
		var reg=/(.*).(jpg|bmp|gif|pdf|jpeg|png)$/; 
		if($("[name='indexItemCode']").length<=2){
			
			layer.alert("缺少填充数据，请搜索", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
			});
		}else if(operateAuthFile==""){
			
			layer.alert("请上传授权文件", {
						icon: 2,
						shade: 0.3,
						shadeClose: true
			});
		}else if(!reg.test(operateAuthFile)) {
			layer.alert("请上传'.pdf'、'.jpg'、'.jpeg'、'.gif'、'.png'或'.bmp'格式文件文件",{icon:2,shade:0.3,shouldClose:true});
		}else{
			loading = layer.load();
			$("form").submit();
		}
	}
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
<title>手工录入添加</title>
	</head>
	<body>
		<div div class="showListBox" style="border: 0px;">
		<form  method="post" enctype="multipart/form-data" method="post" action="${request.contextPath}/admin/manualEntry/save.jhtml">
			<table width="70%" cellpadding="0" cellspacing="0" class="marginB20">
				<caption class="titleFont1 titleFont1Ex">手工修改</caption>
				<tr>
					<td width="300" class="noBorderL firstTD"><label class="mainOrange"> * </label>截止归档时间</td>
					<td width="400" class="secondTD"><input name="timeReport" id="timeControler" autocomplete="off" class="laydate-icon inputSty fontSize12"></td>
				</tr>
				<tr>
					<td class="noBorderL firstTD">指标大类<input type="hidden" name="indexId"/></td>
					<td class="secondTD">
						<select  class="inputSty shouldHide">
							<#list indexTbs as item>
								<#if item.indexId==indexId>
									<option value="${item.indexId}" selected="selected">${item.indexName}</option>
								<#else>
									<option value="${item.indexId}">${item.indexName}</option>
								</#if>
							</#list>
						</select>
					</td>
				</tr>
				<tr>
					<td class="noBorderL firstTD"><label class="mainOrange"> * </label>统一社会信用代码/组织机构代码/身份证号</td>
					<td class="secondTD"><input type="text" class="inputSty" name="defaultIndexItemCode"></td>
				</tr>
			</table>
			<input type="button" id="search" class="sureBtn sureBtnEx marginR10" value="搜索填充">
			<span class="warmFont">注：搜索填充的数据为截止归档时间的最新一条记录</span>
			<!--<table cellpadding="0" cellspacing="0">
					<tr>
						<td width="200" class="chooseCustomProductQueryTermsTD1st">统一社会信用码</td>
						<td width="500" class="chooseCustomProductQueryTermsTD2nd"><input class="inputSty" name="indexItemCodes"></td>
					</tr>
					<tr>
						<td class="chooseCustomProductQueryTermsTD1st">组织机构代码</td>
						<td class="chooseCustomProductQueryTermsTD2nd"><input class="inputSty" name="indexItemCodes"></td>
					</tr>
			</table>-->
			<div id="updateBtuDiv">
				
			</div>
			<table id="valueMaxListTable" cellpadding="0" cellspacing="0" style="overflow: hidden; table-layout:fixed;">
				
			</table>
			<table width="70%" id="indexLastValueTable" cellpadding="0" cellspacing="0">
				<#if myIndexItemTbList?? !>
					<a href='${request.getContextPath()}/admin/manualEntry/changeHistory.jhtml?defaultIndexItemId=${defaultIndexItemId}' style='text-decoration:underline'>历史修改记录</a>
					<tr>
						<td  width='300' class='noBorderL firstTD'>统一社会信用代码</td>
						<td  width='400' class="secondTD"><input type="text" readonly="readonly" value="${myValueList[0]}" /><input type="hidden" name="defaultIndexItemId" value="${defaultIndexItemId}"></td>
					</tr>
					<tr>
						<td class='noBorderL firstTD'>组织机构代码</td>
						<td class="secondTD"><input type="text" readonly="readonly" value="${myValueList[1]}" /></td>
					</tr>
					<#list myIndexItemTbList as item>
						<tr>
							<td class='noBorderL firstTD'>${item.indexItemName}</td>
							<td  class="secondTD">
								<#if item.indexItemType==1>
									<input  autocomplete="off" class="laydate-icon inputSty fontSize12" type="text" name="indexItemCode" value="${myValueList[item_index +2]}" onclick="laydate({istime: false,max: laydate.now(),format: 'YYYY-MM-DD'})" /><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
									<input autocomplete="off" class="laydate-icon inputSty fontSize12" type="text" name="indexItemCode" value="${myValueList[item_index +2]}" onclick="laydate({istime: false,max: laydate.now(),format: 'YYYY-MM-DD'})" /><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
								<#else>
									<input class="inputSty" type="text" name="indexItemCode" value="${myValueList[item_index+2]}" /><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${errorMsgList[item_index]}</span>
								</#if>
							</td>
						</tr>
					</#list>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>授权文件</td>
						<td class="secondTD"><input name="file" class="inputSty inlineBlock" type="file" value="浏览"><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${fileError}</span></td>
					</tr>
				</#if>
			</table>
			<div class="showBtnBox">
				<input type="button" class="cancleBtn closeThisLayer" value="关 闭" />
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