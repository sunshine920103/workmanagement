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
//			layer.alert(msg,{
//				icon: (msg=="操作成功")?1:2,
//				time:15000,
//				shade:0.3,
//				shadeClose:true
//			});
			alertInfo(msg);
			$('.layui-layer-shade').height($(window).height());
			
			layer.close(loading);
		}
		
		//时间控件
		$("#timeControler").click(function(){
			laydate({
				istime: false,
				max: laydate.now(), 
				format: 'YYYY-MM-DD'
			});
		})
		
	});
	//保存
	function save(){
		var authFileSwitch=$("#authFileSwitch").val();
		var file1=$("[name='file']").val()
	    if(authFileSwitch==1){
	    	if(file1==""){
				alertInfo("请上传授权文件");
			}else{
				var reg=/(.*).(jpg|bmp|gif|pdf|jpeg|png)$/; 
				if(!reg.test(file1)) {
					layer.alert("请上传'.pdf'、'.jpg'、'.jpeg'、'.gif'、'.png'或'.bmp'格式文件文件",{icon:2,shade:0.3,shouldClose:true});
			    }else{
			    	$("#YesNo").val(1);
					loading = layer.load();
					$("form").submit();
			    } 
			}
	    }else{
	    	$("#YesNo").val(1);
			loading = layer.load();
			$("form").submit();
	    }
	}
	//取消
	function saveNo(){
		$("#YesNo").val(0);
		loading = layer.load();
		$("form").submit();
	}
	//IE下 背景全屏
    window.onresize = function(){
		$('.layui-layer-shade').height($(window).height());
	} 
	
	function openArea(obj, id){
		obj = $(obj);
		//子区域的缩进
		var spacing = parseInt(obj.parent().css("padding-left").split("px")[0]) + 15 + "px";
		
		if(obj.attr("id")==0){ //未展开
			//显示正在加载子区域弹窗
			var tc_index = layer.load(0,{
				shade: [0.5,'#fff'] //0.1透明度的白色背景
			});
			//设置为展开状态
			obj.attr("id",1);
			//将图标设置为展开图标
			$(obj.find("img")[0]).css("right",5);
			
			//获取父区域的tr
			var ptr = obj.parent().parent();
			var url = "${request.getContextPath()}/admin/sysArea/getArea.jhtml";
			$.get(url,{_:Math.random(),id:id},function(result){
				//关闭加载提示弹窗
				layer.close(tc_index);
				if(result!=null){
					var subs = result.subArea;
					for(var i = 0; i < subs.length; i++){
						//子地区
						var sub = subs[i];
						//展开图标
						var icon = "";
						if(sub.subArea!=null && sub.subArea.length!=0){
							icon = '<div id="0" class="open-shrink" onclick="openArea(this,'+sub.sysAreaId+')">'
								   +'<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>'
								   +'</div>'
						}
						
						var tr = $("<tr name='"+sub.sysAreaId+"' class='"+id+"'></tr>");
						var name = null;
						
						if(sub.sysAreaType==="3"){
							name = "<td id='"+sub.sysAreaCode+"' onclick='javascript:layer.alert(\"不能选择最后一级的地区\")' style='padding-left:"+spacing+"'>"
									+icon
									+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
									+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
									+"</td>";
						}else{
							name = "<td id='"+sub.sysAreaCode+"' onclick='selUpstream5(this)' style='padding-left:"+spacing+"'>"
									+icon
									+"<label class='fontSize12'>"+sub.sysAreaName+"</label>"
									+"<input type='hidden' value='"+ sub.sysAreaType +"' />"
									+"</td>";
						}
						
						tr.append(name);
						
						ptr.after(tr);
					}
				}
			});
		}else{ //已展开
			//设置为收缩状态
			obj.attr("id",0);
			//将图标设置为收缩图标
			$(obj.find("img")[0]).css("right",20);
			//删除子区域
			delSubArea(id);
		}
	}
	
	//删除地区管理子区域
	function delSubArea(id){
		$.each($("."+id),function(i, v){
			var pid = v.attributes.getNamedItem("name").nodeValue;
			
			//删除子区域
			$(this).remove();
	
			//递归删除子区域
			delSubArea(pid);
		});
	}
	
	function openPop(num){
        $("#covered").show();
        $("#poplayer").show();
        $("#poplayer").children(".zIndex").removeClass("zIndex");
        if(num==1){
            $(".hylb").show();
            $(".jglb").hide(); 
            $(".zflb").hide();
			$(".dqlb").hide();
			$(".hylb").addClass("zIndex");
        }else if(num==2){
            $(".hylb").hide();
            $(".jglb").show(); 
            $(".zflb").hide();
			$(".dqlb").hide();
			$(".jglb").addClass("zIndex");
			if($("#treeDemo").html()==""){
				var loading = layer.load();
					$.ajax({
						url:'${request.getContextPath()}/admin/manualEntry/getOrgLists.jhtml',
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
        }else if(num==3){
            $(".hylb").hide();
            $(".jglb").hide(); 
            $(".zflb").show();
			$(".dqlb").hide();
			$(".zflb").addClass("zIndex");
        }else if(num==5){
        	$(".hylb").hide();
            $(".jglb").hide();
		 	$(".zflb").hide();
			$(".dqlb").show();
			$(".dqlb").addClass("zIndex");
        }
    }
    
    function selUpstream1(obj){
      $.each($(".hylb .seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }
    
    function selUpstream2(obj){
      $.each($(".jglb .seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }
    
  	function selUpstream3(obj){
      $.each($(".zflb .seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }
    
    function selUpstream5(obj){
      $.each($(".dqlb .seleced"),function(){
          $(this).removeClass("seleced");
      });
      $(obj).addClass("seleced");
    }
    
    //确认行业选择
    function confirmSel1(){
      var seleced = $(".hylb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  var txt = seleced.text();
  	  $(".hy").next().text(txt);
  	  $(".hy").val(seleced.attr("id"));
    }
    
    //确认政府选择
    function confirmSel3(){
      var seleced = $(".zflb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  var txt = seleced.text();
  	  $(".zf").next().text(txt);
  	  $(".zf").val(seleced.attr("id"));
    }
    

  	//确认选择机构
	function confirmSel2() {
		closePop();
		var htext=$("#hideorg input").val()
		var hteid=$("#hideorg input").attr("id")
		 $(".jg").next("a").text(htext);
		 $(".jg").val(htext); 
	}
 
 
    //地区列表
    function confirmSel5(){
      var seleced = $(".dqlb .seleced");
      closePop();
      var area = $(seleced.find("label")).text(); 
  	  var txt = seleced.text();
  	  $(".dq").next().text(txt);
  	  $(".dq").val(seleced.attr("id"));
    }
    
    //关闭弹出框
    function closePop(){
        $("#covered").hide();
        $("#poplayer").hide();
    }
    
    
 
</script>
<title>手工录入添加</title>
</head>
<body>
	<#-- 弹出框 -->
<div id="covered"></div>
<div id="poplayer">
    <div class="borderBox hylb">
        <div class="titleFont1">
            <span>行业列表</span>
        </div>
        <div> </div>
        <div class="listBox">
        	<div>
        		<input id="searchInput1" class="inputSty" type="text" value="" style="width: 140px;"/>
        		<input id="searchBtn1" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
        	</div>
            <table cellpadding="0" cellspacing="0" id="searchTable1">
            <#list industryList as it>
                <tr>
                    <td level="1" id="${it.sysIndustryCode}" onclick="selUpstream1(this)">
                        <label>${it.sysIndustryName}</label>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel1()"/>
        </div>
    </div>
    <div class="borderBox jglb">
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
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel2()"/>
        </div>
    </div>
    <div class="borderBox zflb">
        <div class="titleFont1">
            <span>政府列表</span>
        </div>
        <div> </div>
        <div class="listBox">
        	<div>
        		<input id="searchInput3" class="inputSty" type="text" value="" style="width: 140px;"/>
        		<input id="searchBtn3" type="submit" value="搜 索" class="sureBtn" style="width: 70px; height: 30px; margin-top:10px; text-align: center;"/>
        	</div>
            <table cellpadding="0" cellspacing="0" id="searchTable3">
            <#list sysGoverList as it>
                <tr>
                    <td level="1" id="${it.sys_org_financial_code}" onclick="selUpstream3(this)">
                        <label>${it.sys_org_name}</label>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel3()"/>
        </div>
    </div> 
    <div class="borderBox dqlb">
        <div class="titleFont1">
            <span>地区列表</span>
        </div>
        <div class="listBox">
            <table cellpadding="0" cellspacing="0" id="searchTable5">
            <#list areaList as it>
                <tr>
                    <td level="1" id="${it.sysAreaCode}" onclick="selUpstream5(this)">
                        <#if (it.subArea?? && it.subArea?size > 0) >
							<div id="${it.sysAreaId}" name="0" class="open-shrink" onclick="openArea(this, ${it.sysAreaId})">
								<img src="${request.getContextPath()}/assets/images/open-shrink.gif"/>
							</div>
						</#if>
                        <span class="fll fontSize12">${it.sysAreaName}</span>
                    </td>
                </tr>
            </#list>
            </table>
        </div>
        <div class="btnBox">
            <input type="button" value="取 消" class="cancleBtn" onclick="closePop()" style="margin-right:0px"/>
            <input type="button" value="确 认" class="button sureBtn" onclick="confirmSel5()"/>
        </div>
    </div>
</div>
		<div div class="showListBox">
		<form  method="post" enctype="multipart/form-data" method="post" action="${request.contextPath}/admin/manualEntry/save.jhtml">
			<table width="70%" id="indexLastValueTable" cellpadding="0" cellspacing="0">
					<input type="hidden" name="indexId" value="${indexId}"/>
					<input type="hidden" name="majorId" value="${majorId}"/>
					<input type="hidden" id="YesNo" name="YesNo" value=""/>
					<input type="hidden" id="defaultId" name="defaultId" value="${mapSon.DEFAULT_INDEX_ITEM_ID}"/>
					<tr>
						<td  width='300' class='noBorderL firstTD'>社会统一信用代码</td>
						<td  width='400' class="secondTD">
							<input class="inputSty" type="text" id="codeCredit" name="codeCredit" value="${mapSon.CODE_CREDIT}" />
						</td>
					</tr>
					<tr>
						<td class='noBorderL firstTD'>组织机构代码</td>
						<td class="secondTD">
							<input class="inputSty" type="text" id="codeOrg" name="codeOrg" value="${mapSon.CODE_ORG}" />
						</td>
					</tr>
					<#list myIndexItemTbList as item>
						<tr>
							<td class='noBorderL firstTD'>${item.indexItemName}</td>
							<td  class="secondTD">
								<#if item.indexItemType==1>
									<input  autocomplete="off" class="laydate-icon inputSty fontSize12" type="text" name="indexItemValue" value="${myValueList[item_index]}" onclick="laydate({istime: false,max: laydate.now(),format: 'YYYY-MM-DD'})" />
								<#elseif item.indexItemType==3>
									<#if item.dicType==4>
										<select class="inputSty" name="indexItemValue"> 
												<#list dicContentList as d>
													<#if d.dicId==item.dicId>
													<option value="${d.dicContentCode}" <#if d.dicContentValue==myValueList[item_index]>selected</#if>>${d.dicContentValue}</option>
													</#if>
												</#list>
										</select>
									<#elseif item.dicType==5>
										<input class="inputSty dq" type="hidden" name="indexItemValue" value="${myValueList[item_index]}" />
										<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(5)">${myValueList[item_index]}</a>
									<#elseif item.dicType==3>
										<input class="inputSty zf" type="hidden" name="indexItemValue" value="${myValueList[item_index]}" />
										<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(3)">${myValueList[item_index]}</a>
									<#elseif item.dicType==2>
										<input class="inputSty jg" type="hidden" name="indexItemValue" value="${myValueList[item_index]}" />
										<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(2)">${myValueList[item_index]}</a>
									<#elseif item.dicType==1>
										<input class="inputSty hy" type="hidden" name="indexItemValue" value="${myValueList[item_index]}" />
										<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer" onclick="openPop(1)">${myValueList[item_index]}</a>
									<#else>
										<input class="inputSty" type="text" name="indexItemValue" value="${myValueList[item_index]}" />
									</#if>
								<#else>
									<input class="inputSty" type="text" name="indexItemValue" value="${myValueList[item_index]}" />
								</#if>
								<input class="inputSty" type="hidden" name="indexItemCode" value="${item.indexItemCode}" />
							</td>
						</tr>
					</#list>
					<input class="inputSty" type="hidden" id="authFileSwitch" name="authFileSwitch" value="${authFileSwitch}" />
					<#if authFileSwitch==1>
					<tr>
						<td class="noBorderL firstTD"><label class="mainOrange"> * </label>授权文件</td>
						<td class="secondTD"><input name="file" class="inputSty inlineBlock" type="file" value="浏览"><span class="fontSize12 paddingL5" style="color: rgb(56,165,226);">${fileError}</span></td>
					</tr>
					</#if>
			</table>
			<div class="showBtnBox">
				<input type="button" onclick="saveNo()" class="cancleBtn closeThisLayer" value="取 消" />
				<input type="button" onclick="save()" class="sureBtn" value="确 认" />
			</div>
		</form>
		</div>
	</body>
<script>
	var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
	$('.closeThisLayer').on('click', function(){
    	parent.layer.close(index); //执行关闭
	});
	
	//搜索按钮
	$("#searchBtn1").click(function(){
		var searchVal = $.trim($("#searchInput1").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable1");
		$.post("${request.getContextPath()}/admin/manualEntry/sysIndustrByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sysIndustryCode+"' onclick='selUpstream1(this)'><label>"+v.sysIndustryName+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput1").val(""));
			});
			layer.close(searchLoading);
		});
	});
	
	//搜索按钮
	$("#searchBtn2").click(function(){
		var searchVal = $.trim($("#searchInput2").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable2");
		$.post("${request.getContextPath()}/admin/manualEntry/sysOrgsByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sys_org_financial_code+"' onclick='selUpstream2(this)'><label>"+v.sys_org_name+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput2").val(""));
			});
			layer.close(searchLoading);
		});
	});
	
	//搜索按钮
	$("#searchBtn3").click(function(){
		var searchVal = $.trim($("#searchInput3").val());
		var searchLoading = layer.load();
		var tabel = $("#searchTable3");
		$.post("${request.getContextPath()}/admin/manualEntry/sysGoverByName.jhtml",{name:searchVal},function(data){
			tabel.html("");
			$.each(data,function(i,v){
				var tr = $("<tr></tr>");
				var td = $("<td id='"+v.sys_org_financial_code+"' onclick='selUpstream3(this)'><label>"+v.sys_org_name+"</label></td>");
				tr.append(td);
				tabel.append(tr);
				$.trim($("#searchInput3").val(""));
			});
			layer.close(searchLoading);
		});
	});
</script>	
</html>