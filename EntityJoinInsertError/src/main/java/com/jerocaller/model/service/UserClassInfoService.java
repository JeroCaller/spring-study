package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.UserClassInfo;
import com.jerocaller.model.repository.UserClassInfoRepository;

@Service
public class UserClassInfoService implements ServiceInter<UserClassInfoDto> {
	
	@Autowired
	private UserClassInfoRepository userClassInfoRepository;
	
	@Autowired
	@Qualifier("userClassInfoConverter")
	private DtoEntityConverter<UserClassInfoDto, UserClassInfo> userClassInfoConverter;

	@Override
	public List<UserClassInfoDto> selectAll() {
		return userClassInfoRepository.findAll()
				.stream()
				.map(userClassInfoConverter :: toDto)
				.collect(Collectors.toList());
	}
	
	
}
