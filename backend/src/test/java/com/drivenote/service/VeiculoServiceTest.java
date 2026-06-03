package com.drivenote.service;

import com.drivenote.dto.VeiculoCreateRequest;
import com.drivenote.entity.Usuario;
import com.drivenote.entity.Veiculo;
import com.drivenote.enums.TipoCombustivel;
import com.drivenote.exception.BusinessException;
import com.drivenote.repository.UsuarioRepository;
import com.drivenote.repository.VeiculoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    public VeiculoServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveLancarErroQuandoPlacaDuplicada() {
        VeiculoCreateRequest request = new VeiculoCreateRequest(
                "Honda", "Civic", 2020, "ABC1234",
                TipoCombustivel.GASOLINA, 10000L
        );

        when(veiculoRepository.existsByPlaca("ABC1234")).thenReturn(true);

        assertThrows(BusinessException.class, () -> veiculoService.criar(1L, request));
        verify(veiculoRepository, times(1)).existsByPlaca("ABC1234");
    }
}