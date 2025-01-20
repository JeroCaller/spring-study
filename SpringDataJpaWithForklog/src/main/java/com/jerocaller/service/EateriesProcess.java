package com.jerocaller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jerocaller.entity.Eateries;
import com.jerocaller.repository.EateriesRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EateriesProcess {

	private final EateriesRepository eateriesRepository;
	
	public List<Eateries> getAll() {
		return eateriesRepository.findAll();
	}
	
	/**
     * 특정 음식점의 조회수 1 증가 시키는 메서드.
     * 
     * 참고) 
     * Dirty Checking을 이용하여 update하는 방법 사용 시에는 
     * 업데이트 내용을 반영하기 위해 반드시 @Transactional 어노테이션을 부여해야 함.
     * 그렇지 않으면 내용이 DB에 반영되지 않음을 확인함.
     * 
     * @param eateryNo - 음식점 엔티티 No
     * @return - true 시 특정 음식점 조회수 1 증가 DB 반영 성공. false 시 입력된 no 값에 
     * 해당하는 음식점 데이터를 DB에서 조회하지 못함을 의미.
     */
	@Transactional
	public boolean addOneViewCount(int eateryNo) {
		
		Optional<Eateries> eateryOpt = eateriesRepository
			.findById(eateryNo);
		if (eateryOpt.isEmpty()) return false;
		
		Eateries eatery = eateryOpt.get();
		
		// Dirty Checking을 이용하여 조회수 1 증가하도록 update.
    	// 즉, 새로운 엔티티 객체를 생성하여 repository에 save하는 방식이 아닌, 
    	// 이미 기존 영속성 컨텍스트에 존재하는 엔티티 객체 참조를 가져와 
    	// 해당 객체의 특정 필드값을 setter 메서드로 변경하는 방식임. 
    	// Dirty Checking 이용 시 repository.save()를 이용하지 않아도 
    	// 자동으로 업데이트 됨. 단 @Transactional 어노테이션을 반드시 부여해야 
    	// 실제 DB에 업데이트된 값이 반영됨. 
		eatery.setViewCount(eatery.getViewCount() + 1);
		
		return true;
		
	}
	
}
