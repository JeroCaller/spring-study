package com.jerocaller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 클라이언트로부터 넘어온 로그인 정보를 받기 위한 용도의 DTO 클래스
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MembersRequest {
	
	private String nickname;
	private String password;
	
}
