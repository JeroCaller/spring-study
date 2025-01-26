package com.jerocaller.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "members")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)  // 부모 엔티티의 필드들까지 엔티티 객체 비교 대상 추가.
@EqualsAndHashCode(callSuper = true) // 부모 엔티티의 필드들까지 비교 대상 추가.
public class Members extends BaseEntity {
	
	@Id
	@Column(nullable = false, length = 20)
	private String nickname;
	
	@Column(nullable = false, length = 100)
	private String password;
	
}
