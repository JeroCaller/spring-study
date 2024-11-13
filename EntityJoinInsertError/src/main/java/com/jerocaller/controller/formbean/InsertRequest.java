package com.jerocaller.controller.formbean;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InsertRequest {
	private String username;
	private Integer mileage;
	private Integer averPurchase;
	private Integer classNumber;
	private String recommBy;
}
