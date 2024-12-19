package com.jerocaller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerocaller.feign.MyFeignInterface;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FeignService {
	
	@Autowired
	private MyFeignInterface myFeignInterface;
	
	public Object getOnePost(Integer id) {
		Object result = myFeignInterface.getOnePost(id);
		log.info("=== posts 로그 ===");
		log.info(result.toString());
		log.info("=== posts 로그 끝 ===");
		return result;
	}
	
	public Object getOneComment(Integer id) {
		Object result = myFeignInterface.getOneComment(id);
		log.info("=== comment 로그 ===");
		log.info(result.toString());
		log.info("=== comment 로그 끝 ===");
		return result;
	}
}
