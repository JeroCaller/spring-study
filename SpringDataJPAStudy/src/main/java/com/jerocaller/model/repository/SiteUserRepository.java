package com.jerocaller.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.model.entity.SiteUsers;

public interface SiteUserRepository 
	extends JpaRepository<SiteUsers, Integer> {
	
	/**
	 * 클래스 넘버가 특정 넘버이고, 추천자가 있는 모든 유저 조회.
	 * @param classNumber
	 * @return
	 */
	List<SiteUsers> findByClassNumberAndRecommByNotNull(int classNumber);
	
	/**
	 * 마일리지가 minimum 이상 maximum 이하인 유저들을 마일리지 기준 내림차순으로 조회.
	 * @param minimum
	 * @param maximum
	 * @return
	 */
	List<SiteUsers> findByMileageBetweenOrderByMileageDesc(int minimum, int maximum);
}
