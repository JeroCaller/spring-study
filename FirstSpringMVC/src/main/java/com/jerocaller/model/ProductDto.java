package com.jerocaller.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDto {
	private int id, price;
	private String name, category;
}
