package com.jerocaller.model.entity;

import java.util.List;

import com.jerocaller.model.dto.ClassesDto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Classes {
	@Id
	private Integer classNumber;
	private String className;
	private Integer bonus;
	
	@OneToMany(mappedBy = "classes")
	private List<SiteUsers> users;
	
	public static Classes toEntity(ClassesDto dto) {
		return Classes.builder()
				.classNumber(dto.getClassNumber())
				.className(dto.getClassName())
				.bonus(dto.getBonus())
				.build();
	}
}
