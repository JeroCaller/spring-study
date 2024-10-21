package pack.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class ProductDao {
	private SqlSessionFactory sqlSessionFactory 
		= SqlMapConfig.getSqlSessionFactory();
	
	public List<ProductDto> selectAllProducts() {
		List<ProductDto> records = new ArrayList<ProductDto>();
		
		try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
			// xml 파일 내 id 속성값 이용.
			//records = sqlSession.selectList("selectAll"); 
			SqlMapperInterface inter = sqlSession.getMapper(SqlMapperInterface.class);
			records = inter.selectAllProducts();
		} catch (Exception e) {
			System.out.println("[selectAllProducts] Method Error");
			e.printStackTrace();
		}
		
		return records;
	}
	
	public boolean insertOneProduct(ProductDto productDto) {
		boolean isSuccess = false;
		
		// Auto Commit을 원한다면 openSession(true)로 설정. 
		SqlSession sqlSession = sqlSessionFactory.openSession();
				
		try {
			SqlMapperInterface inter = sqlSession.getMapper(SqlMapperInterface.class);
			int resultNum = inter.insertOneProduct(productDto);
			//int resultNum = sqlSession.insert("insertOne", productDto);
			if (resultNum > 0) {
				isSuccess = true;
			}
			sqlSession.commit(); // DB 작업 결과를 커밋하여 DB에 반영. 
		} catch (Exception e) {
			System.out.println("[insertOneProduct] Method Error");
			e.printStackTrace();
			
			// DB 작업 실패 시 롤백.
			sqlSession.rollback();
		} finally {
			if (sqlSession != null) sqlSession.close();
		}
		
		return isSuccess;
	}
	
	public boolean updateOneById(ProductDto productDto) {
		boolean isSuccess = false;
		SqlSession sqlSession = sqlSessionFactory.openSession();
				
		try {
			SqlMapperInterface inter = sqlSession.getMapper(SqlMapperInterface.class);
			//int resultNum = sqlSession.update("updateOneProductName", productDto);
			int resultNum = inter.updateOneById(productDto);
			if (resultNum > 0) {
				isSuccess = true;
			}
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("[updateOneById] Method Error");
			e.printStackTrace();
			sqlSession.rollback();
		} finally {
			if (sqlSession != null) sqlSession.close();
		}
		
		return isSuccess;
	}

	public boolean deleteOneById(String targetId) {
		boolean isSuccess = false;
		SqlSession sqlSession = sqlSessionFactory.openSession();
				
		try {
			SqlMapperInterface inter = sqlSession.getMapper(SqlMapperInterface.class);
			int resultNum = inter.deleteOneById(targetId);
			//int resultNum = sqlSession.delete("deleteOne", targetId);
			if (resultNum > 0) {
				isSuccess = true;
			}
			sqlSession.commit();
		} catch (Exception e) {
			System.out.println("[deleteOneById] Method Error");
			e.printStackTrace();
			sqlSession.rollback();
		} finally {
			if (sqlSession != null) sqlSession.close();
		}
		
		return isSuccess;
	}
}
