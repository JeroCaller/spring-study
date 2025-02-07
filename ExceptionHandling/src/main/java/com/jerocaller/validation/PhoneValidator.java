package com.jerocaller.validation;

import com.jerocaller.validation.annotation.Phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		
		if (value == null) return false;
		
		return value.matches("01\\d-\\d{3,4}-\\d{4}");
	}

}
