<!DOCTYPE html>
<html>
<head>
<#include "/fragment/common.ftl"/>
    <script type="text/javascript">
        //IE下 背景全屏
        window.onresize = function () {
            $('.layui-layer-shade').height($(window).height());
        };
    </script>
		<title>指标列表</title>
	</head>
	<body>
		<form id="searchForm" method="post" action="${request.getContextPath()}/admin/indexTb/sysQuery.jhtml">
		</form>
		<!-- 默认获取的一个指标项 列表      -->
		<div class="eachInformationSearch">
		<div id="min" class="listBox">
			<table cellpadding="0" cellspacing="0">
			<caption class="titleFont1 titleFont1Ex">指标大类列表</caption>
				<tr class="firstTRFont">
					<td width="150">指标大类名称 </td>                                                                                                                                                                                                                                              </td>
					<td width="100">识别码</td>
					<td width="150">所属区域</td>
					<td width="80">指标状态</td>
					<td width="100">指标类型</td>
					<td width="140">操作</td>
				</tr>
				<#list list as li>
                <#if li.INDEX_NAME=='崇左市-行政处罚信息' || li.INDEX_NAME=='崇左市-行政许可信息'|| li.INDEX_NAME=='崇左市-基本信息'>
                    <tr>
                        <td width="" class="hide">${li.INDEX_ID}</td>
                        <td width="">${li.INDEX_NAME}</td>
                        <td width="">${li.INDEX_CODE}</td>
                        <td width="">${li.SYSAREANAME}</td>
                        <#if li.INDEX_USED == 0>
                            <td  width="">无效</td>
                        <#else>
                            <td  width="">有效</td>
                        </#if>
                        <#if li.INDEX_TYPE == 0>
                            <td width="">基本信息</td>
                        <#else>
                            <td width="">业务信息</td>
                        </#if>
                        <td width="">
                            <a class="changeFont fontSize12 hasUnderline cursorPointer" >查看</a>
                        <#if li.INDEX_CODE !="index_jbxx">
                            <a class="delFont fontSize12  hasUnderline " >置为${(li.INDEX_USED==0)?string("有效","无效")}</a>
                        <#else>

                        </#if>
                        </td>
                    </tr>
                </#if>
				</#list>
			</table>
			<#include "/fragment/paginationbar.ftl"/>
		</div>
		</div>
	</body>
</html>