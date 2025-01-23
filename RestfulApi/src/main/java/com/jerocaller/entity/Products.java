package com.jerocaller.entity;

import com.jerocaller.dto.ProductsDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Products {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, length = 11)
	private Integer id;
	
	@Column(nullable = false, length = 20)
	private String name;
	
	@Column(nullable = false, length = 11)
	private Integer price;
	
	@Column(nullable = false, length = 20)
	private String category;
	
	public static Products toInsertEntity(ProductsDto dto) {
		
		return Products.builder()
			.name(dto.getName())
			.price(dto.getPrice())
			.category(dto.getCategory())
			.build();
		
	}
	
	public static Products toUpdateEntity(int no, ProductsDto dto) {
		
		return Products.builder()
			.id(no)
			.name(dto.getName())
			.price(dto.getPrice())
			.category(dto.getCategory())
			.build();
		
	}
	
}
