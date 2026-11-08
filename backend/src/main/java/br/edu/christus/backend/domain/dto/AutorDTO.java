
package br.edu.christus.backend.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutorDTO {

    private String nome;
    private String nacionalidade;
    private Integer anoNascimento;
    private String biografia;
    private Boolean ativo;
}
