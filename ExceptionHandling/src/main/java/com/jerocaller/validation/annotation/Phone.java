package com.jerocaller.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.jerocaller.validation.PhoneValidator;

import jakarta.validation.Constraint;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
	
	String message() default "전화번호 형식에 맞지 않습니다.";
	Class<?>[] groups() default {};
	Class<?>[] payload() default {};
	
}
