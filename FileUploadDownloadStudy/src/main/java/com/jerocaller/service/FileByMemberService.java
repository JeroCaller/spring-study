package com.jerocaller.service;

import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jerocaller.dto.FilesLogging;
import com.jerocaller.entity.FileEntity;
import com.jerocaller.entity.Members;
import com.jerocaller.exception.DirectoryDeleteFailedException;
import com.jerocaller.repository.FileEntityRepository;
import com.jerocaller.repository.MembersRepository;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 회원 정보 수정 및 삭제 시 뒤따라와야할 파일 작업을 위한 서비스 클래스.
 * 
 * 업로드된 파일들을 유저별로 분류하기 위해 유저별 서브 디렉토리를 만들어 파일을 분류하여 
 * 저장하는 방식은 루트 디렉토리에 모든 유저들의 파일을 저장하는 방식에 비해 별로 좋은 방식은 
 * 아닌 것 같음. 그 이유는 다음과 같음.
 * 
 * 1. 유저 닉네임 변경 또는 삭제 시 해당 유저의 서브 디렉토리 이름을 바꾸고 이를 다시 
 * DB에 반영하거나 삭제하는 기능도 구현해야 하는데, 이로 인해 코드의 양과 복잡성이 증가하고, 
 * 디렉토리명을 변경하는 것도 서버의 자원을 소모하는 일일 수도 있기 때문. 
 * 2. 이미 사용자별 파일 구분은 DB에서 가능하기에 DB 정보만 잘 살아있다면 굳이 
 * 유저별로 서브 디렉토리를 나눠 파일들을 관리할 필요성을 못 느끼겠음. 
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FileByMemberService {
	
	private final FileEntityRepository fileRepository;
	private final MembersRepository membersRepository;
	private final FilesLogging filesLogging;
	
	@Value("${file.upload-dir}")
	private String uploadBaseDir;
	
	private Path uploadBaseDirPath;
	
	@PostConstruct
	public void init() {
		// 파일 업로드를 위한 루트 디렉토리 경로 얻기.
		uploadBaseDirPath = Paths.get(uploadBaseDir);
	}
	
	/**
	 * 회원의 닉네임 수정 시 해당 유저가 보유한 모든 파일 DB 정보의 
	 * 닉네임 정보를 그에 맞게 바꾼다. 
	 * 
	 * @param oldMember
	 * @param newMember
	 * @throws IOException 
	 */
	public void updateUserInfoInFiles(Members oldMember, Members newMember) 
		throws IOException 
	{
		
		// 닉네임 정보가 바뀌지 않았다면 파일 정보 수정 작업도 불필요하므로 종료.
		if (oldMember.getNickname().equals(newMember.getNickname())) return;
		
		// 실제 디렉토리명을 새로운 유저 닉네임에 맞게 바꾼다.
		updateUserDirName(oldMember.getNickname(), newMember.getNickname());
		
		// DB 상에 저장되어 있는 파일의 경로 내에 있는 유저 닉네임을 딴 디렉토리명도 바꾼다.
		fileRepository.updateUserInfoWithPath(oldMember, newMember);
		
	}
	
	/**
	 * 파일을 저장하는 유저 닉네임을 딴 디렉토리명을 새로 바꾼 닉네임으로 변경.
	 * 
	 * 예) 닉네임이 kimquel1234 -> kimquel1111로 바뀐 경우,
	 * ./files/users/kimquel1234 -> ./files/users/kimquel1111로 변경.
	 * 
	 * @param oldName
	 * @param newName
	 * @return - 새로 생성된 회원 디렉토리의 전체 경로 (파일명 미포함).
	 * @throws IOException 
	 */
	private Path updateUserDirName(String oldName, String newName) 
		throws IOException 
	{
		
		Path original = Paths.get(oldName);
		Path fullPath = uploadBaseDirPath.resolve(original);
		log.info("원래 유저 디렉토리 경로: " + fullPath.toString());
		
		Path newNamedDirPath = fullPath.resolveSibling(newName);
		log.info("새로운 유저 디렉토리 경로: " + newNamedDirPath);
		
		// pathA.resolveSibling(pathB) : pathA로 지정된 
		// 경로의 부모 경로와 pathB를 합친 새로운 경로를 반환. 
		// 예) "./files/users/oh".resolveSibling("good")
		// => "./files/users/good"
		
		// 아래 코드는 디렉토리명을 다른 이름으로 바꾸는 기능을 함.
		
		// 참고 자료)
		// https://www.codejava.net/java-se/file-io/how-to-rename-move-file-or-directory-in-java
		// https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Files.html#move(java.nio.file.Path,java.nio.file.Path,java.nio.file.CopyOption...)
		// https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/nio/file/Path.html#resolveSibling(java.lang.String)
		return Files.move(fullPath, newNamedDirPath);
		
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
	 * @throws DirectoryNotEmptyException 
	 */
	public void deleteAllFilesByUsername(String username) throws 
		EntityNotFoundException, 
		DirectoryNotEmptyException,
		DirectoryDeleteFailedException
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
		
		// 유저 닉네임으로 지어진 디렉토리 자체를 삭제.
		Path fullPath = uploadBaseDirPath.resolve(Paths.get(username));
		try {
			Files.deleteIfExists(fullPath);
		} catch (IOException e) {
			if (e instanceof DirectoryNotEmptyException) {
				throw new DirectoryNotEmptyException(
					"해당 유저 닉네임의 디렉토리 삭제 실패. 해당 디렉토리가 비어있지 않기 때문입니다."
				);
			} else {
				throw new DirectoryDeleteFailedException(e.getMessage());
			}
		}
		
	}
	
}
