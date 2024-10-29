package com.jerocaller.beanizers;

import org.checkerframework.checker.units.qual.t;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
/**
 * 외부 라이브러리로부터 온 객체를 반환하는 방법을 다루고 있음. 
 */
public class LoggerMaker {
	
	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(getClass());
	}
	
}
