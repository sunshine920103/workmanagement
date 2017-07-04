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
		};

		$("#search1").click(function(){
			if($("#timeReport").val()==""){  
			    layer.alert("请选择归档时间",{icon:2,shade:0.3,shouldClose:true});
			    return false;  
			}   
			if($("#indexId").val()==""){
				 layer.alert("请选择指标大类",{icon:2,shade:0.3,shouldClose:true});
			     return false;  
			}
			if($("#sysOrgId").val()==""){
				 layer.alert("请选择报送机构",{icon:2,shade:0.3,shouldClose:true});
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
			if(checkChineseNoSpe($("#orgName").val())==1){
				 layer.alert("企业名称不能为空",{icon:2,shade:0.3,shouldClose:true});
			      return false;  
			}
			if(checkChineseNoSpe($("#orgName").val())==0){
				 layer.alert("企业名称不合法",{icon:2,shade:0.3,shouldClose:true});
			     return false;  
			}
			
			var operateAuthFile=$("#operateAuthFile").val();
			if(operateAuthFile==1){
				var reg=/(.*).(jpg|bmp|gif|pdf|jpeg|png)$/;
				var fileName=$("[name='file']").val();
				if(fileName==""){
					layer.alert("请上传授权文件",{icon:2,shade:0.3,shouldClose:true});
				     return false;  
				} 
				 
				if(!reg.test(fileName)) {
					layer.alert("请上传'.pdf'、'.jpg'、'.jpeg'、'.gif'、'.png'或'.bmp'格式文件文件",{icon:2,shade:0.3,shouldClose:true});
				     return false;  
				}
			}
			
			//$("#changeWidth").width(20+"%");
			var timeReport=$("#timeReport").val() 
			var indexId=$("#indexId").val()
			var defaultIndexItemCode=$("#defaultIndexItemCode").val()
			var sysOrgId=$("#sysOrgId").val()
			var orgName=$("#orgName").val()
			var infile=$("[name='file']").val();
			window.location.href="${request.getContextPath()}/admin/adminObj/reportList.jhtml?timeReport="+timeReport+"&indexId="+indexId+"&code="+defaultIndexItemCode+"&sysOrgId="+sysOrgId+"&orgName="+orgName+"&file="+infile+"&operateAuthFile="+operateAuthFile;
//			var myurl="${request.getContextPath()}/admin/adminObj/show.jhtml?timeReport="+timeReport+"&indexId="+indexId+"&code="+defaultIndexItemCode+"&sysOrgId="+sysOrgId;
//			myFrameName.location.href=myurl
			$("form").submit();	
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
        
        
    function selUpstream(obj){
      $.each($(".seleced"),function(){
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
					$("#sysOrgId").val("");
				} else { 
					$("#orgSelect").text(htext);
					$("#sysOrgId").val(hteid); 
				}  
			}

    function confirmSel2(clear){
       var seleced = $(".xzzb .seleced");
       closePop();
       var area = $(seleced.find("label")).text();
       if(clear===1||seleced.length==0){
          $("#indexSelect").text("选择指标大类");
          $("#indexId").val("");
       }else{
          $("#indexSelect").text(area);
		  $("#indexId").val(seleced.attr("id"));
       }
    }
    //模糊查询指标大类
    function test(obj){
	    var v=$("#searchInput1").val();
	    	$.post("${request.getContextPath()}/admin/adminObj/editmohu.jhtml",{indexName:v},function(result){
			str_json=eval("("+result+")");
			var con="";
			$('#searchTable tr').remove();
			$.each(str_json,function(a,b){
			var tr ="<tr><td id='"+b.indexId+"' onclick='selUpstream(this)'><label>"+b.indexName+"</label></td></tr>";
			$("#searchTable").append(tr);
			});
	     });
    }
 
 
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
</script>
<title>异议处理</title>
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
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel1(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
        </div>
    </div>
    <div class="borderBox xzzb">
        <div class="titleFont1">
            <span>指标大类列表</span>
        </div>
        <div class="listBox">
        	<div>
        		<input id="searchInput1" class="inputSty" type="text" value="" style="width: 140px;"/>
        		<input id="searchBtn1" type="submit" value="搜 索" onclick="test(this)" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
        	</div>
            <table cellpadding="0" cellspacing="0" id="searchTable">
			<#list indexTb as it>
                <tr>
                    <td level="1" id="${it.indexId}" onclick="selUpstream(this)">
                        <label>${it.indexName}</label>
                    </td>
                </tr>
			</#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="重 置" class="resetBtn" onclick="confirmSel2(1)"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>
</div>
<div class="eachInformationSearch">
<div id="changeWidth" class="showListBox noBorder">
	
		
	
<form  method="post" enctype="multipart/form-data" method="post" action="${request.contextPath}/admin/adminObj/reportList.jhtml">
	<table  cellpadding="0" cellspacing="0">
		<caption class="titleFont1 titleFont1Ex">异议处理</caption>
		<tr >
			<td  class="noBorderL firstTD" width="200"><label class="mainOrange"> * </label>归档时间
			</td>
			<td width="400">
				<input class="laydate-icon inputSty" autocomplete="off" 
					id="timeReport" onclick="laydate({istime: false, format: 'YYYY-MM-DD'})" name="timeReport" value="">
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
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>报送机构
			</td>
			<td>
				<input type="hidden" id="sysOrgId" name="sysOrgId" value=""/>
				<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)" id="orgSelect">选择报送机构</a>
			</td>
		</tr>
		<tr>
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>统一社会信用代码/组织机构代码/身份证
			</td>
			<td>
				<input type="text" class="inputSty" id="defaultIndexItemCode" name="defaultIndexItemCode" onblur="onblurVal(this,25,18)" maxlength="18"  onKeyDown="if(this.value.length > 18){ return false }">
			
			</td>
		</tr>
		
		<tr>
			<td class="noBorderL firstTD" ><label class="mainOrange"> * </label>企业名称</td>
			<td>
				<input type="text" class="inputSty" id="orgName" name="orgName" onblur="onblurVal(this,13,50)" maxlength="50" onKeyDown="if(this.value.length > 50){ return false }">
			
			</td>
		
		</tr>
		<input class="inputSty" type="hidden" id="operateAuthFile" name="operateAuthFile" value="${operateAuthFileSwitch}" />
		<#if operateAuthFileSwitch==1>
		<tr>
			<td class="noBorderL firstTD"><label class="mainOrange"> * </label>授权文件</td>
			<td class="secondTD">
				<input name="file" class="inputSty inlineBlock" type="file" value="浏览">
			</td>
		</tr>
		</#if>
	</table>
	<div class="showBtnBox">
		<input type="button" id="search1" class="sureBtn sureBtnEx" value="搜索填充">
		<span class="warmFont marginL20">注：搜索填充的数据为截止归档时间的最新一条记录</span>
	</div>
	
</form>
</div>
</div>
<div class="marginT20 eachInformationSearch" >
	<div class="queryInputBox">
			
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
					<#list queryMajorIddataValues as it>
					<tr class="firstTRFont">
						<td width="50">${it_index+1}</td>
						<td width="100">${indexName}</td>
						<td width="100">${it.SYS_ORG_NAME}</td>
						<td width="100">${it.RECORD_DATE}</td>
						<td width="100">${it.SUBMIT_TIME}</td>
						<td width="80">
							<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" method="post" enctype="multipart/form-data" onclick="setLayer('异议处理','${request.getContextPath()}/admin/adminObj/show.jhtml?id=${it[code]}&&IndexTableName=${IndexTableName}&&orgCreditCode=${orgCreditCode}&&file=${file}&&reportOrgId=${it.SYS_ORG_ID}&&ceshi=0&&itemId=${it.DEFAULT_INDEX_ITEM_ID}')">异议处理</a>
						</td>
					</tr>
					</#list>
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
</html>