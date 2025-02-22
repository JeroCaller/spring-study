package com.jerocaller.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도로명 주소의 중분류를 표현하는 엔티티
 * 
 * 예) 강남구, 평택시 등등
 */
@Entity
@Table(name = "locations")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Locations {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private Integer no;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@ManyToOne(cascade = CascadeType.PERSIST) // 영속성 전이
	//@ManyToOne
	@JoinColumn(name = "group_no", nullable = false)
	private LocationGroups groups;
	
	/*
	@OneToMany(mappedBy = "locations", cascade = CascadeType.ALL)
	private List<LocationRoads> roads;
	*/
}
