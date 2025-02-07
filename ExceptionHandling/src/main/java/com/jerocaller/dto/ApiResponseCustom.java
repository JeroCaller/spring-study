package com.jerocaller.dto;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.jerocaller.common.CustomResponseCode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponseCustom {
	
	@Builder.Default
	private CustomResponseCode responseCode = CustomResponseCode.OK;
	
	/**
	 * 클라이언트가 요청한 URI
	 */
	private String uri;
	
	/**
	 * 응답 데이터
	 */
	private Object data;
	
	/**
	 * 유효성 검사 실패 시의 에러 메시지 출력용. 
	 * 유효성 검사가 아닌 상황에서는 이 필드에 값을 넣지 않는다.
	 */
	private Map<String, String> validationMsg;
	
	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	@JsonFilter("validationJsonFilter")
	public static class DetailedApiResponseCustom {
		
		private String code;
		private String message;
		
		private Map<String, String> validationMsg;
		
		private String uri;
		private Object data;
		
	}
	
	public ResponseEntity<Object> toResponse() {
		
		DetailedApiResponseCustom response 
			= DetailedApiResponseCustom.builder()
			.code(this.getResponseCode().getCode())
			.message(this.getResponseCode().getMessage())
			.validationMsg(this.getValidationMsg())
			.uri(this.getUri())
			.data(data)
			.build();
		
		MappingJacksonValue mapped = getFilteredData(response);
		
		return ResponseEntity.status(this.getResponseCode()
				.getHttpStatus())
				.body(mapped);
		
	}
	
	/**
	 * JSON 응답용 DTO 클래스에서 특정 필드를 JSON 응답 프로퍼티에 추가할지 여부를 결정하는 메서드. 
	 * 
	 * validationMsg에 값이 하나라도 있으면 해당 필드를 응답 데이터에 포함시키고, 
	 * 그렇지 않으면 응답 데이터에서 제외시킨다. 
	 * 
	 * @param response
	 * @return
	 */
	private MappingJacksonValue getFilteredData(DetailedApiResponseCustom response) {
		
		SimpleBeanPropertyFilter filter;
		
		if (response.getValidationMsg() == null || 
			response.getValidationMsg().size() == 0
		) {
			// validationMsg 필드의 값이 null 이거나 데이터가 하나도 없을 경우 
			// 해당 필드를 JSON 응답 프로퍼티에서 제외시킨다. 
			filter = SimpleBeanPropertyFilter.serializeAllExcept("validationMsg");
		} else {
			// validationMsg 필드의 값이 존재한다면 해당 필드를 포함하여 
			// 모든 필드들을 JSON 응답 프로퍼티에 포함시킨다. 
			filter = SimpleBeanPropertyFilter.serializeAll();
		}
		
		// `@JsonFilter`로 지정된 필터명과 앞서 SimpleBeanPropertyFilter으로 
		// 해당 필터에 특정 필터 기능을 지정해놓은 filter를 등록. 
		FilterProvider filters = new SimpleFilterProvider()
			.addFilter("validationJsonFilter", filter);
		
		// 위에서 적용한 필터가 적용된 응답용 자바 객체를 반환. 
		MappingJacksonValue mapping = new MappingJacksonValue(response);
		mapping.setFilters(filters);
		
		return mapping;
	}

}
