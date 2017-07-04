<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<script type="text/javascript" >
		$(function(){
				var err= "${err}";
				if(err!=null&&err!=""){
					layer.alert(err, {
							icon: 2,
							shade: 0.3,
							shadeClose: true
						});
				}
			});
			
	
		 $(function(){
		 	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		
			if("${msg}"=="操作失败"){
				layer.open({
				 type: 1,
				 skin: 'layui-layer-demo', //加上边框
				 area: ['420px', '240px'], //宽高
				  closeBtn: 0, //不显示关闭按钮
					anim: 2,
				shadeClose: true, //开启遮罩关闭
				content: '<div class="listBox"><table><caption class="titleFont1 titleFont1Ex">导入失败</caption><#list errorList as e><tr><td>${e}</td></tr></#list></table></div>'
				});
			}else if("${msg}"=="该重点企业群名称已存在，请换一个名称"){
				var msg = "${msg}";
				layer.alert(msg,{icon:2,shade:0.3,shouldClose:true})
			}else if("${msg}"=="操作成功"){
				var msg = "${msg}";
				layer.alert(msg,{icon:1,shade:0.3,shouldClose:true},function(){
					parent.layer.close(index); //执行关闭
					parent.window.location.href = "${request.getContextPath()}/admin/NewlyIncreased/list.jhtml";
				}
				);
				
			}
			
		
			$('.closeThisLayer').on('click', function(){
		    	parent.layer.close(index); //执行关闭
			});	
			$("#submitBtn").click(function(){
				var cancelName = $("#cancelName").val();
				var fileVal = $("#file").val();
				if(cancelName=="") {
					layer.alert("重点企业群名称不能为空",{icon:2,shade:0.3,shouldClose:true});
	              	 return false;
		       }
				if(checkChineseNoSpe(cancelName)=0) {
						layer.alert("重点企业群名称不合法",{icon:2,shade:0.3,shouldClose:true});
		                return false;
			    }
				if(fileVal=="") {
					layer.alert("请选择导入文件",{icon:2,shade:0.3,shouldClose:true});
	                return false;
		      }
				var reg=/(.*).(xls|xlsx)$/;
				if(!reg.test(fileVal)){
						layer.alert("上传文件不是excel格式",{icon:2,shade:0.3,shoudClose:true})
						return false;
				};
			});
			
	 })
		</script>
		<title>菜单</title>
	</head>
	<body>
		<#-- 新增框 -->
			<div class="showListBox">
				<form id="ff" enctype="multipart/form-data" action="${request.getContextPath()}/admin/NewlyIncreased/importExcle.jhtml" method="post" class="marginL20" >
					<table cellpadding="0" cellspacing="0">
						<caption class="titleFont1 titleFont1Ex">新增</caption>
	 					<tr>
	 						<td width="100"  class="noBorderL firstTD">重点企业群名称</td>
	 						<td width="200"  class="secondTD"><input type="text" id="cancelName" name="name" class="inputSty allnameVal" value=""  onblur="onblurVal(this,13,50)" maxlength="50" onKeyDown="if(this.value.length >50){ return false }"/></td>
	 					</tr>
	 					<tr>
	 						<td  class="noBorderL firstTD">导入文件</td>
	 						<td  class="secondTD"><input type="file" id="file" name="file" class="inputSty" value="" /></td>
	 					</tr>
	 				</table>
	 				
	 				<div class="showBtnBox">
					<input type="button" class="cancleBtn closeThisLayer" value="取 消" />
					<input id="submitBtn" type="submit" class="sureBtn" value="确 认" />
					</div>
				</form>
			</div>
	</body>
</html>
