package com.jerocaller.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.service.SampleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/samples")
@RequiredArgsConstructor
public class SampleController {
	
	private final SampleService sampleService;
	
	@GetMapping
	public ResponseEntity<List<Map<String, Object>>> getSampleData(
		@RequestParam Map<String, Object> params
	) {
		List<Map<String, Object>> responseResult = new ArrayList<Map<String,Object>>();
		Map<String, Object> result = sampleService.getSampleData();
		
		responseResult.add(result);
		responseResult.add(params);
		
		return ResponseEntity.ok(responseResult);
		
	}
	
}
