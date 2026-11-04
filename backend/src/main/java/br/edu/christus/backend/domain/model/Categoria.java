
package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tb_categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    @Column(length = 500)
    private String descricao;

    @Column(length = 50, nullable = false, unique = true)
    private String codigo;

    @Column(length = 100)
    private String areaConhecimento;

    @Column(nullable = false)
    private Boolean ativo;
}
