package com.jerocaller.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SampleService {

	/**
	 * Map 자료구조로 응답 데이터 전송을 위한 샘플 데이터 생성
	 * 
	 * @return
	 */
	public Map<String, Object> getSampleData() {
		
		Map<String, Object> sample = new HashMap<String, Object>();
		
		sample.put("name", "샘플 데이터");
		sample.put("category", "Sample Test");
		
		return sample;
		
	}
	
}
