package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class CategoriaLowDTO {

    private Long id;
    private String nome;
    private String codigo;
    private String areaConhecimento;

}