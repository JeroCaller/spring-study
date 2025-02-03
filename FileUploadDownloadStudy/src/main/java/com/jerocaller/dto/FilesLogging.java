package com.jerocaller.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import lombok.Getter;

/**
 * 파일 작업 서비스 클래스 내에서 파일 작업 후 작업에 성공한 파일들과 실패한 파일들의 
 * 정보를 담아 컨트롤러에서 사용할 목적의 클래스.
 * 
 * 매번 서비스 클래스의 메서드 내부에서 해당 클래스를 new 키워드를 통해 객체화하는 겻은 특히나 
 * 해당 메서드가 자주 호출되는 상황에서 불필요한 객체들을 생성하게 될 수 있기에 이를 방지하기 위해 
 * 서비스 클래스에서 의존성 주입을 통해 사용하는 것을 권장.
 */
@Getter
@Component
public class FilesLogging {
	
	/**
	 * 작업에 성공한 파일 경로들을 리스트 형태로 저장.
	 */
	protected final List<String> succeedFileNames = new ArrayList<String>();
	
	/**
	 * 작업에 실패한 파일의 경로를 key, 실패 원인을 value로 기록.
	 */
	protected final Map<String, String> failedPaths = new HashMap<String, String>();
	
	/**
	 * 모든 컬렉션 자료구조들의 데이터들을 삭제한다. 
	 */
	public void clearAll() {
		succeedFileNames.clear();
		failedPaths.clear();
	}
	
}
