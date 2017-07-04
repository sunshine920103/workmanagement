/**
 * 
 */
package com.workmanagement.json;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author Administrator
 * 
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class JsonResWrapper {

	private String status = ResponseStatus.OK;
	private Boolean flag = Boolean.TRUE;
	private String message = "操作成功";

	/**
	 * 总记录数
	 */
	private Integer totalRecord;

	/**
	 * 每页数量
	 */
	private Integer pageSize;

	/**
	 * 起始下标
	 */
	private Integer pageOffset;

	/**
	 * 响应数据
	 */
	private Object data;

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(Integer totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageOffset() {
		return pageOffset;
	}

	public void setPageOffset(Integer pageOffset) {
		this.pageOffset = pageOffset;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	@Override
	public String toString() {
		return "JsonResWrapper [status=" + status + ", flag=" + flag
				+ ", message=" + message + ", totalRecord=" + totalRecord
				+ ", pageSize=" + pageSize + ", pageOffset=" + pageOffset
				+ ", data=" + data + "]";
	}
	

}
