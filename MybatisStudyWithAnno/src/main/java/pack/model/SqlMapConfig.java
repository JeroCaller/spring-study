package pack.model;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

/**
 * 자바 클래스에서 DB 작업을 하기 위해 필요한 sqlSession 객체를 생성하는 
 * SqlSessionFactory 객체를 반환하는 클래스. 
 * DB 연동 설정 정보가 담긴 XML 파일(Configure.xml)을 가져와 객체화함으로써 
 * 개발자 정의 SQL문이 담긴 SqlMapper.xml를 이용하여 DB 작업이 가능하게끔 
 * 설정하는 코드가 담겨 있다. 
 */
public class SqlMapConfig {
	public static SqlSessionFactory sqlSessionFactory; // DB의 SQL명령을 실행시킬 때 필요한 메소드를 갖고 있다.

	static {
		// xml 내 내용들이 객체화한다. 
		String resource = "Configure.xml";
		try (Reader reader = Resources.getResourceAsReader(resource)) {
			sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
			
			// 어노테이션이 적용된 클래스를 SqlSessionFactory에 등록한다.
			List<Class> mappers = new ArrayList<Class>();
			mappers.add(SqlMapperInterface.class);  // 어노테이션 적용된 클래스를 등록. 여러 개 등록 가능.
			mappers.forEach(map -> {
				// SqlSessionFactory 객체에 mapper 클래스들을 실제로 등록한다. 
				sqlSessionFactory.getConfiguration().addMapper(map);
			});
			// 어노테이션 클래스 등록 완료
			
		} catch (Exception e) {
			System.out.println("SqlMapConfig 오류 : " + e);
		}
	}

	public static SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}
}