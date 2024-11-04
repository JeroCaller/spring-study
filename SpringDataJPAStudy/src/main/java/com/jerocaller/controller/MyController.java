package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	@GetMapping("/userList2")
	public String userList(
			Model model, 
			@RequestParam("classNumber") int classNumber
	) {
		model.addAttribute("users", business.getUsersByClassNumberAndRecommByNotNull(classNumber));
		return "/userList";
	}
	
	@GetMapping("/userList3")
	public String userList(
			Model model, 
			@RequestParam("min") int minimum,
			@RequestParam("max") int maximum
	) {
		model.addAttribute("users", business.getUsersByMileageBetween(minimum, maximum));
		return "/userList";
	}
}
