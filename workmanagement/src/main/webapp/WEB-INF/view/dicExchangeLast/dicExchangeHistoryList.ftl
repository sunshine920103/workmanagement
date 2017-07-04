<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		
		<script type="text/javascript"  >
			//回显
			var msg = "${msg}";
			if(msg != "") {
				layer.alert(msg,{
					icon: (msg=="操作成功")?1:2,
					shade:0.3,
					shadeClose:true
				});
			}
			
			function testDic(obj){
				var val = $(obj).val();
				var reg = /^\d+(\.\d+)?$/;
				if(reg.test(val)==false){
					if($(obj).nextAll(".redWarm3").length==0){
						var str = "";
						str = "<span class='redWarm3'>请输入正确的汇率格式</span>";
						$(obj).after(str);
					}
					$(obj).focus();
					return false;
				}else{
					$(obj).nextAll(".redWarm3").remove();
				}
			}
			
			
			
			
		</script>
		<title>汇率历史</title>
	</head>
	<body>
	
		<form id="searchForm" method="post">
			
		</form>
		<div class="showListBox listBox fontSize12">
			<div>
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">查看历史汇率</caption>
					<input type="hidden" value="${dicAreaId}"  id="dicAreaId">
					<tbody>
						<tr class="firstTRFont">
							<td style="width:50%;">币种</td>
							<td style="width:50%;">${dicExchangeHistoryList[0].dicExchangeName}</td>
						</tr>
						<tr class="firstTRFont">
							<td width="350">代码</td>
							<td width="350">${dicExchangeHistoryList[0].dicExchangeCode}</td>
						</tr>
						</tbody>
					
				</table>
				<div class=" marginT30">
					<input type="submit" class="sureBtn sureBtnEx marginT20 marginB20"  name="${dicExchangeHistoryList[0].dicExchangeName}"  onclick="testTime(this)" value="筛选" style="margin-left:20px;float:left;"/>
					<input class="laydate-icon inputSty fontSize12  marginT20 marginB20" autocomplete="off" id="dicExchangeHistoryTime" style="width: 120px;margin-right:80px;float:left" onclick="laydate({istime: false, format: 'YYYY-MM-DD',max: laydate.now()})" name="dicExchangeHistoryTime" value="" >			
				</div>
				<table cellpadding="0" cellspacing="0" id="con" style="margin-top: 20px;">
						<tbody>
						<tr>
							<td width="300">时间</td>
							<td width="400">汇率</td>
							<td width="100">操作</td>
						</tr>
						<#list dicExchangeHistoryList as item>
							<tr>
								<td>${item.dicExchangeTime?substring(0,10)} </td>
								<td><span class="Mod_old fontSize12">${item.dicExchangeValue}</span> <input type="text"  name="" style="width: 150px;height: 18px;" class="new hide inputSty" value="${item.dicExchangeValue}" id="${item.dicExchangeId}"  onblur="testDic(this)" /></td>
								
							
								<td><a class="changeFont fontSize12 cursorPointer hasUnderline update" 
									 onclick="openPP(this)"
									 >修 改</a> 
									 <a class="delFont fontSize12 cursorPointer hasUnderline hide" onclick="saveDiv(this)">保 存</a></td>
							</tr>
						</#list>
					</tbody>
					
				</table>
			
				<div class="showBtnBox">
                	<input class="cancleBtn closeThisLayer" type="button" value="关 闭">
            </div>
			</div>
		</div>
	
	</body>
	<script  type="text/javascript">
		function testTime(obj){
			var name=obj.name;
			var time=$("#dicExchangeHistoryTime").val();
			var dicAreaId=$("#dicAreaId").val();
	    		var con="<tbody><tr><td width='450'>时间</td><td width='250'>汇率</td><td width='100'>操作</td></tr>";
				 $.ajax({
	    			url:'${request.getContextPath()}/admin/dicExchangeLast/dicExchangeHistoryListsosuo.jhtml',
	    			type:"POST",
	    	 		data:{"name":name,"time":time,"dicAreaId":dicAreaId},
	    	 		success:function(result){
		    	 		str_json=eval(result);
		    	 		if(result){
			    	 		$.each(str_json,function(m,item){
			    	 			con += '<tr><td width="300">' +item.dicExchangeTime.substring(0,10)+ '</td><td width="400"><span class="Mod_old fontSize12">' +item.dicExchangeValue+ '</span>  <input type="text"  name="" style="width: 150px;height: 18px;" class="new hide inputSty" value="'+ item.dicExchangeValue +'"id= "'+item.dicExchangeId +'" onblur="testDic(this)" /></td><td width="100"><a class="changeFont fontSize12 cursorPointer hasUnderline update"  onclick="openPP(this)">修 改</a><a class="changeFont fontSize12 cursorPointer hasUnderline hide" onclick="saveDiv(this)">保 存</a> </td></tr>';
							});							
		    	 		}else{
		    	 	//	con += '<tr><td width="400"><span class="Mod_old fontSize12"></span>  <input type="text"  name="" style="width: 150px;height: 18px;" class="new hide inputSty" value=""id= " " onblur="testDic(this)" /></td><td width="300">' +time+ '</td><td width="100"><a class="changeFont fontSize12 cursorPointer hasUnderline update"  onclick="openPP(this)"></a></td></tr>';
		    	 		//	layer.alert("请勾选存在异议的指标项再进行推送任务",{
						//		icon: 2,
						//		shade:0.3,
						//		shadeClose:true
						//	});
		    	 		}
						con +="</tbody>";
						$("#con tbody").remove();
						$("#con").html(con);
	    	 		}
	    	 	});
		}
		function saveDiv(obj){
				var val=$(obj).parent().prev().children(".new").val();
				var id=$(obj).parent().prev().children(".new").attr("id");
				 
				var o = $(obj).parent().prev().children(".new");
				var reg = /^\d+(\.\d+)?$/;
				if(reg.test(val)==false){
					if($(o).nextAll(".redWarm3").length==0){
						var str = "";
						str = "<span class='redWarm3'>请输入正确的汇率格式</span>";
						$(o).after(str);
					}
					$(o).focus();
					layer.alert("保存失败",{icon:2,shade:0.3,shouldClose:true});
					return false;
				}else{
					$(o).nextAll(".redWarm3").remove();
					 $.ajax({
		    			 url:'${request.getContextPath()}/admin/dicExchangeLast/submit.jhtml?',
		    			 type:"POST",
		    	 		data:{"val":val,"id":id},
		    	 		success:function(result){
							layer.alert("保存成功",{icon:1,shade:0.3,shouldClose:true});
		    	 		}
		    	 	});
					$(obj).hide().siblings(".update").show();
				}

	    	 	$(obj).parent().prev().children('.new').hide();
	    	 	$(obj).parent().prev().children('.Mod_old').text(val).show();				
		}
		
				
		function openPP(obj){
		   var obj = $(obj);
		   if($(".new:visible").size()>0){
			   	layer.alert("请先保存汇率",{icon:2,shade:0.3,shouldClose:true});
			   	return false;
		   }else{
		   		obj.hide().siblings(".hide").show();
			   $(".new").hide().siblings(".Mod_old").show();	
			   obj.parent().prev().children(".Mod_old").hide();
			   obj.parent().prev().children(".new").show();
		   }  
		}
			
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引		
		$('.closeThisLayer').on('click', function(){
			parent.window.location.href="${request.getContextPath()}/admin/dicExchangeLast/list.jhtml";
	    	parent.layer.close(index); //执行关闭
		});
	
	</script>
</html>
