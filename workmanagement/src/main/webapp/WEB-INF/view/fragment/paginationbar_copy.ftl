<div class='pagination pagination-centered'>
	<ul>
		<#if page.isFirst()>
		<li class="disabled">
			<span>首页</span>
		</li>
		<#else>
		<li>
			<a href="javascript:void(0);" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=0')">首页</a>
		</li>
		</#if>

		<#if page.hasPreviousPage()>
		<li>
			<a href="javascript:void(0);" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getPrevPageOffset()}')" title='上一页'>上一页</a>
		</li>
		<#else>
		<li class="disabled">
			<span>上一页</span> 
		</li>
		</#if>
		
		<#if (page.getShowFirstPageNo()-1 > 0)>
		<li>
	        <span>...</span>
	    </li>
	    </#if>
		
		<#list page.getShowFirstPageNo()..page.getShowLastPageNo() as pageNo>
		<#if (page.getCurrentPage() == pageNo)>
		<li class="active">
	        <span>${pageNo}</span>
	    </li>
		<#else>
	    <li>
	        <a href="javascript:void(0);" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${(pageNo-1) * page.getPageSize()}')">
			${pageNo}
			</a>
	    </li>
		</#if>
	    </#list>
	    
	    <#if (page.getShowLastPageNo()+1 <= page.getTotalPage())>
	    <li>
	        <span>...</span>
	    </li>
	    </#if>
		
		<#if page.hasNextPage()>
		<li>
			<a href="javascript:void(0);" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getNextPageOffset()}')" title='下一页'>下一页</a>
		</li>
		<#else>
		<li class="disabled">
			<span>下一页</span> 
		</li>
		</#if>
		<#if page.isLast()>
		<li class="disabled">
			<span>末页</span>
		</li>
		<#else>
		<li>
			<a href="javascript:void(0);" onclick="paginationLink('${request.getContextPath() + page.url}?pageOffset=${page.getLastPageOffset()}')">末页</a>
		</li>
		</#if>
	</ul>
</div>