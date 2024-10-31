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
	
	public void updateProductPrice() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myjpa");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		
		try {
			transaction.begin();
			
			Products targetProduct = findByName(manager, "냉동피자");
			
			// 만약 엔티티 객체에 setter가 있었다면 
			// setter를 통해 값을 바꾸기만 해도 
			// 수정된 값이 영속성 컨텍스트에 반영된다. 
			// 왜냐면 해당 엔티티 객체는 이미 영속성 컨텍스트 내부에 포함되어 있는 
			// 영속 객체이기에 dirty checking이 자동으로 되고 있기 때문. 
			//targetProduct.setPrice(targetProduct.getPrice() + 1000);
			
			Products updateProduct = Products.builder()
					.id(targetProduct.getId())
					.name(targetProduct.getName())
					.category(targetProduct.getCategory())
					.price(targetProduct.getPrice() + 1000)
					.build();
			// persist()를 수정용으로 사용하려고 하면 에러가 발생한다. 
			// 대신 merge() 메서드를 사용해야 한다. 
			//manager.persist(updateProduct);
			manager.merge(updateProduct);
			
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
	
	public void deleteByName() {
		EntityManagerFactory factory = Persistence.createEntityManagerFactory("myjpa");
		EntityManager manager = factory.createEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		
		try {
			transaction.begin();
			
			Products targetProduct = findByName(manager, "냉동피자");
			manager.remove(targetProduct);
			
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
	
	public Products findByName(EntityManager manager, String targetName) {
		String sql = "SELECT p FROM Products p WHERE p.name = :targetName";
		return manager.createQuery(sql, Products.class)
				.setParameter("targetName", targetName)
				.getSingleResult();
	}
}
