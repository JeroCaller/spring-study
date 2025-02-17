package com.jerocaller.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.jerocaller.common.SessionAttributeNames;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class TestAdvice {
	
	private final HttpServletRequest httpRequest;

	@Around("@annotation(com.jerocaller.aop.annotation.ForTestAdvice)")
	public Object handlerControllerMethod(ProceedingJoinPoint joinPoint) 
		throws Throwable 
	{
		
		log.info("TestAdvice 호출됨.");
		
		httpRequest.setAttribute(
			SessionAttributeNames.SESSION_AOP, 
			"Hi From AOP"
		);
		
		Object result = joinPoint.proceed();
		
		log.info("TestAdvice 종료.");
		return result;
	}
}
