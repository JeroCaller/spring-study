package pack.business;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"pack.model", "pack.business"})
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context
			= new AnnotationConfigApplicationContext(Main.class);
		
		BusinessLogic logic = context.getBean(
				"businessLogic", BusinessLogic.class
		);
		logic.printData();
	}
}
