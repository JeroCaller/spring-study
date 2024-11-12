package com.jerocaller.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jerocaller.model.entity.SiteUsers;

public interface SiteUsersRepository
	extends JpaRepository<SiteUsers, Integer> {
	
	@Query(value = "SELECT MAX(s.memberId) FROM SiteUsers s")
	Integer findByIdMax();
	
	List<SiteUsers> findByRecommByNotNull();
}