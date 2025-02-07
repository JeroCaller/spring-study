package com.jerocaller.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.ApiResponseCustom;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
	
	private final HttpServletRequest httpRequest;
	
	@GetMapping("/response/outer")
	public ResponseEntity<Object> getApiResponseItself() {
		
		ApiResponseCustom response = ApiResponseCustom.builder()
			.uri(httpRequest.getRequestURI())
			.data("hi")
			.build();
		
		return ResponseEntity.status(response.getResponseCode()
				.getHttpStatus())
				.body(response);
	}
	
	@GetMapping("/response/inner")
	public ResponseEntity<Object> getInnerResponse() {
		
		return ApiResponseCustom.builder()
			.uri(httpRequest.getRequestURI())
			.data("wow")
			.build()
			.toResponse();
	}
	
	@GetMapping("/response/filter/validation/{bool}")
	public ResponseEntity<Object> getFilteredData(@PathVariable("bool") boolean filterOn) {
		
		ApiResponseCustom response = null;
		
		String thisUri = httpRequest.getRequestURI();
		String data = "hahaha";
		
		if (filterOn) {
			response = ApiResponseCustom.builder()
				.uri(thisUri)
				.data(data)
				.build();
		} else {
			Map<String, String> msg = new HashMap<String, String>();
			msg.put("1", "첫번째 메시지");
			msg.put("2", "두번째 메시지");
			msg.put("3", "소시지");
			
			response = ApiResponseCustom.builder()
				.uri(thisUri)
				.data(data)
				.validationMsg(msg)
				.build();
		}
		
		return response.toResponse();
	}
}
