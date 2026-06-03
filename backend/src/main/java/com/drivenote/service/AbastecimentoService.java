package com.drivenote.service;

import com.drivenote.dto.AbastecimentoCreateRequest;
import com.drivenote.dto.AbastecimentoResponse;
import com.drivenote.entity.Abastecimento;
import com.drivenote.entity.Veiculo;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.exception.BusinessException;
import com.drivenote.exception.ResourceNotFoundException;
import com.drivenote.mapper.AbastecimentoMapper;
import com.drivenote.repository.AbastecimentoRepository;
import com.drivenote.repository.VeiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AbastecimentoService {

    private final AbastecimentoRepository abastecimentoRepository;
    private final VeiculoRepository veiculoRepository;
    private final PlanoManutencaoAlertaService alertaService;

    public AbastecimentoResponse criar(AbastecimentoCreateRequest request) {
        Veiculo veiculo = veiculoRepository.findById(request.veiculoId())
                .orElseThrow(() -> new ResourceNotFoundException("Veículo não encontrado"));
        validarCombustivel(veiculo.getCombustivel(), request.tipoCombustivel());

        if (request.quilometragem() < veiculo.getQuilometragemAtual()) {
            throw new BusinessException("Quilometragem não pode ser menor que a atual do veículo");
        }

        abastecimentoRepository.findTopByVeiculoIdOrderByQuilometragemDesc(veiculo.getId())
                .ifPresent(ultimo -> {
                    if (request.quilometragem() <= ultimo.getQuilometragem()) {
                        throw new BusinessException("Quilometragem deve ser maior que o último abastecimento");
                    }

                    if (request.data().isBefore(ultimo.getData())) {
                        throw new BusinessException(
                                "Data do abastecimento deve ser posterior ao último registro: "
                                        + ultimo.getData());
                    }
                });

        BigDecimal valorLitro = request.valorLitro();
        if (valorLitro == null) {
            valorLitro = request.valorTotal()
                    .divide(request.litros(), 2, RoundingMode.HALF_UP);
        }

        BigDecimal consumoMedio = calcularConsumoMedio(request, veiculo);

        BigDecimal custoPorKm = request.valorTotal()
                .divide(BigDecimal.valueOf(request.quilometragem() - veiculo.getQuilometragemAtual()),
                        4, RoundingMode.HALF_UP);

        Abastecimento abastecimento = Abastecimento.builder()
                .veiculo(veiculo)
                .data(request.data())
                .tipoCombustivel(request.tipoCombustivel())
                .valorTotal(request.valorTotal())
                .litros(request.litros())
                .valorLitro(valorLitro)
                .quilometragem(request.quilometragem())
                .consumoMedio(consumoMedio)
                .custoPorKm(custoPorKm)
                .build();

        veiculo.setQuilometragemAtual(request.quilometragem());
        veiculoRepository.save(veiculo);

        AbastecimentoResponse response =
                AbastecimentoMapper.toResponse(abastecimentoRepository.save(abastecimento));

        alertaService.verificarPorVeiculo(veiculo);

        return response;
    }

    private void validarCombustivel(TipoCombustivel veiculoCombustivel,
                                    TipoCombustivel abastecimentoCombustivel) {
        boolean valido = switch (veiculoCombustivel) {
            case GASOLINA -> abastecimentoCombustivel == TipoCombustivel.GASOLINA;
            case ETANOL   -> abastecimentoCombustivel == TipoCombustivel.ETANOL;
            case DIESEL   -> abastecimentoCombustivel == TipoCombustivel.DIESEL;
            case FLEX     -> abastecimentoCombustivel == TipoCombustivel.GASOLINA
                    || abastecimentoCombustivel == TipoCombustivel.ETANOL;
        };

        if (!valido) {
            throw new BusinessException(
                    "Combustível inválido para este veículo. " +
                            "Esperado: " + combustivelPermitido(veiculoCombustivel) +
                            ", informado: " + abastecimentoCombustivel
            );
        }
    }

    private String combustivelPermitido(TipoCombustivel tipo) {
        return switch (tipo) {
            case GASOLINA -> "GASOLINA";
            case ETANOL   -> "ETANOL";
            case DIESEL   -> "DIESEL";
            case FLEX     -> "GASOLINA ou ETANOL";
        };
    }

    public List<AbastecimentoResponse> listarPorVeiculo(Long veiculoId) {
        return abastecimentoRepository.findAllByVeiculoId(veiculoId)
                .stream().map(AbastecimentoMapper::toResponse).toList();
    }

    public List<AbastecimentoResponse> listarPorPeriodo(Long veiculoId, LocalDate inicio, LocalDate fim) {
        return abastecimentoRepository.findAllByVeiculoIdAndDataBetween(veiculoId, inicio, fim)
                .stream().map(AbastecimentoMapper::toResponse).toList();
    }

    private BigDecimal calcularConsumoMedio(AbastecimentoCreateRequest request, Veiculo veiculo) {
        long kmPercorridos = request.quilometragem() - veiculo.getQuilometragemAtual();
        if (kmPercorridos <= 0) return BigDecimal.ZERO;

        return BigDecimal.valueOf(kmPercorridos)
                .divide(request.litros(), 2, RoundingMode.HALF_UP);
    }

    public void excluirUltimo(Long veiculoId) {
        Abastecimento ultimo = abastecimentoRepository
                .findTopByVeiculoIdOrderByQuilometragemDesc(veiculoId)
                .orElseThrow(() -> new ResourceNotFoundException("Nenhum abastecimento encontrado"));

        abastecimentoRepository
                .findTopByVeiculoIdAndQuilometragemLessThanOrderByQuilometragemDesc(
                        veiculoId, ultimo.getQuilometragem())
                .ifPresentOrElse(
                        anterior -> {
                            Veiculo veiculo = ultimo.getVeiculo();
                            veiculo.setQuilometragemAtual(anterior.getQuilometragem());
                            veiculoRepository.save(veiculo);
                        },
                        () -> {
                            throw new BusinessException(
                                    "Não é possível excluir o único abastecimento registrado");
                        }
                );

        abastecimentoRepository.delete(ultimo);
    }
}