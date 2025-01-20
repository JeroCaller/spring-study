package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.CategoryGroups;

public interface CategoryGroupsRepository 
	extends JpaRepository<CategoryGroups, Integer> {
	
}
