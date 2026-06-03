package com.drivenote.entity;

import com.drivenote.enums.TipoCombustivel;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Entidade que registra abastecimentos do veículo.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "abastecimentos")
public class Abastecimento extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Veículo vinculado ao abastecimento.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /**
     * Data do abastecimento.
     */
    @Column(nullable = false)
    private LocalDate data;

    /**
     * Tipo de combustível utilizado.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCombustivel tipoCombustivel;

    /**
     * Valor total do abastecimento.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorTotal;

    /**
     * Quantidade de litros abastecidos.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal litros;

    /**
     * Valor por litro.
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valorLitro;

    /**
     * Quilometragem do veículo no abastecimento.
     */
    @Column(nullable = false)
    private Long quilometragem;

    /**
     * Consumo médio (km/l).
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal consumoMedio;

    /**
     * Custo por quilômetro (R$/km).
     */
    @Column(nullable = false, precision = 10, scale = 4)
    private BigDecimal custoPorKm;
}