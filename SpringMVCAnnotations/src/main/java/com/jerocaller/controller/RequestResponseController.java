package com.jerocaller.controller;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RequestResponseController {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/requestBody")
	public String goToForm() {
		return "requestBody";
	}
	
	// produces를 빼도 실행은 된다. 
	// 다만 크롬 브라우저에서 F12 -> 개발자 도구 -> network에서 requestBody.html을 클릭하여 
	// HTTP 응답 정보를 보면 'text/plain; charset=UTF-8'로 설정되며, 응답 데이터가 텍스트 
	// 형태로 전송됨을 의미한다. 
	@PostMapping(value = "/requestBody", produces = "application/json; charset=UTF-8")
	//@PostMapping("/requestBody")
	@ResponseBody
	public String handleRequest(
			@RequestBody Map<String, Object> userData
	) {
		logger.info("userData : " + userData);
		String responseData = rotateMapAll(userData);
		return responseData;
	}
	
	private String rotateMapAll(Map<String, Object> data) {
		Set<String> keys = data.keySet();
		StringBuilder stringBuilder = new StringBuilder("{");
		
		String newInsertedString = " Oh~~!!!!";
		
		logger.info("======================");
		
		/*
		 * json 형태 예시)
		 * {"name" : "kimquel Oh~~!!!!", "job" : "소프트웨어 엔지니어 Oh~~!!!!", "age" : "23 Oh~~!!!!" }
		 */
		keys.forEach(oneKey -> {
			logger.info("key : " + oneKey + ", value : " + data.get(oneKey));
			stringBuilder.append("\"" + oneKey + "\" : ");
			stringBuilder.append("\"" + data.get(oneKey));
			stringBuilder.append(newInsertedString);
			stringBuilder.append("\", ");
		});
		logger.info("======================");
		
		// 마지막에 추가한 불필요한 쉼표 문자 제거
		stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(","));
		stringBuilder.append("}");
		
		logger.info("stringBuilder: " + stringBuilder);
		
		return stringBuilder.toString();
	}
}
