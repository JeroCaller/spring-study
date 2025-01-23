package com.jerocaller.dto;

import com.jerocaller.entity.Products;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductsDto {
	
	private String name;
	private Integer price;
	private String category;
	
	public static ProductsDto toDto(Products entity) {
		
		return ProductsDto.builder()
			.name(entity.getName())
			.price(entity.getPrice())
			.category(entity.getCategory())
			.build();
		
	}
	
}
