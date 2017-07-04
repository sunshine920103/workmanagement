<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<meta charset="UTF-8"/>
		<title>新增指标分类列表</title>
	</head>
	<body>
	
 <div class="showListBox">
	<form id="form" method="post" >
		<input type="hidden" value="" name="indexstring"  />
		<input type="hidden" name="indexId" value="${indexTb.indexId}"/>
		<input id="newIndexId" type="hidden" name="newIndexId" value="${newIndexId}"/>
		<div >
			<table class="centerTable" cellpadding="0" cellspacing="0">
				<caption class="titleFont1 titleFont1Ex">转为省级指标</caption>
				<tr>
					<td style="width:35%;" class="noBorderL firstTD">原指标名</td>
					<td style="width:65%;" class="secondTD">新指标名 </td>
				</tr>
				<tr>
					<td  class="noBorderL firstTD">
						<input readonly="readonly" type="text" value="${indexTb.indexName}" onblur="onblurVal(this,13,50)" />
					</td>
					<td   class="secondTD">
						<input id="newIndexNameId" name="newIndexName" class="inputSty allnameVal" type="text" value="${indexTb.indexName}" onblur="onblurVal(this,13,50)" title="必填，不能超过50个字"/>
					</td>
				</tr>
				<tr>
					<td  class="noBorderL firstTD" colspan="2"> </td>
				</tr>
				<tr>
					<td style="width:35%;" class="noBorderL firstTD">原指标项名</td>
					<td style="width:65%;" class="secondTD">新指标项名 </td>
				</tr>
				<#list itemList as list>
					<tr>
						<td   class="noBorderL firstTD">
							<label for="tbWeb0"><input onclick="test(this)" type="checkbox" value="${list.indexItemId}" name="indexItemId" id="tbWeb0" />${list.indexItemName}</label>
						</td>
						<td  class="secondTD">
							
							<input name="newIndexItemName" class="inputSty allnameVal" type="text" value="${list.indexItemName}" onblur="onblurVal(this,13,50)" />
						</td>
					</tr>		
				</#list>
			</table>
			<div class="showBtnBox">
				<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
				<input id="submitBtn" type="button" class="sureBtn" value="确 认" />
			</div>
			
		</div>
	</form>
	</div>
	<script>
	 
		function test(obj){
			var  str1=$(obj).parent().parent().siblings().children("input[name='newIndexItemName']").val;
			var str2=$("input[name='indexstring']").val();
			$("input[name='indexstring']").val(str1+str2);
		}
	
	$(function(){                    
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
	    	parent.layer.close(index); //执行关闭
		});	
		
		$("#submitBtn").click(function(){
			var str="";
			var num="";
			for(i=0;i<$("input[name='indexItemId']").length;i++){
				if($("input[name='indexItemId']").eq(i).is(':checked')){
					 num=$("input[name='indexItemId']").eq(i).parent().parent().siblings().children("input[name='newIndexItemName']").val()
					str+=num+','
				}
				
			} 
			$("input[name='indexstring']").val(str);
				
			var loading = layer.load();
				$.post("${request.getContextPath()}/admin/indexTb/saveCopyl.jhtml",$("#form").serialize(),function(data){	
					if(data.flag){
							var index = layer.confirm(data.message,{btn: ['确定','取消']},function(){
							var a =data.data.newIndexId+'';
							var oldItemIds =data.data.oldItemIds+'';
							$.post("${request.getContextPath()}/admin/indexTb/insertToNewTable.jhtml",{oldItemIdss:oldItemIds,newIndexId:a,oldIndexId:${indexTb.indexId}},function(data){
								layer.alert(data.message,{
									icon: 1,
									shade:0.3,
									shadeClose:true
								},function(){
									var index2 = parent.layer.getFrameIndex(window.name); //获取当前窗体索引								
							    	parent.layer.close(index2); //执行关闭									
								});
							});
						},function(){
							var index1 = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
							parent.layer.close(index1);
							layer.close(loading);
						});						
					}else{
						alert(data.message);
						layer.close(loading);
					}
				});
		});	
	})	
	</script>
</body>
</html>