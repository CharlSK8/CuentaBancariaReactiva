package com.banco.operaciones_bancarias.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banco.operaciones_bancarias.component.AuditoriaLogger;
import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;
import com.banco.operaciones_bancarias.service.ITransaccionesService;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

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
        void procesarRetiro_CuandoExitoso_RetornaResponseOk() {
                RetiroCuentaRequestDTO request = RetiroCuentaRequestDTO.builder()
                        .numeroCuenta(12345)
                        .monto(BigDecimal.valueOf(1000))
                        .build();
                
                ResponseDTO<Object> expectedResponse = ResponseDTO.builder()
                        .code(200)
                        .message("Retiro exitoso")
                        .response("Transacción completada")
                        .build();
                
                when(transaccionesService.procesarRetiro(any(), any()))
                        .thenReturn(Mono.just(expectedResponse));
                when(auditoriaLogger.logEventoAuditoria(any(), any(), any()))
                        .thenReturn(Mono.empty());

                StepVerifier.create(transaccionesController.procesarRetiro(request, TOKEN))
                        .expectNextMatches(responseEntity -> 
                        responseEntity.getStatusCode().is2xxSuccessful() &&
                        responseEntity.getBody().getMessage().equals("Retiro exitoso"))
                        .verifyComplete();
        }

        @Test
        void procesarDeposito_CuandoExitoso_RetornaResponseOk() {
                DepositoCuentaRequestDTO request = DepositoCuentaRequestDTO.builder()
                        .numeroCuenta(12345)
                        .monto(BigDecimal.valueOf(1000))
                        .build();
                
                ResponseDTO<Object> expectedResponse = ResponseDTO.builder()
                        .code(200)
                        .message("Depósito exitoso")
                        .response("Transacción completada")
                        .build();
                
                when(transaccionesService.procesarDeposito(any(), any()))
                        .thenReturn(Mono.just(expectedResponse));
                when(auditoriaLogger.logEventoAuditoriaDeposito(any(), any(), any(), any()))
                        .thenReturn(Mono.empty());

                StepVerifier.create(transaccionesController.procesarDeposito(request, TOKEN))
                        .expectNextMatches(responseEntity -> 
                        responseEntity.getStatusCode().is2xxSuccessful() &&
                        responseEntity.getBody().getMessage().equals("Depósito exitoso"))
                        .verifyComplete();
        }

        @Test
        void procesarRetiro_CuandoError_RetornaBadRequest() {
                RetiroCuentaRequestDTO request = RetiroCuentaRequestDTO.builder()
                        .numeroCuenta(12345)
                        .monto(BigDecimal.valueOf(1000))
                        .build();
                
                when(transaccionesService.procesarRetiro(any(), any()))
                        .thenReturn(Mono.error(new RuntimeException("Error en el retiro")));
                when(auditoriaLogger.logEventoAuditoria(any(), any(), any()))
                        .thenReturn(Mono.empty());

                StepVerifier.create(transaccionesController.procesarRetiro(request, TOKEN))
                        .expectNextMatches(responseEntity -> 
                        responseEntity.getStatusCode().is4xxClientError() &&
                        responseEntity.getBody().getMessage().equals("Error en el retiro"))
                        .verifyComplete();
        }
}
