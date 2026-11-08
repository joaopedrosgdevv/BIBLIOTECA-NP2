package br.edu.christus.backend.domain.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class MaterialDidaticoDTO {

    private String titulo;
    private String descricao;
    private String tipoArquivo;
    private String urlArquivo;
    private LocalDate dataPublicacao;
    private Long categoriaId;

}
