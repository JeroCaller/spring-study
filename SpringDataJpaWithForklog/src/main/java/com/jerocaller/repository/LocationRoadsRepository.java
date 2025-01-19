package com.jerocaller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jerocaller.entity.LocationRoads;
import com.jerocaller.entity.Locations;

public interface LocationRoadsRepository extends JpaRepository<LocationRoads, Integer> {
	
	@Query(value = "SELECT MAX(lr.no) FROM LocationRoads lr")
	Integer findMaxId();
	
	/**
	 * 문자열 타입으로 주어진 도로명 주소의 중분류와 
	 * Entity 타입으로 주어진 도로명 주소의 도로명 정보와 일치하는 
	 * 도로명 정보를 Entity로 반환.
	 * 
	 * @param name
	 * @param locations
	 * @return
	 */
	Optional<LocationRoads> findByNameAndLocations(
			String name, 
			Locations locations
	);
	
}
