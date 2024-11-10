package com.jerocaller.model.entity;

import com.jerocaller.model.dto.SiteUsersDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUsers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;
	private String signUpDate;
	private Integer mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
	
	@ManyToOne
	@JoinColumn(name = "class_number")
	private Classes classes;
	
	public static SiteUsers toEntity(SiteUsersDto dto) {
		return SiteUsers.builder()
				.memberId(dto.getMemberId())
				.signUpDate(dto.getSignUpDate())
				.mileage(dto.getMileage())
				.username(dto.getUsername())
				.averPurchase(dto.getAverPurchase())
				.recommBy(dto.getRecommBy())
				.build();
	}
}
