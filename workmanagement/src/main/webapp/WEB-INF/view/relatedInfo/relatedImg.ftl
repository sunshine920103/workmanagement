<!DOCTYPE html>
<html>
	<head>
		<#include "/fragment/common.ftl"/>
	<script src="${request.getContextPath()}/assets/js/jquery-1.9.1.js"></script>
	<script src="${request.getContextPath()}/assets/js/echarts-all.js"></script>
	</head>
	<body>
		<div class="showListBox">
			<div id="main" style="height:400px;padding: 20px;"></div>
		
			
		
			<div class="showBtnBox" style="border-top: 1px solid #CCCCCC;padding-top:20px ;">
					<input class="cancleBtn closeThisLayer" type="button" value="关 闭"/>
			</div>
			</div>
		<script type="text/javascript">
	function  alond(nodesADD,linksADD){
		var myChart = echarts.init(document.getElementById('main'));
		option = {
		    title : {
		        text:'关联图谱',
		        subtext: '双公示信用信息系统',
		        x:'right',
		        y:'bottom'
		    },
	    tooltip : {
	        trigger: 'item',
	        formatter: '{a} : {b}'
	    },
	    toolbox: {
	        show : true,
	        feature : {
	            restore : {show: true},
	            magicType: {show: true, type: ['force', 'chord']},
	            saveAsImage : {show: true}
	        }
	    },
	    legend: {
	        x: 'left',
	        data:['投资关联','担保关联','高管关联']
	    },
	    series : [
	        {
	            type:'force',
	            name : "关联关系",
	            ribbonType: false,
	            categories : [
	                {
	                    name: '公司'
	                },
	                {
	                    name: '投资关联'
	                },
	                {
	                    name: '担保关联'
	                },
	                {
	                    name:'高管关联'
	                }
	            ],
	            itemStyle: {
	                normal: {
	                    label: {
	                        show: true,
	                        textStyle: {
	                            color: '#333'
	                        }
	                    },
	                    nodeStyle : {
	                        brushType : 'both',
	                        borderColor : 'rgba(255,215,0,0.4)',
	                        borderWidth : 1
	                    },
	                    linkStyle: {
	                        type: 'curve'
	                    }
	                },
	                emphasis: {
	                    label: {
	                        show: false
	                        // textStyle: null      // 默认使用全局文本样式，详见TEXTSTYLE
	                    },
	                    nodeStyle : {
	                        //r: 30
	                    },
	                    linkStyle : {}
	                }
	            },
	            useWorker: false,
	            minRadius : 15,
	            maxRadius : 25,
	            gravity: 1.1,
	            scaling: 1.1,
	            roam: 'move',
	            nodes:nodesADD,
	            links:linksADD
		        }
		    ]
		};
		myChart.setOption(option);
	}
//	var nodesADD=[{category:0, name: '乔布斯', value : 10, label: '乔布斯\n（主要）'},{category:1, name: '丽萨-乔布斯',value : 2},{category:1, name: '保罗-乔布斯',value : 3},{category:3, name: '克拉拉-乔布斯',value : 3},{category:3, name: '劳伦-鲍威尔',value : 7},{category:2, name: '史蒂夫-沃兹尼艾克',value : 5}];
//	var linksADD=[{source : '丽萨-乔布斯', target : '乔布斯', weight : 1, name: '女儿'},{source : '保罗-乔布斯', target : '乔布斯', weight : 2, name: '父亲'},{source : '克拉拉-乔布斯', target : '乔布斯', weight : 3, name: '母亲'},{source : '劳伦-鲍威尔', target : '乔布斯', weight : 2},{source : '史蒂夫-沃兹尼艾克', target : '乔布斯', weight : 3, name: '合伙人'}];
//  
    
    $(function(){
    	var url = "${request.getContextPath()}/admin/relatedInfo/relatedImgJson.jhtml";
    	$.post(url,function(result){
			var str1 = [];
			var str2 = [];
			
    		for (var i = 0; i < result.length; i++) {
    			var obj1 = new Object();
				var obj2 = new Object();
    			
	    		if(result[i].guanxi=="本企业"){
	    			obj1.category = 0;
	    			obj1.name = result[i].qymc;
	    			obj1.value = 10;
	    			obj1.label = result[i].qymc+"\n主要";
	    		}else if(result[i].guanxi=="投资关联"||result[i].guanxi=="资本构成"){
	    			obj1.category  = 1;
	    			obj2.source =obj1.name  = result[i].qymc+"\n("+result[i].zbd+")"+result[i].guanxi;
	    			obj1.value = 2;
	    			obj2.weight = i;
	    			obj2.name = result[i].guanxi;
	    		}else if(result[i].guanxi=="担保关联"){
	    			obj1.category  = 2;
	    			obj2.source =obj1.name  = result[i].qymc+"\n("+result[i].zbd+")"+result[i].guanxi;
	    			obj1.value = 2;
	    			obj2.weight = i;
	    			obj2.name = "担保关联";
	    		}else if(result[i].guanxi=="高管关联"){
	    			obj1.category  = 3;
	    			obj2.source =obj1.name  = result[i].qymc+"\n"+result[i].guanxi;;
	    			obj1.value = 2;
	    			obj2.weight = i;
	    			obj2.name ="高管关联";
	    		}
	    		
	    		obj2.target =result[result.length-1].qymc;
	    		str1.push(obj1);
	    		str2.push(obj2);
    		}
    		alond(str1,str2);
   		});
   		
   		
    	
    })
    $(function(){
		var index = parent.layer.getFrameIndex(window.name); //获取当前窗体索引
		$('.closeThisLayer').on('click', function(){
			if("${returnNum}" =="returnNum"){
						history.go(-2);
					}else{
		    			parent.layer.close(index); //执行关闭
		    		}
		});
	})
	</script>
	</body>
</html>
