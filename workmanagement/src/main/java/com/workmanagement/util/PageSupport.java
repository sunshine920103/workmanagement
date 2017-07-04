package com.workmanagement.util;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

public class PageSupport {
	public final static String PAGE_SIZE = "pageSize";//每页显示数
	public final static String PAGE_OFFSET = "pageOffset";//第几页
	public final static int DEFAULT_PAGE_SIZE = 10;
	public final static int FIRST_PAGE = 0;//开始页数

	public final static String REQUEST_PAGE_SUPPORT = "page";

	/**
	 * 修改了每页数量
	 */
	public final static String CHANGE_PAGE_CODE = "changePageCode";

	private int pageSize;//每页显示数
	private int pageOffset;//第几页
	private long totalRecord;//总页数
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(int pageOffset) {
		this.pageOffset = pageOffset;
	}

	public long getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(long totalRecord) {
		this.totalRecord = totalRecord;
	}

	/**
	 * 获取当前的页数
	 *
	 * @return
	 */
	public int getCurrentPage() {
		return pageOffset;
	}

	/**
	 * 获取最大的页数
	 *
	 * @return
	 */
	public int getCurrentMaxOffset() {
		int maxPageOffset = pageOffset + pageSize;
		return maxPageOffset > totalRecord ? (int) totalRecord : maxPageOffset;
	}

	/**
	 * @return 下一页的offset
	 */
	public int getNextPageOffset() {
		//MYSQL
		/*return (getCurrentPage()) * pageSize;*/
		//DB2
		return (getCurrentPage()) + 1;
	}

	/**
	 * @return 上一页的offset
	 */
	public int getPrevPageOffset() {
		//MYSQL
		/*return (getCurrentPage() - 2) * pageSize;*/
		//DB2
		return (getCurrentPage()) - 1;
	}

	/**
	 * @return 最后一页的offset
	 */
	public int getLastPageOffset() {
		//MYSQL
		/*return getTotalPage() == 0 ? 0 : (getTotalPage() - 1) * pageSize;*/
		//DB2
		return getTotalPage() - 1;
	}

	/**
	 * @return 总页数
	 */
	public int getTotalPage() {
		if (totalRecord % pageSize == 0) {
			return (int) (totalRecord / pageSize);
		} else {
			return (int) (totalRecord / pageSize + 1);
		}
	}

	/**
	 * @return 是否有上一页
	 */
	public boolean hasPreviousPage() {
		return getPrevPageOffset() >= 0;
	}

	/**
	 * @return 是否有下一页
	 */
	public boolean hasNextPage() {
		return getNextPageOffset() <= getLastPageOffset();
	}

	/**
	 *
	 * @return 是否第一页
	 */
	public boolean isFirst() {
		return PageSupport.FIRST_PAGE == getPageOffset();
	}

	/**
	 * @return 是否最后一页
	 */
	public boolean isLast() {
		return getLastPageOffset() == getPageOffset();
	}

	public Integer getShowFirstPageNo() {
		int lp = getShowLastPageNo();
		int fp = Math.max(1, this.getCurrentPage() - 2);
		if (lp - fp < 4) {
			fp = lp - 4;
		}
		return fp <= 0 ? 1 : fp;
	}

	public Integer getShowLastPageNo() {
		int tcp = this.getCurrentPage() - 2;
		int d = 0;
		if (tcp <= 0) {
			d = Math.abs(tcp) + 1;
		}

		Integer lastPN = Math.min(this.getTotalPage(), this.getCurrentPage() + 2 + d);
		lastPN = lastPN == 0 ? 1 : lastPN;

		return lastPN;
	}

	/**
	 * 初始化并返回分页PageSupport对象
	 *
	 * @param request
	 * @return
	 */
	public static PageSupport initPageSupport(HttpServletRequest request) {
		PageSupport pageSupport = new PageSupport();
		String pageSize = request.getParameter(PageSupport.PAGE_SIZE);
		pageSupport.setPageSize(pageSize == null || pageSize.equals("") ? PageSupport.DEFAULT_PAGE_SIZE : Integer.valueOf(pageSize));

		String offset = request.getParameter(PageSupport.PAGE_OFFSET);
		pageSupport.setPageOffset(offset == null || offset.equals("") ? PageSupport.FIRST_PAGE : Integer.valueOf(offset));

		pageSupport.setUrl(request.getServletPath());

		request.setAttribute(PageSupport.REQUEST_PAGE_SUPPORT, pageSupport);
		return pageSupport;
	}

	/**
	 * 初始化查询请求参数中的分页信息
	 *
	 * @param request
	 * @param requestKey
	 * @return pageSupport 分页对象
	 */
	public static PageSupport initPageSupport(HttpServletRequest request, String requestKey){
		PageSupport pageSupport = new PageSupport();
		String pageSize = request.getParameter(PageSupport.PAGE_SIZE);
		String changePageCode = request.getParameter(PageSupport.CHANGE_PAGE_CODE);
		int iPageSize = StringUtils.isBlank(pageSize) ? PageSupport.DEFAULT_PAGE_SIZE : Integer.valueOf(pageSize);


		pageSupport.setPageSize(iPageSize);

		if (StringUtils.isBlank(changePageCode)) {
			String pageCode = request.getParameter("pageCode");
			int ioffset = 1;
			if (!StringUtils.isBlank(pageCode)) {
				ioffset = (Integer.valueOf(pageCode) - 1) * iPageSize;
				ioffset = ioffset < 0 ? PageSupport.FIRST_PAGE : ioffset;
			} else
				ioffset = PageSupport.FIRST_PAGE;

			pageSupport.setPageOffset(ioffset);
		}

		request.setAttribute(requestKey, pageSupport);
		pageSupport.setUrl(request.getServletPath());
		return pageSupport;
	}

}
