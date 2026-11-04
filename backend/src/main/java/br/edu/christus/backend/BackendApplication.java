package br.edu.christus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "Documentação API - Tópicos Especiais Dev. Web",
				version = "1.0",
				description = "API construída em Sala",
				contact = @Contact(
						name = "Adriano",
						email = "adriano.candido@unichristus.edu.br",
						url = "https://www.unichristus.edu.br")
		)
)
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
