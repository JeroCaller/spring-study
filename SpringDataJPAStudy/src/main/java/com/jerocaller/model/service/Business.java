package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerocaller.model.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.repository.SiteUserRepository;

@Service
public class Business {
	
	@Autowired
	private SiteUserRepository repository;
	
	/**
	 * 모든 필드를 포함하여 모든 데이터 가져오기.
	 * @return - Entity 객체를 Dto 객체로 변경하여 반환. 
	 */
	public List<SiteUsersDto> getUserAll() {
		/*
		return repository.findAll()
				.stream()
				.map(DtoEntityConverter :: toDto)
				.collect(Collectors.toList());
		*/
		return resultToDtos(repository.findAll());
	}
	
	public List<SiteUsersDto> getUsersByClassNumberAndRecommByNotNull(int classNumber) {
		/*
		return repository.findByClassNumberAndRecommByNotNull(classNumber)
				.stream()
				.map(DtoEntityConverter :: toDto)
				.collect(Collectors.toList());*/
		return resultToDtos(repository.findByClassNumberAndRecommByNotNull(classNumber));
	}
	
	public List<SiteUsersDto> getUsersByMileageBetween(int minimum, int maxinum) {
		return resultToDtos(repository.findByMileageBetweenOrderByMileageDesc(minimum, maxinum));
	}
	
	private List<SiteUsersDto> resultToDtos(List<SiteUsers> entityResults) {
		return entityResults.stream()
				.map(DtoEntityConverter :: toDto)
				.collect(Collectors.toList());
	}
}
