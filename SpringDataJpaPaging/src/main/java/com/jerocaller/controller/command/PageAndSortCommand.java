package com.jerocaller.controller.command;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.ui.Model;

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.service.ServiceInter;
import com.jerocaller.model.service.SiteUserService;
import com.jerocaller.requestbean.PageRequestBean;
import com.jerocaller.responsebean.PageResponseBean;

public class PageAndSortCommand implements CommandInter {

	@Override
	public void processCommand(Model model, ServiceInter service, Object... args) {
		SiteUserService serviceImpl = (SiteUserService)service;
		PageRequestBean pRequest = (PageRequestBean) args[0];
		
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
		final int PAGE_SIZE = 10;
		Pageable pageRequest = PageRequest.of(
				pRequest.getSelectedPage() - 1, 
				PAGE_SIZE, 
				pageSort
		);
		Page<SiteUsersDto> pageResult = serviceImpl.selectPages("pages", pageRequest);
		
		// 페이지 네비게이션을 위한 관련 정보 생성
		final int BLOCKS = 5;
		PageResponseBean<SiteUsersDto> pageResponse 
			= new PageResponseBean<SiteUsersDto>(pageResult, BLOCKS);
		
		// 응답
		model.addAttribute("users", pageResult);
		model.addAttribute("nav", pageResponse);
		
		// 이전 요청 정보를 그대로 응답
		model.addAttribute("reqInfo", pRequest);
	}
	
}
