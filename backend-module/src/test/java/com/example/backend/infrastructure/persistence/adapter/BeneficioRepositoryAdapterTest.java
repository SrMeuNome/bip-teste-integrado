package com.example.backend.infrastructure.persistence.adapter;

import com.example.backend.infrastructure.persistence.mapper.BeneficioMapper;
import com.example.backend.infrastructure.persistence.repository.BeneficioRepository;
import com.example.domain.model.Beneficio;
import com.example.ejb.BeneficioEjbServiceRemote;
import com.example.persistence.repository.jpa.entity.BeneficioEntity;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes do BeneficioRepositoryAdapter")
class BeneficioRepositoryAdapterTest {

    @Mock
    private BeneficioRepository repository;

    @Mock
    private BeneficioEjbServiceRemote ejbServiceRemote;

    @Mock
    private BeneficioMapper mapper;

    @InjectMocks
    private BeneficioRepositoryAdapter repositoryAdapter;

    private Beneficio beneficio;
    private BeneficioEntity beneficioEntity;

    @BeforeEach
    void setUp() {
        beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Vale Alimentação");
        beneficio.setDescricao("Benefício de alimentação");
        beneficio.setValor(new BigDecimal("500.00"));
        beneficio.setAtivo(true);
        beneficio.setVersion(1L);

        beneficioEntity = new BeneficioEntity();
        beneficioEntity.setId(1L);
        beneficioEntity.setNome("Vale Alimentação");
        beneficioEntity.setDescricao("Benefício de alimentação");
        beneficioEntity.setValor(new BigDecimal("500.00"));
        beneficioEntity.setAtivo(true);
        beneficioEntity.setVersion(1L);
    }

    @Test
    @DisplayName("Deve buscar benefício por ID com sucesso")
    void testFindById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(beneficioEntity));
        when(mapper.toBeneficio(beneficioEntity)).thenReturn(beneficio);

        Optional<Beneficio> resultado = repositoryAdapter.findById(1L);

        assertThat(resultado).isPresent();
        assertThat(resultado.get().getId()).isEqualTo(1L);
        assertThat(resultado.get().getNome()).isEqualTo("Vale Alimentação");
        verify(repository, times(1)).findById(1L);
        verify(mapper, times(1)).toBeneficio(beneficioEntity);
    }

    @Test
    @DisplayName("Deve retornar Optional vazio quando benefício não existir")
    void testFindById_NotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        Optional<Beneficio> resultado = repositoryAdapter.findById(99L);

        assertThat(resultado).isEmpty();
        verify(repository, times(1)).findById(99L);
        verify(mapper, never()).toBeneficio(any());
    }

    @Test
    @DisplayName("Deve listar todos os benefícios com sucesso")
    void testFindAll_Success() {
        BeneficioEntity entity2 = new BeneficioEntity();
        entity2.setId(2L);
        entity2.setNome("Vale Transporte");

        Beneficio beneficio2 = new Beneficio();
        beneficio2.setId(2L);
        beneficio2.setNome("Vale Transporte");

        List<BeneficioEntity> entities = Arrays.asList(beneficioEntity, entity2);

        when(repository.findAll()).thenReturn(entities);
        when(mapper.toBeneficio(beneficioEntity)).thenReturn(beneficio);
        when(mapper.toBeneficio(entity2)).thenReturn(beneficio2);

        List<Beneficio> resultado = repositoryAdapter.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNome()).isEqualTo("Vale Alimentação");
        assertThat(resultado.get(1).getNome()).isEqualTo("Vale Transporte");
        verify(repository, times(1)).findAll();
        verify(mapper, times(2)).toBeneficio(any(BeneficioEntity.class));
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não houver benefícios")
    void testFindAll_Empty() {
        when(repository.findAll()).thenReturn(Collections.emptyList());

        List<Beneficio> resultado = repositoryAdapter.findAll();

        assertThat(resultado).isNotNull();
        assertThat(resultado).isEmpty();
        verify(repository, times(1)).findAll();
        verify(mapper, never()).toBeneficio(any(BeneficioEntity.class));
    }

    @Test
    @DisplayName("Deve criar um benefício com sucesso")
    void testCreate_Success() {
        when(mapper.toBriefStudentsEntity(beneficio)).thenReturn(beneficioEntity);
        when(repository.save(beneficioEntity)).thenReturn(beneficioEntity);
        when(mapper.toBeneficio(beneficioEntity)).thenReturn(beneficio);

        Beneficio resultado = repositoryAdapter.create(beneficio);

        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1L);
        assertThat(resultado.getNome()).isEqualTo("Vale Alimentação");
        verify(mapper, times(1)).toBriefStudentsEntity(beneficio);
        verify(repository, times(1)).save(beneficioEntity);
        verify(mapper, times(1)).toBeneficio(beneficioEntity);
    }

    @Test
    @DisplayName("Deve deletar um benefício existente com sucesso")
    void testDelete_Success() {
        when(repository.existsById(1L)).thenReturn(true);
        doNothing().when(repository).deleteById(1L);

        boolean resultado = repositoryAdapter.delete(1L);

        assertThat(resultado).isTrue();
        verify(repository, times(1)).existsById(1L);
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Deve retornar false ao tentar deletar benefício inexistente")
    void testDelete_NotFound() {
        when(repository.existsById(99L)).thenReturn(false);

        boolean resultado = repositoryAdapter.delete(99L);

        assertThat(resultado).isFalse();
        verify(repository, times(1)).existsById(99L);
        verify(repository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Deve transferir valor entre benefícios via EJB")
    void testTransfer_Success() {
        BigDecimal valor = new BigDecimal("100.00");
        doNothing().when(ejbServiceRemote).transfer(1L, 2L, valor);

        repositoryAdapter.transfer(1L, 2L, valor);

        verify(ejbServiceRemote, times(1)).transfer(1L, 2L, valor);
    }
}