package pack.model;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

public interface SqlMapperInterface {
	String TABLE_NAME = " products ";
	String WHERE_ID = " WHERE id=#{id} ";
	
	//@Select(value = "SELECT id, name, price, category FROM " + TABLE_NAME)
	@SelectProvider(type = QueryBuilder.class, method = "getQuerySelectAllProducts")
	List<ProductDto> selectAllProducts();
	
	//@Insert("INSERT INTO products " + "VALUES (#{id}, #{name}, #{price}, #{category})")
	@InsertProvider(type = QueryBuilder.class, method = "getQueryInsertOneProduct")
	int insertOneProduct(ProductDto productDto);
	
	//@Update("UPDATE " + TABLE_NAME + "SET name=#{name} " + WHERE_ID)
	@UpdateProvider(type = QueryBuilder.class, method = "getQueryUpdateOneById")
	int updateOneById(ProductDto productDto);
	
	//@Delete("DELETE FROM " + TABLE_NAME + WHERE_ID)
	@DeleteProvider(type = QueryBuilder.class, method = "getQueryDeleteOneById")
	int deleteOneById(String targetId);
}
