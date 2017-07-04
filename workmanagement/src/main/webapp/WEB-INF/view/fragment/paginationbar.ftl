<style type="text/css">
	.margin15{margin: 15px;}
	.margin20{margin: 20px;}
	.bigdiv{float:right;color:#333;font-size:12px;margin:10px; 0px}
	.spanall{ margin-right:15px;font-size:12px;display: inline-block;*display:inline;*zoom:1;height: 20px; line-height: 20px; padding: 4px 10px;text-decoration: none;border: 1px solid #dadada;}
	.span1dis{ color:#dadada;} 
 	.span1none{cursor: pointer;color:#333;}
   
</style>
<div class="bigdiv">
			<span class="margin15">
				共
				${page.getTotalRecord()}
				条
			</span> 
			<span class="margin20">
				第  ${page.getCurrentPage()+1} / ${page.getTotalPage()} 页
			</span>
			
			<#if page.isFirst()>
				<span  class="span1dis spanall">
					<span style="display:inline-block;*display:inline;*zoom:1;line-height: 20px;">首页</span>
				</span>
			<#else> 
				<span   title='首页' onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=0')" class="spanall span1none">
					<span style="color: rgb(56,165,226);font-size:16px;">◀</span>
					<span style="display:inline-block;*display:inline;*zoom:1;line-height: 20px;">首页</span>
				</span>
			</#if>
			
			<#if page.hasPreviousPage()>
				<span   onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getPrevPageOffset()}')" title='上一页'class="spanall span1none">上一页</span>
			<#else>
				<span    class="span1dis spanall">上一页</span>
			</#if>
			
			<#if page.hasNextPage()>
				<span  onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getNextPageOffset()}')" title='下一页' class="spanall span1none">下一页</span>
			<#else>
				<span    class="span1dis spanall">下一页</span>
			</#if>
			
			<#if page.isLast()>
				<span    class="span1dis spanall">
					<span style="display:inline-block;*display:inline;*zoom:1;line-height: 20px;">尾页</span>
				</span>
			<#else>
				<span href="javascript:void(0)" title="尾页" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getLastPageOffset()}')" class="spanall span1none">
					<span style="color: rgb(56,165,226);font-size:16px;">▶</span>
					<span style="display:inline-block;*display:inline;*zoom:1;line-height: 20px;">尾页</span>
				</span>
			</#if>
			
			<select style="cursor: pointer; width:55px;height:30px;font-size:12px;border:1px solid #dadada;margin-top: -6px;" class="button" onchange="paginationLink('${request.getContextPath() + page.url}?pageOffset='+this.value)">
				<#list 1..page.getTotalPage() as pageNo> 
					<!--DB2-->  
					<option <#if (page.getCurrentPage()+1==pageNo)>selected=selected</#if> value="${(pageNo-1)}">${pageNo}页</option>
				</#list>
			</select>
		</div>
