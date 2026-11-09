package br.edu.christus.backend.domain.model;

import java.time.LocalDate;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Representa materiais de apoio e armazena o endereço dos arquivos.
@Entity
@Table(name = "tb_material_didatico")
@Getter
@Setter
@NoArgsConstructor
public class MaterialDidatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(length = 1000)
    private String descricao;

    @Column(nullable = false, length = 50)
    private String tipoArquivo;

    @Column(nullable = false, length = 500)
    private String urlArquivo;

    private LocalDate dataPublicacao;

    // Cada material pertence a uma categoria; uma categoria pode ter vários materiais.
    @ManyToOne
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;
}
