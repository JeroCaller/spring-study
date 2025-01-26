package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.Members;

public interface MembersRepository 
	extends JpaRepository<Members, String> {

}
