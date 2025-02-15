package com.banco.operaciones_bancarias.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEventMessage implements Serializable {
    private static final long serialVersionUID = 1l;

    private String eventType; // "DEPOSIT", "WITHDRAW"
    private Integer numeroCuenta;
    private BigDecimal monto;
}
