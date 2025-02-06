package com.banco.operaciones_bancarias.config;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.mongodb.core.CollectionOptions;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Mono;

class MongoConfigTest {

    @Mock
    private ReactiveMongoTemplate reactiveMongoTemplate;

    @InjectMocks
    private MongoConfig mongoConfig;

    private static final String COLLECTION_NAME = "evento_auditoria";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCappedCollection_ShouldCreateCollection_WhenNotExists() {
        when(reactiveMongoTemplate.collectionExists(COLLECTION_NAME)).thenReturn(Mono.just(false));
        when(reactiveMongoTemplate.createCollection(eq(COLLECTION_NAME), any(CollectionOptions.class)))
                .thenReturn(Mono.empty());

        mongoConfig.createCappedCollection();

        verify(reactiveMongoTemplate, times(1)).collectionExists(COLLECTION_NAME);
        verify(reactiveMongoTemplate, times(1)).createCollection(eq(COLLECTION_NAME), any(CollectionOptions.class));
    }

    @Test
    void createCappedCollection_ShouldNotCreateCollection_WhenAlreadyExists() {
        when(reactiveMongoTemplate.collectionExists(COLLECTION_NAME)).thenReturn(Mono.just(true));

        mongoConfig.createCappedCollection();

        verify(reactiveMongoTemplate, times(1)).collectionExists(COLLECTION_NAME);
        verify(reactiveMongoTemplate, never()).createCollection(anyString(), any(CollectionOptions.class));
    }

    @Test
    void createCappedCollection_ShouldHandleErrorGracefully() {
        when(reactiveMongoTemplate.collectionExists(COLLECTION_NAME))
                .thenReturn(Mono.error(new RuntimeException("Error en MongoDB")));

        mongoConfig.createCappedCollection();

        verify(reactiveMongoTemplate, times(1)).collectionExists(COLLECTION_NAME);
    }
}
