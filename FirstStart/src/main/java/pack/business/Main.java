package pack.business;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	public static void main(String[] args) {
		ApplicationContext context = 
			new ClassPathXmlApplicationContext("BeanContainer.xml");
		
		BusinessLogic businessLogic = context.getBean(
				"businessLogic", BusinessLogic.class
		);
		businessLogic.printData();
	}

}
