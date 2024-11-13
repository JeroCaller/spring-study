package com.jerocaller.model.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.jerocaller.controller.formbean.BeanDtoConverter;
import com.jerocaller.controller.formbean.InsertRequest;
import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.repository.SiteUsersRepository;

@Service
public class SiteUsersService implements ServiceInter<SiteUsersDto> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiteUsersRepository siteUsersRepository;
	
	@Autowired
	@Qualifier("siteUsersConverter")
	private DtoEntityConverter<SiteUsersDto, SiteUsers> siteUsersConverter;
	
	@Autowired
	private BeanDtoConverter beanDtoConverter;

	@Override
	public List<SiteUsersDto> selectAll() {
		return siteUsersRepository.findAll()
				.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
	}
	
	/**
	 * 모든 추천인 정보들을 중복 및 null, 빈값 없이 문자열 형태로 가져온다. 
	 * @return
	 */
	public List<String> selectAllUsername() {
		return siteUsersRepository.findAllUsername();
	}
	
	/**
	 * 사이트 회원 한 명을 추가한다. 
	 * @param insertRequest
	 * @return 삽입된 엔티티의 DTO 반환.
	 */
	public SiteUsersDto saveOne(InsertRequest insertRequest) {
		SiteUsersDto savedResult = null;
		try {
			SiteUsersDto targetDto 
				= beanDtoConverter.convertRequestToDto(insertRequest);
			SiteUsers savedEntity = siteUsersRepository.save(
					siteUsersConverter.toEntity(targetDto)
			);
			savedResult = siteUsersConverter.toDto(savedEntity);
		} catch (Exception e) {
			logger.error("From Method [saveOneSiteUser] : 에러 발생!");
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
		
		return savedResult;
	}
	
}
