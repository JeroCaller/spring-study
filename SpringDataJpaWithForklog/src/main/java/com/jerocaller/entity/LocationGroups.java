package com.jerocaller.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 도로명 주소의 대분류를 표현하는 엔티티.
 * 
 * 예) 서울, 경기 등
 */
@Entity
@Table(name = "location_groups")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationGroups {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private Integer no;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	/*
	@OneToMany(mappedBy = "groups", cascade = CascadeType.ALL)
	private List<Locations> locations;
	*/
	
}
