package com.jerocaller.data.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jerocaller.data.entity.Comment;
import com.jerocaller.data.entity.Member;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    
    List<Comment> findByMember(Member member);
    
    @Query(value = """
        SELECT c
        FROM Comment c
        JOIN c.member m
        WHERE c.id = :id AND
            m.username = :username
    """)
    Optional<Comment> findByIdAndUsername(
        @Param("id") int id, 
        @Param("username") String username
    );
    
    boolean existsByIdAndMember(int id, Member member);
    
    @Query(value = """
        SELECT c
        FROM Comment c
        JOIN c.member m
        WHERE m.role = :role
    """)
    List<Comment> findByRole(@Param("role") String role);
    
}
