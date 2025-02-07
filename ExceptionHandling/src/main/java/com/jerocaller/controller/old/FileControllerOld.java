package com.jerocaller.controller.old;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.old.ResponseJson;
import com.jerocaller.service.FileServiceImpl;
import com.jerocaller.service.interf.FileServiceInterface;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/old/files")
public class FileControllerOld {
	
	private final FileServiceInterface<FileServiceImpl> fileService;
	
	@GetMapping("/members/{memberId}")
	public ResponseEntity<ResponseJson> getFilesByMemberId(
		@PathVariable("memberId") int memberId
	) {
		
		ResponseJson responseJson = null;
		
		List<Integer> filesId = null;
		
		try {
			filesId = fileService.getFilesIdByMember(memberId);
		} catch (RuntimeException e) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.NOT_FOUND)
				.message(e.getMessage())
				.build();
		}
		
		if (responseJson == null) {
			responseJson = ResponseJson.builder()
				.httpStatus(HttpStatus.OK)
				.message("조회 성공")
				.data(filesId)
				.build();
		}
		
		return responseJson.toResponse();
	}
}
