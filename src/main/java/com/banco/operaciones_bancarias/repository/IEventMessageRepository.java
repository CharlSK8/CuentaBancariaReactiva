package com.banco.operaciones_bancarias.repository;

import com.banco.operaciones_bancarias.model.EventMessage;
import com.banco.operaciones_bancarias.model.EventoAuditoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.mongodb.repository.Tailable;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IEventMessageRepository extends ReactiveMongoRepository<EventMessage, String>{
}
