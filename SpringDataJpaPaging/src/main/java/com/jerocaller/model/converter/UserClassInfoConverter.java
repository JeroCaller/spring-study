package com.jerocaller.model.converter;

import org.springframework.stereotype.Component;

import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.UserClassInfo;

@Component
public class UserClassInfoConverter
	implements DtoEntityConverter<UserClassInfoDto, UserClassInfo> {

	@Override
	public UserClassInfoDto toDto(UserClassInfo entity) {
		UserClassInfoDto dto = new UserClassInfoDto();
		dto.setClassNumber(entity.getClassNumber());
		dto.setClassName(entity.getClassName());
		dto.setBonus(entity.getBonus());
		
		return dto;
	}

	@Override
	public UserClassInfo toEntity(UserClassInfoDto dto) {
		UserClassInfo entity = new UserClassInfo();
		entity.setBonus(dto.getBonus());
		entity.setClassNumber(dto.getClassNumber());
		entity.setClassName(dto.getClassName());
		
		return entity;
	}
	
}
