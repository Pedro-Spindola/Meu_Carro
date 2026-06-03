package com.drivenote.entity;

import com.drivenote.enums.StatusLembrete;
import com.drivenote.enums.TipoLembrete;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Entidade de lembretes automotivos.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lembretes")
public class Lembrete extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Veículo relacionado ao lembrete.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo veiculo;

    /**
     * Título do lembrete.
     */
    @Column(nullable = false, length = 80)
    private String titulo;

    /**
     * Descrição adicional.
     */
    @Column(length = 200)
    private String descricao;

    /**
     * Tipo do lembrete.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoLembrete tipo;

    /**
     * Data de alerta.
     */
    @Column(nullable = false)
    private LocalDate dataAlerta;

    /**
     * Status do lembrete.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private StatusLembrete status;
}