package com.banco.operaciones_bancarias.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;

import com.banco.operaciones_bancarias.model.EventoAuditoria;

import reactor.core.publisher.Flux;

@Repository
public interface IEventoAuditoriaRepository extends ReactiveMongoRepository<EventoAuditoria, String>{

    @Tailable
	public Flux<EventoAuditoria> findWithTailableCursorByCuentaId(int cuentaId);

}
