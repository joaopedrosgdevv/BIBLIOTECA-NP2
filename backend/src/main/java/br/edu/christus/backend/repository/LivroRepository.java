package br.edu.christus.backend.repository;

import br.edu.christus.backend.domain.model.Livro;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    // VERIFICAR SE O ISBN JÁ EXISTE
    boolean existsByIsbn(String isbn);

    // VERIFICAR ISBN DUPLICADO NA ATUALIZAÇÃO
    boolean existsByIsbnAndIdNot(String isbn, Long id);

    // VERIFICAR SE UM AUTOR ESTÁ ASSOCIADO A ALGUM LIVRO
    boolean existsByAutores_Id(Long autorId);

    // LISTAR LIVROS CARREGANDO CATEGORIA E AUTORES
    @Override
    @EntityGraph(attributePaths = {"categoria", "autores"})
    List<Livro> findAll();

    // BUSCAR LIVRO POR ID CARREGANDO CATEGORIA E AUTORES
    @Override
    @EntityGraph(attributePaths = {"categoria", "autores"})
    Optional<Livro> findById(Long id);
}