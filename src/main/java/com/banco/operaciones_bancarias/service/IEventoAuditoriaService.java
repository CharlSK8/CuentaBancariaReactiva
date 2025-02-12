package com.banco.operaciones_bancarias.service;

import com.banco.operaciones_bancarias.model.EventoAuditoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IEventoAuditoriaService {

    //Metodo para recibir y guardar eventos desde ActiveMQ
    Mono<EventoAuditoria> guardarEvento(EventoAuditoria evento);

    // Metodo para hacer streaming de eventos en tiempo real desde MongoDB
    Flux<EventoAuditoria> streamEventosAuditoria(int cuentaId);
}
