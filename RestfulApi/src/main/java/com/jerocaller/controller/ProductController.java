package com.jerocaller.controller;

import java.util.List;

import org.hibernate.dialect.lock.OptimisticEntityLockException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jerocaller.dto.ProductsDto;
import com.jerocaller.service.ProductService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;
	
	@GetMapping
	public ResponseEntity<List<ProductsDto>> getAll() {
		
		List<ProductsDto> products = productService.getAll();
		
		return ResponseEntity.ok(products);
		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<ProductsDto> getOne(
		@PathVariable("id") int id
	) {
		
		ProductsDto result = null;
		
		try {
			result = productService.getOne(id);
		} catch (EntityNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	@GetMapping("/filter")
	public ResponseEntity<List<ProductsDto>> getByCategory(
		@RequestParam(name = "category") String category
	) {
		
		List<ProductsDto> products = productService.getByCategory(category);
		
		return ResponseEntity.ok(products);
		
	}
	
	@PostMapping
	public ResponseEntity<ProductsDto> insert(
		@RequestBody ProductsDto request
	) {
		
		ProductsDto result = null;
		
		try {
			result = productService.insert(request);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (OptimisticEntityLockException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<ProductsDto> update(
		@PathVariable("id") int no, 
		@RequestBody ProductsDto request
	) {
		
		ProductsDto result = null;
		
		try {
			result = productService.update(no, request);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().build();
		} catch (OptimisticEntityLockException e) {
			return ResponseEntity.internalServerError().build();
		}
		
		return ResponseEntity.ok(result);
		
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> deleteOne(
		@PathVariable("id") int no
	) {
		
		boolean result = productService.deleteOne(no);
		
		return ResponseEntity.ok(result);
		
	}
	
}
