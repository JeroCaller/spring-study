package com.jerocaller.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.jerocaller.common.SessionAttributeNames;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {
	
	@Override
	public boolean preHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler
	) throws Exception {
		
		log.info("Session Interceptor prehandle 호출");
		
		request.setAttribute(
			SessionAttributeNames.SESSION_INTERCEPTOR, 
			"Hi From Session Interceptor!"
		);
		log.info("Session Interceptor prehandle 끝");
		
		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(
		HttpServletRequest request, 
		HttpServletResponse response, 
		Object handler,
		ModelAndView modelAndView
	) throws Exception {
		
		log.info("Session Interceptor postHandle 호출");
		
		String sessionInterceptorValue = (String) request.getAttribute(
				SessionAttributeNames.SESSION_INTERCEPTOR
		);
		log.info("Session Interceptor value: {}", sessionInterceptorValue);
		
		log.info("Session Interceptor postHandle 끝");
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

}
