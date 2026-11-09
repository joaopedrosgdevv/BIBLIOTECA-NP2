package br.edu.christus.backend.domain.dto;

import java.time.LocalDate;
import lombok.Data;

// Recebe os dados do material e o ID da categoria que será associada.
@Data
public class MaterialDidaticoDTO {

    private String titulo;
    private String descricao;
    private String tipoArquivo;
    private String urlArquivo;
    private LocalDate dataPublicacao;
    private Long categoriaId;

}
