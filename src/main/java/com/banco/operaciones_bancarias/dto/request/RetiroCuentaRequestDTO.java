package com.banco.operaciones_bancarias.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RetiroCuentaRequestDTO {

    @NotBlank(message = "El campo 'numeroCuenta' es obligatorio")
    private int numeroCuenta;
    @NotBlank(message = "El campo 'monto' es obligatorio")
    private BigDecimal monto;
}
