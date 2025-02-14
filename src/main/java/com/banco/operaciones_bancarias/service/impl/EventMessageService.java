package com.banco.operaciones_bancarias.service.impl;

import com.banco.operaciones_bancarias.model.EventMessage;
import com.banco.operaciones_bancarias.repository.IEventMessageRepository;
import com.banco.operaciones_bancarias.service.IEventMessageService;
import jakarta.jms.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class EventMessageService implements IEventMessageService {

    private final IEventMessageRepository eventMessageRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMessageService.class);

    public EventMessageService(IEventMessageRepository eventMessageRepository) {
        this.eventMessageRepository = eventMessageRepository;
    }

    @Override
    public Mono<Void> saveMongoDbMessages(String queueName, String eventMessage) {
        EventMessage eventMessageModel = new EventMessage(queueName, eventMessage);

        return eventMessageRepository.save(eventMessageModel)
                .doOnSuccess(msg -> LOGGER.info("Mensaje guardado en MongoDB: APP = AuditLog, {}", msg))
                .doOnError(error -> LOGGER.error("Error al guardar en MongoDB: {}", error.getMessage()))
                .then();
    }

}
