package com.drivenote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "token_blacklist")
public class TokenBlacklist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // salva o jti do JWT
    @Column(nullable = false, unique = true, length = 100)
    private String jti;

    @Column(nullable = false)
    private Instant expiresAt;
}