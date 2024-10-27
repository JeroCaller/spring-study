package com.jerocaller.model;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

@Mapper
public interface SqlMappterInterface {
	@SelectProvider(
			type = QueryBuilder.class, 
			method = "getQuerySelectOneUser")
	UserDto getOneUser(
			@Param("id") String id, 
			@Param("password") String password
	);
	
	@SelectProvider(
			type = QueryBuilder.class, 
			method = "getQuerySelectAllProducts")
	List<ProductDto> getAllProducts();
}
