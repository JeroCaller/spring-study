package com.jerocaller.controller.command;

import org.springframework.ui.Model;

import com.jerocaller.model.service.ServiceInter;

public class SelectAllCommand implements CommandInter {

	@Override
	public void processCommand(Model model, ServiceInter service, Object... args) {
		service.selectAll(model, "users");
	}
	
}
