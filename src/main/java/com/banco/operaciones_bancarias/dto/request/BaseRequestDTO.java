package com.banco.operaciones_bancarias.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseRequestDTO {

    @NotBlank(message = "El campo 'numeroCuenta' es obligatorio")
    private int numeroCuenta;
    @NotBlank(message = "El campo 'monto' es obligatorio")
    @PositiveOrZero(message = "El monto debe ser mayor que 0")
    private BigDecimal monto;

}
