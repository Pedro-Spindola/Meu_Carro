package com.drivenote.repository;

import com.drivenote.entity.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

// Repository auxiliar (para gastos do mês)

public interface AbastecimentoCustomRepository extends JpaRepository<Abastecimento, Long> {

    List<Abastecimento> findAllByVeiculoIdAndDataBetween(Long veiculoId, LocalDate inicio, LocalDate fim);
}