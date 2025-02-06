package com.jerocaller.dto;

import java.time.LocalDate;

import com.jerocaller.validation.ValidationGroupOne;
import com.jerocaller.validation.ValidationGroupTwo;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MemberRequestTwo implements RegisterRequest {
	
	/**
	 * 닉네임.
	 * null 및 "", " "은 불가. 
	 * 닉네임 문자열의 길이는 5이상 20이하의 범위만 허용.
	 */
	@NotBlank
	@Size(min = 5, max = 20)
	String nickname;
	
	/**
	 * 이메일 형식 검사.
	 */
	@Email
	String email;
	
	/**
	 * 현재보다 과거의 일시만 생년월일로 입력 가능.
	 */
	@Past
	LocalDate birthDate;
	
	/**
	 * 정규식을 이용하여 전화번호 형식 유효성 검증.
	 */
	// 01x-xxx(x)-xxxx
	@Pattern(regexp = "01\\d-\\d{3,4}-\\d{4}")
	String phone;
	
	/**
	 * 정수형으로 표현될 수 있는 회원 가입 가능한 나이는 
	 * 20세부터 80세까지로 지정.
	 */
	@Min(value = 20, groups = ValidationGroupOne.class)
	@Max(value = 80, groups = ValidationGroupOne.class)
	int age;
	
	/**
	 * 이 사이트에 쳐음 회원가입하는지 여부.
	 * 여기선 True만 허용.
	 */
	@AssertTrue
	boolean areYouNew;
	
	/**
	 * 회원 가입 시 발급받은 쿠폰 일련번호. 
	 * 양수만 유효.
	 */
	@Positive(groups = ValidationGroupTwo.class)
	int couponCode;
	
}
