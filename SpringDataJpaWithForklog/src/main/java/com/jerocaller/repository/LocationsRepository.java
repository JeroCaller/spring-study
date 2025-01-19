package com.jerocaller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jerocaller.entity.LocationGroups;
import com.jerocaller.entity.Locations;

public interface LocationsRepository 
	extends JpaRepository<Locations, Integer> {
	
	@Query(value = "SELECT MAX(l.no) FROM Locations l")
	Integer findMaxId();
	
	/**
	 * 문자열 타입으로 주어진 도로명 주소 중분류, 
	 * Entity 형태로 주어진 도로명 주소 대분류 정보에 일치하는 
	 * 엔티티 반환. 
	 * 
	 * @param name
	 * @param groups
	 * @return
	 */
	Optional<Locations> findByNameAndGroups(
			String name, 
			LocationGroups groups
	);
	
	Optional<Locations> findByName(String name);
	
}
