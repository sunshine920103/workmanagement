<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
			
			//IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
			
			var loading;
			//回显
			var msg = "${msg}";
			if(msg != "") {
				alertInfo(msg);
//				layer.alert(msg,{
//					icon: (msg=="操作成功")?1:2,
//					shade:0.3,
//					shadeClose:true
//				});
				layer.close(loading);
			}
//			layer.alert(msg, {
//					shade:0.3,
//				    time: 5000, //20s后自动关闭
//				    btn: ['确定', '取消'],
//				    yes: function(index){
//				    	layer.close(index);
//				  	}
//			  	});
			
			//删除机构
			function del(obj, id, name){
				var tip = "确定要删除 <span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	//关闭确认弹窗
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = layer.load(0,{
							shade: [0.5,'#fff'] //0.1透明度的白色背景
						});
				    	var url = "${request.getContextPath()}/admin/sysGover/del.jhtml";
						$.post(url,{_:Math.random(),id:id},function(data){
							//关闭正在操作弹窗
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
			}	
			//全部导出
			function down(){
				window.open('${request.getContextPath()}/admin/sysGover/exportAll.jhtml?name=${sysGovName}');
			}
			//下载模板
			function downModel(){
				window.open('${request.getContextPath()}/admin/sysGover/exportModel.jhtml');
			}
			
			$(function(){
				$("#upload").submit(function(){
					var fileName=$("#file").val();
					if(fileName == "") {
//				        layer.alert("请选择excel文件", {
//							icon: 2,
//							shade: 0.3,
//							shadeClose: true
//						});
						layer.confirm("请选择excel文件", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				        return false;
				    }
					var reg=/(.*).(xls|xlsx)$/; 
				 
					if(!reg.test(fileName)) {
//				        layer.alert("文件不是excel格式", {
//							icon: 2,
//							shade: 0.3,
//							shadeClose: true
//						});
						layer.confirm("请选择后缀名为.xls或.xlsx的文件", {btn: ['确定']}, function(){$(".shouldHide").show();layer.closeAll('dialog');});
				        return false;
				    }
					loading = layer.load();
				});
			});
		</script>
		<title>政府部门管理</title>
	</head>
	
<body class="eachInformationSearch">
	<#if (msgString.size>0)>
		<input type="hidden" id="err" value="${msgString?size} " />
	<#else>
		<input type="hidden" id="err" value="" />
	</#if>
	<#if (msgStr.size>0)>
		<input type="hidden" id="errList" value="${msgStr?size}" />
	<#else>
		<input type="hidden" id="errList" value="" />
	</#if>
	<form id="searchForm" method="post"></form>
	<div class="queryInputBox" style="margin-bottom:10px">
		<div style="zoom: 1;" class="verticalMiddle">
			<input onclick="setLayer('新增部门','${request.getContextPath()}/admin/sysGover/add.jhtml');$('.layui-layer-shade').height($(window).height());" type="button" value="新增部门" class="sureBtn" style="margin-left:30px"/> 
			<form id="upload" action="${request.getContextPath()}/admin/sysGover/excelReader.jhtml" method="post" enctype="multipart/form-data" style="display: inline-block;*zoom=1;*display:inline;margin-left:20px"> 
				 <input class="inputSty" id="file" type="file" name="file" value="上传"  />
			     <input type="submit" value="导入部门" class="sureBtn sureBtnEx marginL20" style="margin-left:0px ;"/>
			     
			  </form>
		     <input onclick="downModel()" type="button" value="下载模板" class="sureBtn sureBtnEx marginL20"/>
		     
		</div>
		</div>
		<div class="listBox">
			<div>
			 <input onclick="down()" type="button" value="全部导出" class="sureBtn sureBtnEx" />
			   <form id="form" action="${request.getContextPath()}/admin/sysGover/list.jhtml" method="post" style="display: inline-block;*zoom=1;*display:inline;position:relative" class="marginL20" >
		    	
				 <span class="fuck">请输入政府部门名称查询</span>
			     <input class="inputSty inputOtherCondition" type="text" name="sysGovName" id="sysGovName" />
			     <input type="submit" class="sureBtn sureBtnEx" value="查  询" style="margin-left:0px"/>
			</form>
			   
			     
			</div>
			<table cellpadding="0" cellspacing="0" class="marginT10">
				<caption class="titleFont1 titleFont1Ex">政府部门编码列表</caption>
				<thead>
					<tr class="firstTRFont">
						<td width="300">部门名称</td>
						<td width="120">部门编码</td>
						<td width="200">备注</td>
						<td width="150">操作</td>
					</tr>
				</thead>
				<tbody>
					<#list its as it>
						<tr>
							<td>${it.sysGovName}</td>
							<td>${it.sysGovFinancialCode}</td>
							<td>${it.sysOrgNotes}</td>
							<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline"
								onclick='setLayer("修改部门","${request.getContextPath()}/admin/sysGover/add.jhtml?id=${it.sysGovId}");$(".layui-layer-shade").height($(window).height());'
								 >修 改</a><a class="changeFont fontSize12 cursorPointer hasUnderline" href="${request.getContextPath()}/admin/sysGover/export.jhtml?id=${it.sysGovId}">导出</a>
								 <a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="return del(this, ${it.sysGovId}, '${it.sysGovName}')">删 除</a>
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
	$(function(){
		if($("#err").val()!=0){
			layer.open({
 			type: 1,
 			skin: 'layui-layer-demo', //加上边框
 			area: ['420px', '240px'], //宽高
 			closeBtn: 0, //不显示关闭按钮
 			anim: 2,
  			shadeClose: true, //开启遮罩关闭
 			content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgString as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
			});
		}
	})
	$(function(){
		if($("#errList").val()!=0){
			layer.open({
 				type: 1,
 				skin: 'layui-layer-demo', //加上边框
 				area: ['420px', '240px'], //宽高
 				closeBtn: 0, //不显示关闭按钮
 				anim: 2,
  				shadeClose: true, //开启遮罩关闭
 				content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list msgStr as it><tr><td>${it_index+1}</td><td>${it}</td></tr></#list></table></div>'
			});
		}
	})
	
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
	$(function(){
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
			
			$("#exp").click(function(){
				var num = $("tbody").children().length;
				var arr=[];
				for(var i=1;i<num;i++){
					var code = $("tr").eq(i).find("#code").text();
					arr.push(code);
				}
				location.href="${request.getContextPath()}/admin/sysOrgType/exportAll.jhtml?arr="+arr.join(",");
			});
	})
</script>
</html>
