package com.banco.operaciones_bancarias.mapper;

import java.math.BigDecimal;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.model.EventoAuditoria;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IEventoAuditoriaMapper {

    @Mapping(target = "cuentaId", source = "request.numeroCuenta")
    @Mapping(target = "monto", source = "request.monto")
    @Mapping(target = "fecha", expression = "java(java.time.Instant.now())")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "tipoTransaccion", source = "tipoTransaccion")
    EventoAuditoria toEventoAuditoria(String estado, RetiroCuentaRequestDTO request, String tipoTransaccion);

    @Mapping(target = "cuentaId", source = "request.numeroCuenta")
    @Mapping(target = "monto", source = "request.monto")
    @Mapping(target = "fecha", expression = "java(java.time.Instant.now())")
    @Mapping(target = "estado", source = "estado")
    @Mapping(target = "tipoTransaccion", source = "tipoTransaccion")
    @Mapping(target = "saldoActual", source = "saldoActual")
    EventoAuditoria toEventoAuditoriaDeposito(String estado, DepositoCuentaRequestDTO request, String tipoTransaccion, BigDecimal saldoActual);

}
