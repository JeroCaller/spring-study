package com.jerocaller.model.converter;

public interface DtoEntityConverter<DTO, Entity> {
	DTO toDto(Entity entity);
	Entity toEntity(DTO dto);
}
