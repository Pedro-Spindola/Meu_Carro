package com.drivenote.entity;

import com.drivenote.enums.TipoCombustivel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidade que representa um veículo do usuário.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "veiculos", uniqueConstraints = {
        @UniqueConstraint(name = "uk_veiculo_placa", columnNames = "placa")
})
public class Veiculo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false, length = 60)
    private String marca;

    @Column(nullable = false, length = 60)
    private String modelo;

    @Column(nullable = false)
    private Integer ano;

    @Column(nullable = false, length = 10, unique = true)
    private String placa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCombustivel combustivel;

    @Column(nullable = false)
    private Long quilometragemAtual;

    // <- adicionar estes quatro relacionamentos
    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Abastecimento> abastecimentos = new ArrayList<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Manutencao> manutencoes = new ArrayList<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlanoManutencao> planos = new ArrayList<>();

    @OneToMany(mappedBy = "veiculo", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Lembrete> lembretes = new ArrayList<>();

}