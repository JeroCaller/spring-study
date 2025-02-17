package com.jerocaller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.aop.annotation.ForTestAdvice;
import com.jerocaller.common.RequestAttributeToJson;
import com.jerocaller.service.TestService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
@RequiredArgsConstructor
public class TestController {
	
	private final HttpServletRequest httpRequest;
	private final TestService testService;
	
	@GetMapping("/simple")
	@ForTestAdvice
	public ResponseEntity<Object> test() {
		
		log.info("test() 컨트롤러 메서드 호출됨.");
		
		try {
			testService.delay(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		log.info("test() 컨트롤러 메서드 종료.");
		
		return ResponseEntity.ok(RequestAttributeToJson
			.getJsonFromRequestAttributes(httpRequest)
		);
	}
}
