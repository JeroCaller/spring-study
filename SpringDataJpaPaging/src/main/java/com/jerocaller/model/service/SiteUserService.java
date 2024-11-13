package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.entity.UserClassInfo;
import com.jerocaller.model.repository.SiteUsersRepository;

@Service
public class SiteUserService implements ServiceInter {
	
	@Autowired
	private SiteUsersRepository siteUsersRepository;
	
	// @Qualifier로 구분하지 않아도 매개변수화 타입 (parameterized type)에서 구분되면 
	// type에 의한 매핑이 여전히 가능하다.
	@Autowired
	private DtoEntityConverter<SiteUsersDto, SiteUsers> siteUsersConverter;
	
	// @Qualifier를 사용하지 않아도 type에 의한 매핑이 되는지 확인하기 위한 테스트용
	@Autowired
	private DtoEntityConverter<UserClassInfoDto, UserClassInfo> userClassInfoConverter;
	
	@Override
	public void selectAll(Model model, String keyName) {
		List<SiteUsersDto> users = siteUsersRepository.findAll()
				.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
		model.addAttribute(keyName, users);
	}
}
