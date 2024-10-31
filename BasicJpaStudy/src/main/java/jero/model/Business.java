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
	
	public void deleteOne() {
		dao.deleteByName();
	}
	
	public void updateOne() {
		dao.updateProductPrice();
	}
	
	public void crudPrint() {
		printAllProducts();
		System.out.println("=== DML 작업 전 목록이었음 ===");
		
		insertOne();
		printAllProducts();
		System.out.println("=== 데이터 한 건 삽입 후의 목록이었음 ===");
		
		updateOne();
		printAllProducts();
		System.out.println("=== 데이터 한 건 수정 후의 목록이었음 ===");
		
		deleteOne();
		printAllProducts();
		System.out.println("=== 데이터 한 건 삭제 후의 목록이었음 ===");
	}
}
