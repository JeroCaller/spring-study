package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jerocaller.model.SiteService;

@Controller
public class SiteController {
	
	@Autowired
	private SiteService service;
	
	@GetMapping("/")
	public String goToIndex() {
		return "index";
	}
	
	@GetMapping("/users")
	public String showUsers(Model model) {
		model.addAttribute("users", service.selectAllUsers());
		return "userList";
	}
}
