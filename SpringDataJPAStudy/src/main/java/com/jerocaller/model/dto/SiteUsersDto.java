package com.jerocaller.model.dto;

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
	private int memberId;
	private String signUpDate;
	private int classNumber;
	private int mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
}
