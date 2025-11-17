package com.example.backend.presentation.rest.v1.mapper;

import com.example.backend.presentation.rest.v1.dto.in.CreateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.in.UpdateBeneficioDTO;
import com.example.backend.presentation.rest.v1.dto.out.BeneficioResponseDTO;
import com.example.domain.model.Beneficio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes do BeneficioDTOsMapper")
class BeneficioDTOsMapperTest {

    private BeneficioDTOsMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new BeneficioDTOsMapper();
    }

    @Test
    @DisplayName("Deve mapear CreateBeneficioDTO para Beneficio corretamente")
    void testToBeneficio_FromCreateDTO() {
        CreateBeneficioDTO createDTO = new CreateBeneficioDTO();
        createDTO.setNome("Vale Alimentação");
        createDTO.setDescricao("Benefício de alimentação");
        createDTO.setValor(new BigDecimal("500.00"));
        createDTO.setAtivo(true);

        Beneficio beneficio = mapper.toBeneficio(createDTO);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getNome()).isEqualTo("Vale Alimentação");
        assertThat(beneficio.getDescricao()).isEqualTo("Benefício de alimentação");
        assertThat(beneficio.getValor()).isEqualByComparingTo(new BigDecimal("500.00"));
        assertThat(beneficio.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve mapear UpdateBeneficioDTO para Beneficio corretamente")
    void testToBeneficio_FromUpdateDTO() {
        UpdateBeneficioDTO updateDTO = new UpdateBeneficioDTO();
        updateDTO.setNome("Vale Transporte");
        updateDTO.setDescricao("Benefício de transporte");
        updateDTO.setValor(new BigDecimal("300.00"));
        updateDTO.setAtivo(false);

        Beneficio beneficio = mapper.toBeneficio(updateDTO);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getNome()).isEqualTo("Vale Transporte");
        assertThat(beneficio.getDescricao()).isEqualTo("Benefício de transporte");
        assertThat(beneficio.getValor()).isEqualByComparingTo(new BigDecimal("300.00"));
        assertThat(beneficio.getAtivo()).isFalse();
    }

    @Test
    @DisplayName("Deve mapear Beneficio para BeneficioResponseDTO corretamente")
    void testToBeneficioResponseDTO() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(1L);
        beneficio.setNome("Vale Refeição");
        beneficio.setDescricao("Benefício de refeição");
        beneficio.setValor(new BigDecimal("450.00"));
        beneficio.setAtivo(true);

        BeneficioResponseDTO responseDTO = mapper.toBeneficioResponseDTO(beneficio);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(1L);
        assertThat(responseDTO.getNome()).isEqualTo("Vale Refeição");
        assertThat(responseDTO.getDescricao()).isEqualTo("Benefício de refeição");
        assertThat(responseDTO.getValor()).isEqualByComparingTo(new BigDecimal("450.00"));
        assertThat(responseDTO.getAtivo()).isTrue();
    }

    @Test
    @DisplayName("Deve mapear CreateBeneficioDTO com valores nulos")
    void testToBeneficio_FromCreateDTO_WithNulls() {
        CreateBeneficioDTO createDTO = new CreateBeneficioDTO();
        createDTO.setNome(null);
        createDTO.setDescricao(null);
        createDTO.setValor(null);
        createDTO.setAtivo(null);

        Beneficio beneficio = mapper.toBeneficio(createDTO);

        assertThat(beneficio).isNotNull();
        assertThat(beneficio.getNome()).isNull();
        assertThat(beneficio.getDescricao()).isNull();
        assertThat(beneficio.getValor()).isNull();
        assertThat(beneficio.getAtivo()).isNull();
    }

    @Test
    @DisplayName("Deve mapear Beneficio com todos os campos preenchidos")
    void testToBeneficioResponseDTO_AllFields() {
        Beneficio beneficio = new Beneficio();
        beneficio.setId(10L);
        beneficio.setNome("Vale Cultura");
        beneficio.setDescricao("Benefício cultural");
        beneficio.setValor(new BigDecimal("200.00"));
        beneficio.setAtivo(false);
        beneficio.setVersion(5L);

        BeneficioResponseDTO responseDTO = mapper.toBeneficioResponseDTO(beneficio);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(10L);
        assertThat(responseDTO.getNome()).isEqualTo("Vale Cultura");
        assertThat(responseDTO.getDescricao()).isEqualTo("Benefício cultural");
        assertThat(responseDTO.getValor()).isEqualByComparingTo(new BigDecimal("200.00"));
        assertThat(responseDTO.getAtivo()).isFalse();
    }
}