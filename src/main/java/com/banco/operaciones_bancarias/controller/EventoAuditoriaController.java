package com.banco.operaciones_bancarias.controller;

import org.springframework.web.bind.annotation.*;
import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/auditoria")
public class EventoAuditoriaController {

    private final IEventoAuditoriaService eventoAuditoriaService;

    public EventoAuditoriaController(IEventoAuditoriaService eventoAuditoriaService) {
        this.eventoAuditoriaService = eventoAuditoriaService;
    }

    @GetMapping("/stream/{cuentaId}")
    public Flux<EventoAuditoria> streamEventos(@PathVariable int cuentaId) {
        return eventoAuditoriaService.streamEventosAuditoria(cuentaId);
    }
}
