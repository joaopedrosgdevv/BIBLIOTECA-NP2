package br.edu.christus.backend.domain.dto;

import java.time.LocalDate;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

// FUNCIONALIDADE EXTRA: resposta da sugestão de estudo.
// Traz uma mensagem amigável e os dados do material sorteado.
@Data
@Schema(description = "Material didático sugerido para estudo")
public class SugestaoEstudoDTO {

    @Schema(description = "Mensagem de sugestão para o aluno", example = "Sugestão de estudo: Apostila Spring Boot")
    private String mensagem;

    @Schema(description = "ID do material sorteado", example = "1")
    private Long materialId;

    @Schema(description = "Título do material", example = "Apostila Spring Boot")
    private String titulo;

    @Schema(description = "Descrição do material", example = "Introdução ao desenvolvimento de APIs REST")
    private String descricao;

    @Schema(description = "Tipo do arquivo", example = "PDF")
    private String tipoArquivo;

    @Schema(description = "Endereço para acessar o material", example = "https://exemplo.com/apostila.pdf")
    private String urlArquivo;

    @Schema(description = "Data de publicação do material", example = "2026-03-15")
    private LocalDate dataPublicacao;

    @Schema(description = "Nome da categoria do material", example = "Programação")
    private String categoria;

}
