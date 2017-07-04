package com.workmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/refresh")
public class refreshController {
	
	@RequestMapping("/list")
	public String list(){
		return "refresh";
	}
}
