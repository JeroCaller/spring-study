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

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.service.ServiceInter;
import com.jerocaller.model.service.SiteUserService;
import com.jerocaller.requestbean.PageRequestBean;
import com.jerocaller.responsebean.PageResponseBean;

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
	
	// 정렬 기능만 보기용
	/*
	@GetMapping("/userList")
	public String showAllUsers(Model model) {
		SiteUserService serviceImpl = (SiteUserService)siteUserService;
		
		//siteUserService.selectAll(model, "users");
		serviceImpl.selectAllSorted(model, "users");
		return "userList";
	}*/
	
	@GetMapping("/userList")
	public String showAllUsers(
			Model model, 
			PageRequestBean pRequest 
	) {
		SiteUserService serviceImpl = (SiteUserService)siteUserService;
		
		// 정렬 및 페이지 요청은 서비스에서 자체적으로 정하기보단 
		// 컨트롤러 단에서 서비스로 요청하게끔 하여 조금 더 유연하고 동적인 요청을 가능하게끔
		// 하는 것이 좋지 않을까란 생각에 정렬 및 페이지 요청 코드는 컨트롤러 단에서 해보았다. 
		Sort pageSort = Sort.by(
				Order.asc("userClassInfo.classNumber"),
				Order.desc("averPurchase"),
				Order.desc("mileage")
		);
		
		// 페이지네이션 블록 개수가 유지되는지 보고자 한다면 
		// 두 번째 인자를 3으로 설정.
		Pageable pageRequest = PageRequest.of(
				pRequest.getSelectedPage() - 1, 
				10, 
				pageSort
		);
		Page<SiteUsersDto> pageResult = serviceImpl.selectPages("pages", pageRequest);
		
		// 페이지 네비게이션을 위한 관련 정보 생성
		PageResponseBean<SiteUsersDto> pageResponse 
			= new PageResponseBean<SiteUsersDto>(pageResult, 5);
		
		// 응답
		model.addAttribute("users", pageResult);
		model.addAttribute("nav", pageResponse);
		
		// 이전 요청 정보를 그대로 응답
		model.addAttribute("reqInfo", pRequest);
		
		return "userList";
	}
	
	@GetMapping("/classList")
	public String showAllClasses(Model model) {
		userClassInfoService.selectAll(model, "classes");
		return "classList";
	}
}
