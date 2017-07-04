<!DOCTYPE html>
<html>
	<#include "/fragment/common.ftl"/>
	<head>
		<title>查看字典</title>
	</head>
	<body>
		<div>
			<div class="showListBox">
					<table id="edit" cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">查看字典</caption>
						<tbody>
						<tr>
							<td width="200" class="textCenter" >字典名称</td>
							<td width="250" class="textCenter"><p>${dic.dicName}</p></td>
						</tr>
						<tr>
		                    <td  class="textCenter" >备注:</td>
                            <td colspan="2" width="250" class="textCenter"><p>${dic.dicNotes}</p></td>
		                    </td>
		                </tr>
						<tr>
							<td colspan="2"> &nbsp;</td>
						</tr>
						<tr>
							<td class="textCenter">指标值</td>
							<td class="textCenter">字典代码</td>
						</tr>
						
							<#list dicContentList as item>
								<tr>
									<td class="textCenter">${item.dicContentValue}</td>
									<td class="textCenter">${item.dicContentCode}</td>
								</tr>
							</#list>
						
						</tbody>
					</table>
					<div class="showBtnBox">
						<input class="cancleBtn closeThisLayer" type="button" value="关 闭"/>
					</div>
					
			</div>
		</div>
	</body>
	<script>
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
	$('.closeThisLayer').on('click', function(){
    	parent.layer.close(index); //执行关闭
	});
	</script>
</html>
