package pack.model;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface SqlMapperInterface {
	String TABLE_NAME = " products ";
	String WHERE_ID = " WHERE id=#{id} ";
	
	@Select(value = "SELECT id, name, price, category FROM " 
			+ TABLE_NAME)
	List<ProductDto> selectAllProducts();
	
	@Insert("INSERT INTO products "
			+ "VALUES (#{id}, #{name}, #{price}, #{category})")
	int insertOneProduct(ProductDto productDto);
	
	@Update("UPDATE " + TABLE_NAME + "SET name=#{name} " + 
			WHERE_ID)
	int updateOneById(ProductDto productDto);
	
	@Delete("DELETE FROM " + TABLE_NAME + WHERE_ID)
	int deleteOneById(String targetId);
}
