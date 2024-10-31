package jero;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import jero.model.Business;

@Configuration
@ComponentScan(basePackages = {"jero", "jero.model", "jero.model.entity"})
public class Main {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext context 
			= new AnnotationConfigApplicationContext(Main.class);
		
		Business business = context.getBean("business", Business.class);
		
		business.crudPrint();
	}
}
