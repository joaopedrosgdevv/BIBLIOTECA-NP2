package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class EditoraDTO {

    private String nome;
    private String cnpj;
    private String paisOrigem;
    private Integer anoFundacao;
    private String site;
    private String descricao;

}
