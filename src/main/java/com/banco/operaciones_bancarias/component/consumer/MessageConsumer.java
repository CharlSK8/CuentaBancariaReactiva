package com.banco.operaciones_bancarias.component.consumer;

import com.banco.operaciones_bancarias.common.SystemMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    private  static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @JmsListener(destination = "QueueAuthCustomer")
    public void messageListener(String messageProducer) throws JsonProcessingException {
        SystemMessage message = objectMapper.readValue(messageProducer, SystemMessage.class);
        LOGGER.info("Mensaje recivido en la segunda aplicación: {}", message);
    }
}
