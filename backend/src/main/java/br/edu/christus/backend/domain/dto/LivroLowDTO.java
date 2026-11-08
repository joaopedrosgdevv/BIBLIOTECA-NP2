
package br.edu.christus.backend.domain.dto;

import lombok.Data;

@Data
public class LivroLowDTO {

    private Long id;
    private String titulo;
    private String isbn;
    private Long categoriaId;

}
