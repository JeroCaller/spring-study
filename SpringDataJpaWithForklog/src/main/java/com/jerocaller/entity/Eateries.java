package com.jerocaller.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eateries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Eateries {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private int no;
	
	@Column(nullable = false, length = 255)
	private String name;
	
	@Column(length = 11)
	private int viewCount;
	
	@Column(length = 1000)
	private String thumbnail;
	
	// MariaDB에서 text 타입으로 선언됨을 알림
	@Column(columnDefinition = "text")  
	private String description;
	
	@Column(length = 20)
	private String tel;
	
	// 전체 2자리 수(precision) 중 한 자리(scale)는 소수로 쓰임
	@Column(precision = 2, scale = 1)
	private BigDecimal rating;
	
	@Column(length = 255)
	private String address;
	
	@Column(precision = 11, scale = 8)
	private BigDecimal longitude;
	
	@Column(precision = 10, scale = 8)
	private BigDecimal latitude;
	
	@ManyToOne
	@JoinColumn(name = "category_no", nullable = false)
	private Categories category;
	
}
