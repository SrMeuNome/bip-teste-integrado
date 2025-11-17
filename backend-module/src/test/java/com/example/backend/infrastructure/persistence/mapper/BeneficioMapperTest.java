package com.example.backend.infrastructure.persistence.mapper;

import com.example.domain.model.Beneficio;
import com.example.persistence.repository.jpa.entity.BeneficioEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do BeneficioMapper")
class BeneficioMapperTest {

    private BeneficioMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BeneficioMapper();
    }

    @Test
    @DisplayName("Deve mapear BeneficioEntity para Beneficio corretamente")
    void testToBeneficio() {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setId(1L);
        entity.setNome("Vale Alimentação");
        entity.setDescricao("Benefício de alimentação");
        entity.setValor(new BigDecimal("500.00"));
        entity.setAtivo(true);
        entity.setVersion(1L);

        Beneficio beneficio = mapper.toBeneficio(entity);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getId()).isEqualTo(1L);
        assertThat(beneficio.getNome()).isEqualTo("Vale Alimentação");
        assertThat(beneficio.getDescricao()).isEqualTo("Benefício de alimentação");
        assertThat(beneficio.getValor()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(beneficio.getAtivo()).isTrue();
        assertThat(beneficio.getVersion()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve mapear Beneficio para BeneficioEntity corretamente")
    void testToBriefStudentsEntity() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(2L);
        beneficio.setNome("Vale Transporte");
        beneficio.setDescricao("Benefício de transporte");
        beneficio.setValor(new BigDecimal("300.00"));
        beneficio.setAtivo(false);
        beneficio.setVersion(3L);

        BeneficioEntity entity = mapper.toBriefStudentsEntity(beneficio);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(2L);
        assertThat(entity.getNome()).isEqualTo("Vale Transporte");
        assertThat(entity.getDescricao()).isEqualTo("Benefício de transporte");
        assertThat(entity.getValor()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(entity.getAtivo()).isFalse();
        assertThat(entity.getVersion()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Deve mapear entity com campos nulos")
    void testToBeneficio_WithNullFields() {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setId(1L);
        entity.setNome(null);
        entity.setDescricao(null);
        entity.setValor(null);
        entity.setAtivo(null);
        entity.setVersion(null);

        Beneficio beneficio = mapper.toBeneficio(entity);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getId()).isEqualTo(1L);
        assertThat(beneficio.getNome()).isNull();
        assertThat(beneficio.getDescricao()).isNull();
        assertThat(beneficio.getValor()).isNull();
        assertThat(beneficio.getAtivo()).isNull();
        assertThat(beneficio.getVersion()).isNull();
    }

    @Test
    @DisplayName("Deve mapear beneficio com campos nulos para entity")
    void testToBriefStudentsEntity_WithNullFields() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome(null);
        beneficio.setDescricao(null);
        beneficio.setValor(null);
        beneficio.setAtivo(null);
        beneficio.setVersion(null);

        BeneficioEntity entity = mapper.toBriefStudentsEntity(beneficio);

        assertThat(entity).isNotNull();
        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getNome()).isNull();
        assertThat(entity.getDescricao()).isNull();
        assertThat(entity.getValor()).isNull();
        assertThat(entity.getAtivo()).isNull();
        assertThat(entity.getVersion()).isNull();
    }

    @Test
    @DisplayName("Deve preservar precisão do BigDecimal no mapeamento")
    void testToBeneficio_PreservesBigDecimalPrecision() {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setId(1L);
        entity.setValor(new BigDecimal("123.456"));

        Beneficio beneficio = mapper.toBeneficio(entity);

        assertThat(beneficio.getValor()).isEqualByComparingTo(new BigDecimal("123.456"));
    }

    @Test
    @DisplayName("Deve mapear corretamente benefício inativo")
    void testToBeneficio_InactiveBenefit() {
        BeneficioEntity entity = new BeneficioEntity();
        entity.setId(5L);
        entity.setNome("Benefício Desativado");
        entity.setAtivo(false);

        Beneficio beneficio = mapper.toBeneficio(entity);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getAtivo()).isFalse();
        assertThat(beneficio.getNome()).isEqualTo("Benefício Desativado");
    }
}