package com.jerocaller.controller.command;

import org.springframework.ui.Model;

import com.jerocaller.model.service.ServiceInter;
import com.jerocaller.model.service.SiteUserService;

public class SelectAllOnlySort implements CommandInter {

	@Override
	public void processCommand(Model model, ServiceInter service, Object... args) {
		SiteUserService serviceImpl = (SiteUserService)service;
		
		serviceImpl.selectAllSorted(model, "users");
	}
	
}
