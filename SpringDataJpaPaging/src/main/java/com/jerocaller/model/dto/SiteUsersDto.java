package com.jerocaller.model.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SiteUsersDto {
	private Integer memberId;
	private LocalDate signUpDate;
	private Integer mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
	//private UserClassInfo userClassInfo;
	
	private UserClassInfoDto userClassInfoDto;
}
