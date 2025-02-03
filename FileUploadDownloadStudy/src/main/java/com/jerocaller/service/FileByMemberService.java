package com.jerocaller.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jerocaller.dto.FilesLogging;
import com.jerocaller.entity.FileEntity;
import com.jerocaller.entity.Members;
import com.jerocaller.repository.FileEntityRepository;
import com.jerocaller.repository.MembersRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 정보 수정 및 삭제 시 뒤따라와야할 파일 작업을 위한 서비스 클래스.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileByMemberService {
	
	private final FileEntityRepository fileRepository;
	private final MembersRepository membersRepository;
	private final FilesLogging filesLogging;
	
	/**
	 * 회원의 닉네임 수정 시 해당 유저가 보유한 모든 파일 DB 정보의 
	 * 닉네임 정보를 그에 맞게 바꾼다. 
	 * 
	 * @param oldMember
	 * @param newMember
	 */
	public void updateUserInfoInFiles(Members oldMember, Members newMember) {
		fileRepository.updateUserInfo(oldMember, newMember);
	}
	
	/**
	 * 회원 탈퇴 시 해당 회원이 보유한 모든 파일을 삭제. 
	 * 
	 * 절차)
	 * 1. 해당 유저 네임에 해당하는 모든 파일 정보들을 가져온다. 
	 * 2. 폴더 상에 존재하는 해당 파일들을 모두 삭제한다. 
	 * 3. DB 상에 존재하는 파일 정보들을 삭제한다. (2번과 3번 순서가 뒤바뀌면 안됨)
	 * 
	 * @param username
	 */
	public void deleteAllFilesByUsername(String username) 
		throws EntityNotFoundException 
	{
		
		filesLogging.clearAll();
		 
		// 유저 닉네임으로 파일들을 찾아 삭제하기 위해 
		// repository로부터 해당 닉네임을 가지는 회원 정보 Entity를 가져온다. 
		Members member = membersRepository.findById(username)
			.orElseThrow(() -> new EntityNotFoundException(
				"파일 삭제 작업 전 예외 발생: 해당 유저가 DB 상에 존재하지 않음. 유저 정보: " + username
			));
		
		List<FileEntity> files = fileRepository.findByMembers(member);
		
		// 유저의 파일이 없을 경우 파일 삭제 작업은 불필요하므로 여기서 종료.
		if (files == null || files.size() == 0) return;
		 
		for (FileEntity file: files) {
			Path filePath = Paths.get(file.getFilePath());
			
			// 먼저 서버 내에 물리적으로 존재하는 파일들을 모두 삭제한다.
			try {
				Files.deleteIfExists(filePath);
			} catch (IOException e) {
				// 다른 파일들의 삭제를 위해 
				// 예외가 발생해도 다음 파일로 넘어가도록 함.
				// 대신 실패한 파일 경로 및 원인을 기록.
				filesLogging.getFailedPaths().put(
					file.getFilePath(), 
					"물리적 파일 삭제 실패. \n" + e.getMessage()
				);
				continue;
			}
			
			// DB 내 해당 파일 정보들을 삭제 
			try {
				fileRepository.delete(file);
			} catch (Exception e) {
				filesLogging.getFailedPaths().put(
					file.getFilePath(),
					"DB 삭제 실패. \n" + e.getMessage()
				);
				continue;
			}
			
			// 여기까지 오면 파일의 물리적 삭제 및 DB 내 삭제 
			// 모두 성공으로 간주.
			filesLogging.getSucceedFileNames().add(file.getFilePath());
		}
		
	}
	
}
