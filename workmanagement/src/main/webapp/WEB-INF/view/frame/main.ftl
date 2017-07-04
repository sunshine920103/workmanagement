<!DOCTYPE HTML>
<html>
<head>
<#include "/fragment/common.ftl"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<title>双公示信用信息系统</title>
</head>
<frameset rows="50,*" cols="*" frameborder="no" border="0" framespacing="0">
  <frame src="${request.getContextPath()}/admin/top.jhtml" name="topFrame" scrolling="No" noresize="noresize" id="topFrame" title="topFrame" />
  <frameset cols="201,*" frameborder="no" border="0" framespacing="0">
  	<frame src="${request.getContextPath()}/admin/left.jhtml" name="leftFrame" scrolling="No" noresize="noresize" id="leftFrame"/>
    <frameset rows="*" cols="*" frameborder="no" border="0" framespacing="0"  id="rightFrameset" name="rightFrameset">
      	<frame src="${request.getContextPath()}/admin/myPanel/index.jhtml" name="rightFrame" id="rightFrame" title="rightFrame" />
      </frameset>
  </frameset>
</frameset>
<noframes>
<body onload="">
</body>
</noframes>
</html>