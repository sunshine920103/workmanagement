<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>	
		<script type="text/javascript" >
		$(function(){
			if($("#noshuju").length>0){
				$("#ta td").width("100%");
			}else{
				$("#ta").width($(".tdNum").length*50+"px");
				$("#ta td").width(200+"px");
			}
			
		})
			//回显
			var msg = "${msg}";
			if(msg == "已超过本月的查询限制！"){
				layer.alert(msg, {
					shade: 0.3
				});
				history.go(-1);
			}
			else if(msg != "") {
				layer.alert(msg, {
					shade: 0.3
				});
			}
			$(function(){
				 
				 $(".notnull").each(function(){
//				 			alert("1");
   						 if($(this).text()=='null'){
   						 	alert("1");
   						 	$(this).html(" ");
   						 }
  						});
				$("#searchName").focus(function(){
				if($.trim($(this).val())=="输入重点企业监测群名称"){
					$(this).val("");
				}
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(this).val("输入重点企业监测群名称");
					}
				});
				$("input[value='查询']").click(function(){
					var searchName = $.trim($("#searchName").val());
					if(searchName=="输入重点企业监测群名称"){
//						alert('请输入重点企业监测群名称');
						searchName="";
					}
					window.location.href="${request.getContextPath()}/admin/keyEnterpriseQuery/list.jhtml?searchName="+searchName+"";
				});
				
				
			});
			function isExport(){
				if($("#noshuju").html()=="暂无数据"){
					alert("没有数据不支持导出");
				}else{
					window.location.href='${request.getContextPath()}/admin/keyEnterpriseQuery/export.jhtml';
				}
			}
		</script>

		<script type="text/javascript" language="javascript">  
		
//    	function ExportToExcel(tableId) //读取表格中每个单元到EXCEL中
//		{ 
//	    try{       
//	            var curTbl = document.getElementById(tableId); 
//	            var oXL = new ActiveXObject("Excel.Application"); 
//	            //创建AX对象excel  
//	            var oWB = oXL.Workbooks.Add(); 
//	            //获取workbook对象  
//	            var oSheet = oWB.ActiveSheet; 
//	
//	            var lenRow = curTbl.rows.length; 
//	            //取得表格行数  
//	            for (i = 0; i < lenRow; i++) 
//	            { 
//	                var lenCol = curTbl.rows(i).cells.length; 
//	                //取得每行的列数  
//	                for (j = 0; j < lenCol; j++) 
//	                { 
//	                    oSheet.Cells(i + 1, j + 1).value = curTbl.rows(i).cells(j).innerText;  
//						
//	                } 
//	            } 
//	            oXL.Visible = true; 
//	            //设置excel可见属性  
//	      }catch(e){ 
//	            if((!+'/v1')){ //ie浏览器  
//	              alert("无法启动Excel，请确保电脑中已经安装了Excel!/n/n如果已经安装了Excel，"+"请调整IE的安全级别。/n/n具体操作：/n/n"+"工具 → Internet选项 → 安全 → 自定义级别 → ActiveX 控件和插件 → 对未标记为可安全执行脚本的ActiveX 控件初始化并执行脚本 → 启用 → 确定"); 
//	           }else{ 
//	               alert("请使用IE浏览器进行“导入到EXCEL”操作！");  //方便设置安全等级，限制为ie浏览器  
//	           } 
//	       } 
//	} 
    
		</script>  
		<title>企业重点监测列表</title>
	</head>
	<body>
		<div class="showListBox paddingB30 noBorder">
			<div class="showBtnBox" style="text-align: left;margin: 30px 0px 30px 50px;">
				<!--onClick="javascript:ExportToExcel('ta')"-->
					<input onclick="isExport();" type="button" class="sureBtn marginR40" value="导出EXCEL文件">
					<input type="button" class="cancleBtn closeThisLayer" value="关闭" />
	     	 </div>
			
			<table id="ta" cellpadding="0" cellspacing="0"  class="marginB0" style="table-layout: fixed;">
				<caption class="titleFont1 titleFont1Ex" >重点企业监测群【${enterprisesName}】查询结果</caption>
					<!--<tr style="border-top: 1px solid #dadada;">-->
						<#if indexValues??&&(indexValues?size > 1)>
							<#list indexValues as ivs>
							<tr>
								<#list ivs as testKey>
										<td class="tdNum notnull">${(testKey)!} </td>
								</#list>	
							</tr>
							</#list>
							<!--<#list indexItemNames as its>
								<#if its == "组织机构代码" >
									<td class="noBorderL" style="width: 200px;">组织机构代码</td>
								<#elseif its=="统一社会信用码">	
									<td style="width: 200px;">统一社会信用代码</td>
								<#elseif its=="企业名称">
									<td style="width: 200px;">企业名称</td>
								<#else>
									<td>${its}</td>
								</#if>
							</#list>-->
						<#else>
							<tr >
								<td id="noshuju" class="textCenter fontBold">暂无数据</td>
							</tr>
						</#if>
					<!--</tr>-->
					
			</table>

			
		</div>
			
	</body>
		<script>
			var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
			$('.closeThisLayer').on('click', function(){
	    		parent.layer.close(index); //执行关闭
			});
		</script>
</html>
