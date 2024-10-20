package pack.business;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pack.model.ProductDao;
import pack.model.ProductDto;

@Service
public class BusinessLogic {
	
	@Autowired
	private ProductDao productDao;
	
	public void showAllProducts() {
		List<ProductDto> records = productDao.selectAllProducts();
		
		System.out.println("=== 현재 상품 목록 ===");
		records.forEach(dto -> {
			String oneRecord = String.format(
					"%d \t %10s \t %5d \t %8s", 
					dto.getId(),
					dto.getName(),
					dto.getPrice(),
					dto.getCategory()
			);
			System.out.println(oneRecord);
		});
		System.out.println("총 건수 : " + records.size());
	}
	
	public void insertOne() {
		ProductDto dto = new ProductDto();
		dto.setId(13);
		dto.setName("미니청소기");
		dto.setPrice(30000);
		dto.setCategory("sundries");
		boolean isSuccess = productDao.insertOneProduct(dto);
		
		if(isSuccess) {
			System.out.println("새로운 데이터가 한 건 추가되었습니다.");
			System.out.println(dto.toString());
			showAllProducts();
		} else {
			System.out.println("데이터 한 건 삽입 시도 실패");
		}
		
	}
	
	public void updateOneProductName() {
		ProductDto dto = new ProductDto();
		dto.setId(13);
		dto.setName("노트북 청소기");
		
		boolean isSuccess = productDao.updateOneById(dto);
		
		if(isSuccess) {
			System.out.println("데이터 한 건이 수정되었습니다.");
			System.out.println(dto.toString());
			showAllProducts();
		} else {
			System.out.println("데이터 한 건 수정 시도 실패");
		}
	}
	
	public void deleteOneById() {
		String targetId = "13";
		
		System.out.println("가장 최근에 추가한 데이터 한 건 삭제");
		boolean isSuccess = productDao.deleteOneById(targetId);
		
		if(isSuccess) {
			System.out.println("데이터 한 건이 삭제되었습니다.");
			showAllProducts();
		} else {
			System.out.println("데이터 한 건 삭제 시도 실패");
		}
	}
}
