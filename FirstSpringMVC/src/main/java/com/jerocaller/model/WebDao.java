package com.jerocaller.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class WebDao {
	
	@Autowired
	private SqlMappterInterface mappterInterface;
	
	public UserDto getOneUser(String id, String password) {
		return mappterInterface.getOneUser(id, password);
	}
}
