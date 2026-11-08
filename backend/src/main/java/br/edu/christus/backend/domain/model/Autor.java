
package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_autor")
@Getter
@Setter
@NoArgsConstructor
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, length = 100)
    private String nacionalidade;

    @Column(nullable = false)
    private Integer anoNascimento;

    @Column(length = 1000)
    private String biografia;

    @Column(nullable = false)
    private Boolean ativo;
}
