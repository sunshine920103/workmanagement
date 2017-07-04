<!DOCTYPE html>
<html>
	<head>
		<title>信用评分查询</title>
		<#include "/fragment/common.ftl"/>
		<script src="${request.getContextPath()}/assets/js/ajaxfileupload.js"></script>
		
		<script>
			var codeCredits = [];
			var codeOrgs = [];
			var flag = false;
			var date;
			
			//下载excel模板
			function downLoad(){
				window.location.href="${request.getContextPath()}/admin/creditScoreQuery/export_template.jhtml";
			}
		
			//查询单个评分
			function searchScore(index_jbxx_id, searchDate){
				if(typeof index_jbxx_id == "undefined"){
					alertInfo("未获取到企业标识");
					return false;
				}
				var template_id = $("#template_id").val();
				var url = '${request.getContextPath()}/admin/creditScoreQuery/detailScore.jhtml?index_jbxx_id=' + index_jbxx_id + "&template_id=" + template_id + "&date=" + searchDate+ " 23:59:59";
				setLayer('查询',url);
			}
			
			//查询企业
			function sigleSearch(form){
				var date = $("#sigleDate").val();
				var key = $("#key").val();
				var template_id = $("#template_id").val();
				if(date=="" || key=="" || template_id==null){
					alertInfo("请填写所有查询条件");
					return false;				
				}
				if(form.key.value.length != 18 && form.key.value.length != 10){
			    	alertInfo("请输入准确的统一社会信用代码或组织机构代码");
		            return false;
			    }
				if(checkTYNoLT18(form.key.value) == 0) {
					alertInfo("查询内容输入不合法");
		            return false;
			    }
				wait();
				return true;
			}
			
			//判断批量查询条件是否完全填写
			function uploadSearchLimit(){
				var template_id = $("#queryTotalTemplateId").val();
			
				if($("#file").val()==""){
					alertInfo("您还没有选择文件");
					return false;
				}
				wait();
				return true;
			}
			
			//设置评分日期
			function setDate(dates){
				date = dates
			}
			
			//批量查询评分
			function batchSearchScore(){
				if(flag){
					var template_id = $("#template_id").val();
					var date = $("#scoreDate").val();
					var diics = $("#diics").val();

					if(date=="" || template_id==null){
						alertInfo("请填写所有查询参数");		
					}else if(diics==0){
						alertInfo("查询企业信息为空");
					}else{
						var url = '${request.getContextPath()}/admin/creditScoreQuery/result.jhtml?template_id=' + template_id + "&date=" + date;
						setLayer('查询',url);
					}
				}else{
					alertInfo("您还没有上传文件", false);
				}
			}
			
		</script>
		<script type="text/javascript">
			$(function(){
				if($(".inputKuang").val() != ""){
					$(".fuck").hide();
				}
				var msg = "${msg}";
				if(msg!=""){
					alertInfo(msg);
				}
			
				$(".inputKuang").focus(function(){
					$(".fuck").hide();
				}).blur(function(){
					if($.trim($(this).val())==""){
						$(".fuck").show();
					}
				});
				$(".fuck").click(function(){
					$(".inputKuang").focus();
				})
				
				/*var flag = $("#key").val();
				setFlag($(".setFlag"), flag);*/
			})
			 //IE下 背景全屏
		    window.onresize = function(){
				$('.layui-layer-shade').height($(window).height());
			} 
		</script>
	</head>
	<body class="eachInformationSearch">
		<div class="tabBoxScore">
			<span class="singleQuery" style="font-weight:bold;color:black">单个查询</span><span class="doubleQuery">批量查询</span>
		</div>
	    <div class="hasBorderScore">
	   
		   	<!--单次查询-->
			<div class="single">
				<div class="queryInputBox marginLR30">
					<span class="paddingR10">评分标准 : </span>
					<select id="template_id" name="queryTotalTemplateId" class="inputSty shouldHide">
						<#list template as t>
							<option value="${t.queryCopeTemplateId}">${t.queryCopeTemplateName}</option>
						</#list>
					</select>
				</div>
				<div class="queryInputBox marginLR30">
					<form method="post" action="${request.getContextPath()}/admin/creditScoreQuery/index.jhtml" style="position: relative;">
						<div class="queryInputBox">
							<span class="paddingR10">归档时间 : </span>
							<input id="sigleDate" onclick="laydate({istime:false,format: 'YYYY-MM-DD', max:laydate.now()})" autocomplete="off" class="laydate-icon inputSty fontSize12" name="date" value="${date}">
						</div>
						<#if code??>
							<input id="key" class="inputKuang" name="key" value="${code}" type="text"/>
							<span class="fuck" style="position: absolute; left: 6px;top: 55px;" class="warmFont fontSize12">统一社会信用代码 | 组织机构代码</span>
						<#else>
							<input id="key" class="inputKuang" name="key" value="${key}" type="text"/>
							<span class="fuck" style="position: absolute; left: 6px;top: 55px;" class="warmFont fontSize12">统一社会信用代码 | 组织机构代码</span>
						</#if>
						<input type="hidden" value="true" name="query" />
						<input class="sureBtn" type="submit" onclick="return sigleSearch(this.form)" value="查询企业"/>
					</form>
				</div>
				<div class="listBox">
					<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">查询企业结果列表</caption>
						<tbody>
							<tr class="firstTRFont">
								<td width="200" >统一社会信用代码</td>
								<td width="200">组织机构代码</td>
								<td width="200">企业名称</td>
								<td width="80">操作</td>
							</tr>
							<#list enterprise as e>
								<tr>
									<td class="setFlag">${e.CODE_CREDIT}</td>
									<td class="setFlag">${e.CODE_ORG}</td>
									<td>${e.INDEX_JBXX_QYMC}</td>
									<td><a href="javascript:" onclick="searchScore(${e.INDEX_JBXX_ID}, '${date}')" class="changeFont fontSize12 cursorPointer hasUnderline">开始查询</a></td>
								</tr>
							</#list>
						</tbody>
					</table>
					<#if !(enterprise?? && enterprise?size > 0)>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>
				</div>
			</div>
			
			
			
			
			
			<!--批量查询-->
			<div class="double" style="display: none;">
				<form action="${request.getContextPath()}/admin/creditScoreQuery/batch.jhtml" method="post" enctype="multipart/form-data">
					<div class="queryInputBox marginLR30">
						<span class="paddingR10">评分标准 : </span>
						<select id="queryTotalTemplateId" name="queryTotalTemplateId" class="inputSty shouldHide">
							<#list template as t>
								<option value="${t.queryCopeTemplateId}">${t.queryCopeTemplateName}</option>
							</#list>
						</select>
					</div>
					<div class="queryInputBox marginLR30">
						<span class="paddingR10">归档时间 : </span>
						<input id="scoreDate" onclick="laydate({istime:false,format: 'YYYY-MM-DD', choose:setDate, max:laydate.now()})" autocomplete="off" class="laydate-icon inputSty fontSize12" name="date" value="${date}">
					</div>
					<div class="queryInputBox marginLR30">
						<input id="file" name="file" type="file" class="inputSty"/>
						
						<input type="submit" onclick="return uploadSearchLimit()" class="sureBtn" value="上传批量查询名单" />
						<input type="button" class="sureBtn sureBtnEx" onclick="downLoad()" value="下载excel查询模板">
						
						<p class="warmFont paddingL20 fontSize12 marginT20">
							注：上传excel文件，列名为“统一社会信用代码”“组织机构代码”
							“企业名称”，一次性查询不能超过50个企业
						</p>
					</div>
				</form>	
					
				<div class="listBox">
					<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">上传企业列表</caption>
						<thead>
							<tr class="firstTRFont">
								<td width="200" >统一社会信用代码</td>
								<td width="200">组织机构代码</td>
								<td width="200">企业名称</td>
							</tr>
						</thead>
						<tbody>
							<#list diics as d>
								<tr>
									<td>${d.codeCredit}</td>
									<td>${d.codeOrg}</td>
									<td>${d.qymc}</td>
								</tr>
							</#list>
						</tbody>
					</table>
					<#if !(diics?? && diics?size > 0)>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>
					
					<input type="hidden" value="${diics?size}" id="diics" />
	
					<div class="showBtnBox textCenter marginT20">
						<input id="sure" onclick="return batchSearchScore()" class="sureBtn sureBtnEx" data-id="1" type="button" value="开始查询"/>
					</div>
				</div>
			</div>
	  	</div>
	</body>
	
	<script>
		//TAB切换
		$(".singleQuery").click(function(){
			$(".double").hide();
			$(".single").show();
			$(".singleQuery").css({"color":"black","font-weight":"bold"});
			$(".doubleQuery").css({"color":"gray","font-weight":"normal"});
		});
		$(".doubleQuery").click(function(){
			$(".double").show();
			$(".single").hide();
			$(".doubleQuery").css({"color":"black","font-weight":"bold"});
			$(".singleQuery").css({"color":"gray","font-weight":"normal"});
		})
		var type = "${type}";
		var msg = "${msg}";
		if(type!=""){
			$(".doubleQuery").click();
			flag = true;
		}
		if(msg!=""){
			alertInfo(msg);
		}
	</script>
</html>
