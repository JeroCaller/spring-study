package com.jerocaller.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.model.entity.UserClassInfo;

public interface UserClassInfoRepository
	extends JpaRepository<UserClassInfo, Integer> {

}
