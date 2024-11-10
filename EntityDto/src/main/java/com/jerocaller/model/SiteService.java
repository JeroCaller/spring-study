package com.jerocaller.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.repository.SiteUserRepository;

@Service
public class SiteService {
	
	@Autowired
	private SiteUserRepository siteUserRepository;
	
	/**
	 * 모든 사이트 유저 정보 반환
	 * 
	 * @return
	 */
	public List<SiteUsersDto> selectAllUsers() {
		return siteUserRepository.findAll()
				.stream()
				.map(SiteUsersDto :: toDto)
				.collect(Collectors.toList());
	}
	
}
