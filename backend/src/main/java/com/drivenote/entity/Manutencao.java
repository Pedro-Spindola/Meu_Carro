package com.drivenote.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Registro de execução de manutenção.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "manutencoes")
public class Manutencao extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Veículo relacionado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /**
     * Plano de manutenção (pode ser null se for avulsa).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plano_id")
    private PlanoManutencao planoManutencao;

    /**
     * Descrição livre da manutenção realizada.
     */
    @Column(nullable = false, length = 150)
    private String descricao;

    /**
     * Data da manutenção.
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Quilometragem no momento.
     */
    @Column(nullable = false)
    private Long quilometragem;

    /**
     * Valor gasto.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}