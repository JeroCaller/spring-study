package com.jerocaller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.service.ServiceInter;
import com.jerocaller.model.service.SiteUsersService;

@Controller
@RequestMapping("/site")
public class SiteController {
	
	@Autowired
	@Qualifier(value = "siteUsersService")
	private ServiceInter<SiteUsersDto> siteUserService;
	
	@Autowired
	@Qualifier("userClassInfoService")
	private ServiceInter<UserClassInfoDto> userClassInfoService;
	
	@GetMapping("")
	public String toIndex() {
		return "index";
	}
	
	@GetMapping("/userList")
	public String showAllList(Model model) {
		List<SiteUsersDto> results = siteUserService.selectAll();
		model.addAttribute("users", results);
		return "userList";
	}
	
	@GetMapping("/insert")
	public String toInsertPage(Model model) {
		List<UserClassInfoDto> userClasses 
			= userClassInfoService.selectAll();
		model.addAttribute("uClasses", userClasses);
		
		List<SiteUsersDto> recomms = (
				(SiteUsersService)siteUserService
			).selectRecomms();
		model.addAttribute("recomms", recomms);
		
		return "insert";
	}
}
