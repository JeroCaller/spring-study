package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jerocaller.entity.Categories;

public interface CategoriesRepository 
	extends JpaRepository<Categories, Integer> {

}
