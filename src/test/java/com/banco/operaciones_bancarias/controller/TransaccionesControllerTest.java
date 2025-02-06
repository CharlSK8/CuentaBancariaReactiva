package com.banco.operaciones_bancarias.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Objects;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import com.banco.operaciones_bancarias.component.AuditoriaLogger;
import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;
import com.banco.operaciones_bancarias.service.ITransaccionesService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TransaccionesControllerTest {

    @Mock
    private ITransaccionesService transaccionesService;

    @Mock
    private AuditoriaLogger auditoriaLogger;

    @InjectMocks
    private TransaccionesController transaccionesController;

    private static final String TOKEN = "Bearer token123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void procesarRetiro_Success() {
        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setCode(200);
        responseDTO.setMessage("Retiro exitoso");
        responseDTO.setResponse("Transacción completada");

        when(transaccionesService.procesarRetiro(any(), anyString())).thenReturn(Mono.just(responseDTO));
        when(auditoriaLogger.logEventoAuditoria(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());

        StepVerifier.create(transaccionesController.procesarRetiro(request, TOKEN))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful() &&
                        Objects.requireNonNull(response.getBody()).getMessage().equals("Retiro exitoso"))
                .verifyComplete();
    }

    @Test
    void procesarRetiro_Error() {
        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        String errorMessage = "Error en el retiro";

        when(transaccionesService.procesarRetiro(any(), anyString())).thenReturn(Mono.error(new RuntimeException(errorMessage)));
        when(auditoriaLogger.logEventoAuditoria(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());

        StepVerifier.create(transaccionesController.procesarRetiro(request, TOKEN))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                        Objects.requireNonNull(response.getBody()).getMessage().equals(errorMessage))
                .verifyComplete();
    }

    @Test
    void procesarDeposito_Success() {
        DepositoCuentaRequestDTO request = new DepositoCuentaRequestDTO();
        ResponseDTO<Object> responseDTO = new ResponseDTO<>();
        responseDTO.setCode(200);
        responseDTO.setMessage("Depósito exitoso");
        responseDTO.setResponse("Transacción completada");

        when(transaccionesService.procesarDeposito(any(), anyString())).thenReturn(Mono.just(responseDTO));
        when(auditoriaLogger.logEventoAuditoriaDeposito(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());

        StepVerifier.create(transaccionesController.procesarDeposito(request, TOKEN))
                .expectNextMatches(response -> response.getStatusCode().is2xxSuccessful() &&
                        Objects.requireNonNull(response.getBody()).getMessage().equals("Depósito exitoso"))
                .verifyComplete();
    }

    @Test
    void procesarDeposito_Error() {
        DepositoCuentaRequestDTO request = new DepositoCuentaRequestDTO();
        String errorMessage = "Error en el depósito";

        when(transaccionesService.procesarDeposito(any(), anyString())).thenReturn(Mono.error(new RuntimeException(errorMessage)));
        when(auditoriaLogger.logEventoAuditoriaDeposito(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());

        StepVerifier.create(transaccionesController.procesarDeposito(request, TOKEN))
                .expectNextMatches(response -> response.getStatusCode() == HttpStatus.BAD_REQUEST &&
                        Objects.requireNonNull(response.getBody()).getMessage().equals(errorMessage))
                .verifyComplete();
    }
}
