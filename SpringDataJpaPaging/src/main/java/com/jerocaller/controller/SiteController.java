package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jerocaller.model.service.ServiceInter;

@Controller
@RequestMapping("/site")
public class SiteController {
	
	@Autowired
	@Qualifier("siteUserService")
	private ServiceInter siteUserService;
	
	@Autowired
	@Qualifier("userClassInfoService")
	private ServiceInter userClassInfoService;
	
	@GetMapping("")
	public String toIndex() {
		return "index";
	}
	
	@GetMapping("/userList")
	public String showAllUsers(Model model) {
		siteUserService.selectAll(model, "users");
		return "userList";
	}
	
	@GetMapping("/classList")
	public String showAllClasses(Model model) {
		userClassInfoService.selectAll(model, "classes");
		return "classList";
	}
}
