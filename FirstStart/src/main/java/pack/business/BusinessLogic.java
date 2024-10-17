package pack.business;

import java.util.List;

import pack.model.SomeDao;

public class BusinessLogic {
	private SomeDao someDao;
	
	// 생성자 의존성 주입.
	public BusinessLogic(SomeDao someDao) {
		this.someDao = someDao;
	}
	
	void printData() {
		List<String> allData = someDao.selectAllData();
		
		System.out.println("=== 동물 농장 ===");
		allData.forEach(animal -> {
			System.out.println(animal);
		});
	}
}
