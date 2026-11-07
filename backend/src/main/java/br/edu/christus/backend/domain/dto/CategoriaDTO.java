package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class CategoriaDTO {

    private String nome;
    private String descricao;
    private String codigo;
    private String areaConhecimento;
    private Boolean ativo;

}