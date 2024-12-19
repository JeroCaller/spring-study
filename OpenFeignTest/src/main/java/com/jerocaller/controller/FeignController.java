package com.jerocaller.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.service.FeignService;

@RestController
public class FeignController {
	
	@Autowired
	private FeignService feignService;
	
	@GetMapping("/posts/{id}")
	public Object getOnePost(@PathVariable("id") Integer id) {
		return feignService.getOnePost(id);
	}
	
	@GetMapping("/comments/{id}")
	public Object getOneComment(@PathVariable("id") Integer id) {
		return feignService.getOneComment(id);
	}
}
