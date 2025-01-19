package com.jerocaller.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.LocationRequest;
import com.jerocaller.entity.LocationGroups;
import com.jerocaller.entity.LocationRoads;
import com.jerocaller.entity.Locations;
import com.jerocaller.service.LocationProcess;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
@Slf4j
public class LocationController {
	
	private final LocationProcess locationProcess;
	
	@GetMapping
	public Object getLocationGroupAll() {
		return locationProcess.getAll();
	}
	
	@PostMapping
	public ResponseEntity<Object> insertLocation(
			@RequestBody LocationRequest requestDto
	) {
		
		log.info(requestDto.toString());
		
		//locationProcess.insertLocationWrong(requestDto);  // 실패
		locationProcess.insertLocationTwo(requestDto); // 성공
		//locationProcess.insertFullLocations(requestDto);  // 성공
		
		// 성공
		/*
		try {
			locationProcess.insertLocationThree(requestDto);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
		return ResponseEntity.ok("good");
		
	}
	
	@PostMapping("/large")
	public Object insertLocationGroup(
			@RequestParam("large") String large
	) {
		return locationProcess.insertLocationGroups(large);
	}
	
}
