package com.drivenote.repository;

import com.drivenote.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório de veículos.
 */
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    boolean existsByPlaca(String placa);
    Optional<Veiculo> findByIdAndUsuarioId(Long id, Long usuarioId);
    List<Veiculo> findAllByUsuarioId(Long usuarioId);
}