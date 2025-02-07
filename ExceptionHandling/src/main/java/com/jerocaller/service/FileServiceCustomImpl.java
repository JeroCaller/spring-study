package com.jerocaller.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

import com.jerocaller.exception.FileNotFoundException;
import com.jerocaller.service.interf.FileServiceInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileServiceCustomImpl implements FileServiceInterface<FileServiceCustomImpl> {
	
	private final int MEMBER_LIMIT = 3;
	private final int LOOF_LIMIT = 10;
	private final Random random = new Random();
	
	@Override
	public List<Integer> getFilesIdByMember(int memberId) {
		
		log.info("{} 서비스 객체 호출됨.", this.getClass().getName());
		
		if (memberId > MEMBER_LIMIT) {
			throw new FileNotFoundException();
		}
		
		// 테스트용 가짜 데이터 생성.
		List<Integer> filesId = new ArrayList<Integer>();
		for (int i = 0; i < LOOF_LIMIT; i++) {
			filesId.add(random.nextInt(1, 100));
		}
		
		return filesId;
	}

}
