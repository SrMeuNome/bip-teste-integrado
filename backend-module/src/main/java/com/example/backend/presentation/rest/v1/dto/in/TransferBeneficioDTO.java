package com.example.backend.presentation.rest.v1.dto.in;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransferBeneficioDTO {

    @NotNull(message = "ID do benefício de origem é obrigatório")
    private Long fromId;

    @NotNull(message = "ID do benefício de destino é obrigatório")
    private Long toId;

    @NotNull(message = "Valor da transferência é obrigatório")
    @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero")
    private BigDecimal amount;
}
