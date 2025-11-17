package com.example.backend.application.service;

import com.example.backend.application.excepiton.BeneficioNotFoundException;
import com.example.domain.model.Beneficio;
import com.example.domain.repository.BeneficioRepositoryInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do BeneficioService")
class BeneficioServiceTest {

    @Mock
    private BeneficioRepositoryInterface beneficioRepository;

    @InjectMocks
    private BeneficioService beneficioService;

    private Beneficio beneficio;

    @BeforeEach
    void setUp() {
        beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Vale Alimentação");
        beneficio.setDescricao("Benefício de alimentação");
        beneficio.setValor(new BigDecimal("500.00"));
        beneficio.setAtivo(true);
        beneficio.setVersion(1L);
    }

    @Test
    @DisplayName("Deve buscar benefício por ID com sucesso")
    void testFindById_Success() {
        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio));

        Beneficio resultado = beneficioService.findById(1L);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Vale Alimentação");
        verify(beneficioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando benefício não for encontrado")
    void testFindById_NotFound() {
        when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficioService.findById(99L))
                .isInstanceOf(BeneficioNotFoundException.class)
                .hasMessageContaining("O benefício de id 99 não foi encontrado.");

        verify(beneficioRepository, times(1)).findById(99L);
    }

    @Test
    @DisplayName("Deve listar todos os benefícios com sucesso")
    void testFindAll_Success() {
        Beneficio beneficio2 = new Beneficio();
        beneficio2.setId(2L);
        beneficio2.setNome("Vale Transporte");

        List<Beneficio> beneficios = Arrays.asList(beneficio, beneficio2);
        when(beneficioRepository.findAll()).thenReturn(beneficios);

        List<Beneficio> resultado = beneficioService.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Vale Alimentação");
        assertThat(resultado.get(1).getNome()).isEqualTo("Vale Transporte");
        verify(beneficioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver benefícios")
    void testFindAll_Empty() {
        when(beneficioRepository.findAll()).thenReturn(Collections.emptyList());

        List<Beneficio> resultado = beneficioService.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
        verify(beneficioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve criar um benefício com sucesso")
    void testCreate_Success() {
        when(beneficioRepository.create(any(Beneficio.class))).thenReturn(beneficio);

        Beneficio resultado = beneficioService.create(beneficio);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Vale Alimentação");
        verify(beneficioRepository, times(1)).create(beneficio);
    }

    @Test
    @DisplayName("Deve atualizar um benefício com sucesso")
    void testUpdate_Success() {
        Beneficio dadosAtualizados = new Beneficio();
        dadosAtualizados.setNome("Vale Alimentação Atualizado");
        dadosAtualizados.setValor(new BigDecimal("600.00"));

        when(beneficioRepository.findById(1L)).thenReturn(Optional.of(beneficio));
        when(beneficioRepository.create(any(Beneficio.class))).thenReturn(beneficio);

        Beneficio resultado = beneficioService.update(1L, dadosAtualizados);

        assertThat(resultado).isNotNull();
        verify(beneficioRepository, times(1)).findById(1L);
        verify(beneficioRepository, times(1)).create(any(Beneficio.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar benefício não encontrado")
    void testUpdate_NotFound() {
        Beneficio dadosAtualizados = new Beneficio();
        dadosAtualizados.setNome("Novo Nome");

        when(beneficioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> beneficioService.update(99L, dadosAtualizados))
                .isInstanceOf(BeneficioNotFoundException.class)
                .hasMessageContaining("O benefício de id 99 não foi encontrado.");

        verify(beneficioRepository, times(1)).findById(99L);
        verify(beneficioRepository, never()).create(any());
    }

    @Test
    @DisplayName("Deve deletar um benefício com sucesso")
    void testDelete_Success() {
        when(beneficioRepository.delete(1L)).thenReturn(true);

        beneficioService.delete(1L);

        verify(beneficioRepository, times(1)).delete(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar benefício não encontrado")
    void testDelete_NotFound() {
        when(beneficioRepository.delete(99L)).thenReturn(false);

        assertThatThrownBy(() -> beneficioService.delete(99L))
                .isInstanceOf(BeneficioNotFoundException.class)
                .hasMessageContaining("O benefício de id 99 não foi encontrado.");

        verify(beneficioRepository, times(1)).delete(99L);
    }

    @Test
    @DisplayName("Deve transferir valor entre benefícios com sucesso")
    void testTransferir_Success() {
        BigDecimal valor = new BigDecimal("100.00");
        doNothing().when(beneficioRepository).transfer(1L, 2L, valor);

        beneficioService.transferir(1L, 2L, valor);

        verify(beneficioRepository, times(1)).transfer(1L, 2L, valor);
    }
}