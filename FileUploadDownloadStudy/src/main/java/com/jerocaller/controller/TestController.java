package com.jerocaller.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/test")
@Slf4j
public class TestController {
	
	@Value("${file.upload-dir}")
	private String uploadBaseDirString;
	
	/**
	 * WebConfig.java 에서 업로드를 위한 파일 경로 설정을 위한 테스트.
	 * 이클립스의 콘솔창으로 그 결과를 확인한다. 
	 */
	@GetMapping("/upload-dir")
	public void uploadDirPathTest() {
		
		Path uploadBaseDir = Paths.get(uploadBaseDirString);
		log.info("uploadBaseDir.toString() : " + uploadBaseDir.toString());
		log.info("uploadBaseDir full path: " + uploadBaseDir
			.toFile().getAbsolutePath()
		);
		
		Path normalizedUploadBaseDir = Paths.get(uploadBaseDirString)
			.normalize();
		log.info("normalizedUploadBaseDir.toString(): " + normalizedUploadBaseDir.toString());
		log.info("normalizedUploadBaseDir full path: " + normalizedUploadBaseDir
			.toFile().getAbsolutePath()
		);
		log.info("normalizedUploadBaseDir filename: " + normalizedUploadBaseDir.getFileName());
		
		String contextPath = "/" + normalizedUploadBaseDir.toString().replace("\\", "/");
		log.info("contextPath: " + contextPath);
		
	}
	
}
