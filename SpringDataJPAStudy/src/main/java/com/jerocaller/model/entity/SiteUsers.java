package com.jerocaller.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "site_users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteUsers {
	@Id
	@Column(name = "member_id")
	private int memberId;
	
	private String signUpDate;
	private int classNumber;
	private int mileage;
	private String username;
	private Integer averPurchase;
	private String recommBy;
	
}
