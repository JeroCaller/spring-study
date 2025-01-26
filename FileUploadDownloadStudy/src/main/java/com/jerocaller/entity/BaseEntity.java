package com.jerocaller.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

/**
 * 여러 엔티티에서 공통적으로 나타나는 필드들을 하나의 부모 엔티티에서 
 * 공통적으로 다루고자 함.
 * 
 * 여기서는 새로운 어노테이션들이 등장하는데, 이를 사용하기 위해선 먼저 
 * @EnableJpaAuditing 어노테이션이 적용된 config 클래스를 먼저 정의해야 한다. 
 * 
 * (참고로 "누가" 엔티티 생성 및 수정했는지를 알고자 한다면 @CreatedBy, @ModifiedBy 
 * 어노테이션을 사용할 수 있도록 하는 AuditorAware를 Spring Bean으로 등록해야한다고 함. 
 * 여기선 생략)
 * 
 * 새 어노테이션 소개)
 * @EntityListeners - 엔티티를 DB에 적용하기 전후에 콜백 요청을 위한 어노테이션.
 * 여기서는 AuditingEntityListener 클래스를 대입하였는데, 이 클래스는 
 * 엔티티의 Auditing(감시 - 여기서는 누가 언제 데이터 생성 및 변경했는지를 감시) 정보를 
 * 주입하는 JPA 엔티티 리스너 클래스이다. 
 * 
 * @MappedSuperClass - 자식 엔티티가 이 엔티티 클래스를 상속받을 경우, 자식 클래스에게 
 * 부모 엔티티의 매핑 정보를 전달한다. 여기서는 안에 정의한 필드 및 Getter 등 클래스 수준에 
 * 적용된 모든 어노테이션을 의미하는 것 같다.
 * 
 * @CreatedDate - 데이터 생성 날짜를 자동으로 주입하는 어노테이션.
 * 
 * @LastModifedDate - 데이터 수정 날짜를 자동으로 주입하는 어노테이션.
 */
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public class BaseEntity {
	
	@CreatedDate
	@Column(updatable = false)
	private LocalDateTime createdAt;
	
	@LastModifiedDate
	private LocalDateTime updatedAt;
	
}
