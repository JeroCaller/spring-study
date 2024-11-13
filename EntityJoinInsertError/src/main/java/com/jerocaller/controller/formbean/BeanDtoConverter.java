package com.jerocaller.controller.formbean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.jerocaller.model.converter.DtoEntityConverter;
import com.jerocaller.model.dto.SiteUsersDto;
import com.jerocaller.model.dto.UserClassInfoDto;
import com.jerocaller.model.entity.UserClassInfo;
import com.jerocaller.model.repository.SiteUsersRepository;
import com.jerocaller.model.repository.UserClassInfoRepository;

@Component
public class BeanDtoConverter {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private SiteUsersRepository siteUsersRepository;
	
	@Autowired
	private UserClassInfoRepository userClassInfoRepository;
	
	@Autowired
	@Qualifier("userClassInfoConverter")
	private DtoEntityConverter<UserClassInfoDto, UserClassInfo> userClassInfoConverter;
	
	/**
	 * 사용자 요청 정보를 DTO로 변경한다. 변경된 DTO를 repository에 전달하여 
	 * 해당 데이터를 저장하기 위함.
	 * 
	 * @param insertRequest
	 * @return - SiteUsersDto 내부에 이미 변환된 UserClassInfoDto도 포함되어 있음. 
	 */
	public SiteUsersDto convertRequestToDto(InsertRequest insertRequest) {
		// SiteUser Id에는 현재 DB에 있는 가장 큰 Id값보다 1 더 큰 값을 삽입하도록 한다. 
		// AUTO_INCREMENT를 재현.
		Integer maxNum = siteUsersRepository.findByIdMax();
		if (maxNum == null) {
			maxNum = 1;
		} else {
			maxNum += 1;
		}
		
		logger.info("From Method [convertRequestToDto], maxNum : " + maxNum);
		
		SiteUsersDto dto = new SiteUsersDto();
		dto.setMemberId(maxNum);
		dto.setUsername(insertRequest.getUsername());
		dto.setAverPurchase(insertRequest.getAverPurchase());
		dto.setMileage(insertRequest.getMileage());
		if (insertRequest.getRecommBy().isEmpty()) {
			insertRequest.setRecommBy(null);
		}
		dto.setRecommBy(insertRequest.getRecommBy());
		
		logger.info("From method [convertRequestToDto], formBean.classNumber: " + insertRequest.getClassNumber());
		
		// SiteUsersDto에 삽입할 UserClassInfoDto를 얻어오는 로직. 
		UserClassInfo targetClassInfo = null;
		if (insertRequest.getClassNumber() == null) {
			targetClassInfo = getAlterIfNotExistClassNumber();
		} else {
			targetClassInfo = userClassInfoRepository.findById(
					insertRequest.getClassNumber()
			).orElseGet(() -> getAlterIfNotExistClassNumber());
		}
		
		
		logger.info("From method [convertRequestToDto], targetClassInfo: " + targetClassInfo.getClassNumber());
		
		UserClassInfoDto userClassInfoDto = userClassInfoConverter.toDto(targetClassInfo);
		dto.setUserClassInfoDto(userClassInfoDto);
		
		return dto;
	}
	
	/**
	 * 사용자가 입력한 클래스 넘버에 해당하는 값을 찾지 못했다는 것은 
	 * classes 테이블에 해당 데이터가 없다는 뜻으로, 
	 * 이 경우 pk 컬럼에서 가장 큰 값을 가지는 숫자에 해당하는 정보를 가져온다. 
	 * 즉, 맨 아래 계급부터 시작하게 하도록 한다. 
	 * 
	 * @return - 최하위 계급 정보 엔티티
	 */
	private UserClassInfo getAlterIfNotExistClassNumber() {
		String infoMsg = """
				From method [getAlterIfNotExistClassNumber] : 
				사용자가 입력한 클래스 넘버에 해당하는 레코드가 DB에 없습니다. 
				그래서 가장 큰 클래스 넘버를 가지는 레코드로 대신합니다.
			""";
		logger.info(infoMsg);
					
		Integer maxNumForClassInfo = userClassInfoRepository.findByIdMax();
		return userClassInfoRepository.findById(maxNumForClassInfo).get();
	}
}
