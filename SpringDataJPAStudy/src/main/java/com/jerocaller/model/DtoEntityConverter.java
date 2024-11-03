package com.jerocaller.model;

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.entity.SiteUsers;

public class DtoEntityConverter {
	
	public static SiteUsersDto toDto(SiteUsers entity) {
		return SiteUsersDto.builder()
			.memberId(entity.getMemberId())
			.signUpDate(entity.getSignUpDate())
			.classNumber(entity.getClassNumber())
			.mileage(entity.getMileage())
			.username(entity.getUsername())
			.averPurchase(entity.getAverPurchase())
			.recommBy(entity.getRecommBy())
			.build();
	}
	
	public static SiteUsers toEntity(SiteUsersDto dto) {
		return SiteUsers.builder()
				.memberId(dto.getMemberId())
				.signUpDate(dto.getSignUpDate())
				.classNumber(dto.getClassNumber())
				.mileage(dto.getMileage())
				.username(dto.getUsername())
				.averPurchase(dto.getAverPurchase())
				.recommBy(dto.getRecommBy())
				.build();
	}
	
}
