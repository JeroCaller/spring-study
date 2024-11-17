package com.jerocaller.controller.command;

import org.springframework.ui.Model;

import com.jerocaller.model.service.ServiceInter;

public interface CommandInter {
	void processCommand(Model model, ServiceInter service, Object... args);
}
