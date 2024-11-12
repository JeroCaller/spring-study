package com.jerocaller.model.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "classes")
@Getter
@Setter
public class UserClassInfo implements EntityInter {
	@Id
	private Integer classNumber;
	
	@Column(name = "class_name", length = 20)
	private String className;
	
	@Column(length = 11)
	private Integer bonus;
	
	@OneToMany(mappedBy = "userClassInfo", fetch = FetchType.EAGER)
	private List<SiteUsers> users;
}
