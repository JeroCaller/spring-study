package com.jerocaller.model.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "site_users")
@Getter
@Setter
public class SiteUsers implements EntityInter {
	@Id
	private Integer memberId;
	
	@Column(name = "sign_up_date")
	private LocalDate signUpDate;
	
	@Column(length = 11)
	private Integer mileage;
	
	@Column(length = 50)
	private String username;
	
	@Column(name = "aver_purchase", length = 11)
	private Integer averPurchase;
	
	@Column(name = "recomm_by", length = 50)
	private String recommBy;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "class_number")
	private UserClassInfo userClassInfo;
}
