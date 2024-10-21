package pack.model;

import org.apache.ibatis.jdbc.SQL;

public class QueryBuilder {
	private final String TABLE_NAME = " products ";
	private final String WHERE_ID = " id=#{id} ";
	
	public String getQuerySelectAllProducts() {
		// 메서드 체이닝 방식
		return new SQL()
			.SELECT("id, name, price, category")
			.FROM(TABLE_NAME)
			.toString();
	}
	
	public String getQueryInsertOneProduct() {
		// 내부 클래스 방식.
		return new SQL() {{
			INSERT_INTO(TABLE_NAME);
			VALUES("id", "#{id}");
			VALUES("name", "#{name}");
			VALUES("price", "#{price}");
			VALUES("category", "#{category}");
		}}.toString();
	}
	
	public String getQueryUpdateOneById() {
		return new SQL()
			.UPDATE(TABLE_NAME)
			.SET("name = #{name}")
			.WHERE(WHERE_ID)
			.toString();
	}
	
	public String getQueryDeleteOneById() {
		return new SQL()
			.DELETE_FROM(TABLE_NAME)
			.WHERE(WHERE_ID)
			.toString();
	}
	
}
