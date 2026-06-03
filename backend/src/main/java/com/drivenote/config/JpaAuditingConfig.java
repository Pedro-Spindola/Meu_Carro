package com.drivenote.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita auditoria automática do JPA.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}