package com.jerocaller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보 수정 시 이력을 보여주기 위한 용도의 DTO 클래스.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersHistory {

	private String pastNickname; // 수정 전 닉네임
	private String newNickname; // 수정 후 닉네임
	
}
