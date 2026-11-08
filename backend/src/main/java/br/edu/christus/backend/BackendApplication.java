package br.edu.christus.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "Biblioteca Pública Virtual - API REST",
				version = "1.0",
				description = "API REST desenvolvida para a NP2 da disciplina "
						+ "Tópicos Especiais em Desenvolvimento Web. "
						+ "Sistema de biblioteca virtual para uma escola de ensino médio, "
						+ "com gerenciamento de livros, autores, categorias, editoras "
						+ "e materiais didáticos, incluindo busca de materiais por título.",
				contact = @Contact(
						name = "joão pedro e itiel",
						email = "marcositielgomes@gmail.com",

						url = "https://www.unichristus.edu.br")
		)
)
@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
