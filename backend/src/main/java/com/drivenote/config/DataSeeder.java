package com.drivenote.config;

import com.drivenote.entity.*;
import com.drivenote.enums.*;
import com.drivenote.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final VeiculoRepository veiculoRepository;
    private final PlanoManutencaoRepository planoRepository;
    private final ManutencaoRepository manutencaoRepository;
    private final AbastecimentoRepository abastecimentoRepository;
    private final LembreteRepository lembreteRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) return;

        Usuario user = Usuario.builder()
                .nome("João da Silva")
                    .email("joao@drivenote.com")
                .senha(passwordEncoder.encode("123456"))
                .telefone("62999999999")
                .build();

        Usuario admin = Usuario.builder()
                .nome("Admin")
                .email("admin@drivenote.com")
                .senha(passwordEncoder.encode("123456"))
                .telefone("62988888888")
                .build();

        usuarioRepository.saveAll(List.of(user, admin));

        Veiculo v1 = Veiculo.builder()
                .usuario(user)
                .marca("Honda")
                .modelo("Civic")
                .ano(2020)
                .placa("ABC1D23")
                .combustivel(TipoCombustivel.GASOLINA)
                .quilometragemAtual(44800L)
                .build();

        Veiculo v2 = Veiculo.builder()
                .usuario(user)
                .marca("Toyota")
                .modelo("Corolla")
                .ano(2019)
                .placa("DEF4G56")
                .combustivel(TipoCombustivel.FLEX)
                .quilometragemAtual(62000L)
                .build();

        veiculoRepository.saveAll(List.of(v1, v2));

        PlanoManutencao plano1 = PlanoManutencao.builder()
                .veiculo(v1)
                .tipo(TipoManutencao.OLEO_MOTOR)
                .descricao("Troca de óleo 5W30")
                .intervaloKm(10000L)
                .intervaloDias(180)
                .ativo(true)
                .build();

        PlanoManutencao plano2 = PlanoManutencao.builder()
                .veiculo(v1)
                .tipo(TipoManutencao.FILTRO_AR)
                .descricao("Filtro de ar")
                .intervaloKm(15000L)
                .intervaloDias(365)
                .ativo(true)
                .build();

        planoRepository.saveAll(List.of(plano1, plano2));

        manutencaoRepository.save(
                Manutencao.builder()
                        .veiculo(v1)
                        .planoManutencao(plano1)
                        .descricao("Troca de óleo e filtro")
                        .data(LocalDate.now().minusDays(30))
                        .quilometragem(44000L)
                        .valor(new BigDecimal("280.00"))
                        .build()
        );

        abastecimentoRepository.saveAll(List.of(
                Abastecimento.builder()
                        .veiculo(v1)
                        .data(LocalDate.now().minusDays(10))
                        .tipoCombustivel(TipoCombustivel.GASOLINA)
                        .valorTotal(new BigDecimal("250.00"))
                        .litros(new BigDecimal("35.00"))
                        .valorLitro(new BigDecimal("7.14"))
                        .quilometragem(44800L)
                        .consumoMedio(new BigDecimal("12.80"))
                        .custoPorKm(new BigDecimal("0.56"))
                        .build(),
                Abastecimento.builder()
                        .veiculo(v2)
                        .data(LocalDate.now().minusDays(5))
                        .tipoCombustivel(TipoCombustivel.FLEX)
                        .valorTotal(new BigDecimal("220.00"))
                        .litros(new BigDecimal("32.00"))
                        .valorLitro(new BigDecimal("6.87"))
                        .quilometragem(61500L)
                        .consumoMedio(new BigDecimal("11.30"))
                        .custoPorKm(new BigDecimal("0.61"))
                        .build()
        ));

        lembreteRepository.save(
                Lembrete.builder()
                        .veiculo(v1)
                        .titulo("Revisão geral")
                        .descricao("Verificar freios e suspensão")
                        .tipo(TipoLembrete.MANUTENCAO)
                        .dataAlerta(LocalDate.now().plusDays(15))
                        .status(StatusLembrete.PENDENTE)
                        .build()
        );
    }
}