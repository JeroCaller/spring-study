package com.jerocaller.advice.old;

import java.util.HashMap;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.jerocaller.dto.old.ResponseJson;

import lombok.extern.slf4j.Slf4j;

/**
 * 유효성 검사 예외 MethodArgumentNotValidException는 ArgumentResolver 단계에서 발생한다. 
 * ArgumentResolver는 Handler Adapter와 Handler(Controller) 사이에 존재하며, 
 * 클라이언트로부터 넘어오는 HTTP 요청 정보를 \@RequestParam 등의 어노테이션이 적용된 컨트롤러 메서드의 파라미터와 
 * 매핑하는 역할을 한다. 
 * 이러한 구조로 인해 컨트롤러 메서드 내부, 또는 아래와 같은 일반적인 Advice로는 해당 예외를 처리할 수 없다. 
 * (전자의 경우, Handler Adapter와 Handler 외부에 존재하기 때문. 
 * 후자의 경우, \@RestControllerAdvice로 처리가 가능한 것으로 보아서는 AOP로 처리가 가능한 것 같은데, 
 * 순수 AOP로 직접 처리하려면 아래 코드가 아닌 다른 방법을 써야 하는 것 같다. )
 * 가장 간단한 방법은 \@RestControllerAdvice 어노테이션이 적용된 클래스의 메서드 내부에서 \@ExceptionHandler
 * 어노테이션이 적용된 메서드 내부에서 처리하는 것이다. 
 * 
 * 즉 아래 AOP 코드는 MethodArgumentNotValidException를 처리하지 못한다. 
 * 
 * ArgumentResolver 관련 개념 참고 사이트)
 * https://kangworld.tistory.com/289
 * https://hudi.blog/spring-argument-resolver/
 * https://velog.io/@uiurihappy/Spring-Argument-Resolver-%EC%A0%81%EC%9A%A9%ED%95%98%EC%97%AC-%EC%9C%A0%EC%A0%80-%EC%A0%95%EB%B3%B4-%EB%B6%88%EB%9F%AC%EC%98%A4%EA%B8%B0
 */
@Aspect
@Component
@Slf4j
public class ValidationExceptionHandler {
	
	@Around("execution(public * com.jerocaller.controller.old.*.*(..))")
	public Object handleValidationException(ProceedingJoinPoint joinPoint) throws Throwable {
		
		Object result = null;
		
		log.info("=== {} Advice 적용 됨 ===", this.getClass().getName());
		try {
			result = joinPoint.proceed();
		} catch (MethodArgumentNotValidException e) {
			log.error("=== {} 클래스에서 유효성 검사 관련 예외 발생. ===", joinPoint.getClass().getName());
			
			// 유효성 검사 미통과 시 실패한 필드와 그 메시지 기록용.
			// key : field name, value : message
			Map<String, String> validationExceptionMsg = new HashMap<String, String>();
			
			// 유효성 검사 예외로부터 검사 통과 실패한 필드의 이름과 그 메시지를 저장.
			e.getBindingResult().getFieldErrors().forEach(error -> {
				validationExceptionMsg.put(error.getField(), error.getDefaultMessage());
			});
			
			return ResponseJson.builder()
				.httpStatus(HttpStatus.BAD_REQUEST)
				.message("입력하신 회원 가입 정보가 유효하지 않습니다.")
				.data(validationExceptionMsg)
				.build();
			
		} catch (Throwable th) {
			throw th;
		}
		
		log.info("=== {} Advice 적용 끝 ===", this.getClass().getName());
		
		return result;
	}
	
}
