<!DOCTYPE html>
<html>
<head>
	<#include "/fragment/common.ftl"/>
<script type="text/javascript">
	$(function(){
		var loading;
		//回显
		var msg = "${msg}"; 
		if(msg!=""){
			layer.alert(msg,{icon:2,shade:0.3,shouldClose:true});
			$('.layui-layer-shade').height($(window).height());
			layer.close(loading);
		}

		$("#search").click(function(){
			var begin=$("#begin").val();
			var end=$("#end").val();
			var indexId=$("#indexId").val();
			var defaultIndexItemCode= $("#defaultIndexItemCode").val();
			var sysOrgId=$("#sysOrgId").val();
			if($("#begin").val()==""||$("#end").val()==""){  
				layer.alert("归档时间不能为空",{icon:2,shade:0.3,shouldClose:true});
			    return false;  
			}  
			if($("#indexId").val()==""){
				layer.alert("请选择指标大类",{icon:2,shade:0.3,shouldClose:true});
			    return false;  
			}
			if(BusinessCreditCode($("#defaultIndexItemCode").val())==1){
				 layer.alert("统一社会信用代码/组织机构代码/身份证号不能为空",{icon:2,shade:0.3,shouldClose:true});
			     return false;  
			}
			if(BusinessCreditCode($("#defaultIndexItemCode").val())==0){
				 layer.alert("统一社会信用代码/组织机构代码/身份证号不合法",{icon:2,shade:0.3,shouldClose:true});
			      return false;  
			}
			if($("#sysOrgId").val()==""){
				layer.confirm("请选择报送机构",{icon:2,shade:0.3,shouldClose:true});
			  	 return false;  
			}
			window.location.href="${request.getContextPath()}/admin/manualEntry/add.jhtml?begin="+begin+"&end="+end+"&indexId="+indexId+"&code="+defaultIndexItemCode+"&sysOrgId="+sysOrgId+"&addOrlist=1";
		});		
		
	});
	
    function openPop(num){
        $("#covered").show();
        $("#poplayer").show();
        $("#poplayer").children(".zIndex").removeClass("zIndex");
        if(num==1){
           $(".xzjg").hide();
           $(".xzzb").show();
           $(".xzzb").addClass("zIndex");
        }else if(num==2){
           $(".xzjg").show();
           $(".xzzb").hide();
           $(".xzjg").addClass("zIndex");
           if($("#treeDemo").html()==""){
	            var loading = layer.load(); 
					$.ajax({
						url:'${request.getContextPath()}/admin/manualEntry/getOrgList.jhtml',
						dataType:'json',
						success:function(nodes){
							layer.close(loading);
							var str1 = [];
							for (var i = 0; i < nodes.length; i++) {
								var obj= new Object();
								 obj.id= nodes[i].id;
								 obj.name= nodes[i].name;
								 obj.pId= nodes[i].parent;
								 str1.push(obj);
							}
							orgtype(str1);
						}
				});
			}
        }
     }


    //关闭上级区域弹出框
    function closePop(){
        $("#covered").hide();
        $("#poplayer").hide();
    }
        

     function selUpstream2(obj){
      $.each($(".xzzb .seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }    
    //确认选择机构
			function confirmSel1(clear) {
				closePop();
				var htext=$("#hideorg input").val()
				var hteid=$("#hideorg input").attr("id")
				if(clear == 1) {
					$("#orgSelect").text("请选择机构");
					$("#indexId").val("");
				} else { 
					$("#orgSelect").text(htext);
					$("#sysOrgId").val(hteid); 
				}  
			}
	//选择指标确认
    function confirmSel2(){
       var seleced = $(".xzzb .seleced");
       closePop();
       var area = $(seleced.find("label")).text();
       if(seleced.length==0){
          $("#indexSelect").text("选择指标大类");
          $("#indexId").val("");
       }else{
          $("#indexSelect").text(area);
		  $("#indexId").val(seleced.attr("id"));
      	}
    } 
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
<title>手工录入添加</title>
</head>
<body>
<#-- 弹出框 -->
<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox xzjg">
        <div class="titleFont1">
            <span>机构列表</span>
        </div>
        <div class="listBox" style="overflow: auto;">
		    <div class="zTreeDemoBackground left">
				<ul id="treeDemo" class="ztree"></ul>
			</div>
		</div>
		<p class="hide" id="hideorg"></p>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
        </div>
    </div>
    <div class="borderBox xzzb">
        <div class="titleFont1">
            <span>指标大类列表</span>
        </div>
        <div class="listBox">
        	<div>
        		<input id="searchInput" class="inputSty" type="text" value="" style="width: 140px;"/>
        		<input id="searchBtn" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
        	</div>
            <table cellpadding="0" cellspacing="0" id="searchTable">
			<#list indexTbs as it>
                <tr>
                    <td level="1" id="${it.indexId}" onclick="selUpstream2(this)">
                        <label>${it.indexName}</label>
                    </td>
                </tr>
			</#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>
</div>
<div class="showListBox" style="width:100%;border: 0px; margin-bottom:0px;margin-left: 0px;margin-top:0px">
<form  method="post" enctype="multipart/form-data" method="post" action="${request.contextPath}/admin/manualEntry/save.jhtml">
	<table  cellpadding="0" cellspacing="0" style="margin-top:20px;margin-bottom: 10px;">
		<caption class="titleFont1 titleFont1Ex">手工修改</caption>
		<tr>
			<td  class="noBorderL firstTD" width:35%><label class="mainOrange"> * </label>截止归档时间
			</td>
			<td width="65%">
				<input name="timeReport" id="begin" autocomplete="off" class="laydate-icon inputSty fontSize12"> ~ 
				<input name="timeReport" id="end" autocomplete="off" class="laydate-icon inputSty fontSize12">
			</td>
		</tr>
		<tr>
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>指标大类
			</td>
			<td>
				<input type="hidden" id="indexId" name="indexId" value=""/>
				<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)" id="indexSelect">选择指标大类</a>
			</td>
		</tr>
		<tr>
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>统一社会信用代码/组织机构代码/身份证号
			</td>
			<td>
				<input type="text" class="inputSty allcorgcodeVal" id="defaultIndexItemCode" name="defaultIndexItemCode" onblur="onblurVal(this,25,18)" maxlength="18"  onKeyDown="if(this.value.length > 18){ return false }">
			
			</td>
		</tr>
		<tr>
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>报送机构
			</td>
			<td>
				<input type="hidden" id="sysOrgId" name="sysOrgId" value=""/>
				<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)" id="orgSelect">请选择机构</a>
			</td>
		</tr>
	</table>
	<input type="button" id="search" class="sureBtn sureBtnEx" value="搜索填充">
    <span class="warmFont marginL20">注：搜索填充的数据为截止归档时间的最新一条记录</span>
</form>
</div>
<div class="marginT20 eachInformationSearch" style="text-align:center;">
  <div class="queryInputBox">
  	<div>
		<input type="text" class="bigBtn textCenter" type="button" value="历史手工修改" onclick="setLayer('历史手工修改','${request.getContextPath()}/admin/manualEntry/changeHistory.jhtml?indexId=${indexTb.indexId}')" style="margin-left: 30px;"/>
	</div>
  	<div class="listBox">
				<table cellpadding="0" cellspacing="0">
					<caption class="titleFont1 titleFont1Ex">企业相关报送记录列表</caption>
					<tr class="firstTRFont">
						<td width="50">序号</td>
						<td width="100">指标大类</td>
						<td width="100">录入机构</td>
						<td width="100">归档时间</td>
						<td width="100">录入时间</td>
						<td width="80">操作</td>
					</tr>
					<#list queryMajorIddataValues as rps>
					<#list rps?keys as itemKey>
						<#if itemKey_index==0>
						<tr>
		            		<td>${(1 + rps_index) + (page.getPageSize() * page.getCurrentPage())}</td>
							<td>${indexTb.indexName}</td>
							<td>${rps.SYS_ORG_NAME}</td>
							<td>${(rps.RECORD_DATE?string("yyyy-MM-dd"))!} </td>
							<td>${(rps.SUBMIT_TIME?string("yyyy-MM-dd HH:mm:ss"))!} </td>
							<td>
								<a class="changeFont fontSize12 cursorPointer hasUnderline" href="javascript:void(0);" onclick="setLayer('查看详情','${request.getContextPath()}/admin/manualEntry/defaultData.jhtml?id=${rps[code]}&indexId=${indexTb.indexId}')">查看详情</a>
							</td>
						</tr>	
						</#if>
					</#list>	
					</#list>
					<!-- 获取所有手工修改数据 -->
				</table>
				<#if (queryMajorIddataValues?? && queryMajorIddataValues?size > 0)>
					<#include "/fragment/paginationbar.ftl"/>
				<#else>
					<table style="border-top: 0px; "  cellpadding="0" cellspacing="0">
						<tr class="firstTRFontColor">
							<td style="text-align: center;font-weight: bold;" >暂无信息</td>
						</tr>
					</table>	    	
				</#if>
			</div>
		</div>
</div>
</body>
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
	
	//搜索按钮
	$("#searchBtn").click(function(){
		var searchVal = $.trim($("#searchInput").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable");
		$.post("${request.getContextPath()}/admin/manualEntry/indexTbByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.indexId+"' onclick='selUpstream2(this)'><label>"+v.indexName+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput").val(""));
			});
			layer.close(searchLoading);
		});
	});
	
	//搜索按钮
	$("#searchBtn1").click(function(){
		var searchVal = $.trim($("#searchInput1").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable1");
		$.post("${request.getContextPath()}/admin/manualEntry/sysOrgByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sys_org_id+"' onclick='selUpstream1(this)'><label>"+v.sys_org_name+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput1").val(""));
			});
			layer.close(searchLoading);
		});
	});
</script>	
</html>