package com.jerocaller.model.entity;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Classes {
	@Id
	private Integer classNumber;
	private String className;
	private Integer bonus;
	
	// SiteUsers 클래스에서 fetch type을 어떤 것을 주든 간에 
	// 여기서의 fetch type을 따른다. 
	// 여기서 EAGER로 하면 즉시 로딩으로 인해 조인된 두 테이블의 정보가 
	// 동시에 조회되고, LAZY로 하면 SiteUsers 엔티티의 테이블 정보가 먼저 
	// 조회된 다음에 Classes 데이터가 조회되는 순차적 방식이 된다. 
	@OneToMany(mappedBy = "superEntity", fetch = FetchType.EAGER)
	private List<SiteUsers> users;
}
