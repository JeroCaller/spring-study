package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jerocaller.model.service.Business;

@Controller
public class MyController {
	
	@Autowired
	private Business business;
	
	@GetMapping("/")
	public String index() {
		return "/index";
	}
	
	@GetMapping("/userList")
	public String userList(Model model) {
		model.addAttribute("users", business.getUserAll());
		return "/userList";
	}
}
