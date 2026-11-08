package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class EditoraLowDTO {

    private Long id;
    private String nome;
    private String cnpj;
    private String paisOrigem;

}
