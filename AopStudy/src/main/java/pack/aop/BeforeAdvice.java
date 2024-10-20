package pack.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class BeforeAdvice {
	
	@Before("execution(public void pack.business.*.printWebPage())")
	public void doBeforeAdvice() {
		System.out.println("==== From Before Advice ====");
		System.out.println("============================");
	}
}
