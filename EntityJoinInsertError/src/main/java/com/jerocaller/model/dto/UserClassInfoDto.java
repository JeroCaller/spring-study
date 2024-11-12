package com.jerocaller.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserClassInfoDto implements DtoInter {
	private Integer classNumber;
	private String className;
	private Integer bonus;
	//private List<SiteUsers> users;
}
