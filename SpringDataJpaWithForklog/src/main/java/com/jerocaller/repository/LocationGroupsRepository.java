package com.jerocaller.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jerocaller.entity.LocationGroups;

public interface LocationGroupsRepository 
	extends JpaRepository<LocationGroups, Integer> {
	
	@Query(value = "SELECT MAX(lg.no) FROM LocationGroups lg")
	@Deprecated
	Integer findMaxId();
	
	Optional<LocationGroups> findByName(String name);
	
}
