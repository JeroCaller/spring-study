package com.jerocaller.exception;

/**
 * IOException은 checked exception의 일종이기에 반드시 처리해야 하는 예외이므로 
 * try ~ catch를 사용하지 않는다면 throws를 통해 예외를 던져야 한다. 
 * 그러나 인터페이스의 추상 메서드를 구현하는 구현 메서드에서는 추상 메서드에 throws가 
 * 적히지 않은 경우 이를 throw할 수 없다. throws 문도 메서드의 시그니처에 포함되기 때문. 
 * 따라서 unchecked exception이며, 이로 인해 throws를 명시해도 추상 메서드를 문제 없이 
 * 오버라이딩할 수 있는 RuntimeException으로 변환하기 위한 커스텀 예외 클래스.
 */
public class IORuntimeException extends RuntimeException {
	
	public IORuntimeException(String message) {
		super(message);
	}
	
}
