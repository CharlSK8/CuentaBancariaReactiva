package com.banco.operaciones_bancarias.controller;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class EventoAuditoriaControllerTest {

    @Mock
    private IEventoAuditoriaService eventoAuditoriaService;

    @InjectMocks
    private EventoAuditoriaController eventoAuditoriaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void streamEventoAuditoria_ShouldReturnEventos() {
        EventoAuditoria evento = new EventoAuditoria();
        when(eventoAuditoriaService.streamEventosAuditoria(anyInt())).thenReturn(Flux.just(evento));

        StepVerifier.create(eventoAuditoriaController.streamEventoAuditoria(123))
                .expectNext(evento)
                .verifyComplete();
    }

    @Test
    void streamEventoAuditoria_ShouldHandleError() {
        when(eventoAuditoriaService.streamEventosAuditoria(anyInt()))
                .thenReturn(Flux.error(new RuntimeException("Error")));

        StepVerifier.create(eventoAuditoriaController.streamEventoAuditoria(123))
                .expectError(ResponseStatusException.class)
                .verify();
    }
}
