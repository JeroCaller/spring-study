package com.jerocaller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.FileEntity;
import com.jerocaller.entity.Members;

public interface FileEntityRepository 
	extends JpaRepository<FileEntity, Integer> {
	
	List<FileEntity> findByMembers(Members member);
	
}
