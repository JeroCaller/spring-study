package com.jerocaller.util;

public class PermitAllRequestUriUtils {
	
	private static final String[] permitAllRequestUris = {
		"/swagger-ui/**", 
		"/v3/api-docs",
		"/v3/api-docs/**", 
		"/swagger-ui.html",
		"/swagger-ui/index.html",
		"/auth/**", 
		"/members",
		"/members/**",
		"/files/**",
	};
	
	public static String[] getPermitAllRequestUris() {
		return permitAllRequestUris;
	}
	
}
