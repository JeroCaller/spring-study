package com.jerocaller.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jerocaller.controller.formbean.InsertRequest;
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
		
		List<String> recomms = (
				(SiteUsersService)siteUserService
			).selectAllUsername();
		model.addAttribute("recomms", recomms);
		
		return "insert";
	}
	
	/**
	 * 사용자가 입력한 새 유저 정보를 DB에 등록
	 * 성공 시 새로 입력된 회원 정보를 다시 불러와 화면에 표시하도록 한다. 
	 * 
	 * @param insertRequest
	 * @return
	 */
	@PostMapping("/insert")
	public String insertProcess(
			InsertRequest insertRequest, 
			Model model
	) {
		SiteUsersDto saveResult = ((SiteUsersService)siteUserService).saveOne(insertRequest);
		model.addAttribute("newUser", saveResult);
		return "insertResult";
	}
}
