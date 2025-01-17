package com.banco.operaciones_bancarias.component;

import org.springframework.stereotype.Component;

import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.mapper.IEventoAuditoriaMapper;
import com.banco.operaciones_bancarias.repository.IEventoAuditoriaRepository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuditoriaLogger {

    private final IEventoAuditoriaRepository eventoAuditoriaRepository;
    private final IEventoAuditoriaMapper eventoAuditoriaMapper;

    public Mono<Void> logEventoAuditoria(String estado, RetiroCuentaRequestDTO request, String tipoTransaccion) {
        return eventoAuditoriaRepository.save(eventoAuditoriaMapper.toEventoAuditoria(estado, request, tipoTransaccion)).then();
    }

    public Mono<Void> logEventoAuditoriaDeposito(String estado, DepositoCuentaRequestDTO request, String tipoTransaccion) {
        return eventoAuditoriaRepository.save(eventoAuditoriaMapper.toEventoAuditoriaDeposito(estado, request, tipoTransaccion)).then();
    }

}
