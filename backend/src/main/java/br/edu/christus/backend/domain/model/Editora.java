package br.edu.christus.backend.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tb_editora")
@Getter
@Setter
@NoArgsConstructor
public class Editora {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, length = 20)
    private String cnpj;

    @Column(nullable = false, length = 100)
    private String paisOrigem;

    private Integer anoFundacao;

    @Column(length = 255)
    private String site;

    @Column(length = 1000)
    private String descricao;
}
