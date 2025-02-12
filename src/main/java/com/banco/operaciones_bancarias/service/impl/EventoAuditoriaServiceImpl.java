package com.banco.operaciones_bancarias.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.repository.IEventoAuditoriaRepository;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class EventoAuditoriaServiceImpl implements IEventoAuditoriaService {

    @Autowired
    private IEventoAuditoriaRepository eventoAuditoriaRepository;

    // Guardar evento de auditoría cuando se recibe desde ActiveMQ
    @Override
    public Mono<EventoAuditoria> guardarEvento(EventoAuditoria evento) {
        return eventoAuditoriaRepository.save(evento);
    }

    // Streaming en tiempo real de eventos de auditoría por cuenta
    @Override
    public Flux<EventoAuditoria> streamEventosAuditoria(int cuentaId) {
        return eventoAuditoriaRepository.findWithTailableCursorByCuentaId(cuentaId);
    }
}
