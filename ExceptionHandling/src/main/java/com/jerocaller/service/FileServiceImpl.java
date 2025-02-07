package com.jerocaller.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.jerocaller.service.interf.FileServiceInterface;

@Service
public class FileServiceImpl implements FileServiceInterface<FileServiceImpl> {
	
	private final int MEMBER_LIMIT = 3;
	private final int LOOF_LIMIT = 10;
	private final Random random = new Random();
	
	@Override
	public List<Integer> getFilesIdByMember(int memberId) {
		
		if (memberId > MEMBER_LIMIT) {
			throw new RuntimeException("해당 회원이 보유한 파일이 존재하지 않습니다.");
		}
		
		// 테스트용 가짜 데이터 생성.
		List<Integer> filesId = new ArrayList<Integer>();
		for (int i = 0; i < LOOF_LIMIT; i++) {
			filesId.add(random.nextInt(1, 100));
		}
		
		return filesId;
	}
	
}
