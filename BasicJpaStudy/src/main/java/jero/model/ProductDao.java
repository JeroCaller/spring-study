package jero.model;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import jero.model.entity.Products;

@Repository
public class ProductDao {
	
	public List<Products> getDataAll() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myjpa");
		EntityManager manager = factory.createEntityManager();
		
		List<Products> products = null;
		
		try {
			// JPQL을 이용하여 테이블 내 모든 상품들 조회.
			String jpql = "SELECT p FROM Products p";
			TypedQuery<Products> query = manager.createQuery(jpql, Products.class);
			products = query.getResultList();
		} catch (Exception e) {
			System.out.println("=== DB 작업 중 에러 발생! ===");
			e.printStackTrace();
		} finally {
			manager.close();
			factory.close();
		}
		
		return products;
	}
	
	public void insertOne() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myjpa");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		
		try {
			transaction.begin();
			
			Products products = Products.builder()
					.name("냉동피자")
					.price(10000)
					.category("food")
					.build();
			manager.persist(products);
			
			transaction.commit();
		} catch (Exception e) {
			System.out.println("=== DB 작업 중 에러 발생! ===");
			e.printStackTrace();
			
			// 오류 발생 시 이전까지의 작업들을 모두 취소하고 이전 커밋으로 되돌아감.
			transaction.rollback();
		} finally {
			manager.close();
			factory.close();
		}
	}
}
