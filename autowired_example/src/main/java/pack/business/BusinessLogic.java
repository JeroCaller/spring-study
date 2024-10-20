package pack.business;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import pack.model.DaoInterface;

@Service
public class BusinessLogic {
	
	//@Autowired
	//@Qualifier(value = "customerDao")
	//@Qualifier(value = "staffDao")
	//@Resource(name = "staffDao")
	@Resource(name = "customerDao")
	private DaoInterface daoInter;
	
	public void printData() {
		System.out.println("From " + daoInter.getClass().getName());
		System.out.println("=== 사람 명단 ===");
		daoInter.selectAll().forEach(data -> {
			System.out.println(data);
		});
		System.out.println("=== 사람 명단 끝! ===");
	}
}
