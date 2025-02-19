package com.jerocaller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jerocaller.common.RequestAttributeNames;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class TimeCheckerInterceptor implements HandlerInterceptor {@Override
	
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler
	) throws Exception {
		
		log.info("Time Checker Interceptor prehandle 호출");
		
		long startTime = System.currentTimeMillis();
		request.setAttribute(RequestAttributeNames.START_TIME_INTERCEPTOR, startTime);
		
		log.info("Time Checker Interceptor prehandle 끝");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler,
		ModelAndView modelAndView
	) throws Exception {
		
		log.info("Time Checker Interceptor posthandle 호출");
		
		long endTime = System.currentTimeMillis();
		long startTime = (long) request.getAttribute(
			RequestAttributeNames.START_TIME_INTERCEPTOR
		);
		long duration = endTime - startTime;
		log.info("Time Checker Interceptor 동작 시간(밀리초): {}", duration);
		
		log.info("Time Checker Interceptor posthandle 끝");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
}
