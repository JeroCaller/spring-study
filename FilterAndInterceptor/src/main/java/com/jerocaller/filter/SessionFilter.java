package com.jerocaller.filter;

import java.io.IOException;

import com.jerocaller.common.SessionAttributeNames;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@WebFilter(urlPatterns = "/test/*")
@Slf4j
public class SessionFilter extends HttpFilter implements Filter {

	@Override
	protected void doFilter(
		HttpServletRequest request, 
		HttpServletResponse response, 
		FilterChain chain
	) throws IOException, ServletException {
		
		log.info("Session Filter 호출");
		
		request.setAttribute(SessionAttributeNames.SESSION_FILTER, "Hi From session filter");
		
		chain.doFilter(request, response);
		
		String sessionFilterValue = (String) request.getAttribute(
			SessionAttributeNames.SESSION_FILTER
		);
		log.info("sessionFilterValue: {}", sessionFilterValue);
		
		log.info("Session Filter 끝");
		
	}
	
}
