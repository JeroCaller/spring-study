package com.jerocaller.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestAttributeToJson {
	
	/**
	 * HttpServletRequest.getAttribute()에 있는 모든 attribute들의 이름과 
	 * 값을 꺼내 Map 형식으로 응답 JSON 생성.
	 * 
	 * @param httpRequest
	 * @return
	 */
	public static Map<String, Object> getJsonFromRequestAttributes(
		HttpServletRequest httpRequest
	) {
		
		Map<String, Object> toJson = new HashMap<String, Object>();
		
		Enumeration<String> attrNames = httpRequest.getAttributeNames();
		while(attrNames.hasMoreElements()) {
			String name = attrNames.nextElement();
			Object valueObj = httpRequest.getAttribute(name);
			
			// Attribute(Request Context)에는 개발자가 입력한 프로퍼티 외 다른 값들도 
			// 있으므로, 이들을 제외하고 오로지 개발자가 입력한 값들만 JSON 응답 객체에 포함. 
			if ((valueObj instanceof String) || (valueObj instanceof Long)) {
				toJson.put(name, valueObj);
			}
			//log.info("name: {}", name);
		}
		
		return toJson;
	}

}
