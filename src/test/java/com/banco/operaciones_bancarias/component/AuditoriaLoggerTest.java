package com.banco.operaciones_bancarias.component;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.mapper.IEventoAuditoriaMapper;
import com.banco.operaciones_bancarias.repository.IEventoAuditoriaRepository;

import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class AuditoriaLoggerTest {

    @Mock
    private IEventoAuditoriaRepository eventoAuditoriaRepository;

    @Mock
    private IEventoAuditoriaMapper eventoAuditoriaMapper;

    @InjectMocks
    private AuditoriaLogger auditoriaLogger;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void logEventoAuditoria_ShouldSaveEvento() {
        when(eventoAuditoriaRepository.save(any())).thenReturn(Mono.empty());

        RetiroCuentaRequestDTO request = new RetiroCuentaRequestDTO();
        BigDecimal saldoActual = BigDecimal.valueOf(5000);

        StepVerifier.create(auditoriaLogger.logEventoAuditoria("APROBADO", request, "RETIRO", saldoActual))
                .verifyComplete();
    }

    @Test
    void logEventoAuditoriaDeposito_ShouldSaveEvento() {
        when(eventoAuditoriaRepository.save(any())).thenReturn(Mono.empty());

        DepositoCuentaRequestDTO request = new DepositoCuentaRequestDTO();
        BigDecimal saldoActual = BigDecimal.valueOf(8000);

        StepVerifier.create(auditoriaLogger.logEventoAuditoriaDeposito("APROBADO", request, "DEPÓSITO", saldoActual))
                .verifyComplete();
    }
}
