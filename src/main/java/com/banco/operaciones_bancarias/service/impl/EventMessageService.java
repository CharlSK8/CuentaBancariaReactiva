package com.banco.operaciones_bancarias.service.impl;

import com.banco.operaciones_bancarias.model.EventMessage;
import com.banco.operaciones_bancarias.repository.IEventMessageRepository;
import com.banco.operaciones_bancarias.service.IEventMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.jms.MessageConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class EventMessageService implements IEventMessageService {

    private final IEventMessageRepository eventMessageRepository;
    private static final Logger LOGGER = LoggerFactory.getLogger(EventMessageService.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public EventMessageService(IEventMessageRepository eventMessageRepository) {
        this.eventMessageRepository = eventMessageRepository;
    }

    @Override
    public Mono<Void> saveMongoDbMessages(String app, String queueName, String eventMessage) {
        try {
            // Convertir el JSON String a Map
            Map<String, Object> messageMap = objectMapper.readValue(eventMessage, Map.class);

            // Crear el objeto con el Map en lugar de un String
            EventMessage eventMessageModel = new EventMessage(app, queueName, messageMap);

            return eventMessageRepository.save(eventMessageModel)
                    .doOnSuccess(msg -> LOGGER.info("Mensaje guardado en MongoDB: APP = {}, Cola: {}: {}",app, queueName, msg))
                    .doOnError(error -> LOGGER.error("Error al guardar en MongoDB de la APP:{} error: {}",app, error.getMessage()))
                    .then();
        } catch (Exception e) {
            LOGGER.error("Error al convertir JSON: {}", e.getMessage(), e);
            return Mono.empty(); // Retorna vacío en caso de error
        }
    }
}
