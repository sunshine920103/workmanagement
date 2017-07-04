<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>		
		<script type="text/javascript" >
			
			//删除政府类型
			function del(obj, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/goverType/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭弹窗
							layer.close(option_index);
							if(data.flag){
								//删除页面上的数据
								$(obj).parent().parent().remove();
							}
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
				return false;
			}	
			//全部导出
			function down(){
				window.open('${request.getContextPath()}/admin/goverType/exportAll.jhtml?name=${sysGovTypeName}');
			}
		</script>
		<title>政府部门类型</title>
	</head>
	<body class="eachInformationSearch">
		<form id="searchForm" method="post"></form>
		<div class="queryInputBox">
			<div class="verticalMiddle">
				<input 
				onclick="setLayer('新增类别','${request.getContextPath()}/admin/goverType/add.jhtml');$('.layui-layer-shade').height($(window).height());"
				type="button" value="新增类别" class="sureBtn" style="margin-left:30px"/>
				<form id="form" action="${request.getContextPath()}/admin/goverType/list.jhtml" method="post" style="display:inline-block;*zoom=1;*display:inline;position: relative;margin-left:20px">
				<#-- 当前分组方式, 只用来回显 -->
				<input type="hidden" name="methodTextVal" id="methodTextVal" value="${methodTextVal}"/>
				<input id="sysGovTypeName" name="sysGovTypeName" class="inputSty inputOtherCondition" value="" />
				<span class="fuck">请输入政府类型名称查询</span>
				<input type="submit" class="sureBtn" value="查  询" style="margin-left:0px"/>
			 </form>
			 <input onclick="down()" type="button" class="sureBtn" value="全部导出" />	
				
				
			</div>
			 <div class="verticalMiddle marginL30 marginT10">
			       <span class="warmFont inlineBlock fontSize12">注：按照实际的政府类别名称和代码进行管理操作；无法删除被使用的政府类别。</span>
			 </div>
			
			<div class="listBox">
				<div>
				    
				</div>
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">政府类别列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="150">政府类别名称</td>
							<td width="150">政府类别代码</td>
							<td width="300">备注</td>
							<td width="100">操作</td>
						</tr>
						<#list its as it>
							<tr>
								<td>${it.sysGovTypeName}</td>
								<td>${it.sysGovTypeCode}</td>
								<td>${it.sysGovTypeNotes}</td>
								<td>
									<a class="changeFont fontSize12 cursorPointer hasUnderline" 
										onclick="setLayer('修改政府类别','${request.getContextPath()}/admin/goverType/add.jhtml?id=${it.sysGovTypeId}');$('.layui-layer-shade').height($(window).height());" 
										>修 改</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${it.sysGovTypeId}, '${it.sysGovTypeName}')">删 除</a>
								</td>
							</tr>
						</#list>
					</tbody>
				</table>
				
				<#if (its?? && its?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; " cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>
				</#if>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		//IE下 背景全屏
	    window.onresize = function(){
			$('.layui-layer-shade').height($(window).height());
		} 
		$(function(){
				//$(".groupList").height($(window).height()-50);
				
				if($(".inputOtherCondition").val() != ""){
					$(".fuck").hide();
				}
				$(".inputOtherCondition").focus(function(){
					$(".fuck").hide();
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(".fuck").show();
					}
				});
				$(".fuck").click(function(){
					$(".inputOtherCondition").focus();
				})
				
			})
	</script>
</html>
