package com.workmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/taskPush")
public class TaskPushController {

	@RequestMapping("/index")
	public String index(){
		return "taskPush/list";
	}
	
	@RequestMapping("/add")
	public String add(){
		return "taskPush/add";
	}
}
