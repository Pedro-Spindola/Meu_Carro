package com.drivenote.entity;

import com.drivenote.enums.TipoManutencao;
import jakarta.persistence.*;
import lombok.*;

/**
 * Plano preventivo de manutenção do veículo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "planos_manutencao")
public class PlanoManutencao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Veículo ao qual o plano pertence.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /**
     * Tipo do plano (óleo, filtro, etc).
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TipoManutencao tipo;

    /**
     * Descrição livre.
     */
    @Column(length = 150)
    private String descricao;

    /**
     * Intervalo em quilômetros.
     */
    private Long intervaloKm;

    /**
     * Intervalo em dias.
     */
    private Integer intervaloDias;

    /**
     * Indica se o plano está ativo.
     */
    @Column(nullable = false)
    private boolean ativo;
}