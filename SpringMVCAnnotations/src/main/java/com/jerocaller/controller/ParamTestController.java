package com.jerocaller.controller;

import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ParamTestController {
	
	//@Autowired
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@GetMapping("/something")
	/**
	 * 사용자가 어떤 요청 정보를 주던 동적으로 받을 수 있게 되었다. 
	 * 
	 * @param allParams
	 * @return
	 */
	public String something(
			@RequestParam Map<String, String> allParams
			//@RequestParam("param") Object param
	) {
		logger.info("hi");
		//logger.info("param : " + param);
		logAllParams(allParams);
		return "index";
	}
	
	private void logAllParams(Map<String, String> allParams) {
		logger.info("======================================");
		Set<String> keys = allParams.keySet();
		keys.forEach(oneKey -> {
			String oneRecord = String.format(
					"key : %s, value : %s", 
					oneKey, 
					allParams.get(oneKey)
			);
			logger.info(oneRecord);
		});
		logger.info("======================================");
	}
}
