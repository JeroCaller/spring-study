package com.jerocaller.filter;

import java.io.IOException;

import com.jerocaller.common.RequestAttributeNames;

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
public class RequestFilter extends HttpFilter implements Filter {

	@Override
	protected void doFilter(
		HttpServletRequest request, 
		HttpServletResponse response, 
		FilterChain chain
	) throws IOException, ServletException {
		
		log.info("Request Filter 호출");
		
		request.setAttribute(RequestAttributeNames.REQUEST_FILTER, "Hi From request filter");
		
		chain.doFilter(request, response);
		
		String requestFilterValue = (String) request.getAttribute(
			RequestAttributeNames.REQUEST_FILTER
		);
		log.info("requestFilterValue: {}", requestFilterValue);
		
		log.info("request Filter 끝");
		
	}
	
}
