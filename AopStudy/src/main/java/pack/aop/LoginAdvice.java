package pack.aop;

import java.util.Scanner;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Aspect
@Component
@PropertySource("classpath:AdminData.properties")
public class LoginAdvice {
	
	@Value("${admin.id}")
	private String adminId;
	
	@Value("${admin.pw}")
	private String adminPw;
	
	@Around("execution(public void pack.business.*.printWebPage())")
	public Object proceedLogin(ProceedingJoinPoint joinPoint) throws Throwable {
		System.out.println("Hi from begin of Advice");
		
		boolean loginResult = loginLogic();
		
		if (!loginResult) return null; // 로그인 실패 시 웹 사이트 방문 불가.
		Object returnResultOfTarget = joinPoint.proceed();
		
		return returnResultOfTarget;
	}
	
	private boolean loginLogic() {
		Scanner scan = new Scanner(System.in);
		
		System.out.println("로그인 하기 - 로그인 후 웹 사이트 방문 가능");
		System.out.print("아이디: ");
		String id = scan.next();
		
		System.out.print("비밀번호: ");
		String pw = scan.next();
		
		if (!id.equals(adminId)) {
			System.out.println("아이디가 틀렸습니다.");
			scan.close();
			return false;
		}
		if (!pw.equals(adminPw)) {
			System.out.println("비밀번호가 틀렸습니다.");
			scan.close();
			return false;
		}
		
		System.out.println("로그인 성공!");
		
		scan.close();
		return true;
	}
}
