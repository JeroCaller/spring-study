package com.jerocaller.dto;

import java.time.LocalDate;

import com.jerocaller.annotation.validation.Phone;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
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
public class MemberRequestThree implements RegisterRequest {
	
	/**
	 * 닉네임.
	 * null 및 "", " "은 불가. 
	 * 닉네임 문자열의 길이는 5이상 20이하의 범위만 허용.
	 */
	@NotBlank(message = "닉네임은 공백을 허용하지 않으며, 반드시 입력해야 합니다.")
	@Size(min = 5, max = 20, message = "5글자 이상 20글자 이하만 가능합니다.")
	String nickname;
	
	/**
	 * 이메일 형식 검사.
	 */
	@Email
	String email;
	
	/**
	 * 현재보다 과거의 일시만 생년월일로 입력 가능.
	 */
	@Past(message = "생년월일은 현재보다 과거의 일시만 입력할 수 있습니다.")
	LocalDate birthDate;
	
	/**
	 * 정규식을 이용하여 전화번호 형식 유효성 검증.
	 */
	// 01x-xxx(x)-xxxx
	@Phone
	String phone;
	
	/**
	 * 정수형으로 표현될 수 있는 회원 가입 가능한 나이는 
	 * 20세부터 80세까지로 지정.
	 */
	@Min(value = 20, message = "이 사이트의 회원 가입 가능 연령은 만 20세부터입니다.")
	@Max(value = 80, message = "이 사이트의 회원 가입 제한 연령은 만 80세까지입니다.")
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
	@Positive
	int couponCode;
	
}
