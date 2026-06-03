package com.drivenote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade principal de usuário do sistema.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios", uniqueConstraints = {
        @UniqueConstraint(name = "uk_usuario_email", columnNames = "email")
})
public class Usuario extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome completo do usuário.
     */
    @Column(nullable = false, length = 100)
    private String nome;

    /**
     * Email único, usado para autenticação.
     */
    @Column(nullable = false, length = 120, unique = true)
    private String email;

    /**
     * Senha criptografada (hash).
     */
    @Column(nullable = false, length = 255)
    private String senha;

    /**
     * Telefone de contato.
     */
    @Column(length = 20)
    private String telefone;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Veiculo> veiculos = new ArrayList<>();
}