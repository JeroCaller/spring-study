package com.jerocaller.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.jerocaller.dto.ProductsDto;
import com.jerocaller.entity.Products;
import com.jerocaller.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
	
	private final ProductRepository productRepository;
	
	public List<ProductsDto> getAll() {
		
		List<ProductsDto> products = productRepository.findAll()
			.stream()
			.map(ProductsDto :: toDto)
			.collect(Collectors.toList());
		
		return products;
		
	}
	
	public ProductsDto getOne(int no) throws EntityNotFoundException {
		
		Products product = productRepository.findById(no)
			.orElseThrow(() -> new EntityNotFoundException("찾고자 하는 정보가 없습니다."));
		
		return ProductsDto.toDto(product);
		
	}
	
	public List<ProductsDto> getByCategory(String category) {
		
		List<ProductsDto> products = productRepository
				.findByCategory(category)
				.stream()
				.map(ProductsDto :: toDto)
				.collect(Collectors.toList());
		
		return products;
		
	}
	
	public ProductsDto insert(ProductsDto dto) {
		
		Products product = Products.toInsertEntity(dto);
		Products savedProduct = productRepository.save(product);
		
		return ProductsDto.toDto(savedProduct);
		
	}
	
	public ProductsDto update(int no, ProductsDto dto) {
		
		Products product = Products.toUpdateEntity(no, dto);
		Products updatedProduct = productRepository.save(product);
		
		return ProductsDto.toDto(updatedProduct);
		
	}
	
	public boolean deleteOne(int no) {
		
		try {
			productRepository.deleteById(no);
		} catch (IllegalArgumentException e) {
			return false;
		}
		
		return true;
		
	}
	
}
