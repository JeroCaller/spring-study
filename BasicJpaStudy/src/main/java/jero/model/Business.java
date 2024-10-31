package jero.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Business {
	
	@Autowired
	private ProductDao dao;
	
	public void printAllProducts() {
		System.out.println("현재 보유 물품 목록");
		dao.getDataAll().forEach(product -> {
			String oneRecord = String.format("%d %s %d %s", 
				product.getId(),
				product.getName(),
				product.getPrice(),
				product.getCategory()
			);
			System.out.println(oneRecord);
		});
		
		System.out.println("====================");
	}
	
	public void insertOne() {
		dao.insertOne();
	}
}
