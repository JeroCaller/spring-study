package com.jerocaller.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 여러 개 파일들의 업로드 혹은 다운로드 수행 시 
 * 일부 파일만 작업 성공하고 다른 일부 파일들은 실패했을 때, 
 * 해당 파일들의 정보를 작업 성공 여부를 기준으로 나눠 
 * 응답 데이터에 보내는 용도의 DTO 클래스.
 * 
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileSuccessFailed {
	
	private List<String> success;
	private Map<String, String> failed;
	
}
