package com.banco.operaciones_bancarias;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Operaciones Bancarias API", version = "1.0.0", description = "API para operaciones bancarias"))
public class OperacionesBancariasApplication {

	public static void main(String[] args) {
		SpringApplication.run(OperacionesBancariasApplication.class, args);
	}

}
