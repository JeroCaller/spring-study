package com.jerocaller.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

/**
 * WebMvcConfigurer : Spring MVC에 관한 설정. 웹 환경 설정용.
 */
@Configuration
@Slf4j
public class WebConfig implements WebMvcConfigurer {
	
	@Value("${file.upload-dir}")
	private String uploadBaseDirString;
	
	/**
	 * 정적 리소스(이미지, CSS, JS 등) 경로 추가 설정 담당.
	 * 업로드 설정이 목표. 
	 * 
	 * 이 메서드를 정의하지 않으면 프론트엔드에서 서버로부터 파일 경로를 받아도 
	 * img 태그를 이용하여 이미지를 화면에 출력할 수 없다. 따라서 이 설정은 필수!
	 */
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		// 업로드 될 파일들이 저장될 폴더의 정보를 얻고 그 절대 경로를 얻는다. 
		Path uploadBaseDir = Paths.get(uploadBaseDirString);
		String fullPath = uploadBaseDir.toFile().getAbsolutePath();
		
		// application.properties 파일에 설정한 file.upload-dir의 경로에 "."과 같이 
		// 상대 경로 표기법이 들어가 있는 경우 이를 제거함으로써, 정적 자원들이 들어있는 경로에 접근 가능한 
		// 경로 패턴 문자열을 동적으로 생성한다. 
		// 예) file-upload-dir = ./upload/files 라 되어 있는 경우, 
		// => /upload/files 로 바꾼다. 
		Path normalizedUploadBaseDir = Paths.get(uploadBaseDirString).normalize();
		String contextPath = "/" + normalizedUploadBaseDir.toString().replace("\\", "/");
		
		// 정적 자원을 제공하기 위해 registry.addResourceHandler(String pattern) 메서드를 
		// 사용한다. "/files/users/**"라는 문자열이 해당 메서드의 인자로 전달된 경우, 
		// /files/users/test.png 와 같은 URL이 들어오면 그 위치에 존재하는 test.png를 
		// 반환할 수 있도록 설정한다. 
		
		// registry.addResourceHandler("/files/users/**") // 이것과 동일
		registry.addResourceHandler(contextPath + "/**")
			.addResourceLocations("file:" + fullPath + "/");
		
		// "file:" + uploadPath + "/" => 
		// 파일 시스템의 uploads 디렉토리를 나타냄. 
		// "file:" 접두사를 붙여 이 경로가 파일 시스템의 경로임을 지정한다. 
		// 해당 접두사는 WebMvcConfigurer에 대한 설정을 위함. 
	}
	
}
