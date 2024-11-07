package com.jerocaller.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SiteUsers {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer memberId;
	private String signUpDate;
	private Integer mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "class_number") // site_users 테이블의 필드.
	private Classes superEntity;
}
