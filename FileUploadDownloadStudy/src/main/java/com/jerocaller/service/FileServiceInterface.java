package com.jerocaller.service;

import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.jerocaller.dto.FileResponse;

/**
 * 조사 결과, 파일 업로드 및 다운로드 기능을 구현하는 방법에는 
 * 여러 가지가 있다. 따라서 각각의 방법들로 따로 구현하고, 
 * 다른 방법으로 구현한 서비스 클래스로 손쉽게 스위칭하기 위해 
 * 추상화를 이용하기 위해 인터페이스를 선언함. 
 * 파일 업로드 및 다운로드 기능을 구현하는 서비스 클래스들은 모두 
 * 이 인터페이스를 구현해야 함. 
 */
public interface FileServiceInterface<Service> {
	
	/**
	 * 클라이언트로부터 넘어온 파일과 그 경로를 각각 서버 내 폴더 및 
	 * DB에 저장한다. 
	 *
	 * @param files - 여러 개의 파일들의 업로드 허용
	 * @return 작업 결과 성공 또는 실패, 또는 그 외 결과를 어떻게 표시할지 
	 * 자유롭게 하기 위해 Object로 선언함. 
	 */
	Object uploadFiles(MultipartFile[] files);
	
	/**
	 * 현재 인증된 사용자의 파일들을 리스트로 반환.
	 * 
	 * @return
	 */
	List<FileResponse> getFiles();
	
	/**
	 * 파일 id 입력 시 해당 파일을 다운로드
	 * 
	 * @param id
	 * @return 작업 결과 성공 또는 실패, 또는 그 외 결과를 어떻게 표시할지 
	 * 자유롭게 하기 위해 Object로 선언함. 
	 */
	Object downloadByFileId(int id);
	
	/**
	 * 파일 id 입력 시 이에 해당하는 파일을 삭제. 
	 * 
	 * 파일 시스템 상에서도 삭제되어야 하고, DB에서도 
	 * 해당 파일 정보가 삭제되어야 한다. 
	 * 
	 * @param id
	 * @return 작업 결과 성공 또는 실패, 또는 그 외 결과를 어떻게 표시할지 
	 * 자유롭게 하기 위해 Object로 선언함. 
	 */
	Object deleteByFileId(int id);
	
	/**
	 * 여러 파일들의 업로드 혹은 다운로드 시 실패한 파일 경로들을 
	 * 로깅할 목적의 데이터를 반환하는 메서드.
	 * 
	 * @return
	 */
	default Map<String, String> getFailedPaths() {
		return null;
	}
	
}
