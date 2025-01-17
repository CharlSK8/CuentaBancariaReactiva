package com.banco.operaciones_bancarias.service.impl;

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

    @Override
    public Mono<ResponseDTO<?>> procesarRetiro(RetiroCuentaRequestDTO request, String token) {
        return auditoriaLogger.logEventoAuditoria(Constants.INICIO, request, Constants.RETIRO)
                .then(coreBancarioSofka.obtenerSaldoCuenta(request, token))
                .flatMap(response -> {
                    if (response.getCode() != 200) {
                        return auditoriaLogger.logEventoAuditoria(Constants.ERROR, request, Constants.RETIRO)
                                .then(Mono.just(response));
                    }
                    return auditoriaLogger.logEventoAuditoria(Constants.EXITO, request, Constants.RETIRO)
                            .then(Mono.just(response));
                });
    }

    @Override
    public Mono<ResponseDTO<?>> procesarDeposito(DepositoCuentaRequestDTO request, String token) {
        return auditoriaLogger.logEventoAuditoriaDeposito(Constants.INICIO, request, Constants.DEPOSITO)
                .then(coreBancarioSofka.obtenerSaldoCuentaDeposito(request, token))
                .flatMap(response -> {
                    if (response.getCode() != 200) {
                        return auditoriaLogger.logEventoAuditoriaDeposito(Constants.ERROR, request, Constants.DEPOSITO)
                                .then(Mono.just(response));
                    }
                    return auditoriaLogger.logEventoAuditoriaDeposito(Constants.EXITO, request, Constants.DEPOSITO)
                            .then(Mono.just(response));
                });
    }

}
