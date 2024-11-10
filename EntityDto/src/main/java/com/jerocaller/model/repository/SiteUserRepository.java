package com.jerocaller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.model.entity.SiteUsers;

public interface SiteUserRepository extends JpaRepository<SiteUsers, Integer> {
	
}
