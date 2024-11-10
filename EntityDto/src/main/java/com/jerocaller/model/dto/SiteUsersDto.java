package com.jerocaller.model.dto;

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
public class SiteUsersDto {
	private Integer memberId;
	private String signUpDate;
	private Integer mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
	
	private ClassesDto classesDto;
	
	public static SiteUsersDto toDto(SiteUsers entity) {
		Classes classes = entity.getClasses();
		ClassesDto classesDto = ClassesDto.toDto(classes);
		
		return SiteUsersDto.builder()
			.memberId(entity.getMemberId())
			.signUpDate(entity.getSignUpDate())
			.mileage(entity.getMileage())
			.username(entity.getUsername())
			.averPurchase(entity.getAverPurchase())
			.recommBy(entity.getRecommBy())
			.classesDto(classesDto)
			.build();
	}
}
