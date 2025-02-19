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
public class RequestInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler
	) throws Exception {
		
		log.info("Request Interceptor prehandle 호출");
		
		request.setAttribute(
			RequestAttributeNames.REQUEST_INTERCEPTOR, 
			"Hi From Request Interceptor!"
		);
		log.info("Request Interceptor prehandle 끝");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler,
		ModelAndView modelAndView
	) throws Exception {
		
		log.info("Request Interceptor postHandle 호출");
		
		String requestInterceptorValue = (String) request.getAttribute(
				RequestAttributeNames.REQUEST_INTERCEPTOR
		);
		log.info("Request Interceptor value: {}", requestInterceptorValue);
		
		log.info("Request Interceptor postHandle 끝");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
