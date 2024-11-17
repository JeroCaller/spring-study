package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jerocaller.controller.command.CommandInter;
import com.jerocaller.controller.command.Commands;
import com.jerocaller.controller.command.PageAndSortFactory;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.service.ServiceInter;
import com.jerocaller.model.service.SiteUserService;
import com.jerocaller.requestbean.PageRequestBean;
import com.jerocaller.responsebean.PageResponseBean;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/site")
public class SiteController {
	
	@Autowired
	@Qualifier("siteUserService")
	private ServiceInter siteUserService;
	
	@Autowired
	@Qualifier("userClassInfoService")
	private ServiceInter userClassInfoService;
	
	@Autowired
	private PageAndSortFactory factory;
	
	@GetMapping("")
	public String toIndex() {
		return "index";
	}
	
	@GetMapping("/userList")
	public String showAllUsers(
			Model model, 
			PageRequestBean pRequest, 
			@RequestParam(
					name = "command", 
					defaultValue = "PAGING"
			) Commands command
	) {
		CommandInter commander = factory.getCommand(command);
		commander.processCommand(model, siteUserService, pRequest);
		
		return "userList";
	}
	
	@GetMapping("/classList")
	public String showAllClasses(Model model) {
		userClassInfoService.selectAll(model, "classes");
		return "classList";
	}
}
