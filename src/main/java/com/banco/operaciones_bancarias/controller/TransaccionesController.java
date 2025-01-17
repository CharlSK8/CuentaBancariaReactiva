package com.banco.operaciones_bancarias.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banco.operaciones_bancarias.component.AuditoriaLogger;
import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;
import com.banco.operaciones_bancarias.service.ITransaccionesService;
import com.banco.operaciones_bancarias.utils.Constants;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transacciones")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(name = "Transacciones", description = "API para transacciones bancarias")
public class TransaccionesController {
    private final ITransaccionesService transaccionesService;
    private final AuditoriaLogger auditoriaLogger;

    @PostMapping("/retiro")
    @Operation(summary = "Procesar retiro", description = "Procesar retiro de una cuenta")
    public Mono<ResponseEntity<ResponseDTO<Object>>> procesarRetiro(@RequestBody RetiroCuentaRequestDTO request, @RequestHeader("Authorization") String token) {
        return transaccionesService.procesarRetiro(request, token)
                .map(result -> ResponseEntity.ok(ResponseDTO.builder().response(result.getResponse()).code(result.getCode()).message(result.getMessage()).build()))
                .doOnError(e -> auditoriaLogger.logEventoAuditoria(Constants.ERROR, request, Constants.RETIRO).subscribe())
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(ResponseDTO.builder().message(e.getMessage()).code(HttpStatus.BAD_REQUEST.value()).build())));
    }
}
