package com.banco.operaciones_bancarias.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banco.operaciones_bancarias.component.AuditoriaLogger;
import com.banco.operaciones_bancarias.component.CoreBancarioSofka;
import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;
import com.banco.operaciones_bancarias.utils.Constants;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class TransaccionesServiceImplTest {

    @Mock
    private CoreBancarioSofka coreBancarioSofka;

    @Mock
    private AuditoriaLogger auditoriaLogger;

    @InjectMocks
    private TransaccionesServiceImpl transaccionesService;

    private static final String TOKEN = "Bearer token123";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /*@Test
    void procesarRetiro_Success() {
        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        request.setNumeroCuenta(12345);

        Map<String, Object> saldoMap = new HashMap<>();
        saldoMap.put("saldo", "1000");

        ResponseDTO<Object> saldoResponse = new ResponseDTO<>();
        saldoResponse.setResponse(saldoMap);

        ResponseDTO<Object> retiroResponse = new ResponseDTO<>();
        retiroResponse.setCode(200);
        retiroResponse.setMessage("Retiro exitoso");

        when(coreBancarioSofka.saldoCuenta(anyInt(), anyString())).thenReturn(Mono.just(saldoResponse));
        when(auditoriaLogger.logEventoAuditoria(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());
        when(coreBancarioSofka.retiroCuenta(any(), anyString())).thenReturn(Mono.just(retiroResponse));

        StepVerifier.create(transaccionesService.procesarRetiro(request, TOKEN))
                .expectNextMatches(response -> response.getCode() == 200 && response.getMessage().equals("Retiro exitoso"))
                .verifyComplete();
    }*/

    @Test
    void procesarRetiro_ErrorSaldo() {
        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        request.setNumeroCuenta(12345);

        ResponseDTO<Object> saldoResponse = new ResponseDTO<>();
        saldoResponse.setResponse(new HashMap<>()); // Sin saldo en la respuesta

        when(coreBancarioSofka.saldoCuenta(anyInt(), anyString())).thenReturn(Mono.just(saldoResponse));

        StepVerifier.create(transaccionesService.procesarRetiro(request, TOKEN))
                .expectError()
                .verify();
    }

    @Test
    void procesarRetiro_ErrorEnRetiro() {
        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        request.setNumeroCuenta(12345);

        Map<String, Object> saldoMap = new HashMap<>();
        saldoMap.put("saldo", "1000");

        ResponseDTO<Object> saldoResponse = new ResponseDTO<>();
        saldoResponse.setResponse(saldoMap);

        ResponseDTO<Object> retiroResponse = new ResponseDTO<>();
        retiroResponse.setCode(400);
        retiroResponse.setMessage("Error en el retiro");

        when(coreBancarioSofka.saldoCuenta(anyInt(), anyString())).thenReturn(Mono.just(saldoResponse));
        when(auditoriaLogger.logEventoAuditoria(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());
        when(coreBancarioSofka.retiroCuenta(any(), anyString())).thenReturn(Mono.just(retiroResponse));

        StepVerifier.create(transaccionesService.procesarRetiro(request, TOKEN))
                .expectNextMatches(response -> response.getCode() == 400 && response.getMessage().equals("Error en el retiro"))
                .verifyComplete();
    }

    /*@Test
    void procesarDeposito_Success() {
        DepositoCuentaRequestDTO request = new DepositoCuentaRequestDTO();
        request.setNumeroCuenta(12345);

        Map<String, Object> saldoMap = new HashMap<>();
        saldoMap.put("saldo", "1000");

        ResponseDTO<Object> saldoResponse = new ResponseDTO<>();
        saldoResponse.setResponse(saldoMap);

        ResponseDTO<Object> depositoResponse = new ResponseDTO<>();
        depositoResponse.setCode(200);
        depositoResponse.setMessage("Depósito exitoso");

        when(coreBancarioSofka.saldoCuenta(anyInt(), anyString())).thenReturn(Mono.just(saldoResponse));
        when(auditoriaLogger.logEventoAuditoriaDeposito(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());
        when(coreBancarioSofka.depositoCuenta(any(), anyString())).thenReturn(Mono.just(depositoResponse));

        StepVerifier.create(transaccionesService.procesarDeposito(request, TOKEN))
                .expectNextMatches(response -> response.getCode() == 200 && response.getMessage().equals("Depósito exitoso"))
                .verifyComplete();
    }*/

    @Test
    void procesarDeposito_ErrorEnDeposito() {
        DepositoCuentaRequestDTO request = new DepositoCuentaRequestDTO();
        request.setNumeroCuenta(12345);

        Map<String, Object> saldoMap = new HashMap<>();
        saldoMap.put("saldo", "1000");

        ResponseDTO<Object> saldoResponse = new ResponseDTO<>();
        saldoResponse.setResponse(saldoMap);

        ResponseDTO<Object> depositoResponse = new ResponseDTO<>();
        depositoResponse.setCode(400);
        depositoResponse.setMessage("Error en el depósito");

        when(coreBancarioSofka.saldoCuenta(anyInt(), anyString())).thenReturn(Mono.just(saldoResponse));
        when(auditoriaLogger.logEventoAuditoriaDeposito(any(), any(), any(), any(BigDecimal.class))).thenReturn(Mono.empty());
        when(coreBancarioSofka.depositoCuenta(any(), anyString())).thenReturn(Mono.just(depositoResponse));

        StepVerifier.create(transaccionesService.procesarDeposito(request, TOKEN))
                .expectNextMatches(response -> response.getCode() == 400 && response.getMessage().equals("Error en el depósito"))
                .verifyComplete();
    }
}
