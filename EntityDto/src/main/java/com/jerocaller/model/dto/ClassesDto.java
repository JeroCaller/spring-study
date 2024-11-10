package com.jerocaller.model.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.jerocaller.model.entity.Classes;
import com.jerocaller.model.entity.SiteUsers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClassesDto {
	private Integer classNumber;
	private String className;
	private Integer bonus;
	
	// 순환참조 문제 발생
	/*
	private List<SiteUsersDto> usersDto;
	*/
	
	public static ClassesDto toDto(Classes entity) {
		// 순환참조 문제 코드 시작점
		/*
		List<SiteUsers> siteUsers = entity.getUsers();
		List<SiteUsersDto> siteUsersDto = siteUsers.stream()
				.map(SiteUsersDto :: toDto)
				.collect(Collectors.toList()); */
		// 순환참조 문제 코드 끝
		
		return ClassesDto.builder()
			.classNumber(entity.getClassNumber())
			.className(entity.getClassName())
			.bonus(entity.getBonus())
			//.usersDto(siteUsersDto)
			.build();
	}
}
