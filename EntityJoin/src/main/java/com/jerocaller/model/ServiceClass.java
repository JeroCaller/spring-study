package com.jerocaller.model;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jerocaller.model.entity.SiteUsers;
import com.jerocaller.model.repository.SiteUserRepository;

@Service
public class ServiceClass {
	
	@Autowired
	private SiteUserRepository siteUserRepository;
	
	public List<SiteUsers> getUsersAll() {
		return siteUserRepository.findAll();
	}
}
