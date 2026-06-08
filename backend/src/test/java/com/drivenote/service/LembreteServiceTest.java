package com.drivenote.service;

import com.drivenote.dto.LembreteCreateRequest;
import com.drivenote.dto.LembreteResponse;
import com.drivenote.dto.LembreteUpdateRequest;
import com.drivenote.entity.Lembrete;
import com.drivenote.entity.Veiculo;
import com.drivenote.enums.StatusLembrete;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.enums.TipoLembrete;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.LembreteMapper;
import com.drivenote.repository.LembreteRepository;
import com.drivenote.repository.VeiculoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Requisito professor Reinaldo
 * Testes de unidade — LembreteService
 * Utiliza Mockito para isolar o serviço do banco de dados.
 * Nenhuma instância de Spring Context é iniciada.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Testes Unitários — LembreteService")
class LembreteServiceTest {

    @Mock
    private LembreteRepository lembreteRepository;

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private LembreteService lembreteService;

    private Veiculo veiculoMock;
    private Lembrete lembreteMock;

    @BeforeEach
    void setUp() {
        veiculoMock = Veiculo.builder()
                .id(1L)
                .marca("Honda")
                .modelo("Civic")
                .ano(2020)
                .placa("ABC1D23")
                .combustivel(TipoCombustivel.GASOLINA)
                .quilometragemAtual(45000L)
                .build();

        lembreteMock = Lembrete.builder()
                .id(1L)
                .veiculo(veiculoMock)
                .titulo("IPVA 2026")
                .descricao("Vencimento em janeiro")
                .tipo(TipoLembrete.IPVA)
                .dataAlerta(LocalDate.now().plusDays(10))
                .status(StatusLembrete.PENDENTE)
                .build();
    }

    // ─────────────────────────────────────────────────────────────────────
    // TESTE 1 — criar() deve criar lembrete com status PENDENTE
    // ─────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Teste 1: criar() deve salvar lembrete com status PENDENTE")
    void criar_DeveSalvarLembreteComStatusPendente() {
        // ARRANGE — prepara os dados e configura os mocks
        LembreteCreateRequest request = new LembreteCreateRequest(
                1L,
                "IPVA 2026",
                "Vencimento em janeiro",
                TipoLembrete.IPVA,
                LocalDate.now().plusDays(10)
        );

        when(veiculoRepository.findById(1L))
                .thenReturn(Optional.of(veiculoMock));

        when(lembreteRepository.save(any(Lembrete.class)))
                .thenReturn(lembreteMock);

        try (MockedStatic<LembreteMapper> mapperMock = mockStatic(LembreteMapper.class)) {
            LembreteResponse responseMock = new LembreteResponse(
                    1L, 1L, "Honda Civic", "ABC1D23",
                    "IPVA 2026", "Vencimento em janeiro",
                    TipoLembrete.IPVA, LocalDate.now().plusDays(10),
                    StatusLembrete.PENDENTE, LocalDateTime.now()
            );
            mapperMock.when(() -> LembreteMapper.toResponse(any())).thenReturn(responseMock);

            // ACT — executa o método a ser testado
            LembreteResponse resultado = lembreteService.criar(request);

            // ASSERT — verifica os resultados esperados
            assertThat(resultado).isNotNull();
            assertThat(resultado.status()).isEqualTo(StatusLembrete.PENDENTE);
            assertThat(resultado.titulo()).isEqualTo("IPVA 2026");

            // Verifica que o repositório foi chamado exatamente uma vez
            verify(lembreteRepository, times(1)).save(any(Lembrete.class));
            verify(veiculoRepository, times(1)).findById(1L);
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // TESTE 2 — criar() deve lançar exceção se veículo não encontrado
    // ─────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Teste 2: criar() deve lançar ResourceNotFoundException se veículo não existir")
    void criar_DeveLancarExcecao_QuandoVeiculoNaoEncontrado() {
        // ARRANGE
        LembreteCreateRequest request = new LembreteCreateRequest(
                99L,  // ID inexistente
                "IPVA 2026",
                "Vencimento em janeiro",
                TipoLembrete.IPVA,
                LocalDate.now().plusDays(10)
        );

        when(veiculoRepository.findById(99L))
                .thenReturn(Optional.empty());  // simula veículo não encontrado

        // ACT + ASSERT — verifica que a exceção correta é lançada
        assertThatThrownBy(() -> lembreteService.criar(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Veículo não encontrado");

        // Verifica que o repositório de lembrete NUNCA foi chamado
        verify(lembreteRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────────
    // TESTE 3 — concluir() deve mudar status para CONCLUIDO
    // ─────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Teste 3: concluir() deve alterar status do lembrete para CONCLUIDO")
    void concluir_DeveAlterarStatusParaConcluido() {
        // ARRANGE
        when(lembreteRepository.findById(1L))
                .thenReturn(Optional.of(lembreteMock));

        // Simula o save retornando o lembrete com status já atualizado
        Lembrete lembreteConcluido = Lembrete.builder()
                .id(1L)
                .veiculo(veiculoMock)
                .titulo("IPVA 2026")
                .descricao("Vencimento em janeiro")
                .tipo(TipoLembrete.IPVA)
                .dataAlerta(LocalDate.now().plusDays(10))
                .status(StatusLembrete.CONCLUIDO)  // status atualizado
                .build();

        when(lembreteRepository.save(any(Lembrete.class)))
                .thenReturn(lembreteConcluido);

        try (MockedStatic<LembreteMapper> mapperMock = mockStatic(LembreteMapper.class)) {
            LembreteResponse responseMock = new LembreteResponse(
                    1L, 1L, "Honda Civic", "ABC1D23",
                    "IPVA 2026", "Vencimento em janeiro",
                    TipoLembrete.IPVA, LocalDate.now().plusDays(10),
                    StatusLembrete.CONCLUIDO, LocalDateTime.now()
            );
            mapperMock.when(() -> LembreteMapper.toResponse(any())).thenReturn(responseMock);

            // ACT
            LembreteResponse resultado = lembreteService.concluir(1L);

            // ASSERT
            assertThat(resultado.status()).isEqualTo(StatusLembrete.CONCLUIDO);

            // Verifica que save foi chamado com o lembrete tendo status CONCLUIDO
            verify(lembreteRepository).save(argThat(l ->
                    l.getStatus() == StatusLembrete.CONCLUIDO
            ));
        }
    }

    // ─────────────────────────────────────────────────────────────────────
    // TESTE 4 — atualizar() deve lançar exceção se lembrete já CONCLUIDO
    // ─────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Teste 4: atualizar() deve lançar BusinessException se lembrete já estiver CONCLUIDO")
    void atualizar_DeveLancarExcecao_QuandoLembreteJaConcluido() {
        // ARRANGE — lembrete já está concluído
        Lembrete lembreteConcluido = Lembrete.builder()
                .id(1L)
                .veiculo(veiculoMock)
                .titulo("IPVA 2026")
                .tipo(TipoLembrete.IPVA)
                .dataAlerta(LocalDate.now().plusDays(10))
                .status(StatusLembrete.CONCLUIDO)  // já foi concluído
                .build();

        when(lembreteRepository.findById(1L))
                .thenReturn(Optional.of(lembreteConcluido));

        LembreteUpdateRequest updateRequest = new LembreteUpdateRequest(
                "IPVA Atualizado",
                "Nova descrição",
                TipoLembrete.IPVA,
                LocalDate.now().plusDays(20)
        );

        // ACT + ASSERT
        assertThatThrownBy(() -> lembreteService.atualizar(1L, updateRequest))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("concluído");

        // Verifica que o save NUNCA foi chamado — nada deve ser persistido
        verify(lembreteRepository, never()).save(any());
    }

    // ─────────────────────────────────────────────────────────────────────
    // TESTE BÔNUS — marcarAtrasados() deve atualizar apenas lembretes vencidos
    // ─────────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("Bônus: marcarAtrasados() deve atualizar status para ATRASADO apenas dos vencidos")
    void marcarAtrasados_DeveAtualizarApenasVencidos() {
        // ARRANGE — simula 2 lembretes vencidos retornados pelo banco
        Lembrete vencido1 = Lembrete.builder()
                .id(1L).veiculo(veiculoMock).titulo("IPVA")
                .tipo(TipoLembrete.IPVA)
                .dataAlerta(LocalDate.now().minusDays(5))
                .status(StatusLembrete.PENDENTE).build();

        Lembrete vencido2 = Lembrete.builder()
                .id(2L).veiculo(veiculoMock).titulo("Seguro")
                .tipo(TipoLembrete.SEGURO)
                .dataAlerta(LocalDate.now().minusDays(1))
                .status(StatusLembrete.PENDENTE).build();

        when(lembreteRepository.findAllByStatusAndDataAlertaBefore(
                eq(StatusLembrete.PENDENTE), any(LocalDate.class)))
                .thenReturn(List.of(vencido1, vencido2));

        when(lembreteRepository.saveAll(anyList()))
                .thenReturn(List.of(vencido1, vencido2));

        // ACT
        lembreteService.marcarAtrasados();

        // ASSERT — verifica que ambos tiveram o status alterado
        assertThat(vencido1.getStatus()).isEqualTo(StatusLembrete.ATRASADO);
        assertThat(vencido2.getStatus()).isEqualTo(StatusLembrete.ATRASADO);

        verify(lembreteRepository, times(1)).saveAll(anyList());
    }
}