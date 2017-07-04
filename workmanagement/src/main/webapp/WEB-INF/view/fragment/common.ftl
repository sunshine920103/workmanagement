<meta charset="UTF-8" />
<meta content="text/html; charset=UTF-8" http-equiv="Content-Type"/>
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;IE=EDGE">
<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
<script src="${request.getContextPath()}/assets/js/common.js"></script>
<script src="${request.getContextPath()}/assets/js/alertM.js"></script>
<script src="${request.getContextPath()}/assets/js/utils.js"></script> 
<link rel="stylesheet" href="${request.getContextPath()}/assets/css/style.css" />
<link rel="stylesheet" href="${request.getContextPath()}/assets/css/newCommon.css" />
<script src="${request.getContextPath()}/assets/js/lay/laydate/laydate.js"></script> 
<link rel="stylesheet" href="${request.getContextPath()}/assets/js/lay/laydate/need/laydate.css" />
<script src="${request.getContextPath()}/assets/js/lay/layer/layer.js"></script> 
<script src="${request.getContextPath()}/assets/js/layer/mobile/layer.js"></script> 
<link rel="stylesheet" href="${request.getContextPath()}/assets/js/layer/mobile/need/layer.css" />
<script src="${request.getContextPath()}/assets/js/layer/layer.js"></script> 
<script src="${request.getContextPath()}/assets/js/setLayer.js"></script>
<link rel="stylesheet" type="text/css"  href="${request.getContextPath()}/assets/js/zTree_v3-master/css/zTreeStyle.css"/>
<script src="${request.getContextPath()}/assets/js/zTree_v3-master/js/jquery.ztree.all.min.js"></script>
<script src="${request.getContextPath()}/assets/js/organd.js"></script>
<script language="javascript" type="text/javascript">  
 	//禁用Enter键表单自动提交  
        document.onkeydown = function(event) {  
            var target, code, tag;  
            if (!event) {  
                event = window.event; //针对ie浏览器  
                target = event.srcElement;  
                code = event.keyCode;  
                if (code == 13) {  
                    tag = target.tagName;  
                    if (tag == "TEXTAREA") { return true; }  
                    else { return false; }  
                }  
            }  
            else {  
                target = event.target; //针对遵循w3c标准的浏览器，如Firefox  
                code = event.keyCode;  
                if (code == 13) {  
                    tag = target.tagName;  
                    if (tag == "INPUT") { return false; }  
                    else { return true; }  
                }  
            }  
        };  
</script> 