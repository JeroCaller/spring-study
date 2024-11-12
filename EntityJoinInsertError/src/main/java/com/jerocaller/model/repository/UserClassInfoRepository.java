package com.jerocaller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jerocaller.model.entity.UserClassInfo;

public interface UserClassInfoRepository
	extends JpaRepository<UserClassInfo, Integer> {
	
	@Query(value = "SELECT MAX(u.classNumber) FROM UserClassInfo u")
	Integer findByIdMax();

}
