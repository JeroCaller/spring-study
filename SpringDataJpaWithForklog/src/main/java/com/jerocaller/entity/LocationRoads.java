package com.jerocaller.entity;

import jakarta.persistence.CascadeType;
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

/**
 * 도로명 주소 중 도로명을 표현하는 엔티티
 * 
 * 예) XX대로
 */
@Entity
@Table(name = "location_roads")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationRoads {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private Integer no;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@ManyToOne(cascade = CascadeType.PERSIST)  // 영속성 전이
	//@ManyToOne
	@JoinColumn(name = "location_no", nullable = false)
	private Locations locations;
	
}
