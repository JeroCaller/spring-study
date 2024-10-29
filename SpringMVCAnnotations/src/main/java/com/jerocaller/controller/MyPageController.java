package com.jerocaller.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/mypage/mypages")
public class MyPageController {
	
	@GetMapping("/myhome")
	public String goToMyHome(Model model) {
		model.addAttribute("msg", "나의 홈페이지");
		return "/mypages/myhome";
	}
	
	@GetMapping("/config")
	public String goToConfig(Model model) {
		model.addAttribute("msg", "홈페이지 환경 설정");
		return "/mypages/config";
	}
	
	@GetMapping("")   //  domain/mypage
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String goToMyPage(Model model) {
		model.addAttribute("msg", "마이 페이지");
		return "/mypages/mypage";
	}
}
