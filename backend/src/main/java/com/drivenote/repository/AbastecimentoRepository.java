package com.drivenote.repository;

import com.drivenote.entity.Abastecimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AbastecimentoRepository extends JpaRepository<Abastecimento, Long> {

    Optional<Abastecimento> findTopByVeiculoIdOrderByQuilometragemDesc(Long veiculoId);

    List<Abastecimento> findAllByVeiculoId(Long veiculoId);

    List<Abastecimento> findAllByVeiculoIdAndDataBetween(Long veiculoId, LocalDate inicio, LocalDate fim);

    Optional<Abastecimento> findTopByVeiculoIdAndQuilometragemLessThanOrderByQuilometragemDesc(
            Long veiculoId, Long quilometragem);
}