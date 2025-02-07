package com.jerocaller.dto;

import com.jerocaller.validation.annotation.Phone;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
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
public class MemberRegister {
	
	@NotBlank(message = "닉네임은 공백일 수 없습니다. 반드시 입력해주세요.")
	@Size(min = 5, max = 20, message = "5글자 이상 20글자 이하여야 합니다.")
	private String nickname;
	
	@Email
	private String email;
	
	@Phone
	private String phone;
	
	@Min(value = 19, message = "가입 가능 연령은 만 19세 이상부터입니다.")
	@Max(value = 80, message = "가입 제한 연령은 만 80세까지입니다.")
	private int age;
	
}
