package com.banco.operaciones_bancarias.service.impl;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.repository.IEventoAuditoriaRepository;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

class EventoAuditoriaServiceImplTest {

    @Mock
    private IEventoAuditoriaRepository eventoAuditoriaRepository;

    @InjectMocks
    private EventoAuditoriaServiceImpl eventoAuditoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void streamEventosAuditoria_ShouldReturnEventos() {
        EventoAuditoria evento = new EventoAuditoria();
        when(eventoAuditoriaRepository.findWithTailableCursorByCuentaId(anyInt())).thenReturn(Flux.just(evento));

        StepVerifier.create(eventoAuditoriaService.streamEventosAuditoria(123))
                .expectNext(evento)
                .verifyComplete();
    }
}
