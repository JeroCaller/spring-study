package com.jerocaller.service;

import org.springframework.stereotype.Service;

import com.jerocaller.service.interf.MemberServiceinterface;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MemberServiceImpl implements MemberServiceinterface<MemberServiceImpl> {
	
	private final int MEMBER_LIMIT = 3;

	@Override
	public int getById(int id) {
		
		if (id > MEMBER_LIMIT) {
			throw new RuntimeException("해당 번호의 회원을 찾을 수 없습니다.");
		}
		
		if (id < 0) {
			throw new EntityNotFoundException("음수 번호에 해당하는 회원은 없습니다.");
		}
		
		// 테스트용 데이터로 간주하여 반환.
		return id;
	}

}
