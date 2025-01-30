package com.banco.operaciones_bancarias.component;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.banco.operaciones_bancarias.dto.request.DepositoCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.request.RetiroCuentaRequestDTO;
import com.banco.operaciones_bancarias.dto.response.ResponseDTO;

import reactor.core.publisher.Mono;

@Component
public class CoreBancarioSofka {

    private final WebClient webClient;

    public CoreBancarioSofka(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8080").build();
    }
    public Mono<ResponseDTO<?>> retiroCuenta(RetiroCuentaRequestDTO request, String token) {
        return webClient.post()
                .uri("/api/v1/cuenta-bancaria/retiro-cuenta")
                .bodyValue(request)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(ResponseDTO.class)
                        .flatMap(errorBody -> Mono.just(new RuntimeException(errorBody.getMessage()))))
                .bodyToMono(new ParameterizedTypeReference<ResponseDTO<?>>() {});
    }

    public Mono<ResponseDTO<?>> depositoCuenta(DepositoCuentaRequestDTO request, String token) {
        return webClient.post()
                .uri("/api/v1/cuenta-bancaria/deposito-cuenta")
                .bodyValue(request)
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                    response -> response.bodyToMono(ResponseDTO.class)
                        .flatMap(errorBody -> Mono.just(new RuntimeException(errorBody.getMessage()))))
                .bodyToMono(new ParameterizedTypeReference<ResponseDTO<?>>() {});
    }

    public Mono<ResponseDTO<?>> saldoCuenta(int numeroCuenta, String token) {
    return webClient.get()
            .uri("/api/v1/cuenta-bancaria/mostrar-saldo-actual/{numeroCuenta}", numeroCuenta)
            .headers(headers -> headers.setBearerAuth(token))
            .retrieve()
            .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(),
                response -> response.bodyToMono(String.class)
                    .flatMap(errorBody -> {
                        return Mono.error(new RuntimeException("Error en la solicitud: " + errorBody));
                    }))
            .bodyToMono(new ParameterizedTypeReference<ResponseDTO<?>>() {})
            .onErrorResume(error -> {
                return Mono.error(new RuntimeException("Error al obtener el saldo: " + error.getMessage()));
            });
}
}
