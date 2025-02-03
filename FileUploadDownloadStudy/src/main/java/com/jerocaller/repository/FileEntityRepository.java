package com.jerocaller.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jerocaller.entity.FileEntity;
import com.jerocaller.entity.Members;

public interface FileEntityRepository 
	extends JpaRepository<FileEntity, Integer> {
	
	List<FileEntity> findByMembers(Members member);
	
	/**
	 * 회원의 닉네임 변경 시 해당 유저가 보유한 파일들의 닉네임 정보도 
	 * 그에 맞게 바꾼다. 
	 * 유저 닉네임으로 맞춘 디렉토리명도 그에 맞게 바꾼다. 
	 * 
	 * \@Modifying - 해당 JPQL이 SELECT문이 아닌 DML 작업임을 명시.
	 * 
	 * flushAutomatically = true) 변경 쿼리문 실행 전 영속성 컨텍스트 내 
	 * 정보들을 DB에 flush(DB로의 영속화)할 것인지 여부를 묻는 속성으로, 
	 * true 시 그렇게 실행된다. 
	 * 여기서는 해당 속성을 true로 설정하지 않으면 외래키 조건에 부합하지 않는다며, 
	 * 즉 부모 테이블에 없는 값으로 업데이트를 한다며 에러가 발생한다. 
	 * 에러 메시지)
	 * java.sql.SQLIntegrityConstraintViolationException: Cannot add or update a child row: a foreign key constraint fails
	 * 
	 * 이러한 에러가 발생하는 이유는, 새로운 회원 정보가 영속성 컨텍스트 내부로 
	 * 영속화가 되었지만 아직 DB에까지 반영되지 않은 상황이므로, DB에는 
	 * 해당 회원 정보가 없는 셈이다. 이 상황에서 파일 정보를 
	 * 새 회원 정보로 바꾸려고 하니 없는 정보로 FK를 바꾸는 것과 마찬가지이기에 
	 * 참조 무결성에 어긋난다고 에러가 뜨는 것이다. 
	 * 따라서 아래 JPQL 쿼리문처럼 파일의 FK 정보를 수정하는 쿼리문을 
	 * 실행하기 전에 반드시 새로운 회원 정보가 들어있는 영속성 컨텍스트를 
	 * flush하여 DB에 새 회원 정보가 반영되게끔 해야 한다. 
	 * 그래야 참조 무결성에 어긋나지 않게 파일 정보도 바꿀 수 있다. 
	 * 
	 * JPQL에서 미지원하는 Database function은 
	 * function()이라는 함수를 통해 사용 가능. 
	 * 
	 * 참고 사이트)
	 * replace() 함수)
	 * https://coding-house.tistory.com/175
	 * 
	 * https://whitewise95.tistory.com/234
	 * https://en.wikibooks.org/wiki/Java_Persistence/JPQL#Functions
	 * 
	 * @param oldMember
	 * @param newMember
	 */
	@Modifying(flushAutomatically = true)
	@Query("""
		UPDATE FileEntity f
		SET f.filePath = FUNCTION(
			'REPLACE',
			f.filePath, 
			:#{#oldMember.nickname}, 
			:#{#newMember.nickname}
		),
		f.members = :#{#newMember} 
		WHERE f.members = :#{#oldMember}
	""")
	void updateUserInfoWithPath(
		@Param("oldMember") Members oldMember,
		@Param("newMember") Members newMember
	);
	
}
