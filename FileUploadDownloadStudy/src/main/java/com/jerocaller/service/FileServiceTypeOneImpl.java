package com.jerocaller.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.jerocaller.dto.FileResponse;
import com.jerocaller.entity.FileEntity;
import com.jerocaller.entity.Members;
import com.jerocaller.exception.FileNotFoundOrReadableException;
import com.jerocaller.exception.IORuntimeException;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.repository.FileEntityRepository;
import com.jerocaller.repository.MembersRepository;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceTypeOneImpl implements FileServiceInterface {
	
	private final FileEntityRepository fileRepository;
	private final MembersRepository membersRepository;
	
	private Path uploadBaseDirPath;
	
	// uploadFiles() 메서드 실행 도중 특정 파일이 업로드에 실패했을 때 
	// 어떤 파일이 업로드에 실패했는지 로깅하기 위한 필드.
	private final Map<String, String> failedPaths = new HashMap<String, String>();
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir;
	
	/**
	 * <p>
	 * 파일 업로드를 위한 서버 내 베이스 디렉토리 생성(없을 시에만) 및 
	 * 경로 초기화. <br/>
	 * 업로드될 파일을 저장하기 위한 베이스 디렉토리 생성은 최초 단 한 번만 실행되면 되기에 
	 * 초기화 메서드를 통해 생성하도록 함. 
	 * </p>
	 * 
	 * <p>
	 * 어노테이션 설명) <br/>
	 * \@PostConstruct - 생성자를 이용한 DI(Dependency Injection, 의존성 주입)의 
	 * 과정을 떠올려보면 알겠지만, 생성자 호출 시 해당 생성자 메서드 내부에서 
	 * DI가 일어나 의존성이 주입된 후에야 스프링 빈이 생성되는 구조이다. 
	 * 그런데 어떤 메서드에 @PostConstruct 를 적용하면, DI가 진행된 이후에 
	 * 해당 메서드가 호출되기에, 주입된 의존성을 확인하면서 초기화 작업을 할 수 있게 된다. 
	 * 즉, 주입된 의존성을 이용한 초기화가 가능해진다. 
	 * 또한, 이 어노테이션이 적용된 초기화용 메서드는 bean lifecycle에서 오로지 단 한 번만 호출되며, 
	 * Bean 자체도 한 번만 인스턴스화되기에 불필요한 여러 번의 초기화 또는 빈 재생성을 방지할 수 있게 된다. <br/>
	 * 여기서는 @RequiredArgsConstructor 이라는 어노테이션을 이용하여 생성자 DI를 하기에 생성자 메서드를 
	 * 직접 정의하여 초기화 코드를 마련할 수 없어 사용하게 됨. 
	 * <br/>
	 * 참고 자료) <br/>
	 * https://hstory0208.tistory.com/entry/Spring-PostConstruct%EC%99%80-PreDestory-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B9%88-%EC%83%9D%EB%AA%85%EC%A3%BC%EA%B8%B0-%EA%B4%80%EB%A6%AC
	 * https://zorba91.tistory.com/223
	 * https://stackoverflow.com/questions/3406555/why-use-postconstruct
	 * </p>
	 * 
	 * @throws IOException
	 */
	@PostConstruct
	public void init() throws IOException {
		
		// 경로 URI를 토대로 실제로 물리적으로 베이스 디렉토리 생성 및 
		// 업로드된 파일들을 저장하기 위해 Path 객체로 변환.
		// Paths.get(String) : String을 Path 객체로 변환.
		uploadBaseDirPath = Paths.get(uploadBaseDir);
		log.info("베이스 디렉토리 절대 경로: " + uploadBaseDirPath.toFile().getAbsolutePath());
		
		// 만약 해당 베이스 디렉토리가 존재하지 않는다면 생성.
		if (Files.notExists(uploadBaseDirPath)) {
			Files.createDirectories(uploadBaseDirPath);
			log.info("업로드된 파일의 서버 내 저장을 위한 베이스 디렉토리가 생성되었습니다.");
		}
		
	}
	
	public Map<String, String> getFailedPaths() {
		return failedPaths;
	}
	
	@Override
	public Object uploadFiles(MultipartFile[] files) throws 
		NotAuthenticatedUserException, 
		IORuntimeException,
		EntityNotFoundException
	{
		
		failedPaths.clear();
		
		// 현재 사용자의 닉네임 추출. 
		// 미인증 사용자의 경우 NotAuthenticatedUserException 예외 발생
		// 다른 유저들과 구분하기 위해 유저 닉네임을 이름으로 하는 디렉토리 추가 후 
		// 여기에 파일들을 저장하도록 한다. 
		String username = AuthenticationUtils.getLoggedinUserInfo()
			.getNickname();
		
		// Path_A.resolve(String childDir) : Path_A/childDir로 URI를 합친 후 이 
		// 문자열을 다시 Path로 반환.
		Path baseUserDirPath = uploadBaseDirPath.resolve(username);
		if (Files.notExists(baseUserDirPath)) {
			try {
				// 존재하지 않는 모든 부모 디렉토리를 자동 생성해준다고 함.
				Files.createDirectories(baseUserDirPath);
			} catch (IOException e) {
				throw new IORuntimeException("유저 닉네임 디렉토리 생성 실패. \n" + e.getMessage());
			}
		}
		
		for (MultipartFile file : files) {
			// 파일 내용이 다르나 서버 내에 똑같은 파일명이 존재할 경우를 방지하기 위해 
			// 클라이언트가 전달한 파일명에 부가 정보를 추가한다. 
			// 여기서는 현재 시간이란 정보를 이용한다. 
			String fileName = System.currentTimeMillis() // 현재 시각을 밀리초로 반환.
				+ "_" 
				+ file.getOriginalFilename(); // 멀티파일의 원래 파일명.
			
			// 부모 디렉토리 경로와 방금 생성한 파일명을 합쳐 최종 경로를 생성함.
			Path filePath = baseUserDirPath.resolve(fileName); 
			log.info("서버 내에 저장될 파일의 최종 경로: " + filePath.toFile()
				.getAbsolutePath()
			);
			
			try {
				// 파일을 특정 경로로 복사 하기. 
				// 이미 동일 파일 존재 시 덮어쓰기
				// Files.copy() : InputStream으로부터 파일을 읽어 filePath 위치에 
				// 파일을 저장함. 
				// StandardCopyOption.REPLACE_EXISTING : 덮어쓰기 옵션
				Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				// 업로드 실패한 파일의 전체 경로를 기록.
				// 다른 파일들은 계속 업로드할 수 있도록 예외를 던지지 않음. 
				// 예외가 발생한 파일 전체 경로 : 에러 메시지
				failedPaths.put(filePath.toFile().getAbsolutePath(), e.getMessage());
				continue;
			}
			
			// 파일 정보를 DB에 저장.
			Members currentMember = membersRepository.findById(username)
				.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
			FileEntity fileEntity = FileEntity.builder()
				.filePath(filePath.toFile().getAbsolutePath())
				.members(currentMember)
				.build();
			fileRepository.save(fileEntity);
		}
		
		if (failedPaths.size() > 0) {
			return false;  // 전체 파일들 중 일부 또는 모두 업로드 되지 않은 경우.
		}
		
		return true; // 모든 파일들이 정상적으로 업로드 된 경우.
	}
	
	@Override
	public List<FileResponse> getFiles() throws EntityNotFoundException {
		
		String username = AuthenticationUtils.getLoggedinUserInfo()
			.getNickname();
		
		Members currentMember = membersRepository.findById(username)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
		
		List<FileEntity> fileEntities = fileRepository.findByMembers(currentMember);
		
		return fileEntities.stream()
			.map(FileResponse :: toDto)
			.collect(Collectors.toList());
		
	}

	@Override
	public Object downloadByFileId(int id) throws 
		EntityNotFoundException, 
		IORuntimeException,
		FileNotFoundOrReadableException
	{
		
		FileEntity targetFile = fileRepository.findById(id)
			.orElseThrow(() -> new EntityNotFoundException("존재하지 않는 파일입니다."));
		
		// 서버 상에 존재하는 파일 경로 추출.
		// normalize() : ".", ".."과 같이 상대 경로 지정에 쓰이는 문자들을 
		// 불필요한 문자로 간주하고 삭제해주는 기능.
		// 예) abc/./test.png -> abc/test.png
		Path filePath = Paths.get(targetFile.getFilePath()).normalize();
		log.info("서버 내에 저장된 파일의 최종 경로: " + filePath.toFile()
			.getAbsolutePath()
		);
		
		// 파일, 이미지 등의 모든 리소스를 표현하는 객체
		// 기존 문자열 형식의 경로에서, file://~ 프로토콜로 시작하는 URI 형식으로 변경됨.
		Resource resource = null;
		try {
			resource = new UrlResource(filePath.toUri());
		} catch (MalformedURLException e) {
			throw new IORuntimeException("Path -> Resource로 변경 중 URI가 잘못 생성되었습니다.");
		}
		
		if (!resource.exists() || !resource.isReadable()) {
			String exceptionPath = null;
			try {
				exceptionPath = resource.getURL().getPath();
				throw new FileNotFoundOrReadableException(
					"해당 파일이 존재하지 않거나 열 수 없습니다.", 
					exceptionPath
				);
			} catch (IOException e) {
				throw new IORuntimeException("resource 객체로부터 URL 문자열로 변환 중 예외 발생.");
			}
		}
		
		// 파일의 MIME-TYPE 정보 파악 후 타입 결정 (이미지인지 동영상인지 등을 판별)
		String contentType = null;
		try {
			contentType = Files.probeContentType(filePath);
		} catch (IOException e) {
			contentType = "application/octet-stream";
		}
		
		// 만약 MIME-TYPE을 읽어오지 못한다면 다운로드 작업 수행.
		if (contentType == null) {
			// 해당 타입은 브라우저가 해석하지 못하는 타입이다. 
			// 브라우저가 해석하지 못하는 컨텐트 타입은 무조건 다운로드를 실행하는 
			// 특성이 있으니 이를 이용하는 것이다. 
			contentType = "application/octet-stream";
		}
		log.info("파일의 MIME-TYPE : " + contentType);
		
		// 파일의 원래 이름 가져오기
		String originalFileName = filePath.getFileName().toString();
		
		// CONTENT_DISPOSITION : 해당 파일이 브라우저에서 열리지 않도록 함.
		return ResponseEntity.ok()
			.contentType(MediaType.parseMediaType(contentType))
			.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + originalFileName + "\"")
			.body(resource);
		
	}

	@Override
	public Object deleteByFileId(int id) {
		
		try {
			fileRepository.deleteById(id);
		} catch (IllegalArgumentException e) {
			return false;  // 삭제 실패.
		}
		
		return true; // 삭제 성공
	}
	
}
