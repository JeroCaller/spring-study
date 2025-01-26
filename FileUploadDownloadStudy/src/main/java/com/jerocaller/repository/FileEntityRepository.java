package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.FileEntity;

public interface FileEntityRepository 
	extends JpaRepository<FileEntity, Integer> {

}
