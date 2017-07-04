<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript">
			$(function(){
				//回显
				var msg = "${msg}";
				if(msg != "") {
		            layer.alert(msg,{
						icon: (msg=="操作成功")?1:2,
//						time:15000,
						shade:0.3,
						shadeClose:true
					});
//					alertInfo(msg);
				}
				var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
				$(".closeThisLayer").on("click", function(){
					if("${returnNum}" =="returnNum"){
						history.go(-1);
					}else{
		    			parent.layer.close(index); //执行关闭
		    		}
				});
				$("form").submit(function(){
					
					 
					if($("[name='indexDwdbxxRecordTime']").val()==""){
//						alertInfo("请选择截止归档时间");
//						return false;
						$("[name='indexDwdbxxRecordTime']").val(getNowDate());
					}
					if($(":checkbox:checked").size() <1){
						layer.alert("请至少选择一个关联类型", {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
//						alertInfo("请至少选择一个关联类型");
						return false;
					}
				});
				
				$("#indexDwdbxxRecordTime").click(function(){
					setLayDate("#indexDwdbxxRecordTime");
				})
				
			})
		</script>
		<title>选择关联信息查询类型</title>
	</head>
		<body>
		
		<div class="showListBox">
			<form action="${request.getContextPath()}/admin/relatedInfo/result.jhtml" method="post">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">选择关联信息查询类型</caption>
					<tbody>
					<tr>
						<td width="250" class="noBorderL firstTD">统一社会信用代码<input type="hidden" name="defaultIndexItemId" value="${defaultIndexItemCustom.defaultIndexItemId}"></td>
						<td width="400" class="secondTD"><input type="text" readonly="readonly" name="codeCredit" value="${defaultIndexItemCustom.codeCredit}"></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">组织机构代码</td>
						<td class="secondTD"><input type="text" readonly="readonly" name="codeOrg" value="${defaultIndexItemCustom.codeOrg}"></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD">企业名称</td>
						<td class="secondTD"><input style="width:300px" type="text" readonly="readonly" name="qymc" value="${defaultIndexItemCustom.qymc}"></td>
					</tr>
					<tr>
						<td class="cnoBorderL firstTD">法定代表人</td>
						<td class="secondTD"><input type="text" readonly="readonly" name="fddbr" value="${defaultIndexItemCustom.fddbr}"></td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>截止归档时间</td>
						<td class="secondTD">
							<input autocomplete="off" class="laydate-icon inputSty fontSize12" name="indexDwdbxxRecordTime" id="indexDwdbxxRecordTime" type="text" />
						</td>
					</tr>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>关联类型</td>
						<td class="secondTD">
							<label for="relatedTypes0"><input type="checkbox" name="relatedTypes" value="投资关联" class="verticalMiddle" id="relatedTypes0"/> 投资关联</label>
							<label for="relatedTypes1"><input type="checkbox" name="relatedTypes" value="担保关联" class="verticalMiddle" id="relatedTypes1"/>担保关联</label>
							<label for="relatedTypes2"><input type="checkbox" name="relatedTypes" value="高管关联" class="verticalMiddle" id="relatedTypes2"/> 高管关联</label>
						</td>
					</tr>
					</tbody>
				</table>
				<div class="showBtnBox">
					<input class="cancleBtn closeThisLayer" type="button" value="取 消"/>
					<input class="bigBtn" data-id="1" type="submit" value="查询关联结果"/>
				</div>
			</form>			
		</div>
	</body>

</html>
