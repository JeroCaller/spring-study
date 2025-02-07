package com.jerocaller.service;

import org.springframework.stereotype.Service;

import com.jerocaller.exception.MemberNotFoundException;
import com.jerocaller.service.interf.MemberServiceinterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MemberServiceCustomImpl implements MemberServiceinterface<MemberServiceCustomImpl> {
	
	private final int MEMBER_LIMIT = 3;
	
	@Override
	public int getById(int id) {
		
		log.info("{} 서비스 객체 호출됨.", this.getClass().getName());
		
		if (id > MEMBER_LIMIT) {
			throw new MemberNotFoundException();
		}
		
		// 테스트용 데이터로 간주하여 반환.
		return id;
	}

}
