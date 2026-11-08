package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class MaterialDidaticoLowDTO {

    private Long id;
    private String titulo;
    private String tipoArquivo;
    private String urlArquivo;
    private Long categoriaId;

}
