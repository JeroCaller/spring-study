package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.UserClassInfo;
import com.jerocaller.model.repository.UserClassInfoRepository;

@Service
public class UserClassInfoService implements ServiceInter{
	
	@Autowired
	private UserClassInfoRepository userClassInfoRepository;
	
	@Autowired
	private DtoEntityConverter<UserClassInfoDto, UserClassInfo> userClassInfoConverter;
	
	@Override
	public void selectAll(Model model, String keyName) {
		List<UserClassInfoDto> results = userClassInfoRepository.findAll()
				.stream()
				.map(userClassInfoConverter :: toDto)
				.collect(Collectors.toList());
		model.addAttribute(keyName, results);
	}

}
