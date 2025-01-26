package com.jerocaller.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Jpa Audit 기능을 위한 설정 클래스.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditConfig {

}
