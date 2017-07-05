<!DOCTYPE HTML>
<html>
	<head>
	<#include "/fragment/common.ftl"/>
	<script type="text/javascript">
		$(function () {
	    //回显
		    var msg = "${msg}";
		    if (msg != "") {
		        layer.alert(msg, {
		            icon: (msg == "操作成功") ? 1 : 2,
		            shade: 0.3,
		            shadeClose: true
		        }, function () {
		                var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		                parent.layer.close(index); //执行关闭
		                parent.window.location.href = "${request.getContextPath()}/admin/messageSubmission/list.jhtml";
		        });
		    }
	    })
	</script>

		<title>基本信息企业名称差异列表</title>
	</head>
	<body class="showListBox excelContent">
		<div class="listBox">
				<table  cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">企业名称不匹配列表</caption>
					<tr class="firstTRFont">
						<td width="30"><input type="checkbox" onclick="CheckedRev()" >全选/反选</td>
						<td width="80">企业名称信息</td>
						<td width="80">信息总条数：${qymcMsg?size}</td>
					</tr>
					<#list qymcMsg as map>
						<tr>
							<td width="30">
							<input  type="checkbox" >							
							<input type="hidden" name="recordDate" value="${map['record_date']}">
							<input type="hidden" name="newName" value="${map['newName']}">
							<input type="hidden" name="oldName" value="${map['dii'].qymc}">
							<input type="hidden" name="defaultId" value="${map['data']['DEFAULT_INDEX_ITEM_ID']}">
							</td>
							<td colspan="2">${map['msg']}</td>
														
						</tr>
					</#list>
				</table>
		</div>
		<div class="showBtnBox">
		<input class="sureBtn" type="button" value="替换" onclick="saveThis()"/>
    	</div>
	</body>
<script>
	function saveThis() {
        var reco = "";
        var newn = "";
        var oldn = "";
        var defa = "";
        $("input[type='checkbox']").each(function () {
            if ($(this).is(':checked')) {
                var recordDate = $(this).siblings("input[name='recordDate']").val();
                var newName = $(this).siblings("input[name='newName']").val();
                var oldName = $(this).siblings("input[name='oldName']").val();
                var defaultId = $(this).siblings("input[name='defaultId']").val();
                reco += (recordDate + ",");
                newn += (newName + ",");
                oldn += (oldName + ",");
                defa += (defaultId + ",");
            }
        });
        $.post("${request.getContextPath()}/admin/reportIndex/changeName.jhtml",
                {"reco": reco, "newn": newn, "oldn": oldn, "defa": defa}, function (msg) {
                    layer.alert(msg, {
                        icon: (msg == "操作成功") ? 1 : 2,
                        shade: 0.3,
                        shadeClose: true
                    },function(){ 
                    	  window.close();
                    });
                }
        )
    }

	//全选反选
		function CheckedRev(){		
		 $(':checkbox').each(function(){
		 	this.checked = ! this.checked; 
		 })
		 this.checked = ! this.checked;
		}
	</script>
</html>
