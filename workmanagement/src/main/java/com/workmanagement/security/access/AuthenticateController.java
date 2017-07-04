package com.workmanagement.security.access;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
@Controller
public class AuthenticateController {
	
	@RequestMapping(value = "/login", method = { RequestMethod.POST, RequestMethod.GET })
	public String login(HttpServletRequest req, Date date) {
		return "login";
	}
	
	@RequestMapping(value = "/index", method = { RequestMethod.POST, RequestMethod.GET })
	public String index(HttpServletRequest req, Date date) {
		return "redirect:/login.jhtml";
	}
	
	@RequestMapping(value = "/denied", method = { RequestMethod.POST, RequestMethod.GET })
	public String denied(HttpServletRequest req, Date date) {
		return "denied";
	}
	
	@RequestMapping(value = "/logout", method = { RequestMethod.GET })
	public String logout(HttpServletRequest req) {
		req.getSession().invalidate();
		return "redirect:login.jhtml";
	}
	@RequestMapping(value = "/exceptionPage")
	public String exceptionPage() {
		return "404/pleaseWait";
	}
	@RequestMapping(value = "/errorPage")
	public String errorPage() {
		return "404/pleaseWait";
	}
	
}
