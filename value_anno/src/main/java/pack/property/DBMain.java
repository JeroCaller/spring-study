package pack.property;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "pack.property")
public class DBMain {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context 
			= new AnnotationConfigApplicationContext(DBMain.class);
		UseInfo useInfo = context.getBean("useInfo", UseInfo.class);
		useInfo.printDbInfo();
	}
}
