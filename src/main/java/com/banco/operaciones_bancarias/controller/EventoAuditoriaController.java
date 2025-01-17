package com.banco.operaciones_bancarias.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.banco.operaciones_bancarias.model.EventoAuditoria;
import com.banco.operaciones_bancarias.service.IEventoAuditoriaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auditoria")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "EventoAuditoria", description = "API para eventos de auditoría")
public class EventoAuditoriaController {

    private final IEventoAuditoriaService eventoAuditoriaService;

    @GetMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "Stream de eventos de auditoría", description = "Stream de eventos de auditoría para una cuenta específica")
    public Flux<EventoAuditoria> streamEventoAuditoria(@RequestParam int cuentaId) {
        return eventoAuditoriaService.streamEventosAuditoria(cuentaId);
    }

}
