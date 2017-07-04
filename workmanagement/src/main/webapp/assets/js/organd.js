 //机构类机构树结构 
function orgtype(zndes){
	        var setting = {
				view: {
					showIcon: showIconForTree
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: { onClick: onClick}
			}; 
			 
			var zNodes =zndes; 
		 	
		 	function showIconForTree(treeId, treeNode) {
			return !treeNode.isParent;
		};
		function onClick(e,treeId,treeNode,clickFlag){ 
			var str=treeNode.id
				
			if(str.indexOf("A")==0){ 
				var num=treeNode.tId
				$("#"+num).children(".curSelectedNode").removeClass("curSelectedNode");
				
			  }else{
			  	  	$("#hideorg input").remove();
	           		$("#hideorg").append("<input type='hidden' name='orgIds' value='"+treeNode.name+"'  id='"+treeNode.id+"'>") 
			  }
		}
		$(document).ready(function(){
			$.fn.zTree.init($("#treeDemo"), setting, zNodes);
		});
}


 //机构类机构树结构 复选框
function orgtypeZ(zndes){
	        var setting = {
				check: {
					enable: true,
					chkStyle: "checkbox",
					chkboxType: { "Y": "s", "N": "s" }
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: { onClick: onClick,onCheck: onCheck } }; 
			
	
			var zNodes =zndes;
			$(document).ready(function(){
				$.fn.zTree.init($("#treeDemo"), setting, zNodes);
			});
			
			
			function onCheck(e,treeId,treeNode,clickFlag){
	            var treeObj=$.fn.zTree.getZTreeObj("treeDemo"),
	            nodes=treeObj.getCheckedNodes(true),
	            v="";
	            $("#hideorg span").remove(); 
				for(var i=0;i<nodes.length;i++){	
					v+=nodes[i].name + ",";
					 var str=nodes[i].id
					if(str.indexOf("A")==-1){   
			        	$("#hideorg").append("<span class='org_name'><input class='org_name' type='hidden' name='orgIds' value='"+nodes[i].id+"'  id='"+nodes[i].id+"'>"+nodes[i].name+"</span>") //获取选中节点的值
			       } 
			    }
           	}
         	function onClick(e,treeId,treeNode,clickFlag){ 
				var str=treeNode.id 
				if(str.indexOf("A")==0){ 
					var num=treeNode.tId
					$("#"+num).children(".curSelectedNode").removeClass("curSelectedNode"); 
				}else{
				  	  	$("#hideorg span").remove();
		           		$("#hideorg").append("<span class='org_name' style='font-size:12px;'><input class='org_name' type='hidden' name='orgIds' value='"+treeNode.id+"'  id='"+treeNode.id+"'>"+treeNode.name+"</span>") 
				}
			}
}