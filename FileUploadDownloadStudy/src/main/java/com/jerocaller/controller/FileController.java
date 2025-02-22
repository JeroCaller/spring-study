package com.jerocaller.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.jerocaller.common.ResponseMessages;
import com.jerocaller.dto.FileResponse;
import com.jerocaller.dto.FileSuccessFailed;
import com.jerocaller.dto.ResponseJson;
import com.jerocaller.exception.NotAuthenticatedUserException;
import com.jerocaller.service.FileServiceInterface;
import com.jerocaller.service.FileServiceTypeOneImpl;
import com.jerocaller.util.AuthenticationUtils;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

/**
 * 좀 더 엄격하게 하고자 한다면 A 사용자가 B 사용자의 파일을 
 * 열람, 다운로드하거나 새 파일 삽입하지 않도록 방지하는 기능도 추가할 수 있음. 
 */
@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {
	
	private final FileServiceInterface<FileServiceTypeOneImpl> fileService;
	
	/**
	 * 클라이언트로부터 여러 개의 파일 업로드 요청 시 이를 처리한다. 
	 * 
	 * 프론트엔드로부터 요청이 들어오면 \@RequestParam 파라미터로 받아도 
	 * 파일을 업로드할 수 있는 것으로 알고 있었으나, Swagger에서 테스트할 때에는 
	 * \@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) 및
	 * \@RequestPart(name = "files") MultipartFile[] files 
	 * 와 같이 consumes 설정과 @RequestPart를 사용해야 작동되는 것을 확인함. 
	 * 
	 * @param files - 파일 전송 시 파일 정보는 request의 body가 아닌 
	 * header에 쿼리스트링 형태로 실어 전송해야한다.
	 * @return
	 */
	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<ResponseJson> uploadFiles(
		//@RequestParam("files") MultipartFile[] files
		@RequestPart(name = "files") MultipartFile[] files
	) {

		ResponseJson responseJson = null;
		
		List<String> results = (List<String>) fileService.uploadFiles(files);
		Map<String, String> failed = fileService.getFailedPaths();
		FileSuccessFailed fileResults = FileSuccessFailed.builder()
			.success(results)
			.failed(failed)
			.build();
		
		if (results.size() == 0) {
			// 클라이언트가 요청한 파일 모두 업로드에 실패한 경우.
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.message("요청한 파일 모두 업로드에 실패하였습니다.")
				.data(fileResults)
				.build();
		} else if (failed.size() > 0) {
			// 클라이언트가 요청한 파일들 중 일부만 업로드 성공한 경우.
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.PARTIAL_CONTENT)
				.message("요청한 파일들 중 일부만 업로드에 성공하였습니다.")
				.data(fileResults)
				.build();
		} else {
			// 모든 요청한 파일들이 업로드에 성공한 경우
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.CREATED)
				.message("요청한 모든 파일 업로드 성공")
				.data(fileResults)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
	/**
	 * 현재 유저가 서버에 저장한 모든 파일들의 정보를 응답한다. 
	 * 
	 * 파일을 다운로드하는 기능이 아니고 그저 파일들의 정보만을 
	 * 응답하는 기능임에 주의.
	 * 
	 * @return
	 */
	@GetMapping
	public ResponseEntity<ResponseJson> getFileDetails() {
		
		ResponseJson responseJson = null;
		List<FileResponse> results = null;
		
		try {
			results = fileService.getFiles(); 
		} catch (EntityNotFoundException | NotAuthenticatedUserException e) {
			return ResponseJson.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(e.getMessage())
				.build()
				.toResponseEntity();
		}
		
		if (results.size() == 0) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message("조회된 파일 데이터가 없습니다.")
				.build();
		} else {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message(ResponseMessages.READ_SUCCESS)
				.data(results)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
	@GetMapping("/download/{id}")
	public ResponseEntity download(@PathVariable("id") int id) {
		
		Object result = null;
		ResponseJson responseOnlyIfFailed = null;
		
		// 인증된 유저만 자신의 사진을 다운로드 할 수 있도록 하기 위해 
		// 인증 여부 검사 후 미인증 시 다운로드 방지
		if (AuthenticationUtils.isAnonymous()) {
			return ResponseJson.builder()
				.httpStatus(HttpStatus.UNAUTHORIZED)
				.message(ResponseMessages.UNAUTHORIZED)
				.build()
				.toResponseEntity();
		}
		
		try {
			result = fileService.downloadByFileId(id);
		} catch (EntityNotFoundException enfe) {
			responseOnlyIfFailed = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(enfe.getMessage())
				.build();
		} catch (RuntimeException e) {
			responseOnlyIfFailed = ResponseJson.builder()
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(e.getMessage())
				.build();
		}
		
		if (responseOnlyIfFailed != null) {
			return responseOnlyIfFailed.toResponseEntity();
		}
		
		return (ResponseEntity) result;
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseJson> deleteOneFile(@PathVariable("id") int id) {
		
		ResponseJson responseJson = null;
		
		try {
			fileService.deleteByFileId(id);
		} catch (EntityNotFoundException enfe) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(enfe.getMessage())
				.build();
		} catch (RuntimeException e) {
			// 그 외 모든 커스텀 예외들은 모두 서버 내부에서 
			// 작업 실행 중 발생한 것이기에 INTERNAL SERVER ERROR로 
			// 응답 상태를 통일.
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message(ResponseMessages.DELETE_SUCCESS)
				.build();
		}
		
		return responseJson.toResponseEntity();
		
	}
	
}
