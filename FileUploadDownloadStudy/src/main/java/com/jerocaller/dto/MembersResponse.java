package com.jerocaller.dto;

import com.jerocaller.entity.Members;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 클라이언트에 응답할 용도의 DTO 클래스.
 * 패스워드 유출 방지 처리함. 
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MembersResponse {
	
	private String nickname;
	
	public static MembersResponse toDto(Members entity) {
		
		return MembersResponse.builder()
				.nickname(entity.getNickname())
				.build();
		
	}
	
}
