
package br.edu.christus.backend.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LivroDTO {

    private String titulo;
    private String isbn;
    private Integer anoPublicacao;
    private Integer quantidadeDisponivel;
    private Long categoriaId;
    private Long editoraId;

    // IDs dos autores associados ao livro
    private Set<Long> autoresIds;
}
