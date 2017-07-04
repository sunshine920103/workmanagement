<!DOCTYPE html>
<html>
<head>
    <title>我的面板</title>
<#include "/fragment/common.ftl"/>
</head>
<body>

<div>
    <!--系统消息开始-->
    <div class="sysInfoBox marginLR30 marginT20">
        <div class="leftPart solueFloat" style="position:relative;">
        	<div class="titleFont1 paddingR10"><span>信息</span> <a class="fontSize12 changeFont floatRight  cursorPointer" style="position:absolute;right:10px;top:0px;"
                        onclick="setLayer('更多系统消息','${request.getContextPath()}/admin/myPanel/moreSysInfo.jhtml')" >更多...</a>
        	</div>
            <table cellpadding="0" cellspacing="0">
                <tr>
                    <td class="textLeft" width="100">菜单名</td>
                    <td class="textRight" width="100">登录名</td>
                    <td class="textRight" width="100">时间</td>
                </tr>
            <#list logList as ll>
                <tr>
                    <td class="textLeft"><a class="menuName fontSize12"  onmouseover="if($(this).text().split('').length>7){layer.tips('${ll.sysUserLogMenuName}',this,{time: 0});}" onmouseout="layer.closeAll('tips');">${ll.sysUserLogMenuName}</a></td>
                    <td class="textRight"><a class="timer fontSize12" onmouseover="if($(this).text().split('').length>10){layer.tips('${ll.sysUserLogUserName}',this,{time: 0});}" onmouseout="layer.closeAll('tips');">${ll.sysUserLogUserName}</a></td>
                    <td class="textRight"><a class="timer fontSize12" onmouseover="if($(this).text().split('').length>10){layer.tips('${(ll.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss")!)}',this,{time: 0});}" onmouseout="layer.closeAll('tips');">${(ll.sysUserLogTime?string("yyyy-MM-dd HH:mm:ss")!)}</a></td>
                </tr>
            </#list>
            </table>
            <table cellspacing="0" cellpadding="0">
            </table>
        </div>
        <div class="rightPart solueFloat" style="padding: 0;">
            <div >
                <div class="titleFont1" style="border-bottom:1px solid #DADADA">信息 </div>
              
                <div class="loginInfo" style="padding: 15px 5px;float: left; width: 100%;margin-left: 10%;">
                    <p><span class="leftCol">登&nbsp;&nbsp;录&nbsp;&nbsp;账&nbsp;&nbsp;号&nbsp;:</span><span
                            class="rightCol">${user.username}</span></p>
                    <p><span class="leftCol">所&nbsp;&nbsp;在&nbsp;&nbsp;机&nbsp;&nbsp;构&nbsp;:</span><span
                            class="rightCol">${user.sys_user_org_name}</span></p>
                    <p><span class="leftCol">本&nbsp;次&nbsp;登&nbsp;录IP:</span><span
                            class="rightCol">${user.sys_user_login_ip}</span></p>
                    <p><span class="leftCol">本次登录时间: </span><span
                            class="rightCol">${(user.sys_user_login_time?string("yyyy-MM-dd HH:mm:ss"))!}</span></p>
                    <p><span class="leftCol">上&nbsp;次&nbsp;登&nbsp;录IP:</span><span
                            class="rightCol">${user.sys_user_last_login_ip}</span></p>
                    <p><span class="leftCol">上次登录时间:</span><span
                            class="rightCol">${(user.sys_user_last_login_time?string("yyyy-MM-dd HH:mm:ss"))!}</span>
                    </p>
                </div>
            </div>
        </div>
    </div>
    <!--系统消息结束-->
    <div class="eachList margin60301030 clearFloat"  style="margin-right: 0px;border:none;padding-top:10px">
	    <#if dceNum gt 0>
		   <a onclick="setLayer('企业二码合并消息','${request.getContextPath()}/admin/myPanel/comPanyShow.jhtml');$(this).parent().remove()" class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"><span class="solueFloat">企业二码合并消息通知，当前有<span style="color:rgb(255,0,0)">${dceNum}</span>条信息未查看</span></a>
		 </#if>
	 </div>
    <!--异议处理开始-->
<#if yycl gt 0 && (type==7||type==8)>

    <div class="eachList margin60301030 clearFloat" style="margin-right: 0px;">
        <form id="searchForm" method="post"></form>
        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1"><span class="solueFloat">异议处理<span class="noUnderline changeFont"
                                                                           href="">${yycl}</span>条</span></caption>
            <div style="text-align:right;position: absolute;right: 10px;top: 0px;display: none;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td width="30">序号</td>
                    <td width="250">企业二码</td>
                    <td width="150">企业名称</td>
                    <td width="100">指标大类</td>
                    <td width="150">报送机构</td>
                    <td width="100">报送时间</td>
                    <td width="80">状 态</td>
                    <td width="100">操作</td>
                </tr>

                <span class="span1">
                    <#list sysOperateList as sol>
                        <#if sol_index < 5  >
                            <tr>
					            	<td>${(1 + sol_index) + (page.getPageSize() * page.getCurrentPage())}</td>
					            		<td >${sol.codeCredit}/${sol.codeOrg}</td>
					            		<td>${sol.jbxxQimc}</td>	
										<td>${sol.indexName}</td>
										<td>${sol.sysReportOrgName}</td>
										<td>${(sol.sysOperateTime?string("yyyy-MM-dd HH:mm:ss"))!} </td>
										<td>
                                            <#if sol.sysOperateStatus==1>
                                                处理中
                                            <#elseif sol.sysOperateStatus==2>
                                                已处理
                                            <#else>
                                                待处理
                                            </#if>
										</td>
                                <#if OrgId == sol.reportOrgId && sol.sysOperateStatus==1>
                                    <td>
											<a class="inlineBlock changeFont fontSize12 hasUnderline cursorPointer"
                                               onclick="setLayer('异议处理','${request.getContextPath()}/admin/adminObj/revise.jhtml?sysOperateId=${sol.sysOperateId}&dtbId=${sol.dataId}&reportOrgId=${sol.reportOrgId}&recordDate=${(sol.recordDate?string("yyyy-MM-dd"))!}&defaultId=${sol.defaultIndexItemId}&operateTime=${(sol.sysOperateTime?string("yyyy-MM-dd"))!}&sysOrgId=${sol.sysOrgId}&indexId=${sol.indexItemId}&qb=1')">异议处理</a>
										</td>
                                <#else>
                                    <td>
											</td>
                                </#if>
									</tr>
                        </#if>
                        <#if sol_index == 5>
                            <tr>
										<td colspan="8" style="text-align:center;"><a
                                                class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                                                onclick="setLayer('司法信息','${request.getContextPath()}/admin/myPanel/myPanel.jhtml')">更 多...</a></td>
									</tr>
                        </#if>
                    </#list>
								</span>
            </table>
        </div>
    </div>
</div>
</#if>
<!--异议处理结束-->


<#if menu?index_of("数据报送")!=-1>
<!--我的任务列表开始-->
    <#if num gt 0>
    <div class="eachList margin40301030 clearFloat" style="margin-right: 0px;">
        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1">未完成任务列表（未完成<span class="noUnderline changeFont" href="">${num}</span>条）
            </caption>
            <div style="text-align:right;display:none;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>

            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td class="noBorderL" style="width:6%;">编号</td>
                    <td style="width:35%;">任务名称</td>
                    <td style="width:25%;">截止时间</td>
                    <td style="width:14%;">任务状态</td>
                </tr>
                <#list report as r>
                    <#if r_index < 5 >
                        <tr>
                            <td class="noBorderL">${r_index+1}</td>
                            <td>${r.reportTaskPushSetName}</td>
                            <td>${(r.reportTaskPushListEndTime?string("yyyy-MM-dd"))!}</td>

                            <#if r.reportTaskPushStatus == 0>
                                <td class="delFont">未完成</td>
                            </#if>
                            <#if r.reportTaskPushStatus == 1>
                                <td class="delFont">已完成</td>
                            </#if>
                            <#if r.reportTaskPushStatus == 2>
                                <td class="delFont">逾期</td>
                            </#if>
                        </tr>
                    </#if>
                </#list>
                <tr>
                    <td colspan="6" style="text-align:center;"><a
                            class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                            onclick="setLayer('我的任务','${request.getContextPath()}/admin/myPanel/other3.jhtml')">更
                        多...</a></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="eachList margin40301030 clearFloat" style="margin-right: 0px;">
        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1">行政许可情况 <span class="noUnderline changeFont" href="">${num}</span>条
            </caption>
            <div style="text-align:right;display:none;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>

            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td class="noBorderL" style="width:6%;">编号</td>
                    <td style="width:35%;">机构</td>
                    <td style="width:25%;">数据量</td>
                    <td style="width:14%;">更新时间</td>
                </tr>
                <#list report as r>
                    <#if r_index < 5 >
                        <tr>
                            <td class="noBorderL">${r_index+1}</td>
                            <td>崇左市质监局</td>
                            <td>${r_index+18}</td>
                            <td>${(r.reportTaskPushListEndTime?string("yyyy-MM-dd"))!}</td>
                        </tr>
                    </#if>
                </#list>
                <tr>
                    <td colspan="6" style="text-align:center;"><a
                            class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                            onclick="window.location.href='${request.getContextPath()}/admin/reportedDataList/list.jhtml'">更
                        多...</a></td>
                </tr>
            </table>
        </div>
    </div>
    <div class="eachList margin40301030 clearFloat" style="margin-right: 0px;">
        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1">行政处罚情况 <span class="noUnderline changeFont" href="">${num}</span>条
            </caption>
            <div style="text-align:right;display:none;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>

            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td class="noBorderL" style="width:6%;">编号</td>
                    <td style="width:35%;">机构</td>
                    <td style="width:25%;">数据量</td>
                    <td style="width:14%;">更新时间</td>
                </tr>
                <#list report as r>
                    <#if r_index < 5 >
                        <tr>
                            <td class="noBorderL">${r_index+1}</td>
                            <td>崇左市交通局</td>
                            <td>${r_index+10}</td>
                            <td>${(r.reportTaskPushListEndTime?string("yyyy-MM-dd"))!}</td>
                        </tr>
                    </#if>

                </#list>
                <tr>
                    <td colspan="6" style="text-align:center;"><a
                            class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                            onclick="setLayer('行政处罚情况','${request.getContextPath()}/admin/myPanel/other.jhtml')">更
                        多...</a></td>
                </tr>
            </table>
        </div>
    </div>
    </#if>
    
    
    
   
    
</#if>
<!--我的任务列表结束-->
<#if menu?index_of("信用报告查询") != -1||menu?index_of("企业名单筛选") != -1||menu?index_of("重点企业监测") != -1||menu?index_of("信用评分查询") != -1||menu?index_of("关联信息查询") != -1||menu?index_of("统计报表查询") != -1||menu?index_of("定制产品查询") != -1>


<!--贷款逾期情况开始-->
    <#if yhdk gt 0>
    <div class="eachList margin60301030 clearFloat" style="margin-right: 0px;">
        <form id="searchForm" method="post"></form>

        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1"><span class="solueFloat">贷款逾期情况（本机构客户在其他机构最新的逾期情况）  <span
                    class="noUnderline changeFont" href="">${yhdk}</span>条</span></caption>
            <div style="text-align:right;display:none;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td style="width:60;" class="noBorderL">编号</td>
                    <td style="width:300;">信用码/机构码</td>
                    <td style="width:300;">企业名称</td>
                    <td style="width:250;">到期时间</td>
                </tr>
                <span class="span1">
                    <#list indexYhdkList as yhdk>

                        <#if yhdk_index < 5 >
                            <tr>
									<td class="noBorderL"> ${yhdk_index+1} </td>
									<td>${yhdk.CODE_CREDIT}/${yhdk.CODE_ORG}</td>
									<td>${yhdk.INDEX_JBXX_QYMC}</td>
									<td>${yhdk.INDEX_YXDK_DQR}</td>
								</tr>
                        </#if>
                        <#if yhdk_index == 5>
                            <tr>
									<td colspan="6" style="text-align:center;"><a
                                            class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                                            onclick="setLayer('贷款逾期情况','${request.getContextPath()}/admin/myPanel/other1.jhtml')">更 多...</a></td>
								</tr>
                        </#if>
                    </#list>
							</span>
            </table>
        </div>
    </div>
    </#if>
<!--贷款逾期情况结束-->
<!--行政处罚情况开始-->
    <#if xzcf gt 2000>
    <div class="eachList margin60301030 clearFloat" style="margin-right: 0px;">
        <form id="searchForm" method="post"></form>
        <div class="titleFont1 CSH-k" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1"><span class="solueFloat">行政处罚情况（本机构客户在其他机构最新的行政处罚情况）<span
                    class="noUnderline changeFont" href="">${xzcf}</span>条</span></caption>
            <div style="text-align:right;display: none; position: absolute;right: 5px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
            <div style="text-align:right; position: absolute;right: 5px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12 expanded">展 开&nbsp;<img class="pointImg"
                                                                                   src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                                   style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td style="width:60;" class="noBorderL">编号</td>
                    <td style="width:250;">信用码/机构码</td>
                    <td style="width:250;">企业名称</td>
                    <td style="width:300;">处罚部门</td>
                    <td style="width:300;">日期</td>
                </tr>
                <span class="span1">
                    <#list indexXzcfxxList as x>
                        <#if x_index <5 >
                            <tr>
								<td class="noBorderL">${x_index+1}</td>
								<td>${x.CODE_CREDIT}/${x.CODE_ORG}</td>
								<td>${x.INDEX_JBXX_QYMC}</td>
								<td>${x.INDEX_XZCFXX_CDCFBM}</td>
								<td>
                                ${(x.INDEX_XZCFXX_CFSJ?string("yyyy-MM-dd"))!}</td>
							</tr>
                        </#if>
                        <#if x_index == 5>
                            <tr>
							<td colspan="6" style="text-align:center;"><a
                                    class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                                    onclick="setLayer('行政处罚情况','${request.getContextPath()}/admin/myPanel/other.jhtml')">更 多...</a></td>
						</tr>
                        </#if>
                    </#list>
						</span>
            </table>
        </div>
    </div>
    </#if>
<!--行政处罚情况结束-->

<!--司法信息开始-->
    <#if sfxx gt 2000>
    <div class="eachList margin60301030 clearFloat" style="margin-right: 0px;">
        <form id="searchForm" method="post"></form>
        <div class="CSH-k titleFont1" style="position: relative;">
            <input type="hidden" class="hiddenVal" value="1"/>
            <caption class="titleFont1"><span class="solueFloat">司法信息（本机构客户在其他机构最新的司法信息）<span
                    class="noUnderline changeFont" href="">${sfxx}</span>条</span></caption>
            <div style="text-align:right;display:none;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">收 缩&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-up.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
            <div style="text-align:right;position: absolute;right: 10px;top: 0px;">
                <a style="color:#4898d5" class="fontSize12">展 开&nbsp;<img class="pointImg"
                                                                          src="${request.getContextPath()}/assets/images/my-p-down.png"
                                                                          style="height:12px; padding:0;"></a>
            </div>
        </div>
        <div class="HShow">
            <table cellpadding="0" cellspacing="0">
                <tr class="firstTRFont">
                    <td style="width:5%;" class="noBorderL">编号</td>
                    <td style="width:20%;">信用码/机构码</td>
                    <td style="width:25%;">企业名称</td>
                    <td style="width:20%;">案由</td>
                    <td style="width:20%;">受理法院</td>
                    <td style="width:10%;">日期</td>
                </tr>

                <span class="span1">
                    <#list indexSfxxList as sfxx>
                        <#if sfxx_index <5 >
                            <tr>
											<td class="noBorderL">${sfxx_index+1}</td>
											<td>${sfxx.CODE_CREDIT}/${sfxx.CODE_ORG}</td>
											<td>${sfxx.INDEX_JBXX_QYMC}</td>
											<td>${sfxx.INDEX_SFXX_AY}</td>
											<td>${sfxx.INDEX_SFXX_LAFY}</td>
											<td>${(sfxx.INDEX_SFXX_LARQ?string("yyyy-MM-dd")!)}</td>
											</tr>
                        </#if>
                        <#if sfxx_index == 5>
                            <tr>
										<td colspan="6" style="text-align:center;"><a
                                                class="fontSize12 changeFont  paddingR10 cursorPointer solueFloat"
                                                onclick="setLayer('司法信息','${request.getContextPath()}/admin/myPanel/other2.jhtml')">更 多...</a></td>
									</tr>
                        </#if>
                    </#list>
								</span>
            </table>
        </div>
    </div>
    </#if>
</#if>
<!--司法信息结束-->

</body>
<script type="text/javascript"> 

	
        //如果是第一次登录 firstBlood判定用户是否第一次登录 
   		$.post("${request.getContextPath()}/admin/myPanel/getPwd.jhtml",function(data){
			if(data){
				 layer.confirm("为了您的账号安全，请您修改密码", { btn: ["确定"],closeBtn: 0, },function () { 
                        location.href="${request.getContextPath()}/admin/sysPwd/changePassword.jhtml" 
                 })
			}
		})
    function objManage(obj, recordDate, defaultIndexItemId, operateTime, indexId, sysOrgId, reportOrgId, dtbId) {
        location.href = "${request.getContextPath()}/admin/adminObj/revise.jhtml?indexId=" + indexId + "&sysOrgId=" + sysOrgId + "&operateTime=" + operateTime + "&defaultId=" + defaultIndexItemId + "&recordDate=" + recordDate + "&reportOrgId=" + reportOrgId + "&dtbId=" + dtbId;

    }
    
    function queding(id){
    		var tip = "确定合并吗?";
				layer.alert(tip, {
					shade:0.3,
				    btn: ['确定', '取消'],
				    yes: function(index){
				    	layer.close(index);
				    	//显示正在操作弹窗
						var option_index = wait();
				    	var url = "${request.getContextPath()}/admin/codeCombine/yes.jhtml";
						$.post(url,{id:id},function(data){
							//关闭弹窗
							close_wait(option_index);
							
							layer.alert(data.message,{
								icon:data.flag?1:2,
								shade:0.3
							});
						});
				  	}
			  	});
				return false;
    }

    $(window).load(function () {
        for (var i = 0; i < $(".CSH-k").length; i++) {
            $(".HShow").hide();
            $(".CSH-k").eq(i).click(function () {
                if ($(this).find(".hiddenVal").val() == "1") {
                    $(this).children("div").eq(0).show();
                    $(this).children("div").eq(1).hide();
                    $(this).find(".hiddenVal").val("0");
                    $(this).parent().children(".HShow").slideDown();
                }
                else {
                    $(this).children("div").eq(1).show();
                    $(this).children("div").eq(0).hide();
                    $(this).find(".hiddenVal").val("1");
                    $(this).parent().children(".HShow").slideUp();
                }
            })

        }
    })
    
     $(function(){
			var i=0
			$(".orgid").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getOrgName.jhtml",{id:orgId},function(data1){
						 $(".orgid").eq(i).text(data1)
						 i++
					})
			})
    })
      $(function(){
			var i=0
			$(".code").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getcode.jhtml",{id:orgId},function(data1){
						 $(".code").eq(i).text(data1)
						 i++
					})
			})
    })
 	$(function(){
			var i=0;
			$(".credit").each(function(){
					var orgId = $(this).text();
					$.post("${request.getContextPath()}/admin/menuAdd/getcride.jhtml",{id:orgId},function(data1){
						 $(".credit").eq(i).text(data1)
						 i++
					})
			})				
    })
    $(function(){

    	for (var i = 0; i < $(".timer").length; i++) {
    		if($(".timer").eq(i).text().length>10){
    			$(".timer").eq(i).text($(".timer").eq(i).text().substring(0,10));
    			$(".timer").eq(i).html($(".timer").eq(i).html()+'…');
    		}
    	}
    	for (var i = 0; i < $(".menuName").length; i++) {
    		if($(".menuName").eq(i).text().length>10){
    			$(".menuName").eq(i).text($(".menuName").eq(i).text().substring(0,7));
    			$(".menuName").eq(i).html($(".menuName").eq(i).html()+'…');
    		}
    	}
    })
</script>
</html>
