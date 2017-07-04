<!DOCTYPE html>
<html lang="en">
<head>
	<#include "/fragment/common.ftl"/>
    <title></title>
    <script>
    	//网络路径+文件名
    	function cli(name){
    		window.location.href = "${prePath}"+name;
    	}
    	function sub(){
    		var begin = $("#begin").val();
    		var end = $("#end").val();
    		if(begin!=""&&end!=""){
    			window.location.href="${request.getContextPath()}/admin/sysDataBackups/index.jhtml?begin="+begin+"&end="+end;
    		}else if(begin==""&&end==""){
    			window.location.href="${request.getContextPath()}/admin/sysDataBackups/index.jhtml";
    		}else{
    			alert("请选择开始时间和结束时间！");
    		}
    		
    	}
    </script>
</head>
<body class="eachInformationSearch">
	<div class="queryInputBox" style="margin-bottom: 0px;">
		
		<div class="marginT20 marginL30">
			<span class="fontSize12 paddingR10">备份时间:</span><input class="laydate-icon inputSty fontSize12" id="begin" style="width: 120px;" name="begin" value="${begin}"> ~ <input class="laydate-icon inputSty fontSize12" id="end" style="width: 120px;"  name="end" value="${end}">
			<input type="button" value="查询" class="sureBtn" onclick="sub()">
		</div>
		<span class="warmFont inlineBlock marginL30 fontSize12 marginT10">注：数据备份将把数据库的所有数据输出为数据库备份文件，存放在服务器中的备份目录databak中。</span>
	</div>
	
	<div class="listBox">
	    <table cellpadding="0" cellspacing="0">
	    	<caption class="titleFont1 titleFont1Ex">历史备份列表</caption>
	        <tbody>
	        	<tr class="firstTRFont">
		            <td width="50">序号</td>
		            <td width="100">备份时间</td>
		            <td width="100">操作</td>
		        </tr>
		        <#list li as l>
	        <tr>
	            <td>${l_index+1}</td>
	            <td>${l.time}</td> 
	            
	            <td>
	                <a onclick="cli(${l.address})" class="changeFont fontSize12 cursorPointer hasUnderline">下载备份文件</a>
	            </td>
	        </tr>
	        </#list>
	       
	        </tbody>
	    </table>
	    
	</div>
	<script>
	        var start = {
	  			elem: '#begin',
	  			format: 'YYYY-MM-DD',
	  			//min: laydate.now(), //设定最小日期为当前日期
	 			// max: laydate.now(), //最大日期
	  			istime: true,
	  			istoday: true,
	  			choose: function(datas){
	    			 end.min = datas; //开始日选好后，重置结束日的最小日期
	    			 end.start = datas //将结束日的初始值设定为开始日
	  			}
			};
			var end = {
	  			elem: '#end',
	  			format: 'YYYY-MM-DD',
	  			//min: laydate.now(),
	  			//max: laydate.now(),
	  			istime: true,
	  			istoday: true,
	  			choose: function(datas){
	    			start.max = datas; //结束日选好后，重置开始日的最大日期
	  			}
			};
			laydate(start);
			laydate(end);
	</script>
</body>
</html>