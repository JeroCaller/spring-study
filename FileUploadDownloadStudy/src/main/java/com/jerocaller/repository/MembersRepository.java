package com.jerocaller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jerocaller.entity.Members;

public interface MembersRepository 
	extends JpaRepository<Members, String> {
	
	@Modifying(clearAutomatically = true)
	@Query("""
		UPDATE Members m
		SET m = :#{#newMember}
		WHERE m.nickname = :oldNickname
	""")
	void updateMember(
		@Param("oldNickname") String oldNickname,
		@Param("newMember") Members newMember
	);
	
}
