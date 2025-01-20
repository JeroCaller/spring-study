package com.jerocaller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.service.EateriesProcess;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/eateries")
@RequiredArgsConstructor
public class EateriesController {
	
	private final EateriesProcess eateriesProcess;
	
	@GetMapping
	public Object getEateriesAll() {
		return eateriesProcess.getAll();
	}
	
	@PutMapping("/view/count")
	public ResponseEntity<Object> addOneViewCount(
		@RequestParam("eateryNo") int eateryNo
	) {
		
		boolean result = eateriesProcess.addOneViewCount(eateryNo);
		
		return result ? 
			ResponseEntity.ok("good") : 
			ResponseEntity.notFound()
				.build();
				
	}
	
}
