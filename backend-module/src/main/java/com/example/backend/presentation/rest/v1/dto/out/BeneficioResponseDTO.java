package com.example.backend.presentation.rest.v1.dto.out;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BeneficioResponseDTO {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal valor;
    private Boolean ativo;
}
