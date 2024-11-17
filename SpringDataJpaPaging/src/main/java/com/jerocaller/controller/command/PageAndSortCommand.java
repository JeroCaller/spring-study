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
import com.jerocaller.requestbean.ReprocessRequestOfPage;
import com.jerocaller.responsebean.PageResponseBean;

public class PageAndSortCommand implements CommandInter {

	@Override
	public void processCommand(Model model, ServiceInter service, Object... args) {
		SiteUserService serviceImpl = (SiteUserService)service;
		PageRequestBean pRequest = (PageRequestBean) args[0];
		
		// 사용자 요청 재가공 로직
		ReprocessRequestOfPage reprocessor = new ReprocessRequestOfPage();
		Pageable pageRequest = reprocessor.getReprocessedPageRequest(pRequest);
		
		// Repository에 페이지 요청 전달 및 응답 데이터 받기
		Page<SiteUsersDto> pageResult = serviceImpl.selectPages("pages", pageRequest);
		
		// 페이지 네비게이션을 위한 관련 정보 생성 (응답 데이터 생성)
		final int BLOCKS = 5;
		PageResponseBean<SiteUsersDto> pageResponse 
			= new PageResponseBean<SiteUsersDto>(pageResult, BLOCKS);
		
		// 응답
		model.addAttribute("users", pageResult);
		model.addAttribute("nav", pageResponse);
		
	}
	
}
