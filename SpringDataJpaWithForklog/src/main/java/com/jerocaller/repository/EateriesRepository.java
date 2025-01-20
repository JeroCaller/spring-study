package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.Eateries;

public interface EateriesRepository 
	extends JpaRepository<Eateries, Integer> {

}
