package com.banco.operaciones_bancarias.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("broker_messages")
public class EventMessage {

    private String app;
    private String Queue;
    private Map<String, Object> message;
}
