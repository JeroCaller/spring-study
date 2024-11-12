package com.jerocaller.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classes")
@Getter
@Setter
public class UserClassInfo {
	@Id
	private Integer classNumber;
	
	@Column(name = "class_name", length = 20)
	private String className;
	
	@Column(length = 11)
	private Integer bonus;
	
	@OneToMany(mappedBy = "userClassInfo")
	private List<SiteUsers> users;
}
