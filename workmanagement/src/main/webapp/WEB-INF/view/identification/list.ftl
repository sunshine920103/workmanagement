<!DOCTYPE HTML>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
		<title>标识</title>
	</head>
	<body class="eachInformationSearch marginT0">
	<form id="searchForm" method="post">
		    <input type="hidden" id ="begin1" name="begin" value="${begin}" />
		    <input type="hidden" name="end" id ="end1"  value="${end}" />
		</form>
	<input type="hidden" id="err" value="">
		<div class="queryInputBox">
			<div class="verticalMiddle">
				<input onclick="setLayer('新增标识','${request.getContextPath()}/admin/identification/add.jhtml');$('.layui-layer-shade').height($(window).height());$(this).blur();"
  				type="button" value="新增标识" class="sureBtn" style="margin-left:30px"/>
  				<input type="button" onclick="exp()" value="导 出" id="exp"  class="sureBtn" style="margin-left:30px"/>
  				<div style="margin-left:30px;margin-top: 10px;">
  					<form  method="post"  action="${request.getContextPath()}/admin/identification/list.jhtml">
		  				<input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="begin"
		                       name="begin" value="${begin}"> 至
		                <input class="laydate-icon inputSty fontSize12" autocomplete="off" style="width: 120px;" id="end"
		                       name="end" value="${end}">
		                <input   type="submit" value="查 询" class="sureBtn" style="margin-left:30px"/>
	                </form>
                </div>
                
			</div> 
			<div class="search">
				
			</div>

			<div class="listBox marginT20">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">标识列表</caption>
					<tbody>
						<tr class="firstTRFont">
							<td width="200">标识名称</td>
							<td width="80">所属区域</td>
							<td width="100">操作</td>
						</tr>
						<#list identiFication as iden>
						<tr>
							<td>
								<span class="fll fontSize12">${iden.sys_identification_name}</span>
							</td>
							<td>${iden.sys_area_name}</td>
							<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline" onclick="setLayer('修改标识','${request.getContextPath()}/admin/identification/add.jhtml?id=${iden.sys_identification_id}')">修 改</a><a class="delFont fontSize12 cursorPointer hasUnderline" href="javascript:" onclick="del(this,'${iden.sys_identification_id}','${iden.sys_identification_name}')">删 除</a>
							</td>
						</tr>
						</#list>
					</tbody>
				</table>
				<#if (identiFication?? && identiFication?size > 0)>
						<#include "/fragment/paginationbar.ftl"/>
					<#else>
						<table style="border-top: 0px;" cellpadding="0" cellspacing="0">
							<tr class="firstTRFontColor">
								<td style="text-align: center;font-weight: bold;" >暂无信息</td>
							</tr>
						</table>
					</#if>
			</div>
		</div>
	</body>
	<script type="text/javascript">
		 var start = {
        elem: '#begin',
        format: 'YYYY-MM-DD',
        //min: laydate.now(), //设定最小日期为当前日期
        max: laydate.now(), //最大日期
        istime: true,
        istoday: true,
        choose: function (datas) {
            end.min = datas; //开始日选好后，重置结束日的最小日期
            end.start = datas; //将结束日的初始值设定为开始日
        }
    };
    var end = {
        elem: '#end',
        format: 'YYYY-MM-DD',
        //min: laydate.now(),
        max: laydate.now(),
        istime: true,
        istoday: true,
        choose: function (datas) {
            start.max = datas; //结束日选好后，重置开始日的最大日期
        }
    };
    laydate(start);
    laydate(end);
	
	
		//删除用户
			function del(obj, id, name){
				var tip = "确定要删除<span class='striking'>"+name+"</span> 吗";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
				    	var url = "${request.getContextPath()}/admin/identification/del.jhtml";
						$.post(url,{id:id},function(data){
							//关闭弹窗
							close_wait(option_index);
							if(data.flag){
								//删除页面上的数据
								$(obj).parents("tr").remove();
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
			
		function exp(){
		<#if (identiFication?? && identiFication?size>0)>
		var begin= '';
		var end ='';
		if($("#begin1").val()!='')
				begin = $("#begin1").val();
				if($("#end1").val()!='')
				 end = $("#end1").val();
				window.location.href = "${request.getContextPath()}/admin/identification/reportExcle.jhtml?begin="+begin+"&end="+end;
			<#else>
			alert("导出数据为空！")
		</#if>
		}
	
		//IE下 背景全屏
	    window.onresize = function(){
			$('.layui-layer-shade').height($(window).height());
		} 
		
	</script>
</html>
