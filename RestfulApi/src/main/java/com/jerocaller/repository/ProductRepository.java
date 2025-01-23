package com.jerocaller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.Products;

public interface ProductRepository 
	extends JpaRepository<Products, Integer> {
	
	List<Products> findByCategory(String category);
	
}
