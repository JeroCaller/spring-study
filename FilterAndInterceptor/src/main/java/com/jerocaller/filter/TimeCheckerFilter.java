package com.jerocaller.filter;

import java.io.IOException;

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
public class TimeCheckerFilter extends HttpFilter implements Filter {
	
	@Override
	protected void doFilter(
		HttpServletRequest request, 
		HttpServletResponse response, 
		FilterChain chain
	) throws IOException, ServletException {
		
		log.info("TimeChecker Filter 호출");
		
		long start = System.currentTimeMillis();
		chain.doFilter(request, response);
		long end = System.currentTimeMillis();
		
		long duration = end - start;
		log.info("TimeChecker Filter: 동작 시간(밀리초): {}", duration);
		
		log.info("TimeChecker Filter 끝");
	}

}
