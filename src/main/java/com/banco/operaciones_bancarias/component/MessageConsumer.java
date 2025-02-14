package com.banco.operaciones_bancarias.component;

import com.banco.operaciones_bancarias.service.impl.EventMessageService;
import jakarta.jms.TextMessage;
import org.apache.activemq.command.ActiveMQTextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @Autowired
    private EventMessageService eventMessageService;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);

    @JmsListener(destination = "auth-queue", containerFactory = "jmsListenerContainerFactory")
    public void listenAuthQueue(Object eventMessage) {
        processMessage(eventMessage, "auth-queue");
    }

    @JmsListener(destination = "op_bank_react-queue", containerFactory = "jmsListenerContainerFactory")
    public void listenOpBankReactQueue(Object eventMessage) {
        processMessage(eventMessage, "op_bank_react-queue");
    }

    public void processMessage(Object eventMessage, String queueName) {
        try {
            String jsonMessage;

            // Extraer el contenido del mensaje si es de tipo ActiveMQTextMessage
            if (eventMessage instanceof ActiveMQTextMessage) {
                jsonMessage = ((ActiveMQTextMessage) eventMessage).getText();
            } else if (eventMessage instanceof TextMessage) {
                jsonMessage = ((TextMessage) eventMessage).getText();
            } else {
                LOGGER.warn("Tipo de mensaje desconocido: {}", eventMessage.getClass().getName());
                return;
            }
            System.out.println("📩 Evento recibido de " + queueName + ": " + jsonMessage);
            LOGGER.info("JSON Message received from {}: {}", queueName, jsonMessage);

            eventMessageService.saveMongoDbMessages(queueName, jsonMessage).subscribe();

        } catch (Exception e) {
            LOGGER.error("Error processing message from {}: {}", queueName, e.getMessage(), e);
        }
    }
}
