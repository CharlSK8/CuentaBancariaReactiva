package com.banco.operaciones_bancarias.model;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("evento_auditoria")
public class EventoAuditoria {

    private int cuentaId;
    private BigDecimal monto;
    private Instant fecha;
    private String estado;
    private String tipoTransaccion;
    private BigDecimal saldoActual;

}
