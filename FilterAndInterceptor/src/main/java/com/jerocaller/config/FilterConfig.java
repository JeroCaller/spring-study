package com.jerocaller.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jerocaller.filter.OnceFilter;
import com.jerocaller.filter.SessionFilter;
import com.jerocaller.filter.TimeCheckerFilter;

/**
 * 여러 필터들의 호출 순서가 상관없다면 아래 클래스 내부의 코드는 모두 주석 처리하고, 
 * <code>@ServletComponentScan</code> 어노테이션만 적용하면 되고, 
 * 반대로 호출 순서를 정해야한다면 해당 어노테이션은 주석 처리하고 아래 코드를 
 * 주석 해제한다. 
 */
@Configuration
//@ServletComponentScan(basePackages = "com.jerocaller.filter")
public class FilterConfig {
	
	@Bean
	public FilterRegistrationBean<SessionFilter> sessionFilter() {
		
		FilterRegistrationBean<SessionFilter> bean 
			= new FilterRegistrationBean<SessionFilter>();
		bean.setFilter(new SessionFilter());
		//bean.addUrlPatterns("/test/*");
		bean.setOrder(2);
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<TimeCheckerFilter> timeCheckerFilter() {
		
		FilterRegistrationBean<TimeCheckerFilter> bean 
			= new FilterRegistrationBean<TimeCheckerFilter>();
		bean.setFilter(new TimeCheckerFilter());
		//bean.addUrlPatterns("/test/*");
		bean.setOrder(1);
		return bean;
	}
	
	@Bean
	public FilterRegistrationBean<OnceFilter> onceFilter() {
		
		FilterRegistrationBean<OnceFilter> bean 
			= new FilterRegistrationBean<OnceFilter>();
		bean.setFilter(new OnceFilter());
		bean.setOrder(3);
		return bean;
	}
}
