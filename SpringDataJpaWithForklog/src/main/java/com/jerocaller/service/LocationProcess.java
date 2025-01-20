package com.jerocaller.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jerocaller.dto.LocationRequest;
import com.jerocaller.entity.LocationGroups;
import com.jerocaller.entity.LocationRoads;
import com.jerocaller.entity.Locations;
import com.jerocaller.repository.LocationGroupsRepository;
import com.jerocaller.repository.LocationRoadsRepository;
import com.jerocaller.repository.LocationsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationProcess {
	
	private final LocationGroupsRepository locationGroupsRepository;
	private final LocationsRepository locationsRepository;
	private final LocationRoadsRepository locationRoadsRepository;
	
	public List<LocationGroups> getAll() {
		return locationGroupsRepository.findAll();
	}
	
	/**
	 * 주어진 도로명 주소 대분류, 중분류, 도로명 정보를 DB에 저장.
	 * 
	 * @param requestDto
	 */
	@Transactional
	public void insertLocationWrong(LocationRequest requestDto) {
		
		LocationGroups groups = locationGroupsRepository
			.findByName(requestDto.getLarge())
			.orElse(
				LocationGroups.builder()
					//.no(getNextId(KindOfLocation.LARGE))
					.name(requestDto.getLarge())
					.build()
			);
		
		// 엔티티에 cascade 영속성 전이를 설정하더라도 종속 엔티티(여기선 groups)가 
		// DB 내에 존재하지 않을 경우 종속 엔티티 자체를 주인 엔티티 조회 조건으로 사용할 수 없다. 
		// 그래서 영속성 전이를 설정하더라도 에러는 여전히 뜬다. 
		// 참고로 굳이 종속 엔티티까지를 주인 엔티티의 조회 조건으로 사용한 이유는 
		// 하위 분류에 해당하는 주소명이 겹칠 수도 있기에 (예 - 경기도 "광주", 전라도 "광주") 
		// 이에 대한 혼선을 줄이고 정확한 작업을 하기 위함. 
		Locations locations = locationsRepository
			.findByNameAndGroups(requestDto.getMiddle(), groups)
			.orElse(
				Locations.builder()
					//.no(getNextId(KindOfLocation.MIDDLE))
					.name(requestDto.getMiddle())
					.groups(groups)
					.build()
			);
		
		Optional<LocationRoads> roadsOpt = locationRoadsRepository
			.findByNameAndLocations(requestDto.getRoad(), locations);
		if (roadsOpt.isEmpty()) {
			LocationRoads roads = LocationRoads.builder()
				//.no(getNextId(KindOfLocation.ROAD))
				.name(requestDto.getRoad())
				.locations(locations)
				.build();
			locationRoadsRepository.save(roads);
			log.info("새 도로명 주소 데이터 삽입 완료");
		} else {
			log.info("이미 DB에 해당 데이터가 존재하므로 삽입 작업은 일어나지 않았습니다.");
		}
		
	}
	
	/**
	 * 주어진 도로명 주소 대분류, 중분류, 도로명 정보를 DB에 저장.
	 * 
	 * @param requestDto
	 */
	@Transactional
	public void insertLocationTwo(LocationRequest requestDto) {
		
		LocationGroups groups = null;
		Optional<LocationGroups> groupsOpt = locationGroupsRepository
			.findByName(requestDto.getLarge());
		if (groupsOpt.isEmpty()) {
			groups = locationGroupsRepository.save(
				LocationGroups.builder()
					//.no(getNextId(KindOfLocation.LARGE))
					.name(requestDto.getLarge())
					.build()
			);
		} else {
			groups = groupsOpt.get();
		}
		
		Locations locations = null;
		Optional<Locations> locationsOpt = locationsRepository
			.findByNameAndGroups(requestDto.getMiddle(), groups);
		if (locationsOpt.isEmpty()) {
			locations = locationsRepository.save(
				Locations.builder()
					//.no(getNextId(KindOfLocation.MIDDLE))
					.name(requestDto.getMiddle())
					.groups(groups)
					.build()
			);
		} else {
			locations = locationsOpt.get();
		}
		
		LocationRoads roads = null;
		Optional<LocationRoads> roadsOpt = locationRoadsRepository
			.findByNameAndLocations(requestDto.getRoad(), locations);
		if (roadsOpt.isEmpty()) {
			roads = locationRoadsRepository.save(
				LocationRoads.builder()
					//.no(getNextId(KindOfLocation.ROAD))
					.name(requestDto.getRoad())
					.locations(locations)
					.build()
			);
		} else {
			roads = roadsOpt.get();
		}
		
	}
	
	@Transactional
	public void insertLocationThree(LocationRequest requestDto) throws Exception {
		
		log.info("대분류: " + requestDto.getLarge());
		log.info("중분류: " + requestDto.getMiddle());
		log.info("도로명: " + requestDto.getRoad());
		
		// DB에 삽입하고자 하는 주소명이 이미 존재한다면 삽입 작업 불필요.
		Optional<LocationGroups> groupsOpt = locationGroupsRepository
			.findByName(requestDto.getLarge());
		
		// 조회 결과가 없다면 새 데이터 삽입
		if (groupsOpt.isEmpty()) {
			locationGroupsRepository.save(
				LocationGroups.builder()
					.name(requestDto.getLarge())
					.build()
			);
		}
		
		// Locations의 FK 삽입용
		LocationGroups groupForFK = locationGroupsRepository
			.findByName(requestDto.getLarge())
			.orElseThrow(() -> new Exception("도로명 주소 대분류 엔티티 조회 실패"));
		
		Optional<Locations> locationsOpt = locationsRepository
			.findByNameAndGroups(requestDto.getMiddle(), groupForFK);
		if (locationsOpt.isEmpty()) {
			locationsRepository.save(
				Locations.builder()
					.name(requestDto.getMiddle())
					.groups(groupForFK)
					.build()
			);
		}
		
		// LocationRoads의 FK 삽입용
		Locations locationsForFK = locationsRepository
			.findByName(requestDto.getMiddle())
			.orElseThrow(() -> new Exception("도로명 주소 중분류 엔티티 조회 실패"));
		
		Optional<LocationRoads> roadsOpt = locationRoadsRepository
			.findByNameAndLocations(requestDto.getRoad(), locationsForFK);
		if (roadsOpt.isEmpty()) {
			locationRoadsRepository.save(
				LocationRoads.builder()
					.name(requestDto.getRoad())
					.locations(locationsForFK)
					.build()
			);
		}
		
	}
	
	@Transactional
	public void insertFullLocations(LocationRequest request) {
		
		LocationGroups groups = insertLocationGroups(request.getLarge());
		Locations locations = insertLocations(request.getMiddle(), groups);
		LocationRoads roads = insertLocationRoads(request.getRoad(), locations);
		
	}
	
	@Transactional
	public LocationGroups insertLocationGroups(String large) {
		
		LocationGroups groups = locationGroupsRepository
			.findByName(large)
			.orElseGet(() -> {
				return locationGroupsRepository.save(
					LocationGroups.builder()
						//.no(getNextId(KindOfLocation.LARGE))
						.name(large)
						.build()
				);
			});
		return groups;
		
	}
	
	@Transactional
	public Locations insertLocations(String middle, LocationGroups groups) {
		
		Locations locations = locationsRepository
			.findByNameAndGroups(middle, groups)
			.orElseGet(() -> {
				return locationsRepository.save(
					Locations.builder()
						//.no(getNextId(KindOfLocation.MIDDLE))
						.name(middle)
						.groups(groups)
						.build()
				);
			});
		return locations;
		
	}
	
	@Transactional
	public LocationRoads insertLocationRoads(
		String roadName, 
		Locations locations
	) {
		
		LocationRoads roads = locationRoadsRepository
			.findByNameAndLocations(roadName, locations)
			.orElseGet(() -> {
				return locationRoadsRepository.save(
					LocationRoads.builder()
						//.no(getNextId(KindOfLocation.ROAD))
						.name(roadName)
						.locations(locations)
						.build()
				);
			});
		return roads;
		
	}
	
	/**
	 * insertLocationWrong() 메서드에 대한 해결책
	 * 
	 * 각 엔티티들의 ManyToOne 어노테이션 속성으로 cascade = CascadeType.PERSIST을 설정해야 
	 * 제대로 데이터 삽입이 수행됨. 
	 * 
	 * @param requestDto
	 */
	@Transactional
	public void insertLocationWrongSolution(LocationRequest requestDto) {
		
		LocationGroups groups = locationGroupsRepository
			.findByName(requestDto.getLarge())
			.orElse(
				LocationGroups.builder()
					.name(requestDto.getLarge())
					.build()
			);
		
		// 만일 위 코드에서 조회된 종속 엔티티(groups)가 없을 경우, 
		// 이 시점에서는 DB에 해당 종속 엔티티의 데이터가 없다는 뜻이므로 
		// 위에서 생성한 아직 영속성 컨텍스트에 포함되지 않은 종속 엔티티 객체를 
		// 주인 엔티티 조회 조건으로 사용하면 에러가 발생한다. 
		// 따라서 여기서는 이 문제를 우회하기 위해 대신 종속 엔티티가 대표하는 
		// 도로명 주소 상위 분류의 문자열 값도 같이 입력하여 상위 분류 및 현재 분류의 문자열 값 
		// 모두 일치하는지 여부로 주인 엔티티를 조회한다. 
		Locations locations = locationsRepository
			.findByNames(requestDto)
			.orElse(
				Locations.builder()
					.name(requestDto.getMiddle())
					.groups(groups)
					.build()
			);
		
		Optional<LocationRoads> roadsOpt = locationRoadsRepository
			.findByNames(requestDto);
		if (roadsOpt.isEmpty()) {
			LocationRoads roads = LocationRoads.builder()
				//.no(getNextId(KindOfLocation.ROAD))
				.name(requestDto.getRoad())
				.locations(locations)
				.build();
			locationRoadsRepository.save(roads);
			log.info("새 도로명 주소 데이터 삽입 완료");
		} else {
			log.info("이미 DB에 해당 데이터가 존재하므로 삽입 작업은 일어나지 않았습니다.");
		}
		
	}
	
	/**
	 * DB에 존재하는 PK 필드의 최대값보다 1 더 큰 숫자를 반환.
	 * 
	 * @deprecated Entity에는 GeneratedValue(strategy = GenerationType.IDENTITY) 
	 * 어노테이션을 적용하였으나 정작 DB에서는 location_roads PK 컬럼에 auto_increment를 적용하지 않아서 
	 * ID 필드값이 자동 증가되지 않은 것으로 밝혀졌다. auto_increment를 적용하니 
	 * 더 이상 ID 필드값을 수동으로 증가시키지 않아도 문제 없이 동작하기에 해당 메서드를 deprecate한다.
	 * @param kind
	 * @return
	 */
	@Deprecated
	private int getNextId(KindOfLocation kind) {
		
		if (kind == null) return -1;
		
		Integer nextNumber = -1;
		
		switch (kind) {
			case LARGE:
				nextNumber = locationGroupsRepository.findMaxId();
				break;
			case MIDDLE:
				nextNumber = locationsRepository.findMaxId();
				break;
			case ROAD:
				nextNumber = locationsRepository.findMaxId();
				break;
			default:
				return -1;
		}
		
		if (nextNumber == null) return -1;
		return ++nextNumber;
		
	}
	
}
