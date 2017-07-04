package com.workmanagement.model;

public class ZtreeVo {
	private String id;
	private String name;
	private String parent;
	
	public ZtreeVo() {
		super();
	}
	public ZtreeVo(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	public ZtreeVo(String id, String name, String parent) {
		super();
		this.id = id;
		this.name = name;
		this.parent = parent;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	
}
