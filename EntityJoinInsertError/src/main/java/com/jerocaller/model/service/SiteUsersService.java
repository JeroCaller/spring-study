package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.repository.SiteUsersRepository;

@Service
public class SiteUsersService implements ServiceInter<SiteUsersDto> {
	
	@Autowired
	private SiteUsersRepository siteUsersRepository;
	
	@Autowired
	@Qualifier("siteUsersConverter")
	private DtoEntityConverter<SiteUsersDto, SiteUsers> siteUsersConverter;

	@Override
	public List<SiteUsersDto> selectAll() {
		return siteUsersRepository.findAll()
				.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
	}
	
	public List<SiteUsersDto> selectRecomms() {
		return siteUsersRepository.findByRecommByNotNull()
				.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
	}
	
}
