package com.drivenote.repository;

import com.drivenote.entity.Lembrete;
import com.drivenote.enums.StatusLembrete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface LembreteRepository extends JpaRepository<Lembrete, Long> {

    List<Lembrete> findAllByVeiculoId(Long veiculoId);

    List<Lembrete> findAllByVeiculoUsuarioIdOrderByDataAlertaAsc(Long usuarioId);

    List<Lembrete> findAllByStatusAndDataAlertaBefore(StatusLembrete status, LocalDate data);

    boolean existsByVeiculoIdAndTituloAndStatusIn(Long veiculoId, String titulo, Collection<StatusLembrete> statuses);
}