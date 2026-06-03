package com.drivenote.repository;

import com.drivenote.entity.PlanoManutencao;
import com.drivenote.enums.TipoManutencao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlanoManutencaoRepository extends JpaRepository<PlanoManutencao, Long> {

    boolean existsByVeiculoIdAndTipoAndAtivoTrue(Long veiculoId, TipoManutencao tipo);

    List<PlanoManutencao> findAllByVeiculoId(Long veiculoId);

    List<PlanoManutencao> findAllByAtivoTrue();

    List<PlanoManutencao> findAllByVeiculoIdAndAtivoTrue(Long veiculoId);
}