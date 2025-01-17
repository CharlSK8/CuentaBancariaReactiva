package com.banco.operaciones_bancarias.service.impl;

import org.springframework.stereotype.Service;

import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.repository.IEventoAuditoriaRepository;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class EventoAuditoriaServiceImpl implements IEventoAuditoriaService{

    private final IEventoAuditoriaRepository eventoAuditoriaRepository;

    @Override
    public Flux<EventoAuditoria> streamEventosAuditoria(int cuentaId) {
        return eventoAuditoriaRepository.findWithTailableCursorByCuentaId(cuentaId);
    }

}
