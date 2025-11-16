package com.example.backend.presentation.rest.v1.dto.in;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateBeneficioDTO {
    @NotNull(message = "O nome é obrigatório")
    private String nome;

    @NotNull(message = "A descrição é obrigatório")
    private String descricao;

    @NotNull(message = "O valor é obrigatório")
    private BigDecimal valor;

    private Boolean ativo = true;
}
