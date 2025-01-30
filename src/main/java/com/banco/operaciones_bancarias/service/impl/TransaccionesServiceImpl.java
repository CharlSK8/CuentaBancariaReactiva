package com.banco.operaciones_bancarias.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.banco.operaciones_bancarias.component.AuditoriaLogger;
import com.banco.operaciones_bancarias.component.CoreBancarioSofka;
import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;
import com.banco.operaciones_bancarias.service.ITransaccionesService;
import com.banco.operaciones_bancarias.utils.Constants;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TransaccionesServiceImpl implements ITransaccionesService{

    private final CoreBancarioSofka coreBancarioSofka;
    private final AuditoriaLogger auditoriaLogger;
    private BigDecimal saldoActual;

    @Override
    public Mono<ResponseDTO<?>> procesarRetiro(RetiroCuentaRequestDTO request, String token) {
        return auditoriaLogger.logEventoAuditoria(Constants.INICIO, request, Constants.RETIRO)
                .then(coreBancarioSofka.retiroCuenta(request, token))
                .flatMap(response -> {
                    if (response.getCode() != 200) {
                        return auditoriaLogger.logEventoAuditoria(Constants.ERROR, request, Constants.RETIRO)
                                .then(Mono.just(response));
                    }
                    //Llamar servicio y enviar saldo actual
                    return coreBancarioSofka.saldoCuenta(request.getNumeroCuenta(), token)
                        .flatMap(saldoResponse -> 
                            auditoriaLogger.logEventoAuditoria(Constants.EXITO, request,saldoResponse.getResponse().toString())
                                .then(Mono.just(response))
                        );
                });
    }

    @Override
public Mono<ResponseDTO<?>> procesarDeposito(DepositoCuentaRequestDTO request, String token) {
    return coreBancarioSofka.saldoCuenta(request.getNumeroCuenta(), token)
        .flatMap(saldoResponse -> {
            saldoActual = BigDecimal.ZERO;
            if (saldoResponse.getResponse() instanceof Map<?, ?> responseMap) {
                saldoActual = new BigDecimal(responseMap.get("saldo").toString());
            }
            return auditoriaLogger.logEventoAuditoriaDeposito(Constants.INICIO, request, Constants.DEPOSITO, saldoActual)
                .then(coreBancarioSofka.depositoCuenta(request, token))
                .flatMap(depositoResponse -> {
                    if (depositoResponse.getCode() != 200) {
                        return auditoriaLogger.logEventoAuditoriaDeposito(Constants.ERROR, request, Constants.DEPOSITO, saldoActual)
                            .then(Mono.just(depositoResponse));
                    }
                    return coreBancarioSofka.saldoCuenta(request.getNumeroCuenta(), token)
                        .flatMap(nuevoSaldoResponse -> {
                            BigDecimal nuevoSaldo = BigDecimal.ZERO;
                            if (nuevoSaldoResponse.getResponse() instanceof Map<?, ?> nuevoSaldoMap) {
                                nuevoSaldo = new BigDecimal(nuevoSaldoMap.get("saldo").toString());
                            }
                            return auditoriaLogger.logEventoAuditoriaDeposito(Constants.EXITO, request, Constants.DEPOSITO, nuevoSaldo)
                                .then(Mono.just(depositoResponse));
                        });
                });
        });
}

}
