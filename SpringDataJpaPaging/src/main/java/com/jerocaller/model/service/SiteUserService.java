package com.jerocaller.model.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.entity.UserClassInfo;
import com.jerocaller.model.repository.SiteUsersRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SiteUserService implements ServiceInter {
	
	@Autowired
	private SiteUsersRepository siteUsersRepository;
	
	// @Qualifier로 구분하지 않아도 매개변수화 타입 (parameterized type)에서 구분되면 
	// type에 의한 매핑이 여전히 가능하다.
	@Autowired
	private DtoEntityConverter<SiteUsersDto, SiteUsers> siteUsersConverter;
	
	// @Qualifier를 사용하지 않아도 type에 의한 매핑이 되는지 확인하기 위한 테스트용
	@SuppressWarnings("unused")
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
	
	public void selectAllSorted(Model model, String keyName) {
		// 1차 정렬 기준 : 등급 번호별 내림차순으로 정렬
		// SiteUsers 엔티티에 정의된 종속 엔티티 필드명을 그대로 사용하였다.
		Sort mySort = Sort.by(
				Order.asc("userClassInfo.classNumber"),
				Order.desc("averPurchase"),
				Order.desc("mileage")
		);
		List<SiteUsersDto> users = siteUsersRepository.findAll(mySort)
				.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
		model.addAttribute(keyName, users);
	}
	
	/**
	 * Page<Entity> 객체를 Page<SiteUsersDto>로 변환하여 내보낸다.
	 * 
	 * @param model
	 * @param pageRequest
	 */
	public Page<SiteUsersDto> selectPages(String keyName, Pageable pageRequest) {
		Page<SiteUsers> pageWithEntity = siteUsersRepository.findAll(pageRequest);
		
		// Page 인터페이스에 어떤 메서드가 있는지 확인해보기
		log.info("=== From Method [selectPages] ===");
		log.info("페이징 관련 메서드 확인해보기");
		log.info("전체 페이지 수: " + pageWithEntity.getTotalPages());
		log.info("전체 데이터 수: " + pageWithEntity.getTotalElements());
		log.info("현재 페이지 번호 (기본은 인덱스 0부터 시작한다): " + pageWithEntity.getNumber());
		log.info("현재 페이지 하나에 담긴 데이터 수 : " + pageWithEntity.getNumberOfElements());
		log.info("페이지 당 최대 데이터 수: " + pageWithEntity.getSize());
		log.info("다음 페이지 존재 여부: " + pageWithEntity.hasNext());
		log.info("이전 페이지 존재 여부: " + pageWithEntity.hasPrevious());
		log.info("내용 존재 여부: " + pageWithEntity.hasContent());
		
		log.info("=== 페이징 관련 메서드 확인해보기 끝 ===");
		
		List<SiteUsersDto> pageDtos = pageWithEntity.stream()
				.map(siteUsersConverter :: toDto)
				.collect(Collectors.toList());
		
		// PageImpl은 Page라는 인터페이스의 구현체이다. 
		// PageImpl 생성자 매개변수에는 
		// 첫 번째 인자에는 List<Dto> 형태의 데이터를, 
		// 두 번째 인자에는 페이지 요청 정보가 담긴 PageRequest 객체를, 
		// 세 번째 인자에는 전체 데이터의 개수를 대입한다. 
		// PageImpl을 통해 궁극적으로 Page<Entity>를 Page<Dto)로 변환 가능하다.
		Page<SiteUsersDto> result = new PageImpl<SiteUsersDto>(
				pageDtos, 
				pageRequest, 
				pageWithEntity.getTotalElements()
		);
		
		return result;
	}
}
