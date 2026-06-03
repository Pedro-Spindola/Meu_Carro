package com.drivenote.repository;

import com.drivenote.entity.Manutencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ManutencaoRepository extends JpaRepository<Manutencao, Long> {

    List<Manutencao> findAllByVeiculoId(Long veiculoId);

    List<Manutencao> findAllByPlanoManutencaoId(Long planoId);

    Optional<Manutencao> findTopByPlanoManutencaoIdOrderByDataDesc(Long planoId);
}