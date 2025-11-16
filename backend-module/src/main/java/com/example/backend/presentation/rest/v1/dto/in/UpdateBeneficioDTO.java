package com.example.backend.presentation.rest.v1.dto.in;

import com.example.backend.infrastructure.validation.AtLeastOneFieldNotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AtLeastOneFieldNotNull
public class UpdateBeneficioDTO {
    private String nome;

    private String descricao;

    private BigDecimal valor;

    private Boolean ativo;
}
