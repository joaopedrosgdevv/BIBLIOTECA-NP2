
package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class LivroDTO {

    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidadeDisponivel;
    private Long categoriaId;

}
