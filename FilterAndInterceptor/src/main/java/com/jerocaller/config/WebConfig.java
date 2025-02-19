package com.jerocaller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jerocaller.interceptor.RequestInterceptor;
import com.jerocaller.interceptor.TimeCheckerInterceptor;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
	
	private final RequestInterceptor requestInterceptor;
	private final TimeCheckerInterceptor timeCheckerInterceptor;
	
	/**
	 * 인터셉터 등록 순서에 따라 호출 순서도 그에 따라 달라진다. 
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		//registry.addInterceptor(requestInterceptor)
		//	.addPathPatterns("/test/**");
		
		registry.addInterceptor(timeCheckerInterceptor)
			.addPathPatterns("/test/**");
		
		registry.addInterceptor(requestInterceptor)
			.addPathPatterns("/test/**");
	}

}
