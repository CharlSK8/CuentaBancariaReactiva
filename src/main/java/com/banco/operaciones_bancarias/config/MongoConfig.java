package com.banco.operaciones_bancarias.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;


@Configuration
@RequiredArgsConstructor
public class MongoConfig {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @PostConstruct
    public void createCappedCollection() {
        String collectionName = "evento_auditoria";
        reactiveMongoTemplate.collectionExists(collectionName)
            .flatMap(exists -> {
                if (Boolean.TRUE.equals(exists)) {
                    return reactiveMongoTemplate.createCollection(collectionName, 
                        CollectionOptions.empty().capped().size(10485760)
                    );
                } else {
                    return Mono.empty();
                }
            })
            .subscribe();
    }
}

