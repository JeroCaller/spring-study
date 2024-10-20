package pack.aop;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AfterAdvice {
	
	@After("execution(public void pack.business.*.printWebPage())")
	public void doAfterAdvice() {
		System.out.println("===== From After Advice =====");
		System.out.println("웹 페이지를 종료합니다.");
		System.out.println("=============================");
	}
}
