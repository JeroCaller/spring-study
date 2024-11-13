package com.jerocaller.model.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.entity.UserClassInfo;

@Component
public class SiteUsersConverter 
	implements DtoEntityConverter<SiteUsersDto, SiteUsers> {
	
	@Autowired
	@Qualifier(value = "userClassInfoConverter")
	private DtoEntityConverter<UserClassInfoDto, UserClassInfo> converter;
	
	@Override
	public SiteUsersDto toDto(SiteUsers entity) {
		SiteUsersDto dto = new SiteUsersDto();
		dto.setMemberId(entity.getMemberId());
		dto.setSignUpDate(entity.getSignUpDate());
		dto.setMileage(entity.getMileage());
		dto.setUsername(entity.getUsername());
		dto.setAverPurchase(entity.getAverPurchase());
		dto.setRecommBy(entity.getRecommBy());
		dto.setUserClassInfoDto(converter.toDto(entity.getUserClassInfo()));
		
		return dto;
	}

	@Override
	public SiteUsers toEntity(SiteUsersDto dto) {
		SiteUsers entity = new SiteUsers();
		entity.setMemberId(dto.getMemberId());
		entity.setSignUpDate(dto.getSignUpDate());
		entity.setMileage(dto.getMileage());
		entity.setUsername(dto.getUsername());
		entity.setAverPurchase(dto.getAverPurchase());
		entity.setRecommBy(dto.getRecommBy());
		entity.setUserClassInfo(converter.toEntity(dto.getUserClassInfoDto()));
		
		return entity;
	}
	
	
}
