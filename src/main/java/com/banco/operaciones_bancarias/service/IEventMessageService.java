package com.banco.operaciones_bancarias.service;

import com.banco.operaciones_bancarias.model.EventMessage;
import reactor.core.publisher.Mono;

public interface IEventMessageService {
    public Mono<Void> saveMongoDbMessages(String app, String queueName, String eventMessage);
}
